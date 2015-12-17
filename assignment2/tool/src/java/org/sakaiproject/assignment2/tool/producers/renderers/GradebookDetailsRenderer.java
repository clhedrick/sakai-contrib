/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/renderers/GradebookDetailsRenderer.java $
 * $Id: GradebookDetailsRenderer.java 67456 2010-05-03 16:32:49Z wagnermr@iupui.edu $
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

import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradeInformation;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.tool.producers.FinishedHelperProducer;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;


public class GradebookDetailsRenderer {
    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    private AssignmentPermissionLogic permissionLogic;
    public void setPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }

    public void makeGradebookDetails(UIContainer tofill, String divID, AssignmentSubmission as, Long assignmentId, String userId){

        Assignment2 assignment = assignmentLogic.getAssignmentByIdWithAssociatedData(assignmentId);
        
        // we don't include grade details if this student isn't gradable in the Gradebook tool
        if (!gradebookLogic.isUserAStudentInGradebook(assignment.getContextId(), as.getUserId())) {
            return;
        }
        
        UIJointContainer joint = new UIJointContainer(tofill, divID, "gradebook_details:", ""+1);

        //Grade Permission
        Boolean grade_perm = gradebookLogic.isCurrentUserAbleToGradeStudentForItem(assignment.getContextId(), as.getUserId(), assignment.getGradebookItemId());

        //Grading Helper Link
        String url = externalLogic.getUrlForGradeGradebookItemHelper(assignment.getGradebookItemId(), userId, FinishedHelperProducer.VIEWID, assignment.getContextId());

        if (grade_perm) {
            UILink.make(joint, "gradebook_grading_helper",
                    UIMessage.make("assignment2.assignment_grade.gradebook_grade"),
                    url);
        }

        // retrieve the grade information
        String grade = "";
        String gradeComment = "";
        if (as != null && as.getAssignment() != null && as.getAssignment().isGraded()) {
            GradeInformation gradeInfo = gradebookLogic.getGradeInformationForStudent(assignment.getContextId(), assignment.getGradebookItemId(), as.getUserId());
            if (gradeInfo != null) {
                grade = gradeInfo.getGradebookGrade();
                gradeComment = gradeInfo.getGradebookComment();
            }
        }

        UIOutput.make(joint, "gradebook_grade", (as!= null && grade != null ? grade : ""));
        UIOutput.make(joint, "gradebook_comment", (as != null && gradeComment != null ? gradeComment : ""));
    }
}