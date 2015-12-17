/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/dao/AssignmentDao.java $
 * $Id: AssignmentDao.java 53248 2008-09-18 17:33:24Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.genericdao.api.GeneralGenericDao;

/**
 * Basic DAO functionality for the Assignment2 tool
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public interface AssignmentDao extends GeneralGenericDao {

    /**
     * Used to identify the next sort index for new assignments
     * @param contextId
     * @return the highest sort index for the existing assignments
     */
    public Integer getHighestSortIndexInSite(String contextId);

    /**
     * 
     * @param contextId
     * @return all of the assignments (not deleted) in the given contextId with the associated
     * AssignmentGroup and AssignmentAttachment data populated ordered by sortIndex.
     */
    public List<Assignment2> getAssignmentsWithGroupsAndAttachments(String contextId);

    /**
     * 
     * @param contextId
     * @return all of the assignments (including any deleted assignments) in the given contextId with the associated
     * AssignmentGroup and AssignmentAttachment data populated ordered by sortIndex.
     */
    public List<Assignment2> getAllAssignmentsWithGroupsAndAttachments(String contextId);

    /**
     * 
     * @param assignmentIdList
     * @return the assignments associated with the given id list. Populates
     * the attachments and groups
     */
    public Set<Assignment2> getAssignmentsWithGroupsAndAttachmentsById(Collection<Long> assignmentIdList);

    /**
     * 
     * @param assignmentId
     * @return the Assignment2 object associated with the given id with the
     * associated AssignmentGroup and AssignmentAttachment data populated
     * @throws AssignmentNotFoundException - if no assignment exists with the given assignmentId
     */
    public Assignment2 getAssignmentByIdWithGroupsAndAttachments(Long assignmentId);

    /**
     * 
     * @param assignmentId
     * @return the Assignment2 object associated with the given id with the
     * associated AssignmentGroup data populated. No attachments or submission info
     * @throws AssignmentNotFoundException - if no assignment exists with the given assignmentId
     */
    public Assignment2 getAssignmentByIdWithGroups(Long assignmentId);

    /**
     * 
     * @param submission
     * @return returns the current submission version for the given AssignmentSubmission. If a feedback-only
     * version exists, it will only be defined as the "current" version if no other versions
     * exist. Otherwise, the most recent student version (this could be draft) will be returned
     */
    public AssignmentSubmissionVersion getCurrentSubmissionVersionWithAttachments(AssignmentSubmission submission);

    /**
     * 
     * @param assignments
     * @param studentId
     * @return all AssignmentSubmission records with the currentVersion populated for
     * the given student and assignments. if there is no submission for an assignment,
     * nothing is returned. does not populate version history or attachments
     */
    public List<AssignmentSubmission> getCurrentAssignmentSubmissionsForStudent(Collection<Assignment2> assignments, String studentId);

    /**
     * 
     * @param studentIds
     * @param assignment
     * @return given a list of studentIds and an assignment, returns the associated 
     * AssignmentSubmissions for the given assignment. If
     * no submission exists for the student yet, no submission is returned
     * will populate the currentSubmissionVersion 
     */
    Set<AssignmentSubmission> getCurrentSubmissionsForStudentsForAssignment(Collection<String> studentIds, Assignment2 assignment);

    /**
     * 
     * @param studentId
     * @param assignment
     * @return the AssignmentSubmission rec for the given student and assignment with the
     * submissionHistorySet populated with all of the AssignmentSubmissionVersions associated
     * with this submission. returns null if no submission has been made. will populate the
     * currentSubmissionVersion and associated attachments
     */
    public AssignmentSubmission getSubmissionWithVersionHistoryForStudentAndAssignment(String studentId, Assignment2 assignment);

    /**
     * 
     * @param studentIdList
     * @param assignment
     * @return the AssignmentSubmission recs for the given students for the given assignment.
     * populates the submissionHistorySet with all of the AssignmentSubmissionVersions for
     * each submission. if no submission has been made, no rec will be returned. will
     * populate currentVersion. will also populate attachments for the versions
     * 
     */
    public Set<AssignmentSubmission> getSubmissionsWithVersionHistoryForStudentListAndAssignment(Collection<String> studentIdList, Assignment2 assignment);

    /**
     * 
     * @param submissionVersionId
     * @return the AssignmentSubmissionVersion with associated attachments for the given id.
     * @throws VersionNotFoundException if no version exists with the given id
     */
    public AssignmentSubmissionVersion getAssignmentSubmissionVersionByIdWithAttachments(Long submissionVersionId);

    /**
     * 
     * @param submissionId
     * @return the AssignmentSubmission with the given id with the history set populated. Version
     * attachments are NOT populated. Will populate currentVersion 
     * @throws SubmissionNotFoundException if no submission exists with the given id
     */
    public AssignmentSubmission getSubmissionWithVersionHistoryById(Long submissionId);

    /**
     * 
     * @param submission
     * @return list of AssignmentSubmissionVersions associated with the given submission.
     * will populate the feedback and submission attachments for each version.
     * ordered by submitted version number
     */
    public List<AssignmentSubmissionVersion> getVersionHistoryForSubmission(final AssignmentSubmission submission);

    /**
     * 
     * @param studentId
     * @param assignmentId
     * @return the number of submissions the given student has made for the given assignment.
     * does not count versions with draft status.
     */
    public int getNumSubmittedVersions(final String studentId, final Long assignmentId);
    
    /**
     * 
     * @param studentId
     * @param assignmentId
     * @return the number of student-created versions for this student and assignment.
     * Includes drafts and submissions.  Does not count feedback-only versions
     */
    public int getNumStudentVersions(final String studentId, final Long assignmentId);

    /**
     * 
     * @param assignment
     * @param studentIdList
     * @return the number of students from the given studentId list who have at least
     * one submission for the given assignment
     */
    public int getNumStudentsWithASubmission(final Assignment2 assignment, final Collection<String> studentIdList);

    /**
     * 
     * @param submission
     * @return returns the highest submittedVersionNumber currently
     * existing for this submission
     */
    public int getHighestSubmittedVersionNumber(final AssignmentSubmission submission);

    /**
     * 
     * @param studentId
     * @param assignmentList
     * @return the AssignmentSubmissions associated with the given assignments for the given student. If no
     * submission exists, none is returned for that assignment. Populates versionHistory,
     * attachments, and currentVersion.
     */
    public Set<AssignmentSubmission> getSubmissionsForStudentWithVersionHistoryAndAttach(final String studentId, final Collection<Assignment2> assignmentList);

    /**
     * 
     * @param studentId
     * @param contextId
     * @return a set of AssignmentSubmissions for the given student for any removed
     * assignments in the given context. This is useful if an instructor deletes
     * an assignment after the student has already made a submission and the
     * student still needs access to that submission. Populates attachments, version 
     * history, and currentVersion
     */
    public Set<AssignmentSubmission> getExistingSubmissionsForRemovedAssignments(final String studentId, final String contextId);

    /**
     * 
     * @param studentUids
     * @param assignment [Non-null]
     * @return the most recently submitted AssignmentSubmissionVersion for the given students and assignment.
     * if submission does not exist, will not return version for that student. does not
     * populate attachments
     */
    public List<AssignmentSubmissionVersion> getCurrentSubmittedVersions(Collection<String> studentUids, Assignment2 assignment);

    /**
     * 
     * @param versionHistory
     * @return given a version history, returns the version with the highest submittedVersionNumber
     */
    public AssignmentSubmissionVersion getCurrentVersionFromHistory(Collection<AssignmentSubmissionVersion> versionHistory);
    
    /**
     * 
     * @param userId
     * @param assignment
     * @return the reference for the AssignmentSubmission object represented by the
     * given userId and assignment
     */
    public String getSubmissionReference(String userId, Assignment2 assignment);
    
    /**
     * In the logic, some objects will be returned with
     * modified fields (that are not meant to be saved). For example, the feedback
     * for a student's submission version may not be released yet, so we do not want
     * to expose it to the student yet. This allows the logic
     * to evict these objects from the session before they are returned to
     * ensure they are not persistent at that point
     * @param obj
     */
    public void evictObject(Object obj);
    
    public List<Assignment2> getAssignmentsWithLinkedGradebookItemId(Long id);
}