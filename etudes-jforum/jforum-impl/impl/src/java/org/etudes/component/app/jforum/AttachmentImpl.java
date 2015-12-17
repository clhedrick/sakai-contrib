/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/AttachmentImpl.java $ 
 * $Id: AttachmentImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.AttachmentInfo;

public class AttachmentImpl implements Attachment
{
	protected int id;
	
	protected AttachmentInfo info;
	
	protected int postId;
	
	protected int privmsgsId;
	
	protected int userId;
	
	
	protected AttachmentImpl(AttachmentImpl other)
	{
		this.id = other.id;
		this.postId = other.postId;
		this.privmsgsId = other.privmsgsId;
		this.userId = other.userId;
		this.info = new AttachmentInfoImpl((AttachmentInfoImpl)other.info);
	}
	
	public AttachmentImpl(){}
	
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
	public AttachmentInfo getInfo()
	{
		return info;
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
	public int getPrivmsgsId()
	{
		return privmsgsId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getUserId()
	{
		return userId;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setInfo(AttachmentInfo info)
	{
		this.info = info;
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
	public void setPrivmsgsId(int privmsgsId)
	{
		this.privmsgsId = privmsgsId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
}
