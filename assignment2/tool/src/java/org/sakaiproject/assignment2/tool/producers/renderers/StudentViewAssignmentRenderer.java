/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/renderers/StudentViewAssignmentRenderer.java $
 * $Id: StudentViewAssignmentRenderer.java 86090 2014-04-14 18:15:43Z dsobiera@indiana.edu $
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

package org.sakaiproject.assignment2.tool.producers.renderers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.producers.StudentAssignmentListProducer;
import org.sakaiproject.user.api.User;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;


/**
 * Renders the Students view of an assignments details and instructions, as well
 * as completing the material and viewing previous submissions and feedback.
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class StudentViewAssignmentRenderer {
    private static Log log = LogFactory.getLog(StudentViewAssignmentRenderer.class);

    private static final String STUDENT_SUBMISSION_DIVID = "student-view-assignment-area:";

    // Dependency
    private Locale locale;
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    // Dependency
    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    // Dependency
    private AssignmentSubmissionLogic submissionLogic;
    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }

    // Dependency
    private AsnnSubmissionDetailsRenderer asnnSubmissionDetailsRenderer;
    public void setAsnnSubmissionDetailsRenderer(AsnnSubmissionDetailsRenderer asnnSubmissionDetailsRenderer) {
        this.asnnSubmissionDetailsRenderer = asnnSubmissionDetailsRenderer;
    }

    // Dependency
    private AsnnInstructionsRenderer asnnInstructionsRenderer;
    public void setAsnnInstructionsRenderer(AsnnInstructionsRenderer asnnInstructionsRenderer) {
        this.asnnInstructionsRenderer = asnnInstructionsRenderer;
    }

    // Dependency
    private AsnnSubmitEditorRenderer asnnSubmitEditorRenderer;
    public void setAsnnSubmitEditorRenderer(AsnnSubmitEditorRenderer asnnSubmitEditorRenderer) {
        this.asnnSubmitEditorRenderer = asnnSubmitEditorRenderer;
    }

    // Dependency
    private AsnnSubmissionHistoryRenderer asnnSubmissionHistoryRenderer;
    public void setAsnnSubmissionHistoryRenderer(AsnnSubmissionHistoryRenderer asnnSubmissionHistoryRenderer) {
        this.asnnSubmissionHistoryRenderer = asnnSubmissionHistoryRenderer;
    }

    // Dependency
    private AsnnSubmissionVersionRenderer asnnSubmissionVersionRenderer;
    public void setAsnnSubmissionVersionRenderer(
            AsnnSubmissionVersionRenderer asnnSubmissionVersionRenderer) {
        this.asnnSubmissionVersionRenderer = asnnSubmissionVersionRenderer;
    }
    
    // Dependency
    private AsnnDetailsRenderer asnnDetailsRenderer;
    public void setAsnnDetailsRenderer(AsnnDetailsRenderer asnnDetailsRenderer) {
        this.asnnDetailsRenderer = asnnDetailsRenderer;
    }
    
    private GradeDetailsRenderer gradeDetailsRenderer;
    public void setGradeDetailsRenderer(GradeDetailsRenderer gradeDetailsRenderer) {
        this.gradeDetailsRenderer = gradeDetailsRenderer;
    }
    
    private AsnnTagsRenderer tagsRenderer;
    public void setAsnnTagsRenderer(AsnnTagsRenderer tagsRenderer) {
        this.tagsRenderer = tagsRenderer;
    }

    // Dependency
    private User currentUser;
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * It's important to note that the boolean preview on this method is for
     * previewing what the assignment will look like to a student. It is not the
     * Preview Submission view for when the student is about to submit.
     * 
     * @param tofill
     * @param divID
     * @param assignmentSubmission
     * @param assignment
     * @param params
     * @param ASOTPKey
     * @param previewAsStudent
     * @param resubmit true if the student clicked the "Resubmit" link to get here
     */
    public void makeStudentView(UIContainer tofill, String divID, AssignmentSubmission assignmentSubmission, 
            Assignment2 assignment, ViewParameters params, String ASOTPKey, Boolean previewAsStudent, Boolean studentSubmissionPreview,
            boolean resubmit) {
        /**
         * Breadcrumbs
         */
        if (!previewAsStudent) {
            UIInternalLink.make(tofill, "breadcrumb", 
                    messageLocator.getMessage("assignment2.student-assignment-list.heading"),
                    new SimpleViewParameters(StudentAssignmentListProducer.VIEW_ID));
        } else {
            UIMessage.make(tofill, "breadcrumb", "assignment2.student-assignment-list.heading");
            // if we have an id, this is an edit assignment page, otherwise it is an add assignment page
            if (assignment.getId()!=null)
            {
                UIMessage.make(tofill, "add_edit_breadcrumb", "assignment2.assignment_preview.edit_assignment");
            }
            else
            {
                UIMessage.make(tofill, "add_edit_breadcrumb", "assignment2.assignment_preview.add_assignment");
            }
            UIMessage.make(tofill, "preview_breadcrumb", "assignment2.assignment_preview.preview_as_student");
        }

        if (!previewAsStudent) {
            UIOutput.make(tofill, "student-submit-heading", assignment.getTitle());
        }

        if (assignmentSubmission != null) {
            assignmentSubmission.setAssignment(assignment);
        }
        UIJointContainer joint = new UIJointContainer(tofill, divID, STUDENT_SUBMISSION_DIVID, ""+1);

        //For preview, get a decorated list of disabled="disabled"
        Map<String, String> disabledAttr = new HashMap<String, String>();
        disabledAttr.put("disabled", "disabled");

        Map<String, String> disabledLinkAttr = new HashMap<String, String>();
        disabledLinkAttr.put("onclick", "return false;");

        boolean submissionIsOpen = false;
        if (assignment.getId() != null) {
            submissionIsOpen = submissionLogic.isSubmissionOpenForStudentForAssignment(currentUser.getId(), assignment.getId());
        }

        boolean resubmissionIsOpen = false;

        /* 
         * If the Student is previewing their submission, only want to show the
         * text and attachments of that submission.
         */
        if (!studentSubmissionPreview) {
            asnnSubmissionDetailsRenderer.fillComponents(joint, "assignment-status:", assignmentSubmission, previewAsStudent);

            // Submission History
            if (previewAsStudent) {
                // display the assignment details at the top
                asnnDetailsRenderer.fillComponents(joint, "assignment-details-top:", assignmentSubmission, true, false, false);
                
            } else {

                List<AssignmentSubmissionVersion> versionHistory = submissionLogic.getVersionHistoryForSubmission(assignmentSubmission);

                if (versionHistory.size() == 1 && !submissionIsOpen) {
                    AssignmentSubmissionVersion singleVersion = versionHistory.get(0);
                    asnnSubmissionVersionRenderer.fillComponents(joint, "assignment-single-version:", singleVersion, false, false, null);

                    // we need to mark this feedback as read (if released and unread)
                    if (singleVersion.isFeedbackReleased() && !singleVersion.isFeedbackRead()) {
                        List<Long> markRead = new ArrayList<Long>();
                        markRead.add(singleVersion.getId());
                        submissionLogic.markFeedbackAsViewed(singleVersion.getAssignmentSubmission().getId(), markRead);
                    }
                } else if (versionHistory.size() > 1 || (versionHistory.size() == 1 && !versionHistory.get(0).isDraft())) {
                    // only expand feedback if the student didn't click "resubmit"
                    asnnSubmissionHistoryRenderer.fillComponents(joint, "assignment-previous-submissions:", assignmentSubmission, !resubmit);
                }
                
                // we only display the grade information if grade is released OR released fb exists, so
                // we need to determine if released fb exists
                if (assignment.isGraded()) {
                    boolean feedbackExists = false;
                    if (versionHistory != null) {
                        for (AssignmentSubmissionVersion ver : versionHistory) {
                            if (ver.isFeedbackReleased()) {
                                feedbackExists = true;
                                break;
                            }
                        }
                    }

                    // display grade information, if appropriate

                    gradeDetailsRenderer.fillComponents(joint, "grade-details-top:", assignmentSubmission, false, !feedbackExists);

                }

                // determine if this is a resubmission scenario. we will use
                // this in combination with the resubmit param to determine if
                // we display the resubmission editor or just the button to resubmit
                if (submissionIsOpen && versionHistory != null) {
                    for (AssignmentSubmissionVersion ver : versionHistory) {
                        if (ver.getSubmittedDate() != null) {
                            resubmissionIsOpen = true;
                            break;
                        }
                    }
                }
                
                Map<String, Object> optionalParamMap = new HashMap<String, Object>();
                optionalParamMap.put(AssignmentConstants.ASSIGNMENT_SUBMISSION, assignmentSubmission);
                
                // logic for displaying the assignment details and instructions
                if (versionHistory == null || versionHistory.isEmpty()) {
                    asnnDetailsRenderer.fillComponents(joint, "assignment-details-top:", assignmentSubmission, false, false, false);
                    // only display these instructions if submission is closed. the editor will display them if open
                    if (!submissionIsOpen) {
                        asnnInstructionsRenderer.makeInstructions(joint, "assignment-instructions-top:", assignment, false, false, false, optionalParamMap);
                    }
                } else {
                    if (resubmissionIsOpen && !resubmit) {
                        // make the instructions and details with the toggle bar at the bottom
                        // of the screen
                        asnnInstructionsRenderer.makeInstructions(joint, "asnn-instructions-bottom:", assignment, true, true, false, optionalParamMap);
                        asnnDetailsRenderer.fillComponents(joint, "asnn-details-bottom:", assignmentSubmission, false, true, false);
                        tagsRenderer.makeTagInformation(joint, "asnn-tags-bottom:", assignment, true, true, false);
                    }
                    else if (submissionIsOpen) {
                        // just display the details b/c the editor will display the instructions
                        asnnDetailsRenderer.fillComponents(joint, "assignment-details-top:", assignmentSubmission, false, false, false);
                    } else {
                        // make the instructions and details with the toggle bar at the bottom
                        // of the screen
                        asnnInstructionsRenderer.makeInstructions(joint, "asnn-instructions-bottom:", assignment, true, true, false, optionalParamMap);
                        asnnDetailsRenderer.fillComponents(joint, "asnn-details-bottom:", assignmentSubmission, false, true, false);
                        tagsRenderer.makeTagInformation(joint, "asnn-tags-bottom:", assignment, true, true, false);
                    }
                }
            }
        }
        else {
            asnnSubmissionDetailsRenderer.fillComponents(joint, "assignment-status:", assignmentSubmission, previewAsStudent, true);
        }

        if (previewAsStudent) {
            asnnSubmitEditorRenderer.fillComponents(joint, "assignment-edit-submission:", assignmentSubmission, true, false, false);
        } else if (resubmissionIsOpen && !resubmit) {
            // if resubmission is open but we didn't get here via a resubmit link,
            // don't display the editor. just display the resubmit button
            UIOutput.make(joint, "view-submission-buttons");
            UIForm returnform = UIForm.make(joint, "view-submission-form");
            UICommand.make(returnform, "return-button", UIMessage.make("assignment2.student-submission.returntolist"), "StudentSubmissionBean.processActionCancel");
            UICommand.make(returnform, "resubmit-button", UIMessage.make("assignment2.student-submission.resubmit"), "StudentSubmissionBean.processActionResubmit");
            
        } else if (submissionIsOpen) {
            asnnSubmitEditorRenderer.fillComponents(joint, "assignment-edit-submission:", assignmentSubmission, previewAsStudent, studentSubmissionPreview, resubmit);
        }
        else {
            // If this isn't a preview, and the student can't resubmit, just
            // render the "Return to List" button
            UIOutput.make(joint, "view-submission-buttons");
            UIForm returnform = UIForm.make(joint, "view-submission-form");
            UICommand.make(returnform, "return-button", UIMessage.make("assignment2.student-submission.returntolist"), "StudentSubmissionBean.processActionCancel");
        }

    }
}