/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/service/model/AssignmentDefinition.java $
 * $Id: AssignmentDefinition.java 86722 2014-07-11 04:59:45Z wagnermr@iupui.edu $
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
package org.sakaiproject.assignment2.service.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Object to model Assignment2 data that will be used for
 * public consumption (via the service) and import/export.
 */
public class AssignmentDefinition implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private int sortIndex;
    private Date openDate;
    private Date acceptUntilDate;
    private String associatedGbItemName;
    private Double associatedGbItemPtsPossible;
    private boolean graded;
    private boolean draft;
    private Date dueDate;
    private boolean honorPledge;
    private String instructions;
    private boolean hasAnnouncement;
    private boolean addedToSchedule;
    private int numSubmissionsAllowed;
    private boolean sendSubmissionNotifications;
    private int submissionType;
    private boolean requiresSubmission;
    private boolean contentReviewEnabled;
    private boolean contentReviewStudentViewReport;

    private List<String> groupRestrictionGroupTitles;
    private List<String> attachmentReferences;
    private Map properties;


    public AssignmentDefinition() {
    }

    /**
     * 
     * @return the id of the Assignment2 object this definition represents
     */
    public Long getId() {
        return id;
    }

    /**
     * the id of the Assignment2 object this definition represents
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return the title of the assignment
     */
    public String getTitle() {
        return title;
    }

    /**
     * the title of the assignment
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return the index representing the position of this assignment in the
     * student view of the assignment listing
     */
    public int getSortIndex() {
        return sortIndex;
    }

    /**
     * the index representing the position of this assignment in the
     * student view of the assignment listing
     * @param sortIndex
     */
    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }


    public Date getOpenDate() {
        return openDate;
    }


    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }


    public Date getDueDate() {
        return dueDate;
    }


    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    
    public Date getAcceptUntilDate() {
        return acceptUntilDate;
    }


    public void setAcceptUntilDate(Date acceptUntilDate) {
        this.acceptUntilDate = acceptUntilDate;
    }


    public String getAssociatedGbItemName() {
        return associatedGbItemName;
    }


    public void setAssociatedGbItemName(String associatedGbItemName) {
        this.associatedGbItemName = associatedGbItemName;
    }


    public Double getAssociatedGbItemPtsPossible() {
        return associatedGbItemPtsPossible;
    }


    public void setAssociatedGbItemPtsPossible(Double associatedGbItemPtsPossible) {
        this.associatedGbItemPtsPossible = associatedGbItemPtsPossible;
    }


    public boolean isGraded() {
        return graded;
    }


    public void setGraded(boolean graded) {
        this.graded = graded;
    }


    public boolean isDraft() {
        return draft;
    }


    public void setDraft(boolean draft) {
        this.draft = draft;
    }


    public boolean isHonorPledge() {
        return honorPledge;
    }


    public void setHonorPledge(boolean honorPledge) {
        this.honorPledge = honorPledge;
    }


    public String getInstructions() {
        return instructions;
    }


    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }


    public boolean isHasAnnouncement() {
        return hasAnnouncement;
    }


    public void setHasAnnouncement(boolean hasAnnouncement) {
        this.hasAnnouncement = hasAnnouncement;
    }


    public boolean isAddedToSchedule()
    {
        return addedToSchedule;
    }


    public void setAddedToSchedule(boolean addedToSchedule)
    {
        this.addedToSchedule = addedToSchedule;
    }


    public int getNumSubmissionsAllowed() {
        return numSubmissionsAllowed;
    }


    public void setNumSubmissionsAllowed(int numSubmissionsAllowed) {
        this.numSubmissionsAllowed = numSubmissionsAllowed;
    }


    public boolean isSendSubmissionNotifications() {
        return sendSubmissionNotifications;
    }


    public void setSendSubmissionNotifications(boolean sendSubmissionNotifications) {
        this.sendSubmissionNotifications = sendSubmissionNotifications;
    }


    public int getSubmissionType() {
        return submissionType;
    }


    public void setSubmissionType(int submissionType) {
        this.submissionType = submissionType;
    }

    public boolean isRequiresSubmission()
    {
        return requiresSubmission;
    }


    public void setRequiresSubmission(boolean requiresSubmission)
    {
        this.requiresSubmission = requiresSubmission;
    }


    public boolean isContentReviewEnabled()
    {
        return contentReviewEnabled;
    }


    public void setContentReviewEnabled(boolean contentReviewEnabled)
    {
        this.contentReviewEnabled = contentReviewEnabled;
    }


    public List<String> getGroupRestrictionGroupTitles() {
        return groupRestrictionGroupTitles;
    }


    public void setGroupRestrictionGroupTitles(
            List<String> groupRestrictionGroupTitles) {
        this.groupRestrictionGroupTitles = groupRestrictionGroupTitles;
    }


    public List<String> getAttachmentReferences() {
        return attachmentReferences;
    }


    public void setAttachmentReferences(List<String> attachmentReferences) {
        this.attachmentReferences = attachmentReferences;
    }


    public Map getProperties()
    {
        return properties;
    }


    public void setProperties(Map properties)
    {
        this.properties = properties;
    }

	public boolean isContentReviewStudentViewReport() {
		return contentReviewStudentViewReport;
	}

	public void setContentReviewStudentViewReport(
			boolean contentReviewStudentViewReport) {
		this.contentReviewStudentViewReport = contentReviewStudentViewReport;
	}

}
