/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/SimpleAssignmentViewParams.java $
 * $Id: SimpleAssignmentViewParams.java 66569 2010-03-16 16:20:09Z chmaurer@iupui.edu $
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
import org.sakaiproject.assignment2.tool.producers.ListProducer;

/**
 * This view params is for navigating to the details view of the assignment. This
 * may be accessed via a helper or other screens within the tool, so you can
 * optionally pass the fromView param to render navigation
 *
 */
public class AssignmentDetailsViewParams extends SimpleAssignmentViewParams {

    private static final Log LOG = LogFactory.getLog(AssignmentDetailsViewParams.class);

    /**
     * The viewId of the producer you are navigating from. For example, if
     * {@link ListProducer#VIEW_ID}, will render the breadcrumbs and "return to list"
     * button for the list screen. Will not render any navigation if not set
     */
    public String fromView;

    public AssignmentDetailsViewParams() {}

    public AssignmentDetailsViewParams(String viewId, Long assignmentId, String fromView){
        super(viewId, assignmentId);
        this.fromView = fromView;
    }
    
    public AssignmentDetailsViewParams(String viewId, Long assignmentId, String fromView, String tagReference, String tagDecoWrapper){
        super(viewId, assignmentId, tagReference, tagDecoWrapper);
        this.fromView = fromView;
    }

    public String getParseSpec() {
        return super.getParseSpec() + ",fromView";
    }
}