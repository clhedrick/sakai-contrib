/**********************************************************************************
 * $URL:https://source.sakaiproject.org/contrib/assignment2/trunk/impl/logic/src/test/org/sakaiproject/assignment2/logic/test/ExternalGradebookLogicTest.java $
 * $Id:ExternalGradebookLogicTest.java 48274 2008-04-23 20:07:00Z wagnermr@iupui.edu $
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
package org.sakaiproject.assignment2.logic.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.GradeInformation;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.test.Assignment2TestBase;
import org.sakaiproject.assignment2.test.AssignmentTestDataLoad;
import org.sakaiproject.site.api.Group;


/**
 * These tests are a little tricky because many of the methods simply reproduce
 * the gradebook results, and we don't want to implement tests on the gradebook's
 * functionality.
 * 
 * The GradebookService has been mocked to take care of some extremely messy
 * dependencies required to actually integrate the real GradebookService and the
 * gb tables into our tests.
 * 
 * @author michellewagner
 *
 */
public class ExternalGradebookLogicTest extends Assignment2TestBase {

    private static final Log log = LogFactory.getLog(ExternalGradebookLogicTest.class);

    /**
     * @see org.springframework.test.AbstractTransactionalSpringContextTests#onSetUpInTransaction()
     */
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

    }

    

    public void testCreateGradebookDataIfNecessary() {
        // we will skip this test since it relies on ComponentManager

        // let's try to create a gradebook that already exists. nothing should happen
        /*gradebookLogic.createGradebookDataIfNecessary(AssignmentTestDataLoad.CONTEXT_ID);

    	// let's try to actually create a new one
    	String NEW_CONTEXT = "newContext";
    	// double check that it doesn't exist
    	try {
    		gradebookLogic.getAllGradebookItems(NEW_CONTEXT);
    		fail("gradebook already exists!");
    	} catch (GradebookNotFoundException gfe) {}

    	// now let's add it
    	gradebookLogic.createGradebookDataIfNecessary(NEW_CONTEXT);
    	// double check that it works
    	try {
    		gradebookLogic.getAllGradebookItems(NEW_CONTEXT);

    	} catch (GradebookNotFoundException gfe) {
    		fail("gradebook was not added properly!");
    	}*/
    }

    public void testGetViewableGradebookItems() {
        // try a null contextId
        try {
            gradebookLogic.getViewableGradebookItems(null);
            fail("did not catch null contextId passed to testGetViewableGradebookItemIdTitleMap");
        } catch (IllegalArgumentException iae) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // start out as the instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        List<GradebookItem> gbItems = gradebookLogic.getViewableGradebookItems(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(3, gbItems.size());
        for (GradebookItem gbItem : gbItems) {
            if (gbItem.getGradebookItemId().equals(AssignmentTestDataLoad.GB_ITEM1_ID)) {
                assertEquals(gbItem.getTitle(), AssignmentTestDataLoad.GB_ITEM1_NAME);
            } else if (gbItem.getGradebookItemId().equals(AssignmentTestDataLoad.GB_ITEM2_ID)) {
                assertEquals(gbItem.getTitle(), AssignmentTestDataLoad.GB_ITEM2_NAME);
            } else if (gbItem.getGradebookItemId().equals(AssignmentTestDataLoad.GB_ITEM3_ID)) {
                assertEquals(gbItem.getTitle(), AssignmentTestDataLoad.GB_ITEM3_NAME);
            } else {
                fail ("Unknown gb item returned by getViewableGradebookItems");
            }
        }

        // try a ta
        //TODO grader perms
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        gbItems = gradebookLogic.getViewableGradebookItems(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(3, gbItems.size());
        for (GradebookItem gbItem : gbItems) {
            if (gbItem.getGradebookItemId().equals(AssignmentTestDataLoad.GB_ITEM1_ID)) {
                assertEquals(gbItem.getTitle(), AssignmentTestDataLoad.GB_ITEM1_NAME);
            } else if (gbItem.getGradebookItemId().equals(AssignmentTestDataLoad.GB_ITEM2_ID)) {
                assertEquals(gbItem.getTitle(), AssignmentTestDataLoad.GB_ITEM2_NAME);
            } else if (gbItem.getGradebookItemId().equals(AssignmentTestDataLoad.GB_ITEM3_ID)) {
                assertEquals(gbItem.getTitle(), AssignmentTestDataLoad.GB_ITEM3_NAME);
            } else {
                fail ("Unknown gb item returned by getViewableGradebookItems");
            }
        }

        // now try a student - should only get released items
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        gbItems = gradebookLogic.getViewableGradebookItems(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(2, gbItems.size());
        for (GradebookItem gbItem : gbItems) {
            if (gbItem.getGradebookItemId().equals(AssignmentTestDataLoad.GB_ITEM2_ID)) {
                assertEquals(gbItem.getTitle(), AssignmentTestDataLoad.GB_ITEM2_NAME);
            } else if (gbItem.getGradebookItemId().equals(AssignmentTestDataLoad.GB_ITEM3_ID)) {
                assertEquals(gbItem.getTitle(), AssignmentTestDataLoad.GB_ITEM3_NAME);
            } else {
                fail ("Unknown gb item returned by getViewableGradebookItems");
            }
        }

    }

    public void testGetAllGradebookItems() {
        // try passing a null contextId
        try {
            gradebookLogic.getAllGradebookItems(null, false);
            fail("did not catch null contextId passed to getAllGradebookItems");
        } catch (IllegalArgumentException iae) {}

        // start as instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // what if we pass a bad contextId? should throw SecurityException
        try {
            gradebookLogic.getAllGradebookItems(AssignmentTestDataLoad.BAD_CONTEXT, false);
            fail("Did not catch SecurityException for context that doesn't exist yet");
        } catch (SecurityException se) {}

        // should return all gb items for instructor and ta
        List<GradebookItem> allItems = gradebookLogic.getAllGradebookItems(AssignmentTestDataLoad.CONTEXT_ID, false);
        assertEquals(3, allItems.size());

        // switch to TA
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        allItems = gradebookLogic.getAllGradebookItems(AssignmentTestDataLoad.CONTEXT_ID, false);
        assertEquals(3, allItems.size());

        // student should throw security exception
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            gradebookLogic.getAllGradebookItems(AssignmentTestDataLoad.CONTEXT_ID, false);
            fail("Did not catch SecurityException for student accessing all gb items");
        } catch (SecurityException se) {}
    }

    public void testGetViewableGroupsInGradebook() {
        // try null
        try {
            gradebookLogic.getViewableGroupsInGradebook(null);
            fail("did not catch null contextId passed to getViewableGroupIdToTitleMap");
        } catch (IllegalArgumentException iae) {}

        // start as instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try an invalid context
        List<Group> viewableGroups = gradebookLogic.getViewableGroupsInGradebook(AssignmentTestDataLoad.BAD_CONTEXT);
        assertEquals(0, viewableGroups.size());

        viewableGroups = gradebookLogic.getViewableGroupsInGradebook(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(3, viewableGroups.size());
        for (Group group : viewableGroups) {
            if (!group.getId().equals(AssignmentTestDataLoad.GROUP1_NAME) &&
                    !group.getId().equals(AssignmentTestDataLoad.GROUP2_NAME) &&
                    !group.getId().equals(AssignmentTestDataLoad.GROUP3_NAME)) {
                fail("Invalid group returned by getViewableGroupsInGradebook");
            }
        }

        // try the ta
        // TODO grader perms
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        viewableGroups = gradebookLogic.getViewableGroupsInGradebook(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(1, viewableGroups.size());
        for (Group group : viewableGroups) {
            if (!group.getId().equals(AssignmentTestDataLoad.GROUP1_NAME)) {
                fail("Invalid group returned by getViewableGroupsInGradebook");
            }
        }

        // try the student - should not get any groups returned b/c can't grade
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        viewableGroups = gradebookLogic.getViewableGroupsInGradebook(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(0, viewableGroups.size());

    }

    public void testGetViewableStudentsForGradedItemMap() {
        // try null parameters

        try {
            gradebookLogic.getViewableStudentsForGradedItemMap(null, AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("Did not catch null contextId passed to getViewableStudentsForGradedItemMap");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getViewableStudentsForGradedItemMap(AssignmentTestDataLoad.INSTRUCTOR_UID, null, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("Did not catch null contextId passed to getViewableStudentsForGradedItemMap");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getViewableStudentsForGradedItemMap(AssignmentTestDataLoad.INSTRUCTOR_UID, AssignmentTestDataLoad.CONTEXT_ID, null);
            fail("Did not catch null gbItemId passed to getViewableStudentsForGradedItemMap");
        } catch (IllegalArgumentException iae) {}

        // what if we pass a bad contextId? should return 0

        Map<String, String> studentFunctionMap = gradebookLogic.getViewableStudentsForGradedItemMap(AssignmentTestDataLoad.INSTRUCTOR_UID, AssignmentTestDataLoad.BAD_CONTEXT, AssignmentTestDataLoad.GB_ITEM1_ID);
        assertEquals(0, studentFunctionMap.size());

        // what if gb item doesn't exist?
        studentFunctionMap = gradebookLogic.getViewableStudentsForGradedItemMap(AssignmentTestDataLoad.INSTRUCTOR_UID, AssignmentTestDataLoad.CONTEXT_ID, 12345L);
        assertTrue(studentFunctionMap.isEmpty());

        // instructor should be able to view all students
        studentFunctionMap = gradebookLogic.getViewableStudentsForGradedItemMap(AssignmentTestDataLoad.INSTRUCTOR_UID, AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID);
        assertEquals(3, studentFunctionMap.size());
        assertEquals(AssignmentConstants.GRADE, studentFunctionMap.get(AssignmentTestDataLoad.STUDENT1_UID));
        assertEquals(AssignmentConstants.GRADE, studentFunctionMap.get(AssignmentTestDataLoad.STUDENT2_UID));
        assertEquals(AssignmentConstants.GRADE, studentFunctionMap.get(AssignmentTestDataLoad.STUDENT3_UID));

        // TA should only get students in his/her group
        // TODO grader perms
        studentFunctionMap = gradebookLogic.getViewableStudentsForGradedItemMap(AssignmentTestDataLoad.TA_UID, AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID);
        assertEquals(1, studentFunctionMap.size());
        assertEquals(AssignmentConstants.GRADE, studentFunctionMap.get(AssignmentTestDataLoad.STUDENT1_UID));

        // try a student - should get an empty list
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        studentFunctionMap = gradebookLogic.getViewableStudentsForGradedItemMap(AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID);
        assertEquals(0, studentFunctionMap.size());
    }

    public void testIsCurrentUserAbleToEdit() {
        // this should only be true for the instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assertTrue(gradebookLogic.isCurrentUserAbleToEdit(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToEdit(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToEdit(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToEdit(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT3_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToEdit(AssignmentTestDataLoad.CONTEXT_ID));
    }

    public void testIsCurrentUserAbleToGradeAll() {
        // this should only be true for the instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assertTrue(gradebookLogic.isCurrentUserAbleToGradeAll(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToGradeAll(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToGradeAll(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToGradeAll(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT3_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToGradeAll(AssignmentTestDataLoad.CONTEXT_ID));
    }

    public void testIsCurrentUserAbleToGrade() {
        // this should be true for the instructor and ta
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assertTrue(gradebookLogic.isCurrentUserAbleToGrade(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        assertTrue(gradebookLogic.isCurrentUserAbleToGrade(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToGrade(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToGrade(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT3_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToGrade(AssignmentTestDataLoad.CONTEXT_ID));
    }

    public void testIsCurrentUserAbleToViewOwnGrades() {
        // this is true for students
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToViewOwnGrades(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToViewOwnGrades(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        assertTrue(gradebookLogic.isCurrentUserAbleToViewOwnGrades(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        assertTrue(gradebookLogic.isCurrentUserAbleToViewOwnGrades(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT3_UID);
        assertTrue(gradebookLogic.isCurrentUserAbleToViewOwnGrades(AssignmentTestDataLoad.CONTEXT_ID));
    }

    public void testIsCurrentUserAStudentInGb() {
        // this is true for students
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assertFalse(gradebookLogic.isCurrentUserAStudentInGb(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        assertFalse(gradebookLogic.isCurrentUserAStudentInGb(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        assertTrue(gradebookLogic.isCurrentUserAStudentInGb(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        assertTrue(gradebookLogic.isCurrentUserAStudentInGb(AssignmentTestDataLoad.CONTEXT_ID));

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT3_UID);
        assertTrue(gradebookLogic.isCurrentUserAStudentInGb(AssignmentTestDataLoad.CONTEXT_ID));
    }

    public void testIsCurrentUserAbleToGradeStudentForItem() {
        // try some null data
        try {
            gradebookLogic.isCurrentUserAbleToGradeStudentForItem(null, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("did not catch null contextId passed to isCurrentUserAbleToGradeStudentForItem");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.isCurrentUserAbleToGradeStudentForItem(AssignmentTestDataLoad.CONTEXT_ID, null, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("did not catch null studentId passed to isCurrentUserAbleToGradeStudentForItem");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.isCurrentUserAbleToGradeStudentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null gbItemId passed to isCurrentUserAbleToGradeStudentForItem");
        } catch (IllegalArgumentException iae) {}

        // start as instructor
        // should be true for all students
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assertTrue(gradebookLogic.isCurrentUserAbleToGradeStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertTrue(gradebookLogic.isCurrentUserAbleToGradeStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertTrue(gradebookLogic.isCurrentUserAbleToGradeStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT3_UID, AssignmentTestDataLoad.GB_ITEM1_ID));

        // ta should only be able to grade student 1
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        assertTrue(gradebookLogic.isCurrentUserAbleToGradeStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertFalse(gradebookLogic.isCurrentUserAbleToGradeStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertFalse(gradebookLogic.isCurrentUserAbleToGradeStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT3_UID, AssignmentTestDataLoad.GB_ITEM1_ID));

        // student should all be false
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        assertFalse(gradebookLogic.isCurrentUserAbleToGradeStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertFalse(gradebookLogic.isCurrentUserAbleToGradeStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertFalse(gradebookLogic.isCurrentUserAbleToGradeStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT3_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
    }

    public void testGetGradeViewPermissionForCurrentUserForStudentForItem() {
        // try some null parameters
        try {
            gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(null, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("did not catch null contextId passed to getGradeViewPermissionForCurrentUserForStudentForItem");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(AssignmentTestDataLoad.CONTEXT_ID, null, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("did not catch null studentId passed to getGradeViewPermissionForCurrentUserForStudentForItem");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null itemId passed to getGradeViewPermissionForCurrentUserForStudentForItem");
        } catch (IllegalArgumentException iae) {}

        // instructor should be able to grade all
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assertEquals(AssignmentConstants.GRADE, gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertEquals(AssignmentConstants.GRADE, gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertEquals(AssignmentConstants.GRADE, gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT3_UID, AssignmentTestDataLoad.GB_ITEM1_ID));

        // ta should only be able to grade student 1
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        assertEquals(AssignmentConstants.GRADE, gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertNull(gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertNull(gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT3_UID, AssignmentTestDataLoad.GB_ITEM1_ID));

        // student should all be null
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        assertNull(gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertNull(gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
        assertNull(gradebookLogic.getGradeViewPermissionForCurrentUserForStudentForItem(
                AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT3_UID, AssignmentTestDataLoad.GB_ITEM1_ID));
    }

    public void testGetStudentGradeForItem() {
        // try some null parameters
        try {
            gradebookLogic.getStudentGradeForItem(null, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("did not catch null contextId passed to getStudentGradeForItem");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, null, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("did not catch null studentId passed to getStudentGradeForItem");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null itemId passed to getStudentGradeForItem");
        } catch (IllegalArgumentException iae) {}

        // this method basically tests the gradebook (which we don't need to do here),
        // so don't test much

        // start out as instructor
        /*externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
    	// try a bad context first
    	String grade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.BAD_CONTEXT, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertNull(grade);

    	// try a gb item that doesn't exist
    	grade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, 12345L);
    	assertNull(grade);

    	// try a real one
    	grade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(AssignmentTestDataLoad.st1a3Grade.toString(), grade);

    	// switch to the ta
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
    	// should only be able to see student1's grade
    	grade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(AssignmentTestDataLoad.st1a3Grade.toString(), grade);
    	// shouldn't see st2a4grade
    	try {
    		grade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM2_ID);
    		fail("did not catch ta trying to get grade for student without auth");
    	} catch (SecurityException se) {}

    	// switch to student
    	// should only be able to retrieve their own
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
    	grade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	// this gb item isn't released yet, so student should get null grade back
    	assertNull(grade);

    	try {
    		grade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM2_ID);
    		fail("did not catch student trying to get grade for another student without auth");
    	} catch (SecurityException se) {}*/
    }

    public void testGetStudentGradeCommentForItem() {
        // try some null parameters
        try {
            gradebookLogic.getStudentGradeCommentForItem(null, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("did not catch null contextId passed to getStudentGradeCommentForItem");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, null, AssignmentTestDataLoad.GB_ITEM1_ID);
            fail("did not catch null studentId passed to getStudentGradeCommentForItem");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null itemId passed to getStudentGradeCommentForItem");
        } catch (IllegalArgumentException iae) {}

        // this method basically tests the gradebook (which we don't need to do here),
        // so don't test much

        /*
    	// start out as instructor
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
    	// try a bad context first
    	String comment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.BAD_CONTEXT, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertNull(comment);

    	// try a gb item that doesn't exist
    	comment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, 12345L);
    	assertNull(comment);

    	// try a real one
    	comment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(AssignmentTestDataLoad.st1a3Comment, comment);

    	// switch to the ta
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
    	// the gb does not have its own authz checks for retrieving comments, so should be able to retrieve any student
    	comment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(AssignmentTestDataLoad.st1a3Comment, comment);

    	comment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM2_ID);
    	assertEquals(AssignmentTestDataLoad.st2a4Comment, comment);

    	// switch to student
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
    	// the gb does not have its own authz checks for retrieving comments, so should be able to retrieve any student
    	comment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(AssignmentTestDataLoad.st1a3Comment, comment);

    	comment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.GB_ITEM2_ID);
    	assertEquals(AssignmentTestDataLoad.st2a4Comment, comment);*/
    }

    public void testGetGradeInformationForStudents() {
        // try some null parameters
        try {
            gradebookLogic.getGradeInformationForStudents(new ArrayList<String>(), null, testData.a1.getGradebookItemId(), AssignmentConstants.GRADE);
            fail("did not catch null contextId passed to getGradeInformationForStudents");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getGradeInformationForStudents(new ArrayList<String>(), testData.a1.getContextId(), null, AssignmentConstants.GRADE);
            fail("did not catch null gradebookItemId passed to getGradeInformationForStudents");
        } catch (IllegalArgumentException iae) {}

        // try passing a null list
        // should do nothing
        gradebookLogic.getGradeInformationForStudents(null, AssignmentTestDataLoad.CONTEXT_ID, testData.a3.getGradebookItemId(), AssignmentConstants.GRADE);

        Map<String, GradeInformation> studentIdToGradeInfoMap = new HashMap<String, GradeInformation>();

        List<String> studentList = new ArrayList<String>();
        studentList.add(AssignmentTestDataLoad.STUDENT1_UID);
        studentList.add(AssignmentTestDataLoad.STUDENT2_UID);

        // switch to instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        studentIdToGradeInfoMap = gradebookLogic.getGradeInformationForStudents(studentList, testData.a3.getContextId(), testData.a3.getGradebookItemId(), AssignmentConstants.GRADE);
        // verify grades were populated
        GradeInformation gradeInfo = studentIdToGradeInfoMap.get(AssignmentTestDataLoad.STUDENT1_UID);
        assertFalse(gradeInfo.isGradebookGradeReleased());
        assertEquals(AssignmentTestDataLoad.st1a3Grade.toString(), gradeInfo.getGradebookGrade());
        assertEquals(AssignmentTestDataLoad.st1a3Comment, gradeInfo.getGradebookComment());

        gradeInfo = studentIdToGradeInfoMap.get(AssignmentTestDataLoad.STUDENT2_UID);
        assertFalse(gradeInfo.isGradebookGradeReleased());
        assertNull(gradeInfo.getGradebookGrade());
        assertNull(gradeInfo.getGradebookComment());

        // switch to ta
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // st2 should be excluded from the returned map
        studentIdToGradeInfoMap = gradebookLogic.getGradeInformationForStudents(studentList, testData.a3.getContextId(), testData.a3.getGradebookItemId(), AssignmentConstants.GRADE);
        assertFalse(studentIdToGradeInfoMap.containsKey(AssignmentTestDataLoad.STUDENT2_UID));

        // let's only include auth students in list
        studentList = new ArrayList<String>();
        studentList.add(AssignmentTestDataLoad.STUDENT1_UID);
        studentIdToGradeInfoMap = gradebookLogic.getGradeInformationForStudents(studentList, testData.a3.getContextId(), testData.a3.getGradebookItemId(), AssignmentConstants.GRADE);
        // verify grades were populated
        gradeInfo = studentIdToGradeInfoMap.get(AssignmentTestDataLoad.STUDENT1_UID);
        assertFalse(gradeInfo.isGradebookGradeReleased());
        assertEquals(AssignmentTestDataLoad.st1a3Grade.toString(), gradeInfo.getGradebookGrade());
        assertEquals(AssignmentTestDataLoad.st1a3Comment, gradeInfo.getGradebookComment());
    }

    public void testSaveGradeAndCommentForStudent() {
        // try some nulls
        try {
            gradebookLogic.saveGradeAndCommentForStudent(null, AssignmentTestDataLoad.GB_ITEM1_ID, AssignmentTestDataLoad.STUDENT1_UID, null, null);
            fail("did not catch null contextId passed to saveGradeAndCommentForStudent");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.saveGradeAndCommentForStudent(AssignmentTestDataLoad.CONTEXT_ID, null, AssignmentTestDataLoad.STUDENT1_UID, null, null);
            fail("did not catch null gradebookItemId passed to saveGradeAndCommentForStudent");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.saveGradeAndCommentForStudent(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, null, null, null);
            fail("did not catch null studentId passed to saveGradeAndCommentForStudent");
        } catch (IllegalArgumentException iae) {}

        // this method basically tests the gradebook (which we don't need to do here),
        // so don't test much

        // try a bad contextId
        /*try {
    		gradebookLogic.saveGradeAndCommentForStudent(AssignmentTestDataLoad.BAD_CONTEXT, AssignmentTestDataLoad.GB_ITEM1_ID, AssignmentTestDataLoad.STUDENT1_UID, null, null);
    		fail("did not catch invalid contextId passed to saveGradeAndCommentForStudent");
    	} catch (NoGradebookDataExistsException ngdee) {}

    	// try a bad gradebookItemId
    	try {
    		gradebookLogic.saveGradeAndCommentForStudent(AssignmentTestDataLoad.CONTEXT_ID, 12345L, AssignmentTestDataLoad.STUDENT1_UID, null, null);
    		fail("did not catch invalid gradebookItemId passed to saveGradeAndCommentForStudent");
    	} catch (GradebookItemNotFoundException gine) {}



    	// start as instructor
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
    	String grade = "15.0";
    	String comment = "Good work";

    	gradebookLogic.saveGradeAndCommentForStudent(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, AssignmentTestDataLoad.STUDENT1_UID, grade, comment);
    	// now retrieve it to make sure it stuck
    	String dbGrade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(grade, dbGrade);
    	String dbComment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(comment, dbComment);

    	// try an invalid grade
    	try {
    		gradebookLogic.saveGradeAndCommentForStudent(AssignmentTestDataLoad.CONTEXT_ID, 
    			AssignmentTestDataLoad.GB_ITEM1_ID, AssignmentTestDataLoad.STUDENT1_UID, "A+", "BAD COMMENT");
    		fail("Did not catch invalid grade passed to saveGradeAndCommentForStudent");
    	} catch (InvalidGradeForAssignmentException igfae) {}
    	// now retrieve it to make sure it didn't do an update
    	dbGrade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(grade, dbGrade);
    	dbComment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(comment, dbComment);

    	// switch to TA
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
    	// should be able to update st1
    	grade = "25.87";
    	comment = "Jolly good show!";
    	gradebookLogic.saveGradeAndCommentForStudent(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, AssignmentTestDataLoad.STUDENT1_UID, grade, comment);
    	// now retrieve it to make sure it stuck
    	dbGrade = gradebookLogic.getStudentGradeForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(grade, dbGrade);
    	dbComment = gradebookLogic.getStudentGradeCommentForItem(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.GB_ITEM1_ID);
    	assertEquals(comment, dbComment);

    	// but should not be able to update st2
    	try {
    		gradebookLogic.saveGradeAndCommentForStudent(AssignmentTestDataLoad.CONTEXT_ID, 
        			AssignmentTestDataLoad.GB_ITEM1_ID, AssignmentTestDataLoad.STUDENT2_UID, grade, comment);
        		fail("Did not catch ta trying to update grades w/o auth in saveGradeAndCommentForStudent");
    	} catch (SecurityException se) {}

    	// student's should not have auth
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
    	try {
    		gradebookLogic.saveGradeAndCommentForStudent(AssignmentTestDataLoad.CONTEXT_ID, 
        			AssignmentTestDataLoad.GB_ITEM1_ID, AssignmentTestDataLoad.STUDENT2_UID, grade, comment);
        		fail("Did not catch student trying to update grades w/o auth in saveGradeAndCommentForStudent");
    	} catch (SecurityException se) {}*/
    }

    public void testIsGradeValid() {
        //try a null context
        try {
            gradebookLogic.isGradeValid(null, "A");
            fail("did not catch null contextId passed to isGradeValid");
        } catch (IllegalArgumentException iae) {}

        // this method basically tests the gradebook (which we don't need to do here),
        // so don't test much

        /*
    	// try passing a null grade - should be valid
    	assertTrue(gradebookLogic.isGradeValid(AssignmentTestDataLoad.CONTEXT_ID, null));

    	//if these tests fail, double check the gradebook. we are using assumptions
    	// of valid grades as of 3/08
    	// we are using a default gb, which is points-based
    	assertTrue(gradebookLogic.isGradeValid(AssignmentTestDataLoad.CONTEXT_ID, "98"));
    	assertFalse(gradebookLogic.isGradeValid(AssignmentTestDataLoad.CONTEXT_ID, "A"));*/
    }

    public void testIdentifyStudentsWithInvalidGrades() {
        // try a null context
        try {
            gradebookLogic.identifyStudentsWithInvalidGrades(null, new HashMap<String, String>());
            fail("did not catch null contextId passed to identifyStudentsWithInvalidGrades");
        } catch (IllegalArgumentException iae) {}

        // a null map should return an empty list
        List<String> studentList = gradebookLogic.identifyStudentsWithInvalidGrades(AssignmentTestDataLoad.CONTEXT_ID, null);
        assertTrue(studentList.isEmpty());

        // this method basically tests the gradebook (which we don't need to do here),
        // so don't test much

        // we are using a points-based gb
        /*Map<String, String> studentGradeMap = new HashMap<String, String>();
    	studentGradeMap.put(AssignmentTestDataLoad.STUDENT1_UID, "10");
    	studentGradeMap.put(AssignmentTestDataLoad.STUDENT2_UID, "124.5");

    	studentList = gradebookLogic.identifyStudentsWithInvalidGrades(AssignmentTestDataLoad.CONTEXT_ID, studentGradeMap);
    	assertTrue(studentList.isEmpty()); // should all be valid

    	// try some invalid grades
    	studentGradeMap = new HashMap<String, String>();
    	studentGradeMap.put(AssignmentTestDataLoad.STUDENT1_UID, "-10"); // invalid
    	studentGradeMap.put(AssignmentTestDataLoad.STUDENT2_UID, "124.5"); //valid
    	studentGradeMap.put(AssignmentTestDataLoad.STUDENT3_UID, "A"); // invalid

    	studentList = gradebookLogic.identifyStudentsWithInvalidGrades(AssignmentTestDataLoad.CONTEXT_ID, studentGradeMap);
    	assertEquals(2, studentList.size()); // should have 2 invalid students
    	assertTrue(studentList.contains(AssignmentTestDataLoad.STUDENT1_UID));
    	assertTrue(studentList.contains(AssignmentTestDataLoad.STUDENT3_UID));*/

    }

    public void testSaveGradesAndCommentsForSubmissions() {
        // try null info
        try {
            gradebookLogic.saveGradesAndComments(null, AssignmentTestDataLoad.GB_ITEM1_ID, new ArrayList<GradeInformation>());
            fail("did not catch null contextId passed to saveGradesAndCommentsForSubmissions");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.saveGradesAndComments(AssignmentTestDataLoad.CONTEXT_ID, null, new ArrayList<GradeInformation>());
            fail("did not catch null gradebookItemId passed to saveGradesAndCommentsForSubmissions");
        } catch (IllegalArgumentException iae) {}

        // this method basically tests the gradebook (which we don't need to do here),
        // so don't test much

        // as instructor
        /*externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

    	// try passing a null list - should do nothing
    	gradebookLogic.saveGradesAndCommentsForSubmissions(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, null);

    	// let's refresh the submission objects to make sure the gb associations are there
    	testData.st1a3Submission = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId());
    	testData.st2a3Submission = submissionLogic.getAssignmentSubmissionById(testData.st2a3Submission.getId());

    	List<AssignmentSubmission> subList = new ArrayList<AssignmentSubmission>();
    	// try adding submissions for different assignments
    	subList.add(testData.st1a1Submission);
    	subList.add(testData.st1a3Submission);

    	try {
    		gradebookLogic.saveGradesAndCommentsForSubmissions(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, subList);
    		fail("did not catch a passed submission list from diff assignments");
    	} catch (IllegalArgumentException iae) {}

    	subList = new ArrayList<AssignmentSubmission>();
    	String st1Grade = "15.5";
    	String st1Comment = "Good work";
    	String st2Grade = "0.0";
    	String st2Comment = "You need to turn this in ASAP";
    	testData.st1a3Submission.setGradebookGrade(st1Grade);
    	testData.st1a3Submission.setGradebookComment(st1Comment);
    	testData.st2a3Submission.setGradebookGrade(st2Grade);
    	testData.st2a3Submission.setGradebookComment(st2Comment);

    	subList.add(testData.st1a3Submission);
    	subList.add(testData.st2a3Submission);

    	// should work this time
    	gradebookLogic.saveGradesAndCommentsForSubmissions(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, subList);

    	// now let's retrieve it to see if the save was successful
    	AssignmentSubmission sub1 = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId());
    	assertEquals(st1Grade, sub1.getGradebookGrade());
    	assertEquals(st1Comment, sub1.getGradebookComment());
    	AssignmentSubmission sub2 = submissionLogic.getAssignmentSubmissionById(testData.st2a3Submission.getId());
    	assertEquals(st2Grade, sub2.getGradebookGrade());
    	assertEquals(st2Comment, sub2.getGradebookComment());

    	// try a bad grade - make sure nothing was updated
    	testData.st1a3Submission.setGradebookGrade("10.5");
    	testData.st1a3Submission.setGradebookComment("lovely");
    	testData.st2a3Submission.setGradebookGrade("A");  // INVALID
    	testData.st2a3Submission.setGradebookComment("jolly good show");
    	subList = new ArrayList<AssignmentSubmission>();
    	subList.add(testData.st1a3Submission);
    	subList.add(testData.st2a3Submission);

    	try {
    		gradebookLogic.saveGradesAndCommentsForSubmissions(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, subList);
        	fail("did not catch invalid grade passed to saveGradesAndCommentsForSubmissions");
    	} catch (InvalidGradeForAssignmentException igfae) {}

    	// double check that the grades and comments weren't actually changed
    	sub1 = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId());
    	assertEquals(st1Grade, sub1.getGradebookGrade());
    	assertEquals(st1Comment, sub1.getGradebookComment());
    	sub2 = submissionLogic.getAssignmentSubmissionById(testData.st2a3Submission.getId());
    	assertEquals(st2Grade, sub2.getGradebookGrade());
    	assertEquals(st2Comment, sub2.getGradebookComment());

    	// check security
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
    	// the ta should only be able to update st1
    	subList = new ArrayList<AssignmentSubmission>();
    	st1Grade = "25.52";
    	st1Comment = "graded by ta";
    	testData.st1a3Submission.setGradebookGrade(st1Grade);
    	testData.st1a3Submission.setGradebookComment(st1Comment);
    	subList.add(testData.st1a3Submission);

    	gradebookLogic.saveGradesAndCommentsForSubmissions(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, subList);

    	// now let's retrieve it to see if the save was successful
    	sub1 = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId());
    	assertEquals(st1Grade, sub1.getGradebookGrade());
    	assertEquals(st1Comment, sub1.getGradebookComment());

    	// now try to include st2 in the list - nothing should be updated
    	testData.st1a3Submission.setGradebookGrade("10.5");
    	testData.st1a3Submission.setGradebookComment("lovely");
    	testData.st2a3Submission.setGradebookGrade("11.26"); 
    	testData.st2a3Submission.setGradebookComment("jolly good show");
    	subList = new ArrayList<AssignmentSubmission>();
    	subList.add(testData.st1a3Submission);
    	subList.add(testData.st2a3Submission);

    	try {
    		gradebookLogic.saveGradesAndCommentsForSubmissions(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, subList);
    		fail("did not catch ta trying to grade st2 without auth!");
    	} catch (SecurityException se) {}

    	// make sure students hit security, as well
    	externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
    	try {
    		gradebookLogic.saveGradesAndCommentsForSubmissions(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, subList);
    		fail("did not catch student trying to save grade info without auth!");
    	} catch (SecurityException se) {}*/
    }

    public void testGetGradeInformationForStudent() {
        // let's try some nulls
        try {
            gradebookLogic.getGradeInformationForStudent(null, 123L, AssignmentTestDataLoad.STUDENT1_UID);
            fail("did not catch null contextId passed to getGradeInformationForStudent");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.getGradeInformationForStudent(AssignmentTestDataLoad.CONTEXT_ID, null, AssignmentTestDataLoad.STUDENT1_UID);
            fail("did not catch null gb item id passed to getGradeInformationForStudent");
        } catch (IllegalArgumentException iae) {}
        
        try {
            gradebookLogic.getGradeInformationForStudent(AssignmentTestDataLoad.CONTEXT_ID, 123L, null);
            fail("did not catch null student id passed to getGradeInformationForStudent");
        } catch (IllegalArgumentException iae) {}

        // become an instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        GradeInformation gradeInfo = gradebookLogic.getGradeInformationForStudent(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.GB_ITEM1_ID, AssignmentTestDataLoad.STUDENT1_UID);
        assertFalse(gradeInfo.isGradebookGradeReleased());
        assertEquals(AssignmentTestDataLoad.st1a3Grade.toString(), gradeInfo.getGradebookGrade());
        assertEquals(AssignmentTestDataLoad.st1a3Comment, gradeInfo.getGradebookComment());
    }

    public void testAssignGradeToUngradedStudents() {
        //test the null params
        try {
            gradebookLogic.assignGradeToUngradedStudents(null, testData.a3.getGradebookItemId(), new ArrayList<String>(), "A");
            fail("Did not catch null contextId passed to assignGradeToUngradedStudents");
        } catch (IllegalArgumentException iae) {}

        try {
            gradebookLogic.assignGradeToUngradedStudents(testData.a3.getContextId(), null, new ArrayList<String>(), "A");
            fail("Did not catch null gradebookItemId passed to assignGradeToUngradedStudents");
        } catch (IllegalArgumentException iae) {}
    }
}
