/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/LocalPermissionLogic.java $
 * $Id: LocalPermissionLogic.java 86090 2014-04-14 18:15:43Z dsobiera@indiana.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 The Sakai Foundation.
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

package org.sakaiproject.assignment2.tool;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.producers.*;
import org.sakaiproject.assignment2.tool.producers.fragments.*;
import org.sakaiproject.assignment2.tool.params.AssignmentViewParams;
import org.sakaiproject.assignment2.tool.params.FragmentGradebookDetailsViewParams;
import org.sakaiproject.assignment2.tool.params.GradeViewParams;
import org.sakaiproject.assignment2.tool.params.SimpleAssignmentViewParams;
import org.sakaiproject.assignment2.tool.params.ViewSubmissionParams;
import org.sakaiproject.assignment2.tool.params.ViewSubmissionsViewParams;
import org.sakaiproject.assignment2.tool.params.ZipViewParams;

import uk.org.ponder.rsf.builtin.UVBProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * Given a view params, this class allows you determine whether a User has
 * permission to actually view that URL.
 * 
 * Also included is the logic that determines whether to go to the 
 * StudentSubmitSummary or just StudentSubmit page.  I'm thinking about 
 * combining those.
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class LocalPermissionLogic {

    private AssignmentPermissionLogic permissionLogic;
    public void setPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    
    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }
    
    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }

    /**
     * Determines whether or not a User can actually view the page defined
     * by these view parameters.
     * 
     * @param viewParams
     * @return Whether not the user can view this page.  False if not, True if
     * they are.
     */
    public Boolean checkCurrentUserHasViewPermission(ViewParameters viewParams) {
        String contextId = externalLogic.getCurrentContextId();
        String viewId = viewParams.viewID;
        
        // let's see if we can derive it from the viewParams
        if (contextId == null) {
            if (viewParams instanceof SimpleAssignmentViewParams) {
                // let's try to derive the contextId from the assignmentId
                SimpleAssignmentViewParams params = (SimpleAssignmentViewParams) viewParams;
                Assignment2 assign = assignmentLogic.getAssignmentById(params.assignmentId);
                contextId = assign.getContextId();
            }
        }

        /* This first check is special and for our new hybrid pages that need to
         * go to the content directory for various Fluid interfaces.
         */
        // ASNN-466 if (viewId.equals("content")) {
        //    return Boolean.TRUE;
        // }
        if (AddAttachmentHelperProducer.VIEWID.equals(viewId)) {
            return Boolean.TRUE;
        }
        else if (PreviewAsStudentProducer.VIEW_ID.equals(viewId)) {
            // the instructor may preview new assignments if they have the add
            // perm but they can only preview existing assignments if they may
            // edit that assignment
            if (viewParams instanceof SimpleAssignmentViewParams) {
                SimpleAssignmentViewParams params = (SimpleAssignmentViewParams) viewParams;
                return isUserAllowedToAddOrEditAssignment(params.assignmentId, contextId);
            }
            
            return Boolean.FALSE;
        }
        else if (ListProducer.VIEW_ID.equals(viewId)) {
            // a user may see this view if they are NOT allowed to submit but
            // they are allowed to view assignments generally. if they have any
            // instructor privileges, they will also be able to view the list page
            return permissionLogic.isUserAllowedToTakeInstructorAction(null, contextId) ||
            (!permissionLogic.isUserAllowedToSubmit(null, contextId) &&
                permissionLogic.isUserAllowedToViewAssignments(null, contextId));
        }
        else if (ReorderStudentViewProducer.VIEW_ID.equals(viewId)) {
            return permissionLogic.isUserAllowedToEditAllAssignments(null, contextId);
        }
        else if (AssignmentInfoDataProducer.VIEW_ID.equals(viewId)) {
            return permissionLogic.isUserAllowedToTakeInstructorAction(null, contextId);
        }
        else if (AssignmentProducer.VIEW_ID.equals(viewId)) {
            // permission to view this screen depends upon whether this is an add
            // or edit scenario
            if (viewParams instanceof AssignmentViewParams) {
                AssignmentViewParams params = (AssignmentViewParams) viewParams;
                return isUserAllowedToAddOrEditAssignment(params.assignmentId, contextId);
            }
            
            return Boolean.FALSE;

        } else if (FinishedHelperProducer.VIEWID.equals(viewId)) {
            return Boolean.TRUE;

        } else if (GradeProducer.VIEW_ID.equals(viewId)) {
            if (viewParams instanceof GradeViewParams)
            {
                GradeViewParams params = (GradeViewParams) viewParams;
                return permissionLogic.isUserAllowedToManageSubmissionForAssignmentId(null, params.userId, params.assignmentId);
            } 

            return Boolean.FALSE;

        } 
        else if (StudentAssignmentListProducer.VIEW_ID.equals(viewId)) {
            return permissionLogic.isUserAllowedToSubmit(null, contextId) && 
                !permissionLogic.isUserAllowedToTakeInstructorAction(null, contextId);

        } else if (StudentSubmitProducer.VIEW_ID.equals(viewId)) {
            if (viewParams instanceof SimpleAssignmentViewParams) {
                SimpleAssignmentViewParams params = (SimpleAssignmentViewParams) viewParams;

                return permissionLogic.isUserAllowedToMakeSubmissionForAssignmentId(null, params.assignmentId);
            }

            return Boolean.FALSE;

        } else if (ViewSubmissionsProducer.VIEW_ID.equals(viewId)) {
            if (viewParams instanceof ViewSubmissionsViewParams) {
                ViewSubmissionsViewParams params = (ViewSubmissionsViewParams) viewParams;

                return permissionLogic.isUserAllowedToManageSubmissionsForAssignmentId(null, params.assignmentId);
            }

            return Boolean.FALSE;

        } else if (FragmentAssignment2SelectProducer.VIEW_ID.equals(viewId)) {
            // TODO: it isn't clear what permission you should have for this one,
            // so defaulting to add perm
            return permissionLogic.isUserAllowedToAddAssignments(null, contextId);
        }
        else if (FragmentGradebookDetailsProducer.VIEW_ID.equals(viewId)) {
            if (viewParams instanceof FragmentGradebookDetailsViewParams) {
                FragmentGradebookDetailsViewParams params = (FragmentGradebookDetailsViewParams) viewParams;
                return permissionLogic.isUserAllowedToViewSubmissionForAssignment(null, params.userId, params.assignmentId, null);
            }

            return Boolean.FALSE;
        } else if (FragmentSubmissionGradePreviewProducer.VIEW_ID.equals(viewId)) {
            //TODO - RYAN!  Remove this producer!
            return Boolean.FALSE;
        } else if (UploadAllProducer.VIEW_ID.equals(viewId) || UploadAllConfirmProducer.VIEW_ID.equals(viewId)) {
            if (viewParams instanceof AssignmentViewParams) {
                AssignmentViewParams params = (AssignmentViewParams) viewParams;

                return permissionLogic.isUserAllowedToManageSubmissionsForAssignmentId(null, params.assignmentId);
            }

            return Boolean.FALSE;

        } else if ("zipSubmissions".equals(viewId)) {
            if (viewParams instanceof ZipViewParams) {
                ZipViewParams params = (ZipViewParams) viewParams;

                return permissionLogic.isUserAllowedToManageSubmissionsForAssignmentId(null, params.assignmentId);
            }

            return Boolean.FALSE;

        } else if (TaggableHelperProducer.VIEWID.equals(viewId)) {
            return permissionLogic.isUserAllowedToTakeInstructorAction(null, contextId);
        
        } else if (PermissionsProducer.VIEW_ID.equals(viewId)) {
            return permissionLogic.isUserAllowedToUpdateSite(contextId);
            
        } else if (GraderPermissionsProducer.VIEW_ID.equals(viewId)) {
            // NOTE: this is checking to see if the user has edit privileges in the gradebook tool
            return gradebookLogic.isCurrentUserAbleToEdit(contextId);
 
        } else if (ViewAssignmentProducer.VIEW_ID.equals(viewId)) {
            if (viewParams instanceof SimpleAssignmentViewParams) {
                SimpleAssignmentViewParams params = (SimpleAssignmentViewParams) viewParams;

                Map<String, Object> optionalParams = new HashMap<String, Object>();
                optionalParams.put(AssignmentConstants.TAGGABLE_REF_KEY, params.tagReference);
                return permissionLogic.isUserAllowedToViewAssignmentId(null, params.assignmentId, optionalParams);
            }

            return Boolean.FALSE;

        }
        else if (ViewStudentSubmissionProducer.VIEW_ID.equals(viewId)) {
        if (viewParams instanceof ViewSubmissionParams) {
            ViewSubmissionParams params = (ViewSubmissionParams) viewParams;
            Map<String, Object> optionalParams = new HashMap<String, Object>();
            optionalParams.put(AssignmentConstants.TAGGABLE_REF_KEY, params.tagReference);
            
            return permissionLogic.isUserAllowedToViewSubmissionForAssignment(null, params.userId, params.assignmentId, optionalParams);
        }

        return Boolean.FALSE;

        } else if(ImportAssignmentsProducer.VIEW_ID.equals(viewId)){
        	return permissionLogic.isUserAllowedToAddAssignments(null, contextId)
        	&& permissionLogic.isUserAllowedForAllGroups(null, contextId);
        }

        //Here are some RSF Generic always true viewIds

        else if (UVBProducer.VIEW_ID.equals(viewId)) {
            return Boolean.TRUE;
        }

        //else just say No
        return Boolean.FALSE;
    }
    
    /**
     * 
     * @param assignId if null, assumes add scenario
     * @param contextId
     * @return depending upon whether or not the assignId is null, will return true
     * if the current user has permission to add a new assignment (if assignId is null) or edit
     * an existing assignment (if assignId is not null)
     */
    private boolean isUserAllowedToAddOrEditAssignment(Long assignId, String contextId) {
        if (assignId == null) {
            // add assignment scenario
            return permissionLogic.isUserAllowedToAddAssignments(null, contextId);
        } else {
            // we are editing
            if (permissionLogic.isUserAllowedToEditAllAssignments(null, contextId)) {
                return true;
            } else {
                Assignment2 assign = assignmentLogic.getAssignmentById(assignId);
                return permissionLogic.isUserAllowedToEditAssignment(null, assign);
            }
        }
    }

}
