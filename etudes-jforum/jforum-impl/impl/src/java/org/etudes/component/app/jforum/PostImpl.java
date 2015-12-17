/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/PostImpl.java $ 
 * $Id: PostImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011, 2012 Etudes, Inc. 
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;

public class PostImpl implements Post
{
	private Boolean htmlEnabled = Boolean.TRUE;
	
	protected List<Attachment> attachments = new ArrayList<Attachment>();
	
	protected Boolean bbCodeEnabled = Boolean.TRUE;
	
	protected Boolean canEdit = Boolean.FALSE;
	
	protected int editCount;
	
	protected Date editTime;
	
	protected int forumId;
	
	protected Boolean hasAttachments = Boolean.FALSE;
	
	protected int id;
	
	protected User postedBy;
	
	protected String rawText;
	
	protected Boolean signatureEnabled = Boolean.TRUE;
	
	protected Boolean smiliesEnabled = Boolean.FALSE;
	
	protected String subject;
	
	protected String text;	
	
	protected Date time = null;
	
	protected Topic topic = null;
	
	protected int topicId;
	
	protected int userId;
	
	protected String userIp;
	
	protected Date userLatestPostTime = null;
	
	transient JForumPostService jforumPostService = null;
	
	public PostImpl()
	{		
	}

	public PostImpl(JForumPostService jforumPostService)
	{		
		this.jforumPostService = jforumPostService;
	}
	
	protected PostImpl(int id, String subject, String text)
	{
		this.id = id;
		this.subject = subject;
		this.text = text;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		// two posts are equals if they have the same id
		return ((obj instanceof Post) && (((Post)obj).getId() == this.id));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Attachment> getAttachments()
	{
		return attachments;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getEditCount()
	{
		return editCount;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getEditTime()
	{
		return editTime;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getForumId()
	{
		return forumId;
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
	public User getPostedBy()
	{
		return postedBy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getRawText()
	{
		return rawText;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getSubject()
	{
		return subject;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Date getTime()
	{
		return time;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Topic getTopic()
	{
		if (topic != null)
		{
			return topic;
		}
		
		if (this.jforumPostService != null && getTopicId() > 0)
		{
			Topic topic = this.jforumPostService.getTopic(getTopicId());
			
			setTopic(topic);
			
			return topic;
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTopicId()
	{
		return topicId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getUserId()
	{
		return userId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getUserIp()
	{
		return userIp;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getUserLatestPostTime()
	{
		return userLatestPostTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean hasAttachments()
	{
		return hasAttachments;
	}
	
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isBbCodeEnabled()
	{
		return bbCodeEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isCanEdit()
	{
		return canEdit;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isHtmlEnabled()
	{
		return htmlEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isSignatureEnabled()
	{
		return signatureEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isSmiliesEnabled()
	{
		return smiliesEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAttachments(List<Attachment> attachments)
	{
		this.attachments = attachments;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBbCodeEnabled(Boolean bbCodeEnabled)
	{
		this.bbCodeEnabled = bbCodeEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCanEdit(Boolean canEdit)
	{
		this.canEdit = canEdit;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditCount(int editCount)
	{
		this.editCount = editCount;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditTime(Date editTime)
	{
		this.editTime = editTime;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setForumId(int forumId)
	{
		this.forumId = forumId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHasAttachments(Boolean hasAttachments)
	{
		this.hasAttachments = hasAttachments;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHtmlEnabled(Boolean htmlEnabled)
	{
		this.htmlEnabled = htmlEnabled;
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
	public void setPostedBy(User postedBy)
	{
		this.postedBy = postedBy;
	}

	/**
	 * sets raw text of the post
	 * 
	 * @param rawText	Raw text of the post
	 * 
	 * @return	The raw text of the post
	 */
	public void setRawText(String rawText)
	{
		this.rawText = rawText;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSignatureEnabled(Boolean signatureEnabled)
	{
		this.signatureEnabled = signatureEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSmiliesEnabled(Boolean smiliesEnabled)
	{
		this.smiliesEnabled = smiliesEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTime(Date time)
	{
		this.time = time;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(Topic topic)
	{
		this.topic = topic;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTopicId(int topicId)
	{
		this.topicId = topicId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUserIp(String userIp)
	{
		this.userIp = userIp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUserLatestPostTime(Date userLatestPostTime)
	{
		this.userLatestPostTime = userLatestPostTime;
	}
}
