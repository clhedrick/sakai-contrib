package org.sakaiproject.assignment2.tool.commands;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;

/**
 * This is a simple action bean (most likely to be used by UVB and 
 * simple POST's) to mark the feed back on a particular 
 * {@link AssignmentSubmissionVersion} as read/opened.
 * 
 * @author sgithens
 *
 */
public class MarkFeedbackAsReadCommand {

    // Dependency
    private AssignmentSubmissionLogic submissionLogic;
    public void setSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }

    // Property 
    private Long asnnSubId;
    public void setAsnnSubId(Long asnnSubId) {
        this.asnnSubId = asnnSubId;
    }
    public Long getAsnnSubId() {
        return asnnSubId;
    }

    // Property
    private Long asnnSubVersionId;
    public void setAsnnSubVersionId(Long asnnSubVersionId) {
        this.asnnSubVersionId = asnnSubVersionId;
    }
    public Long getAsnnSubVersionId() {
        return asnnSubVersionId;
    }

    /**
     * The action method.
     */
    public void execute() {
        List<Long>versionIds = new ArrayList<Long>();
        versionIds.add(asnnSubVersionId);
        submissionLogic.markFeedbackAsViewed(asnnSubId, versionIds);
    }
}
