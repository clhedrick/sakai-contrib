/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/model/constants/AssignmentConstants.java $
 * $Id: AssignmentConstants.java 86722 2014-07-11 04:59:45Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.model.constants;

/**
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class AssignmentConstants {
    
    public static final String
        /**
         * The prefix for assignment2 permissions
         */
        PERMISSION_PREFIX = "asnn2.",
        /**
         * User may view assignments
         */
        PERMISSION_VIEW_ASSIGNMENTS = PERMISSION_PREFIX + "assignment.read",
        /**
         * User may make submissions to assignments
         */
        PERMISSION_SUBMIT = PERMISSION_PREFIX + "submit",
        /**
         * User may add new assignments
         */
        PERMISSION_ADD_ASSIGNMENTS = PERMISSION_PREFIX + "assignment.new",
        /**
         * User may edit existing assignments
         */
        PERMISSION_EDIT_ASSIGNMENTS = PERMISSION_PREFIX + "assignment.edit",
        /**
         * User may delete existing assignments
         */
        PERMISSION_REMOVE_ASSIGNMENTS = PERMISSION_PREFIX + "assignment.delete",
        /**
         * User may view and provide feedback on assignment submissions
         */
        PERMISSION_MANAGE_SUBMISSIONS = PERMISSION_PREFIX + "submissions.manage",
        /**
         * User may view and act on all students and all groups in the site in 
         * accordance with the other permissions that have been assigned to this role.
         * Without this permission, users may only view or act on assignments that are released
         * to his/her group and/or act on submissions if they were submitted by a user
         * in his/her group, if applicable
         */
        PERMISSION_ALL_GROUPS = PERMISSION_PREFIX + "all.groups";

    /*
     * Sakai.properties options
     * 
     */
    
    /**
     * If true, will include a link to edit the gradebook grader permissions from
     * assignment2.  Defaults to false.  The functionality for allowing access
     * to gradebook grader permissions via a helper was not added until after 2.7 gradebook,
     * so don't include this property unless you have a customized gradebook or are
     * post-2.7.  See SAK-18299
     */
    public final static String PROP_GRADER_PERMISSIONS_HELPER = "assignment2.grader.perm.helper";
    
    /**
     * Used to indicate that the assignment may only be viewed, not graded by
     * the specified user
     */
    public final static String VIEW = "view";

    /**
     * Used to indicate that the assignment may be viewed and graded by the
     * specified user
     */
    public final static String GRADE = "grade";

    /**
     * Used to indicate that a user may submit indefinitely
     */
    public final static int UNLIMITED_SUBMISSION = -1;

    // Notification Types
    /**
     * Do not send notification emails for any submissions
     */
    public final static int NOTIFY_NONE = 0;
    /**
     * Send a notification email for each submission
     */
    public final static int NOTIFY_FOR_EACH = 1;
    /**
     * Send me one email per day summarizing notifications for submissions
     */
    public final static int NOTIFY_DAILY_SUMMARY = 2;

    // Submission Types
    /**
     * Submissions may only be inline
     */
    public final static int SUBMIT_INLINE_ONLY = 0;
    /**
     * Submissions may only be attachments
     */
    public final static int SUBMIT_ATTACH_ONLY = 1;
    /**
     * Submissions may be inline and/or attachments
     */
    public final static int SUBMIT_INLINE_AND_ATTACH = 2;
    /**
     * Submissions will be non-electronic
     */
    public final static int SUBMIT_NON_ELECTRONIC = 3;
    
    // Model Answer Types
    /**
     * Never
     */
    public final static int MODEL_NEVER = 0;
    /**
     * Immediately
     */
    public final static int MODEL_IMMEDIATELY = 1;
    /**
     * After student submits assignment
     */
    public final static int MODEL_AFTER_STUDENT_SUBMITS = 2;
    /**
     * After feedback is released to student
     */
    public final static int MODEL_AFTER_FEEDBACK_RELEASED = 3;
    /**
     * After due date has passed
     */
    public final static int MODEL_AFTER_DUE_DATE = 4;
    /**
     * After accept until date has passed
     */
    public final static int MODEL_AFTER_ACCEPT_DATE = 5;
    /**
     * Constant for checking if the user is an instructor
     */
    public final static String MODEL_ANSWER_IS_INSTRUCTOR = "isInstructor";
    /**
     * Constant for checking if the user is on a preview screen
     */
    public final static String MODEL_ANSWER_IS_PREVIEW = "isPreview";
    /**
     * Assignment submission object
     */
    public final static String ASSIGNMENT_SUBMISSION = "assignmentSubmission";

    // Assignment status
    /**
     * This assignment is in draft status
     */
    public final static int STATUS_DRAFT = 0;
    /**
     * The assignment is not draft status but the current date is prior to
     * the open date
     */
    public final static int STATUS_NOT_OPEN = 1;
    /**
     * The assignment is not draft. The current date is after the open date
     * but prior to the "accept until" date and due date.
     */
    public final static int STATUS_OPEN = 2;
    /**
     * The assignment is not draft. The current date is after the "accept
     * until" date.
     */
    public final static int STATUS_CLOSED = 3;
    /**
     * The assignment is not draft. The current date is after the open and
     * due dates but before the "accept until" date.
     */
    public final static int STATUS_DUE = 4;

    // Submission status
    /**
     * Submission has not been started
     */
    public final static int SUBMISSION_NOT_STARTED = 0;

    /**
     * Submission has been saved (draft) but not submitted
     */
    public final static int SUBMISSION_IN_PROGRESS = 1;

    /**
     * Student has made a submission
     */
    public final static int SUBMISSION_SUBMITTED = 2;

    /**
     * Student made a submission but it was submitted after the due date
     */
    public final static int SUBMISSION_LATE = 3;	

    public static final String REFERENCE_ROOT = "asnn2";
    public static final String ASSIGNMENT_TYPE = "a";
    public static final String SUBMISSION_TYPE = "s";
    
    // Optional properties that may be passed via a map to several methods
    
    /**
     * Key used to pass a taggable reference to handle the scenario where
     * a tagged assignment or submission may be exposed to users via expanded permissions
     * due to this tag
     */
    public static final String TAGGABLE_REF_KEY = "taggableRef";
    /**
     * Key used to pass additional information from the "tagger" to handle
     * extended privileges for accessing attachments
     */
    public static final String TAGGABLE_DECO_WRAPPER = "tagDecoWrapper";
    
    // VIEW IDS for Assignment2 tool (may be used for generating urls outside the application layer)
    /**
     * the view id for the view of assignment details in the assignment2 tool
     */
    public static final String TOOL_VIEW_ASSIGN = "view-assignment";
    /**
     * the view id for the view of a student's submission details in the assignment2 tool
     */
    public static final String TOOL_VIEW_SUBMISSION = "view-submission";
   
    public static final String PROP_REVIEW_INFO = "content_review_info";
    
    /**
     * The ContentReviewService name for the Turnitin service
     */
    public static final String CONTENT_REVIEW_NAME_TII = "TurnItIn";
    
    /* These properties are specific to content review implementation using Turnitin */
    
    /**
     * Set this property to true if you want to turn on the Turnitin option for assignment2
     */
    public static final String TII_ENABLED = "turnitin.enable.assignment2";
    public static final String CONTENT_REVIEW_ENABLED = "assignment2.useContentReview";
    
    /**
     * This property is specific to Turnitin implementation of ContentReview.
     * if you want to restrict how your content is submitted to a repository, 
     * include this property in sakai.properties. Possible values are {@link #VALUE_NO_REPO}, 
     * {@link #VALUE_INSTITUTION_REPO}, {@link #VALUE_STANDARD_REPO}. If this property is
     * not included, will default to all three options. Set using the list approach in sakai.properties:
     * ie, to include the "no repo" and "standard repo" options only, use: 
     * turnitin.repository.setting.count=2
     * turnitin.repository.setting.1 = 0
     * turnitin.repository.setting.2 = 1
     * 
     */
    public static final String TII_PROP_SUBMIT_TO_REPO = "turnitin.repository.setting";
    
    /**
     * The default value for the {@link #TII_PROP_SUBMIT_TO_REPO}. Possible values are:
     * {@link #TII_VALUE_INSTITUTION_REPO}, {@link #TII_VALUE_NO_REPO}, {@link #TII_VALUE_STANDARD_REPO}
     */
    public static final String TII_PROP_DEFAULT_SUBMIT_TO_REPO = "turnitin.repository.setting.value";
    
    /**
     * possible value for {@link #TII_PROP_SUBMIT_TO_REPO}.
     * use this option if you do not want submissions saved to a repository.
     */
    public static final String TII_VALUE_NO_REPO = "0";
    /**
     * possible value for {@link #TII_PROP_SUBMIT_TO_REPO}.
     * use this option if you want all submissions saved to the content review
     * standard repository. This generally means that submissions from your institution
     * can be used to check plagiarism at other institutions.
     */
    public static final String TII_VALUE_STANDARD_REPO = "1";
    /**
     * possible value for {@link #TII_PROP_SUBMIT_TO_REPO}.
     * use this option if you want submissions saved only to your institutional repository.
     */
    public static final String TII_VALUE_INSTITUTION_REPO = "2";
    
    /**
     * This property is specific to Turnitin implementation of ContentReview.
     * Optional property to set the name of your institutional repository (most likely
     * for use in the UI)
     */
    public static final String TII_PROP_INSTITUTION_REPO_NAME = "turnitin.repository.institutional.name";
    
    /**
     * Optional property in sakai.properties to add a url link from the UI that points to a page
     * containing the requirements for attachments to be accepted by turnitin
     */
    public static final String TII_PROP_FILE_REQUIREMENTS_URL = "turnitin.file.requirements.url";

    public static final String TII_RETCODE_RCODE = "rcode";
    public static final String TII_RETCODE_RMESSAGE = "rmessage";
    public static final String TII_RETCODE_OBJECT = "object";
    public static final String TII_RETCODE_SUBMIT_PAPERS_TO = "submit_papers_to";
    public static final String TII_RETCODE_REPORT_GEN_SPEED = "report_gen_speed";
    public static final String TII_RETCODE_SEARCHPAPERS = "searchpapers";
    public static final String TII_RETCODE_SEARCHINTERNET = "searchinternet";
    public static final String TII_RETCODE_SEARCHJOURNALS = "searchjournals";
    public static final String TII_RETCODE_SEARCHINSTITUTION = "searchinstitution";
    public static final String TII_RETCODE_SVIEWREPORTS = "sviewreports";

    public static final String TII_API_PARAM_REPOSITORY = "repository";
    public static final String TII_API_PARAM_GENERATE = "generate";
    public static final String TII_API_PARAM_S_PAPER_CHECK = "s_paper_check";
    public static final String TII_API_PARAM_INTERNET_CHECK = "internet_check";
    public static final String TII_API_PARAM_JOURNAL_CHECK = "journal_check";
    public static final String TII_API_PARAM_INSTITUTION_CHECK = "institution_check";
    public static final String TII_API_PARAM_S_VIEW_REPORT = "s_view_report";
    
    /**
     * sakai.property to make the grademark (paid) service option available
     */
    public static final String TII_PROP_GRADEMARK_ENABLED = "turnitin.useGrademark";
    
    //erater
    /**
     * sakai.property to make the erater (paid) service options available
     */
    public static final String TII_PROP_ERATER_SERVICE_ENABLED = "turnitin.erater.enabled";
    public static final String TII_RETCODE_ERATER= "erater";
    public static final String TII_RETCODE_ETS_HANDBOOK= "ets_handbook";
    public static final String TII_RETCODE_ETS_DICTIONARY= "ets_dictionary";
    public static final String TII_RETCODE_ETS_SPELLING= "ets_spelling";
    public static final String TII_RETCODE_ETS_STYLE= "ets_style";
    public static final String TII_RETCODE_ETS_GRAMMAR= "ets_grammar";
    public static final String TII_RETCODE_ETS_MECHANICS= "ets_mechanics";
    public static final String TII_RETCODE_ETS_USAGE= "ets_usage";

    public static final String TII_API_PARAM_ERATER = "erater";
    public static final String TII_API_PARAM_ETS_HANDBOOK = "ets_handbook";
    public static final String TII_API_PARAM_ETS_DICTIONARY = "ets_dictionary";
    public static final String TII_API_PARAM_ETS_SPELLING = "ets_spelling";
    public static final String TII_API_PARAM_ETS_STYLE = "ets_style";
    public static final String TII_API_PARAM_ETS_GRAMMAR = "ets_grammar";
    public static final String TII_API_PARAM_ETS_MECHANICS = "ets_mechanics";
    public static final String TII_API_PARAM_ETS_USAGE = "ets_usage";

    /*
     * Assignment 2 Event Codes Below
     *
     */
    
    /**
     * This event is to be triggered when an Instructor type user authors and 
     * saves a new assignment.
     */
    public static final String EVENT_ASSIGN_CREATE = "assignment2.assignment.create";
    
    /**
     * This event is to be triggered when an Instructor type user edits the 
     * information for an existing assignment.
     */
    public static final String EVENT_ASSIGN_UPDATE = "assignment2.assignment.update";
    
    /** 
     * This event is to be triggered when an Instructor deletes an assignment.
     */
    public static final String EVENT_ASSIGN_DELETE = "assignment2.assignment.delete";
    
    /**
     * This event is to be triggered when a Student user saves a draft of their submission.
     */
    public static final String EVENT_SUB_SAVEDRAFT = "assignment2.submission.savedraft";
    
    /**
     * This event is to be triggered when a Student user submits an assignment.
     */
    public static final String EVENT_SUB_SUBMIT = "assignment2.submission.submit";
    
    /**
     * This event is to be triggered when an Instructor saves feedback for a Student's submission
     */
    public static final String EVENT_SUB_SAVE_GRADE_AND_FEEDBACK = "assignment2.saveFeedback";
    
    /**
     * This event is to be triggered when an Instructor saves and releases feedback for a Student's submission
     */
    public static final String EVENT_SUB_SAVE_AND_RELEASE_GRADE_AND_FEEDBACK = "assignment2.saveReleaseFeedback";
    
    // Done
    public static final String EVENT_RELEASE_ALL_FEEDBACK = "assignment2.releaseAllFeedback";
    
    // Done
    public static final String EVENT_RETRACT_ALL_FEEDBACK = "assignment2.retractAllFeedback";
    
    // Done
    public static final String EVENT_RELEASE_ALL_GRADES = "assignment2.releaseAllGrades";
    
    // Done
    public static final String EVENT_RETRACT_ALL_GRADES = "assignment2.retractAllGrades";
    
    /**
     * This event is to be triggered when an Instructor uses the Upload feature to upload grades and/or feedback
     */
    public static final String EVENT_UPLOAD_FEEDBACK_AND_GRADES = "assignment2.uploadFeedback";
    
    public static String[] getEventCodes() {
        return new String [] {
                EVENT_ASSIGN_CREATE,
                EVENT_ASSIGN_UPDATE,
                EVENT_ASSIGN_DELETE,
                EVENT_SUB_SAVEDRAFT,
                EVENT_SUB_SUBMIT,
                EVENT_SUB_SAVE_GRADE_AND_FEEDBACK,
                EVENT_SUB_SAVE_AND_RELEASE_GRADE_AND_FEEDBACK,
                EVENT_RELEASE_ALL_FEEDBACK,
                EVENT_RETRACT_ALL_FEEDBACK,
                EVENT_RELEASE_ALL_GRADES,
                EVENT_RETRACT_ALL_GRADES,
                EVENT_UPLOAD_FEEDBACK_AND_GRADES
        };
    }
    
    public static final String CONFIG_ALLOW_GB_SYNC = "assignment2.config.allowgbsync";
    
    /**
     * sakai.property - true if you want to diplay the "Import from Assignments" option that allows
     * the instructor to import assignments data from the original sakai Assignments tool. Defaults true.
     */
    public static final String CONFIG_ALLOW_IMPORT_FROM_ASNN1 = "assignment2.config.allowImportFromAsnn1";
}