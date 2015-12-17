package org.sakaiproject.assignment2.tool.producers.renderers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.GradeInformation;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.user.api.User;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.producers.BasicProducer;

public class GradeDetailsRenderer implements BasicProducer {
    private static Log log = LogFactory.getLog(GradeDetailsRenderer.class);

    // Dependency
    private User currentUser;
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Dependency
    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    // Dependency
    private ExternalGradebookLogic externalGradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic externalGradebookLogic) {
        this.externalGradebookLogic = externalGradebookLogic;
    }  

    // Dependency
    private String curContext;
    public void setCurContext(String curContext) {
        this.curContext = curContext;
    }
    
    // Dependency
    private AsnnToggleRenderer toggleRenderer;
    public void setAsnnToggleRenderer(AsnnToggleRenderer toggleRenderer) {
        this.toggleRenderer = toggleRenderer;
    }

    /**
     * Renders the grade details at the top of the Student Submission
     * Page(s)
     * 
     * @param parent
     * @param clientID
     * @param assignmentSubmission
     * @param includeToggle
     * @param displayOnlyIfReleased true if you only want this section to display if the
     * grade has been released. If false, will display section but grade and comment information
     * will be N/A if not released
     */
    public void fillComponents(UIContainer parent, String clientID, AssignmentSubmission assignmentSubmission, boolean includeToggle, boolean displayOnlyIfReleased) {
        /***
         * Grade Details including:
         *   - Grade
         *   - Comments
         */

        Assignment2 assignment = assignmentSubmission.getAssignment();

        // double check that assignment is graded and this user is actually a "student" in the gradebook tool
        // and, thus, eligible for gradebook grading
        if (assignment.isGraded() && assignment.getGradebookItemId() != null &&
                externalGradebookLogic.isUserAStudentInGradebook(assignment.getContextId(), assignmentSubmission.getUserId())) {

            // make sure this gradebook item still exists
            GradebookItem gradebookItem;
            try {
                gradebookItem = 
                    externalGradebookLogic.getGradebookItemById(curContext, 
                            assignment.getGradebookItemId());
            } catch (GradebookItemNotFoundException ginfe) {
                if (log.isDebugEnabled()) log.debug("Student attempting to access assignment " + 
                        assignment.getId() + " but associated gb item no longer exists!");
                gradebookItem = null;
            }

            // check to see if displayOnlyIfReleased is true or false and display accordingly
            if (gradebookItem != null && 
                    (!displayOnlyIfReleased || (displayOnlyIfReleased && gradebookItem.isReleased()))) {

                UIJointContainer joint = new UIJointContainer(parent, clientID, "grade-details-widget:");

                // Title
                if (includeToggle) {
                    String hoverText = messageLocator.getMessage("grade.details.toggle.hover");
                    String heading = messageLocator.getMessage("assignment2.grade.details.heading");

                    toggleRenderer.makeToggle(joint, "grade_toggle_section:", null, true, 
                            heading, hoverText, false, false, false, false, null);
                } else {
                    UIMessage.make(joint, "grade-details-header", "assignment2.grade.details.heading");
                }

                UIOutput gradeSection = UIOutput.make(joint, "gradeSection");
                if (includeToggle) {
                    // everything below the toggle is a subsection
                    gradeSection.decorate(new UIFreeAttributeDecorator("class", "toggleSubsection subsection1"));
                    gradeSection.decorate(new UIFreeAttributeDecorator("style", "display: none;"));
                }

                // Details Table
                UIOutput.make(joint, "grade-details-table");

                // Render the graded information if it's available and gb item is released
                String grade = null;
                String comment = null;
                if (gradebookItem != null && gradebookItem.isReleased()) {
                    GradeInformation gradeInfo = externalGradebookLogic.getGradeInformationForStudent(curContext, assignment.getGradebookItemId(), currentUser.getId());
                    if (gradeInfo != null) {
                        grade = gradeInfo.getGradebookGrade();
                        comment = gradeInfo.getGradebookComment();
                    }
                }

                if (grade != null) {
                    UIOutput.make(joint, "grade-row");
                    UIOutput.make(joint, "grade", grade);
                }
                else
                {
                    UIOutput.make(joint, "grade-row");
                    UIMessage.make(joint, "grade", "assignment2.grade.details.grade.none");
                }

                if (comment != null) {
                    UIOutput.make(joint, "comment-row");
                    UIOutput.make(joint, "comment", comment);
                }
                else
                {
                    UIOutput.make(joint, "comment-row");
                    UIMessage.make(joint, "comment", "assignment2.grade.details.comments.none");
                }
            }
        }
    }

    public void fillComponents(UIContainer parent, String clientID) {
        // TODO Auto-generated method stub

    }
}