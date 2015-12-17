package org.sakaiproject.assignment2.logic;

import java.io.OutputStream;
import java.util.regex.Pattern;

import org.sakaiproject.assignment2.model.Assignment2;

public interface ZipExportLogic
{
    /**
     * The regular expression used for extracting identifying information from the
     * folder names.  This pattern extracts the value within parentheses.
     * For instance, the submitter folder is in the format "Wagner, Michelle (wagnermr)".
     * This expression will extract the username for use in uploading info.
     * Also used for the version folder which is in the format "20090225_12:45PM (34)" where
     * the value in parens is the version id.
     */
    public static final String FILE_NAME_REGEX = "(?<=\\()[^\\(\\)]+(?=\\))";

    /**
     * the file can't be extracted and is considered "corrupt" if the folder 
     * name is longer than this for some reason
     */
    public static final int MAX_FILE_NAME_LENGTH = 83;

    /**
     * Zip up the submissions for the given assignment.  Will only include
     * submissions that the current user is allowed to view or grade. 
     * Each student has a folder that contains every submission version. Each version
     * folder contains the submitted text and attachments. It also contains a
     * "Feedback" folder.  If there are no submitted versions, the student's folder
     * will only contain a "Feedback" folder for the instructor to provide feedback 
     * without submission. The "Feedback" folder contains a file for feedback comments and
     * a file for annotating the submitted text (if it exists).  If assignment is graded, includes a csv file containing 
     * the grade information.  Only gradable students are include in the grades csv file.
     * @param outputStream
     * @param assignmentId
     * @throws SecurityException if current user is not authorized to access
     * the given assignment from a grading perspective
     */
    void getSubmissionsZip(OutputStream outputStream, Long assignmentId);

    /**
     * Zip up the submissions for the given assignment.  Will only include
     * submissions that the current user is allowed to view or grade. 
     * Each student has a folder that contains every submission version. Each version
     * folder contains the submitted text and attachments. It also contains a
     * "Feedback" folder.  If there are no submitted versions, the student's folder
     * will only contain a "Feedback" folder for the instructor to provide feedback 
     * without submission. The "Feedback" folder contains a file for feedback comments and
     * a file for annotating the submitted text (if it exists).  If assignment is graded, includes a csv file containing 
     * the grade information.  Only gradable students are include in the grades csv file.
     * @param outputStream
     * @param assignmentId
     * @param filterGroupId
     * @throws SecurityException if current user is not authorized to access
     * the given assignment from a grading perspective
     */
    void getSubmissionsZip(OutputStream outputStream, Long assignmentId, String filterGroupId);

    /**
     * 
     * @param folderName
     * @param pattern the Pattern for extracting the id compiled using {@link ZipExportLogic#FILE_NAME_REGEX}
     * @return the unique identifier associated with the given folder.
     * Returns null if id cannot be derived from folder name. For instance,
     * for the student submission folder will return the student's displayId. For
     * a version folder will return the version id.
     */
    public String extractIdFromFolderName(String folderName, Pattern pattern);

    /**
     * 
     * @param value
     * @param replaceSpaces will replace all spaces with the given value. leave
     * null if you want to preserve spaces
     * @return Formats and escapes the given String value.  This is useful for user-supplied text
     * (such as the assignment title) so we don't run into trouble with special
     * characters or exceed the length for folder names when decompressing
     */
    public String escapeZipEntry(String value, String replaceSpaces);

    /**
     * 
     * @return the folder name used for the Feedback folder in the download
     */
    public String getFeedbackFolderName();

    /**
     * 
     * @return the file name for the feedback comments in the download
     */
    public String getFeedbackFileName();

    /**
     * 
     * @return the file name for the annotated submitted text in the download
     */
    public String getAnnotatedTextFileName();

    /**
     * 
     * @param assignment
     * @return the name of the top level folder for this download.
     * built from the assignment title and site title
     * for example "My Assignment Title_SP09 MATH 413"
     */
    public String getTopLevelFolderName(Assignment2 assignment);
}