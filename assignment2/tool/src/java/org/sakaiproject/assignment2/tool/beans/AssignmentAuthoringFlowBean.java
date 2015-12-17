package org.sakaiproject.assignment2.tool.beans;

import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.model.Assignment2;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.entity.EntityBeanLocator;

/**
 * This is a flow bean for the process of Authoring/Editing a new or existing
 * Assignment as an Instructor or someone else with permission.
 * 
 * As this bean is intended for flow scope, it should have zero request scope
 * dependencies.
 * 
 * The BeanLocator lookup can either take an id of an existing version, or a 
 * prefixed new to create a new one.  Currently the bean locator can only create
 * one new version at a time. ( Each Assignment you're editing should have it's
 * own flow scope. )
 * 
 * Let's always return an assignment, and make a new one so it's not null.
 * 
 * @author sgithens
 *
 */
public class AssignmentAuthoringFlowBean implements BeanLocator {

    // Application Scope Dependency
    private Assignment2Creator assignment2Creator;
    public void setAssignment2Creator(Assignment2Creator assignment2Creator) {
        this.assignment2Creator = assignment2Creator;
    }

    // Service Application Scope Dependency
    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    // Property: The Assignment being edited/authored
    private Assignment2 assignment;
    public void setAssignment(Assignment2 assignment) {
        this.assignment = assignment;
    }
    public Assignment2 getAssignment() {
        if (assignment == null) {
            assignment = assignment2Creator.create();
        }
        return assignment;
    }

    public Object locateBean(String name) {
        if (assignment == null && name.startsWith(EntityBeanLocator.NEW_PREFIX)) {
            assignment = assignment2Creator.create();
        }
        else if (assignment == null) {
            assignment = assignmentLogic.getAssignmentByIdWithAssociatedData(new Long(name)); 
        }
        return assignment;
    }

}