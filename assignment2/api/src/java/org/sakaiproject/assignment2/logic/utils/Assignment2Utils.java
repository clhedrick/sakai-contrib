package org.sakaiproject.assignment2.logic.utils;

public class Assignment2Utils {

    public static final String MOZILLA_BR = "<br type=\"_moz\" />";

    /**
     * Attempts to remove all unnecessary tags from html strings.
     * 
     * @param cleanup an html string to clean up. may be null
     * @return the cleaned up string
     */
    public static String cleanupHtmlText(String cleanup) {
        if (cleanup == null) {
            // nulls are ok
            return null;
        } else if (cleanup.trim().length() == 0) {
            // nothing to do
            return cleanup;
        }
        cleanup = cleanup.trim();

        //remove the unnecessary <br type="_moz" />
        cleanup = cleanup.replace(MOZILLA_BR, "");

        return cleanup;
    }

    /**
     * 
     * @param existingString
     * @return a "versioned" string based upon the given existingString. If the existingString
     * ends with a space then number, for example "Homework 1", will increment the existing
     * versioning and return "Homework 2". If no version exists (no space and then
     * number > 0), it will return the existingString plus a space 1 (ie "Persuasive Essay"
     * would be returned as "Persuasive Essay 1").  Returns null if existingString is null.
     * 
     */
    public static String getVersionedString(String existingString) {
        String duplicatedString = null;
        if (existingString != null) {
            int numToAppend = 1;

            // first, let's see if there is an existing version number on this
            // string (such as "Homework 1")
            String[] stringPieces = existingString.split(" ");
            if (stringPieces.length > 1) {
                String possibleNumber = stringPieces[stringPieces.length - 1];
                try {
                    int existingNumber = Integer.parseInt(possibleNumber);
                    if (existingNumber >= 0) {
                        numToAppend = existingNumber + 1;

                        // rebuild the string without the ending version info
                        String unversionedString = "";
                        for (int i = 0; i < stringPieces.length-1; i++) {
                            if (i != 0) {
                                unversionedString += " ";
                            }
                            unversionedString += stringPieces[i];
                        }

                        existingString = unversionedString;
                    }
                } catch (NumberFormatException nfe) {
                    // not an integer so not really versioned
                }
            } 

            duplicatedString = existingString + " " + numToAppend;
        }

        return duplicatedString;
    }

}
