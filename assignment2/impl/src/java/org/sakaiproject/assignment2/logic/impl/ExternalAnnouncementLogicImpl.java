/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/logic/impl/ExternalAnnouncementLogicImpl.java $
 * $Id: ExternalAnnouncementLogicImpl.java 61481 2009-06-29 18:47:43Z swgithen@mtu.edu $
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
import org.sakaiproject.assignment2.logic.ExternalAnnouncementLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.entity.cover.EntityManager;
import org.sakaiproject.event.cover.NotificationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.InUseException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.cover.TimeService;
import org.sakaiproject.announcement.api.AnnouncementChannel;
import org.sakaiproject.announcement.api.AnnouncementMessageEdit;
import org.sakaiproject.announcement.api.AnnouncementMessageHeaderEdit;
import org.sakaiproject.announcement.api.AnnouncementService;
import org.sakaiproject.assignment2.exception.AnnouncementPermissionException;

/**
 * This is the implementation for logic to interact with the Sakai
 * Announcements tool
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class ExternalAnnouncementLogicImpl implements ExternalAnnouncementLogic {

    private static Log log = LogFactory.getLog(ExternalAnnouncementLogic.class);

    public void init() {
        if (log.isDebugEnabled()) log.debug("init");
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    private AnnouncementService anncService;
    public void setAnnouncementService(AnnouncementService anncService) {
        this.anncService = anncService;
    }

    public String addOpenDateAnnouncement(Collection<String> restrictedGroupIds, String contextId,
            String announcementSubject, String announcementBody, Date openDate) throws AnnouncementPermissionException {
        if (contextId == null) {
            throw new IllegalArgumentException("null contextId passed to addOpenDateAnnouncement");
        }

        AnnouncementChannel announcementChannel = getAnnouncementChannel(contextId);
        if (announcementChannel == null) {
            log.warn("announcementChannel was null when trying to add announcement so no annc added");
            return null;
        }

        try
        {
            AnnouncementMessageEdit message = announcementChannel.addAnnouncementMessage();
            AnnouncementMessageHeaderEdit header = message.getAnnouncementHeaderEdit();
            header.setDraft(false);
            header.replaceAttachments(EntityManager.newReferenceList());

            header.setSubject(announcementSubject);
            message.setBody(announcementBody);

            Time releaseDate;
            if (openDate != null) {
                releaseDate = TimeService.newTime(openDate.getTime());
            } else {
                releaseDate = TimeService.newTime();
            }

            // set the release date property
            message.getPropertiesEdit().addProperty(AnnouncementService.RELEASE_DATE, releaseDate.toString());

            if (restrictedGroupIds == null || restrictedGroupIds.isEmpty()) {
                //site announcement
                header.clearGroupAccess();
            } else {
                addGroupRestrictions(restrictedGroupIds, contextId, header);
            }

            announcementChannel.commitMessage(message, NotificationService.NOTI_NONE);
            if (log.isDebugEnabled()) log.debug("announcement added with id: " + message.getId());

            return message.getId();	

        } catch (PermissionException pe) {
            throw new AnnouncementPermissionException("The current user does not " +
                    "have permission to access add announcement in context: " + contextId, pe);
        }
    }

    public String updateOpenDateAnnouncement(String announcementId, Collection<String> restrictedGroupIds, String contextId, 
            String announcementSubject, String announcementBody, Date openDate) throws AnnouncementPermissionException {
        if (contextId == null) {
            throw new IllegalArgumentException("null contextId passed to updateOpenDateAnnouncement");
        }

        if (announcementId == null) {
            log.warn("there was no announcementId passed to update");
            return null;
        }

        AnnouncementChannel announcementChannel = getAnnouncementChannel(contextId);
        if (announcementChannel == null) {
            log.warn("announcementChannel was null when trying to add announcement so no annc added");
            return null;
        }

        try
        {
            AnnouncementMessageEdit message = announcementChannel.editAnnouncementMessage(announcementId);
            AnnouncementMessageHeaderEdit header = message.getAnnouncementHeaderEdit();
            header.setDraft(false);
            header.replaceAttachments(EntityManager.newReferenceList());
            header.setSubject(announcementSubject);
            message.setBody(announcementBody);

            if (restrictedGroupIds == null || restrictedGroupIds.isEmpty()) {
                //site announcement
                header.clearGroupAccess();
            } else {
                addGroupRestrictions(restrictedGroupIds, contextId, header);
            }

            Time releaseDate;
            if (openDate != null) {
                releaseDate = TimeService.newTime(openDate.getTime());
            } else {
                releaseDate = TimeService.newTime();
            }

            // set the release date property
            message.getPropertiesEdit().addProperty(AnnouncementService.RELEASE_DATE, releaseDate.toString());

            announcementChannel.commitMessage(message, NotificationService.NOTI_NONE);
            if (log.isDebugEnabled()) log.debug("Announcement updated with id: " + announcementId);

            return message.getId();	

        } catch (PermissionException pe) {
            throw new AnnouncementPermissionException("The current user does not " +
                    "have permission to access add announcement in context: " + contextId, pe);
        } catch (IdUnusedException iue) {
            // the announcement id stored in the assignment is invalid, so add a new announcement
            if (log.isDebugEnabled()) log.debug("Bad announcementId associated with assignment, so adding new announcement");
            return addOpenDateAnnouncement(restrictedGroupIds, contextId, announcementSubject, announcementBody, openDate);
        } catch (InUseException iue) {
            log.error("Announcement " + announcementId + " is locked and cannot be" +
            "updated");
            return null;
        }
    }

    public void deleteOpenDateAnnouncement(String announcementId, String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("null announcementId or contextId passed to addOpenDateAnnouncement");
        }

        if (announcementId == null) {
            log.warn("there was no announcementId passed, so announcement was not deleted");
            return;
        }

        AnnouncementChannel announcementChannel = getAnnouncementChannel(contextId);
        if (announcementChannel == null) {
            log.warn("announcementChannel was null when trying to delete announcement so no annc deleted");
            return;
        }

        try
        {
            announcementChannel.removeMessage(announcementId);
            if (log.isDebugEnabled()) log.debug("Announcement removed with id: " + announcementId);

        } catch (PermissionException pe) {
            throw new AnnouncementPermissionException("The current user does not " +
                    "have permission to delete announcement in context: " + contextId, pe);
        } catch (Exception e) {
            if (log.isDebugEnabled()) log.debug("Announcement no longer exists so cannot be deleted. It was probably deleted via annc tool.");
            // this is thrown by removeMessage if the annc doesn't exist
        }
    }

    private AnnouncementChannel getAnnouncementChannel(String contextId) {
        AnnouncementChannel announcementChannel = null;
        String channelId = ServerConfigurationService.getString("channel", null);
        if (channelId == null)
        {
            channelId = anncService.channelReference(contextId, SiteService.MAIN_CONTAINER);
            try
            {
                announcementChannel = anncService.getAnnouncementChannel(channelId);
            }
            catch (IdUnusedException e)
            {
                log.warn("No announcement channel found");
                announcementChannel = null;
            }
            catch (PermissionException e)
            {
                throw new AnnouncementPermissionException("The current user does " +
                        "not have permission to access the announcement channel for context: " + contextId, e);
            }
        }

        return announcementChannel;
    }

    private void addGroupRestrictions(Collection<String> restrictedGroupIds, String contextId, AnnouncementMessageHeaderEdit header) {
        if (restrictedGroupIds != null && !restrictedGroupIds.isEmpty()) {

            //make a collection of Group objects from the collection of group ref strings
            Site site = externalLogic.getSite(contextId);
            if (site != null) {
                List<Group> groupRestrictions = new ArrayList<Group>();
                for (String groupId : restrictedGroupIds)
                {
                    if (groupId != null) {
                        Group thisGroup = site.getGroup(groupId);
                        if (thisGroup != null) {
                            groupRestrictions.add(thisGroup);
                        }
                    }
                }

                // set access
                try {
                    header.setGroupAccess(groupRestrictions);
                } catch (PermissionException pe) {
                    log.warn("There was an error adding the group restrictions to the announcement.", pe);
                }

            } else {
                log.warn("There was an error adding the group restrictions to the announcement. Site not found.");
            }
        }
    }

}
