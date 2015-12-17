/*********************************************************************************** 
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/dao/generic/GenericSpecialAccessDAO.java $ 
 * $Id: GenericSpecialAccessDAO.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
 * 
 **********************************************************************************/ 
package org.etudes.jforum.dao.generic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.etudes.jforum.JForum;
import org.etudes.jforum.dao.DataAccessDriver;
import org.etudes.jforum.dao.SpecialAccessDAO;
import org.etudes.jforum.entities.SpecialAccess;
import org.etudes.jforum.entities.User;
import org.etudes.jforum.util.preferences.ConfigKeys;
import org.etudes.jforum.util.preferences.SakaiSystemGlobals;
import org.etudes.jforum.util.preferences.SystemGlobals;
import org.sakaiproject.tool.cover.ToolManager;

/**
 * @author Murthy Tanniru
 *
 */
public class GenericSpecialAccessDAO extends AutoKeys implements SpecialAccessDAO
{

	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#selectById(int)
	 */
	public SpecialAccess selectById(int specialAccessId) throws Exception
	{
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("SpecialAccessModel.selectById"));
		p.setInt(1, specialAccessId);

		ResultSet rs = p.executeQuery();

		SpecialAccess specialAccess = null;

		if (rs.next()) {
			specialAccess = this.fillSpecialAccess(rs);
		}

		rs.close();
		p.close();
		return specialAccess;
	}
	
	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#selectByForumId(int)
	 */
	public List<SpecialAccess> selectByForumId(int forumId) throws Exception
	{
		List<SpecialAccess> specialAccessList = new ArrayList<SpecialAccess>();
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("SpecialAccessModel.selectSpecialAccessByForumId"));
		p.setInt(1, forumId);
		
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			specialAccessList.add(this.fillSpecialAccess(rs));
		}

		rs.close();
		p.close();
		
		return specialAccessList;
	}
	
	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#selectTopicsByForumId(int)
	 */
	public List<SpecialAccess> selectTopicsByForumId(int forumId) throws Exception
	{
		List<SpecialAccess> specialAccessList = new ArrayList<SpecialAccess>();
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("SpecialAccessModel.selectTopicsSpecialAccessByForumId"));
		p.setInt(1, forumId);
		
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			specialAccessList.add(this.fillSpecialAccess(rs));
		}

		rs.close();
		p.close();
		
		return specialAccessList;
	}
	
	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#selectBySite()
	 */
	public List<SpecialAccess> selectBySite() throws Exception
	{
		
		List<SpecialAccess> specialAccessList = new ArrayList<SpecialAccess>();
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("SpecialAccessModel.selectSpecialAccessByCourseId"));
		p.setString(1, ToolManager.getCurrentPlacement().getContext());
		
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			specialAccessList.add(this.fillSpecialAccess(rs));
		}

		rs.close();
		p.close();
		
		return specialAccessList;
	}
	
	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#selectForumSpecialAccessCount(int)
	 */
	public int selectForumSpecialAccessCount(int forumId) throws Exception
	{
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("SpecialAccessModel.selectForumSpecialAccessCountByForumId"));
		p.setInt(1, forumId);
		ResultSet rs = p.executeQuery();
		
		int count = 0;
		if (rs.next()) {
			count = rs.getInt("special_access_count");
		}

		rs.close();
		p.close();
		return count;
	}

	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#addNew(SpecialAccess)
	 */
	public int addNew(SpecialAccess specialAccess) throws Exception
	{

		PreparedStatement p = this.getStatementForAutoKeys("SpecialAccessModel.addNew");

		p.setInt(1, specialAccess.getForumId());
		p.setInt(2, specialAccess.getTopicId());
		
		if (specialAccess.getStartDate() == null)
		{
		  p.setTimestamp(3, null);
		}
		else
		{
		  p.setTimestamp(3, new Timestamp(specialAccess.getStartDate().getTime()));
		}
		
		if (specialAccess.getEndDate() == null)
		{
		  p.setTimestamp(4, null);
		}
		else
		{
		  p.setTimestamp(4, new Timestamp(specialAccess.getEndDate().getTime()));		  
		}		
		p.setInt(5, specialAccess.isOverrideStartDate() ? 1 : 0);
		p.setInt(6, specialAccess.isOverrideEndDate() ? 1 : 0);
		p.setInt(7, specialAccess.isOverrideLockEndDate() ? 1 : 0);
		p.setInt(8, specialAccess.isLockOnEndDate() ? 1 : 0);
		p.setString(9, getUserIdString(specialAccess.getUserIds()));
		
		this.setAutoGeneratedKeysQuery(SystemGlobals.getSql("SpecialAccessModel.lastGeneratedSpecilaAccessId"));

		int specialAccessId = this.executeAutoKeysQuery(p);

		p.close();
		
		return specialAccessId;
	}
	
	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#update(SpecialAccess)
	 */
	public void update(SpecialAccess specialAccess) throws Exception
	{

		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("SpecialAccessModel.update"));
		
		p.setInt(1, specialAccess.getForumId());
		p.setInt(2, specialAccess.getTopicId());
		
		if (specialAccess.getStartDate() == null)
		{
			p.setTimestamp(3, null);
		}
		else
		{
			p.setTimestamp(3, new Timestamp(specialAccess.getStartDate().getTime()));
		}
		
		if (specialAccess.getEndDate() == null)
		{
			p.setTimestamp(4, null);
		}
		else
		{
			p.setTimestamp(4, new Timestamp(specialAccess.getEndDate().getTime()));		  
		}
		p.setInt(5, specialAccess.isLockOnEndDate() ? 1 : 0);
		p.setInt(6, specialAccess.isOverrideStartDate() ? 1 : 0);
		p.setInt(7, specialAccess.isOverrideEndDate() ? 1 : 0);
		p.setInt(8, specialAccess.isOverrideLockEndDate() ? 1 : 0);
		p.setString(9, getUserIdString(specialAccess.getUserIds()));
		p.setInt(10, specialAccess.getId());

		p.executeUpdate();
		
		p.close();

	}
	
	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#delete(int)
	 */
	public void delete(int specialAccessId) throws Exception
	{
		//delete special access
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("SpecialAccessModel.deleteById"));
		p.setInt(1, specialAccessId);

		p.executeUpdate();

		p.close();
		
	}

	/**
	 * fill special access from the result set
	 * @param rs	ResultSet
	 * @return		SpecialAccess
	 * @throws Exception
	 */
	protected SpecialAccess fillSpecialAccess(ResultSet rs) throws Exception
	{
		SpecialAccess specialAccess = new SpecialAccess();
		 
		specialAccess.setId(rs.getInt("special_access_id"));
		specialAccess.setForumId(rs.getInt("forum_id"));
		specialAccess.setTopicId(rs.getInt("topic_id"));
		
		if (rs.getDate("start_date") != null)
		{
		  Timestamp startDate = rs.getTimestamp("start_date");
		  specialAccess.setStartDate(startDate);
		  SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		  specialAccess.setStartDateFormatted(df.format(startDate));
	    }
	    else
	    {
	    	specialAccess.setStartDate(null);
	    }
		
		if (rs.getDate("end_date") != null)
	    {
	      Timestamp endDate = rs.getTimestamp("end_date");
	      specialAccess.setEndDate(endDate);
		  SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		  specialAccess.setEndDateFormatted(df.format(endDate));
	    }
	    else
	    {
	    	specialAccess.setEndDate(null);
	    }
		
		if (rs.getDate("allow_until_date") != null)
	    {
	      Timestamp allowUntilDate = rs.getTimestamp("allow_until_date");
	      specialAccess.setAllowUntilDate(allowUntilDate);
	    }
	    else
	    {
	    	specialAccess.setAllowUntilDate(null);
	    }
		
		specialAccess.setOverrideStartDate(rs.getInt("override_start_date") == 1);
		specialAccess.setOverrideEndDate(rs.getInt("override_end_date") == 1);
		//specialAccess.setOverrideLockEndDate(rs.getInt("override_lock_end_date") == 1);
		specialAccess.setOverrideAllowUntilDate(rs.getInt("override_allow_until_date") == 1);
		
		//specialAccess.setLockOnEndDate(rs.getInt("lock_end_date") > 0);
		
		List<Integer> userIds = getUserIdList(getSpecialAccessUsersFromResultSet(rs));
		specialAccess.setUserIds(userIds);
		
		specialAccess.setUsers(getUsersList(userIds));
		
		return specialAccess;
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
	 * Gets the user id list from the used id string
	 * @param userIds		User id's string
	 * @return
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
	 * Gets the users list from user id's
	 * @param userIds		List of user id's
	 * @return				List of Users
	 * @throws Exception
	 */
	protected List<User> getUsersList(List<Integer> userIds)throws Exception
	{
		List<User> usersList = new ArrayList<User>();
		
		if ((userIds == null) || (userIds.size() == 0))
			return usersList;
		
		for (Integer userId : userIds)
		{
			User user  = DataAccessDriver.getInstance().newUserDAO().selectById(userId);
			usersList.add(user);
		}
		return usersList;
	}
	
	/**
	 * Utility method to read the post text from the result set.
	 * This method may be useful when using some "non-standard" way
	 * to store text, like oracle does when using (c|b)lob
	 * 
	 * @param rs The result set to fetch data from
	 * @return The comments string
	 * @throws Exception
	 */
	protected String getSpecialAccessUsersFromResultSet(ResultSet rs) throws Exception
	{
		return rs.getString("users");
	}

	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#selectByTopic(int, int)
	 */
	public List<SpecialAccess> selectByTopic(int forumId, int topicId) throws Exception
	{
		List<SpecialAccess> specialAccessList = new ArrayList<SpecialAccess>();
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("SpecialAccessModel.selectSpecialAccessByForumTopic"));
		p.setInt(1, forumId);
		p.setInt(2, topicId);
		
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			specialAccessList.add(this.fillSpecialAccess(rs));
		}

		rs.close();
		p.close();
		
		return specialAccessList;
	}

	/**
	 * @see org.etudes.jforum.dao.SpecialAccessDAO#selectTopicSpecialAccessCount(int, int)
	 */
	public int selectTopicSpecialAccessCount(int forumId, int topicId) throws Exception
	{
		if (topicId == 0)
		{
			throw new IllegalArgumentException("Topic id should be greater than zero.");
		}
		
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("SpecialAccessModel.selectForumTopicSpecialAccessCount"));
		p.setInt(1, forumId);
		p.setInt(2, topicId);
		ResultSet rs = p.executeQuery();
		
		int count = 0;
		if (rs.next()) {
			count = rs.getInt("special_access_count");
		}

		rs.close();
		p.close();
		return count;
	}
}
