/**********************************************************************************
* $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/java/org/sakaiproject/assignment2/taggable/impl/AssignmentItemImpl.java $
* $Id: AssignmentItemImpl.java 86090 2014-04-14 18:15:43Z dsobiera@indiana.edu $
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

package org.sakaiproject.assignment2.taggable.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.taggable.api.TaggableActivity;
import org.sakaiproject.taggable.api.TaggableItem;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.util.ResourceLoader;

public class AssignmentItemImpl implements TaggableItem {

    private static final Log logger = LogFactory
    .getLog(AssignmentItemImpl.class);

    private static ResourceLoader rb = new ResourceLoader("messages");

    protected static final String ITEM_REF_SEPARATOR = "@";

    protected AssignmentSubmission submission;

    protected String userId;

    protected TaggableActivity activity;

    public AssignmentItemImpl(AssignmentSubmission submission, String userId,
            TaggableActivity activity) {
        this.submission = submission;
        this.userId = userId;
        this.activity = activity;
    }

    public TaggableActivity getActivity() {
        return activity;
    }

    public String getContent() {
        return submission.getCurrentSubmissionVersion().getSubmittedText();
    }

    public Object getObject() {
        return submission;
    }

    public String getReference() {
        StringBuilder sb = new StringBuilder();
        sb.append(submission.getReference());
        sb.append(ITEM_REF_SEPARATOR);
        sb.append(userId);
        return sb.toString();
    }

    public String getTitle() {
        StringBuilder sb = new StringBuilder();
        try {
            User user = UserDirectoryService.getUser(userId);
            sb.append(user.getFirstName());
            sb.append(' ');
            sb.append(user.getLastName());
            sb.append(' ');
            sb.append(rb.getString("gen.submission"));
        } catch (UserNotDefinedException unde) {
            logger.error(unde.getMessage(), unde);
        }
        return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public String getItemDetailUrl()
    {
        String url = ServerConfigurationService.getServerUrl() + "/direct/view-assignment2-submission/" + 
        Long.toString(submission.getId());
        
        return url;
    }
    
    public String getItemDetailPrivateUrl(){
        return getItemDetailUrl();
    }

    public String getIconUrl()
    {
        String url = ServerConfigurationService.getServerUrl() + "/library/image/silk/page_edit.png";
        return url;
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof AssignmentItemImpl))
            return false;
        else if (!((TaggableItem) obj).getReference().equals(this.getReference()))
            return false;

        return true;
    }

    public int hashCode()
    {
        return this.getReference().hashCode();
    }

    public String getItemDetailUrlParams()
    {
        // TODO Auto-generated method stub
        return "?";
    }

    public Date getLastModifiedDate()
    {
        return submission.getModifiedDate();
    }

    public String getOwner()
    {
        String subUserId = submission.getUserId();
        String owner = subUserId;
        try {    		
            User user = UserDirectoryService.getUser(subUserId);
            owner = user.getDisplayName();
        } catch (UserNotDefinedException e) {
            logger.warn("Unable to get display name from user id: " + subUserId, e);
        }
        return owner;
    }

    public String getSiteTitle()
    {
        String siteId = ((AssignmentSubmission)getObject()).getAssignment().getContextId();
        String title = getSite(siteId).getTitle();
        
        return title;
    }

    public String getTypeName()
    {
        return rb.getString("service_name");
    }

    public boolean getUseDecoration()
    {
        // this appends some extra osp junk to the end of the url before the parameters
        // and we lose the params when this is there for some reason
        return false;
    }
    
    private Site getSite(String siteId) {
        Site site = null;
        try {
            site = SiteService.getSite(siteId);
        } catch (IdUnusedException e) {
            logger.error("Unable to get Site object from site id: " + siteId, e);
        }
        return site;
    }
}
