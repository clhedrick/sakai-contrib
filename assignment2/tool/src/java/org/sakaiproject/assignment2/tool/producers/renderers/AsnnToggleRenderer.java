package org.sakaiproject.assignment2.tool.producers.renderers;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.UIAlternativeTextDecorator;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.producers.BasicProducer;

/**
 * Throughout Assignment2, we use toggles to allow users to expand/collapse
 * different sections. This is used to render those toggles consistently.
 * You must add the class "toggleSubsection" to the element that you want to
 * hide/show, and it must follow immediately after the toggle.
 * 
 *
 */
public class AsnnToggleRenderer implements BasicProducer {
    
    
    // The images were named incorrectly, so the expand is actually the collapse.  Annoying, I know.
    public static final String COLLAPSE_IMAGE_URL = "/sakai-assignment2-tool/content/images/expand.png";
    public static final String EXPAND_IMAGE_URL = "/sakai-assignment2-tool/content/images/collapse.png";
    
    /**
     * 
     * @param tofill
     * @param divID
     * @param elementId optional parameter for you to supply an id for the toggle element. leave null 
     * if you don't want an id
     * @param includeToggleBar true if you want the toggle to appear as a bar (ie with a background). false if you only
     * want the toggle image and heading
     * @param toggleText the text of the toggle
     * @param toggleHoverText the text displayed when hovering over the toggle image
     * @param expand true if you want the toggle expanded
     * @param feedbackReleasedIndicator true if you want to display the feedback released indicator
     * @param readFeedbackIndicator true if you want to display the read feedback indicator
     * @param unreadFeedbackIndicator true if you want to display the unread feedback indicator
     * @param onclickEvent optional onclick event to be associated with this toggle. leave null if
     * you don't want an onclick event
     */
    public void makeToggle(UIContainer tofill, String divID, String elementId, boolean includeToggleBar, String toggleText,
            String toggleHoverText, boolean expand, boolean feedbackReleasedIndicator, boolean readFeedbackIndicator,
            boolean unreadFeedbackIndicator, String onclickEvent) {
        
        UIJointContainer joint = new UIJointContainer(tofill, divID, "assn2-toggle-header-widget:");
        
        if (elementId != null) {
            joint.decorate(new UIFreeAttributeDecorator("id", elementId));
        }
        
        if (onclickEvent != null) {
            joint.decorate(new UIFreeAttributeDecorator("onclick", onclickEvent));
        }
        
        if (!includeToggleBar) {
            // remove the background on the toggle
            joint.decorate(new UIFreeAttributeDecorator("class", "toggleHeader toggleHeaderNoBar"));
        }
        
        UIOutput img = UIOutput.make(joint, "toggle_image");
        String img_location = expand ? EXPAND_IMAGE_URL : COLLAPSE_IMAGE_URL;
        img.decorate(new UIFreeAttributeDecorator("src", img_location));
        img.decorate(new UIAlternativeTextDecorator(toggleHoverText));
        img.decorate(new UITooltipDecorator(toggleHoverText));
        
        UIOutput.make(joint, "toggle_heading", toggleText);
        
        // render the unread feedback indicator, if appropriate.
        if (unreadFeedbackIndicator) {
            UIOutput unreadFbImage = UIOutput.make(joint, "unread_fb_indicator");
            String unreadFbText = messageLocator.getMessage("assignment2.toggle.indicator.feedback.unread");
            unreadFbImage.decorate(new UIAlternativeTextDecorator(unreadFbText));
            unreadFbImage.decorate(new UITooltipDecorator(unreadFbText));
        }
        
        // render the read feedback indicator, if appropriate.
        if (readFeedbackIndicator) {
            UIOutput readFbImage = UIOutput.make(joint, "read_fb_indicator");
            String readFbText = messageLocator.getMessage("assignment2.toggle.indicator.feedback.read");
            readFbImage.decorate(new UIAlternativeTextDecorator(readFbText));
            readFbImage.decorate(new UITooltipDecorator(readFbText));
        }
        
        // render the feedback released indicator, if appropriate
        if (feedbackReleasedIndicator) {
            UIOutput releasedFbImage = UIOutput.make(joint, "fb_released_indicator");
            String releasedFbText = messageLocator.getMessage("assignment2.toggle.indicator.feedback.released");
            releasedFbImage.decorate(new UIAlternativeTextDecorator(releasedFbText));
            releasedFbImage.decorate(new UITooltipDecorator(releasedFbText));
        }
    }

    public void fillComponents(UIContainer parent, String clientID) {

    }
    
    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

}
