package org.sakaiproject.assignment2.model.test;

import java.util.Date;

import org.sakaiproject.assignment2.test.Assignment2TestBase;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;

/**
 * This is primarily for testing read only and other properties on the
 * AssignmentSubmissionVersion model object that are calculated from other
 * properties.
 * 
 * @author sgithens
 *
 */
public class AssignmentSubmissionVersionTest extends Assignment2TestBase {

    /**
     * @see org.springframework.test.AbstractTransactionalSpringContextTests#onSetUpInTransaction()
     */
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
    }

    public void testIsFeedBackReleased() throws Exception {
        AssignmentSubmissionVersion asv = new AssignmentSubmissionVersion();
        asv.setFeedbackReleasedDate(new Date());

        // Make sure the feedback has been released for a moment
        Thread.sleep(1000);

        assertTrue(asv.isFeedbackReleased());

        AssignmentSubmissionVersion asvNoFeedback = new AssignmentSubmissionVersion();

        assertFalse(asvNoFeedback.isFeedbackReleased());
    }

}
