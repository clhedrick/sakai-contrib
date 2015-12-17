package org.sakaiproject.assignment2.tool.producers.renderers;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradeInformation;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.user.api.User;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.producers.BasicProducer;

/**
 * This renders the Assignments Details that typically appear at the top of
 * the Student Submit/View Assignment page.
 * 
 * Usually it looks something like:
 * 
 * <h2>Professional Writing for Visual Media Submission for Earlene Arledge</h2>
 * <h3>DUE: 09 23, 2008 4:00pm</h3>
 * <h2>Assignment Details</h2>
 * <table>
 * <tr>
 *   <th>Graded?</th>	
 *   <td>Yes</td>
 * </tr>
 * <tr>
 *   <th>Points Possible</th>	
 *   <td>25</td>
 * </tr>
 * <tr>
 *   <th>Resubmissions Allowed</th>	
 *   <td>Yes</td>
 * </tr>
 * <tr>
 *   <th>Remaining Resubmissions Allowed</th>	
 *   <td>2</td>
 * </tr>
 * <tr>
 *   <th>Grade</th>	
 *   <td>22</td>
 * </tr>
 * </table>
 * 
 * @author sgithens
 *
 */
public class AsnnSubmissionDetailsRenderer implements BasicProducer {
    private static Log log = LogFactory.getLog(AsnnSubmissionDetailsRenderer.class);

    // Dependency
    private User currentUser;
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Dependency
    private Locale locale;
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    // Dependency
    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    // Dependency
    private ExternalGradebookLogic externalGradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic externalGradebookLogic) {
        this.externalGradebookLogic = externalGradebookLogic;
    }  

    // Dependency
    private String curContext;
    public void setCurContext(String curContext) {
        this.curContext = curContext;
    }

    // Dependency
    private AssignmentSubmissionLogic submissionLogic;
    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }
    
    private ExternalContentReviewLogic contentReviewLogic;
    public void setExternalContentReviewLogic(ExternalContentReviewLogic contentReviewLogic) {
        this.contentReviewLogic = contentReviewLogic;
    }
    
    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public void fillComponents(UIContainer parent, String clientID, AssignmentSubmission assignmentSubmission, boolean previewAsStudent) {
        fillComponents(parent, clientID, assignmentSubmission, previewAsStudent, false);
    }

    /**
     * Renders the assignment details at the top of the Student Submission
     * Page(s)
     * 
     * @param parent
     * @param clientID
     * @param assignmentSubmission
     * @param previewAsStudent
     * @param excludeDetails If this is true, we only render the Title/Name and
     * due date, but leave off the table with Graded, Submission Status etc.
     */
    public void fillComponents(UIContainer parent, String clientID, AssignmentSubmission assignmentSubmission, boolean previewAsStudent, boolean excludeDetails) {
        Assignment2 assignment = assignmentSubmission.getAssignment();
        String title = assignment.getTitle();

        UIJointContainer joint = new UIJointContainer(parent, clientID, "assn2-submission-details-widget:");

        DateFormat df = externalLogic.getDateFormat(null, null, locale, true);
        DateFormat df_short = externalLogic.getDateFormat(DateFormat.SHORT, DateFormat.SHORT, locale, true);

        // we may have been passed an empty assignmentSubmission object (except for assignment and userId),
        // so let's retrieve the real thing
        if (!previewAsStudent && assignmentSubmission.getId() == null) {
            AssignmentSubmission subFromDb = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(assignment.getId(), assignmentSubmission.getUserId(), null);
            if (subFromDb != null) {
                assignmentSubmission = subFromDb;
            }
        }
        
        // get the status of the current version
        int currStatus = submissionLogic.getSubmissionStatusForVersion(assignmentSubmission.getCurrentSubmissionVersion(), assignment.getDueDate(), assignmentSubmission.getResubmitCloseDate());
        boolean submissionIsOpenForStudent;
        
        // if this is a preview, we will show the instructor what it looks like when open
        if (previewAsStudent) {
            submissionIsOpenForStudent = true;
        } else {
            submissionIsOpenForStudent = submissionLogic.isSubmissionOpenForStudentForAssignment(assignmentSubmission.getUserId(), assignment.getId());
        }
        
        /***
         * Title and Due Date Information
         */
        
        UIOutput.make(joint, "heading_title", title);
        if (assignment.isRemoved()) {
            UIMessage.make(joint, "heading_status", "assignment2.student-submit.heading.submission.deleted");
        } else if (currStatus == AssignmentConstants.SUBMISSION_IN_PROGRESS) {
            UIVerbatim.make(joint, "heading_draft", messageLocator.getMessage("assignment2.student-submit.heading.in_progress", 
                    new Object[]{ df_short.format(assignmentSubmission.getCurrentSubmissionVersion().getStudentSaveDate())}));
        }

        // figure out this student's due date. it may be different if the instructor
        // extended their submission privileges
        Date studentDueDate;
        if (assignmentSubmission != null && assignmentSubmission.getResubmitCloseDate() != null) {
            studentDueDate = assignmentSubmission.getResubmitCloseDate();
        } else {
            studentDueDate = assignment.getDueDate();
        }
        

        String dueDateText = null;

        if (!previewAsStudent && assignment.isRequiresSubmission() && 
                assignment.getSubmissionType() != AssignmentConstants.SUBMIT_NON_ELECTRONIC) {
            if (!submissionIsOpenForStudent) {

                // display error message indicating that submission is closed
                // if submission is closed and:
                // 1) student never made a submission -OR-
                // 2) student had a submission in progress
                if (!assignment.isRemoved()) {
                    if (currStatus == AssignmentConstants.SUBMISSION_NOT_STARTED ||
                            currStatus == AssignmentConstants.SUBMISSION_IN_PROGRESS) {
                        UIOutput.make(joint, "submission_closed", messageLocator.getMessage("assignment2.student-submit.submission_closed"));
                    }
                }

                // if submission is closed and there is at least one submission, we replace the
                // due date text with "Submitted Jan 3, 2009 5:23 PM" info or "Last Submitted..." if there are multiple
                // submissions.
                List<AssignmentSubmissionVersion> history = submissionLogic.getVersionHistoryForSubmission(assignmentSubmission);
                
                if (history != null && history.size() > 0) {
                    AssignmentSubmissionVersion version = history.get(0);
                    if (version.getSubmittedDate() != null) {
                        if (studentDueDate != null && version.getSubmittedDate().after(studentDueDate)) {
                            // the text displayed differs slightly depending on if there are multiple
                            // submissions
                            if (history.size() == 1) {
                                dueDateText = messageLocator.getMessage("assignment2.student-submit.submitted_info.late", 
                                        new Object[]{df.format(version.getSubmittedDate())});
                            } else {
                                dueDateText = messageLocator.getMessage("assignment2.student-submit.submitted_info.late.multiple", 
                                        new Object[]{df.format(version.getSubmittedDate())});
                            }
                        } else {
                            if (history.size() == 1) {
                                dueDateText = messageLocator.getMessage("assignment2.student-submit.submitted_info", 
                                        new Object[]{df.format(version.getSubmittedDate())});
                            } else {
                                dueDateText = messageLocator.getMessage("assignment2.student-submit.submitted_info.multiple", 
                                        new Object[]{df.format(version.getSubmittedDate())});
                            }
                        }
                    }
                }
            }
        }

        // if dueDateMessage has text already, we must be replacing the due date
        // with the submission info. otherwise, display the due date
        if (dueDateText == null) {
            if (studentDueDate == null) {
                dueDateText = messageLocator.getMessage("assignment2.student-submit.no_due_date");
            } else {
                // display something special if the submission is open and is going to be late
                if (submissionIsOpenForStudent && studentDueDate.before(new Date())) {
                    dueDateText = messageLocator.getMessage("assignment2.student-submit.due_date.late", 
                            new Object[] {df.format(studentDueDate)});
                } else {
                    dueDateText = messageLocator.getMessage("assignment2.student-submit.due_date", 
                            new Object[] {df.format(studentDueDate)});
                }
            }
        }

        UIVerbatim.make(joint, "due_date", dueDateText);

        

    }

    public void fillComponents(UIContainer parent, String clientID) {
        // TODO Auto-generated method stub

    }


}
