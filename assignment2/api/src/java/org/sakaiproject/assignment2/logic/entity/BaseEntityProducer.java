/**********************************************************************************
 *
 * $Id: BaseEntityProducer.java 66569 2010-03-16 16:20:09Z chmaurer@iupui.edu $
 *
 ***********************************************************************************
 *
 * Copyright (c) 2006 The Regents of the University of California, The MIT Corporation
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

package org.sakaiproject.assignment2.logic.entity;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.EntityProducer;
import org.sakaiproject.entity.api.HttpAccess;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.cover.EntityManager;

/**
 * Utility class that provides safe defaults for all EntityProducer methods.
 * By subclassing, developers can focus only on the methods they
 * actually need to customize. External configuration files can be used to
 * set their label, reference root, and service name. The public "init()" method can be
 * used to register as an EntityProducer.
 */
public class BaseEntityProducer implements EntityProducer {
    private static final Log log = LogFactory.getLog(BaseEntityProducer.class);

    private String label;	// This should always be set.
    private String referenceRoot = Entity.SEPARATOR + AssignmentConstants.REFERENCE_ROOT;
    private String serviceName = null;

    /**
     * Register this class as an EntityProducer.
     */
    public void init() {
        EntityManager.registerEntityProducer(this, referenceRoot);
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public String getReferenceRoot() {
        return referenceRoot;
    }
    public void setReferenceRoot(String referenceRoot) {
        this.referenceRoot = referenceRoot;
    }

    /**
     * Although not required, the service name is frequently used. As a
     * convenience, it's also settable by this bean.
     */
    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    // EntityProducer methods begin here.

    public String getLabel() {
        return label;
    }

    public boolean willArchiveMerge() {
        return false;
    }

    public String archive(String siteId, Document doc, Stack stack, String archivePath, List attachments) {
        return null;
    }

    public String merge(String siteId, Element root, String archivePath, String fromSiteId, Map attachmentNames, Map userIdTrans,
            Set userListAllowImport) {
        return null;
    }

    public boolean parseEntityReference(String reference, Reference ref) {
        if (reference.startsWith(referenceRoot)) {
            String[] parts = reference.split(Entity.SEPARATOR, 5);
            if (parts.length < 4) {
                return false;
            }
            String type = parts[1];
            /*
             * This is only really used so we know what kind of object we are
             * referencing
             */
            String subtype = parts[2];

            String context = parts[3];
            /*
             * This is only used when we have a reference to a specific object
             */
            String id = parts[4];

            ref.set(type, subtype, id, null, context);
            return true;
        }
        return false;
    }

    public String getEntityDescription(Reference ref) {
        return null;
    }

    public ResourceProperties getEntityResourceProperties(Reference ref) {
        return null;
    }

    public Entity getEntity(Reference ref) {
        return null;
    }

    public String getEntityUrl(Reference ref) {
        return null;
    }

    public Collection getEntityAuthzGroups(Reference ref, String userId) {
        return null;
    }

    public HttpAccess getHttpAccess() {
        return null;
    }
}
