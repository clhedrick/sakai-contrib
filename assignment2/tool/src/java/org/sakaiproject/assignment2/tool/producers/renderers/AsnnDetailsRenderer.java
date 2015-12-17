package org.sakaiproject.assignment2.tool.producers.renderers;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.user.api.User;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.producers.BasicProducer;

public class AsnnDetailsRenderer implements BasicProducer {
    private static Log log = LogFactory.getLog(AsnnDetailsRenderer.class);

    // Dependency
    private User currentUser;
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Dependency
    private Locale locale;
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    // Dependency
    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    // Dependency
    private AssignmentSubmissionLogic submissionLogic;
    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }
    
    // Dependency
    private ExternalContentReviewLogic contentReviewLogic;
    public void setExternalContentReviewLogic(ExternalContentReviewLogic contentReviewLogic) {
        this.contentReviewLogic = contentReviewLogic;
    }
    
    // Dependency
    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }
    
    // Dependency
    private AsnnToggleRenderer toggleRenderer;
    public void setAsnnToggleRenderer(AsnnToggleRenderer toggleRenderer) {
        this.toggleRenderer = toggleRenderer;
    }
    
    // Dependency
    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    /**
     * Renders the assignment details at the top of the Student Submission
     * Page(s)
     * 
     * @param parent
     * @param clientID
     * @param assignmentSubmission
     * @param previewAsStudent
     * @param expandToggle true if this section should be expanded
     * @param excludeDetails If this is true, we only render the Title/Name and
     * due date, but leave off the table with Graded, Submission Status etc.
     */
    public void fillComponents(UIContainer parent, String clientID, AssignmentSubmission assignmentSubmission, boolean previewAsStudent, boolean includeToggle, boolean expandToggle) {
        /***
         * Assignment Details including:
         *   - Graded?
         *   - Points Possible
         *   - Resubmissions Allowed
         *   - Remaining Resubmissions Allowed
         *   - Grade
         *   - Comments
         */

        Assignment2 assignment = assignmentSubmission.getAssignment();
        
        UIJointContainer joint = new UIJointContainer(parent, clientID, "assn2-details-widget:");
        
        // Title
        if (includeToggle) {
            String hoverText = messageLocator.getMessage("assignment2.details.toggle.hover");
            String heading = messageLocator.getMessage("assignment2.details.heading");

            toggleRenderer.makeToggle(joint, "details_toggle_section:", null, true, 
                    heading, hoverText, expandToggle, false, false, false, null);
        } else {
            UIMessage.make(joint, "assignment-details-header", "assignment2.student-submit.details_title");
        }
        
        UIOutput detailsSection = UIOutput.make(joint, "detailsSection");
        if (includeToggle) {
            // everything below the toggle is a subsection
            detailsSection.decorate(new UIFreeAttributeDecorator("class", "toggleSubsection subsection1"));
            if (!expandToggle) {
                detailsSection.decorate(new UIFreeAttributeDecorator("style", "display: none;"));
            }

            // display a different heading for the attachments
            UIMessage.make(joint, "toggle_attach_heading", "assignment2.instructions.attachments");
        }

        // Details Table
        UIOutput.make(joint, "assignment-details-table");

        // use a date which is related to the current users locale
        DateFormat df = externalLogic.getDateFormat(null, null, locale, true);

        UIOutput.make(joint, "open-date", df.format(assignment.getOpenDate()));

     // Grading
        // We include whether this assignment is graded and, if it is,
        // we include points possible if it is graded by points. If the
        // user does not have gradebook privileges, we hide the grading info

        if (assignment.isGraded() && assignment.getGradebookItemId() != null) {
            // make sure this gradebook item still exists
            try {
                GradebookItem gradebookItem = gradebookLogic.getGradebookItemById(assignment.getContextId(), 
                        assignment.getGradebookItemId());
                
                UIMessage.make(joint, "graded", "assignment2.details.graded");
                UIMessage.make(joint, "is-graded", "assignment2.student-submit.yes_graded");
                
                // only display points possible if grade entry by points
                if (gradebookLogic.getGradebookGradeEntryType(assignment.getContextId()) == ExternalGradebookLogic.ENTRY_BY_POINTS) {
                    UIOutput.make(joint, "points-possible-row");

                    String pointsDisplay;
                    if (gradebookItem.getPointsPossible() == null) {
                        pointsDisplay = messageLocator.getMessage("assignment2.details.gradebook.points_possible.none");
                    } else {
                        pointsDisplay = gradebookItem.getPointsPossible().toString();
                    }
                    UIOutput.make(joint, "points-possible", pointsDisplay); 
                }

            } catch (GradebookItemNotFoundException ginfe) {
                if (log.isDebugEnabled()) log.debug("Attempt to access assignment " + 
                        assignment.getId() + " but associated gb item no longer exists!");
            } catch (SecurityException se) {
                // this user doesn't have gb privileges, so hide the gb info
            }
        } else {
            // this assignment is ungraded
            UIMessage.make(joint, "graded", "assignment2.details.graded");
            UIMessage.make(joint, "is-graded", "assignment2.student-submit.no_graded");
        }

        /*
         * Resubmissions Allowed
         * Only display the resubmission information if this assignment requires submission
         */
        if (assignment.isRequiresSubmission()) {
            UIOutput.make(joint, "resubmission-allowed-row");
            boolean resubmissionAllowed = false;
            Integer subLevelNumSubmissions = assignmentSubmission.getNumSubmissionsAllowed();
            if (subLevelNumSubmissions != null) {
                if (subLevelNumSubmissions.equals(AssignmentConstants.UNLIMITED_SUBMISSION) ||
                        subLevelNumSubmissions.intValue() > 1) {
                    resubmissionAllowed = true;
                }
            } else {
                if (assignment.getNumSubmissionsAllowed() > 1 || 
                        assignment.getNumSubmissionsAllowed() == AssignmentConstants.UNLIMITED_SUBMISSION) {
                    resubmissionAllowed = true;
                }
            }

            if (resubmissionAllowed) {
                UIMessage.make(joint, "resubmissions-allowed", "assignment2.student-submit.resubmissions_allowed");
            }
            else {
                UIMessage.make(joint, "resubmissions-allowed", "assignment2.student-submit.resubmissions_not_allowed");
            }

            /*
             * Remaining resubmissions allowed
             */
            if (!previewAsStudent && resubmissionAllowed) {
                UIOutput.make(joint, "remaining-resubmissions-row");
                int numSubmissionsAllowed = submissionLogic.getNumberOfRemainingSubmissionsForStudent(currentUser.getId(), assignment.getId());
                String numAllowedDisplay;
                if (numSubmissionsAllowed == AssignmentConstants.UNLIMITED_SUBMISSION) {
                    numAllowedDisplay = messageLocator.getMessage("assignment2.indefinite_resubmit");
                } else {
                    numAllowedDisplay = "" + numSubmissionsAllowed;
                }
                UIOutput.make(joint, "remaining-resubmissions", numAllowedDisplay);
            }
            
            // only display the originality checking info if it is enabled for this assignment
            if (!previewAsStudent && assignment.isContentReviewEnabled() && contentReviewLogic.isContentReviewAvailable(assignment.getContextId())) { 
                UIOutput.make(joint, "plagiarism-check-row");
                UIMessage.make(joint, "plagiarism-check-enabled", "assignment2.student-submit.plagiarism.enabled");
            }
        }
    }

    public void fillComponents(UIContainer parent, String clientID) {
        // TODO Auto-generated method stub
    }
}