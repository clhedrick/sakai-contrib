/**********************************************************************************
*
* $Id: I18nMessages.java 78796 2012-03-10 00:37:30Z tpamsler@ucdavis.edu $
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
package org.sakaiproject.gradebook.gwt.client;

import com.google.gwt.i18n.client.Messages;

public interface I18nMessages extends Messages {

	
	String itemTreePanelAlertMessage(String cssClasses, String msg);
	
	String greaterThanOrEqualToValue(String value);
	
	String importDataMinValue(String value);
	
	String importDataMaxValue(String value);
	
	String valueConfusing(String value);

	String pointsFieldEmptyText(String value);
	
	String statisticsDataAge(int age);
}
