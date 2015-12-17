package org.sakaiproject.assignment2.tool.commands;

import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;

/**
 * A small action bean that can be used to release or retract all feedback 
 * for a given assignment
 * 
 * @author wagnermr
 *
 */
public class ReleaseFeedbackCommand {

    // Dependency
    private AssignmentSubmissionLogic submissionLogic;
    public void setAssignmentSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }

    // Property 
    private Long assignmentId;
    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    // Property
    private boolean releaseFeedback;
    public void setReleaseFeedback(boolean releaseFeedback) {
        this.releaseFeedback = releaseFeedback;
    }

    public void execute() {

        if (assignmentId == null) {
            throw new IllegalArgumentException("Null assignmentId param passed to ReleaseFeedbackCommand");
        }

        submissionLogic.releaseOrRetractFeedback(assignmentId, null, releaseFeedback);
    }
}
