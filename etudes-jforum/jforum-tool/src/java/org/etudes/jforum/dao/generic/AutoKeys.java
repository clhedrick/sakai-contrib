/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/dao/generic/AutoKeys.java $ 
 * $Id: AutoKeys.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008 Etudes, Inc. 
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
 * Portions completed before July 1, 2004 Copyright (c) 2003, 2004 Rafael Steil, All rights reserved, licensed under the BSD license. 
 * http://www.opensource.org/licenses/bsd-license.php 
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met: 
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following disclaimer. 
 * 2) Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution. 
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE 
 ***********************************************************************************/
package org.etudes.jforum.dao.generic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import org.etudes.jforum.JForum;
import org.etudes.jforum.util.JForumUtil;
import org.etudes.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 */
public class AutoKeys
{

//	Since we no longer configure the DB here, we can't count on knowing whether to use auto-generated keys -- JMH
//	private static boolean supportAutoGeneratedKeys = SystemGlobals.getBoolValue(ConfigKeys.DATABASE_AUTO_KEYS);
	private String autoGeneratedKeysQuery;
	
	
	protected boolean supportAutoGeneratedKeys()
	{
		return JForumUtil.isAutoKeyGenerationSupported();
	}

	/**
	 * Query to execute when {@link #setSupportAutoGeneratedKey(boolean)} is set
	 * to <code>false</code>
	 * @param query The query to execute to retreive the last generated key
	 */
	protected void setAutoGeneratedKeysQuery(String query)
	{
		this.autoGeneratedKeysQuery = query;
	}
	
	protected String getAutoGeneratedKeysQuery()
	{
		return this.autoGeneratedKeysQuery;
	}
	
	protected String getErrorMessage()
	{
		return "Could not obtain the latest generated key. This error may be associated"
			+" to some invalid database driver operation or server failure."
			+" Please check the database configurations and code logic.";
	}
	
	protected PreparedStatement getStatementForAutoKeys(String queryName, Connection conn) throws SQLException
	{
		PreparedStatement p = null;
		// TODO Sakai can not count on auto-generated keys, since Oracle doesn't support it.  Consider removing all of the auto-key cruft -- JMH
		if (this.supportAutoGeneratedKeys()) {
			p = conn.prepareStatement(SystemGlobals.getSql(queryName),
				Statement.RETURN_GENERATED_KEYS);
		}
		else {
			p = conn.prepareStatement(SystemGlobals.getSql(queryName));
		}
		
		return p;
	}
	
	protected PreparedStatement getStatementForAutoKeys(String queryName) throws SQLException
	{
		return this.getStatementForAutoKeys(queryName, JForum.getConnection());
	}
	
	protected int executeAutoKeysQuery(PreparedStatement p) throws SQLException
	{
		return this.executeAutoKeysQuery(p, JForum.getConnection());
	}
	
	protected int executeAutoKeysQuery(PreparedStatement p, Connection conn) throws SQLException
	{
		int id = -1;
		p.executeUpdate();
		
//		if (this.supportAutoGeneratedKeys()) {
//			ResultSet rs = p.getGeneratedKeys();
//			if (rs.next()) {
//				id = rs.getInt(1);
//			}
//			rs.close();
//		}
//		else {
			p = conn.prepareStatement(this.getAutoGeneratedKeysQuery());
			ResultSet rs = p.executeQuery();
			
			if (rs.next()) {
				id = rs.getInt(1);
			}
			
			rs.close();
//		}
	
		if (id == -1) {
			throw new SQLException(this.getErrorMessage());
		}
		
		return id;
	}
}