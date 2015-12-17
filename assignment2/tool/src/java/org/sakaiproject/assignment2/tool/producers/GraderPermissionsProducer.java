/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/branches/ASNN-521/tool/src/java/org/sakaiproject/assignment2/tool/producers/GradeProducer.java $
 * $Id: GradeProducer.java 66084 2010-02-09 21:48:28Z wagnermr@iupui.edu $
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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.params.SimpleAssignmentViewParams;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.component.api.ServerConfigurationService;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * This view displays the gradebook grading privileges for the different roles
 * defined in this site. It also allows a user to edit TA grader permissions via
 * the gradebook helper
 *
 */
public class GraderPermissionsProducer implements ViewComponentProducer, ViewParamsReporter {

    public static final String VIEW_ID = "graderPermissions";
    public String getViewID() {
        return VIEW_ID;
    }

    private static Log log = LogFactory.getLog(GraderPermissionsProducer.class);

    private ExternalGradebookLogic gradebookLogic;
    private ExternalLogic externalLogic;
    private ServerConfigurationService serverConfigurationService;


    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {

        UIInternalLink.make(tofill, "breadcrumb_asnn_list", 
                UIMessage.make("assignment2.list.heading"),
                new SimpleViewParameters(ListProducer.VIEW_ID));

        String currContextId = externalLogic.getCurrentContextId();
        boolean useHelper = serverConfigurationService.getBoolean(AssignmentConstants.PROP_GRADER_PERMISSIONS_HELPER, false);

        if (useHelper) {
            UIMessage.make(tofill, "grader-perms-instructions", "assignment2.grader-perms.instructions");
        } else {
            UIMessage.make(tofill, "grader-perms-instructions", "assignment2.grader-perms.instructions.no_helper");
        }

        // make a table of all of the roles in the site to the corresponding gradebook grading privileges
        Map<Role, Map<String, Boolean>> rolePermissionsMap = gradebookLogic.getGradebookPermissionsForRoles(currContextId);
        if (rolePermissionsMap != null) {
            for (Map.Entry<Role, Map<String, Boolean>> entry : rolePermissionsMap.entrySet()) {
                UIBranchContainer roleRow = UIBranchContainer.make(tofill, "role_row:");
                Role role = entry.getKey();
                Map<String, Boolean> permissionsMap = entry.getValue();

                UIOutput.make(roleRow, "role_name", role.getId());

                String permissionString;
                if (hasPermission(ExternalGradebookLogic.GB_GRADE_ALL, permissionsMap)) {
                    permissionString = "assignment2.grader-perms.grade.all";
                } else if (hasPermission(ExternalGradebookLogic.GB_GRADE_SECTION, permissionsMap)) {
                    permissionString = "assignment2.grader-perms.grade.section";
                } else {
                    permissionString = "assignment2.grader-perms.grade.none";
                }

                UIMessage.make(roleRow, "role_privileges", permissionString);

                // check to see if we display the "Customize" link
                if (useHelper && hasPermission(ExternalGradebookLogic.GB_TA, permissionsMap)) {
                    String url = externalLogic.getGraderPermissionsUrl(currContextId);
                    // append the extra params for the thickbox view
                    String finishedURL = externalLogic.getAssignmentViewUrl(FinishedHelperProducer.VIEWID);
                    String getParams = "?TB_iframe=true&width=700&height=415&KeepThis=true&finishURL=" + finishedURL;
                    url += getParams;
                    UILink.make(roleRow, "customize_link",
                            UIMessage.make("assignment2.grader-perms.customize"), url);
                }
            }
        }

        // make the button to return to the list
        UIForm form = UIForm.make(tofill, "return-to-list-form");
        UICommand.make(form, "return-to-list", UIMessage.make("assignment2.grader-perms.cancel"), "CommonNavigationBean.processActionCancelToList");
    }

    /**
     * 
     * @param function
     * @param permissionsMap
     * @return true if the permissionsMap contains the given function
     */
    private boolean hasPermission(String function, Map<String, Boolean> permissionsMap) {

        boolean hasPerm;
        if (permissionsMap != null && permissionsMap.containsKey(function)) {
            hasPerm = permissionsMap.get(function);
        } else {
            hasPerm = false;
        }

        return hasPerm;
    }

    public ViewParameters getViewParameters() {
        return new SimpleAssignmentViewParams();
    }

    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }

    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public void setServerConfigurationService(ServerConfigurationService serverConfigurationService) {
        this.serverConfigurationService = serverConfigurationService;
    }
}
