/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/AssignmentGradeReportProducer.java $
 * $Id: AssignmentGradeReportProducer.java 64148 2009-10-20 19:03:46Z swgithen@mtu.edu $
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

import org.sakaiproject.assignment2.tool.params.PagerViewParams;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class AssignmentGradeReportProducer implements ViewComponentProducer, ViewParamsReporter {

    public static final String VIEW_ID = "assignment_grade-report";
    public String getViewID() {
        return VIEW_ID;
    }

    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
        PagerViewParams pagerparams = (PagerViewParams) viewparams;

        Integer total_count = 0;

        UIMessage.make(tofill, "page-title", "assignment2.assignment_grade-report.title");
        //pagerRenderer.makePager(tofill, "pagerDiv:", VIEW_ID, pagerparams, total_count);
        UIMessage.make(tofill, "heading", "assignment2.assignment_grade-report.heading");

    }

    public ViewParameters getViewParameters(){
        return new PagerViewParams();
    }

}