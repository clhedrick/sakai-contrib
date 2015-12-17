/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/util/post/EmailUtil.java $ 
 * $Id: EmailUtil.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.util.post;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmailUtil
{
	private static Log logger = LogFactory.getLog(EmailUtil.class);

	/**
	 * Formats the topic reply email notification message
	 * 
	 * @param params	Message text params topic.title(topic title), site.path(site title), server.url(server url), site.title(site title), post.from (post from), post.subject , post.text
	 * 
	 * @return	Formatted topic reply email notification message
	 */
	public static String getNewReplyMessageText(Map<String, String> params)
	{
		String messageFile = "email/mailNewReply.txt";
		
		return buildNewReplyMessageText(params, messageFile);
	}

	/**
	 * Formats the new topic email notification message
	 * 
	 * @param params	Message text params topic.title(topic title), site.path(site title), server.url(server url), site.title(site title), topic.from (topic from), post.subject , post.text
	 *  
	 * @return	Formatted new topic email notification message
	 */
	public static String getNewTopicMessageText(Map<String, String> params)
	{
		String messageFile = "email/mailNewTopic.txt";
		
		return buildNewTopicMessageText(params, messageFile);
	}
	
	/**
	 * Formats the private message text for email notification
	 * 
	 * @param params	Message text params pm.to (to user name), pm.from (from user name), pm.subject, pm.text, site.title, server.url
	 * 
	 * @return	Formatted private message text
	 */
	public static String getPrivateMessageText(Map<String, String> params)
	{
		String messageFile = "email/newPrivateMessage.txt";
		
		return buildPrivateMessageText(params, messageFile);
	}
	
	/**
	 * Builds topic reply email notification from the message template file
	 * 
	 * @param params		Message text params topic.title(topic title), site.path(site title), server.url(server url), site.title(site title), post.from (post from), post.subject , post.text
	 * 
	 * @param messageFile	Message file
	 * 
	 * @return	Formatted topic reply email notification
	 */
	protected static String buildNewReplyMessageText(Map<String, String> params, String messageFile)
	{
		StringBuilder contents = new StringBuilder();
		BufferedReader input = null;
		String message = null;
		InputStream inputStream = null;

		try
		{
			inputStream = EmailUtil.class.getClassLoader().getResourceAsStream(messageFile);
			
			input = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = null;
				
			while ((line = input.readLine()) != null)
			{
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
			
			message = contents.toString();
			
			String topicTitle = params.get("topic.title");
			if (topicTitle != null)
			{
				message = message.replace("topic.title", topicTitle);
			}
			else
			{
				message = message.replace("topic.title", "");
			}
			
			String siteUrl =  params.get("site.url");
			if (siteUrl != null)
			{
				message = message.replace("site.url", siteUrl);
			}
			else
			{
				message = message.replace("site.url", "");
			}
						
			String siteTitle =  params.get("site.title");
			if (siteTitle != null)
			{
				message = message.replace("site.title", siteTitle);
			}
			else
			{
				message = message.replace("site.title", "");
			}
			
			String postFrom =  params.get("post.from");
			if (postFrom != null)
			{
				message = message.replace("post.from", postFrom);
			}
			else
			{
				message = message.replace("post.from", "");
			}
			
			String postSubject =  params.get("post.subject");
			if (postSubject != null)
			{
				message = message.replace("post.subject", postSubject);
			}
			else
			{
				message = message.replace("post.subject", "");
			}
			
			String postText =  params.get("post.text");
			if (postText != null)
			{
				message = message.replace("post.text", postText);
			}
			else
			{
				message = message.replace("post.text", "");
			}
		}
		catch(Exception e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("Error in getNewReplyMessageText", e);
			} 
		}
		finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e)
				{
				}
			}
			
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					
				}
			}
		}
		
		return message;
	}
	
	/**
	 * Builds new topic message from the message template file
	 * 
	 * @param params	Message text params topic.title(topic title), site.path(site title), server.url(server url), site.title(site title), topic.from (topic from), post.subject , post.text 
	 * 
	 * @param messageFile	Message file
	 * 
	 * @return	Formatted new topic email notification text
	 */
	protected static String buildNewTopicMessageText(Map<String, String> params, String messageFile)
	{
		StringBuilder contents = new StringBuilder();
		BufferedReader input = null;
		String message = null;
		InputStream inputStream = null;

		try
		{
			inputStream = EmailUtil.class.getClassLoader().getResourceAsStream(messageFile);
			
			input = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = null;
				
			while ((line = input.readLine()) != null)
			{
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
			
			message = contents.toString();
			
			String topicTitle = params.get("topic.title");
			if (topicTitle != null)
			{
				message = message.replace("topic.title", topicTitle);
			}
			else
			{
				message = message.replace("topic.title", "");
			}
			
			String siteUrl =  params.get("site.url");
			if (siteUrl != null)
			{
				message = message.replace("site.url", siteUrl);
			}
			else
			{
				message = message.replace("site.url", "");
			}
			
			String portalUrl =  params.get("portal.url");
			if (portalUrl != null)
			{
				message = message.replace("portal.url", portalUrl);
			}
			else
			{
				message = message.replace("portal.url", "");
			}
			
			String siteTitle =  params.get("site.title");
			if (siteTitle != null)
			{
				message = message.replace("site.title", siteTitle);
			}
			else
			{
				message = message.replace("site.title", "");
			}
			
			String topicFrom =  params.get("topic.from");
			if (topicFrom != null)
			{
				message = message.replace("topic.from", topicFrom);
			}
			else
			{
				message = message.replace("topic.from", "");
			}
			
			String postSubject =  params.get("post.subject");
			if (postSubject != null)
			{
				message = message.replace("post.subject", postSubject);
			}
			else
			{
				message = message.replace("post.subject", "");
			}
			
			String postText =  params.get("post.text");
			if (postText != null)
			{
				message = message.replace("post.text", postText);
			}
			else
			{
				message = message.replace("post.text", "");
			}
		}
		catch(Exception e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("Error in getNewReplyMessageText", e);
			} 
		}
		finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e)
				{
				}
			}
			
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					
				}
			}
		}
		
		return message;
	}
	
	/**
	 * Builds private message from the message template file
	 * 
	 * @param params Message text params pm.to (to user name), pm.from (from user name), pm.subject, pm.text, site.title, server.url
	 * 
	 * @param messageFile	Private message template file
	 * 
	 * @return Formatted private message text
	 */
	protected static String buildPrivateMessageText(Map<String, String> params, String messageFile)
	{
		StringBuilder contents = new StringBuilder();
		BufferedReader input = null;
		String message = null;
		InputStream inputStream = null;

		try
		{
			inputStream = EmailUtil.class.getClassLoader().getResourceAsStream(messageFile);
			
			input = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = null;
				
			while ((line = input.readLine()) != null)
			{
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
			
			message = contents.toString();
			
			String pmTo = params.get("pm.to");
			if (pmTo != null)
			{
				message = message.replace("pm.to", pmTo);
			}
			else
			{
				message = message.replace("pm.to", "");
			}
			
			String pmFrom =  params.get("pm.from");
			if (pmFrom != null)
			{
				message = message.replace("pm.from", pmFrom);
			}
			else
			{
				message = message.replace("pm.from", "");
			}
			
			String pmSubject =  params.get("pm.subject");
			if (pmSubject != null)
			{
				message = message.replace("pm.subject", pmSubject);
			}
			else
			{
				message = message.replace("pm.subject", "");
			}
			
			String pmText =  params.get("pm.text");
			if (pmText != null)
			{
				message = message.replace("pm.text", pmText);
			}
			else
			{
				message = message.replace("pm.text", "");
			}
			
			String siteTitle =  params.get("site.title");
			if (siteTitle != null)
			{
				message = message.replace("site.title", siteTitle);
			}
			else
			{
				message = message.replace("site.title", "");
			}
			
			String portalUrl =  params.get("portal.url");
			if (portalUrl != null)
			{
				message = message.replace("portal.url", portalUrl);
			}
			else
			{
				message = message.replace("portal.url", "");
			}
			
			String siteUrl =  params.get("site.url");
			if (siteUrl != null)
			{
				message = message.replace("site.url", siteUrl);
			}
			else
			{
				message = message.replace("site.url", "");
			}
			
		}
		catch(Exception e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("Error in getPrivateMessageText", e);
			} 
		}
		finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e)
				{
				}
			}
			
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					
				}
			}
		}
		
		return message;
	}
	
	private EmailUtil()
	{
	}

}
