package org.sakaiproject.assignment2.tool.beans;

import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.entity.EntityBeanLocator;

/**
 * This is a flow bean for the process of Students submitting an assignment (
 * That includes editing, uploading attachments, previewing, etc.  It's required 
 * to have zero request scope dependencies.
 * 
 * The BeanLocator lookup can either take an id of an existing version, or a 
 * prefixed new to create a new one.  Currently the bean locator can only create
 * one new version at a time.
 * 
 * @author sgithens
 *
 */
public class StudentSubmissionVersionFlowBean implements BeanLocator {

    // Application Scope Dependency
    private AssignmentSubmissionVersionCreator assignmentSubmissionVersionCreator;
    public void setAssignmentSubmissionVersionCreator(AssignmentSubmissionVersionCreator assignmentSubmissionVersionCreator) {
        this.assignmentSubmissionVersionCreator = assignmentSubmissionVersionCreator;
    }

    // Service Application Scope Dependency
    private AssignmentSubmissionLogic assignmentSubmissionLogic;
    public void setAssignmentSubmissionLogic(AssignmentSubmissionLogic assignmentSubmissionLogic) {
        this.assignmentSubmissionLogic = assignmentSubmissionLogic;
    }

    // Property
    private AssignmentSubmissionVersion assignmentSubmissionVersion;
    public void setAssignmentSubmissionVersion(AssignmentSubmissionVersion assignmentSubmissionVersion) {
        this.assignmentSubmissionVersion = assignmentSubmissionVersion;
    }
    public AssignmentSubmissionVersion getAssignmentSubmissionVersion() {
        return assignmentSubmissionVersion;
    }

    public Object locateBean(String name) {
        if (assignmentSubmissionVersion == null && name.startsWith(EntityBeanLocator.NEW_PREFIX)) {
            assignmentSubmissionVersion = assignmentSubmissionVersionCreator.create();
        }
        else if (assignmentSubmissionVersion == null) {
            assignmentSubmissionVersion = assignmentSubmissionLogic.getSubmissionVersionById(new Long(name));
        }
        return assignmentSubmissionVersion;
    }
}
