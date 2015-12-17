/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/StudentSubmitProducer.java $
 * $Id: StudentSubmitProducer.java 70486 2010-09-30 16:41:46Z swgithen@mtu.edu $
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

import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.tool.params.StudentSubmissionParams;
import org.sakaiproject.assignment2.tool.producers.renderers.StudentViewAssignmentRenderer;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class StudentSubmitProducer implements ViewComponentProducer,  ViewParamsReporter {

    public static final String VIEW_ID = "student-submit";
    public String getViewID() {
        return VIEW_ID;
    }

    String reqStar = "<span class=\"reqStar\">*</span>";

    private ExternalLogic externalLogic;
    private AssignmentSubmissionLogic submissionLogic;
    private EntityBeanLocator assignment2BeanLocator;
    private EntityBeanLocator assignmentSubmissionBeanLocator;
    private StudentViewAssignmentRenderer studentViewAssignmentRenderer;

    @SuppressWarnings("unchecked")
    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
        StudentSubmissionParams params = (StudentSubmissionParams) viewparams;

        Long assignmentId = params.assignmentId;
        Assignment2 assignment = (Assignment2) assignment2BeanLocator.locateBean(assignmentId.toString());

        AssignmentSubmission submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(assignmentId, externalLogic.getCurrentUserId(), null);

        String ASOTPKey = "";
        if (submission == null || submission.getId() == null || submission.getCurrentSubmissionVersion() == null) {
            ASOTPKey += EntityBeanLocator.NEW_PREFIX + "1";
        } else {
            ASOTPKey += submission.getId();
        }

        //Now do submission stuff
        AssignmentSubmission assignmentSubmission = (AssignmentSubmission) assignmentSubmissionBeanLocator.locateBean(ASOTPKey); 

        studentViewAssignmentRenderer.makeStudentView(tofill, "portletBody:", assignmentSubmission, assignment, params, ASOTPKey, Boolean.FALSE, params.previewsubmission, params.resubmit); 


        /* TODO FIXME Marking feedback as viewed. 
         * For now we are doing this here. Eventually this is suppose to be
         * Ajaxy and on a version by version basis. For now, marking them all
         * in bulk when viewing a submission, in order to finish the student
         * assignment list landing page.
         */
        /*
        Set<AssignmentSubmissionVersion>versions = assignmentSubmission.getSubmissionHistorySet();
        List<Long>versionIds = new ArrayList<Long>();
        for (AssignmentSubmissionVersion version: versions) {
            versionIds.add(version.getId());
        }
        submissionLogic.markFeedbackAsViewed(assignmentSubmission.getId(), versionIds);
         */

        //Initialize js otpkey
        UIVerbatim.make(tofill, "attachment-ajax-init", "otpkey=\"" + org.sakaiproject.util.Web.escapeUrl(ASOTPKey) + "\";\n");

    }

    public ViewParameters getViewParameters() {
        return new StudentSubmissionParams();
    }

    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public void setAssignment2EntityBeanLocator(EntityBeanLocator entityBeanLocator) {
        this.assignment2BeanLocator = entityBeanLocator;
    }

    public void setAssignmentSubmissionBeanLocator(EntityBeanLocator entityBeanLocator) {
        this.assignmentSubmissionBeanLocator = entityBeanLocator;
    }

    public void setAssignmentSubmissionEntityBeanLocator(EntityBeanLocator entityBeanLocator) {
        this.assignmentSubmissionBeanLocator = entityBeanLocator;
    }

    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }

    public void setStudentViewAssignmentRenderer(
            StudentViewAssignmentRenderer studentViewAssignmentRenderer) {
        this.studentViewAssignmentRenderer = studentViewAssignmentRenderer;
    }

}
