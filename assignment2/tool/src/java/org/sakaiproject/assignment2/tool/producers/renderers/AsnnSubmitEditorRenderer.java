package org.sakaiproject.assignment2.tool.producers.renderers;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.LocalTurnitinLogic;
import org.sakaiproject.assignment2.tool.beans.StudentSubmissionVersionFlowBean;
import org.sakaiproject.assignment2.tool.params.FilePickerHelperViewParams;
import org.sakaiproject.assignment2.tool.producers.AddAttachmentHelperProducer;
import org.sakaiproject.assignment2.tool.producers.evolvers.AttachmentInputEvolver;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInputMany;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.producers.BasicProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * Renders the area of the Student Submit pages where the student does the 
 * actual work of putting in the submission text and uploading any attachments
 * that are part of the submission.
 * 
 * This does have to detect the type of assignment: Non-electronic, text,
 * text and attachments, attachments only.  In the future this may be 
 * modularized to allow new pluggable assignment types.
 * 
 * In the non-electronic or non-submission assignment type, this will really
 * just be a check box that says you've completed it and a "Save and Return"
 * button.
 * 
 * @author sgithens
 *
 */
public class AsnnSubmitEditorRenderer implements BasicProducer {

    // Dependency
    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    // Dependency
    private TextInputEvolver richTextEvolver;
    public void setRichTextEvolver(TextInputEvolver richTextEvolver) {
        this.richTextEvolver = richTextEvolver;
    }

    // Dependency
    private AttachmentInputEvolver attachmentInputEvolver;
    public void setAttachmentInputEvolver(AttachmentInputEvolver attachmentInputEvolver){
        this.attachmentInputEvolver = attachmentInputEvolver;
    }
    
    // Dependency
    private AsnnInstructionsRenderer asnnInstructionsRenderer;
    public void setAsnnInstructionsRenderer(AsnnInstructionsRenderer asnnInstructionsRenderer) {
        this.asnnInstructionsRenderer = asnnInstructionsRenderer;
    }
    
    // Dependency
    private AsnnToggleRenderer toggleRenderer;
    public void setAsnnToggleRenderer(AsnnToggleRenderer toggleRenderer) {
        this.toggleRenderer = toggleRenderer;
    }
    
    private AsnnTagsRenderer tagsRenderer;
    public void setAsnnTagsRenderer(AsnnTagsRenderer tagsRenderer) {
        this.tagsRenderer = tagsRenderer;
    }

    // Dependency
    private ViewParameters viewParameters;
    public void setViewParameters(ViewParameters viewParameters) {
        this.viewParameters = viewParameters;
    }
    
    // Dependency
    private LocalTurnitinLogic localTurnitinLogic;
    public void setLocalTurnitinLogic(LocalTurnitinLogic localTurnitinLogic) {
        this.localTurnitinLogic = localTurnitinLogic;
    }

    // Dependency
    private AssignmentSubmissionLogic submissionLogic;
    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }
    
    private ExternalContentReviewLogic contentReviewLogic;
    public void setExternalContentReviewLogic(ExternalContentReviewLogic contentReviewLogic) {
        this.contentReviewLogic = contentReviewLogic;
    }

    // Flow Scope Bean for Student Submission
    private StudentSubmissionVersionFlowBean studentSubmissionVersionFlowBean;
    public void setStudentSubmissionVersionFlowBean(StudentSubmissionVersionFlowBean studentSubmissionVersionFlowBean) {
        this.studentSubmissionVersionFlowBean = studentSubmissionVersionFlowBean;
    }
    
    private AssignmentPermissionLogic permissionLogic;
    public void setPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    /**
     * Renders the actual editing area for the Assignment Submission.  The 
     * beginning of the method signature is fairly self explanatory, but gets
     * a bit cabberwonky near the end there.  This is something that we might
     * want to rethink in the future ( or just leave if everything is going to
     * be rewritten in JavaScript anyways ).
     * 
     * The first boolean, preview, indicates (if true), that this is view is 
     * actually an Instructor who is authoring the assignment, and just wants to
     * see what it would look like if the student was completing it.
     * 
     * The second boolean, studentPreviewSubmission, indicates (if true), that 
     * this is a Student actually completing an assignment, but are previewing
     * their work before submitting (or editing some more).
     *  
     *  
     * @param parent
     * @param clientID
     * @param assignmentSubmission
     * @param preview
     * @param resubmit true if this is a resubmit scenario and that section should be highlighted
     */
    public void fillComponents(UIContainer parent, String clientID, AssignmentSubmission assignmentSubmission, 
            boolean preview, boolean studentPreviewSubmission, boolean resubmit) {

        Assignment2 assignment = assignmentSubmission.getAssignment();

        UIJointContainer joint = new UIJointContainer(parent, clientID, "asnn2-submit-editor-widget:");
        String asOTP = "AssignmentSubmission.";
        String asOTPKey = "";
        if (assignmentSubmission != null && assignmentSubmission.getId() != null) {
            asOTPKey += assignmentSubmission.getId();
        } else {
            asOTPKey += EntityBeanLocator.NEW_PREFIX + "1";
        }
        asOTP = asOTP + asOTPKey;

        String asvOTP = null;
        if (!preview) {
            asvOTP = "StudentSubmissionVersionFlowBean.";
        }
        else {
            asvOTP = "AssignmentSubmissionVersion.";
        }
        String asvOTPKey = "";
        if (assignmentSubmission != null && assignmentSubmission.getCurrentSubmissionVersion() != null 
                && assignmentSubmission.getCurrentSubmissionVersion().isDraft()) {
            asvOTPKey += assignmentSubmission.getCurrentSubmissionVersion().getId();
        } else {
            asvOTPKey += EntityBeanLocator.NEW_PREFIX + "1";
        }
        asvOTP = asvOTP + asvOTPKey;

        //For preview, get a decorated list of disabled="disabled"
        Map<String, String> disabledAttr = new HashMap<String, String>();
        disabledAttr.put("disabled", "disabled");
        DecoratorList disabledDecoratorList = new DecoratorList(new UIFreeAttributeDecorator(disabledAttr));

        UIForm form = UIForm.make(joint, "form");
	Object sessionToken = org.sakaiproject.tool.cover.SessionManager.getCurrentSession().getAttribute("sakai.csrf.token");
	if (sessionToken != null)
	    UIInput.make(form, "csrf", "StudentSubmissionBean.csrfToken", sessionToken.toString());

        // Fill in with submission type specific instructions
        // If this is a Student Preview, we dont' want these instruction headers
        // per the design spec.
        if (!studentPreviewSubmission) {
            UIOutput.make(form, "submission_instructions", messageLocator.getMessage("assignment2.student-submit.instructions." + assignment.getSubmissionType())); 
        }

        if (assignment.isHonorPledge()) {
            UIVerbatim.make(form, "required", messageLocator.getMessage("assignment2.student-submit.required"));
        }
        
        UIOutput editSubmissionContainer = UIOutput.make(form, "edit_submission");
        
        boolean displayResubmitToggle = false;
        boolean resubmitToggleExpanded = resubmit;
        
        // if this is a resubmission and not a preview view, display a resubmission toggle
        if (!studentPreviewSubmission && !preview) {
            if (submissionLogic.getNumSubmittedVersions(assignmentSubmission.getUserId(), assignment.getId()) > 0) {
                // render the resubmit toggle
                displayResubmitToggle = true;
                
                String heading = messageLocator.getMessage("assignment2.student-submit.toggle.resubmit");
                String hoverText = messageLocator.getMessage("assignment2.student-submit.hover.resubmit");
                
                toggleRenderer.makeToggle(joint, "resubmit_toggle:", "resubmit_toggle", true, heading, hoverText, resubmitToggleExpanded, false, false, false, null);
                
                // since we are rendering the toggle, let's identify the submission editing section
                // with css classes
                editSubmissionContainer.decorate(new UIFreeAttributeDecorator("class", "subsection1 toggleSubsection"));
                
                if (!resubmitToggleExpanded) {
                    editSubmissionContainer.decorate(new UIFreeAttributeDecorator("style", "display: none;"));
                }
            }
        }
        
        // display instructions if it isn't the student preview. they do display if student is editing
        // or instructor is previewing
        if (!studentPreviewSubmission) {
            // for model answer
            Map<String, Object> optionalParamMap = new HashMap<String, Object>();
            optionalParamMap.put(AssignmentConstants.MODEL_ANSWER_IS_INSTRUCTOR, permissionLogic.isUserAllowedToTakeInstructorAction(null, assignment.getContextId()));
            optionalParamMap.put(AssignmentConstants.MODEL_ANSWER_IS_PREVIEW, preview);
            optionalParamMap.put(AssignmentConstants.ASSIGNMENT_SUBMISSION, assignmentSubmission);
            
            // render the instructions
            asnnInstructionsRenderer.makeInstructions(joint, "assignment-instructions-edit:", assignment, false, false, false, optionalParamMap);
            
            // render the assignment tags
            tagsRenderer.makeTagInformation(joint, "tagging-info-edit:", assignment, false, false, false);
        }

        // Because the flow might not be starting on the initial view, the
        // studentSubmissionPreviewVersion should always use the flow bean 
        // unless it is null.
        AssignmentSubmissionVersion studentSubmissionPreviewVersion = 
            (AssignmentSubmissionVersion) studentSubmissionVersionFlowBean.locateBean(asvOTPKey);

        //Rich Text Input
        if (assignment.getSubmissionType() == AssignmentConstants.SUBMIT_INLINE_ONLY || 
                assignment.getSubmissionType() == AssignmentConstants.SUBMIT_INLINE_AND_ATTACH){

            UIOutput.make(form, "submit_text");
            
            if (studentPreviewSubmission) {
                UIMessage.make(form, "submit_text_label", "assignment2.student-submit.submission_text.submitted");
            } else {
                UIMessage.make(form, "submit_text_label", "assignment2.student-submit.submission_text");
            }

            if (studentPreviewSubmission) {
                // TODO FIXME This is being duplicated
                UIVerbatim make = UIVerbatim.make(form, "text:", studentSubmissionPreviewVersion.getSubmittedText());
            }
            else if (!preview) {
                UIInput text = UIInput.make(form, "text:", asvOTP + ".submittedText");
                // if this is a resubmission, load the user's most recent submission in the text
                if (resubmit)
                {
                    AssignmentSubmissionVersion previousVersion = assignmentSubmission.getCurrentSubmissionVersion();
                    text.setValue(previousVersion.getSubmittedText() == null ? "" : previousVersion.getSubmittedText());
                }
                text.mustapply = Boolean.TRUE;
                richTextEvolver.evolveTextInput(text);
            } 
            else {
                //disable textarea
                UIInput text = UIInput.make(form, "text:", asvOTP + ".submittedText");
                UIInput text_disabled = UIInput.make(form, "text_disabled",asvOTP + ".submittedText");
                text_disabled.decorators = disabledDecoratorList;
            }


        }

        // Attachment Stuff
        // the editor will only display attachments for the current version if
        // it is a draft or a resubmission. otherwise, the user is working on a new submission
        UIOutput attachSection = null;
        if (assignment.getSubmissionType() == AssignmentConstants.SUBMIT_ATTACH_ONLY ||
                assignment.getSubmissionType() == AssignmentConstants.SUBMIT_INLINE_AND_ATTACH){
            attachSection = UIOutput.make(form, "submit_attachments");

            if (resubmit)
            {
                AssignmentSubmissionVersion previousVersion = assignmentSubmission.getCurrentSubmissionVersion();
                String[] attachmentRefs = 
                    previousVersion.getSubmittedAttachmentRefs();

                renderSubmittedAttachments(false, asvOTP,
                        asvOTPKey, form, attachmentRefs);
            }
            else if (studentPreviewSubmission || !preview) {
                String[] attachmentRefs = 
                    studentSubmissionPreviewVersion.getSubmittedAttachmentRefs();

                renderSubmittedAttachments(studentPreviewSubmission, asvOTP,
                        asvOTPKey, form, attachmentRefs);
            }
        }

        // attachment only situations will not return any values in the OTP map; thus,
        // we won't enter the processing loop in processActionSubmit (and nothing will be saved)
        // this will force rsf to bind the otp mapping
        if (assignment.getSubmissionType() == AssignmentConstants.SUBMIT_ATTACH_ONLY) {
            UIInput.make(form, "submitted_text_for_attach_only", asvOTP + ".submittedText");
        }

        if (assignment.isHonorPledge()) {
            UIOutput.make(joint, "honor_pledge_fieldset");
            UIBoundBoolean honorPledge = null;
            
            if (preview || studentPreviewSubmission)
            {
            	honorPledge = UIBoundBoolean.make(form, "honor_pledge", studentSubmissionPreviewVersion.getHonorPledge());
                honorPledge.decorate(new UIFreeAttributeDecorator("disabled","true"));
            }
            else {
                honorPledge = UIBoundBoolean.make(form, "honor_pledge", asvOTP + ".honorPledge");
            }
            
            if (resubmit)
            {
              AssignmentSubmissionVersion previousVersion = assignmentSubmission.getCurrentSubmissionVersion();
              honorPledge.setValue(previousVersion.getHonorPledge() == null ? Boolean.FALSE : previousVersion.getHonorPledge());
            }

            UIVerbatim.make(form, "honor_pledge_text", UIMessage.make("assignment2.student-submit.honor_pledge_text"));;
        }
        
        // display plagiarism check warning
        if (assignment.isContentReviewEnabled() && contentReviewLogic.isContentReviewAvailable(assignment.getContextId())) {
            if (attachSection != null) {
                attachSection.decorate(new UIFreeAttributeDecorator("class", "messageConfirmation"));
            }
            
            if (assignment.isContentReviewStudentViewReport()) {
                UIMessage.make(joint, "plagiarism_check", "assignment2.turnitin.submit.warning.inst_and_student", new Object[]{contentReviewLogic.getServiceName()});
            } else {
                UIMessage.make(joint, "plagiarism_check", "assignment2.turnitin.submit.warning.inst_only", new Object[]{contentReviewLogic.getServiceName()});
            }
            
            String fileRequirementsUrl = localTurnitinLogic.getSupportedFormatsUrl();
            if (fileRequirementsUrl != null) {
                UIOutput.make(joint, "plagiarism_file_req_section");
                UILink.make(joint, "plagiarism_file_req", messageLocator.getMessage("assignment2.turnitin.file_requirements"), fileRequirementsUrl);
            }
        }

        form.parameters.add( new UIELBinding("StudentSubmissionBean.ASOTPKey", asOTPKey));
        form.parameters.add( new UIELBinding("StudentSubmissionBean.assignmentId", assignment.getId()));

        // this section contains the honor pledge and the submit buttons. if this
        // is a resubmit scenario, we are going to hide these options initially. if preview,
        // we just hide the buttons within this section
        UIOutput submissionSection = UIOutput.make(form, "submission_info");

        if (preview) {
            // don't display the buttons at all for instructor preview
        } else {
            UIOutput.make(form, "submit_section");
            
            UICommand.make(form, "submit_button", UIMessage.make("assignment2.student-submit.submit"), 
            "StudentSubmissionBean.processActionSubmit");
            UICommand.make(form, "save_draft_button", UIMessage.make("assignment2.student-submit.save_draft"), 
            "StudentSubmissionBean.processActionSaveDraft");
            
            if (studentPreviewSubmission) {
                UICommand.make(form, "back_to_edit_button", UIMessage.make("assignment2.student-submit.back_to_edit"),
                "StudentSubmissionBean.processActionBackToEdit");
            } else {
                UICommand.make(form, "preview_button", UIMessage.make("assignment2.student-submit.preview"), 
                "StudentSubmissionBean.processActionPreview");

                UICommand.make(form, "cancel_button", UIMessage.make("assignment2.student-submit.cancel"), 
                "StudentSubmissionBean.processActionCancel");
            }
            
            if (displayResubmitToggle && !resubmitToggleExpanded) {
                // render the "Return to List" button. this only appears if this is a resubmission
                UIOutput.make(form, "view_only_section");
                UICommand.make(form, "return_to_list_button", UIMessage.make("assignment2.student-submit.return_to_list"), 
                        "StudentSubmissionBean.processActionCancel");
                
                // since this is a resubmit scenario, we hide the honor pledge and
                // submission buttons until they expand the resubmit toggle
                submissionSection.decorate(new UIFreeAttributeDecorator("style", "display: none;"));
            }
        }
    }

    /**
     * @param studentPreviewSubmission
     * @param asvOTP
     * @param asvOTPKey
     * @param form
     * @param attachmentRefs
     */
    private void renderSubmittedAttachments(boolean studentPreviewSubmission,
            String asvOTP, String asvOTPKey, UIForm form,
            String[] attachmentRefs) {
        UIInputMany attachmentInput = UIInputMany.make(form, "attachment_list:", asvOTP + ".submittedAttachmentRefs", 
                attachmentRefs);
        attachmentInputEvolver.evolveAttachment(attachmentInput, !studentPreviewSubmission, null);

        if (!studentPreviewSubmission) {
            UIInternalLink addAttachLink = UIInternalLink.make(form, "add_submission_attachments", UIMessage.make("assignment2.student-submit.add_attachments"),
                    new FilePickerHelperViewParams(AddAttachmentHelperProducer.VIEWID, Boolean.TRUE, 
                            Boolean.TRUE, 500, 700, asvOTPKey, true));
            addAttachLink.decorate(new UIFreeAttributeDecorator("onclick", attachmentInputEvolver.getOnclickMarkupForAddAttachmentEvent(null)));
        }

        UIOutput noAttach = UIOutput.make(form, "no_attachments_yet", messageLocator.getMessage("assignment2.student-submit.no_attachments"));
        if (attachmentRefs != null && attachmentRefs.length > 0) {
            noAttach.decorate(new UIFreeAttributeDecorator("style", "display: none;"));
        }
    }

    public void fillComponents(UIContainer parent, String clientID) {

    }

}
