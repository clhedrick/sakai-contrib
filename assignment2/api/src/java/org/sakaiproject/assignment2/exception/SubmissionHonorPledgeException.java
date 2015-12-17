/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/branches/ASNN-496/api/src/java/org/sakaiproject/assignment2/exception/SubmissionNotFoundException.java $
 * $Id: SubmissionHonorPledgeException.java 2011-01-25 $
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

package org.sakaiproject.assignment2.exception;

public class SubmissionHonorPledgeException extends AssignmentException
{
    public static final long serialVersionUID = 0L;

    public SubmissionHonorPledgeException(String message) {
        super(message);
    }

    public SubmissionHonorPledgeException(String msg, Throwable t) {
        super(msg, t);
    }


}
