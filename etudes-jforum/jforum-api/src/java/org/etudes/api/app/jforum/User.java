/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/User.java $ 
 * $Id: User.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

public interface User
{
	/**
	 * @return the attachSignatureEnabled
	 */
	public boolean getAttachSignatureEnabled();

	/**
	 * @return the faceBookAccount
	 */
	public String getFaceBookAccount();

	/**
	 * @return the googlePlus
	 */
	public String getGooglePlus();

	/**
	 * @return the linkedIn
	 */
	public String getLinkedIn();

	/**
	 * @return the skype
	 */
	public String getSkype();

	/**
	 * @return the twitterAccount
	 */
	public String getTwitterAccount();

	/**
	 * @return the htmlEnabled
	 */
	@Deprecated
	public boolean isHtmlEnabled();
	
	/**
	 * @param attachSignatureEnabled the attachSignatureEnabled to set
	 */
	public void setAttachSignatureEnabled(boolean attachSignatureEnabled);

	/**
	 * @param faceBookAccount the faceBookAccount to set
	 */
	public void setFaceBookAccount(String faceBookAccount);

	/**
	 * @param googlePlus the googlePlus to set
	 */
	public void setGooglePlus(String googlePlus);

	/**
	 * @param htmlEnabled the htmlEnabled to set
	 */
	@Deprecated
	public void setHtmlEnabled(boolean htmlEnabled);
	
	/**
	 * @param linkedIn the linkedIn to set
	 */
	public void setLinkedIn(String linkedIn);

	/**
	 * @param skype the skype to set
	 */
	public void setSkype(String skype);

	/**
	 * @param twitterAccount the twitterAccount to set
	 */
	public void setTwitterAccount(String twitterAccount);
	
	/**
	 * Attaches avatar to upload
	 * 
	 * @param fileName	File name - only image files are allowed
	 * 
	 * @param fileContent	File content
	 */
	void attachAvatar(String fileName, byte[] fileContent);
	
	/**
	 * @return the aim
	 */
	String getAim();

	/**
	 * @return the avatar
	 */
	String getAvatar();
	
	/**
	 * @return the email
	 */
	String getEmail();

	/**
	 * Gets the first name
	 * 
	 * @return 	The firstName
	 */
	String getFirstName();

	/**
	 * @return the from
	 */
	String getFrom();

	/**
	 * @return the icq
	 */
	@Deprecated
	String getIcq();
	
	/**
	 * Gets the jforum user id
	 * 
	 * @return 	The id
	 */
	int getId();
	
	/**
	 * @return the interests
	 */
	String getInterests();

	/**
	 * @return the lang
	 */
	String getLang();

	/**
	 * Gets the last name
	 * 
	 * @return 	The lastName
	 */
	String getLastName();

	/**
	 * @return the msnm
	 */
	String getMsnm();

	/**
	 * @return the occupation
	 */
	String getOccupation();

	/**
	 * Gets the sakai user id
	 * 
	 * @return The sakaiUserId
	 */
	String getSakaiUserId();

	/**
	 * @return the signature
	 */
	String getSignature();

	/**
	 * @return the totalSitePosts
	 */
	int getTotalSitePosts();

	/**
	 * @return the username
	 */
	String getUsername();
	
	/**
	 * @return the webSite
	 */
	String getWebSite();

	/**
	 * @return the yim
	 */
	String getYim();

	/**
	 * Indicates if the avatar points to an external URL
	 * @return <code>true</code> if the avatar is some external image
	 */
	boolean isExternalAvatar();

	/**
	 * @return the notifyOnMessagesEnabled
	 */
	boolean isNotifyOnMessagesEnabled();

	/**
	 * @return the notifyPrivateMessagesEnabled
	 */
	boolean isNotifyPrivateMessagesEnabled();

	/**
	 * @return the viewEmailEnabled
	 */
	boolean isViewEmailEnabled();
	
	/**
	 * @param aim the aim to set
	 */
	void setAim(String aim);

	/**
	 * @param avatar the avatar to set
	 */
	void setAvatar(String avatar);

	/**
	 * @param email the email to set
	 */
	void setEmail(String email);

	/**
	 * Sets the first name
	 * 
	 * @param firstName 	The firstName to set
	 */
	void setFirstName(String firstName);

	/**
	 * @param from the from to set
	 */
	void setFrom(String from);
	
	/**
	 * @param icq the icq to set
	 */
	@Deprecated
	void setIcq(String icq);
	
	/**
	 * @param interests the interests to set
	 */
	void setInterests(String interests);
	/**
	 * @param lang the lang to set
	 */
	void setLang(String lang);
	
	/**
	 * Sets the last name
	 * 
	 * @param lastName 	The lastName to set
	 */
	void setLastName(String lastName);
	
	/**
	 * @param msnm the msnm to set
	 */
	void setMsnm(String msnm);
	
	/**
	 * @param notifyOnMessagesEnabled the notifyOnMessagesEnabled to set
	 */
	void setNotifyOnMessagesEnabled(boolean notifyOnMessagesEnabled);
	
	/**
	 * @param notifyPrivateMessagesEnabled the notifyPrivateMessagesEnabled to set
	 */
	void setNotifyPrivateMessagesEnabled(boolean notifyPrivateMessagesEnabled);

	/**
	 * @param occupation the occupation to set
	 */
	void setOccupation(String occupation);

	/**
	 * @param signature the signature to set
	 */
	void setSignature(String signature);

	/**
	 * @param viewEmailEnabled the viewEmailEnabled to set
	 */
	void setViewEmailEnabled(boolean viewEmailEnabled);

	/**
	 * @param webSite the webSite to set
	 */
	void setWebSite(String webSite);

	/**
	 * @param yim the yim to set
	 */
	void setYim(String yim);
}