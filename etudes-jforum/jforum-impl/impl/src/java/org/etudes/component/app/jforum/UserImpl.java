/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/UserImpl.java $ 
 * $Id: UserImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.net.FileNameMap;
import java.net.URLConnection;

import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.User;
import org.etudes.component.app.jforum.util.image.ImageUtils;

public class UserImpl implements User
{
	protected String aim;
	protected boolean attachSignatureEnabled = true;
	protected String avatar;
	protected Attachment avatarAttach = null;
	protected String email;
	protected String faceBookAccount;
	protected String firstName;
	protected String from;
	protected String googlePlus;
	protected boolean htmlEnabled = true;
	protected String icq;
	protected int id;
	protected String interests;
	protected boolean isExternalAvatar;
	protected String lang;
	protected String lastName;
	protected String linkedIn;
	protected String msnm;
	protected boolean notifyOnMessagesEnabled = true;
	protected boolean notifyPrivateMessagesEnabled = true;
	protected String occupation;
	protected String sakaiUserId;
	protected String signature;
	protected String skype;
	protected int totalSitePosts;
	protected String twitterAccount;
	protected String username;
	protected boolean viewEmailEnabled = true;	
	protected String webSite;
	protected String yim;

	public UserImpl() {}

	protected UserImpl(UserImpl other)
	{
		this.id = other.id;
		this.firstName = other.firstName;
		this.lastName = other.lastName;
		this.username = other.username;
		this.sakaiUserId = other.sakaiUserId;
		this.avatar = other.avatar;
		this.notifyPrivateMessagesEnabled = other.notifyPrivateMessagesEnabled;
		this.notifyOnMessagesEnabled = other.notifyOnMessagesEnabled;
		this.email = other.email;
		this.icq = other.icq;
		this.webSite = other.webSite;
		this.from = other.from;
		this.signature = other.signature;
		this.aim = other.aim;
		this.yim = other.yim;
		this.msnm = other.msnm;
		this.faceBookAccount = other.faceBookAccount;
		this.twitterAccount = other.twitterAccount;
		this.occupation = other.occupation;
		this.interests = other.interests;
		this.viewEmailEnabled = other.viewEmailEnabled;
		this.lang = other.lang;
		this.htmlEnabled = other.htmlEnabled;
		this.attachSignatureEnabled = other.attachSignatureEnabled;
		this.totalSitePosts = other.totalSitePosts;
		this.googlePlus = other.googlePlus;
		this.skype = other.skype;
		this.linkedIn = other.linkedIn;
	}

	public void attachAvatar(String fileName, byte[] fileContent)
	{
		if ((fileName == null || fileName.trim().length() == 0) ||(fileContent == null || fileContent.length == 0))
		{
			return;
		}
		
		// Get only the filename, without the path
		String separator = "/";
		int index = fileName.indexOf(separator);

		if (index == -1)
		{
			separator = "\\";
			index = fileName.indexOf(separator);
		}

		if (index > -1)
		{
			if (separator.equals("\\"))
			{
				separator = "\\\\";
			}

			String[] p = fileName.split(separator);
			fileName = p[p.length - 1];
		}
		
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
		
		if (fileName.trim().length() == 0)
		{
			return;
		}
		
		int type = -1;

		if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg"))
		{
			type = ImageUtils.IMAGE_JPEG;
		}
		else if (extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("png"))
		{
			type = ImageUtils.IMAGE_PNG;
		}

		if (type != -1)
		{
		
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			String mimeType = fileNameMap.getContentTypeFor(fileName);
			 
			Attachment attachment = new AttachmentImpl();
			
			AttachmentInfoImpl attachmentInfo = new AttachmentInfoImpl();
			attachmentInfo.setMimetype(mimeType);
			attachmentInfo.setRealFilename(fileName);
			attachmentInfo.setFileContent(fileContent);
			
			attachment.setInfo(attachmentInfo);
			
			this.avatarAttach = attachment;
		}
	}

	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		// two user objects are equals if they have the same id
		return ((obj instanceof User) && (((User)obj).getId() == this.id));
	}

	/**
	 * {@inheritDoc}
	 */
	public String getAim()
	{
		return aim;
	}
	/**
	 * {@inheritDoc}
	 */
	public boolean getAttachSignatureEnabled()
	{
		return attachSignatureEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getAvatar()
	{
		return avatar;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getFaceBookAccount()
	{
		return faceBookAccount;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getFrom()
	{
		return from;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getGooglePlus()
	{
		return googlePlus;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getIcq()
	{
		return icq;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getInterests()
	{
		return interests;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLang()
	{
		return lang;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLastName()
	{
		return lastName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLinkedIn()
	{
		return linkedIn;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getMsnm()
	{
		return msnm;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getOccupation()
	{
		return occupation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getSakaiUserId()
	{
		return sakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSignature()
	{
		return signature;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSkype()
	{
		return skype;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTotalSitePosts()
	{
		return totalSitePosts;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getTwitterAccount()
	{
		return twitterAccount;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getWebSite()
	{
		return webSite;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getYim()
	{
		return yim;
	}

	/** 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExternalAvatar() 
	{
		return this.isExternalAvatar;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isHtmlEnabled()
	{
		return htmlEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isNotifyOnMessagesEnabled()
	{
		return notifyOnMessagesEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isNotifyPrivateMessagesEnabled()
	{
		return notifyPrivateMessagesEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isViewEmailEnabled()
	{
		return viewEmailEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAim(String aim)
	{
		this.aim = aim;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAttachSignatureEnabled(boolean attachSignatureEnabled)
	{
		this.attachSignatureEnabled = attachSignatureEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
		
		if (avatar != null && avatar.toLowerCase().startsWith("http://")) 
		{
			this.isExternalAvatar = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFaceBookAccount(String faceBookAccount)
	{
		this.faceBookAccount = faceBookAccount;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFrom(String from)
	{
		this.from = from;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGooglePlus(String googlePlus)
	{
		this.googlePlus = googlePlus;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHtmlEnabled(boolean htmlEnabled)
	{
		this.htmlEnabled = htmlEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setIcq(String icq)
	{
		this.icq = icq;
	}

	/**
	 * Sets the id
	 * 
	 * @param id	Id
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setInterests(String interests)
	{
		this.interests = interests;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLang(String lang)
	{
		this.lang = lang;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLinkedIn(String linkedIn)
	{
		this.linkedIn = linkedIn;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMsnm(String msnm)
	{
		this.msnm = msnm;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNotifyOnMessagesEnabled(boolean notifyOnMessagesEnabled)
	{
		this.notifyOnMessagesEnabled = notifyOnMessagesEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNotifyPrivateMessagesEnabled(boolean notifyPrivateMessagesEnabled)
	{
		this.notifyPrivateMessagesEnabled = notifyPrivateMessagesEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setOccupation(String occupation)
	{
		this.occupation = occupation;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSakaiUserId(String sakaiUserId)
	{
		this.sakaiUserId = sakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSkype(String skype)
	{
		this.skype = skype;
	}

	/**
	 * @param totalSitePosts the totalSitePosts to set
	 */
	public void setTotalSitePosts(int totalSitePosts)
	{
		this.totalSitePosts = totalSitePosts;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTwitterAccount(String twitterAccount)
	{
		this.twitterAccount = twitterAccount;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setViewEmailEnabled(boolean viewEmailEnabled)
	{
		this.viewEmailEnabled = viewEmailEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWebSite(String webSite)
	{
		this.webSite = webSite;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setYim(String yim)
	{
		this.yim = yim;
	}
	
	/**
	 * @return the avatarAttach
	 */
	protected Attachment getAvatarAttach()
	{
		return avatarAttach;
	}
}
