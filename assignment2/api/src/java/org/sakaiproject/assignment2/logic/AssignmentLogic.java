/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/AssignmentLogic.java $
 * $Id: AssignmentLogic.java 79543 2012-05-07 17:49:46Z dsobiera@indiana.edu $
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

import java.util.List;
import java.util.Map;

import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.logic.utils.Assignment2Utils;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.service.model.AssignmentDefinition;


/**
 * This is the interface for the Assignment object
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public interface AssignmentLogic {
    // sorting information
    public static final String SORT_DIR_ASC = "asc";
    public static final String SORT_DIR_DESC = "desc";
    public static final String SORT_BY_INDEX = "sortIndex";
    public static final String SORT_BY_TITLE = "title";
    public static final String SORT_BY_OPEN = "openDate";
    public static final String SORT_BY_DUE = "dueDate";

    public static final String REDIRECT_ASSIGNMENT_VIEW_ID = "redirectAssignmentViewId";


    /**
     * Create or update an assignment. The contextId must be populated on the assignment
     * @param assignment
     * the assignment to create or update
     * @throws SecurityException -
     * user must have "edit" permission to add or update an assignment
     * @throws NoGradebookItemForGradedAssignmentException - if the
     * assignment is marked as graded but there is no gradebookItemId
     * @throws AssignmentNotFoundException if the id associated with the
     * assignment object does not exist
     * @throws InvalidGradebookItemAssociationException if the gradebook item associated
     * with the assignment is invalid
     */
    public void saveAssignment(Assignment2 assignment);

    /**
     * Saves assignment with option as to whether the Sakai Gradebook should be
     * updated with the title and other metadata. Other than the second 
     * parameter, this follows the same behavior as 
     * {@link AssignmentLogic#saveAssignment(Assignment2)}
     * 
     * @see AssignmentLogic#saveAssignment(Assignment2)
     * @param assignment
     * @param syncGradebook
     */
    public void saveAssignment(Assignment2 assignment, boolean syncGradebook);
    
    /**
     * Delete an Assignment. This method will also remove the following associations:
     * announcements, schedule items, and tags. If an error is encountered while
     * removing an association, the deletion of the assignment will continue. 
     * note: no assignments are actually deleted; the "removed" property
     * will be set to true
     * @param assignment the Assignment to delete
     * @throws SecurityException if user is not allowed to delete this assignment
     */	
    public void deleteAssignment(Assignment2 assignment);

    /**
     * Returns list of Assignment objects that the given user has permission
     * to view or grade. Assignments that the user does not have permission 
     * to view or grade will not be returned. If assignment is graded and
     * associated gb item was deleted, sets the gradebookItemId to null on 
     * the assignment object to flag that it needs attention
     * @param contextId 
     * @return A non-null list of viewable assignments ordered by sort index
     */
    public List<Assignment2> getViewableAssignments(String contextId);

    /**
     * Reorder the assignments in your site. The array of assignment ids must
     * represent all of the assignments in your site
     * @param assignmentIds - an array of Long assignment ids that are ordered in the
     * order that you would like the assignments in the site to appear. 
     * @param contextId 
     * @throws SecurityException if current user does not have permission to edit assignments
     */
    public void reorderAssignments(List<Long> assignmentIds, String contextId);

    /**
     * @param assignmentId
     * @param optionalParameters in special situations, you may need to pass additional information
     * (such as the tag reference) to successfully retrieve the assignment. leave null if this is
     * a normal scenario
     * @return the Assignment2 object with the given id and populate associated
     * data (ie attachments, groups). Also populates ContentReview information, if applicable.
     * Does not include student submission information.
     * @throws AssignmentNotFoundException if no assignment exists with the given id
     * @throws SecurityException if current user is not allowed to access assignment info
     */
    public Assignment2 getAssignmentByIdWithAssociatedData(Long assignmentId, Map<String, Object> optionalParameters);

    /**
     * @param assignmentId
     * @return the Assignment2 object with the given id and populate associated
     * data (ie attachments, groups). Also populates ContentReview information, if applicable.
     * Does not include student submission information.
     * @throws AssignmentNotFoundException if no assignment exists with the given id
     * @throws SecurityException if current user is not allowed to access assignment info
     */
    public Assignment2 getAssignmentByIdWithAssociatedData(Long assignmentId);
    
    /**
     * @param assignmentId
     * @return the Assignment2 object with the given id and populate the
     * associated AssignmentGroups and AssignmentAttachments
     * @throws AssignmentNotFoundException if no assignment exists with the given id
     * @throws SecurityException if the current user is not allowed to view the
     * given assignment
     */
    public Assignment2 getAssignmentById(Long assignmentId);

    /**
     * Uses the open, due, and accept until dates to determine the current status
     * of the given assignment
     * @param assignment
     * @return a constant equivalent to the assignment's status
     */
    public int getStatusForAssignment(Assignment2 assignment);

    /**
     * 
     * @param contextId
     * @param titleToDuplicate
     * @return a new title duplicated from the given titleToDuplicate for the given contextId.
     * The title will be the titleToDuplicate plus a number. 
     * For example, if the title is "Persuasive Essay", the returned title would be "Persuasive Essay 1." 
     * If the title "Persuasive Essay 1" already exists, try "Persuasive Essay 2", etc. 
     * If titleToDuplicate already ends with a space and a number, we will increment 
     * that number. So if we are duplicating a title "Homework 1", the duplicated title would 
     * be "Homework 2" (and if that exists already "Homework 3", etc).  
     * See {@link Assignment2Utils#getVersionedString(String)} for more info
     */
    public String getDuplicatedAssignmentTitle(String contextId, String titleToDuplicate);
    
    /**
     * 
     * @param assignment
     * @param gbIdItemMap
     * @param groupIdToTitleMap
     * @return a populated {@link AssignmentDefinition} object based upon the information
     * provided via the assignment, gbIdItemMap, and groupIdToTitleMap
     */
    public AssignmentDefinition getAssignmentDefinition(Assignment2 assignment, Map<Long, GradebookItem> gbIdItemMap,
            Map<String, String> groupIdToTitleMap);
    
    /**
     * Get all the assignments that are linked to a specific gradebook item. 
     * These will we not be depth populated. 
     * 
     * This is originally intended for Gradebook sync 
     * functionality that updates the title, due date, and points for both the
     * Gradebook and Assignment if they are one-to-one matched.
     * 
     * @param Long gradebookItemId
     * @return List of Assignment2
     */
    public List<Assignment2> getAssignmentsWithLinkedGradebookItemId(Long id);
}
