/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/ZipViewParams.java $
 * $Id: ZipViewParams.java 81733 2012-10-25 18:00:58Z dsobiera@indiana.edu $
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

public class ZipViewParams extends AssignmentViewParams {

	public String filterGroupId;
	
    public ZipViewParams(String viewID, Long assignmentId, String filterGroupId) {
        super(viewID, assignmentId);
    	this.filterGroupId = filterGroupId;
    }

    public ZipViewParams(String viewID, Long assignmentId) {
        super(viewID, assignmentId);
    	this.filterGroupId = null;
    }

    public ZipViewParams() {}

    public String getParseSpec() {
        return super.getParseSpec() + ",filterGroupId";
    }
}
