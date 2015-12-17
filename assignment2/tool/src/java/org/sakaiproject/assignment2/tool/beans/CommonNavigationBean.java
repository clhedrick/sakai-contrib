/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/beans/AssignmentSubmissionBean.java $
 * $Id: AssignmentSubmissionBean.java 66269 2010-02-24 17:31:51Z wagnermr@iupui.edu $
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

import org.sakaiproject.assignment2.tool.WorkFlowResult;

/**
 * This bean contains commonly-used navigation actions, such as
 * "Return to List"
 * 
 *
 */
public class CommonNavigationBean {

    /**
     * 
     * Navigate to the Assignment List screen
     */
    public WorkFlowResult processActionCancelToList() {
        return WorkFlowResult.CANCEL_TO_LIST_VIEW;
    }

}