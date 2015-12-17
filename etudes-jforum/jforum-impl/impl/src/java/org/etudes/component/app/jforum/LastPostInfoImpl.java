/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/LastPostInfoImpl.java $ 
 * $Id: LastPostInfoImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.util.Date;

import org.etudes.api.app.jforum.LastPostInfo;

public class LastPostInfoImpl implements LastPostInfo
{
	protected int topicId;
	
	protected int postId;
	
	protected int userId;
	
	protected String sakaiUserId;
	
	protected int topicReplies;
	
	protected Date postDate;
	
	protected String firstName;
	
	protected String lastName;
	
	protected long postTimeMillis;
	
	protected LastPostInfoImpl(LastPostInfoImpl other)
	{
		this.topicId = other.topicId;
		this.postId = other.postId;
		this.userId = other.userId;
		this.topicReplies = other.topicReplies;
		if (other.postDate != null)
		{
			this.postDate = new Date(other.postDate.getTime());
		}
		this.firstName = other.firstName;
		this.lastName = other.lastName;
		this.postTimeMillis = other.postTimeMillis;
	}
	
	public LastPostInfoImpl(){}

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
	public void setTopicId(int topicId)
	{
		this.topicId = topicId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPostId()
	{
		return postId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPostId(int postId)
	{
		this.postId = postId;
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
	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSakaiUserId()
	{
		return sakaiUserId;
	}

	/**
	 * @param sakaiUserId the sakaiUserId to set
	 */
	public void setSakaiUserId(String sakaiUserId)
	{
		this.sakaiUserId = sakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTopicReplies()
	{
		return topicReplies;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTopicReplies(int topicReplies)
	{
		this.topicReplies = topicReplies;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getPostDate()
	{
		return postDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPostDate(Date postDate)
	{
		this.postDate = postDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getPostTimeMillis()
	{
		return postTimeMillis;
	}

	/**
	 * @param postTimeMillis the postTimeMillis to set
	 */
	public void setPostTimeMillis(long postTimeMillis)
	{
		this.postTimeMillis = postTimeMillis;
	}
}

