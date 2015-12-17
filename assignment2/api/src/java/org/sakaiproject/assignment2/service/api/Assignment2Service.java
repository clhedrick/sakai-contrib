/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/AssignmentLogic.java $
 * $Id: AssignmentLogic.java 65784 2010-01-20 19:04:25Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.service.api;

import java.util.List;

import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.service.model.AssignmentDefinition;


/**
 * The externally exposed API for Assignment2. 
 * 
 */
public interface Assignment2Service {

    /**
     * 
     * @param assignmentId
     * @return Returns the {@link AssignmentDefinition} representing the Assignment2
     * object with the given assignmentId. Assignment properties that are restricted
     * for the current user will be set to null (ie students may view some assignment
     * details but not all)
     * @throws AssignmentNotFoundException if no assignment exists with the given id
     * @throws SecurityException if current user is not allowed to access this assignment
     */
    public AssignmentDefinition getAssignmentById(Long assignmentId);
    
    /**
     * 
     * @param contextId
     * @return a list of {@link AssignmentDefinition}s representing the Assignments
     * viewable to the current user. Restricted information will be filtered on the
     * returned objects.
     */
    public List<AssignmentDefinition> getAssignments(String contextId);

}
