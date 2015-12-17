/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/RedirectToAssignmentProducer.java $
 * $Id: RedirectToAssignmentProducer.java 61484 2009-06-29 19:01:16Z swgithen@mtu.edu $
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

import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.tool.params.SimpleAssignmentViewParams;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * Used to create generic link back to the assignments tool that is "smart" enough
 * to determine which page the user should see based upon their permissions. ie The
 * same link can be used by students and instructors to view an assignment linked
 * from another tool
 * It is then intercepted and converted to the correct viewParams
 * @author michellewagner
 *
 */
public class RedirectToAssignmentProducer implements ViewComponentProducer, ViewParamsReporter
{
    public static final String VIEWID = AssignmentLogic.REDIRECT_ASSIGNMENT_VIEW_ID;

    public String getViewID()
    {
        return VIEWID;
    }

    public void fillComponents(UIContainer tofill, ViewParameters viewparams,
            ComponentChecker checker)
    {
        SimpleAssignmentViewParams params = (SimpleAssignmentViewParams) viewparams;

    }

    public ViewParameters getViewParameters()
    {
        return new SimpleAssignmentViewParams();
    }

}
