package org.sakaiproject.assignment2.tool.beans;

/**
 * This is a small bean to hold the information for a view table, such as 
 * page size, current page, sort index, sort order, and search string.
 * 
 * This will be used to store the view state for short lived things, so that if
 * a user leaves the page and comes back shortly it remembers their location
 * and settings.
 * 
 * Initially this will probably be stored in session state for each siteID, 
 * (ugh, I know...). But will be hopefully moved to some other gapped bean with
 * a timeout, or put in a DB table.  I think that even if we move the entire UI
 * to javascript, this might still be necessary since they still might reload 
 * the page. And client side browser storage is not ubiquitous yet at the time
 * of writing.
 * 
 * @author sgithens
 *
 */
public class SubmissionTableViewState {
    private Long asnnId;
    private String groupId;
    private String orderBy;
    private long start;
    private long size;
    
    public SubmissionTableViewState() {
        
    }
    
    public SubmissionTableViewState(Long asnnId, String groupId,
            String orderBy, long start, long size) {
        this.asnnId = asnnId;
        this.groupId = groupId;
        this.orderBy = orderBy;
        this.start = start;
        this.size = size;
    }
    
    public Long getAsnnId() {
        return asnnId;
    }
    public void setAsnnId(Long asnnId) {
        this.asnnId = asnnId;
    }
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public long getStart() {
        return start;
    }
    public void setStart(long start) {
        this.start = start;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
}
