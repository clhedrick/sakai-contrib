/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/branches/ASNN-617/impl/src/java/org/sakaiproject/assignment2/logic/impl/AssignmentBundleLogicImpl.java $
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

package org.sakaiproject.assignment2.logic.test.stubs;

import org.sakaiproject.assignment2.logic.ExternalEventLogic;


/**
 * Stub class used for handling event tracking for assignment2 testing
 */
public class ExternalEventLogicStub implements ExternalEventLogic
{
    
    public void postEvent(String message, String objectReference) {
        System.out.println("EVENT TRACKED for objectReference: " + objectReference);
        System.out.println("MESSAGE: " + message);
    }

}