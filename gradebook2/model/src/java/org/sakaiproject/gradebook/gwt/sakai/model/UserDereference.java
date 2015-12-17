/**********************************************************************************
*
* $Id: UserDereference.java 79715 2012-05-22 02:17:16Z jpgorrono@ucdavis.edu $
*
***********************************************************************************
*
* Copyright (c) 2008, 2009 The Regents of the University of California
*
* Licensed under the
* Educational Community License, Version 2.0 (the "License"); you may
* not use this file except in compliance with the License. You may
* obtain a copy of the License at
* 
* http://www.osedu.org/licenses/ECL-2.0
* 
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS"
* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
* or implied. See the License for the specific language governing
* permissions and limitations under the License.
*
**********************************************************************************/

package org.sakaiproject.gradebook.gwt.sakai.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class UserDereference implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String eid;
	private String userUid;
	private String displayId;
	private String displayName;
	private String lastNameFirst;
	private String sortName;
	private String email;
	private Date createdOn;
	
	public UserDereference() {
		
	}
	
	public UserDereference(String userUid) {
		this.userUid = userUid;
	}
	
	public UserDereference(String userUid, String eid, String displayId, String displayName, String lastNameFirst, String sortName, String email) {
		this.userUid = userUid;
		this.eid = eid;
		this.displayId = displayId;
		this.displayName = displayName;
		this.lastNameFirst = lastNameFirst;
		this.sortName = sortName;
		this.email = email;
		this.createdOn = new Date();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserUid() {
		return userUid;
	}
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getDisplayId() {
		return displayId;
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getLastNameFirst() {
		return lastNameFirst;
	}

	public void setLastNameFirst(String lastNameFirst) {
		this.lastNameFirst = lastNameFirst;
	}
	
	public boolean equals(Object o) {
		
		 if (o instanceof UserDereference == false) {
		     return false;
		   }
		 
		   if (this == o) {
		     return true;
		   }
		   
		   UserDereference rhs = (UserDereference)o;
		   
		   return new EqualsBuilder()
           .append(userUid, rhs.getUserUid())
           .isEquals();
	
		   
		   
	}
	
	 public int hashCode() {
	     return new HashCodeBuilder(933, 31735).
	       append(userUid)
	       .toHashCode();
	 }
	       
	

}
