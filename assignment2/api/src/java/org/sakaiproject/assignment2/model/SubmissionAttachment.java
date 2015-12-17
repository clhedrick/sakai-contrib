/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/model/SubmissionAttachment.java $
 * $Id: SubmissionAttachment.java 71207 2010-11-05 20:42:37Z swgithen@mtu.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.assignment2.model;

/**
 * The SubmissionAttachment object.  SubmissionAttachments 
 * are attachments associated with the submitter's submission.
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class SubmissionAttachment extends SubmissionAttachmentBase {
    
    
    /**
     * This is a runtime calculated property that is not persisted in the 
     * attachments DB table. In the various logic modules it can optionally be
     * set to include ContentReview information for this attachment. If this 
     * property is null it means that this submission attachment has not been
     * checked, or that the information is not available.
     */
    private Assignment2ContentReviewInfo contentReviewInfo;

    public SubmissionAttachment() {

    }

    public SubmissionAttachment(AssignmentSubmissionVersion submissionVersion, String attachmentReference) {
        this.submissionVersion = submissionVersion;
        this.attachmentReference = attachmentReference;
    }
    
    public Assignment2ContentReviewInfo getContentReviewInfo() {
        return contentReviewInfo;
    }
    
    public void setContentReviewInfo(Assignment2ContentReviewInfo contentReviewInfo) {
        this.contentReviewInfo = contentReviewInfo;
    }

}
