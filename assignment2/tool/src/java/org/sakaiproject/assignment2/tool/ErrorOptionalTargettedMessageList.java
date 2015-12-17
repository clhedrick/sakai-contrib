package org.sakaiproject.assignment2.tool;

import org.sakaiproject.assignment2.tool.producers.AssignmentProducer;
import org.sakaiproject.assignment2.tool.producers.StudentSubmitProducer;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * This is a targetted message list we will use for our workflows. It allows
 * the ability to completely turn off error conditions for a workflow. We 
 * essentially need to use this feature whenever we need to consistently
 * get the previous request beans actual values by lookup rather than just
 * with bindings. This is because the request scope value beans aren't 
 * restored until after the producer is run.
 * 
 * I guess part of the historical reason for this behavior was the idea that
 * producers could be run in parallel on multiple nodes.
 * 
 * We are looking at the request URL here, because if we wait any longer in the
 * request, I believe it's too late to change the course of things for this 
 * targettedMessageList.  
 * 
 * Actually, maybe we could just inject the Viewparameters for the request,
 * although I'm not sure if those are created at all for POST requests...
 * 
 * @author sgithens
 *
 */
public class ErrorOptionalTargettedMessageList extends TargettedMessageList {
    
    private ViewParameters viewParams;
    public void setViewParameters(ViewParameters viewParams) {
        this.viewParams = viewParams;
    }
    
    private String[] viewsToSkip = new String[] {
           AssignmentProducer.VIEW_ID, StudentSubmitProducer.VIEW_ID
    };
    
    @Override
    public boolean isError() {
        if (ArrayUtil.contains(viewsToSkip, viewParams.viewID)) { 
            return false;
        }
        else {
            return super.isError();
        }
    }
    
    public boolean isOriginalError() {
        return super.isError();
    }
}