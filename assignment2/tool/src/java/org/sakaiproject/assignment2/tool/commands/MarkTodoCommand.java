package org.sakaiproject.assignment2.tool.commands;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.user.api.User;

/**
 * This bean is being used primarily from the Student Asnn List view where they
 * can use a checkbox to mark an assignment as completed, but of course can be
 * used anywhere else.
 * 
 * You just need to set user, assignmentId, and checkTodo properties, then call
 * markTodo.  
 * 
 * This has a dependency on assignmentSubmissionLogic.
 * 
 * @author sgithens
 *
 */
public class MarkTodoCommand {
    private static Log log = LogFactory.getLog(MarkTodoCommand.class);

    // Dependency
    private AssignmentSubmissionLogic assignmentSubmissionLogic;
    public void setAssignmentSubmissionLogic(
            AssignmentSubmissionLogic assignmentSubmissionLogic) {
        this.assignmentSubmissionLogic = assignmentSubmissionLogic;
    }

    // Property
    private User currentUser;
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Property
    private Long assignmentId;
    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }
    public Long getAssignmentId() {
        return assignmentId;
    }

    // Property 
    private boolean checkTodo;
    public void setCheckTodo(boolean checkTodo) {
        this.checkTodo = checkTodo;
    }
    public boolean isCheckTodo() {
        return checkTodo;
    }

    // Action Method
    public String markTodo() {
        log.warn("Marking Assignment Item: " + assignmentId + " , " + checkTodo);

        Map<Long, Boolean> completed = new HashMap<Long, Boolean>();
        completed.put(assignmentId, new Boolean(checkTodo));
        assignmentSubmissionLogic.markAssignmentsAsCompleted(currentUser.getId(), completed);

        return "marked";
    }
}
