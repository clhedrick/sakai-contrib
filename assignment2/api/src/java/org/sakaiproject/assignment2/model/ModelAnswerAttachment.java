package org.sakaiproject.assignment2.model;

public class ModelAnswerAttachment extends AssignmentAttachmentBase {

    public ModelAnswerAttachment() {
        
    }
    
    public ModelAnswerAttachment(Assignment2 assignment, String attachmentReference) {
        this.attachmentReference = attachmentReference;
        this.assignment = assignment;
    }
}
