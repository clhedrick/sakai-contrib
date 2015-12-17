/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumSearchIndexingExecutorServiceImpl.java $ 
 * $Id: JForumSearchIndexingExecutorServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.JForumEmailService;
import org.etudes.api.app.jforum.JForumSearchIndexingExecutorService;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.dao.SearchIndexerDao;

public class JForumSearchIndexingExecutorServiceImpl implements JForumSearchIndexingExecutorService
{
	protected class PostInfo
	{
		int id;
		
		PostInfoAction postInfoAction;

		String subject;

		String text;
		
		PostInfo(int id, String subject, String text, PostInfoAction postInfoAction)
		{
			this.id = id;
			this.subject = subject;
			this.text = text;
			this.postInfoAction = postInfoAction;
		}

		/**
		 * @return the id
		 */
		int getId()
		{
			return id;
		}

		/**
		 * @return the postInfoAction
		 */
		PostInfoAction getPostInfoAction()
		{
			return postInfoAction;
		}

		/**
		 * @return the subject
		 */
		String getSubject()
		{
			return subject;
		}

		/**
		 * @return the text
		 */
		String getText()
		{
			return text;
		}
	}
	
	protected enum PostInfoAction
	{
		add, clean;
	}
	
	protected class searchIndexWorker implements Runnable 
	{
		public void run() 
		{
			try
			{
				while (true)
				{
					
					PostInfo postInfo = null;
					
					synchronized(postQueue)
					{
						if(postQueue.isEmpty()) 
						{
							break;
						}
						
						postInfo = postQueue.poll();
					}
					
					if (postInfo != null)
					{
						processIndexing(postInfo);
					}
				}
			}
			catch (Throwable e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("searchIndexWorker: "+e.toString(), e);
				}
			}
			finally
			{
				cleanup();
			}
		}
		
		protected void cleanup() 
		{
			synchronized (lock)
			{
				currentThread = null;
				if (logger.isDebugEnabled())
				{
					logger.debug("Cleaning up the JForum searchIndexWorker thread...");
				}
			}
		}
		
	}

	private static Log logger = LogFactory.getLog(JForumSearchIndexingExecutorServiceImpl.class);
	
	private final Object lock = new Object();
	
	protected Thread currentThread = null;
	
	
	protected JForumEmailService jforumEmailService;

	/** The queue of posts. */
	protected LinkedList<PostInfo> postQueue = new LinkedList<PostInfo>();
	
	/** Dependency: SearchIndexerDao. */
	protected SearchIndexerDao searchIndexerDao = null;
	
	/**
	 * {@inheritDoc}
	 */
	public void cleanIndex(Post post)
	{
		if (post == null || ((post.getSubject() == null) && (post.getText() == null)))
		{
			return;
		}
		
		PostInfo postInfoToClean = new PostInfo(post.getId(), post.getSubject(), post.getText(), PostInfoAction.clean);

		addToQueue(postInfoToClean);
		
		startIndexWorker();
	}

	public void destroy()
	{
		if (postQueue.size() > 0)
		{
			logger.warn(".destroy(): postQueue has items in jforum indexing post queue");
		}
		postQueue.clear();
		
		currentThread = null;
		
		if (logger.isInfoEnabled())
		{
			logger.info("destroy....");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void indexPost(Post post)
	{
		if (post == null || ((post.getSubject() == null) && (post.getText() == null)))
		{
			return;
		}
		
		PostInfo postInfoToIndex = new PostInfo(post.getId(), post.getSubject(), post.getText(), PostInfoAction.add);

		addToQueue(postInfoToIndex);
		
		startIndexWorker();
	}
	
	public void init()
	{
		if (logger.isInfoEnabled())
		{
			logger.info("init....");
		}
		currentThread = null;
		
		postQueue.clear();
	}

	/**
	 * @param searchIndexerDao the searchIndexerDao to set
	 */
	public void setSearchIndexerDao(SearchIndexerDao searchIndexerDao)
	{
		this.searchIndexerDao = searchIndexerDao;
	}
	
	/**
	 * Add to queue
	 * 
	 * @param PostInfoToClean	
	 */
	protected void addToQueue(PostInfo postInfo)
	{
		synchronized(postQueue)
		{
			postQueue.add(postInfo);
			
			if (logger.isDebugEnabled())
			{
				logger.debug("postQueue size is:"+ postQueue.size());
			}
		}
	}
	
	/**
	 * process post indexing for search
	 * 
	 * @param postInfo	Post info
	 */
	protected void processIndexing(PostInfo postInfo)
	{
		if (postInfo != null)
		{
			if (postInfo.getPostInfoAction() == PostInfoAction.add)
			{
				Post post = new PostImpl(postInfo.getId(), postInfo.getSubject(), postInfo.getText());
				this.searchIndexerDao.insertSearchWords(post);
			}
			else if (postInfo.getPostInfoAction() == PostInfoAction.clean)
			{
				Post post = new PostImpl(postInfo.getId(), postInfo.getSubject(), postInfo.getText());
				this.searchIndexerDao.deleteSearchWords(post);
			}
		}
	}
	
	/**
	 * Stars the worker thread
	 */
	protected void startIndexWorker()
	{
		synchronized (lock)
		{
			if (currentThread == null)
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Creating a new JForum searchIndexWorker thread...");
				}

				currentThread = new Thread(new searchIndexWorker(), "jforum-search-index");
				currentThread.setDaemon(true);
				currentThread.start();
			}
		}
	}
}
