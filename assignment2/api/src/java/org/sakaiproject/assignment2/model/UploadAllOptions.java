package org.sakaiproject.assignment2.model;

public class UploadAllOptions
{
    public Long assignmentId;
    public boolean releaseFeedback;
    public boolean releaseGrades;

    public UploadAllOptions()
    {
    }

    public UploadAllOptions(Long assignmentId, boolean releaseFeedback, boolean releaseGrades)
    {
        this.assignmentId = assignmentId;
        this.releaseFeedback = releaseFeedback;
        this.releaseGrades = releaseGrades;
    }
}