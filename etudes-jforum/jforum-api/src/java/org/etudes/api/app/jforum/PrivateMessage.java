/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/PrivateMessage.java $ 
 * $Id: PrivateMessage.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011 Etudes, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 ***********************************************************************************/
package org.etudes.api.app.jforum;

public interface PrivateMessage
{
	// TODO remove after updating references to enum PrivateMessageType
	public static final int TYPE_READ = 0;
	public static final int TYPE_NEW = 1;
	public static final int TYPE_SENT = 2;
	public static final int TYPE_SAVED_IN = 3;
	public static final int TYPE_SAVED_OUT = 4;
	public static final int TYPE_UNREAD = 5;
	
	public static final int PRIORITY_GENERAL = 0;
	public static final int PRIORITY_HIGH = 1;
	
	public static final String MAIL_NEW_PM_SUBJECT = "Private Message";
	
	public enum PrivateMessageType
	{
		READ(0), NEW(1), SENT(2), SAVED_IN(3), SAVED_OUT(4), UNREAD(5);
		
		private final int type;
		
		PrivateMessageType(int type)
		{
			this.type = type;
		}
		
		final public int getType()
		{
			return type;
		}
	}
	
	public enum PrivateMessagePriority
	{
		GENERAL(0), HIGH(1);
		
		private final int priority;
		
		PrivateMessagePriority(int priority)
		{
			this.priority = priority;
		}
		
		final public int getPriority()
		{
			return priority;
		}
	}
	
	/**
	 * @return the id
	 */
	public int getId();
	
	/**
	 * @param id the id to set
	 */
	//public void setId(int id);
	
	/**
	 * @return the type
	 */
	public int getType();
	
	/**
	 * @param type the type to set
	 */
	public void setType(int type);
	
	/**
	 * @return the fromUser
	 */
	public User getFromUser();
	
	/**
	 * @param fromUser the fromUser to set
	 */
	public void setFromUser(User fromUser);
	
	/**
	 * @return the toUser
	 */
	public User getToUser();
	
	/**
	 * @param toUser the toUser to set
	 */
	public void setToUser(User toUser);
	
	/**
	 * @return the post
	 */
	public Post getPost();
	
	/**
	 * @param post the post to set
	 */
	public void setPost(Post post);
	
	/**
	 * @return the hasAttachments
	 */
	public boolean isHasAttachments();
	
	/**
	 * @param hasAttachments the hasAttachments to set
	 */
	public void setHasAttachments(boolean hasAttachments);
	
	/**
	 * @return the flagToFollowup
	 */
	public boolean isFlagToFollowup();
	
	/**
	 * @param flagToFollowup the flagToFollowup to set
	 */
	public void setFlagToFollowup(boolean flagToFollowup);
	
	/**
	 * @return the replied
	 */
	public boolean isReplied();
	
	/**
	 * @param replied the replied to set
	 */
	public void setReplied(boolean replied);
	
	/**
	 * @return the priority
	 */
	public int getPriority();
	
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority);
	
	/**
	 * Sets the context
	 * 
	 * @param context	The context to set
	 */
	void setContext(String context);
	
	/**
	 * Gets the category context
	 * 
	 * @return	The category's context
	 */
	String getContext();
}
