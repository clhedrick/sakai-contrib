package org.sakaiproject.assignment2.tool.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.AnnouncementPermissionException;
import org.sakaiproject.assignment2.exception.StaleObjectModificationException;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.model.Assignment2;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

/**
 * Small executor to remove assignments.  Doing this to avoid Assignment2Bean
 * from becoming unwieldy.
 * 
 * @author sgithens
 *
 */
public class RemoveAssignmentCommand {
    private static final Log LOG = LogFactory.getLog(RemoveAssignmentCommand.class);

    // Dependency
    private TargettedMessageList messages;
    public void setMessages(TargettedMessageList messages) {
        this.messages = messages;
    }

    // Dependency
    private AssignmentLogic logic;
    public void setLogic(AssignmentLogic logic) {
        this.logic = logic;
    }

    // Property
    private Long assignmentId;
    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }
    public Long getAssignmentId() {
        return assignmentId;
    }

    public String execute() {
        if (assignmentId != null) {
            try {
                Assignment2 assignment = logic.getAssignmentById(assignmentId);
                logic.deleteAssignment(assignment);
                messages.addMessage( new TargettedMessage("assignment2.assignment_removed",
                        new Object[] {  }, TargettedMessage.SEVERITY_INFO));
            } catch (AnnouncementPermissionException ape) {
                LOG.error(ape.getMessage(), ape);
                // TODO the assign was deleted, but announcement was not
                // b/c user did not have delete perm in annc tool
            } catch (StaleObjectModificationException some) {
                LOG.error(some.getMessage(), some);
                // TODO provide a message to user that someone else was editing
                // this object at the same time
            }
        }

        return "remove";
    }
}
