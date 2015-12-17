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

package org.sakaiproject.assignment2.logic.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.ContentReviewException;
import org.sakaiproject.assignment2.logic.AssignmentBundleLogic;
import org.sakaiproject.assignment2.logic.ExternalContentLogic;
import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.Assignment2ContentReviewInfo;
import org.sakaiproject.assignment2.model.SubmissionAttachment;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.contentreview.exception.QueueException;
import org.sakaiproject.contentreview.exception.ReportException;
import org.sakaiproject.contentreview.exception.SubmissionException;
import org.sakaiproject.contentreview.model.ContentReviewItem;
import org.sakaiproject.contentreview.service.ContentReviewService;
import org.sakaiproject.id.api.IdManager;
import org.sakaiproject.site.api.Site;

import uk.org.ponder.arrayutil.ArrayUtil;

public class ExternalContentReviewLogicImpl implements ExternalContentReviewLogic {

    private static Log log = LogFactory.getLog(ExternalContentReviewLogicImpl.class);

    private ContentReviewService contentReview;
    private ExternalContentLogic contentLogic;
    private AssignmentBundleLogic bundleLogic;
    private ServerConfigurationService serverConfigurationService;
    private ExternalLogic externalLogic;
    
    private IdManager idManager;
    public void setIdManager(IdManager idManager) {
        this.idManager = idManager;
    }

    public void init(){
        if(log.isDebugEnabled()) log.debug("init");
        //if no contentReviewService was set try discovering it
        if (contentReview == null)
        {
            contentReview = (ContentReviewService) ComponentManager.get(ContentReviewService.class.getName());
        }
    }

    public boolean isContentReviewAvailable(String siteId) {
        if (siteId == null) {
            throw new IllegalArgumentException("Null siteId passed to isContentReviewAvailable");
        }
        
        boolean available = false;
        if (contentReview != null) {
            // check and see if Turnitin was enabled
            String turnitinEnabled = serverConfigurationService.getString(AssignmentConstants.TII_ENABLED, "false");
            String contentReviewEnabled = serverConfigurationService.getString(AssignmentConstants.CONTENT_REVIEW_ENABLED, "false");
            if ("true".equals(turnitinEnabled) || "true".equals(contentReviewEnabled)) {
                // we need to see if it was enabled at the site level
                Site site = externalLogic.getSite(siteId);
                if (site != null && contentReview.isSiteAcceptable(site)) {
                    available = true;
                } else {
                    available = false;
                }
            }
        }

        return available;
    }
    
    public boolean isContentReviewAvailable() {
        String siteId = externalLogic.getCurrentContextId();
        return isContentReviewAvailable(siteId);
    }

    public void reviewAttachment(String userId, Assignment2 assign, Set<SubmissionAttachment> attachments) {
        if (assign == null || attachments == null) {
            throw new IllegalArgumentException("Null assignment or contentId passed to " +
                    "reviewAttachments. assign: " + " contentId: ");
        }

        try
        {
        	List<ContentResource> resources = new ArrayList<ContentResource>();
        	for(SubmissionAttachment attach : attachments){
        		ContentResource resource = contentLogic.getContentResource(attach.getAttachmentReference());
        		if(resource != null){
        			resources.add(resource);
        		}
        	}
        	
            if (resources.size() > 0) {
            	contentReview.queueContent(userId, assign.getContextId(), getTaskId(assign), resources);
            }else{
            	throw new IllegalArgumentException("contentId passed to reviewAttachments is invalid and can't be found; contentId: ");
            }
        }
        catch (QueueException e)
        {
            // this is thrown if this attachment has already been queued
            log.warn("Attempt to queue content via the ContentReviewService that has already been queued. Content id:");
        }
    }


    public List<ContentReviewItem> getReviewItemsForAssignment(Assignment2 assign) {
        if (assign == null) {
            throw new IllegalArgumentException("Null assignment passed to getReviewItemsForAssignment");
        }

        List<ContentReviewItem> reviewItems = new ArrayList<ContentReviewItem>();

        try
        {
            reviewItems = contentReview.getAllContentReviewItems(assign.getContextId(), getTaskId(assign));
        }
        catch (QueueException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SubmissionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ReportException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return reviewItems;
    }

    public boolean isAttachmentAcceptableForReview(String attachmentReference) {
        if (attachmentReference == null) {
            throw new IllegalArgumentException("Null attachmentReference passed to isAttachmentEligibleForReview");
        }

        boolean acceptable = false;

        // we need to retrieve the ContentResource for this attachment
        ContentResource resource = contentLogic.getContentResource(attachmentReference);
        if (resource != null) {
            acceptable = contentReview.isAcceptableContent(resource);
        }

        return acceptable;
    }

    /**
     * 
     * @param assign
     * @return the "taskId" required by the {@link ContentReviewService} to uniquely
     * identify this assignment in the service
     */
    public String getTaskId(Assignment2 assign) {
        if (assign.getContentReviewRef() == null || assign.getContentReviewRef().equals("")) {
            return "/asnn2contentreview/" + idManager.createUuid();
        }
        else {
            return assign.getContentReviewRef();
        }
    }
    
    public void populateReviewProperties(Assignment2 assignment, Collection<SubmissionAttachment> attachments, boolean instructorView, String userId) {
        if (assignment == null) {
            throw new IllegalArgumentException("Null assignment passed to populateReviewProperties");
        }

        if (attachments != null && !attachments.isEmpty()) {

            boolean populateReports;
            if (instructorView) {
                populateReports = true;
            } else {
                // if this isn't the instructor view, we need to check and see if the assignment
                // allows students to view the reports
                if (assignment.getProperties() == null || assignment.getProperties().isEmpty()) {
                    populateAssignmentPropertiesFromAssignment(assignment);
                }

                if (assignment.isContentReviewStudentViewReport()) {
                    populateReports = true;
                } else {
                    populateReports = false;
                }
            }

            if (populateReports) {
                // let's get all of the review items for this assignment
                List<ContentReviewItem> allReviewItems = getReviewItemsForAssignment(assignment);
                // put these items into a map of the attachment reference to the review item for easier access
                Map<String, ContentReviewItem> attRefReviewItemMap = new HashMap<String, ContentReviewItem>();
                if (allReviewItems != null) {
                    for (ContentReviewItem reviewItem : allReviewItems) {
                        attRefReviewItemMap.put(reviewItem.getContentId(), reviewItem);
                    }
                }

                // now let's iterate through the passed attachments and populate the
                // properties
                for (SubmissionAttachment attach : attachments) {
                    ContentReviewItem reviewItem = new ContentReviewItem(attach.getAttachmentReference());

                    if (attRefReviewItemMap.containsKey(attach.getAttachmentReference())) {
                        reviewItem = attRefReviewItemMap.get(attach.getAttachmentReference());
                    } else {
                        // check to see if this has been submitted yet. The call to getReviewItems only
                        // returns successfully submitted review items. we want to know if
                        // this attachment encountered an error along the way
                        try
                        {
                            Long status = contentReview.getReviewStatus(attach.getAttachmentReference());
                            reviewItem.setStatus(status);
                            if(!"TurnItIn".equalsIgnoreCase(contentReview.getServiceName())){
                            	//look up score if it exist
                            	int reviewScore = contentReview.getReviewScore(attach.getAttachmentReference(), getTaskId(assignment), userId);
                            	reviewItem.setReviewScore(reviewScore);
                            	if(reviewItem.getStatus() == null && reviewScore >= 0){
                            		//found the score, set status to 3l
                            		reviewItem.setStatus(3l);
                            	}
                            }
                        }
                        catch (QueueException e)
                        {
                            if (log.isDebugEnabled()) log.debug("Attempt to retrieve status for attachment that has not been queued");
                            // this attachment has not been submitted so leave ContentReviewItem empty
                            ContentResource resource = contentLogic.getContentResource(attach.getAttachmentReference());
                            if(resource != null){
                                try {
                                    contentReview.queueContent(userId, assignment.getContextId(), getTaskId(assignment), Arrays.asList(resource));
                                } catch (QueueException e1) {
                                    if (log.isDebugEnabled()) log.debug("Attempt to re-queue but failed", e);
                                }
                            }
                        } catch (ReportException e) {
                            if (log.isDebugEnabled()) log.debug("Report exception, requeueing", e);
                        } catch (Exception e) {
                            if (log.isDebugEnabled()) log.debug("Attempt to retrieve status for attachment that has not been queued", e);
                        }
                    }

                    populateProperties(assignment, reviewItem, attach, instructorView, userId);
                }
            }
        }
    }
    
    /**
     * Populates the properties from this review item on the given attach
     * @param reviewItem
     * @param attach
     * @param instructorView true if this is for the instructor view. false if for student view
     */
    private void populateProperties(Assignment2 assign, ContentReviewItem reviewItem, SubmissionAttachment attach, boolean instructorView, String userId) {
        if (assign == null) {
            throw new IllegalArgumentException("Null assign passed to populateProperties");
        }
        if (reviewItem != null && attach != null) {
            Map properties = attach.getProperties() != null ? attach.getProperties() : new HashMap();

            Assignment2ContentReviewInfo reviewInfo = new Assignment2ContentReviewInfo();
            reviewInfo.setContentReviewItem(reviewItem);
            attach.setContentReviewInfo(reviewInfo);
            
            if (reviewItem.getStatus() != null) {
                if (reviewItem.getStatus().equals(ContentReviewItem.SUBMITTED_REPORT_AVAILABLE_CODE)) {
                    reviewInfo.setScoreDisplay(reviewItem.getReviewScore() + "%");
                } 
                
                // now retrieve the report url if status shows it exists
                String reportUrl = getReportUrl(attach.getAttachmentReference(), assign, instructorView, userId);
                if (reportUrl != null) {
                    reviewInfo.setReviewUrl(reportUrl);
                }
            }
            
            properties.put(AssignmentConstants.PROP_REVIEW_INFO, reviewInfo);
            attach.setProperties(properties);
            
        }
    }
    
    public String getReportUrl(String attachmentReference, Assignment2 assign, boolean instructorView, String userId) {
        if (attachmentReference == null) {
            throw new IllegalArgumentException("Null attachmentReference passed to getReportUrl");
        }
        
        String reportUrl = null;
        
        if (instructorView) {
            try
            {
                reportUrl = contentReview.getReviewReportInstructor(attachmentReference, getTaskId(assign), userId);
            }
            catch (QueueException e)
            {
                // TODO Auto-generated catch block
                // this is thrown if content was never queued previously
                e.printStackTrace();
            }
            catch (ReportException e)
            {
                // TODO Auto-generated catch block
                // this is likely thrown if the attachment hasn't been reviewed yet
                e.printStackTrace();
            }
        } else {
            try
            {
                reportUrl = contentReview.getReviewReportStudent(attachmentReference, getTaskId(assign), userId);
            }
            catch (QueueException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ReportException e)
            {
                //ASNN-729 we expect some of these
            	if (log.isDebugEnabled()) {
            		log.debug("ReportException", e);
            	}
            }
        }
        
        return reportUrl;
    }
    
    public String getErrorMessage(Long status, Integer errorCode) {
        String errorMessage = null; 
        
        if (status != null) {
            if (status.equals(ContentReviewItem.REPORT_ERROR_NO_RETRY_CODE)) {
                errorMessage = bundleLogic.getString("assignment2.content_review.error.REPORT_ERROR_NO_RETRY_CODE");
            } else if (status.equals(ContentReviewItem.REPORT_ERROR_RETRY_CODE)) {
                errorMessage = bundleLogic.getString("assignment2.content_review.error.REPORT_ERROR_RETRY_CODE");
            } else if (status.equals(ContentReviewItem.SUBMISSION_ERROR_NO_RETRY_CODE)) {
                // Look up actual error from ContentReview
                if (errorCode == null) {
                    errorMessage = bundleLogic.getString("assignment2.content_review.error.SUBMISSION_ERROR_NO_RETRY_CODE");
                }
                else {
                    // TODO what happens if there is no translation for this code?
                    errorMessage = contentReview.getLocalizedStatusMessage(errorCode.toString());
                }
            } else if (status.equals(ContentReviewItem.SUBMISSION_ERROR_RETRY_CODE)) {
                errorMessage = bundleLogic.getString("assignment2.content_review.error.SUBMISSION_ERROR_RETRY_CODE");
            } else if (status.equals(ContentReviewItem.SUBMISSION_ERROR_RETRY_EXCEEDED)) {
                errorMessage = bundleLogic.getString("assignment2.content_review.error.SUBMISSION_ERROR_RETRY_EXCEEDED_CODE");
            } else if (status.equals(ContentReviewItem.SUBMISSION_ERROR_USER_DETAILS_CODE)) {
                errorMessage = bundleLogic.getString("assignment2.content_review.error.SUBMISSION_ERROR_USER_DETAILS_CODE");
            }
        }
        
        if (errorMessage == null) {
            errorMessage = bundleLogic.getString("assignment2.content_review.error");
        }
        
        return errorMessage;
        
    }
    
    public void populateAssignmentPropertiesFromAssignment(Assignment2 assign) {
        if (!assign.isContentReviewEnabled() || assign.getContentReviewRef() == null || assign.getContentReviewRef().equals("")) {
            return;
        }
        
        Map asnnmap = new HashMap();
        try {
            asnnmap = contentReview.getAssignment(assign.getContextId(), assign.getContentReviewRef());
        } catch (Exception e) {
            // TODO I believe we were catching all exceptions to make sure this
            // never ever stops us from loading an assignment.
            log.error(e);
        }
        if(asnnmap == null){
        	asnnmap = new HashMap();
        }
        boolean useGradeMark = serverConfigurationService.getBoolean(AssignmentConstants.TII_PROP_GRADEMARK_ENABLED, false);
        //Check Grade
        if(useGradeMark){
            try {
                contentReview.getReviewScore(assign.getContextId()+
                        "#"+assign.getContentReviewRef()+
                        "#"+assign.getTitle(), getTaskId(assign), null);

            }catch(Exception e){
                log.error(e);
            }
        }
        
        if (asnnmap.containsKey(AssignmentConstants.TII_RETCODE_RCODE)) {
            assign.getProperties().put(AssignmentConstants.TII_RETCODE_RCODE, 
                    asnnmap.get(AssignmentConstants.TII_RETCODE_RCODE));
        }
        else {
            assign.getProperties().put(AssignmentConstants.TII_RETCODE_RCODE, "-1");
        }
        
        if (asnnmap.containsKey(AssignmentConstants.TII_RETCODE_RMESSAGE)) {
            assign.getProperties().put(AssignmentConstants.TII_RETCODE_RMESSAGE, 
                    asnnmap.get(AssignmentConstants.TII_RETCODE_RMESSAGE));
        }
        
        if (asnnmap.containsKey(AssignmentConstants.TII_RETCODE_OBJECT)) {
            Map asnnobj = (Map) asnnmap.get(AssignmentConstants.TII_RETCODE_OBJECT);
            assign.getProperties().put(AssignmentConstants.TII_RETCODE_SUBMIT_PAPERS_TO,
                    asnnobj.get(AssignmentConstants.TII_API_PARAM_REPOSITORY));
            assign.getProperties().put(AssignmentConstants.TII_RETCODE_REPORT_GEN_SPEED,
                    asnnobj.get(AssignmentConstants.TII_API_PARAM_GENERATE));
            
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_SEARCHPAPERS, 
                    assign, AssignmentConstants.TII_API_PARAM_S_PAPER_CHECK);
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_SEARCHINTERNET, 
                    assign, AssignmentConstants.TII_API_PARAM_INTERNET_CHECK);
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_SEARCHJOURNALS, 
                    assign, AssignmentConstants.TII_API_PARAM_JOURNAL_CHECK);
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_SEARCHINSTITUTION, 
                    assign, AssignmentConstants.TII_API_PARAM_INSTITUTION_CHECK);
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_SVIEWREPORTS, 
                    assign, AssignmentConstants.TII_API_PARAM_S_VIEW_REPORT);

            //erater
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_ERATER,
                    assign, AssignmentConstants.TII_API_PARAM_ERATER);

            //Handbook
            if(asnnobj.containsKey(AssignmentConstants.TII_RETCODE_ETS_HANDBOOK)){
                assign.getProperties().put(AssignmentConstants.TII_API_PARAM_ETS_HANDBOOK,
                        asnnobj.get(AssignmentConstants.TII_API_PARAM_ETS_HANDBOOK));
            }

            //Dictionary
            if(asnnobj.containsKey(AssignmentConstants.TII_RETCODE_ETS_DICTIONARY)){
                assign.getProperties().put(AssignmentConstants.TII_API_PARAM_ETS_DICTIONARY,
                        asnnobj.get(AssignmentConstants.TII_RETCODE_ETS_DICTIONARY));
            }

            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_ETS_SPELLING,
                    assign, AssignmentConstants.TII_API_PARAM_ETS_SPELLING);
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_ETS_GRAMMAR,
                    assign, AssignmentConstants.TII_API_PARAM_ETS_GRAMMAR);
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_ETS_MECHANICS,
                    assign, AssignmentConstants.TII_API_PARAM_ETS_MECHANICS);
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_ETS_USAGE,
                    assign, AssignmentConstants.TII_API_PARAM_ETS_USAGE);
            setTurnitinBooleanOption(asnnobj, AssignmentConstants.TII_RETCODE_ETS_STYLE,
                    assign, AssignmentConstants.TII_API_PARAM_ETS_STYLE);
        }
        
    }
    
    private void setTurnitinBooleanOption(Map asnnobj, String mapname, Assignment2 assign, String propname) {
        if (asnnobj.containsKey(mapname) && asnnobj.get(mapname).equals("1")) {
            assign.getProperties().put(propname, new Boolean(true));
            if(AssignmentConstants.TII_API_PARAM_S_VIEW_REPORT.equals(propname)){
            	//this handles old assignments that relied on TII for this setting (instead of sakai's db)
            	assign.setContentReviewStudentViewReport(true);
            }
        }
        else {
            assign.getProperties().put(propname, new Boolean(false));
        }
    }

    public void createAssignment(Assignment2 assign) {
        Map opts = new HashMap();
        
        String[] tiioptKeys = new String[] { "submit_papers_to", "report_gen_speed",
                "s_paper_check", "internet_check", "journal_check", "institution_check", "s_view_report",
                "erater","ets_handbook","ets_dictionary","ets_spelling","ets_style","ets_grammar",
                "ets_mechanics","ets_usage"
        };

        for (Object key: assign.getProperties().keySet()) {
            if (ArrayUtil.contains(tiioptKeys, key)) {
                if (assign.getProperties().get(key) instanceof Boolean) {
                    if (((Boolean) assign.getProperties().get(key)).booleanValue()) {
                        opts.put(key, "1");
                    }
                    else {
                        opts.put(key, "0");
                    }
                }
                else {
                    opts.put(key.toString(), assign.getProperties().get(key).toString());
                }
            }
        }
        
        // ONC-2486
        opts.put("late_accept_flag", "1");
        
        SimpleDateFormat dform = ((SimpleDateFormat) DateFormat.getDateInstance());
        dform.applyPattern("yyyy-MM-dd HH:mm:ss");
        
        if (assign.getOpenDate() != null) {
            opts.put("dtstart", dform.format(assign.getOpenDate()));
        }
        
        if (assign.getDueDate() != null) {
            opts.put("dtdue", dform.format(assign.getDueDate()));
        }
        
        try {
        	opts.put("title", assign.getTitle());
            contentReview.createAssignment(assign.getContextId(), 
                    this.getTaskId(assign), opts);
        } catch (Exception e) {
            log.warn("Exception saving Turnitin settings", e);
            throw new ContentReviewException("An exception occurred attempting to save TII settings", e);
        }
    }
    
    public void setExternalContentLogic(ExternalContentLogic contentLogic) {
        this.contentLogic = contentLogic;
    }
    
    public void setContentReviewService(ContentReviewService contentReview) {
        this.contentReview = contentReview;
    }
    
    public void setAssignmentBundleLogic(AssignmentBundleLogic bundleLogic) {
        this.bundleLogic = bundleLogic;
    }
    
    public void setServerConfigurationService(ServerConfigurationService serverConfigurationService) {
        this.serverConfigurationService = serverConfigurationService;
    }
    
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    
    public String getServiceName(){
    	return contentReview.getServiceName();
    }

}