/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/TaggableHelperProducer.java $
 * $Id: TaggableHelperProducer.java 87902 2015-12-14 18:01:30Z hedrick@rutgers.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2008 The Sakai Foundation.
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

import org.sakaiproject.assignment2.tool.params.TaggableHelperViewParams;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

import org.sakaiproject.rsf.helper.HelperViewParameters;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class TaggableHelperProducer implements ViewComponentProducer, ViewParamsReporter
{
    public static final String VIEWID = "TaggableHelperProducer";

    private SessionManager sessionManager;

    public String getViewID()
    {
        return VIEWID;
    }

    public void fillComponents(UIContainer tofill, ViewParameters viewparams,
            ComponentChecker checker)
    {
        TaggableHelperViewParams params = (TaggableHelperViewParams)viewparams;

        ToolSession toolSession = sessionManager.getCurrentToolSession();
        if (params.keys != null) {
            for (int i=0; i<params.keys.length; i++) {
                toolSession.setAttribute(params.keys[i], params.values[i]);
            }
        }

        UIOutput.make(tofill, HelperViewParameters.HELPER_ID, params.helperId);

        //UICommand help = UICommand.make(tofill, HelperViewParameters.POST_HELPER_BINDING, "", null);
        //help.parameters = new ParameterList();
        //for (int i=0; i<params.keys.length; i++) {
        //	help.parameters.add(new UIParameter(params.keys[0], params.values[0]));
        //}
    }

    public ViewParameters getViewParameters()
    {
        return new TaggableHelperViewParams();
    }

    public void setSessionManager(SessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
    }

}
