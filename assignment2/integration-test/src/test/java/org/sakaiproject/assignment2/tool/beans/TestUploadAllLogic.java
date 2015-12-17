package org.sakaiproject.assignment2.tool.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.dao.AssignmentDao;
import org.sakaiproject.assignment2.logic.UploadAllLogic;
import org.sakaiproject.assignment2.logic.ZipExportLogic;
import org.sakaiproject.assignment2.model.UploadAllOptions;
import org.sakaiproject.assignment2.tool.beans.PreloadTestData;

public class TestUploadAllLogic extends SakaiTransactionalTestBase
{
	private static final Log log = LogFactory.getLog(TestUploadAllLogic.class);

	private UploadAllLogic updownLogic;
	private ZipExportLogic zipExporter;
	private UploadAllOptions options;
	private AssignmentTestDataLoad testData;
	private AssignmentDao assignmentDao;

	public static Test suite()
	{
		TestSetup setup = new TestSetup(new TestSuite(TestUploadAllLogic.class))
		{
			@Override
			protected void setUp() throws Exception
			{
				if (log.isDebugEnabled())
					log.debug("starting setup");
				try
				{
					oneTimeSetup();
				}
				catch (Exception e)
				{
					log.warn(e);
				}
				if (log.isDebugEnabled())
					log.debug("finished setup");
			}

			@Override
			protected void tearDown() throws Exception
			{
				if (log.isDebugEnabled())
					log.debug("tearing down");
				oneTimeTearDown();
			}
		};
		return setup;
	}

	@Override
	protected void onSetUpBeforeTransaction() throws Exception
	{
		updownLogic = (UploadAllLogic) getService("org.sakaiproject.assignment2.logic.UploadDownloadLogic");
		zipExporter = (ZipExportLogic) getService("org.sakaiproject.assignment2.logic.ZipExportLogic");
		// setContentHostingService(ContentHostingService)
		// setExternalLogic(ExternalLogic)
		options = new UploadAllOptions();
		assignmentDao = (AssignmentDao) getService("org.sakaiproject.assignment2.dao.AssignmentDao");
	}

	@Override
	protected void onSetUpInTransaction() throws Exception
	{
		PreloadTestData ptd = new PreloadTestData();
		// if (ptd == null) {
		// throw new NullPointerException("PreloadTestData could not be retrieved from spring");
		// }
		ptd.setAssignmentDao(assignmentDao);
		ptd.init();
		testData = ptd.getAtdl();
	}

	public void testUploadAll() throws Exception
	{
		Long assignmentId = testData.a1Id;
		options.assignmentId = assignmentId;
		File f = File.createTempFile("assn" + assignmentId, "zip");
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(f));
		zipExporter.getSubmissionsZip(zos, assignmentId);
		zos.flush();
		zos.close();
		updownLogic.uploadAll(options, f);
	}
}