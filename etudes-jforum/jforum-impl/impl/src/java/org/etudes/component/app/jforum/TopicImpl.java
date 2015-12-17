/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/TopicImpl.java $ 
 * $Id: TopicImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.LastPostInfo;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;

public class TopicImpl implements Topic
{
	private Boolean read = Boolean.TRUE;
	
	protected AccessDates accessDates = null;
	
	protected Boolean blocked = Boolean.FALSE;
	
	protected String blockedByDetails;
	
	protected String blockedByTitle;
	
	protected List<Evaluation> evaluations = new ArrayList<Evaluation>();
	
	protected Boolean exportTopic = Boolean.FALSE;	
	
	protected int firstPostId;
	
	protected Date firstPostTime;
	
	protected Forum forum = null;
	
	protected int forumId;
	
	protected Grade grade = null;	

	protected Boolean gradeTopic = Boolean.FALSE;
	
	protected Boolean hasAttach = Boolean.FALSE;
	
	protected Boolean hot = Boolean.FALSE;
	
	protected int id;
	
	protected User lastPostBy;
	
	protected int lastPostId;

	protected LastPostInfo lastPostInfo = null;
	
	protected Date lastPostTime;
	
	protected Boolean mayPost = Boolean.TRUE;
	
	protected Boolean paginate = Boolean.FALSE;
	
	protected User postedBy;
	
	protected List<Post> posts = new ArrayList<Post>();
	
	protected List<SpecialAccess> specialAccess = new ArrayList<SpecialAccess>();
	
	protected int status;
	
	protected Date time;
	
	protected String title = null;
	
	protected int totalReplies;
	
	protected int type;
	
	protected Map<String, Integer> userPostCount = new HashMap<String, Integer>();
	
	transient String currentSakaiUserId = null;
	
	transient JForumForumService jforumForumService = null;
	
	public TopicImpl()
	{
		
	}

	public TopicImpl(JForumForumService jforumForumService)
	{
		this.jforumForumService = jforumForumService;
	}

	protected TopicImpl(TopicImpl other)
	{
		this.id = other.id;
		this.forumId = other.forumId;
		this.title = other.title;
		if (other.time != null)
		{
			this.time = new Date(other.time.getTime());
		}
		this.type = other.type;
		this.firstPostId = other.firstPostId;
		this.lastPostId = other.lastPostId;	
		this.gradeTopic = other.gradeTopic;
		this.exportTopic = other.exportTopic;
		this.hasAttach = other.hasAttach;
		this.accessDates = new AccessDatesImpl((AccessDatesImpl)other.accessDates);
		if (other.grade != null)
		{
			this.grade = new GradeImpl((GradeImpl)other.grade);
		}
		this.status = other.status;
		
		//TODO: if getEvaluations() is empty and topic if gradable retrieve evaluations
		/*for (Evaluation evaluation : other.evaluations)
		{
			evaluations.add(new EvaluationImpl((EvaluationImpl)evaluation));
		}*/

		for (SpecialAccess specialAccess : other.getSpecialAccess())
		{
			
			this.specialAccess.add(new SpecialAccessImpl((SpecialAccessImpl)specialAccess));
		}

		if (other.lastPostInfo != null)
		{
			this.lastPostInfo = new LastPostInfoImpl((LastPostInfoImpl)other.lastPostInfo);
		}

		// not needed userPostCount = new HashMap<String, Integer>();

		if (other.firstPostTime != null)
		{
			this.firstPostTime = new Date(other.firstPostTime.getTime());
		}

		if (other.lastPostTime != null)
		{
			this.lastPostTime = new Date(other.lastPostTime.getTime());
		}

		this.postedBy = new UserImpl((UserImpl)other.postedBy);
		this.lastPostBy = new UserImpl((UserImpl)other.lastPostBy);
		this.totalReplies = other.totalReplies;
		// TODO posts = new ArrayList<Post>(); if getPosts() list is empty retrieve the topic posts
		// not needed specific to user - read = Boolean.TRUE;
		this.blocked = other.blocked;
		this.blockedByTitle = other.blockedByTitle;
		this.blockedByDetails = other.blockedByDetails;
		this.hot = other.hot;
		this.paginate = other.paginate;
		this.jforumForumService = other.jforumForumService;
		this.mayPost = other.mayPost;		
	}

	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) 
	{
		// two topics are equals if they have the same id
		return ((o instanceof Topic) && (((Topic)o).getId() == this.id));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AccessDates getAccessDates()
	{
		return accessDates;
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
	public List<Evaluation> getEvaluations()
	{
		return evaluations;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getFirstPostId()
	{
		return firstPostId;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getFirstPostTime()
	{
		return firstPostTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public Forum getForum()
	{
		if (this.forum != null)
		{
			return this.forum;
		}
		
		if (this.jforumForumService != null && getForumId() > 0)
		{
			Forum forum = null;
			if (getCurrentSakaiUserId() != null)
			{
				try
				{
					forum = this.jforumForumService.getForum(getForumId(), getCurrentSakaiUserId());
				}
				catch (JForumAccessException e)
				{
					
				}
			}else
			{
				forum = this.jforumForumService.getForum(getForumId());
			}			
			
			this.setForum(forum);
			
			return forum;
		}
		
		return null;
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
	public Grade getGrade()
	{
		return grade;
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
	public User getLastPostBy()
	{
		return lastPostBy;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLastPostId()
	{
		return lastPostId;
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
	public Date getLastPostTime()
	{
		return lastPostTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean getPaginate()
	{
		return paginate;
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
	public List<Post> getPosts()
	{
		return posts;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean getRead()
	{
		return read;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> getSpecialAccess()
	{
		return specialAccess;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getStatus()
	{
		return status;
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
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTotalReplies()
	{
		return totalReplies;
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
	public TopicDisplayStatus getUserDiplayStatus()
	{
		TopicDisplayStatus userStatusDisplay = TopicDisplayStatus.NORMAL;
		
		// consider read or unread
		if (this.getStatus() == TopicStatus.UNLOCKED.getStatus())
		{
			 Date currentTime = Calendar.getInstance().getTime();
			 Date dueDate = this.getAccessDates().getDueDate();
			 if ((this.getAccessDates().getOpenDate() != null) && (currentTime.after(this.getAccessDates().getOpenDate())))
			 {
				 userStatusDisplay = TopicDisplayStatus.NOT_YET_OPEN;
			 }
			 else if ((dueDate != null) && (currentTime.after(dueDate)) && this.getAccessDates().isLocked())
			 {
				 userStatusDisplay = TopicDisplayStatus.LOCKED;
			 }
			 else if (this.getType() == TopicType.ANNOUNCE.getType())
			 {
				 userStatusDisplay = TopicDisplayStatus.ANNOUNCE;
			 }
			 else if (this.getType() == TopicType.STICKY.getType())
			 {
				 userStatusDisplay = TopicDisplayStatus.STICKY;
			 }
			 else if (this.isExportTopic())
			 {
				 userStatusDisplay = TopicDisplayStatus.REUSE;
			 }
		}
		else
		{
			userStatusDisplay = TopicDisplayStatus.LOCKED;
		}
		
		return userStatusDisplay;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, Integer> getUserPostCount()
	{
		return userPostCount;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean hasAttach()
	{
		return hasAttach;
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
	public Boolean isExportTopic()
	{
		return exportTopic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isGradeTopic()
	{
		return gradeTopic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isHot()
	{
		return hot;
	}

	public Boolean mayPost()
	{
		return mayPost;
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
	public void setExportTopic(Boolean exportTopic)
	{
		this.exportTopic = exportTopic;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFirstPostId(int firstPostId)
	{
		this.firstPostId = firstPostId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFirstPostTime(Date firstPostTime)
	{
		this.firstPostTime = firstPostTime;
	}

	/**
	 * @param forum the forum to set
	 */
	public void setForum(Forum forum)
	{
		this.forum = forum;
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
	public void setGrade(Grade grade)
	{
		this.grade = grade;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGradeTopic(Boolean gradeTopic)
	{
		this.gradeTopic = gradeTopic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setHasAttach(Boolean hasAttach)
	{
		this.hasAttach = hasAttach;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHot(Boolean hot)
	{
		this.hot = hot;
	}

	/**
	 * Sets the id
	 * 
	 * @param id 	The id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLastPostBy(User lastPostBy)
	{
		this.lastPostBy = lastPostBy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setLastPostId(int lastPostId)
	{
		this.lastPostId = lastPostId;
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
	public void setLastPostTime(Date lastPostTime)
	{
		this.lastPostTime = lastPostTime;
	}

	/**
	 * @param mayPost the mayPost to set
	 */
	public void setMayPost(Boolean mayPost)
	{
		this.mayPost = mayPost;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPaginate(Boolean paginate)
	{
		this.paginate = paginate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPostedBy(User postedBy)
	{
		this.postedBy = postedBy;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPosts(List<Post> posts)
	{
		this.posts = posts;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRead(Boolean read)
	{
		this.read = read;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTime(Date time)
	{
		this.time = time;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTotalReplies(int totalReplies)
	{
		this.totalReplies = totalReplies;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setType(int type)
	{
		this.type = type;
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
