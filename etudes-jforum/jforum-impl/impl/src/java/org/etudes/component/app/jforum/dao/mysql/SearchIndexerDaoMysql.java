/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/mysql/SearchIndexerDaoMysql.java $ 
 * $Id: SearchIndexerDaoMysql.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.dao.mysql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Post;
import org.etudes.component.app.jforum.dao.generic.SearchIndexerDaoGeneric;

public class SearchIndexerDaoMysql extends SearchIndexerDaoGeneric
{
private static Log logger = LogFactory.getLog(SearchIndexerDaoMysql.class);
	
	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void insertPostSearchWords(Post post, String word, int hash)
	{
		String insertSql = "INSERT INTO jforum_search_words ( word_hash, word ) VALUES (?, ?)";
		int insertFieldsCount = 0;
		Object[] fieldsInsert = new Object[2];
		
		fieldsInsert[insertFieldsCount++] = hash;
		fieldsInsert[insertFieldsCount++] = word;
		
		Long wordId = null;
		try
		{
			wordId = this.sqlService.dbInsert(null, insertSql, fieldsInsert, "word_id");
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
		}
		
		if (wordId == null)
		{
			throw new RuntimeException("insert jforum search words: dbInsert failed");
		}
		else
		{			
			this.associateWordToPost(word, wordId.intValue(), post);
		}
	}

}
