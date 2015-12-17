package org.sakaiproject.assignment2.tool.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.ImportExportLogic;
import org.sakaiproject.assignment2.tool.WorkFlowResult;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class ImportAssignmentsCommand {
	
	private Map<String, Boolean> selectedIds = new HashMap<String, Boolean>();

	private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    private ImportExportLogic importExportLogic;
    public void setImportExportLogic(ImportExportLogic importExportLogic) {
        this.importExportLogic = importExportLogic;
    }
    
    
    private TargettedMessageList messages;
    public void setMessages(TargettedMessageList messages) {
        this.messages = messages;
    }
    
	public WorkFlowResult execute() {
		
		if(selectedIds != null){
			for(Entry<String, Boolean> importSites : selectedIds.entrySet()){
				if(importSites.getValue()){
					// if the site we are importing from has the "new" assignment2 tool,
			        // import from that tool. Otherwise, check to see if that site has the
			        // "old" assignments tool.  If it does, we are importing from the old tool
			        // to the new tool
					if (externalLogic.siteHasTool(importSites.getKey(), ExternalLogic.TOOL_ID_OLD_ASSIGN)) {
			            String fromOldAssignmentToolXml =
			                importExportLogic.getAssignmentToolDefinitionXmlFromOriginalAssignmentsTool(importSites.getKey(), externalLogic.getCurrentContextId());
			            importExportLogic.mergeAssignmentToolDefinitionXml(externalLogic.getCurrentContextId(), fromOldAssignmentToolXml);
			        }
				}
			}
		}
		
		messages.addMessage(new TargettedMessage("assignment2.import-assignments.success",  new Object[] {}, TargettedMessage.SEVERITY_INFO));
		
		return WorkFlowResult.IMPORT_ASSIGNMENTS_VIEW_IMPORT;
	}

	public WorkFlowResult cancel() {
		return WorkFlowResult.IMPORT_ASSIGNMENTS_VIEW_CANCEL;
	}
	
	public Map<String, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<String, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

}
