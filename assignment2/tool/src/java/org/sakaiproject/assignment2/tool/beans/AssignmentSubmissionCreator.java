/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/beans/AssignmentSubmissionCreator.java $
 * $Id: AssignmentSubmissionCreator.java 61484 2009-06-29 19:01:16Z swgithen@mtu.edu $
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

package org.sakaiproject.assignment2.tool.beans;

import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;

public class AssignmentSubmissionCreator {

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public AssignmentSubmission create(){
        AssignmentSubmission togo = new AssignmentSubmission();

        //create the AssignmentSubmissionVersion object
        AssignmentSubmissionVersion currentSubmissionVersion = new AssignmentSubmissionVersion();
        currentSubmissionVersion.setCreatedBy(externalLogic.getCurrentUserId());

        togo.setCurrentSubmissionVersion(currentSubmissionVersion);
        togo.setUserId(externalLogic.getCurrentUserId());

        return togo;
    }
}