package org.sakaiproject.assignment2.tool.producers;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.tool.beans.AssignmentAuthoringFlowBean;
import org.sakaiproject.assignment2.tool.beans.AssignmentSubmissionCreator;
import org.sakaiproject.assignment2.tool.params.SimpleAssignmentViewParams;
import org.sakaiproject.assignment2.tool.producers.renderers.StudentViewAssignmentRenderer;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * This renders the "Preview As Student" view for Instructors editing
 * assignments. Currently it's accessed at the bottom of the Assignment Editing
 * with the "Preview As Student" button.  This uses the 
 * {@link StudentViewAssignmentRenderer} to render the actual sample view, and
 * then adds an Edit button at the bottom, which will take you back to the 
 * screen to edit the assignment.
 * 
 * @author sgithens
 *
 */
public class PreviewAsStudentProducer implements ViewComponentProducer, ViewParamsReporter {
    public static final String VIEW_ID = "preview-as-student";
    private static final Log log = LogFactory.getLog(PreviewAsStudentProducer.class);

    // Dependency
    private StudentViewAssignmentRenderer studentViewAssignmentRenderer;
    public void setStudentViewAssignmentRenderer(
            StudentViewAssignmentRenderer studentViewAssignmentRenderer) {
        this.studentViewAssignmentRenderer = studentViewAssignmentRenderer;
    }

    // Dependency
    //private EntityBeanLocator assignment2BeanLocator;
    //public void setAssignment2BeanLocator(EntityBeanLocator assignment2BeanLocator) {
    //    this.assignment2BeanLocator = assignment2BeanLocator;
    // }

    // Authoring Flow Scope Dependency
    private AssignmentAuthoringFlowBean assignmentAuthoringFlowBean;
    public void setAssignmentAuthoringFlowBean(AssignmentAuthoringFlowBean assignmentAuthoringFlowBean) {
        this.assignmentAuthoringFlowBean = assignmentAuthoringFlowBean;
    }

    // Dependency
    private AssignmentSubmissionCreator assignmentSubmissionCreator;
    public void setAssignmentSubmissionCreator(AssignmentSubmissionCreator assignmentSubmissionCreator) {
        this.assignmentSubmissionCreator = assignmentSubmissionCreator;
    }

    @SuppressWarnings("unchecked")
    public void fillComponents(UIContainer tofill, ViewParameters viewparams,
            ComponentChecker checker) {
        Assignment2 assignment = assignmentAuthoringFlowBean.getAssignment();
        //Collection assignments = assignment2BeanLocator.getDeliveredBeans().values();
        //if (assignments.size() != 1) {
        //    log.equals("Wrong number of assignments passed to PreviewAsStudent: " + assignments.size());
        // }
        //assignment = (Assignment2) assignments.toArray()[0];

        AssignmentSubmission submission = assignmentSubmissionCreator.create();

        studentViewAssignmentRenderer.makeStudentView(tofill, "preview-area:", submission, assignment, viewparams, EntityBeanLocator.NEW_PREFIX + "1", true, false, false);

        UIForm form = UIForm.make(tofill, "return-to-edit-form");
        UICommand editButton = UICommand.make(form, "edit-button", UIMessage.make("assignment2.assignment_preview.edit"), "AssignmentAuthoringBean.processActionEdit");
    }

    public String getViewID() {
        return VIEW_ID;
    }
    
    public ViewParameters getViewParameters() {
        return new SimpleAssignmentViewParams();
    }

}
