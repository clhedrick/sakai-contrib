/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/Assignment2ViewParamsInterceptor.java $
 * $Id: Assignment2ViewParamsInterceptor.java 53899 2008-10-13 15:07:58Z swgithen@mtu.edu $
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

package org.sakaiproject.assignment2.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.tool.params.AssignmentListSortViewParams;
import org.sakaiproject.assignment2.tool.params.AssignmentViewParams;
import org.sakaiproject.assignment2.tool.params.SimpleAssignmentViewParams;
import org.sakaiproject.assignment2.tool.params.StudentSubmissionParams;
import org.sakaiproject.assignment2.tool.params.VerifiableViewParams;
import org.sakaiproject.assignment2.tool.params.ViewSubmissionsViewParams;
import org.sakaiproject.assignment2.tool.producers.AssignmentProducer;
import org.sakaiproject.assignment2.tool.producers.ListProducer;
import org.sakaiproject.assignment2.tool.producers.AuthorizationFailedProducer;
import org.sakaiproject.assignment2.tool.producers.PreviewAsStudentProducer;
import org.sakaiproject.assignment2.tool.producers.RedirectToAssignmentProducer;
import org.sakaiproject.assignment2.tool.producers.StudentAssignmentListProducer;
import org.sakaiproject.assignment2.tool.producers.StudentSubmitProducer;
import org.sakaiproject.assignment2.tool.producers.UploadAllConfirmProducer;
import org.sakaiproject.assignment2.tool.producers.UploadAllProducer;
import org.sakaiproject.assignment2.tool.producers.ViewSubmissionsProducer;

import uk.org.ponder.rsf.builtin.UVBProducer;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsInterceptor;

/**
 * The primary HTTP GET interceptor for Assignments2, that checks URL's for 
 * permissions and other logic, and then changes where they should be going or
 * their parameters if necessary.
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class Assignment2WorkFlowLogic implements ViewParamsInterceptor, ActionResultInterceptor {
    private static final Log log = LogFactory.getLog(Assignment2WorkFlowLogic.class);

    private LocalPermissionLogic localPermissionLogic;
    public void setLocalPermissionLogic(LocalPermissionLogic localPermissionLogic){
        this.localPermissionLogic = localPermissionLogic;
    }


    public AnyViewParameters adjustViewParameters(ViewParameters incoming) {
        if (AuthorizationFailedProducer.VIEWID.equals(incoming.viewID)) {
            //Always return incoming if we are going to the Authorization Failed Page
            return incoming;
        }

        //Verify View Params for completeness
        if (incoming instanceof VerifiableViewParams) {
            if(!((VerifiableViewParams)incoming).verify()){
                return new AssignmentListSortViewParams(ListProducer.VIEW_ID);
            }
        }

        // depending on the user's perms in the site, will redirect them to either
        // the "View Submissions" page or Student Summary/Submit page from this generic link
        if (RedirectToAssignmentProducer.VIEWID.equals(incoming.viewID)) {
            if (incoming instanceof SimpleAssignmentViewParams) {
                SimpleAssignmentViewParams params = (SimpleAssignmentViewParams) incoming;
                if (localPermissionLogic.checkCurrentUserHasViewPermission(new ViewSubmissionsViewParams(ViewSubmissionsProducer.VIEW_ID, params.assignmentId))) {
                    return new ViewSubmissionsViewParams(ViewSubmissionsProducer.VIEW_ID, params.assignmentId);
                } else if (localPermissionLogic.checkCurrentUserHasViewPermission(new SimpleAssignmentViewParams(StudentSubmitProducer.VIEW_ID, params.assignmentId))) {
                    return new SimpleAssignmentViewParams(StudentSubmitProducer.VIEW_ID, params.assignmentId);
                }
            }
        }

        if (localPermissionLogic.checkCurrentUserHasViewPermission(incoming)){
            return incoming;
        }
        else if (localPermissionLogic.checkCurrentUserHasViewPermission(new AssignmentListSortViewParams(StudentAssignmentListProducer.VIEW_ID))) {
            return new AssignmentListSortViewParams(StudentAssignmentListProducer.VIEW_ID);
        }

        return new SimpleViewParameters(AuthorizationFailedProducer.VIEWID);
    }

    private void interceptWorkFlowResult(ARIResult result, ViewParameters incoming, WorkFlowResult actionReturn) {
        Long assignmentId = null;
        if (incoming instanceof StudentSubmissionParams) {
            assignmentId = ((StudentSubmissionParams) incoming).assignmentId;
        }
        else if (incoming instanceof SimpleAssignmentViewParams) {
            assignmentId = ((SimpleAssignmentViewParams) incoming).assignmentId;
        }
        else if (incoming instanceof AssignmentViewParams) {
            assignmentId = ((AssignmentViewParams) incoming).assignmentId;
        }

        switch (actionReturn) {
        case INSTRUCTOR_CANCEL_ASSIGNMENT:
            result.resultingView = new SimpleViewParameters(ListProducer.VIEW_ID);
            result.propagateBeans = ARIResult.FLOW_END;
            break;
        case INSTRUCTOR_POST_ASSIGNMENT:
            result.resultingView = new SimpleViewParameters(ListProducer.VIEW_ID);
            result.propagateBeans = ARIResult.FLOW_END;
            break;
        case INSTRUCTOR_PREVIEW_ASSIGNMENT:
            result.resultingView = new SimpleAssignmentViewParams(PreviewAsStudentProducer.VIEW_ID, assignmentId);
            result.propagateBeans = ARIResult.FLOW_FASTSTART;
            break;
        case INSTRUCTOR_SAVE_DRAFT_ASSIGNMENT:
            result.resultingView = new SimpleViewParameters(ListProducer.VIEW_ID);
            result.propagateBeans = ARIResult.FLOW_END;
            break;
        case INSTRUCTOR_CONTINUE_EDITING_ASSIGNMENT:
            result.resultingView = new SimpleViewParameters(AssignmentProducer.VIEW_ID);
            result.propagateBeans = ARIResult.PROPAGATE;
            break;
        case INSTRUCTOR_FEEDBACK_SUBMIT_RETURNTOLIST:
            result.resultingView = new ViewSubmissionsViewParams(ViewSubmissionsProducer.VIEW_ID, assignmentId);
            result.propagateBeans = ARIResult.FLOW_END;
            break;
            //case INSTRUCTOR_ASSIGNMENT_VALIDATION_FAILURE:
            //    result.resultingView = new SimpleViewParameters(AssignmentProducer.VIEW_ID);
            //    result.propagateBeans = ARIResult.FLOW_FASTSTART;
            //    break;
        case STUDENT_CONTINUE_EDITING_SUBMISSION:
            if (incoming instanceof StudentSubmissionParams) {
                StudentSubmissionParams tempParams = (StudentSubmissionParams) incoming;
                result.resultingView = new StudentSubmissionParams(StudentSubmitProducer.VIEW_ID, assignmentId, false, false, tempParams.resubmit );
            } else {
                result.resultingView = new StudentSubmissionParams(StudentSubmitProducer.VIEW_ID, assignmentId, false);                
            }

            result.propagateBeans = ARIResult.PROPAGATE;
            break;
        case STUDENT_PREVIEW_SUBMISSION:
            if (incoming instanceof StudentSubmissionParams) {
                StudentSubmissionParams tempParams = (StudentSubmissionParams) incoming;
                result.resultingView = new StudentSubmissionParams(StudentSubmitProducer.VIEW_ID, assignmentId, true, false, tempParams.resubmit );
            } else {
                StudentSubmissionParams tempParams = (StudentSubmissionParams) incoming;
                result.resultingView = new StudentSubmissionParams(StudentSubmitProducer.VIEW_ID, assignmentId, true, false, tempParams.resubmit );
            }
            
            result.propagateBeans = ARIResult.FLOW_FASTSTART;
            break;
        case STUDENT_SAVE_DRAFT_SUBMISSION:
            result.resultingView = new SimpleViewParameters(StudentAssignmentListProducer.VIEW_ID);
            break;
        case STUDENT_SUBMIT_SUBMISSION:
            result.resultingView = new SimpleViewParameters(StudentAssignmentListProducer.VIEW_ID);
            break;
        case STUDENT_CANCEL_SUBMISSION:
            result.resultingView = new SimpleViewParameters(StudentAssignmentListProducer.VIEW_ID);
            result.propagateBeans = ARIResult.FLOW_END;
            break;
        case STUDENT_SUBMISSION_FAILURE:
            result.propagateBeans = ARIResult.FLOW_FASTSTART;
            break;
        case STUDENT_RESUBMIT:
            result.resultingView = new StudentSubmissionParams(StudentSubmitProducer.VIEW_ID, assignmentId, false, false, true);
            result.propagateBeans = ARIResult.FLOW_END;
            break;
            /*
             * Upload CSV or ZIP file
             */
        case UPLOADALL_CSV_BACK_TO_UPLOAD:
            if (incoming instanceof AssignmentViewParams) {
                AssignmentViewParams params = (AssignmentViewParams) incoming;
                result.resultingView = new AssignmentViewParams(UploadAllProducer.VIEW_ID, params.assignmentId);
                result.propagateBeans = ARIResult.FLOW_END;
            }
            break;
        case UPLOADALL_CSV_CONFIRM_AND_SAVE:
            if (incoming instanceof AssignmentViewParams) {
                AssignmentViewParams params = (AssignmentViewParams) incoming;
                result.resultingView = new ViewSubmissionsViewParams(ViewSubmissionsProducer.VIEW_ID, params.assignmentId);
                result.propagateBeans = ARIResult.FLOW_END;
            }
            break;
        case UPLOADALL_CSV_UPLOAD:
            if (incoming instanceof AssignmentViewParams) {
                AssignmentViewParams params = (AssignmentViewParams) incoming;
                result.resultingView = new AssignmentViewParams(UploadAllConfirmProducer.VIEW_ID, params.assignmentId);
                result.propagateBeans = ARIResult.FLOW_FASTSTART;
            }
            break;

            /*
             * Upload All zip file 
             */
        case UPLOAD_SUCCESS:
            if (incoming instanceof AssignmentViewParams) {
                AssignmentViewParams params = (AssignmentViewParams) incoming;
                result.resultingView = new ViewSubmissionsViewParams(ViewSubmissionsProducer.VIEW_ID, params.assignmentId);
                result.propagateBeans = ARIResult.FLOW_END;
            }
            break;

        case REORDER_STUDENT_VIEW_SAVE:
            result.resultingView = new SimpleViewParameters(ListProducer.VIEW_ID);
            break;
        case REORDER_STUDENT_VIEW_CANCEL:
            result.resultingView = new SimpleViewParameters(ListProducer.VIEW_ID);
            break;
        case IMPORT_ASSIGNMENTS_VIEW_IMPORT:
            result.resultingView = new SimpleViewParameters(ListProducer.VIEW_ID);
            break;
        case IMPORT_ASSIGNMENTS_VIEW_CANCEL:
            result.resultingView = new SimpleViewParameters(ListProducer.VIEW_ID);
            break;
            
        case CANCEL_TO_LIST_VIEW:
            result.resultingView = new SimpleViewParameters(ListProducer.VIEW_ID);
            result.propagateBeans = ARIResult.FLOW_END;
            break;

        default:
            log.warn("Unknown/Unhandled WorkFlowResult in Asnn2 Workflow: " + actionReturn);
        break;
        }
    }

    public void interceptActionResult(ARIResult result, ViewParameters incoming, Object actionReturn) {
        if (UVBProducer.VIEW_ID.equals(incoming.viewID)) {
            log.debug("Ingoring UVB Views in the Asnn2 Action Workflow.");
        }
        else if (actionReturn instanceof WorkFlowResult) {
            interceptWorkFlowResult(result, incoming, (WorkFlowResult) actionReturn);
        }
        else {
            log.warn("Unknown Action Return in Asnn2 Workflow: " + actionReturn);
        }
    }

}