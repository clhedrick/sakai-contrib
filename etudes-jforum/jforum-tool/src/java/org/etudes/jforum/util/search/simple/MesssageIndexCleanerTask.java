/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/util/search/simple/MesssageIndexCleanerTask.java $ 
 * $Id: MesssageIndexCleanerTask.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.jforum.util.search.simple;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.jforum.DBConnection;
import org.etudes.jforum.dao.DataAccessDriver;
import org.etudes.jforum.dao.SearchIndexerDAO;
import org.etudes.jforum.entities.Post;
import org.etudes.jforum.util.concurrent.Task;

public class MesssageIndexCleanerTask implements Task
{
	private static Log logger = LogFactory.getLog(MesssageIndexCleanerTask.class);
	private Connection conn;
	private Post post;

	public MesssageIndexCleanerTask(Post post) throws Exception
	{
		this.post = post;
		this.conn = DBConnection.getImplementation().getConnection();
	}

	public Object execute() throws Exception
	{
		try
		{
			SearchIndexerDAO indexer = DataAccessDriver.getInstance().newSearchIndexerDAO();
			indexer.setConnection(this.conn);
			indexer.deleteSearchWords(this.post);
		}
		catch (Exception e)
		{
			logger.warn("Error while cleaning the indexes of a post: " + e, e);
		}
		finally
		{
			if (this.conn != null)
			{
				try
				{
					DBConnection.getImplementation().releaseConnection(this.conn);
				}
				catch (Exception e)
				{
				}
			}
		}

		return null;
	}

}
