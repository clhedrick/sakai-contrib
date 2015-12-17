/**********************************************************************************
*
* $Id: Realm.java 67602 2010-05-07 20:43:22Z jpgorrono@ucdavis.edu $
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

import java.util.Set;


public class Realm {
	
	Long realmKey;
	String realmId;
	String providerId;
	Set<RealmGroup> realmGroups;
	
	public Set<RealmGroup> getRealmGroups() {
		return realmGroups;
	}

	public void setRealmGroups(Set<RealmGroup> realmGroups) {
		this.realmGroups = realmGroups;
	}

	public Long getRealmKey() {
	
		return realmKey;
	}
	
	public void setRealmKey(Long realmKey) {
	
		this.realmKey = realmKey;
	}
	
	public String getRealmId() {
	
		return realmId;
	}
	
	public void setRealmId(String realmId) {
	
		this.realmId = realmId;
	}
	
	public String getProviderId() {
	
		return providerId;
	}
	
	public void setProviderId(String providerId) {
	
		this.providerId = providerId;
	}
}
