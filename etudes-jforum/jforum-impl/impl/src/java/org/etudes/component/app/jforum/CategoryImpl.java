/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/CategoryImpl.java $ 
 * $Id: CategoryImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.LastPostInfo;

public class CategoryImpl implements Category
{
	//private static Log logger = LogFactory.getLog(CategoryImpl.class);
	
	protected AccessDates accessDates = null;
	
	protected Boolean blocked = Boolean.FALSE;
	
	protected String blockedByDetails;
	
	protected String blockedByTitle;
	
	protected String context  = null;
	
	protected String createdBySakaiUserId;
	
	protected List<Evaluation> evaluations = new ArrayList<Evaluation>();
	
	protected List<Forum> forums = new ArrayList<Forum>();
	
	protected Boolean gradable = Boolean.FALSE;
	
	protected Grade grade = null;
	
	protected int id;
	
	protected LastPostInfo lastPostInfo = null;
	
	protected String modifiedBySakaiUserId;
	
	protected int order;
	
	protected String title = null;
	
	protected Map<String, Integer> userPostCount = new HashMap<String, Integer>();
	
	transient String currentSakaiUserId = null;
	
	public CategoryImpl(){}

	protected CategoryImpl(CategoryImpl other)
	{
		this.id = other.id;
		this.title = other.title;
		this.context = other.context;
		this.accessDates = new AccessDatesImpl((AccessDatesImpl)other.accessDates);
		this.gradable = other.gradable;	
		this.lastPostInfo = other.lastPostInfo;
		this.order = other.order;
		
		if (other.grade != null)
		{
			this.grade = new GradeImpl((GradeImpl)other.grade);
		}
		
		this.currentSakaiUserId = other.currentSakaiUserId;
		
		this.blocked = other.blocked;
		this.blockedByDetails = other.blockedByDetails;
		this.blockedByTitle = other.blockedByTitle;	
		
		this.modifiedBySakaiUserId = other.modifiedBySakaiUserId;
		
		// evaluations - retrieve if needed
	}
	
	/**
	 * @param blockedByDetails the blockedByDetails to set
	 */
	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		// two categories are equals if they have the same id
		return ((obj instanceof Category) && (((Category)obj).getId() == this.id));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AccessDates getAccessDates()
	{
		return this.accessDates;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean getBlocked()
	{
		return blocked;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getBlockedByDetails()
	{
		return blockedByDetails;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBlockedByTitle()
	{
		return blockedByTitle;
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
	public String getCreatedBySakaiUserId()
	{
		return createdBySakaiUserId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> getEvaluations()
	{
		return evaluations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Forum> getForums()
	{
		return this.forums;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Grade getGrade()
	{
		return grade;
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
	public LastPostInfo getLastPostInfo()
	{
		return lastPostInfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getModifiedBySakaiUserId()
	{
		return modifiedBySakaiUserId;
	}

	/**
	 * @return the order
	 */
	public int getOrder()
	{
		return order;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
	{
		return this.title;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Integer> getUserPostCount()
	{
		return userPostCount;
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
	public Boolean isGradable()
	{
		return this.gradable;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAccessDates(AccessDates accessDates)
	{
		this.accessDates = accessDates;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBlocked(Boolean blocked)
	{
		this.blocked = blocked;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBlockedByDetails(String blockedByDetails)
	{
		this.blockedByDetails = blockedByDetails;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBlockedByTitle(String blockedByTitle)
	{
		this.blockedByTitle = blockedByTitle;
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
	public void setCreatedBySakaiUserId(String createdBySakaiUserId)
	{
		this.createdBySakaiUserId = createdBySakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGradable(Boolean gradable)
	{
		this.gradable = gradable;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGrade(Grade grade)
	{
		this.grade = grade;
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
	public void setLastPostInfo(LastPostInfo lastPostInfo)
	{
		this.lastPostInfo = lastPostInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setModifiedBySakaiUserId(String modifiedBySakaiUserId)
	{
		this.modifiedBySakaiUserId = modifiedBySakaiUserId;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the currentSakaiUserId
	 */
	protected String getCurrentSakaiUserId()
	{
		return currentSakaiUserId;
	}

	/**
	 * @param currentSakaiUserId the currentSakaiUserId to set
	 */
	protected void setCurrentSakaiUserId(String currentSakaiUserId)
	{
		this.currentSakaiUserId = currentSakaiUserId;
	}
}
