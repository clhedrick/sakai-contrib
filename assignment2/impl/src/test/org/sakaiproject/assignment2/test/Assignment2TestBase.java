/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/impl/src/test/org/sakaiproject/assignment2/test/Assignment2TestBase.java $
 * $Id: Assignment2TestBase.java 69975 2010-08-27 15:03:46Z wagnermr@iupui.edu $
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
package org.sakaiproject.assignment2.test;


import org.sakaiproject.assignment2.dao.AssignmentDao;
import org.sakaiproject.assignment2.logic.AssignmentBundleLogic;
import org.sakaiproject.assignment2.logic.ExternalAnnouncementLogic;
import org.sakaiproject.assignment2.logic.impl.AssignmentLogicImpl;
import org.sakaiproject.assignment2.logic.impl.AssignmentPermissionLogicImpl;
import org.sakaiproject.assignment2.logic.impl.AssignmentSubmissionLogicImpl;
import org.sakaiproject.assignment2.logic.impl.ExternalAnnouncementLogicImpl;
import org.sakaiproject.assignment2.logic.impl.ExternalContentReviewLogicImpl;
import org.sakaiproject.assignment2.logic.impl.ExternalGradebookLogicImpl;
import org.sakaiproject.assignment2.logic.test.stubs.AssignmentAuthzLogicStub;
import org.sakaiproject.assignment2.logic.test.stubs.ExternalEventLogicStub;
import org.sakaiproject.assignment2.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.assignment2.logic.test.stubs.GradebookServiceStub;
import org.sakaiproject.assignment2.service.impl.Assignment2ServiceImpl;
import org.sakaiproject.assignment2.test.AssignmentTestDataLoad;
import org.sakaiproject.assignment2.test.PreloadTestData;
import org.springframework.test.AbstractTransactionalSpringContextTests;


/**
 * Base class for assignment test classes that provides the spring application
 * context.  The database used is an in-memory hsqldb by default, but this can
 * be overridden to test specific database configurations by setting the "mem"
 * system property to "false".  In the "mem=false" case, the database configuration
 * is set in the hibernate.properties file in the "hibernate.properties.dir" directory.
 *
 */
public abstract class Assignment2TestBase extends AbstractTransactionalSpringContextTests {
    protected AssignmentDao dao;
    protected AssignmentTestDataLoad testData;

    protected ExternalAnnouncementLogic announcementLogic;

    protected ExternalGradebookLogicImpl gradebookLogic;
    protected AssignmentLogicImpl assignmentLogic;
    protected AssignmentSubmissionLogicImpl submissionLogic;
    protected AssignmentPermissionLogicImpl permissionLogic;
    protected AssignmentBundleLogic bundleLogic;
    protected ExternalContentReviewLogicImpl contentReviewLogic;
    protected GradebookServiceStub gradebookService;
    protected ExternalLogicStub externalLogic;
    protected Assignment2ServiceImpl assignment2Service;
    protected AssignmentAuthzLogicStub authz;
    protected ExternalEventLogicStub eventLogic;

    protected void onSetUpBeforeTransaction() throws Exception {

    }
    protected void onSetUpInTransaction() throws Exception {
        PreloadTestData ptd = (PreloadTestData) applicationContext.getBean("org.sakaiproject.assignment2.test.PreloadTestData");
        if (ptd == null) {
            throw new NullPointerException("PreloadTestData could not be retrieved from spring");
        }

        testData = ptd.getAtdl();

        dao = (AssignmentDao)applicationContext.getBean("org.sakaiproject.assignment2.dao.AssignmentDao");
        if (dao == null) {
            throw new NullPointerException(
            "DAO could not be retrieved from spring");
        }

        bundleLogic = (AssignmentBundleLogic)applicationContext.getBean("org.sakaiproject.assignment2.logic.AssignmentBundleLogic");
        if (bundleLogic == null) {
            throw new NullPointerException(
            "bundleLogic could not be retrieved from spring");
        } 

        externalLogic = new ExternalLogicStub();
        
        authz = new AssignmentAuthzLogicStub();
        authz.setExternalLogic(externalLogic);
        
        eventLogic = new ExternalEventLogicStub();

        gradebookService = new GradebookServiceStub();
        gradebookService.setExternalLogic(externalLogic);

        gradebookLogic = new ExternalGradebookLogicImpl();
        gradebookLogic.setGradebookService(gradebookService);
        gradebookLogic.setExternalLogic(externalLogic);
        gradebookLogic.setAssignmentAuthzLogic(authz);

        announcementLogic = new ExternalAnnouncementLogicImpl();
        
        contentReviewLogic = new ExternalContentReviewLogicImpl();
        
        permissionLogic = new AssignmentPermissionLogicImpl();
        permissionLogic.setDao(dao);
        permissionLogic.setExternalLogic(externalLogic);
        permissionLogic.setAssignmentAuthzLogic(authz);

        assignmentLogic = new AssignmentLogicImpl();
        assignmentLogic.setDao(dao);
        assignmentLogic.setExternalAnnouncementLogic(announcementLogic);
        assignmentLogic.setExternalGradebookLogic(gradebookLogic);
        assignmentLogic.setExternalLogic(externalLogic);
        assignmentLogic.setPermissionLogic(permissionLogic);
        assignmentLogic.setAssignmentBundleLogic(bundleLogic);
        assignmentLogic.setExternalContentReviewLogic(contentReviewLogic);
        assignmentLogic.setExternalEventLogic(eventLogic);


        submissionLogic = new AssignmentSubmissionLogicImpl();
        submissionLogic.setDao(dao);
        submissionLogic.setExternalGradebookLogic(gradebookLogic);
        submissionLogic.setExternalLogic(externalLogic);
        submissionLogic.setPermissionLogic(permissionLogic);
        submissionLogic.setAssignmentLogic(assignmentLogic);
        submissionLogic.setExternalEventLogic(eventLogic);

        assignment2Service = new Assignment2ServiceImpl();
        assignment2Service.setAssignmentLogic(assignmentLogic);
        assignment2Service.setAssignmentPermissionLogic(permissionLogic);
        assignment2Service.setExternalGradebookLogic(gradebookLogic);
        assignment2Service.setExternalLogic(externalLogic);
    }

    /**
     * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
     */
    protected String[] getConfigLocations() {
        // point to the needed spring config files, must be on the classpath
        // (add component/src/webapp/WEB-INF to the build path in Eclipse),
        // they also need to be referenced in the pom.xml file
        return new String[] { "hibernate-test.xml", "spring-hibernate.xml",
                "logic-beans-test.xml"

        };

    }

}
