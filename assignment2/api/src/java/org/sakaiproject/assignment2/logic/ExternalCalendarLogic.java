/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/ExternalCalendarLogic.java $
 * $Id: ExternalCalendarLogic.java 61480 2009-06-29 18:39:09Z swgithen@mtu.edu $
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

package org.sakaiproject.assignment2.logic;

import java.util.Collection;
import java.util.Date;


/**
 * This is the interface for logic which is related to the integration
 * with the Schedule (aka Calendar) tool
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public interface ExternalCalendarLogic {

    /**
     * 
     * @param restrictedGroupIds - the ids for any groups that this event is restricted to. 
     * null if not restricted to groups
     * @param contextId 
     * @param eventTitle - the title of the Schedule event
     * @param eventDescription - the description of this event
     * @param dueDate - the date of this event
     * @param assignmentId - the id of the assignment that this event is assoc with
     * @return the id of the newly created event in the Schedule/Calendar tool
     * @throws CalendarPermissionException if the current user is not authorized
     * to add events in the Schedule/Calendar tool
     */
    public String addDueDateToSchedule(Collection<String> restrictedGroupIds, String contextId,
            String eventTitle, String eventDescription, Date dueDate, Long assignmentId);

    /**
     * 
     * @param eventId - the id of the event to update
     * @param restrictedGroupIds - the ids for any groups that this event is restricted to. 
     * null if not restricted to groups
     * @param contextId
     * @param eventTitle - the title of the Schedule event
     * @param eventDescription - the description of this event
     * @param dueDate - the date of this event
     * @param assignmentId - the id of the assignment this event is linked to
     * @return the id of the event
     */
    public String updateDueDateEvent(String eventId, Collection<String> restrictedGroupIds, String contextId, 
            String eventTitle, String eventDescription, Date dueDate, Long assignmentId);

    /**
     * Will remove the given event from the Schedule/Calendar tool
     * @param eventId - the id of the event you wish to remove
     * @param contextId
     */
    public void deleteDueDateEvent(String eventId, String contextId);

}
