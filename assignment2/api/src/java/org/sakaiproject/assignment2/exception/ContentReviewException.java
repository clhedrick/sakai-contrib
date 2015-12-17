package org.sakaiproject.assignment2.exception;

/**
 * An exception type for any error that occuring while trying to save or sync
 * Assignment2 information with a Content Review service such as Turnitin.
 * 
 * @author sgithens
 *
 */
public class ContentReviewException extends AssignmentException {

    private static final long serialVersionUID = 1L;
    
    public ContentReviewException(String message) {
        super(message);
    }
    
    public ContentReviewException(String message, Throwable t) {
        super(message, t);
    }

}
