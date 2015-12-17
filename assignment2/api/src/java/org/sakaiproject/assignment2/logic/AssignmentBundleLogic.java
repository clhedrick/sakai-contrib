/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/AssignmentBundleLogic.java $
 * $Id: AssignmentBundleLogic.java 66836 2010-03-25 11:59:42Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.logic;

import java.util.Locale;

/**
 * This is the interface for the Assignment Bundle object
 * 
 * @author <a href="mailto:rjlowe@iupui.edu">ryan lowe</a>
 */
public interface AssignmentBundleLogic {

    /** Path to bundle messages */
    public static final String ASSIGNMENT2_BUNDLE = "messages";
    
    public static final String ASSIGNMENT2_BUNDLE_PERMISSIONS = "permissions";

    public String getString(String key);

    public String getFormattedMessage(String key, Object[] parameters);

    /**
     * 
     * @return user's preferred locale
     */
    public Locale getLocale();

}