package org.sakaiproject.assignment2.tool.producers.renderers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.tool.DisplayUtil;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.producers.BasicProducer;

/**
 * This renders the aggregated view of all the submissions from a student for
 * a particular assignment. It does this by using collapsable drop downs for 
 * each version. For each one of those it uses the 
 * {@link AsnnSubmissionVersionRenderer} to render that drop downs content.
 * 
 * This also needs to show some nifty notifications on the title bars for
 * each version if that version has any feedback that has not been read. Also,
 * if any of the aggregated versions have new feedback, an overall notification
 * should be present somewhere at the top of the widget.
 * 
 * Currently this renderer is marking the submissions as viewed, so only use it
 * for a student who is actually viewing a submission.
 * 
 * @author sgithens
 *
 */
public class AsnnSubmissionHistoryRenderer implements BasicProducer {

    // Dependency
    private AsnnSubmissionVersionRenderer asnnSubmissionVersionRenderer;
    public void setAsnnSubmissionVersionRenderer(AsnnSubmissionVersionRenderer asnnSubmissionVersionRenderer) {
        this.asnnSubmissionVersionRenderer = asnnSubmissionVersionRenderer;
    }

    // Dependency
    private AssignmentSubmissionLogic submissionLogic;
    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }

    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }
    
    private DisplayUtil displayUtil;
    public void setDisplayUtil(DisplayUtil displayUtil) {
        this.displayUtil = displayUtil;
    }
    
    private AsnnToggleRenderer toggleRenderer;
    public void setAsnnToggleRenderer(AsnnToggleRenderer toggleRenderer) {
        this.toggleRenderer = toggleRenderer;
    }
    
    // Dependency
    private AsnnInstructionsRenderer asnnInstructionsRenderer;
    public void setAsnnInstructionsRenderer(AsnnInstructionsRenderer asnnInstructionsRenderer) {
        this.asnnInstructionsRenderer = asnnInstructionsRenderer;
    }

    /**
     * 
     * @param parent
     * @param clientID
     * @param assignmentSubmission
     * @param expandFeedback true if the most recent version with unread feedback should be expanded
     * and marked as read
     */
    public void fillComponents(UIContainer parent, String clientID, AssignmentSubmission assignmentSubmission, boolean expandFeedback) {       

        Assignment2 assignment = assignmentSubmission.getAssignment();
        List<AssignmentSubmissionVersion> versionHistory = submissionLogic.getVersionHistoryForSubmission(assignmentSubmission);

        if (versionHistory.size() >= 1) {
            // Show the history view if:
            // 1) there is more than one version or 
            // 2) there is one submitted version and submission is still open
            // Do not display history if there is one version and that version is draft.
            // That info will be displayed in the editor/summary view

            // check to see if submission is still open. we only include draft
            // versions in this display if submission is closed and there is more than one version
            boolean submissionOpen = submissionLogic.isSubmissionOpenForStudentForAssignment(assignmentSubmission.getUserId(), assignment.getId());
            boolean includeDraftVersion = !submissionOpen && versionHistory.size() > 1;

            UIJointContainer joint = new UIJointContainer(parent, clientID, "asnn2-submission-history-widget:");
            UIOutput.make(joint, "submissions-header");
            UIOutput.make(joint, "multiple-submissions");
            
            Long versionIdToExpand = null;
            if (expandFeedback) {
                versionIdToExpand = determineVersionToExpand(versionHistory);
            }

            for (AssignmentSubmissionVersion version: versionHistory) {
                // do not include draft versions in this history display unless
                // submission is closed and there were multiple submissions
                if (!version.isDraft() || includeDraftVersion) {
                    UIBranchContainer versionDiv = UIBranchContainer.make(joint, "submission-version:");
                    
                    // the feedback indicator will change depending upon whether or not this version is expanded
                    // and marked as read
                    boolean expand = false;
                    boolean showFeedbackAsRead = false;
                    if (versionIdToExpand != null && versionIdToExpand.equals(version.getId())) {
                        // expand this version
                        expand = true;
                        // if this version has released, unread feedback, mark it as viewed
                        if (version.isFeedbackReleased() && !version.isFeedbackRead()) {
                            showFeedbackAsRead = true;
                            // mark this feedback version as read
                            List<Long> versionIdList = new ArrayList<Long>();
                            versionIdList.add(version.getId());
                            submissionLogic.markFeedbackAsViewed(assignmentSubmission.getId(), versionIdList);
                        }
                    }
                    
                    makeVersionToggle(versionDiv, version, assignment.getDueDate(), 
                            assignmentSubmission.getResubmitCloseDate(), expand, assignmentSubmission.getId(), version.getId(), showFeedbackAsRead);

                    asnnSubmissionVersionRenderer.fillComponents(versionDiv, "submission-entry:", version, true, false, null);
                    
                    asnnInstructionsRenderer.makeInstructions(versionDiv, "assignment-instructions-toggle:", assignment, true, false, false);
                }
            }
        }
    }
    
    /**
     * creates the toggle for displaying the submission history
     * @param versionContainer
     * @param version
     * @param assignDueDate
     * @param submissionDueDate {@link AssignmentSubmission#getResubmitCloseDate()} for this version
     * @param expand true if this toggle should be expanded
     * @param submissionId
     * @param versionId
     * @param showFeedbackAsRead true if you want this version's fb to show up as read, even if the
     * parameters on the version don't agree. this may be because we are expanding this version and
     * marking it as read as it displays
     */
    private void makeVersionToggle(UIBranchContainer versionContainer, AssignmentSubmissionVersion version, 
            Date assignDueDate, Date submissionDueDate, boolean expand, Long submissionId, Long versionId, boolean showFeedbackAsRead) {
        String toggleHoverText = messageLocator.getMessage("assignment2.version.toggle.hover");
        
        // figure out the status so we can determine what the heading should be
        int status = submissionLogic.getSubmissionStatusForVersion(version, assignDueDate, submissionDueDate);
        String headerText;
        if (version.getSubmittedVersionNumber() == AssignmentSubmissionVersion.FEEDBACK_ONLY_VERSION_NUMBER) {
            headerText = messageLocator.getMessage("assignment2.version.toggle.status.feedback_only_version");
        } else {
            headerText = displayUtil.getVersionStatusText(status, version.getStudentSaveDate(), version.getSubmittedDate());
        }
        
        // figure out which indicator we need to display on the toggle
        boolean displayFeedbackRead = false;
        boolean displayFeedbackUnread = false;
        
        if (showFeedbackAsRead) {
            displayFeedbackRead = true;
            displayFeedbackUnread = false;
        } else {
            boolean feedbackExists = version.isFeedbackReleased();
            boolean feedbackRead = version.isFeedbackRead();
            
            if (feedbackExists && feedbackRead) {
                displayFeedbackRead = true;
                displayFeedbackUnread = false;
            } else if (feedbackExists && !feedbackRead) {
                displayFeedbackRead = false;
                displayFeedbackUnread = true;
            } else {
                displayFeedbackRead = false;
                displayFeedbackUnread = false;
            }
        }
        
        // if we have unread feedback, add an onclick event to the toggle
        String onclickEvent = null;
        if (displayFeedbackUnread) {
            String fbReadText = messageLocator.getMessage("assignment2.toggle.indicator.feedback.read");
            
            StringBuilder sb = new StringBuilder();
            sb.append("asnn2.readFeedback(this,");
            sb.append(submissionId + ",");
            sb.append(versionId + ",");
            sb.append("'" + fbReadText + "'");
            sb.append(");");
            onclickEvent = sb.toString();
        }
        
        
        toggleRenderer.makeToggle(versionContainer, "version_toggle:", null, true, headerText, toggleHoverText, 
                expand, false, displayFeedbackRead, displayFeedbackUnread, onclickEvent);
        
        UIOutput versionDetails = UIOutput.make(versionContainer, "versionInformation");
        if (!expand) {
            versionDetails.decorate(new UIFreeAttributeDecorator("style", "display:none;"));
        }
    }
    
    /**
     * 
     * @param versionHistory
     * @return the id of the most recent version with unread released feedback
     */
    private Long determineVersionToExpand(List<AssignmentSubmissionVersion> versionHistory) {
        Long versionIdToExpand = null;
        if (versionHistory != null && !versionHistory.isEmpty()) {
            // the versions should be in submitted descending order, so pick the first one
            for (AssignmentSubmissionVersion version : versionHistory) {
                if (version.isFeedbackReleased() && !version.isFeedbackRead()) {
                    versionIdToExpand = version.getId();
                    break;
                }
            }
            
            // if versionIdToExpand is still null, expand the most recent version
            if (versionIdToExpand == null) {
                versionIdToExpand = versionHistory.get(0).getId();
            }
        }
        
        return versionIdToExpand;
    }

    public void fillComponents(UIContainer parent, String clientID) {

    }

}
