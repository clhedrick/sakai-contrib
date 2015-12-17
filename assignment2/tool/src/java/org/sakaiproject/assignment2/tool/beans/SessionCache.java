package org.sakaiproject.assignment2.tool.beans;

import java.util.List;

import org.sakaiproject.assignment2.tool.entity.Assignment2SubmissionEntityProvider;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;

/**
 * This will cache information for various things during a users session that
 * we don't want in session storage, and don't have a database table for.
 * 
 * @author sgithens
 *
 */
public class SessionCache {

    private MemoryService memoryService;
    public void setMemoryService(MemoryService memoryService) {
        this.memoryService = memoryService;
    }
    
    private Assignment2SubmissionEntityProvider assignment2SubmissionEntityProvider;
    public void setAssignment2SubmissionEntityProvider(
            Assignment2SubmissionEntityProvider assignment2SubmissionEntityProvider) {
        this.assignment2SubmissionEntityProvider = assignment2SubmissionEntityProvider;
    }
    
    private Cache sortedStudentIdsCache = null;
    private Cache asnn2AttachmentCache = null;
    
    private static final String ASSIGN_ATTACH_PREFIX = "asnn2AttachRefs";
    
    public List<String> getSortedStudentIds(String curUserID, long asnnId, String placementId) {
        String key = curUserID+asnnId+placementId;
        List<String> studentIds = null;
        if (sortedStudentIdsCache.containsKey(key)) {
            studentIds = (List<String>) sortedStudentIdsCache.get(key);
        }
        if (studentIds == null) {
            assignment2SubmissionEntityProvider.getEntitiesWithStoredSessionState(asnnId, placementId);
            studentIds = (List<String>) sortedStudentIdsCache.get(key);
        }
        return studentIds;
    }
    
    public void setSortedStudentIds(String curUserID, long asnnId, List<String> studentIds, String placementId) {
        sortedStudentIdsCache.put(curUserID+asnnId+placementId, studentIds);
    }
    
    public List<String> getAsnn2AttachRefs(String curUserID) {
        String key = getAsnn2AttachRefsKey(curUserID);
        List<String> assignAttachRefs = null;
        if (asnn2AttachmentCache.containsKey(key)) {
            assignAttachRefs = (List<String>) asnn2AttachmentCache.get(key);
        }
        
        return assignAttachRefs;
    }
    
    /**
     * Adds the given attachment references to the asnn2AttachmentCache. May be
     * any type of attachment. This method is additive for this key, so you may
     * add assignment attachments and submission attachments separately
     * @param curUserID
     * @param asnnId
     * @param attachmentRefs
     */
    public void addAsnn2AttachRefs(String curUserID, List<String> attachmentRefs) {
        List<String> existingAttachRefs = getAsnn2AttachRefs(curUserID);
        if (existingAttachRefs != null) {
            existingAttachRefs.addAll(attachmentRefs);
        } else {
            existingAttachRefs = attachmentRefs;
        }
        
        asnn2AttachmentCache.put(getAsnn2AttachRefsKey(curUserID), existingAttachRefs);
    }
    
    private String getAsnn2AttachRefsKey(String curUserID) {
        return ASSIGN_ATTACH_PREFIX + curUserID;
    }

    public void init() {
        sortedStudentIdsCache = memoryService.newCache(this.getClass().getCanonicalName()+".sortedStudentIdsCache");
        asnn2AttachmentCache = memoryService.newCache(this.getClass().getCanonicalName()+".asnn2AttachmentCache");
    }
}
