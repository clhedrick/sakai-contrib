/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/model/FeedbackAttachment.java $
 * $Id: FeedbackAttachment.java 61480 2009-06-29 18:39:09Z swgithen@mtu.edu $
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
 * The FeedbackAttachment object.  FeedbackAttachments 
 * are attachments added during grading as part of the grader's feedback on
 * a submission.
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class FeedbackAttachment extends SubmissionAttachmentBase {

    public FeedbackAttachment() {

    }

    public FeedbackAttachment(AssignmentSubmissionVersion submissionVersion, String attachmentReference) {
        this.submissionVersion = submissionVersion;
        this.attachmentReference = attachmentReference;
    }
}
