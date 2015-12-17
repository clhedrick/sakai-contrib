/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/AssignmentViewParams.java $
 * $Id: AssignmentViewParams.java 61484 2009-06-29 19:01:16Z swgithen@mtu.edu $
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

package org.sakaiproject.assignment2.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/**
 * Standard View Parameters for operating on a single Assignment2 entry.
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class AssignmentViewParams extends SimpleViewParameters {

    public Long assignmentId;
    public Long duplicatedAssignmentId;

    public AssignmentViewParams() {}

    public AssignmentViewParams(String viewId) {
        super(viewId);
    }

    public AssignmentViewParams(String viewId, Long assignmentId) {
        super(viewId);
        this.assignmentId = assignmentId;
    }

    public AssignmentViewParams(String viewId, Long assignmentId, Long duplicatedAssignmentId) {
        super(viewId);
        this.assignmentId = assignmentId;
        this.duplicatedAssignmentId = duplicatedAssignmentId;
    }

    public String getParseSpec() {
        return super.getParseSpec() + ",@1:assignmentId,duplicatedAssignmentId";
    }
}