/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/ViewSubmissionsViewParams.java $
 * $Id: ViewSubmissionsViewParams.java 65451 2009-12-22 18:38:43Z swgithen@mtu.edu $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.tool.producers.ViewSubmissionsProducer;

public class ViewSubmissionsViewParams extends SortPagerViewParams implements VerifiableViewParams {

    private static final Log LOG = LogFactory.getLog(ViewSubmissionsViewParams.class);

    public Long assignmentId;
    public int pageIndex = 0;

    /**
     * optionally filter the view by group membership. null if you
     * do not want to filter by group
     */
    public String groupId;

    public ViewSubmissionsViewParams() {}

    public ViewSubmissionsViewParams(String viewId) {
        super(viewId);
    }

    public ViewSubmissionsViewParams(String viewId, Long assignmentId) {
        super(viewId);
        this.assignmentId = assignmentId;
    }

    public ViewSubmissionsViewParams(String viewId, Long assignmentId, String groupId) {
        super(viewId);
        this.assignmentId = assignmentId;
        this.groupId = groupId;
    }

    public ViewSubmissionsViewParams(String viewId, String sort_by, String sort_dir) {
        super(viewId, sort_by, sort_dir);
    }

    public ViewSubmissionsViewParams(String viewId, String sort_by, String sort_dir, int currentStart, int currentCount) {
        super(viewId, sort_by, sort_dir, currentStart, currentCount);
    }

    public ViewSubmissionsViewParams(String viewId, String sort_by, String sort_dir, int currentStart, int currentCount, Long assignmentId) {
        super(viewId, sort_by, sort_dir, currentStart, currentCount);
        this.assignmentId = assignmentId;
    }

    public ViewSubmissionsViewParams(String viewId, String sort_by, String sort_dir, int currentStart, int currentCount, Long assignmentId, int pageIndex) {
        super(viewId, sort_by, sort_dir, currentStart, currentCount);
        this.assignmentId = assignmentId;
        this.pageIndex = pageIndex;
    }
    
    public String getParseSpec() {
        // include a comma delimited list of the public properties in this class
        return super.getParseSpec() + ",@1:assignmentId,groupId,pageIndex";
    }

    public Boolean verify()
    {
        if (ViewSubmissionsProducer.VIEW_ID.equals(this.viewID) && this.assignmentId == null) {
            LOG.error("Null assignmentId in viewparameters while attempting to load ViewSubmissionsProducer");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}