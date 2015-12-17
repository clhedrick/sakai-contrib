package org.sakaiproject.assignment2.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.ExternalTaggableLogic;
import org.sakaiproject.assignment2.taggable.api.AssignmentActivityProducer;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.taggable.api.TaggableActivityProducer;
import org.sakaiproject.taggable.api.TaggingHelperInfo;
import org.sakaiproject.taggable.api.TaggingManager;
import org.sakaiproject.taggable.api.TaggingProvider;

/**
 * This class contains logic that is responsible for interacting with some 
 * taggable helpers.
 * @author chrismaurer
 *
 */
public class ExternalTaggableLogicImpl implements ExternalTaggableLogic {

    private static Log log = LogFactory.getLog(ExternalTaggableLogicImpl.class);
	private TaggingManager taggingManager = null;
	private TaggableActivityProducer activityProducer = null;

	public ExternalTaggableLogicImpl() {
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Place any code that should run when this class is initialized by spring here
     */
    public void init() {
        if (log.isDebugEnabled()) log.debug("init");
    }

	public boolean isTaggable() {
		return getTaggingManager().isTaggable();
	}
	
	public boolean isProducerEnabled() {
		return ServerConfigurationService.getBoolean(AssignmentActivityProducer.PRODUCER_ENABLED_KEY, true);
	}

	public TaggableActivityProducer getMyProducer() {
		if (activityProducer == null) {
			activityProducer = (TaggableActivityProducer) ComponentManager.get(
					ACTIVITY_PRODUCER_ID);
		}
		return activityProducer;		 
	}

	public List<TaggingProvider> getProviders() {
		return getTaggingManager().getProviders();
	}	

	private TaggingManager getTaggingManager() {
		if (taggingManager == null) {
			taggingManager = (TaggingManager) ComponentManager
			.get(TAGGING_MANAGER_ID);
		}
		return taggingManager;
	}

	public List<TaggingHelperInfo> getActivityHelperInfo(String activityRef) {
		List<TaggingHelperInfo> activityHelpers = new ArrayList<TaggingHelperInfo>();
		for (TaggingProvider provider : getProviders()) {
			TaggingHelperInfo helper = provider.getActivityHelperInfo(activityRef);
			if (helper != null)
			{
				activityHelpers.add(helper);
			}
		}
		return activityHelpers;
	}

	public Map<String, List<TaggingHelperInfo>> getActivityHelperInfo(String siteId, List<String> activityRefs) {
		Map<String, List<TaggingHelperInfo>> returnMap = new HashMap<String, List<TaggingHelperInfo>>();
		for (TaggingProvider provider : getProviders()) {
			Map<String, TaggingHelperInfo> providerMap = new HashMap<String, TaggingHelperInfo>();
			providerMap = provider.getActivityHelperInfo(siteId, activityRefs);
			for (String key : providerMap.keySet()) {
				returnMap.get(key).add(providerMap.get(key));
			}			
		}
		return returnMap;
	}

	public boolean isSiteAssociated(String context) {
		boolean isAllowed = false;
		for (TaggingProvider provider : getProviders()) {
			isAllowed = provider.allowViewTags(context);
			break;
		}
		return isAllowed;
	}
	
	public boolean canGetActivity(String activityRef, String currentUser, String taggedItem) {
		boolean canGetActivity = false;
		
		List<TaggingProvider> providers = getProviders();
		for (TaggingProvider provider : providers) {
			canGetActivity = provider.allowGetActivity(activityRef, currentUser, taggedItem);
			if (canGetActivity) 
				break;
		}
		
		return canGetActivity;
	}
	
	public boolean canGetItem(String activityRef, String itemRef, String currentUser, String taggedItem) {
		boolean canGetItem = false;
		List<TaggingProvider> providers = getProviders();
		for (TaggingProvider provider : providers) {
			canGetItem = provider.allowGetItem(activityRef, itemRef, currentUser, taggedItem);
			if (canGetItem) 
				break;
		}
		return canGetItem;
	}
}
