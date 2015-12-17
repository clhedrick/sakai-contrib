/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/GradeViewParams.java $
 * $Id: GradeViewParams.java 67170 2010-04-14 16:23:04Z swgithen@mtu.edu $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.tool.beans.AssignmentAuthoringBean;
import org.sakaiproject.assignment2.tool.producers.GradeProducer;

public class GradeViewParams extends SimpleViewParameters implements VerifiableViewParams {

    private static final Log LOG = LogFactory.getLog(AssignmentAuthoringBean.class);

    public Long assignmentId;
    public String userId;
    public Long versionId;
    public int viewSubPageIndex; // This is so we can return to the same page on the View Submissions.

    public GradeViewParams(){}

    public GradeViewParams(String viewId, Long assignmentId, String userId){
        super(viewId);
        this.assignmentId = assignmentId;
        this.userId = userId;
    }

    public GradeViewParams(String viewId, Long assignmentId, String userId, Long versionId) {
        super(viewId);
        this.assignmentId = assignmentId;
        this.userId = userId;
        this.versionId = versionId;
    }
    
    public GradeViewParams(String viewId, Long assignmentId, String userId, Long versionId, int viewSubPageIndex) {
        super(viewId);
        this.assignmentId = assignmentId;
        this.userId = userId;
        this.versionId = versionId;
        this.viewSubPageIndex = viewSubPageIndex;
    }
    
    public GradeViewParams(String viewId, GradeViewParams other) {
    	super(viewId);
        this.assignmentId = other.assignmentId;
        this.userId = other.userId;
        this.versionId = other.versionId;
        this.viewSubPageIndex = other.viewSubPageIndex;
    }

    public String getParseSpec(){
        return super.getParseSpec() + ",@1:assignmentId,@2:userId,versionId,viewSubPageIndex";
    }

    public Boolean verify()
    {
        if (GradeProducer.VIEW_ID.equals(this.viewID) && (this.assignmentId == null || this.userId == null)) {
            LOG.error("Null assignmentId or userId in viewparameters while attempting to load GradeProducer");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}