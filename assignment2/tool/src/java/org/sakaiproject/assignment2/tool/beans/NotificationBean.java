/**********************************************************************************
 * $URL: 
 * $Id: 
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

import org.sakaiproject.api.app.scheduler.DelayedInvocation;
import org.sakaiproject.api.app.scheduler.ScheduledInvocationManager;
import org.sakaiproject.assignment2.logic.ScheduledNotification;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.api.TimeService;
import org.sakaiproject.user.api.UserNotDefinedException;

public class NotificationBean
{
    // injected dependencies

    private TimeService timeService;

    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }

    private ScheduledInvocationManager scheduledInvocationManager;

    public void setScheduledInvocationManager(
            ScheduledInvocationManager scheduledInvocationManager)
    {
        this.scheduledInvocationManager = scheduledInvocationManager;
    }

    private ScheduledNotification scheduledNotification;

    public void setScheduledNotification(ScheduledNotification scheduledNotification)
    {
        this.scheduledNotification = scheduledNotification;
    }

    public void notifyStudentsOfNewAssignment(Assignment2 assignment)
    throws IdUnusedException, UserNotDefinedException
    {
        String assignmentId = assignment.getId().toString();
        if (!assignment.isDraft())
        {
            Time openTime = timeService.newTime(assignment.getOpenDate().getTime());

            // Remove any existing notifications for this assignment
            DelayedInvocation[] fdi = scheduledInvocationManager.findDelayedInvocations("org.sakaiproject.assignment2.logic.ScheduledNotification",
                    assignmentId);
            if (fdi != null && fdi.length > 0)
            {
                for (DelayedInvocation d : fdi)
                {
                    scheduledInvocationManager.deleteDelayedInvocation(d.uuid);
                }
            }
            // Schedule the new notification
            if (openTime.after(timeService.newTime()))
            {
                scheduledInvocationManager.createDelayedInvocation(openTime,
                        "org.sakaiproject.assignment2.logic.ScheduledNotification",
                        assignmentId);
            }
            else
            {
                scheduledNotification.execute(assignmentId);
            }
        }
    }
}
