/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/util/bbcode/BBCode.java $ 
 * $Id: BBCode.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.util.bbcode;

import java.io.Serializable;

public class BBCode implements Serializable
{
	private static final long serialVersionUID = 3077468013413762543L;
	
	private String tagName = "";
	private String regex;
	private String replace;
	private boolean removQuotes;
	private boolean alwaysProcess;

	public BBCode()
	{
	}

	/**
	 * BBCode class constructor
	 * 
	 * @param tagName
	 *        The tag name we are going to match
	 * @param regex
	 *        Regular expression relacted to the tag
	 * @param replace
	 *        The replacement string
	 */
	public BBCode(String tagName, String regex, String replace)
	{
		this.tagName = tagName;
		this.regex = regex;
		this.replace = replace;
	}

	/**
	 * Gets the regex
	 * 
	 * @return String witht the regex
	 */
	public String getRegex()
	{
		return this.regex;
	}

	/**
	 * Gets the replacement string
	 * 
	 * @return string with the replacement data
	 */
	public String getReplace()
	{
		return this.replace;
	}

	/**
	 * Getst the tag name
	 * 
	 * @return The tag name
	 */
	public String getTagName()
	{
		return this.tagName;
	}

	public boolean removeQuotes()
	{
		return this.removQuotes;
	}

	/**
	 * Sets the regular expression associated to the tag
	 * 
	 * @param regex
	 *        Regular expression string
	 */
	public void setRegex(String regex)
	{
		this.regex = regex;
	}

	/**
	 * Sets the replacement string, to be aplyied when matching the code
	 * 
	 * @param replace
	 *        The replacement string data
	 */
	public void setReplace(String replace)
	{
		this.replace = replace;
	}

	/**
	 * Setst the tag name
	 * 
	 * @param tagName
	 *        The tag name
	 */
	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public void enableAlwaysProcess()
	{
		this.alwaysProcess = true;
	}

	public boolean alwaysProcess()
	{
		return this.alwaysProcess;
	}

	public void enableRemoveQuotes()
	{
		this.removQuotes = true;
	}
}
