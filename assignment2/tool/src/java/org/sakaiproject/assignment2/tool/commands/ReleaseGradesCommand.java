package org.sakaiproject.assignment2.tool.commands;

import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;

/**
 * A small action bean that can be used to release or retract grades in the
 * gradebook item associated with this assignment
 * 
 * @author wagnermr
 *
 */
public class ReleaseGradesCommand {

    // Dependency
    private ExternalGradebookLogic gradebookLogic;
    public void setGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }


    // Dependency / Property
    private String curContext;
    public void setCurContext(String curContext) {
        this.curContext = curContext;
    }
    public String getCurContext() {
        return curContext;
    }

    // Property 
    private Long gradebookItemId;
    public void setGradebookItemId(Long gradebookItemId) {
        this.gradebookItemId = gradebookItemId;
    }
    public Long getGradebookItemId() {
        return gradebookItemId;
    }

    // Property
    private boolean releaseGrades;
    public void setReleaseGrades(boolean releaseGrades) {
        this.releaseGrades = releaseGrades;
    }
    public boolean getReleaseGrades() {
        return releaseGrades;
    }

    // Property
    private boolean includeInCourseGrade;
    public void setIncludeInCourseGrade(boolean includeInCourseGrade) {
        this.includeInCourseGrade = includeInCourseGrade;
    }
    public boolean getIncludeInCourseGrade() {
        return includeInCourseGrade;
    }

    public void execute() {

        if (gradebookItemId == null) {
            throw new IllegalArgumentException("Null gradebookItemId param passed to ReleaseGradesCommand");
        }

        if (curContext == null) {
            throw new IllegalArgumentException("Null curContext param passed to ReleaseGradesCommand");
        }

        gradebookLogic.releaseOrRetractGrades(curContext, gradebookItemId, releaseGrades, includeInCourseGrade);
    }
}
