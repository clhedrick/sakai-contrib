/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/GradeInformation.java $
 * $Id: GradeInformation.java 61480 2009-06-29 18:39:09Z swgithen@mtu.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007 The Sakai Foundation.
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

package org.sakaiproject.assignment2.logic;

/**
 * A GradeInformation object for containing grade data retrieved from the
 * Gradebook tool for a student for a graded assignment
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class GradeInformation {

    // fields populated with gradebook data
    private Long gradebookItemId;
    private String studentId;
    private String gradebookGrade;
    private String gradebookComment;
    private boolean gradebookGradeReleased;


    public GradeInformation() {
    }

    /**
     * 
     * @return the id of the assignment's associated gradebook item
     */
    public Long getGradebookItemId()
    {
        return gradebookItemId;
    }

    /**
     * the id of the assignment's associated gradebook item
     * @param gradebookItemId
     */
    public void setGradebookItemId(Long gradebookItemId)
    {
        this.gradebookItemId = gradebookItemId;
    }

    /**
     * 
     * @return the student id associated with this grade info
     */
    public String getStudentId()
    {
        return studentId;
    }

    /**
     * the student id associated with this grade info
     * @param studentId
     */
    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    /**
     * 
     * @return the grade for this submission from the associated gb item. This
     * grade will be returned in converted form according to the gradebook's
     * grade entry type (ie letter grade, percentage, etc)
     */
    public String getGradebookGrade() {
        return gradebookGrade;
    }

    /**
     * set the grade for this submission to be stored in the associated gb item.
     * This grade must be in the correct form according to the gradebook's
     * grade entry type (ie letter grade, percentage, etc)
     * @param gradebookGrade
     */
    public void setGradebookGrade(String gradebookGrade) {
        this.gradebookGrade = gradebookGrade;
    }

    /**
     * 
     * @return the gradebook comment from the associated gb item for this student
     */
    public String getGradebookComment() {
        return gradebookComment;
    }

    /**
     * set the comment to be stored in the gradebook
     * @param gradebookComment
     */
    public void setGradebookComment(String gradebookComment) {
        this.gradebookComment = gradebookComment;
    }

    /**
     * 
     * @return true if this grade has been released to the student
     */
    public boolean isGradebookGradeReleased() {
        return gradebookGradeReleased;
    }

    /**
     * true if this grade has been released to the student
     * @param gradebookGradeReleased
     */
    public void setGradebookGradeReleased(boolean gradebookGradeReleased) {
        this.gradebookGradeReleased = gradebookGradeReleased;
    }
}
