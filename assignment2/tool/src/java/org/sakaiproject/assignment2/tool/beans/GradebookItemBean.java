/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/beans/GradebookItemBean.java $
 * $Id: GradebookItemBean.java 61484 2009-06-29 19:01:16Z swgithen@mtu.edu $
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

package org.sakaiproject.assignment2.tool.beans;

import java.util.Map;

import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.model.Assignment2;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;

public class GradebookItemBean {

    private static final String CANCEL = "cancel";
    private static final String ADD_ITEM = "add_item";


    private TargettedMessageList messages;
    public void setMessages(TargettedMessageList messages) {
        this.messages = messages;
    }

    private Map<String, Assignment2> OTPMap;
    @SuppressWarnings("unchecked")
    public void setEntityBeanLocator(EntityBeanLocator entityBeanLocator) {
        this.OTPMap = entityBeanLocator.getDeliveredBeans();
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    /**	
	private ExternalLogic externalGradebookLogic;
	public void setExternalGradebookLogic(ExternalGradebookLogic externalGradebookLogic) {
		this.externalGradebookLogic = externalGradebookLogic;
	}
     **/
    private MessageLocator messageLocator;
    public void setMessageLocator (MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    public String processActionAddItem(){

        return ADD_ITEM;
    }

    public String processActionCancel(){

        return CANCEL;
    }
}