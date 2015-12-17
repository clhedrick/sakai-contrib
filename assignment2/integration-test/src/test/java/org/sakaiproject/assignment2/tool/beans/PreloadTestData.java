/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/integration-test/src/test/java/org/sakaiproject/assignment2/tool/beans/PreloadTestData.java $
 * $Id: PreloadTestData.java 48274 2008-04-23 20:07:00Z wagnermr@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.assignment2.tool.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.dao.AssignmentDao;

/**
 * This preloads data needed for testing<br/> Do not load this data into a live or production
 * database<br/> Load this after the normal preload<br/> Add the following (or something like it)
 * to a spring beans def file:<br/>
 * 
 * <pre>
 *  &lt;!-- create a test data preloading bean --&gt;
 *  &lt;bean id=&quot;org.sakaiproject.assignment.test.PreloadTestData&quot;
 *  class=&quot;org.sakaiproject.assignment.test.PreloadTestData&quot;
 *  init-method=&quot;init&quot;&gt;
 *  &lt;property name=&quot;assignmentDao&quot;
 *  ref=&quot;org.sakaiproject.assignment.dao.AssignmentDao&quot; /&gt;
 *  &lt;property name=&quot;pdl&quot;
 *  ref=&quot;org.sakaiproject.assignment.dao.PreloadData&quot; /&gt;
 *  &lt;/bean&gt;
 * </pre>
 */
public class PreloadTestData
{
	private static Log log = LogFactory.getLog(PreloadTestData.class);

	private AssignmentDao assignmentDao;

	public void setAssignmentDao(AssignmentDao assignmentDao)
	{
		this.assignmentDao = assignmentDao;
	}

	private AssignmentTestDataLoad atdl;

	/**
	 * @return the test data loading class with copies of all saved objects
	 */
	public AssignmentTestDataLoad getAtdl()
	{
		return atdl;
	}

	public void init()
	{
		log.info("INIT");
		preloadDB();
	}

	/**
	 * Preload the data
	 */
	public void preloadDB()
	{
		log.info("preloading DB...");

		atdl = new AssignmentTestDataLoad();
		atdl.createTestData(assignmentDao);
	}
}
