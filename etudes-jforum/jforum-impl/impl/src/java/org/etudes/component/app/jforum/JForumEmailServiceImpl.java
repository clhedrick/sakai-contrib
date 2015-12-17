/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumEmailServiceImpl.java $ 
 * $Id: JForumEmailServiceImpl.java 84254 2013-07-10 19:07:02Z murthy@etudes.org $ 
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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.EmailAttachment;
import org.etudes.api.app.jforum.JForumAttachmentService;
import org.etudes.api.app.jforum.JForumEmailService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.email.api.EmailService;

public abstract class JForumEmailServiceImpl implements JForumEmailService
{
	private static Log logger = LogFactory.getLog(JForumEmailServiceImpl.class);
	
	protected static final String EMAIL_TESTMODE = "testMode@org.sakaiproject.email.api.EmailService";
	
	protected static final String POSTMASTER = "postmaster";
	
	/** Email address to use for SMTP MAIL command. This sets the envelope return address. Defaults to message.getFrom() or InternetAddress.getLocalAddress(). */
	protected static final String SMTP_FROM = "mail.smtp.from";
	
	/** The SMTP server to connect to. */
	protected static final String SMTP_HOST = "mail.smtp.host";
	
	/** The SMTP server to connect from sakai.properties */
	protected static final String SMTP_HOST_SAKAI = "smtp@org.sakaiproject.email.api.EmailService";
	
	/** The SMTP server port to connect. */
	protected static final String SMTP_PORT = "mail.smtp.port";

	/** The SMTP server port to connect from sakai.properties */
	protected static final String SMTP_PORT_SAKAI = "smtp.port";
	
	/** message smtp property */
	protected String smtp = null;

	/** message smtp from property */
	protected String smtpFrom = null;
	
	/** message smtp port property*/
	protected String smtpPort = null;
	
	/** email test mode */
	protected boolean testMode = false;
	
	protected Session session = null;
	
	public void destroy()
	{
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
		
		/*wait component manager to cofigure*/
		ComponentManager.waitTillConfigured();
		
		testMode = serverConfigurationService().getBoolean(EMAIL_TESTMODE, false);
		
		smtp = System.getProperty(SMTP_HOST);
		if (smtp == null)
		{
			smtp = serverConfigurationService().getString(SMTP_HOST_SAKAI);
			
			if (smtp == null || smtp.trim().length() == 0)
			{
				smtp = null;
				
				if (logger.isWarnEnabled())
				{
					logger.warn("smtp value is not set.");
				}
			}
		}
		
		smtpPort = System.getProperty(SMTP_PORT);
		if (smtpPort == null)
		{
			smtpPort = serverConfigurationService().getString(SMTP_PORT_SAKAI);
			
			if (smtpPort == null || smtpPort.trim().length() == 0)
			{
				smtpPort = null;
				if (logger.isDebugEnabled())
				{
					logger.debug("smtpPort value is not set.");
				}
			}
		}
		
		smtpFrom = System.getProperty(SMTP_FROM);
		if (this.smtpFrom == null)
		{
			smtpFrom = POSTMASTER + "@" + serverConfigurationService().getServerName();
		}
		
		Properties props = new Properties();
		
		// set the port, if specified
		if (smtpPort != null && smtpPort.trim().length() > 0)
		{
			props.put(SMTP_PORT, smtpPort);
		}

		// set the mail envelope return address
		props.put(SMTP_FROM, smtpFrom);
		
		if (smtp == null || smtp.trim().length() == 0)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("smtp not set");
			}
			return;
		}
		
		// set the server host
		props.put(SMTP_HOST, smtp);

		session = Session.getInstance(props);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void send(String fromStr, String toStr, String subject, String content, String headerToStr, String replyToStr, List<String> additionalHeaders)
	{
		emailService().send(fromStr, toStr, subject, content, headerToStr, replyToStr, additionalHeaders);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendMail(InternetAddress from, InternetAddress[] to, String subject, String content, InternetAddress[] headerTo, InternetAddress[] replyTo, List<String> additionalHeaders)
	{
		if (from == null)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("sendMail: from is null");
			}
			return;
		}

		if (to == null)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("sendMail: to is null");
			}
			return;
		}

		if (content == null)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("sendMail: content is null");
			}
			return;
		}
		
		for (int i = 0; i < to.length; i++) 
		{
			InternetAddress[] toEmail = new InternetAddress[1];
			toEmail[0] = to[i];
			
			emailService().sendMail(from, toEmail, subject, content, toEmail, replyTo, additionalHeaders);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendMail(InternetAddress from, InternetAddress[] to, String subject, String content, InternetAddress[] headerTo, InternetAddress[] replyTo, List<String> additionalHeaders, List<EmailAttachment> emailAttachments)
	{
		for (int i = 0; i < to.length; i++) 
		{
			InternetAddress[] toEmail = new InternetAddress[1];
			toEmail[0] = to[i];
			
			if (emailAttachments != null && emailAttachments.size() > 0)
			{
				sendMailWithAttachments(from, toEmail, subject, content, to, replyTo, additionalHeaders, emailAttachments);
			}
			else
			{
				sendMail(from, toEmail, subject, content, to, replyTo, additionalHeaders);
			}
		}		
	}
	
	/**
	 * Array to string to print test email
	 * 
	 * @param array	Array
	 * 
	 * @return	Array as string
	 */
	protected String arrayToStr(Object[] array)
	{
		StringBuffer buf = new StringBuffer();
		if (array != null)
		{
			buf.append("[");
			for (int i = 0; i < array.length; i++)
			{
				if (i != 0)
					buf.append(", ");
				buf.append(array[i].toString());
			}
			buf.append("]");
		}
		else
		{
			buf.append("");
		}

		return buf.toString();
	}
	
	/**
	 * @return the EmailService collaborator.
	 */
	protected abstract EmailService emailService();
	
	/**
	 * List to string
	 * 
	 * @param list	List
	 * 
	 * @return	List as string
	 */
	protected String listToStr(Collection<String> list)
	{
		if (list == null)
		{
			return "";
		}
		return arrayToStr(list.toArray());
	}
	
	/**
	 * Sends email with attachments
	 * 
	 * @param from
	 *        The address this message is to be listed as coming from.
	 * @param to
	 *        The address(es) this message should be sent to.
	 * @param subject
	 *        The subject of this message.
	 * @param content
	 *        The body of the message.
	 * @param headerToStr
	 *        If specified, this is placed into the message header, but "to" is used for the recipients.
	 * @param replyTo
	 *        If specified, this is the reply to header address(es).
	 * @param additionalHeaders
	 *        Additional email headers to send (List of String). For example, content type or forwarded headers (may be null)        
	 * @param messageAttachments
	 * 		  Message attachments
	 */
	protected void sendMailWithAttachments(InternetAddress from, InternetAddress[] to, String subject, String content, InternetAddress[] headerTo, InternetAddress[] replyTo, List<String> additionalHeaders, List<EmailAttachment> emailAttachments)
	{
		if (testMode)
		{
			testSendMail(from, to, subject, content, headerTo, replyTo, additionalHeaders, emailAttachments);
			return;
		}
		
		if (smtp == null || smtp.trim().length() == 0)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("sendMailWithAttachments: smtp not set");
			}
			return;
		}
		
		if (from == null)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("sendMail: from is needed to send email");
			}
			return;
		}
		
		if (to == null)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("sendMail: to is needed to send email");
			}
			return;
		}
		
		if (content == null)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("sendMail: content is needed to send email");
			}
			return;
		}
		
		if (emailAttachments == null || emailAttachments.size() == 0)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("sendMail: emailAttachments are needed to send email with attachments");
			}
			return;
		}		
		
		try
		{
			if (session == null)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("mail session is null");
				}
				return;
				
			}
			MimeMessage message = new MimeMessage(session);

			// default charset
			String charset = "UTF-8";

			message.setSentDate(new Date());
			message.setFrom(from);
			message.setSubject(subject, charset);

			MimeBodyPart messageBodyPart = new MimeBodyPart();
		    
			// Content-Type: text/plain; text/html;
			String contentType = null, contentTypeValue = null;
			
			if (additionalHeaders != null)
			{
				for (String header : additionalHeaders)
				{
					if (header.toLowerCase().startsWith("content-type:"))
					{
						contentType = header;
						
						contentTypeValue = contentType.substring(contentType.indexOf("content-type:")+"content-type:".length(), contentType.length());
						break;
					}
				}
			}
			
			// message
			String messagetype = "";
			if ((contentTypeValue != null) && (contentTypeValue.trim().equalsIgnoreCase("text/html")))
			{
				messagetype = "text/html; charset=" + charset;
				messageBodyPart.setContent(content, messagetype);
			} 
			else 
			{
				
				messagetype = "text/plain; charset="+ charset;
				messageBodyPart.setContent(content, messagetype);
			}
			
			//messageBodyPart.setContent(content, "text/html; charset="+ charset);
			
			Multipart multipart = new MimeMultipart();

		    multipart.addBodyPart(messageBodyPart);
		    
		    String jforumAttachmentStoreDir = serverConfigurationService().getString(JForumAttachmentService.ATTACHMENTS_STORE_DIR);
		    if (jforumAttachmentStoreDir == null || jforumAttachmentStoreDir.trim().length() == 0)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("JForum attachments directory ("+ JForumAttachmentService.ATTACHMENTS_STORE_DIR +") property is not set in sakai.properties ");
				}
			}
		    else
		    {
			    // attachments
				for (EmailAttachment emailAttachment : emailAttachments)
				{
					
					String filePath =  jforumAttachmentStoreDir + "/" + emailAttachment.getPhysicalFileName();
	
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(filePath);
					try
					{
						messageBodyPart.setDataHandler(new DataHandler(source));
						messageBodyPart.setFileName(emailAttachment.getRealFileName());
	
						multipart.addBodyPart(messageBodyPart);
					}
					catch (MessagingException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("Error while attaching attachments: " + e, e);
						}
					}
				}
		    }

		    message.setContent(multipart);
		    
		    Transport.send(message, to);
			
		}
		catch (MessagingException e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("sendMail: Error in sending email: " + e, e);
			}			
		}
	}

	/**
	 * @return the ServerConfigurationService collaborator.
	 */
	protected abstract ServerConfigurationService serverConfigurationService();

	/**
	 * if send email is in test mode prints the email
	 * 
	 * @param from
	 *        The address this message is to be listed as coming from.
	 * @param to
	 *        The address(es) this message should be sent to.
	 * @param subject
	 *        The subject of this message.
	 * @param content
	 *        The body of the message.
	 * @param headerToStr
	 *        If specified, this is placed into the message header, but "to" is used for the recipients.
	 * @param replyTo
	 *        If specified, this is the reply to header address(es).
	 * @param additionalHeaders
	 *        Additional email headers to send (List of String). For example, content type or forwarded headers (may be null)	 *        
	 * @param messageAttachments
	 * 		  Message attachments
	 */
	protected void testSendMail(InternetAddress from, InternetAddress[] to, String subject, String content, InternetAddress[] headerTo,
			InternetAddress[] replyTo, List<String> additionalHeaders, List<EmailAttachment> messageAttachments)
	{
		logger.info("sendMail: from: " + from + " to: " + arrayToStr(to) + " subject: " + subject + " headerTo: " + arrayToStr(headerTo)
				+ " replyTo: " + arrayToStr(replyTo) + " content: " + content + " additionalHeaders: " + listToStr(additionalHeaders) + " messageAttachments: "+ messageAttachments);
	}

}
