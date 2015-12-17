package org.sakaiproject.assignment2.tool;

/**
 * Because our workflow of POST forms goes between the Instructor 
 * and Student layer (such as an Instructor previewing what an asnn
 * would look like as a Student) we are centralizing all the possible
 * action returns here.
 * 
 * @author sgithens
 *
 */
public enum WorkFlowResult {
    /*
     * Student Authoring and Submission.
     */
    STUDENT_SUBMIT_SUBMISSION,
    STUDENT_CONTINUE_EDITING_SUBMISSION,
    STUDENT_PREVIEW_SUBMISSION,
    STUDENT_SAVE_DRAFT_SUBMISSION,
    STUDENT_SUBMISSION_FAILURE,
    STUDENT_CANCEL_SUBMISSION,
    STUDENT_RESUBMIT,

    /*
     * Instructor Assignment Authoring
     */
    INSTRUCTOR_POST_ASSIGNMENT,
    INSTRUCTOR_PREVIEW_ASSIGNMENT,
    INSTRUCTOR_SAVE_DRAFT_ASSIGNMENT,
    INSTRUCTOR_CANCEL_ASSIGNMENT,
    INSTRUCTOR_ASSIGNMENT_FAILURE,
    INSTRUCTOR_CONTINUE_EDITING_ASSIGNMENT, 
    INSTRUCTOR_ASSIGNMENT_VALIDATION_FAILURE,

    /*
     * Uploading zip or grades csv file
     */
    UPLOAD_FAILURE,
    UPLOAD_SUCCESS,
    UPLOADALL_CSV_UPLOAD,
    UPLOADALL_CSV_UPLOAD_FAILURE,
    UPLOADALL_CSV_CONFIRM_AND_SAVE,
    UPLOADALL_CSV_BACK_TO_UPLOAD,

    /*
     * Reordering Student View
     */
    REORDER_STUDENT_VIEW_SAVE,
    REORDER_STUDENT_VIEW_CANCEL,
    
    /*
     * Import Assignments View
     */
    IMPORT_ASSIGNMENTS_VIEW_IMPORT,
    IMPORT_ASSIGNMENTS_VIEW_CANCEL,
    
    /*
     * Grading and Feedback View
     */
    INSTRUCTOR_FEEDBACK_SUBMIT,
    INSTRUCTOR_FEEDBACK_SUBMIT_PREV,
    INSTRUCTOR_FEEDBACK_SUBMIT_NEXT,
    INSTRUCTOR_FEEDBACK_RELEASE_PREV,
    INSTRUCTOR_FEEDBACK_RELEASE_NEXT,
    INSTRUCTOR_FEEDBACK_SUBMIT_RETURNTOLIST,
    INSTRUCTOR_FEEDBACK_RELEASE_RETURNTOLIST,
    INSTRUCTOR_FEEDBACK_PREVIEW,
    INSTRUCTOR_FEEDBACK_CANCEL,
    INSTRUCTOR_FEEDBACK_SAVE_AND_EDIT_PREFIX,
    INSTRUCTOR_FEEDBACK_FAILURE,
    
    /*
     * Common Navigation
     */
    CANCEL_TO_LIST_VIEW;
    
    /** 
     * This property is used with the Grading and Feedback View to designate
     * the next Submission Version that is to be edited after processing the
     * current one.
     */
    private Long nextSubVersion;
    public void setNextSubVersion(Long nextSubVersion) {
        this.nextSubVersion = nextSubVersion;
    }
    public Long getNextSubVersion() {
        return nextSubVersion;
    }

    
}
