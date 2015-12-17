/**********************************************************************************
 * $URL:https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/beans/locallogic/LocalAssignmentLogic.java $
 * $Id:LocalAssignmentLogic.java 48274 2008-04-23 20:07:00Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.tool;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.component.api.ServerConfigurationService;


/**
 * Contains methods that are used by the UI. These methods will be used if your
 * implemented content review service is Turnitin.
 */
public class LocalTurnitinLogic {

    private static final Log LOG = LogFactory.getLog(LocalTurnitinLogic.class);
    
    private ServerConfigurationService serverConfigurationService;

    /**
     * 
     * @return the submission repository options available. possible values
     * are {@link AssignmentConstants#TII_VALUE_NO_REPO}, {@link AssignmentConstants#TII_VALUE_INSTITUTION_REPO}, {@link AssignmentConstants#TII_VALUE_STANDARD_REPO}.
     * Returns all three options if no preference was set in sakai.properties
     */
    public List<String> getSubmissionRepositoryOptions() {
        List<String> submissionRepoSettings = new ArrayList<String>();
        String[] propertyValues = serverConfigurationService.getStrings(AssignmentConstants.TII_PROP_SUBMIT_TO_REPO);
        if (propertyValues != null && propertyValues.length > 0) {
            for (int i=0; i < propertyValues.length; i++) {
                String propertyVal = propertyValues[i];
                if (propertyVal.equals(AssignmentConstants.TII_VALUE_NO_REPO) || 
                        propertyVal.equals(AssignmentConstants.TII_VALUE_INSTITUTION_REPO) ||
                        propertyVal.equals(AssignmentConstants.TII_VALUE_STANDARD_REPO)) {
                    submissionRepoSettings.add(propertyVal);
                }
            }
        }

        // if there are still no valid settings in the list at this point, use the default
        if (submissionRepoSettings.isEmpty()) {
            // add all three
            submissionRepoSettings.add(AssignmentConstants.TII_VALUE_NO_REPO);
            submissionRepoSettings.add(AssignmentConstants.TII_VALUE_INSTITUTION_REPO);
            submissionRepoSettings.add(AssignmentConstants.TII_VALUE_STANDARD_REPO);
        }

        return submissionRepoSettings;
    }
    
    /**
     * 
     * @return the default repository setting from sakai.properties. Returns {@link AssignmentConstants#TII_VALUE_NO_REPO} if no property is set
     */
    public String getDefaultSubmissionRepository() {
        String defaultRepo = serverConfigurationService.getString(AssignmentConstants.TII_PROP_DEFAULT_SUBMIT_TO_REPO, AssignmentConstants.TII_VALUE_NO_REPO);
        return defaultRepo;
    }
    
    /**
     * 
     * @return Returns the value for the institutional repository name set in sakai.properties.
     * Returns null if no value was set.
     */
    public String getInstitutionalRepositoryName() {
        String repoName = null;
        String prop = serverConfigurationService.getString(AssignmentConstants.TII_PROP_INSTITUTION_REPO_NAME);
        if (prop != null && !"".equals(prop.trim())) {
            repoName = prop;
        }
        
        return repoName;
    }
    
    /**
     * 
     * @return the url for the document containing the supported format information for
     * an attachment to be accepted by turnitin. Null if no property was set.
     */
    public String getSupportedFormatsUrl() {
        return serverConfigurationService.getString(AssignmentConstants.TII_PROP_FILE_REQUIREMENTS_URL, null);
    }
    
    public void setServerConfigurationService(ServerConfigurationService serverConfigurationService) {
        this.serverConfigurationService = serverConfigurationService;
    }

}