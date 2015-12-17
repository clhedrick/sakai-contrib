/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/logic/impl/ExternalGradebookLogicImpl.java $
 * $Id: ExternalGradebookLogicImpl.java 87902 2015-12-14 18:01:30Z hedrick@rutgers.edu $
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

package org.sakaiproject.assignment2.logic.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.ConflictingAssignmentNameInGradebookException;
import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.exception.InvalidGradeForAssignmentException;
import org.sakaiproject.assignment2.exception.NoGradebookDataExistsException;
import org.sakaiproject.assignment2.logic.AssignmentAuthzLogic;
import org.sakaiproject.assignment2.logic.ExternalEventLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradeInformation;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.service.gradebook.shared.AssessmentNotFoundException;
import org.sakaiproject.service.gradebook.shared.Assignment;
import org.sakaiproject.service.gradebook.shared.ConflictingAssignmentNameException;
import org.sakaiproject.service.gradebook.shared.GradeDefinition;
import org.sakaiproject.service.gradebook.shared.GradebookFrameworkService;
import org.sakaiproject.service.gradebook.shared.GradebookNotFoundException;
import org.sakaiproject.service.gradebook.shared.GradebookService;
import org.sakaiproject.service.gradebook.shared.InvalidGradeException;
import org.sakaiproject.service.gradebook.shared.GradebookService.PointsPossibleValidation;
import org.sakaiproject.site.api.Group;

/**
 * This is the implementation for logic to interact with the Sakai
 * Gradebook tool
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class ExternalGradebookLogicImpl implements ExternalGradebookLogic {

    private static Log log = LogFactory.getLog(ExternalGradebookLogicImpl.class);

    public void init() {
        if (log.isDebugEnabled()) log.debug("init");
    }

    private GradebookService gradebookService;
    public void setGradebookService(GradebookService gradebookService) {
        this.gradebookService = gradebookService;
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    
    private AssignmentAuthzLogic authzLogic;
    public void setAssignmentAuthzLogic(AssignmentAuthzLogic authzLogic) {
        this.authzLogic = authzLogic;
    }
    
    private ExternalEventLogic eventLogic;
    public void setEventLogic(ExternalEventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }

    public void createGradebookDataIfNecessary(String contextId) {
        // we need to check if there is gradebook data defined for this site. if not,
        // create it (but will not actually add the tool, just the backend)

        GradebookFrameworkService frameworkService = (org.sakaiproject.service.gradebook.shared.GradebookFrameworkService) 
        ComponentManager.get("org.sakaiproject.service.gradebook.GradebookFrameworkService");
        if (!frameworkService.isGradebookDefined(contextId)) {
            if (log.isInfoEnabled()) 
                log.info("Gradebook data being added to context " + contextId + " by Assignment2 tool");
            frameworkService.addGradebook(contextId, contextId);
        }
    }

    public List<GradebookItem> getViewableGradebookItems(String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getViewableGradebookItemIdTitleMap");
        }

        List<Assignment> gbAssignments = gradebookService.getViewableAssignmentsForCurrentUser(contextId);
        List<GradebookItem> viewableGbItems = new ArrayList<GradebookItem>();

        if (gbAssignments != null) {
            for (Assignment assign : gbAssignments) {
                if (assign != null && !assign.isExternallyMaintained()) {
                    viewableGbItems.add(getGradebookItem(assign));
                }
            }
        }

        return viewableGbItems;
    }

    public List<GradebookItem> getAllGradebookItems(String contextId, boolean includeExternallyMaintained) {
        if (contextId == null) {
            throw new IllegalArgumentException("null contextId passed to getAllGradebookItems");
        }

        List<GradebookItem> gradebookItems = new ArrayList<GradebookItem>();

        try {
            List<Assignment> allGbItems = gradebookService.getAssignments(contextId);

            if (allGbItems != null) {

                for (Assignment assign : allGbItems) {
                    if (assign != null && (!assign.isExternallyMaintained() || includeExternallyMaintained)) {
                        GradebookItem item = getGradebookItem(assign);

                        gradebookItems.add(item);
                    }
                }
            }
        } catch (SecurityException se) {
            throw new SecurityException("User without edit or grade perm attempted to access the list of all gb items.", se);
        } catch (GradebookNotFoundException gnfe) {
            throw new NoGradebookDataExistsException("No gradebook exists for the given contextId: " + contextId, gnfe);
        }

        return gradebookItems;
    }

    public List<Group> getViewableGroupsInGradebook(String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getViewableGroupIdToTitleMap");
        }

        List<Group> viewableGroups = new ArrayList<Group>();

        Collection<Group> allGroupsInSite = externalLogic.getSiteGroups(contextId);
        if (allGroupsInSite != null && !allGroupsInSite.isEmpty()) {
            // let's identify the groups that the current user is allowed to
            // view in the gradebook. this method returns the sectionUid (group reference)
            Map<String,String> sectionUidToNameMap =  gradebookService.getViewableSectionUuidToNameMap(contextId);
            if (sectionUidToNameMap != null && !sectionUidToNameMap.isEmpty()) {
                for (Group siteGroup : allGroupsInSite) {
                    if (sectionUidToNameMap.containsKey(siteGroup.getReference())) {
                        viewableGroups.add(siteGroup);
                    }
                }
            }
        }

        return viewableGroups;
    }

    public Map<String, String> getViewableStudentsForGradedItemMap(String userId, String contextId, Long gradebookItemId) {
        if (contextId == null || userId == null) {
            throw new IllegalArgumentException("Null contextId or userId passed to " +
                    "getViewableGroupIdToTitleMap. contextId: " + contextId +
                    " userId:" + userId);
        }

        Map<String, String> studentIdAssnFunctionMap = new HashMap<String, String>();


        Map<String, String> studentIdGbFunctionMap = gradebookService.getViewableStudentsForItemForUser(userId, contextId, gradebookItemId);

        if (studentIdGbFunctionMap != null) {
            for (Map.Entry<String, String> entry : studentIdGbFunctionMap.entrySet()) {
                String studentId = entry.getKey();
                String function = entry.getValue();
                if (studentId != null && function != null) {
                    if (function != null) {
                        if (function.equals(GradebookService.gradePermission)) {
                            studentIdAssnFunctionMap.put(studentId, AssignmentConstants.GRADE);
                        } else {
                            studentIdAssnFunctionMap.put(studentId, AssignmentConstants.VIEW);
                        }
                    }
                }
            }
        }

        return studentIdAssnFunctionMap;
    }
    
    public List<String> getGradableStudentsForGradebookItem(String userId, String contextId, Long gradebookItemId) {
        if (userId == null || contextId == null || gradebookItemId == null) {
            throw new IllegalArgumentException("Null userId (" + userId + "), contextId (" + "), " +
                    "or gradebookItemId (" + gradebookItemId + ") passed to getGradableStudentsForGradebookItem");
        }
        
        Map<String, String> studentIdViewGradeMap = 
            getViewableStudentsForGradedItemMap(userId, contextId, gradebookItemId);
        List<String> gradableStudents = new ArrayList<String>();
        if (studentIdViewGradeMap != null) {
            for (Map.Entry<String, String> entry : studentIdViewGradeMap.entrySet()) {
                String studentId = entry.getKey();
                String viewOrGrade = entry.getValue();
                
                if (viewOrGrade != null && viewOrGrade.equals(AssignmentConstants.GRADE)) {
                    gradableStudents.add(studentId);
                }
            }
        }
        
        return gradableStudents;
    }

    public boolean isCurrentUserAbleToEdit(String contextId) {
        return gradebookService.currentUserHasEditPerm(contextId);
    }

    public boolean isCurrentUserAbleToGradeAll(String contextId) {
        return gradebookService.currentUserHasGradeAllPerm(contextId);
    }

    public boolean isUserAbleToGradeAll(String contextId, String userId) {
        return gradebookService.isUserAllowedToGradeAll(contextId, userId); 
    }

    public boolean isCurrentUserAbleToGrade(String contextId) {
        return gradebookService.currentUserHasGradingPerm(contextId);
    }

    public boolean isUserAbleToGrade(String contextId, String userId) {
        return gradebookService.isUserAllowedToGrade(contextId, userId); 
    }

    public boolean isCurrentUserAbleToViewOwnGrades(String contextId) {
        return gradebookService.currentUserHasViewOwnGradesPerm(contextId);
    }

    public boolean isCurrentUserAStudentInGb(String contextId) {
        boolean userIsAStudentInGb = false;

        if (isCurrentUserAbleToViewOwnGrades(contextId) &&
                !isCurrentUserAbleToGrade(contextId) &&
                !isCurrentUserAbleToEdit(contextId)) {
            userIsAStudentInGb = true;
        }

        return userIsAStudentInGb;
    }

    public String getGradeViewPermissionForCurrentUserForStudentForItem(String contextId, String studentId, Long gbItemId) {
        if (contextId == null || studentId == null || gbItemId == null) {
            throw new IllegalArgumentException("Null contextId or studentId or itemId passed to getGradeViewPermissionForCurrentUserForStudentForItem");
        }

        String viewOrGrade = null;

        String function =
            gradebookService.getGradeViewFunctionForUserForStudentForItem(contextId, gbItemId, studentId);

        if (function == null) {
            viewOrGrade = null;
        } else if (function.equals(GradebookService.gradePermission)) {
            viewOrGrade = AssignmentConstants.GRADE;
        } else if (function.equals(GradebookService.viewPermission)) {
            viewOrGrade = AssignmentConstants.VIEW;
        }

        return viewOrGrade;
    }

    public String getStudentGradeForItem(String contextId, String studentId, Long gradebookItemId) {
        if (contextId == null || studentId == null || gradebookItemId == null) {
            throw new IllegalArgumentException("Null contextId or studentId or gbItemId passed to getStudentGradeForItem");
        }

        String grade = null;

        GradeInformation gradeInfo = getGradeInformationForStudent(contextId, gradebookItemId, studentId);
        if (gradeInfo != null) {
            grade = gradeInfo.getGradebookGrade();
        }

        return grade;
    }

    public String getStudentGradeCommentForItem(String contextId, String studentId, Long gradebookItemId) {
        if (contextId == null || studentId == null || gradebookItemId == null) {
            throw new IllegalArgumentException("Null contextId or studentId or gbItemId passed to getStudentGradeCommentForItem");
        }

        String comment = null;

        GradeInformation gradeInfo = getGradeInformationForStudent(contextId, gradebookItemId, studentId);
        if (gradeInfo != null) {
            comment = gradeInfo.getGradebookComment();
        }

        return comment;
    }

    public boolean isCurrentUserAbleToGradeStudentForItem(String contextId, String studentId, Long gradebookItemId) {
        if (contextId == null || studentId == null || gradebookItemId == null) {
            throw new IllegalArgumentException("null contextId, studentId or gradebookItemId " +
                    "passed to isCurrentUserAbleToGradeStudentForItem. contextId:" + contextId +
                    " studentId:" + studentId + " gradebookItemId: " + gradebookItemId);
        }

        return gradebookService.isUserAbleToGradeItemForStudent(contextId, gradebookItemId, studentId);
    }

    public GradeInformation getGradeInformationForStudent(String contextId, Long gradebookItemId, String studentId) {
        if (contextId == null || gradebookItemId == null || studentId == null) {
            throw new IllegalArgumentException("Null data passed to getGradeInformationForStudent. contextId: " + contextId +
                    " gradebookItemId: " + gradebookItemId + " studentId:" + studentId);
        }

        GradeInformation gradeInfo = new GradeInformation();
        gradeInfo.setStudentId(studentId);

        try {
            GradeDefinition gradeDef = gradebookService.getGradeDefinitionForStudentForItem(contextId, gradebookItemId, studentId);

            if (gradeDef != null) {
                gradeInfo.setGradebookGrade(gradeDef.getGrade());
                gradeInfo.setGradebookGradeReleased(gradeDef.isGradeReleased());
                gradeInfo.setGradebookComment(gradeDef.getGradeComment());
            }

        } catch (AssessmentNotFoundException anfe) {
            // this gb item no longer exists, so there is no information to populate
            if (log.isDebugEnabled()) log.debug("gb item with id " + gradebookItemId + " no longer exists, so returning null grade info");
        } catch (SecurityException se) {
            throw new SecurityException("User does not have authorization to access " +
                    "grade information for " + studentId + " for gb item " + 
                    gradebookItemId, se);
        } catch (GradebookNotFoundException gnfe) {
            throw new NoGradebookDataExistsException(
                    "No gradebook exists for the given contextId: " + contextId, gnfe);
        }

        return gradeInfo;
    }

    public Long createGbItemInGradebook(String contextId, String title, Double pointsPossible, Date dueDate,
            boolean releasedToStudents, boolean countedInCourseGrade) {
        if (contextId == null || title == null) {
            throw new IllegalArgumentException("Null contextId or title passed to createGbItemInGradebook");
        }

        if (countedInCourseGrade && !releasedToStudents) {
            throw new IllegalArgumentException("You may not count an item in course" +
            " grade without releasing to students.");
        }

        Long gradebookItemId = null;
        Assignment newItem = new Assignment();
        newItem.setCounted(countedInCourseGrade);
        newItem.setDueDate(dueDate);
        newItem.setName(title);
        newItem.setPoints(pointsPossible);
        newItem.setReleased(releasedToStudents);

        if (pointsPossible == null) {
            newItem.setUngraded(true);
        } else {
            newItem.setUngraded(false);
        }

        gradebookService.addAssignment(contextId, newItem); 
        if (log.isDebugEnabled()) log.debug("New gradebook item added to gb via assignment2 tool");

        // now let's retrieve the id of this newly created item
        Assignment newlyCreatedAssign = gradebookService.getAssignment(contextId, title);
        if (newlyCreatedAssign != null) {
            gradebookItemId = newlyCreatedAssign.getId();
        }

        return gradebookItemId;
    }

    @Override
    public void updateGbItemInGradebook(String contextId, GradebookItem gbItem) {
        
        if (contextId == null || gbItem == null){
            throw new IllegalArgumentException ("Null contextId or gradebookItem " +
                    "passed to updateGbItemInGradebook. contextId:" +
                    contextId + " gradebookItem:" + gbItem);
        }
        
        Assignment assignmentGbItem = null;

        try {
            assignmentGbItem = gradebookService.getAssignment(contextId, gbItem.getGradebookItemId());
        } catch (AssessmentNotFoundException anfe) {
            throw new GradebookItemNotFoundException ("No gradebook item exists with gradebookItemId " 
                    + gbItem.getGradebookItemId() + " in context " + contextId, anfe);
        }
        
        assignmentGbItem.setDueDate(gbItem.getDueDate());
        
       if (isPointsPossibleValid(contextId, gbItem)  == PointsPossibleValidation.VALID) {
            assignmentGbItem.setPoints(gbItem.getPointsPossible());
        }

        String oldName = assignmentGbItem.getName();
        
        
        
        if (gbItem.getTitle() != null) {
            assignmentGbItem.setName(gbItem.getTitle());
        }
        
        try {
            if (! oldName.equalsIgnoreCase(assignmentGbItem.getName()) && 
                    gradebookService.isAssignmentDefined(contextId, assignmentGbItem.getName())) {
                throw new ConflictingAssignmentNameException("error");
            }

        } catch (ConflictingAssignmentNameException cane) {
            throw new ConflictingAssignmentNameInGradebookException("conflicting gradebook name " + assignmentGbItem.getName());
        }

        if (oldName != null) {
            gradebookService.updateAssignment(contextId, assignmentGbItem.getId(), assignmentGbItem);
        }
        
    }
    


    public GradebookItem getGradebookItemById(String contextId, Long gradebookItemId) {
        if (contextId == null || gradebookItemId == null) {
            throw new IllegalArgumentException ("Null contextId or gradebookItemId " +
                    "passed to getGradebookItemById. contextId:" +
                    contextId + " gradebookItemId:" + gradebookItemId);
        }

        GradebookItem gradebookItem = new GradebookItem();

        try {
            Assignment assign = gradebookService.getAssignment(contextId, gradebookItemId);
            gradebookItem = getGradebookItem(assign);
        } catch (AssessmentNotFoundException anfe) {
            throw new GradebookItemNotFoundException ("No gradebook item exists with gradebookItemId " 
                    + gradebookItemId + " in context " + contextId, anfe);
        }

        return gradebookItem;
    }

    public void saveGradeAndCommentForStudent(String contextId, Long gradebookItemId, String studentId, String grade, String comment) {
        if (contextId == null || gradebookItemId == null || studentId == null) {
            throw new IllegalArgumentException("Null contextId or gradebookItemId " +
                    "or studentId passed to saveGradeAndCommentForStudent. contextId:" + 
                    contextId + " studentId:" + studentId + " gradebookItemId:" + gradebookItemId);
        }

        try {
            gradebookService.saveGradeAndCommentForStudent(contextId, gradebookItemId, studentId, grade, comment);
            if(log.isDebugEnabled()) log.debug("Grade and comment for student " + studentId + 
                    " for gbItem " + gradebookItemId + "updated successfully");
        } catch (GradebookNotFoundException gnfe) {
            throw new NoGradebookDataExistsException("No gradebook exists in the given context "
                    + contextId, gnfe);
        } catch (AssessmentNotFoundException anfe) {
            throw new GradebookItemNotFoundException("No gradebook item exists with the given id "
                    + gradebookItemId, anfe);
        } catch (InvalidGradeException ige) {
            throw new InvalidGradeForAssignmentException("The grade: " + grade + 
                    " for gradebook " + contextId + " is invalid.", ige);
        } catch (SecurityException se) {
            throw new SecurityException(
                    "The current user attempted to saveGradeAndCommentForStudent "
                    + "without authorization. Error: " + se.getMessage(), se);
        }
    }

    public Map<String, GradeInformation> getGradeInformationForStudents(Collection<String> studentIdList, String contextId, Long gradebookItemId, String viewOrGrade) {
        if (contextId == null || gradebookItemId == null) {
            throw new IllegalArgumentException("null contextId or gradebookItemId " +
                    "passed to getGradeInformationForStudents. contextId:" + 
                    contextId + " gradebookItemId:" + gradebookItemId);
        }
        
        if (viewOrGrade == null || (!AssignmentConstants.VIEW.equals(viewOrGrade) && !AssignmentConstants.GRADE.equals(viewOrGrade))) {
            throw new IllegalArgumentException("Invalid value passed to getGradeInformationForStudents. viewOrGrade=" + viewOrGrade);
        }

        Map<String, GradeInformation> studentIdToGradeInfoMap = new HashMap<String, GradeInformation>();

        if (studentIdList != null && !studentIdList.isEmpty()) {
            // let's retrieve a map of the students and their grades
            Map<String, GradeDefinition> studentIdToGradeDefMap = new HashMap<String, GradeDefinition>();
            
            // first, we need to filter the studentIdList passed in to only include students the
            // curr user is allowed to view or grade
            studentIdList = getFilteredStudentsForGradebookItem(null, contextId, gradebookItemId, viewOrGrade, studentIdList);

            try {
                List<GradeDefinition>gradeDefs = gradebookService.getGradesForStudentsForItem(contextId, 
                        gradebookItemId, new ArrayList<String>(studentIdList));

                if (gradeDefs != null) {
                    for (GradeDefinition gradeDef : gradeDefs) {
                        studentIdToGradeDefMap.put(gradeDef.getStudentUid(), gradeDef);
                    }
                }
            } catch (SecurityException se) {
                throw new SecurityException("User attempted to access a list of student grades" +
                        " that he/she did not have authorization for in the gb.", se);
            }

            for (String studentId : studentIdList) {

                GradeInformation gradeInfo = new GradeInformation();
                gradeInfo.setStudentId(studentId);
                gradeInfo.setGradebookItemId(gradebookItemId);

                // get the GradeDefinition for this student and convert it to
                // our local GradeInformation object
                GradeDefinition gradeDef = studentIdToGradeDefMap.get(studentId);
                if (gradeDef != null) {
                    gradeInfo.setGradebookComment(gradeDef.getGradeComment());
                    gradeInfo.setGradebookGrade(gradeDef.getGrade());
                    gradeInfo.setGradebookGradeReleased(gradeDef.isGradeReleased());
                } 

                studentIdToGradeInfoMap.put(studentId, gradeInfo);
            }
        }

        return studentIdToGradeInfoMap;
    }

    public void saveGradesAndComments(String contextId, Long gradebookItemId, 
            List<GradeInformation> gradeInfoList) {
        if (contextId == null || gradebookItemId == null) {
            throw new IllegalArgumentException("Null contextId or gradebookItemId " +
                    "passed to saveGradesAndCommentsForSubmissions. contextId:" + contextId +
                    " gradebookItemId:" + gradebookItemId);
        }

        List<GradeDefinition> gradeDefList = new ArrayList<GradeDefinition>();

        if (gradeInfoList != null) {
            for (GradeInformation gradeInfo : gradeInfoList) {
                if (gradeInfo != null) {
                    // double check that we weren't passed submissions for 
                    // different assignments - we don't want to update the wrong grades!
                    Long assignGo = gradeInfo.getGradebookItemId();
                    if (assignGo == null ||	!assignGo.equals(gradebookItemId)) {
                        throw new IllegalArgumentException("The given submission's gradebookItemId " + assignGo +
                                " does not match the goId passed to saveGradesAndCommentsForSubmissions: " + gradebookItemId);
                    }

                    // convert the info into a GradeDefinition
                    GradeDefinition gradeDef = new GradeDefinition();
                    gradeDef.setGrade(gradeInfo.getGradebookGrade());
                    gradeDef.setGradeComment(gradeInfo.getGradebookComment());
                    gradeDef.setStudentUid(gradeInfo.getStudentId());

                    gradeDefList.add(gradeDef);
                } 
            }

            // now try to save the new information
            try {
                gradebookService.saveGradesAndComments(contextId, gradebookItemId, gradeDefList);
                if(log.isDebugEnabled()) log.debug("Grades and comments for contextId " + contextId + 
                        " for gbItem " + gradebookItemId + "updated successfully for " + gradeDefList.size() + " students");
            } catch (GradebookNotFoundException gnfe) {
                throw new NoGradebookDataExistsException(
                        "No gradebook exists in the given context " + contextId, gnfe);
            } catch (AssessmentNotFoundException anfe) {
                throw new GradebookItemNotFoundException(
                        "No gradebook item exists with the given id " + gradebookItemId, anfe);
            } catch (InvalidGradeException ige) {
                throw new InvalidGradeForAssignmentException("Attempted to save an invalid grade in gradebook " 
                        + contextId + " via saveGradesAndCommentsForSubmissions. " +
                        "No grade information was updated.", ige);
            } catch (SecurityException se) {
                throw new SecurityException(
                        "The current user attempted to saveGradeAndCommentForStudent "
                        + "without authorization. Error: " + se.getMessage(), se);
            }
        }
    }

    public boolean gradebookDataExistsInSite(String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to gradebookDataExistsInSite");
        }

        return gradebookService.isGradebookDefined(contextId);
    }

    public boolean isGradeValid(String contextId, String grade) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to isGradeValid");
        }

        boolean valid = false;

        try {
            valid = gradebookService.isGradeValid(contextId, grade);
        } catch (GradebookNotFoundException gnfe) {
            throw new NoGradebookDataExistsException("No gradebook exists in the given context: "
                    + contextId, gnfe);
        }

        return valid;
    }

    public List<String> identifyStudentsWithInvalidGrades(String contextId, Map<String, String> studentIdToGradeMap) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getStudentsWithInvalidGrades");
        }

        List<String> studentsWithInvalidGrades = new ArrayList<String>();
        if (studentIdToGradeMap != null) {
            try {
                studentsWithInvalidGrades = gradebookService.identifyStudentsWithInvalidGrades(contextId, studentIdToGradeMap);
            } catch (GradebookNotFoundException gnfe) {
                throw new NoGradebookDataExistsException(
                        "No gradebook exists in the given context: " + contextId, gnfe);
            }
        }

        return studentsWithInvalidGrades;
    }

    public boolean isCurrentUserAbleToViewGradebookItem(String contextId, Long gradebookItemId) {
        if (contextId == null || gradebookItemId == null) {
            throw new IllegalArgumentException ("Null parameter passed to " +
                    "sCurrentUserAbleToViewGradebookItem - contextId: " + contextId + 
                    ", gradebookItemId: " + gradebookItemId);
        }

        boolean allowed = false;

        if (isCurrentUserAbleToGradeAll(contextId)) {
            allowed = true;
        } else if (isCurrentUserAbleToGrade(contextId) || isCurrentUserAStudentInGb(contextId)) {
            // check to see if this assignment is among the viewable assign for this user
            List<GradebookItem> viewableGbItems = getViewableGradebookItems(contextId);
            List<Long> gbItemIds = new ArrayList<Long>();
            if (viewableGbItems != null) {
                for (GradebookItem gbItem : viewableGbItems) {
                    gbItemIds.add(gbItem.getGradebookItemId());
                }
            }

            if (gbItemIds.contains(gradebookItemId)) {
                allowed = true;
            }
        } 

        return allowed;
    }

    public void assignGradeToUngradedStudents(String contextId, Long gradebookItemId, List<String> studentIds, String grade) {
        if (contextId == null || gradebookItemId == null) {
            throw new IllegalArgumentException("Null contextId or gradebookItemId passed " +
                    "to assignGradeToUngradedSubmissions. contextId:" + contextId + " gradebookItemId:" + gradebookItemId);
        }

        // no need to continue if they didn't pass a new grade
        if (grade != null && grade.trim().length() > 0) {
            // first, let's validate the grade
            if (!isGradeValid(contextId, grade)) {
                throw new InvalidGradeForAssignmentException("Invalid grade passed to assignGradeToUngradedSubmissions: " + grade);
            }

            if (studentIds != null && !studentIds.isEmpty()) {
                // now determine which don't have a grade yet
                Map<String, GradeInformation> studentIdGradeInfoMap = getGradeInformationForStudents(studentIds, contextId, gradebookItemId, AssignmentConstants.GRADE);
                List<String> ungradedStudents = new ArrayList<String>();

                if (studentIdGradeInfoMap != null && !studentIdGradeInfoMap.isEmpty()) {
                    for (Map.Entry<String, GradeInformation> entry : studentIdGradeInfoMap.entrySet()) {
                        String studentId = entry.getKey();
                        GradeInformation gradeInfo = entry.getValue();
                        if (studentId != null && gradeInfo != null) {
                            if (gradeInfo.getGradebookGrade() == null || gradeInfo.getGradebookGrade().trim().equals("")) {
                                ungradedStudents.add(studentId);
                            }
                        }
                    }

                    if (!ungradedStudents.isEmpty()) {
                        List<GradeInformation> gradeInfoToSave = new ArrayList<GradeInformation>();
                        for (String ungradedStudent : ungradedStudents) {
                            GradeInformation gradeInfo = studentIdGradeInfoMap.get(ungradedStudent);
                            gradeInfo.setGradebookGrade(grade);
                            gradeInfoToSave.add(gradeInfo);
                        }

                        saveGradesAndComments(contextId, gradebookItemId, gradeInfoToSave);
                    }
                }
            }
        }
    }

    public void releaseOrRetractGrades(String contextId, Long gradebookItemId, boolean release, Boolean includeInCourseGrade) {
        if (gradebookItemId == null || contextId == null) {
            throw new IllegalArgumentException("Null gradebookItemId passed to releaseOrRetractGrades." +
                    "contextId: " + contextId + " gradebookItemId: " + gradebookItemId);
        }

        try {
            Assignment gbAssign = gradebookService.getAssignment(contextId, gradebookItemId);
            gbAssign.setReleased(release);
            if (!release) {
                gbAssign.setCounted(false);
            } else if (includeInCourseGrade != null){
                gbAssign.setCounted(includeInCourseGrade);
            }

            gradebookService.updateAssignment(contextId, gbAssign.getId(), gbAssign);
            if (log.isDebugEnabled()) log.debug("Gradebook setting released updated to " + release);
        } catch (AssessmentNotFoundException anfe) {
            throw new GradebookItemNotFoundException(
                    "No gradebook item exists with the given id " + gradebookItemId, anfe);
        }
        
        /* 
         * ASNN-29 We are triggering an event that will correspond to retracting
         * or releasing all grades when an Instructor clicks that link or 
         * other similar actions/scripts.
         * 
         * TODO FIXME ASNN-699 There doesn't seem to be a way to get an Assignment at this
         * point, we'll have to think about what to actually pass in as a reference
         * and how to get that.
         */
        if (release) {
            eventLogic.postEvent(AssignmentConstants.EVENT_RELEASE_ALL_GRADES, null);
        }
        else {
            eventLogic.postEvent(AssignmentConstants.EVENT_RETRACT_ALL_GRADES, null);
        }
    }

    public int getGradebookGradeEntryType(String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getGradebookGradeEntryType");
        }

        int gradeEntryType;
        
        int gbGradeType = gradebookService.getGradeEntryType(contextId);
        if (gbGradeType == GradebookService.GRADE_TYPE_POINTS) {
            gradeEntryType = ENTRY_BY_POINTS;
        } else if (gbGradeType == GradebookService.GRADE_TYPE_PERCENTAGE) {
            gradeEntryType = ENTRY_BY_PERCENT;
        } else if (gbGradeType == GradebookService.GRADE_TYPE_LETTER) {
            gradeEntryType = ENTRY_BY_LETTER;
        } else {
            // default to points
            log.warn("Unknown grade entry type returned by the gradebook");
            gradeEntryType = ENTRY_BY_POINTS;
        }

        return gradeEntryType;
    }

    public String getLowestPossibleGradeForGradebookItem(String contextId, Long gradebookItemId) {
        if (contextId == null || gradebookItemId == null) {
            throw new IllegalArgumentException("Null contextId and/or gradebookItemId " +
                    "passed to getLowestPossibleGradeForGradebookItem. contextId:" + 
                    contextId + " gradebookItemId:" + gradebookItemId);
        }

        String lowestPossibleGrade = null;
        try {
            lowestPossibleGrade = gradebookService.getLowestPossibleGradeForGbItem(contextId, gradebookItemId);
        } catch (AssessmentNotFoundException anfe) {
            throw new GradebookItemNotFoundException("No gradebook item found with id: " + gradebookItemId, anfe);
        }

        return lowestPossibleGrade;
    }

    public boolean gradebookItemExists(Long gbItemId) {
        boolean gbItemExists = false;
        if (gbItemId != null) {
            gbItemExists = gradebookService.isGradableObjectDefined(gbItemId);
        }

        return gbItemExists;
    }

    public boolean isGradebookItemAssociationValid(String contextId, Long gradebookItemId) {
        boolean valid = false;
        try {
            Assignment assign = gradebookService.getAssignment(contextId, gradebookItemId);
            if (!assign.isExternallyMaintained()) {
                valid = true;
            } 
        } catch (AssessmentNotFoundException anfe) {
            valid = false;
        }

        return valid;
    }
    
    /**
     * 
     * @param gbAssignment
     * @return the {@link GradebookItem} representing the given gradebook {@link Assignment}.
     * Returns null if gbAssignment is null
     */
    private GradebookItem getGradebookItem(Assignment gbAssignment) {
        GradebookItem gbItem = null;
        if (gbAssignment != null) {
            gbItem = new GradebookItem(gbAssignment.getId(), gbAssignment.getName(), 
                    gbAssignment.getPoints(), gbAssignment.getDueDate(), gbAssignment.isReleased(), 
                    gbAssignment.getUngraded());
            
            if (gbAssignment.isExternallyMaintained()) {
                gbItem.setExternalId(gbAssignment.getExternalId());
            }
        }
        
        return gbItem;
    }
    
    public List<String> getFilteredStudentsForGradebookItem(String userId, String contextId, Long gradebookItemId, String viewOrGrade, Collection<String> students) {
        if (contextId == null || gradebookItemId == null) {
            throw new IllegalArgumentException("Null contextId (" + contextId + ") or gradebookItemId (" + ") passed to filterGradableStudents");
        }
        
        if (viewOrGrade == null || (!viewOrGrade.equals(AssignmentConstants.VIEW) && 
                !viewOrGrade.equals(AssignmentConstants.GRADE))) {
            throw new IllegalArgumentException("Invalid valid passed for viewOrGrade to filterStudentsForGradebookItem: " + viewOrGrade);
        }
        
        if (userId == null) {
            userId = externalLogic.getCurrentUserId();
        }
        
        List<String> filteredStudents = new ArrayList<String>();
        if (students != null && !students.isEmpty()) {
            Map<String, String> studentViewGradeMap = getViewableStudentsForGradedItemMap(userId, contextId, gradebookItemId);
            
            // filter the students according to the view/grade param
            if (studentViewGradeMap != null) {
                for (String student : students) {
                    if (studentViewGradeMap.containsKey(student)) {
                        String permission = studentViewGradeMap.get(student);
                        if (permission != null) {
                            if (viewOrGrade.equals(AssignmentConstants.GRADE) && 
                                    permission.equals(AssignmentConstants.GRADE)) {
                                filteredStudents.add(student);
                            } else if (viewOrGrade.equals(AssignmentConstants.VIEW)){
                                filteredStudents.add(student);
                            }
                        }
                    }
                }
            }
        }
        
        return filteredStudents;
    }
    
    public Map<Role, Map<String, Boolean>> getGradebookPermissionsForRoles(String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getGradebookPermissionsForRoles");
        }
        
        List<String> gradebookPerms = new ArrayList<String>();
        gradebookPerms.add(GB_EDIT);
        gradebookPerms.add(GB_GRADE_ALL);
        gradebookPerms.add(GB_GRADE_SECTION);
        gradebookPerms.add(GB_VIEW_OWN_GRADES);
        gradebookPerms.add(GB_TA);
        
        return authzLogic.getRolePermissionsForSite(contextId, gradebookPerms);
    }
    
    public Set<String> getStudentsInGradebook(String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getStudentsInGradebook");
        }
        
        return authzLogic.getUsersWithPermission(contextId, GB_STUDENT);
    }
    
    public boolean isUserAStudentInGradebook(String contextId, String userId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to isUserAStudentInGradebook");
        }
        
        if (userId == null) {
            userId = externalLogic.getCurrentUserId();
        }
        
        return authzLogic.userHasPermission(userId, contextId, GB_STUDENT);
    }
    
    public boolean isAssignmentNameDefinedinGradebook(String gradebookUid, String assignmentTitle)
    {
        try {
            return gradebookService.isAssignmentDefined(gradebookUid, assignmentTitle);
        } catch (Exception e) {
            return false;
        }        
    }
    
    public String getFreeAssignmentName(String gradebookUid, String assignmentTitle) {
       
        int itemindex = 1;
        String safeAssignmentName = null;
        
        while (isAssignmentNameDefinedinGradebook(gradebookUid, (safeAssignmentName = assignmentTitle + " (" + itemindex + ")") )) {
            itemindex++;
        }
        
        return safeAssignmentName;
    }

    public PointsPossibleValidation isPointsPossibleValid(String gradebookUid, GradebookItem gradebookItem)
    {
        Assignment assignment = gradebookService.getAssignment(gradebookUid, gradebookItem.getGradebookItemId());
        
        return gradebookService.isPointsPossibleValid(gradebookUid, assignment, gradebookItem.getPointsPossible());
    }
    
}
