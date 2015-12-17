/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/ListProducer.java $
 * $Id: ListProducer.java 87495 2015-04-06 15:12:29Z hedrick@rutgers.edu $
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

package org.sakaiproject.assignment2.tool.producers;

import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.params.AssignmentViewParams;
import org.sakaiproject.component.cover.ServerConfigurationService;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * This renders the Instructor Landing Page that shows the list of assignments
 * in the course. Along with the ability to drag'n'drop reorder, delete, edit,
 * and go to the submissions.
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class ListProducer implements ViewComponentProducer, DefaultView {

    public static final String VIEW_ID = "list";

    public String getViewID() {
        return VIEW_ID;
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    
    private AssignmentPermissionLogic permissionLogic;
    public void setAssignmentPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }
    
    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }
    
    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
        
        // render the tool-level action links
        String currContextId = externalLogic.getCurrentContextId();
        String currUserId = externalLogic.getCurrentUserId();
        
        boolean add = permissionLogic.isUserAllowedToAddAssignments(currUserId, currContextId);
        boolean reorder = permissionLogic.isUserAllowedToEditAllAssignments(currUserId, currContextId);
        boolean siteUpd = permissionLogic.isUserAllowedToUpdateSite(currContextId);
        boolean allGoups = permissionLogic.isUserAllowedForAllGroups(currUserId, currContextId);
        
        if (add || reorder || siteUpd || (add && allGoups)) {
            UIOutput.make(tofill, "actionBar");
            // the Add, Reorder, and Permissions links
            if (add) {
                UIOutput.make(tofill, "add_action");
                UIInternalLink.make(tofill, "add_link", UIMessage.make("assignment2.list.add_assignment"), 
                        new AssignmentViewParams(AssignmentProducer.VIEW_ID));
            }
            if (reorder) {
                UIOutput.make(tofill, "reorder_action");
                UIInternalLink.make(tofill, "reorder_link", UIMessage.make("assignment2.list.reorder"), 
                        new AssignmentViewParams(ReorderStudentViewProducer.VIEW_ID));

                if (add) {
                    UIOutput.make(tofill, "sep0");
                }
            }
            if (add && allGoups && 
                    ServerConfigurationService.getBoolean(AssignmentConstants.CONFIG_ALLOW_IMPORT_FROM_ASNN1, true)) {
                UIOutput.make(tofill, "import_action");
                UIInternalLink.make(tofill, "import_link", UIMessage.make("assignment2.list.import_assignments"), 
                        new AssignmentViewParams(ImportAssignmentsProducer.VIEW_ID));
                if(reorder){
                	UIOutput.make(tofill, "sep1");
                }else if(add){
                	UIOutput.make(tofill, "sep0");
                }
            }
            if (siteUpd) {
                UIOutput.make(tofill, "permissions_action");
                UIInternalLink.make(tofill, "permissions_link", UIMessage.make("assignment2.list.permissions"), 
                        new AssignmentViewParams(PermissionsProducer.VIEW_ID));

                if (add || reorder) {
                    UIOutput.make(tofill, "sep2");
                }

		Object sessionToken = org.sakaiproject.tool.cover.SessionManager.getCurrentSession().getAttribute("sakai.csrf.token");
		if (sessionToken != null)
		    UIOutput.make(tofill, "csrf", sessionToken.toString());

            }
            
            if (gradebookLogic.isCurrentUserAbleToEdit(currContextId) && externalLogic.siteHasTool(currContextId, ExternalLogic.TOOL_ID_GRADEBOOK)) {
                UIOutput.make(tofill, "grading_action");
                UIInternalLink.make(tofill, "grading_link", UIMessage.make("assignment2.grader-perms.page_title"), 
                        new AssignmentViewParams(GraderPermissionsProducer.VIEW_ID));
                
                if (add || reorder || siteUpd) {
                    UIOutput.make(tofill, "sep3");
                }
            }
        }
    }

}
