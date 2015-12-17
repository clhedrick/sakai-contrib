/**********************************************************************************
 * $URL:https://source.sakaiproject.org/contrib/assignment2/trunk/impl/logic/src/java/org/sakaiproject/assignment2/logic/impl/ExternalCalendarLogicImpl.java $
 * $Id:ExternalCalendarLogicImpl.java 48274 2008-04-23 20:07:00Z wagnermr@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.assignment2.logic.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.CalendarPermissionException;
import org.sakaiproject.assignment2.logic.ExternalCalendarLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.calendar.api.Calendar;
import org.sakaiproject.calendar.api.CalendarEvent;
import org.sakaiproject.calendar.api.CalendarEventEdit;
import org.sakaiproject.calendar.api.CalendarService;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.entity.cover.EntityManager;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.InUseException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.time.api.TimeRange;
import org.sakaiproject.time.cover.TimeService;

/**
 * This is the implementation for logic to interact with the Sakai
 * Schedule (aka Calendar) tool
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class ExternalCalendarLogicImpl implements ExternalCalendarLogic {

    private static Log log = LogFactory.getLog(ExternalCalendarLogic.class);

    // unfortunately, the following is not externally available from the Sched tool....
    private static final String DEADLINE_EVENT_TYPE = "Deadline"; 

    private CalendarService calService;
    public void setCalendarService(CalendarService calService) {
        this.calService = calService;
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public void init() {
        if (log.isDebugEnabled()) log.debug("init");
    }

    public String addDueDateToSchedule(Collection<String> restrictedGroupIds, String contextId,
            String eventTitle, String eventDescription, Date dueDate, Long assignmentId) 
    throws CalendarPermissionException {
        if (contextId == null) {
            throw new IllegalArgumentException("null contextId passed to addOpenDateAnnouncement");
        }

        if (assignmentId == null) {
            throw new IllegalArgumentException("null assignmentId passed to addOpenDateAnnouncement");
        }

        if (dueDate == null || eventDescription == null || eventTitle == null) {
            throw new IllegalArgumentException("null eventText or dueDate or eventDescription " +
                    "passed to addDueDateToSchedule. dueDate:" + dueDate + " eventDescription:" + 
                    eventDescription + " eventTitle:" + eventTitle);
        }

        Calendar calendar = getCalendar(contextId);
        if (calendar == null) {
            log.warn("calendar was null when trying to add event so no event added");
            return null;
        }

        String eventId = null;

        try
        {
            CalendarEvent event = null;

            // determine any group restrictions for this event
            CalendarEvent.EventAccess eAccess = CalendarEvent.EventAccess.SITE;	
            List<Group> groupRestrictions = getGroupRestrictions(restrictedGroupIds, contextId);
            if (groupRestrictions != null && !groupRestrictions.isEmpty()) {
                eAccess = CalendarEvent.EventAccess.GROUPED;
            }

            // convert the due date to a TimeRange
            TimeRange timeRange = TimeService.newTimeRange(dueDate.getTime(), 0);

            // add event to calendar
            event = calendar.addEvent(timeRange,
                    eventTitle,
                    eventDescription,
                    DEADLINE_EVENT_TYPE,
                    "",
                    eAccess,
                    groupRestrictions,
                    EntityManager.newReferenceList());

            eventId = event.getId();

            // now add the linkage to the assignment on the calendar side
            if (event.getId() != null) {
                // add the assignmentId to the calendar object
                try {
                    CalendarEventEdit edit = calendar.getEditEvent(event.getId(), CalendarService.EVENT_ADD_CALENDAR);
                    // TODO - uncomment this when calendar is updated!!
                    //edit.setField(CalendarService.PROP_DISPLAY_EVENT_ON_DATE, dueDate.getTime() + "");
                    // we want this to be a formattedDescription to escape html chars, so we have to edit the object
                    edit.setDescriptionFormatted(eventDescription);

                    calendar.commitEvent(edit);

                } catch (IdUnusedException iue) {
                    log.warn("Error linking newly added event with id: " + event.getId() + 
                            " to assignment: " + assignmentId + ". Assignment link was not added", iue);
                } catch  (InUseException iue) {
                    log.warn("Error linking newly added event with id: " + event.getId() + 
                            " to assignment: " + assignmentId + ". Assignment link was not added", iue);
                }
            }

            if (log.isDebugEnabled()) log.debug("Event added with id: " + event.getId() + " for assignnment: " + assignmentId);

            return eventId;	

        } catch (PermissionException pe) {
            throw new CalendarPermissionException("The current user does not " +
                    "have permission to add an event in context: " + contextId, pe);
        }
    }

    public String updateDueDateEvent(String eventId, Collection<String> restrictedGroupIds, String contextId, 
            String eventTitle, String eventDescription, Date dueDate, Long assignmentId) throws CalendarPermissionException {

        if (eventId == null) {
            log.warn("there was no eventId passed, so event was not updated");
            return null;
        }

        if (contextId == null || assignmentId == null) {
            throw new IllegalArgumentException("null contextId or assignmentId " +
            "passed to updateOpenDateAnnouncement");
        }

        if (eventTitle == null || eventDescription == null || dueDate == null) {
            throw new IllegalArgumentException("null eventTitle or eventDescription or due date " +
            "passed to updateOpenDateAnnouncement");
        }

        String updatedEventId = eventId;

        Calendar calendar = getCalendar(contextId);
        if (calendar == null) {
            log.warn("calendar was null when trying to add event so no event added");
            updatedEventId = null;

        } else {

            try {
                CalendarEventEdit edit = calendar.getEditEvent(eventId, CalendarService.EVENT_MODIFY_CALENDAR);

                edit.setDescriptionFormatted(eventDescription);
                edit.setDisplayName(eventTitle);

                // convert the due date to a TimeRange
                TimeRange timeRange = TimeService.newTimeRange(dueDate.getTime(), 0);
                edit.setRange(timeRange);

                // determine any group restrictions for this event
                List<Group> groupRestrictions = getGroupRestrictions(restrictedGroupIds, contextId);
                edit.setGroupAccess(groupRestrictions, true);

                // TODO - set assignmentId link
                //edit.setField(blah, blah);

                calendar.commitEvent(edit);

                if (log.isDebugEnabled()) log.debug("Event updated with id: " + updatedEventId + " for assignnment: " + assignmentId);

            } catch (PermissionException pe) {
                throw new CalendarPermissionException("The current user does not " +
                        "have permission to update Schedule/Calendar events in context: " + contextId, pe);
            } catch (IdUnusedException iue) {
                // the calendar id stored in the assignment is invalid, so add a new event
                if (log.isDebugEnabled()) log.debug("Bad announcementId associated with assignment, so adding new announcement");
                return addDueDateToSchedule(restrictedGroupIds, contextId, eventTitle, eventDescription, dueDate, assignmentId);
            } catch (InUseException iue) {
                log.error("Event " + eventId + " is locked and cannot be" +
                "updated");
                updatedEventId = null;
            }
        }

        return updatedEventId;
    }

    public void deleteDueDateEvent(String eventId, String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("null announcementId or contextId passed to addOpenDateAnnouncement");
        }

        if (eventId == null) {
            log.warn("there was no announcementId passed, so announcement was not deleted");
            return;
        }

        Calendar calendar = getCalendar(contextId);
        if (calendar == null) {
            log.warn("calendar was null when trying to delete event so no event deleted");
            return;
        }

        try
        {
            CalendarEventEdit edit = calendar.getEditEvent(eventId, CalendarService.EVENT_REMOVE_CALENDAR);
            calendar.removeEvent(edit);

            if (log.isDebugEnabled()) log.debug("Event removed with id: " + eventId);

        } catch (PermissionException pe) {
            throw new CalendarPermissionException("The current user does not " +
                    "have permission to delete events in context: " + contextId, pe);
        } catch (IdUnusedException iue) {
            if (log.isDebugEnabled()) log.debug("Announcement no longer exists so " +
            "cannot be deleted. It was likely deleted via schedule tool.");
        } catch (InUseException iue) {
            log.warn("Event " + eventId + " is locked and cannot be" +
            "removed");
        }
    }

    private Calendar getCalendar(String contextId) {
        Calendar calendar = null;
        String calendarId = ServerConfigurationService.getString("calendar", null);
        if (calendarId == null)
        {
            calendarId = calService.calendarReference(contextId, SiteService.MAIN_CONTAINER);
            try
            {
                calendar = calService.getCalendar(calendarId);
            }
            catch (IdUnusedException e)
            {
                log.warn("No calendar found for site: " + contextId);
                calendar = null;
            }
            catch (PermissionException e)
            {
                throw new CalendarPermissionException("The current user does " +
                        "not have permission to access the calendar for context: " + contextId, e);
            }
            catch (Exception ex)
            {
                log.warn("Unknown exception occurred retrieving calendar for site: " + contextId, ex);
                calendar = null;
            }
        }

        return calendar;
    }

    private List<Group> getGroupRestrictions(Collection<String> restrictedGroupIds, String contextId) {
        List<Group> groupRestrictions = new ArrayList<Group>();

        if (restrictedGroupIds != null && !restrictedGroupIds.isEmpty()) {

            //make a collection of Group objects from the collection of group ref strings
            Site site = externalLogic.getSite(contextId);
            if (site != null) {
                for (String groupId : restrictedGroupIds)
                {
                    if (groupId != null) {
                        Group thisGroup = site.getGroup(groupId);
                        if (thisGroup != null) {
                            groupRestrictions.add(thisGroup);
                        }
                    }
                }
            } else {
                log.warn("There was an error adding the group restrictions to the announcement. Site not found.");
            }
        }

        return groupRestrictions;
    }


}
