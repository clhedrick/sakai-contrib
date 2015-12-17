/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/fragments/FragmentAssignment2SelectProducer.java $
 * $Id: FragmentAssignment2SelectProducer.java 79823 2012-06-01 19:25:12Z wagnermr@iupui.edu $
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

package org.sakaiproject.assignment2.tool.producers.fragments;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradebookItem;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.content.ContentTypeReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;


/**
 * This appears to be rendering the select box on the Assignment authoring page
 * for hooking an assignment to a gradebook item.  However, putting a debug 
 * point in it and breaking, the page still seems to render. So will need to 
 * still determine exactly what this is doing on the page. TODO FIXME
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class FragmentAssignment2SelectProducer implements ViewComponentProducer, ContentTypeReporter{

    public static final String VIEW_ID = "fragment-assignment2-select";
    public String getViewID() {
        return VIEW_ID;
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    private MessageLocator messageLocator;
    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    private ExternalGradebookLogic externalGradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic externalGradebookLogic){
        this.externalGradebookLogic = externalGradebookLogic;
    }

    private Locale locale;
    public void setLocale(Locale locale){
        this.locale = locale;
    }


    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {

        UIForm form = UIForm.make(tofill, "form");

        // use a date which is related to the current users locale
        DateFormat df = externalLogic.getDateFormat(null, null, locale, false);

        //Get Gradebook Items
        List<GradebookItem> gradebook_items = externalGradebookLogic.getAllGradebookItems(externalLogic.getCurrentContextId(), false);


        String[] gradebook_item_labels = new String[gradebook_items.size()+1];
        String[] gradebook_item_values = new String[gradebook_items.size()+1];
        gradebook_item_values[0] = "0";
        gradebook_item_labels[0] = messageLocator.getMessage("assignment2.assignment_add.gradebook_item_select");
        String js_gradebook_items_data = "var gradebook_items_date = {\n";
        js_gradebook_items_data += "0: \"" + messageLocator.getMessage("assignment2.assignment_add.gradebook_item_not_selected") + "\"";
        for (int i=1; i <= gradebook_items.size(); i++) {
            //Fill out select options
            gradebook_item_labels[i] = gradebook_items.get(i-1).getTitle();
            gradebook_item_values[i] = gradebook_items.get(i-1).getGradebookItemId().toString();

            //store js hash of id => due_date string
            js_gradebook_items_data += "," + gradebook_items.get(i-1).getGradebookItemId().toString();
            if(gradebook_items.get(i-1).getDueDate() != null){
                js_gradebook_items_data += ":\"" + df.format(gradebook_items.get(i-1).getDueDate()) + "\"";
            }else{
                js_gradebook_items_data += ":\"" + messageLocator.getMessage("assignment2.assignment_add.gradebook_item_no_due_date") + "\"";
            }
        }
        js_gradebook_items_data += "}";
        UISelect.make(form, "assignments",gradebook_item_values, gradebook_item_labels, null);

        //Output the JS vars
        UIVerbatim.make(form, "gradebook_items_data", js_gradebook_items_data);
    }

    public String getContentType() {
        return ContentTypeInfoRegistry.HTML_FRAGMENT;
    }
}