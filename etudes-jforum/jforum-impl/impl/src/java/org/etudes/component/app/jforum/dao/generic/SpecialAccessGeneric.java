/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/SpecialAccessGeneric.java $ 
 * $Id: SpecialAccessGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.dao.generic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.dao.SpecialAccessDao;
import org.etudes.component.app.jforum.AccessDatesImpl;
import org.etudes.component.app.jforum.SpecialAccessImpl;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;

public abstract class SpecialAccessGeneric implements SpecialAccessDao
{
	private static Log logger = LogFactory.getLog(SpecialAccessGeneric.class);

	/** Dependency: SqlService */
	protected SqlService sqlService = null;

	/**
	 * {@inheritDoc}
	 */
	public int addForumSpecialAccess(SpecialAccess specialAccess)
	{
		if (specialAccess == null || specialAccess.getId() > 0 || (specialAccess.getForumId() <= 0 || specialAccess.getTopicId() > 0) || (specialAccess.getUserIds() == null || specialAccess.getUserIds().isEmpty()) )
		{
			throw new IllegalArgumentException("Special access data is missing.");
		}
		
		return insertForumSpecialAccessTx(specialAccess);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int addTopicSpecialAccess(SpecialAccess specialAccess)
	{
		if (specialAccess == null || specialAccess.getId() > 0 || (specialAccess.getForumId() <= 0 || specialAccess.getTopicId() <= 0) || (specialAccess.getUserIds() == null || specialAccess.getUserIds().isEmpty()) )
		{
			throw new IllegalArgumentException("Special access data is missing.");
		}
		
		return insertTopicSpecialAccessTx(specialAccess);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(final int specialAccessId)
	{

		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				deleteTXN(specialAccessId);
			}
		}, "delete: " + specialAccessId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteUserSpecialAccess(int specialAccessId, int userId)
	{
		if (specialAccessId <= 0 || userId <= 0)
		{
			return;
		}
		
		deleteUserSpecialAccessTx(specialAccessId, userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> selectByForum(int forumId)
	{
		String sql;
		Object[] fields;
		int i = 0;

		sql = "SELECT special_access_id, forum_id, topic_id, start_date, hide_until_open, end_date, allow_until_date, override_start_date, override_hide_until_open, override_end_date, override_allow_until_date, password, lock_end_date, users FROM jforum_special_access WHERE forum_id = ?  AND topic_id = 0";

		fields = new Object[1];
		fields[i++] = forumId;

		return getSpecialAccess(sql, fields);
	}

	/**
	 * {@inheritDoc}
	 */
	public SpecialAccess selectById(int specialAccessId)
	{
		String sql = "SELECT special_access_id, forum_id, topic_id, start_date, hide_until_open, end_date, allow_until_date, override_start_date, override_hide_until_open, override_end_date, override_allow_until_date, password, lock_end_date, users FROM jforum_special_access WHERE special_access_id = ?";
	
		int i = 0;
		Object[] fields = new Object[1];
		fields[i++] = specialAccessId;
	
		List<SpecialAccess> specialAccessList =  getSpecialAccess(sql.toString(), fields);
		
		SpecialAccess specialAccess = null;
		if (specialAccessList.size() == 1)
		{
			specialAccess = specialAccessList.get(0);
		}
		
		return specialAccess;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> selectBySite(String siteId)
	{
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;

		sql.append("SELECT s.special_access_id, s.forum_id, s.topic_id, s.start_date, s.hide_until_open, s.end_date, s.allow_until_date, s.override_start_date, s.override_hide_until_open, s.override_end_date, s.override_allow_until_date, s.password, s.lock_end_date, s.users ");
		//sql.append("SELECT s.special_access_id, s.forum_id, s.topic_id, s.start_date, s.end_date, ");
		//sql.append("s.lock_end_date, s.override_start_date, s.override_end_date, s.override_lock_end_date, s.password, s.users ");
		sql.append("FROM jforum_special_access s, jforum_forums f, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.forum_id = f.forum_id ");
		sql.append("AND f.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");

		fields = new Object[1];
		fields[i++] = siteId;

		return getSpecialAccess(sql.toString(), fields);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> selectBySiteAllForums(String siteId)
	{
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;

		//sql.append("SELECT s.special_access_id, s.forum_id, s.topic_id, s.start_date, s.end_date, ");
		//sql.append("s.lock_end_date, s.override_start_date, s.override_end_date, s.override_lock_end_date, s.password, s.users ");
		sql.append("SELECT s.special_access_id, s.forum_id, s.topic_id, s.start_date, s.hide_until_open, s.end_date, s.allow_until_date, s.override_start_date, s.override_hide_until_open, s.override_end_date, s.override_allow_until_date, s.password, s.lock_end_date, s.users ");
		sql.append("FROM jforum_special_access s, jforum_forums f, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.forum_id = f.forum_id AND s.topic_id = 0 ");
		sql.append("AND f.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");

		fields = new Object[1];
		fields[i++] = siteId;

		return getSpecialAccess(sql.toString(), fields);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> selectBySiteAllTopics(String siteId)
	{
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;

		//sql.append("SELECT s.special_access_id, s.forum_id, s.topic_id, s.start_date, s.end_date, ");
		//sql.append("s.lock_end_date, s.override_start_date, s.override_end_date, s.override_lock_end_date, s.password, s.users ");
		sql.append("SELECT s.special_access_id, s.forum_id, s.topic_id, s.start_date, s.hide_until_open, s.end_date, s.allow_until_date, s.override_start_date, s.override_hide_until_open, s.override_end_date, s.override_allow_until_date, s.password, s.lock_end_date, s.users ");
		sql.append("FROM jforum_special_access s, jforum_forums f, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.forum_id = f.forum_id AND s.topic_id != 0 ");
		sql.append("AND f.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");

		fields = new Object[1];
		fields[i++] = siteId;

		return getSpecialAccess(sql.toString(), fields);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> selectByTopic(int forumId, int topicId)
	{
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;

		sql.append("SELECT special_access_id, forum_id, topic_id, start_date, hide_until_open, end_date, allow_until_date, override_start_date, override_hide_until_open, override_end_date, override_allow_until_date, password, lock_end_date, users FROM jforum_special_access");
		sql.append(" WHERE forum_id = ? and Topic_id  = ?");

		fields = new Object[2];
		fields[i++] = forumId;
		fields[i++] = topicId;

		return getSpecialAccess(sql.toString(), fields);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> selectTopicsByForumId(int forumId)
	{
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;
	
		sql.append("SELECT special_access_id, forum_id, topic_id, start_date, hide_until_open, end_date, allow_until_date, override_start_date, override_hide_until_open, override_end_date, override_allow_until_date, password, lock_end_date, users ");
		sql.append("FROM jforum_special_access WHERE forum_id = ?  AND topic_id > 0");
	
		fields = new Object[1];
		fields[i++] = forumId;
	
		return getSpecialAccess(sql.toString(), fields);
	}

	/**
	 * @param sqlService
	 *        the sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateForumSpecialAccess(SpecialAccess specialAccess)
	{
		if (specialAccess == null || specialAccess.getId() <= 0 || (specialAccess.getForumId() <= 0 || specialAccess.getTopicId() > 0) || (specialAccess.getUserIds() == null || specialAccess.getUserIds().isEmpty()) )
		{
			throw new IllegalArgumentException("Special access data is missing.");
		}
		
		updateForumSpecialAccessTx(specialAccess);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateTopicSpecialAccess(SpecialAccess specialAccess)
	{
		if (specialAccess == null || specialAccess.getId() <= 0 || (specialAccess.getForumId() <= 0 || specialAccess.getTopicId() <= 0) || (specialAccess.getUserIds() == null || specialAccess.getUserIds().isEmpty()) )
		{
			throw new IllegalArgumentException("Special access data is missing.");
		}
		
		updateTopicSpecialAccessTx(specialAccess);
	}
	
	/**
	 * Delete the special access
	 * 
	 * @param specialAccessId
	 * 
	 * @throws RuntimeException
	 */
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
	}
	
	/**
	 * Deletes user special access
	 * 
	 * @param specialAccessId	Special access id
	 * 
	 * @param userId	User id
	 */
	protected void deleteUserSpecialAccessTx(final int specialAccessId, final int userId)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					SpecialAccess exisSpecialAccess = selectById(specialAccessId);
					List<Integer>  exisUserIds = exisSpecialAccess.getUserIds();
					
					List<Integer> specialAccessUser = new ArrayList<Integer>();
					specialAccessUser.add(new Integer(userId));
					
					if (exisUserIds.removeAll(specialAccessUser))
					{
						if (exisUserIds.size() > 0)
						{	
							exisSpecialAccess.setUserIds(exisUserIds);
							update(exisSpecialAccess);
						}
						else
						{
							delete(exisSpecialAccess.getId());
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while deleting user special access.", e);
				}
			}
		}, "deleteuser: " + specialAccessId + ":" + userId);
		
	}
	
	
	/**
	 * Gets the special access
	 * 
	 * @param sql
	 *        Query to execute
	 * 
	 * @param fields
	 *        Query params
	 * 
	 * @return Returns the list of special access
	 */
	protected List<SpecialAccess> getSpecialAccess(String sql, Object[] fields)
	{
		final List<SpecialAccess> specialAccessList = new ArrayList<SpecialAccess>();

		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					SpecialAccess specialAccess = new SpecialAccessImpl();
					((SpecialAccessImpl)specialAccess).setId(result.getInt("special_access_id"));
					((SpecialAccessImpl)specialAccess).setForumId(result.getInt("forum_id"));
					((SpecialAccessImpl)specialAccess).setTopicId(result.getInt("topic_id"));

					AccessDates accessDates = new AccessDatesImpl();
										
					if (result.getDate("start_date") != null)
					{
						Timestamp startDate = result.getTimestamp("start_date");
						accessDates.setOpenDate(startDate);
						accessDates.setHideUntilOpen(result.getInt("hide_until_open") > 0);
					}
					else
					{
						accessDates.setOpenDate(null);
					}
					
					/*if (result.getDate("end_date") != null)
					{
						Timestamp endDate = result.getTimestamp("end_date");
						accessDates.setDueDate(endDate);
						//accessDates.setLocked(result.getInt("lock_end_date") > 0);
					}
					else
					{
						accessDates.setDueDate(null);
					}*/
					accessDates.setDueDate(result.getTimestamp("end_date"));
					
					accessDates.setAllowUntilDate(result.getTimestamp("allow_until_date"));
					
					((SpecialAccessImpl)specialAccess).setAccessDates(accessDates);
					
					if (result.getInt("override_hide_until_open") == 1)
					{
						specialAccess.setOverrideHideUntilOpen(result.getInt("override_hide_until_open") == 1);
						accessDates.setHideUntilOpen(result.getInt("hide_until_open") > 0);
					}

					specialAccess.setOverrideStartDate(result.getInt("override_start_date") == 1);

					specialAccess.setOverrideEndDate(result.getInt("override_end_date") == 1);
					
					specialAccess.setOverrideAllowUntilDate(result.getInt("override_allow_until_date") == 1);

					/*if (result.getInt("override_lock_end_date") == 1)
					{
						specialAccess.setOverrideLockEndDate(result.getInt("override_lock_end_date") == 1);
						accessDates.setLocked(result.getInt("lock_end_date") > 0);
					}*/

					List<Integer> userIds = getUserIdList(result.getString("users"));
					specialAccess.setUserIds(userIds);

					specialAccessList.add(specialAccess);

					return null;
				}
				catch (SQLException e)
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
	}
	
	/**
	 * get userid list from the string
	 * 
	 * @param userIds
	 *        userids string
	 * 
	 * @return list of userid's
	 */
	protected List<Integer> getUserIdList(String userIds)
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
	}
	
	/**
	 * Get user id string from the users id's list
	 * @param userIds		List of user id's
	 * @return				User id string
	 */
	protected String getUserIdString(List<Integer> userIds)
	{
		if ((userIds == null) || (userIds.size() == 0)) 
		{
			return null;
		}
		
		StringBuilder userIdSB = new StringBuilder();
		for (Integer userId : userIds)
		{
			if (userId != null)
			{
				userIdSB.append(userId);
				userIdSB.append(":");
			}
		}
		
		String userIdsStr = userIdSB.toString();
		return userIdsStr.substring(0, userIdsStr.length() - 1);
	}

	/**
	 * Inserts forum special access. One user must have one special access
	 * 
	 * @param specialAccess	Special access
	 * 
	 * @return The special access id
	 */
	protected int insertForumSpecialAccessTx(final SpecialAccess specialAccess)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					// delete any existing special access for the selected users and create new special access
					List<SpecialAccess> forumSpecialAccessList = selectByForum(specialAccess.getForumId());
					List<Integer> users = specialAccess.getUserIds();
					
					for (SpecialAccess exiSpecialAccess : forumSpecialAccessList)
					{
						List<Integer>  exisUserIds = exiSpecialAccess.getUserIds();
						if (exisUserIds.removeAll(users))
						{
							if (exisUserIds.size() > 0)
							{	
								exiSpecialAccess.setUserIds(exisUserIds);
								update(exiSpecialAccess);
							}
							else
							{
								delete(exiSpecialAccess.getId());
							}
								
						}
					}
					
					if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideHideUntilOpen()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideAllowUntilDate())) && (users.size() > 0))
					{
						int specialAccessId = insertSpecialAccess(specialAccess);
						((SpecialAccessImpl)specialAccess).setId(specialAccessId);
					}					
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while creating new forum special access.", e);
				}
			}
		}, "saveSpecialAccess: " + specialAccess.getForumId() + ":" + specialAccess.getTopicId());
		
		return specialAccess.getId();
	}
	
	/**
	 * Inserts the special access
	 * 
	 * @param specialAccess	Special access
	 * 
	 * @return	The special access id
	 */
	protected abstract int insertSpecialAccess(SpecialAccess specialAccess);
	
	protected int insertTopicSpecialAccessTx(final SpecialAccess specialAccess)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					// delete any existing special access for the selected users and create new special access
					// TODO: validate topic id with forum id
					List<SpecialAccess> topicSpecialAccessList = selectByTopic(specialAccess.getForumId(), specialAccess.getTopicId());
					List<Integer> users = specialAccess.getUserIds();
					
					for (SpecialAccess exiSpecialAccess : topicSpecialAccessList)
					{
						List<Integer>  exisUserIds = exiSpecialAccess.getUserIds();
						if (exisUserIds.removeAll(users))
						{
							if (exisUserIds.size() > 0)
							{	
								exiSpecialAccess.setUserIds(exisUserIds);
								update(exiSpecialAccess);
							}
							else
							{
								delete(exiSpecialAccess.getId());
							}
								
						}
					}
					
					//if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideLockEndDate())) && (users.size() > 0))
					if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideHideUntilOpen()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideAllowUntilDate())) && (users.size() > 0))
					{
						int specialAccessId = insertSpecialAccess(specialAccess);
						((SpecialAccessImpl)specialAccess).setId(specialAccessId);
					}					
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while creating new topic special access.", e);
				}
			}
		}, "saveSpecialAccess: " + specialAccess.getForumId() + ":" + specialAccess.getTopicId());
		
		return specialAccess.getId();
	}
	

	/**
	 * Updates the special access
	 * 
	 * @param specialAccess	Special access
	 */
	protected void update(SpecialAccess specialAccess)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE jforum_special_access SET forum_id = ?, topic_id = ?, start_date = ?, hide_until_open = ?, end_date = ?, allow_until_date = ?, ");
		sql.append("override_start_date = ?, override_hide_until_open = ?, override_end_date = ?, override_allow_until_date = ?, users = ? WHERE special_access_id = ?");
		
		Object[] fields = new Object[12];
		int i = 0;

		fields[i++] = specialAccess.getForumId();
		fields[i++] = specialAccess.getTopicId();
		
		if (specialAccess.getAccessDates().getOpenDate() == null)
		{
			fields[i++] = null;
		}
		else
		{
			fields[i++] = new Timestamp(specialAccess.getAccessDates().getOpenDate().getTime());
		}
		
		fields[i++] = specialAccess.getAccessDates().isHideUntilOpen() ? 1 : 0;
		
		if (specialAccess.getAccessDates().getDueDate() == null)
		{
			fields[i++] = null;
		}
		else
		{
			fields[i++] = new Timestamp(specialAccess.getAccessDates().getDueDate().getTime());		  
		}
		
		if (specialAccess.getAccessDates().getAllowUntilDate() == null)
		{
			fields[i++] = null;
		}
		else
		{
			fields[i++] = new Timestamp(specialAccess.getAccessDates().getAllowUntilDate().getTime());		  
		}
		
		fields[i++] = specialAccess.isOverrideStartDate() ? 1 : 0;
		fields[i++] = specialAccess.isOverrideHideUntilOpen() ? 1 : 0;
		fields[i++] = specialAccess.isOverrideEndDate() ? 1 : 0;
		fields[i++] = specialAccess.isOverrideAllowUntilDate() ? 1 : 0;
		fields[i++] = getUserIdString(specialAccess.getUserIds());
		fields[i++] = specialAccess.getId();
			
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("update Special Access: db write failed");
		}
	}
	
	/**
	 * update forum special access. One user must have one special access.
	 * 
	 * @param specialAccess	Special access
	 */
	protected void updateForumSpecialAccessTx(final SpecialAccess specialAccess)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{		
					// delete any existing special access for the selected users
					List<SpecialAccess> forumSpecialAccessList = selectByForum(specialAccess.getForumId());
					List<Integer> users = specialAccess.getUserIds();
					
					for (SpecialAccess exiSpecialAccess : forumSpecialAccessList)
					{
						if (exiSpecialAccess.getId() == specialAccess.getId())
							continue;
						
						List<Integer>  exisUserIds = exiSpecialAccess.getUserIds();
						if (exisUserIds.removeAll(users))
						{
							if (exisUserIds.size() > 0)
							{	
								exiSpecialAccess.setUserIds(exisUserIds);
								update(exiSpecialAccess);
							}
							else
							{
								delete(exiSpecialAccess.getId());
							}
								
						}
					}
					
					//if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideLockEndDate())) && (users.size() > 0))
					if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideHideUntilOpen()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideAllowUntilDate())) && (users.size() > 0))
					{
						update(specialAccess);
					}
					else
					{
						delete(specialAccess.getId());
					}
	
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while updating forum special access.", e);
				}
			}
		}, "updateSpecialAccess: " + specialAccess.getForumId() + ":" + specialAccess.getTopicId());
	}
	
	/**
	 * Updates topic special access
	 * 
	 * @param specialAccess	Special access
	 */
	protected void updateTopicSpecialAccessTx(final SpecialAccess specialAccess)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{		
					// delete any existing special access for the selected users
					List<SpecialAccess> topicSpecialAccessList = selectByTopic(specialAccess.getForumId(), specialAccess.getTopicId());
					List<Integer> users = specialAccess.getUserIds();
					
					for (SpecialAccess exiSpecialAccess : topicSpecialAccessList)
					{
						if (exiSpecialAccess.getId() == specialAccess.getId())
							continue;
						
						List<Integer>  exisUserIds = exiSpecialAccess.getUserIds();
						if (exisUserIds.removeAll(users))
						{
							if (exisUserIds.size() > 0)
							{	
								exiSpecialAccess.setUserIds(exisUserIds);
								update(exiSpecialAccess);
							}
							else
							{
								delete(exiSpecialAccess.getId());
							}
								
						}
					}
					
					//if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideLockEndDate())) && (users.size() > 0))
					if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideHideUntilOpen()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideAllowUntilDate())) && (users.size() > 0))
					{
						update(specialAccess);
					}
					else
					{
						delete(specialAccess.getId());
					}
	
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while creating new special access.", e);
				}
			}
		}, "updateSpecialAccess: " + specialAccess.getForumId() + ":" + specialAccess.getTopicId());
	}
}
