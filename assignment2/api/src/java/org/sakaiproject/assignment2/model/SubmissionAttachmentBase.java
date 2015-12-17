/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/model/SubmissionAttachmentBase.java $
 * $Id: SubmissionAttachmentBase.java 61480 2009-06-29 18:39:09Z swgithen@mtu.edu $
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
 * The base attachment object for submission-related attachments
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public abstract class SubmissionAttachmentBase extends AttachmentBase {

    protected AssignmentSubmissionVersion submissionVersion;

    /**
     * 
     * @return the AssignmentSubmissionVersion rec associated with this attachment
     */
    public AssignmentSubmissionVersion getSubmissionVersion() {
        return submissionVersion;
    }

    /**
     * set the AssignmentSubmissionVersion rec associated with this attachment
     * @param submissionVersion
     */
    public void setSubmissionVersion(AssignmentSubmissionVersion submissionVersion) {
        this.submissionVersion = submissionVersion;
    }

    // CONVENIENCE METHODS

    /**
     * 
     * @return true if all of the properties required for this attachment
     * to be successfully saved have been populated
     */
    public boolean isAttachmentValid() {
        boolean attachmentIsValid = true;

        if (this.attachmentReference == null || this.attachmentReference.trim().length() == 0) {
            attachmentIsValid = false;
        }

        if (this.submissionVersion == null) {
            attachmentIsValid = false;
        }

        return attachmentIsValid;
    }

}
