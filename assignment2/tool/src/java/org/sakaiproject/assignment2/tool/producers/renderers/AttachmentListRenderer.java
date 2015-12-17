/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/renderers/AttachmentListRenderer.java $
 * $Id: AttachmentListRenderer.java 69061 2010-07-06 11:36:52Z swgithen@mtu.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 The Sakai Foundation.
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

package org.sakaiproject.assignment2.tool.producers.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.AssignmentAuthzLogic;
import org.sakaiproject.assignment2.logic.AttachmentInformation;
import org.sakaiproject.assignment2.logic.ExternalContentLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentAttachment;
import org.sakaiproject.assignment2.model.FeedbackAttachment;
import org.sakaiproject.assignment2.model.ModelAnswerAttachment;
import org.sakaiproject.assignment2.model.SubmissionAttachment;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.beans.SessionCache;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.entity.api.Entity;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;

/**
 * Contains a number of convenience methods for rendering different kinds of
 * Attachments (ie. Supporting Materials). Note that there are different methods
 * to be used depending on whether the attachments are on an Assignment,
 * Submission, Feedback, etc.
 * 
 * TODO FIXME I'm still not sure why some of these methods require a viewid.
 * I'm guessing they might need to generate return URL's or something.
 * It would be cool to have the option of passing null for the viewid, in 
 * which case it would look it up in the context. But that would also allow
 * you to script this in case you were generating markup for future events.
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class AttachmentListRenderer {
    private static final Log LOG = LogFactory.getLog(AttachmentListRenderer.class);

    private ExternalContentLogic contentLogic;
    public void setExternalContentLogic(ExternalContentLogic contentLogic) {
        this.contentLogic = contentLogic;
    }

    private EntityBeanLocator assignment2EntityBeanLocator;
    public void setAssignment2EntityBeanLocator(EntityBeanLocator assignment2EntityBeanLocator) {
        this.assignment2EntityBeanLocator = assignment2EntityBeanLocator;
    }
    
    private ReviewStatusRenderer reviewStatusRenderer;
    public void setReviewStatusRenderer(ReviewStatusRenderer reviewStatusRenderer) {
        this.reviewStatusRenderer = reviewStatusRenderer;
    }

    /**
     * Use this for rendering attachments from an Assignment2 assignment
     * object. 
     * 
     * @param tofill
     * @param divID
     * @param currentViewID
     * @param aaSet
     * @param optionalParams optional extra information that might be useful for rendering the assignment info.
     * ie, you may need extended privileges for viewing the attachments so you could pass that info here
     */
    public void makeAttachmentFromAssignmentAttachmentSet(UIContainer tofill, String divID, String currentViewID, Set<AssignmentAttachment> aaSet, Map<String, Object> optionalParams) {
        Map<String, Map> attRefPropertiesMap = new HashMap<String, Map>();
        if (aaSet != null){
            for (AssignmentAttachment aa : aaSet) {
                attRefPropertiesMap.put(aa.getAttachmentReference(), aa.getProperties());
            }
        }
        makeAttachment(tofill, divID, currentViewID, attRefPropertiesMap, optionalParams);
    }
    
    /**
     * Use this for rendering attachments from an Assignment2 assignment
     * object. 
     * 
     * @param tofill
     * @param divID
     * @param currentViewID
     * @param aaSet
     * @param optionalParams optional extra information that might be useful for rendering the assignment info.
     * ie, you may need extended privileges for viewing the attachments so you could pass that info here
     */
    public void makeAttachmentFromModelAssignmentAttachmentSet(UIContainer tofill, String divID, String currentViewID, Set<ModelAnswerAttachment> maaSet, Map<String, Object> optionalParams) {
        Map<String, Map> mattRefPropertiesMap = new HashMap<String, Map>();
        if (maaSet != null){
            for (ModelAnswerAttachment maa : maaSet) {
                mattRefPropertiesMap.put(maa.getAttachmentReference(), maa.getProperties());
            }
        }
        makeAttachment(tofill, divID, currentViewID, mattRefPropertiesMap, optionalParams);
    }
    
    /**
     * Use this for rendering attachments from an Assignment2 assignment
     * object. 
     * 
     * @param tofill
     * @param divID
     * @param currentViewID
     * @param aaSet
     */
    public void makeAttachmentFromAssignmentAttachmentSet(UIContainer tofill, String divID, String currentViewID, Set<AssignmentAttachment> aaSet) {
        makeAttachmentFromAssignmentAttachmentSet(tofill, divID, currentViewID, aaSet, null);
    }


    public void makeAttachmentFromAssignment2OTPAttachmentSet(UIContainer tofill, String divID, String currentViewID, String a2OTPKey) {
        Assignment2 assignment = (Assignment2)assignment2EntityBeanLocator.locateBean(a2OTPKey);
        Map<String, Map> attRefPropertiesMap = new HashMap<String, Map>();
        if (assignment != null && assignment.getAttachmentSet() != null){
            for (AssignmentAttachment aa : assignment.getAttachmentSet()) {
                attRefPropertiesMap.put(aa.getAttachmentReference(), aa.getProperties());
            }
        }
        makeAttachment(tofill, divID, currentViewID, attRefPropertiesMap, null);
    }

    public void makeAttachmentFromSubmissionAttachmentSet(UIContainer tofill, String divID, String currentViewID,
            Set<SubmissionAttachment> asaSet, Map<String, Object> optionalParams) {
        Map<String, Map> attRefPropertiesMap = new HashMap<String, Map>();
        if (asaSet != null) {
            for (SubmissionAttachment asa : asaSet) {
                attRefPropertiesMap.put(asa.getAttachmentReference(), asa.getProperties());
            }
        }
        makeAttachment(tofill, divID, currentViewID, attRefPropertiesMap, optionalParams);
    }

    public void makeAttachmentFromFeedbackAttachmentSet(UIContainer tofill, String divID, String currentViewID,
            Set<FeedbackAttachment> afaSet) {
        Map<String, Map> attRefPropertiesMap = new HashMap<String, Map>();
        if (afaSet != null) {
            for (FeedbackAttachment afa : afaSet) {
                attRefPropertiesMap.put(afa.getAttachmentReference(), afa.getProperties());
            }
        }
        makeAttachment(tofill, divID, currentViewID, attRefPropertiesMap, null);
    }

    /**
     * 
     * @param tofill
     * @param divID
     * @param currentViewID
     * @param attRefPropertiesMap a map of the attachment reference to its associated properties map
     * @param optionalParams optional extra information that might be useful for rendering the assignment info.
     * ie, you may need extended privileges for viewing the attachments so you could pass that info here
     */
    private void makeAttachment(UIContainer tofill, String divID, String currentViewID, Map<String, Map> attRefPropertiesMap, Map<String, Object> optionalParams) {


        int i = 1;
        if (attRefPropertiesMap.size() == 0) {
            UIJointContainer joint = new UIJointContainer(tofill, divID, "attachments:", ""+1);
            UIMessage.make(joint, "no_attachments_list", "assignment2.no_attachments_yet");
            return;
        }

        for (String ref : attRefPropertiesMap.keySet()){
            UIJointContainer joint = new UIJointContainer(tofill, divID, "attachments:", ""+(i++));

            //TODO FIXME For some reason, when there are no attachments, we 
            // still getting a single item in the Set<String> ref that is 
            // just an empty string.  This is on previewing an assignment.
            // To reproduce, just put in a title and hit preview.
            if (ref != null && !ref.equals("")) {
                
                AttachmentInformation attach = contentLogic.getAttachmentInformation(ref);
                
                if (attach == null) {
                    // we may have a permission override in the cache that will allow us
                    // to retrieve this attachment via a SecurityAdvisor
                    attach = getAttachmentWithAdvisor(ref, optionalParams);
                }
                
                if (attach != null) {
                    String file_size = "(" + attach.getContentLength() + ")";

                    UILink.make(joint, "attachment_image", attach.getContentTypeImagePath());
                    UILink.make(joint, "attachment_link", attach.getDisplayName(), attach.getUrl());  
                    UIOutput.make(joint, "attachment_size", file_size);
                    
                    // ASNN-516 check for properties
                    Map properties = attRefPropertiesMap.get(ref);
                    if (properties != null) {
                        reviewStatusRenderer.makeReviewStatusIndicator(joint, "review_report:", properties);
                    }
                }
            }

        } //Ending for loop
    }
    
    private AttachmentInformation getAttachmentWithAdvisor(String attachmentReference, Map<String, Object> optionalParams) {
        AttachmentInformation attach = null;
        if (optionalParams != null && 
                optionalParams.containsKey(AssignmentConstants.TAGGABLE_DECO_WRAPPER) && 
                optionalParams.get(AssignmentConstants.TAGGABLE_DECO_WRAPPER) != null) {
            
            String tagDecoWrapper = (String) optionalParams.get(AssignmentConstants.TAGGABLE_DECO_WRAPPER);
            tagDecoWrapper = tagDecoWrapper.replaceAll("_", Entity.SEPARATOR);
            
            List<String> attachRefs = sessionCache.getAsnn2AttachRefs(externalLogic.getCurrentUserId());
            if (attachRefs != null && attachRefs.contains(attachmentReference)) {
                // add a security advisor for this attachment
                List<String> references = new ArrayList<String>();
                references.add("/content" + attachmentReference);
                SecurityAdvisor advisor = authzLogic.getSecurityAdvisor("content.read", references);
                authzLogic.addSecurityAdvisor(advisor);
                attach = contentLogic.getAttachmentInformation(attachmentReference);
                authzLogic.removeSecurityAdvisor();

                // now we need to modify the url to access this attachment
                String attachUrl = attach.getUrl();
                attachUrl = attachUrl.replaceFirst("access/content", "access/" + tagDecoWrapper + "/content");
                attach.setUrl(attachUrl);
            }
        }

        return attach;
    }
    
    private SessionCache sessionCache;
    public void setA2SessionCache(SessionCache sessionCache) {
        this.sessionCache = sessionCache;
    }
    
    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    
    private AssignmentAuthzLogic authzLogic;
    public void setAssignmentAuthzLogic(AssignmentAuthzLogic authzLogic) {
        this.authzLogic = authzLogic;
    }

}
