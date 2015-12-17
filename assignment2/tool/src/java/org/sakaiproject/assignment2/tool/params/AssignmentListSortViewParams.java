/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/AssignmentListSortViewParams.java $
 * $Id: AssignmentListSortViewParams.java 61484 2009-06-29 19:01:16Z swgithen@mtu.edu $
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

public class AssignmentListSortViewParams extends SortPagerViewParams {

    public Long assignmentIdToDuplicate;

    public AssignmentListSortViewParams() {}

    public AssignmentListSortViewParams(String viewId) {
        super(viewId);
    }

    public AssignmentListSortViewParams(String viewId, String sort_by, String sort_dir) {
        super(viewId, sort_by, sort_dir);
    }

    public AssignmentListSortViewParams(String viewId, String sort_by, String sort_dir, int currentStart, int currentCount) {
        super(viewId, sort_by, sort_dir, currentStart, currentCount);
    }

    public AssignmentListSortViewParams(String viewId, String sort_by, String sort_dir, int currentStart, int currentCount, Long assignmentIdToDuplicate) {
        super(viewId, sort_by, sort_dir, currentStart, currentCount);
        this.assignmentIdToDuplicate = assignmentIdToDuplicate;
    }

    public String getParseSpec() {
        // include a comma delimited list of the public properties in this class
        return super.getParseSpec() + ",assignmentIdToDuplicate";
    }
}