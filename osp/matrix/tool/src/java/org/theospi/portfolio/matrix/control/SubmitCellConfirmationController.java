
/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/osp/branches/sakai-10.x/matrix/tool/src/java/org/theospi/portfolio/matrix/control/SubmitCellConfirmationController.java $
* $Id: SubmitCellConfirmationController.java 105079 2012-02-24 23:08:11Z ottenhoff@longsight.com $
***********************************************************************************
*
 * Copyright (c) 2005, 2006, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*
**********************************************************************************/
package org.theospi.portfolio.matrix.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import org.sakaiproject.metaobj.shared.mgt.IdManager;
import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.metaobj.utils.mvc.intf.CustomCommandController;
import org.sakaiproject.metaobj.utils.mvc.intf.LoadObjectController;
import org.sakaiproject.metaobj.utils.mvc.intf.FormController;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.theospi.portfolio.matrix.MatrixFunctionConstants;
import org.theospi.portfolio.matrix.MatrixManager;
import org.theospi.portfolio.matrix.WizardPageHelper;
import org.theospi.portfolio.matrix.model.Cell;
import org.theospi.portfolio.matrix.model.WizardPage;
import org.theospi.portfolio.wizard.WizardFunctionConstants;
import org.theospi.portfolio.wizard.mgt.WizardManager;
import org.theospi.portfolio.wizard.model.WizardPageSequence;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.metaobj.shared.model.IdImpl;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.coursemanagement.api.Section;
import org.sakaiproject.coursemanagement.api.CourseManagementService;
import org.sakaiproject.component.cover.ComponentManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.sakaiproject.db.cover.SqlService;
import org.springframework.web.servlet.view.RedirectView;


/**
 * @author chmaurer
 */
public class SubmitCellConfirmationController implements LoadObjectController, CustomCommandController {

	IdManager idManager = null;
	MatrixManager matrixManager = null;
	WizardManager wizardManager = null;
        CourseManagementService cms = (CourseManagementService) ComponentManager
	    .get(CourseManagementService.class);

	/* (non-Javadoc)
	 * @see org.theospi.utils.mvc.intf.CustomCommandController#formBackingObject(java.util.Map, java.util.Map, java.util.Map)
	 */
	public Object formBackingObject(Map request, Map session, Map application) {
		return new HashMap();
	}

	/* (non-Javadoc)
	 * @see org.theospi.utils.mvc.intf.LoadObjectController#fillBackingObject(java.lang.Object, java.util.Map, java.util.Map, java.util.Map)
	 */
	public Object fillBackingObject(Object incomingModel, Map request, Map session, Map application) throws Exception {
		return incomingModel;
	}

    public class GroupEntry {
	String id;
	String section;
	String instructor;
	String description;
        String groupid;
	String grouptitle;

	public String getId() {
	    return this.id;
	}
	public String getDescription() {
	    return this.description;
	}
    }

    public void setGroups(Map request, Map model) {
	String sql = "select section,instructor,groupid from rutgers_osp_evaluators where pagedef = ?";
	
	String userId = SessionManager.getCurrentSessionUserId();
        String page_id = (String) request.get("page_id");
	WizardPage page = matrixManager.getWizardPage(new IdImpl(page_id,null));
	String pageDefId = page.getPageDefinition().getId().getValue();
	String siteid = page.getPageDefinition().getSiteId();
	Site site = null;
	try {
	    site = SiteService.getSite(siteid);
	} catch (Exception ignore) {
	    System.out.println("Can't find site " + siteid);
	    return;
	};
	// can't find site; should be impossible

	// look up all courses associated with this page. Currently we use the course directly
	// rather than the group

	List<GroupEntry> entries = new ArrayList<GroupEntry>();

	Connection conn = null;
	try {
	    conn = SqlService.borrowConnection();

	    Object fields[] = new Object[1];
	    fields[0] = pageDefId;

	    entries = SqlService.dbRead(conn, sql, fields, new SqlReader()
		{
		    public Object readSqlResultRecord(ResultSet result)
		    {
			try {
			    GroupEntry entry = new GroupEntry();
			    entry.section = result.getString(1);
			    entry.instructor = result.getString(2);
			    entry.groupid = result.getString(3);
			    return entry;
			} catch (Exception ignore) {};
			return null;
		    }
		});

	} catch (Exception e) {
	    // null return is normal, but this is not
	    System.out.println("SubmitCellConfirmationController lookup of page failed " + e);
	} finally {
	    try {
		if (conn != null)
		    SqlService.returnConnection(conn);
	    } catch (Exception ignore) {};
	}

	// if no entries associated with page, it's not under roster control
	if (entries.size() == 0) {
	    model.put("numgroups", -1);
	    return;
	}
	
	// we have to filter the list of sections that use this page
	// only allow those that the user is in

	String userid = SessionManager.getCurrentSessionUserId();
	String eid = null;
	try {
	    eid = UserDirectoryService.getUserEid(userid);
	} catch (Exception ignore) {
	    System.out.println("SubmitCellConfirmationController can't find userid " + userid);
	    return;
	};

	// sectionEids sholud be normalized set of all sections the user is in

	Set<Section> sections = cms.findEnrolledSections(eid);
	Set<String> sectionEids = new HashSet<String>();
	for (Section s: sections) {
	    String e = s.getEid();
	    while (e.endsWith(" "))
		e = e.substring(0, e.length()-1);
	    if (e.endsWith(":"))
		e = e.substring(0, e.length()-1);
	    sectionEids.add(e);
	}

	// groupIds should be all groups the user is in

	Collection<Group> groups = site.getGroupsWithMember(userid);
	Set<String> groupIds = new HashSet<String>();	
	for (Group g: groups)
	    groupIds.add(g.getId());

	// now filter the page entries
	// make two passes, for section numbers and groups.
	// use a hash table based on id because we can have more than
	// one entry for the same section if there's more then one
	// instructor
	Map<String,GroupEntry> filtered = new HashMap<String,GroupEntry>();

	// pass one, sections
	for (GroupEntry entry: entries) {
	    if (entry.section == null)
		continue;
	    String id = entry.section;
	    // normalize id
	    while (id.endsWith(" "))
		id = id.substring(0, id.length()-1);
	    if (id.endsWith(":"))
		id = id.substring(0, id.length()-1);

	    // may be more than one, merge the instructor names
	    if (sectionEids.contains(id)) {
		String instructor = null;
		try {
		    User u = UserDirectoryService.getUser(entry.instructor);
		    instructor = entry.groupid + " " + u.getDisplayName();
		} catch (Exception ignore) {
		    instructor = entry.groupid + " " + entry.instructor;
		}

		String entryId = "/section/" + id;
		GroupEntry e = filtered.get(entryId);
		if (e == null) {
		    entry.id = entryId;
		    entry.description = id + " " + instructor;
		    filtered.put(entryId, entry);
		} else {
		    e.description = e.description + ", " + instructor;
		}
	    }
	}

	// pass two, groups
	for (GroupEntry entry: entries) {
	    String groupid = entry.groupid;
	    if (groupid == null)
		continue;

	    if (!groupIds.contains(groupid))
		continue;

	    String entryId = "/group/" + groupid;
	    entry.id = entryId;
	    Group group = site.getGroup(groupid);
	    if (group == null)
		entry.description = groupid;
	    else
		entry.description = group.getTitle();

	    filtered.put(entryId, entry);
	}

	model.put("numgroups", filtered.size());
	if (filtered.size() == 1)
	    model.put("firstgroup", filtered.keySet().iterator().next());

        model.put("groups", filtered.values());

    }
	
    public void recordSubmission(String page, String pagedef, String site, String user, String id) {
	System.out.println("recordsub " + page + " pagedef " + pagedef + " site " + site + " user " + user + " id " + id);
	String sql_group = "insert into rutgers_osp_evaluations(page, pagedef, site, user, requesttime, groupid, grouptitle) values(?,?,?,?, current_timestamp,?,?)";
	String sql_section = "insert into rutgers_osp_evaluations(page, pagedef, site, user, requesttime, section) values(?,?,?,?,current_timestamp,?)";
	String groupid = null;
	String grouptitle = null;
	String section = null;
	if (id==null) {
	    return; // user should not use this functionality if there is no group, so do nothing (hedrick)
	}
	if (id == null) {
	    // not under roster control, nothing to do
	    return;
	}
	if (id.startsWith("/group/")) {
	    groupid = id.substring(7);
	    Site siteobj = null;
	    try {
		siteobj = SiteService.getSite(site);
	    } catch (Exception ignore) {
		System.out.println("SubmitCellConfirmationController can't find site " + site);
		return;
	    };
	    Group group = siteobj.getGroup(groupid);
	    if (group == null)
		return;
	    grouptitle = group.getTitle();
	} else if (id.startsWith("/section/")) {
	    section = id.substring(9);
	} else {
	    System.out.println("SubmitCellConfirmationController: illegal id from user: " + id);
	    return;
	}

	Connection conn = null;
	boolean wasCommit = false;
	PreparedStatement pst = null;
	try {
	    conn = SqlService.borrowConnection();

	    System.out.println("add " + page + " " + pagedef + " " + site + " " + user + " " + groupid + " " + grouptitle + " " + section);
	    wasCommit = conn.getAutoCommit();
	    conn.setAutoCommit(false);
	    if (section == null)
		pst = conn.prepareStatement(sql_group);
	    else
		pst = conn.prepareStatement(sql_section);

	    pst.setString(1, page);
	    pst.setString(2, pagedef);
	    pst.setString(3, site);
	    pst.setString(4, user);
	    if (section == null) {
		pst.setString(5, groupid);
		pst.setString(6, grouptitle);
	    } else {
		pst.setString(5, section);
	    }

	    int i = pst.executeUpdate();
	    conn.commit();
	    System.out.println("add ok");
	} catch (Exception e) {
	    System.out.println("SubmitCellConfirmationController: insert failed: " + e);
	} finally {
	    if (pst != null) {
		try {
		    pst.close();
		} catch (Exception ignore) {};
	    }
	    if (conn != null) {
		try {
		    conn.setAutoCommit(wasCommit);
		} catch (Exception ignore) {};
		try {
		    SqlService.returnConnection(conn);
		} catch (Exception ignore) {};
	    }
	}
    }

	/* (non-Javadoc)
	 * @see org.theospi.utils.mvc.intf.Controller#handleRequest(java.lang.Object, java.util.Map, java.util.Map, java.util.Map, org.springframework.validation.Errors)
	 */
	public ModelAndView handleRequest(Object requestModel, Map request, Map session, Map application, Errors errors) {
		boolean isCellPage = false;
		String view = "continueSeq";

		WizardPage page = (WizardPage) session.get(WizardPageHelper.WIZARD_PAGE);

		Cell cell = null;
		if (page == null) {
			Id pageId = idManager.getId((String) request.get("page_id"));
			cell = getMatrixManager().getCellFromPage(pageId);
			page = cell.getWizardPage();
			isCellPage = true;
		} else {
			WizardPageSequence seq = wizardManager.getWizardPageSeqByDef(page.getPageDefinition().getId());

			if(seq.getCategory().getWizard().getType().equals(WizardFunctionConstants.WIZARD_TYPE_HIERARCHICAL))
				view = "continueHier";
		}
		String submitAction = (String)request.get("submit");
		String cancelAction = (String)request.get("cancel");
		if (submitAction != null) {
		    // record page, pagedefinition, site, user, group
		    recordSubmission(page.getId().getValue(), page.getPageDefinition().getId().getValue(), 
				     page.getPageDefinition().getSiteId(), SessionManager.getCurrentSessionUserId(),
				     (String)request.get("selgroup"));

			Id reviewObjectId = null;
			String parentTitle = "";
			if(isCellPage){
				if(cell.getScaffoldingCell().isDefaultEvaluators()){
					reviewObjectId = cell.getScaffoldingCell().getScaffolding().getId();
				}else{
					reviewObjectId = cell.getScaffoldingCell().getWizardPageDefinition().getId();
				}
				parentTitle = cell.getScaffoldingCell().getScaffolding().getTitle();
			}else{
				
				parentTitle = wizardManager.getWizardPageSeqByDef(page.getPageDefinition().getId()).getCategory().getWizard().getName();
				reviewObjectId = page.getPageDefinition().getId();
			}
			getMatrixManager().notifyAudience(page, reviewObjectId, true, null, null, parentTitle, MatrixFunctionConstants.EVALUATE_MATRIX);

			if (!isCellPage) {
				getMatrixManager().submitPageForEvaluation(page);
				session.put("altDoneURL", "submitWizardPage");
				session.put("submittedPage", page);

				if (isLast(session)) {
					view = "done";
				}

				return new ModelAndView(view, "page_id", page.getId().getValue());
			}
			else {
				getMatrixManager().submitCellForEvaluation(cell);
			}


			return new ModelAndView(view, "page_id", page.getId().getValue());
		}
		if (cancelAction != null) {
			// the current page is set to the next page after the submitted page for confirmation.
			//    So the current step needs to be rolled back
			Object stepObj = (Object) session.get(WizardPageHelper.SEQUENTIAL_WIZARD_CURRENT_STEP);
			if (stepObj != null && stepObj instanceof Integer && !isLast(session)) {
				session.put(WizardPageHelper.SEQUENTIAL_WIZARD_CURRENT_STEP, Integer.valueOf(((Integer)stepObj).intValue() - 1) );
			}
			return new ModelAndView(view, "page_id", page.getId().getValue());
		}
		Map model = new HashMap();
		model.put("page", page);
		setGroups(request, model);
		return new ModelAndView("success", model);
		//      return new ModelAndView("success", "page", page);
	}

	protected boolean isLast(Map session) {
		return session.get(WizardPageHelper.IS_LAST_STEP) != null;
	}


	/**
	 * @return Returns the idManager.
	 */
	public IdManager getIdManager() {
		return idManager;
	}
	/**
	 * @param idManager The idManager to set.
	 */
	public void setIdManager(IdManager idManager) {
		this.idManager = idManager;
	}
	/**
	 * @return Returns the matrixManager.
	 */
	public MatrixManager getMatrixManager() {
		return matrixManager;
	}
	/**
	 * @param matrixManager The matrixManager to set.
	 */
	public void setMatrixManager(MatrixManager matrixManager) {
		this.matrixManager = matrixManager;
	}

	public WizardManager getWizardManager() {
		return wizardManager;
	}

	public void setWizardManager(WizardManager wizardManager) {
		this.wizardManager = wizardManager;
	}
}
