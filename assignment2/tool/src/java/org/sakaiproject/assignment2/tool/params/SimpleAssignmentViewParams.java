/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/SimpleAssignmentViewParams.java $
 * $Id: SimpleAssignmentViewParams.java 67637 2010-05-12 14:27:52Z wagnermr@iupui.edu $
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
import org.sakaiproject.assignment2.tool.beans.AssignmentAuthoringBean;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/**
 * This view params is for navigating to the apparently student view of an 
 * assignment, though I'm guessing other views may use it as well.
 * 
 * It implements VerifiableViewParams, to allow for auditing/validating itself.
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class SimpleAssignmentViewParams extends SimpleViewParameters implements VerifiableViewParams{

    private static final Log LOG = LogFactory.getLog(AssignmentAuthoringBean.class);

    public Long assignmentId;
    // when an assignment is tagged, there may be expanded permissions for viewing it. we
    // use this optional reference to go back and see if the user should have expanded
    // permissions to view this assignment
    public String tagReference;
    // this param is used similarly to the tagReference but is mainly used for modifying
    // the url for attachments to allow extended privileges, if appropriate
    public String tagDecoWrapper;

    public SimpleAssignmentViewParams() {}

    public SimpleAssignmentViewParams(String viewId, Long assignmentId){
        super(viewId);
        this.assignmentId = assignmentId;
    }
    
    public SimpleAssignmentViewParams(String viewId, Long assignmentId, String tagReference, String tagDecoWrapper){
        super(viewId);
        this.assignmentId = assignmentId;
        this.tagReference = tagReference;
        this.tagDecoWrapper = tagDecoWrapper;
    }

    public String getParseSpec() {
        return super.getParseSpec() + ",@1:assignmentId,tagReference";
    }

    public Boolean verify()
    {
        // SWG TODO FIXME
        // if (StudentSubmitProducer.VIEW_ID.equals(this.viewID) && this.assignmentId == null){
        //     LOG.error("Null assignmentId in viewparamters while attempting to load StudentSubmitProducer");
        //     return Boolean.FALSE;
        // }

        return Boolean.TRUE;
    }
}