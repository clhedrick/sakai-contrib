/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/ExternalGradebookLogic.java $
 * $Id: ExternalGradebookLogic.java 76349 2011-09-07 17:37:48Z dsobiera@indiana.edu $
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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.exception.InvalidGradeForAssignmentException;
import org.sakaiproject.assignment2.exception.NoGradebookDataExistsException;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.service.gradebook.shared.Assignment;
import org.sakaiproject.service.gradebook.shared.GradebookService.PointsPossibleValidation;
import org.sakaiproject.site.api.Group;

/**
 * This is the interface for logic which is related to the integration
 * with the Gradebook tool
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public interface ExternalGradebookLogic {
    
    /**
     * Gradebook grade entry is by points
     */
    public static int ENTRY_BY_POINTS = 0;
    /**
     * Gradebook grade entry is by percentage
     */
    public static int ENTRY_BY_PERCENT = 1;
    /**
     * Gradebook grade entry is by letter
     */
    public static int ENTRY_BY_LETTER = 2;
    
    /**
     * Realm permission for editing in the gradebook tool
     */
    public static final String GB_EDIT = "gradebook.editAssignments";
    
    /**
     * Realm permission to grade all in the gradebook tool
     */
    public static final String GB_GRADE_ALL = "gradebook.gradeAll";
    
    /**
     * Realm permission for grading by section in the gradebook tool
     */
    public static final String GB_GRADE_SECTION = "gradebook.gradeSection";
    
    /**
     * Realm permission for viewing your own grades in the gradebook tool
     */
    public static final String GB_VIEW_OWN_GRADES = "gradebook.viewOwnGrades";
    
    /**
     * Realm permission that identifies a "TA" who may have overridden grader permissions
     */
    public static final String GB_TA = "section.role.ta";
    
    /**
     * Realm permission that the Gradebook uses to identify a "Student"
     */
    public static final String GB_STUDENT = "section.role.student";

    /**
     * The Assignment2 tool stores all grading information in the gradebook. Thus,
     * the gradebook backend must exist for the Assignment2 tool to work. This
     * method will check to see if the gradebook data exists and, if not, will
     * add it
     * @param contextId
     */
    public void createGradebookDataIfNecessary(String contextId);

    /**
     * 
     * @param contextId
     * @return a list of {@link GradebookItem}s representing all of the gradebook
     * items that the current user may view or grade.  Does not include
     * "externally maintained" gradebook items (gb items that are managed by
     * tools outside the gradebook like Tests & Quizzes)
     */
    public List<GradebookItem> getViewableGradebookItems(String contextId);

    /**
     * @param contextId
     * @param includeExternallyMaintained true if you want to include "externally maintained"
     * gradebook items in this list. These are gb items that are managed by a tool outside the
     * gradebook, such as Test & Quizzes or original Assignments tool.
     * In general, you should use false since you should not allow a user to link 
     * an assignment with an externally maintained gb item
     * @return a list of GradebookItem objects that represent all of
     * the gradebook items currently defined in the gradebook tool
     * @throws SecurityException if user does not have edit or grade perm
     */
    public List<GradebookItem> getAllGradebookItems(String contextId, boolean includeExternallyMaintained);

    /**
     * returns a list of all groups that the current user is authorized to view
     * according to the gradebook grader permissions
     * @param contextId
     * @return
     */
    public List<Group> getViewableGroupsInGradebook(String contextId);

    /**
     * Using the grader permissions, returns a map of all of the student ids of 
     * the students that the given user is allowed to view or grade
     * to the actual function (grade/view)
     * @param userId
     * @param contextId
     * @param gradebookItemId
     * @return
     * @throws SecurityException if the current user is not allowed to access student info for
     * the given gb item
     */
    public Map<String, String> getViewableStudentsForGradedItemMap(String userId, String contextId, Long gradebookItemId);

    /**
     * 
     * @param userId
     * @param contextId
     * @param gradebookItemId
     * @return a list of the userIds of the students that the given user is allowed to
     * GRADE for the given gradebookItemId. Does not include students that the user may
     * only view.  Convenience method that utilizes {@link #getViewableStudentsForGradedItemMap(String, String, Long)}
     * to extract the gradable students
     */
    public List<String> getGradableStudentsForGradebookItem(String userId, String contextId, Long gradebookItemId);
    
    /**
     * @param contextId
     * @return true if the current user is authorized to edit the gradebook
     */
    public boolean isCurrentUserAbleToEdit(String contextId);

    /**
     * @param contextId
     * @return true if the current user is authorized to grade all in gradebook
     */
    public boolean isCurrentUserAbleToGradeAll(String contextId);

    /**
     * @param contextId
     * @param userId
     * @return true if the given user is authorized to grade all in gradebook
     */
    public boolean isUserAbleToGradeAll(String contextId, String userId);

    /**
     * @param contextId
     * @return true if the current user is authorized to grade in some 
     * capacity in the gradebook.  (ie they may grade all or grade
     * section)
     */
    public boolean isCurrentUserAbleToGrade(String contextId);

    /**
     * 
     * @param contextId
     * @param userId
     * @return true if the given user is authorized to grade in some
     * capacity in the gradebook. (ie they may grade all or grade section)
     */
    public boolean isUserAbleToGrade(String contextId, String userId);

    /**
     * @param contextId
     * @return true if current user is authorized to view their own
     * grades in the gradebook
     */
    public boolean isCurrentUserAbleToViewOwnGrades(String contextId);

    /**
     * 
     * @param contextId
     * @return true if the current user does not have grading or editing 
     * privileges in the gradebook but does have the view own grades perm
     */
    public boolean isCurrentUserAStudentInGb(String contextId);

    /**
     * @contextId
     * @param studentId
     * @param gradebookItemId
     * @return true if the current user is authorized to grade the given student
     * for the gradebook item with the given gradebookItemId
     */
    public boolean isCurrentUserAbleToGradeStudentForItem(String contextId, String studentId, Long gradebookItemId);

    /**
     * 
     * @param contextId
     * @param studentId
     * @param gbItemId
     * @return AssignmentConstants.GRADE if current user is able to grade this item for this student,
     * AssignmentConstants.VIEW if user is only able to view the grade, and null if
     * the user may not view this student for the given item at all
     */
    public String getGradeViewPermissionForCurrentUserForStudentForItem(String contextId, String studentId, Long gbItemId);

    /**
     * 
     * @param contextId
     * @param studentId
     * @param gradebookItemId
     * @return the grade in the gb for the given gradebookItemId and student. null if no
     * grade or if the gb item does not exist. 
     * @see #getGradeInformationForStudent(String, Long, String) This method is used
     * for retrieving comment info. Look here for thrown exceptions
     */
    public String getStudentGradeForItem(String contextId, String studentId, Long gradebookItemId);

    /**
     * 
     * @param contextId
     * @param studentId
     * @param gradebookItemId
     * @return the grade comment in the gb for the given gradebookItemId and student. null if no
     * comment or if the gb item does not exist.  
     * @see #getGradeInformationForStudent(String, Long, String) This method is used
     * for retrieving grade info. Look here for thrown exceptions
     */
    public String getStudentGradeCommentForItem(String contextId, String studentId, Long gradebookItemId);

    /**
     * 
     * @param contextId not null
     * @param gradebookItemId not null
     * @param studentId not null
     * @return a GradeInformation object containing the grade information from the
     * Gradebook for the given studentId and gradebookItemId.
     * Returns an "empty" GradeInformation object if the gb item does not exist - may
     * indicate it was deleted from the gradebook tool and the a2 link is stale
     * @throws SecurityException if user is not authorized to view grade info for
     * the student for the gb item in the Gradebook tool
     * @throws GradebookNotFoundException if no gradebook exists in the given contextId
     */
    public GradeInformation getGradeInformationForStudent(String contextId, Long gradebookItemId, String studentId);

    /**
     * Create a gradebook item in the gradebook tool with the given information.
     * @param contextId
     * @param title
     * @param pointsPossible
     * @param dueDate
     * @param releasedToStudents - true if this item should be available to students
     * @param countedInCourseGrade - true if grades for this gb item will be included
     * 			in course grade - may only be true if releasedToStudents is true
     * @return id of the newly created gradebook item in the gradebook.
     */
    public Long createGbItemInGradebook(String contextId, String title, Double pointsPossible, Date dueDate,
            boolean releasedToStudents, boolean countedInCourseGrade);
    
    /**
     * @param contextId
     * @param gradebookItem
     * @throws GradebookItemNotFoundException if no gradebook item exists with the given gbItem.gradebookItemId
     */
    public void updateGbItemInGradebook(String contextId, GradebookItem gbItem);


    /**
     * @param contextId
     * @param gradebookItemId
     * @return GradebookItem object associated with the given gradebookItemId
     * in the given contextId in the gradebook tool
     * @throws GradebookItemNotFoundException if no gradebook item exists with the given gradebookItemId
     */
    public GradebookItem getGradebookItemById(String contextId, Long gradebookItemId);

    /**
     * Save the given grade and comment for the given student, gb item, and context
     * in the gradebook. 
     * @param contextId
     * @param gradebookItemId
     * @param studentId
     * @param grade
     * @param comment
     * @throws InvalidGradeForAssignmentException if the grade is invalid according
     * to the gradebook's grade entry type
     * @throws SecurityException if user is not auth to grade the student
     * @throws NoGradebookDataExistsException if there is no gradebook data in the given context
     * @throws GradebookItemNotFoundException if there is no gradebook item with the
     * associated gradebookItemId
     * 
     */
    public void saveGradeAndCommentForStudent(String contextId, Long gradebookItemId, 
            String studentId, String grade, String comment) throws InvalidGradeForAssignmentException;

    /**
     * Given a list of GradeInformation objects with the gradebookGrade and gradebookComment
     * information populated with the info you want to update, will update the
     * grades and comments in the gradebook. GradeInformation must all be for the same
     * gradebook item
     * @param contextId
     * @param gradebookItemId - the id of the associated gradebook item
     * @param gradeInfoList - list of GradeInformation objects populated with
     * the grade and comment you want to update. Note: this will save whatever
     * you have passed for grade AND comment, even if they are null
     * @throws InvalidGradeForAssignmentException if any passed grade is invalid according
     * to the gradebook's grade entry type
     * @throws SecurityException if user is not auth to grade any student in the list
     * @throws NoGradebookDataExistsException if there is no gradebook data in the given context
     * @throws GradebookItemNotFoundException if there is no gradebook item with the
     * associated gradebookItemId
     */
    public void saveGradesAndComments(String contextId, Long gradebookItemId, 
            List<GradeInformation> gradeInfoList);

    /**
     * 
     * @param contextId
     * @return true if gradebook data exists in the given contextId. This does
     * not mean that the tool is in the site, it indicates that the backend data
     * is there for integration
     */
    public boolean gradebookDataExistsInSite(String contextId);

    /**
     * 
     * @param contextId
     * @param studentIdToGradeMap - a map of studentId to the entered grade
     * 			
     * @return a list of studentIds associated with invalid grades according
     * to the gradebook.  returns empty list if all grades are valid
     */
    public List<String> identifyStudentsWithInvalidGrades(String contextId, Map<String, String> studentIdToGradeMap);

    /**
     * 
     * @param contextId
     * @param grade
     * @return true if the given grade is valid for the gradebook. if you are
     * processing more than one student, use getStudentsWithInvalidGrades for
     * efficiency
     */
    public boolean isGradeValid(String contextId, String grade);

    /**
     * 
     * @param studentIdList
     * @param contextId
     * @param gradebookItemId the id of the gradebook item associated with the assignment
     * @param viewOrGrade this method will filter your passed studentIdList either by the
     * students you may view or the students you may grade depending on whether you pass
     * {@link AssignmentConstants#VIEW} or {@link AssignmentConstants#GRADE} here to
     * avoid a SecurityException from the gb tool
     * @return a map of the studentId to the GradeInformation record populated
     * with the student's grade info from the gradebook item associated with the
     * given assignment. if the current user is not allowed to view grade info for a student in the
     * studentIdList, that student will be skipped and not included in the returned map. Every
     * viewable/gradable student will be included in the map even if they do not have a grade or comment yet
     * @throws IllegalArgumentException - if contextId or gradebookItemId are null
     */
    public Map<String, GradeInformation> getGradeInformationForStudents(Collection<String> studentIdList, String contextId, Long gradebookItemId, String viewOrGrade);

    /**
     * 
     * @param contextId
     * @param gradebookItemId
     * @return true if the current user may view this gradebook item in the gradebook
     */
    public boolean isCurrentUserAbleToViewGradebookItem(String contextId, Long gradebookItemId);

    /**
     * Assign the given grade to all students in the list who do not have a grade
     * yet (grade is null or empty string) for this gradebook item. The student list
     * will be filtered by students the current user is allowed to grade prior to assigning the
     * grade.  Ungradable students in the list will be ignored.
     * @param contextId
     * @param gradebookItemId - the id of the gradebook item the assignment is associated with
     * @param studentIds - ids of the students that you would like to assign a grade to if they
     * do not yet have a grade
     * @param grade non-null grade to be saved for all of the students who do not yet have a grade
     * @throws InvalidGradeException if the passed grade is not valid for this gradebook item
     */
    public void assignGradeToUngradedStudents(String contextId, Long gradebookItemId, List<String> studentIds, String grade);

    /**
     * Modify the gradebook item with the given gradebookItemId to release or
     * retract the grade information to students. Note: if the grades are retracted,
     * the grades are also changed to "not counted" in this gradebook item since 
     * grades cannot be counted but not released
     * @param contextId
     * @param gradebookItemId
     * @param release if true, will release grade info. if false, will retract grade info
     * @param includeInCourseGrade if true and release is true, will also include grade info in the course
     * grade. leave null if you do not want to modify this setting
     * @throw {@link GradebookItemNotFoundException} if no gradebook item exists with the given gradebookItemId
     */
    public void releaseOrRetractGrades(String contextId, Long gradebookItemId, boolean release, Boolean includeInCourseGrade);

    /**
     * 
     * @param contextId
     * @return {@link #ENTRY_BY_POINTS}, {@link #ENTRY_BY_PERCENT}, or {@link #ENTRY_BY_LETTER} to represent
     * the grade entry type for the gradebook in the given contextId
     */
    public int getGradebookGradeEntryType(String contextId);

    /**
     * 
     * @param contextId
     * @param gradebookItemId
     * @return the lowest possible grade for the gb item with the given
     * gradebookItemId in the gradebook. for example, it may return 0 or F or null
     * depending on the gradebook and gb item settings
     * @throws GradebookItemNotFoundException if no gradebook item exists with the given gradebookItemId
     */
    public String getLowestPossibleGradeForGradebookItem(String contextId, Long gradebookItemId);

    /**
     * 
     * @param gbItemId
     * @return true if the gradebook item with the given gbItemId exists in the Gradebook.
     * Useful if you want to check if the gradebook item associated with an assignment
     * still exists
     */
    public boolean gradebookItemExists(Long gbItemId);

    /**
     * 
     * @param contextId
     * @param gradebookItemId
     * @return true if the given gradebookItemId may be linked to assignments in the given contextId.
     * for instance, will return false if the gradebook item is "externally maintained" or the
     * gradebook item does not exist in the given context
     */
    public boolean isGradebookItemAssociationValid(String contextId, Long gradebookItemId);
    
    /**
     * 
     * @param userId userId to check for grading privileges. if null, will use current user
     * @param contextId
     * @param gradebookItemId
     * @param viewOrGrade {@link AssignmentConstants#VIEW} or {@link AssignmentConstants#GRADE} to
     * specify if you want the students the user may just grade or if you want all that are viewable
     * in the gradebook
     * @param students list of userIds for the students you want to filter
     * @return a filtered list of students that the given user has permission to view or grade in the gradebook
     * for the given gradebook item depending on your passed viewOrGrade param
     */
    public List<String> getFilteredStudentsForGradebookItem(String userId, String contextId, Long gradebookItemId, String viewOrGrade, Collection<String> students);

    /**
     * 
     * @param contextId
     * @return a map of the Roles in the given contextId to a map of the gradebook permissions
     * to true/false if that role has the given permission
     */
    public Map<Role, Map<String, Boolean>> getGradebookPermissionsForRoles(String contextId);
    
    /**
     * 
     * @param contextId
     * @return a set of the userids for users who are considered students in the gradebook.
     * This could be useful because it is possible for a user to make a submission in Assignment2
     * but not be gradable in the gradebook because of permission discrepancies between the two tools
     */
    public Set<String> getStudentsInGradebook(String contextId);
    
    /**
     * 
     * @param contextId
     * @param userId if null will assume the current user
     * @return true if the given user is a "student" in the context of the gradebook
     * tool. This could be useful because it is possible for a user to make a submission in Assignment2
     * but not be gradable in the gradebook because of permission discrepancies between the two tools
     */
    public boolean isUserAStudentInGradebook(String contextId, String userId);
    
    /**
     * @param gradebookUid
     * @param assignmentTitle
     * @return true if an assignment with the given name already exists in the
     * given gradebook... false otherwise 
     */
    public boolean isAssignmentNameDefinedinGradebook(String gradebookUid, String assignmentTitle);
    
    /**
     * @param gradebookUid
     * @param assignmentTitle
     * @return an assignmentTitle not currently used in the gradebook based on a numerical pattern. 
     * For instance, if the assignmentTitle parameter is sent in as "ted", this method will look to see if 
     * "ted (1)" is used in the gradebook.  If it is not in use it returns that.  If it is in use then 
     * "ted (2)" is checked. And so on and so on until a non in use gradebook assignmentTitle is found.
     */
    public String getFreeAssignmentName(String gradebookUid, String assignmentTitle);

    /**
     * 
     * @param gradebookUid
     * @param gradebookItem
     * @return
     */
    public PointsPossibleValidation isPointsPossibleValid(String gradebookUid, GradebookItem gradebookItem);
    
}
