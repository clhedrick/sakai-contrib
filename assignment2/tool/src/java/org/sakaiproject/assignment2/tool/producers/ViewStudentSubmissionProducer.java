/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/branches/ASNN-521/tool/src/java/org/sakaiproject/assignment2/tool/producers/GradeProducer.java $
 * $Id: GradeProducer.java 66084 2010-02-09 21:48:28Z wagnermr@iupui.edu $
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.DisplayUtil;
import org.sakaiproject.assignment2.tool.params.ViewSubmissionParams;
import org.sakaiproject.assignment2.tool.producers.renderers.AsnnInstructionsRenderer;
import org.sakaiproject.assignment2.tool.producers.renderers.AsnnSubmissionVersionRenderer;
import org.sakaiproject.assignment2.tool.producers.renderers.AsnnToggleRenderer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * This view is a read-only view of the assignment details
 * 
 *
 */
public class ViewStudentSubmissionProducer implements ViewComponentProducer, ViewParamsReporter {
    // this view id is referenced outside the tool layer, so we use a constant
    public static final String VIEW_ID = AssignmentConstants.TOOL_VIEW_SUBMISSION;
    public String getViewID() {
        return VIEW_ID;
    }
    
    private static Log log = LogFactory.getLog(ViewStudentSubmissionProducer.class);

    private AssignmentLogic assignmentLogic;
    private Locale locale;
    private AssignmentPermissionLogic permissionLogic;
    private AsnnInstructionsRenderer asnnInstructionsRenderer;
    private AssignmentSubmissionLogic submissionLogic;
    private AsnnSubmissionVersionRenderer asnnSubmissionVersionRenderer;
    private MessageLocator messageLocator;
    private DisplayUtil displayUtil;
    private AsnnToggleRenderer toggleRenderer;
    private ExternalGradebookLogic gradebookLogic;
    private ExternalLogic externalLogic;

    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
          // Get Params
          ViewSubmissionParams params = (ViewSubmissionParams) viewparams;
          Long assignmentId = params.assignmentId;
          String studentUserId = params.userId;
          if (assignmentId == null || studentUserId == null){
              //handle error
              return;
          }
          
          Map<String, Object> optionalParams = new HashMap<String, Object>();
          optionalParams.put(AssignmentConstants.TAGGABLE_REF_KEY, params.tagReference);
          optionalParams.put(AssignmentConstants.TAGGABLE_DECO_WRAPPER, params.tagDecoWrapper);
          if (!permissionLogic.isUserAllowedToViewSubmissionForAssignment(null, studentUserId, assignmentId, optionalParams))
          {
              // user is not allowed to view the submission for this studentId
              throw new SecurityException("Attempt to view a submission without permission");
          }
          // validate assignmentId
          Assignment2 assignment;
          try
          {
              assignment = assignmentLogic.getAssignmentByIdWithAssociatedData(assignmentId, optionalParams);
          }
          catch (AssignmentNotFoundException anfe)
          {
              // AssignmentId is invalid, so return to handle error
              log.warn("User attempted to use an invalid assignmentId", anfe);
              return;
          }
          
          AssignmentSubmission assignmentSubmission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(assignmentId, studentUserId, optionalParams);
          
          // make sure the assignment is set correctly in the assignmentSubmission object, or it make cause problems later
          if (assignmentSubmission != null) {
              assignmentSubmission.setAssignment(assignment);
          }

          // use a date which is related to the current users locale
          DateFormat df = externalLogic.getDateFormat(null, null, locale, true);
          
          // title
          UIOutput.make(tofill, "title", assignment.getTitle());
          
          // due date. individual level due date is used if it exists, otherwise use the assignment level due date
          // if no due date at all, use a message to explain there is no due date on the assignment
          String dueDate;
          if (assignmentSubmission.getResubmitCloseDate()!=null)
          {
              dueDate = messageLocator.getMessage("assignment2.view-submission.due_date", new Object[]{df.format(assignmentSubmission.getResubmitCloseDate())});
          }
          else if (assignment.getDueDate()!=null)
          {
              dueDate = messageLocator.getMessage("assignment2.view-submission.due_date", new Object[]{df.format(assignment.getDueDate())});
          }
          else
          {
              dueDate = messageLocator.getMessage("assignment2.view-submission.no_due_date");
          }
          
          UIOutput.make(tofill, "dueDate", dueDate);
          
          // These are checks to display various statuses if there are no submissions for this page
          List<AssignmentSubmissionVersion> submittedVersions = new ArrayList<AssignmentSubmissionVersion>();
          if (assignment.getSubmissionType() == AssignmentConstants.SUBMIT_NON_ELECTRONIC)
          {
              // This is a non-electronic assignment, so display an appropriate message
              UIMessage.make(tofill, "submissionStatus", "assignment2.view-submission.status.non_electronic");
          }
          else if (!assignment.isRequiresSubmission())
          {
              // There are no submissions required for this assignment, so display an appropriate message
              UIMessage.make(tofill, "submissionStatus", "assignment2.view-submission.status.no_submission_required");
          }
          else
          {
              // We are filtering out versions that have not been submitted yet (e.g. drafts)
              if (assignmentSubmission.getSubmissionHistorySet()!=null)
              {
                  for (AssignmentSubmissionVersion asv : assignmentSubmission.getSubmissionHistorySet())
                  {
                      if (asv.getSubmittedDate()!=null)
                      {
                          submittedVersions.add(asv);
                      }
                  }
              }
              if (submittedVersions.isEmpty())
              {
                  // There are no submissions from the user yet, so display the appropriate message
                  UIMessage.make(tofill, "submissionStatus", "assignment2.view-submission.status.no_submission");
              }
          }
          
          // assignment details
          UIOutput.make(tofill, "open-date", df.format(assignment.getOpenDate()));

          // Grading
          // We include whether this assignment is graded and, if it is,
          // we include points possible if it is graded by points.
          // if user does not have gradebook privileges, we hide the grading info

          if (assignment.isGraded() && assignment.getGradebookItemId() != null) {
              // make sure this gradebook item still exists
              try {
                  GradebookItem gradebookItem = gradebookLogic.getGradebookItemById(assignment.getContextId(), 
                          assignment.getGradebookItemId());

                  UIMessage.make(tofill, "graded", "assignment2.details.graded");
                  UIMessage.make(tofill, "is-graded", "assignment2.details.graded.yes");

                  // only display points possible if grade entry by points
                  if (gradebookLogic.getGradebookGradeEntryType(assignment.getContextId()) == ExternalGradebookLogic.ENTRY_BY_POINTS) {
                      UIOutput.make(tofill, "points-possible-row");

                      String pointsDisplay;
                      if (gradebookItem.getPointsPossible() == null) {
                          pointsDisplay = messageLocator.getMessage("assignment2.details.gradebook.points_possible.none");
                      } else {
                          pointsDisplay = gradebookItem.getPointsPossible().toString();
                      }
                      UIOutput.make(tofill, "points-possible", pointsDisplay); 
                  }

              } catch (GradebookItemNotFoundException ginfe) {
                  if (log.isDebugEnabled()) log.debug("Attempt to access assignment " + 
                          assignment.getId() + " but associated gb item no longer exists!");
              } catch (SecurityException se) {
                  // this user doesn't have gb privileges, so hide the gb info
              }
          } else {
              // this assignment is ungraded
              UIMessage.make(tofill, "graded", "assignment2.details.graded");
              UIMessage.make(tofill, "is-graded", "assignment2.details.graded.no");
          }
          
          // if this assignment requires submission, we'll see if resubmission is allowed
          if (assignment.isRequiresSubmission()) {
              UIOutput.make(tofill, "resubmission-allowed-row");
              String resubmissionAllowedString;
              // try to use individual level first
              if (assignmentSubmission.getNumSubmissionsAllowed()!=null)
              {
                  UIOutput.make(tofill, "assign-num-submissions-allowed-row");
                  if (submissionLogic.getNumberOfRemainingSubmissionsForStudent(studentUserId, assignmentId) > 0 || assignmentSubmission.getNumSubmissionsAllowed() == AssignmentConstants.UNLIMITED_SUBMISSION)
                  {
                      resubmissionAllowedString = messageLocator.getMessage("assignment2.details.resubmission.allowed.yes");
                  }
                  else
                  {
                      resubmissionAllowedString = messageLocator.getMessage("assignment2.details.resubmission.allowed.no");
                  }
                  
                  if (assignmentSubmission.getNumSubmissionsAllowed() == AssignmentConstants.UNLIMITED_SUBMISSION) {
                      UIOutput.make(tofill, "assign-num-submissions-allowed", messageLocator.getMessage("assignment2.details.resubmission.unlimited"));
                  } else {
                      UIOutput.make(tofill, "assign-num-submissions-allowed", assignmentSubmission.getNumSubmissionsAllowed() + "");
                  }
              }
              else if (assignment.getNumSubmissionsAllowed() > 1 || 
                      assignment.getNumSubmissionsAllowed() == AssignmentConstants.UNLIMITED_SUBMISSION) {
                  UIOutput.make(tofill, "assign-num-submissions-allowed-row");
                  resubmissionAllowedString = messageLocator.getMessage("assignment2.details.resubmission.allowed.yes");
                  
                  if (assignment.getNumSubmissionsAllowed() == AssignmentConstants.UNLIMITED_SUBMISSION) {
                      UIOutput.make(tofill, "assign-num-submissions-allowed", messageLocator.getMessage("assignment2.details.resubmission.unlimited"));
                  } else {
                      UIOutput.make(tofill, "assign-num-submissions-allowed", assignment.getNumSubmissionsAllowed() + "");
                  }
              } else {
                  resubmissionAllowedString = messageLocator.getMessage("assignment2.details.resubmission.allowed.no");
              }
              
              UIOutput.make(tofill, "resubmissions-allowed", resubmissionAllowedString);
          }
          
          // for model answer
          optionalParams.put(AssignmentConstants.MODEL_ANSWER_IS_INSTRUCTOR, permissionLogic.isUserAllowedToTakeInstructorAction(null, assignment.getContextId()));
          
          // instructions widget
          asnnInstructionsRenderer.makeInstructions(tofill, "instructions:", assignment, false, false, false, optionalParams);
          
          if (!submittedVersions.isEmpty())
          {
              for (AssignmentSubmissionVersion single : submittedVersions)
              {
                  // figure out the status so we can determine what the heading should be
                  int status = submissionLogic.getSubmissionStatusForVersion(single, assignment.getDueDate(), assignmentSubmission.getResubmitCloseDate());
                  String headerText;
                  if (single.getSubmittedVersionNumber() == AssignmentSubmissionVersion.FEEDBACK_ONLY_VERSION_NUMBER) {
                      headerText = messageLocator.getMessage("assignment2.version.toggle.status.feedback_only_version");
                  } else {
                      headerText = displayUtil.getVersionStatusText(status, single.getStudentSaveDate(), single.getSubmittedDate());
                  }
                  String toggleHoverText = messageLocator.getMessage("assignment2.version.toggle.hover");
                  UIBranchContainer versionDiv = UIBranchContainer.make(tofill, "toggle-wrapper:");
                  toggleRenderer.makeToggle(versionDiv, "version_toggle:", null, true, headerText, toggleHoverText, false, false, false, false, null);
                  asnnSubmissionVersionRenderer.fillComponents(versionDiv, "submission:", single, true, optionalParams.containsKey(AssignmentConstants.TAGGABLE_REF_KEY), optionalParams);
              }
          }
    }

    public ViewParameters getViewParameters() {
        return new ViewSubmissionParams();
    }

    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setAssignmentPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }
    
    public void setAsnnInstructionsRenderer(AsnnInstructionsRenderer asnnInstructionsRenderer) {
        this.asnnInstructionsRenderer = asnnInstructionsRenderer;
    }

    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }
    
    public void setAsnnSubmissionVersionRenderer(AsnnSubmissionVersionRenderer asnnSubmissionVersionRenderer) {
        this.asnnSubmissionVersionRenderer = asnnSubmissionVersionRenderer;
    }
    
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }
    
    public void setDisplayUtil(DisplayUtil displayUtil) {
        this.displayUtil = displayUtil;
    }
    
    public void setAsnnToggleRenderer(AsnnToggleRenderer toggleRenderer) {
        this.toggleRenderer = toggleRenderer;
    }
    
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }
    
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
}
