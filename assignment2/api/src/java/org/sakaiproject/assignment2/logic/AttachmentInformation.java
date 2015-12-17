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


/**
 * An AttachmentInformation object (not persisted) for supplying useful data from the 
 * ContentResource object and its related properties referenced by the
 * Assignment2 attachments
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public class AttachmentInformation {

    private String reference;
    private String displayName;
    private String contentLength;
    private String contentType;
    private String contentTypeImagePath;
    private String url;

    public AttachmentInformation() {
    }

    /**
     * 
     * @return the reference identifying this attachment 
     */
    public String getReference()
    {
        return reference;
    }

    /**
     * the reference identifying this attachment
     * @param reference
     */
    public void setReference(String reference)
    {
        this.reference = reference;
    }

    /**
     * 
     * @return the display name for this attachment. ie "linearAlgebra.txt"
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * the display name for this attachment. ie "linearAlgebra.txt"
     * @param displayName
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * 
     * @return the content length in a human-readable format ie "156 KB" or "2.2 MB"
     */
    public String getContentLength()
    {
        return contentLength;
    }

    /**
     * the content length in a human-readable format ie "156 KB" or "2.2 MB"
     * @param contentLength
     */
    public void setContentLength(String contentLength)
    {
        this.contentLength = contentLength;
    }

    /**
     * 
     * @return the content type property for this attachment
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * the content type property for this attachment
     * @param contentType
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    /**
     * 
     * @return String of path for the resource image type icon
     */
    public String getContentTypeImagePath()
    {
        return contentTypeImagePath;
    }

    /**
     * String of path for the resource image type icon
     * @param contentTypeImagePath
     */
    public void setContentTypeImagePath(String contentTypeImagePath)
    {
        this.contentTypeImagePath = contentTypeImagePath;
    }

    /**
     * 
     * @return The URL which can be used to access the resource
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * The URL which can be used to access the resource
     * @param url
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
}
