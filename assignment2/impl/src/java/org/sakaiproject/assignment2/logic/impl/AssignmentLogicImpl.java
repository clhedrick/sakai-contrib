/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/logic/impl/AssignmentLogicImpl.java $
 * $Id: AssignmentLogicImpl.java 86722 2014-07-11 04:59:45Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.logic.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.dao.AssignmentDao;
import org.sakaiproject.assignment2.exception.AnnouncementPermissionException;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.CalendarPermissionException;
import org.sakaiproject.assignment2.exception.ContentReviewException;
import org.sakaiproject.assignment2.exception.InvalidGradebookItemAssociationException;
import org.sakaiproject.assignment2.exception.NoGradebookItemForGradedAssignmentException;
import org.sakaiproject.assignment2.exception.StaleObjectModificationException;
import org.sakaiproject.assignment2.logic.AssignmentBundleLogic;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalAnnouncementLogic;
import org.sakaiproject.assignment2.logic.ExternalCalendarLogic;
import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.logic.ExternalEventLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.logic.utils.Assignment2Utils;
import org.sakaiproject.assignment2.logic.utils.ComparatorsUtils;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentAttachment;
import org.sakaiproject.assignment2.model.AssignmentAttachmentBase;
import org.sakaiproject.assignment2.model.AssignmentGroup;
import org.sakaiproject.assignment2.model.ModelAnswerAttachment;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.service.model.AssignmentDefinition;
import org.sakaiproject.assignment2.taggable.api.AssignmentActivityProducer;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.entitybroker.entityprovider.EntityProviderManager;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.genericdao.api.search.Search;
import org.sakaiproject.taggable.api.TaggingManager;
import org.sakaiproject.taggable.api.TaggingProvider;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;


/**
 * This is the interface for the Assignment object
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class AssignmentLogicImpl implements AssignmentLogic{

    private static Log log = LogFactory.getLog(AssignmentLogicImpl.class);

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }

    private ExternalAnnouncementLogic announcementLogic;
    public void setExternalAnnouncementLogic(ExternalAnnouncementLogic announcementLogic) {
        this.announcementLogic = announcementLogic;
    }

    private ExternalCalendarLogic calendarLogic;
    public void setExternalCalendarLogic(ExternalCalendarLogic calendarLogic) {
        this.calendarLogic = calendarLogic;
    }

    private AssignmentPermissionLogic permissionLogic;
    public void setPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    private AssignmentDao dao;
    public void setDao(AssignmentDao dao) {
        this.dao = dao;
    }

    private AssignmentBundleLogic bundleLogic;
    public void setAssignmentBundleLogic(AssignmentBundleLogic bundleLogic) {
        this.bundleLogic = bundleLogic;
    }

    private TaggingManager taggingManager;
    public void setTaggingManager(TaggingManager taggingManager) {
        this.taggingManager = taggingManager;
    }

    private AssignmentActivityProducer assignmentActivityProducer;
    public void setAssignmentActivityProducer(AssignmentActivityProducer assignmentActivityProducer) {
        this.assignmentActivityProducer = assignmentActivityProducer;
    }
    
    private EntityProviderManager entityProviderManager;
    public void setEntityProviderManager(EntityProviderManager entityProviderManager) {
        this.entityProviderManager = entityProviderManager;
    }
    
    private ExternalContentReviewLogic externalContentReviewLogic;
    public void setExternalContentReviewLogic(ExternalContentReviewLogic externalContentReviewLogic) {
        this.externalContentReviewLogic = externalContentReviewLogic;
    }
    
    private ExternalEventLogic externalEventLogic;
    public void setExternalEventLogic(ExternalEventLogic externalEventLogic) {
        this.externalEventLogic = externalEventLogic;
    }
    
    // Move to an external API
    private ServerConfigurationService serverConfigurationService; 
    public void setServerConfigurationService (ServerConfigurationService serverConfigurationService) {
            this.serverConfigurationService = serverConfigurationService;
    }
    
    private boolean allowGradebookSync;

    public void init(){
        if(log.isDebugEnabled()) log.debug("init");
        allowGradebookSync = serverConfigurationService.getBoolean(AssignmentConstants.CONFIG_ALLOW_GB_SYNC, true);
    }
    
    public Assignment2 getAssignmentByIdWithAssociatedData(Long assignmentId) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("Null assignmentId passed to getAssignmentByIdWithAssociatedData");
        }
        
        return getAssignmentByIdWithAssociatedData(assignmentId, null);
    }

    public Assignment2 getAssignmentByIdWithAssociatedData(Long assignmentId, Map<String, Object> optionalParameters) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("Null assignmentId passed to getAssignmentByIdWithAssociatedData");
        }
        
        // retrieve Assignment2 object
        Assignment2 assign = (Assignment2) dao.getAssignmentByIdWithGroupsAndAttachments(assignmentId);

        if (assign == null) {
            throw new AssignmentNotFoundException("No assignment found with id: " + assignmentId);
        }
        
        // make sure the user can access the assignment object
        if (!permissionLogic.isUserAllowedToViewAssignment(null, assign, optionalParameters)) {
            throw new SecurityException("User attempted to access assignment with id " + assignmentId + " without permission");
        }

        // TODO ASNN-516 Check for ContentReview and populate
        // check for null entityProviderManager so we don't have to mock it for the unit tests
        //if (entityProviderManager != null) {
        //    EntityProvider turnitinAsnnProvider = entityProviderManager.getProviderByPrefix("turnitin-assignment");
        //    if (turnitinAsnnProvider != null && turnitinAsnnProvider instanceof CRUDable) {
        //        CRUDable crudable = (CRUDable) turnitinAsnnProvider;
        //        Map tiiopts = (Map) crudable.getEntity(new EntityReference("turnitin-assignment", externalContentReviewLogic.getTaskId(assign)));
        //        assign.setProperties(tiiopts); // TODO this should be a map merge and not a complete replacement
        //    }
        //}
        if (externalContentReviewLogic.isContentReviewAvailable(assign.getContextId())) {
            externalContentReviewLogic.populateAssignmentPropertiesFromAssignment(assign);
        }
        
        return assign;
    }

    public Assignment2 getAssignmentById(Long assignmentId) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("Null assignmentId passed to getAssignmentByIdWithGroupsAndAttachments");
        }
        
        // make sure the user can access the assignment object
        if (!permissionLogic.isUserAllowedToViewAssignmentId(null, assignmentId, null)) {
            throw new SecurityException("User attempted to access assignment with id " + assignmentId + " without permission");
        }

        Assignment2 assign = (Assignment2) dao.getAssignmentByIdWithGroupsAndAttachments(assignmentId);

        if (assign == null) {
            throw new AssignmentNotFoundException("No assignment found with id: " + assignmentId);
        }

        return assign;
    }
    
    public void saveAssignment(Assignment2 assignment) throws SecurityException, 
    NoGradebookItemForGradedAssignmentException {
        saveAssignment(assignment, true);
    }

    public void saveAssignment(Assignment2 assignment, boolean syncGradebook) throws SecurityException, 
    NoGradebookItemForGradedAssignmentException {
        if (assignment == null || assignment.getContextId() == null) {
            throw new IllegalArgumentException("Null assignment or assignment.contextId passed to saveAssignment");
        }

        // now let's double check the non-null properties have been set
        if (assignment.getTitle() == null || 
                "".equals(assignment.getTitle().trim()) || 
                assignment.getOpenDate() == null) {

            throw new IllegalArgumentException("A non-null property of the assignment was null. title:" + 
                    assignment.getTitle() + " open date:" + assignment.getOpenDate());
        }

        String currentUserId = externalLogic.getCurrentUserId();

        if (assignment.isGraded() && assignment.getGradebookItemId() == null) {
            throw new NoGradebookItemForGradedAssignmentException("The assignment to save " + 
            "was defined as graded but it had a null getGradebookItemId");
        }

        boolean isNewAssignment = true;
        Assignment2 existingAssignment = null;

        // determine if this is a new assignment
        if (assignment.getId() != null) {
            // check to see if assignment exists
            existingAssignment = (Assignment2)dao.getAssignmentByIdWithGroupsAndAttachments(assignment.getId());	
            if (existingAssignment != null) {
                isNewAssignment = false;
            } else {
                throw new AssignmentNotFoundException("No assignment exists with id: " + assignment.getId() + " Assignment update failure.");
            }
        }
        
        // if it is a new assignment, check to see if user is allowed to add assignments
        // in this context. otherwise, ensure the user may edit this assignment
        if (isNewAssignment && !permissionLogic.isUserAllowedToAddAssignment(currentUserId, assignment)) {
            throw new SecurityException("Current user may not save assignment " + assignment.getTitle()
                    + " because they do not have add permission");
        } else if (!isNewAssignment && !permissionLogic.isUserAllowedToEditAssignment(currentUserId, assignment)) {
            throw new SecurityException("Current user may not edit assignment " + assignment.getTitle()
                    + " because they do not have edit permission");
        }

        // check to see if the gradebook item association is valid
        if (assignment.isGraded() && !gradebookLogic.isGradebookItemAssociationValid(
                assignment.getContextId(), assignment.getGradebookItemId())) {
            throw new InvalidGradebookItemAssociationException("The gradebook item " + assignment.getGradebookItemId() + 
            " is not a valid gradebook item to associate with this assignment");
        }

        // trim trailing spaces on title
        assignment.setTitle(assignment.getTitle().trim());

        // clean up the html string for the instructions
        assignment.setInstructions(externalLogic.cleanupUserStrings(assignment.getInstructions(), true));
        

        // double check that content review is available for this assignment
        if (assignment.isContentReviewEnabled()) {
            if (!externalContentReviewLogic.isContentReviewAvailable(assignment.getContextId())) {
                if (log.isDebugEnabled()) log.debug("Content review turned off b/c not available in this site");
                assignment.setContentReviewEnabled(false);
                assignment.setContentReviewStudentViewReport(false);
            }
        }

        Set<AssignmentAttachment> attachToDelete = new HashSet<AssignmentAttachment>();
        Set<ModelAnswerAttachment> modelAnswerAttachToDelete = new HashSet<ModelAnswerAttachment>();
        Set<AssignmentGroup> groupsToDelete = new HashSet<AssignmentGroup>();

        if (isNewAssignment) {
            // identify the next sort index to be used
            Integer highestIndex = dao.getHighestSortIndexInSite(assignment.getContextId());
            if (highestIndex != null) {
                assignment.setSortIndex(highestIndex + 1);
            } else {
                assignment.setSortIndex(0);
            }

            assignment.setRemoved(false);
            assignment.setCreateDate(new Date());
            assignment.setCreator(currentUserId);
        } else {

            assignment.setRemoved(false);
            assignment.setModifiedBy(currentUserId);
            assignment.setModifiedDate(new Date());

            attachToDelete = identifyAttachmentsToDelete(existingAssignment.getAttachmentSet(), assignment.getAttachmentSet());
            modelAnswerAttachToDelete = identifyAttachmentsToDelete(existingAssignment.getModelAnswerAttachmentSet(), 
                    assignment.getModelAnswerAttachmentSet());
            groupsToDelete = identifyGroupsToDelete(existingAssignment, assignment);
        }

        try {

            Set<AssignmentGroup> groupSet = new HashSet<AssignmentGroup>();
            if (assignment.getAssignmentGroupSet() != null) {
                groupSet = assignment.getAssignmentGroupSet();
            }

            Set<AssignmentAttachment> attachToCreate = identifyAttachmentsToCreate(
                    existingAssignment == null ? null : existingAssignment.getAttachmentSet(), 
                    assignment == null ? null : assignment.getAttachmentSet());
            
            Set<ModelAnswerAttachment> modelAnswerAttachToCreate = identifyAttachmentsToCreate(
                    existingAssignment == null ? null : existingAssignment.getModelAnswerAttachmentSet(), 
                    assignment == null ? null : assignment.getModelAnswerAttachmentSet());

            // make sure the assignment has been set for the attachments and groups
            populateAssignmentForAttachmentAndGroupSets(modelAnswerAttachToCreate, 
                    attachToCreate, groupSet, assignment);

            // ensure that these objects are ready for saving
            validateAttachmentsAndGroups(modelAnswerAttachToCreate, attachToCreate, groupSet);

            // TODO ASNN-530 Where is this really happening and why doesn't the null
            // error occur with regular attachments? 
            /*
            if (assignment.getModelAnswerAttachmentSet() == null) {
                assignment.setModelAnswerAttachmentSet(new HashSet<ModelAnswerAttachment>());
            }
            if (assignment.getAttachmentSet() == null) {
                assignment.setAttachmentSet(new HashSet<AssignmentAttachment>());
            }
            if (assignment.getAssignmentGroupSet() == null) {
                assignment.setAssignmentGroupSet(new HashSet<AssignmentGroup>());
            }
            */
            Set<Assignment2> assignSet = new HashSet<Assignment2>();
            assignSet.add(assignment);

            
            // to avoid the WARN: Nothing to update messages...
            List<Set> setsToSave = new ArrayList<Set>();
            
            if (!attachToCreate.isEmpty()) {
                setsToSave.add(attachToCreate);
            }
            if (!groupSet.isEmpty()) {
                setsToSave.add(groupSet);
            }
            if (!modelAnswerAttachToCreate.isEmpty()) {
                setsToSave.add(modelAnswerAttachToCreate);
            }
            
            if (setsToSave.size() > 0) {
                Set[] toSave = new Set[setsToSave.size()+1];
                toSave[0] = assignSet;
                for (int i = 1; i <= setsToSave.size(); i++) {
                    toSave[i] = setsToSave.get(i-1);
                }
                dao.saveMixedSet(toSave);
            }
            else {
                dao.save(assignment);
            }
            
            /*
            if (!attachToCreate.isEmpty() && !groupSet.isEmpty()) {
                dao.saveMixedSet(new Set[] {assignSet, attachToCreate, groupSet});
            } else if (!attachToCreate.isEmpty()) {
                dao.saveMixedSet(new Set[] {assignSet, attachToCreate});
            } else if (!groupSet.isEmpty()) {
                dao.saveMixedSet(new Set[] {assignSet, groupSet});
            } else {
                dao.save(assignment);
            }
            */

            if (log.isDebugEnabled()) {
                if (isNewAssignment) {
                    log.debug("Created assignment: " + assignment.getTitle());
                } else {
                    log.debug("Updated assignment: " + assignment.getTitle() + "with id: " + assignment.getId());
                }
            }

            if (!isNewAssignment) {
                if ((attachToDelete != null && !attachToDelete.isEmpty()) ||
                        (groupsToDelete != null && !groupsToDelete.isEmpty()) ||
                        (modelAnswerAttachToDelete != null && !modelAnswerAttachToDelete.isEmpty())) {
                    dao.deleteMixedSet(new Set[] {attachToDelete, groupsToDelete, modelAnswerAttachToDelete});
                    if(log.isDebugEnabled())log.debug("Attachments and/or groups removed for updated assignment " + assignment.getId());
                }
            }
            
            if (isNewAssignment) {
            	// ASNN-29
            	externalEventLogic.postEvent(AssignmentConstants.EVENT_ASSIGN_CREATE, assignment.getReference());
            }
            else {
                externalEventLogic.postEvent(AssignmentConstants.EVENT_ASSIGN_UPDATE, assignment.getReference());
            }
            
        } catch (HibernateOptimisticLockingFailureException holfe) {
            if(log.isInfoEnabled()) log.info("An optimistic locking failure occurred while attempting to update assignment with id: " + assignment.getId());
            throw new StaleObjectModificationException("An optimistic locking failure occurred while attempting to update assignment with id: " + assignment.getId(), holfe);
        }


        // now let's handle the impact on announcements
        if (externalLogic.siteHasTool(assignment.getContextId(), ExternalLogic.TOOL_ID_ANNC)) {
            try {
                saveAssignmentAnnouncement(existingAssignment, assignment);
            } catch (AnnouncementPermissionException ape) {
                log.warn("The current user is not " +
                        "authorized to update announcements in the announcements " +
                        "tool. Any related announcements were NOT updated", ape);
            }
        }

        // now let's handle the impact on the Schedule
        if (externalLogic.siteHasTool(assignment.getContextId(), ExternalLogic.TOOL_ID_SCHEDULE)) {
            try {
                handleDueDateEvent(existingAssignment, assignment);
            } catch (CalendarPermissionException cpe) {
                log.warn("The current user is not " +
                        "authorized to update events in the Schedule " +
                        "tool. Any related events were NOT updated", cpe);
            }
        }

        // TODO ASNN-516 Content Review / Turnitin Integration
        if (assignment.isContentReviewEnabled()) {
            boolean newIntegration = assignment.getContentReviewRef() == null;
            String tiiAsnnTitle = externalContentReviewLogic.getTaskId(assignment);
            assignment.setContentReviewRef(tiiAsnnTitle);
            log.debug("Going to Create/Update TII Asnn with title: " + tiiAsnnTitle);
            try {
                externalContentReviewLogic.createAssignment(assignment);
                dao.update(assignment);
            } catch (ContentReviewException cre) {
                // if we were attempting to integrate for the first time,
                // we don't want TII enabled to be left as true
                if (newIntegration) {
                    assignment.setContentReviewEnabled(false);
                    assignment.setContentReviewStudentViewReport(false);
                    assignment.setContentReviewRef(null);
                    dao.update(assignment);
                }
                
                throw new ContentReviewException("The assignment was saved, " +
                 "but Turnitin information was unable to be updated", cre);
            }
        }
        
        
        if (assignment.isGraded()) {
            GradebookItem gbItem = gradebookLogic.getGradebookItemById(assignment.getContextId(), assignment.getGradebookItemId());
            boolean updateGradebook = false;
            
            if (allowGradebookSync && syncGradebook) { // ONC-3115 (refactored)
                List<Assignment2> linkedAsnns = 
                    getAssignmentsWithLinkedGradebookItemId(assignment.getGradebookItemId());
                if (linkedAsnns != null && linkedAsnns.size() == 1) {
                    gbItem.setTitle(assignment.getTitle());
                    gbItem.setDueDate(assignment.getDueDate());
                    updateGradebook = true;
                }
            }
                        
            if (assignment.getGradebookPointsPossible() != null && // this needs to be checked because if this is called
                                                           // by updateEntity() during an JS inline title rename
                                                           // PointsPossible will be null 
                    gbItem.getPointsPossible() != assignment.getGradebookPointsPossibleDouble()) {
                
                gbItem.setPointsPossible(assignment.getGradebookPointsPossibleDouble());
                updateGradebook = true;
            }

            if (updateGradebook) {
                gradebookLogic.updateGbItemInGradebook(assignment.getContextId(), gbItem);
            }

        } // end if isGraded()
        
    }

    public void deleteAssignment(Assignment2 assignment) throws SecurityException, AnnouncementPermissionException
    {
        if (assignment == null) {
            throw new IllegalArgumentException("Null assignment passed to deleteAssignment");
        }

        if (assignment.getId() == null) {
            throw new IllegalArgumentException("The passed assignment does not have an id. Can only delete persisted assignments");
        }

        if (assignment.getContextId() == null) {
            throw new IllegalArgumentException("The passed assignment does not have an " +
            "associated contextId. You may not delete an assignment without a contextId");
        }

        if (!permissionLogic.isUserAllowedToDeleteAssignment(null, assignment)) {
            throw new SecurityException("Current user may not delete assignment " + assignment.getTitle()
                    + " because they do not have delete permission");
        }
        
        // we remove the tags first because it will check for "delete" permission
        // while removing the tags, and users can't delete items that have already
        // been deleted.
        // only process if taggingManager != null --> this is for the unit
        // tests to run without mocking up all the tagging stuff
        if (taggingManager != null) {
            try {
                if (taggingManager.isTaggable()) {
                    for (TaggingProvider provider : taggingManager
                            .getProviders()) {
                        provider.removeTags(assignmentActivityProducer
                                .getActivity(assignment));
                    }
                }
            }
            catch (PermissionException pe) {
                log.warn("The current user is not authorized to remove tags in the assignment tool, " +
                        "but the associated assignment was deleted", pe);
            }
        }

        assignment.setRemoved(true);
        assignment.setModifiedBy(externalLogic.getCurrentUserId());
        assignment.setModifiedDate(new Date());

        // remove associated announcements, if appropriate
        String announcementIdToDelete = null;
        if (assignment.getAnnouncementId() != null) {
            announcementIdToDelete = assignment.getAnnouncementId();
            assignment.setAnnouncementId(null);
            assignment.setHasAnnouncement(false);
        }

        // remove associated Schedule/Calendar events, if appropriate
        String eventIdToDelete = null;
        if (assignment.getEventId() != null) {
            eventIdToDelete = assignment.getEventId();
            assignment.setEventId(null);
            assignment.setAddedToSchedule(false);
        }

        try {
            dao.update(assignment);
            if(log.isDebugEnabled()) log.debug("Deleted assignment: " + assignment.getTitle() + " with id " + assignment.getId());

            // ASNN-29 
            externalEventLogic.postEvent(AssignmentConstants.EVENT_ASSIGN_DELETE, assignment.getReference());
            
            // now remove the announcement, if applicable
            if (announcementIdToDelete != null) {
                try {
                    announcementLogic.deleteOpenDateAnnouncement(announcementIdToDelete, assignment.getContextId());
                    if(log.isDebugEnabled()) log.debug("Deleted announcement with id " + announcementIdToDelete + " for assignment " + assignment.getId());
                } catch (AnnouncementPermissionException ape) {
                    log.warn("The current user is not authorized to remove announcements in the annc tool, " +
                    "but the linked assignment was deleted");
                }
            }

            // now remove the event, if applicable
            if (eventIdToDelete !=  null) {
                try {
                    calendarLogic.deleteDueDateEvent(eventIdToDelete, assignment.getContextId());
                    if(log.isDebugEnabled()) log.debug("Deleted event with id " + eventIdToDelete + 
                            " for assignment " + assignment.getId());
                } catch (CalendarPermissionException cpe) {
                    log.warn("The current user is not authorized to remove events in the Schedule tool, " +
                    "but the associated assignment was deleted");
                }
            }
        } catch (HibernateOptimisticLockingFailureException holfe) {
            if(log.isInfoEnabled()) log.info("An optimistic locking failure occurred " +
            "while attempting to update an assignment");
            throw new StaleObjectModificationException("Locking failure occurred " +
                    "while removing assignment with id: " + assignment.getId(), holfe);
        } 
        
    }


    public List<Assignment2> getViewableAssignments(String contextId)
    {   
        if (contextId == null) {
            throw new IllegalArgumentException("null contextId passed to " + this);
        }

        List<Assignment2> viewableAssignments = new ArrayList<Assignment2>();
        // include the removed assignments in case the user is auth to view it
        List<Assignment2> allAssignments = dao.getAllAssignmentsWithGroupsAndAttachments(contextId);

        if (allAssignments != null && !allAssignments.isEmpty()) {
            viewableAssignments = permissionLogic.filterViewableAssignments(contextId, allAssignments);	    
        }

        Collections.sort(viewableAssignments, new ComparatorsUtils.Assignment2SortIndexComparator());

        return viewableAssignments;
    }

    public void reorderAssignments(List<Long> assignmentIds, String contextId)
    {	
        if (assignmentIds == null) {
            throw new IllegalArgumentException("Null list of assignmentIds passed to reorder.");
        }

        if (contextId == null) {
            throw new IllegalArgumentException("Null contextId passed to " + this);
        }

        if (!permissionLogic.isUserAllowedToEditAllAssignments(null, contextId)) {
            throw new SecurityException("Unauthorized user attempted to reorder assignments!");
        }

        List<Assignment2> allAssigns = dao.findByProperties(Assignment2.class, 
                new String[] {"contextId", "removed"}, new Object[]{contextId, false});

        Map<Long, Assignment2> assignIdAssignMap = new HashMap<Long, Assignment2>();
        if (allAssigns != null) {
            for (Assignment2 assign : allAssigns) {
                assignIdAssignMap.put(assign.getId(), assign);
            }
        }

        // throw the passed ids into a set to remove duplicates
        Set<Long> assignIdSet = new HashSet<Long>();
        for (Long assignId : assignmentIds) {
            assignIdSet.add(assignId);
        }

        // check that there are an equal number in the passed list as there are
        // assignments in this site
        if (assignIdSet.size() != assignIdAssignMap.size()) {
            throw new IllegalArgumentException("The number of unique assignment ids passed does not match the num assignments in the site");
        }

        // now make sure all of the passed ids actually exist
        for (Long assignId : assignmentIds) {
            if (!assignIdAssignMap.containsKey(assignId)) {
                throw new IllegalArgumentException("The assignment id " + assignId 
                        + " does not exist in site " + contextId);
            }
        }

        String userId = externalLogic.getCurrentUserId();
        //Assume array of longs is in correct order now
        //so that the index of the array is the new 
        //sort index
        Set<Assignment2> assignSet = new HashSet<Assignment2>();
        for (int i=0; i < assignmentIds.size(); i++){
            //get Assignment
            Long assignId = assignmentIds.get(i);
            Assignment2 assignment = assignIdAssignMap.get(assignId);
            if (assignment != null){
                //check if we need to update
                if (assignment.getSortIndex() != i){
                    //update and save
                    assignment.setSortIndex(i);
                    assignment.setModifiedBy(userId);
                    assignment.setModifiedDate(new Date());
                    assignSet.add(assignment);
                    if(log.isDebugEnabled()) log.debug("Assignment " + assignment.getId() + " sort index changed to " + i);
                }
            }
        }

        try {
            dao.saveMixedSet(new Set[]{assignSet});
            if(log.isDebugEnabled()) log.debug("Reordered assignments saved. " + 
                    assignSet.size() + " assigns were updated");
        } catch (HibernateOptimisticLockingFailureException holfe) {
            if(log.isInfoEnabled()) log.info("An optimistic locking failure occurred while attempting to reorder the assignments");
            throw new StaleObjectModificationException("An optimistic locking failure occurred while attempting to reorder the assignments", holfe);
        }
    }

    public int getStatusForAssignment(Assignment2 assignment) {
        if (assignment == null){
            throw new IllegalArgumentException("Null assignment passed to getStatusForAssignment");
        }
        if (assignment.isDraft())
            return AssignmentConstants.STATUS_DRAFT;

        Date currDate = new Date();

        if (currDate.before(assignment.getOpenDate()))
            return AssignmentConstants.STATUS_NOT_OPEN;

        if (!assignment.isSubmissionOpen()) {
            return AssignmentConstants.STATUS_CLOSED;
        }

        if (assignment.getDueDate() != null) {
            if (currDate.after(assignment.getDueDate()))
                return AssignmentConstants.STATUS_DUE;				
        }

        return AssignmentConstants.STATUS_OPEN;	
    }

    private void populateAssignmentForAttachmentAndGroupSets(Set<ModelAnswerAttachment> modelAnswerAttachmentSet, Set<AssignmentAttachment> attachSet, Set<AssignmentGroup> groupSet, Assignment2 assign) {
        if (modelAnswerAttachmentSet != null && !modelAnswerAttachmentSet.isEmpty()) {
            for (ModelAnswerAttachment attach : modelAnswerAttachmentSet) {
                if (attach != null) {
                    attach.setAssignment(assign);
                }
            }
        }
        if (attachSet != null && !attachSet.isEmpty()) {
            for (AssignmentAttachment attach : attachSet) {
                if (attach != null) {
                    attach.setAssignment(assign);
                }
            }
        }
        if (groupSet != null && !groupSet.isEmpty()) {
            for (AssignmentGroup group : groupSet) {
                if (group != null) {
                    group.setAssignment(assign);
                }
            }
        }
    }

    private Set identifyAttachmentsToDelete(Set existingAttachments, Set updatedAttachments) {
        Set attachToRemove = new HashSet();

        // make a set of attachment references in case the id wasn't populated
        // properly
        Set<String> updatedAttachSetRefs = new HashSet<String>();
        if (updatedAttachments != null) {
            for (Object next : updatedAttachments) {
                AssignmentAttachmentBase attach = (AssignmentAttachmentBase) next;
                updatedAttachSetRefs.add(attach.getAttachmentReference());
            }
        }

        if (updatedAttachments != null && existingAttachments != null) {
            for (Object next : existingAttachments) {
            //for (AssignmentAttachment attach : existingAssign.getAttachmentSet()) {
                AssignmentAttachmentBase attach = (AssignmentAttachmentBase) next;
                if (attach != null) {
                    if (!updatedAttachSetRefs.contains(attach.getAttachmentReference())) {
                        // we need to delete this attachment
                        attachToRemove.add(attach);
                        if (log.isDebugEnabled()) log.debug("Attach to remove: " + attach.getAttachmentReference());
                    } 
                }
            }
        }

        return attachToRemove;
    }
    /**
     * 
     * @param existingAssignment
     * @param updatedAssignment
     * @return a set of attachments associated with the updatedAssignment that do not
     * currently exist. this is determined by checking to see if there is
     * already an attachment in the given existingAssignment attachment set with the same
     * attachmentReference as each attachment in the updatedAssignment. 
     */
    private Set identifyAttachmentsToCreate(
            Set existingAttachments, Set updatedAttachments)
            {
        Set attachToCreate = new HashSet();

        // make a set of attachment references in case the id wasn't populated
        // properly
        Set<String> existingAttachSetRefs = new HashSet<String>();
        if (existingAttachments != null) {
            for (Object next : existingAttachments) {
                AssignmentAttachmentBase attach = (AssignmentAttachmentBase) next;
                existingAttachSetRefs.add(attach.getAttachmentReference());
            }
        }

        if (updatedAttachments != null) {
            for (Object next : updatedAttachments) {
                AssignmentAttachmentBase attach = (AssignmentAttachmentBase) next; 
                if (attach != null) {
                    if (!existingAttachSetRefs.contains(attach.getAttachmentReference())) {
                        attachToCreate.add(attach);
                    }
                }
            }
        }

        return attachToCreate;
    }

    private Set<AssignmentGroup> identifyGroupsToDelete(Assignment2 existingAssign, Assignment2 updatedAssign) {
        Set<AssignmentGroup> groupsToRemove = new HashSet<AssignmentGroup>();

        if (updatedAssign != null && existingAssign != null && existingAssign.getAssignmentGroupSet() != null) {
            for (AssignmentGroup group : existingAssign.getAssignmentGroupSet()) {
                if (group != null) {
                    if (updatedAssign.getAssignmentGroupSet() == null ||
                            !updatedAssign.getAssignmentGroupSet().contains(group)) {
                        // we need to delete this group
                        groupsToRemove.add(group);
                        if (log.isDebugEnabled()) log.debug("Group to remove: " + group.getGroupId());
                    } 
                }
            }
        }

        return groupsToRemove;
    }

    /**
     * Given the originalAssignment and the updated (or newly created) version, will determine if an
     * announcement needs to be added, updated, or deleted. Announcements are updated
     * if there is a change in title, open date, or group restrictions. They are
     * deleted if the assignment is changed to draft status. Does not re-check permissions, so
     * make sure you are authorized to update assignments if you call this method.
     * @param originalAssignmentWithGroups - original assignment with the group info populated
     * @param updatedAssignment - updated (or newly created) assignment with the group info populated
     */
    private void saveAssignmentAnnouncement(Assignment2 originalAssignment, Assignment2 updatedAssignment) {
        if (updatedAssignment == null) {
            throw new IllegalArgumentException("Null updatedAssignment passed to saveAssignmentAnnouncement");
        }

        if (updatedAssignment.getId() == null) {
            throw new IllegalArgumentException("The updatedAssignment passed to saveAssignmentAnnouncement must have an id");
        }

        // make the open date locale-aware
        // use a date which is related to the current users locale
        DateFormat df = externalLogic.getDateFormat(null, DateFormat.FULL, bundleLogic.getLocale(), true);
        // create url to point back to this assignment to be included in the description
        // ASNN-477
        //String assignUrl = externalLogic.getAssignmentViewUrl(REDIRECT_ASSIGNMENT_VIEW_ID) + "/" + updatedAssignment.getId();
        String toolTitle = externalLogic.getToolTitle();
        String newAnncSubject = bundleLogic.getFormattedMessage("assignment2.assignment_annc_subject",
                new Object[] {toolTitle, updatedAssignment.getTitle()});
        String newAnncBody = bundleLogic.getFormattedMessage("assignment2.assignment_annc_body",
                new Object[] {updatedAssignment.getTitle(), df.format(updatedAssignment.getOpenDate()), toolTitle});
        String updAnncSubject = bundleLogic.getFormattedMessage("assignment2.assignment_annc_subject_edited",
                new Object[] {toolTitle, updatedAssignment.getTitle()});
        String updAnncBody = bundleLogic.getFormattedMessage("assignment2.assignment_annc_body_edited",
                new Object[] {updatedAssignment.getTitle(), df.format(updatedAssignment.getOpenDate()), toolTitle});

        if (originalAssignment == null) {
            // this was a new assignment
            // check to see if there will be an announcement for the open date
            if (updatedAssignment.getHasAnnouncement() && !updatedAssignment.isDraft()) {
                // add an announcement for the open date for this assignment
                String announcementId = announcementLogic.addOpenDateAnnouncement(
                        updatedAssignment.getListOfAssociatedGroupReferences(), 
                        updatedAssignment.getContextId(), newAnncSubject,
                        newAnncBody, updatedAssignment.getOpenDate());
                updatedAssignment.setAnnouncementId(announcementId);
                dao.update(updatedAssignment);
            }
        } else if (updatedAssignment.isDraft()) {
            if (updatedAssignment.getAnnouncementId() != null) {
                announcementLogic.deleteOpenDateAnnouncement(updatedAssignment.getAnnouncementId(), updatedAssignment.getContextId());
                updatedAssignment.setAnnouncementId(null);
                dao.update(updatedAssignment);
            }
        } else if (originalAssignment.getAnnouncementId() == null && updatedAssignment.getHasAnnouncement()) {
            // this is a new announcement
            String announcementId = announcementLogic.addOpenDateAnnouncement(updatedAssignment.getListOfAssociatedGroupReferences(), 
                    updatedAssignment.getContextId(), newAnncSubject, newAnncBody, updatedAssignment.getOpenDate());
            updatedAssignment.setAnnouncementId(announcementId);
            dao.update(updatedAssignment);
        } else if (originalAssignment.getAnnouncementId() != null && !updatedAssignment.getHasAnnouncement()) {
            // we must remove the original announcement
            announcementLogic.deleteOpenDateAnnouncement(updatedAssignment.getAnnouncementId(), updatedAssignment.getContextId());
            updatedAssignment.setAnnouncementId(null);
            dao.update(updatedAssignment);
        } else if (updatedAssignment.getHasAnnouncement()){
            // if title, open date, or group restrictions were updated, we need to update the announcement
            Date oldTime = (Date)originalAssignment.getOpenDate();
            Date newTime = updatedAssignment.getOpenDate();
            if (!originalAssignment.getTitle().equals(updatedAssignment.getTitle()) ||
                    (oldTime.after(newTime) || oldTime.before(newTime)) ||
                    !originalAssignment.getListOfAssociatedGroupReferences().equals(updatedAssignment.getListOfAssociatedGroupReferences())) {
                announcementLogic.updateOpenDateAnnouncement(updatedAssignment.getAnnouncementId(), 
                        updatedAssignment.getListOfAssociatedGroupReferences(), 
                        updatedAssignment.getContextId(), updAnncSubject, updAnncBody, updatedAssignment.getOpenDate());
                // don't need to re-save assignment b/c id already exists
            }
        }
    }

    /**
     * will handle the business logic and updates required to determine if an event
     * needs to be added, updated, or deleted from the Schedule (Calendar) tool.
     * Compares the existing assignment (if not null) to the new assignment to
     * carry out any actions that are required for the relationship with the
     * Schedule tool.  Events are updated upon a change in the due date, title, or
     * group restrictions for the assignment.  Events are deleted if the assignment
     * is deleted, changed to draft status, or the due date is removed.  will also
     * add event when appropriate. Does not re-check permissions, so
     * make sure you are authorized to update assignments if you call this method.
     * @param originalAssignment - null if "updatedAssignment" is newly created
     * @param updatedAssignment
     */
    private void handleDueDateEvent(Assignment2 originalAssignment, Assignment2 updatedAssignment) {
        if (updatedAssignment == null) {
            throw new IllegalArgumentException("Null updatedAssignment passed to saveDueDateEvent");
        }

        if (updatedAssignment.getId() == null) {
            throw new IllegalArgumentException("The updatedAssignment passed to " +
            "saveDueDateEvent must have an id");
        }


        String contextId = updatedAssignment.getContextId();

        // make the due date locale-aware
        // use a date which is related to the current users locale
        DateFormat df = externalLogic.getDateFormat(null, DateFormat.FULL, bundleLogic.getLocale(), true);
        // create url to point back to this assignment to be included in the description
        // ASNN-477
        // String assignUrl = externalLogic.getAssignmentViewUrl(REDIRECT_ASSIGNMENT_VIEW_ID) + "/" + updatedAssignment.getId();

        String eventTitle = "";
        String eventDescription = "";
        if (updatedAssignment.getDueDate() != null) {
            String toolTitle = externalLogic.getToolTitle();
            eventTitle = bundleLogic.getFormattedMessage("assignment2.schedule_event_title",
                    new Object[] {toolTitle, updatedAssignment.getTitle()});
            eventDescription = bundleLogic.getFormattedMessage("assignment2.schedule_event_description",
                    new Object[] {updatedAssignment.getTitle(), df.format(updatedAssignment.getDueDate()), toolTitle});
        }

        if (originalAssignment == null) {
            // this was a new assignment
            // check to see if there will be an event added for the due date
            if (updatedAssignment.getAddedToSchedule() && !updatedAssignment.isDraft() &&
                    updatedAssignment.getDueDate() != null) {
                // add an event for the due date for this assignment
                String eventId = calendarLogic.addDueDateToSchedule(updatedAssignment.getListOfAssociatedGroupReferences(), 
                        contextId, eventTitle, eventDescription, updatedAssignment.getDueDate(), updatedAssignment.getId());
                updatedAssignment.setEventId(eventId);
                dao.update(updatedAssignment);
            }
        } else if (updatedAssignment.isDraft()) {
            if (updatedAssignment.getEventId() != null) {
                calendarLogic.deleteDueDateEvent(updatedAssignment.getEventId(), contextId);
                updatedAssignment.setEventId(null);
                dao.update(updatedAssignment);
            }
        } else if (originalAssignment.getEventId() == null && updatedAssignment.getAddedToSchedule()) {
            // this is a new event
            String eventIdId = calendarLogic.addDueDateToSchedule(updatedAssignment.getListOfAssociatedGroupReferences(),
                    contextId, eventTitle, eventDescription, updatedAssignment.getDueDate(), updatedAssignment.getId());
            updatedAssignment.setEventId(eventIdId);
            dao.update(updatedAssignment);
        } else if (originalAssignment.getEventId() != null && !updatedAssignment.getAddedToSchedule()) {
            // we must remove the original event
            calendarLogic.deleteDueDateEvent(originalAssignment.getEventId(), contextId);
            updatedAssignment.setEventId(null);
            dao.update(updatedAssignment);
        } else if (updatedAssignment.getAddedToSchedule()){
            // if title, due date, or group restrictions were updated, we need to update the event
            Date oldDueDate = originalAssignment.getDueDate();
            Date newDueDate = updatedAssignment.getDueDate();

            if (oldDueDate != null && newDueDate == null) {
                // we need to remove this event because no longer has a due date
                calendarLogic.deleteDueDateEvent(originalAssignment.getEventId(), contextId);
                updatedAssignment.setEventId(null);
                dao.update(updatedAssignment);

            } else if (!originalAssignment.getTitle().equals(updatedAssignment.getTitle()) ||
                    (oldDueDate.after(newDueDate) || oldDueDate.before(newDueDate)) ||
                    !originalAssignment.getListOfAssociatedGroupReferences().equals(updatedAssignment.getListOfAssociatedGroupReferences())) {
                // otherwise, we update only if there is a change in the assignment title, due date,
                // or group restrictions
                calendarLogic.updateDueDateEvent(updatedAssignment.getEventId(), 
                        updatedAssignment.getListOfAssociatedGroupReferences(),
                        contextId, eventTitle, eventDescription, updatedAssignment.getDueDate(), 
                        updatedAssignment.getId());
                // don't need to re-save assignment b/c id already exists
            }
        }
    }

    /**
     * ensure that the attachments and groups are populated with everything
     * required for saving
     * @param attachSet
     * @param groupSet
     * @throws IllegalArgumentException if any group or attachment is invalid
     */
    private void validateAttachmentsAndGroups(Set<ModelAnswerAttachment> modelAnswerAttachSet, Set<AssignmentAttachment> attachSet, Set<AssignmentGroup> groupSet) {
        
        if (modelAnswerAttachSet != null) {
            for (ModelAnswerAttachment attach : modelAnswerAttachSet) {
                if (!attach.isAttachmentValid()) {
                    throw new IllegalArgumentException("At least one attachment associated " +
                            "with the model view answer data is missing necessary data. Check to see " +
                    "if your attachmentReference is populated.");
                }
            }

        }
        
        // ensure that the necessary data was populated for the attachments
        if (attachSet != null) {
            for (AssignmentAttachment attach : attachSet) {
                if (!attach.isAttachmentValid()) {
                    throw new IllegalArgumentException("At least one attachment associated " +
                            "with the assignment is missing necessary data. Check to see " +
                    "if your attachmentReference is populated.");
                }
            }

        }

        // ensure group info required for saving was populated
        if (groupSet != null) {
            for (AssignmentGroup group : groupSet) {
                if (!group.isAssignmentGroupValid()) {
                    throw new IllegalArgumentException("At least one AssignmentGroup " +
                            "associated with this assignment is not valid. Check to " +
                    "see if all required info is populated.");
                }
            }
        }
    }

    public String getDuplicatedAssignmentTitle(String contextId, String titleToDuplicate) {
        if (contextId == null || titleToDuplicate == null) {
            throw new IllegalArgumentException("Null contextId or titleToDuplicate passed to getDuplicatedAssignmentTitle." 
                    + " contextId:" + contextId + " titleToDuplicate:" + titleToDuplicate);
        }
        // first, get all of the existing assignment titles
        Search search = new Search(new String[] {"contextId", "removed"}, new Object[] {contextId, false});
        List<Assignment2> allAssigns = dao.findBySearch(Assignment2.class, search);
        List<String> existingAssignTitles = new ArrayList<String>();
        if (allAssigns != null) {
            for (Assignment2 assign : allAssigns) {
                existingAssignTitles.add(assign.getTitle());
            }
        }

        String duplicatedTitle = Assignment2Utils.getVersionedString(titleToDuplicate);
        while (existingAssignTitles.contains(duplicatedTitle)) {
            duplicatedTitle = Assignment2Utils.getVersionedString(duplicatedTitle);
        }

        return duplicatedTitle;
    }
    
    public AssignmentDefinition getAssignmentDefinition(Assignment2 assignment, Map<Long, GradebookItem> gbIdItemMap,
            Map<String, String> groupIdToTitleMap) {
        if (assignment == null) {
            throw new IllegalArgumentException("Null assignment passed to getAssignmentDefinition");
        }

        AssignmentDefinition assignDef = new AssignmentDefinition();
        assignDef.setId(assignment.getId());
        assignDef.setAcceptUntilDate(assignment.getAcceptUntilDate());
        assignDef.setDraft(assignment.isDraft());
        assignDef.setDueDate(assignment.getDueDate());
        assignDef.setHasAnnouncement(assignment.getHasAnnouncement());
        assignDef.setHonorPledge(assignment.isHonorPledge());
        assignDef.setInstructions(assignment.getInstructions());
        assignDef.setSendSubmissionNotifications(assignment.isSendSubmissionNotifications());
        assignDef.setNumSubmissionsAllowed(assignment.getNumSubmissionsAllowed());
        assignDef.setOpenDate(assignment.getOpenDate());
        assignDef.setSortIndex(assignment.getSortIndex());
        assignDef.setSubmissionType(assignment.getSubmissionType());
        assignDef.setTitle(assignment.getTitle());
        assignDef.setGraded(assignment.isGraded());
        assignDef.setRequiresSubmission(assignment.isRequiresSubmission());
        assignDef.setContentReviewEnabled(assignment.isContentReviewEnabled());
        assignDef.setContentReviewStudentViewReport(assignment.isContentReviewStudentViewReport());

        // if it is graded, we need to retrieve the name of the associated gb item
        if (assignment.isGraded() && assignment.getGradebookItemId() != null &&
                gbIdItemMap != null) {
            GradebookItem gbItem = (GradebookItem)gbIdItemMap.get(assignment.getGradebookItemId());
            if (gbItem != null) {
                assignDef.setAssociatedGbItemName(gbItem.getTitle());
                assignDef.setAssociatedGbItemPtsPossible(gbItem.getPointsPossible());
            }
        }

        // we need to make a list of the attachment references
        List<String> attachRefList = new ArrayList<String>();
        if (assignment.getAttachmentSet() != null) {
            for (AssignmentAttachment attach : assignment.getAttachmentSet()) {
                if (attach != null) {
                    attachRefList.add(attach.getAttachmentReference());
                }
            }
        }
        assignDef.setAttachmentReferences(attachRefList);

        // we need to make a list of the group names
        List<String> associatedGroupNames = new ArrayList<String>();
        if (assignment.getAssignmentGroupSet() != null && groupIdToTitleMap != null) {
            for (AssignmentGroup aGroup : assignment.getAssignmentGroupSet()) {
                if (aGroup != null) {
                    String groupName = (String)groupIdToTitleMap.get(aGroup.getGroupId());
                    if (groupName != null) {
                        associatedGroupNames.add(groupName);
                    }
                }
            }
        }
        assignDef.setGroupRestrictionGroupTitles(associatedGroupNames);

        assignDef.setProperties(assignment.getProperties());

        return assignDef;
    }

    @Override
    public List<Assignment2> getAssignmentsWithLinkedGradebookItemId(Long id) {
        // TODO Permissions I'm assuming at the moment, that if someone had
        // permissions to update the gradebook, they can update the assignment
        // too, but that may not be true...
        return dao.getAssignmentsWithLinkedGradebookItemId(id);
    }

}
