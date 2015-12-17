package org.sakaiproject.assignment2.tool.producers.renderers;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.HtmlDiffUtil;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.producers.BasicProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * Renders a single submission version from a Student. You'll see this on the
 * Student View Assn Details page, or as part of an aggregated drop down when
 * there are multiple submissions.  It includes the title, student name, 
 * submission date, and then the text/attachments of the submission.
 * 
 * If there is feedback from the Instructor for this version, that will be 
 * rendered if the feedback is released or the current user has grading privileges
 * for this student and assignment
 * 
 * 
 * 
 * @author sgithens
 *
 */
public class AsnnSubmissionVersionRenderer implements BasicProducer {
    private static Log log = LogFactory.getLog(AsnnSubmissionVersionRenderer.class);

    // Dependency
    private ViewParameters viewParameters;
    public void setViewParameters(ViewParameters viewParameters) {
        this.viewParameters = viewParameters;
    }

    // Dependency
    private AttachmentListRenderer attachmentListRenderer;
    public void setAttachmentListRenderer (AttachmentListRenderer attachmentListRenderer) {
        this.attachmentListRenderer = attachmentListRenderer;
    }
    
    private ExternalContentReviewLogic contentReviewLogic;
    public void setExternalContentReviewLogic(ExternalContentReviewLogic contentReviewLogic) {
        this.contentReviewLogic = contentReviewLogic;
    }
    
    private AssignmentPermissionLogic permissionLogic;
    public void setAssignmentPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }
    
    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }
    
    private AsnnToggleRenderer toggleRenderer;
    public void setAsnnToggleRenderer(AsnnToggleRenderer toggleRenderer) {
        this.toggleRenderer = toggleRenderer;
    }

    /**
     * Renders the Submission Version in the parent container in element with 
     * the client id. Returns the new UIContainer that holds this rendered
     * version.
     * 
     * @param parent
     * @param clientID
     * @param asnnSubVersion
     * @param multipleVersionDisplay true if this version is being displayed in a multi-version scenario.
     * this will prevent unnecessary repeated headers
     * @param hideFeedback true if you want to hide feedback for all users unless the user is allowed to manage the submission.  (hides annotated text, feedback notes, and feedback attachments)
     * @param optionalParams optional extra information that might be useful for rendering the assignment info.
     * ie, you may need extended privileges for viewing the attachments so you could pass that info here
     * @return
     */
    public UIContainer fillComponents(UIContainer parent, String clientID, AssignmentSubmissionVersion asnnSubVersion, boolean multipleVersionDisplay, boolean hideFeedback, Map<String, Object> optionalParams) {
        UIJointContainer joint = new UIJointContainer(parent, clientID, "asnn2-submission-version-widget:");

        AssignmentSubmission assignmentSubmssion = asnnSubVersion.getAssignmentSubmission();
        Assignment2 assignment = assignmentSubmssion.getAssignment();
        int submissionType = assignment.getSubmissionType();
        boolean userCanGrade = permissionLogic.isUserAllowedToManageSubmission(null, assignmentSubmssion.getUserId(), assignment);
        
        UIOutput feedbackSection = UIOutput.make(joint, "feedbackSection");
        
        // If this is a single version display and feedback is released, we add
        // an Assignment Feedback header
        if (!multipleVersionDisplay) {
            if (asnnSubVersion.isFeedbackReleased()) {
                String hoverText = messageLocator.getMessage("assignment2.student-submission.feedback.toggle.hover");
                String heading = messageLocator.getMessage("assignment2.student-submission.feedback.toggle.header");

                toggleRenderer.makeToggle(joint, "feedback_toggle:", null, true, 
                        heading, hoverText, true, false, false, false, null);
                
                // everything below the toggle is a subsection
                feedbackSection.decorate(new UIFreeAttributeDecorator("class", "toggleSubsection subsection1"));
            }
        }

        //TODO FIXME time and date of submission here

        /*
         * Render the Students Submission Materials
         * These are not displayed if the version number is 0 - this indicates
         * feedback without a submission
         */
        if (asnnSubVersion.getSubmittedVersionNumber() != 0) {
            if (submissionType == AssignmentConstants.SUBMIT_ATTACH_ONLY || submissionType == AssignmentConstants.SUBMIT_INLINE_AND_ATTACH) {
                if (asnnSubVersion.getSubmissionAttachSet() != null && !asnnSubVersion.getSubmissionAttachSet().isEmpty()){
                    if (assignment.isContentReviewEnabled() && contentReviewLogic.isContentReviewAvailable(assignment.getContextId())) {
                        contentReviewLogic.populateReviewProperties(assignment, asnnSubVersion.getSubmissionAttachSet(), false, asnnSubVersion.getAssignmentSubmission().getUserId());
                    }
                    UIMessage.make(joint, "submission-attachments-header", "assignment2.student-submit.submitted_attachments");
                    attachmentListRenderer.makeAttachmentFromSubmissionAttachmentSet(joint, "submission-attachment-list:", viewParameters.viewID, 
                            asnnSubVersion.getSubmissionAttachSet(), optionalParams);
                }
            }

            if (submissionType == AssignmentConstants.SUBMIT_INLINE_AND_ATTACH || 
                    submissionType == AssignmentConstants.SUBMIT_INLINE_ONLY) {
                // if feedback is released or user has grading privileges for this student, we display the submitted text with
                // instructor annotations
                
                if (userCanGrade || (asnnSubVersion.isFeedbackReleased() && !hideFeedback)) {
                    if (asnnSubVersion.getAnnotatedText() != null && asnnSubVersion.getAnnotatedText().trim().length() > 0) {
                        UIMessage.make(joint, "submission-text-header", "assignment2.student-submit.submission_text.annotated");
                        HtmlDiffUtil differ = new HtmlDiffUtil();
                        UIVerbatim.make(joint, "submission-text", differ.diffHtml(asnnSubVersion.getSubmittedText(), asnnSubVersion.getAnnotatedText()));
                    }
                } else {
                    if (asnnSubVersion.getSubmittedText() != null && asnnSubVersion.getSubmittedText().trim().length() > 0) {
                        UIMessage.make(joint, "submission-text-header", "assignment2.student-submit.submission_text.submitted");
                        UIVerbatim.make(joint, "submission-text", asnnSubVersion.getSubmittedText());
                    }
                }
            }
        }

        /* 
         * Render the Instructor's Feedback Materials if feedback is released
         * or current user has grading privileges
         */
        if (userCanGrade || (asnnSubVersion.isFeedbackReleased() && !hideFeedback)) {
            UIMessage.make(joint, "feedback-header", "assignment2.student-submission.feedback.header");
            String feedbackText = asnnSubVersion.getFeedbackNotes();

            if (feedbackText != null && feedbackText.trim().length() > 0) {
                UIVerbatim.make(joint, "feedback-text", asnnSubVersion.getFeedbackNotes());
            } else {
                UIMessage.make(joint, "feedback-text", "assignment2.student-submission.feedback.none");
            }

            if (asnnSubVersion.getFeedbackAttachSet() != null && 
                    asnnSubVersion.getFeedbackAttachSet().size() > 0) {
                UIMessage.make(joint, "feedback-attachments-header", "assignment2.student-submission.feedback.materials.header");
                attachmentListRenderer.makeAttachmentFromFeedbackAttachmentSet(joint, 
                        "feedback-attachment-list:", viewParameters.viewID, 
                        asnnSubVersion.getFeedbackAttachSet());
            }
        }

        return joint;
    }

    public void fillComponents(UIContainer parent, String clientID) {

    }

}
