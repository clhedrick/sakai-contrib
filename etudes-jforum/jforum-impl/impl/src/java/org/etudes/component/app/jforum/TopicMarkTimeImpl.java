/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/TopicMarkTimeImpl.java $ 
 * $Id: TopicMarkTimeImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import org.etudes.api.app.jforum.TopicMarkTime;

public class TopicMarkTimeImpl implements TopicMarkTime
{
	protected int topicId;
	
	protected int userId;
	
	protected Date markTime = null;
	
	protected boolean isRead = false;

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
	public Date getMarkTime()
	{
		return markTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMarkTime(Date markTime)
	{
		this.markTime = markTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRead()
	{
		return isRead;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRead(boolean isRead)
	{
		this.isRead = isRead;
	}
}
