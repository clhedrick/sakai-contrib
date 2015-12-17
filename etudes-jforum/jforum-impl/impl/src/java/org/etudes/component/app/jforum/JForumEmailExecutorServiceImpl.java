/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumEmailExecutorServiceImpl.java $ 
 * $Id: JForumEmailExecutorServiceImpl.java 83603 2013-05-06 21:14:40Z murthy@etudes.org $ 
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.AttachmentInfo;
import org.etudes.api.app.jforum.EmailAttachment;
import org.etudes.api.app.jforum.JForumEmailExecutorService;
import org.etudes.api.app.jforum.JForumEmailService;

public class JForumEmailExecutorServiceImpl implements JForumEmailExecutorService
{
	/**
	 * Sends email 
	 */
	protected class EmailWorker implements Runnable 
	{
		public void run() 
		{
			try
			{
				while (true)
				{
					
					MessageInfo messageInfo = null;
					
					synchronized(messageQueue)
					{
						if(messageQueue.isEmpty()) 
						{
							break;
						}
						
						messageInfo = messageQueue.poll();
					}
					
					processEmail(messageInfo);
				}
			}
			catch (Throwable e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("EmailWorker: "+e.toString(), e);
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
				if (logger.isInfoEnabled())
				{
					logger.info("Cleaning up the JForum EmailWorker thread...");
				}
			}
		}
		
	}
	
	/**
	 * Message Attachment
	 * 
	 *
	 */
	protected class MessageAttachment
	{
		String physicalFilename = null;
		String realFilename = null;

		MessageAttachment(String realFilename, String physicalFilename)
		{
			this.realFilename = realFilename;
			this.physicalFilename = physicalFilename;			
		}

		/**
		 * @return the physicalFilename
		 */
		String getPhysicalFilename()
		{
			return physicalFilename;
		}

		/**
		 * @return the realFilename
		 */
		String getRealFilename()
		{
			return realFilename;
		}

		/**
		 * @param physicalFilename the physicalFilename to set
		 */
		void setPhysicalFilename(String physicalFilename)
		{
			this.physicalFilename = physicalFilename;
		}		
		
		/**
		 * @param realFilename the realFilename to set
		 */
		void setRealFilename(String realFilename)
		{
			this.realFilename = realFilename;
		}
	}
	
	/**
	 * Mesage info
	 *
	 */
	protected class MessageInfo
	{
		String content;
		
		InternetAddress from;

		List<MessageAttachment> messageAttachments = null;

		String subject;

		InternetAddress	to[];

		MessageInfo(InternetAddress from, InternetAddress to[], String subject, String content, List<MessageAttachment> messageAttachments)
		{
			this.from = from;
			this.to = to;
			this.subject = subject;
			this.content = content;
			this.messageAttachments = messageAttachments;
		}

		/**
		 * @return the content
		 */
		String getContent()
		{
			return content;
		}

		/**
		 * @return the from
		 */
		InternetAddress getFrom()
		{
			return from;
		}

		/**
		 * @return the messageAttachments
		 */
		List<MessageAttachment> getMessageAttachments()
		{
			return messageAttachments;
		}

		/**
		 * @return the subject
		 */
		String getSubject()
		{
			return subject;
		}

		/**
		 * @return the to
		 */
		InternetAddress[] getTo()
		{
			return to;
		}

		/**
		 * @param content the content to set
		 */
		void setContent(String content)
		{
			this.content = content;
		}
		
		/**
		 * @param from the from to set
		 */
		void setFrom(InternetAddress from)
		{
			this.from = from;
		}
		
		/**
		 * @param messageAttachments the messageAttachments to set
		 */
		void setMessageAttachments(List<MessageAttachment> messageAttachments)
		{
			this.messageAttachments = messageAttachments;
		}
		
		/**
		 * @param subject the subject to set
		 */
		void setSubject(String subject)
		{
			this.subject = subject;
		} 
		
		/**
		 * @param to the to to set
		 */
		void setTo(InternetAddress[] to)
		{
			this.to = to;
		}
	}

	private static Log logger = LogFactory.getLog(JForumEmailExecutorServiceImpl.class);
	
	private final Object lock = new Object();
	
	protected Thread currentThread = null;
	
	protected JForumEmailService jforumEmailService;
	
	/** The queue of messages. */
	protected LinkedList<MessageInfo> messageQueue = new LinkedList<MessageInfo>();
	
	public void destroy()
	{
		if (messageQueue.size() > 0)
		{
			logger.warn(".destroy(): messageQueue has items in jforum email queue");
		}
		messageQueue.clear();
		
		currentThread = null;
		
		if (logger.isInfoEnabled())
		{
			logger.info("destroy....");
		}
	}
	
	public void init()
	{
		if (logger.isInfoEnabled())
		{
			logger.info("init....");
		}
		currentThread = null;
		
		messageQueue.clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void notifyUsers(InternetAddress from, InternetAddress[] to, String subject, String content)
	{
		notifyUsers(from, to, subject, content, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void notifyUsers(InternetAddress from, InternetAddress[] to, String subject, String content, List<Attachment> attachments)
	{
		if (from == null || to == null || subject == null || content == null)
		{
			return;
		}
		
		List<MessageAttachment> messageAttachments = null;
		
		if (attachments != null && attachments.size() > 0)
		{
			messageAttachments =  new ArrayList<MessageAttachment>();

			String realFilename = null;
			String physicalFilename = null;			
			
			MessageAttachment messageAttachment = null;
			for (Attachment attachment : attachments)
			{
				realFilename = null;
				physicalFilename = null;				
				
				messageAttachment = null;
				AttachmentInfo attachmentInfo = attachment.getInfo();
				
				if (attachmentInfo != null)
				{
					realFilename = attachmentInfo.getRealFilename();
					physicalFilename = attachmentInfo.getPhysicalFilename();
					
					
					if ((physicalFilename != null && physicalFilename.trim().length() > 0) && (realFilename != null && realFilename.trim().length() > 0))
					{
						messageAttachment = new MessageAttachment(realFilename, physicalFilename);
						messageAttachments.add(messageAttachment);
					}
				}
			}
			
		}
		
		MessageInfo messageInfo = new MessageInfo(from, to, subject, content, messageAttachments);
		
		synchronized(messageQueue)
		{
			messageQueue.add(messageInfo);
			
			if (logger.isDebugEnabled())
			{
				logger.debug("messageQueue size is:"+ messageQueue.size());
			}
		}
		
		synchronized (lock)
		{
			if (currentThread == null)
			{
				if (logger.isInfoEnabled())
				{
					logger.info("Creating a new JForum EmailWorker thread...");
				}

				currentThread = new Thread(new EmailWorker(), "jforum-email");
				//currentThread.setDaemon(true);
				currentThread.start();
			}
		}
	}
	
	/**
	 * @param jforumEmailService the jforumEmailService to set
	 */
	public void setJforumEmailService(JForumEmailService jforumEmailService)
	{
		this.jforumEmailService = jforumEmailService;
	}

	/**
	 * process email
	 * 
	 * @param messageInfo	Message info
	 */
	protected void processEmail(MessageInfo messageInfo)
	{
		if (messageInfo == null)
		{
			return;
		}
		
		List<String> additionalHeaders = new ArrayList<String>();
		additionalHeaders.add("content-type: text/html");

		List<EmailAttachment> emailAttachments = new ArrayList<EmailAttachment>();
		// send email
		if ((messageInfo.messageAttachments != null) && (messageInfo.messageAttachments.size() > 0))
		{
			emailAttachments = new ArrayList<EmailAttachment>();
			
			String physicalFileName = null;
			String realFileName = null;
			
			EmailAttachment emailAttachment = null; 
			for (MessageAttachment messageAttachment : messageInfo.messageAttachments)
			{
				physicalFileName = null;
				realFileName = null;
				
				emailAttachment = null; 
				
				if ((messageAttachment.physicalFilename != null) && (messageAttachment.physicalFilename.trim().length() > 0))
				{
					physicalFileName = messageAttachment.physicalFilename;
				}
				
				if ((messageAttachment.realFilename != null) && (messageAttachment.realFilename.trim().length() > 0))
				{
					realFileName = messageAttachment.realFilename;
				}
				
				if (physicalFileName != null && realFileName != null)
				{
					emailAttachment = new EmailAttachmentImpl();
					emailAttachment.setPhysicalFileName(physicalFileName);
					emailAttachment.setRealFileName(realFileName);
					
					emailAttachments.add(emailAttachment);
				}
			}
		}
		
		if (emailAttachments.size() > 0)
		{
			
			jforumEmailService.sendMail(messageInfo.getFrom(), messageInfo.getTo(), messageInfo.getSubject(), messageInfo.getContent(), null, null, additionalHeaders, emailAttachments);
		}
		else
		{
			jforumEmailService.sendMail(messageInfo.getFrom(), messageInfo.getTo(), messageInfo.getSubject(), messageInfo.getContent(), null, null, additionalHeaders);
		}
	}
	
}
