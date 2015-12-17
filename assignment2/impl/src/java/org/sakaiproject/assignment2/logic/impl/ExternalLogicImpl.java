/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/logic/impl/ExternalLogicImpl.java $
 * $Id: ExternalLogicImpl.java 79823 2012-06-01 19:25:12Z wagnermr@iupui.edu $
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.utils.Assignment2Utils;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.time.api.TimeService;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.util.FormattedText;

/**
 * This is the implementation for logic which is external to our app logic
 */
public class ExternalLogicImpl implements ExternalLogic {

    private static Log log = LogFactory.getLog(ExternalLogicImpl.class);

    /**
     * Encoding method to use when URL encoding
     */
    public static final String URL_ENCODING = "UTF-8";

    private ToolManager toolManager;
    public void setToolManager(ToolManager toolManager) {
        this.toolManager = toolManager;
    }

    private SessionManager sessionManager;
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    private SiteService siteService;
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    private UserDirectoryService userDirectoryService;
    public void setUserDirectoryService(UserDirectoryService userDirectoryService) {
        this.userDirectoryService = userDirectoryService;
    }
    
    private ServerConfigurationService serverConfigurationService;
    public void setServerConfigurationService(ServerConfigurationService serverConfigurationService) {
        this.serverConfigurationService = serverConfigurationService;
    }
    
    private TimeService timeService;
    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }

    /**
     * Place any code that should run when this class is initialized by spring here
     */
    public void init() {
        if (log.isDebugEnabled()) log.debug("init");
    }

    public String getCurrentContextId() {
        if (toolManager != null && toolManager.getCurrentPlacement() != null && toolManager.getCurrentPlacement().getContext() != null){
            return toolManager.getCurrentPlacement().getContext();

        } else {
            return null;
        }
    }
    
    public String getCurrentLocationId() {
        try {
           if (toolManager.getCurrentPlacement() == null)
           {
              return NO_LOCATION;
           }
           Site s = siteService.getSite(toolManager.getCurrentPlacement().getContext());
           return s.getReference(); // get the entity reference to the site
        } catch (IdUnusedException e) {
           return NO_LOCATION;
        }
     };

    public Site getSite(String contextId) {
        Site site = null;
        try {
            site = siteService.getSite(contextId);
        } catch (IdUnusedException iue) {
            log.warn("IdUnusedException attempting to find site with id: " + contextId);
        }

        return site;
    }

    public String getSiteTitle(String contextId) {
        String siteTitle = null;
        Site site = getSite(contextId);
        if (site != null) {
            siteTitle = site.getTitle();
        }

        return siteTitle;
    }

    public String getToolTitle() {
        return toolManager.getTool(ExternalLogic.TOOL_ID_ASSIGNMENT2).getTitle();
    }

    public String getCurrentUserId() {
        return sessionManager.getCurrentSessionUserId();
    }

    public String getUserDisplayName(String userId) {
        try {
            User user = userDirectoryService.getUser(userId);
            return user.getDisplayName();
        } catch (UserNotDefinedException ex) {
            log.error("Could not get user from userId: " + userId, ex);
        }

        return "----------";
    }

    public String cleanupUserStrings(String userSubmittedString, boolean cleanupHtml) {
        // clean up the string
        if (userSubmittedString != null && !"".equals(userSubmittedString)) {
            if (cleanupHtml) {
                userSubmittedString = Assignment2Utils.cleanupHtmlText(userSubmittedString);
            }
            
            userSubmittedString = FormattedText.processFormattedText(userSubmittedString, new StringBuilder(), true, false);
        } 

        return userSubmittedString;
    }

    public String getAssignmentViewUrl(String viewId) {
        return serverConfigurationService.getToolUrl() + Entity.SEPARATOR
        + toolManager.getCurrentPlacement().getId() + Entity.SEPARATOR + viewId;
    }

    public Collection<Group> getSiteGroups(String contextId) {
        try {
            Site s = siteService.getSite(contextId);
            return s.getGroups();
        } catch (IdUnusedException e){
            log.warn("IdUnusedException attempting to find site with id: " + contextId);
            return new ArrayList<Group>();
        }
    }

    public Collection<Group> getUserMemberships(String userId, String contextId) {
        if (userId == null || contextId == null) {
            throw new IllegalArgumentException("Null userId or contextId passed to getUserMemberships");
        }
        try {
            Site s = siteService.getSite(contextId);
            return s.getGroupsWithMember(userId);
        } catch (IdUnusedException e){
            log.error("IdUnusedException attempting to find site with id: " + contextId);
            return new ArrayList<Group>();
        }
    }

    public List<String> getUserMembershipGroupIdList(String userId, String contextId) {
        if (userId == null || contextId == null) {
            throw new IllegalArgumentException("Null userId or contextId passed to getUserMembershipGroupIdList");
        }
        List<Group> memberships = new ArrayList<Group>(getUserMemberships(userId, contextId));
        List<String> groupIds = new ArrayList<String>();
        if (memberships != null) {
            for (Group group : memberships) {
                if (group != null) {
                    groupIds.add(group.getId());
                }
            }
        }

        return groupIds;
    }

    public Map<String, String> getGroupIdToNameMapForSite(String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getGroupIdToNameMapForSite");
        }

        Collection<Group> siteGroups = getSiteGroups(contextId);

        Map<String, String> groupIdToNameMap = new HashMap<String, String>();
        if (siteGroups != null && !siteGroups.isEmpty()) {
            for (Group siteGroup : siteGroups) {
                if (siteGroup != null) {
                    groupIdToNameMap.put(siteGroup.getId(), siteGroup.getTitle());
                }
            }
        }

        return groupIdToNameMap;
    }

    public boolean siteHasTool(String contextId, String toolId) {
        boolean siteHasTool = false;
        try {
            Site currSite = siteService.getSite(contextId);
            if (currSite.getToolForCommonId(toolId) != null) {
                siteHasTool = true;
            }
        } catch (IdUnusedException ide) {
            log.warn("IdUnusedException caught in siteHasTool with contextId: " + contextId + " and toolId: " + toolId);
        }
        return siteHasTool;
    }

    public List<String> getUsersInGroup(String contextId, String groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("null groupId passed to getStudentsInSection");
        }

        List<String> usersInGroup = new ArrayList<String>();
        
        Site site = getSite(contextId);
        if (site == null) {
            log.error("Error retrieving site with contextId:" + contextId);
        } else {
            Group group = site.getGroup(groupId);
            if (group != null) {
                Set<Member> members = group.getMembers();
                if (members != null) {
                    for (Member member : members) {
                        usersInGroup.add(member.getUserId());
                    }
                }
            }
        }

        return usersInGroup;
    }

    public String getUrlForGradebookItemHelper(Long gradeableObjectId, String returnViewId, String contextId) {
        //TODO URL encode this so I can put it as a url parameter
        String url = "/direct/gradebook/_/gradebookItem/" + contextId;
        String finishedURL = getAssignmentViewUrl(returnViewId);
        String getParams = "?TB_iframe=true&width=700&height=415&KeepThis=true&finishURL=" + finishedURL;

        return url + "/" + (gradeableObjectId != null ? gradeableObjectId : "") + getParams;
    }
    
    
    public String getGraderPermissionsUrl(String contextId) {
        StringBuilder url = new StringBuilder();
        
        Site site = getSite(contextId);
        if (site != null) {
            // we need to retrieve the placement of the gradebook tool
            ToolConfiguration gbToolConfig = site.getToolForCommonId(TOOL_ID_GRADEBOOK);
            if (gbToolConfig != null) {
                String gbPlacement = gbToolConfig.getId();
                
                // now build the url
                url.append(serverConfigurationService.getToolUrl());
                url.append(Entity.SEPARATOR);
                url.append(gbPlacement);
                url.append(Entity.SEPARATOR);
                url.append("sakai.gradebook.permissions.helper/graderRules");
            }
        }
        
        return url.toString();
    }

    public String getUrlForGradebookItemHelper(Long gradeableObjectId, String gradebookItemName, String returnViewId, String contextId, Date dueDate) {
        // encode the params
        try {
            gradebookItemName = URLEncoder.encode(gradebookItemName, URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Invalid character encoding specified: " + URL_ENCODING);
        }

        String dueDateTime = "";
        if (dueDate != null) {
            dueDateTime = dueDate.getTime() + "";
        }

        String url = "/direct/gradebook/_/gradebookItem/" + contextId;
        String finishedURL = getAssignmentViewUrl(returnViewId);
        String getParams = "?TB_iframe=true&width=700&height=415&KeepThis=true&finishURL=" + finishedURL + 
        "&name=" + gradebookItemName + "&dueDateTime=" + dueDateTime;

        return url + "/" + (gradeableObjectId != null ? gradeableObjectId : "") + getParams;
    }

    public String getUrlForGradeGradebookItemHelper(Long gradeableObjectId, String userId, String returnViewId, String contextId) {
        // encode the params
        try {
            userId = URLEncoder.encode(userId, URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Invalid character encoding specified: " + URL_ENCODING);
        }

        String url = "/direct/gradebook/_/gradeGradebookItem/" + contextId +
        "/" + gradeableObjectId + "/" + userId; 
        String finishedURL = getAssignmentViewUrl(returnViewId);
        String getParams = "?TB_iframe=true&width=700&height=380&KeepThis=true&finishURL=" + finishedURL;

        return url + getParams;
    }

    public String getUserSortName(String userId) {
        String userSortName = ", ";
        try {
            User user = userDirectoryService.getUser(userId);
            userSortName = user.getSortName();
        } catch (UserNotDefinedException ex) {
            log.error("Could not get user from userId: " + userId, ex);
        }

        return userSortName;
    }

    public String getUserEmail(String userId) {
        String userEmail = null;

        try {
            User user = userDirectoryService.getUser(userId);
            userEmail =  user.getEmail();
        } catch (UserNotDefinedException ex) {
            log.error("Could not get user from userId: " + userId + "Returning null email address.", ex);
        }

        return userEmail;
    }

    public User getUser(String userId)
    {
        User user = null;

        try {
            user = userDirectoryService.getUser(userId);
        } catch (UserNotDefinedException ex) {
            log.error("Could not get user from userId: " + userId, ex);
        }

        return user;
    }

    public Map<String, User> getUserIdUserMap(List<String> userIds) {
        Map<String, User> userIdUserMap = new HashMap<String, User>();
        if (userIds != null) {
            List<User> userList = new ArrayList<User>();
            userList = userDirectoryService.getUsers(userIds);

            if (userList != null) {
                for (User user : userList) {
                    userIdUserMap.put(user.getId(), user);
                }
            }
        }

        return userIdUserMap;
    }

    public Map<String, String> getUserDisplayIdUserIdMapForUsers(Collection<String> userIds) {
        Map<String, String> userDisplayIdUserIdMap = new HashMap<String, String>();

        if (userIds != null) {
            List<User> userList = new ArrayList<User>();
            userList = userDirectoryService.getUsers(userIds);

            if (userList != null) {
                for (User user : userList) {
                    userDisplayIdUserIdMap.put(user.getDisplayId(), user.getId());
                }
            }
        }

        return userDisplayIdUserIdMap;
    }

    public String getMyWorkspaceSiteId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Null userId passed to getMyWorkspaceSiteId");
        }

        String myWorkspaceId = siteService.getUserSiteId(userId);

        return myWorkspaceId;
    }

    public Map<String, String> getUserIdToSortNameMap(Collection userIds) {      
        Map<String, String> userIdSortNameMap = new HashMap<String, String>();

        if (userIds != null) {
            List<User> users = userDirectoryService.getUsers(userIds);
            if (users != null) {
                for (User user : users) {
                	//TODO Should paren formatting be i18n-ized?
                    userIdSortNameMap.put(user.getId(), user.getSortName() 
                    		+ " (" + user.getEid() + ")");
                }
            }
        }

        return userIdSortNameMap;
    }
    
    public String getServerUrl() {
        return serverConfigurationService.getServerUrl();
    }
    
    public List<Site> getUserSitesWithAssignments(){
    	List<Site> returnList = new ArrayList<Site>();
    	List<Site> userSites = siteService.getSites(SiteService.SelectionType.UPDATE, null, null, 
				null, SiteService.SortType.TITLE_ASC, null);
    	
    	for(Site site : userSites){
    		if(siteHasTool(site.getId(), ExternalLogic.TOOL_ID_OLD_ASSIGN)){
    			returnList.add(site);
    		}    		
    	}
    	
    	return returnList;
    }
    
    public void addToSession(String attribute, Object value) {
        sessionManager.getCurrentSession().setAttribute(attribute, value);
    }
    
    public DateFormat getDateFormat(Integer optionalDateStyle, Integer optionalTimeStyle, Locale locale, boolean currentUserTimezone) {
        int dateStyle = optionalDateStyle != null ? optionalDateStyle : DateFormat.MEDIUM;
        int timeStyle = optionalTimeStyle != null ? optionalTimeStyle : DateFormat.SHORT;
        
        DateFormat df = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        if (currentUserTimezone) {
            df.setTimeZone(timeService.getLocalTimeZone());
        }
        
        return df;
    }
    
    public void setLocalTimeZone(DateFormat df) {
        if (df != null) {
            df.setTimeZone(timeService.getLocalTimeZone());
        }
    }
}