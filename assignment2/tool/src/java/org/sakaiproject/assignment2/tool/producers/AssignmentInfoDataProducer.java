package org.sakaiproject.assignment2.tool.producers;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.tool.params.AssignmentViewParams;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.view.DataView;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * This is a JSON Data View for getting some information about a particular 
 * assignment.  The first use case is populating the confirmation dialog for
 * deleting an assignment, because it needs to show how many submissions there
 * are.
 * 
 * @author sgithens
 *
 */
public class AssignmentInfoDataProducer implements DataView , ViewParamsReporter {
    public static final String VIEW_ID = "assignmentinfo";

    // Dependency
    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    // Dependency
    private AssignmentSubmissionLogic assignmentSubmissionLogic;
    public void setAssignmentSubmissionLogic(AssignmentSubmissionLogic assignmentSubmissionLogic) {
        this.assignmentSubmissionLogic = assignmentSubmissionLogic;
    }

    // Dependency
    private AssignmentPermissionLogic assignmentPermissionLogic;
    public void setAssignmentPermissionLogic(AssignmentPermissionLogic assignmentPermissionLogic) {
        this.assignmentPermissionLogic = assignmentPermissionLogic;
    }
    
    // Dependency
    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
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

    // Property
    private String currUserId;
    public void setCurrUserId(String currUserId) {
        this.currUserId = currUserId;
    }

    public Object getData(ViewParameters viewparams) {
        AssignmentViewParams params = (AssignmentViewParams) viewparams;
        if (params.assignmentId == null) {
            return new Object[] {};
        }

        DateFormat df = externalLogic.getDateFormat(DateFormat.SHORT, DateFormat.SHORT, locale, true);

        Assignment2 assignment = assignmentLogic.getAssignmentById(params.assignmentId);

        Map data = new HashMap();
        data.put("id", assignment.getId());
        data.put("title", assignment.getTitle());
        if (assignment.getDueDate() != null) {
            data.put("due", df.format(assignment.getDueDate()));
        }
        else {
            data.put("due", messageLocator.getMessage("assignment2.list.no_due_date"));
        }
        data.put("numsubmissions", getNumberOfSubmissions(assignment));

        return data;
    }

    /**
     * TODO FIXME This is duplicated code from {@link ListProducer}
     * 
     * @param assignment
     */
    private int getNumberOfSubmissions(Assignment2 assignment) {
        int total = 0;
        int withSubmission = 0;

        List<String> viewableStudents = assignmentPermissionLogic.getViewableStudentsForAssignment(currUserId, assignment);
        if (viewableStudents != null) {
            total = viewableStudents.size();
            if (total > 0) {
                withSubmission = assignmentSubmissionLogic.getNumStudentsWithASubmission(assignment, viewableStudents);
            }
        }

        return withSubmission;
    }

    public String getContentType() {
        return ContentTypeInfoRegistry.JSON;
    }

    public String getViewID() {
        return VIEW_ID;
    }

    public ViewParameters getViewParameters() {
        return new AssignmentViewParams();
    }

}
