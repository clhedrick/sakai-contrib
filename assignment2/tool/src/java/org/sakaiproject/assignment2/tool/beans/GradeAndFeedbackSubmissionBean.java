/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/beans/GradeAndFeedbackSubmissionBean.java $
 * $Id: GradeAndFeedbackSubmissionBean.java 87495 2015-04-06 15:12:29Z hedrick@rutgers.edu $
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

package org.sakaiproject.assignment2.tool.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalEventLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.WorkFlowResult;
import org.sakaiproject.util.FormattedText;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

/**
 * This bean is for action methods being used from the Instructor Grade and
 * Feedback page.
 * 
 * @author sgithens
 *
 */
public class GradeAndFeedbackSubmissionBean {

    public Map<String, Boolean> selectedIds = new HashMap<String, Boolean>();
    public Long assignmentId;
    public String ASOTPKey;
    public String userId;
    public Boolean releaseFeedback;

    private TargettedMessageList messages;
    public void setMessages(TargettedMessageList messages) {
        this.messages = messages;
    }

    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    private AssignmentSubmissionLogic submissionLogic;
    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }
    
    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }
    
    private ExternalEventLogic eventLogic;
    public void setExternalEventLogic(ExternalEventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }
    
    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    private Map<String, AssignmentSubmission> OTPMap;
    private EntityBeanLocator asEntityBeanLocator;
    @SuppressWarnings("unchecked")
    public void setAssignmentSubmissionEntityBeanLocator(EntityBeanLocator entityBeanLocator) {
        this.OTPMap = entityBeanLocator.getDeliveredBeans();
        this.asEntityBeanLocator = entityBeanLocator;
    }

    private Map<String, AssignmentSubmissionVersion> asvOTPMap;
    public void setAsvEntityBeanLocator(EntityBeanLocator entityBeanLocator) {
        this.asvOTPMap = entityBeanLocator.getDeliveredBeans();
    }

    private Boolean honorPledge;
    public void setHonorPledge(Boolean honorPledge) {
        this.honorPledge = honorPledge;
    }

    private NotificationBean notificationBean;
    public void setNotificationBean(NotificationBean notificationBean) {
        this.notificationBean = notificationBean;
    }

    private Boolean overrideResubmissionSettings;
    public void setOverrideResubmissionSettings(Boolean overrideResubmissionSettings) {
        this.overrideResubmissionSettings = overrideResubmissionSettings;
    }

    private Boolean resubmitUntil;
    public void setResubmitUntil(Boolean resubmitUntil) {
        this.resubmitUntil = resubmitUntil;
    }
    
    private String grade;
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    private String gradeComment;
    public void setGradeComment(String gradeComment) {
        this.gradeComment = gradeComment;
    }

    /**
     * This property is used primarily with the 
     * {@link GradeAndFeedbackSubmissionBean.processActionGradeSubmitAndEditAnotherVersion}
     * to set the next version to edit.
     */
    private Long nextVersionIdToEdit;

    public void setNextVersionIdToEdit(Long nextVersionIdToEdit) {
        this.nextVersionIdToEdit = nextVersionIdToEdit;
    }

    public Long getNextVersionIdToEdit() {
        return nextVersionIdToEdit;
    }

    private String submitOption = "submitOption";
    public void setSubmitOption (String submitOption)
    {
    	this.submitOption = submitOption;
    }


    private String csrfToken;
    public void setCsrfToken(String data) {
	csrfToken = data;
    }

    /*
     * INSTRUCTOR FUNCTIONS
     */


    public WorkFlowResult processActionSaveAndReleaseFeedbackForSubmission(){
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE;

        this.releaseFeedback = true;
        return processActionGradeSubmit();
    }
    
    public WorkFlowResult processActionGradeSubmitOption()
    {
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE;

        if (WorkFlowResult.INSTRUCTOR_FEEDBACK_RELEASE_NEXT.toString().equals(submitOption) 
          || WorkFlowResult.INSTRUCTOR_FEEDBACK_RELEASE_PREV.toString().equals(submitOption) 
          || WorkFlowResult.INSTRUCTOR_FEEDBACK_RELEASE_RETURNTOLIST.toString().equals(submitOption))
        {
            return processActionSaveAndReleaseFeedbackForSubmission();
        }
        else
        {
            return processActionGradeSubmit();
        }
    }

    /**
     * This action method is primarily used for the "Edit" links on the Grade
     * page under the History section, where you want to go edit a previous 
     * version.  This method exists because we are supposed to save any changes
     * in the feedback on the current screen before going back to edit another
     * version.  This will do the usual work involved in saving (but NOT 
     * releasing) the feedback, and then return a formatted string whose first
     * half is the identifier demarcating this action, and the second part is 
     * ID of the Version we are going to edit next. This way the version id can
     * be used in the ActionResultInterceptor.
     * 
     * The format will look like this:
     * 
     * SAVE_FEEDBACK_AND_EDIT_VERSION_14
     * 
     * where 14 is the ID of the next version to edit.  Currently in our DB 
     * schema the version ID will always be Long.
     * 
     * @return
     */
    public WorkFlowResult processActionGradeSubmitAndEditAnotherVersion() {
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE;

        WorkFlowResult saveResult = processActionGradeSubmit();
        if (WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE.equals(saveResult)) {
            return WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE;
        }
        WorkFlowResult togo = WorkFlowResult.INSTRUCTOR_FEEDBACK_SAVE_AND_EDIT_PREFIX;
        togo.setNextSubVersion(getNextVersionIdToEdit());
        return togo;
    }

    public WorkFlowResult processActionGradeSubmit(){
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE;

        if (assignmentId == null || userId == null){
            return WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE;
        }
        Assignment2 assignment = assignmentLogic.getAssignmentById(assignmentId);
        AssignmentSubmission assignmentSubmission = new AssignmentSubmission(assignment, userId);
        
        // validate the grade entry first
        boolean allowedToGradeInGB = false;
        if (assignment.isGraded() && gradebookLogic.gradebookItemExists(assignment.getGradebookItemId())) {
            allowedToGradeInGB = gradebookLogic.isCurrentUserAbleToGradeStudentForItem(assignment.getContextId(), 
                    assignmentSubmission.getUserId(), assignment.getGradebookItemId());
        }
        
        if (allowedToGradeInGB) {
            boolean gradeValid = gradebookLogic.isGradeValid(assignment.getContextId(), grade);
            if (!gradeValid) {
                int gradeEntryType = gradebookLogic.getGradebookGradeEntryType(assignment.getContextId());
                String errorMessageRef;
                if (gradeEntryType == ExternalGradebookLogic.ENTRY_BY_POINTS) {
                    errorMessageRef = "assignment2.gradebook.grading.points.error";
                } else if (gradeEntryType == ExternalGradebookLogic.ENTRY_BY_PERCENT) {
                    errorMessageRef = "assignment2.gradebook.grading.percent.error";
                } else if (gradeEntryType == ExternalGradebookLogic.ENTRY_BY_LETTER) {
                    errorMessageRef = "assignment2.gradebook.grading.letter.error";
                } else {
                    errorMessageRef = "assignment2.gradebook.grading.unknown.error";
                }
                
                messages.addMessage(new TargettedMessage(errorMessageRef, new Object[] {}, TargettedMessage.SEVERITY_ERROR));
                return WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE;
            }
        }

        for (String key : OTPMap.keySet()){
            assignmentSubmission = OTPMap.get(key);
            assignmentSubmission.setAssignment(assignment);
            assignmentSubmission.setUserId(userId);

            if (resubmitUntil == null || Boolean.FALSE.equals(resubmitUntil)) {
                assignmentSubmission.setResubmitCloseDate(null);
            }

            if (overrideResubmissionSettings == null || !overrideResubmissionSettings) {
                assignmentSubmission.setNumSubmissionsAllowed(null);
                assignmentSubmission.setResubmitCloseDate(null);
            }
        }
        for (String key : asvOTPMap.keySet()){

            AssignmentSubmissionVersion asv = asvOTPMap.get(key);
            
            // validate the input text from the WYSIWYG editors
            if (asv.getFeedbackNotes() != null) {
                StringBuilder alertMsg = new StringBuilder();
                asv.setFeedbackNotes(FormattedText.processFormattedText(asv.getFeedbackNotes(), 
                        alertMsg, true, true));
                
                if (alertMsg != null && alertMsg.length() > 0) {
                    messages.addMessage(new TargettedMessage("assignment2.assignment_grade.error.feedback_notes", 
                            new Object[] {alertMsg.toString()}));
                    return WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE;
                }
            }
            if (asv.getAnnotatedText() != null) {
                StringBuilder alertMsg = new StringBuilder();
                asv.setAnnotatedText(FormattedText.processFormattedText(asv.getAnnotatedText(), 
                        alertMsg, true, true));
                
                if (alertMsg != null && alertMsg.length() > 0) {
                    messages.addMessage(new TargettedMessage("assignment2.assignment_grade.error.annotated_text", 
                            new Object[] {alertMsg.toString()}));
                    return WorkFlowResult.INSTRUCTOR_FEEDBACK_FAILURE;
                }
            }

            asv.setAssignmentSubmission(assignmentSubmission);
            if (this.releaseFeedback != null && asv.getFeedbackReleasedDate() == null) {
                asv.setFeedbackReleasedDate(new Date());
            }

            submissionLogic.saveInstructorFeedback(asv.getId(), assignmentSubmission.getUserId(),
                    assignmentSubmission.getAssignment(), asv.getAnnotatedText(),
                    asv.getFeedbackNotes(), asv.getFeedbackReleasedDate(), asv.getFeedbackAttachSet());

            List<String> studentUids = new ArrayList<String>();
            studentUids.add(assignmentSubmission.getUserId());
            submissionLogic.updateStudentResubmissionOptions(studentUids, assignmentSubmission.getAssignment(), 
                    assignmentSubmission.getNumSubmissionsAllowed(), assignmentSubmission.getResubmitCloseDate());
        }
        
        if (allowedToGradeInGB) {
            // save the grade and comments
            gradebookLogic.saveGradeAndCommentForStudent(assignment.getContextId(), 
                    assignment.getGradebookItemId(), assignmentSubmission.getUserId(), grade, gradeComment);
        }
        
        /*
         * ASNN-29 This appears to be the best spot to trigger our Event for
         * Saving feedback and grades.
         */
        if (this.releaseFeedback != null && this.releaseFeedback.equals(Boolean.TRUE)) {
            eventLogic.postEvent(
                AssignmentConstants.EVENT_SUB_SAVE_AND_RELEASE_GRADE_AND_FEEDBACK, 
                assignment.getReference());
        }
        else {
            eventLogic.postEvent(
                    AssignmentConstants.EVENT_SUB_SAVE_GRADE_AND_FEEDBACK, 
                    assignment.getReference());
        }
        
        
        messages.addMessage(new TargettedMessage("assignment2.assignment_grade.save_confirmation", new Object[] {externalLogic.getUserDisplayName(userId)}, TargettedMessage.SEVERITY_INFO));
        return WorkFlowResult.valueOf(submitOption);
    }

    public WorkFlowResult processActionCancel() {
        return WorkFlowResult.INSTRUCTOR_FEEDBACK_CANCEL;
    }

}