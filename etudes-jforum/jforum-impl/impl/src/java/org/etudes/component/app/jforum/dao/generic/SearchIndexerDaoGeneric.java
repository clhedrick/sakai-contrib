/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/SearchIndexerDaoGeneric.java $ 
 * $Id: SearchIndexerDaoGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.dao.SearchIndexerDao;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;

public abstract class SearchIndexerDaoGeneric implements SearchIndexerDao
{
	private static Log logger = LogFactory.getLog(SearchIndexerDaoGeneric.class);

	/** Dependency: SqlService */
	protected SqlService sqlService = null;

	/**
	 * {@inheritDoc}
	 */
	public void deleteSearchWords(List<Post> posts)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteSearchWords(Post post)
	{
		String sql = "DELETE FROM jforum_search_wordmatch WHERE post_id = ?";
		int i = 0;
		Object[] fields = new Object[1];
		fields[i++] = post.getId();
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("deleteSearchWords: db write failed");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertSearchWords(List<Post> posts)
	{
		int minWordSize = SEARCH_MIN_WORD_SIZE;
		
		Pattern pattern = Pattern.compile("[\\.\\\\\\/~^&\\(\\)-_+=!@#$%\"\'\\[\\]\\{\\}?<:>,*¡"
				+ "A¡B¡C¡D¡E¡F¡G¡H¡I¡J¡K¡L¡U¡Z¡]¡^¡a¡b¡e¡f¡i¡j¡m¡n¡q¡r¡u¡v¡y¡z¡£¡¤¡¥¡¦¡§¡¨¡©¡ª¡«¡¬\n\r\t]");

		// Process all posts
		for (Post post : posts)
		{
			insertPostSearchWords(minWordSize, pattern, post);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void insertSearchWords(Post post)
	{
		List<Post> posts = new ArrayList<Post>();
		posts.add(post);
		
		this.insertSearchWords(posts);
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
	 * Associate word to post
	 * 
	 * @param word		The word
	 * 
	 * @param wordId	The word id
	 * 
	 * @param post		The post
	 */
	protected void associateWordToPost(String word, int wordId, Post post)
	{
		String sql = "INSERT INTO jforum_search_wordmatch (post_id, word_id, title_match) VALUES (?, ?, ?)";
		int i = 0;
		Object[] fields = new Object[3];
		fields[i++] = post.getId();
		fields[i++] = wordId;
		
		String subject = post.getSubject();
		int inSubject = 0;
		if (subject != null && !subject.equals(""))
		{
			inSubject = subject.indexOf(word) > -1 ? 1 : 0;
		}
		
		fields[i++] = inSubject;
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("associateWordToPost: db write failed");
		}
	}

	/**
	 * Check then if the current post is not already associated to the word associate the word with post
	 * 
	 * @param post
	 * @param word
	 * @param existingWords
	 */
	protected void checkAndInsertPostSearchWords(Post post, String word, List<Integer> existingWords)
	{
		int wordId = existingWords.get(0).intValue();
		
		String existingAssociationSql = "SELECT post_id FROM jforum_search_wordmatch WHERE word_id = ? AND post_id = ?";
		int existingAssociationFieldsCount = 0;
		Object[] existingAssociationFields = new Object[2];
		
		existingAssociationFields[existingAssociationFieldsCount++] = wordId;
		existingAssociationFields[existingAssociationFieldsCount++] = post.getId();
		
		final List<Integer> existingPosts = new ArrayList<Integer>();
		
		this.sqlService.dbRead(existingAssociationSql, existingAssociationFields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					existingPosts.add(result.getInt("post_id"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("insertSearchWords(): Error while reading exising Association: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (existingPosts.size() == 0)
		{
			this.associateWordToPost(word, wordId, post);
		}
	}

	/**
	 * Save post text words 
	 * 
	 * @param minWordSize	Minimum size of the word to consider
	 * 
	 * @param pattern	Pattern
	 * 
	 * @param post	Post
	 */
	protected void insertPostSearchWords(int minWordSize, Pattern pattern, Post post)
	{
		String str = post.getText() + " " + post.getSubject();
		str = pattern.matcher(str).replaceAll(" ");

		StringTokenizer tok = new StringTokenizer(str, " ");
		while (tok.hasMoreTokens())
		{
			String word = tok.nextToken().trim();

			// Skip words less than "n" chars
			if (word.length() < minWordSize)
			{
				continue;
			}

			// Truncate words longer than 100 chars
			if (word.length() > 100)
			{
				word = word.substring(0, 100);
			}			
			
			insertSearchWordTx(post, word);
		}
	}
	
	/**
	 * Insert post search words to database
	 * 
	 * @param post	Post
	 * 
	 * @param word	Word
	 * 
	 * @param hash	Word hash
	 */
	protected abstract void insertPostSearchWords(Post post, String word, int hash);
	
	/**
	 * Insert post words
	 * 
	 * @param post	Post
	 * 
	 * @param word	Word
	 */
	protected void insertSearchWordTx(final Post post, final String word)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					// Verify if the current word is not in the database before proceeding
					int hash = word.hashCode();
					
					List<Integer> existingWords = selectExistingWords(hash);
					
					if (existingWords.size() == 0)
					{
						// The word is not in the database. Insert it now
						insertPostSearchWords(post, word, hash);
					}
					else
					{
						// The word is already in the database ( jforum_search_words )
						// Check then if the current post is not already associated to the word
						checkAndInsertPostSearchWords(post, word, existingWords);
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error("Error while inserting post search words. "+ e.toString(), e);
					}
					
					//throw new RuntimeException("Error while inserting post search words.", e);
				}
			}
		}, "insertSearchWords: " + post.getId());
				
	}
	
	/**
	 * Select existing words for the give hash
	 * 
	 * @param hash	Word hash
	 * 
	 * @return	The list of existing words
	 */
	protected List<Integer> selectExistingWords(int hash)
	{
		String existingSql = "SELECT w.word_id FROM jforum_search_words w WHERE w.word_hash = ?";
		int existingFieldsCount = 0;
		Object[] existingFields = new Object[1];
		existingFields[existingFieldsCount++] = hash;
		
		final List<Integer> existingWords = new ArrayList<Integer>();
		
		this.sqlService.dbRead(existingSql, existingFields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					existingWords.add(result.getInt("word_id"));
	
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectExistingWords(): Error while reading exising search words:  " + e, e);
					}
					return null;
				}
			}
		});
		
		return existingWords;
	}

}
