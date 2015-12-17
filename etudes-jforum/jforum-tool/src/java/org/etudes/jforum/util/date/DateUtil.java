/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/util/date/DateUtil.java $ 
 * $Id: DateUtil.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010 Etudes, Inc. 
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

package org.etudes.jforum.util.date;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.jforum.util.user.JForumUserUtil;
import org.etudes.util.DateHelper;

public class DateUtil
{
	private static Log logger = LogFactory.getLog(JForumUserUtil.class);
	
	public DateUtil()
	{
	}

	/**
	 * parse the date string to date
	 * 
	 * @param date
	 * @return the date from the parsed date string
	 * @throws ParseException
	 */
	static public Date getDateFromString(String date) throws ParseException
	{
		Date endDate;
		try
		{
			if ((date == null) || (date.trim().length() == 0))
			{
				return null;
			}
			
			endDate = DateHelper.parseDate(date, null);
		} 
		catch (ParseException e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("getDateFromString() : " + "Error occurred while parsing the date for '" + date + "'", e);
			}
			throw e;
		}
		return endDate;
	}
	
	
	/**
	 * Format the date into a string
	 * 
	 * @param date Date object
	 * @return String from date object taking into account user's timezone preference
	 */
	static public String getStringFromDate(Date date)
	{
		String dateZoneStr = null;
		if (date == null) return null;
		dateZoneStr = DateHelper.formatDate(date, null);
		return dateZoneStr;
	}
	
	/**
	 * Format the date in milliseconds into a string
	 * 
	 * @param millis in milliseconds(long)
	 * @return String from date object taking into account user's timezone preference
	 */
	static public String getStringFromMillis(long millis)
	{
		return getStringFromDate(new Date(millis));
	}

}
