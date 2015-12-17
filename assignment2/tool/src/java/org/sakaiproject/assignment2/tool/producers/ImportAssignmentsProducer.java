package org.sakaiproject.assignment2.tool.producers;

import java.util.List;

import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ImportAssignmentsProducer implements ViewComponentProducer{
	
	public static final String VIEW_ID = "import-assignments";	
	public String getViewID() {
		return VIEW_ID;
	}
	
	private MessageLocator messageLocator;

	private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		//Breadcrumbs
        UIInternalLink.make(tofill, "breadcrumb", 
                messageLocator.getMessage("assignment2.list.heading"),
                new SimpleViewParameters(ListProducer.VIEW_ID));        
        //Breadcrumb
        UIMessage.make(tofill, "last_breadcrumb", "assignment2.import-assignments.import_heading");
        //Heading messages
        UIMessage.make(tofill, "page-title", "assignment2.import-assignments.title");
        
        UIMessage.make(tofill, "instructions", "assignment2.import-assignments.instructions");
        
        UIForm form = UIForm.make(tofill, "import-assignments-form");
        
        List<Site> userSites = externalLogic.getUserSitesWithAssignments();
        if(userSites != null && userSites.size() > 0){
        	UICommand.make(form, "import-top", UIMessage.make("assignment2.import-assignments.import"), "ImportAssignmentsAction.execute");
        	UICommand.make(form, "import-bottom", UIMessage.make("assignment2.import-assignments.import"), "ImportAssignmentsAction.execute");

        	UICommand.make(form, "cancel-import-top", UIMessage.make("assignment2.import-assignments.cancel"), "ImportAssignmentsAction.cancel");
        	UICommand.make(form, "cancel-import-bottom", UIMessage.make("assignment2.import-assignments.cancel"), "ImportAssignmentsAction.cancel");


        	UIOutput.make(form, "import-selection-area");
        	for (Site site : userSites) {
        		//Update OTP
        		UIBranchContainer groups_row = UIBranchContainer.make(form, "import_row:");
        		UIBoundBoolean checkbox = UIBoundBoolean.make(groups_row, "import_check",  
        				"ImportAssignmentsAction.selectedIds." + site.getId(), Boolean.FALSE);
        		UIOutput.make(groups_row, "import_label", site.getTitle());
        	}
        }else{
        	//no sites to import from:
        	UICommand.make(form, "cancel-import-bottom", UIMessage.make("assignment2.import-assignments.cancel"), "ImportAssignmentsAction.cancel");
        }
        
	}
	
	public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }


}
