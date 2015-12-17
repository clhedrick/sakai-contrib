/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/test/org/sakaiproject/assignment2/logic/test/AssignmentSubmissionLogicTest.java $
 * $Id: AssignmentSubmissionLogicTest.java 79745 2012-05-24 19:11:17Z wagnermr@iupui.edu $
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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.SubmissionClosedException;
import org.sakaiproject.assignment2.exception.SubmissionHonorPledgeException;
import org.sakaiproject.assignment2.exception.SubmissionNotFoundException;
import org.sakaiproject.assignment2.exception.VersionNotFoundException;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.FeedbackAttachment;
import org.sakaiproject.assignment2.model.SubmissionAttachment;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.test.Assignment2TestBase;
import org.sakaiproject.assignment2.test.AssignmentTestDataLoad;


public class AssignmentSubmissionLogicTest extends Assignment2TestBase {

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

    public void testGetAssignmentSubmissionById() {
        // try passing a null id
        try {
            submissionLogic.getAssignmentSubmissionById(null, null);
            fail("did not catch null submissionId passed to getAssignmentSubmissionById");
        } catch(IllegalArgumentException iae) {}

        // try passing an id that doesn't exist
        try {
            submissionLogic.getAssignmentSubmissionById(12345L, null);
            fail("did not catch non-existent id passed to getAssignmentSubmissionById");
        } catch (SubmissionNotFoundException snfe) {}

        // the instructor should be able to retrieve any submission
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        AssignmentSubmission submission = submissionLogic.getAssignmentSubmissionById(testData.st1a1Submission.getId(), null);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        submission = submissionLogic.getAssignmentSubmissionById(testData.st2a2SubmissionNoVersions.getId(), null);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID));
        submission = submissionLogic.getAssignmentSubmissionById(testData.st2a4Submission.getId(), null);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID));
        submission = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId(), null);
        assertNotNull(submission);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        // but shouldn't see submission details b/c still draft
        assertTrue(submission.getCurrentSubmissionVersion().getSubmittedText().equals(""));
        assertTrue(submission.getCurrentSubmissionVersion().getSubmissionAttachSet().isEmpty());

        // will throw SecurityException if currentUser isn't auth to view submission
        // let's try a TA
        // should be able to view student 1's submission for assignment 1
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        submission = submissionLogic.getAssignmentSubmissionById(testData.st1a1Submission.getId(), null);
        assertNotNull(submission);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));

        // should get a SecurityException trying to get st2
        try {
            submission = submissionLogic.getAssignmentSubmissionById(testData.st2a1Submission.getId(), null);
            fail("did not catch TA was trying to access a submission w/o authorization!");
        } catch (SecurityException se) {}
        
        // should be able to view st1 in a3
        submission = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId(), null);
        assertNotNull(submission);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        
        // should get security error for st3 in a3
        try {
            submission = submissionLogic.getAssignmentSubmissionById(testData.st3a3Submission.getId(), null);
            fail("did not catch TA was trying to access a submission w/o authorization!");
        } catch (SecurityException se) {}


        // student should be able to get their own
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        submission = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId(), null);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        // double check submission info is populated
        assertTrue(submission.getCurrentSubmissionVersion().getSubmissionAttachSet().size() == 1);
        assertFalse(submission.getCurrentSubmissionVersion().getSubmittedText().equals(""));

        // double check feedback is empty since not released yet
        assertTrue(submission.getCurrentSubmissionVersion().getAnnotatedText().equals(""));
        assertTrue(submission.getCurrentSubmissionVersion().getFeedbackAttachSet().isEmpty());
        assertTrue(submission.getCurrentSubmissionVersion().getFeedbackNotes().equals(""));
        // student should not be able to get other student
        try {
            submission = submissionLogic.getAssignmentSubmissionById(testData.st2a1Submission.getId(), null);
            fail("did not catch a student trying to access another student's submission");
        } catch (SecurityException se) {}
    }
    
    public void testGetAssignmentSubmissionById2() {
        // try passing a null id
        try {
            submissionLogic.getAssignmentSubmissionById(null);
            fail("did not catch null submissionId passed to getAssignmentSubmissionById");
        } catch(IllegalArgumentException iae) {}

        // try passing an id that doesn't exist
        try {
            submissionLogic.getAssignmentSubmissionById(12345L);
            fail("did not catch non-existent id passed to getAssignmentSubmissionById");
        } catch (SubmissionNotFoundException snfe) {}

        // the instructor should be able to retrieve any submission
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        AssignmentSubmission submission = submissionLogic.getAssignmentSubmissionById(testData.st1a1Submission.getId());
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        submission = submissionLogic.getAssignmentSubmissionById(testData.st2a2SubmissionNoVersions.getId());
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID));
        submission = submissionLogic.getAssignmentSubmissionById(testData.st2a4Submission.getId());
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID));
        submission = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId());
        assertNotNull(submission);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        // but shouldn't see submission details b/c still draft
        assertTrue(submission.getCurrentSubmissionVersion().getSubmittedText().equals(""));
        assertTrue(submission.getCurrentSubmissionVersion().getSubmissionAttachSet().isEmpty());

        // will throw SecurityException if currentUser isn't auth to view submission
        // let's try a TA
        // should be able to view student 1's submission for assignment 1
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        submission = submissionLogic.getAssignmentSubmissionById(testData.st1a1Submission.getId());
        assertNotNull(submission);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));

        // should get a SecurityException trying to get st2
        try {
            submission = submissionLogic.getAssignmentSubmissionById(testData.st2a1Submission.getId());
            fail("did not catch TA was trying to access a submission w/o authorization!");
        } catch (SecurityException se) {}
        
        // should be able to view st1 in a3
        submission = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId());
        assertNotNull(submission);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        
        // should get security error for st3 in a3
        try {
            submission = submissionLogic.getAssignmentSubmissionById(testData.st3a3Submission.getId());
            fail("did not catch TA was trying to access a submission w/o authorization!");
        } catch (SecurityException se) {}


        // student should be able to get their own
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        submission = submissionLogic.getAssignmentSubmissionById(testData.st1a3Submission.getId());
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        // double check submission info is populated
        assertTrue(submission.getCurrentSubmissionVersion().getSubmissionAttachSet().size() == 1);
        assertFalse(submission.getCurrentSubmissionVersion().getSubmittedText().equals(""));

        // double check feedback is empty since not released yet
        assertTrue(submission.getCurrentSubmissionVersion().getAnnotatedText().equals(""));
        assertTrue(submission.getCurrentSubmissionVersion().getFeedbackAttachSet().isEmpty());
        assertTrue(submission.getCurrentSubmissionVersion().getFeedbackNotes().equals(""));
        // student should not be able to get other student
        try {
            submission = submissionLogic.getAssignmentSubmissionById(testData.st2a1Submission.getId());
            fail("did not catch a student trying to access another student's submission");
        } catch (SecurityException se) {}
    }

    public void testGetSubmissionVersionById() {
        // try a null versionId
        try {
            submissionLogic.getSubmissionVersionById(null);
            fail("did not catch null versionId passed to getSubmissionVersionById");
        } catch (IllegalArgumentException iae) {}

        // try a versionId that doesn't exist
        try {
            submissionLogic.getSubmissionVersionById(12345L);
            fail("did not catch non-existent id passed to getSubmissionVersionById");
        } catch (VersionNotFoundException vnfe) {}

        // instructors should be able to retrieve any version
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        AssignmentSubmissionVersion version = submissionLogic.getSubmissionVersionById(testData.st1a1CurrVersion.getId());
        assertTrue(version.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        version = submissionLogic.getSubmissionVersionById(testData.st2a4CurrVersion.getId());
        assertTrue(version.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID));
        // now let's double check that the instructor can't see student submission details when it is
        // draft...
        version = submissionLogic.getSubmissionVersionById(testData.st1a3CurrVersion.getId());
        assertTrue(version.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        assertTrue(version.getFeedbackAttachSet().size() == 1);
        assertTrue(version.isDraft());
        // make sure these are empty!
        assertTrue(version.getSubmissionAttachSet().isEmpty());
        assertTrue(version.getSubmittedText().equals(""));

        // ta should be restricted
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // should be able to view student 1 but not student 2
        version = submissionLogic.getSubmissionVersionById(testData.st1a1CurrVersion.getId());
        assertTrue(version.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        // student 2 should throw SecurityException
        try {
            version = submissionLogic.getSubmissionVersionById(testData.st2a4CurrVersion.getId());
            fail("Did not catch TA accessing st2 version w/o authorization");
        } catch (SecurityException se) { }

        // let's try an assignment w/o groups. ta should only have access to students in group 1
        version = submissionLogic.getSubmissionVersionById(testData.st1a3FirstVersion.getId());
        assertTrue(version.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        version = submissionLogic.getSubmissionVersionById(testData.st1a3CurrVersion.getId());
        assertTrue(version.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));

        // now make sure ta can't see st 3
        try {
            version = submissionLogic.getSubmissionVersionById(testData.st3a3CurrVersion.getId());
            fail("ta should not be able to access st3's submission for a3!");
        } catch (SecurityException se) {}

        // student may see their own submission
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        version = submissionLogic.getSubmissionVersionById(testData.st1a3CurrVersion.getId());
        assertTrue(version.getAssignmentSubmission().getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        // feedback was not released, so double check that it was not populated
        assertTrue(version.getFeedbackAttachSet().isEmpty());
        assertTrue(version.getFeedbackNotes().equals(""));
        assertTrue(version.getAnnotatedText().equals(""));
        // make sure these are populated! other users may not see this info b/c draft status
        assertTrue(version.getSubmissionAttachSet().size() == 1);
        assertTrue(!version.getSubmittedText().equals(""));

        // double check that student can't see other users
        try {
            version = submissionLogic.getSubmissionVersionById(testData.st2a3CurrVersion.getId());
            fail("st1 should not be able to access st2's submission for a3!");
        } catch (SecurityException se) {}
    }

    public void testGetCurrentSubmissionByAssignmentIdAndStudentId() {
        // try passing a null assignmentId
        try {
            submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(null, AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not handle null assignmentId passed to testGetCurrentSubmissionByAssignmentIdAndStudentId");
        } catch (IllegalArgumentException iae) {}
        // try passing a null studentId
        try {
            submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a1Id, null, null);
            fail("did not handle null studentId passed to testGetCurrentSubmissionByAssignmentIdAndStudentId");
        } catch (IllegalArgumentException iae) {}

        // pass an assignmentId that doesn't exist
        try {
            submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(12345L, AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch non-existent id passed to getCurrentSubmissionByAssignmentIdAndStudentId");
        } catch (AssignmentNotFoundException anfe) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try to get one for a student who hasn't made a submission yet
        AssignmentSubmission submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a2Id, AssignmentTestDataLoad.STUDENT1_UID, null);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        assertNull(submission.getId()); // this should be an "empty rec"

        // try one for a student with multiple versions
        submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a1Id, AssignmentTestDataLoad.STUDENT2_UID, null);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID));
        assertTrue(submission.getCurrentSubmissionVersion().getId().equals(testData.st2a1CurrVersion.getId()));

        // get a currentVersion that is draft and make sure submission info is not populated
        submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a3Id, AssignmentTestDataLoad.STUDENT1_UID, null);
        assertTrue(submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        assertTrue(submission.getCurrentSubmissionVersion().getId().equals(testData.st1a3CurrVersion.getId()));
        assertTrue(submission.getCurrentSubmissionVersion().getSubmissionAttachSet().isEmpty());
        assertTrue(submission.getCurrentSubmissionVersion().getSubmittedText().equals(""));

        // what if the student is not part of the passed assignment?
        // ie it is restricted to groups that the student is not a member of

        // now, switch to a ta
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // should only have access to student 1 b/c in group 1
        submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a1Id, AssignmentTestDataLoad.STUDENT1_UID, null);
        assertEquals(AssignmentTestDataLoad.STUDENT1_UID, submission.getUserId());
        assertEquals(testData.st1a1CurrVersion.getId(), submission.getCurrentSubmissionVersion().getId());

        try {
            submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a1Id, AssignmentTestDataLoad.STUDENT2_UID, null);
            fail("Ta should not have authorization to retrieve submission for st2 through getCurrentSubmissionByAssignmentIdAndStudentId");
        } catch (SecurityException se) {}

        // shouldn't have access to this one either
        try {
            submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a3Id, AssignmentTestDataLoad.STUDENT2_UID, null);
            fail("Ta should not have authorization to retrieve submission for st2 through getCurrentSubmissionByAssignmentIdAndStudentId");
        } catch (SecurityException se) {}

        // now switch to a student
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        // should be able to retrieve own submission
        submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a3Id, AssignmentTestDataLoad.STUDENT1_UID, null);
        assertEquals(AssignmentTestDataLoad.STUDENT1_UID, submission.getUserId());
        assertEquals(testData.st1a3CurrVersion.getId(), submission.getCurrentSubmissionVersion().getId());
        // this is a draft, so check that the submission info is populated for student
        assertEquals(1, submission.getCurrentSubmissionVersion().getSubmissionAttachSet().size());
        assertNotSame("", submission.getCurrentSubmissionVersion().getSubmittedText());

        // double check that student can't view others
        try {
            submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a1Id, AssignmentTestDataLoad.STUDENT2_UID, null);
            fail("Did not catch student accessing another student via getCurrentSubmissionByAssignmentIdAndStudentId");
        } catch (SecurityException se) {}
    }

    public void testSaveStudentSubmission() {
        // try passing a null userId
        try {
            submissionLogic.saveStudentSubmission(null, new Assignment2(), true, null, Boolean.TRUE, null, false);
            fail("Did not catch null userId passed to saveStudentSubmission");
        } catch (IllegalArgumentException iae) {}

        // try passing a null assignment
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, null, true, null, Boolean.TRUE, null, false);
            fail("Did not catch null assignment passed to saveStudentSubmission");
        } catch (IllegalArgumentException iae) {}

        // try passing an empty assignment (with no id)
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, 
                    new Assignment2(), false, null, Boolean.TRUE, null, false);
        } catch (IllegalArgumentException iae) {}

        // let's see if an instructor can make a submission for a student
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID,
                    testData.a1, false, null, Boolean.TRUE, null, false);
            fail("did not catch instructor trying to save a student's submission via saveStudentSubmission");
        } catch (SecurityException se) {}
        // do the same thing with the saveAsDraftIfClosed switch true
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID,
                    testData.a1, false, null, Boolean.TRUE, null, true);
            fail("did not catch instructor trying to save a student's submission via saveStudentSubmission");
        } catch (SecurityException se) {}

        // try the ta
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID,
                    testData.a1, false, null, Boolean.TRUE, null, false);
            fail("did not catch ta trying to save a student's submission via saveStudentSubmission");
        } catch (SecurityException se) {}
        // do the same thing with the saveAsDraftIfClosed switch true
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID,
                    testData.a1, false, null, Boolean.TRUE, null, true);
            fail("did not catch ta trying to save a student's submission via saveStudentSubmission");
        } catch (SecurityException se) {}

        // scenarios
        // student tries to save for assignment that he/she is not part of
        // student creates a draft
        // student edits the draft and submits it
        // student creates new submission where there is no prev submission
        // student resubmits when ok
        // student resubmits but is not allowed
        // student submits for closed assignment with saveAsDraftIfClosed == true

        // student 1 does not have any submission for a2 yet
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        // double check that no submission exists yet
        List<AssignmentSubmission> subList = dao.findByProperties(
                AssignmentSubmission.class, new String[] {"userId", "assignment"}, 
                new Object[] {AssignmentTestDataLoad.STUDENT1_UID, testData.a2});
        assertTrue(subList.isEmpty());

        SubmissionAttachment attach1 = new SubmissionAttachment();
        attach1.setAttachmentReference("ref1");
        SubmissionAttachment attach2 = new SubmissionAttachment();
        attach2.setAttachmentReference("ref2");
        Set<SubmissionAttachment> attachSet = new HashSet<SubmissionAttachment>();
        attachSet.add(attach1);
        attachSet.add(attach2);

        submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, 
                testData.a2, true, "this is my text", Boolean.TRUE, attachSet, false);

        // now check that it exists 
        subList = dao.findByProperties(
                AssignmentSubmission.class, new String[] {"userId", "assignment"}, 
                new Object[] {AssignmentTestDataLoad.STUDENT1_UID, testData.a2});
        assertFalse(subList.isEmpty());
        AssignmentSubmission existingSub = (AssignmentSubmission)subList.get(0);
        Long subId = existingSub.getId();

        assertNotNull(existingSub);
        AssignmentSubmissionVersion currVersion = dao.getCurrentSubmissionVersionWithAttachments(existingSub);
        assertTrue(currVersion.isDraft());
        // submittedVersionNumber for a student submission starts at 1
        assertEquals(1, currVersion.getSubmittedVersionNumber());
        // make sure it wasn't marked as completed yet since still draft
        assertFalse(existingSub.isCompleted());

        // now let's try to edit this version but keep it draft
        // should not create a new version
        attachSet.remove(attach2);
        submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, 
                testData.a2, true, "this is my text - revised!", Boolean.TRUE, attachSet, false);
        // text and attach should have been updated
        existingSub = (AssignmentSubmission)dao.findById(AssignmentSubmission.class, subId);
        List<AssignmentSubmissionVersion> versionHistory = dao.getVersionHistoryForSubmission(existingSub);
        assertTrue(versionHistory.size() == 1);
        currVersion = dao.getCurrentSubmissionVersionWithAttachments(existingSub);
        // make sure it wasn't marked as completed yet since still draft
        assertFalse(existingSub.isCompleted());

        // now let's actually submit it (make draft = false)
        submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, 
                testData.a2, false, "this is my text - revised!", Boolean.TRUE, currVersion.getSubmissionAttachSet(), false);
        existingSub = (AssignmentSubmission)dao.findById(AssignmentSubmission.class, subId);
        versionHistory = dao.getVersionHistoryForSubmission(existingSub);
        assertTrue(versionHistory.size() == 1);
        // should be marked as completed now
        assertTrue(existingSub.isCompleted());

        // next time, the student should get an error b/c not allowed to resubmit
        String sub1Text = "sub 1";
        String sub2Text = "sub 2";
        String sub3Text = "sub 3";
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, 
                    testData.a2, false, sub1Text, Boolean.TRUE, currVersion.getSubmissionAttachSet(), false);
            fail("submission saved even though not allowed to resubmit!");
        } catch (SubmissionClosedException sce) {}

        // allow student to submit one more time
        existingSub.setNumSubmissionsAllowed(2);
        dao.save(existingSub);

        submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, 
                testData.a2, false, sub2Text, Boolean.TRUE, null, false);
        versionHistory = dao.getVersionHistoryForSubmission(existingSub);
        assertTrue(versionHistory.size() == 2);

        // double check that student is not allowed to submit again
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, 
                    testData.a2, false, sub2Text, Boolean.TRUE, currVersion.getSubmissionAttachSet(), false);
            fail("submission saved even though not allowed to resubmit!");
        } catch (SubmissionClosedException sce) {}

        // now let's flip the saveAsDraftIfClosed switch
        submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, 
                testData.a2, false, sub3Text, Boolean.TRUE, null, true);
        versionHistory = dao.getVersionHistoryForSubmission(existingSub);
        assertEquals(3, versionHistory.size());
        for (AssignmentSubmissionVersion ver : versionHistory) {
            if (ver.getSubmittedText().equals(sub1Text) || ver.getSubmittedText().equals(sub2Text)) {
                // ignore
            } else if (ver.getSubmittedText().equals(sub3Text)) {
                // make sure it is draft
                assertTrue(ver.isDraft());
            }
        }

        // what if student is not allowed to submit to a restricted assignment?
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, testData.a4, true, null, Boolean.TRUE, null, false);
            fail("did not catch student making submission to assignment that is restricted");
        } catch(SecurityException se) {}
        
        
        // honor pledge
        
        // No honor pledge submitted on an assignment that requires one and submission IS a draft
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, testData.a5, true, null, Boolean.FALSE, null, false);
        } catch(SubmissionHonorPledgeException shpe) {
            fail("should not have caught a student making a no honor pledge supplied submission to assignment that requires an honor pledge and it is a draft");
        }

        // No honor pledge submitted on an assignment that requires one and submission is NOT a draft
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, testData.a5, false, null, Boolean.FALSE, null, false);
            fail("did not catch a student making a no honor pledge supplied submission to an assignment that requires an honor pledge and it's NOT a draft");
        } catch(SubmissionHonorPledgeException shpe) {}
        
        // Honor pledge submitted on an assignment that requires one and submission is NOT a draft
        try {
            submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT1_UID, testData.a5, false, null, Boolean.TRUE, null, false);
        } catch(SubmissionHonorPledgeException shpe) {
            fail("should not have caught a student making an honor pledge supplied submission to an assignment that requires an honor pledge and it's NOT a draft");
        }
        
    }

    public void testSaveAllInstructorFeedback() {
        Map<String, Collection<AssignmentSubmissionVersion>> updateMap = new HashMap<String, Collection<AssignmentSubmissionVersion>>();
        // try a null assignment
        try {
            submissionLogic.saveAllInstructorFeedback(null, updateMap, true);
            fail("did not catch null assignment passed to saveAllInstructorFeedback");
        } catch (IllegalArgumentException iae) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try a studentId not associated with the passed version
        Collection<AssignmentSubmissionVersion> versionList = new ArrayList<AssignmentSubmissionVersion>();
        versionList.add(testData.st1a1CurrVersion);
        updateMap.put(AssignmentTestDataLoad.STUDENT2_UID, versionList);
        try {
            submissionLogic.saveAllInstructorFeedback(testData.a1, updateMap, true);
            fail("did not catch passed studentId not associated with the given version");
        } catch (VersionNotFoundException iae) {}

        // try an assignment not associated with the passed version
        updateMap = new HashMap<String, Collection<AssignmentSubmissionVersion>>();
        updateMap.put(AssignmentTestDataLoad.STUDENT1_UID, versionList);
        try {
            submissionLogic.saveAllInstructorFeedback(testData.a2, updateMap, true);
            fail("did not catch passed assignment not associated with the given version");
        } catch (VersionNotFoundException iae) {}

        // start as an instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try a null version when submission already exists. this should be saved as a
        // feedback-only version (submittedVersionNumber = 0)

        AssignmentSubmissionVersion newVersion = new AssignmentSubmissionVersion();
        newVersion.setFeedbackNotes("my feedback prior to submission");
        updateMap = new HashMap<String, Collection<AssignmentSubmissionVersion>>();
        versionList = new ArrayList<AssignmentSubmissionVersion>();
        versionList.add(newVersion);
        updateMap.put(AssignmentTestDataLoad.STUDENT1_UID, versionList);
        submissionLogic.saveAllInstructorFeedback(testData.a1, updateMap, true);

        AssignmentSubmission st1a1Submission = dao.getSubmissionWithVersionHistoryForStudentAndAssignment(AssignmentTestDataLoad.STUDENT1_UID, testData.a1);
        assertTrue(st1a1Submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        Long st1a1SubId = st1a1Submission.getId();
        assertNotNull(st1a1SubId);
        assertEquals(2, st1a1Submission.getSubmissionHistorySet().size());
        // there should be one version with submittedVersionNumber = 1 (the one submitted by student)
        // and one = 0 (feedback-only)
        for (AssignmentSubmissionVersion ver : st1a1Submission.getSubmissionHistorySet()) {
            if (ver.getId().equals(testData.st1a1CurrVersion.getId())) {
                assertEquals(1, ver.getSubmittedVersionNumber());
            } else {
                assertEquals(0, ver.getSubmittedVersionNumber());
                assertEquals("my feedback prior to submission", ver.getFeedbackNotes());
            }
        } 

        // submit feedback for student w/o submission
        // student 1 has not submitted for a2 yet
        Set<FeedbackAttachment> feedbackAttachSet = new HashSet<FeedbackAttachment>();
        FeedbackAttachment fb1 = new FeedbackAttachment(null, "fb1");
        feedbackAttachSet.add(fb1);
        newVersion = new AssignmentSubmissionVersion();
        newVersion.setFeedbackNotes("Please submit this soon!");
        newVersion.setFeedbackAttachSet(feedbackAttachSet);

        updateMap = new HashMap<String, Collection<AssignmentSubmissionVersion>>();
        versionList = new ArrayList<AssignmentSubmissionVersion>();

        versionList.add(newVersion);
        updateMap.put(AssignmentTestDataLoad.STUDENT1_UID, versionList);

        submissionLogic.saveAllInstructorFeedback(testData.a2, updateMap, true);
        // try to retrieve it now
        AssignmentSubmission st1a2Submission = dao.getSubmissionWithVersionHistoryForStudentAndAssignment(AssignmentTestDataLoad.STUDENT1_UID, testData.a2);
        assertTrue(st1a2Submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        Long st1a2SubId = st1a2Submission.getId();
        assertNotNull(st1a2SubId);

        AssignmentSubmissionVersion st1a2CurrVersion = dao.getCurrentSubmissionVersionWithAttachments(st1a2Submission);
        assertNotNull(st1a2CurrVersion);
        assertNull(st1a2CurrVersion.getFeedbackReleasedDate());
        // fb without a submission should have submittedVersionNumber = 0
        assertEquals(0, st1a2CurrVersion.getSubmittedVersionNumber()); 
        Long st1a2CurrVersionId = st1a2CurrVersion.getId();

        newVersion.setId(st1a2CurrVersionId);
        updateMap = new HashMap<String, Collection<AssignmentSubmissionVersion>>();
        versionList = new ArrayList<AssignmentSubmissionVersion>();

        versionList.add(newVersion);
        updateMap.put(AssignmentTestDataLoad.STUDENT1_UID, versionList);
        // let's try to re-save this one without the attachments
        submissionLogic.saveAllInstructorFeedback(testData.a2, updateMap, true);
        st1a2Submission = (AssignmentSubmission)dao.findById(AssignmentSubmission.class, st1a2SubId);
        assertTrue(st1a2Submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));

        // make sure there is still only one version
        List<AssignmentSubmissionVersion> versionHistory = dao.getVersionHistoryForSubmission(st1a2Submission);
        assertTrue(versionHistory.size() == 1);

        // try a TA
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        updateMap = new HashMap<String, Collection<AssignmentSubmissionVersion>>();

        updateMap.put(AssignmentTestDataLoad.STUDENT2_UID, versionList);
        // should not be allowed to grade student 2
        try {
            submissionLogic.saveAllInstructorFeedback(testData.a1, updateMap, true);
            fail("Did not catch SecurityException when TA attempted to grade unauth student!");
        } catch (SecurityException se) {}

        // should be allowed to grade st1 for a1
        // make sure versions aren't added
        newVersion = new AssignmentSubmissionVersion();
        newVersion.setId(testData.st1a1CurrVersion.getId());
        newVersion.setAnnotatedText("annotated fb");
        newVersion.setFeedbackNotes("notes");
        updateMap = new HashMap<String, Collection<AssignmentSubmissionVersion>>();
        versionList = new ArrayList<AssignmentSubmissionVersion>();

        versionList.add(newVersion);
        updateMap.put(AssignmentTestDataLoad.STUDENT1_UID, versionList);

        submissionLogic.saveAllInstructorFeedback(testData.a1, updateMap, true);
        versionHistory = dao.getVersionHistoryForSubmission(testData.st1a1Submission);
        assertEquals(versionHistory.size(), 2);
        /*TODO Fix this - it isn't working b/c id of student's version < feedback-only version
         * AssignmentSubmissionVersion currVersion = dao.getCurrentSubmissionVersionWithAttachments(testData.st1a1Submission);
        assertTrue(currVersion.getFeedbackNotes().equals("notes"));
        assertTrue(currVersion.getAssignmentSubmission().getNumSubmissionsAllowed().equals(2));
        assertTrue(currVersion.getAssignmentSubmission().getResubmitCloseDate().equals(resubmitCloseDate));*/

        // student should not be authorized
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            submissionLogic.saveAllInstructorFeedback(testData.a1, updateMap, true);
            fail("Student was able to saveInstructorFeedback without authorization!!!");
        } catch (SecurityException se) {}
    }

    public void testSaveInstructorFeedback() {
        // try a null studentId
        try {
            submissionLogic.saveInstructorFeedback(testData.st1a1CurrVersion.getId(), null, testData.a1, 
                    null, null, null, null);
            fail("did not catch null studentId passed to saveInstructorFeedback");
        } catch (IllegalArgumentException iae) {}
        // try a null assignment
        try {
            submissionLogic.saveInstructorFeedback(testData.st1a1CurrVersion.getId(), 
                    AssignmentTestDataLoad.STUDENT1_UID, null, null, null, null, null);
            fail("did not catch null assignment passed to saveInstructorFeedback");
        } catch (IllegalArgumentException iae) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try a versionId that doesn't exist
        try {
            submissionLogic.saveInstructorFeedback(12345L, AssignmentTestDataLoad.STUDENT1_UID, 
                    testData.a2, null, null, null, null);
            fail("did not catch passed versionId that does not exist to saveInstructorFeedback");
        } catch (VersionNotFoundException iae) {}

        // try a studentId not associated with the passed version
        try {
            submissionLogic.saveInstructorFeedback(testData.st1a1CurrVersion.getId(), AssignmentTestDataLoad.STUDENT2_UID, 
                    testData.a1, null, null, null, null);
            fail("did not catch passed studentId not associated with the given versionId");
        } catch (VersionNotFoundException iae) {}

        // try an assignment not associated with the passed version
        try {
            submissionLogic.saveInstructorFeedback(testData.st1a1CurrVersion.getId(), AssignmentTestDataLoad.STUDENT1_UID, 
                    testData.a2, null, null, null, null);
            fail("did not catch passed assignment not associated with the given versionId");
        } catch (VersionNotFoundException iae) {}

        // start as an instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try a null version when submission already exists. this should be saved as a
        // feedback-only version (submittedVersionNumber = 0)

        submissionLogic.saveInstructorFeedback(null, AssignmentTestDataLoad.STUDENT1_UID, 
                testData.a1, null, "Please submit this soon!", null, null);

        AssignmentSubmission st1a1Submission = dao.getSubmissionWithVersionHistoryForStudentAndAssignment(AssignmentTestDataLoad.STUDENT1_UID, testData.a1);
        assertTrue(st1a1Submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        Long st1a1SubId = st1a1Submission.getId();
        assertNotNull(st1a1SubId);
        assertEquals(2, st1a1Submission.getSubmissionHistorySet().size());
        // there should be one version with submittedVersionNumber = 1 (the one submitted by student)
        // and one = 0 (feedback-only)
        for (AssignmentSubmissionVersion ver : st1a1Submission.getSubmissionHistorySet()) {
            if (ver.getId().equals(testData.st1a1CurrVersion.getId())) {
                assertEquals(1, ver.getSubmittedVersionNumber());
            } else {
                assertEquals(0, ver.getSubmittedVersionNumber());
                assertEquals("Please submit this soon!", ver.getFeedbackNotes());
            }
        } 

        // submit feedback for student w/o submission
        // student 1 has not submitted for a2 yet
        Set<FeedbackAttachment> feedbackAttachSet = new HashSet<FeedbackAttachment>();
        FeedbackAttachment fb1 = new FeedbackAttachment(null, "fb1");
        feedbackAttachSet.add(fb1);

        submissionLogic.saveInstructorFeedback(null, AssignmentTestDataLoad.STUDENT1_UID,
                testData.a2, null, "Please submit this soon!", new Date(), feedbackAttachSet);
        // try to retrieve it now
        AssignmentSubmission st1a2Submission = dao.getSubmissionWithVersionHistoryForStudentAndAssignment(AssignmentTestDataLoad.STUDENT1_UID, testData.a2);
        assertTrue(st1a2Submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));
        Long st1a2SubId = st1a2Submission.getId();
        assertNotNull(st1a2SubId);

        AssignmentSubmissionVersion st1a2CurrVersion = dao.getCurrentSubmissionVersionWithAttachments(st1a2Submission);
        assertNotNull(st1a2CurrVersion);
        // fb without a submission should have submittedVersionNumber = 0
        assertEquals(0, st1a2CurrVersion.getSubmittedVersionNumber()); 
        Long st1a2CurrVersionId = st1a2CurrVersion.getId();

        // let's try to re-save this one without the attachments
        submissionLogic.saveInstructorFeedback(st1a2CurrVersionId, AssignmentTestDataLoad.STUDENT1_UID, 
                testData.a2, null, "Revised feedback", new Date(), null);
        st1a2Submission = (AssignmentSubmission)dao.findById(AssignmentSubmission.class, st1a2SubId);
        assertTrue(st1a2Submission.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID));

        // make sure there is still only one version
        List<AssignmentSubmissionVersion> versionHistory = dao.getVersionHistoryForSubmission(st1a2Submission);
        assertTrue(versionHistory.size() == 1);

        // try a TA
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // should not be allowed to grade student 2
        try {
            submissionLogic.saveInstructorFeedback(testData.st2a1CurrVersion.getId(), 
                    AssignmentTestDataLoad.STUDENT2_UID, testData.a1, null, null, null, null);
            fail("Did not catch SecurityException when TA attempted to grade unauth student!");
        } catch (SecurityException se) {}

        // should be allowed to grade st1 for a1
        // make sure versions aren't added
        submissionLogic.saveInstructorFeedback(testData.st1a1CurrVersion.getId(), 
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1, "annotated fb", "notes", null, feedbackAttachSet);
        versionHistory = dao.getVersionHistoryForSubmission(testData.st1a1Submission);
        assertEquals(versionHistory.size(), 2);
        /*TODO Fix this - it isn't working b/c id of student's version < feedback-only version
         * AssignmentSubmissionVersion currVersion = dao.getCurrentSubmissionVersionWithAttachments(testData.st1a1Submission);
    	assertTrue(currVersion.getFeedbackNotes().equals("notes"));
    	assertTrue(currVersion.getAssignmentSubmission().getNumSubmissionsAllowed().equals(2));
    	assertTrue(currVersion.getAssignmentSubmission().getResubmitCloseDate().equals(resubmitCloseDate));*/

        // student should not be authorized
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            submissionLogic.saveInstructorFeedback(testData.st1a1CurrVersion.getId(), 
                    AssignmentTestDataLoad.STUDENT1_UID, testData.a1, null, null, null, null);
            fail("Student was able to saveInstructorFeedback without authorization!!!");
        } catch (SecurityException se) {}
    }

    public void testGetViewableSubmissionsWithHistoryForAssignmentId() {
        // try a null assignmentId
        try {
            submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(null, null);
            fail("did not catch null assignmentId passed to getViewableSubmissionsWithHistoryForAssignmentId");
        } catch (IllegalArgumentException iae) {}

        // try a non-existent assignmentId
        try {
            submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(12345L, null);
            fail("did not catch non-existent id passed to getViewableSubmissionsWithHistoryForAssignmentId");
        } catch (AssignmentNotFoundException anfe) {}

        // start as instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        // we should get 2 students back for a1 b/c group restrictions
        List<AssignmentSubmission> subList = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(testData.a1Id, null);
        assertTrue(subList.size() == 2);
        assertNotNull(subList.get(0).getSubmissionHistorySet());
        for (int i=0; i<subList.size(); i++) {
            AssignmentSubmission thisSub = (AssignmentSubmission) subList.get(i);
            if (thisSub.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID)) {
                // there should be 3 versions in the history
                assertEquals(1, thisSub.getSubmissionHistorySet().size());
            } else if (thisSub.getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID)) {
                assertEquals(3, thisSub.getSubmissionHistorySet().size());
            } else {
                throw new IllegalArgumentException("Unknown/Invalid user returned by testGetViewableSubmissionsWithHistoryForAssignmentId!");
            }
        }

        // we should get 3 for a2 b/c no restrictions
        subList = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(testData.a2Id, null);
        assertTrue(subList.size() == 3);
        // we should get 3 for a3 b/c no restrictions
        subList = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(testData.a3Id, null);
        assertTrue(subList.size() == 3);
        // let's make sure the submission for st1 is restricted b/c draft
        for (AssignmentSubmission sub : subList) {
            if (sub.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID)) {
                assertEquals(sub.getCurrentSubmissionVersion().getSubmittedText(), "");
                assertTrue(sub.getCurrentSubmissionVersion().getSubmissionAttachSet().isEmpty());
            }
        }

        // we should get 1 for a4 b/c group restrictions
        subList = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(testData.a4Id, null);
        assertTrue(subList.size() == 1);

        // now become ta
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // we should get 1 student back for a1 b/c only allowed to view group 1
        subList = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(testData.a1Id, null);
        assertTrue(subList.size() == 1);
        // ta may only view students in his/her group, even though assignments aren't restricted to groups

        subList = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(testData.a2Id, null);
        assertEquals(1, subList.size());

        subList = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(testData.a3Id, null);
        assertEquals(1, subList.size());

        // a4 is restricted to a different group than the ta's so no students
        try {
            subList = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(testData.a4Id, null);
            fail("Did not catch user attempting to access submissions via getViewableSubmissionsForAssignmentId without permission");
        } catch (SecurityException se) {}

        // students should get SecurityException
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            subList = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(testData.a1Id, null);
            fail("Did not catch student attempting to access submissions via getViewableSubmissionsWithHistoryForAssignmentId");
        } catch (SecurityException se) {}

    }

    public void testGetViewableSubmissionsForAssignmentId() {
        // try a null assignmentId
        try {
            submissionLogic.getViewableSubmissionsForAssignmentId(null, null);
            fail("did not catch null assignmentId passed to getViewableSubmissionsForAssignmentId");
        } catch (IllegalArgumentException iae) {}

        // try a non-existent assignmentId
        try {
            submissionLogic.getViewableSubmissionsForAssignmentId(12345L, null);
            fail("did not catch non-existent id passed to getViewableSubmissionsForAssignmentId");
        } catch (AssignmentNotFoundException anfe) {}

        // start as instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        // we should get 2 students back for a1 b/c group restrictions
        List<AssignmentSubmission> subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a1Id, null);
        assertEquals(2, subList.size());
        // we should get 3 for a2 b/c no restrictions
        subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a2Id, null);
        assertEquals(3, subList.size());
        // we should get 3 for a3 b/c no restrictions
        subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a3Id, null);
        assertEquals(3, subList.size());
        // let's make sure the submission for st1 is restricted b/c draft
        for (AssignmentSubmission sub : subList) {
            if (sub.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID)) {
                assertEquals(sub.getCurrentSubmissionVersion().getSubmittedText(), "");
                assertTrue(sub.getCurrentSubmissionVersion().getSubmissionAttachSet().isEmpty());
            }
        }

        // we should get 1 for a4 b/c group restrictions
        subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a4Id, null);
        assertEquals(1, subList.size());

        //let's check the group filter - should only return one student now
        subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a1Id, AssignmentTestDataLoad.GROUP1_NAME);
        assertEquals(1, subList.size());

        // now become ta
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // we should get 1 student back for a1, a2, and a3 b/c only allowed to view group 1
        subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a1Id, null);
        assertTrue(subList.size() == 1);
        
        subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a2Id, null);
        assertTrue(subList.size() == 1);

        subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a3Id, null);
        assertTrue(subList.size() == 1);
        
        try {
            subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a4Id, null);
            fail("Did not catch user attempting to access submissions via getViewableSubmissionsForAssignmentId without permission");
        } catch (SecurityException se) {}

        // students should get SecurityException
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            subList = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a1Id, null);
            fail("Did not catch student attempting to access submissions via getViewableSubmissionsForAssignmentId");
        } catch (SecurityException se) {}

    }

    public void testSetSubmissionStatusConstantForAssignments() {
        // try null studentId
        try {
            submissionLogic.getSubmissionStatusConstantForAssignments(new ArrayList<Assignment2>(), null);
            fail("Did not catch null studentId passed to setSubmissionStatusForAssignments");
        } catch(IllegalArgumentException iae) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try a null assignment list
        // should do nothing
        submissionLogic.getSubmissionStatusConstantForAssignments(null, AssignmentTestDataLoad.STUDENT1_UID);

        // let's create a list of assignments
        List<Assignment2> assignList = new ArrayList<Assignment2>();
        assignList.add(testData.a1);
        assignList.add(testData.a2);
        assignList.add(testData.a3);
        assignList.add(testData.a4);

        Map<Assignment2, Integer> assignToStatusMap = submissionLogic.getSubmissionStatusConstantForAssignments(assignList, AssignmentTestDataLoad.STUDENT1_UID);
        Integer status = assignToStatusMap.get(testData.a1);
        assertTrue(status.equals(AssignmentConstants.SUBMISSION_SUBMITTED));

        status = assignToStatusMap.get(testData.a2);
        assertTrue(status.equals(AssignmentConstants.SUBMISSION_NOT_STARTED));

        status = assignToStatusMap.get(testData.a3);
        assertTrue(status.equals(AssignmentConstants.SUBMISSION_IN_PROGRESS));

        status = assignToStatusMap.get(testData.a4);
        assertTrue(status.equals(AssignmentConstants.SUBMISSION_NOT_STARTED));
    }

    public void testGetSubmissionStatusForVersion() {

        // can be in progress, not started, or submitted
        // due date has passed
        // start with one that is in progress/draft
        Calendar cal = Calendar.getInstance();
        cal.set(2005, 10, 01);
        Date dueDate = cal.getTime();

        Integer status = submissionLogic.getSubmissionStatusForVersion(testData.st1a3CurrVersion, dueDate, null);
        assertEquals(AssignmentConstants.SUBMISSION_IN_PROGRESS, status.intValue());
        // empty submission 
        status = submissionLogic.getSubmissionStatusForVersion(new AssignmentSubmissionVersion(), dueDate, null);
        assertEquals(AssignmentConstants.SUBMISSION_NOT_STARTED, status.intValue());
        // null submission
        status = submissionLogic.getSubmissionStatusForVersion(null, dueDate, null);
        assertEquals(AssignmentConstants.SUBMISSION_NOT_STARTED, status.intValue());
        // let's try one that is submitted
        status = submissionLogic.getSubmissionStatusForVersion(testData.st1a1CurrVersion, dueDate, null);
        assertEquals(AssignmentConstants.SUBMISSION_LATE, status.intValue());
        
        // it shouldn't be late if we extend the due date for this student
        status = submissionLogic.getSubmissionStatusForVersion(testData.st1a1CurrVersion, dueDate, new Date());
        assertEquals(AssignmentConstants.SUBMISSION_SUBMITTED, status.intValue());

        // try submitted one with null due Date
        status = submissionLogic.getSubmissionStatusForVersion(testData.st1a1CurrVersion, null, null);
        assertEquals(AssignmentConstants.SUBMISSION_SUBMITTED, status.intValue());

        // try a due date in the future
        cal.set(2020, 10, 1);
        dueDate = cal.getTime();
        status = submissionLogic.getSubmissionStatusForVersion(testData.st1a1CurrVersion, dueDate, null);
        assertEquals(AssignmentConstants.SUBMISSION_SUBMITTED, status.intValue());
    }

    public void testGetNumberOfRemainingSubmissionsForStudent() {
        // try a null student
        try {
            submissionLogic.getNumberOfRemainingSubmissionsForStudent(null, testData.a1Id);
            fail("did not catch null studentId passed to getNumberOfRemainingSubmissionsForStudent");
        } catch (IllegalArgumentException iae) {}

        // try a null assignment
        try {
            submissionLogic.getNumberOfRemainingSubmissionsForStudent(AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null assignmentId passed to getNumberOfRemainingSubmissionsForStudent");
        } catch (IllegalArgumentException iae) {}

        // try one with a submission already and no resubmission
        int numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertEquals(0, numRemaining);

        // let's allow resubmission on the assignment level for assign1.
        // allow 3 submissions - this means st1 will still be open but not st2
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        Assignment2 assign1 = dao.getAssignmentByIdWithGroups(testData.a1Id);
        assign1.setNumSubmissionsAllowed(3);
        assign1.setAcceptUntilDate(null);
        assignmentLogic.saveAssignment(assign1);

        // st 1 only has one submission, so still open
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertEquals(2, numRemaining);
        // st 2 already has 3 submissions, so closed
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertEquals(0, numRemaining);

        // now let's set resubmission on the submission level - increase allowed # to 4
        // st 2 only has 3 submissions
        List<String> studentList = new ArrayList<String>();
        studentList.add(AssignmentTestDataLoad.STUDENT2_UID);
        testData.st2a1CurrVersion = dao.getAssignmentSubmissionVersionByIdWithAttachments(testData.st2a1CurrVersion.getId());
        submissionLogic.updateStudentResubmissionOptions(studentList, testData.a1, 4, null);
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertEquals(1, numRemaining);

        // let's make a draft submission to double check it is still open
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT2_UID, testData.a1, true, "blah", Boolean.TRUE, null, false);
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertEquals(1, numRemaining);

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // now let's restrict it by date on the submission level
        Calendar cal = Calendar.getInstance();
        cal.set(2005, 10, 01);
        Date resubmitCloseDate = cal.getTime();
        submissionLogic.updateStudentResubmissionOptions(studentList, testData.a1, 4, resubmitCloseDate);
        // should be closed even though num submissions not reached
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertEquals(0, numRemaining);
        // should still be open for student1 b/c we haven't changed their submission
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertEquals(2, numRemaining);

        // let's allow resubmission on the assignment level - should not affect it
        // since submission level trumps
        assign1.setNumSubmissionsAllowed(4);
        assignmentLogic.saveAssignment(assign1);

        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertEquals(0, numRemaining);
        // there are no submission-level settings,so use assignment-level
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertEquals(3, numRemaining);

        // let's make this assignment not open yet
        cal.set(2020, 10, 01);
        Date assignOpenTime = cal.getTime();
        assign1.setOpenDate(assignOpenTime);
        assignmentLogic.saveAssignment(assign1);
        // should be closed for both
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertEquals(0, numRemaining);
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertEquals(0, numRemaining);

        // open it back up
        // let's restrict it by date on the assign level
        cal.set(2000, 10, 01);
        assignOpenTime = cal.getTime();
        assign1.setOpenDate(assignOpenTime);
        assign1.setAcceptUntilDate(resubmitCloseDate);
        assignmentLogic.saveAssignment(assign1);


        // let's open up on the submission level for student 2 and set the extended due date (otherwise, 
        //it won't allow the student to re-submit because the assignment is closed
        cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 1);
        submissionLogic.updateStudentResubmissionOptions(studentList, testData.a1, 4, cal.getTime());

        // student 1 should not be able to submit b/c of assignment-level restriction
        // but student 2 can
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertEquals(1, numRemaining);
        numRemaining = submissionLogic.getNumberOfRemainingSubmissionsForStudent(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertEquals(0, numRemaining);
    }


    public void testSubmissionIsOpenForStudentForAssignment() {
        // try a null student
        try {
            submissionLogic.isSubmissionOpenForStudentForAssignment(null, testData.a1Id);
            fail("did not catch null studentId passed to submissionIsOpenForStudentForAssignment");
        } catch (IllegalArgumentException iae) {}

        // try a null assignment
        try {
            submissionLogic.isSubmissionOpenForStudentForAssignment(AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null assignmentId passed to submissionIsOpenForStudentForAssignment");
        } catch (IllegalArgumentException iae) {}

        // try one with a submission already and no resubmission
        boolean open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertFalse(open);

        // let's allow resubmission on the assignment level for assign1.
        // allow 3 submissions - this means st1 will still be open but not st2
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        Assignment2 assign1 = dao.getAssignmentByIdWithGroups(testData.a1Id);
        assign1.setNumSubmissionsAllowed(3);
        assign1.setAcceptUntilDate(null);
        assignmentLogic.saveAssignment(assign1);

        // st 1 only has one submission, so still open
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertTrue(open);
        // st 2 already has 3 submissions, so closed
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertFalse(open);

        // now let's set resubmission on the submission level
        List<String> studentUids = new ArrayList<String>();
        studentUids.add(AssignmentTestDataLoad.STUDENT2_UID);
        testData.st2a1CurrVersion = dao.getAssignmentSubmissionVersionByIdWithAttachments(testData.st2a1CurrVersion.getId());
        submissionLogic.updateStudentResubmissionOptions(studentUids, testData.a1, 4, null);
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertTrue(open);

        // let's make a draft submission to double check it is still open
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        submissionLogic.saveStudentSubmission(AssignmentTestDataLoad.STUDENT2_UID, testData.a1, true, "blah", Boolean.TRUE, null, false);
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertTrue(open);

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // now let's restrict it by date on the submission level
        Calendar cal = Calendar.getInstance();
        cal.set(2005, 10, 01);
        Date resubmitCloseTime = cal.getTime();
        submissionLogic.updateStudentResubmissionOptions(studentUids, testData.a1, 4, resubmitCloseTime);
        // should be closed even though num submissions not reached
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertFalse(open);
        // should still be open for student1 b/c we haven't changed their submission
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertTrue(open);

        // let's allow resubmission on the assignment level - should not affect it
        // since submission level trumps
        assign1.setNumSubmissionsAllowed(4);
        assignmentLogic.saveAssignment(assign1);

        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertFalse(open);
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertTrue(open);

        // let's make this assignment not open yet
        cal.set(2020, 10, 01);
        Date assignOpenTime = cal.getTime();
        assign1.setOpenDate(assignOpenTime);
        assignmentLogic.saveAssignment(assign1);
        // should be closed for both
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertFalse(open);
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertFalse(open);

        // open it back up
        // let's restrict it by date on the assign level
        cal.set(2000, 10, 01);
        assignOpenTime = cal.getTime();
        assign1.setOpenDate(assignOpenTime);
        assign1.setAcceptUntilDate(resubmitCloseTime);
        assignmentLogic.saveAssignment(assign1);


        // let's open up on the submission level for student 2 and set the accept until time (otherwise, 
        //it won't allow the student to re-submit because
        cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 1);
        submissionLogic.updateStudentResubmissionOptions(studentUids, testData.a1, 4, cal.getTime());

        // student 1 should not be able to submit b/c of assignment-level restriction
        // but student 2 can
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT2_UID, testData.a1Id);
        assertTrue(open);
        open = submissionLogic.isSubmissionOpenForStudentForAssignment(
                AssignmentTestDataLoad.STUDENT1_UID, testData.a1Id);
        assertFalse(open);
    }

    public void testIsMostRecentVersionDraft() {
        // try a null submission
        try {
            submissionLogic.isMostRecentVersionDraft(null);
            fail("Did not catch null submission passed to isMostRecentVersionDraft");
        } catch (IllegalArgumentException iae) {}

        // try one with a submitted curr version
        boolean draft = submissionLogic.isMostRecentVersionDraft(testData.st1a1Submission);
        assertFalse(draft);
        // try one with a draft curr version
        draft = submissionLogic.isMostRecentVersionDraft(testData.st1a3Submission);
        assertTrue(draft);
        // try one without a current version
        draft = submissionLogic.isMostRecentVersionDraft(testData.st2a2SubmissionNoVersions);
        assertFalse(draft);
    }

    public void testReleaseOrRetractFeedback() {
        // try a null assignmentId
        try {
            submissionLogic.releaseOrRetractFeedback(null, null, true);
            fail("did not catch null assignmentId passed to releaseAllFeedbackForAssignment");
        } catch (IllegalArgumentException iae) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        // try an assignmentId that doesn't exist
        try {
            submissionLogic.releaseOrRetractFeedback(12345L, null, true);
            fail("did not catch non-existent assignId passed to releaseAllFeedbackForAssignment");
        } catch (AssignmentNotFoundException iae) {}

        // try as a student
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            submissionLogic.releaseOrRetractFeedback(testData.a1Id, null, true);
            fail("Did not catch a student releasing feedback!!");
        } catch (SecurityException se) {}

        // try as a TA
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        submissionLogic.releaseOrRetractFeedback(testData.a1Id, null, true);
        // should only have updated the one student this TA is allowed to grade!
        List<AssignmentSubmissionVersion> st1a1History = dao.getVersionHistoryForSubmission(testData.st1a1Submission);
        for (AssignmentSubmissionVersion asv : st1a1History) {
            assertNotNull(asv.getFeedbackReleasedDate());
        }
        // nothing should be released for student 2
        List<AssignmentSubmissionVersion> st2a1History = dao.getVersionHistoryForSubmission(testData.st2a1Submission);
        for (AssignmentSubmissionVersion asv : st2a1History) {
            assertNull(asv.getFeedbackReleasedDate());
        }

        // instructor should update all
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        submissionLogic.releaseOrRetractFeedback(testData.a3Id, null, true);

        // every version should be released for all students
        List<AssignmentSubmissionVersion> st1a3History = dao.getVersionHistoryForSubmission(testData.st1a3Submission);
        for (AssignmentSubmissionVersion asv : st1a3History) {
            assertNotNull(asv.getFeedbackReleasedDate());
        }
        List<AssignmentSubmissionVersion> st2a3History = dao.getVersionHistoryForSubmission(testData.st2a3Submission);
        for (AssignmentSubmissionVersion asv : st2a3History) {
            assertNotNull(asv.getFeedbackReleasedDate());
        }
        List<AssignmentSubmissionVersion> st3a3History = dao.getVersionHistoryForSubmission(testData.st3a3Submission);
        for (AssignmentSubmissionVersion asv : st3a3History) {
            assertNotNull(asv.getFeedbackReleasedDate());
        }
        
        //  now let's try passing specific students
        List<String> students = new ArrayList<String>();
        students.add(AssignmentTestDataLoad.STUDENT1_UID);
        students.add(AssignmentTestDataLoad.STUDENT2_UID);
        
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        submissionLogic.releaseOrRetractFeedback(testData.a3Id, students, false);
        // only st3 should be released
        st1a3History = dao.getVersionHistoryForSubmission(testData.st1a3Submission);
        for (AssignmentSubmissionVersion asv : st1a3History) {
            assertNull(asv.getFeedbackReleasedDate());
        }
        st2a3History = dao.getVersionHistoryForSubmission(testData.st2a3Submission);
        for (AssignmentSubmissionVersion asv : st2a3History) {
            assertNull(asv.getFeedbackReleasedDate());
        }
        st3a3History = dao.getVersionHistoryForSubmission(testData.st3a3Submission);
        for (AssignmentSubmissionVersion asv : st3a3History) {
            assertNotNull(asv.getFeedbackReleasedDate());
        }

        // try as a TA
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        try {
            submissionLogic.releaseOrRetractFeedback(testData.a1Id, students, true);
            fail("Did not catch TA releasing fb for student 2 for a1");
        } catch (SecurityException se) {}

        // remove student 2 from the list
        students = new ArrayList<String>();
        students.add(AssignmentTestDataLoad.STUDENT1_UID);

        submissionLogic.releaseOrRetractFeedback(testData.a1Id, students, true);
        // should only have updated the one student
        st1a1History = dao.getVersionHistoryForSubmission(testData.st1a1Submission);
        for (AssignmentSubmissionVersion asv : st1a1History) {
            assertNotNull(asv.getFeedbackReleasedDate());
        }
        // nothing should be released for student 2
        st2a1History = dao.getVersionHistoryForSubmission(testData.st2a1Submission);
        for (AssignmentSubmissionVersion asv : st2a1History) {
            assertNull(asv.getFeedbackReleasedDate());
        }
        
        // double check that retracting works (st2 was already retracted)
        submissionLogic.releaseOrRetractFeedback(testData.a1Id, students, false);
        // should only have updated the one student this TA is allowed to grade!
        st1a1History = dao.getVersionHistoryForSubmission(testData.st1a1Submission);
        for (AssignmentSubmissionVersion asv : st1a1History) {
            assertNull(asv.getFeedbackReleasedDate());
        }
        // nothing should be released for student 2
        st2a1History = dao.getVersionHistoryForSubmission(testData.st2a1Submission);
        for (AssignmentSubmissionVersion asv : st2a1History) {
            assertNull(asv.getFeedbackReleasedDate());
        }
        
    }

    public void testReleaseAllFeedbackForSubmission() {
        // try null submissionId
        try {
            submissionLogic.releaseOrRetractAllFeedbackForSubmission(null, true);
            fail("Did not catch null submissionId passed to releaseAllFeedbackForSubmission");
        } catch (IllegalArgumentException iae) {}

        // try a submissionId that doesn't exist
        try {
            submissionLogic.releaseOrRetractAllFeedbackForSubmission(12345L, true);
            fail("Did not catch nonexistent submission passed to releaseAllFeedbackForSubmission");
        } catch (SubmissionNotFoundException iae) {}

        // try as a student - should be security exception
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            submissionLogic.releaseOrRetractAllFeedbackForSubmission(testData.st1a1Submission.getId(), true);
            fail("Did not catch student trying to release feedback for a submission!!");
        } catch (SecurityException se) {}

        // now try as a TA
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // should get SecurityException for student he/she can't grade
        try {
            submissionLogic.releaseOrRetractAllFeedbackForSubmission(testData.st2a1Submission.getId(), true);
            fail("Did not catch TA trying to release feedback for a student he/she is not allowed to grade!!!");
        } catch (SecurityException se) {}

        // let's try one the ta is authorized to grade
        submissionLogic.releaseOrRetractAllFeedbackForSubmission(testData.st1a1Submission.getId(), true);
        List<AssignmentSubmissionVersion> versionHistory = dao.getVersionHistoryForSubmission(testData.st1a1Submission);
        for (AssignmentSubmissionVersion asv : versionHistory) {
            assertNotNull(asv.getFeedbackReleasedDate());
        }

        // make sure instructor can release, as well
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        submissionLogic.releaseOrRetractAllFeedbackForSubmission(testData.st2a1Submission.getId(), true);
        versionHistory = dao.getVersionHistoryForSubmission(testData.st2a1Submission);
        for (AssignmentSubmissionVersion asv : versionHistory) {
            assertNotNull(asv.getFeedbackReleasedDate());
        }
    }

    public void testReleaseFeedbackForVersion() {
        // try a null versionId
        try {
            submissionLogic.releaseOrRetractFeedbackForVersion(null, true);
            fail("did not catch null versionId passed to releaseFeedbackForVersion");
        } catch (IllegalArgumentException iae) {}

        // try a versionId that doesn't exist
        try {
            submissionLogic.releaseOrRetractFeedbackForVersion(12345L, true);
            fail("did not catch bad versionId passed to releaseFeedbackForVersion");
        } catch (VersionNotFoundException iae) {}

        // try as a student - should be security exception
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            submissionLogic.releaseOrRetractFeedbackForVersion(testData.st1a1CurrVersion.getId(), true);
            fail("Did not catch student trying to release feedback for a version!!");
        } catch (SecurityException se) {}

        // now try as a TA
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // should get SecurityException for student he/she can't grade
        try {
            submissionLogic.releaseOrRetractFeedbackForVersion(testData.st2a1CurrVersion.getId(), true);
            fail("Did not catch TA trying to release feedback for a student he/she is not allowed to grade!!!");
        } catch (SecurityException se) {}

        // let's try one the ta is authorized to grade
        submissionLogic.releaseOrRetractFeedbackForVersion(testData.st1a1CurrVersion.getId(), true);
        List<AssignmentSubmissionVersion> versionHistory = dao.getVersionHistoryForSubmission(testData.st1a1Submission);
        for (AssignmentSubmissionVersion asv : versionHistory) {
            if (asv.getId().equals(testData.st1a1CurrVersion.getId())) {
                assertNotNull(asv.getFeedbackReleasedDate());
            } else {
                // make sure no other versions were released
                assertNull(asv.getFeedbackReleasedDate());
            }
        }
        // now let's retract it
        submissionLogic.releaseOrRetractFeedbackForVersion(testData.st1a1CurrVersion.getId(), false);
        versionHistory = dao.getVersionHistoryForSubmission(testData.st1a1Submission);
        for (AssignmentSubmissionVersion asv : versionHistory) {
            // make sure no versions are released
            assertNull(asv.getFeedbackReleasedDate());
        }

        // make sure instructor can release, as well
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        submissionLogic.releaseOrRetractFeedbackForVersion(testData.st2a1CurrVersion.getId(), true);
        versionHistory = dao.getVersionHistoryForSubmission(testData.st2a1Submission);
        for (AssignmentSubmissionVersion asv : versionHistory) {
            if (asv.getId().equals(testData.st2a1CurrVersion.getId())) {
                assertNotNull(asv.getFeedbackReleasedDate());
            } else {
                //make sure no other versions were released
                assertNull(asv.getFeedbackReleasedDate());
            }
        }
        // now let's retract
        submissionLogic.releaseOrRetractFeedbackForVersion(testData.st2a1CurrVersion.getId(), false);
        versionHistory = dao.getVersionHistoryForSubmission(testData.st2a1Submission);
        for (AssignmentSubmissionVersion asv : versionHistory) {
            //make sure no other versions were released
            assertNull(asv.getFeedbackReleasedDate());
        }
    }

    public void testGetVersionHistoryForSubmission() {
        // try a null submission
        try {
            submissionLogic.getVersionHistoryForSubmission(null);
            fail("did not catch null submission passed to getVersionHistoryForSubmission");
        } catch (IllegalArgumentException iae) {}

        // student should only be able to retrieve their own history
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        try {
            submissionLogic.getVersionHistoryForSubmission(testData.st1a1Submission);
            fail("Did not catch a student retrieving versionHistory for another student!");
        } catch (SecurityException se) {}

        List<AssignmentSubmissionVersion> history = submissionLogic.getVersionHistoryForSubmission(testData.st2a1Submission);
        assertEquals(history.size(), 3);
        // check that feedback was restricted b/c none released
        for (AssignmentSubmissionVersion asv : history) {
            assertTrue(asv.getFeedbackNotes().equals(""));
            assertTrue(asv.getAnnotatedText().equals(""));
            assertTrue(asv.getFeedbackAttachSet().isEmpty());
        }

        // switch to ta
        // shouldn't be able to view student2 history
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        try {
            submissionLogic.getVersionHistoryForSubmission(testData.st2a1Submission);
            fail("Did not catch a ta retrieving versionHistory for student not authorized to view");
        } catch (SecurityException se) {}



        history = submissionLogic.getVersionHistoryForSubmission(testData.st1a1Submission);
        assertEquals(history.size(), 1);

        // switch to instructor
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        history = submissionLogic.getVersionHistoryForSubmission(testData.st1a3Submission);
        assertEquals(history.size(), 2);
        // check that submission info for curr version was restricted b/c draft
        for (AssignmentSubmissionVersion asv : history) {
            if (asv.getId().equals(testData.st1a3CurrVersion.getId())) {
                assertTrue(asv.getSubmittedText().equals(""));
                assertTrue(asv.getSubmissionAttachSet().isEmpty());
            }
        }
    }

    public void testGetNumSubmittedVersions() throws Exception {
        // try null params
        try {
            submissionLogic.getNumSubmittedVersions(null, testData.a1Id);
            fail("did not catch null studentId passed to getNumSubmittedVersions");
        } catch (IllegalArgumentException iae) {}

        try {
            submissionLogic.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT1_UID, null);
            fail("did not catch null assignmentId passed to getNumSubmittedVersions");
        } catch (IllegalArgumentException iae) {}

        // try an assignmentId that doesn't exist
        assertEquals(0, submissionLogic.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT3_UID, 12345L));

        // try a student with no submissions 
        assertEquals(0, submissionLogic.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT3_UID, testData.a1Id));

        // try a student with 2 submissions but one is draft
        assertEquals(1, submissionLogic.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT1_UID, testData.a3Id));

        // add instructor feedback w/o a submission
        AssignmentSubmission st3a1Submission = testData.createGenericSubmission(testData.a1, AssignmentTestDataLoad.STUDENT3_UID);
        AssignmentSubmissionVersion st3a1CurrVersion = testData.createGenericVersion(st3a1Submission, 0);
        st3a1CurrVersion.setDraft(false);
        st3a1CurrVersion.setSubmittedDate(null);
        dao.save(st3a1Submission);
        dao.save(st3a1CurrVersion);

        // should show up 0 b/c not submitted
        assertEquals(0, submissionLogic.getNumSubmittedVersions(AssignmentTestDataLoad.STUDENT3_UID, testData.a1Id));
    }

    public void testMarkFeedbackAsViewed() {
        // try a null submissionId
        try {
            submissionLogic.markFeedbackAsViewed(null, new ArrayList<Long>());
            fail("Did not catch null submissionId passed to markFeedbackAsViewed");
        } catch (IllegalArgumentException iae) {}

        // try a null list - should do nothing
        submissionLogic.markFeedbackAsViewed(testData.st1a1Submission.getId(), null);

        // st2 has 3 versions - let's mark the 1st and 3rd versions as read
        List<Long> versionsToUpdate = new ArrayList<Long>();
        versionsToUpdate.add(testData.st2a1CurrVersion.getId());
        versionsToUpdate.add(testData.st2a1Version1.getId());

        // try marking feedback for someone else
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        try {
            submissionLogic.markFeedbackAsViewed(testData.st1a1Submission.getId(), versionsToUpdate);
            fail("Did not catch attempt to mark another user's feedback as read!");
        } catch (SecurityException se) {}

        // set the user to student 2
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        submissionLogic.markFeedbackAsViewed(testData.st2a1Submission.getId(), versionsToUpdate);

        // since feedback has not been released none of these should have a value
        // in feedbackLastViewed yet
        AssignmentSubmission st2a1SubWithHistory = dao.getSubmissionWithVersionHistoryById(testData.st2a1Submission.getId());
        for (AssignmentSubmissionVersion version : st2a1SubWithHistory.getSubmissionHistorySet()) {
            assertNull(version.getFeedbackLastViewed());
        }

        // set the feedback as released on version 1 and 3
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        submissionLogic.saveInstructorFeedback(testData.st2a1Version1.getId(), AssignmentTestDataLoad.STUDENT2_UID, 
                testData.a1, "hello", "hello", new Date(), testData.st2a1Version1.getFeedbackAttachSet());

        submissionLogic.saveInstructorFeedback(testData.st2a1CurrVersion.getId(), AssignmentTestDataLoad.STUDENT2_UID, 
                testData.a1, "hello", "hello", new Date(), testData.st2a1CurrVersion.getFeedbackAttachSet());

        // try marking as read again
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        submissionLogic.markFeedbackAsViewed(testData.st2a1Submission.getId(), versionsToUpdate);
        st2a1SubWithHistory = dao.getSubmissionWithVersionHistoryById(testData.st2a1Submission.getId());
        for (AssignmentSubmissionVersion version : st2a1SubWithHistory.getSubmissionHistorySet()) {
            if (version.getId().equals(testData.st2a1Version1.getId())) {
                assertNotNull(version.getFeedbackLastViewed());
            } else if (version.getId().equals(testData.st2a1CurrVersion.getId())) {
                assertNotNull(version.getFeedbackLastViewed());
            } else if (version.getId().equals(testData.st2a1Version2.getId())) {
                assertNull(version.getFeedbackLastViewed());
            } else {
                throw new IllegalArgumentException("Invalid version returned while checking markFeedbackAsViewed!");
            }
        }
    }

    public void testMarkAssignmentsAsCompleted() {
        // try passing a null studentId
        try {
            submissionLogic.markAssignmentsAsCompleted(null, new HashMap<Long, Boolean>());
            fail("Did not catch null studentId passed to markAssignmentsAsCompleted");
        } catch (IllegalArgumentException iae) {}

        // try to mark someone else's work as completed
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        try {
            submissionLogic.markAssignmentsAsCompleted(AssignmentTestDataLoad.STUDENT1_UID, new HashMap<Long, Boolean>());
            fail("did not catch user attempting to mark another student's assignment as complete");
        } catch (SecurityException se) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        // try passing a null map - should do nothing
        submissionLogic.markAssignmentsAsCompleted(AssignmentTestDataLoad.STUDENT1_UID, null);

        // try passing an empty map - should do nothing
        submissionLogic.markAssignmentsAsCompleted(AssignmentTestDataLoad.STUDENT1_UID, new HashMap<Long, Boolean>());

        Map<Long, Boolean> assignmentIdToCompletedMap = new HashMap<Long, Boolean>();
        // let's put some null booleans in the map to make sure it fails
        assignmentIdToCompletedMap.put(testData.a1Id, true);
        assignmentIdToCompletedMap.put(testData.a2Id, null);
        try {
            submissionLogic.markAssignmentsAsCompleted(AssignmentTestDataLoad.STUDENT1_UID, assignmentIdToCompletedMap);
            fail("did not catch null completed value passed to markAssignmentsAsCompleted");
        } catch (IllegalArgumentException iae) {}

        // let's mark some assignments as completed
        assignmentIdToCompletedMap.put(testData.a2Id, true);
        submissionLogic.markAssignmentsAsCompleted(AssignmentTestDataLoad.STUDENT1_UID, assignmentIdToCompletedMap);

        List<Assignment2> assignList = new ArrayList<Assignment2>();
        assignList.add(testData.a1);
        assignList.add(testData.a2);
        assignList.add(testData.a3);
        assignList.add(testData.a4);

        // now, retrieve these submissions to double check them
        List<AssignmentSubmission> subList = dao.getCurrentAssignmentSubmissionsForStudent(assignList, AssignmentTestDataLoad.STUDENT1_UID);
        for (AssignmentSubmission sub : subList) {
            if (sub.getAssignment().equals(testData.a1)) {
                assertTrue(sub.isCompleted());
            } else if (sub.getAssignment().equals(testData.a2)) {
                assertTrue(sub.isCompleted());
            } else if (sub.getAssignment().equals(testData.a3)) {
                assertFalse(sub.isCompleted());
            } else if (sub.getAssignment().equals(testData.a4)) {
                assertFalse(sub.isCompleted());
            }
        }

        // let's try marking one as complete even though no sub exists
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT3_UID);
        assignmentIdToCompletedMap.put(testData.a2Id, true);
        assignmentIdToCompletedMap.put(testData.a1Id, false); 
        submissionLogic.markAssignmentsAsCompleted(AssignmentTestDataLoad.STUDENT3_UID, assignmentIdToCompletedMap);
        subList = dao.getCurrentAssignmentSubmissionsForStudent(assignList, AssignmentTestDataLoad.STUDENT3_UID);
        for (AssignmentSubmission sub : subList) {
            if (sub.getAssignment().equals(testData.a1)) {
                assertFalse(sub.isCompleted());
            } else if (sub.getAssignment().equals(testData.a2)) {
                assertTrue(sub.isCompleted());
            } else if (sub.getAssignment().equals(testData.a3)) {
                assertFalse(sub.isCompleted());
            } else if (sub.getAssignment().equals(testData.a4)) {
                assertFalse(sub.isCompleted());
            }
        }
    }

    public void testUpdateStudentResubmissionOptions() {
        try {
            submissionLogic.updateStudentResubmissionOptions(new ArrayList<String>(), null, null, null);
            fail("Did not catch null assignment passed to updateStudentResubmissionOptions");
        } catch (IllegalArgumentException iae) {}

        // make sure student can't do anything
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        try {
            submissionLogic.updateStudentResubmissionOptions(new ArrayList<String>(), testData.a1, null, null);
        } catch (SecurityException se) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);

        List<String> studentUids = new ArrayList<String>();
        studentUids.add(AssignmentTestDataLoad.STUDENT1_UID);
        studentUids.add(AssignmentTestDataLoad.STUDENT2_UID);
        Integer numSubmissionsAllowed = 5;
        Date resubmitClose = new Date();

        submissionLogic.updateStudentResubmissionOptions(studentUids, testData.a1, numSubmissionsAllowed, resubmitClose);

        // now let's retrieve them and double check they were updated
        List<AssignmentSubmission> submissions = submissionLogic.getViewableSubmissionsForAssignmentId(testData.a1Id, null);
        for (AssignmentSubmission sub : submissions) {
            if (sub.getUserId().equals(AssignmentTestDataLoad.STUDENT1_UID)) {
                assertEquals(numSubmissionsAllowed, sub.getNumSubmissionsAllowed());
                assertEquals(resubmitClose, sub.getResubmitCloseDate());
            } else if (sub.getUserId().equals(AssignmentTestDataLoad.STUDENT2_UID)) {
                assertEquals(numSubmissionsAllowed, sub.getNumSubmissionsAllowed());
                assertEquals(resubmitClose, sub.getResubmitCloseDate());
            }
        }

        // now check and see what happens when no submission exists yet
        // there is no submission for student1, assign2
        studentUids = new ArrayList<String>();
        studentUids.add(AssignmentTestDataLoad.STUDENT1_UID);
        submissionLogic.updateStudentResubmissionOptions(studentUids, testData.a2, numSubmissionsAllowed, resubmitClose);

        AssignmentSubmission sub = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(testData.a2Id, AssignmentTestDataLoad.STUDENT1_UID, null);
        assertEquals(numSubmissionsAllowed, sub.getNumSubmissionsAllowed());
        assertEquals(resubmitClose, sub.getResubmitCloseDate());
        assertEquals(0, sub.getSubmissionHistorySet().size());
    }

    public void testGetSubmissionsForCurrentUser() {
        try {
            submissionLogic.getSubmissionsForCurrentUser(null);
            fail("Did not catch null contextId passed to getSubmissionsForCurrentUser");
        } catch (IllegalArgumentException iae) {}

        // let's try retrieving submissions for a non-student
        // should throw SecurityException
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        try {
            submissionLogic.getSubmissionsForCurrentUser(AssignmentTestDataLoad.CONTEXT_ID);
            fail("Did not catch non-student attempting to retrieve submissions via getSubmissionsForCurrentUser");
        } catch (SecurityException se) {}

        // switch to student1 - can view assign 1,2,3
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        List<AssignmentSubmission> subList = submissionLogic.getSubmissionsForCurrentUser(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(3, subList.size());
        // since none are marked as complete yet, should be ordered by sortIndex
        // this student only has submissions for assign 1 and 3, but there should
        // be a filler rec for 2
        assertEquals(testData.st1a1Submission.getId(), subList.get(0).getId());
        assertEquals(testData.a2Id, subList.get(1).getAssignment().getId()); // empty rec
        assertEquals(testData.st1a3Submission.getId(), subList.get(2).getId());

        // let's mark a1 as completed
        Map<Long, Boolean> assignIdCompletedMap = new HashMap<Long, Boolean>();
        assignIdCompletedMap.put(testData.a1Id, true);
        submissionLogic.markAssignmentsAsCompleted(AssignmentTestDataLoad.STUDENT1_UID, assignIdCompletedMap);

        // re-retrieve the submissions - should be in different order now
        subList = submissionLogic.getSubmissionsForCurrentUser(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(3, subList.size());
        // should be uncompleted first, then completed
        assertEquals(testData.a2Id, subList.get(0).getAssignment().getId()); // empty rec
        assertEquals(testData.st1a3Submission.getId(), subList.get(1).getId());
        assertEquals(testData.st1a1Submission.getId(), subList.get(2).getId());

        //let's try another student - can view all assign
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        subList = submissionLogic.getSubmissionsForCurrentUser(AssignmentTestDataLoad.CONTEXT_ID);
        // only assign 1, 3, and 4 have submissions
        assertEquals(testData.st2a1Submission.getId(), subList.get(0).getId());
        assertEquals(testData.a2Id, subList.get(1).getAssignment().getId()); // empty rec
        assertEquals(testData.st2a3Submission.getId(), subList.get(2).getId());
        assertEquals(testData.st2a4Submission.getId(), subList.get(3).getId());

        // mark assign 1 and 3 as completed
        assignIdCompletedMap = new HashMap<Long, Boolean>();
        assignIdCompletedMap.put(testData.a1Id, true);
        assignIdCompletedMap.put(testData.a3Id, true);
        submissionLogic.markAssignmentsAsCompleted(AssignmentTestDataLoad.STUDENT2_UID, assignIdCompletedMap);

        // now they should be in a diff order
        subList = submissionLogic.getSubmissionsForCurrentUser(AssignmentTestDataLoad.CONTEXT_ID);

        assertEquals(testData.a2Id, subList.get(0).getAssignment().getId()); // empty rec
        assertEquals(testData.st2a4Submission.getId(), subList.get(1).getId());
        assertEquals(testData.st2a1Submission.getId(), subList.get(2).getId());
        assertEquals(testData.st2a3Submission.getId(), subList.get(3).getId());

        // now let's test the scenario where we remove an assignment
        // should still return a submission if it exists but otherwise there
        // will be no placeholder
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        testData.a2 = assignmentLogic.getAssignmentById(testData.a2Id);
        assignmentLogic.deleteAssignment(testData.a2);

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        subList = submissionLogic.getSubmissionsForCurrentUser(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(2, subList.size());

        // even though st 2 has 4 submissions, the submissions for assign 2 does not
        // have associated versions. so this one should not be returned
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        subList = submissionLogic.getSubmissionsForCurrentUser(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(3, subList.size());

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT3_UID);
        subList = submissionLogic.getSubmissionsForCurrentUser(AssignmentTestDataLoad.CONTEXT_ID);
        assertEquals(1, subList.size());
    }

    public void testGetNumNewSubmissions() {
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        
        // try a null assignment
        try {
            submissionLogic.getNumNewSubmissions(null, new ArrayList<String>());
            fail("Did not catch null assignment passed to getNumNewSubmissions");
        } catch (IllegalArgumentException iae) {}

        // passing a null list of students shouldn't do anything
        assertEquals(0, submissionLogic.getNumNewSubmissions(testData.a1, null));

        // assign 1 should have 2
        List<String> students = new ArrayList<String>();
        students.add(AssignmentTestDataLoad.STUDENT1_UID);
        students.add(AssignmentTestDataLoad.STUDENT2_UID);
        students.add(AssignmentTestDataLoad.STUDENT3_UID);
        assertEquals(2, submissionLogic.getNumNewSubmissions(testData.a1, students));

        // now, let's release feedback for st1
        submissionLogic.releaseOrRetractFeedbackForVersion(testData.st1a1CurrVersion.getId(), true);
        // we should only get 1 now
        assertEquals(1, submissionLogic.getNumNewSubmissions(testData.a1, students));

        // now let's check a graded assignment. none have feedback but st 1 has a grade
        assertEquals(2, submissionLogic.getNumNewSubmissions(testData.a3, students));

        // student 1 has a draft version, so let's release fb on the non-draft one
        // this student already has a grade so shouldn't show up as "new"
        submissionLogic.releaseOrRetractFeedbackForVersion(testData.st1a3FirstVersion.getId(), true);
        assertEquals(2, submissionLogic.getNumNewSubmissions(testData.a3, students));

        // student 2 does not have a grade yet, so releasing fb should change
        // "new" status
        submissionLogic.releaseOrRetractFeedbackForVersion(testData.st2a3CurrVersion.getId(), true);
        assertEquals(1, submissionLogic.getNumNewSubmissions(testData.a3, students));
    }
}
