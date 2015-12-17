package org.sakaiproject.assignment2.tool.entity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.azeckoski.reflectutils.DeepUtils;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.logic.AssignmentBundleLogic;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.AttachmentInformation;
import org.sakaiproject.assignment2.logic.ExternalContentLogic;
import org.sakaiproject.assignment2.logic.ExternalContentReviewLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.ExternalTaggableLogic;
import org.sakaiproject.assignment2.logic.GradebookItem;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.AssignmentAttachment;
import org.sakaiproject.assignment2.model.AssignmentGroup;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.DisplayUtil;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.EntityView;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RESTful;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RequestAware;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RequestStorable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Statisticable;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.extension.RequestGetter;
import org.sakaiproject.entitybroker.entityprovider.extension.RequestStorage;
import org.sakaiproject.entitybroker.entityprovider.search.Search;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.annotations.EntityCustomAction;
import org.sakaiproject.site.api.Group;

import sun.util.logging.resources.logging;

import org.springframework.context.MessageSource;


/**
 * Entity Provider for Assn2 assignments.
 * 
 * @author sgithens
 *
 */
public class Assignment2EntityProvider extends AbstractEntityProvider implements
CoreEntityProvider, RESTful, RequestStorable, RequestAware, Statisticable {
    private static Log log = LogFactory.getLog(Assignment2EntityProvider.class);

    // Dependency
    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }

    // Dependency
    private AssignmentPermissionLogic permissionLogic;
    public void setPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    // Dependency
    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    
    // Dependency
    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }
    
    // Dependency
    private ExternalContentLogic contentLogic;
    public void setExternalContentLogic(ExternalContentLogic contentLogic) {
    	this.contentLogic = contentLogic;
    }

    // Dependency
    private DisplayUtil displayUtil;
    public void setDisplayUtil(DisplayUtil displayUtil) {
        this.displayUtil = displayUtil;
    }
    
    // Dependency
    private ExternalTaggableLogic taggableLogic;
    public void setExternalTaggableLogic(ExternalTaggableLogic taggableLogic) {
    	this.taggableLogic = taggableLogic;
    }

    private RequestStorage requestStorage;
    public void setRequestStorage(RequestStorage requestStorage) {
        this.requestStorage = requestStorage;
    }

    private RequestGetter requestGetter;
    public void setRequestGetter(RequestGetter requestGetter) {
        this.requestGetter = requestGetter;
    }

    private AssignmentBundleLogic assignmentBundleLogic;
    public void setAssignmentBundleLogic(AssignmentBundleLogic assignmentBundleLogic) {
        this.assignmentBundleLogic = assignmentBundleLogic;
    }
    
    private ExternalContentReviewLogic contentReviewLogic;
    public void setExternalContentReviewLogic(ExternalContentReviewLogic contentReviewLogic) {
        this.contentReviewLogic = contentReviewLogic;
    }
    
    
    /**
     * This is necessary because the Statisticable interface gives us a Locale
     * to use so we shouldn't just use the default.  MessageLocator doesn't
     * include any methods that take a locale.
     */
    private MessageSource messageSource;
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    
    
    public static String PREFIX = "assignment2";
    public String getEntityPrefix() {
        return PREFIX;
    }

    public boolean checkCsrf(String csrf) {
	Object sessionToken = org.sakaiproject.tool.cover.SessionManager.getCurrentSession().getAttribute("sakai.csrf.token");
        if (sessionToken != null && sessionToken.toString().equals(csrf)) {
            return true;
        }
	else
	    return false;
    }

    /**
     * TODO: Change this so it's not a GET
     * 
     * @param view
     */
    @EntityCustomAction(action="reorder", viewKey=EntityView.VIEW_LIST)
    public void reorderAssignments(EntityView view) {
        String context = (String) requestStorage.getStoredValue("siteid");
        String order = (String) requestStorage.getStoredValue("order");

        String[] stringAssignIds = order.split(",");
        try {
            // convert the strings to longs
            List<Long> longAssignmentIds = new ArrayList<Long>();
            for (int i=0; i < stringAssignIds.length; i++){
                String idAsString = stringAssignIds[i];
                if (idAsString != null && idAsString.trim().length() > 0) { 
                    longAssignmentIds.add(Long.valueOf(stringAssignIds[i]));
                }
            }
            assignmentLogic.reorderAssignments(longAssignmentIds, context);

            if (log.isDebugEnabled()) log.debug("Assignments reordered via Entity Feed");
        } catch (NumberFormatException nfe) {
            log.error("Non-numeric value passed to ReorderAssignmentsCommand. No reordering was saved.");
        }
    }

    /**
     * This is a custom action for retrieving the Assignment Data we need to 
     * render the list of assignments for landing pages. Currently this does
     * require a 'context' or 'siteid', but we should move towards this not
     * requiring that so it can be used for newer age 3akai things.
     * 
     * It's likely this will just be moved to the getEntities method after 
     * prototyping.
     * 
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    @EntityCustomAction(action="sitelist", viewKey=EntityView.VIEW_LIST)
    public List getAssignmentListForSite(EntityView view) {        
        String context = (String) requestStorage.getStoredValue("siteid");
        // if true, will return the attachment type for attachments associated with each assignment
        String attachmentTypeString = (String) requestStorage.getStoredValue("attachmenttype");
        boolean includeAttachmentType = attachmentTypeString != null && "true".equalsIgnoreCase(attachmentTypeString);

        DateFormat df = externalLogic.getDateFormat(null, null, assignmentBundleLogic.getLocale(), true);

        if (context == null) {
            return new ArrayList();
        }

        List<Assignment2> viewable = assignmentLogic.getViewableAssignments(context);
        
        // let's grab all of the gradebook items to see if we need to flag any
        // graded assignments b/c their associated gb item was deleted
        // first, we need to check authz or the gradebook will throw a security exception when
        // we try to retrieve the gb info
        boolean userMayViewGbItems = gradebookLogic.isCurrentUserAbleToEdit(context) || gradebookLogic.isCurrentUserAbleToGrade(context);
        Map<Long, GradebookItem> gbItemIdToItemMap = new HashMap<Long, GradebookItem>();
        if (userMayViewGbItems) {
            List<GradebookItem> existingGbItems = gradebookLogic.getAllGradebookItems(context, false);
            if (existingGbItems != null) {
                for (GradebookItem gbItem : existingGbItems) {
                    gbItemIdToItemMap.put(gbItem.getGradebookItemId(), gbItem);
                }
            }
        }

        List togo = new ArrayList();

        Map<Assignment2, List<String>> assignmentViewableStudentsMap = 
            permissionLogic.getViewableStudentsForAssignments(externalLogic.getCurrentUserId(), context, viewable);

        Collection<Group> groups = externalLogic.getSiteGroups(context);
        Map<String,Group> groupmap = new HashMap<String,Group>();

        for (Group group: groups) {
            groupmap.put(group.getId(), group);
        }
        
        boolean contentReviewAvailable = contentReviewLogic.isContentReviewAvailable(context);
        boolean siteAssociated = taggableLogic.isSiteAssociated(context);
        
        // retrieve the edit, grade, add, and delete permissions for each assignment. 
        // The add perm will determine if user can duplicate.
        List<String> permissions = new ArrayList<String>();
        permissions.add(AssignmentConstants.PERMISSION_EDIT_ASSIGNMENTS);
        permissions.add(AssignmentConstants.PERMISSION_MANAGE_SUBMISSIONS);
        permissions.add(AssignmentConstants.PERMISSION_REMOVE_ASSIGNMENTS);
        permissions.add(AssignmentConstants.PERMISSION_ADD_ASSIGNMENTS);
        
        Map<Long, Map<String, Boolean>> assignPermMap = permissionLogic.getPermissionsForAssignments(viewable, permissions);
        
        // TODO - use the service for entity work
       filterRestrictedAssignmentInfo(viewable, context);

        for (Assignment2 asnn: viewable) {
            Map asnnmap = new HashMap();
            asnnmap.put("ref", asnn.getReference());
            asnnmap.put("id", asnn.getId());
            asnnmap.put("title", asnn.getTitle());
            asnnmap.put("openDate", asnn.getOpenDate());
            if (asnn.getOpenDate() != null) {
                asnnmap.put("openDateFormatted", df.format(asnn.getOpenDate()));
            }
            asnnmap.put("dueDate", asnn.getDueDate());
            if (asnn.getDueDate() != null) {
                asnnmap.put("dueDateFormatted", df.format(asnn.getDueDate()));
            }
            asnnmap.put("graded", asnn.isGraded());
            asnnmap.put("sortIndex", asnn.getSortIndex());
            asnnmap.put("requiresSubmission", asnn.isRequiresSubmission());
            asnnmap.put("draft", asnn.isDraft());
            
            // add gradebook info, if applicable
            if (asnn.isGraded() && asnn.getGradebookItemId() != null) {
                GradebookItem gbItem = gbItemIdToItemMap.get(asnn.getGradebookItemId());
                if (gbItem != null) {
                    asnnmap.put("gradebookItemName", gbItem.getTitle());
                    asnnmap.put("gradebookItemPointsPossible", gbItem.getPointsPossible());
                    asnnmap.put("gradebookItemId", asnn.getGradebookItemId());
                }
            }

            // Permissions for this particular assignment
            boolean canEdit= false;
            boolean canGrade= false;
            boolean canDelete= false;
            boolean canAdd = false;
            
            Map<String, Boolean> permMap = assignPermMap.get(asnn.getId());
            if (permMap != null) {
                if (permMap.containsKey(AssignmentConstants.PERMISSION_EDIT_ASSIGNMENTS)) {
                    canEdit = permMap.get(AssignmentConstants.PERMISSION_EDIT_ASSIGNMENTS);
                }
                if (permMap.containsKey(AssignmentConstants.PERMISSION_MANAGE_SUBMISSIONS)) {
                    canGrade = permMap.get(AssignmentConstants.PERMISSION_MANAGE_SUBMISSIONS);
                }
                if (permMap.containsKey(AssignmentConstants.PERMISSION_REMOVE_ASSIGNMENTS)) {
                    canDelete = permMap.get(AssignmentConstants.PERMISSION_REMOVE_ASSIGNMENTS);
                }
                if (permMap.containsKey(AssignmentConstants.PERMISSION_ADD_ASSIGNMENTS)) {
                    canAdd = permMap.get(AssignmentConstants.PERMISSION_ADD_ASSIGNMENTS);
                }
                
            }
            asnnmap.put("canEdit", canEdit);
            asnnmap.put("canDelete", canDelete);
            asnnmap.put("canGrade", canGrade);
            asnnmap.put("canAdd", canAdd);
            
            // In case assignment has a gradebook item, but that gradebook item
            // no longer exists. don't include this flag unless the user may edit the assignment
            if (canEdit && userMayViewGbItems && asnn.isGraded() && (asnn.getGradebookItemId() == null || 
                    !gbItemIdToItemMap.containsKey(asnn.getGradebookItemId()))) {
                asnnmap.put("gbItemMissing", true);
            }

            // Create/Edit Matrix Links
            asnnmap.put("canMatrixLink", siteAssociated && canEdit && taggableLogic.isProducerEnabled());

            List<String> viewableStudents = assignmentViewableStudentsMap.get(asnn);
            
            // The in/new column needs to display something different if the user can't grade this one
            String inAndNewText;
            if (canGrade) {
                Map<String, String> subStatusMap = displayUtil.getSubmissionStatusForAssignment(asnn, viewableStudents);
                inAndNewText = subStatusMap.get(DisplayUtil.IN_NEW_DISPLAY);
                String numSubmissions = subStatusMap.get(DisplayUtil.NUM_SUB);
                asnnmap.put("numSubmissions", numSubmissions);
            } else {
                inAndNewText = assignmentBundleLogic.getString("assignment2.list.in_new.no_grade_perm");
            }

            asnnmap.put("inAndNew", inAndNewText);
            asnnmap.put("reviewEnabled", canEdit && contentReviewAvailable && asnn.isContentReviewEnabled());

            List groupstogo = new ArrayList();
            // we need to double check that all of the associated groups still exist.
            // if they don't, we will display an indicator that this assignment needs attention
            for (AssignmentGroup group: asnn.getAssignmentGroupSet()) {
                if (groupmap.containsKey(group.getGroupId())) {
                    Map groupprops = new HashMap();
                    groupprops.put("groupId", group.getGroupId());
                    groupprops.put("id", group.getId());

                    Group g = groupmap.get(group.getGroupId());
                    groupprops.put("title",g.getTitle());
                    groupprops.put("description", g.getDescription());
                    groupstogo.add(groupprops);
                } else {
                    // group was probably deleted, so signal a problem to user if they 
                    // can edit this assignment
                    if (canEdit) {
                        asnnmap.put("groupMissing", true);
                    }
                }
            }
            asnnmap.put("groups", groupstogo);

            List attachstogo = new ArrayList();
            for (AssignmentAttachment attach: asnn.getAttachmentSet()) {
            	String attachmentType = null;
            	
                Map attachprops = new HashMap();
                attachprops.put("id", attach.getId());
                attachprops.put("attachmentReference", attach.getAttachmentReference());
                
                // add the mime type for the attachment, if the parameter was set
                if (includeAttachmentType) {
            		AttachmentInformation attachInfo = contentLogic.getAttachmentInformation(attach.getAttachmentReference());
            		if (attachInfo != null) {
            			attachprops.put("attachmentType", attachInfo.getContentType());
            		}
            	}
                
                attachstogo.add(attachprops);
            }
            asnnmap.put("attachments", attachstogo);

            togo.add(asnnmap);
        }

        // IE Won't stop caching even with the no-cache.
        HttpServletResponse httpServletResponse = requestGetter.getResponse();
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("Cache-Control", "max-age=0,no-cache,no-store,must-revalidate,private,post-check=0,pre-check=0,s-max-age=0");
        httpServletResponse.setDateHeader("Expires", 0 );

        boolean canDelete = permissionLogic.isUserAllowedToDeleteAssignments(null, context);
        httpServletResponse.setHeader("x-asnn2-canDelete", canDelete+"");
        
        boolean canManageSubmissions = permissionLogic.isUserAllowedToManageSubmissions(null, context);
        httpServletResponse.setHeader("x-asnn2-canManageSubmissions", canManageSubmissions+"");

        return togo;
    }

    public boolean entityExists(String id) {
        boolean exists;
        try {
            assignmentLogic.getAssignmentById(new Long(id));
            exists = true;
        }
        catch (AssignmentNotFoundException anfe) {
            exists = false;
        }
        return exists;
    }

    public String createEntity(EntityReference ref, Object entity,
            Map<String, Object> params) {
        Assignment2 assignment = (Assignment2) entity;
        assignmentLogic.saveAssignment(assignment);
        return assignment.getId().toString();
    }

    public Object getSampleEntity() {
        return new Assignment2();
    }

    public void updateEntity(EntityReference ref, Object entity,
            Map<String, Object> params) {

	if (!checkCsrf((String)params.get("csrftoken")))
	    return;

        Assignment2 assignment = (Assignment2) entity;

        Assignment2 tosave = assignmentLogic.getAssignmentByIdWithAssociatedData(assignment.getId());

        /*
         * This is going to be obtuse.  Because Hibernate Model objects are so
         * different that the models we really want available to RESTful feeds,
         * and because of the wierd cascade populating, we're going to have to 
         * have a custom list of things that can be updated by the regular 
         * REST PUT update operation. The problem right now is that we can't use
         * the assignment object passed in to the method, because we deepCloned
         * it without stuff that couldn't be serialized in getEntity.
         * 
         * This is not a huge deal necessarily, but something we have to
         * remember about for now, until we make new model objects for REST
         * or start using some other metaprogramming paradigm.
         * 
         */
        tosave.setTitle(assignment.getTitle());

        assignmentLogic.saveAssignment(tosave);
    }

    public Object getEntity(EntityReference ref) {
        Assignment2 asnn = assignmentLogic.getAssignmentByIdWithAssociatedData(new Long(ref.getId()));
        
        // TODO use the service methods
        List<Assignment2> assignList = new ArrayList<Assignment2>();
        assignList.add(asnn);
        
        if (asnn.isGraded() && asnn.getGradebookItemId() != null && gradebookLogic.gradebookItemExists(asnn.getGradebookItemId())) {
            GradebookItem gradebookItem = gradebookLogic.getGradebookItemById(asnn.getContextId(), asnn.getGradebookItemId());
            asnn.setGradebookItemName(gradebookItem.getTitle());

            if (!gradebookItem.isUngraded()) {
                asnn.setGradebookPointsPossible(gradebookItem.getPointsPossible().toString());
            }
        }
        
        filterRestrictedAssignmentInfo(assignList, asnn.getContextId());

        DeepUtils deep = DeepUtils.getInstance();

        return deep.deepClone(asnn, 3, new String[] {"submissionsSet",
                "ListOfAssociatedGroupReferences","assignmentGroupSet",
                "attachmentSet", "modelAnswerAttachmentSet", 
                "assignmentAttachmentRefs"});
    }

    public void deleteEntity(EntityReference ref, Map<String, Object> params) {
	if (!checkCsrf((String)params.get("csrftoken")))
	    return;

        Assignment2 asnn = assignmentLogic.getAssignmentById(new Long(ref.getId()));
        assignmentLogic.deleteAssignment(asnn);
    }

    public List<?> getEntities(EntityReference ref, Search search) {
        // TODO Auto-generated method stub
        return null;
    }

    public String[] getHandledOutputFormats() {
        return new String[] {Formats.XML, Formats.JSON, Formats.HTML };
    }

    public String[] getHandledInputFormats() {
        return new String[] {Formats.XML, Formats.JSON, Formats.HTML };
    }
    
    /**
     * Until we have time to rework the EntityProviders to utilize the service AssignmentDefinition,
     * we will manually filter the assignments here
     * @param assignList
     */
    private void filterRestrictedAssignmentInfo(List<Assignment2> assignList, String context) {
        if (assignList != null) {
            List<String> permsToCheck = new ArrayList<String>();
            permsToCheck.add(AssignmentConstants.PERMISSION_EDIT_ASSIGNMENTS);
            permsToCheck.add(AssignmentConstants.PERMISSION_REMOVE_ASSIGNMENTS);
            permsToCheck.add(AssignmentConstants.PERMISSION_MANAGE_SUBMISSIONS);
            Map<Long, Map<String, Boolean>> assignPerms = permissionLogic.getPermissionsForAssignments(assignList, permsToCheck);
            // non-instructors cannot view the accept until date or properties
            for (Assignment2 assign : assignList) {
                Map<String, Boolean> userPerms = assignPerms.get(assign.getId());
                if (userPerms != null) {
                    boolean canEdit = userPerms.get(AssignmentConstants.PERMISSION_EDIT_ASSIGNMENTS);
                    boolean canDelete = userPerms.get(AssignmentConstants.PERMISSION_REMOVE_ASSIGNMENTS);
                    boolean canManage = userPerms.get(AssignmentConstants.PERMISSION_MANAGE_SUBMISSIONS);

                    if (!canEdit && !canDelete && !canManage) {
                    	removeNonInstProperties(assign);
                    }
                } else {
                	removeNonInstProperties(assign);
                }
            }
        }
    }
    
    private void removeNonInstProperties(Assignment2 assign) {
    	assign.setProperties(null);
        assign.setAcceptUntilDate(null);
        assign.setModelAnswerAttachmentRefs(new String[] {});
        assign.setModelAnswerDisplayRule(0);
        assign.setModelAnswerEnabled(false);
        assign.setModelAnswerText("");
    }

    public String getAssociatedToolId() {
        return "sakai.assignment2";
    }

    public String[] getEventKeys() {
        return AssignmentConstants.getEventCodes();
    }

    public Map<String, String> getEventNames(Locale locale) {
        Map<String,String> eventNames = new HashMap<String,String>();
        
        for (String eventCode: AssignmentConstants.getEventCodes()) {
            eventNames.put(eventCode, messageSource.getMessage(eventCode, new Object[] {}, locale));
        }
        
        return eventNames;
    }

    @SuppressWarnings("unchecked")
    @EntityCustomAction(action="getMessageBundleText", viewKey=EntityView.VIEW_LIST)
    public String getMessageBundleText(EntityView view, Map<String, Object> params) {

        String key = null;
        String message = "";
        List <String> messageParams = new ArrayList<String>();
        final String argumentBase = "mbarg";
        int argumentindex = 0;
        String tempArgumentName = null;
        String tempArgumentValue = null;
        
        if (params != null && params.containsKey("key") && (key = (String) params.get("key")) != null) {
            key = key.trim();

            while (params.containsKey(tempArgumentName = argumentBase + argumentindex)) {
                if ((tempArgumentValue = (String) params.get(tempArgumentName)) != null) {
                    messageParams.add(tempArgumentValue);
                    
                    argumentindex++;
                }
            }
            
            if (key.startsWith("assignment2")) {
                try {
                    message = assignmentBundleLogic.getFormattedMessage(key, messageParams.toArray());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
        
        
        return message;
    }

    @SuppressWarnings("unchecked")
    @EntityCustomAction(action="isLinkedAssignmentNameInGradebook", viewKey=EntityView.VIEW_SHOW)
    public String isLinkedAssignmentNameInGradebook(EntityView view, Map<String, Object> params) {
        String togo = String.valueOf(Boolean.FALSE);

        Assignment2 as2 = assignmentLogic.getAssignmentById(Long.parseLong(view.getEntityReference().getId()));
        
        if (as2 == null || ! as2.isGraded() || params == null || ! params.containsKey("title"))
        {
            return String.valueOf(Boolean.FALSE);
        }

        String title = (String) params.get("title");
        String existingGbTitle = null;
        
        try {
            existingGbTitle = gradebookLogic.getGradebookItemById(as2.getContextId(), as2.getGradebookItemId()).getTitle();
        } catch (GradebookItemNotFoundException ginfe) {
            if (log.isDebugEnabled()) log.debug("No gb item exists with id " + as2.getGradebookItemId() + " checking isLinkedAssignmentNameInGradebook");
        }

        // Make sure the title to change is different than the existing title
        if (title != null && !title.equals(existingGbTitle)) {
            togo = String.valueOf(gradebookLogic.isAssignmentNameDefinedinGradebook(as2.getContextId(), title));
        }

        return togo;
    }

    @SuppressWarnings("unchecked")
    @EntityCustomAction(action="getFreeAssignmentNameinGradebook", viewKey=EntityView.VIEW_SHOW)
    public String getFreeAssignmentNameInGradebook(EntityView view, Map<String, Object> params) {
        String togo = null;
        
        Assignment2 as2 = assignmentLogic.getAssignmentById(Long.parseLong(view.getEntityReference().getId()));
        String title = (String) params.get("title");

        if (title != null && as2 != null) {
            togo = String.valueOf(gradebookLogic.getFreeAssignmentName(as2.getContextId(), title));
        }

        return togo;
    }

    @SuppressWarnings("unchecked")
    @EntityCustomAction(action="getAssignmentPointsinGradebook", viewKey=EntityView.VIEW_LIST)
    public String getAssignmentPointsInGradebook(EntityView view, Map<String, Object> params) {
        String contextId = null;
        String gradebookItemId = null;
        GradebookItem gradebookItem;
        
        String message = "";
        
        if (params != null && 
            params.containsKey("contextId") && params.containsKey("gradebookItemId") &&
            ((contextId = (String) params.get("contextId")) != null) &&
            ((gradebookItemId = (String) params.get("gradebookItemId")) != null)) {

            contextId = contextId.trim();
            gradebookItemId = gradebookItemId.trim();
            
            gradebookItem = gradebookLogic.getGradebookItemById(contextId, Long.valueOf(gradebookItemId));
            
            if (! gradebookItem.isUngraded()) {
                message = gradebookItem.getPointsPossible().toString();
            }
        }

        return message;
    }
    
    @SuppressWarnings("unchecked")
    @EntityCustomAction(action="isPointsGradable", viewKey=EntityView.VIEW_LIST)
    public String isPointsGradable(EntityView view, Map<String, Object> params) {
        String contextId = null;
        String gradebookItemId = null;
        int gradeentrytype = 0;
        GradebookItem gradebookItem;

        String togo = String.valueOf(Boolean.FALSE);
        
        if (params != null && 
            params.containsKey("contextId") && params.containsKey("gradebookItemId") &&
            ((contextId = (String) params.get("contextId")) != null) &&
            ((gradebookItemId = (String) params.get("gradebookItemId")) != null)) {
            
            contextId = contextId.trim();
            
            gradeentrytype = gradebookLogic.getGradebookGradeEntryType(contextId);
            
            gradebookItemId = gradebookItemId.trim();
            
            gradebookItem = gradebookLogic.getGradebookItemById(contextId, Long.valueOf(gradebookItemId));
 
            if (gradeentrytype == gradebookLogic.ENTRY_BY_LETTER) {
            // placeholder in case we want to add something here
            } 
            else if (gradeentrytype == gradebookLogic.ENTRY_BY_PERCENT ||
                     gradeentrytype == gradebookLogic.ENTRY_BY_POINTS) {
                     
                     if (! gradebookItem.isUngraded()) {
                         togo = String.valueOf(Boolean.TRUE);
                     }
            }
        }

        return togo;
    }
    
    /**
     * Custom action to allow deleting multiple assignments
     * @param view
     */
    @EntityCustomAction(action = "deleteAssignments", viewKey = EntityView.VIEW_NEW)
    public void deleteAssignments(EntityView view) {
	if (!checkCsrf((String) requestStorage.getStoredValue("csrftoken")))
	    return;

        String assignIds = (String) requestStorage.getStoredValue("delete-ids");
        if (assignIds != null && !"".equals(assignIds)) {
            assignIds = assignIds.trim();
            if (!"".equals(assignIds)) {
                List<String> assignIdList = Arrays.asList(assignIds.split(","));
                for (String assignId : assignIdList) {
                    Assignment2 asnn = assignmentLogic.getAssignmentById(new Long(assignId));
                    assignmentLogic.deleteAssignment(asnn);
                }
            } else {
                throw new IllegalArgumentException("No ids to delete (string had only whitespace).");
            }
        } else {
            throw new IllegalArgumentException("No ids to delete (blank or null).");
        }
    }

}
