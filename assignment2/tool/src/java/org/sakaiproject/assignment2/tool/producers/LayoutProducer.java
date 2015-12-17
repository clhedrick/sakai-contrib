/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/LayoutProducer.java $
 * $Id: LayoutProducer.java 67549 2010-05-06 15:23:23Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.tool.producers;

import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.tool.api.Placement;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.producers.NullaryProducer;
import uk.org.ponder.rsf.view.ViewGroup;
import uk.org.ponder.rsf.view.support.ViewGroupResolver;
import uk.org.ponder.rsf.viewstate.ViewParameters;


/**
 * This inherited producer forms the shell for all the rest of
 * the producers in Assignments 2, so they merely need to render
 * the body. At the moment this is where you need to specify extra
 * javascript files that certain producers need to include.
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class LayoutProducer implements NullaryProducer { 
    private NullaryProducer pageproducer;
    private ViewGroupResolver viewGroupResolver;
    private ViewParameters viewParameters;
    private ViewGroup group;
    private ExternalLogic externalLogic;

    public void fillComponents(UIContainer tofill) {

        if (!viewGroupResolver.isMatch(group, viewParameters)){
            pageproducer.fillComponents(tofill);
        } else {
            UIJointContainer page = new UIJointContainer(tofill, "page-replace:", "page:");

            Placement placement = org.sakaiproject.tool.cover.ToolManager.getCurrentPlacement();
            if (placement != null) {
                //Initialize iframeId var -- for a few pages that need it still :-(
                String frameId = org.sakaiproject.util.Web.escapeJavascript("Main" + placement.getId());
                UIVerbatim.make(tofill, "iframeId_init", "var iframeId = \"" + frameId + "\";");
                
                UIVerbatim.make(tofill, "sakai-location-decl-js", "var sakai = sakai || {};"
                        + "sakai.curPlacement = '"+placement.getId()+"';"
                        + "sakai.curContext = '"+externalLogic.getCurrentContextId()+"';");
            }
            

            if (viewParameters.viewID.equals(ListProducer.VIEW_ID)){
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/list.js");
                UILink.make(tofill, "asnn-css-include:","/sakai-assignment2-tool/content/css/list.css");
            }
            
            if (viewParameters.viewID.equals(ReorderStudentViewProducer.VIEW_ID)) {
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/list.js");
                UILink.make(tofill, "asnn-css-include:","/sakai-assignment2-tool/content/css/list.css");
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/reorder-student-view.js");
            }
            else if (viewParameters.viewID.equals(ViewSubmissionsProducer.VIEW_ID)) {
                UILink.make(tofill, "asnn-js-include:", "/sakai-assignment2-tool/content/js/viewSubmissions.js");
            }
            else if (viewParameters.viewID.equals(GradeProducer.VIEW_ID)) {
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/grade.js");
                UILink.make(tofill, "asnn-css-include:","/sakai-assignment2-tool/content/css/grade.css");
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/thickbox.js");
            }
            else if (viewParameters.viewID.equals(StudentSubmitProducer.VIEW_ID)) {
                UILink.make(tofill, "asnn-css-include:","/sakai-assignment2-tool/content/css/student-view.css");
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/thickbox.js");
            }
            else if (AssignmentProducer.VIEW_ID.equals(viewParameters.viewID)) {
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/thickbox.js");
            }else if (StudentAssignmentListProducer.VIEW_ID.equals(viewParameters.viewID)){
            	UILink.make(tofill, "asnn-css-include:","/sakai-assignment2-tool/content/css/student-view.css");
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/student-assignment-list.js");
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/jquery.tablesorter.js");
            }else if(ImportAssignmentsProducer.VIEW_ID.equals(viewParameters.viewID)){
            	UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/import-assignments.js");
            } else if (GraderPermissionsProducer.VIEW_ID.equals(viewParameters.viewID)) {
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/thickbox.js");
            } else if (PreviewAsStudentProducer.VIEW_ID.equals(viewParameters.viewID)) {
                UILink.make(tofill, "asnn-js-include:","/sakai-assignment2-tool/content/js/thickbox.js");
            }

            //include the components from the page body into tag "page-replace:"
            pageproducer.fillComponents(page);
        }
    }

    public void setViewGroupResolver(ViewGroupResolver viewGroupResolver) {
        this.viewGroupResolver = viewGroupResolver;
    }
    
    public void setViewParameters(ViewParameters viewParameters) {
        this.viewParameters = viewParameters;
    }
    
    public void setGroup(ViewGroup group) {
        this.group = group;
    }
    
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    
    public void setPageProducer(NullaryProducer pageproducer) {
        this.pageproducer = pageproducer;
    }

}
