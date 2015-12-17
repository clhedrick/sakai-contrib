package org.sakaiproject.assignment2.tool.beans;

import java.util.Date;

import org.sakaiproject.assignment2.exception.SubmissionClosedException;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ScheduledNotification;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.tool.WorkFlowResult;
import org.sakaiproject.util.FormattedText;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;


public class StudentSubmissionBean {

    // Service Application Scope Dependency
    private AssignmentSubmissionLogic submissionLogic;
    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }

    // Service Application Scope Dependency
    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    // Service Application Scope Dependency
    private ScheduledNotification scheduledNotification;
    public void setScheduledNotification(ScheduledNotification scheduledNotification) {
        this.scheduledNotification = scheduledNotification;
    }

    // Flow Scope Bean for Student Submission
    private StudentSubmissionVersionFlowBean studentSubmissionVersionFlowBean;
    public void setStudentSubmissionVersionFlowBean(StudentSubmissionVersionFlowBean studentSubmissionVersionFlowBean) {
        this.studentSubmissionVersionFlowBean = studentSubmissionVersionFlowBean;
    }

    // Property Binding
    public Long assignmentId;

    // Property Binding
    private Boolean honorPledge;
    public void setHonorPledge(Boolean honorPledge) {
        this.honorPledge = honorPledge;
    }

    // Property Binding
    public String ASOTPKey;

    // Request Scope Dependency
    private TargettedMessageList messages;
    public void setMessages(TargettedMessageList messages) {
        this.messages = messages;
    }

    // Request Scope Dependency (sort of even though it's declared in App Scope)
    private EntityBeanLocator asEntityBeanLocator;
    @SuppressWarnings("unchecked")
    public void setAssignmentSubmissionEntityBeanLocator(EntityBeanLocator entityBeanLocator) {
        this.asEntityBeanLocator = entityBeanLocator;
    }

    private String csrfToken;
    public void setCsrfToken(String data) {
	csrfToken = data;
    }

    /*
     * STUDENT FUNCTIONS
     */
    public WorkFlowResult processActionSubmit(){
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;

        if (assignmentId == null ) {
            return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;
        }

        AssignmentSubmission assignmentSubmission = (AssignmentSubmission) asEntityBeanLocator.locateBean(ASOTPKey);
        Assignment2 assignment = assignmentLogic.getAssignmentById(assignmentId);

        AssignmentSubmissionVersion asv = studentSubmissionVersionFlowBean.getAssignmentSubmissionVersion();
        
        if (!cleanUpSubmission(asv)) {
            return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;
        }
        
        //check whether honor pledge was added if required
//        if (assignment.isHonorPledge() && !(this.honorPledge != null && Boolean.TRUE.equals(honorPledge))) {
      if (assignment.isHonorPledge() && (asv.getHonorPledge() == null || Boolean.FALSE.equals(asv.getHonorPledge()))) {
            messages.addMessage(new TargettedMessage("assignment2.student-submit.error.honor_pledge_required",
                    new Object[] { assignment.getTitle() }, TargettedMessage.SEVERITY_ERROR));
            return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;
      } else if ((assignment.acceptsAttachments() && asv.getSubmissionAttachSet().isEmpty()) &&
              (assignment.acceptsInlineText() && asv.getSubmittedText().isEmpty())) {
          messages.addMessage(new TargettedMessage("assignment2.student-submit.error.empty_submission",
                  new Object[]{assignment.getTitle()}, TargettedMessage.SEVERITY_ERROR));
          return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;
        }else {

            submissionLogic.saveStudentSubmission(assignmentSubmission.getUserId(), assignment, false, 
                    asv.getSubmittedText(), asv.getHonorPledge(), asv.getSubmissionAttachSet(), true);

            // just in case submission closed while the student was working on
            // it, double check that the current submission isn't still
            // draft before we take the "success" actions. if the submission was
            // closed when they hit "submit", the backend saved their submission as draft
            AssignmentSubmission newSubmission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(assignmentId, assignmentSubmission.getUserId(), null);

            if (!newSubmission.getCurrentSubmissionVersion().isDraft()) {
                // add a sucess message.  the message will change depending on 
                // if this submission is late or not
                if (assignment.getDueDate() != null && assignment.getDueDate().before(new Date())) {
                    messages.addMessage(new TargettedMessage("assignment2.student-submit.info.submission_submitted.late",
                            new Object[] { assignment.getTitle() }, TargettedMessage.SEVERITY_INFO));
                } else {
                    messages.addMessage(new TargettedMessage("assignment2.student-submit.info.submission_submitted",
                            new Object[] { assignment.getTitle() }, TargettedMessage.SEVERITY_INFO));
                }

                // Send out notifications
                if (assignment.isSendSubmissionNotifications()) {                 
                    scheduledNotification.notifyInstructorsOfSubmission(newSubmission);
                }

                // students always get a notification
                scheduledNotification.notifyStudentThatSubmissionWasAccepted(newSubmission);
            } else {
                messages.addMessage(new TargettedMessage("assignment2.student-submit.error.submission_save_draft",
                        new Object[] { assignment.getTitle() }, TargettedMessage.SEVERITY_ERROR));
                return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;
            }
        }

        return WorkFlowResult.STUDENT_SUBMIT_SUBMISSION;
    }

    public WorkFlowResult processActionPreview(){
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;

        // save this submission as draft if submission is closed so we don't
        // lose the student's work. this may happen if the user was working
        // on their submission in the UI when the assignment closed and then
        // the student hit "preview"
        Assignment2 assignment = assignmentLogic.getAssignmentById(assignmentId);
        if (assignmentId != null) {
            AssignmentSubmission assignmentSubmission = (AssignmentSubmission) asEntityBeanLocator.locateBean(ASOTPKey);
            assignmentSubmission.setAssignment(assignment);
            
            AssignmentSubmissionVersion asv = (AssignmentSubmissionVersion) studentSubmissionVersionFlowBean.getAssignmentSubmissionVersion();
            
            if (!cleanUpSubmission(asv)) {
                return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;
            }
            
            if (!submissionLogic.isSubmissionOpenForStudentForAssignment(assignmentSubmission.getUserId(), assignmentId)) {

                submissionLogic.saveStudentSubmission(assignmentSubmission.getUserId(),
                        assignmentSubmission.getAssignment(), true, asv.getSubmittedText(),
                        asv.getHonorPledge(), asv.getSubmissionAttachSet(), true);
            }
        }

        return WorkFlowResult.STUDENT_PREVIEW_SUBMISSION;
    }

    public WorkFlowResult processActionBackToEdit() {
        return WorkFlowResult.STUDENT_CONTINUE_EDITING_SUBMISSION;
    }

    public WorkFlowResult processActionSaveDraft() {
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;

        Assignment2 assignment = assignmentLogic.getAssignmentById(assignmentId);
        if (assignmentId == null){
            return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;
        }

        AssignmentSubmission assignmentSubmission = (AssignmentSubmission) asEntityBeanLocator.locateBean(ASOTPKey);
        assignmentSubmission.setAssignment(assignment);

        AssignmentSubmissionVersion asv = (AssignmentSubmissionVersion) studentSubmissionVersionFlowBean.getAssignmentSubmissionVersion();

        asv.setAssignmentSubmission(assignmentSubmission);
        asv.setDraft(Boolean.TRUE);
        
        if (!cleanUpSubmission(asv)) {
            return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;
        }

        try {
            submissionLogic.saveStudentSubmission(assignmentSubmission.getUserId(),
                    assignmentSubmission.getAssignment(), true, asv.getSubmittedText(),
                    asv.getHonorPledge(), asv.getSubmissionAttachSet(), true);
            messages.addMessage(new TargettedMessage("assignment2.student-submit.info.submission_save_draft",
                    new Object[] { assignment.getTitle() }, TargettedMessage.SEVERITY_INFO));
        } catch (SubmissionClosedException sce) {
            messages.addMessage(new TargettedMessage("assignment2.student-submit.error.submission_closed",
                    new Object[] {}, TargettedMessage.SEVERITY_ERROR));
            return WorkFlowResult.STUDENT_SUBMISSION_FAILURE;
        }

        return WorkFlowResult.STUDENT_SAVE_DRAFT_SUBMISSION;
    }

    public WorkFlowResult processActionCancel() {
        return WorkFlowResult.STUDENT_CANCEL_SUBMISSION;
    }
    
    public WorkFlowResult processActionResubmit() {
        return WorkFlowResult.STUDENT_RESUBMIT;
    }
    
    /**
     * 
     * @param version
     * @return true if the text supplied by WYSIWYG editors on the version is
     * valid. false if the text contains malicious tags or attributes that need
     * to be addressed before submission can proceed. Strips the text of extraneous
     * whitespace, as well
     */
    private boolean cleanUpSubmission(AssignmentSubmissionVersion version) {
        boolean textValid = true;
        if (version != null) {
            StringBuilder alertMsg = new StringBuilder();
            if (version.getSubmittedText() != null) {
                version.setSubmittedText(FormattedText.
                        processFormattedText(version.getSubmittedText(), alertMsg, true, true));
                if (alertMsg != null && alertMsg.length() > 0) {
                    messages.addMessage(new TargettedMessage("assignment2.student-submit.error.submitted_text", new Object[] {alertMsg.toString()}));
                    textValid = false;
                }
            }
        }
        
        return textValid;
    }

}
