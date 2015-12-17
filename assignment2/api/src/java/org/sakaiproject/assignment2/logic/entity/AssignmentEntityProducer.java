/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/api/src/java/org/sakaiproject/assignment2/logic/entity/AssignmentEntityProducer.java $
 * $Id: AssignmentEntityProducer.java 87083 2014-10-06 22:58:48Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.logic.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.entity.api.ContextObserver;
import org.sakaiproject.entity.api.EntityTransferrer;
import org.sakaiproject.entity.api.EntityTransferrerRefMigrator;
import org.sakaiproject.importer.api.HandlesImportable;
import org.sakaiproject.importer.api.Importable;

import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ImportExportLogic;

/**
 * Implements the Sakai EntityProducer approach to integration of tool-specific
 * storage with site management.
 */
public class AssignmentEntityProducer extends BaseEntityProducer implements ContextObserver, EntityTransferrer, EntityTransferrerRefMigrator, HandlesImportable {
    private static final Log log = LogFactory.getLog(AssignmentEntityProducer.class);

    private String[] toolIdArray;

    public void setToolIds(List toolIds) {
        if (log.isDebugEnabled()) log.debug("setToolIds(" + toolIds + ")");
        if (toolIds != null) {
            toolIdArray = (String[])toolIds.toArray(new String[0]);
        }
    }

    public String[] myToolIds() {
        return toolIdArray;
    }

    public void contextCreated(String context, boolean toolPlacement) {
        // If the Assignment2 tool is part of this site, we may need to
        // add the backend for the gradebook, as well (not the actual tool) if
        // it doesn't already exist
        if (context != null && toolPlacement) {
            gradebookLogic.createGradebookDataIfNecessary(context);
        }

    }

    public void contextUpdated(String context, boolean toolPlacement) {
        // If the Assignment2 tool is part of this site, we may need to
        // add the backend for the gradebook, as well (not the actual tool) if
        // it doesn't already exist
        if (context != null && toolPlacement) {
            gradebookLogic.createGradebookDataIfNecessary(context);
        }
    }

    public void contextDeleted(String context, boolean toolPlacement) {
        // do nothing
    }

    public void transferCopyEntities(String fromContext, String toContext, List ids) {
	transferCopyEntitiesRefMigrator(fromContext, toContext, ids);
    }

    public Map<String, String> transferCopyEntitiesRefMigrator(String fromContext, String toContext, List ids) {
        String fromAssignment2ToolXml = importExportLogic.getAssignmentToolDefinitionXML(fromContext);
        return importExportLogic.mergeAssignmentToolDefinitionXml(toContext, fromAssignment2ToolXml);
    }

    public void transferCopyEntities(String fromContext, String toContext, List ids, boolean cleanup) {
	transferCopyEntitiesRefMigrator(fromContext, toContext, ids, cleanup);
    }

    public Map<String, String> transferCopyEntitiesRefMigrator(String fromContext, String toContext, List ids, boolean cleanup) {
        if (cleanup) {
            // we need to remove all assignments in the current site
            importExportLogic.cleanToolForImport(toContext);
        }

        return transferCopyEntitiesRefMigrator(fromContext, toContext, ids);
    }

    public void updateEntityReferences(String toContext, Map<String, String> transversalMap){
        importExportLogic.updateEntityReferences(toContext, transversalMap);
    }

    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }

    private ImportExportLogic importExportLogic;
    public void setImportExportLogic(ImportExportLogic importExportLogic) {
        this.importExportLogic = importExportLogic;
    }

    ////////////////////////////////////////////////////////////////
    // TODO Speculative support for future migration / import / export starts here.

    public static final String A2_DEFINITION_TYPE = "sakai-assignment2";

    public boolean canHandleType(String typeName) {
        return (typeName.equals(A2_DEFINITION_TYPE));
    }

    public void handle(Importable importable, String siteId) {
        if (importable.getTypeName().equals(A2_DEFINITION_TYPE)) {
            //TODO implement import
        }
    }

    public List<Importable> getAllImportables(String contextId) {
        List<Importable> importables = new ArrayList<Importable>();
        importables.add(new XmlImportable(A2_DEFINITION_TYPE, importExportLogic.getAssignmentToolDefinitionXML(contextId)));
        return importables;
    }
}
