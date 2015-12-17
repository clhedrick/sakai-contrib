/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/renderers/GradebookDetailsRenderer.java $
 * $Id: GradebookDetailsRenderer.java 61484 2009-06-29 19:01:16Z swgithen@mtu.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 The Sakai Foundation.
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

package org.sakaiproject.assignment2.tool.producers.renderers;

import java.util.Map;

import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.model.Assignment2ContentReviewInfo;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.DisplayUtil;
import org.sakaiproject.contentreview.model.ContentReviewItem;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;


/**
 * 
 * Renders the review status indicator for items run through a content review service
 *
 */
public class ReviewStatusRenderer {
    
    private ExternalContentReviewLogic contentReviewLogic;
    private MessageLocator messageLocator;

    public void makeReviewStatusIndicator(UIContainer tofill, String divID, Map properties){
       
        UIJointContainer joint = new UIJointContainer(tofill, divID, "review_report_info:");

        if (properties != null && !properties.isEmpty()) {
            
            // we may need to display plagiarism checking results
            if (properties.containsKey(AssignmentConstants.PROP_REVIEW_INFO)) {
                Assignment2ContentReviewInfo reviewInfo = 
                    (Assignment2ContentReviewInfo) properties.get(AssignmentConstants.PROP_REVIEW_INFO);
                
                UIOutput.make(joint, "review_report_info");
                
                Long status = reviewInfo.getContentReviewItem().getStatus();
                
                if (ContentReviewItem.SUBMITTED_REPORT_AVAILABLE_CODE.equals(status)) {
                    // create the container
                    UIOutput.make(joint, "review_report_status");
                    
                    Integer scoreAsNum = reviewInfo.getContentReviewItem().getReviewScore();
                    String statusCssClass = DisplayUtil.getCssClassForReviewScore(scoreAsNum);
                    
                    String scoreAsString = reviewInfo.getScoreDisplay();
                    
                    // create the link
                    UILink reportLink = UILink.make(joint, "review_report_link", scoreAsString, reviewInfo.getReviewUrl());
                    DecoratorList reportLinkDecorators = new DecoratorList();
                    reportLinkDecorators.add(new UITooltipDecorator(messageLocator.getMessage("assignment2.content_review.report_link")));
                    reportLinkDecorators.add(new UIFreeAttributeDecorator("class", statusCssClass));
                    reportLink.decorators = reportLinkDecorators;
                    
                } 
                else if (ContentReviewItem.SUBMITTED_AWAITING_REPORT_CODE.equals(status)
                        || ContentReviewItem.NOT_SUBMITTED_CODE.equals(status)) {
                    UIOutput pendingDisplay = UIOutput.make(joint, "review_pending", messageLocator.getMessage("assignment2.content_review.pending.display"));
                    DecoratorList pendingDeco = new DecoratorList();
                    pendingDeco.add(new UITooltipDecorator(messageLocator.getMessage("assignment2.content_review.pending.info")));
                    pendingDisplay.decorators = pendingDeco;
                }
                else { 
                    String errorText = contentReviewLogic.getErrorMessage(
                    		reviewInfo.getContentReviewItem().getStatus(),
                    		reviewInfo.getContentReviewItem().getErrorCode());
                    UIOutput errorDisplay = UIOutput.make(joint, "review_error");
                    DecoratorList errorDisplayDecorators = new DecoratorList();
                    errorDisplayDecorators.add(new UITooltipDecorator(errorText));
                    errorDisplay.decorators = errorDisplayDecorators;
                }
            }
        }
    }
    

    
    public void setExternalContentReviewLogic(ExternalContentReviewLogic contentReviewLogic) {
        this.contentReviewLogic = contentReviewLogic;
    }
    
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }
}