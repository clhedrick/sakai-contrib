/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/ExternalAnnouncementLogic.java $
 * $Id: ExternalAnnouncementLogic.java 61480 2009-06-29 18:39:09Z swgithen@mtu.edu $
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

import org.sakaiproject.assignment2.exception.AnnouncementPermissionException;

/**
 * This is the interface for logic which is related to the integration
 * with the Announcements tool
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public interface ExternalAnnouncementLogic {

    /**
     * Add an announcement of the given assignment's open date to the 
     * Announcements tool
     * @param restrictedGroupIds
     * @param contextId
     * @param announcementSubject
     * @param announcementBody
     * @param openDate the date after which this announcement will appear in the Annc tool
			if null, defaults to current date and time
     * @throws AnnouncementPermissionException
     * 		if the current user is not authorized to add an announcement
     * @return the id of the newly created announcement
     */
    public String addOpenDateAnnouncement(Collection<String> restrictedGroupIds, String contextId, 
            String announcementSubject, String announcementBody, Date openDate) throws AnnouncementPermissionException;

    /**
     * Update an announcement for the given assignment. Announcements must be
     * updated when the title, group restrictions, or open date of the assignment changes.
     * @param announcementId
     * @param restrictedGroupIds
     * @param contextId
     * @param announcementSubject
     * @param announcementBody
     * @param openDate the date after which this announcement will appear in the Annc tool
			if null, defaults to current date and time
     * @throws AnnouncementPermissionException
     * 		if the current user is not authorized to update an announcement
     * @return the id of the updated announcement
     */
    public String updateOpenDateAnnouncement(String announcementId, Collection<String> restrictedGroupIds, String contextId,
            String announcementSubject, String announcementBody, Date openDate) 
    throws AnnouncementPermissionException;

    /**
     * Delete an existing announcement associated with the given assignment.
     * @param announcementId
     * @param contextId
     * @throws AnnouncementPermissionException
     * 		if the current user is not authorized to delete an announcement
     */
    public void deleteOpenDateAnnouncement(String announcementId, String contextId) 
    throws AnnouncementPermissionException;
}
