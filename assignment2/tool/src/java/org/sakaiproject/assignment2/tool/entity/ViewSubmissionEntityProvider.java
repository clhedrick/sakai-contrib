/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/logic/impl/ImportExportLogicImpl.java $
 * $Id: ImportExportLogicImpl.java 67172 2010-04-14 19:33:06Z bahollad@indiana.edu $
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

package org.sakaiproject.assignment2.tool.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.LocalCacheLogic;
import org.sakaiproject.assignment2.tool.params.AssignmentDetailsViewParams;
import org.sakaiproject.assignment2.tool.params.ViewSubmissionParams;
import org.sakaiproject.assignment2.tool.producers.ViewAssignmentProducer;
import org.sakaiproject.assignment2.tool.producers.ViewStudentSubmissionProducer;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RequestStorable;
import org.sakaiproject.entitybroker.entityprovider.extension.RequestStorage;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;

import org.sakaiproject.rsf.entitybroker.EntityViewParamsInferrer;
import uk.org.ponder.rsf.viewstate.ViewParameters;


/**
 * Entity Provider for view an Assignment2 submission
 *
 */
public class ViewSubmissionEntityProvider extends AbstractEntityProvider implements
CoreEntityProvider, EntityViewParamsInferrer, RequestStorable {
    private static Log log = LogFactory.getLog(ViewSubmissionEntityProvider.class);

    public static final String ENTITY_PREFIX="view-assignment2-submission";
    public boolean entityExists(String id) {
        return true;
    }

    public String getEntityPrefix() {
        return ENTITY_PREFIX;
    }
    
    public String[] getHandledPrefixes() {
        return new String[] {ENTITY_PREFIX};
     }
    
    public ViewParameters inferDefaultViewParameters(String reference) {
        EntityReference ref = new EntityReference(reference);
        Long submissionId = Long.parseLong(ref.getId());
        
        // if the request includes a tagReference, we need to pass it along
        // in case it gives expanded permissions for viewing the submission
        String tagReference = (String) requestStorage.getStoredValue("tagReference");
        String tagDecoWrapper = (String) requestStorage.getStoredValue("tagDecoWrapper");
        
        Map<String, Object> optionalParams = new HashMap<String, Object>();
        optionalParams.put(AssignmentConstants.TAGGABLE_REF_KEY, tagReference);
        
        // now we need to derive the assignmentid and userid from the submission
        AssignmentSubmission sub = submissionLogic.getAssignmentSubmissionById(submissionId, optionalParams);
        
        // if we made it this far, we have permission to view the submission. in case the
        // tag ref is overriding default permissions, we may need to also override
        // security on any attachments
        if (tagReference != null && !"".equals(tagReference)) {
            localCacheLogic.addAssignmentAttachmentsToCache(sub.getAssignment().getId(), optionalParams);
            localCacheLogic.addSubmittedAttachmentsToCache(sub.getAssignment().getId(), sub.getUserId(), optionalParams);
            
            // we also need to add a SecurityAdvisor to the session for use when the user clicks the attach link
            localCacheLogic.addAttachmentCacheSecurityAdvisor();
        }

        return new ViewSubmissionParams(ViewStudentSubmissionProducer.VIEW_ID, sub.getAssignment().getId(), sub.getUserId(), tagReference, tagDecoWrapper);
     }
     
     private RequestStorage requestStorage;
     public void setRequestStorage(RequestStorage requestStorage) {
         this.requestStorage = requestStorage;
     }
     
     private AssignmentSubmissionLogic submissionLogic;
     public void setAssignmentSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
         this.submissionLogic = submissionLogic;
     }
     
     private LocalCacheLogic localCacheLogic;
     public void setLocalCacheLogic(LocalCacheLogic localCacheLogic) {
         this.localCacheLogic = localCacheLogic;
     }

}
