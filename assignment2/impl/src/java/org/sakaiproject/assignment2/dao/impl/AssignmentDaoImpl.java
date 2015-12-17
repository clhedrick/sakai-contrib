/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/dao/impl/AssignmentDaoImpl.java $
 * $Id: AssignmentDaoImpl.java 85366 2013-12-22 04:28:09Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.sakaiproject.assignment2.dao.AssignmentDao;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.SubmissionNotFoundException;
import org.sakaiproject.assignment2.exception.VersionNotFoundException;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.genericdao.api.search.Search;
import org.sakaiproject.genericdao.hibernate.HibernateGeneralGenericDao;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * Implementations of any specialized DAO methods from the specialized DAO that allows the developer to extend the functionality of the
 * generic dao package
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class AssignmentDaoImpl extends HibernateGeneralGenericDao implements AssignmentDao {

    private static Log log = LogFactory.getLog(AssignmentDaoImpl.class);

    public static final int MAX_NUMBER_OF_SQL_PARAMETERS_IN_LIST = 1000;

    public void init() {
        if (log.isDebugEnabled()) log.debug("init");
    }

    public Integer getHighestSortIndexInSite(final String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("contextId cannot be null");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                String hql = "select max(assignment.sortIndex) from Assignment2 as assignment where assignment.contextId = :contextId and assignment.removed != true";

                Query query = session.createQuery(hql);
                query.setParameter("contextId", contextId);

                Integer highestIndex = (Integer)query.uniqueResult();
                if (highestIndex == null) {
                    highestIndex = 0;
                }

                return highestIndex; 
            }
        };

        return (Integer)getHibernateTemplate().execute(hc);

    }

    public Set<Assignment2> getAssignmentsWithGroupsAndAttachmentsById(final Collection<Long> assignmentIdList) {

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Set<Assignment2> assignmentSet = new HashSet<Assignment2>();

                if (assignmentIdList != null && !assignmentIdList.isEmpty()) {
                    Query query = session.getNamedQuery("findAssignmentsByIdList");	

                    List<Assignment2> assignmentList = queryWithParameterList(query, "assignmentIdList", new ArrayList<Long>(assignmentIdList));

                    if (assignmentList != null) {
                        assignmentSet = new HashSet<Assignment2>(assignmentList);
                    }
                }

                return assignmentSet;
            }
        };

        return (Set<Assignment2>)getHibernateTemplate().execute(hc);
    }

    public List<Assignment2> getAssignmentsWithGroupsAndAttachments(final String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getAssignmentsWithGroupsAndAttachments");
        }

        List<Assignment2> allActiveAssigns = new ArrayList<Assignment2>();

        List<Assignment2> allAssigns = getAllAssignmentsWithGroupsAndAttachments(contextId);
        if (allAssigns != null) {
            for (Assignment2 assign : allAssigns) {
                if (!assign.isRemoved()) {
                    allActiveAssigns.add(assign);
                }
            }
        }

        return allActiveAssigns;
    }

    public List<Assignment2> getAllAssignmentsWithGroupsAndAttachments(final String contextId) {
        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to getAllAssignmentsWithGroupsAndAttachments");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Query query = session.getNamedQuery("findAllAssignmentsWithGroupsAndAttachments");
                query.setParameter("contextId", contextId);

                List<Assignment2> assignmentList = query.list();

                // we need to remove duplicates but retain order, so put
                // in a LinkedHashSet and then back into a list
                if (assignmentList != null) {
                    Set<Assignment2> assignmentSet = new LinkedHashSet<Assignment2>(assignmentList);
                    assignmentList.clear();
                    assignmentList.addAll(assignmentSet);
                }

                return assignmentList;
            }
        };

        return (List<Assignment2>)getHibernateTemplate().execute(hc);
    }

    public Assignment2 getAssignmentByIdWithGroupsAndAttachments(final Long assignmentId) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("Null assignmentId passed to getAssignmentByIdWithGroupsAndAttachments");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Query query = session.getNamedQuery("findAssignmentByIdWithGroupsAndAttachments");
                query.setParameter("assignmentId",assignmentId);

                return (Assignment2) query.uniqueResult();
            }
        };

        Assignment2 assign = (Assignment2)getHibernateTemplate().execute(hc);
        if (assign == null) {
            throw new AssignmentNotFoundException("No assignment exists with id: " + assignmentId);
        }

        return assign;
    }

    public Assignment2 getAssignmentByIdWithGroups(final Long assignmentId) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("Null assignmentId passed to getAssignmentByIdWithGroups");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Query query = session.getNamedQuery("findAssignmentByIdWithGroups");
                query.setParameter("assignmentId",assignmentId);

                return (Assignment2) query.uniqueResult();
            }
        };

        Assignment2 assign = (Assignment2)getHibernateTemplate().execute(hc);
        if (assign == null) {
            throw new AssignmentNotFoundException("No assignment exists with id: " + assignmentId);
        }

        return assign;
    }


    // Assignment Submissions DAO

    public AssignmentSubmissionVersion getCurrentSubmissionVersionWithAttachments(final AssignmentSubmission submission) {
        if (submission == null || submission.getId() == null) {
            throw new IllegalArgumentException("null submission or submission w/o id passed to getSubmissionVersionForUserIdWithAttachments");
        }
        
        // since it is possible that a student creates a draft version and then the instructor
        // provides feedback prior to the draft being submitted, let's retrieve all of the versions.
        // it isn't safe to just grab the max(id)
        
        List<AssignmentSubmissionVersion> history = getVersionHistoryForSubmission(submission);
        AssignmentSubmissionVersion currVersion = getCurrentVersionFromHistory(history);
        
        return currVersion;
    }

    /**
     * 
     * @param submissions
     * @return a map of the submission id to version id.  will return the version id
     * of the latest student submission version (ignores feedback-only versions) for
     * each submission
     */
    private Map<Long, Long> getLatestStudentVersionIdsForSubmissions(final Collection<AssignmentSubmission> submissions) { 

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {

                Map<Long, Long> submissionIdToVersionIdMap = new HashMap<Long, Long>();

                if (submissions != null && !submissions.isEmpty()) {

                    List<AssignmentSubmission> submissionList = new ArrayList<AssignmentSubmission>(submissions);

                    Query query = session.getNamedQuery("findLatestStudentVersionIds");

                    // if submission list is > than the max length allowed in sql, we need
                    // to cycle through the list

                    List resultsList = queryWithParameterList(query, "submissionList", submissionList);

                    if (resultsList != null) {
                        for (Iterator i = resultsList.iterator(); i.hasNext();)
                        {
                            Object[] results = (Object[]) i.next();        

                            if (results != null) {
                                Long submissionId = (Long)results[0];
                                Long versionId = (Long)results[1];
                                submissionIdToVersionIdMap.put(submissionId, versionId);
                            }
                        }
                    }
                }
                return submissionIdToVersionIdMap;
            }
        };

        return (Map<Long, Long>)getHibernateTemplate().execute(hc);
    }

    /**
     * 
     * @param submissions
     * @return the ids of the "feedback-only" versions that are associated with
     * the given submissions.  "feedback-only" is identified by a submittedVersionNumber
     * of {@link AssignmentSubmissionVersion#FEEDBACK_ONLY_VERSION_NUMBER}
     */
    private List<Long> getFeedbackOnlyVersionIdsForSubmissions(final Collection<AssignmentSubmission> submissions) { 
        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                List<Long> versionIdList = new ArrayList<Long>();

                if (submissions != null && !submissions.isEmpty()) {

                    List<AssignmentSubmission> submissionList = new ArrayList<AssignmentSubmission>(submissions);

                    Query query = session.getNamedQuery("findFeedbackOnlyVersionIds");

                    // if submission list is > than the max length allowed in sql, we need
                    // to cycle through the list

                    versionIdList = queryWithParameterList(query, "submissionList", submissionList);
                }

                return versionIdList;
            }
        };

        return (List<Long>)getHibernateTemplate().execute(hc);

    }

    /**
     * 
     * @param submissions
     * @return the version ids for the "current version" for the given submissions.
     * the current version is the version with the highest submittedVersionNumber
     */
    private List<Long> getCurrentVersionIdsForSubmissions(final Collection<AssignmentSubmission> submissions) {   
        // identifying the current version efficiently is a bit tricky. we have to
        // be careful because the versions will not necessarily be created in order. it
        // is possible for the feedback-only version (with submittedVersionNumber = 0)
        // to be created *after* the student has made a submission via the upload process.
        // the goal is to identify the assignmentSubmissionVersion with the highest
        // submissionVersionNumber.
        // to limit the queries involved we are going to do this in two steps:
        // first, identify the most recent student version (ignore fb-only versions).
        // these versions should be ok retrieving by max(id) if we ignore the fb-only
        // then, for all of the submissions that didn't have a student version
        // we are going to check for a fb-only version

        List<Long> currVersionIds = new ArrayList<Long>();

        if (submissions != null) {
            // first, identify the latest student version
            Map<Long, Long> studentSubIdVersionIdMap = getLatestStudentVersionIdsForSubmissions(submissions);

            // now, identify all of the submissions that don't have a version yet
            Set<Long> subIdsWithVersion = new HashSet<Long>();
            if (studentSubIdVersionIdMap != null) {
                subIdsWithVersion = studentSubIdVersionIdMap.keySet();
                // add all of the versions we have found so far
                currVersionIds.addAll(studentSubIdVersionIdMap.values());
            }

            List<AssignmentSubmission> subWithNoVersion = new ArrayList<AssignmentSubmission>();
            for (AssignmentSubmission sub : submissions) {
                if (!subIdsWithVersion.contains(sub.getId())) {
                    subWithNoVersion.add(sub);
                }
            }

            // now see if we have fb-only versions for the submissions that are
            // left
            List<Long> fbOnlyVersions = getFeedbackOnlyVersionIdsForSubmissions(subWithNoVersion);
            if (fbOnlyVersions != null) {
                currVersionIds.addAll(fbOnlyVersions);
            }
        }

        return currVersionIds;
    }

    public List<AssignmentSubmission> getCurrentAssignmentSubmissionsForStudent(final Collection<Assignment2> assignments, final String studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("null studentId passed to getAllSubmissionRecsForStudentWithVersionData");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                List<AssignmentSubmission> submissions = new ArrayList<AssignmentSubmission>();

                if (assignments != null && !assignments.isEmpty()) {
                    // retrieve the submissions
                    Query query = session.getNamedQuery("findSubmissionsForStudentForAssignments");
                    query.setParameter("studentId",studentId);

                    submissions = queryWithParameterList(query, "assignmentList", new ArrayList<Assignment2>(assignments));

                    // now, populate the version information
                    populateCurrentVersion(submissions);
                }

                return submissions;
            }
        };
        return (List<AssignmentSubmission>)getHibernateTemplate().execute(hc);

    }

    public AssignmentSubmissionVersion getAssignmentSubmissionVersionByIdWithAttachments(final Long submissionVersionId) {
        if (submissionVersionId == null) {
            throw new IllegalArgumentException("Null submissionVersionId passed to getAssignmentSubmissionVersionByIdWithAttachments");
        }
        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Query query = session.getNamedQuery("findSubmissionVersionByIdWithAttachments");
                query.setParameter("submissionVersionId", submissionVersionId);
                return query.uniqueResult();
            }
        };

        AssignmentSubmissionVersion version = (AssignmentSubmissionVersion)getHibernateTemplate().execute(hc);
        if (version == null) {
            throw new VersionNotFoundException("No version exists with id: " + submissionVersionId);
        }

        return version;
    }

    private List<AssignmentSubmissionVersion> getAssignmentSubmissionVersionsById(final List<Long> versionIds) {

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                List<AssignmentSubmissionVersion> versions = new ArrayList<AssignmentSubmissionVersion>();

                if (versionIds != null && !versionIds.isEmpty()) {
                    Query query = session.getNamedQuery("findAssignmentSubmissionVersionsByIds");

                    versions = queryWithParameterList(query, "versionIdList", versionIds);
                }

                return versions;
            }
        };
        return (List<AssignmentSubmissionVersion>)getHibernateTemplate().execute(hc);

    }

    public Set<AssignmentSubmission> getCurrentSubmissionsForStudentsForAssignment(final Collection<String> studentIds, final Assignment2 assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("null assignment passed to getSubmissionsForStudentsForAssignment");    		
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Set<AssignmentSubmission> submissionSet = new HashSet<AssignmentSubmission>();

                if (studentIds != null && !studentIds.isEmpty()) {
                    Query query = session.getNamedQuery("findSubmissionsForStudentsForAssignment");
                    query.setParameter("assignment", assignment);

                    List<AssignmentSubmission> submissionList = queryWithParameterList(query, "studentIdList", new ArrayList<String>(studentIds));

                    if (submissionList != null) {
                        submissionSet = new HashSet<AssignmentSubmission>(submissionList);

                        // now retrieve the current version information
                        populateCurrentVersion(submissionSet);
                    }
                }

                return submissionSet;
            }
        };
        return (Set<AssignmentSubmission>)getHibernateTemplate().execute(hc);


    }

    /**
     * populates the most recent version for the given submissions.
     * @param submissions
     */
    private void populateCurrentVersion(Collection<AssignmentSubmission> submissions) {
        if (submissions != null && !submissions.isEmpty()) {
            // then, we will populate the version data

            // first, retrieve the ids of the current versions
            List<Long> versionIds = getCurrentVersionIdsForSubmissions(submissions);

            // now retrieve the associated AssignmentSubmissionVersion recs
            List<AssignmentSubmissionVersion> currentVersions = getAssignmentSubmissionVersionsById(versionIds);

            if (currentVersions != null) {
                Map<Long, AssignmentSubmissionVersion> submissionIdVersionMap = new HashMap<Long, AssignmentSubmissionVersion>();
                for (AssignmentSubmissionVersion version : currentVersions) {
                    if (version != null) {
                        submissionIdVersionMap.put(version.getAssignmentSubmission().getId(), version);
                    }
                }

                for (AssignmentSubmission submission : submissions) {
                    if (submission != null) {
                        AssignmentSubmissionVersion currVersion = 
                            (AssignmentSubmissionVersion)submissionIdVersionMap.get(submission.getId());
                        if (currVersion != null) {
                            submission.setCurrentSubmissionVersion(currVersion);
                        }
                    }
                }
            }

        }
    }

    public Set<AssignmentSubmission> getSubmissionsWithVersionHistoryForStudentListAndAssignment(final Collection<String> studentIdList, final Assignment2 assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("null assignment passed to getSubmissionsWithVersionHistoryForStudentListAndAssignment");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Set<AssignmentSubmission> submissionSet = new HashSet<AssignmentSubmission>();

                if (studentIdList != null && !studentIdList.isEmpty()) {
                    Query query = session.getNamedQuery("findSubmissionsWithHistoryForAssignmentAndStudents");	
                    query.setParameter("assignment", assignment);

                    List<AssignmentSubmission> submissionList = queryWithParameterList(query, "studentIdList", new ArrayList<String>(studentIdList));

                    if (submissionList != null) {
                        submissionSet = new HashSet<AssignmentSubmission>(submissionList);

                        // now retrieve the current version information
                        populateCurrentVersionGivenHistory(submissionSet);
                    }
                }

                return submissionSet;
            }
        };

        return (Set<AssignmentSubmission>)getHibernateTemplate().execute(hc);
    }

    public AssignmentSubmission getSubmissionWithVersionHistoryForStudentAndAssignment(final String studentId, final Assignment2 assignment) {
        if (studentId == null || assignment == null) {
            throw new IllegalArgumentException("null parameter passed to getSubmissionWithVersionHistoryForStudentAndAssignment");
        }
        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Query query = session.getNamedQuery("findStudentSubmissionWithHistoryForAssignment");
                query.setParameter("studentId", studentId);
                query.setParameter("assignment", assignment);

                AssignmentSubmission submission = (AssignmentSubmission) query.uniqueResult();

                if (submission != null) {
                    AssignmentSubmissionVersion currVersion = getCurrentVersionFromHistory(submission.getSubmissionHistorySet());
                    submission.setCurrentSubmissionVersion(currVersion);
                }

                return submission;
            }
        };

        return (AssignmentSubmission)getHibernateTemplate().execute(hc);

    }

    public AssignmentSubmission getSubmissionWithVersionHistoryById(final Long submissionId) {
        if (submissionId == null) {
            throw new IllegalArgumentException("null submissionId passed to getSubmissionWithVersionHistoryById");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Query query = session.getNamedQuery("findSubmissionByIdWithHistory");
                query.setParameter("submissionId", submissionId);

                AssignmentSubmission submission = (AssignmentSubmission) query.uniqueResult();

                if (submission != null) {
                    AssignmentSubmissionVersion currVersion = getCurrentVersionFromHistory(submission.getSubmissionHistorySet());
                    submission.setCurrentSubmissionVersion(currVersion);
                }

                return submission;
            }
        };

        AssignmentSubmission submission = (AssignmentSubmission)getHibernateTemplate().execute(hc);
        if (submission == null) {
            throw new SubmissionNotFoundException("No AssignmentSubmission exists with id: " + submissionId);
        }

        return submission;

    }

    /**
     * 
     * @param query - your query with all other parameters already defined
     * @param queryParamName - the name of the list parameter referenced in the query
     * @param fullList - the list that you are using as a parameter
     * @return the resulting list from a query that takes in a list as a parameter;
     * this will cycle through with sublists if the size of the list exceeds the
     * allowed size for an sql query
     */
    private List queryWithParameterList(Query query, String queryParamName, List fullList) {
        // sql has a limit for the size of a parameter list, so we may need to cycle
        // through with sublists
        List queryResultList = new ArrayList();

        if (fullList.size() < MAX_NUMBER_OF_SQL_PARAMETERS_IN_LIST) {
            query.setParameterList(queryParamName, fullList);
            queryResultList = query.list();

        } else {
            // if there are more than MAX_NUMBER_OF_SQL_PARAMETERS_IN_LIST, we need to do multiple queries
            int begIndex = 0;
            int endIndex = 0;

            while (begIndex < fullList.size()) {
                endIndex = begIndex + MAX_NUMBER_OF_SQL_PARAMETERS_IN_LIST;
                if (endIndex > fullList.size()) {
                    endIndex = fullList.size();
                }
                List tempSubList = new ArrayList();
                tempSubList.addAll(fullList.subList(begIndex, endIndex));

                query.setParameterList(queryParamName, tempSubList);

                queryResultList.addAll(query.list());
                begIndex = endIndex;
            }
        }

        return queryResultList;
    }

    public List<AssignmentSubmissionVersion> getVersionHistoryForSubmission(final AssignmentSubmission submission) {
        if (submission == null) {
            throw new IllegalArgumentException("null submission passed to getVersionHistoryForSubmission");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {

                Query query = session.getNamedQuery("findVersionHistoryForSubmission");	
                query.setParameter("submission", submission);

                List<AssignmentSubmissionVersion> versionList = query.list();

                if (versionList != null) {
                    // get rid of duplicates introduced by join but retain order
                    Set<AssignmentSubmissionVersion>versionSet = new LinkedHashSet<AssignmentSubmissionVersion>(versionList);
                    versionList = new ArrayList<AssignmentSubmissionVersion>(versionSet);
                }

                return versionList;
            }
        };

        return (List<AssignmentSubmissionVersion>)getHibernateTemplate().execute(hc);
    }

    public int getNumSubmittedVersions(final String studentId, final Long assignmentId) {
        if (studentId == null || assignmentId == null) {
            throw new IllegalArgumentException("Null studentId or assignmentId passed " +
            "to getNumSubmittedVersions");
        }

        HibernateCallback hc = new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException, SQLException
            {
                Query query = session.getNamedQuery("countNumSubmittedVersions");
                query.setParameter("studentId", studentId, Hibernate.STRING);
                query.setParameter("assignmentId", assignmentId, Hibernate.LONG);

                return query.uniqueResult();
            }
        };
        return ((Number) getHibernateTemplate().execute(hc)).intValue();
    }
    
    public int getNumStudentVersions(final String studentId, final Long assignmentId) {
        if (studentId == null || assignmentId == null) {
            throw new IllegalArgumentException("Null studentId or assignmentId passed " +
            "to getNumStudentVersions");
        }

        HibernateCallback hc = new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException, SQLException
            {
                Query query = session.getNamedQuery("countNumStudentVersions");
                query.setParameter("studentId", studentId, Hibernate.STRING);
                query.setParameter("assignmentId", assignmentId, Hibernate.LONG);

                return query.uniqueResult();
            }
        };
        return ((Number) getHibernateTemplate().execute(hc)).intValue();
    }

    public int getNumStudentsWithASubmission(final Assignment2 assignment, final Collection<String> studentIdList) {
        if (assignment == null) {
            throw new IllegalArgumentException("null assignment passed to getNumStudentsWithASubmission");
        }

        int numStudentsWithSubmission = 0;

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {

                List<String> studentsWithSubmission = new ArrayList<String>();
                if (studentIdList != null && !studentIdList.isEmpty()) {
                    Query query = session.getNamedQuery("findStudentsWithASubmission");	
                    query.setParameter("assignment", assignment);

                    studentsWithSubmission = queryWithParameterList(query, "studentIdList", new ArrayList<String>(studentIdList));

                }

                return studentsWithSubmission;
            }
        };

        List<String> studentsWithSubmission = (List<String>)getHibernateTemplate().execute(hc);
        if (studentsWithSubmission != null) {
            numStudentsWithSubmission = studentsWithSubmission.size();
        }

        return numStudentsWithSubmission;
    }


    public int getHighestSubmittedVersionNumber(final AssignmentSubmission submission) {
        if (submission == null) {
            throw new IllegalArgumentException("submission cannot be null in getNextSubmittedVersionNumber");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                String hql = "select max(version.submittedVersionNumber) from org.sakaiproject.assignment2.model.AssignmentSubmissionVersion as version where version.assignmentSubmission = :submission";

                Query query = session.createQuery(hql);
                query.setParameter("submission", submission);

                Integer currHighestVersionNum = (Integer)query.uniqueResult();

                if (currHighestVersionNum == null) {
                    currHighestVersionNum = 0;
                }

                return currHighestVersionNum; 
            }
        };

        return ((Integer)getHibernateTemplate().execute(hc)).intValue();
    }

    public Set<AssignmentSubmission> getSubmissionsForStudentWithVersionHistoryAndAttach(final String studentId, final Collection<Assignment2> assignmentList) {
        if (studentId == null) {
            throw new IllegalArgumentException("Null studentId passed to getSubmissionsForStudentWithVersionHistoryAndAttach");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Set<AssignmentSubmission> submissionSet = new HashSet<AssignmentSubmission>();

                if (assignmentList != null && !assignmentList.isEmpty()) {
                    Query query = session.getNamedQuery("findSubmissionsForStudentWithVersionHistoryAndAttach");	
                    query.setParameter("studentId", studentId);

                    List<AssignmentSubmission> submissionList = queryWithParameterList(query, "assignmentList", new ArrayList<Assignment2>(assignmentList));

                    if (submissionList != null && !submissionList.isEmpty()) {
                        // get rid of duplicates 
                        submissionSet = new HashSet<AssignmentSubmission>(submissionList);
                        // now retrieve the current version information
                        populateCurrentVersionGivenHistory(submissionSet);
                    }
                }

                return submissionSet;
            }
        };

        return (Set<AssignmentSubmission>)getHibernateTemplate().execute(hc);
    }

    public Set<AssignmentSubmission> getExistingSubmissionsForRemovedAssignments(final String studentId, final String contextId) {
        if (studentId == null || contextId == null) {
            throw new IllegalArgumentException("Null studentId of contextId passed to getExistingSubmissionsForRemovedAssignments. " +
                    "studentId=" + studentId + " contextId=" + contextId);
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {

                Set<AssignmentSubmission> submissionSet = new HashSet<AssignmentSubmission>();
                Query query = session.getNamedQuery("findSubmissionsForRemovedAssignments"); 
                query.setParameter("contextId", contextId);
                query.setParameter("studentId", studentId);

                List<AssignmentSubmission> submissionsList = query.list();

                if (submissionsList != null) {
                    // get rid of duplicates introduced by join
                    submissionSet = new HashSet<AssignmentSubmission>(submissionsList);
                }

                return submissionSet;
            }
        };

        return (Set<AssignmentSubmission>)getHibernateTemplate().execute(hc);

    }

    public List<AssignmentSubmissionVersion> getCurrentSubmittedVersions(Collection<String> studentUids, Assignment2 assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("Null assignment passed to getCurrentSubmittedVersions");
        }

        List<AssignmentSubmissionVersion> currentSubVersions = new ArrayList<AssignmentSubmissionVersion>();
        if (studentUids != null && !studentUids.isEmpty()) {
            // first, retrieve the ids of the most recent submitted versions
            List<Long> currVersionIds = getCurrentVersionIdsForStudents(studentUids, assignment);
            if (currVersionIds != null && !currVersionIds.isEmpty()) {
                // get the versions
                currentSubVersions = getAssignmentSubmissionVersionsById(currVersionIds);
            }
        }

        return currentSubVersions;
    }

    public void evictObject(Object obj) {
        if (obj != null) {
            getHibernateTemplate().evict(obj);
        }
    }

    private void populateCurrentVersionGivenHistory(Set<AssignmentSubmission> submissionsWithHistory) {

        if (submissionsWithHistory != null) {
            for (AssignmentSubmission sub : submissionsWithHistory) {
                Set<AssignmentSubmissionVersion> versionHistory = sub.getSubmissionHistorySet();
                AssignmentSubmissionVersion currVersion = getCurrentVersionFromHistory(versionHistory);
                sub.setCurrentSubmissionVersion(currVersion);
            }
        }
    }

    /**
     * 
     * @param versionHistory
     * @return given a version history, returns the version with the highest submittedVersionNumber
     */
    public AssignmentSubmissionVersion getCurrentVersionFromHistory(Collection<AssignmentSubmissionVersion> versionHistory) {
        AssignmentSubmissionVersion currVersion = null;
        if (versionHistory != null) {
            for (AssignmentSubmissionVersion ver : versionHistory) {
                if (currVersion == null ||
                        currVersion.getSubmittedVersionNumber() < ver.getSubmittedVersionNumber()) {
                    currVersion = ver;
                } 
            }
        }

        return currVersion;
    }

    private List<Long> getCurrentVersionIdsForStudents(final Collection<String> studentUids, final Assignment2 assignment) {   
        if (assignment == null) {
            throw new IllegalArgumentException("Null assignment passed to geetCurrentVersionIdsForStudents");
        }

        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                List<Long> versionIdList = new ArrayList<Long>();

                if (studentUids != null && !studentUids.isEmpty()) {

                    List<String> studentUidList = new ArrayList<String>(studentUids);

                    Query query = session.getNamedQuery("findCurrentSubmittedVersionIds");
                    query.setParameter("assignment", assignment);

                    // if submission list is > than the max length allowed in sql, we need
                    // to cycle through the list

                    versionIdList = queryWithParameterList(query, "studentUidList", studentUidList);
                }

                return versionIdList;
            }
        };

        return (List<Long>)getHibernateTemplate().execute(hc);

    }
    
    public String getSubmissionReference (String userId, Assignment2 assignment) {
        if (userId == null || assignment == null) {
            throw new IllegalArgumentException("Null userId (" + userId + ") or assignment (" + assignment + ") passed to getSubmissionReference");
        }
        
        String submissionReference = null;
        
        Search search = new Search(new String[] {"assignment", "userId"}, new Object[] {assignment, userId});
        AssignmentSubmission submission = this.findOneBySearch(AssignmentSubmission.class, search);
        
        if (submission != null) {
            submissionReference = submission.getReference();
        }
        
        return submissionReference;
    }

    public List<Assignment2> getAssignmentsWithLinkedGradebookItemId(final Long gradebookItemId) {
        HibernateCallback hc = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException ,SQLException {
                Query query = session.getNamedQuery("findAssignmentsByGradebookItemId");
                query.setParameter("gradebookItemId", gradebookItemId);
                
                List<Assignment2> assignmentList = query.list();
                
                return assignmentList;
            }
        };
        return (List<Assignment2>)getHibernateTemplate().execute(hc);
    }
}