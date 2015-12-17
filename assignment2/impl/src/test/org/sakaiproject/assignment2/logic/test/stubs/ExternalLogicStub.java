/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/test/org/sakaiproject/assignment2/logic/test/stubs/ExternalLogicStub.java $
 * $Id: ExternalLogicStub.java 79823 2012-06-01 19:25:12Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.logic.test.stubs;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.test.AssignmentTestDataLoad;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.mock.domain.Group;
import org.sakaiproject.mock.domain.Site;
import org.sakaiproject.user.api.User;



/**
 * This is a stub class for testing purposes, it will allow us to test all the classes
 * that depend on it since it has way too many external dependencies to make it worth
 * it to mock them all up<br/>
 *
 * Methods only used by the ui are not implemented
 */
public class ExternalLogicStub implements ExternalLogic {

    private String currentUserUid;

    /**
     * @return the current sakai user id (not username)
     */
    public String getCurrentUserId() {
        //return authn.getUserUid();
        if (currentUserUid == null) {
            currentUserUid = AssignmentTestDataLoad.INSTRUCTOR_UID;
        }
        return currentUserUid;
    }

    public void setCurrentUserId(String currentUserUid) {
        this.currentUserUid = currentUserUid;
    }

    /**
     * Get the display name for a user by their unique id
     * 
     * @param userId
     *            the current sakai user id (not username)
     * @return display name (probably firstname lastname) or "----------" (10 hyphens) if none found
     */
    public String getUserDisplayName(String userId) {
        return "Artichoke, Arty";
    }

    /**
     * @return the current location id of the current user
     */
    public String getCurrentLocationId() {
        return AssignmentTestDataLoad.CONTEXT_ID;
    }

    /**
     * 
     * @return the current context for the current user
     */
    public String getCurrentContextId() {
        return AssignmentTestDataLoad.CONTEXT_ID;
    }

    /**
     * Cleans up the users submitted strings to protect us from XSS
     * 
     * @param userSubmittedString any string from the user which could be dangerous
     * @return a cleaned up string which is now safe
     */
    public String cleanupUserStrings(String userSubmittedString, boolean cleanupHtml) {
        // this just returns it back, so tests for malicious strings won't be caught
        return userSubmittedString;
    }

    /**
     * Returns URL to viewId pass in
     * @param viewId of view to build path to
     * @return a url path to the vie
     */
    public String getAssignmentViewUrl(String viewId) {
        return null; // used for ui
    }

    /**
     * Return a Collection of all Groups
     * @return a collection
     */
    public Collection<Group> getSiteGroups(String contextId) {
        Site currSite = new Site();
        Group group1 = new Group(currSite);
        group1.setId(AssignmentTestDataLoad.GROUP1_NAME);
        group1.setReference(AssignmentTestDataLoad.GROUP1_NAME);
        Group group2 = new Group(currSite);
        group2.setId(AssignmentTestDataLoad.GROUP2_NAME);
        group2.setReference(AssignmentTestDataLoad.GROUP2_NAME);
        Group group3 = new Group(currSite);
        group3.setId(AssignmentTestDataLoad.GROUP3_NAME);
        group3.setReference(AssignmentTestDataLoad.GROUP3_NAME);

        Collection<Group> siteGroups = new ArrayList<Group>();
        siteGroups.add((Group) group1);
        siteGroups.add((Group) group2);
        siteGroups.add((Group) group3);

        return siteGroups;    
    }

    /**
     * 
     * @return a collection of the groups that the given user is a member of
     */
    public Collection<Group> getUserMemberships(String userId, String contextId) {
        Site currSite = new Site();
        Collection<Group> membership = new ArrayList<Group>();
        Group group1 = new Group(currSite);
        group1.setId(AssignmentTestDataLoad.GROUP1_NAME);
        group1.setReference(AssignmentTestDataLoad.GROUP1_NAME);
        Group group2 = new Group(currSite);
        group2.setId(AssignmentTestDataLoad.GROUP2_NAME);
        group2.setReference(AssignmentTestDataLoad.GROUP2_NAME);
        Group group3 = new Group(currSite);
        group3.setId(AssignmentTestDataLoad.GROUP3_NAME);
        group3.setReference(AssignmentTestDataLoad.GROUP3_NAME);

        if (userId.equals(AssignmentTestDataLoad.STUDENT1_UID)) {
            membership.add(group1);
        } else if (userId.equals(AssignmentTestDataLoad.STUDENT2_UID)) {
            membership.add(group3);
        } else if (userId.equals(AssignmentTestDataLoad.STUDENT3_UID)) {
            // not a member of any groups
        } else if (userId.equals(AssignmentTestDataLoad.TA_UID)) {
            membership.add(group1);
        } else {
            // not a member of any groups
        }

        return membership;
    }

    /**
     * 
     * @return list of the group ids of the groups that the given user is
     * a member of
     */
    public List<String> getUserMembershipGroupIdList(String userId, String contextId) {
        List<String> groupIdList = new ArrayList<String>();

        if (userId.equals(AssignmentTestDataLoad.STUDENT1_UID)) {
            groupIdList.add(AssignmentTestDataLoad.GROUP1_NAME);
        } else if (userId.equals(AssignmentTestDataLoad.STUDENT2_UID)) {
            groupIdList.add(AssignmentTestDataLoad.GROUP3_NAME);
        } else if (userId.equals(AssignmentTestDataLoad.STUDENT3_UID)) {
            // not a member of any groups
        } else if (userId.equals(AssignmentTestDataLoad.TA_UID)) {
            groupIdList.add(AssignmentTestDataLoad.GROUP1_NAME);
        } else {
            // not a member of any groups
        }

        return groupIdList;
    }

    /**
     * 
     * @return a map of group id to group name for all of the sections/groups
     * associated with the current site
     */
    public Map<String, String> getGroupIdToNameMapForSite(String contextId) {
        return null; //used for ui
    }

    /**
     * @param contextId
     * @param toolId
     * @return true if tool with the given toolId exists in the site with the given siteId
     */
    public boolean siteHasTool(String contextId, String toolId) {
        return false; //used for ui
    }

    /**
     * 
     * @param contentReference
     * @return String of path for <img> tag for resource image type icon
     */
    public String getContentTypeImagePath(ContentResource contentReference) {
        return null; //used for ui
    }

    /**
     * 
     * @param groupId
     * @return a list of the student ids of students in the Group with the given groupId  
     */
    public List<String> getStudentsInGroup(String groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("null groupId passed to getStudentsInSection");

        }

        List<String> studentsInGroup = new ArrayList<String>();

        if (groupId.equals(AssignmentTestDataLoad.GROUP1_NAME)) {
            studentsInGroup.add(AssignmentTestDataLoad.STUDENT1_UID);
        } else if (groupId.equals(AssignmentTestDataLoad.GROUP2_NAME)) {
            // none
        } else if (groupId.equals(AssignmentTestDataLoad.GROUP3_NAME)) {
            studentsInGroup.add(AssignmentTestDataLoad.STUDENT2_UID);
        }

        return studentsInGroup;
    }

    /**
     * 
     * @param gradeableObjectId
     * @param returnViewId
     * @param contextId
     * @return url to helper
     */
    public String getUrlForGradebookItemHelper(Long gradeableObjectId, String returnViewId, String contextId) {
        return null; //used for ui
    }

    /**
     * 
     * @param gradeableObjectId
     * @param userId
     * @param returnViewId
     * @param contextId
     * @return url to helper
     */
    public String getUrlForGradeGradebookItemHelper(Long gradeableObjectId, String userId, String returnViewId, String contextId) {
        return null; //used for ui
    }

    public String getUserSortName(String userId) {
        return getUserDisplayName(userId);
    }

    public String getToolTitle() {
        return "Assignment2";
    }
    public User getUser(String userId)
    {
        return null;
    }

    public String getReadableFileSize(int sizeVal) {
        return null;
    }

    public Map<String, User> getUserIdUserMap(List<String> userIds)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUrlForGradebookItemHelper(Long gradeableObjectId,
            String gradebookItemName, String returnViewId, String contextId, Date dueDate)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Site getSite(String contextId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUserEmail(String userId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSiteTitle(String contextId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMyWorkspaceSiteId(String userId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<String, String> getUserIdToSortNameMap(Collection userIds)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List<String> getUsersInGroup(String contextId, String groupId)
    {
        
        List<String> allUsers = new ArrayList<String>();
        allUsers.add(AssignmentTestDataLoad.INSTRUCTOR_UID);
        allUsers.add(AssignmentTestDataLoad.TA_UID);
        allUsers.add(AssignmentTestDataLoad.TA_WITH_NO_GROUPS);
        allUsers.add(AssignmentTestDataLoad.STUDENT1_UID);
        allUsers.add(AssignmentTestDataLoad.STUDENT2_UID);
        allUsers.add(AssignmentTestDataLoad.STUDENT3_UID);
        
        List<String> usersInGroup = new ArrayList<String>();
        for (String user : allUsers) {
            List<String> groupMemberships = getUserMembershipGroupIdList(user, AssignmentTestDataLoad.CONTEXT_ID);
            if (groupMemberships != null) {
                for (String userGroupId : groupMemberships) {
                    if (userGroupId.equals(groupId)) {
                        usersInGroup.add(user);
                    }
                }
            }
        }
        
        return usersInGroup;
    }


    public Map<String, String> getUserDisplayIdUserIdMapForUsers(Collection<String> userIds)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String getServerUrl()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List getUserSitesWithAssignments(){
    	return null;
    }


    public String getGraderPermissionsUrl(String contextId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void addToSession(String attribute, Object value)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public DateFormat getDateFormat(Integer optionalDateStyle, Integer optionalTimeStyle, Locale locale, boolean currentUserTimezone) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLocalTimeZone(DateFormat df) {
        // TODO Auto-generated method stub
        
    }

}
