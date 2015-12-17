/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumSpecialAccessServiceImpl.java $ 
 * $Id: JForumSpecialAccessServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.JForumSpecialAccessService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.SpecialAccessDao;
import org.sakaiproject.db.api.SqlService;

public class JForumSpecialAccessServiceImpl implements JForumSpecialAccessService
{
	private static Log logger = LogFactory.getLog(JForumSpecialAccessServiceImpl.class);

	/** Dependency: JForumUserService */
	protected JForumUserService jforumUserService = null;

	/** Dependency: SpecialAccessDao */
	protected SpecialAccessDao specialAccessDao = null;

	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	public void createForumSpecialAccess(SpecialAccess specialAccess)
	{
		if (specialAccess == null || specialAccess.getId() > 0 || (specialAccess.getForumId() <= 0 || specialAccess.getTopicId() > 0) || (specialAccess.getUserIds() == null || specialAccess.getUserIds().isEmpty()) )
		{
			throw new IllegalArgumentException("Special access data is missing.");
		}
		
		// TODO: group forums - allow users belong to group
		
		specialAccessDao.addForumSpecialAccess(specialAccess);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void createTopicSpecialAccess(SpecialAccess specialAccess)
	{
		if (specialAccess == null || specialAccess.getId() > 0 || (specialAccess.getForumId() <= 0 || specialAccess.getTopicId() <= 0) || (specialAccess.getUserIds() == null || specialAccess.getUserIds().isEmpty()) )
		{
			throw new IllegalArgumentException("Special access data is missing.");
		}
		
		// TODO: group forums - allow users belong to group
		
		specialAccessDao.addTopicSpecialAccess(specialAccess);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void delete(final int specialAccessId)
	{
		if (specialAccessId <= 0)
		{
			return;
		}
		specialAccessDao.delete(specialAccessId);

		/*this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				deleteTXN(specialAccessId);
			}
		}, "delete: " + specialAccessId);*/
	}

	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> getByForum(int forumId)
	{
		if (forumId == 0) throw new IllegalArgumentException();

		List<SpecialAccess> specialAccessList  = specialAccessDao.selectByForum(forumId);
		
		for (SpecialAccess specialAccess : specialAccessList)
		{
			// TODO: remove the special access and delete the user if user is inactive or removed from the site
			specialAccess.setUsers(getUsersList(specialAccess.getUserIds()));
		}
		return specialAccessList;
		/*String sql;
		Object[] fields;
		int i = 0;

		sql = "SELECT special_access_id, forum_id, topic_id, start_date, end_date, override_start_date, override_end_date, override_lock_end_date, password, lock_end_date, users FROM jforum_special_access WHERE forum_id = ?  AND topic_id = 0";

		fields = new Object[1];
		fields[i++] = forumId;

		return getSpecialAccess(sql, fields);*/
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> getBySite(String siteId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			throw new IllegalArgumentException();
		}
		
		return specialAccessDao.selectBySite(siteId);
		
		/*StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;

		sql.append("SELECT  sa.special_access_id, sa.forum_id, sa.topic_id, sa.start_date, sa.end_date, sa.lock_end_date, sa.override_start_date, ");
		sql.append("sa.override_end_date, sa.override_lock_end_date, sa.password, sa.users ");
		sql.append("FROM jforum_special_access sa, jforum_sakai_course_categories cc, jforum_forums f WHERE cc.course_id = ? ");
		sql.append("AND cc.categories_id = f.categories_id AND f.forum_id = sa.forum_id ORDER BY sa.forum_id");

		fields = new Object[1];
		fields[i++] = siteId;

		return getSpecialAccess(sql.toString(), fields);*/
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> getByTopic(int forumId, int topicId)
	{
		if ((forumId == 0) || (topicId == 0)) throw new IllegalArgumentException();
		
		return specialAccessDao.selectByTopic(forumId, topicId);

		/*StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;

		sql.append("SELECT special_access_id, forum_id, topic_id, start_date, end_date, lock_end_date, override_start_date, override_end_date, override_lock_end_date, password, users FROM jforum_special_access");
		sql.append(" WHERE forum_id = ? and Topic_id  = ?");

		fields = new Object[2];
		fields[i++] = forumId;
		fields[i++] = topicId;

		return getSpecialAccess(sql.toString(), fields);*/
	}
	
	/**
	 * {@inheritDoc}
	 */
	public SpecialAccess getSpecialAccess(int specialAccessId)
	{
		if (specialAccessId <= 0)
		{
			return null;
		}
		
		return specialAccessDao.selectById(specialAccessId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> getTopicsByForumId(int forumId)
	{
		if (forumId == 0) throw new IllegalArgumentException();
		
		return specialAccessDao.selectTopicsByForumId(forumId);
		/*StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;

		sql.append("SELECT special_access_id, forum_id, topic_id, start_date, end_date, override_start_date, override_end_date, override_lock_end_date, ");
		sql.append("password, lock_end_date, users FROM jforum_special_access WHERE forum_id = ?  AND topic_id > 0");

		fields = new Object[1];
		fields[i++] = forumId;

		return getSpecialAccess(sql.toString(), fields);*/
	}
	
	/*
	*//**
	 * Delete the special access
	 * 
	 * @param specialAccessId
	 * 
	 * @throws RuntimeException
	 *//*
	protected void deleteTXN(int specialAccessId)
	{
		String sql;
		Object[] fields;
		int i = 0;

		sql = "DELETE FROM jforum_special_access WHERE special_access_id = ?";

		fields = new Object[1];
		fields[i++] = specialAccessId;

		if (!sqlService.dbWrite(sql.toString(), fields))
		{
			throw new RuntimeException("deleteTXN: db write failed");
		}
	}*/

	/**
	 * Gets the special access
	 * 
	 * @param sql
	 *            Query to execute
	 * 
	 * @param fields
	 *            Query params
	 * 
	 * @return Returns the list of special access
	 */
	/*protected List<SpecialAccess> getSpecialAccess(String sql, Object[] fields)
	{
		final List<SpecialAccess> specialAccessList = new ArrayList<SpecialAccess>();

		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					SpecialAccess specialAccess = new SpecialAccessImpl();
					specialAccess.setId(result.getInt("special_access_id"));
					specialAccess.setForumId(result.getInt("forum_id"));
					specialAccess.setTopicId(result.getInt("topic_id"));

					AccessDates accessDates = new AccessDatesImpl();
					accessDates.setOpenDate(result.getTimestamp("start_date"));
					if (result.getDate("end_date") != null)
					{
						Timestamp endDate = result.getTimestamp("end_date");
						accessDates.setDueDate(endDate);
						accessDates.setLocked(result.getInt("lock_end_date") > 0);
					} else
					{
						accessDates.setDueDate(null);
					}
					specialAccess.setAccessDates(accessDates);

					specialAccess.setOverrideStartDate(result.getInt("override_start_date") == 1);							

					specialAccess.setOverrideEndDate(result.getInt("override_end_date") == 1);
					
					specialAccess.setOverrideLockEndDate(result.getInt("override_lock_end_date") == 1);				

					List<Integer> userIds = getUserIdList(result.getString("users"));
					specialAccess.setUserIds(userIds);

					specialAccessList.add(specialAccess);

					return null;
				} catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getSpecialAccess: " + e, e);
					}
					return null;
				}
			}
		});

		return specialAccessList;
	}*/

	/**
	 * get userid list from the string
	 * 
	 * @param userIds
	 *            userids string
	 * 
	 * @return list of userid's
	 */
	/*protected List<Integer> getUserIdList(String userIds)
	{
		if ((userIds == null) || (userIds.trim().length() == 0))
		{
			return null;
		}
		List<Integer> userIdList = new ArrayList<Integer>();

		String[] userIdsArray = userIds.split(":");
		if ((userIdsArray != null) && (userIdsArray.length > 0))
		{
			for (String userId : userIdsArray)
			{
				userIdList.add(new Integer(userId));
			}
		}

		return userIdList;
	}*/

	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isForumSpecialAccessDatesValid(Forum forum, SpecialAccess specialAccess)
	{
		
		if (forum == null || forum.getAccessDates() == null || specialAccess == null || specialAccess.getAccessDates() == null)
		{
			throw new IllegalArgumentException("Special access or forum data is missing.");
		}
		
		Date openDate = null, dueDate = null, allowUntilDate = null;
		
		if (specialAccess.isOverrideStartDate())
		{
			openDate = specialAccess.getAccessDates().getOpenDate();
		}
		else
		{
			openDate = forum.getAccessDates().getOpenDate();
		}
	
		if (specialAccess.isOverrideEndDate())
		{
			dueDate = specialAccess.getAccessDates().getDueDate();
		}
		else
		{
			dueDate = forum.getAccessDates().getDueDate();
		}
		
		if (specialAccess.isOverrideAllowUntilDate())
		{
			allowUntilDate = specialAccess.getAccessDates().getAllowUntilDate();
		}
		else
		{
			allowUntilDate = forum.getAccessDates().getAllowUntilDate();
		}
		
				
		boolean blnOpenDate = false, blnDueDate = false, blnAllowUntilDate = false;
		
		if (openDate != null) 
	 	{
			blnOpenDate = true;
	 	}
		
	 	if (dueDate != null) 
	 	{
	 		blnDueDate = true;
	 	}
	 	
	 	if (allowUntilDate != null) 
	 	{
	 		blnAllowUntilDate = true;
	 	}
	 	
	 	if (blnOpenDate && blnDueDate && blnAllowUntilDate)
	 	{
		 	if ((openDate.after(dueDate)) || (openDate.after(allowUntilDate)) || (dueDate.after(allowUntilDate))) 
		 	{
		   		return false;
		 	}
	 	}
	 	else if (blnOpenDate && blnDueDate && !blnAllowUntilDate)
		{
	 		if (openDate.after(dueDate)) 
		 	{
		   		
		   		return false;
		 	}
		}
	 	else if (!blnOpenDate && blnDueDate && blnAllowUntilDate)
		{
	 		if (dueDate.after(allowUntilDate)) 
		 	{
		   		return false;
		 	}
		}
	 	else if (blnOpenDate && !blnDueDate && blnAllowUntilDate)
	 	{
		 	if (openDate.after(allowUntilDate)) 
		 	{
		   		return false;
		 	}
	 	}

	 	return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyForumSpecialAccess(SpecialAccess specialAccess)
	{
		if (specialAccess == null || specialAccess.getId() <= 0 || (specialAccess.getForumId() <= 0 || specialAccess.getTopicId() > 0) || (specialAccess.getUserIds() == null || specialAccess.getUserIds().isEmpty()) )
		{
			throw new IllegalArgumentException("Special access data is missing.");
		}
		
		// TODO: group forums - allow users belong to group
		
		specialAccessDao.updateForumSpecialAccess(specialAccess);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyTopicSpecialAccess(SpecialAccess specialAccess)
	{
		if (specialAccess == null || specialAccess.getId() <= 0 || (specialAccess.getForumId() <= 0 || specialAccess.getTopicId() <= 0) || (specialAccess.getUserIds() == null || specialAccess.getUserIds().isEmpty()) )
		{
			throw new IllegalArgumentException("Special access data is missing.");
		}
		
		// TODO: group forums - allow users belong to group
		
		specialAccessDao.updateTopicSpecialAccess(specialAccess);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public SpecialAccess newSpecialAccess(int forumId, int topicId)
	{
		if (forumId <= 0 && topicId <= 0)
		{
			return null;
		}
		
		SpecialAccess specialAccess = new SpecialAccessImpl();
		
		((SpecialAccessImpl)specialAccess).setForumId(forumId);
		((SpecialAccessImpl)specialAccess).setTopicId(topicId);
		
		AccessDates accessDates = new AccessDatesImpl();
		((SpecialAccessImpl)specialAccess).setAccessDates(accessDates);
		
		return specialAccess;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeUserSpecialAccess(int specialAccessId, int userId)
	{
		if (specialAccessId <= 0 || userId <= 0)
		{
			return;
		}
		
		specialAccessDao.deleteUserSpecialAccess(specialAccessId, userId);
	}
	
	/**
	 * @param jforumUserService the jforumUserService to set
	 */
	public void setJforumUserService(JForumUserService jforumUserService)
	{
		this.jforumUserService = jforumUserService;
	}
	
	/**
	 * @param specialAccessDao the specialAccessDao to set
	 */
	public void setSpecialAccessDao(SpecialAccessDao specialAccessDao)
	{
		this.specialAccessDao = specialAccessDao;
	}
	
	/**
	 * @param sqlService
	 *            the sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * Gets the users list from user id's
	 * @param userIds		List of user id's
	 * @return				List of Users
	 */
	protected List<User> getUsersList(List<Integer> userIds)
	{
		List<User> usersList = new ArrayList<User>();
		
		if ((userIds == null) || (userIds.size() == 0))
			return usersList;
		
		for (Integer userId : userIds)
		{
			User user  = jforumUserService.getByUserId(userId.intValue());
			if (user != null)
			{
				usersList.add(user);
			}
		}
		return usersList;
	}
}
