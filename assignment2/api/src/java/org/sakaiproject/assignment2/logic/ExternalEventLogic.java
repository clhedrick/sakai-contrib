package org.sakaiproject.assignment2.logic;


/**
 * This is the implementation for logic which uses Sakai's event implementation
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public interface ExternalEventLogic {

    /**
     * Post a sakai event
     * @param message
     * @param referenceObject
     */
    public void postEvent(String message, String referenceObject); 
}
