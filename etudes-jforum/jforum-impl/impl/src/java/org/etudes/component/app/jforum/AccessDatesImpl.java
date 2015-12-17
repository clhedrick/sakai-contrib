/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/AccessDatesImpl.java $ 
 * $Id: AccessDatesImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011 Etudes, Inc. 
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

import java.util.Date;

import org.etudes.api.app.jforum.AccessDates;

public class AccessDatesImpl implements AccessDates
{

	protected Date allowUntilDate = null;
	
	protected Date dueDate = null;
	
	protected Boolean hideUntilOpen = Boolean.FALSE;
	
	@Deprecated
	protected Boolean locked = Boolean.FALSE;
	
	protected Date openDate = null;
	
	public AccessDatesImpl(){}

	protected AccessDatesImpl(AccessDatesImpl other)
	{
		if (other != null)
		{
			if (other.openDate != null)
			{
				this.openDate = new Date(other.openDate.getTime());
				
				this.hideUntilOpen = other.hideUntilOpen;
			}
			
			if (other.dueDate != null)
			{
				this.dueDate = new Date(other.dueDate.getTime());
				// this.locked = other.locked;
			}
			
			if (other.allowUntilDate != null)
			{
				this.allowUntilDate = new Date(other.allowUntilDate.getTime());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getAllowUntilDate()
	{
		return allowUntilDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getDueDate()
	{
		return this.dueDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getOpenDate()
	{
		return this.openDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDatesValid()
	{
		boolean blnOpenDate = false, blnDueDate = false, blnAllowUntilDate = false;
		
		if (this.getOpenDate() != null) 
	 	{
			blnOpenDate = true;
	 	}
		
	 	if (this.getDueDate() != null) 
	 	{
	 		blnDueDate = true;
	 	}
	 	
	 	if (this.getAllowUntilDate() != null) 
	 	{
	 		blnAllowUntilDate = true;
	 	}
	 	
	 	if (blnOpenDate && blnDueDate && blnAllowUntilDate)
	 	{
		 	if ((this.getOpenDate().after(this.getDueDate())) || 
		 			(this.getOpenDate().after(this.getAllowUntilDate()))
		 				|| (this.getDueDate().after(this.getAllowUntilDate()))) 
		 	{
		   		return false;
		 	}
	 	}
	 	else if (blnOpenDate && blnDueDate && !blnAllowUntilDate)
		{
	 		if (this.getOpenDate().after(this.getDueDate())) 
		 	{
		   		
		   		return false;
		 	}
		}
	 	else if (!blnOpenDate && blnDueDate && blnAllowUntilDate)
		{
	 		if (this.getDueDate().after(this.getAllowUntilDate())) 
		 	{
		   		return false;
		 	}
		}
	 	else if (blnOpenDate && !blnDueDate && blnAllowUntilDate)
	 	{
		 	if (this.getOpenDate().after(this.getAllowUntilDate())) 
		 	{
		   		return false;
		 	}
	 	}

	 	return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isHideUntilOpen()
	{
		return hideUntilOpen;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public Boolean isLocked()
	{
		return this.locked;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAllowUntilDate(Date allowUntilDate)
	{
		this.allowUntilDate = allowUntilDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDueDate(Date dueDate)
	{
		this.dueDate = dueDate;
	}
	
	/**
	 * @param hideUntilOpen the hideUntilOpen to set
	 */
	public void setHideUntilOpen(Boolean hideUntilOpen)
	{
		this.hideUntilOpen = hideUntilOpen;
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public void setLocked(Boolean locked)
	{
		this.locked = locked;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setOpenDate(Date openDate)
	{
		this.openDate = openDate;
	}
}