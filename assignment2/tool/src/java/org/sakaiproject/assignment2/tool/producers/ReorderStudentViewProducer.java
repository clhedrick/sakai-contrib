package org.sakaiproject.assignment2.tool.producers;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ReorderStudentViewProducer implements ViewComponentProducer {
    public static final String VIEW_ID = "reorder-student-view";
    
    private MessageLocator messageLocator;

    public void fillComponents(UIContainer tofill, ViewParameters viewparams,
            ComponentChecker checker) {    
        
        //Breadcrumbs
        UIInternalLink.make(tofill, "breadcrumb", 
                messageLocator.getMessage("assignment2.list.heading"),
                new SimpleViewParameters(ListProducer.VIEW_ID));
        
        UIForm form = UIForm.make(tofill, "reorder-form");
        UIInput.make(form, "asnn-order", "ReorderAssignmentsAction.orderedAssignIds");
        
        UICommand.make(form, "save-reorder-top", UIMessage.make("assignment2.reorder-student-view.save"), "ReorderAssignmentsAction.execute");
        UICommand.make(form, "save-reorder-bottom", UIMessage.make("assignment2.reorder-student-view.save"), "ReorderAssignmentsAction.execute");
        
        UICommand.make(form, "cancel-reorder-top", UIMessage.make("assignment2.reorder-student-view.cancel"), "ReorderAssignmentsAction.cancel");
        UICommand.make(form, "cancel-reorder-bottom", UIMessage.make("assignment2.reorder-student-view.cancel"), "ReorderAssignmentsAction.cancel");

    }

    public String getViewID() {
        return VIEW_ID;
    }
    
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }
    
}
