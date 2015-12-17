/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/model/AttachmentBase.java $
 * $Id: AttachmentBase.java 61924 2009-07-21 20:30:37Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.model;

import java.util.Map;


/**
 * The AttachmentBase object.  All attachments extend this object
 * 
 * @author <a href="mailto:wagnermr@iupui.edu">michelle wagner</a>
 */
public abstract class AttachmentBase {

    protected Long id;
    protected String attachmentReference;
    private int optimisticVersion;
    private Map properties;

    /**
     * @return the id of this assignment attachment
     */
    public Long getId() {
        return id;
    }

    /**
     * set the the id of this assignment attachment
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return the reference to this attachment
     */
    public String getAttachmentReference() {
        return attachmentReference;
    }

    /**
     * set the reference to this attachment
     * @param attachmentReference
     */
    public void setAttachmentReference(String attachmentReference) {
        this.attachmentReference = attachmentReference;
    }

    /**
     * 
     * @return version stored for hibernate's automatic optimistic concurrency control.
     * this is not related to any of the submission version data for assignment2
     */
    public int getOptimisticVersion() {
        return optimisticVersion;
    }

    /**
     * version stored for hibernate's automatic optimistic concurrency control.
     * this is not related to any of the submission version data for assignment2
     * @param optimisticVersion
     */
    public void setOptimisticVersion(int optimisticVersion) {
        this.optimisticVersion = optimisticVersion;
    }
    
    // Properties not persisted on this object
    
    /**
     * 
     * @return Unstructured properties for the attachments.  These may or may not
     * be persisted depending on their nature.  Some may be generated at 
     * runtime.
     */
    public Map getProperties()
    {
        return properties;
    }

    /**
     * Unstructured properties for the attachments.  These may or may not
     * be persisted depending on their nature.  Some may be generated at 
     * runtime.
     * @param properties
     */
    public void setProperties(Map properties)
    {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof AttachmentBase)) return false;
        else {
            AttachmentBase compAttach = (AttachmentBase) obj;
            if (this.id == null || compAttach.id == null) {
                return false;
            }
            if (null == this.id || null == compAttach.id) return false;
            else return (
                    this.id.equals(compAttach.id)
            );
        }
    }

    @Override
    public int hashCode() {
        if (null == this.id) return super.hashCode();
        String hashStr = this.getClass().getName() + ":" + this.id.hashCode();
        return hashStr.hashCode();
    }
}
