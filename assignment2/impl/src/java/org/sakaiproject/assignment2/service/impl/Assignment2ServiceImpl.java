/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/logic/impl/AssignmentLogicImpl.java $
 * $Id: AssignmentLogicImpl.java 65784 2010-01-20 19:04:25Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.service.api.Assignment2Service;
import org.sakaiproject.assignment2.service.model.AssignmentDefinition;


/**
 * The implementation for the externally exposed service for Assignment2. 
 */
public class Assignment2ServiceImpl implements Assignment2Service {

    private static Log log = LogFactory.getLog(Assignment2ServiceImpl.class);

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    private AssignmentPermissionLogic permissionLogic;
    public void setAssignmentPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }
    
    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }
    
    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }


    public void init(){
        if(log.isDebugEnabled()) log.debug("init");
    }

    public AssignmentDefinition getAssignmentById(Long assignmentId) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("Null assignmentId passed to getAssignmentById");
        }
        
        if (!permissionLogic.isUserAllowedToViewAssignmentId(null, assignmentId, null)) {
            throw new SecurityException("User attempted to view assignment with id " + assignmentId + " without permission");
        }

        Assignment2 assign = assignmentLogic.getAssignmentByIdWithAssociatedData(assignmentId);
        
        // the method above should already throw this exception, but just in case that changes...
        if (assign == null) {
            throw new AssignmentNotFoundException("No assignment exists with id: " + assignmentId);
        }
        
        Map<String, String> groupIdToTitleMap = null;
        if (assign.getAssignmentGroupSet() != null && !assign.getAssignmentGroupSet().isEmpty()) {
            groupIdToTitleMap = externalLogic.getGroupIdToNameMapForSite(assign.getContextId());
        }
        
        Map<Long, GradebookItem> gbIdItemMap = null;
        if (assign.isGraded()) {
            gbIdItemMap = getGradebookItemIdToItemMap(assign.getContextId());
        }
        
        AssignmentDefinition assignDef = assignmentLogic.getAssignmentDefinition(assign, gbIdItemMap, groupIdToTitleMap);
        
        filterAssignmentDefinition(assign, assignDef);
        
        return assignDef;
    }
    
    public List<AssignmentDefinition> getAssignments(String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getAssignments");
        }
        
        List<AssignmentDefinition> assignList = new ArrayList<AssignmentDefinition>();
        List<Assignment2> viewableAssignments = assignmentLogic.getViewableAssignments(contextId);
        if (viewableAssignments != null) {
            Map<String, String> groupIdToTitleMap = externalLogic.getGroupIdToNameMapForSite(contextId);
            Map<Long, GradebookItem> gbIdItemMap = getGradebookItemIdToItemMap(contextId);
            
            // we want to return the AssignmentDefinition filtered for restricted info
            for (Assignment2 assign : viewableAssignments) {
                AssignmentDefinition assignDef = assignmentLogic.getAssignmentDefinition(assign, gbIdItemMap, groupIdToTitleMap);
                filterAssignmentDefinition(assign, assignDef);
                assignList.add(assignDef);
            }
        }
        
        return assignList;
    }
    
    /**
     * 
     * @param contextId
     * @return a map of gradebook item ids to {@link GradebookItem} for the
     * given context. only includes gradebook items that the current user has
     * permission to view in the gradebook
     */
    private Map<Long, GradebookItem> getGradebookItemIdToItemMap(String contextId) {
        Map<Long, GradebookItem> gbIdItemMap = null;
        List<GradebookItem> allGbItems = gradebookLogic.getViewableGradebookItems(contextId);
        gbIdItemMap = new HashMap<Long, GradebookItem>();
        if (allGbItems != null) {
            for (GradebookItem item : allGbItems) {
                gbIdItemMap.put(item.getGradebookItemId(), item);
            }
        }
        
        return gbIdItemMap;
    }
    
    /**
     * Some information on the {@link AssignmentDefinition} is restricted based upon
     * the current user's permissions. This method will set restricted properties to
     * null on the given assignDef
     * @param contextId
     * @param assignDef
     */
    private void filterAssignmentDefinition(Assignment2 assign, AssignmentDefinition assignDef) {
        if (assign == null) {
            throw new IllegalArgumentException("Null assign passed to filterAssignmentDefinition");
        }
        
        if (assignDef != null) {
            // if the current user does not have instructor-level privileges for this assignment,
            // we may need to set some of the properties to null
            if (permissionLogic.isUserAllowedToManageSubmissionsForAssignment(null, assign) ||
                    permissionLogic.isUserAllowedToEditAssignment(null, assign) ||
                    permissionLogic.isUserAllowedToDeleteAssignment(null, assign)) {
                // we can display the restricted info
            } else {
                assignDef.setAcceptUntilDate(null);
                // for now, restrict all of the properties
                assignDef.setProperties(new HashMap());
            }
        }
    };
    
}
