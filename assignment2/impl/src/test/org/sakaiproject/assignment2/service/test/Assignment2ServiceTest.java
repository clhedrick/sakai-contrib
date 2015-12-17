/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/test/org/sakaiproject/assignment2/logic/test/AssignmentLogicTest.java $
 * $Id: AssignmentLogicTest.java 65784 2010-01-20 19:04:25Z wagnermr@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007 The Sakai Foundation.
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
package org.sakaiproject.assignment2.service.test;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.exception.AssignmentNotFoundException;
import org.sakaiproject.assignment2.model.Assignment2;
import org.sakaiproject.assignment2.service.model.AssignmentDefinition;
import org.sakaiproject.assignment2.test.Assignment2TestBase;
import org.sakaiproject.assignment2.test.AssignmentTestDataLoad;


public class Assignment2ServiceTest extends Assignment2TestBase {

    private static final Log log = LogFactory.getLog(Assignment2ServiceTest.class);

    /**
     * @see org.springframework.test.AbstractTransactionalSpringContextTests#onSetUpInTransaction()
     */
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
    }


    public void testGetAssignmentById() throws Exception {
        // try a null id
        try {
            assignment2Service.getAssignmentById(null);
            fail("did not catch null id passed to getAssignmentById");
        } catch (IllegalArgumentException iae) {}
        
        // try retrieving the assignment w/o permission
        externalLogic.setCurrentUserId("random");
        try {
            assignment2Service.getAssignmentById(testData.a1Id);
            fail("Did not catch invalid user attempting to access assignment via getAssignmentById");
        } catch (SecurityException se) {}

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        // try a bogus id
        try {
            assignment2Service.getAssignmentById(12345L);
            fail("did not catch bogus assignmentId passed to getAssignmentById");
        } catch (AssignmentNotFoundException anfe) {}

        // grab assignment 1
        AssignmentDefinition assignment = assignment2Service.getAssignmentById(testData.a1Id);
        assertNotNull(assignment);
        assertTrue(assignment.getTitle().equals(AssignmentTestDataLoad.ASSIGN1_TITLE));
        
        // let's set the accept until date on this assignment object
        Assignment2 modelAssign = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a1Id);
        modelAssign.setAcceptUntilDate(new Date());
        dao.save(modelAssign);
        
        // double check the instructor can see this date
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        assignment = assignment2Service.getAssignmentById(testData.a1Id);
        assertNotNull(assignment.getAcceptUntilDate());
        
        // switch to a student to ensure restricted fields have been set to null
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        assignment = assignment2Service.getAssignmentById(testData.a1Id);
        assertEquals(null, assignment.getAcceptUntilDate());
    }
    
    public void testGetAssignments() {
        // try a null context
        try {
            assignment2Service.getAssignments(null);
            fail("Did not catch null contextId passed to getAssignments");
        } catch (IllegalArgumentException iae) {}
        
        // set accept until dates on a1 and a2
        Assignment2 a1 = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a1Id);
        a1.setAcceptUntilDate(new Date());
        dao.save(a1);

        Assignment2 a2 = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a2Id);
        a2.setAcceptUntilDate(new Date());
        dao.save(a2);
        
        // let's make assignment3 and assignment4 graded and set accept until dates
        Assignment2 a3 = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a3Id);
        a3.setGraded(true);
        a3.setGradebookItemId(AssignmentTestDataLoad.GB_ITEM1_ID);
        a3.setAcceptUntilDate(new Date());
        dao.save(a3);

        Assignment2 a4 = dao.getAssignmentByIdWithGroupsAndAttachments(testData.a4Id);
        a4.setGraded(true);
        a4.setGradebookItemId(AssignmentTestDataLoad.GB_ITEM2_ID);
        a4.setAcceptUntilDate(new Date());
        dao.save(a4);

        // assign1 is restricted to group 1 and 3
        // assign2 is not restricted
        // graded assign 3 is not restricted
        // graded assign 4 is restricted to group 3

        // let's start with instructor. he/she should get all of the assignments
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.INSTRUCTOR_UID);
        List<AssignmentDefinition> assignList = assignment2Service.getAssignments(AssignmentTestDataLoad.CONTEXT_ID);
        assertNotNull(assignList);
        assertEquals(4, assignList.size());
        // check for the accept until date populated
        for (AssignmentDefinition def : assignList) {
            if (def.getId().equals(testData.a1Id)) {
                assertNotNull(def.getAcceptUntilDate());
            } else if (def.getId().equals(testData.a2Id)) {
                assertNotNull(def.getAcceptUntilDate());
            } else if (def.getId().equals(testData.a3Id)) {
                assertNotNull(def.getAcceptUntilDate());
            } else if (def.getId().equals(testData.a4Id)) {
                assertNotNull(def.getAcceptUntilDate());
            }
        }

        // let's try the ta. allowed to view all of the info b/c has general
        // manage submissions permission

        externalLogic.setCurrentUserId(AssignmentTestDataLoad.TA_UID);
        // should return assignment 1, 2, 3
        assignList = assignment2Service.getAssignments(AssignmentTestDataLoad.CONTEXT_ID);
        assertNotNull(assignList);
        assertEquals(3, assignList.size());
        // let's make sure that these are the right assign
        for (AssignmentDefinition assign : assignList) {
            if (assign.getId().equals(testData.a1Id)) {
                assertNotNull(assign.getAcceptUntilDate());
            }else if (assign.getId().equals(testData.a2Id)) {
                assertNotNull(assign.getAcceptUntilDate());
            } else if (assign.getId().equals(testData.a3Id)) {
                assertNotNull(assign.getAcceptUntilDate());
            } else {
                fail("Invalid assignment returned for TA via getViewableAssignments");
            }
        }

        // switch to student 1
        // member of group 1
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT1_UID);
        // should return assignment 1, 2, 3
        assignList = assignment2Service.getAssignments(AssignmentTestDataLoad.CONTEXT_ID);

        assertNotNull(assignList);
        assertEquals(3, assignList.size());
        for (AssignmentDefinition assign : assignList) {
            if (assign.getId().equals(testData.a1Id) || assign.getId().equals(testData.a2Id) ||
                    assign.getId().equals(testData.a3Id)) {
                // valid
                // accept until date should not be populated
                if (assign.getId().equals(testData.a3Id)) {
                    assertNull(assign.getAcceptUntilDate());
                } 
            }
            else {
                fail("Invalid assignment returned for STUDENT1 via getViewableAssignments");
            }
        }

        // switch to student 2
        // member of group 3
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT2_UID);
        // should return 1, 2, 3, 4
        assignList = assignment2Service.getAssignments(AssignmentTestDataLoad.CONTEXT_ID);
        assertNotNull(assignList);
        assertTrue(assignList.size() == 4);
        // let's make sure that these are the right assign
        for (AssignmentDefinition assign : assignList) {
            if (assign.getId().equals(testData.a2Id) || assign.getId().equals(testData.a1Id) ||
                    assign.getId().equals(testData.a3Id) || assign.getId().equals(testData.a4Id)) {
                // valid
            } else {
                fail("Invalid assignment returned for STUDENT2 via getViewableAssignments");
            }
        }

        // switch to student 3
        // not a member of any groups
        externalLogic.setCurrentUserId(AssignmentTestDataLoad.STUDENT3_UID);
        // should return 2 and 3
        assignList = assignment2Service.getAssignments(AssignmentTestDataLoad.CONTEXT_ID);
        assertNotNull(assignList);
        assertTrue(assignList.size() == 2);
        // let's make sure that these are the right assign
        for (AssignmentDefinition assign : assignList) {
            if (assign.getId().equals(testData.a2Id)) {

            } else if (assign.getId().equals(testData.a3Id)) {

            } else {
                fail("Invalid assignment returned for STUDENT3 via getViewableAssignments");
            }
        }
    }

}
