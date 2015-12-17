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
package org.sakaiproject.assignment2.logic.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.logic.AssignmentBundleLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.AttachmentInformation;
import org.sakaiproject.assignment2.logic.ExternalContentLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.ScheduledNotification;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.SubmissionAttachment;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.email.api.DigestService;
import org.sakaiproject.email.api.EmailService;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.user.api.User;
import org.sakaiproject.util.FormattedText;
import org.sakaiproject.util.StringUtil;
import org.sakaiproject.util.Validator;

public class ScheduledNotificationImpl implements ScheduledNotification
{
    String relativeAccessPoint = null;

    private static final String MULTIPART_BOUNDARY = "======sakai-multi-part-boundary======";
    private static final String MIME_ADVISORY = "This message is for MIME-compliant mail readers.";
    private static final String BOUNDARY_LINE = "\n\n--" + MULTIPART_BOUNDARY + "\n";
    private static final String PLAIN_TEXT_HEADERS = "Content-Type: text/plain\n\n";
    private static final String HTML_HEADERS = "Content-Type: text/html\n\n";
    private static final String HTML_END = "\n  </body>\n</html>\n";
    private static final String TERMINATION_LINE = "\n\n--" + MULTIPART_BOUNDARY + "--\n\n";

    private AssignmentPermissionLogic permissionLogic;
    public void setAssignmentPermissionLogic(AssignmentPermissionLogic permissionLogic)
    {
        this.permissionLogic = permissionLogic;
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    private EmailService emailService;
    public void setEmailService(EmailService emailService)
    {
        this.emailService = emailService;
    }

    private AssignmentBundleLogic assignmentBundleLogic;
    public void setAssignmentBundleLogic(AssignmentBundleLogic assignmentBundleLogic)
    {
        this.assignmentBundleLogic = assignmentBundleLogic;
    }

    private ServerConfigurationService serverConfigurationService;
    public void setServerConfigurationService(
            ServerConfigurationService serverConfigurationService)
    {
        this.serverConfigurationService = serverConfigurationService;
    }

    private DigestService digestService;
    public void setDigestService(DigestService digestService)
    {
        this.digestService = digestService;
    }

    private ExternalContentLogic contentLogic;
    public void setExternalContentLogic(ExternalContentLogic contentLogic) {
        this.contentLogic = contentLogic;
    }

    private static final Log log = LogFactory.getLog(ScheduledNotificationImpl.class);

    /**
     * Final initialization, once all dependencies are set.
     */
    public void init()
    {
        relativeAccessPoint = AssignmentConstants.REFERENCE_ROOT;
        if (log.isDebugEnabled()) log.debug("init()");
    }

    public void execute(String opaqueContext)
    {
        /* Assignment2 assignment = assignmentLogic.getAssignmentByIdWithGroups(new Long(
                opaqueContext));

        String context = assignment.getContextId();
        Set<AssignmentGroup> groups = assignment.getAssignmentGroupSet();

        // Send to all members of the groups associated with the assignment unless there are no groups,
        // then we send to all site members
        if (groups != null && !groups.isEmpty())
            for (AssignmentGroup group : groups)
            {
                context = group.getGroupId();
                try
                {
                    for (Object u : authzGroupService.getAuthzGroup(context).getMembers())
                    {
                        Member user = (Member) u;
                        notifyUser(user.getUserId(), assignment);
                    }
                }
                catch (GroupNotDefinedException e)
                {
                    log.error(e);
                }
            }
        else
        {
            try
            {
                for (Object u : siteService.getSite(context).getUsers())
                {
                    String userId = (String) u;
                    notifyUser(userId, assignment);
                }
            }
            catch (IdUnusedException e)
            {
                log.error(e);
            }
        }*/

    }

    private void notifyUser(String userId, Assignment2 assignment)
    {
        User user = externalLogic.getUser(userId);
        if (user != null && user.getEmail() != null && 
                StringUtil.trimToNull(user.getEmail()) != null) {
            ArrayList<User> receivers = new ArrayList<User>();
            receivers.add(user);

            emailService.sendToUsers(receivers, buildNotificationHeaders(user
                    .getEmail(), "noti.subject.post.content"),
                    buildPostNotificationMessage(assignment));
        }

    }

    public void notifyStudentThatSubmissionWasAccepted(AssignmentSubmission submission) {
        // send notification
        User user = externalLogic.getUser(submission.getUserId());

        if (user != null && user.getEmail() != null && StringUtil.trimToNull(user.getEmail()) != null)
        {
            ArrayList<User> receivers = new ArrayList<User>();
            receivers.add(user);

            emailService.sendToUsers(receivers, buildNotificationHeaders(
                    user.getEmail(), "noti.subject.submission.content"),
                    buildSubmissionNotificationMessage(submission));
        }

    }

    private List<String> buildNotificationHeaders(String receiverEmail,
            String subjectMessageId)
            {
        ArrayList<String> rv = new ArrayList<String>();

        rv.add("MIME-Version: 1.0");
        rv.add("Content-Type: multipart/alternative; boundary=\"" + MULTIPART_BOUNDARY
                + "\"");
        // set the subject
        rv.add(buildSubject(subjectMessageId));

        // from
        rv.add(buildFrom());

        // to
        if (StringUtil.trimToNull(receiverEmail) != null) rv.add("To: " + receiverEmail);

        return rv;
            }

    private String buildSubject(String messageId)
    {
        return assignmentBundleLogic.getString("noti.subject.label") + " "
        + assignmentBundleLogic.getString(messageId);
    }

    private String buildFrom()
    {
        return "From: " + "\""
        + serverConfigurationService.getString("ui.service", "Sakai")
        + "\"<no-reply@" + serverConfigurationService.getServerName() + ">";
    }

    private String buildPostNotificationMessage(Assignment2 assignment)	{
        StringBuilder message = new StringBuilder();
        message.append(MIME_ADVISORY);
        message.append(BOUNDARY_LINE);
        message.append(PLAIN_TEXT_HEADERS);
        message.append(plainTextContent(assignment));
        message.append(BOUNDARY_LINE);
        message.append(HTML_HEADERS);
        message.append(htmlPreamble("noti.subject.post.content"));
        message.append(htmlContent(assignment));
        message.append(HTML_END);
        message.append(TERMINATION_LINE);
        return message.toString();
    }

    /**
     * Get the message for the email.
     * 
     * @param event
     *        The event that matched criteria to cause the notification.
     * @return the message for the email.
     */
    private String buildSubmissionNotificationMessage(AssignmentSubmission submission) {
        StringBuilder message = new StringBuilder();
        message.append(MIME_ADVISORY);
        message.append(BOUNDARY_LINE);
        message.append(PLAIN_TEXT_HEADERS);
        message.append(plainTextContent(submission));
        message.append(BOUNDARY_LINE);
        message.append(HTML_HEADERS);
        message.append(htmlPreamble("noti.subject.submission.content"));
        message.append(htmlContent(submission));
        message.append(HTML_END);
        message.append(TERMINATION_LINE);
        return message.toString();
    }

    /**
     * Access the partial URL that forms the root of resource URLs.
     * 
     * @param relative -
     *        if true, form within the access path only (i.e. starting with
     *        /msg)
     * @return the partial URL that forms the root of resource URLs.
     */
    private String getAccessPoint(boolean relative)
    {
        return (relative ? "" : serverConfigurationService.getAccessUrl())
        + relativeAccessPoint;
    }

    private String plainTextContent(AssignmentSubmission s) {
        return htmlContent(s);
    }

    private String plainTextContent(Assignment2 a) {
        return htmlContent(a);
    }

    private String htmlPreamble(String messageId)
    {
        StringBuilder buf = new StringBuilder();
        buf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n");
        buf.append("    \"http://www.w3.org/TR/html4/loose.dtd\">\n");
        buf.append("<html>\n");
        buf.append("  <head><title>");
        buf.append(buildSubject(messageId));
        buf.append("</title></head>\n");
        buf.append("  <body>\n");
        return buf.toString();
    }

    private String htmlContent(AssignmentSubmission s) {
        String newline = "<br />\n";

        Assignment2 a = s.getAssignment();
        String context = a.getContextId();
        Site site = externalLogic.getSite(context);
        if (site == null) {
            throw new IllegalArgumentException("There was a problem retrieving " +
            "the site associated with this submission. No notifications sent.");
        }

        StringBuilder content = new StringBuilder();

        // format the date display
        DateFormat df = externalLogic.getDateFormat(null, DateFormat.FULL, assignmentBundleLogic.getLocale(), true);

        // site title and id
        content.append(assignmentBundleLogic.getString("noti.site.title") + " "
                + site.getTitle() + newline);
        content.append(assignmentBundleLogic.getString("noti.site.id") + " " + site.getId()
                + newline + newline);

        // assignment title and due date
        content.append(assignmentBundleLogic.getString("noti.assignment") + " "
                + a.getTitle() + newline);

        String dueDateDisplay = assignmentBundleLogic.getString("noti.assignment.no_due_date");
        if (a.getDueDate() != null)
        {
            dueDateDisplay = df.format(a.getDueDate());
        }
        content.append(assignmentBundleLogic.getString("noti.assignment.due_date")
                + " " + dueDateDisplay + newline + newline);

        // submitter name and id
        AssignmentSubmissionVersion curSubVers = s.getCurrentSubmissionVersion();
        String submitterId = curSubVers.getCreatedBy();
        User submitter = externalLogic.getUser(submitterId);
        String submitterDisplay = assignmentBundleLogic.getFormattedMessage("noti.submit.student_display", new String[] {submitter.getDisplayName(), submitter.getDisplayId()});
        content.append(assignmentBundleLogic.getString("noti.student") + " "
                + submitterDisplay + newline + newline);

        // submission and version ids
        content.append(assignmentBundleLogic.getString("noti.submit.id") + " "
                + s.getId() + newline);
        content.append(assignmentBundleLogic.getString("noti.submit.version_id") + " "
                + s.getCurrentSubmissionVersion().getId() + newline);

        // submit time
        String submitDateDisplay = "";
        if (curSubVers.getSubmittedDate() != null)
        {
            submitDateDisplay = df.format(curSubVers.getSubmittedDate());
        }

        content.append(assignmentBundleLogic.getString("noti.submit.time") + " "
                + submitDateDisplay + newline + newline);

        // submit text
        String text = StringUtil.trimToNull(curSubVers.getSubmittedText());
        if (text != null)
        {
            content.append(assignmentBundleLogic.getString("noti.submit.text") + newline
                    + Validator.escapeHtmlFormattedText(text) + newline + newline);
        }

        // attachment if any
        Set<SubmissionAttachment> attachments = curSubVers.getSubmissionAttachSet();
        if (attachments != null && attachments.size() > 0)
        {
            content.append(assignmentBundleLogic.getString("noti.submit.attachments")
                    + newline);
            for (SubmissionAttachment attachment : attachments)
            {
                String ref = attachment.getAttachmentReference();

                AttachmentInformation attach = contentLogic.getAttachmentInformation(ref);
                if (attach != null) {

                    String resourceLengthDisplay = assignmentBundleLogic.getFormattedMessage(
                            "noti.submit.attach_size_display", new Object[] {attach.getContentLength()});
                    content.append(attach.getDisplayName() + " " + resourceLengthDisplay + newline);
                }

            }
        }

        return content.toString();
    }

    private String htmlContent(Assignment2 a) {
        String newline = "<br />\n";

        String context = a.getContextId();

        Site site = externalLogic.getSite(context);
        if (site == null) {
            throw new IllegalArgumentException("There was a problem retrieving " +
            "the site associated with this submission. No notifications sent.");
        }

        String siteTitle = site.getTitle();
        String siteId = site.getId();

        StringBuilder content = new StringBuilder();
        // site title and id
        content.append(assignmentBundleLogic.getString("noti.site.title") + " "
                + siteTitle + newline);
        content.append(assignmentBundleLogic.getString("noti.site.id") + " " + siteId
                + newline + newline);
        // assignment title and due date
        content.append(assignmentBundleLogic.getString("noti.assignment") + " "
                + a.getTitle() + newline);

        DateFormat df = externalLogic.getDateFormat(null, DateFormat.FULL, assignmentBundleLogic.getLocale(), true);
        String dueDateDisplay = assignmentBundleLogic.getString("noti.assignment.no_due_date");
        if (a.getDueDate() != null)
        {
            dueDateDisplay = df.format(a.getDueDate());
            content.append(assignmentBundleLogic.getString("noti.assignment.duedate")
                    + " " + dueDateDisplay + newline + newline);
        }

        return content.toString();
    }

    public void notifyInstructorsOfSubmission(AssignmentSubmission submission) {

        // need to send notification email
        //TODO - make this customizable. for now, send all as digest
        boolean sendAsDigest = true;

        List<String> userIdsToNotify = permissionLogic.getUsersAllowedToViewStudentForAssignment(submission.getUserId(), submission.getAssignment().getId());
        if (userIdsToNotify != null) {
            // let's convert these userIds to User objects
            Collection<User> users = externalLogic.getUserIdUserMap(userIdsToNotify).values();
            Set<User> usersToNotify = new HashSet<User>();
            if (users != null) {
                usersToNotify.addAll(users);
            }

            if (usersToNotify != null && !usersToNotify.isEmpty()) {

                if (sendAsDigest) {
                    String messageBody = htmlContent(submission);
                    // escape html
                    messageBody = FormattedText.convertFormattedTextToPlaintext(messageBody);
                    for (User user : usersToNotify)
                    {
                        // digest the message to each user
                        digestService.digest(user.getId(),
                                buildSubject("noti.subject.submission.content"), messageBody);
                        if (log.isDebugEnabled()) log.debug("Digest message added for user: " + user.getDisplayId());
                    }
                } else {
                    // send the message immediately
                    String messageBody = buildSubmissionNotificationMessage(submission);
                    emailService.sendToUsers(usersToNotify, buildNotificationHeaders(null,
                    "noti.subject.submission.content"), messageBody);
                }
            }
        }
    }

}
