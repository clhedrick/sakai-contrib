package org.sakaiproject.assignment2.tool.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.test.SakaiTestBase;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class SakaiTransactionalTestBase extends SakaiTestBase
{
	protected Log logger = LogFactory.getLog(SakaiTransactionalTestBase.class);

	/** The transaction manager to use */
	protected PlatformTransactionManager transactionManager;

	/** Should we roll back by default? */
	private boolean defaultRollback = true;

	/** Should we commit the current transaction? */
	private boolean complete = false;

	/** Number of transactions started */
	private int transactionsStarted = 0;

	/**
	 * Transaction definition used by this test class: by default, a plain
	 * DefaultTransactionDefinition. Subclasses can change this to cause different behavior.
	 */
	protected TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();

	/**
	 * TransactionStatus for this test. Typical subclasses won't need to use it.
	 */
	protected TransactionStatus transactionStatus;

	/**
	 * Default constructor for SakaiTransactionalTestBase.
	 */
	public SakaiTransactionalTestBase()
	{
	}

	/**
	 * Constructor for SakaiTransactionalTestBase with a JUnit name.
	 */
	public SakaiTransactionalTestBase(String name)
	{
		// super(name);
	}

	/**
	 * Specify the transaction manager to use. No transaction management will be available if this
	 * is not set. Populated through dependency injection by the superclass.
	 * <p>
	 * This mode works only if dependency checking is turned off in the
	 * {@link AbstractDependencyInjectionSpringContextTests} superclass.
	 */
	public void setTransactionManager(PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	/**
	 * Subclasses can set this value in their constructor to change default, which is always to roll
	 * the transaction back.
	 */
	public void setDefaultRollback(boolean defaultRollback)
	{
		this.defaultRollback = defaultRollback;
	}

	/**
	 * Call this method in an overridden {@link #runBare()} method to prevent transactional
	 * execution.
	 */
	protected void preventTransaction()
	{
		this.transactionDefinition = null;
	}

	/**
	 * Call this method in an overridden {@link #runBare()} method to override the transaction
	 * attributes that will be used, so that {@link #setUp()} and {@link #tearDown()} behavior is
	 * modified.
	 * 
	 * @param customDefinition
	 *            the custom transaction definition
	 */
	protected void setTransactionDefinition(TransactionDefinition customDefinition)
	{
		this.transactionDefinition = customDefinition;
	}

	/**
	 * This implementation is final. Override <code>onSetUp</code> for custom behavior.
	 * 
	 * This implementation creates a transaction before test execution.
	 * <p>
	 * Override {@link #onSetUpBeforeTransaction()} and/or {@link #onSetUpInTransaction()} to add
	 * custom set-up behavior for transactional execution. Alternatively, override this method for
	 * general set-up behavior, calling <code>super.onSetUp()</code> as part of your method
	 * implementation.
	 * 
	 * @throws Exception
	 *             simply let any exception propagate
	 * @see #onTearDown()
	 * @see #onSetUp()
	 */
	@Override
	protected final void setUp() throws Exception
	{
		complete = !defaultRollback;

		if (transactionManager == null)
			transactionManager = (PlatformTransactionManager) getService("org.sakaiproject.springframework.orm.hibernate.GlobalTransactionManager");

		if (transactionManager == null)
		{
			logger.info("No transaction manager set: test will NOT run within a transaction");
		}
		else if (transactionDefinition == null)
		{
			logger.info("No transaction definition set: test will NOT run within a transaction");
		}
		else
		{
			onSetUpBeforeTransaction();
			startNewTransaction();
			try
			{
				onSetUpInTransaction();
			}
			catch (Exception ex)
			{
				endTransaction();
				throw ex;
			}
		}
	}

	/**
	 * Subclasses can override this method to perform any setup operations, such as populating a
	 * database table, <i>before</i> the transaction created by this class. Only invoked if there
	 * <i>is</i> a transaction: that is, if {@link #preventTransaction()} has not been invoked in
	 * an overridden {@link #runTest()} method.
	 * 
	 * @throws Exception
	 *             simply let any exception propagate
	 */
	protected void onSetUpBeforeTransaction() throws Exception
	{
	}

	/**
	 * Subclasses can override this method to perform any setup operations, such as populating a
	 * database table, <i>within</i> the transaction created by this class.
	 * <p>
	 * <b>NB:</b> Not called if there is no transaction management, due to no transaction manager
	 * being provided in the context.
	 * <p>
	 * If any {@link Throwable} is thrown, the transaction that has been started prior to the
	 * execution of this method will be {@link #endTransaction() ended} (or rather an attempt will
	 * be made to {@link #endTransaction() end it gracefully}); The offending {@link Throwable}
	 * will then be rethrown.
	 * 
	 * @throws Exception
	 *             simply let any exception propagate
	 */
	protected void onSetUpInTransaction() throws Exception
	{
	}

	/**
	 * This implementation is final. Override <code>onTearDown</code> for custom behavior.
	 * 
	 * This implementation ends the transaction after test execution.
	 * <p>
	 * Override {@link #onTearDownInTransaction()} and/or {@link #onTearDownAfterTransaction()} to
	 * add custom tear-down behavior for transactional execution. Alternatively, override this
	 * method for general tear-down behavior, calling <code>super.onTearDown()</code> as part of
	 * your method implementation.
	 * <p>
	 * Note that {@link #onTearDownInTransaction()} will only be called if a transaction is still
	 * active at the time of the test shutdown. In particular, it will <i>not</i> be called if the
	 * transaction has been completed with an explicit {@link #endTransaction()} call before.
	 * 
	 * @throws Exception
	 *             simply let any exception propagate
	 * @see #onSetUp()
	 * @see #onTearDown()
	 */
	@Override
	protected final void tearDown() throws Exception
	{
		// Call onTearDownInTransaction and end transaction if the transaction is still active.
		if (transactionStatus != null && !transactionStatus.isCompleted())
		{
			try
			{
				onTearDownInTransaction();
			}
			finally
			{
				endTransaction();
			}
		}
		// Call onTearDownAfterTransaction if there was at least one transaction,
		// even if it has been completed early through an endTransaction() call.
		if (transactionsStarted > 0)
		{
			onTearDownAfterTransaction();
		}
	}

	/**
	 * Subclasses can override this method to run invariant tests here. The transaction is <i>still
	 * active</i> at this point, so any changes made in the transaction will still be visible.
	 * However, there is no need to clean up the database, as a rollback will follow automatically.
	 * <p>
	 * <b>NB:</b> Not called if there is no actual transaction, for example due to no transaction
	 * manager being provided in the application context.
	 * 
	 * @throws Exception
	 *             simply let any exception propagate
	 */
	protected void onTearDownInTransaction() throws Exception
	{
	}

	/**
	 * Subclasses can override this method to perform cleanup after a transaction here. At this
	 * point, the transaction is <i>not active anymore</i>.
	 * 
	 * @throws Exception
	 *             simply let any exception propagate
	 */
	protected void onTearDownAfterTransaction() throws Exception
	{
	}

	/**
	 * Cause the transaction to commit for this test method, even if default is set to rollback.
	 * 
	 * @throws IllegalStateException
	 *             if the operation cannot be set to complete as no transaction manager was provided
	 */
	protected void setComplete()
	{
		if (transactionManager == null)
		{
			throw new IllegalStateException("No transaction manager set");
		}
		complete = true;
	}

	/**
	 * Immediately force a commit or rollback of the transaction, according to the complete flag.
	 * <p>
	 * Can be used to explicitly let the transaction end early, for example to check whether lazy
	 * associations of persistent objects work outside of a transaction (that is, have been
	 * initialized properly).
	 * 
	 * @see #setComplete()
	 */
	protected void endTransaction()
	{
		if (transactionStatus != null)
		{
			try
			{
				if (!complete)
				{
					transactionManager.rollback(transactionStatus);
					logger.info("Rolled back transaction after test execution");
				}
				else
				{
					transactionManager.commit(transactionStatus);
					logger.info("Committed transaction after test execution");
				}
			}
			finally
			{
				transactionStatus = null;
			}
		}
	}

	/**
	 * Start a new transaction. Only call this method if {@link #endTransaction()} has been called.
	 * {@link #setComplete()} can be used again in the new transaction. The fate of the new
	 * transaction, by default, will be the usual rollback.
	 * 
	 * @throws TransactionException
	 *             if starting the transaction failed
	 */
	protected void startNewTransaction() throws TransactionException
	{
		if (transactionStatus != null)
		{
			throw new IllegalStateException(
					"Cannot start new transaction without ending existing transaction: "
							+ "Invoke endTransaction() before startNewTransaction()");
		}
		if (transactionManager == null)
		{
			throw new IllegalStateException("No transaction manager set");
		}

		transactionStatus = transactionManager.getTransaction(transactionDefinition);
		++transactionsStarted;
		complete = !defaultRollback;

		if (logger.isInfoEnabled())
		{
			logger.info("Began transaction (" + transactionsStarted + "): transaction manager ["
					+ transactionManager + "]; default rollback = " + defaultRollback);
		}
	}
}