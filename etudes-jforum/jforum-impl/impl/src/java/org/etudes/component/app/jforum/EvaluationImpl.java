/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/EvaluationImpl.java $ 
 * $Id: EvaluationImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011, 2012, 2013 Etudes, Inc. 
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

import org.etudes.api.app.jforum.Evaluation;

public class EvaluationImpl implements Evaluation
{
	protected String comments;
	
	protected int evaluatedBy;
	
	protected String evaluatedBySakaiUserId;
	
	protected Date evaluatedDate;
	
	protected int gradeId;
	
	protected int id;
	
	protected Date lastPostTime = null;
	
	protected Boolean late = Boolean.FALSE;
	
	protected Boolean released = Boolean.FALSE;
	
	protected Date reviewedDate;
	
	protected String sakaiDisplayId;
	
	protected String sakaiUserId;

	protected Float score;

	protected int totalPosts;

	protected String userFirstName;

	protected int userId;
	
	protected String userLastName;
	
	protected String username;
	
	protected String userSakaiGroupName;

	public EvaluationImpl()
	{
		
	}

	protected EvaluationImpl(EvaluationImpl other)
	{
		this.id = other.id;
		this.comments = other.comments;
		this.evaluatedBy = other.evaluatedBy;
		if (other.evaluatedDate != null)
		{
			this.evaluatedDate = new Date(other.evaluatedDate.getTime());
		}
		this.gradeId = other.gradeId;
		this.id = other.id;
		this.released = Boolean.valueOf(other.released.booleanValue());
		if (other.reviewedDate != null)
		{
			this.reviewedDate = new Date(other.reviewedDate.getTime());
		}
		this.sakaiUserId = other.sakaiUserId;
		if (score != null)
		{
			this.score = Float.valueOf(score.floatValue());
		}
		this.userId = other.userId;
		this.userFirstName = other.userFirstName;		
		this.userLastName = other.userLastName;		
		this.sakaiDisplayId = other.sakaiDisplayId;		
		this.totalPosts = other.totalPosts;		
		this.username = other.username;
		this.userSakaiGroupName = other.userSakaiGroupName;
		this.setLate(other.isLate());

	}

	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		// two evaluations are equals if they have the same id
		return ((obj instanceof Evaluation) && (((Evaluation)obj).getId() == this.id));
	}

	/**
	 * {@inheritDoc}
	 */
	public String getComments()
	{
		return comments;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getEvaluatedBy()
	{
		return evaluatedBy;
	}

	/**
	 * @return the evaluatedBySakaiUserId
	 */
	public String getEvaluatedBySakaiUserId()
	{
		return evaluatedBySakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getEvaluatedDate()
	{
		return evaluatedDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getGradeId()
	{
		return gradeId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the lastPostTime
	 */
	public Date getLastPostTime()
	{
		return lastPostTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getReviewedDate()
	{
		return reviewedDate;
	}

	/**
	 * @return the sakaiDisplayId
	 */
	public String getSakaiDisplayId()
	{
		return sakaiDisplayId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSakaiUserId()
	{
		return sakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public Float getScore()
	{
		return score;
	}
	
	/**
	 * @return the totalPosts
	 */
	public int getTotalPosts()
	{
		return totalPosts;
	}
	
	/**
	 * @return the userFirstName
	 */
	public String getUserFirstName()
	{
		return userFirstName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getUserId()
	{
		return userId;
	}
	
	/**
	 * @return the userLastName
	 */
	public String getUserLastName()
	{
		return userLastName;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * @return the userSakaiGroupName
	 */
	public String getUserSakaiGroupName()
	{
		return userSakaiGroupName;
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
	 * @return the isLate
	 */
	public Boolean isLate()
	{
		return late;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isReleased()
	{
		return released;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}

	/**
	 * Sets the evaluated by jforum id
	 * 
	 * @param evaluatedBy The evaluatedBy to set
	 */
	public void setEvaluatedBy(int evaluatedBy)
	{
		this.evaluatedBy = evaluatedBy;
	}

	/**
	 * @param evaluatedBySakaiUserId the evaluatedBySakaiUserId to set
	 */
	public void setEvaluatedBySakaiUserId(String evaluatedBySakaiUserId)
	{
		this.evaluatedBySakaiUserId = evaluatedBySakaiUserId;
	}

	/**
	 * Sets the evaluated date
	 * 
	 * @param evaluatedDate The evaluatedDate to set
	 */
	public void setEvaluatedDate(Date evaluatedDate)
	{
		this.evaluatedDate = evaluatedDate;
	}

	/**
	 * Sets the grade id
	 * 
	 * @param gradeId The gradeId to set
	 */
	public void setGradeId(int gradeId)
	{
		this.gradeId = gradeId;
	}


	/**
	 * Sets the id
	 * 
	 * @param id The id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * @param lastPostTime the lastPostTime to set
	 */
	public void setLastPostTime(Date lastPostTime)
	{
		this.lastPostTime = lastPostTime;
	}

	/**
	 * @param late the late to set
	 */
	public void setLate(Boolean late)
	{
		this.late = late;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setReleased(Boolean released)
	{
		this.released = released;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setReviewedDate(Date reviewedDate)
	{
		this.reviewedDate = reviewedDate;
	}

	/**
	 * @param sakaiDisplayId the sakaiDisplayId to set
	 */
	public void setSakaiDisplayId(String sakaiDisplayId)
	{
		this.sakaiDisplayId = sakaiDisplayId;
	}

	/**
	 * Sets the sakai user id
	 * 
	 * @param sakaiUserId The sakaiUserId to set
	 */
	public void setSakaiUserId(String sakaiUserId)
	{
		this.sakaiUserId = sakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setScore(Float score)
	{
		this.score = score;
	}

	/**
	 * @param totalPosts the totalPosts to set
	 */
	public void setTotalPosts(int totalPosts)
	{
		this.totalPosts = totalPosts;
	}

	/**
	 * @param userFirstName the userFirstName to set
	 */
	public void setUserFirstName(String userFirstName)
	{
		this.userFirstName = userFirstName;
	}
	
	/**
	 * Sets the user id
	 * 
	 * @param userId The userId to set
	 */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	/**
	 * @param userLastName the userLastName to set
	 */
	public void setUserLastName(String userLastName)
	{
		this.userLastName = userLastName;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	/**
	 * @param userSakaiGroupName the userSakaiGroupName to set
	 */
	public void setUserSakaiGroupName(String userSakaiGroupName)
	{
		this.userSakaiGroupName = userSakaiGroupName;
	}
	
}
