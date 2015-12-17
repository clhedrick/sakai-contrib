package org.sakaiproject.assignment2.model;

import org.sakaiproject.contentreview.model.ContentReviewItem;


/**
 * This model-ish class will contain a copy of the ContentReviewItem and some 
 * extra generated information such as Report URL and translated Display 
 * messages.  The objective here is to attach this as an optional property to
 * AssignmentSubmissionVersions and their Attachments for passing around the
 * application to render items in the UI.  
 * 
 * Previously we had been passing around several (like 5) named properties in
 * the maps and it's just getting too cumbersome. This will still be a property
 * in the map, but at least now the ContentReview information will be 
 * self contained.
 * 
 * @author sgithens
 *
 */
public class Assignment2ContentReviewInfo {
    private ContentReviewItem contentReviewItem;
    
    /**
     * Actual URL of the Turnitin.com page with the view. Calculated using 
     * ContentReviewService methods.
     */
    private String reviewUrl; 
    
    /**
     * The human readable display of the ContentReviewItem.reviewScore. 
     * Typicall the score with a % sign after it.
     */
    private String scoreDisplay;

    public ContentReviewItem getContentReviewItem() {
        return contentReviewItem;
    }

    public void setContentReviewItem(ContentReviewItem contentReviewItem) {
        this.contentReviewItem = contentReviewItem;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getScoreDisplay() {
        return scoreDisplay;
    }

    public void setScoreDisplay(String scoreDisplay) {
        this.scoreDisplay = scoreDisplay;
    }    
    
}
