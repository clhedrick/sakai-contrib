/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/branches/ASNN-617/impl/src/java/org/sakaiproject/assignment2/logic/impl/AssignmentBundleLogicImpl.java $
 * $Id: AssignmentBundleLogicImpl.java 61481 2009-06-29 18:47:43Z swgithen@mtu.edu $
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.AssignmentAuthzLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.FunctionManager;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;

/**
 * Used for Assignment2-specific authorization based upon
 * the fine-grained site-scoped Sakai permissions. This is solely used for
 * answering questions based upon these Sakai permissions and is not meant
 * for use outside the logic layer. For more situation-specific
 * permission answers, use the {@link AssignmentPermissionLogic}
 */
public class AssignmentAuthzLogicImpl implements AssignmentAuthzLogic
{
    private static Log log = LogFactory.getLog(AssignmentAuthzLogicImpl.class);
    
    private FunctionManager functionManager;
    private SecurityService securityService;
    private SiteService siteService;
    private AuthzGroupService authzGroupService;
    private ExternalLogic externalLogic;

    public void init() {
        if (log.isDebugEnabled()) log.debug("init");
        
        registerPermissions();
    }
    
    /**
     * Register the assignment2 permissions
     */
    protected void registerPermissions() {
        // register Sakai permissions for this tool
        for (String permission : getAllPermissions()) {
            functionManager.registerFunction(permission);}
    }
    
    public List<String> getAllPermissions() {
        List<String> allPerms = new ArrayList<String>();
        allPerms.add(AssignmentConstants.PERMISSION_VIEW_ASSIGNMENTS);
        allPerms.add(AssignmentConstants.PERMISSION_SUBMIT);
        allPerms.add(AssignmentConstants.PERMISSION_ADD_ASSIGNMENTS);
        allPerms.add(AssignmentConstants.PERMISSION_EDIT_ASSIGNMENTS);
        allPerms.add(AssignmentConstants.PERMISSION_REMOVE_ASSIGNMENTS);
        allPerms.add(AssignmentConstants.PERMISSION_MANAGE_SUBMISSIONS);
        allPerms.add(AssignmentConstants.PERMISSION_ALL_GROUPS);

        return allPerms;
    }
    
    public boolean userHasAddPermission(String userId, String contextId) {
        String permission = AssignmentConstants.PERMISSION_ADD_ASSIGNMENTS;
        if (userId == null) {
            return userHasPermission(contextId, permission);
        } else {
            return userHasPermission(userId, contextId, permission);
        }
    }
    
    public boolean userHasEditPermission(String userId, String contextId) {
        String permission = AssignmentConstants.PERMISSION_EDIT_ASSIGNMENTS;
        if (userId == null) {
            return userHasPermission(contextId, permission);
        } else {
            return userHasPermission(userId, contextId, permission);
        }
    }
    
    public boolean userHasDeletePermission(String userId, String contextId) {
        String permission = AssignmentConstants.PERMISSION_REMOVE_ASSIGNMENTS;
        if (userId == null) {
            return userHasPermission(contextId, permission);
        } else {
            return userHasPermission(userId, contextId, permission);
        }
    }
    
    public boolean userHasAllGroupsPermission(String userId, String contextId) {
        String permission = AssignmentConstants.PERMISSION_ALL_GROUPS;
        if (userId == null) {
            return userHasPermission(contextId, permission);
        } else {
            return userHasPermission(userId, contextId, permission);
        }
    }
    
    public boolean userHasSubmitPermission(String userId, String contextId) {
        String permission = AssignmentConstants.PERMISSION_SUBMIT;
        if (userId == null) {
            return userHasPermission(contextId, permission);
        } else {
            return userHasPermission(userId, contextId, permission);
        }
    }
    
    public boolean userHasManageSubmissionsPermission(String userId, String contextId) {
        String permission = AssignmentConstants.PERMISSION_MANAGE_SUBMISSIONS;
        if (userId == null) {
            return userHasPermission(contextId, permission);
        } else {
            return userHasPermission(userId, contextId, permission);
        }
    }
    
    public boolean userHasViewAssignmentPermission(String userId, String contextId) {
        String permission = AssignmentConstants.PERMISSION_VIEW_ASSIGNMENTS;
        if (userId == null) {
            return userHasPermission(contextId, permission);
        } else {
            return userHasPermission(userId, contextId, permission);
        }
    }
    
    public boolean userHasPermission(String contextId, String permission) {
        boolean hasPerm = securityService.unlock(permission, siteService.siteReference(contextId));
        
        // if they don't have perm for the site, check for group permission (unless they
        // are checking all groups perm)
        if (!hasPerm && !AssignmentConstants.PERMISSION_ALL_GROUPS.equals(permission)) {
            Collection<Group> allowedGroups = getGroupsAllowedForUser(null, contextId, permission);
            if (allowedGroups != null && !allowedGroups.isEmpty()) {
                hasPerm = true;
            }
        }
        
        return hasPerm;
    }
    
    public boolean userHasPermission(String userId, String contextId, String permission) {
        boolean hasPerm = securityService.unlock(userId, permission, siteService.siteReference(contextId));

        // if they don't have perm for the site, check for group permission (unless they
        // are checking all groups perm)
        if (!hasPerm && !AssignmentConstants.PERMISSION_ALL_GROUPS.equals(permission)) {
            Collection<Group> allowedGroups = getGroupsAllowedForUser(userId, contextId, permission);
            if (allowedGroups != null && !allowedGroups.isEmpty()) {
                hasPerm = true;
            }
        }

        return hasPerm;
    }
    
    public List<String> getAssignmentLevelPermissions() {
        List<String> assignmentPerms = new ArrayList<String>();
        for (int i=0; i < assignmentLevelPermissions.length; i++) {
            assignmentPerms.add(assignmentLevelPermissions[i]);
        }
        
        return assignmentPerms;
    }
    
    public List<String> getSiteLevelPermissions() {
        List<String> sitePerms = new ArrayList<String>();
        for (int i=0; i < siteLevelPermissions.length; i++) {
            sitePerms.add(siteLevelPermissions[i]);
        }
        
        return sitePerms;
    }
    
    public List<String> getPermissionsThatRequireAllGroups() {
        List<String> permsWithAllGroups = new ArrayList<String>();
        for (int i=0; i < permissionsThatRequireAllGroups.length; i++) {
            permsWithAllGroups.add(permissionsThatRequireAllGroups[i]);
        }
        
        return permsWithAllGroups;
    }
    
    public List<String> getPermissionsThatRequireOneGroup() {
        List<String> permsWithOneGroup = new ArrayList<String>();
        for (int i=0; i < permissionsThatRequireOneGroup.length; i++) {
            permsWithOneGroup.add(permissionsThatRequireOneGroup[i]);
        }
        return permsWithOneGroup;
    }
    
    public List<String> getPermissionsForAtLeastOneOrNoGroups() {
        List<String> oneOrNoGroups = new ArrayList<String>();
        for (int i=0; i < permissionsForAtLeastOneOrNoGroups.length; i++) {
            oneOrNoGroups.add(permissionsForAtLeastOneOrNoGroups[i]);
        }
        
        return oneOrNoGroups;
    }
    
    public Set<String> getUsersWithPermission(String contextId, String permission) {
        if (contextId == null || permission == null) {
            throw new IllegalArgumentException("Null contextId (" + contextId + ") or permission" +
                    " ("+ permission + ") passed to getUsersWithPermission");
        }
        
        Collection<String> groupRefs = new ArrayList<String>();
        
        // first, add the site
        groupRefs.add(siteService.siteReference(contextId));
        
        // now check for site groups
        Collection<Group> siteGroups = externalLogic.getSiteGroups(contextId);
        if (siteGroups != null) {
            for (Group group : siteGroups) {
                groupRefs.add(group.getReference());
            }
        }
        
        Set<String> userIdsWithPerm = authzGroupService.getUsersIsAllowed(permission, groupRefs);
        if (userIdsWithPerm == null) {
            userIdsWithPerm = new HashSet<String>();
        }
        
        return userIdsWithPerm;
    }
    
    private Collection<Group> getGroupsAllowedForUser(String userId, String context, String function) {
        Collection<Group> allowedGroups = new ArrayList<Group>();
        
        // no need to check if they are looking for the all groups perm
        if (!AssignmentConstants.PERMISSION_ALL_GROUPS.equals(function)) {
            // first, get the groups in this context (site)
            Collection<Group> allGroups = externalLogic.getSiteGroups(context);
            if (allGroups != null && !allGroups.isEmpty()) {
                if (userId == null) {
                    userId = externalLogic.getCurrentUserId();
                }

                // put these in a list to filter
                Collection<String> groupRefs = new ArrayList<String>();
                for (Group group : allGroups) {
                    groupRefs.add(group.getReference());
                }

                // filter out the groups with permission
                groupRefs = authzGroupService.getAuthzGroupsIsAllowed(userId, function, groupRefs);

                // now go back and get the equivalent group
                if (groupRefs != null && !groupRefs.isEmpty()) {
                    for (Group group : allGroups) {
                        if (groupRefs.contains(group.getReference())) {
                            allowedGroups.add(group);
                        }
                    }
                }
            }
        }

        return allowedGroups;
    }
    
    public Map<Role, Map<String, Boolean>> getRolePermissionsForSite(String contextId, Collection<String> functions) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getRolePermissions");
        }
        Map<Role, Map<String, Boolean>> rolePermissionMap = new HashMap<Role, Map<String,Boolean>>();

        if (functions != null) {
            Site site = externalLogic.getSite(contextId);
            if (site != null) {
                Set<Role> siteRoles = site.getRoles();
                if (siteRoles != null) {
                    // sort the roles
                    List<Role> orderedRoles = new ArrayList<Role>(siteRoles);
                    Collections.sort(orderedRoles);
                    for (Role role : orderedRoles) {
                        Map<String, Boolean> permissionMap = new HashMap<String, Boolean>();
                        Set<String> allowedFunctions = role.getAllowedFunctions();
                        for (String function : functions) {
                            if (allowedFunctions != null && allowedFunctions.contains(function)) {
                                permissionMap.put(function, true);
                            } else {
                                permissionMap.put(function, false);
                            }
                        }

                        rolePermissionMap.put(role, permissionMap);
                    }
                }
            }
        }

        return rolePermissionMap;
    }
    
    public SecurityAdvisor getSecurityAdvisor(String function, List<String> references) {
        return new MySecurityAdvisor(externalLogic.getCurrentUserId(), function, references);
    }
    
    public void addSecurityAdvisor(SecurityAdvisor advisor) {
        securityService.pushAdvisor(advisor);
    }
    
    public void removeSecurityAdvisor() {
        securityService.popAdvisor();
    }
    
    /**
     * A simple SecurityAdviser that can be used to override permissions on one reference string for
     * one user for one function.
     */
    private class MySecurityAdvisor implements SecurityAdvisor {

        protected String m_userId;

        protected String m_function;

        protected List<String> m_references = new ArrayList<String>();

        public MySecurityAdvisor(String userId, String function, String reference) {
            m_userId = userId;
            m_function = function;
            m_references.add(reference);
        }

        public MySecurityAdvisor(String userId, String function, List<String> references) {
            m_userId = userId;
            m_function = function;
            m_references = references;
        }

        public SecurityAdvice isAllowed(String userId, String function, String reference) {
            SecurityAdvice rv = SecurityAdvice.PASS;
            if (m_userId.equals(userId) && m_function.equals(function)
                    && m_references.contains(reference)) {
                rv = SecurityAdvice.ALLOWED;
            }
            return rv;
        }
    }

    
    /* Dependencies */

    public void setFunctionManager(FunctionManager functionManager) {
        this.functionManager = functionManager;
    }
    
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
    
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
    
    public void setAuthzGroupService(AuthzGroupService authzGroupService) {
        this.authzGroupService = authzGroupService;
    }
    
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
}