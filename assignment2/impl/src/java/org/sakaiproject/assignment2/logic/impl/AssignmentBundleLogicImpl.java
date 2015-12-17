/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/logic/impl/AssignmentBundleLogicImpl.java $
 * $Id: AssignmentBundleLogicImpl.java 61481 2009-06-29 18:47:43Z swgithen@mtu.edu $
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

package org.sakaiproject.assignment2.logic.impl;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.AssignmentBundleLogic;
import org.sakaiproject.util.ResourceLoader;

/**
 * Makes available the resource bundle for this tool so that messages can be accessed outside of the
 * tool layer.
 * 
 * @author <a href="mailto:rjlowe@iupui.edu">ryan lowe</a>
 * @author <a href="mailto:carl.hall@et.gatech.edu">Carl Hall</a>
 */
public class AssignmentBundleLogicImpl implements AssignmentBundleLogic
{
    private static ResourceLoader rb = null;
    private static Log log = LogFactory.getLog(AssignmentLogicImpl.class);

    public void init()
    {
        if (log.isDebugEnabled())
            log.debug("init");
        // since the field is static, only instantiate of not previously populated
        // this bean should only be created once but this will ensure an overwritten
        // assignment doesn't occur.
        if (rb == null)
            rb = new ResourceLoader(ASSIGNMENT2_BUNDLE);
    }

    public String getString(String key)
    {
        return rb.getString(key);
    }

    public String getFormattedMessage(String key, Object[] parameters) {
        return rb.getFormattedMessage(key, parameters);
    }

    public Locale getLocale() {
        return rb.getLocale();
    }
}