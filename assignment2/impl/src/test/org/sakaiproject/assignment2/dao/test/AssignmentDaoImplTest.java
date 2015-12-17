/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/test/org/sakaiproject/assignment2/dao/test/AssignmentDaoImplTest.java $
 * $Id: AssignmentDaoImplTest.java 67417 2010-04-28 18:04:11Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.dao.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.SubmissionNotFoundException;
import org.sakaiproject.assignment2.exception.VersionNotFoundException;
import org.sakaiproject.assignment2.test.Assignment2TestBase;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.test.AssignmentTestDataLoad;



/**
 * @author michellewagner (this was totally written by michelle and only michelle and no one else should take credit for it - az)
 * 
 */
public class AssignmentDaoImplTest extends Assignment2TestBase {

    // run this before each test starts
    protected void onSetUpBeforeTransaction() throws Exception {

    }

    // run this before each test starts and as part of the transaction
    protected void onSetUpInTransaction() throws Exception {
        // initialize data
        super.onSetUpInTransaction();
    }

    /**
     * ADD unit tests below here, use testMethod as the name of the unit test,
     * Note that if a method is overloaded you should include the arguments in
     * the test name like so: testMethodClassInt (for method(Class, int);
     */

    /**
     * Test method for
     * {@link org.sakaiproject.assignment2.dao.impl.AssignmentDaoImpl#getHighestSortIndexInSite(java.lang.String)}.
     */
    public void testGetHighestSortIndexInSite() {
        // negative test
        // count 0 items in unknown context
        int highestIndex = dao.getHighestSortIndexInSite(AssignmentTestDataLoad.BAD_CONTEXT);
        assertEquals(0, highestIndex);

        // exception testing
        try {
            dao.getHighestSortIndexInSite(null);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }

        // positive test	
        highestIndex = dao.getHighestSortIndexInSite(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(3, highestIndex);
    }

    public void testGetAssignmentsWithGroupsAndAttachments() {
        // try passing a null contextId
        try {
            dao.getAssignmentsWithGroupsAndAttachments(null);
            fail("did not catch null parameter passed to getAssignmentsWithGroupsAndAttachments");
        } catch (IllegalArgumentException e) {

        }

        // try passing a context that doesn't exist
        List<Assignment2> assignments = dao.getAssignmentsWithGroupsAndAttachments(AssignmentTestDataLoad.BAD_CONTEXT);
        assertNotNull(assignments);
        assertTrue(assignments.isEmpty());

        // now try the valid context - there should be 4 assignments
        assignments = dao.getAssignmentsWithGroupsAndAttachments(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(4, assignments.size());

        // for each assignment returned, double check that the attachment and group sets are accurate
        for (Assignment2 assign : assignments) {
            if (assign.getId().equals(testData.a1Id)) {
                assertTrue(assign.getAttachmentSet().size() == 2);
                assertTrue(assign.getAssignmentGroupSet().size() == 2);
            } else if (assign.getId().equals(testData.a2Id)) {
                assertTrue(assign.getAttachmentSet().isEmpty());
                assertTrue(assign.getAssignmentGroupSet().isEmpty());
            } else if (assign.getId().equals(testData.a3Id)) {
                assertNotNull(assign.getAttachmentSet());
                assertTrue(assign.getAttachmentSet().size() == 1);
                assertTrue(assign.getAssignmentGroupSet().isEmpty());
            } else if (assign.getId().equals(testData.a4Id)) {
                assertTrue(assign.getAttachmentSet().isEmpty());
                assertTrue(assign.getAssignmentGroupSet().size() == 1);
            }
        }

        // TODO set one of the assignments to "removed" and make sure it isn't returned

    }

    public void testGetAllAssignmentsWithGroupsAndAttachments() {
        // try passing a null contextId
        try {
            dao.getAllAssignmentsWithGroupsAndAttachments(null);
            fail("did not catch null parameter passed to getAssignmentsWithGroupsAndAttachments");
        } catch (IllegalArgumentException e) {

        }

        // try passing a context that doesn't exist
        List<Assignment2> assignments = dao.getAllAssignmentsWithGroupsAndAttachments(AssignmentTestDataLoad.BAD_CONTEXT);
        assertNotNull(assignments);
        assertTrue(assignments.isEmpty());

        // now try the valid context - there should be 4 assignments
        assignments = dao.getAllAssignmentsWithGroupsAndAttachments(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(4, assignments.size());

        // for each assignment returned, double check that the attachment and group sets are accurate
        for (Assignment2 assign : assignments) {
            if (assign.getId().equals(testData.a1Id)) {
                assertTrue(assign.getAttachmentSet().size() == 2);
                assertTrue(assign.getAssignmentGroupSet().size() == 2);
            } else if (assign.getId().equals(testData.a2Id)) {
                assertTrue(assign.getAttachmentSet().isEmpty());
                assertTrue(assign.getAssignmentGroupSet().isEmpty());
            } else if (assign.getId().equals(testData.a3Id)) {
                assertNotNull(assign.getAttachmentSet());
                assertTrue(assign.getAttachmentSet().size() == 1);
                assertTrue(assign.getAssignmentGroupSet().isEmpty());
            } else if (assign.getId().equals(testData.a4Id)) {
                assertTrue(assign.getAttachmentSet().isEmpty());
                assertTrue(assign.getAssignmentGroupSet().size() == 1);
            }
        }

        // now let's delete one of the assignments. should still get 4 back
        // TODO set one of the assignments to "removed" and make sure it is still returned
    }

    public void testGetAssignmentsWithGroupsAndAttachmentsById() {
        // try passing a null assignmentIdList - should do nothing
        dao.getAssignmentsWithGroupsAndAttachmentsById(null);

        // try passing an empty list 
        Set<Assignment2> assignments = dao.getAssignmentsWithGroupsAndAttachmentsById(new ArrayList<Long>());
        assertNotNull(assignments);
        assertTrue(assignments.isEmpty());

        // now let's make a list of assignments to retrieve
        List<Long> assignmentIdList = new ArrayList<Long>();
        assignmentIdList.add(testData.a1Id);
        assignmentIdList.add(testData.a2Id);
        assignmentIdList.add(testData.a3Id);
        assignments = dao.getAssignmentsWithGroupsAndAttachmentsById(assignmentIdList);
        assertNotNull(assignments);
        assertEquals(3, assignments.size());

        // for each assignment returned, double check that the attachment and group sets are accurate
        for (Assignment2 assign : assignments) {
            if (assign.getId().equals(testData.a1Id)) {
                assertTrue(assign.getAttachmentSet().size() == 2);
                assertTrue(assign.getAssignmentGroupSet().size() == 2);
            } else if (assign.getId().equals(testData.a2Id)) {
                assertTrue(assign.getAttachmentSet().isEmpty());
                assertTrue(assign.getAssignmentGroupSet().isEmpty());
            } else if (assign.getId().equals(testData.a3Id)) {
                assertNotNull(assign.getAttachmentSet());
                assertTrue(assign.getAttachmentSet().size() == 1);
                assertTrue(assign.getAssignmentGroupSet().isEmpty());
            } else  {
                fail("Unknown assignment returned by getAssignmentsWithGroupsAndAttachmentsById");
            } 
        }

    }

    public void testGetAssignmentByIdWithGroups() {
        // pass a null id
        try {
            dao.getAssignmentByIdWithGroups(null);
            fail("did not catch null parameter passed to getAssignmentByIdWithGroups");
        } catch (IllegalArgumentException e) {

        }

        // now try an id that doesn't exist - exception should be thrown
        try {
            dao.getAssignmentByIdWithGroups(27L);
            fail("Did not catch bad id passed to getAssignmentByIdWithGroups");
        } catch (AssignmentNotFoundException anfe) {}

        // now let's try some we know exist
        // double check that the assignmentGroupSet is populated correctly
        Assignment2 assign = dao.getAssignmentByIdWithGroups(testData.a1Id);
        assertNotNull(assign);
        assertTrue(assign.getAssignmentGroupSet().size() == 2);
        assertTrue(assign.getTitle().equals(AssignmentTestDataLoad.ASSIGN1_TITLE));

        assign = dao.getAssignmentByIdWithGroups(testData.a3Id);
        assertNotNull(assign);
        assertTrue(assign.getAssignmentGroupSet().isEmpty());
        assertTrue(assign.getTitle().equals(AssignmentTestDataLoad.ASSIGN3_TITLE));
    }

    public void testGetAssignmentByIdWithGroupsAndAttachments() {
        // try a null id
        try {
            dao.getAssignmentByIdWithGroupsAndAttachments(null);
            fail("did not catch null parameter passed to getAssignmentByIdWithGroupsAndAttachments");
        } catch (IllegalArgumentException e) {

        }

        // now try an id that doesn't exist
        try {
            dao.getAssignmentByIdWithGroupsAndAttachments(27L);
            fail("did not catch bad id passed to getAssignmentByIdWithGroupsAndAttachments");
        } catch (AssignmentNotFoundException anfe) {}

        // now let's try some we know exist - double check that the
        // assignmentGroupSet and attachmentSet are returned properly
        Assignment2 assign = dao.getAssignmentByIdWithGroups(testData.a1Id);
        assertNotNull(assign);
        assertTrue(assign.getAssignmentGroupSet().size() == 2);
        assertTrue(assign.getAttachmentSet().size() == 2);
        assertTrue(assign.getTitle().equals(AssignmentTestDataLoad.ASSIGN1_TITLE));

        assign = dao.getAssignmentByIdWithGroups(testData.a3Id);
        assertNotNull(assign);
        assertTrue(assign.getAssignmentGroupSet().isEmpty());
        assertTrue(assign.getAttachmentSet().size() == 1);
        assertTrue(assign.getTitle().equals(AssignmentTestDataLoad.ASSIGN3_TITLE));
    }

    public void testGetCurrentSubmissionVersionWithAttachments() throws Exception {
        // make sure bad data is caught
        try {
            // try a null submission
            dao.getCurrentSubmissionVersionWithAttachments(null);
            fail("did not catch null submission passed to getCurrentSubmissionVersionWithAttachments");
        } catch (IllegalArgumentException iae) {
        }

        try {
            // what happens if submission doesn't have an id? should throw error
            AssignmentSubmission submission = new AssignmentSubmission();
            dao.getCurrentSubmissionVersionWithAttachments(submission);
            fail("did not catch submission w/ no id passed to getCurrentSubmissionVersionWithAttachments");
        } catch(IllegalArgumentException iae) {}

        // try a submission that does exist
        AssignmentSubmissionVersion version = dao.getCurrentSubmissionVersionWithAttachments(testData.st1a1Submission);
        assertNotNull(version);
        // check that the correct version was returned
        assertTrue(version.getId().equals(testData.st1a1CurrVersion.getId()));
        // there shouldn't be any attachments for this one
        assertTrue(version.getSubmissionAttachSet().isEmpty());

        version = dao.getCurrentSubmissionVersionWithAttachments(testData.st2a1Submission);
        assertNotNull(version);
        // check that the correct version was returned
        assertTrue(version.getId().equals(testData.st2a1CurrVersion.getId()));
        assertNotNull(version.getSubmissionAttachSet());
        // this one should have 2 submission and 2 feedback attach
        assertTrue(version.getSubmissionAttachSet().size() == 2);
        assertTrue(version.getFeedbackAttachSet().size() == 2);

        // test out a submission w/o a version
        version = dao.getCurrentSubmissionVersionWithAttachments(testData.st2a2SubmissionNoVersions);
        assertNull(version);
    }

    public void testGetCurrentAssignmentSubmissionsForStudent() throws Exception {
        // pass a null studentId
        try {
            dao.getCurrentAssignmentSubmissionsForStudent(new ArrayList<Assignment2>(), null);
            fail("method getCurrentAssignmentSubmissionsForStudent did not catch null student parameter");
        } catch (IllegalArgumentException iae) {
        }

        // pass a null assignments list - should return empty list
        List<AssignmentSubmission> submissions = dao.getCurrentAssignmentSubmissionsForStudent(null, AssignmentTestDataLoad.STUDENT1_UID);
        assertTrue(submissions.isEmpty());
        // pass an empty assignments list - should return empty list
        submissions = dao.getCurrentAssignmentSubmissionsForStudent(new ArrayList<Assignment2>(), AssignmentTestDataLoad.STUDENT1_UID);
        assertTrue(submissions.isEmpty());

        // add two assignments to the list
        List<Assignment2> assignList = new ArrayList<Assignment2>();
        assignList.add(testData.a1);
        assignList.add(testData.a2);

        // try a student who won't have any
        submissions = dao.getCurrentAssignmentSubmissionsForStudent(assignList, "bogusStudent");
        assertTrue(submissions.isEmpty());

        // this student should have 1 submission
        submissions = dao.getCurrentAssignmentSubmissionsForStudent(assignList, AssignmentTestDataLoad.STUDENT1_UID);
        assertNotNull(submissions);
        assertTrue(submissions.size() == 1);
        AssignmentSubmission sub = (AssignmentSubmission)submissions.get(0);
        // double check that the current version was populated correctly
        assertNotNull(sub.getCurrentSubmissionVersion());
        assertTrue(sub.getCurrentSubmissionVersion().getId().equals(testData.st1a1CurrVersion.getId()));

        // this student should have 2 submissions (1 for each assign)
        submissions = dao.getCurrentAssignmentSubmissionsForStudent(assignList, AssignmentTestDataLoad.STUDENT2_UID);
        assertNotNull(submissions);
        assertTrue(submissions.size() == 2);
        for (AssignmentSubmission thisSub : submissions) {
            if (thisSub.getAssignment().getId().equals(testData.a1.getId())) {
                // this one should have a currentVersion
                assertNotNull(thisSub.getCurrentSubmissionVersion());
                assertTrue(thisSub.getCurrentSubmissionVersion().getId().equals(testData.st2a1CurrVersion.getId()));
            } else if (thisSub.getAssignment().getId().equals(testData.a2.getId())) {
                // this one shouldn't have a currentVersion
                assertNull(thisSub.getCurrentSubmissionVersion());
            } else {
                fail("Unknown submission returned");
            }
        }

        // double check that it is restricted by the assignments we pass
        assignList = new ArrayList<Assignment2>();
        assignList.add(testData.a2);
        // there are no submissions for this user for the passed assign
        submissions = dao.getCurrentAssignmentSubmissionsForStudent(assignList, AssignmentTestDataLoad.STUDENT1_UID);
        assertTrue(submissions.isEmpty());
        // there is one submissions for this user for the assign
        submissions = dao.getCurrentAssignmentSubmissionsForStudent(assignList, AssignmentTestDataLoad.STUDENT2_UID);
        assertTrue(submissions.size() == 1);
    }

    public void testGetCurrentSubmissionsForStudentsForAssignment() throws Exception {
        // pass a null assignment
        try {
            dao.getCurrentSubmissionsForStudentsForAssignment(new ArrayList<String>(), null);
            fail("did not catch null assignment passed to getCurrentSubmissionsForStudentsForAssignment");
        } catch(IllegalArgumentException iae) {
        }

        // null student list - should return empty list
        Set<AssignmentSubmission> submissions = dao.getCurrentSubmissionsForStudentsForAssignment(null, testData.a1);
        assertTrue(submissions.isEmpty());

        // add one real and one "fake" student
        List<String> studentList = new ArrayList<String>();
        studentList.add(AssignmentTestDataLoad.STUDENT1_UID);
        studentList.add("bogusStudent"); // shouldn't cause any problems

        // there should be 1 submission returned
        submissions = dao.getCurrentSubmissionsForStudentsForAssignment(studentList, testData.a1);
        assertTrue(submissions.size() == 1);
        for (AssignmentSubmission thisSub : submissions) {
            // double check that the currentVersion was populated correctly
            assertNotNull(thisSub.getCurrentSubmissionVersion());
            assertTrue(thisSub.getCurrentSubmissionVersion().getId().equals(testData.st1a1CurrVersion.getId()));
        }

        // add another student with a submission to the list and double check currentVersion
        // was populated correctly
        studentList.add(AssignmentTestDataLoad.STUDENT2_UID);
        submissions = dao.getCurrentSubmissionsForStudentsForAssignment(studentList, testData.a1);
        assertTrue(submissions.size() == 2);
        for (AssignmentSubmission thisSub : submissions) {
            if (thisSub.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID)) {
                assertNotNull(thisSub.getCurrentSubmissionVersion());
                assertTrue(thisSub.getCurrentSubmissionVersion().getId().equals(testData.st1a1CurrVersion.getId()));
            } else if (thisSub.getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID)) {
                assertNotNull(thisSub.getCurrentSubmissionVersion());
                assertTrue(thisSub.getCurrentSubmissionVersion().getId().equals(testData.st2a1CurrVersion.getId()));
            } else {
                fail("Unknown submission returned by getCurrentSubmissionsForStudentsForAssignment");
            }
        }

        // there should only be 1 submission for this assignment
        submissions = dao.getCurrentSubmissionsForStudentsForAssignment(studentList, testData.a2);
        assertTrue(submissions.size() == 1);
        for (AssignmentSubmission thisSub : submissions) {
            // there shouldn't be a currentVersion for this submission
            assertNull(thisSub.getCurrentSubmissionVersion());
        }
    }

    public void testGetSubmissionWithVersionHistoryForStudentAndAssignment() throws Exception {
        try {
            // pass a null student
            dao.getSubmissionWithVersionHistoryForStudentAndAssignment(null, new Assignment2());
            fail("Did not catch null student passed to getSubmissionWithVersionHistoryForStudentAndAssignment");
        } catch (IllegalArgumentException iae) {
        }
        try {
            // pass a non-persisted assignment
            dao.getSubmissionWithVersionHistoryForStudentAndAssignment(AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("Did not catch null assignment passed to getSubmissionWithVersionHistoryForStudentAndAssignment");
        } catch (IllegalArgumentException iae) {}

        // should have no submission
        AssignmentSubmission submission = dao.getSubmissionWithVersionHistoryForStudentAndAssignment(AssignmentTestDataLoad.STUDENT1_UID, testData.a2);
        assertNull(submission);

        // has submission
        submission = dao.getSubmissionWithVersionHistoryForStudentAndAssignment(AssignmentTestDataLoad.STUDENT1_UID, testData.a1);
        assertNotNull(submission);
        // double check that the current version is populated correctly
        assertNotNull(submission.getCurrentSubmissionVersion());
        assertTrue(submission.getCurrentSubmissionVersion().getId().equals(testData.st1a1CurrVersion.getId()));
        // check that the submission history is populated correctly
        assertNotNull(submission.getSubmissionHistorySet());
        assertTrue(submission.getSubmissionHistorySet().size() == 1);

        // this student should have a submission
        submission = dao.getSubmissionWithVersionHistoryForStudentAndAssignment(AssignmentTestDataLoad.STUDENT2_UID, testData.a1);
        assertNotNull(submission);
        // double check that the current version is populated correctly
        assertNotNull(submission.getCurrentSubmissionVersion());
        assertTrue(submission.getCurrentSubmissionVersion().getId().equals(testData.st2a1CurrVersion.getId()));
        // check that the submission history is populated correctly
        assertNotNull(submission.getSubmissionHistorySet());
        assertTrue(submission.getSubmissionHistorySet().size() == 3);
        // double check that the attachments are correct for the returned versions
        for (AssignmentSubmissionVersion vers : submission.getSubmissionHistorySet()) {
            // check that the history versions have attach populated correctly
            if (vers.getId().equals(testData.st2a1Version1.getId())) {
                assertTrue(vers.getSubmissionAttachSet().size() == 1);
                assertTrue(vers.getFeedbackAttachSet().isEmpty());
            } else if (vers.getId().equals(testData.st2a1Version2.getId())) {
                assertTrue(vers.getSubmissionAttachSet().isEmpty());
                assertTrue(vers.getFeedbackAttachSet().isEmpty());
            } else if (vers.getId().equals(testData.st2a1CurrVersion.getId())) {
                assertTrue(vers.getSubmissionAttachSet().size() == 2);
                assertTrue(vers.getFeedbackAttachSet().size() == 2);
            } else {
                fail("Unknown version included in history");
            }
        }

        // submission w/ no versions
        submission = dao.getSubmissionWithVersionHistoryForStudentAndAssignment(AssignmentTestDataLoad.STUDENT2_UID, testData.a2);
        assertNotNull(submission);
        // there should be no current version
        assertNull(submission.getCurrentSubmissionVersion());
        // there should be no history
        assertTrue(submission.getSubmissionHistorySet().isEmpty());
    }

    public void testGetSubmissionsWithVersionHistoryForStudentListAndAssignment() throws Exception {
        // try a null assignment
        try {
            dao.getSubmissionsWithVersionHistoryForStudentListAndAssignment(new ArrayList<String>(), null);
            fail("did not catch null assignment passed to getSubmissionsWithVersionHistoryForStudentListAndAssignment");
        } catch(IllegalArgumentException iae) {
        }

        // null student list - should return empty submission list
        Set<AssignmentSubmission> submissions = dao.getSubmissionsWithVersionHistoryForStudentListAndAssignment(null, testData.a1);
        assertTrue(submissions.isEmpty());

        // add two students - one is not associated with this class
        List<String> studentList = new ArrayList<String>();
        studentList.add(AssignmentTestDataLoad.STUDENT1_UID);
        studentList.add("bogusStudent"); // shouldn't cause any problems

        // should return one submission for this assignment
        submissions = dao.getSubmissionsWithVersionHistoryForStudentListAndAssignment(studentList, testData.a1);
        assertTrue(submissions.size() == 1);
        for (AssignmentSubmission thisSub : submissions) {
            // check that current  version is populated correctly
            assertNotNull(thisSub.getCurrentSubmissionVersion());
            assertTrue(thisSub.getCurrentSubmissionVersion().getId().equals(testData.st1a1CurrVersion.getId()));
            // check that submission history is populated correctly
            assertNotNull(thisSub.getSubmissionHistorySet());
            assertTrue(thisSub.getSubmissionHistorySet().size() == 1);
        }

        // add another student with a submission to the list
        studentList.add(AssignmentTestDataLoad.STUDENT2_UID);
        submissions = dao.getSubmissionsWithVersionHistoryForStudentListAndAssignment(studentList, testData.a1);
        assertTrue(submissions.size() == 2);
        for (AssignmentSubmission thisSub : submissions) {
            if (thisSub.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID)) {
                // check that current  version is populated correctly
                assertNotNull(thisSub.getCurrentSubmissionVersion());
                assertTrue(thisSub.getCurrentSubmissionVersion().getId().equals(testData.st1a1CurrVersion.getId()));
                assertTrue(thisSub.getCurrentSubmissionVersion().getSubmissionAttachSet().isEmpty());
                assertTrue(thisSub.getCurrentSubmissionVersion().getFeedbackAttachSet().isEmpty());
                // check that submission history is populated correctly
                assertTrue(thisSub.getSubmissionHistorySet().size() == 1);
            } else if (thisSub.getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID)) {
                // check that current  version is populated correctly
                assertNotNull(thisSub.getCurrentSubmissionVersion());
                assertTrue(thisSub.getCurrentSubmissionVersion().getId().equals(testData.st2a1CurrVersion.getId()));
                assertTrue(thisSub.getCurrentSubmissionVersion().getSubmissionAttachSet().size() == 2);
                assertTrue(thisSub.getCurrentSubmissionVersion().getFeedbackAttachSet().size() == 2);

                // check that submission history is populated correctly
                assertTrue(thisSub.getSubmissionHistorySet().size() == 3);
                for (AssignmentSubmissionVersion vers : thisSub.getSubmissionHistorySet()) {
                    // check that the history versions have attach populated correctly
                    if (vers.getId().equals(testData.st2a1Version1.getId())) {
                        assertTrue(vers.getSubmissionAttachSet().size() == 1);
                        assertTrue(vers.getFeedbackAttachSet().isEmpty());
                    } else if (vers.getId().equals(testData.st2a1Version2.getId())) {
                        assertTrue(vers.getSubmissionAttachSet().isEmpty());
                        assertTrue(vers.getFeedbackAttachSet().isEmpty());
                    } else if (vers.getId().equals(testData.st2a1CurrVersion.getId())) {
                        assertTrue(vers.getSubmissionAttachSet().size() == 2);
                        assertTrue(vers.getFeedbackAttachSet().size() == 2);
                    } else {
                        fail("Unknown version included in history");
                    }
                }
            } else {
                fail("Unknown submission returned by getSubmissionsWithVersionHistoryForStudentListAndAssignment");
            }
        }

        // now try an assignment with only 1 submission
        submissions = dao.getSubmissionsWithVersionHistoryForStudentListAndAssignment(studentList, testData.a2);
        assertTrue(submissions.size() == 1);
        for (AssignmentSubmission thisSub : submissions) {
            // there should be no current version or history for this submission
            assertNull(thisSub.getCurrentSubmissionVersion());
            assertTrue(thisSub.getSubmissionHistorySet().isEmpty());
        }
    }

    public void testGetAssignmentSubmissionVersionByIdWithAttachments() throws Exception {
        // try passing a null version id
        try {
            dao.getAssignmentSubmissionVersionByIdWithAttachments(null);
            fail("Did not catch null id passed to getAssignmentSubmissionVersionByIdWithAttachments");
        } catch(IllegalArgumentException iae) {
        }
        // try an id that doesn't exist - should throw exception
        try {
            dao.getAssignmentSubmissionVersionByIdWithAttachments(1234567L);
            fail("did not catch bogus version id passed to getAssignmentSubmissionVersionByIdWithAttachments");
        } catch (VersionNotFoundException vnfe) {}

        // try a real one
        AssignmentSubmissionVersion version = dao.getAssignmentSubmissionVersionByIdWithAttachments(testData.st1a1CurrVersion.getId());
        assertNotNull(version);
        assertNotNull(version.getAssignmentSubmission());
        assertTrue(version.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        assertTrue(version.getSubmissionAttachSet().isEmpty());
        assertTrue(version.getFeedbackAttachSet().isEmpty());

        // one with attachments
        version = dao.getAssignmentSubmissionVersionByIdWithAttachments(testData.st2a1CurrVersion.getId());
        assertNotNull(version);
        assertNotNull(version.getAssignmentSubmission());
        assertTrue(version.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID));
        assertTrue(version.getSubmissionAttachSet().size() == 2);
        assertTrue(version.getFeedbackAttachSet().size() == 2);
    }

    public void testGetSubmissionWithVersionHistoryById() throws Exception {
        // try a null submissionId
        try {
            dao.getSubmissionWithVersionHistoryById(null);
            fail("Did not catch null submissionId passed to getSubmissionWithVersionHistoryById");
        } catch(IllegalArgumentException iae) {
        }

        // try a submission id that doesn't exist
        try {
            dao.getSubmissionWithVersionHistoryById(12345L);
            fail("did not catch bogus submission id passed to getSubmissionWithVersionHistoryById");
        } catch (SubmissionNotFoundException snfe) {}

        // try a submission with no versions
        AssignmentSubmission submission = dao.getSubmissionWithVersionHistoryById(testData.st2a2SubmissionNoVersions.getId());
        assertNotNull(submission);
        assertNull(submission.getCurrentSubmissionVersion());
        assertTrue(submission.getSubmissionHistorySet().isEmpty());

        // try one with versions
        submission = dao.getSubmissionWithVersionHistoryById(testData.st2a1Submission.getId());
        assertNotNull(submission);
        assertTrue(submission.getCurrentSubmissionVersion().getId().equals(testData.st2a1CurrVersion.getId()));
        // double check that the history was populated correctly
        assertTrue(submission.getSubmissionHistorySet().size() == 3);
    }

    public void testGetVersionHistoryForSubmission() {
        // try a null submission
        try {
            dao.getVersionHistoryForSubmission(null);
            fail("did not catch null submission passed to getVersionHistoryForSubmission");
        } catch (IllegalArgumentException iae) {}

        // let's try a few different submissions
        List<AssignmentSubmissionVersion> history = dao.getVersionHistoryForSubmission(testData.st1a1Submission);
        assertEquals(history.size(), 1);

        history = dao.getVersionHistoryForSubmission(testData.st2a2SubmissionNoVersions);
        assertEquals(history.size(), 0);

        history = dao.getVersionHistoryForSubmission(testData.st2a1Submission);
        assertEquals(history.size(), 3);

    }

    // TODO - figure out why this test is broken
    /*public void testGetVersionByUserIdAndSubmittedDate() throws Exception
	{
		String userId = null;
		Date submittedDate = null;
		try
		{
			dao.getVersionByUserIdAndSubmittedDate(userId, submittedDate);
			fail("Should've thrown exception with userId == submittedDate == null");
		}
		catch (IllegalArgumentException iae)
		{
			// expected response
		}

		try
		{
			userId = AssignmentTestDataLoad.STUDENT1_UID;
			submittedDate = null;
			dao.getVersionByUserIdAndSubmittedDate(userId, submittedDate);
			fail("Should've thrown exception with userId != null and submittedDate == null");
		}
		catch (IllegalArgumentException iae)
		{
			// expected response
		}

		try
		{
			userId = null;
			submittedDate = testData.st1a1CurrVersion.getSubmittedDate();
			dao.getVersionByUserIdAndSubmittedDate(userId, submittedDate);
			fail("Should've thrown exception with userId == null and submittedDate != null");
		}
		catch (IllegalArgumentException iae)
		{
			// expected response
		}

		try
		{
			userId = AssignmentTestDataLoad.STUDENT1_UID;
			submittedDate = testData.st1a1CurrVersion.getSubmittedDate();
			AssignmentSubmissionVersion asv = dao.getVersionByUserIdAndSubmittedDate(
					userId, submittedDate);
			assertEquals(asv.getCreatedBy(), userId);
			assertEquals(asv.getSubmittedDate(), submittedDate);
		}
		catch (IllegalArgumentException iae)
		{
			fail("Shouldn't have thrown exception with userId != null and submittedDate != null");
		}
	}*/

    public void testGetNumSubmittedVersions() throws Exception {
        // try null params
        try {
            dao.getNumSubmittedVersions(null, testData.a1Id);
            fail("did not catch null studentId passed to getNumSubmittedVersions");
        } catch (IllegalArgumentException iae) {}

        try {
            dao.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null assignmentId passed to getNumSubmittedVersions");
        } catch (IllegalArgumentException iae) {}

        // try an assignmentId that doesn't exist
        assertEquals(0, dao.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT3_UID, 12345L));

        // try a student with no submissions 
        assertEquals(0, dao.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT3_UID, testData.a1Id));

        // try a student with 2 submissions but one is draft
        assertEquals(1, dao.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT1_UID, testData.a3Id));

        // add instructor feedback w/o a submission
        AssignmentSubmission st3a1Submission = testData.createGenericSubmission(testData.a1, AssignmentTestDataLoad.STUDENT3_UID);
        AssignmentSubmissionVersion st3a1CurrVersion = testData.createGenericVersion(st3a1Submission, 0);
        st3a1CurrVersion.setDraft(false);
        st3a1CurrVersion.setSubmittedDate(null);
        dao.save(st3a1Submission);
        dao.save(st3a1CurrVersion);

        // should show up 0 b/c not submitted
        assertEquals(0, dao.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT3_UID, testData.a1Id));
    }
    
    public void testGetNumStudentVersions() throws Exception {
        // try null params
        try {
            dao.getNumStudentVersions(null, testData.a1Id);
            fail("did not catch null studentId passed to getNumSubmittedVersions");
        } catch (IllegalArgumentException iae) {}

        try {
            dao.getNumStudentVersions(AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null assignmentId passed to getNumSubmittedVersions");
        } catch (IllegalArgumentException iae) {}

        // try an assignmentId that doesn't exist
        assertEquals(0, dao.getNumStudentVersions(AssignmentTestDataLoad.STUDENT3_UID, 12345L));

        // try a student with no submissions 
        assertEquals(0, dao.getNumStudentVersions(AssignmentTestDataLoad.STUDENT3_UID, testData.a1Id));

        // try a student with 2 submissions but one is draft
        assertEquals(2, dao.getNumStudentVersions(AssignmentTestDataLoad.STUDENT1_UID, testData.a3Id));

        // add instructor feedback w/o a submission
        AssignmentSubmission st3a1Submission = testData.createGenericSubmission(testData.a1, AssignmentTestDataLoad.STUDENT3_UID);
        AssignmentSubmissionVersion st3a1CurrVersion = testData.createGenericVersion(st3a1Submission, 0);
        st3a1CurrVersion.setDraft(false);
        st3a1CurrVersion.setSubmittedDate(null);
        dao.save(st3a1Submission);
        dao.save(st3a1CurrVersion);

        // should show up 0 b/c not submitted
        assertEquals(0, dao.getNumStudentVersions(AssignmentTestDataLoad.STUDENT3_UID, testData.a1Id));
    }

    public void testGetNumStudentsWithASubmission() {
        // try a null assignment
        try {
            dao.getNumStudentsWithASubmission(null, new ArrayList<String>());
            fail("did not catch null assignment passed to getNumStudentsWithASubmission");
        } catch (IllegalArgumentException iae) {}

        // try a null studentIdList
        assertEquals(0, dao.getNumStudentsWithASubmission(testData.a1, null));
        List<String> studentIdList = new ArrayList<String>();
        studentIdList.add(AssignmentTestDataLoad.STUDENT1_UID);
        studentIdList.add(AssignmentTestDataLoad.STUDENT2_UID);
        studentIdList.add(AssignmentTestDataLoad.STUDENT3_UID);

        // this method counts how many of these students have a submission for the given assignment, not the total # versions
        // see comments in AssignmentTestDataLoad for specific sub info for these assign
        assertEquals(2, dao.getNumStudentsWithASubmission(testData.a1, studentIdList));
        assertEquals(0, dao.getNumStudentsWithASubmission(testData.a2, studentIdList));
        assertEquals(3, dao.getNumStudentsWithASubmission(testData.a3, studentIdList));
        assertEquals(1, dao.getNumStudentsWithASubmission(testData.a4, studentIdList));
    }

    public void testGetHighestSubmittedVersionNumber() {
        // try a null submission
        try {
            dao.getHighestSubmittedVersionNumber(null);
            fail("did not catch null submission passed to getHighestSubmittedVersionNumber");
        } catch (IllegalArgumentException iae) {}

        // should be 2 even though one is draft
        assertEquals(2, dao.getHighestSubmittedVersionNumber(testData.st1a3Submission));
    }

    public void testGetSubmissionsForStudentWithVersionHistoryAndAttach() {
        // try a null studentId
        try {
            dao.getSubmissionsForStudentWithVersionHistoryAndAttach(null, new ArrayList<Assignment2>());
            fail("did not catch null studentId passed to getSubmissionsForStudentForAssignments");
        } catch (IllegalArgumentException iae) {}

        // try a null assignmentList - should do nothing
        dao.getSubmissionsForStudentWithVersionHistoryAndAttach(AssignmentTestDataLoad.STUDENT1_UID, null);

        List<Assignment2> assignList = new ArrayList<Assignment2>();
        assignList.add(testData.a1);
        assignList.add(testData.a2);
        assignList.add(testData.a3);
        assignList.add(testData.a4);

        Set<AssignmentSubmission> subSet = dao.getSubmissionsForStudentWithVersionHistoryAndAttach(AssignmentTestDataLoad.STUDENT1_UID, assignList);
        assertEquals(2, subSet.size());
        for (AssignmentSubmission sub : subSet) {
            if (sub.getAssignment().getId().equals(testData.a1Id)) {
                assertEquals(1, sub.getSubmissionHistorySet().size());
                assertNotNull(sub.getCurrentSubmissionVersion());
            } else if (sub.getAssignment().getId().equals(testData.a3Id)) {
                assertEquals(2, sub.getSubmissionHistorySet().size());
                assertNotNull(sub.getCurrentSubmissionVersion());
            } else {
                fail("Invalid submission returned by getSubmissionsForStudentForAssignments");
            }
        }

        subSet = dao.getSubmissionsForStudentWithVersionHistoryAndAttach(AssignmentTestDataLoad.STUDENT2_UID, assignList);
        assertEquals(4, subSet.size());
        for (AssignmentSubmission sub : subSet) {
            if (sub.getAssignment().getId().equals(testData.a1Id)) {
                assertEquals(3, sub.getSubmissionHistorySet().size());
                assertNotNull(sub.getCurrentSubmissionVersion());
            } else if (sub.getAssignment().getId().equals(testData.a3Id)) {
                assertEquals(1, sub.getSubmissionHistorySet().size());
                assertNotNull(sub.getCurrentSubmissionVersion());
            } else if (sub.getAssignment().getId().equals(testData.a4Id)) {
                assertEquals(2, sub.getSubmissionHistorySet().size());
                assertNotNull(sub.getCurrentSubmissionVersion());
            } else if (sub.getAssignment().getId().equals(testData.a2Id)) {
                assertEquals(0, sub.getSubmissionHistorySet().size());
                assertNull(sub.getCurrentSubmissionVersion());
            } else {
                fail("Invalid submission returned by getSubmissionsForStudentForAssignments");
            }
        }

        subSet = dao.getSubmissionsForStudentWithVersionHistoryAndAttach(AssignmentTestDataLoad.STUDENT3_UID, assignList);
        assertEquals(1, subSet.size());
        for (AssignmentSubmission sub : subSet) {
            if (sub.getAssignment().getId().equals(testData.a3Id)) {
                assertEquals(2, sub.getSubmissionHistorySet().size());
                assertNotNull(sub.getCurrentSubmissionVersion());
            } else {
                fail("Invalid submission returned by getSubmissionsForStudentForAssignments");
            }
        }
    }

    public void testGetExistingSubmissionsForRemovedAssignments() {
        // try null params
        try {
            dao.getExistingSubmissionsForRemovedAssignments(null, AssignmentTestDataLoad.CONTEXT_ID);
            fail("did not catch null studentId passed to getExistingSubmissionsForRemovedAssignments");
        } catch (IllegalArgumentException iae) {}

        try {
            dao.getExistingSubmissionsForRemovedAssignments(AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null contextId passed to getExistingSubmissionsForRemovedAssignments");
        } catch (IllegalArgumentException iae) {}

        // there should be none right now
        Set<AssignmentSubmission> existingSubs = dao.getExistingSubmissionsForRemovedAssignments(AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(0, existingSubs.size());

        // let's remove some assignments!
        testData.a1.setRemoved(true);
        dao.update(testData.a1);

        testData.a3.setRemoved(true);
        dao.update(testData.a3);

        // student 1 should have 2, student 2 should have 2, student 3 should have 1
        existingSubs = dao.getExistingSubmissionsForRemovedAssignments(AssignmentTestDataLoad.STUDENT1_UID, AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(2, existingSubs.size());

        existingSubs = dao.getExistingSubmissionsForRemovedAssignments(AssignmentTestDataLoad.STUDENT2_UID, AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(2, existingSubs.size());

        existingSubs = dao.getExistingSubmissionsForRemovedAssignments(AssignmentTestDataLoad.STUDENT3_UID, AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(1, existingSubs.size());


    }

    public void testGetCurrentSubmittedVersions() {
        // pass a null assignment
        try {
            dao.getCurrentSubmittedVersions(new ArrayList<String>(), null);
            fail("did not catch null assignment passed to testGetCurrentSubmittedVersions");
        } catch(IllegalArgumentException iae) {
        }

        // null student list - should return empty list
        List<AssignmentSubmissionVersion> versions = dao.getCurrentSubmittedVersions(null, testData.a1);
        assertTrue(versions.isEmpty());

        // add one real and one "fake" student
        List<String> studentList = new ArrayList<String>();
        studentList.add(AssignmentTestDataLoad.STUDENT1_UID);
        studentList.add("bogusStudent"); // shouldn't cause any problems

        // there should be 1 version returned
        versions = dao.getCurrentSubmittedVersions(studentList, testData.a1);
        assertEquals(1, versions.size());
        for (AssignmentSubmissionVersion thisVer : versions) {
            assertEquals(testData.st1a1CurrVersion.getId(), thisVer.getId());
        }

        // add another student with a submission to the list 
        studentList.add(AssignmentTestDataLoad.STUDENT2_UID);
        versions = dao.getCurrentSubmittedVersions(studentList, testData.a1);
        assertEquals(2, versions.size());
        for (AssignmentSubmissionVersion thisVer : versions) {
            if (thisVer.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID)) {
                assertEquals(testData.st1a1CurrVersion.getId(), thisVer.getId());
            } else if (thisVer.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID)) {
                assertEquals(testData.st2a1CurrVersion.getId(), thisVer.getId());
            } else {
                fail("Unknown version returned by getCurrentSubmittedVersions");
            }
        }

        // try a version with the most current version a draft. should only return
        // most recent submitted version - st1a3 should return first version, not second version
        studentList = new ArrayList<String>();
        studentList.add(AssignmentTestDataLoad.STUDENT1_UID);
        versions = dao.getCurrentSubmittedVersions(studentList, testData.a3);
        assertEquals(1, versions.size());
        assertEquals(testData.st1a3FirstVersion.getId(), versions.get(0).getId());

    }
}