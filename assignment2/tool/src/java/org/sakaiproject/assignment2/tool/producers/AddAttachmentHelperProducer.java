/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/producers/AddAttachmentHelperProducer.java $
 * $Id: AddAttachmentHelperProducer.java 87902 2015-12-14 18:01:30Z hedrick@rutgers.edu $
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

package org.sakaiproject.assignment2.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.assignment2.logic.ExternalContentLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.tool.params.FilePickerHelperViewParams;
import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

import org.sakaiproject.rsf.helper.HelperViewParameters;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * This producer provides the view for attaching materials to Assignments,
 * Submissions etc.
 * 
 * It's one of these producer helpers that uses an Old School Sakai helper which
 * is why it might look a bit odd. The tool state get based back and forth
 * between the Resources based File Picker Helper.
 * 
 * Also of importance is to notice that this is going to FinishedHelperProducer
 * when finished, which is where the files seem to be processed.  I wonder why
 * that doesn't happen in the FilePickerBean. It might have to do with 
 * interaction with the Thickbox dialog, and the way that the attachments are
 * stored before clicking save. TODO
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class AddAttachmentHelperProducer implements ViewComponentProducer, ViewParamsReporter, NavigationCaseReporter
{
    public static final String VIEWID = "AddAttachment";

    public String getViewID() {
        return VIEWID;
    }

    private SessionManager sessionManager;
    private MessageLocator messageLocator;
    private ExternalLogic externalLogic;
    private ExternalContentLogic contentLogic;

    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
        FilePickerHelperViewParams params = (FilePickerHelperViewParams) viewparams;


        //parameters for helper
        ToolSession toolSession = sessionManager.getCurrentToolSession();
        toolSession.setAttribute(FilePickerHelper.FILE_PICKER_TITLE_TEXT, messageLocator.getMessage("assignment2.add_attachment_helper.title"));
        toolSession.setAttribute(FilePickerHelper.FILE_PICKER_INSTRUCTION_TEXT, messageLocator.getMessage("assignment2.add_attachment_helper.instructions"));
        toolSession.setAttribute(FilePickerHelper.FILE_PICKER_MAX_ATTACHMENTS, FilePickerHelper.CARDINALITY_MULTIPLE);
        // display the "My Workspace" resources instead of the current site's resources
        if (params.showWorkspace != null && params.showWorkspace) {
            String currUserId = externalLogic.getCurrentUserId();
            String myWorkspaceCollectionId = contentLogic.getMyWorkspaceCollectionId(currUserId);
            toolSession.setAttribute(FilePickerHelper.DEFAULT_COLLECTION_ID, myWorkspaceCollectionId);
        }

        UIOutput.make(tofill, HelperViewParameters.HELPER_ID, "sakai.filepicker");
        UICommand goattach = UICommand.make(tofill, HelperViewParameters.POST_HELPER_BINDING, "#{FilePickerBean.process}");
        goattach.parameters = new ParameterList();
        goattach.parameters.add(new UIELBinding("FilePickerBean.otpkey", params.otpkey));
    }

    public ViewParameters getViewParameters() {
        return new FilePickerHelperViewParams();
    }


    public List reportNavigationCases() {
        List l = new ArrayList();
        l.add(new NavigationCase("processed", new SimpleViewParameters(FinishedHelperProducer.VIEWID)));
        return l;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setMessageLocator(MessageLocator messageLocator)
    {
        this.messageLocator = messageLocator;
    }

    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public void setExternalContentLogic(ExternalContentLogic contentLogic) {
        this.contentLogic = contentLogic;
    }
}