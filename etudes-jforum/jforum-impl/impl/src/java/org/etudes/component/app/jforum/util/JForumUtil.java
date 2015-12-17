/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/util/JForumUtil.java $ 
 * $Id: JForumUtil.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.util;

public class JForumUtil
{
	private JForumUtil()
	{
	}

	/**
	 * This is from mneme Convert the points / score value into a double, without picking up float to double conversion junk
	 * 
	 * @param score
	 *        The value to convert.
	 * @return The converted value.
	 */
	static public double toDoubleScore(Float score)
	{
		if (score == null) return 0.0d;

		// we want only .xx precision, and we don't want any double junk from the float to double conversion
		float times100 = score.floatValue() * 100f;

		Double rv = (double) times100;

		rv = rv / 100d;
		return rv;
	}

}
