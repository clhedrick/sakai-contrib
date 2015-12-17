/**********************************************************************************
 * $URL:https://source.sakaiproject.org/contrib/assignment2/trunk/api/logic/src/java/org/sakaiproject/assignment2/logic/AssignmentPermissionLogic.java $
 * $Id:AssignmentPermissionLogic.java 48274 2008-04-23 20:07:00Z wagnermr@iupui.edu $
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sakaiproject.assignment2.exception.SubmissionNotFoundException;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentGroup;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.site.api.Group;

/**
 * This is the interface for logic which is related to the permission 
 * questions in the Assignment2 tool
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public interface AssignmentPermissionLogic {
    
    /**
     * 
     * @param contextId
     * @param permissions a list of the permissions you want to check for in this context.
     * see {@link AssignmentAuthzLogic#getSiteLevelPermissions()} for allowed values.
     * if null, will return all site-level permissions
     * @return a map of the permission to true/false indicating whether the current
     * user has this permission. Only permissions returned by {@link AssignmentAuthzLogic#getSiteLevelPermissions()}
     * will be considered and returned
     */
    public Map<String, Boolean> getPermissionsForSite(String contextId, List<String> permissions);
    
    /**
     * 
     * @param assignments
     * @param permissions a list of the permissions you want to check for each assignment.
     * (see {@link AssignmentAuthzLogic#getAssignmentLevelPermissions()} for allowed values.
     * if null, will return all assignment-level permissions
     * @return a map of the assignment id to a map of the permission to a true/false value indicating
     * whether the current user has that permission for this assignment 
     * (ie <assignmentId <{@link AssignmentConstants#PERMISSION_EDIT_ASSIGNMENTS}, TRUE>).
     * Only permissions returned by {@link AssignmentAuthzLogic#getAssignmentLevelPermissions()}
     * will be considered and returned
     */
    public Map<Long, Map<String, Boolean>> getPermissionsForAssignments(Collection<Assignment2> assignments, Collection<String> permissions);
    
    /**
     * 
     * @param userId if null, will use the current user
     * @param contextId
     * @return true if the given user has permission generically for all groups in the
     * given site
     */
    public boolean isUserAllowedForAllGroups(String userId, String contextId);
    
    /**
     * @param userId userId to check. If null, will retrieve the current user.
     * @param contextId
     * @return true if the user has permission to add assignments in the given context.
     * if the user does not have "all groups" access, he/she must be a member of at least
     * one group to have add permission for this site.
     * Note that this does not mean that a user is allowed to add any assignment. This just
     * answers the general question, "Does this user have any sort of add assignment permission 
     * in this site?"  If you want to know if a user may add a specific assignment, see
     * {@link AssignmentPermissionLogic#isUserAllowedToAddAssignment(String, Assignment2, List, Map)}
     */
    public boolean isUserAllowedToAddAssignments(String userId, String contextId);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param assignment
     * @return true if the user is allowed to add the given assignment. Users without
     * the "all groups" permission are not allowed to add assignments that aren't restricted
     * to that user's groups. If you want an answer to the general question,"Does
     * this user have any sort of add assignment permission in this site?" use
     * {@link AssignmentPermissionLogic#isUserAllowedToAddAssignments(String, String, List)}
     */
    public boolean isUserAllowedToAddAssignment(String userId, Assignment2 assignment);
    
    /**
     * @param userId userId to check. If null, will retrieve the current user.
     * @param assignment
     * @return true if the user has permission to edit the given assignment
     */
    public boolean isUserAllowedToEditAssignment(String userId, Assignment2 assignment);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param contextId
     * @return true if user is allowed to edit all assignments in the
     * given context
     */
    public boolean isUserAllowedToEditAllAssignments(String userId, String contextId);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param assignment
     * @return true if the user is allowed to delete the given assignment. Users without
     * the "all groups" permission are not allowed to delete assignments that aren't restricted
     * to that user's groups. If you want an answer to the general question,"Does
     * this user have any sort of delete assignment permission in this site?" use
     * {@link AssignmentPermissionLogic#isUserAllowedToDeleteAssignments(String, String, List)}
     */
    public boolean isUserAllowedToDeleteAssignment(String userId, Assignment2 assignment);
    
    /**
     * @param userId userId to check. If null, will retrieve the current user.
     * @param contextId
     * @return true if the user has permission to delete assignments in the given context.
     * Note that this does not mean that a user is allowed to delete any assignment. This just
     * answers the general question, "Does this user have any sort of delete assignment permission 
     * in this site?"  If you want to know if a user may delete a specific assignment, see
     * {@link AssignmentPermissionLogic#isUserAllowedToDeleteAssignment(String, Assignment2, List, Map)}
     */
    public boolean isUserAllowedToDeleteAssignments(String userId, String contextId);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param contextId
     * @return true if the user has permission to view assignments in the given context.
     * Note that this does not mean that a user is allowed to view any assignment. This
     * just answers the general question "Does this user have any sort of assignment viewing
     * privileges in this site?" If you want to know if a user may view a specific assignment,
     * see {@link AssignmentPermissionLogic#isUserAllowedToViewAssignment(String, Assignment2, Map)}
     */
    public boolean isUserAllowedToViewAssignments(String userId, String contextId);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param assignment
     * @param optionalParameters in special situations, you may need to pass additional information
     * (such as the tag reference) to answer this question. leave null if you just need
     * the default question answered
     * @return true if the user has permission to view this assignment
     */
    public boolean isUserAllowedToViewAssignment(String userId, Assignment2 assignment, Map<String, Object> optionalParameters);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param contextId
     * @return true if the user is allowed to manage all submissions
     * in the given context (ie view submission, provide feedback, release feedback, etc)
     */
    public boolean isUserAllowedToManageAllSubmissions(String userId, String contextId);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param assignment
     * @return true if the user is allowed to manage submissions (ie view, provide feedback, etc)
     * for the given assignment. If you want an answer to the general question,"Does
     * this user have any sort of submission management permission in this site?" use
     * {@link AssignmentPermissionLogic#isUserAllowedToManageSubmissions(String, String, List)}
     */
    public boolean isUserAllowedToManageSubmissionsForAssignment(String userId, Assignment2 assignment);
    
    /**
     * 
     * @param userId
     * @param assignmentId
     * @return true if the user is allowed to manage submissions (ie view, provide feedback, etc)
     * for the assignment with the given assignmentId. If you want an answer to the general question,"Does
     * this user have any sort of submission management permission in this site?" use
     * {@link AssignmentPermissionLogic#isUserAllowedToManageSubmissions(String, String, List)}
     */
    public boolean isUserAllowedToManageSubmissionsForAssignmentId(String userId, Long assignmentId);
    
    /**
     * @param userId userId to check. If null, will retrieve the current user.
     * @param contextId
     * @return true if the user has permission to manage submissions (ie view, provide feedback, etc) in the given context.
     * Note that this does not mean that a user is allowed to manage any submission in the site. This just
     * answers the general question, "Does this user have any sort of submission management permission 
     * in this site?"  If you want to know if a user may manage submissions for a specific assignment, see
     * {@link AssignmentPermissionLogic#isUserAllowedToManageSubmissionsForAssignment(String, Assignment2, List, Map)}
     */
    public boolean isUserAllowedToManageSubmissions(String userId, String contextId);
    

    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param submissionId
     * @return true if the user is allowed to manage (ie provide feedback for, view, etc) the
     * AssignmentSubmission associated with the given submissionId
     * @throws SubmissionNotFoundException if no submission exists with the given submissionId
     */
    public boolean isUserAllowedToManageSubmission(String userId, Long submissionId);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param studentId
     * @param assignment
     * @return true if the current user is allowed to manage the submission (ie view, provide feedback, etc)
     * for the given studentId and assignment
     */
    public boolean isUserAllowedToManageSubmission(String userId, String studentId, Assignment2 assignment);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param studentId
     * @param assignmentId
     * @return true if the current user is allowed to manage the submission (ie view, provide feedback, etc)
     * for the given studentId and assignmentId
     */
    public boolean isUserAllowedToManageSubmissionForAssignmentId(String userId, String studentId, Long assignmentId);

    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param studentId
     * @param assignmentId
     * @param optionalParameters in special situations, you may need to pass additional information
     * (such as the tag reference) to answer this question. leave null if you just need
     * the default question answered
     * @return true if the user is allowed to view the given student's
     * submission for the given assignment
     */
    public boolean isUserAllowedToViewSubmissionForAssignment(String userId, String studentId, Long assignmentId, Map<String, Object> optionalParameters);

    /**
     * 
     * @param groupIdList
     * 		a list of the group ids for the user's membership
     * @param assignmentGroupSet
     * 		set of AssignmentGroups that the assignments is restricted to
     * @return true if the user is a member of one of the given AssignmentGroup restrictions
     */
    public boolean isUserAMemberOfARestrictedGroup(Collection<String> groupIdList, Collection<AssignmentGroup> assignmentGroups);

    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @return true if the user has at least one of the following instructor-type permissions
     * in the given contextId:
     * {@link AssignmentConstants#PERMISSION_ADD_ASSIGNMENTS},
     * {@link AssignmentConstants#PERMISSION_EDIT_ASSIGNMENTS},
     * {@link AssignmentConstants#PERMISSION_REMOVE_ASSIGNMENTS}, or
     * {@link AssignmentConstants#PERMISSION_MANAGE_SUBMISSIONS} 
     *
     */
    public boolean isUserAllowedToTakeInstructorAction(String userId, String contextId);

    /**
     * @param userId userId that you are retrieving the students for. 
     * if null will use the current user
     * @param assignment
     * @return a list of userIds for the assignment's submitters
     * that this user is allowed to manage (ie view submission, provide feedback, etc)
     */
    public List<String> getViewableStudentsForAssignment(String userId, Assignment2 assignment);

    /**
     * 
     * @param userId if null, will use the current user
     * @param contextId
     * @param assignmentList
     * @return a map of Assignment2 object to a list of userIds for the assignment's submitters
     * that this user is allowed to manage (ie view submission, provide feedback, etc)
     */
    public Map<Assignment2, List<String>> getViewableStudentsForAssignments(String userId, String contextId, List<Assignment2> assignmentList);

    /**
     * 
     * @param userId if null will use the current user
     * @param contextId
     * @return true if the user has submission privileges in the given contextId. To
     * determine if the user is allowed to submit for a specific assignment, see
     * {@link #isUserAllowedToMakeSubmissionForAssignment(String, Assignment2)}
     */
    public boolean isUserAllowedToSubmit(String userId, String contextId);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param assignment
     * @return true if the current user has permission to make a submission for the
     * given assignment. only answers permission question. does not check to see
     * if assignment is open, if student already submitted, etc
     */
    public boolean isUserAllowedToMakeSubmissionForAssignment(String userId, Assignment2 assignment);
    
    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param assignmentId
     * @return true if the current user has permission to make a submission for the
     * given assignment. only answers permission question. does not check to see
     * if assignment is open, if student already submitted, etc
     */
    public boolean isUserAllowedToMakeSubmissionForAssignmentId(String userId, Long assignmentId);

    /**
     * 
     * @param userId userId to check. If null, will retrieve the current user.
     * @param assignmentId
     * @param optionalParameters in special situations, you may need to pass additional information
     * (such as the tag reference) to answer this question. leave null if you just need
     * the default question answered
     * @return true if the current user has access to this assignment. some scenarios that
     * would be false: if user is a student and assignment is restricted to groups outside of student's memberships
     * or not open; if user is TA but is not a member of any of the assignment's restricted groups
     * note: if assignment has been removed, only a student with an existing
     * submission for that assignment may view the assignment. 
     */
    public boolean isUserAllowedToViewAssignmentId(String userId, Long assignmentId, Map<String, Object> optionalParameters);

    /**
     * @param studentId
     * @param assignmentId
     * @return a list of the userIds of users who are able to view the given
     * student's submission(s) for the given assignment. does not include the student
     * as a user who may view the student. 
     */
    public List<String> getUsersAllowedToViewStudentForAssignment(String studentId, Long assignmentId);

    /**
     * @param userId if null, will use the current user
     * @param assignment make sure AssignmentGroups are populated
     * @return a list of Groups that the user is allowed
     * to view.  If the user has {@link AssignmentConstants#PERMISSION_ALL_GROUPS} 
     * in this site, they may view all groups. Otherwise, will return user's own 
     * memberships. The returned groups will be filtered if the assignment itself 
     * is restricted to groups.
     */
    public List<Group> getViewableGroupsForAssignment(String userId, Assignment2 assign);

    /**
     * 
     * @param studentUids
     * @param assignment
     * @return true if the current user is allowed to provide feedback for every
     * student in the given collection for the given assignment
     */
    public boolean isUserAbleToProvideFeedbackForStudents(Collection<String> studentUids, Assignment2 assignment);

    /**
     * 
     * @param contextId - all assignments in the assignmentList must be associated with this contextId
     * @param assignmentList - a collection of Assignment2 objects with the AssignmentGroupSet initialized
     * that you want to filter
     * @return a filtered set of assignments from the given assignmentList that the current user
     * is allowed to view. if an assignment is graded but the associated gb item no longer exists, 
     * it is treated as "ungraded" for determining permission.  if assignment has been removed, only a 
     * student with an existing submission for that assignment may view the assignment
     */
    public List<Assignment2> filterViewableAssignments(String contextId, Collection<Assignment2> assignmentList);
    
    /**
     * 
     * @param contextId
     * @return a set of the userIds of users who have submission privileges in 
     * the given context
     */
    public Set<String> getSubmittersInSite(String contextId);
    
    /**
     * 
     * @param contextId
     * @return true if the current user has site.upd privileges for the given site
     */
    public boolean isUserAllowedToUpdateSite(String contextId);
    
    /**
     * 
     * @return {@link AssignmentAuthzLogic#getAllPermissions()}. This list will be
     * ordered.
     */
    public List<String> getAssignment2PermissionFunctions();
    
    /**
     * 
     * @return a map of the bundle key to its description for the assignment2 permissions
     * (found in permissions.properties)
     */
    public Map<String, String> getAssignment2PermissionDescriptions();
}
