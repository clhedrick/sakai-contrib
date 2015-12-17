/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/dao/SearchIndexerDao.java $ 
 * $Id: SearchIndexerDao.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum.dao;

import java.util.List;

import org.etudes.api.app.jforum.Post;

public interface SearchIndexerDao
{
	public static final int SEARCH_MIN_WORD_SIZE = 3;
	
	/**
	 * Indexes a set of posts.
	 * 
	 * @param posts 	The posts to index
	 */
	public void insertSearchWords(List<Post> posts);

	/**
	 * Indexes a post
	 * 
	 * @param post		The post to index
	 */
	public void insertSearchWords(Post post);

	/**
	 * Deletes the indexes of the posts.
	 * 
	 * @param posts		The posts
	 */
	public void deleteSearchWords(List<Post> posts);

	/**
	 * Deletes the indexes of a post.
	 * 
	 * @param post		The post
	 */
	public void deleteSearchWords(Post post);
}
