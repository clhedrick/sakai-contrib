package org.sakaiproject.assignment2.tool.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a Flow Scope bean to be used with the Assignment Authoring Flow.
 * It goes along with AssignmentAuthoringFlowBean, which holds the actual
 * Assignment2 object binding.  These are extra settings on the form for 
 * checkboxes and whatnot that need to be preserved through the flow and used in
 * the AssignmentAuthoringBean to save changes and what.
 * 
 * As is usual with Flow Scope beans, this needs to have ZERO request scope 
 * dependencies.
 * 
 * @author sgithens
 *
 */
public class AssignmentAuthoringOptionsFlowBean {
    private Boolean requireDueDate;
    private Boolean requireAcceptUntil;
    private Long currentAssignmentId;
    private Date openDate;
    private String otpkey;
    private Map<String, Boolean> selectedIds = new HashMap<String, Boolean>();
    private String restrictedToGroups;

    public Boolean getRequireDueDate() {
        return requireDueDate;
    }

    public void setRequireDueDate(Boolean requireDueDate) {
        this.requireDueDate = requireDueDate;
    }

    public Boolean getRequireAcceptUntil() {
        return requireAcceptUntil;
    }

    public void setRequireAcceptUntil(Boolean requireAcceptUntil) {
        this.requireAcceptUntil = requireAcceptUntil;
    }

    public Long getCurrentAssignmentId() {
        return currentAssignmentId;
    }

    public void setCurrentAssignmentId(Long currentAssignmentId) {
        this.currentAssignmentId = currentAssignmentId;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getOtpkey() {
        return otpkey;
    }

    public void setOtpkey(String otpkey) {
        this.otpkey = otpkey;
    }

    public Map<String, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<String, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public String getRestrictedToGroups() {
        return restrictedToGroups;
    }

    public void setRestrictedToGroups(String restrictedToGroups) {
        this.restrictedToGroups = restrictedToGroups;
    }

}
