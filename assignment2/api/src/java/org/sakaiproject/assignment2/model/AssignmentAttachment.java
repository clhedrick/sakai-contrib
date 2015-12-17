package org.sakaiproject.assignment2.model;

public class AssignmentAttachment extends AssignmentAttachmentBase {
    
    public AssignmentAttachment() {
        
    }
    
    public AssignmentAttachment(Assignment2 assignment, String attachmentReference) {
        this.attachmentReference = attachmentReference;
        this.assignment = assignment;
    }
}
