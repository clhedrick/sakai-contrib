/**********************************************************************************
 * $URL: $
 * $Id:  $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008, 2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.assignment2.logic;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.SubmissionAttachment;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.contentreview.model.ContentReviewItem;

/**
 * For accessing the systems Content Review System.  An example is a plagiarism
 * checking tool.
 * 
 * @author sgithens
 *
 */
public interface ExternalContentReviewLogic {

    /**
     * This will determine whether the given site has the ability to use
     * the Content Review Service.
     * @param siteId non-null
     * 
     * @return true if the site with the given siteId has the ability to use
     * the Content Review Service
     */
    public boolean isContentReviewAvailable(String siteId);
    
    /**
     * This will determine whether the current site has the ability to use the
     * Content Review Service
     * @return true if the current site has the ability to use the Content 
     * Review Service
     */
    public boolean isContentReviewAvailable();
    
    /**
     * Submit the attachment with the given content hosting reference to the content review service
     * for review. To minimize errors, check {@link #isAttachmentAcceptableForReview(String)} before
     * calling this method
     * @param userId if null, assumes current user
     * @param assign the assignment that this attachment is associated with
     * @param attachments the reference for the attachments in content hosting
     */
    public void reviewAttachment(String userId, Assignment2 assign, Set<SubmissionAttachment> attachments);
    
    /**
     * 
     * @param assign
     * @return a list of the {@link ContentReviewItem}s associated with the given assignment
     */
    public List<ContentReviewItem> getReviewItemsForAssignment(Assignment2 assign);
    
    /**
     * 
     * @param attachmentReference
     * @return true if the attachment with the given reference is acceptable for
     * the review service
     */
    public boolean isAttachmentAcceptableForReview(String attachmentReference);
    
    /**
     * 
     * @param assignment
     * @param attachments set of {@link SubmissionAttachment}s for the given assignment that you
     * want to retrieve review info for (ie score, icon url, etc)
     * @param instructorView true if this report is for the instructor view. false if this is
     * for the student view
     * Populates properties related to content review on the given attachments. If you
     * pass instructorView = false and the assignment is set up with the option 
     * prohibiting students from viewing the reports, the properties will not be populated
     */
    public void populateReviewProperties(Assignment2 assignment, Collection<SubmissionAttachment> attachments, boolean instructorView, String userId);

    /**
     * 
     * @param attachmentReference
     * @param instructorView true if you want the report for an instructor. false if
     * you want the report for the student
     * @param assign
     * @return the url of the review report for the given attachmentReference. Returns
     * null if url cannot be retrieved
     */
    public String getReportUrl(String attachmentReference, Assignment2 assign, boolean instructorView, String userId);
    
    /**
     * 
     * @param errorCode
     * @return given the code from the {@link AssignmentConstants#PROP_REVIEW_ERROR_CODE} property,
     * returns an internationalized error message representing the error textually
     */
    public String getErrorMessage(Long status, Integer errorCode);
    
    public String getTaskId(Assignment2 assign);
    
    /**
     * Given an assignment, will create the corresponding assignment via the ContentReviewService.  If
     * the assignment already exists in ContentReview, will update the existing assignment.
     * @param assing
     */
    public void createAssignment(Assignment2 assing);
    
    public void populateAssignmentPropertiesFromAssignment(Assignment2 assign);
    
    /**
     * returns content review service name
     * @return
     */
    public String getServiceName();
}
