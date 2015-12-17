/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/model/AssignmentAttachmentBase.java $
 * $Id: AssignmentAttachmentBase.java 69061 2010-07-06 11:36:52Z swgithen@mtu.edu $
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
 * The AssignmentAttachment object.  AssignmentAttachments are attachments
 * associated with the assignment.
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class AssignmentAttachmentBase extends AttachmentBase {

    protected Assignment2 assignment;

    //public AssignmentAttachmentBase() {
    //}

    //public AssignmentAttachmentBase(Assignment2 assignment, String attachmentReference) {
    //    this.attachmentReference = attachmentReference;
    //    this.assignment = assignment;
    //}

    /**
     * 
     * @return the assignment that this attachment is associated with
     */
    public Assignment2 getAssignment() {
        return assignment;
    }

    /**
     * set the assignment that this attachment is associated with
     * @param assignment
     */
    public void setAssignment(Assignment2 assignment) {
        this.assignment = assignment;
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

        if (this.assignment == null) {
            attachmentIsValid = false;
        }

        return attachmentIsValid;
    }
}
