/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/utils/ComparatorsUtils.java $
 * $Id: ComparatorsUtils.java 61480 2009-06-29 18:39:09Z swgithen@mtu.edu $
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

package org.sakaiproject.assignment2.logic.utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;


/**
 * Utilities for sorting Assignment2 and AssignmentSubmission objects
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class ComparatorsUtils {

    private static Log log = LogFactory.getLog(ComparatorsUtils.class);
    /**
     * static class to sort Assignment2 objects by due date
     */
    public static class Assignment2DueDateComparator implements Comparator<Assignment2>  {
        public int compare(Assignment2 assign1, Assignment2 assign2) {
            Date dueDate1 = assign1.getDueDate();
            Date dueDate2 = assign2.getDueDate();

            int value;
            if (dueDate1 != null && dueDate2 != null) {
                value = dueDate1.compareTo(dueDate2);
            } else if (dueDate1 == null && dueDate2 != null) {
                value = -1;
            } else if (dueDate1 != null && dueDate2 == null) {
                value = 1;
            } else {
                value = 0;
            }

            return value;
        }
    }

    /**
     * static class to sort Assignment2 objects by title
     */
    public static class Assignment2TitleComparator implements Comparator<Assignment2>  {
        public int compare(Assignment2 assign1, Assignment2 assign2) {
            return sortByTitle(assign1, assign2);
        }
    }

    /**
     * static class to sort Assignment2 objects by open date
     */
    public static class Assignment2OpenDateComparator implements Comparator<Assignment2> {
        public int compare(Assignment2 assign1, Assignment2 assign2) {
            int value = assign1.getOpenDate().compareTo(assign2.getOpenDate());
            return value;
        }
    }

    /**
     * static class to sort Assignment2 objects by sort index
     */
    public static class Assignment2SortIndexComparator implements Comparator<Assignment2> {
        public int compare(Assignment2 assign1, Assignment2 assign2) {
            int value;

            if (assign1.getSortIndex() > assign2.getSortIndex()) {
                value = 1;
            } else if (assign1.getSortIndex() < assign2.getSortIndex()) {
                value = -1;
            } else {
                value = sortByTitle(assign1, assign2);
            }

            return value;
        }
    }

    /**
     * static class to sort AssignmentSubmission objects by student name
     */
    public static class SubmissionNameComparator implements Comparator<AssignmentSubmission> {
        public int compare(AssignmentSubmission submission1, AssignmentSubmission submission2) {
            return sortByName(submission1, submission2);
        }
    }

    /**
     * static class to sort AssignmentSubmission objects by submission date
     */
    public static class SubmissionDateComparator implements Comparator<AssignmentSubmission>  {
        public int compare(AssignmentSubmission submission1, AssignmentSubmission submission2) {
            Date subDate1 = submission1.getCurrentSubmissionVersion() != null 
            ? submission1.getCurrentSubmissionVersion().getSubmittedDate() : null;
            Date subDate2 = submission2.getCurrentSubmissionVersion() != null 
            ? submission2.getCurrentSubmissionVersion().getSubmittedDate() : null;

            int value;
            if (subDate1 != null && subDate2 != null) {
                value = subDate1.compareTo(subDate2);
            } else if (subDate1 == null && subDate2 != null) {
                value = -1;
            } else if (subDate1 != null && subDate2 == null) {
                value = 1;
            } else {
                value = 0;
            }

            return value;
        }
    }

    /**
     * static class to sort {@link AssignmentSubmissionVersion}s by their 
     * submittedVersionNumber. This indicates the order in which a submission
     * was made by the student.  Will return the versions in order starting
     * from the most recent student-created version and ending in the feedback-only
     * version, if it exists
     * 
     */
    public static class VersionHistoryComparatorDesc implements Comparator<AssignmentSubmissionVersion> {
        public int compare(AssignmentSubmissionVersion version1, AssignmentSubmissionVersion version2) {

            int value;
            if (version1.getSubmittedVersionNumber() < version2.getSubmittedVersionNumber()) {
                value = 1;
            } else if (version1.getSubmittedVersionNumber() > version2.getSubmittedVersionNumber()) {
                value = -1;
            } else {
                value = 0;
            }

            return value;
        }
    }

    /**
     * static class to sort AssignmentSubmission objects by completed status
     * (uncompleted come first, then completed) and then by the sortIndex of
     * the associated assignment
     */
    public static class SubmissionCompletedSortOrderComparator implements Comparator<AssignmentSubmission>  {
        public int compare(AssignmentSubmission sub1, AssignmentSubmission sub2) {

            int value;
            if (sub1.isCompleted() && !sub2.isCompleted()) {
                value = 1;
            } else if (!sub1.isCompleted() && sub2.isCompleted()) {
                value = -1;
            } else {
                // sort by sortIndex on the assignment
                Assignment2 assign1 = sub1.getAssignment();
                Assignment2 assign2 = sub2.getAssignment();

                if (assign1.getSortIndex() > assign2.getSortIndex()) {
                    value = 1;
                } else if (assign1.getSortIndex() < assign2.getSortIndex()) {
                    value = -1;
                } else {
                    // sortIndex should be unique, so shouldn't get to this point
                    value = 0;
                }
            }

            return value;
        }
    }

    /**
     * static class to sort AssignmentSubmission objects by feedback release status
     */
    public static class SubmissionFeedbackReleasedComparator implements Comparator<AssignmentSubmission>  {
        public int compare(AssignmentSubmission submission1, AssignmentSubmission submission2) {
            int value;
            boolean submission1Released = false;
            boolean submission2Released = false;

            if (submission1.getCurrentSubmissionVersion() != null) {
                submission1Released = submission1.getCurrentSubmissionVersion().isFeedbackReleased();
            }
            if (submission2.getCurrentSubmissionVersion() != null) {
                submission2Released = submission2.getCurrentSubmissionVersion().isFeedbackReleased();
            }

            if (submission1Released && !submission2Released) {
                value = 1;
            } else if (!submission1Released && submission2Released) {
                value = -1;
            } else {
                value = 0;
            }

            return value;
        }
    }

    private static int sortByTitle(Assignment2 assign1, Assignment2 assign2) {
        String title1 = assign1.getTitle() != null ? assign1.getTitle() : "";
        String title2 = assign2.getTitle() != null ? assign2.getTitle() : "";
        return Collator.getInstance().compare(title1, title2);
    }

    private static int sortByName(AssignmentSubmission submission1, AssignmentSubmission submission2) {
        String sortName1 = null;
        String sortName2 = null;
        try {
            User u1 = UserDirectoryService.getUser(submission1.getUserId());
            sortName1 = u1.getSortName() != null ? u1.getSortName() : "";
        } catch (UserNotDefinedException unde) {
            log.error("user with id " + submission1.getUserId() + " not defined");
        }

        try {
            User u2 = UserDirectoryService.getUser(submission2.getUserId());
            sortName2 = u2.getSortName() != null ? u2.getSortName() : "";

        } catch (UserNotDefinedException unde) {
            log.error("user with id " + submission2.getUserId() + " not defined");
        }

        return Collator.getInstance().compare(sortName1, sortName2);
    }

}
