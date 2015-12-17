package org.sakaiproject.assignment2.logic;

import java.util.List;
import java.util.Map;

import org.sakaiproject.taggable.api.TaggableActivityProducer;
import org.sakaiproject.taggable.api.TaggingHelperInfo;
import org.sakaiproject.taggable.api.TaggingProvider;

/**
 * This is the interface for the logic responsible 
 * for interacting with some taggable helpers.
 * @author chrismaurer
 *
 */
public interface ExternalTaggableLogic {

	/** Bean id to get to the generic tagging manager */
	public static String TAGGING_MANAGER_ID = "org.sakaiproject.taggable.api.TaggingManager";

	/** Bean id to get to our activity producer */
	public static String ACTIVITY_PRODUCER_ID = "org.sakaiproject.assignment2.taggable.api.AssignmentActivityProducer";

	/** 
	 * Is Taggable enabled for the system
	 * @return
	 */
	public boolean isTaggable();

	/**
	 * Is this context even allowed to do anything?
	 * @param context Site Id
	 * @return
	 */
	public boolean isSiteAssociated(String context);
	
	/**
	 * Is the producer enabled?
	 * @return
	 */
	public boolean isProducerEnabled();

	/**
	 * Gets the producer indicated by the bean id of {@link #ACTIVITY_PRODUCER_ID}
	 * @return TaggableActivityProducer
	 */
	public TaggableActivityProducer getMyProducer();

	/**
	 * Gets the list of providers
	 * @return List of TaggingProvider objects
	 */
	public List<TaggingProvider> getProviders();


	/**
	 * Return a TaggingHelperInfo object for an assignment reference
	 * @param activityRef Activity's (assignment) reference
	 * @return
	 */
	public List<TaggingHelperInfo> getActivityHelperInfo(String activityRef);

	/**
	 * Return a map where the key is the activity ref (assignment) and the value 
	 * is a list of TaggingHelperInfo objects (one from each provider)
	 * @param siteId Site id that all of the activity refs come from
	 * @param activityRefs List of Activity (assignment) references
	 * @return
	 */
	public Map<String, List<TaggingHelperInfo>> getActivityHelperInfo(String siteId, List<String> activityRefs);
	
	/**
	 * Determine if the user is able to retrieve activity information
	 * @param activityRef Reference of the activity (assignment)
	 * @param currentUser Id of the current user
	 * @param taggedItem Reference of the item that will help determine the permissions
	 * @return
	 */
	public boolean canGetActivity(String activityRef, String currentUser, String taggedItem);
	
	/**
	 * Determine if the user is able to retrieve item information (assignment submissions)
	 * @param activityRef Reference of the activity (assignment)
	 * @param itemRef Reference of the item (submission)
	 * @param currentUser Id of the current user
	 * @param taggedItem Reference of the item that will help determine the permissions
	 * @return
	 */
	public boolean canGetItem(String activityRef, String itemRef, String currentUser, String taggedItem);
}
