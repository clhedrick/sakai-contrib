/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/UserDaoGeneric.java $ 
 * $Id: UserDaoGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.TopicDao;
import org.etudes.api.app.jforum.dao.UserAlreadyExistException;
import org.etudes.api.app.jforum.dao.UserAlreadyInSiteUsersException;
import org.etudes.api.app.jforum.dao.UserDao;
import org.etudes.component.app.jforum.UserImpl;
import org.etudes.util.HtmlHelper;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;


public abstract class UserDaoGeneric implements UserDao
{
	private static Log logger = LogFactory.getLog(UserDaoGeneric.class);
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	protected TopicDao topicDao = null;

	/**
	 * {@inheritDoc}
	 */
	public void addToSiteUsers(String siteId, int userId) throws UserAlreadyInSiteUsersException
	{
		insertSiteUser(siteId, userId);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addUpdateSiteVisitTime(String siteId, int userId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			return;
		}
		
		if (userId <= 0)
		{
			return;
		}
		
		Date visitTime = new Date();
		
		addUpdateSiteVisitTimeTx(siteId, userId, visitTime);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int addUser(User user) throws UserAlreadyExistException
	{
		if (user == null || user.getSakaiUserId() == null || user.getSakaiUserId().trim().length() == 0)
		{
			new IllegalArgumentException("user information is missing");
		}
		
		int userId = insertUser(user);
		((UserImpl)user).setId(userId);
		
		return userId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void markAllReadTime(String siteId, int userId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			return;
		}
		
		if (userId <= 0)
		{
			return;
		}
		
		Date markTime = new Date();
		
		markAllReadTimeTx(siteId, userId, markTime);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User selectBySakaiUserId(String sakaiUserId)
	{
		User user = null;
		
		StringBuilder sql;
		Object[] fields;
		int i = 0;
		
		sql = new StringBuilder();
		sql.append("SELECT user_id, username, user_regdate, user_new_privmsg, user_unread_privmsg, user_last_privmsg, user_viewemail, user_attachsig, ");
		sql.append("user_allowhtml, user_allow_pm, user_notify, user_notify_pm, user_avatar, user_avatar_type, user_email, user_icq, ");
		sql.append("user_website, user_from, user_sig, user_sig_bbcode_uid, user_aim, user_yim, user_msnm, user_occ, user_interests, gender, user_fname, ");
		sql.append("user_lname, sakai_user_id, user_facebook_account, user_twitter_account, user_lang, user_google_plus, user_skype, user_linkedIn FROM jforum_users WHERE sakai_user_id = ?");						
				
		fields = new Object[1];
		fields[i++] = sakaiUserId;
		
		final List<User> users = readUser(sql, fields);
		
		if (users.size() > 0)	// there should be only one record
		{
			user = users.get(0);
		}
		
		return user;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public User selectByUserId(int userId)
	{
		User user = null;
		
		StringBuilder sql;
		Object[] fields;
		int i = 0;
		
		sql = new StringBuilder();
		sql.append("SELECT user_id, username, user_regdate, user_new_privmsg, user_unread_privmsg, user_last_privmsg, user_viewemail, user_attachsig, ");
		sql.append("user_allowhtml, user_allow_pm, user_notify, user_notify_pm, user_avatar, user_avatar_type, user_email, user_icq, ");
		sql.append("user_website, user_from, user_sig, user_sig_bbcode_uid, user_aim, user_yim, user_msnm, user_occ, user_interests, gender, user_fname, ");
		sql.append("user_lname, sakai_user_id, user_facebook_account, user_twitter_account, user_lang, user_google_plus, user_skype, user_linkedIn FROM jforum_users WHERE user_id = ?");						
				
		fields = new Object[1];
		fields[i++] = userId;
		

		final List<User> users = readUser(sql, fields);
		
		if (users.size() > 0)	// there should be only one record
		{
			user = users.get(0);
		}
		
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Date selectMarkAllReadTime(String siteId, int userId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Site information is missing.");
		}
		
		if (userId <= 0)
		{
			throw new IllegalArgumentException("User information is missing");
		}
		
		Date markAllReadTime = null;
		
		String sql;
		Object[] fields;
		int i = 0;
		
		sql = "SELECT course_id, user_id, visit_time, markall_time FROM jforum_sakai_sessions WHERE course_id = ? AND user_id = ?";						
				
		fields = new Object[2];
		fields[i++] = siteId;
		fields[i++] = userId;
		
		final List<Date> markReadTime = new ArrayList<Date>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					markReadTime.add(result.getTimestamp("markall_time"));
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectMarkAllReadTime: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (markReadTime.size() > 0) // there should be only one record
		{
			markAllReadTime = markReadTime.get(0);
		}
		
		return markAllReadTime;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User selectSiteUser(String siteId, int userId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Site information is missing.");
		}
		
		if (userId <= 0)
		{
			throw new IllegalArgumentException("User information is missing");
		}
		
		User user = null;
		
		StringBuilder sql;
		Object[] fields;
		int i = 0;
		
		sql = new StringBuilder();
		sql.append("SELECT u.user_id, username, user_regdate, user_new_privmsg, user_unread_privmsg, user_last_privmsg, user_viewemail, user_attachsig, ");
		sql.append("user_allowhtml, user_allow_pm, user_notify, user_notify_pm, user_avatar, user_avatar_type, user_email, user_icq, ");
		sql.append("user_website, user_from, user_sig, user_sig_bbcode_uid, user_aim, user_yim, user_msnm, user_occ, user_interests, gender, user_fname, ");
		sql.append("user_lname, sakai_user_id, user_facebook_account, user_twitter_account, user_lang, user_google_plus, user_skype, user_linkedIn FROM jforum_users u, jforum_site_users su WHERE su.sakai_site_id = ? AND su.user_id = ? AND su.user_id = u.user_id");						
				
		fields = new Object[2];
		fields[i++] = siteId;
		fields[i++] = userId;
		

		final List<User> users = readUser(sql, fields);
		
		if (users.size() > 0) 	// there should be only one record
		{
			user = users.get(0);
		}
		
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<User> selectSiteUsers(String siteId)
	{

		StringBuilder sql;
		Object[] fields;
		int i = 0;
		
		sql = new StringBuilder();
		sql.append("SELECT u.user_id, username, user_regdate, user_new_privmsg, user_unread_privmsg, user_last_privmsg, user_viewemail, user_attachsig, ");
		sql.append("user_allowhtml, user_allow_pm, user_notify, user_notify_pm, user_avatar, user_avatar_type, user_email, user_icq, ");
		sql.append("user_website, user_from, user_sig, user_sig_bbcode_uid, user_aim, user_yim, user_msnm, user_occ, user_interests, gender, user_fname, ");
		sql.append("user_lname, sakai_user_id, user_facebook_account, user_twitter_account, user_lang, user_google_plus, user_skype, user_linkedIn FROM jforum_users u, jforum_site_users su WHERE su.sakai_site_id = ? AND su.user_id = u.user_id");						
				
		fields = new Object[1];
		fields[i++] = siteId;
		
		return readUser(sql, fields);	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Integer> selectSiteUsersPostsCount(String siteId)
	{
		return fetchSitePostsCount(siteId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Date selectSiteVisitTime(String siteId, int userId)
	{

		if (siteId == null || siteId.trim().length() == 0)
		{
			return null;
		}
		
		if (userId <= 0)
		{
			return null;
		}
		
		Date siteVisitTime = null;
		
		String sql;
		Object[] fields;
		int i = 0;
		
		sql = "SELECT visit_time FROM jforum_sakai_sessions where course_id = ? and user_id = ?";						
				
		fields = new Object[2];
		fields[i++] = siteId;
		fields[i++] = userId;
		
		final List<Date> siteLastVisitTime = new ArrayList<Date>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					siteLastVisitTime.add(result.getTimestamp("visit_time"));
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectSiteVisitTime: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (siteLastVisitTime.size() > 0) 	// there should be only one record
		{
			siteVisitTime = siteLastVisitTime.get(0);
		}
		
		return siteVisitTime;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectUserSitePostsCount(int userId, String siteId)
	{
		if ( userId <= 0 || siteId == null)
		{
			throw new IllegalArgumentException();
		}
		
		StringBuilder sql;
		Object[] fields = null;
		int i = 0;
			
		sql = new StringBuilder();
		
		sql.append("SELECT s.user_id, COUNT(p.user_id) AS num_messages ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f, jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? AND s.user_id = ? ");
		sql.append("AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");
		sql.append("GROUP BY s.user_id");
		
		fields = new Object[3];
		i = 0;
		fields[i++] = siteId;
		fields[i++] = userId;
		fields[i++] = siteId;
		
		final List<Integer> userPostCountList = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					int postsCount = result.getInt("num_messages");
					
					userPostCountList.add(Integer.valueOf(postsCount));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectUserSitePostsCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		int userPostCount = 0;
		
		if (userPostCountList.size() > 0)	// there should be only one record
		{
			userPostCount = userPostCountList.get(0);
		}
		
		return userPostCount;
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
	 * @param topicDao the topicDao to set
	 */
	public void setTopicDao(TopicDao topicDao)
	{
		this.topicDao = topicDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateSakaiAccountDetails(User user)
	{
		String sql = "UPDATE jforum_users SET user_email = ?, user_fname = ?, user_lname = ?, user_lang = ? WHERE user_id = ?";
		Object[] fields = new Object[5];
		int i = 0;
		
		if (user.getEmail() == null || user.getEmail().trim().length() == 0)
		{
			fields[i++] = " ";
		}
		else
		{
			fields[i++] = user.getEmail();
		}
		fields[i++] = user.getFirstName();			
		fields[i++] = user.getLastName();
		fields[i++] = user.getLang();
		fields[i++] = user.getId();
		
		try
		{
			this.sqlService.dbWrite(sql.toString(), fields);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateUser(User user)
	{
		if (user == null)
		{
			return;
		}
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE jforum_users ");
		sql.append("SET user_aim = ?, ");
		sql.append("user_avatar = ?, ");
		//sql.append("user_allowhtml = ?, ");
		sql.append("user_email = ?, ");
		sql.append("user_from = ?, ");
		//sql.append("user_icq = ?, ");
		sql.append("user_interests = ?, ");
		sql.append("user_occ = ?, ");
		sql.append("user_sig = ?, ");
		sql.append("user_website = ?, ");
		sql.append("user_yim = ?, ");
		sql.append("user_msnm = ?, ");
		sql.append("user_viewemail = ?, ");
		sql.append("user_notify = ?, ");
		sql.append("user_attachsig = ?, ");
		sql.append("username = ?, ");
		sql.append("user_lang = ?, ");
		sql.append("user_notify_pm = ?, ");
		sql.append("user_fname = ?, ");
		sql.append("user_lname = ?, ");
		sql.append("user_facebook_account = ?, ");
		sql.append("user_twitter_account = ?, ");		
		sql.append("user_google_plus = ?, ");
		sql.append("user_skype = ?, ");
		sql.append("user_linkedIn = ? ");		
		sql.append("WHERE user_id = ? ");
						
		
		Object[] fields = new Object[24];
		int i = 0;
		fields[i++] = user.getAim();
		fields[i++] = user.getAvatar();
		//fields[i++] = user.isHtmlEnabled() ? 1 : 0;
		
		// to avoid exception as email field is not null in the database
		if (user.getEmail() == null || user.getEmail().trim().length() == 0)
		{
			fields[i++] = " ";
		}
		else
		{
			fields[i++] = user.getEmail();
		}
		fields[i++] = user.getFrom();
		//fields[i++] = user.getIcq();		
		fields[i++] = user.getInterests();
		fields[i++] = user.getOccupation();
		fields[i++] = user.getSignature();
		fields[i++] = user.getWebSite();
		fields[i++] = user.getYim();
		fields[i++] = user.getMsnm();
		fields[i++] = user.isViewEmailEnabled() ? 1 : 0;
		fields[i++] = user.isNotifyOnMessagesEnabled() ? 1 : 0;
		fields[i++] = user.getAttachSignatureEnabled() ? 1 : 0;
		fields[i++] = user.getUsername();
		if (user.getLang() == null)
		{
			fields[i++] = "";
		}
        else
        {
        	fields[i++] = user.getLang();
        }
		
		fields[i++] = user.isNotifyPrivateMessagesEnabled() ? 1 : 0;

		fields[i++] = user.getFirstName();
		fields[i++] = user.getLastName();
		fields[i++] = user.getFaceBookAccount();
		fields[i++] = user.getTwitterAccount();		
		fields[i++] = user.getGooglePlus();
		fields[i++] = user.getSkype();
		fields[i++] = user.getLinkedIn();		
		fields[i++] = user.getId();
		
		try
		{
			this.sqlService.dbWrite(sql.toString(), fields);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateUserAvatar(User user)
	{
		if (user == null)
		{
			return;
		}
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE jforum_users SET user_avatar = ? WHERE user_id = ? ");
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] = user.getAvatar();
		fields[i++] = user.getId();
		
		try
		{
			this.sqlService.dbWrite(sql.toString(), fields);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
		}
		
	}
	
	/**
	 * adds or updates user site last visit time
	 * 
	 * @param siteId	Site id
	 * 
	 * @param userId	User id
	 * 
	 * @param visitTime	Visit time
	 */
	protected void addUpdateSiteVisitTimeTx(final String siteId, final int userId, final Date visitTime)
	{				
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					Date lastVisitTime = selectSiteVisitTime(siteId, userId);
					
					String sql = null;
					Object[] fields = new Object[3];
					
					if (lastVisitTime == null)
					{
						// add user site visit time
						sql = "INSERT INTO jforum_sakai_sessions (course_id, user_id, visit_time) VALUES (?, ?, ?)";
												
						int i = 0;
						
						fields[i++] = siteId;
						fields[i++] = userId;
						fields[i++] = new Timestamp(visitTime.getTime());
					}
					else
					{
						// update user site visit time						
						sql = "UPDATE jforum_sakai_sessions SET visit_time = ? WHERE course_id = ? AND user_id = ?";
						
						int i = 0;
						
						fields[i++] = new Timestamp(visitTime.getTime());;
						fields[i++] = siteId;
						fields[i++] = userId;
					}
					
					try
					{
						sqlService.dbWrite(sql.toString(), fields);
					}
					catch (Exception e)
					{
						if (logger.isErrorEnabled())
						{
							logger.error(e, e);
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while adding or updating user site visit time.", e);
				}
			}
		}, "addUpdateSiteVisitTime: " + siteId + ":"+ userId);																	
	}
	
	
	/**
	 * Fetches the site posts count of the user
	 * 
	 * @param siteId	The site id
	 * 
	 * @return The map of userid as key with post count as value
	 */
	protected Map<String, Integer> fetchSitePostsCount(String siteId)
	{
		if (siteId == null) throw new IllegalArgumentException();
		
		// user site posts count
		StringBuilder sql;
		Object[] fields;
		int i;
		sql = null;
		fields = null;
		i = 0;	
			
		sql = new StringBuilder();
		
		sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f, jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? ");
		sql.append("AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");
		sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
				
		fields = new Object[2];
		i = 0;
		fields[i++] = siteId;
		fields[i++] = siteId;
					
		Map<String, Integer> userPostCount = readUserPostsCount(sql, fields);
		
		return userPostCount;
	}
	
	/**
	 * Fills user info
	 * 
	 * @param result	Result set
	 * 
	 * @return	The user
	 * 
	 * @throws SQLException
	 */
	protected UserImpl fillUserInfo(ResultSet result) throws SQLException
	{
		UserImpl user = new UserImpl();
		
		user.setId(result.getInt("user_id"));
		user.setUsername(result.getString("username"));
		user.setFirstName(result.getString("user_fname"));
		user.setLastName(result.getString("user_lname"));
		user.setSakaiUserId(result.getString("sakai_user_id"));
		user.setNotifyOnMessagesEnabled(result.getInt("user_notify") == 1);
		user.setNotifyPrivateMessagesEnabled(result.getInt("user_notify_pm") == 1);
		user.setAvatar(result.getString("user_avatar"));
		user.setEmail(result.getString("user_email")==null ? "" : result.getString("user_email"));
		//user.setIcq(result.getString("user_icq"));
		user.setWebSite(result.getString("user_website"));
		user.setFrom(result.getString("user_from"));
		user.setSignature(HtmlHelper.clean(result.getString("user_sig"), true));
		user.setAim(result.getString("user_aim"));
		user.setYim(result.getString("user_yim"));
		user.setMsnm(result.getString("user_msnm"));
		user.setFaceBookAccount(result.getString("user_facebook_account"));
		user.setTwitterAccount(result.getString("user_twitter_account"));
		user.setOccupation(result.getString("user_occ"));
		user.setInterests(result.getString("user_interests"));
		user.setViewEmailEnabled(result.getInt("user_viewemail") == 1);
		user.setLang(result.getString("user_lang"));
		user.setAttachSignatureEnabled(result.getInt("user_attachsig") == 1);
		//user.setHtmlEnabled(result.getInt("user_allowhtml") == 1);
		user.setGooglePlus(result.getString("user_google_plus"));
		user.setSkype(result.getString("user_skype"));
		user.setLinkedIn(result.getString("user_linkedIn"));		
		
		return user;
	}
	
	/**
	 * Add user to site
	 * 
	 * @param siteId	Site id
	 * 
	 * @param userId	Jforum user id
	 * 
	 * @throws UserAlreadyInSiteUsersException
	 */
	protected void insertSiteUser(String siteId, int userId) throws UserAlreadyInSiteUsersException
	{
		// check to see if user is in jforum site users
		User jforumUser = selectByUserId(userId);
		
		if (jforumUser == null)
		{
			return;
		}
		
		User siteUser = selectSiteUser(siteId, userId);
		
		if (siteUser == null)
		{
			// user is not a site user. add as a site user.
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO jforum_site_users (sakai_site_id, user_id) ");
			sql.append("VALUES (?, ?)");
			
			Object[] fields = new Object[2];
			int i = 0;
			fields[i++] = siteId;
			fields[i++] = userId;			
			
			try
			{
				this.sqlService.dbWrite(sql.toString(), fields);
			}
			catch (Exception e)
			{
				if (logger.isErrorEnabled())
				{
					logger.error(e, e);
				}
			}
		}
		else
		{
			throw new UserAlreadyInSiteUsersException(siteId, userId);
		}
	}
	
	/**
	 * Create jforum user
	 * 
	 * @param user	Jforum user
	 * 
	 * @return		User id
	 * 
	 * @throws UserAlreadyExistException
	 */
	protected abstract int insertUser(User user) throws UserAlreadyExistException;
	
	/**
	 * Mark user all read time 
	 * 
	 * @param siteId	Site id
	 * 
	 * @param userId	user id
	 * 
	 * @param markAllTime	Mark all time
	 */
	protected void markAllReadTimeTx(final String siteId, final int userId, final Date markAllTime)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					String sql;
					Object[] fields;
					int i = 0;
					
					sql = "SELECT course_id, user_id, visit_time, markall_time FROM jforum_sakai_sessions WHERE course_id = ? AND user_id = ?";						
							
					fields = new Object[2];
					fields[i++] = siteId;
					fields[i++] = userId;
					
					final List<Date> markReadTime = new ArrayList<Date>();
					
					sqlService.dbRead(sql, fields, new SqlReader()
					{
						public Object readSqlResultRecord(ResultSet result)
						{
							try
							{
								markReadTime.add(result.getTimestamp("markall_time"));
								return null;
							}
							catch (SQLException e)
							{
								if (logger.isWarnEnabled())
								{
									logger.warn("selectMarkAllReadTime: " + e, e);
								}
								return null;
							}
						}
					});
					
					boolean dataExist = false;
					if (markReadTime.size() > 0) // there should be only one record
					{
						dataExist = true;
					}
										
					sql = null;
										
					if (!dataExist)
					{
						// add user site mark all read time
						sql = "INSERT INTO jforum_sakai_sessions (course_id, user_id, visit_time, markall_time) VALUES (?, ?, ?, ?)";
												
						i = 0;
						fields = new Object[4];
						
						fields[i++] = siteId;
						fields[i++] = userId;
						fields[i++] = new Timestamp(new Date().getTime());
						fields[i++] = new Timestamp(markAllTime.getTime());
					}
					else
					{
						// update user site mark all read time						
						sql = "UPDATE jforum_sakai_sessions SET markall_time = ? WHERE course_id = ? AND user_id = ?";
						
						i = 0;
						fields = new Object[3];
						
						fields[i++] = new Timestamp(markAllTime.getTime());;
						fields[i++] = siteId;
						fields[i++] = userId;
					}
					
					try
					{
						sqlService.dbWrite(sql.toString(), fields);
					}
					catch (Exception e)
					{
						if (logger.isErrorEnabled())
						{
							logger.error(e, e);
						}
					}
					
					// mark all marked unread topic's to read
					sql = "SELECT tm.topic_id, tm.user_id, tm.mark_time, tm.is_read FROM jforum_sakai_course_categories scc, jforum_forums f, jforum_topics t, jforum_topics_mark tm " +
							"WHERE scc.categories_id = f.categories_id AND f.forum_id = t.forum_id AND t.topic_id = tm.topic_id AND tm.user_id = ? AND tm.is_read = ? AND scc.course_id = ?";
					i = 0;
					fields = new Object[3];
					fields[i++] = userId;
					fields[i++] = 1;
					fields[i++] = siteId;
					
					sqlService.dbRead(sql, fields, new SqlReader()
					{
						public Object readSqlResultRecord(ResultSet result)
						{
							try
							{
								int topicId = result.getInt("topic_id");
								int userId = result.getInt("user_id");
								topicDao.updateTopicMarkTime(topicId, userId, new Date(), true);
								return null;
							}
							catch (SQLException e)
							{
								if (logger.isWarnEnabled())
								{
									logger.warn("selectTopicsMarkedUnread-markTopicRead: " + e, e);
								}
								return null;
							}
						}
					});
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while adding or updating user mark all read time.", e);
				}
			}
		}, "markUserAllReadTime: " + siteId + ":"+ userId);
	}
	
	/**
	 * Read from database
	 * 
	 * @param sql	The query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return	The jforum users
	 */
	protected List<User> readUser(StringBuilder sql, Object[] fields)
	{
		final List<User> users = new ArrayList<User>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					UserImpl user = fillUserInfo(result);					
					
					users.add(user);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readUser: " + e, e);
					}
					return null;
				}
			}
		});
		return users;
	}
	
	/**
	 * Gets user posts count from the database
	 * 
	 * @param sql	The query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return The map of user posts count with userid as key and posts count as value
	 */
	protected Map<String, Integer> readUserPostsCount(StringBuilder sql, Object[] fields)
	{
		final Map<String, Integer> userPostCount = new HashMap<String, Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					String userSakId = result.getString("sakai_user_id");
					int userPostsCount = result.getInt("user_posts_count");
					userPostCount.put(userSakId, userPostsCount);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readUserPostsCount: " + e, e);
					}
					return null;
				}
			}
		});
		return userPostCount;
	}
}
