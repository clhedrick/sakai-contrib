/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/fragments/FragmentSubmissionGradePreviewProducer.java $
 * $Id: FragmentSubmissionGradePreviewProducer.java 65914 2010-01-28 13:23:54Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.tool.producers.fragments;

import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.params.AssignmentViewParams;
import org.sakaiproject.assignment2.tool.producers.renderers.AttachmentListRenderer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.content.ContentTypeReporter;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * TODO FIXME This Preview Functionality is currently in flux.  A number of 
 * things are being removed as part of ASNN-295 
 * 
 * @author sgithens
 *
 */
public class FragmentSubmissionGradePreviewProducer implements ViewComponentProducer, ViewParamsReporter, ContentTypeReporter {

    public static final String VIEW_ID = "fragment-submission-grade_preview";
    public String getViewID() {
        return VIEW_ID;
    }

    private ExternalLogic externalLogic;
    private AssignmentSubmissionLogic submissionLogic;
    private AttachmentListRenderer attachmentListRenderer;
    private MessageLocator messageLocator;

    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
        AssignmentViewParams params = (AssignmentViewParams) viewparams;

        UIMessage.make(tofill, "page-title", "assignment2.assignment_preview.title");

        // TODO FIXME ASNN-295 in progress
        AssignmentSubmission as = null; //previewAssignmentSubmissionBean.getAssignmentSubmission();
        AssignmentSubmissionVersion asv = null; // = previewAssignmentSubmissionBean.getAssignmentSubmissionVersion();
        Assignment2 assignment = as.getAssignment();

        String title = (assignment != null) ? assignment.getTitle() : "";
        UIMessage.make(tofill, "preview_heading", "assignment2.fragment-submission-grade_preview.heading", new Object[]{ title });
        //Free from memory - if that does what I think it will do :-\
        //previewAssignmentSubmissionBean.setAssignmentSubmission(null);

        UIOutput.make(tofill, "student", externalLogic.getUserDisplayName(as.getUserId()));

        // set the textual representation of the submission status
        String status = "";
        int statusConstant = AssignmentConstants.SUBMISSION_NOT_STARTED;
        if (as != null) {
            statusConstant = submissionLogic.getSubmissionStatusForVersion(
                    as.getCurrentSubmissionVersion(), assignment.getDueDate(), as.getResubmitCloseDate());
            status = messageLocator.getMessage(
                    "assignment2.assignment_grade-assignment.submission_status." + 
                    statusConstant);
        }
        UIOutput.make(tofill, "status", status);

        String instructions = (assignment != null) ? assignment.getInstructions() : "";
        UIVerbatim.make(tofill, "instructions", instructions);

        attachmentListRenderer.makeAttachmentFromAssignmentAttachmentSet(tofill, "attachment_list:", params.viewID, assignment.getAttachmentSet());

        UIVerbatim.make(tofill, "feedbackText", asv.getAnnotatedTextFormatted());
        UIVerbatim.make(tofill, "feedback_notes", asv.getFeedbackNotes());
    }

    public ViewParameters getViewParameters() {
        return new AssignmentViewParams();
    }

    public String getContentType() {
        return ContentTypeInfoRegistry.HTML_FRAGMENT;
    }

    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public void setAttachmentListRenderer(AttachmentListRenderer attachmentListRenderer){
        this.attachmentListRenderer = attachmentListRenderer;
    }


    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic)
    {
        this.submissionLogic = submissionLogic;
    }
}