/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/branches/ASNN-521/tool/src/java/org/sakaiproject/assignment2/tool/params/GradeViewParams.java $
 * $Id: GradeViewParams.java 65451 2009-12-22 18:38:43Z swgithen@mtu.edu $
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
import org.sakaiproject.assignment2.tool.producers.ViewStudentSubmissionProducer;

public class ViewSubmissionParams extends SimpleViewParameters implements VerifiableViewParams {

    private static final Log LOG = LogFactory.getLog(AssignmentAuthoringBean.class);

    public Long assignmentId;
    public String userId;
    
    // when an assignment is tagged, there may be expanded permissions for viewing its submissions. we
    // use this optional reference to go back and see if the user should have expanded
    // permissions to view this submission
    public String tagReference;
    // this param is used similarly to the tagReference but is mainly used for modifying
    // the url for attachments to allow extended privileges, if appropriate
    public String tagDecoWrapper;

    public ViewSubmissionParams(){}

    public ViewSubmissionParams(String viewId, Long assignmentId, String userId){
        super(viewId);
        this.assignmentId = assignmentId;
        this.userId = userId;
    }
    
    public ViewSubmissionParams(String viewId, Long assignmentId, String userId, String tagReference){
        super(viewId);
        this.assignmentId = assignmentId;
        this.userId = userId;
        this.tagReference = tagReference;
    }
    
    public ViewSubmissionParams(String viewId, Long assignmentId, String userId, String tagReference, String tagDecoWrapper){
        super(viewId);
        this.assignmentId = assignmentId;
        this.userId = userId;
        this.tagReference = tagReference;
        this.tagDecoWrapper = tagDecoWrapper;
    }

    public String getParseSpec(){
        return super.getParseSpec() + ",@1:assignmentId,@2:userId,tagReference,tagDecoWrapper";
    }

    public Boolean verify()
    {
        if (ViewStudentSubmissionProducer.VIEW_ID.equals(this.viewID) && (this.assignmentId == null || this.userId == null)) {
            LOG.error("Null assignmentId or userId in viewparameters while attempting to load ViewStudentSubmissionProducer");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}