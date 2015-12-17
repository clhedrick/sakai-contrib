/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/GradeImpl.java $ 
 * $Id: GradeImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import org.etudes.api.app.jforum.Grade;

/**
 * Represents grade
 */
public class GradeImpl implements Grade
{
	protected Boolean addToGradeBook = Boolean.FALSE;
	
	protected int categoryId;
	
	protected String context;
	
	protected int forumId;
	
	protected int id;
	
	protected Date itemOpenDate = null;
	
	protected int minimumPosts;
	
	protected Boolean minimumPostsRequired = Boolean.FALSE;
	
	protected Float points;
	
	protected int topicId;
	
	protected int type;
	
	public GradeImpl(){}

	protected GradeImpl(GradeImpl grade)
	{		
		this.id = grade.getId();
		this.context = grade.getContext();
		this.type = grade.getType();
		this.forumId = grade.getForumId();
		this.topicId = grade.getTopicId();
		this.points = grade.getPoints();
		this.addToGradeBook = grade.isAddToGradeBook();
		this.minimumPosts = grade.minimumPosts;
		this.minimumPostsRequired = grade.minimumPostsRequired;
		this.itemOpenDate = grade.itemOpenDate;
	}

	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		// two grades are equals if they have the same id
		return ((obj instanceof Grade) && (((Grade)obj).getId() == this.id));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getCategoryId()
	{
		return categoryId;
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
	public int getForumId()
	{
		return this.forumId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getItemOpenDate()
	{
		return itemOpenDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMinimumPosts()
	{
		return minimumPosts;
	}

	/**
	 * {@inheritDoc}
	 */
	public Float getPoints()
	{
		return this.points;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTopicId()
	{
		return this.topicId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getType()
	{
		return this.type;
	}

	/** 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isAddToGradeBook()
	{
		return this.addToGradeBook;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isMinimumPostsRequired()
	{
		return minimumPostsRequired;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAddToGradeBook(Boolean addToGradeBook)
	{
		this.addToGradeBook = addToGradeBook;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setCategoryId(int categoryId)
	{
		this.categoryId = categoryId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setContext(String context)
	{
		this.context = context;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setForumId(int forumId)
	{
		this.forumId = forumId;
	}

	/**
	 * Sets the grade id
	 * 
	 * @param id 	The id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * @param itemOpenDate the itemOpenDate to set
	 */
	public void setItemOpenDate(Date itemOpenDate)
	{
		this.itemOpenDate = itemOpenDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMinimumPosts(int minimumPosts)
	{
		this.minimumPosts = minimumPosts;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMinimumPostsRequired(Boolean minimumPostsRequired)
	{
		this.minimumPostsRequired = minimumPostsRequired;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPoints(Float points)
	{
		this.points = points;
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
	public void setType(int type)
	{
		this.type = type;
	}
}
