package org.sakaiproject.assignment2.tool.beans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Remembers the submission table view parameters for different assignments that 
 * someone is viewing.
 * 
 * @author sgithens
 *
 */
public class SubmissionTableViewStateHolder {
    private Map<Long,SubmissionTableViewState> subtables;
    
    public void init() {
        subtables = new ConcurrentHashMap<Long,SubmissionTableViewState>();
    }
    
    public void setSubmissionTableViewState(SubmissionTableViewState state) {
        subtables.put(state.getAsnnId(), state);
    }
    
    public SubmissionTableViewState getSubmissionTableViewState(Long asnnId) {
        if (subtables.containsKey(asnnId)) {
            return subtables.get(asnnId);
        }
        else {
            return null;
        }
    }
}
