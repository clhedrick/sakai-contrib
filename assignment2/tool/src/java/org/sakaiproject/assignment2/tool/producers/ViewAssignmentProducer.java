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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.params.AssignmentDetailsViewParams;
import org.sakaiproject.assignment2.tool.producers.renderers.AsnnInstructionsRenderer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * This view is a read-only view of the assignment details
 * 
 *
 */
public class ViewAssignmentProducer implements ViewComponentProducer, ViewParamsReporter {
    // this view id is referenced outside the tool layer, so we use a constant
    public static final String VIEW_ID = AssignmentConstants.TOOL_VIEW_ASSIGN;
    
    public String getViewID() {
        return VIEW_ID;
    }
    
    private static Log log = LogFactory.getLog(ViewAssignmentProducer.class);

    private MessageLocator messageLocator;
    private AssignmentLogic assignmentLogic;
    private Locale locale;
    private AssignmentPermissionLogic permissionLogic;
    private ExternalGradebookLogic gradebookLogic;
    private ExternalContentReviewLogic contentReviewLogic;
    private AsnnInstructionsRenderer asnnInstructionsRenderer;
    private ExternalLogic externalLogic;

    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
        
        //Get Params
        AssignmentDetailsViewParams params = (AssignmentDetailsViewParams) viewparams;
        Long assignmentId = params.assignmentId;
        if (assignmentId == null){
            //handle error
            return;
        }
        
        Map<String, Object> optionalParamMap = new HashMap<String, Object>();
        optionalParamMap.put(AssignmentConstants.TAGGABLE_REF_KEY, params.tagReference);
        optionalParamMap.put(AssignmentConstants.TAGGABLE_DECO_WRAPPER, params.tagDecoWrapper);
        if (!permissionLogic.isUserAllowedToViewAssignmentId(null, assignmentId, optionalParamMap)) {
            throw new SecurityException("Attempt to view assignment without permission");
        }

        Assignment2 assignment = assignmentLogic.getAssignmentByIdWithAssociatedData(assignmentId, optionalParamMap);

        // optionally render breadcrumbs depending upon the view
        if (params.fromView != null) {
            if (ListProducer.VIEW_ID.equals(params.fromView)) {
                UIOutput.make(tofill, "list_breadcrumbs");
                UIInternalLink.make(tofill, "breadcrumb_asnn_list", 
                        messageLocator.getMessage("assignment2.list.heading"),
                        new SimpleViewParameters(ListProducer.VIEW_ID));
                UIOutput.make(tofill, "breadcrumb_asnn_title", assignment.getTitle());
                
                // make the "Return to List" button
                UIForm form = UIForm.make(tofill, "navigation-form");
                UICommand.make(form, "return-to-list", UIMessage.make("assignment2.details.cancel"), "CommonNavigationBean.processActionCancelToList");
            }
        }
        
        // use a date which is related to the current users locale
        DateFormat df = externalLogic.getDateFormat(null, null, locale, true);
        
        // we only display some fields if user may edit this assignment
        boolean instructorView = permissionLogic.isUserAllowedToEditAssignment(null, assignment);
        
        UIOutput.make(tofill, "title", assignment.getTitle());
        
        String dueDateText = assignment.getDueDate() == null ? messageLocator.getMessage("assignment2.details.due_date.none") : 
            messageLocator.getMessage("assignment2.details.due_date", new Object[] {df.format(assignment.getDueDate())});
        UIOutput.make(tofill, "due-date", dueDateText);
        UIOutput.make(tofill, "open-date", df.format(assignment.getOpenDate()));
        
        // we also display the accept until date if it exists and the current user has instructor privileges
        if (instructorView && assignment.getAcceptUntilDate() != null) {
            UIOutput.make(tofill, "accept-until-row");
            UIOutput.make(tofill, "accept-until-date", df.format(assignment.getAcceptUntilDate()));
        }

        // Grading
        // We include whether this assignment is graded and, if it is,
        // we include points possible if it is graded by points. If the
        // user does not have gradebook privileges, we hide the grading info

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

        // Content Review integration
        if (assignment.isContentReviewEnabled() && contentReviewLogic.isContentReviewAvailable(assignment.getContextId())) {
            UIOutput.make(tofill, "plagiarism-check-row");
            UIOutput.make(tofill, "plagiarism-check-enabled", messageLocator.getMessage("assignment2.details.plagiarism.check.yes"));
        }
        
        // if this assignment requires submission, we'll see if resubmission is allowed
        if (assignment.isRequiresSubmission()) {
            UIOutput.make(tofill, "resubmission-allowed-row");
            String resubmissionAllowedString;
            if (assignment.getNumSubmissionsAllowed() > 1 || 
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
        optionalParamMap.put(AssignmentConstants.MODEL_ANSWER_IS_INSTRUCTOR, permissionLogic.isUserAllowedToTakeInstructorAction(null, assignment.getContextId()));
        
        //render the instructions
        asnnInstructionsRenderer.makeInstructions(tofill, "instructions_section:", assignment, 
                false, false, false, optionalParamMap);
    }

    public ViewParameters getViewParameters() {
        return new AssignmentDetailsViewParams();
    }

    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
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
    
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }
    
    public void setExternalContentReviewLogic(ExternalContentReviewLogic contentReviewLogic) {
        this.contentReviewLogic = contentReviewLogic;
    }
    
    public void setAsnnInstructionsRenderer(AsnnInstructionsRenderer asnnInstructionsRenderer) {
        this.asnnInstructionsRenderer = asnnInstructionsRenderer;
    }
    
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

}
