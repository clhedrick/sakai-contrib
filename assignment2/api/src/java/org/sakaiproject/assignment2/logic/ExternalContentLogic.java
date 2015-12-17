/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/GradebookItem.java $
 * $Id: GradebookItem.java 48274 2008-04-23 20:07:00Z wagnermr@iupui.edu $
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

import org.sakaiproject.content.api.ContentResource;


/**
 * This is the interface for logic that interacts with the ContentHostingService
 * 
 * @author Sakai App Builder -AZ
 */
public interface ExternalContentLogic {

    /**
     * @param reference 
     * @return an AttachmentInformation object populated with useful data from
     * the ContentResource object with the given reference.
     * Returns null if there was a problem retrieving the associated ContentResource
     */
    public AttachmentInformation getAttachmentInformation(String reference);

    /**
     * 
     * @param reference non-null
     * @return the ContentResource associated with the given reference.
     * Returns null if unable to retrieve resource
     */
    public ContentResource getContentResource(String reference);


    /**
     * @param contextId non-null
     * @return the collection id of the root collection for this site id
     */
    public String getReferenceCollectionId(String contextId);


    /**
     * Given the reference to an existing attachment and contextId of the site
     * you want the new attachment to be associated with, saves a copy of the
     * passed attachment in the given site
     * @param attachmentReference
     * @param contextId
     * @return the reference to the newly created attachment. returns null if
     * there was a problem while attempting to copy the attachment and no
     * new attachment was created
     */
    public String copyAttachment(String attId, String contextId);

    /**
     * 
     * @param userId
     * @return the collectionId for the My Workspace content associated with
     * the given userId.  Returns null if collectionId could not be found.
     */
    public String getMyWorkspaceCollectionId(String userId);
}
