package org.sakaiproject.assignment2.tool.producers.evolvers;

import org.sakaiproject.assignment2.logic.AttachmentInformation;
import org.sakaiproject.assignment2.logic.ExternalContentLogic;

import uk.org.ponder.beanutil.BeanGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBasicListMember;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIInputMany;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UIStyleDecorator;
import uk.org.ponder.rsf.uitype.UITypes;


/**
 * This evolver renders the list of attachments on the Student Submission Editor
 * and previewer.  This includes teh name of the attachment, it's size, and an
 * optional remove link.
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class AttachmentInputEvolver {

    public static final String COMPONENT_ID = "attachment-list:";
    public static final String CORE_ID = "attachment-list-core:";

    private ExternalContentLogic contentLogic;
    public void setExternalContentLogic(ExternalContentLogic contentLogic) {
        this.contentLogic = contentLogic;
    }

    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    private BeanGetter rbg;
    public void setRequestBeanGetter(BeanGetter rbg) {
        this.rbg = rbg;
    }

    /**
     * Renders the attachments.  There will be an option to remove them.
     * 
     * @param toevolve
     * @param elementId a unique identifier that will be appended to the attachments-binding-id class to
     * uniquely identify which element the attachments will be associated with. if null, nothing will be appended
     * @return
     */
    public UIJointContainer evolveAttachment(UIInputMany toevolve, String elementId) {
        return evolveAttachment(toevolve, true, elementId);
    }

    /**
     * Renders the list of Attachments.  If removable is true, then a link for
     * removing the attachment is rendered as well.
     * 
     * @param toevolve
     * @param removable
     * @param elementId a unique identifier that will be appended to the attachments-binding-id class to
     * uniquely identify which element the attachments will be associated with. if null, nothing will be appended
     * @return
     */
    public UIJointContainer evolveAttachment(UIInputMany toevolve, boolean removable, String elementId) {
        UIJointContainer togo = new UIJointContainer(toevolve.parent, toevolve.ID, COMPONENT_ID);
        toevolve.parent.remove(toevolve);

        toevolve.ID = "attachments-input";
        togo.addComponent(toevolve);

        String[] value = toevolve.getValue();
        // Note that a bound value is NEVER null, an unset value is detected via
        // this standard call
        if (UITypes.isPlaceholder(value)) {
            value = (String[]) rbg.getBean(toevolve.valuebinding.value);
            // May as well save on later fixups
            toevolve.setValue(value);
        }

        UIBranchContainer core = UIBranchContainer.make(togo, CORE_ID);
        core.decorate(new UIFreeAttributeDecorator("class", getAttachmentListClassName(elementId) + " attachList"));
        
        int limit = Math.max(0, value.length);
        for (int i=0; i < limit; ++i) {
            UIBranchContainer row = UIBranchContainer.make(core, "attachment-list-row:", Integer.toString(i));
            String thisvalue = i < value.length ? value[i] : "";

            AttachmentInformation attach = contentLogic.getAttachmentInformation(thisvalue);
            if (attach != null) {
                String fileSizeDisplay = "(" + attach.getContentLength() + ")";

                UILink.make(row, "attachment_image", attach.getContentTypeImagePath());
                UILink.make(row, "attachment_link", attach.getDisplayName(), attach.getUrl());
                UIOutput.make(row, "attachment_size", fileSizeDisplay);

                UIBasicListMember.makeBasic(row, "attachment_item", toevolve.getFullID(), i);

                //Add remove link
                if (removable) {
                    UIVerbatim.make(row, "attachment_remove", 
                            "<a href=\"#\" " +
                            "onclick=\"" +
                            "asnn2.removeAttachmentConfirm(this);" +
                            "\">" +
                            messageLocator.getMessage("assignment2.remove") +
                    "</a>");
                }
            }

        }

        // A few notes about this attachment functionality.  Currently all
        // the demo items are added in the javascript/html in AttachmentEvolver.html
        // and asnn2.updateAttachments javascript. It's a bit odd and fragile,
        // especially if the input names ever change.
        //if (limit == 0) {
            //output "demo" row, with styleClass of skip
            //UIBranchContainer row = UIBranchContainer.make(togo, "attachment-list-demo:", Integer.toString(0));
            //UILink.make(row, "attachment_image", "image.jpg");
            //UILink.make(row, "attachment_link", "demo", "demo.html");
            //UIOutput.make(row, "attachment_item", "demo");
            //UIBasicListMember.makeBasic(togo, "attachment_item_demo", toevolve.getFullID(), 0);
            UIOutput attachBinding = UIOutput.make(togo, "attachments-binding-id", toevolve.getFullID());
            attachBinding.decorate(new UIFreeAttributeDecorator("class", getAttachmentBindingClassName(elementId)));
            //UIOutput.make(row, "attachment_size", "demo");
            //Add remove link
            /*
            UIVerbatim.make(row, "attachment_remove", 
                    "<a href=\"#\" " +
                    "onclick=\"" +
                    "asnn2.removeAttachment(this);" +
                    "\">" +
                    messageLocator.getMessage("assignment2.remove") +
            "</a>");
            row.decorators = new DecoratorList(new UIStyleDecorator("skip"));
            */
        //}

        return togo;
    }
    
    private String getAttachmentBindingClassName(String elementId) {
        String attachBindingClass = "attachments-binding-id";
        if (elementId != null) {
            attachBindingClass += elementId;
        }
        
        return attachBindingClass;
    }
    
    private String getAttachmentListClassName(String elementId) {
        String attachListClass = "attachment-list-core";
        if (elementId != null) {
            attachListClass += elementId;
        }
        
        return attachListClass;
    }
    
    /**
     * 
     * @param elementId - the id that will be used to create unique class names. You may leave this null, but
     * make sure this has a non-null value if there will be more than one instance of the "Add Attachments"
     * section on your page.
     * @return the javascript markup that must be called upon clicking the "Add attachments" link.
     * This link is rendered on the individual screens and is independent of the attachments section rendering, so
     * use this common method to help unify the behavior. We need to do some funky things to accomodate
     * the scenario with multiple "Add Attachments" sections on a single page
     */
    public String getOnclickMarkupForAddAttachmentEvent(String elementId) {
        String bindingClass = getAttachmentBindingClassName(elementId);
        String attachListClass = getAttachmentListClassName(elementId);
        StringBuilder sb = new StringBuilder();
        sb.append("asnn2.updateAttachmentVariables('");
        sb.append("." + bindingClass);
        sb.append("','");
        sb.append("." + attachListClass);
        sb.append("');");
        
        return sb.toString();
    }

}
