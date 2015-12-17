/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/taggable/impl/AssignmentActivityProducerImpl.java $
 * $Id: AssignmentActivityProducerImpl.java 86090 2014-04-14 18:15:43Z dsobiera@indiana.edu $
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

package org.sakaiproject.assignment2.taggable.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.taggable.api.TaggableActivity;
import org.sakaiproject.taggable.api.TaggableItem;
import org.sakaiproject.taggable.api.TaggingManager;
import org.sakaiproject.taggable.api.TaggingProvider;
import org.sakaiproject.assignment2.dao.AssignmentDao;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.SubmissionNotFoundException;
import org.sakaiproject.assignment2.logic.AssignmentBundleLogic;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.taggable.api.AssignmentActivityProducer;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.Reference;

public class AssignmentActivityProducerImpl implements
AssignmentActivityProducer {

    private static final Log logger = LogFactory
    .getLog(AssignmentActivityProducerImpl.class);

    protected AssignmentBundleLogic assignmentBundleLogic;

    protected AssignmentDao assignmentDao;

    protected EntityManager entityManager;

    protected TaggingManager taggingManager;

    protected AssignmentPermissionLogic assignmentPermissionLogic;

    protected AssignmentSubmissionLogic assignmentSubmissionLogic;
    
    protected ExternalLogic externalLogic;
    
    protected AssignmentLogic assignmentLogic;


    public boolean allowGetItems(TaggableActivity activity,
            TaggingProvider provider) {
        // We aren't picky about the provider, so ignore that argument.
        // Only allow this if the user can grade submissions
        //return assignmentDao.allowGradeSubmission(activity.getReference());
        //return assignmentPermissionLogic.isUserAbleToProvideFeedbackForSubmission(submissionId);
        Assignment2 assignment = (Assignment2) activity.getObject();
        return assignmentPermissionLogic.isUserAllowedToManageSubmissionsForAssignment(null, assignment);
    }

    public boolean allowRemoveTags(TaggableActivity activity) {
        Assignment2 assignment = (Assignment2) activity.getObject();
        return assignmentPermissionLogic.isUserAllowedToDeleteAssignment(null, assignment);
    }

    public boolean allowRemoveTags(TaggableItem item) {
        AssignmentSubmission subm = (AssignmentSubmission)item.getObject();
        return assignmentPermissionLogic.isUserAllowedToManageSubmission(null, subm.getId());
    }

    public boolean allowTransferCopyTags(TaggableActivity activity) {
        return assignmentPermissionLogic.isUserAllowedToUpdateSite(activity.getContext());
    }

    public boolean checkReference(String ref) {
        return ref.startsWith(Entity.SEPARATOR + AssignmentConstants.REFERENCE_ROOT);
    }

    public List<TaggableActivity> getActivities(String context,
            TaggingProvider provider) {
        // We aren't picky about the provider, so ignore that argument.
        List<TaggableActivity> activities = new ArrayList<TaggableActivity>();
        List<Assignment2> assignments = assignmentLogic.getViewableAssignments(context);
        for (Assignment2 assignment : assignments) {
            activities.add(getActivity(assignment));
        }
        return activities;
    }

    public TaggableActivity getActivity(Assignment2 assignment) {
        return new AssignmentActivityImpl(assignment, this);
    }

    public TaggableActivity getActivity(String activityRef,
            TaggingProvider provider) {
        // We aren't picky about the provider, so ignore that argument.
        TaggableActivity activity = null;
        if (checkReference(activityRef)) {
            Reference ref = entityManager.newReference(activityRef);
            try {
                Assignment2 assignment = assignmentLogic.getAssignmentByIdWithAssociatedData(Long.valueOf(ref.getId()), null);
                activity = new AssignmentActivityImpl(assignment, this);
            } catch (AssignmentNotFoundException anfe) {
                logger.warn("No assignment found for activityRef: " + activityRef);
            } catch (SecurityException se) {
                logger.warn("User attempted to access assignment2 activity without permission: " + activityRef);
            }
        }
        return activity;
    }

    public String getContext(String ref) {
        return entityManager.newReference(ref).getContext();
    }

    public String getId() {
        return PRODUCER_ID;
    }

    public TaggableItem getItem(AssignmentSubmission assignmentSubmission,
            String userId) {
        return new AssignmentItemImpl(assignmentSubmission, userId,
                new AssignmentActivityImpl(
                        assignmentSubmission.getAssignment(), this));
    }

    public String getName() {
        return assignmentBundleLogic.getString("service_name");
    }

    public void init() {
        logger.info("init()");
        if (ServerConfigurationService.getBoolean(PRODUCER_ENABLED_KEY, true)) {
        	logger.info("Enabling AssignmentActivityProducerImpl");
        	taggingManager.registerProducer(this);
        }
    }

    protected String parseAuthor(String itemRef) {
        return itemRef.split(AssignmentItemImpl.ITEM_REF_SEPARATOR)[1];
    }

    protected Long parseSubmissionRef(String itemRef) {
        return Long.valueOf(itemRef.split(AssignmentItemImpl.ITEM_REF_SEPARATOR)[0]);
    }

    public void setAssignmentDao(AssignmentDao assignmentDao)
    {
        this.assignmentDao = assignmentDao;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setTaggingManager(TaggingManager taggingManager) {
        this.taggingManager = taggingManager;
    }

    public void setAssignmentPermissionLogic(
            AssignmentPermissionLogic assignmentPermissionLogic)
    {
        this.assignmentPermissionLogic = assignmentPermissionLogic;
    }

    public void setAssignmentSubmissionLogic(
			AssignmentSubmissionLogic assignmentSubmissionLogic) {
		this.assignmentSubmissionLogic = assignmentSubmissionLogic;
	}

	public void setAssignmentBundleLogic(AssignmentBundleLogic assignmentBundleLogic) {
        this.assignmentBundleLogic = assignmentBundleLogic;
    }
	
	public void setExternalLogic(ExternalLogic externalLogic) {
	    this.externalLogic = externalLogic;
	}
	
	public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    public boolean allowGetItems(TaggableActivity activity, TaggingProvider provider, boolean getMyItemsOnly, String taggedItem)
    {
    	// We aren't picky about the provider, so ignore that argument.
        // Only allow this if the user can grade submissions
        //return assignmentDao.allowGradeSubmission(activity.getReference());
        //return assignmentPermissionLogic.isUserAbleToProvideFeedbackForSubmission(submissionId);
        Assignment2 assignment = (Assignment2) activity.getObject();
        return assignmentPermissionLogic.isUserAllowedToManageSubmissionsForAssignment(null, assignment);
    }

    public TaggableItem getItem(String itemRef, TaggingProvider provider, boolean getMyItemsOnly, String taggedItem)
    {
        TaggableItem item = null;
        if (checkReference(itemRef)) {
            
            try {
                // passing along the taggedItem allows assignment2 to check for extended privileges
                Map<String, Object> optionalParams = new HashMap<String, Object>();
                optionalParams.put(AssignmentConstants.TAGGABLE_REF_KEY, taggedItem);
                AssignmentSubmission submission = assignmentSubmissionLogic.getAssignmentSubmissionById(parseSubmissionRef(itemRef), optionalParams);
                item = new AssignmentItemImpl(submission, parseAuthor(itemRef),
                        new AssignmentActivityImpl(submission.getAssignment(),
                                this));
            } catch (SecurityException se) {
                logger.warn("Attempt to retrieve assignment submission " + itemRef + 
                " via AssignmentActivityProducer.getItem without permission");
                item = null;
            } catch (SubmissionNotFoundException snfe) {
                logger.warn("Attempt to retrieve assignment submission " + itemRef + 
                " via AssignmentActivityProducer.getItem but submission does not exist");
                item = null;
            }
        	/*boolean allowed = provider.allowGetItem(submission.getAssignment().getReference(), 
        			itemRef, externalLogic.getCurrentUserId(), taggedItem);
        	if (allowed) {
        		item = new AssignmentItemImpl(submission, parseAuthor(itemRef),
        				new AssignmentActivityImpl(submission.getAssignment(),
        						this));
        	}*/
        }
        return item;
    }

    public String getItemPermissionOverride()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public List<TaggableItem> getItems(TaggableActivity activity, TaggingProvider provider,
          boolean getMyItemsOnly, String taggedItem)
    {
       return getItems(activity, provider, getMyItemsOnly, taggedItem, true);
    }

    private List<TaggableItem> getItems(TaggableActivity activity, TaggingProvider provider,
            boolean getMyItemsOnly, String taggedItem, boolean checkPerms)
    {
    	// We aren't picky about the provider, so ignore that argument.
        List<TaggableItem> items = new ArrayList<TaggableItem>();
        Assignment2 assignment = (Assignment2) activity.getObject();
        /*
         * If you're not allowed to grade submissions, you shouldn't be able to
         * look at submission items. It seems that anybody is allowed to get any
         * submissions.
         */
        boolean allowed = false;
        if (checkPerms) {
           allowed = provider.allowGetItems(activity.getReference(), new String[]{}, externalLogic.getCurrentUserId(), taggedItem);
        }
        else {
           allowed = true;
        }
        
        if (allowed) {
            for (Iterator<AssignmentSubmission> i = assignmentSubmissionLogic.getViewableSubmissionsForAssignmentId(assignment.getId(), null).iterator(); i.hasNext();) {
                AssignmentSubmission submission = i.next();
                // we only want the submissions with a status of "submitted"
                if (submission.getCurrentSubmissionVersion() != null && 
                        submission.getCurrentSubmissionVersion().isSubmitted()) {
                    items.add(new AssignmentItemImpl(submission, submission.getUserId(), activity));
                }
            }
        }
        return items;
    }

    public List<TaggableItem> getItems(TaggableActivity activity, String userId,
          TaggingProvider provider, boolean getMyItemsOnly, String taggedItem)
    {
       return getItems(activity, userId, provider, getMyItemsOnly, taggedItem, true);
    }
    
    private List<TaggableItem> getItems(TaggableActivity activity, String userId,
            TaggingProvider provider, boolean getMyItemsOnly, String taggedItem, boolean checkPerms)
    {
    	// We aren't picky about the provider, so ignore that argument.
        List<TaggableItem> returned = new ArrayList<TaggableItem>();
        Assignment2 assignment = (Assignment2) activity.getObject();
        
        boolean allowed = false;
        if (checkPerms) {
           allowed =provider.allowGetItems(activity.getReference(), new String[]{}, externalLogic.getCurrentUserId(), taggedItem);
        }
        else {
           allowed = true;
        }
        
        if (allowed) {
        	AssignmentSubmission submission = assignmentDao.getSubmissionWithVersionHistoryForStudentAndAssignment(userId, assignment);
        	// we only consider submissions with a status of "submitted"
        	if (submission != null) {
        	    if (submission.getSubmissionHistorySet() != null) {
        	        // filter out the versions that don't have a status of "submitted"
        	        Set<AssignmentSubmissionVersion> submittedVersions = new HashSet<AssignmentSubmissionVersion>();
        	        for (AssignmentSubmissionVersion ver : submission.getSubmissionHistorySet()) {
        	            if (ver.isSubmitted()) {
        	                submittedVersions.add(ver);
        	            }
        	        }

        	        // now set the current version to the most recent submitted version
        	        AssignmentSubmissionVersion currVersion = assignmentDao.getCurrentVersionFromHistory(submittedVersions);
        	        submission.setSubmissionHistorySet(submittedVersions);
        	        submission.setCurrentSubmissionVersion(currVersion);
        	    }

        	    TaggableItem item = new AssignmentItemImpl(submission, userId,
        	            activity);
        	    returned.add(item);
        	}
        }
        return returned;
    }
    
    public boolean hasSubmissions(TaggableActivity activity, TaggingProvider provider,
    		boolean getMyItemsOnly, String taggedItem)
    {
    	return hasSubmissions(activity);
    }

    public boolean hasSubmissions(TaggableActivity activity, String userId,
    		TaggingProvider provider, boolean getMyItemsOnly, String taggedItem)
    {
    	return hasSubmissions(activity);
    }

    /**
     * Figure out if there any student submissions for a specific activity
     * @param activity
     * @return
     */
    private boolean hasSubmissions(TaggableActivity activity) {
    	Set<String> studentIdList = assignmentPermissionLogic.getSubmittersInSite(activity.getContext());
    	int numSubmitters = assignmentDao.getNumStudentsWithASubmission((Assignment2)activity.getObject(), studentIdList);
    	return numSubmitters > 0;
    }
}
