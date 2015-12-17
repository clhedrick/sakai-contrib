/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/integration-test/src/test/java/org/sakaiproject/assignment2/tool/beans/AssignmentTestDataLoad.java $
 * $Id: AssignmentTestDataLoad.java 53910 2008-10-13 20:14:51Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.tool.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.sakaiproject.assignment2.dao.AssignmentDao;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentAttachment;
import org.sakaiproject.assignment2.model.AssignmentGroup;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.SubmissionAttachment;
import org.sakaiproject.assignment2.model.FeedbackAttachment;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;

/**
 * This class holds a bunch of items used to prepopulate the database and then do testing, it also
 * handles initialization of the objects and saving (Note for developers - be careful when modifying
 * this b/c it is likely you will break all of the tests...)
 * 
 */
public class AssignmentTestDataLoad
{

	// constants
	public static final String CONTEXT_ID = "validContext";
	public static final String BAD_CONTEXT = "badContext";

	public static final String ASSIGN1_TITLE = "Assignment 1";
	public static final String ASSIGN2_TITLE = "Assignment 2";
	public static final String ASSIGN3_TITLE = "Assignment 3";
	public static final String ASSIGN4_TITLE = "Assignment 4";

	public static final String INSTRUCTOR_UID = "instructorUid";
	public static final String TA_UID = "taUid";
	public static final String STUDENT1_UID = "student1";
	public static final String STUDENT2_UID = "student2";
	public static final String STUDENT3_UID = "student3";

	public static final String GROUP1_NAME = "Group 1";
	public static final String GROUP2_NAME = "Group 2";
	public static final String GROUP3_NAME = "Group 3";

	public Assignment2 a1;
	public Assignment2 a2;
	public Assignment2 a3;
	public Assignment2 a4;

	public Long a1Id;
	public Long a2Id;
	public Long a3Id;
	public Long a4Id;

	// Assignment 1 has 2 attachments and 2 groups
	// Assignment 2 has 0 attachments and 0 group
	// Assignment 3 has 1 attachment and 0 groups
	// Assignment 4 has 0 attachments and 1 group

	// Student 1
	// A1: submission with 1 version
	// A2: no submission
	// A3: submission with 2 versions
	// A4: no submission

	// Student 2
	// A1: submission with 3 versions
	// A2: submission with no version
	// A3: submission with 1 version
	// A4: submission with 2 versions

	// Student 3
	// A1: no submission
	// A2: no submission
	// A3: 1 submission with 2 versions
	// A4: no submission

	public AssignmentSubmission st1a1Submission;
	public AssignmentSubmission st2a1Submission;

	public AssignmentSubmissionVersion st1a1CurrVersion; // no attach
	public AssignmentSubmissionVersion st2a1Version1; // 1 Sub Attach
	public AssignmentSubmissionVersion st2a1Version2; // no attach
	public AssignmentSubmissionVersion st2a1CurrVersion; // 2 FA, 2 SA

	public AssignmentSubmission st2a2SubmissionNoVersions;

	public AssignmentSubmission st1a3Submission;
	public AssignmentSubmissionVersion st1a3FirstVersion;
	public AssignmentSubmissionVersion st1a3CurrVersion; // 1 FA, 1 SA - draft!
	public AssignmentSubmission st2a3Submission;
	public AssignmentSubmissionVersion st2a3CurrVersion; // 1 SA
	public AssignmentSubmission st3a3Submission;
	public AssignmentSubmissionVersion st3a3FirstVersion; // 1 FA
	public AssignmentSubmissionVersion st3a3CurrVersion; // 1 SA

	public AssignmentSubmission st2a4Submission;
	public AssignmentSubmissionVersion st2a4FirstVersion; // 1 SA
	public AssignmentSubmissionVersion st2a4CurrVersion;

	public AssignmentTestDataLoad()
	{
		initialize();

	}

	/**
	 * initialize all the objects in this data load pea (this will make sure all the public
	 * properties are not null)
	 */
	public void initialize()
	{

	}

	/**
	 * Store all of the persistent objects in this pea
	 * 
	 * @param dao
	 *            A DAO with a save method which takes a persistent object as an argument<br/>
	 *            Example: dao.save(templateUser);
	 */
	public void createTestData(AssignmentDao dao)
	{
		a1 = createGenericAssignment2Object(ASSIGN1_TITLE, 0);
		a2 = createGenericAssignment2Object(ASSIGN2_TITLE, 1);
		a3 = createGenericAssignment2Object(ASSIGN3_TITLE, 2);
		a4 = createGenericAssignment2Object(ASSIGN4_TITLE, 3);

		a1Id = a1.getId();
		a2Id = a2.getId();
		a3Id = a3.getId();
		a4Id = a4.getId();

		Set<AssignmentAttachment> attachFora1 = new HashSet<AssignmentAttachment>();
		Set<AssignmentAttachment> attachFora2 = new HashSet<AssignmentAttachment>();
		Set<AssignmentAttachment> attachFora3 = new HashSet<AssignmentAttachment>();
		Set<AssignmentAttachment> attachFora4 = new HashSet<AssignmentAttachment>();

		Set<AssignmentGroup> groupsFora1 = new HashSet<AssignmentGroup>();
		Set<AssignmentGroup> groupsFora2 = new HashSet<AssignmentGroup>();
		Set<AssignmentGroup> groupsFora3 = new HashSet<AssignmentGroup>();
		Set<AssignmentGroup> groupsFora4 = new HashSet<AssignmentGroup>();

		// add attachments
		// to Assignment 1
		attachFora1.add(new AssignmentAttachment(a1, "ref1"));
		attachFora1.add(new AssignmentAttachment(a1, "ref2"));

		// to Assignment 3
		attachFora3.add(new AssignmentAttachment(a3, "ref1"));

		// add AssignmentGroups
		groupsFora1.add(new AssignmentGroup(a1, GROUP1_NAME));
		groupsFora1.add(new AssignmentGroup(a1, GROUP3_NAME));

		groupsFora4.add(new AssignmentGroup(a4, GROUP3_NAME));

		Set<Assignment2> assignSet = new HashSet<Assignment2>();
		assignSet.add(a1);
		assignSet.add(a2);
		assignSet.add(a3);
		assignSet.add(a4);

		dao.saveMixedSet(new Set[] { assignSet, attachFora1, groupsFora1, attachFora2, groupsFora2,
				attachFora3, groupsFora3, attachFora4, groupsFora4 });

		a1Id = a1.getId();
		a2Id = a2.getId();
		a3Id = a3.getId();
		a4Id = a4.getId();

		Set<FeedbackAttachment> feedbackAttachSet = new HashSet<FeedbackAttachment>();
		Set<SubmissionAttachment> subAttachSet = new HashSet<SubmissionAttachment>();

		// now create submissions
		// start with a1
		st1a1Submission = new AssignmentSubmission(a1, STUDENT1_UID);
		st1a1CurrVersion = createGenericVersion(st1a1Submission);
		dao.save(st1a1Submission);
		dao.save(st1a1CurrVersion);

		st2a1Submission = new AssignmentSubmission(a1, STUDENT2_UID);
		st2a1Version1 = createGenericVersion(st2a1Submission);
		dao.save(st2a1Submission);
		dao.save(st2a1Version1);
		subAttachSet.add(new SubmissionAttachment(st2a1Version1, "attachmentRef"));
		st2a1Version2 = createGenericVersion(st2a1Submission);
		dao.save(st2a1Version2);
		st2a1CurrVersion = createGenericVersion(st2a1Submission);
		dao.save(st2a1CurrVersion);
		subAttachSet.add(new SubmissionAttachment(st2a1CurrVersion, "attachmentRef"));
		subAttachSet.add(new SubmissionAttachment(st2a1CurrVersion, "attachmentRef"));
		feedbackAttachSet.add(new FeedbackAttachment(st2a1CurrVersion, "attachmentRef"));
		feedbackAttachSet.add(new FeedbackAttachment(st2a1CurrVersion, "attachmentRef"));

		// create a submission w/o any versions
		st2a2SubmissionNoVersions = new AssignmentSubmission(a2, STUDENT2_UID);
		dao.save(st2a2SubmissionNoVersions);

		// make some submission for a3 and a4

		// student 1 has 2 versions for a3
		st1a3Submission = new AssignmentSubmission(a3, STUDENT1_UID);
		dao.save(st1a3Submission);
		st1a3FirstVersion = createGenericVersion(st1a3Submission);
		dao.save(st1a3FirstVersion);
		st1a3CurrVersion = createGenericVersion(st1a3Submission);
		st1a3CurrVersion.setDraft(true);
		dao.save(st1a3CurrVersion);
		feedbackAttachSet.add(new FeedbackAttachment(st1a3CurrVersion, "blah"));
		subAttachSet.add(new SubmissionAttachment(st1a3CurrVersion, "blah"));

		// student 2 has 1 version for a3
		st2a3Submission = new AssignmentSubmission(a3, STUDENT2_UID);
		dao.save(st2a3Submission);
		st2a3CurrVersion = createGenericVersion(st2a3Submission);
		dao.save(st2a3CurrVersion);
		subAttachSet.add(new SubmissionAttachment(st2a3CurrVersion, "attRef"));
		// student 3 has 2 versions for a3
		st3a3Submission = new AssignmentSubmission(a3, STUDENT3_UID);
		dao.save(st3a3Submission);
		st3a3FirstVersion = createGenericVersion(st3a3Submission);
		dao.save(st3a3FirstVersion);
		feedbackAttachSet.add(new FeedbackAttachment(st3a3FirstVersion, "f"));
		st3a3CurrVersion = createGenericVersion(st3a3Submission);
		dao.save(st3a3CurrVersion);
		subAttachSet.add(new SubmissionAttachment(st3a3CurrVersion, "sfafd"));

		// student 2 has 2 versions for a4
		st2a4Submission = new AssignmentSubmission(a4, STUDENT2_UID);
		dao.save(st2a4Submission);
		st2a4FirstVersion = createGenericVersion(st2a4Submission);
		dao.save(st2a4FirstVersion);
		subAttachSet.add(new SubmissionAttachment(st2a4FirstVersion, "afsdf"));
		st2a4CurrVersion = createGenericVersion(st2a4Submission);
		dao.save(st2a4CurrVersion);

		dao.saveMixedSet(new Set[] { feedbackAttachSet, subAttachSet });

	}

	private Assignment2 createGenericAssignment2Object(String title, int sortIndex)
	{
		Assignment2 assignment = new Assignment2();
		assignment.setContextId(CONTEXT_ID);
		assignment.setCreateDate(new Date());
		assignment.setCreator("ADMIN");
		assignment.setDraft(false);
		assignment.setInstructions("Summarize the article we discussed on 1/8");
		assignment.setSendSubmissionNotifications(false);
		assignment.setOpenDate(new Date());
		assignment.setRemoved(false);
		assignment.setSubmissionType(AssignmentConstants.SUBMIT_INLINE_AND_ATTACH);
		assignment.setGraded(false);
		assignment.setHonorPledge(false);
		assignment.setHasAnnouncement(false);
		assignment.setAddedToSchedule(false);
		assignment.setSortIndex(sortIndex);
		assignment.setTitle(title);

		return assignment;
	}

	private AssignmentSubmissionVersion createGenericVersion(AssignmentSubmission submission)
	{
		AssignmentSubmissionVersion version = new AssignmentSubmissionVersion();
		version.setAssignmentSubmission(submission);
		version.setCreatedBy(submission.getUserId());
		version.setCreatedDate(new Date());
		version.setDraft(false);
		version.setSubmittedText("submitted text by " + submission.getUserId());
		version.setSubmittedDate(new Date());
		return version;
	}
}