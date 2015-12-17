/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/test/org/sakaiproject/assignment2/logic/test/AssignmentLogicTest.java $
 * $Id: AssignmentLogicTest.java 67186 2010-04-15 14:20:57Z wagnermr@iupui.edu $
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.NoGradebookItemForGradedAssignmentException;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentAttachment;
import org.sakaiproject.assignment2.model.AssignmentGroup;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.test.Assignment2TestBase;
import org.sakaiproject.assignment2.test.AssignmentTestDataLoad;


public class AssignmentLogicTest extends Assignment2TestBase {

    private static final Log log = LogFactory.getLog(AssignmentLogicTest.class);

    /**
     * @see org.springframework.test.AbstractTransactionalSpringContextTests#onSetUpInTransaction()
     */
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        
        // refresh the assign data before you begin the tests.  sometimes it gets cranky
        dao.evictObject(testData.a1);
        testData.a1 = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a1Id);
        
        dao.evictObject(testData.a2);
        testData.a2 = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a2Id);
        
        dao.evictObject(testData.a3);
        testData.a3 = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a3Id);
        
        dao.evictObject(testData.a4);
        testData.a4 = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a4Id);
    }

    public void testSaveAssignment() throws Exception {
        // try null param
        try {
            assignmentLogic.saveAssignment(null);
            fail("did not catch null assignment passed to saveAssignment");
        } catch (IllegalArgumentException iae) {
        }

        Assignment2 newAssign = new Assignment2();
        // let's start by creating a new assignment
        newAssign.setContextId(AssignmentTestDataLoad.CONTEXT_ID);
        newAssign.setDraft(false);
        newAssign.setHasAnnouncement(false);
        newAssign.setAddedToSchedule(false);
        newAssign.setHonorPledge(false);
        newAssign.setInstructions("Complete this by friday");
        newAssign.setSendSubmissionNotifications(false);
        newAssign.setOpenDate(new Date());
        newAssign.setTitle(AssignmentTestDataLoad.ASSIGN1_TITLE); //we're using a title that already exists
        newAssign.setGraded(false);

        // start with 1 group and 2 attachments
        Set<AssignmentGroup> assignGroupSet = new HashSet<AssignmentGroup>();
        AssignmentGroup group1 = new AssignmentGroup(newAssign, "group1Ref");
        assignGroupSet.add(group1);

        Set<AssignmentAttachment> attachSet = new HashSet<AssignmentAttachment>();
        AssignmentAttachment attach1 = new AssignmentAttachment(newAssign, "attach1Ref");
        attachSet.add(attach1);
        AssignmentAttachment attach2 = new AssignmentAttachment(newAssign, "attach2Ref");
        attachSet.add(attach2);

        newAssign.setAssignmentGroupSet(assignGroupSet);
        newAssign.setAttachmentSet(attachSet);

        // let's make sure users without the edit perm can't save
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            assignmentLogic.saveAssignment(newAssign);
            fail("SecurityException was not thrown even though user does NOT have permission to save an assignment");
        } catch(SecurityException se) {
        }
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        try {
            assignmentLogic.saveAssignment(newAssign);
            fail("SecurityException was not thrown even though user does NOT have permission to save an assignment");
        } catch(SecurityException se) {
        }

        // switch to a user with edit perm
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // change the name
        String newTitle = "New assignment";
        newAssign.setTitle(newTitle);

        // now set this item to graded but don't populate gradebookItemId
        newAssign.setGraded(true);

        try {
            assignmentLogic.saveAssignment(newAssign);
            fail("Did not catch null gradebookItemId for graded assignment");
        } catch(NoGradebookItemForGradedAssignmentException ge) {}

        // set the gradebookItemId
        newAssign.setGradebookItemId(AssignmentTestDataLoad.GB_ITEM1_ID);

        assignmentLogic.saveAssignment(newAssign);

        // double check that it was saved
        List<Assignment2> assignList = dao.findByProperties(Assignment2.class, new String[] {"title"}, new Object[] {newTitle});
        assertNotNull(assignList);
        assertTrue(assignList.size() == 1);
        newAssign = (Assignment2)assignList.get(0);
        // check that groups and attach were saved, as well
        assertTrue(newAssign.getAssignmentGroupSet().size() == 1);
        assertTrue(newAssign.getAttachmentSet().size() == 2);

        // let's retrieve this one to update
        Assignment2 updatedAssign = dao.getAssignmentByIdWithGroupsAndAttachments(newAssign.getId());
        String revisedTitle = "revised title";
        updatedAssign.setTitle(revisedTitle);

        // add a group
        updatedAssign.getAssignmentGroupSet().add(new AssignmentGroup(updatedAssign, "newGroupRef"));
        // remove the attachments
        updatedAssign.setAttachmentSet(null);

        assignmentLogic.saveAssignment(updatedAssign);

        Assignment2 another = dao.getAssignmentByIdWithGroups(updatedAssign.getId());
        assertNotNull(another);
        assertTrue(another.getTitle().equals(revisedTitle));

        // check that groups and attach were saved, as well
        assertTrue(updatedAssign.getAssignmentGroupSet().size() == 2);
        assertNull(updatedAssign.getAttachmentSet());

        // TODO - try adding an announcement
        
        // Test saving a draft
        Assignment2 draftAssign = new Assignment2();
        String DRAFT_TITLE = "Draft assignment";
        // let's start by creating a new assignment
        draftAssign.setContextId(AssignmentTestDataLoad.CONTEXT_ID);
        draftAssign.setDraft(true);
        draftAssign.setHasAnnouncement(false);
        draftAssign.setAddedToSchedule(false);
        draftAssign.setHonorPledge(false);
        draftAssign.setInstructions("Complete this by friday");
        draftAssign.setSendSubmissionNotifications(false);
        draftAssign.setOpenDate(new Date());
        draftAssign.setTitle(DRAFT_TITLE);
        draftAssign.setGraded(false);

        // start with just the TA's group
        Set<AssignmentGroup> draftAssignGroupSet = new HashSet<AssignmentGroup>();
        AssignmentGroup group2 = new AssignmentGroup(draftAssign, AssignmentTestDataLoad.GROUP1_NAME);
        draftAssignGroupSet.add(group2);

        draftAssign.setAssignmentGroupSet(draftAssignGroupSet);

        // TA should be able to save this
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        assignmentLogic.saveAssignment(draftAssign);
        // double check it was saved
        List<Assignment2> draftAssignList = dao.findByProperties(Assignment2.class, new String[] {"title"}, new Object[] {DRAFT_TITLE});
        assertNotNull(draftAssignList);
        assertTrue(draftAssignList.size() == 1);
        draftAssign = (Assignment2)draftAssignList.get(0);
        assertTrue(draftAssign.isDraft());
        assertEquals(DRAFT_TITLE, draftAssign.getTitle());
        
        // make sure the instructor can edit the draft one. let's delete the
        // group restriction
        draftAssign.setAssignmentGroupSet(new HashSet<AssignmentGroup>());
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assignmentLogic.saveAssignment(draftAssign);
        
        // the ta shouldn't be able to edit anymore b/c not restricted to his/her group
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        try {
            assignmentLogic.saveAssignment(draftAssign);
            fail("Did not catch TA attempting to save a draft assign without permission");
        } catch (SecurityException se) {}

    }

    public void testDeleteAssignment() throws Exception {
        // pass a null assignment
        try {
            assignmentLogic.deleteAssignment(null);
            fail("Null assignment passed to deleteAssignment was not caught");
        } catch (IllegalArgumentException iae) {}

        // pass an assignment w/o an id
        try {
            assignmentLogic.deleteAssignment(new Assignment2());
            fail("Did not catch attempt to delete assignment without an id");
        } catch (IllegalArgumentException iae) {}

        // now let's check the security 
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            assignmentLogic.deleteAssignment(testData.a1);
            fail("SecurityException was not thrown even though user does NOT have permission to delete an assignment");
        } catch(SecurityException se) {
        }
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        try {
            assignmentLogic.deleteAssignment(testData.a1);
            fail("SecurityException was not thrown even though user does NOT have permission to delete an assignment");
        } catch(SecurityException se) {
        }

        // let's actually delete an assignment
        // TODO - this will crash the unit tests b/c of the call to TaggingManager
        // we need to fix this so the test works

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assignmentLogic.deleteAssignment(testData.a1);

        // let's double check that it still exists. it should just have removed = true now
        Assignment2 deletedAssign = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a1Id);
        assertNotNull(deletedAssign);
        assertTrue(deletedAssign.isRemoved());

        //TODO - somehow check the deletion of announcements?
    }

    public void testGetViewableAssignments() throws Exception {
        // this method will return different results based upon the user
        // and group memberships
        // TODO add scenario with grader permissions for TA

        try {
            assignmentLogic.getViewableAssignments(null);
            fail("Did not catch null contextId passed to getViewableAssignments");
        } catch (IllegalArgumentException iae) {}

        // let's make assignment3 and assignment4 graded
        testData.a3.setGraded(true);
        testData.a3.setGradebookItemId(AssignmentTestDataLoad.GB_ITEM1_ID);
        dao.save(testData.a3);

        testData.a4.setGraded(true);
        testData.a4.setGradebookItemId(AssignmentTestDataLoad.GB_ITEM2_ID);
        dao.save(testData.a4);

        // assign1 is restricted to group 1 and 3
        // assign2 is not restricted
        // graded assign 3 is not restricted
        // graded assign 4 is restricted to group 3

        // let's start with instructor. he/she should get all of the assignments
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        List<Assignment2> assignList = assignmentLogic.getViewableAssignments(AssignmentTestDataLoad.CONTEXT_ID);
        assertNotNull(assignList);
        assertEquals(4, assignList.size());

        // let's try the ta. he/she should only be able to view assignments if
        // 1) it is ungraded and it is open to the site
        // 2) it is ungraded and he/she is a member of a restricted group
        // 3) it is graded and he/she is authorized to view/grade the associated
        //		gb item in the gb

        // our ta is a member of group 1. assign1 is restricted to group 1 and 2
        // for a4 (graded), so ta may only see assigns for group 1 (or unrestricted assigns)

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // should return assignment 1, 2, 3
        assignList = assignmentLogic.getViewableAssignments(AssignmentTestDataLoad.CONTEXT_ID);
        assertNotNull(assignList);
        assertEquals(3, assignList.size());
        // let's make sure that these are the right assign
        for (Assignment2 assign : assignList) {
            if (assign.getId().equals(testData.a1Id) || assign.getId().equals(testData.a2Id) || 
                    assign.getId().equals(testData.a3Id)) { 
            } else {
                fail("Invalid assignment returned for TA via getViewableAssignments");
            }
        }

        // switch to student 1
        // member of group 1
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        // should return assignment 1, 2, 3
        assignList = assignmentLogic.getViewableAssignments(AssignmentTestDataLoad.CONTEXT_ID);

        assertNotNull(assignList);
        assertEquals(3, assignList.size());
        for (Assignment2 assign : assignList) {
            if (assign.getId().equals(testData.a1Id) || assign.getId().equals(testData.a2Id) ||
                    assign.getId().equals(testData.a3Id)) {
                // valid
            }
            else {
                fail("Invalid assignment returned for STUDENT1 via getViewableAssignments");
            }
        }

        // switch to student 2
        // member of group 3
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        // should return 1, 2, 3, 4
        assignList = assignmentLogic.getViewableAssignments(AssignmentTestDataLoad.CONTEXT_ID);
        assertNotNull(assignList);
        assertTrue(assignList.size() == 4);
        // let's make sure that these are the right assign
        for (Assignment2 assign : assignList) {
            if (assign.getId().equals(testData.a2Id) || assign.getId().equals(testData.a1Id) ||
                    assign.getId().equals(testData.a3Id) || assign.getId().equals(testData.a4Id)) {
                // valid
            } else {
                fail("Invalid assignment returned for STUDENT2 via getViewableAssignments");
            }
        }

        // switch to student 3
        // not a member of any groups
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT3_UID);
        // should return 2 and 3
        assignList = assignmentLogic.getViewableAssignments(AssignmentTestDataLoad.CONTEXT_ID);
        assertNotNull(assignList);
        assertTrue(assignList.size() == 2);
        // let's make sure that these are the right assign
        for (Assignment2 assign : assignList) {
            if (assign.getId().equals(testData.a2Id)) {

            } else if (assign.getId().equals(testData.a3Id)) {

            } else {
                fail("Invalid assignment returned for STUDENT3 via getViewableAssignments");
            }
        }
    }

    public void testReorderAssignments() throws Exception {
        // this method is used for reordering assignments

        // try to pass a null list
        try {
            assignmentLogic.reorderAssignments(null, AssignmentTestDataLoad.CONTEXT_ID);
            fail("did not catch null list passed to reorderAssignments");
        } catch (IllegalArgumentException iae) {}

        // try to pass a null contextId
        try {
            assignmentLogic.reorderAssignments(new ArrayList<Long>(), null);
            fail("Did not catch null contextId passed to reoderAssignments");
        } catch (IllegalArgumentException iae) {}

        // try to reorder assign as TA
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        try {
            assignmentLogic.reorderAssignments(new ArrayList<Long>(), AssignmentTestDataLoad.CONTEXT_ID);
            fail("Did not catch user trying to reorder assignments w/o permission!");
        } catch (SecurityException se){}

        // switch to someone who can reorder
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        List<Long> assignIdList = new ArrayList<Long>();
        assignIdList.add(3L);
        assignIdList.add(2L);
        assignIdList.add(1L);

        // try passing a list w/ a diff # of values than # assign in site
        try {
            assignmentLogic.reorderAssignments(assignIdList, AssignmentTestDataLoad.CONTEXT_ID);
            fail("Did not catch list w/ 3 passed to setAssignmentSortIndexes when " +
            "there are 4 assign in site");
        } catch (IllegalArgumentException iae) {}

        // try passing a list w/ a duplicate id that results in less than total
        // # assign in site
        assignIdList.add(1L);
        try {
            assignmentLogic.reorderAssignments(assignIdList, AssignmentTestDataLoad.CONTEXT_ID);
            fail("Did not catch list w/ 3 distinct ids (list had a duplicate) passed " +
            "to setAssignmentSortIndexes when there are 4 assign in site");
        } catch (IllegalArgumentException iae) {}

        // try passing a list containing a nonexistent id
        assignIdList = new ArrayList<Long>();
        assignIdList.add(3L);
        assignIdList.add(2L);
        assignIdList.add(125L);
        assignIdList.add(1L);
        try {
            assignmentLogic.reorderAssignments(assignIdList, AssignmentTestDataLoad.CONTEXT_ID);
            fail("Did not catch list w/ nonexistent assignment id");
        } catch (IllegalArgumentException iae) {}

        // right now they are in order assign 1 - 4
        // let's put assign 4 first
        assignIdList = new ArrayList<Long>();
        assignIdList.add(testData.a4Id);
        assignIdList.add(testData.a1Id);
        assignIdList.add(testData.a2Id);
        assignIdList.add(testData.a3Id);
        assignmentLogic.reorderAssignments(assignIdList, AssignmentTestDataLoad.CONTEXT_ID);
        // double check that they were updated
        List<Assignment2> allAssigns = dao.findByProperties(Assignment2.class, new String[] {"contextId","removed"}, new Object[] {AssignmentTestDataLoad.CONTEXT_ID, false});
        for (Assignment2 assign : allAssigns) {
            if (assign.getId().equals(testData.a1Id)) {
                assertEquals(1, assign.getSortIndex());
            } else if (assign.getId().equals(testData.a2Id)) {
                assertEquals(2, assign.getSortIndex());
            } else if (assign.getId().equals(testData.a3Id)) {
                assertEquals(3, assign.getSortIndex());
            } else if (assign.getId().equals(testData.a4Id)) {
                assertEquals(0, assign.getSortIndex());
            } else {
                fail("Invalid assignment returned!");
            }
        }
    }

    public void testGetAssignmentByIdWithAssociatedData() throws Exception {
        // try passing a null id
        try {
            assignmentLogic.getAssignmentByIdWithAssociatedData(null, null);
            fail("Did not catch null assignment id passed to getAssignmentByIdWithAssociatedData");
        } catch (IllegalArgumentException iae) {}
        
     // try retrieving the assignment w/o permission
        externalLogic.setCurrentUserId("random");
        try {
            assignmentLogic.getAssignmentByIdWithAssociatedData(testData.a1Id, null);
            fail("Did not catch invalid user attempting to access assignment via getAssignmentById");
        } catch (SecurityException se) {}

        // start with instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try passing an id that doesn't exist 
        try {
            assignmentLogic.getAssignmentByIdWithAssociatedData(12345L, null);
            fail("did not catch non-existent assignmentId passed to getAssignmentByIdWithAssociatedData");
        }
        catch (AssignmentNotFoundException anfe) {}

        // let's try to retrieve a graded item now
        Assignment2 assign = assignmentLogic.getAssignmentByIdWithAssociatedData(testData.a4Id, null);
        assertNotNull(assign);
        assertTrue(assign.getId().equals(testData.a4Id));

        // double check groups and attach
        assertTrue(assign.getAssignmentGroupSet().size() == 1);
        assertTrue(assign.getAttachmentSet().isEmpty());

        // try an ungraded item
        assign = assignmentLogic.getAssignmentByIdWithAssociatedData(testData.a1Id, null);
        assertTrue(assign.getId().equals(testData.a1Id));
        assertTrue(assign.getAssignmentGroupSet().size() == 2);
        assertTrue(assign.getAttachmentSet().size() == 2); 	
        
        // now try to retrieve as a TA. you should be able to view all but assign 4
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        assignmentLogic.getAssignmentByIdWithAssociatedData(testData.a1Id, null);
        assignmentLogic.getAssignmentByIdWithAssociatedData(testData.a2Id, null);
        assignmentLogic.getAssignmentByIdWithAssociatedData(testData.a3Id, null);
        
        try {
            assignmentLogic.getAssignmentByIdWithAssociatedData(testData.a4Id, null);
        } catch (SecurityException se) {}
        
        // if we set an assignment to draft status, the TA should not have access b/c
        // doesn't have add or edit perm for any of our assignments
        Assignment2 assign1 = assignmentLogic.getAssignmentByIdWithAssociatedData(testData.a1Id, null);
        assign1.setDraft(true);
        dao.save(assign1);
        try {
            assignmentLogic.getAssignmentByIdWithAssociatedData(testData.a1Id, null);
            fail("Did not catch TA attempting to view draft assignment");
        } catch (SecurityException se) {}

    }

    public void testGetAssignmentByIdWithGroupsAndAttachments() throws Exception {
        // try passing a null id
        try {
            assignmentLogic.getAssignmentById(null);
            fail("Did not catch null assignment id passed to getAssignmentByIdWithGroupsAndAttachments");
        } catch (IllegalArgumentException iae) {}
        
        // try retrieving the assignment w/o permission
        externalLogic.setCurrentUserId("random");
        try {
            assignmentLogic.getAssignmentById(testData.a1Id);
            fail("Did not catch invalid user attempting to access assignment via getAssignmentById");
        } catch (SecurityException se) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try passing an id that doesn't exist 
        try {
            assignmentLogic.getAssignmentById(12345L);
            fail("did not catch non-existent assignmentId passed to getAssignmentByIdWithGroupsAndAttachments");
        } catch (AssignmentNotFoundException anfe) {}

        // try a valid item
        Assignment2 assign = assignmentLogic.getAssignmentById(testData.a1Id);
        assertTrue(assign.getId().equals(testData.a1Id));
        assertTrue(assign.getAssignmentGroupSet().size() == 2);	
        assertTrue(assign.getAttachmentSet().size() == 2);
    }

    public void testGetStatusForAssignment() {
        // try a null assignment
        try {
            assignmentLogic.getStatusForAssignment(null);
            fail("Did not catch null assignment passed to getStatusforAssignment");
        } catch (IllegalArgumentException iae) {}

        Assignment2 assignment = new Assignment2();
        // start out with non-draft, ungraded
        assignment.setDraft(false);
        assignment.setGraded(false);

        // first, leave all of nullable fields null:
        // 	acceptUntilTime, dueDate

        // start with an open date in the past
        Calendar cal = Calendar.getInstance();
        cal.set(2005, 10, 01);
        assignment.setOpenDate(cal.getTime());

        // this should be open
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_OPEN);

        // set the open date in the future
        cal.set(2020, 10, 01);
        assignment.setOpenDate(cal.getTime());
        // should be not open
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_NOT_OPEN);

        // set it to draft - should be draft
        assignment.setDraft(true);
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_DRAFT);

        // set it back to draft=false, open date in the past, and make it due in the future
        assignment.setDraft(false);
        cal.set(2005, 10, 01);
        assignment.setOpenDate(cal.getTime());
        cal.set(2020, 10, 01);
        assignment.setDueDate(cal.getTime());
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_OPEN);
        // now make it due. since there is no accept until date, it should be closed
        cal.set(2007, 10, 01);
        assignment.setDueDate(cal.getTime());
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_CLOSED);
        // add an accept until date in the future
        cal.set(2020, 10, 01);
        assignment.setAcceptUntilDate(cal.getTime());
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_DUE);
        // now make the accept until date in the past
        cal.set(2007, 10, 01);
        assignment.setAcceptUntilDate(cal.getTime());
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_CLOSED);

        // now make this graded with accept until in past
        assignment.setGraded(true);
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_CLOSED);

        // now check with accept until in future and no due date
        cal.set(2020, 10, 01);
        assignment.setAcceptUntilDate(cal.getTime());
        assignment.setDueDate(null);
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_OPEN);

        // add a due date in the past
        cal.set(2007, 10, 01);
        assignment.setDueDate(cal.getTime());
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_DUE);

        // double check that open date overrides other dates
        cal.set(2020, 10, 01);
        assignment.setOpenDate(cal.getTime());
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_NOT_OPEN);

        // double check that draft overrides everything
        assignment.setDraft(true);
        assertEquals(assignmentLogic.getStatusForAssignment(assignment), AssignmentConstants.STATUS_DRAFT);
    }

    public void testGetDuplicatedAssignmentTitle() {
        // try some nulls
        try {
            assignmentLogic.getDuplicatedAssignmentTitle(null, null);
        } catch (IllegalArgumentException iae) {}
        try {
            assignmentLogic.getDuplicatedAssignmentTitle(AssignmentTestDataLoad.CONTEXT_ID, null);
        } catch (IllegalArgumentException iae) {}

        // try duplicating "Assignment 1" title. We already have Assignment 1 thru 4, so
        // should return "Assignment 5"
        String newTitle = assignmentLogic.getDuplicatedAssignmentTitle(AssignmentTestDataLoad.CONTEXT_ID, AssignmentTestDataLoad.ASSIGN1_TITLE);
        assertEquals("Assignment 5", newTitle);

        // now let's pass it something that doesn't have an appended number
        newTitle = assignmentLogic.getDuplicatedAssignmentTitle(AssignmentTestDataLoad.CONTEXT_ID, "Essay");
        assertEquals("Essay 1", newTitle);

        // what if we do something funky
        newTitle = assignmentLogic.getDuplicatedAssignmentTitle(AssignmentTestDataLoad.CONTEXT_ID, "Essay A");
        assertEquals("Essay A 1", newTitle);

        newTitle = assignmentLogic.getDuplicatedAssignmentTitle(AssignmentTestDataLoad.CONTEXT_ID, "Essay 16");
        assertEquals("Essay 17", newTitle);

        newTitle = assignmentLogic.getDuplicatedAssignmentTitle(AssignmentTestDataLoad.CONTEXT_ID, "I have a 17 inside");
        assertEquals("I have a 17 inside 1", newTitle);

        newTitle = assignmentLogic.getDuplicatedAssignmentTitle(AssignmentTestDataLoad.CONTEXT_ID, "I have a 17 inside 1");
        assertEquals("I have a 17 inside 2", newTitle);

    }

}
