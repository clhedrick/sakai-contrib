/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/JForumEmailService.java $ 
 * $Id: JForumEmailService.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum;

import java.util.List;

import javax.mail.internet.InternetAddress;


public interface JForumEmailService
{

	/**
	 * Creates and sends a generic text MIME message to the address contained in to.
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
	 */
	void sendMail(InternetAddress from, InternetAddress[] to, String subject, String content, InternetAddress[] headerTo, InternetAddress[] replyTo, List<String> additionalHeaders);
	
	/**
	 * Creates and sends a generic text MIME message to the address contained in to with attachments
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
	 *        
	 * @param messageAttachments
	 * 		  Message attachments
	 */
	void sendMail(InternetAddress from, InternetAddress[] to, String subject, String content, InternetAddress[] headerTo, InternetAddress[] replyTo, List<String> additionalHeaders, List<EmailAttachment> emailAttachments);
	
	/**
	 * Creates and sends a generic text MIME message to the address contained in to.
	 * 
	 * @param fromStr
	 *        The address this message is to be listed as coming from.
	 * @param toStr
	 *        The address(es) this message should be sent to.
	 * @param subject
	 *        The subject of this message.
	 * @param content
	 *        The body of the message.
	 * @param headerToStr
	 *        If specified, this is placed into the message header, but "too" is used for the recipients.
	 * @param replyToStr
	 *        If specified, the reply-to header value.
	 * @param additionalHeaders
	 *        Additional email headers to send (List of String). For example, content type or forwarded headers (may be null)
	 */
	void send(String fromStr, String toStr, String subject, String content, String headerToStr, String replyToStr, List<String> additionalHeaders);

	/**
	 * Send a single message to a set of Users.
	 * 
	 * @param users
	 *        Collection (of User) to send the message to (for those with valid email addresses).
	 * @param headers
	 *        List (of String, form "name: value") of headers for the message.
	 * @param message
	 *        String body of the message.
	 *//*
	void sendToUsers(Collection users, Collection headers, String message);*/
}
