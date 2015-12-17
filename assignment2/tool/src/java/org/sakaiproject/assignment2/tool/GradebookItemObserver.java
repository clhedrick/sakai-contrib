package org.sakaiproject.assignment2.tool;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.event.api.EventTrackingService;
import org.sakaiproject.event.api.Event;

/**
 * This observer watches for gradebook updates.
 * 
 * Following are some implementation notes about the Gradebook updates.  
 * Unfortunately these events are triggered from the tool layer, so we need to
 * watch for separate events from Gradebook 1 and Gradebook 2, as well as parse
 * their references differently.
 * 
 * The events for Gradebook 1 look as follows:
 * 
 * Event: gradebook.updateAssignment
 * Ref: /gradebook/{contextId}/{gradebookItemTitle}/{totalPoints}/-1/false/false/instructor
 * 
 * example: /gradebook/6585e5d3-3b81-424b-8c6c-5ddf031333c1/Many-to-one test 234/100.0/-1/false/false/instructor
 * 
 * The events for Gradebook 2 look as follows:
 * 
 * Event: gradebook2.updateItem
 * Ref: /gradebook/{GRADEBOOK_ID}/{GRADABLE_OBJECT_ID}
 * 
 * example: /gradebook/4/6
 * 4 is the Gradebook (usually belonging to a particular site)
 * 6 is the unique ID of this Gradebook Item. (Gradeable Object)
 * 
 * 
 * @author sgithens
 *
 */
public class GradebookItemObserver implements Observer {
    
    public static final String GB1_UPDATE_EVENT = "gradebook.updateAssignment";
    public static final String GB2_UPDATE_EVENT = "gradebook2.updateItem";
    
    private EventTrackingService eventTrackingService;
    public void setEventTrackingService(
            EventTrackingService eventTrackingService) {
        this.eventTrackingService = eventTrackingService;
    }
    
    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }
    
    private ExternalGradebookLogic gradebookLogic;
    public void setGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }
    
    // Move to an external API
    private ServerConfigurationService serverConfigurationService; 
    public void setServerConfigurationService (ServerConfigurationService serverConfigurationService) {
            this.serverConfigurationService = serverConfigurationService;
    }
        
    public void init() {
        if (serverConfigurationService.getBoolean(AssignmentConstants.CONFIG_ALLOW_GB_SYNC, true)) {
            eventTrackingService.addLocalObserver(this);
        }
    }
    
    public void destroy() {
        if (serverConfigurationService.getBoolean(AssignmentConstants.CONFIG_ALLOW_GB_SYNC, true)) {
            eventTrackingService.deleteObserver(this);
        }
    }
    
    public void updateAsnn2(String contextId, Long gradebookItemId) {
        List<Assignment2> linkedAsnns = 
            assignmentLogic.getAssignmentsWithLinkedGradebookItemId(gradebookItemId);
        if (linkedAsnns.size() == 1) {
            Assignment2 asnn = assignmentLogic.getAssignmentByIdWithAssociatedData(linkedAsnns.get(0).getId());
            GradebookItem gbItem = gradebookLogic.getGradebookItemById(contextId, gradebookItemId);
            asnn.setTitle(gbItem.getTitle());
            // Yes, these date.getter methods are deprecated.  But we need to
            // check the Year,Month, and Date to work around SAK-13020. TODO FIXME
            if (gbItem.getDueDate() != null &&
                  (gbItem.getDueDate().getYear() != asnn.getDueDate().getYear() ||
                  gbItem.getDueDate().getMonth() != asnn.getDueDate().getMonth() ||
                  gbItem.getDueDate().getDay() != asnn.getDueDate().getDay())) {
                asnn.setDueDate(gbItem.getDueDate());
                if (asnn.getAcceptUntilDate() != null && 
                        gbItem.getDueDate().after(asnn.getAcceptUntilDate())) {
                    asnn.setAcceptUntilDate(gbItem.getDueDate());
                }
                if (asnn.getOpenDate() != null && 
                        gbItem.getDueDate().before(asnn.getOpenDate())) {
                    asnn.setOpenDate(new Date(gbItem.getDueDate().getTime()-(7*24*60*60*1000)));
                }
            }
            assignmentLogic.saveAssignment(asnn);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Event e = (Event) arg;
        if (GB1_UPDATE_EVENT.equals(e.getEvent())) {
            String[] parts = e.getResource().split("/");
            String gradebookTitle = parts[3];
            List<GradebookItem> gbItems = gradebookLogic.getViewableGradebookItems(e.getContext());
            for(GradebookItem item: gbItems) {
                if (gradebookTitle.equals(item.getTitle())) {
                    updateAsnn2(e.getContext(), item.getGradebookItemId());
                    break;
                }
            }
        }
        else if (GB2_UPDATE_EVENT.equals(e.getEvent())) {
            String[] parts = e.getResource().split("/");
            if (parts.length >= 4) {
                updateAsnn2(e.getContext(), new Long(parts[3]));
            }
        }
        else {
            // We ignore all other events.
        }        
    }
    
}
