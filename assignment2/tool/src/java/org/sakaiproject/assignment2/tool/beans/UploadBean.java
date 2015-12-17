/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/beans/UploadBean.java $
 * $Id: UploadBean.java 87495 2015-04-06 15:12:29Z hedrick@rutgers.edu $
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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.UploadException;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalEventLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.UploadAllLogic;
import org.sakaiproject.assignment2.logic.UploadGradesLogic;
import org.sakaiproject.assignment2.logic.UploadAllLogic.UploadInfo;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.UploadAllOptions;
import org.sakaiproject.assignment2.model.constants.AssignmentConstants;
import org.sakaiproject.assignment2.tool.WorkFlowResult;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.springframework.web.multipart.MultipartFile;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

/**
 * This bean is for binding the Upload Grades form. Currently thats the 
 * uploadall template and producer.
 * 
 * @author carl.hall
 * @author wagnermr
 * @author stuart.freeman
 * @author sgithens
 *
 */
public class UploadBean
{  
    private static final Log log = LogFactory.getLog(UploadBean.class);

    private UploadAllOptions uploadOptions;
    private Map<String, MultipartFile> uploads;

    // Property / Dependency
    private TargettedMessageList messages;
    public void setTargettedMessageList(TargettedMessageList messages) {
        this.messages = messages;
    }

    // Dependency
    private UploadGradesLogic uploadGradesLogic;
    public void setUploadGradesLogic(UploadGradesLogic uploadGradesLogic)
    {
        this.uploadGradesLogic = uploadGradesLogic;
    }

    private UploadAllLogic uploadAllLogic;
    public void setUploadAllLogic(UploadAllLogic uploadAllLogic)
    {
        this.uploadAllLogic = uploadAllLogic;
    }

    private AssignmentLogic assignmentLogic;
    public void setAssignmentLogic(AssignmentLogic assignmentLogic) {
        this.assignmentLogic = assignmentLogic;
    }
    
    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic) {
        this.gradebookLogic = gradebookLogic;
    }
    
    private AssignmentPermissionLogic permissionLogic;
    public void setAssignmentPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }
    
    private ExternalEventLogic eventLogic;
    public void setExternalEventLogic(ExternalEventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }

    public void setMultipartMap(Map<String, MultipartFile> uploads)
    {
        this.uploads = uploads;
    }

    // Property
    public UploadAllOptions getUploadOptions()
    {
        if (uploadOptions == null)
            uploadOptions = new UploadAllOptions();
        return uploadOptions;
    }

    // Dependency
    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    private String csrfToken;
    public void setCsrfToken(String data) {
	csrfToken = data;
    }

    /*
     * The members below are stateful variables used to store the uploaded data
     * while it's verified in the wizard/workflow
     */
    public Map<String, String> displayIdUserIdMap;
    public List<List<String>> parsedContent;

    /**
     * The user may upload either a zip archive or just the grades csv file.
     * This method will figure out what they uploaded and redirect to 
     * the appropriate upload process. 
     * @return
     */
    public WorkFlowResult processUpload() {
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.UPLOAD_FAILURE;

        if (uploadOptions == null || uploadOptions.assignmentId == null ) {
            messages.addMessage(new TargettedMessage("No assignmentId was passed " +
                    "in the request to processUploadGradesCSV. Cannot continue.", new Object[] {},
                    TargettedMessage.SEVERITY_ERROR));
            return WorkFlowResult.UPLOAD_FAILURE;
        }

        Assignment2 assign = assignmentLogic.getAssignmentByIdWithAssociatedData(uploadOptions.assignmentId);
        if (assign == null) {
            throw new AssignmentNotFoundException("No assignment exists with " +
                    "the assignmentId passed to the upload via uploadOptions: " + uploadOptions.assignmentId);
        }
        
        boolean gbItemExists = assign.isGraded() && 
            assign.getGradebookItemId() != null && 
            gradebookLogic.gradebookItemExists(assign.getGradebookItemId());

        if (uploads.isEmpty()) 
        {
            if (assign.isGraded() && gbItemExists) {
                messages.addMessage(new TargettedMessage("assignment2.uploadall.alert.file_type.graded", new Object[] {},
                        TargettedMessage.SEVERITY_ERROR));
            } else {
                messages.addMessage(new TargettedMessage("assignment2.uploadall.alert.file_type.ungraded", new Object[] {},
                        TargettedMessage.SEVERITY_ERROR));
            }
            return WorkFlowResult.UPLOAD_FAILURE;
        }


        MultipartFile uploadedFile = uploads.get("file");

        long uploadedFileSize = uploadedFile.getSize();
        if (uploadedFileSize == 0)
        {
            if (assign.isGraded() && gbItemExists) {
                messages.addMessage(new TargettedMessage("assignment2.uploadall.alert.file_type.graded", new Object[] {},
                        TargettedMessage.SEVERITY_ERROR));
            } else {
                messages.addMessage(new TargettedMessage("assignment2.uploadall.alert.file_type.ungraded", new Object[] {},
                        TargettedMessage.SEVERITY_ERROR));
            }
            return WorkFlowResult.UPLOAD_FAILURE;
        }

        // double check that the file doesn't exceed our upload limit
        String maxFileSizeInMB = ServerConfigurationService.getString("content.upload.max", "1");
        int maxFileSizeInBytes = 1024 * 1024;
        try {
            maxFileSizeInBytes = Integer.parseInt(maxFileSizeInMB) * 1024 * 1024;
        } catch(NumberFormatException e) {
            log.warn("Unable to parse content.upload.max retrieved from properties file during upload");
        }

        if (uploadedFileSize > maxFileSizeInBytes) {
            messages.addMessage(new TargettedMessage("assignment2.uploadall.error.file_size", new Object[] {maxFileSizeInMB}, TargettedMessage.SEVERITY_ERROR));
            return WorkFlowResult.UPLOAD_FAILURE;
        }

        boolean isZip = "application/zip".equals(uploadedFile.getContentType()) || 
            "application/x-zip-compressed".equals(uploadedFile.getContentType()) ||
            "application/x-zip".equals(uploadedFile.getContentType());
        boolean isCsv = uploadedFile.getOriginalFilename().endsWith(".csv");

        if (isZip) {
            return processUploadAll(uploadedFile, assign);
        } else if (isCsv) {
            // we have a separate limit for the size of the csv file
            int maxGradesFileSize = uploadGradesLogic.getGradesMaxFileSize();
            long maxGradesSizeInBytes = 1024L * 1024L * maxGradesFileSize; 
            if (uploadedFileSize > maxGradesSizeInBytes) {
                messages.addMessage(new TargettedMessage("assignment2.upload_grades.error.file_size", 
                        new Object[] {maxGradesFileSize}, TargettedMessage.SEVERITY_ERROR));
                return WorkFlowResult.UPLOAD_FAILURE;
            }
            // make sure this assignment is graded and the gradebook item still exists
            if (!assign.isGraded()) {
                messages.addMessage(new TargettedMessage("assignment2.uploadall.upload_csv.ungraded", new Object[] {maxFileSizeInMB}, TargettedMessage.SEVERITY_ERROR));
                return WorkFlowResult.UPLOAD_FAILURE;
            } else if (!gbItemExists) {
                messages.addMessage(new TargettedMessage("assignment2.uploadall.upload_csv.no_gb_item", new Object[] {maxFileSizeInMB}, TargettedMessage.SEVERITY_ERROR));
                return WorkFlowResult.UPLOAD_FAILURE;
            }

            return processUploadGradesCSV(uploadedFile, assign);
        } else {
            if (assign.isGraded() && gbItemExists) {
                messages.addMessage(new TargettedMessage("assignment2.uploadall.alert.file_type.graded", new Object[] {},
                        TargettedMessage.SEVERITY_ERROR));
            } else {
                messages.addMessage(new TargettedMessage("assignment2.uploadall.alert.file_type.ungraded", new Object[] {},
                        TargettedMessage.SEVERITY_ERROR));
            }
            return WorkFlowResult.UPLOAD_FAILURE;
        }
    }

    /**
     * Action Method Binding for going back to the Upload after viewing the
     * parsed contents of the previous upload attempt.  
     * 
     * @return
     */
    public WorkFlowResult processBackToUpload() {
        // Clear previous data
        displayIdUserIdMap = null;
        parsedContent = null;
        return WorkFlowResult.UPLOADALL_CSV_BACK_TO_UPLOAD;
    }

    /**
     * Action Method Binding for confirming the save information processing
     * after viewing the parsed data from the upload.
     * 
     * 
     * @return
     */
    public WorkFlowResult processUploadConfirmAndSave() {
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.UPLOAD_FAILURE;

        // Putting in Confirm Dialog ASNN-313
        List<String> usersNotUpdated = uploadGradesLogic.uploadGrades(displayIdUserIdMap, uploadOptions, parsedContent);

        if (!usersNotUpdated.isEmpty()) {
            messages.addMessage(new TargettedMessage("assignment2.upload_grades.upload_successful_with_exception",
                    new Object[] {getListAsString(usersNotUpdated)}, TargettedMessage.SEVERITY_INFO));
        } else {
            messages.addMessage(new TargettedMessage("assignment2.upload_grades.upload_successful",
                    new Object[] {}, TargettedMessage.SEVERITY_INFO));
        }

        // ASNN-29 This is the final bit for uploading a CSV, one event should go here.
        triggerUploadEvent(assignmentLogic.getAssignmentById(uploadOptions.assignmentId));
        return WorkFlowResult.UPLOADALL_CSV_CONFIRM_AND_SAVE;   
    }
    
    /**
     * Private utility method to trigger a Sakai Event for Uploading grades.
     * 
     * @param assign The assignment reference this is occuring on.
     */
    private void triggerUploadEvent(Assignment2 assign) {
        assert assign != null;
        
        eventLogic.postEvent(AssignmentConstants.EVENT_UPLOAD_FEEDBACK_AND_GRADES, 
                assign.getReference());
        
    }

    private WorkFlowResult processUploadAll(MultipartFile uploadedFile, Assignment2 assign)
    {
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.UPLOAD_FAILURE;

        // you should have already done validation on the file at this point

        File newFile = null;
        try {
            newFile = File.createTempFile(uploadedFile.getName(), ".zip");
            uploadedFile.transferTo(newFile);
        } catch (IOException ioe) {
            if (newFile != null) {
                newFile.delete();
            }
            throw new UploadException(ioe.getMessage(), ioe);
        }

        // try to upload this file
        try {
            List<Map<String, String>> uploadInfo = uploadAllLogic.uploadAll(uploadOptions, newFile);
            addUploadMessages(uploadInfo);
        } catch (UploadException ue) {
            if (log.isDebugEnabled()) log.debug("UploadException encountered while attempting to UploadAll for assignment: " + assign.getTitle(), ue);

            messages.addMessage(new TargettedMessage("assignment2.uploadall.error.failure", new Object[] {assign.getTitle()}));
            return WorkFlowResult.UPLOAD_FAILURE;
        } finally {
            if (newFile != null) {
                newFile.delete();
            }
        }

        // ASNN-29 This is the end and it uploads all the grades here for zip
        triggerUploadEvent(assign);
        return WorkFlowResult.UPLOAD_SUCCESS;  
    }

    /**
     * Action Method Binding for the Upload Button on the initial page of the
     * upload workflow/wizard.
     * 
     * @return
     */
    private WorkFlowResult processUploadGradesCSV(MultipartFile uploadedFile, Assignment2 assignment)
    {
	if (!AssignmentAuthoringBean.checkCsrf(csrfToken))
            return WorkFlowResult.UPLOAD_FAILURE;

        // validation on file should have already occurred

        File newFile = null;
        try {
            newFile = File.createTempFile(uploadedFile.getName(), ".csv");
            uploadedFile.transferTo(newFile);
        } catch (IOException ioe) {
            throw new UploadException(ioe.getMessage(), ioe);
        }

        // retrieve the displayIdUserId info once and re-use it
        Set<String> submitters = permissionLogic.getSubmittersInSite(assignment.getContextId());
        displayIdUserIdMap = externalLogic.getUserDisplayIdUserIdMapForUsers(submitters);
        parsedContent = uploadGradesLogic.getCSVContent(newFile);
        
        // delete the file
        newFile.delete();
        
        // ensure that there is not an unreasonable number of rows compared to
        // the number of students in the site
        int numStudentsInSite = displayIdUserIdMap.size();
        int numRowsInUpload = parsedContent.size();
        if (numRowsInUpload > (numStudentsInSite + 50)) {
            messages.addMessage(new TargettedMessage("assignment2.upload_grades.too_many_rows", 
                    new Object[] {numRowsInUpload, numStudentsInSite}, TargettedMessage.SEVERITY_ERROR ));
            return WorkFlowResult.UPLOADALL_CSV_UPLOAD_FAILURE;
        }

        // let's check that the students included in the file are actually in the site
        List<String> invalidDisplayIds = uploadGradesLogic.getInvalidDisplayIdsInContent(displayIdUserIdMap, parsedContent);
        if (invalidDisplayIds != null && !invalidDisplayIds.isEmpty()) {
            messages.addMessage(new TargettedMessage("assignment2.upload_grades.user_not_in_site", 
                    new Object[] {getListAsString(invalidDisplayIds)}, TargettedMessage.SEVERITY_ERROR ));
            return WorkFlowResult.UPLOADALL_CSV_UPLOAD_FAILURE;
        }

        // check that the grades are valid
        List<String> displayIdsWithInvalidGrade = uploadGradesLogic.getStudentsWithInvalidGradesInContent(parsedContent, assignment.getContextId());
        if (displayIdsWithInvalidGrade != null && !displayIdsWithInvalidGrade.isEmpty()) {
            messages.addMessage(new TargettedMessage("assignment2.upload_grades.grades_not_valid", 
                    new Object[] {getListAsString(displayIdsWithInvalidGrade)}, TargettedMessage.SEVERITY_ERROR ));
            return WorkFlowResult.UPLOADALL_CSV_UPLOAD_FAILURE;
        }

        // let's proceed with the grade upload
        return WorkFlowResult.UPLOADALL_CSV_UPLOAD;
    }

    private String getListAsString(List<String> itemList) {
        StringBuilder sb = new StringBuilder();
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }

                sb.append(itemList.get(i));
            }
        }

        return sb.toString();
    }

    /**
     * Will add appropriate error/info messages based upon the {@link UploadInfo}
     * information returned from the upload method
     * @param uploadInfo
     */
    private void addUploadMessages(List<Map<String, String>> uploadInfo) {

        if (uploadInfo != null) {
            String totalNumProcessed = "0";
            String totalNumUpdated = "0";

            for (Map<String, String> infoMap : uploadInfo) {
                String info = infoMap.get(UploadAllLogic.UPLOAD_INFO);
                String param = infoMap.get(UploadAllLogic.UPLOAD_PARAM);

                if (info.equals(UploadAllLogic.UploadInfo.UNABLE_TO_EXTRACT_USERNAME.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.extract_student",
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.UNABLE_TO_EXTRACT_VERSION.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.extract_version", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.FEEDBACK_ATTACHMENT_ERROR.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.attach", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.FEEDBACK_FILE_UPLOAD_ERROR.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.fb_notes", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.ANNOTATED_TEXT_UPLOAD_ERROR.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.ann_text", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.NO_GRADING_PERM_FOR_STUDENT.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.grading_perm", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.INVALID_STUDENT_IN_CSV.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.unknown_student_in_csv", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.STUDENT_WITH_INVALID_GRADE_IN_CSV.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.invalid_grade_csv", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.INVALID_NUM_ROWS_IN_CSV.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.invalid_grade_csv", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.CSV_FILE_TOO_LARGE.toString())) {
                    messages.addMessage(new TargettedMessage("assignment2.uploadall.error.grades_csv_file_size", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.NUM_STUDENTS_UPDATED.toString())) {
                    totalNumUpdated = param;
                } else if (info.equals(UploadAllLogic.UploadInfo.NUM_STUDENTS_IDENTIFIED_FOR_UPDATE.toString())) {
                    totalNumProcessed = param;
                } else if (info.equals(UploadAllLogic.UploadInfo.FEEDBACK_FILE_EVIL_CONTENT.toString())) {
                    // this message is constructed elsewhere and we just display what is returned
                    messages.addMessage(new TargettedMessage("assignment2.displayText", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                } else if (info.equals(UploadAllLogic.UploadInfo.ANNOTATED_TEXT_EVIL_CONTENT.toString())) {
                    // this message is constructed elsewhere and we just display what is returned
                    messages.addMessage(new TargettedMessage("assignment2.displayText", 
                            new Object[] {param}, TargettedMessage.SEVERITY_ERROR));
                }
            }

            // add the success message
            messages.addMessage(new TargettedMessage("assignment2.uploadall.info.success", 
                    new Object[] {totalNumProcessed, totalNumUpdated}, TargettedMessage.SEVERITY_INFO ));
        }
    }
}