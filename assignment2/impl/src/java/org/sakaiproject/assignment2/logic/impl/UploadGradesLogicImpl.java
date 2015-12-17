/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/beans/UploadBean.java $
 * $Id: UploadBean.java 49293 2008-05-22 15:30:04Z stuart.freeman@et.gatech.edu $
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

package org.sakaiproject.assignment2.logic.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.exception.GradebookItemNotFoundException;
import org.sakaiproject.assignment2.exception.InvalidGradeForAssignmentException;
import org.sakaiproject.assignment2.logic.AssignmentLogic;
import org.sakaiproject.assignment2.logic.AssignmentPermissionLogic;
import org.sakaiproject.assignment2.logic.ExternalGradebookLogic;
import org.sakaiproject.assignment2.logic.ExternalLogic;
import org.sakaiproject.assignment2.logic.GradeInformation;
import org.sakaiproject.assignment2.exception.UploadException;
import org.sakaiproject.assignment2.logic.UploadGradesLogic;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.model.UploadAllOptions;
import org.sakaiproject.component.api.ServerConfigurationService;

/**
 * Functionality for uploading grades via a csv file
 * 
 */
public class UploadGradesLogicImpl implements UploadGradesLogic
{
    private static final Log log = LogFactory.getLog(UploadGradesLogicImpl.class);


    private AssignmentPermissionLogic permissionLogic;
    public void setAssignmentPermissionLogic(AssignmentPermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    private AssignmentLogic assnLogic;
    public void setAssignmentLogic(AssignmentLogic assnLogic)
    {
        this.assnLogic = assnLogic;
    }

    private ExternalGradebookLogic gradebookLogic;
    public void setExternalGradebookLogic(ExternalGradebookLogic gradebookLogic)
    {
        this.gradebookLogic = gradebookLogic;
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic)
    {
        this.externalLogic = externalLogic;
    }
    
    private ServerConfigurationService serverConfigurationService;
    public void setServerConfigurationService(ServerConfigurationService serverConfigurationService) {
        this.serverConfigurationService = serverConfigurationService;
    }

    public List<String> uploadGrades(Map<String, String> displayIdUserIdMap, UploadAllOptions options, List<List<String>> parsedContent)
    {
        if (options == null || options.assignmentId == null) {
            throw new IllegalArgumentException("No assignmentId available in options passed to uploadGrades. Either options or options.assignmentId is null");
        }

        Assignment2 assign = assnLogic.getAssignmentById(options.assignmentId);
        if (assign == null) {
            throw new AssignmentNotFoundException("No assignment exists with id: " + options.assignmentId);
        }

        if (!assign.isGraded() || assign.getGradebookItemId() == null) {
            throw new IllegalArgumentException("You may only upload grades for an assignment " +
            "that is associated with a gradebook item.");
        }

        if (!gradebookLogic.gradebookItemExists(assign.getGradebookItemId())) {
            throw new GradebookItemNotFoundException("No gradebook item exists with the given id: " + assign.getGradebookItemId());
        }

        if (!gradebookLogic.isCurrentUserAbleToGrade(assign.getContextId())) {
            throw new SecurityException("User attempted to upload grades without permission");
        }

        String currUserId = externalLogic.getCurrentUserId();

        // parse the content into GradeInformation records
        Map<String, GradeInformation> gradeInfoToUpdate = retrieveGradeInfoFromContent(displayIdUserIdMap, assign.getGradebookItemId(), parsedContent);

        // let's remove any students the user is not authorized to grade from the
        // list we send the gradebook. this will avoid a SecurityException.
        List<String> gradableStudents = gradebookLogic.getGradableStudentsForGradebookItem(currUserId, 
                assign.getContextId(), assign.getGradebookItemId());

        List<String> studentsIgnored = new ArrayList<String>();
        List<GradeInformation> filteredGradeInfoList = new ArrayList<GradeInformation>();

        for (Map.Entry<String, GradeInformation> entry : gradeInfoToUpdate.entrySet()) {
            String displayId = entry.getKey();
            GradeInformation gradeInfo = entry.getValue();

            if (gradableStudents.contains(gradeInfo.getStudentId())) {
                filteredGradeInfoList.add(gradeInfo);
            } else {
                studentsIgnored.add(displayId);
            }
        }

        // update the grades
        try {
            gradebookLogic.saveGradesAndComments(assign.getContextId(), assign.getGradebookItemId(), filteredGradeInfoList);
        } catch (InvalidGradeForAssignmentException igfae) {
            throw new InvalidGradeForAssignmentException(igfae.getMessage(), igfae);
        }
        
        // set the released status for this gradebook item if the user has permission
        if (gradebookLogic.isCurrentUserAbleToEdit(assign.getContextId())) {
            gradebookLogic.releaseOrRetractGrades(assign.getContextId(), assign.getGradebookItemId(), options.releaseGrades, null);
        }

        return studentsIgnored;
    }

    /**
     * 
     * @param displayIdUserIdMap
     * @param gradebookItemId
     * @param parsedContent
     * @return a map of the displayId to GradeInformation object for each student extracted from
     * the content of the parsed file
     */
    private Map<String, GradeInformation> retrieveGradeInfoFromContent(Map<String, String> displayIdUserIdMap, Long gradebookItemId, List<List<String>> parsedContent) {
        if (gradebookItemId == null) {
            throw new IllegalArgumentException("Null gradebookItemId passed to retrieveGradeInfoFromContent");
        }

        Map<String, GradeInformation> displayIdToGradeInfoMap = new HashMap<String, GradeInformation>();

        if (parsedContent != null && !parsedContent.isEmpty()) {

            for (List<String> parsedRow : parsedContent) {
                // content: [Display ID, Sort Name, Grade, Comments]
                String displayId = null;
                String grade = null;
                String comments = null;
                int displayIdIndex = 0;
                int nameIndex = 1;
                int gradeIndex = 2;
                int commentsIndex = 3;

                for (int contentIndex = 0; contentIndex < parsedRow.size(); contentIndex++) {
                    String content = parsedRow.get(contentIndex);
                    if (content != null) {
                        content = content.trim();
                    }

                    if (contentIndex == displayIdIndex) {
                        displayId = content;
                    } else if (contentIndex == nameIndex) {
                        // we don't use the name
                    } else if (contentIndex == gradeIndex) {
                        grade = content;
                    } else if (contentIndex == commentsIndex) {
                        comments = content;
                    }
                }

                if (displayId != null && displayId.length() > 0) {
                    // retrieve the equivalent userId

                    if (displayIdUserIdMap == null || !displayIdUserIdMap.containsKey(displayId)) {
                        // we skip users in the file who aren't in this site
                        if (log.isDebugEnabled()) log.debug("User with id " + displayId + " is not contained in the given displayIdUserIdMap");
                    } else {
                        String userId = displayIdUserIdMap.get(displayId);
                        GradeInformation gradeInfo = new GradeInformation();
                        gradeInfo.setStudentId(userId);
                        gradeInfo.setGradebookItemId(gradebookItemId);
                        gradeInfo.setGradebookGrade(grade);
                        gradeInfo.setGradebookComment(comments);

                        displayIdToGradeInfoMap.put(displayId, gradeInfo);
                    }
                }
            }
        }

        return displayIdToGradeInfoMap;
    }

    /**
     * 
     * @param parsedContent
     * @return a list of all of the displayIds included in the content
     */
    private List<String> getDisplayIdListFromContent(List<List<String>> parsedContent) {
        List<String> displayIdList = new ArrayList<String>();
        if (parsedContent != null) {

            for (List<String> parsedRow : parsedContent) {
                // content: [Display ID, Sort Name, Grade, Comments]
                String displayId = null;

                if (parsedRow != null && parsedRow.size() > 0) {
                    displayId = parsedRow.get(0);
                }

                if (displayId != null && displayId.trim().length() > 0) {
                    displayIdList.add(displayId.trim());
                }
            }
        }
        return displayIdList;
    }

    private Map<String, String> getDisplayIdToGradeMapFromContent(List<List<String>> parsedContent) {
        Map<String, String> displayIdToGradeMap = new HashMap<String, String>();
        if (parsedContent != null) {

            for (List<String> parsedRow : parsedContent) {
                // content: [Display ID, Sort Name, Grade, Comments]
                String displayId = null;
                String grade = null;

                if (parsedRow != null && parsedRow.size() > 2) {
                    displayId = parsedRow.get(0);
                    grade = parsedRow.get(2);
                }

                if (displayId != null && displayId.trim().length() > 0) {
                    displayIdToGradeMap.put(displayId.trim(), grade);
                }
            }
        }
        return displayIdToGradeMap;
    }

    public List<List<String>> getCSVContent(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Null inputStream passed to getCSVContent");
        }

        List<List<String>> csvContent = new ArrayList<List<String>>();

        try {
            // split the data into lines
            List<String> rows = csvtoArray(inputStream);

            // skip the header row
            for (int rowIndex=1; rowIndex < rows.size(); rowIndex++) {
                List<String> rowContent = new ArrayList<String>();
                CSV csv = new CSV();
                rowContent = csv.parse(rows.get(rowIndex));

                if (rowContent != null && !rowContent.isEmpty()) {
                    csvContent.add(rowContent);
                }
            }
        } catch (FileSystemException fse) {
            throw new UploadException("There was an error parsing the data in the csv file", fse);
        } catch (IOException ioe) {
            throw new UploadException("There was an error parsing the data in the csv file", ioe);
        } finally {
            if (inputStream != null) {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    log.warn(("IOException while closing InputStream while parsing CSV content"));
                }
            }
        }

        return csvContent;
    }

    public List<List<String>> getCSVContent(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Null file passed to uploadGrades");
        }

        if (!file.getName().endsWith(".csv")) {
            throw new IllegalArgumentException("Uploaded file must be in .csv format");
        }

        List<List<String>> csvContent = new ArrayList<List<String>>();

        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject gradesFile = fsManager.toFileObject(file);
            FileContent gradesFileContent = gradesFile.getContent();
            csvContent = getCSVContent(gradesFileContent.getInputStream());
        } catch (FileSystemException fse) {
            throw new UploadException("There was an error parsing the data in the csv file", fse);
        } 

        return csvContent;
    }

    public List<String> getInvalidDisplayIdsInContent(Map<String, String> displayIdUserIdMap, List<List<String>> parsedContent) {
        List<String> displayIdsNotInSite = new ArrayList<String>();
        if (parsedContent != null) {
            // now extract the displayIds from the content
            List<String> displayIdsInContent = getDisplayIdListFromContent(parsedContent);
            for (String displayId : displayIdsInContent) {
                if (displayIdUserIdMap == null || !displayIdUserIdMap.containsKey(displayId)) {
                    displayIdsNotInSite.add(displayId);
                }
            }
        }

        return displayIdsNotInSite;
    }   

    public List<String> getStudentsWithInvalidGradesInContent(List<List<String>> parsedContent, String contextId) {
        List<String> displayIdsAssocWithInvalidGrades = new ArrayList<String>();
        if (parsedContent != null) {
            Map<String, String> displayIdToGradeMap = getDisplayIdToGradeMapFromContent(parsedContent);
            displayIdsAssocWithInvalidGrades = gradebookLogic.identifyStudentsWithInvalidGrades(contextId, displayIdToGradeMap);
        }

        return displayIdsAssocWithInvalidGrades;
    }

    public List<List<String>> removeStudentsFromContent(List<List<String>> parsedContent, List<String> studentsToRemove) {
        List<List<String>> revisedContent = new ArrayList<List<String>>();
        if (parsedContent != null && studentsToRemove != null && !studentsToRemove.isEmpty()) {
            for (List<String> parsedRow : parsedContent) {
                // content: [Display ID, Sort Name, Grade, Comments]
                String displayId = null;

                if (parsedRow != null && parsedRow.size() > 2) {
                    displayId = parsedRow.get(0);
                }

                if (displayId != null && displayId.trim().length() > 0 && !studentsToRemove.contains(displayId)) {
                    revisedContent.add(parsedRow);
                }
            }
        }

        return revisedContent;
    }
    
    public int getGradesMaxFileSize() {
        int maxFileSizeInMB;
        try {
            maxFileSizeInMB = serverConfigurationService.getInt(UploadGradesLogic.FILE_UPLOAD_MAX_PROP, 
                    UploadGradesLogic.FILE_UPLOAD_MAX_DEFAULT);
        } catch (NumberFormatException nfe) {
            if (log.isDebugEnabled()) log.debug("Invalid property set for gradebook max file size");
            maxFileSizeInMB = UploadGradesLogic.FILE_UPLOAD_MAX_DEFAULT;
        }
        
        return maxFileSizeInMB;
    }

    /**
     * converts an input stream to a List consisting of Strings
     * representing a line
     *
     * @param inputStream
     * @return a List consisting of Strings representing a line
     */
    private List<String> csvtoArray(InputStream inputStream) throws IOException{
        List<String> contents = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine())!=null){
            if (line.replaceAll(",", "").replaceAll("\"", "").equals("")) {
                continue;
            }
            
            contents.add(line);
        }
        return contents;

    }

    ////////////////////////////////////////////////////////////////////
    // this Class is copied from the Gradebook - used for parsing a line
    // of CSV data

    /** Parse comma-separated values (CSV), a common Windows file format.
     * Sample input: "LU",86.25,"11/4/1998","2:19PM",+4.0625
     * <p>
     * Inner logic adapted from a C++ original that was
     * Copyright (C) 1999 Lucent Technologies
     * Excerpted from 'The Practice of Programming'
     * by Brian W. Kernighan and Rob Pike.
     * <p>
     * Included by permission of the http://tpop.awl.com/ web site,
     * which says:
     * "You may use this code for any purpose, as long as you leave
     * the copyright notice and book citation attached." I have done so.
     * @author Brian W. Kernighan and Rob Pike (C++ original)
     * @author Ian F. Darwin (translation into Java and removal of I/O)
     * @author Ben Ballard (rewrote advQuoted to handle '""' and for readability)
     */
    class CSV {

        public static final char DEFAULT_SEP = ',';

        /** Construct a CSV parser, with the default separator (`,'). */
        public CSV() {
            this(DEFAULT_SEP);
        }

        /** Construct a CSV parser with a given separator.
         * @param sep The single char for the separator (not a list of
         * separator characters)
         */
        public CSV(char sep) {
            fieldSep = sep;
        }

        /** The fields in the current String */
        protected List list = new ArrayList();

        /** the separator char for this parser */
        protected char fieldSep;

        /** parse: break the input String into fields
         * @return java.util.Iterator containing each field
         * from the original as a String, in order.
         */
        public List parse(String line)
        {
            StringBuilder sb = new StringBuilder();
            list.clear();      // recycle to initial state
            int i = 0;

            if (line.length() == 0) {
                list.add(line);
                return list;
            }

            do {
                sb.setLength(0);
                if (i < line.length() && line.charAt(i) == '"')
                    i = advQuoted(line, sb, ++i);  // skip quote
                else
                    i = advPlain(line, sb, i);
                list.add(sb.toString());
                i++;
            } while (i < line.length());
            if(log.isDebugEnabled()) {
                StringBuilder logBuffer = new StringBuilder("Parsed " + line + " as: ");
                for(Iterator iter = list.iterator(); iter.hasNext();) {
                    logBuffer.append(iter.next());
                    if(iter.hasNext()) {
                        logBuffer.append(", ");
                    }
                }
                log.debug("Parsed source string " + line + " as " + logBuffer.toString() + ", length=" + list.size());
            }
            return list;

        }

        /** advQuoted: quoted field; return index of next separator */
        protected int advQuoted(String s, StringBuilder sb, int i)
        {
            int j;
            int len= s.length();
            for (j=i; j<len; j++) {
                if (s.charAt(j) == '"' && j+1 < len) {
                    if (s.charAt(j+1) == '"') {
                        j++; // skip escape char
                    } else if (s.charAt(j+1) == fieldSep) { //next delimeter
                        j++; // skip end quotes
                        break;
                    }
                } else if (s.charAt(j) == '"' && j+1 == len) { // end quotes at end of line
                    break; //done
                }
                sb.append(s.charAt(j));  // regular character.
            }
            return j;
        }

        /** advPlain: unquoted field; return index of next separator */
        protected int advPlain(String s, StringBuilder sb, int i)
        {
            int j;

            j = s.indexOf(fieldSep, i); // look for separator
            if (j == -1) {                 // none found
                sb.append(s.substring(i));
                return s.length();
            } else {
                sb.append(s.substring(i, j));
                return j;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////

}