/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/PrivateMessageImpl.java $ 
 * $Id: PrivateMessageImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum;

import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.PrivateMessage;
import org.etudes.api.app.jforum.User;


public class PrivateMessageImpl implements PrivateMessage
{
	protected int id;
	protected int type;
	protected User fromUser;
	protected User toUser;
	protected Post post;
	protected boolean hasAttachments;
	protected boolean flagToFollowup;
	protected boolean replied;
	protected int priority;
	protected String context = null;
	
	public PrivateMessageImpl()
	{		
	}

		
	protected PrivateMessageImpl(int id, String context, User fromUser, User toUser)
	{
		this.id = id;
		this.context = context;
		this.fromUser = fromUser;
		this.toUser = toUser;
		
		this.setPost(new PostImpl());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User getFromUser()
	{
		return fromUser;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setFromUser(User fromUser)
	{
		this.fromUser = fromUser;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User getToUser()
	{
		return toUser;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToUser(User toUser)
	{
		this.toUser = toUser;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Post getPost()
	{
		return post;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setPost(Post post)
	{
		this.post = post;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isHasAttachments()
	{
		return hasAttachments;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setHasAttachments(boolean hasAttachments)
	{
		this.hasAttachments = hasAttachments;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isFlagToFollowup()
	{
		return flagToFollowup;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setFlagToFollowup(boolean flagToFollowup)
	{
		this.flagToFollowup = flagToFollowup;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isReplied()
	{
		return replied;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setReplied(boolean replied)
	{
		this.replied = replied;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getPriority()
	{
		return priority;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setPriority(int priority)
	{
		this.priority = priority;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getContext()
	{
		return this.context;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setContext(String context)
	{
		this.context = context;
	}
}
