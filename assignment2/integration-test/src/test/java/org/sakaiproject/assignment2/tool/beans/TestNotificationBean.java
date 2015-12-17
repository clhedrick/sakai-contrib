package org.sakaiproject.assignment2.tool.beans;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.test.SakaiTestBase;

public class TestNotificationBean extends SakaiTestBase
{
	private static final Log log = LogFactory.getLog(TestNotificationBean.class);

	public static Test suite()
	{
		TestSetup setup = new TestSetup(new TestSuite(TestNotificationBean.class))
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
	public void setUp() throws Exception
	{
		log.info("Setting up test case...");
	}

	@Override
	public void tearDown() throws Exception
	{
		log.info("Tearing down test case...");
	}

	public void testSomething() throws Exception
	{
		
	}
}