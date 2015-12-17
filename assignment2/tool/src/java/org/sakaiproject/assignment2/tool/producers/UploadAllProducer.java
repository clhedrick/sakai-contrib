/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/UploadAllProducer.java $
 * $Id: UploadAllProducer.java 87495 2015-04-06 15:12:29Z hedrick@rutgers.edu $
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

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.tool.params.AssignmentViewParams;
import org.sakaiproject.assignment2.tool.params.ViewSubmissionsViewParams;
import org.sakaiproject.assignment2.tool.params.ZipViewParams;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.components.UISelectLabel;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * This view shows the form for uploading grades.
 * 
 * One thing to note is the implementation of both {@link NavigationCaseReporter}
 * and {@link ActionResultInterceptor}. During runtime processing, RSF will try
 * to use the Navigation Cases first, and then try Action Result Interceptors.
 * 
 * TODO FIXME I'm pretty sure we can get rid of the NavigationCaseReporter, but
 * can't get a CSV file to upload so I can't test it.  Come back here later. SWG
 * 
 * @author sgithens
 * @author wagnermr
 * @author stuart.freeman
 * @author carl.hall
 *
 */
public class UploadAllProducer implements ViewComponentProducer, ViewParamsReporter,
NavigationCaseReporter, ActionResultInterceptor
{
    public static final String VIEW_ID = "uploadall";

    public String getViewID()
    {
        return VIEW_ID;
    }

    // Dependency
    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    // Dependency
    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }
    
    // Dependency
    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }

    public void fillComponents(UIContainer tofill, ViewParameters viewparams,
            ComponentChecker checker)
    {
        AssignmentViewParams params = (AssignmentViewParams) viewparams;

        Assignment2 assignment = assignmentLogic.getAssignmentByIdWithAssociatedData(params.assignmentId);

        // Make BreadCrumbs
        UIInternalLink.make(tofill, "breadcrumb_asnn_list", 
                messageLocator.getMessage("assignment2.list.heading"),
                new SimpleViewParameters(ListProducer.VIEW_ID));
        UIInternalLink.make(tofill, "breadcrumb_asnn_submissions", 
                messageLocator.getMessage("assignment2.uploadall.breadcrumb.back_to_submissions", assignment.getTitle() )
                , new ViewSubmissionsViewParams(ViewSubmissionsProducer.VIEW_ID, assignment.getId()));
        if (assignment.isGraded() && assignment.getGradebookItemId() != null) {
            UIOutput.make(tofill, "breadcrumb_title", messageLocator.getMessage("assignment2.uploadall.breadcrumb.upload.graded"));

            UIOutput.make(tofill, "upload_title", messageLocator.getMessage("assignment2.uploadall.title.graded"));
            UIOutput.make(tofill, "uploadInstructions", messageLocator.getMessage("assignment2.uploadall.instructions.graded"));
        } else {
            UIOutput.make(tofill, "breadcrumb_title", messageLocator.getMessage("assignment2.uploadall.breadcrumb.upload.ungraded"));

            UIOutput.make(tofill, "upload_title", messageLocator.getMessage("assignment2.uploadall.title.ungraded"));
            UIOutput.make(tofill, "uploadInstructions", messageLocator.getMessage("assignment2.uploadall.instructions.ungraded"));
        }    

        ZipViewParams zvp = new ZipViewParams("zipSubmissions", params.assignmentId);
        UIForm upload_form = UIForm.make(tofill, "upload_form");
        upload_form.addParameter(new UIELBinding("UploadBean.uploadOptions.assignmentId", zvp.assignmentId));
	Object sessionToken = org.sakaiproject.tool.cover.SessionManager.getCurrentSession().getAttribute("sakai.csrf.token");
	if (sessionToken != null)
	    UIInput.make(upload_form, "csrf", "UploadBean.csrfToken", sessionToken.toString());


        // Release feedback radio buttons
        String [] release_fb_values = new String[] {
                Boolean.FALSE.toString(), Boolean.TRUE.toString()
        };
        String [] release_fb_labels = new String[] {
                "assignment2.uploadall.release.feedback.no",
                "assignment2.uploadall.release.feedback.yes"
        };
        UISelect feedback_select = UISelect.make(upload_form, "release_feedback_select", 
                release_fb_values, release_fb_labels, "UploadBean.uploadOptions.releaseFeedback", Boolean.FALSE.toString()).setMessageKeys();
        String release_feedback_id = feedback_select.getFullID();

        for (int i=0; i < release_fb_values.length; i++) {
            UIBranchContainer release_fb_row = UIBranchContainer.make(upload_form, "release_feedback:");
            UISelectChoice.make(release_fb_row, "release_option", release_feedback_id, i);
            UISelectLabel.make(release_fb_row, "release_label", release_feedback_id, i);
        }

        // Release grades radio buttons
        // do not display this section unless this user has permission to release
        // grades in the gradebook
        if (assignment.isGraded() && 
                assignment.getGradebookItemId() != null && 
                gradebookLogic.isCurrentUserAbleToEdit(assignment.getContextId()) &&
                gradebookLogic.gradebookItemExists(assignment.getGradebookItemId())) {
            UIOutput.make(tofill, "release_grades_section");
            String [] release_grades_values = new String[] {
                    Boolean.FALSE.toString(), Boolean.TRUE.toString()
            };
            String [] release_grades_labels = new String[] {
                    "assignment2.uploadall.release.grades.no",
                    "assignment2.uploadall.release.grades.yes"
            };

            UISelect grades_select = UISelect.make(upload_form, "release_grades_select", 
                    release_grades_values, release_grades_labels, "UploadBean.uploadOptions.releaseGrades", Boolean.FALSE.toString()).setMessageKeys();
            String release_grades_id = grades_select.getFullID();

            for (int i=0; i < release_grades_values.length; i++) {
                UIBranchContainer release_grades_row = UIBranchContainer.make(upload_form, "release_grades:");
                UISelectChoice.make(release_grades_row, "release_grades_option", release_grades_id, i);
                UISelectLabel.make(release_grades_row, "release_grades_label", release_grades_id, i);
            }
        }

        // Render buttons
        UICommand.make(upload_form, "uploadButton", UIMessage.make("assignment2.uploadall.upload"),
        "UploadBean.processUpload");
        UICommand.make(upload_form, "cancelButton", UIMessage.make("assignment2.uploadall.cancel"))
        .setReturn(ViewSubmissionsProducer.VIEW_ID);
    }

    public ViewParameters getViewParameters()
    {
        return new AssignmentViewParams();
    }

    public List<NavigationCase> reportNavigationCases()
    {
        List<NavigationCase> nav = new ArrayList<NavigationCase>();
        nav.add(new NavigationCase(ViewSubmissionsProducer.VIEW_ID, new ViewSubmissionsViewParams(
                ViewSubmissionsProducer.VIEW_ID, null)));
        return nav;
    }

    public void interceptActionResult(ARIResult result, ViewParameters incoming, Object actionReturn)
    {
        if (result.resultingView instanceof ViewSubmissionsViewParams)
        {
            ViewSubmissionsViewParams outgoing = (ViewSubmissionsViewParams) result.resultingView;
            AssignmentViewParams in = (AssignmentViewParams) incoming;
            outgoing.assignmentId = in.assignmentId;
        }
    }
}