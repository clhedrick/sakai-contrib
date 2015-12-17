/**********************************************************************************
 * $URL:https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/beans/locallogic/LocalAssignmentLogic.java $
 * $Id:LocalAssignmentLogic.java 48274 2008-04-23 20:07:00Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.AssignmentAuthzLogic;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.tool.beans.SessionCache;


/**
 * Contains methods that are used for putting information into the caches
 */
public class LocalCacheLogic {

    private static final Log LOG = LogFactory.getLog(LocalCacheLogic.class);

    public void addAssignmentAttachmentsToCache(Long assignId, Map<String,Object> optionalParams) {
        // check to see if user has permission to retrieve this assignment. this
        // method will return a security exception if not allowed
        Assignment2 assign = assignmentLogic.getAssignmentByIdWithAssociatedData(assignId, optionalParams);
        if (assign.getAttachmentSet() != null && !assign.getAttachmentSet().isEmpty()) {
            // we may need to override default permissions to grant access to these
            // attachments, so add to cache
            List<String> attachRefs = new ArrayList<String>(Arrays.asList(assign.getAssignmentAttachmentRefs()));
            sessionCache.addAsnn2AttachRefs(externalLogic.getCurrentUserId(), attachRefs);
        }
    }
    
    public void addSubmittedAttachmentsToCache(Long assignmentId, String studentId, Map<String,Object> optionalParams) {
        List<String> subAttachRefs = new ArrayList<String>();
        
        // add attachments for submitted versions. this method will throw a security
        // exception if not allowed
        AssignmentSubmission submission = submissionLogic.getCurrentSubmissionByAssignmentIdAndStudentId(assignmentId, studentId, optionalParams);
        
        if (submission.getSubmissionHistorySet() != null) {
            for (AssignmentSubmissionVersion ver : submission.getSubmissionHistorySet()) {
                if (ver.isSubmitted()) {
                    if (ver.getSubmissionAttachSet() != null && !ver.getSubmissionAttachSet().isEmpty()) {
                        List<String> attachRefs = new ArrayList<String>(Arrays.asList(ver.getSubmittedAttachmentRefs()));
                        subAttachRefs.addAll(attachRefs);
                    }
                }
            }
        }
        
        if (subAttachRefs != null && !subAttachRefs.isEmpty()) {
            sessionCache.addAsnn2AttachRefs(externalLogic.getCurrentUserId(), subAttachRefs);
        }
    }
    
    public void addAttachmentCacheSecurityAdvisor() {
        List<String> references = new ArrayList<String>();
        List<String> attachInCache = sessionCache.getAsnn2AttachRefs(externalLogic.getCurrentUserId());
        if (attachInCache != null && !attachInCache.isEmpty()) {
            for (String ref : attachInCache) {
                references.add("/content" + ref);
            }
            
            // we are just going to use the existing session attribute from assignment1
            externalLogic.addToSession("assignment.content.security.advisor", 
                    authzLogic.getSecurityAdvisor("content.read", references));
        }
    }

    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    private SessionCache sessionCache;
    public void setA2SessionCache(SessionCache sessionCache) {
        this.sessionCache = sessionCache;
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    
    private AssignmentSubmissionLogic submissionLogic;
    public void setAssignmentSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }
    
    private AssignmentAuthzLogic authzLogic;
    public void setAssignmentAuthzLogic(AssignmentAuthzLogic authzLogic) {
        this.authzLogic = authzLogic;
    }

}