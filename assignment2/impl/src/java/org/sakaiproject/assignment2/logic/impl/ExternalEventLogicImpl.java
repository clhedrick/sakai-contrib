package org.sakaiproject.assignment2.logic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.event.api.Event;
import org.sakaiproject.assignment2.logic.ExternalEventLogic;

/**
 * This is the implementation for logic which uses Sakai's event implementation
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class ExternalEventLogicImpl implements ExternalEventLogic {

    private static Log log = LogFactory.getLog(ExternalEventLogicImpl.class);

    private org.sakaiproject.event.api.EventTrackingService eventTrackingService;
    public void setEventTrackingService(org.sakaiproject.event.api.EventTrackingService eventTrackingService) {
        this.eventTrackingService = eventTrackingService;
    }

    /**
     * Place any code that should run when this class is initialized by spring here
     */
    public void init() {
        if (log.isDebugEnabled()) log.debug("init");
    }

    public void postEvent(String message, String objectReference) {
        Event event = eventTrackingService.newEvent(message, objectReference, true);
        eventTrackingService.post(event);
    }

}
