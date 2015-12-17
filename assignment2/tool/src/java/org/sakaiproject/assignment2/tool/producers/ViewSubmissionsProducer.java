/**********************************************************************************
 * $URL$
 * $Id$
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.AssignmentSubmissionLogic;
import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradeInformation;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentGroup;
import org.sakaiproject.assignment2.model.AssignmentSubmission;
import org.sakaiproject.assignment2.model.AssignmentSubmissionVersion;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.params.AssignmentViewParams;
import org.sakaiproject.assignment2.tool.params.ViewSubmissionsViewParams;
import org.sakaiproject.assignment2.tool.params.ZipViewParams;
import org.sakaiproject.assignment2.tool.producers.renderers.AttachmentListRenderer;
import org.sakaiproject.assignment2.tool.entity.Assignment2SubmissionEntityProvider;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

import uk.org.ponder.htmlutil.HTMLUtil;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * This producer renders the page that has the table of all the students
 * with links to their submissions and information on the current grade, 
 * feedback released, etc.
 * 
 * @author rjlowe
 * @author wagnermr
 * @author sgithens
 *
 */
public class ViewSubmissionsProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter, ActionResultInterceptor {

    private static Log log = LogFactory.getLog(ViewSubmissionsProducer.class);

    public static final String VIEW_ID = "viewSubmissions";
    public String getViewID() {
        return VIEW_ID;
    }

    //sorting strings
    public static final String DEFAULT_SORT_DIR = AssignmentLogic.SORT_DIR_ASC;
    public static final String DEFAULT_OPPOSITE_SORT_DIR = AssignmentLogic.SORT_DIR_DESC;
    public static final String DEFAULT_SORT_BY = AssignmentSubmissionLogic.SORT_BY_NAME;

    //images
    public static final String BULLET_UP_IMG_SRC = "/sakai-assignment2-tool/content/images/bullet_arrow_up.png";
    public static final String BULLET_DOWN_IMG_SRC = "/sakai-assignment2-tool/content/images/bullet_arrow_down.png";

    private MessageLocator messageLocator;
    private AssignmentLogic assignmentLogic;
    private AssignmentSubmissionLogic submissionLogic;
    private TargettedMessageList messages;
    private ExternalLogic externalLogic;
    private Locale locale;
    private AssignmentPermissionLogic permissionLogic;
    private ExternalGradebookLogic gradebookLogic;
    private ExternalContentReviewLogic contentReviewLogic;
    private Placement placement;
    private SessionManager sessionManager;

    private Long assignmentId;

    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
        ViewSubmissionsViewParams params = (ViewSubmissionsViewParams) viewparams;
        //make sure that we have an AssignmentID to work with
        if (params.assignmentId == null){
            //ERROR SHOULD BE SET, OTHERWISE TAKE BACK TO ASSIGNMENT_LIST
            messages.addMessage(new TargettedMessage("GeneralActionError"));
            return;
        }
        assignmentId = params.assignmentId;
        Assignment2 assignment = assignmentLogic.getAssignmentByIdWithAssociatedData(assignmentId);
        
        String currUserId = externalLogic.getCurrentUserId();
        
        boolean contentReviewEnabled = assignment.isContentReviewEnabled() && contentReviewLogic.isContentReviewAvailable(assignment.getContextId());

        // let's double check that none of the associated groups were deleted from the site
        boolean displayGroupDeletionWarning = false;
        if (assignment.getAssignmentGroupSet() != null && !assignment.getAssignmentGroupSet().isEmpty()) {
            Collection<Group> siteGroups = externalLogic.getSiteGroups(assignment.getContextId());
            List<String> groupIds = new ArrayList<String>();
            if (siteGroups != null) {
                for (Group group : siteGroups) {
                    groupIds.add(group.getId());
                }
            }

            for (AssignmentGroup assignGroup : assignment.getAssignmentGroupSet()) {
                if (!groupIds.contains(assignGroup.getGroupId())) {
                    displayGroupDeletionWarning = true;
                    break;
                }
            }
        }

        if (displayGroupDeletionWarning) {
            UIOutput.make(tofill, "deleted_group", messageLocator.getMessage("assignment2.assignment_grade-assignment.group_deleted"));
        }

        //Edit Permission
        boolean userMayEditAssign = permissionLogic.isUserAllowedToEditAssignment(currUserId, assignment);
        boolean userMayManageSubmissions = permissionLogic.isUserAllowedToManageSubmissionsForAssignment(currUserId, assignment);

        //get parameters
        if (params.sort_by == null) params.sort_by = DEFAULT_SORT_BY;
        if (params.sort_dir == null) params.sort_dir = DEFAULT_SORT_DIR;

        UIVerbatim.make(tofill, "defaultSortBy", HTMLUtil.emitJavascriptVar("defaultSortBy", DEFAULT_SORT_BY));

        // we need to retrieve the history for the release/retract feedback logic
        List<AssignmentSubmission> submissions = submissionLogic.getViewableSubmissionsWithHistoryForAssignmentId(assignmentId, params.groupId);
        List<String> studentIdList = new ArrayList<String>();
        if (submissions != null) {
            for (AssignmentSubmission submission : submissions) {
                studentIdList.add(submission.getUserId());
            }
        }
       
        // The following is some code to populate the sort order/page size, if
        // it's already been put in session state by the entity provider.
        Long pagesize = null;
        String orderBy = null;
        Boolean ascending = null;
        ToolSession toolSession = sessionManager.getCurrentToolSession();
        if (toolSession.getAttribute(Assignment2SubmissionEntityProvider.SUBMISSIONVIEW_SESSION_ATTR) != null) {
            Map attr = (Map) toolSession.getAttribute(Assignment2SubmissionEntityProvider.SUBMISSIONVIEW_SESSION_ATTR);
            if (attr.containsKey(Assignment2SubmissionEntityProvider.SUBMISSIONVIEW_SESSION_ATTR_PAGE_SIZE)) {
                pagesize = (Long) attr.get(Assignment2SubmissionEntityProvider.SUBMISSIONVIEW_SESSION_ATTR_PAGE_SIZE);
            }
            if (attr.containsKey(Assignment2SubmissionEntityProvider.SUBMISSIONVIEW_SESSION_ATTR_ORDER_BY)) {
                orderBy = (String) attr.get(Assignment2SubmissionEntityProvider.SUBMISSIONVIEW_SESSION_ATTR_ORDER_BY);
            }
            if (attr.containsKey(Assignment2SubmissionEntityProvider.SUBMISSIONVIEW_SESSION_ATTR_ASCENDING)) {
                ascending = (Boolean) attr.get(Assignment2SubmissionEntityProvider.SUBMISSIONVIEW_SESSION_ATTR_ASCENDING);
            }
        }

        // if assign is graded, retrieve the gb details, if appropriate
        GradebookItem gbItem = null;
        boolean gbItemExists = false;
        boolean gradesReleased = false;
        // user may view the associated gradebook item
        boolean userMayViewGbItem = false;
        // user has grading privileges for this gb item
        boolean userMayGrade = false;
        boolean userMayReleaseGrades = false;
        
        if (assignment.isGraded() && assignment.getGradebookItemId() != null) {
            userMayViewGbItem = gradebookLogic.isCurrentUserAbleToViewGradebookItem(assignment.getContextId(), assignment.getGradebookItemId());

            if (userMayViewGbItem) {
                // user may grade if there is at least one gradable student among the submissions
                List<String> gradableStudents = gradebookLogic.getFilteredStudentsForGradebookItem(currUserId, assignment.getContextId(), assignment.getGradebookItemId(), AssignmentConstants.GRADE, studentIdList);
                userMayGrade = gradableStudents != null && !gradableStudents.isEmpty();
                userMayReleaseGrades = gradebookLogic.isCurrentUserAbleToEdit(assignment.getContextId());

                try {
                    gbItem = gradebookLogic.getGradebookItemById(assignment.getContextId(), assignment.getGradebookItemId());
                    gbItemExists = true;
                    gradesReleased = gbItem.isReleased();
                } catch (GradebookItemNotFoundException ginfe) {
                    if (log.isDebugEnabled()) log.debug("Gb item with id: " + assignment.getGradebookItemId() + " no longer exists!");
                    gbItem = null;
                }
            }
        }

        // if user has grading privileges but item no longer exists, display warning
        // to user
        if (assignment.isGraded() && userMayViewGbItem && !gbItemExists) {
            UIOutput.make(tofill, "no_gb_item", messageLocator.getMessage("assignment2.assignment_grade-assignment.gb_item_deleted"));
        }
        
        // We need to check if it's a non electronic submission.  If it is, we don't want to have 
        // the submitted columns appear (Submitted and Submission Status).
        // We pass in the boolean parameter nonElectronicSubmission to viewSubmission.js (specifically snn2subview.init()), 
        // where logic is there to use this parameter.
        boolean nonElectronicSubmission = false;
        
        if (assignment.getSubmissionType() == AssignmentConstants.SUBMIT_NON_ELECTRONIC) {
                nonElectronicSubmission = true;
        }
        	
        
        UIInitBlock.make(tofill, "asnn2subview-init", "asnn2subview.init", 
                new Object[]{assignmentId, externalLogic.getCurrentContextId(), 
                placement.getId(), submissions.size(), assignment.isGraded(), contentReviewEnabled, nonElectronicSubmission, pagesize, orderBy, ascending, gradesReleased, params.pageIndex});

        //Breadcrumbs
        UIInternalLink.make(tofill, "breadcrumb", 
                messageLocator.getMessage("assignment2.assignment_list-sortview.heading"),
                new SimpleViewParameters(ListProducer.VIEW_ID));
        UIMessage.make(tofill, "last_breadcrumb", "assignment2.assignment_grade-assignment.heading", new Object[] { assignment.getTitle() });

        // ACTION BAR
        boolean displayReleaseGrades = false;
        boolean displayReleaseFB = false;
        boolean displayDownloadAll = false;
        boolean displayUploadAll = false;

        if (userMayEditAssign || userMayManageSubmissions) {
            UIOutput.make(tofill, "navIntraTool");
        }

        // RELEASE GRADES
        // don't display this option if the gb item doesn't exist anymore
        if (userMayReleaseGrades && assignment.isGraded() && gbItemExists){
            displayReleaseGrades = true;

            // determine if grades have been released yet
            String releaseLinkText = messageLocator.getMessage("assignment2.assignment_grade-assignment.grades.release");
            if (gradesReleased) {
                releaseLinkText = messageLocator.getMessage("assignment2.assignment_grade-assignment.grades.retract");
            }

            UIForm releaseGradesForm = UIForm.make(tofill, "release_grades_form");
            UICommand releaseGradesButton = UICommand.make(releaseGradesForm, "release_grades");

            UIOutput.make(tofill, "release_grades_li");
            UIInternalLink releaseGradesLink = UIInternalLink.make(tofill, 
                    "release_grades_link", releaseLinkText, viewparams);
            Map<String,String> idmap = new HashMap<String,String>();
            idmap.put("onclick", "asnn2.releaseGradesDialog('"+releaseGradesButton.getFullID()+"', '" + assignment.getContextId() + "', '" + assignment.getGradebookItemId() + "', '" + !gradesReleased + "'); return false;");
            releaseGradesLink.decorate(new UIFreeAttributeDecorator(idmap));

            makeReleaseGradesDialog(gradesReleased, assignment, tofill);
        }

        // RELEASE FEEDBACK
        if (userMayManageSubmissions) {
            displayReleaseFB = true;
            makeReleaseFeedbackLink(tofill, params, submissions);
        }

        // DOWNLOAD ALL
        if (userMayManageSubmissions) {
            displayDownloadAll = true;

            ZipViewParams zvp = new ZipViewParams("zipSubmissions", assignmentId);
            UIInternalLink.make(tofill, "downloadall",
                    UIMessage.make("assignment2.assignment_grade-assignment.downloadall.button"), zvp);
        }

        // UPLOAD GRADES & FEEDBACK
        if (userMayManageSubmissions) {
            displayUploadAll = true;

            AssignmentViewParams avp = new AssignmentViewParams("uploadall", assignmentId);
            if (assignment.isGraded() && gbItemExists && userMayGrade) {
                UIInternalLink.make(tofill, "uploadall",
                        UIMessage.make("assignment2.uploadall.breadcrumb.upload.graded"), avp);
            } else {
                UIInternalLink.make(tofill, "uploadall",
                        UIMessage.make("assignment2.uploadall.breadcrumb.upload.ungraded"), avp);
            }
        }

        // handle those pesky separators
        if (displayReleaseGrades && (displayReleaseFB || displayUploadAll || displayDownloadAll)) {
            UIOutput.make(tofill, "release_grades_sep");
        }

        if (displayReleaseFB && (displayUploadAll || displayDownloadAll)) {
            UIOutput.make(tofill, "release_feedback_sep");
        }

        if (displayDownloadAll && displayUploadAll) {
            UIOutput.make(tofill, "downloadall_sep");
        }

        UIMessage.make(tofill, "page-title", "assignment2.assignment_grade-assignment.title");

        // now make the "View By Sections/Groups" filter
        makeViewByGroupFilter(tofill, params, assignment);

        /*
         * Form for assigning a grade to all submissions without a grade.
         * Do not allow grading if gbItem is null - it must have been deleted
         */
        if (submissions != null && !submissions.isEmpty() && 
                userMayGrade && assignment.isGraded() && gbItemExists) {
            createApplyToUngradedWidget(assignment, tofill, params, "unassigned-apply-form0:");
            createApplyToUngradedWidget(assignment, tofill, params, "unassigned-apply-form1:");
        }

        // Confirmation Dialogs
        // These are only added here for internationalization. They are not part
        // of a real form.
        UICommand.make(tofill, "release-feedback-confirm", UIMessage.make("assignment2.dialogs.release_all_feedback.confirm"));
        UICommand.make(tofill, "release-feedback-cancel", UIMessage.make("assignment2.dialogs.release_all_feedback.cancel"));

        UICommand.make(tofill, "retract-feedback-confirm", UIMessage.make("assignment2.dialogs.retract_all_feedback.confirm"));
        UICommand.make(tofill, "retract-feedback-cancel", UIMessage.make("assignment2.dialogs.retract_all_feedback.cancel"));
    }

    private void makeReleaseFeedbackLink(UIContainer tofill, ViewSubmissionsViewParams viewparams, List<AssignmentSubmission> submissionsWithHistory) {
        // check to see if there is anything to release yet
        boolean feedbackExists = false;
        // determine if we are releasing or retracting
        boolean release = false;

        if (submissionsWithHistory != null) {
            for (AssignmentSubmission submission : submissionsWithHistory) {
                if (submission.getSubmissionHistorySet() != null) {
                    for (AssignmentSubmissionVersion version : submission.getSubmissionHistorySet()) {
                        // only look at versions that have had feedback activity
                        if (version.getLastFeedbackDate() != null) {
                            feedbackExists = true;
                            // if there is at least one version with unreleased feedback,
                            // we will show the "Release" link
                            if (!version.isFeedbackReleased()) {
                                release = true;
                            }
                        }
                    }
                }
            }
        }

        if (feedbackExists) {
            String releaseLinkText;
            if (release) {
                releaseLinkText = messageLocator.getMessage("assignment2.assignment_grade-assignment.feedback.release");
            } else {
                releaseLinkText = messageLocator.getMessage("assignment2.assignment_grade-assignment.feedback.retract");
            }
            UIForm releaseFeedbackForm = UIForm.make(tofill, "release-feedback-form");
            releaseFeedbackForm.parameters.add(new UIELBinding("#{ReleaseFeedbackAction.assignmentId}", assignmentId));
            releaseFeedbackForm.parameters.add(new UIELBinding("#{ReleaseFeedbackAction.releaseFeedback}", release));
            UICommand submitAllFeedbackButton = UICommand.make(releaseFeedbackForm, "release_feedback", releaseLinkText,
            "#{ReleaseFeedbackAction.execute}");

            UIInternalLink releaseFeedbackLink = UIInternalLink.make(tofill, 
                    "release-feedback-link", releaseLinkText, viewparams);
            Map<String,String> idmap = new HashMap<String,String>();
            //  idmap.put("onclick", "document.getElementById('"+submitAllFeedbackButton.getFullID()+"').click(); return false;");
            idmap.put("onclick", "asnn2.releaseFeedbackDialog('"+submitAllFeedbackButton.getFullID()+"', " + release + "); return false;");
            releaseFeedbackLink.decorate(new UIFreeAttributeDecorator(idmap));
        } else {
            // show a disabled link if no feedback to release or retract
            UIOutput.make(tofill, "release_feedback_disabled", messageLocator.getMessage("assignment2.assignment_grade-assignment.feedback.release"));
        }
    }

    private void makeViewByGroupFilter(UIContainer tofill, ViewSubmissionsViewParams params, Assignment2 assignment) {
        List<Group> viewableGroups = permissionLogic.getViewableGroupsForAssignment(null, assignment);
        if (viewableGroups != null && !viewableGroups.isEmpty()) {
            UIForm groupFilterForm = UIForm.make(tofill, "group_filter_form", params);

            // we need to order the groups alphabetically. Group names are unique
            // per site, so let's make a map
            Map<String, String> groupNameToIdMap = new HashMap<String, String>();
            for (Group group : viewableGroups) { 
                groupNameToIdMap.put(group.getTitle(), group.getId());
            }

            List<String> orderedGroupNames = new ArrayList<String>(groupNameToIdMap.keySet());
            Collections.sort(orderedGroupNames, new Comparator<String>() {
                public int compare(String groupName1, String groupName2) {
                    return groupName1.compareToIgnoreCase(groupName2);
                }
            });

            String selectedValue = "";
            if (params.groupId != null && params.groupId.trim().length() > 0) {
                selectedValue = params.groupId;
            }

            int numItemsInDropDown = viewableGroups.size();
            boolean showAllOption = false;

            // if the assignment is restricted to one group, we won't add the "Show All" option
            if (assignment.getAssignmentGroupSet() == null || assignment.getAssignmentGroupSet().size() != 1) {
                // if there is more than one viewable group or the user has grade all perm, add the 
                // "All Sections/Groups option"
                if (viewableGroups.size() > 1 || 
                        permissionLogic.isUserAllowedToManageAllSubmissions(null, assignment.getContextId())) {
                    showAllOption = true;
                    numItemsInDropDown++;
                }
            }

            // Group Ids
            String[] view_filter_values = new String[numItemsInDropDown]; 
            // Group Names
            String[] view_filter_options = new String[numItemsInDropDown];

            int index = 0;

            // the first entry is "All Sections/Groups"
            if (showAllOption) {  
                view_filter_values[index] = "";
                view_filter_options[index] = messageLocator.getMessage("assignment2.assignment_grade.filter.all_sections");
                index++;
            }

            for (String groupName : orderedGroupNames) { 
                view_filter_values[index] = groupNameToIdMap.get(groupName);
                view_filter_options[index] = groupName;
                index++;
            }

            UISelect.make(groupFilterForm, "group_filter", view_filter_values,
                    view_filter_options, "groupId", selectedValue);
        }
    }

    private void makeReleaseGradesDialog(boolean gradesReleased, Assignment2 assignment, UIContainer tofill) {
        // Release Grades Dialog 
        String releaseGradesTitle;
        String releaseGradesMessage;
        String releaseGradesConfirm;
        String releaseGradesCancel;

        if (gradesReleased) {
            // if the grades are already released, the option is "retract"
            releaseGradesTitle = messageLocator.getMessage("assignment2.dialogs.retract_grades.title");
            releaseGradesConfirm = messageLocator.getMessage("assignment2.dialogs.retract_grades.confirm");
            releaseGradesCancel = messageLocator.getMessage("assignment2.dialogs.retract_grades.cancel");
            if (assignment.getAssignmentGroupSet() == null || assignment.getAssignmentGroupSet().isEmpty()) {
                // this assignment is not restricted to groups
                releaseGradesMessage = messageLocator.getMessage("assignment2.dialogs.retract_grades.nogroups.message");
            } else {
                // this has groups, so we display a different warning and
                // require a confirmation checkbox to be clicked
                releaseGradesMessage = messageLocator.getMessage("assignment2.dialogs.retract_grades.groups.message");
                UIOutput.make(tofill, "confirm-checkbox-label", messageLocator.getMessage("assignment2.dialogs.retract_grades.groups.confirmcheckbox"));
                UIOutput.make(tofill, "confirm-checkbox-area");
            }
        } else {
            // the user has the option to release
            releaseGradesTitle = messageLocator.getMessage("assignment2.dialogs.release_grades.title");
            releaseGradesConfirm = messageLocator.getMessage("assignment2.dialogs.release_grades.confirm");
            releaseGradesCancel = messageLocator.getMessage("assignment2.dialogs.release_grades.cancel");
            if (assignment.getAssignmentGroupSet() == null || assignment.getAssignmentGroupSet().isEmpty()) {
                // this assignment is not restricted to groups
                releaseGradesMessage = messageLocator.getMessage("assignment2.dialogs.release_grades.nogroups.message");
            } else {
                // this has groups, so we display a different warning and
                // require a confirmation checkbox to be clicked
                releaseGradesMessage = messageLocator.getMessage("assignment2.dialogs.release_grades.groups.message");
                UIOutput.make(tofill, "confirm-checkbox-label", messageLocator.getMessage("assignment2.dialogs.release_grades.groups.confirmcheckbox"));
                UIOutput.make(tofill, "confirm-checkbox-area");
            }

            // add the checkbox for also including in course grade
            UIOutput.make(tofill, "release-grades-counted");
        }

        UIOutput.make(tofill, "release-grades-title", releaseGradesTitle);
        UIOutput.make(tofill, "release-grades-message", releaseGradesMessage);
        UIOutput.make(tofill, "release-grades-confirm", releaseGradesConfirm);
        UIOutput.make(tofill, "release-grades-cancel", releaseGradesCancel);
    }
    
    private void createApplyToUngradedWidget(Assignment2 assignment, UIContainer tofill, ViewSubmissionsViewParams params, String containerId) {
        String lowestPossibleGrade = gradebookLogic.getLowestPossibleGradeForGradebookItem(assignment.getContextId(), assignment.getGradebookItemId());
        UIForm unassignedForm = UIForm.make(tofill, containerId);
        unassignedForm.addParameter(new UIELBinding("GradeAllRemainingAction.assignmentId", assignment.getId()));
        unassignedForm.addParameter(new UIELBinding("GradeAllRemainingAction.groupIdFilter", params.groupId));
        UIInput.make(unassignedForm, "new-grade-value", "GradeAllRemainingAction.grade", lowestPossibleGrade);
        UICommand.make(unassignedForm, "apply-button", messageLocator.getMessage("assignment2.assignment_grade.assigntoall"), "GradeAllRemainingAction.execute");
    }

    public List<NavigationCase> reportNavigationCases() {
        List<NavigationCase> nav= new ArrayList<NavigationCase>();
        nav.add(new NavigationCase("release_all", new ViewSubmissionsViewParams(
                ViewSubmissionsProducer.VIEW_ID, null)));
        return nav;
    }

    public void interceptActionResult(ARIResult result, ViewParameters incoming, Object actionReturn) {
        if (result.resultingView instanceof ViewSubmissionsViewParams) {
            ViewSubmissionsViewParams outgoing = (ViewSubmissionsViewParams) result.resultingView;
            ViewSubmissionsViewParams in = (ViewSubmissionsViewParams) incoming;
            outgoing.assignmentId = in.assignmentId;
            outgoing.groupId = in.groupId;
        }
    }

    public ViewParameters getViewParameters(){
        return new ViewSubmissionsViewParams();
    }

    public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }

    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    public void setAssignmentSubmissionLogic(AssignmentSubmissionLogic submissionLogic) {
        this.submissionLogic = submissionLogic;
    }

    public void setMessages(TargettedMessageList messages) {
        this.messages = messages;
    }

    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }
    
    public void setExternalContentReviewLogic(ExternalContentReviewLogic contentReviewLogic) {
        this.contentReviewLogic = contentReviewLogic;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }
    
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
}