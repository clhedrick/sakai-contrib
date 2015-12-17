/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/CategoryDaoGeneric.java $ 
 * $Id: CategoryDaoGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011, 2012 Etudes, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 ***********************************************************************************/
package org.etudes.component.app.jforum.dao.generic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumCategoryForumsExistingException;
import org.etudes.api.app.jforum.dao.CategoryDao;
import org.etudes.api.app.jforum.dao.ForumDao;
import org.etudes.api.app.jforum.dao.GradeDao;
import org.etudes.component.app.jforum.AccessDatesImpl;
import org.etudes.component.app.jforum.CategoryImpl;
import org.etudes.component.app.jforum.ForumImpl;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;

public abstract class CategoryDaoGeneric implements CategoryDao
{
	private static Log logger = LogFactory.getLog(CategoryDaoGeneric.class);
	
	/** Dependency: ForumDao */
	protected ForumDao forumDao = null;
	
	/** Dependency: GradeDao */
	protected GradeDao gradeDao = null;
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;	
	
	/**
	 * {@inheritDoc}
	 */
	public int addNew(Category category)
	{
		if ((category == null) || (category.getTitle() == null) || (category.getTitle().trim().length() == 0) ||  (category.getContext()== null))
		{
			throw new IllegalArgumentException("Category information is missing");
		}
		
		if (category.getId() > 0)
		{
			throw new IllegalArgumentException("New category cannot be created as category has id.");
		}
		
		return creatNewCategoryTx(category);
	}

	/**
	 * {@inheritDoc}
	 */
	public void checkAndAddDefaultCategoriesForums(String courseId)
	{
		checkAndAddDefaultCategoriesForumsTx(courseId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void delete(int categoryId) throws JForumCategoryForumsExistingException
	{
		Category category = selectById(categoryId);
		
		if (category == null)
		{
			return;
		}
		
		// if category have forums don't allow to delete category
		if (forumDao.selectByCategoryId(categoryId).size() > 0)
		{
			throw new JForumCategoryForumsExistingException(category.getTitle());
		}
		
		deleteTx(category);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> selectAllByCourse(String courseId)
	{
		if (logger.isDebugEnabled()) 
		{
			logger.debug("selectAllByCourse - courseId:"+ courseId);
		}
		
		//check if site is initialized and created default categories and forums. Moved this call to JForumCategoryServiceImpl
		//checkAndAddDefaultCategoriesForumsTx(courseId);
		
		StringBuilder sql;
		Object[] fields;
		int i;
			
		// all site categories
		sql = new StringBuilder();
		sql.append("SELECT c.categories_id, c.title, c.display_order, c.moderated, c.archived, c.gradable, ");
		// 08/16/2012 - lock on due is not supported anymore - sql.append("c.start_date, c.end_date, c.lock_end_date, c.hide_until_open, c.allow_until_date, cc.course_id ");
		sql.append("c.start_date, c.end_date, c.hide_until_open, c.allow_until_date, cc.course_id ");
		sql.append("FROM jforum_sakai_course_categories cc, jforum_categories c ");
		sql.append("WHERE cc.course_id = ? AND cc.categories_id = c.categories_id ORDER BY c.display_order");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = courseId;		
		
		List<Category> categories = readCategories(sql.toString(), fields);
		Map<Integer, Category> categoryMap = new HashMap<Integer, Category>();
		
		for(Category category : categories)
		{
			// grade
			if (category.isGradable())
			{
				Grade grade = gradeDao.selectByCategory(category.getId());
				category.setGrade(grade);
			}
			categoryMap.put(Integer.valueOf(category.getId()), category);
		}
		
		// category forums
		List<Forum> courseForums = forumDao.selectAllByCourse(courseId);
		
		for (Forum forum : courseForums)
		{
			Category category = (Category) categoryMap.get(forum.getCategoryId());

			if (category != null)
			{
				((ForumImpl)forum).setCategory(category);
				category.getForums().add(forum);
			}
		}
		
		return categories;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category selectById(int categoryId)
	{
		Category category = null;
		
		StringBuilder sql;
		Object[] fields;
		int i;
		
		sql = new StringBuilder();
		sql.append("SELECT c.categories_id, c.title, c.display_order, c.moderated, c.archived, c.gradable, ");
		// 08/16/2012 - lock on due is not supported anymore - sql.append("c.start_date, c.end_date, c.lock_end_date, c.hide_until_open, c.allow_until_date, cc.course_id ");
		sql.append("c.start_date, c.end_date, c.hide_until_open, c.allow_until_date, cc.course_id ");
		sql.append("FROM jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE c.categories_id = cc.categories_id AND c.categories_id = ?");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = categoryId;
		
		List<Category> categories = readCategories(sql.toString(), fields);
		
		if (categories.size() == 1)
		{
			category =  categories.get(0);
			
			// grade
			if (category.isGradable())
			{
				Grade grade = gradeDao.selectByCategory(category.getId());
				category.setGrade(grade);
			}
		}
		
		return category;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectCategoryForumsCount(int categoryId)
	{

		String sql = "SELECT COUNT(1) AS total FROM jforum_forums WHERE categories_id = ?";
		
		int i = 0; 
		Object[] fields = new Object[1];
		fields[i++] = categoryId;
		
		final List<Integer> forumsCountList = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					
					forumsCountList.add(result.getInt("total"));
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectCategoryForumsCount: " + e, e);
					}					
				}
				return null;
			}
		});
		
		int forumsCount = 0;
		if (forumsCountList.size() == 1)
		{
			forumsCount = forumsCountList.get(0).intValue();
		}
		
		return forumsCount;
	
	}
	
	/**
	 * @param forumDao 
	 * 				The forumDao to set
	 */
	public void setForumDao(ForumDao forumDao)
	{
		this.forumDao = forumDao;
	}
	
	/**
	 * @param gradeDao 
	 * 			The gradeDao to set
	 */
	public void setGradeDao(GradeDao gradeDao)
	{
		this.gradeDao = gradeDao;
	}
	
	/**
	 * @param sqlService
	 *          The sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void update(Category category)
	{
		if ((category == null) || (category.getTitle() == null) || (category.getTitle().trim().length() == 0))
		{
			throw new IllegalArgumentException("category information is missing");
		}
		
		if (category.getId() <= 0)
		{
			throw new IllegalArgumentException("Category cannot be edited as category information is missing.");
		}
		
		if (category.getTitle() != null && category.getTitle().length() > 100)
		{
			category.setTitle(category.getTitle().substring(0, 99));
		}
		
		// check for existing category
		Category exisCategory = selectById(category.getId());
		
		if (exisCategory == null)
		{
			return;
		}
		
		updateTx(category);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category updateOrder(Category category, Category other)
	{
		if (!category.getContext().equalsIgnoreCase(other.getContext()))
		{
			return null;
		}
		
		return updateOrderTx(category, other);
		
	}
	
	/**
	 * Adds category to the site
	 * 
	 * @param siteId	Site id
	 * 
	 * @param categoryId	Category id
	 */
	protected void addCategoryToSite(String siteId, int categoryId)
	{
		String sql = "INSERT INTO jforum_sakai_course_categories (course_id, categories_id) VALUES (?, ?)";
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] = siteId;
		fields[i++] = categoryId;
		
		try
		{
			this.sqlService.dbWrite(sql, fields);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
			
			throw new RuntimeException("addCategoryToSite: dbInsert failed");
		}
	}
	
	/**
	 * Adds default categories and forums to the site
	 * 
	 * @param courseId	The site or course id
	 */
	protected void addDefaultCategoriesForums(String courseId)
	{
		int categoryId;
		
		// TODO: get the data from the config file
		
		/*category Main*/
		CategoryImpl categoryMain = new CategoryImpl();
		categoryMain.setTitle("Main");
		categoryMain.setContext(courseId);
		categoryId = addNew(categoryMain);
		categoryMain.setId(categoryId);
		
		// forum Questions
		ForumImpl forum = new ForumImpl();
		forum.setName("Questions");
		forum.setDescription("Do you have any questions about something in this course? Use this forum to ask. Contribute a reply!");
		forum.setCategoryId(categoryMain.getId());
		forumDao.addNew(forum);
		
		// forum "Class Discussions"
		forum = new ForumImpl();
		forum.setName("Class Discussions");
		forum.setDescription("Use this forum to participate in class discussions.");
		forum.setCategoryId(categoryMain.getId());
		forumDao.addNew(forum);
		
		/*category Other*/
		CategoryImpl categoryOther = new CategoryImpl();
		categoryOther.setTitle("Other");
		categoryOther.setContext(courseId);
		categoryId = addNew(categoryOther);
		categoryOther.setId(categoryId);
		
		// forum "Student Lounge"
		forum = new ForumImpl();
		forum.setName("Student Lounge");
		forum.setDescription("Use this forum for other questions/topics amongst yourselves.");
		forum.setCategoryId(categoryOther.getId());
		forumDao.addNew(forum);
	}
	
	/**
	 * Checks and default categories and forums to the site
	 * 
	 * @param courseId	Course or site id
	 */
	protected void checkAndAddDefaultCategoriesForumsTx(final String courseId)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					String sql = "SELECT course_id, init_status FROM jforum_sakai_course_initvalues WHERE course_id = ?";
					Object[] fields = new Object[1];
					int i = 0;
					fields[i++] = courseId;
					
					final Map<String, Integer> courseInitStatus =  new HashMap<String, Integer>();
					sqlService.dbRead(sql, fields, new SqlReader()
					{
						public Object readSqlResultRecord(ResultSet result)
						{
							try
							{
								courseInitStatus.put(result.getString("course_id"), result.getInt("init_status"));
							}
							catch (SQLException e)
							{
								if (logger.isWarnEnabled())
								{
									logger.warn("checkAndAddDefaultCategoriesForums().fetch course init status: " + e, e);
								}
							}
							
							return null;
						}
					});
					
					if (courseInitStatus.size() == 0)
					{
						// check for any existing categories and if categories are existing initialize site
						if (selectCategoryCount(courseId) > 0)
						{
							initializeSite(courseId);
						}
						else
						{
							addDefaultCategoriesForums(courseId);
							initializeSite(courseId);
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while creating new default categories and forums.", e);
				}
			}
		}, "checkAndAddDefaultCategoriesForumsTx: " + courseId);
	}
	
	/**
	 * Creates new category
	 *  
	 * @param category Category with dates(optional) and grade information(optional)
	 * 
	 * @return	Newly created category id
	 */
	protected int creatNewCategoryTx(final Category category)
	{	
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					/*create new category*/
					int categoryId = insertCategory(category);
					((CategoryImpl)category).setId(categoryId);
					
					// create grade if category is gradable.
					if ((category.isGradable()) && (category.getGrade() != null))
					{
						category.getGrade().setContext(category.getContext());
						category.getGrade().setType(Grade.GradeType.CATEGORY.getType());
						
						category.getGrade().setCategoryId(category.getId());
						category.getGrade().setForumId(0);
						category.getGrade().setTopicId(0);
						
						gradeDao.addNew(category.getGrade());
					}
					
					// add category to site
					addCategoryToSite(category.getContext(), category.getId());
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					// remove any external entries like entry in grade book if forum is gradable if created
					throw new RuntimeException("Error while creating new category.", e);
				}
			}
		}, "creatNewCategory: " + category.getTitle());
		
		
		return category.getId();
	}
	
	/**
	 * Deletes the category and grade if the category is gradabable category
	 * 
	 * @param category	Category
	 */
	protected void deleteTx(final Category category)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					// gradable category - delete category grade
					if (category.isGradable())
					{
						Grade grade = category.getGrade();
						
						if (grade != null)
						{
							//delete grade
							gradeDao.delete(grade.getId());
						}
					}
					
					// delete course category
					String sql = "DELETE FROM jforum_sakai_course_categories WHERE categories_id = ?";
					
					Object[] fields = new Object[1];
					int i = 0;
					fields[i++] =  category.getId();
					
					sqlService.dbWrite(sql, fields);
					
					// delete category
					sql = "DELETE FROM jforum_categories WHERE categories_id = ?";
					
					fields = new Object[1];
					i = 0;
					fields[i++] =  category.getId();
					
					sqlService.dbWrite(sql, fields);
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}

					throw new RuntimeException("Error while deleting category.", e);
				}
			}
		}, "delete: " + category.getTitle());
			
	}
	
	/**
	 * Creates the category object from the result set
	 * 
	 * @param result	The result set
	 * 
	 * @return			The category object
	 * 
	 * @throws SQLException
	 */
	protected Category fillCategory(ResultSet result) throws SQLException
	{
		CategoryImpl category = new CategoryImpl();
		category.setId(result.getInt("categories_id"));
		category.setTitle(result.getString("title"));							
		category.setGradable(result.getInt("gradable") == 1);
		
		if ((result.getDate("start_date") != null) || (result.getDate("end_date") != null) || ((result.getDate("allow_until_date") != null)))
		{
			AccessDates accessDates = new AccessDatesImpl();
			if (result.getDate("start_date") != null)
		    {
				accessDates.setOpenDate(result.getTimestamp("start_date"));
				accessDates.setHideUntilOpen(result.getInt("hide_until_open") > 0);
		    }
			
			if (result.getDate("end_date") != null)
		    {
		      Timestamp endDate = result.getTimestamp("end_date");
		      accessDates.setDueDate(endDate);
		      // 08/16/2012 - lock on due is not supported anymore
		      //accessDates.setLocked(result.getInt("lock_end_date") > 0);
		    }
		    else
		    {
		    	accessDates.setDueDate(null);
		    }
			
			accessDates.setAllowUntilDate(result.getTimestamp("allow_until_date"));
			category.setAccessDates(accessDates);
		}
		else
		{
			AccessDates accessDates = new AccessDatesImpl();
			category.setAccessDates(accessDates);
		}
		
		category.setOrder(result.getInt("display_order"));
		category.setContext(result.getString("course_id"));
		
		return category;
	}
	
	/**
	 * gets the maximum display order of all the categories
	 * 
	 * @return	 Maximum display order of all the categories
	 */
	protected int getMaxDisplayOrder()
	{
		final List<Integer> displayOrderList =  new ArrayList<Integer>();
		
		String sql = "SELECT MAX(display_order) FROM jforum_categories";

		Object[] fields = null;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					displayOrderList.add(new Integer(result.getInt(1)));
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getMaxDisplayOrder: " + e, e);
					}					
				}
				return null;
			}
		});
		
		int maxDisplayOrder = -1;
		if (displayOrderList.size() == 1)
		{
			maxDisplayOrder = displayOrderList.get(0).intValue();
		}
		return maxDisplayOrder;
	}
	
	/**
	 * Initializes site to indicate default categories and forums are added to site
	 * 
	 * @param courseId
	 */
	protected void initializeSite(String courseId)
	{
		String sql = "INSERT INTO jforum_sakai_course_initvalues (course_id,init_status) VALUES (?, ?)";
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] = courseId;
		fields[i++] = 1;
		
		try
		{
			this.sqlService.dbWrite(sql, fields);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
			
			throw new RuntimeException("initializeSite: dbInsert failed");
		}
	}
	
	/**
	 * Create new category
	 * 
	 * @param category Category with dates(optional) and grade information(optional)
	 * 
	 * @return Newly created category id
	 */
	protected abstract int insertCategory(Category category);

	/**
	 * Gets the categories 
	 * 
	 * @param sql	Query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return	List of Categories
	 */
	protected List<Category> readCategories(String sql, Object[] fields)
	{
		if (sql == null) throw new IllegalArgumentException();
		if (fields == null) throw new IllegalArgumentException();
		
		final List<Category> categories =  new ArrayList<Category>();
		
		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Category category = fillCategory(result);
					
					categories.add(category);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readCategories: " + e, e);
					}
					return null;
				}
			}
		});
		
		return categories;
	}
	
	/**
	 * Updates the category
	 * 
	 * @param category	Category
	 */
	protected void updateCategory(Category category)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_categories SET title = ?, start_date = ?,  hide_until_open = ?, end_date = ?, allow_until_date = ?, gradable = ? WHERE categories_id = ?");
		
		Object[] fields = new Object[7];
		int i = 0;
		fields[i++] = category.getTitle();

		if (category.getAccessDates() != null)
		{
			if (category.getAccessDates().getOpenDate() == null)
			{
				fields[i++] = null;
				fields[i++] = 0;
			} 
			else
			{
				fields[i++] = new Timestamp(category.getAccessDates().getOpenDate().getTime());
				fields[i++] = category.getAccessDates().isHideUntilOpen() ? 1 : 0;
			}
	
			if (category.getAccessDates().getDueDate() == null)
			{
				fields[i++] = null;
				
				// 08/16/2012 - lock on due is not supported anymore
				//fields[i++] = 0;
			} 
			else
			{
				fields[i++] = new Timestamp(category.getAccessDates().getDueDate().getTime());
				
				// 08/16/2012 - lock on due is not supported anymore
				//fields[i++] = category.getAccessDates().isLocked() ? 1 : 0;
			}
			
			if (category.getAccessDates().getAllowUntilDate() == null)
			{
				fields[i++] = null;
			} 
			else
			{
				fields[i++] = new Timestamp(category.getAccessDates().getAllowUntilDate().getTime());
			}
		}
		else
		{
			fields[i++] = null;
			fields[i++] = 0;
			fields[i++] = null;
			//fields[i++] = 0;
			fields[i++] = null;
		}

		fields[i++] = category.isGradable() ? 1 : 0;
		
		fields[i++] = category.getId();
		
		try
		{
			this.sqlService.dbWrite(sql.toString(), fields);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
			
			throw new RuntimeException("updateCategory: dbWrite failed");
		}
		
	}
	
	/**
	 * Updates the category order and adjusts the order for the other categories that belong to same site
	 * 
	 * @param category	Category the order to be updated
	 * 
	 * @param related	Related category in the current position
	 * 
	 * @return		The updated category
	 */
	protected Category updateOrderTx(final Category category, final Category related)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					int curOrder = category.getOrder();
					int newOrder = related.getOrder();

					category.setOrder(newOrder);
							
					List<Category> categories = selectAllByCourse(category.getContext());
					
					if (curOrder < newOrder)
					{
						// move categories above and including existing category in new position one level up
						for (Category c : categories)
						{
							if ((c.getOrder() > curOrder) && (c.getOrder() <= newOrder))
							{
								String sql = "UPDATE jforum_categories SET display_order = ? WHERE categories_id = ?";
								
								Object[] fields = new Object[2];
								int i = 0;
								fields[i++] = c.getOrder() - 1;
								fields[i++] = c.getId();
								
								try
								{
									sqlService.dbWrite(sql, fields);
								}
								catch (Exception e)
								{
									if (logger.isErrorEnabled())
									{
										logger.error(e, e);
									}
									
									throw new RuntimeException("updateOrder: Other categories order update failed");
								}
							}						
						}
						
						// actual category that need to reset it's order
						String sql = "UPDATE jforum_categories SET display_order = ? WHERE categories_id = ?";
						
						Object[] fields = new Object[2];
						int i = 0;
						fields[i++] = category.getOrder();
						fields[i++] = category.getId();
						
						try
						{
							sqlService.dbWrite(sql, fields);
						}
						catch (Exception e)
						{
							if (logger.isErrorEnabled())
							{
								logger.error(e, e);
							}
							
							throw new RuntimeException("updateOrder: category order update failed");
						}
					}
					else if (curOrder > newOrder)
					{
						// move categories above and including existing category in new position one level down
						for (Category c : categories)
						{
							if ((c.getOrder() >= newOrder) && (c.getOrder() < curOrder))
							{
								String sql = "UPDATE jforum_categories SET display_order = ? WHERE categories_id = ?";
								
								Object[] fields = new Object[2];
								int i = 0;
								fields[i++] = c.getOrder() + 1;
								fields[i++] = c.getId();
								
								try
								{
									sqlService.dbWrite(sql, fields);
								}
								catch (Exception e)
								{
									if (logger.isErrorEnabled())
									{
										logger.error(e, e);
									}
									
									throw new RuntimeException("updateOrder: Other categories order update failed");
								}
							}
						}
						
						// actual category that need to reset it's order
						String sql = "UPDATE jforum_categories SET display_order = ? WHERE categories_id = ?";
						
						Object[] fields = new Object[2];
						int i = 0;
						fields[i++] = category.getOrder();
						fields[i++] = category.getId();
						
						try
						{
							sqlService.dbWrite(sql, fields);
						}
						catch (Exception e)
						{
							if (logger.isErrorEnabled())
							{
								logger.error(e, e);
							}
							
							throw new RuntimeException("updateOrder: category order update failed");
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}

					throw new RuntimeException("Error while updating category.", e);
				}
			}
		}, "updateOrder: " + category.getTitle());
		
		return selectById(category.getId());
	}
	
	/**
	 * Update category
	 * 
	 * @param category	Category
	 */
	protected void updateTx(final Category category)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					// update category
					updateCategory(category);
					
					// category grades
					if (category.isGradable())
					{
						if (category.getGrade() != null)
						{
							// existing grade
							Grade exisGrade = gradeDao.selectByCategory(category.getId());
							
							if (exisGrade == null)
							{
								category.getGrade().setType(Grade.GradeType.CATEGORY.getType());
								category.getGrade().setCategoryId(category.getId());
								category.getGrade().setContext(category.getContext());
								category.getGrade().setForumId(0);
								category.getGrade().setTopicId(0);
								
								gradeDao.addNew(category.getGrade());
							}
							else
							{
								exisGrade.setType(Grade.GradeType.CATEGORY.getType());
								exisGrade.setPoints(category.getGrade().getPoints());
								exisGrade.setContext(category.getGrade().getContext());
								exisGrade.setMinimumPostsRequired(category.getGrade().isMinimumPostsRequired());
								exisGrade.setMinimumPosts(category.getGrade().getMinimumPosts());
								exisGrade.setAddToGradeBook(category.getGrade().isAddToGradeBook());
								exisGrade.setForumId(0);
								exisGrade.setTopicId(0);
								
								gradeDao.updateCategoryGrade(exisGrade);								
							}
						}
						else
						{
							// remove category grade and update category grade type
							gradeDao.deleteCategoryGrade(category.getId());
							
							category.setGradable(Boolean.FALSE); 
							updateCategory(category);
						}
						
					}
					else
					{
						// remove any grade associated with category
						gradeDao.deleteCategoryGrade(category.getId());
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					// remove any external entries like entry in grade book if forum is gradable if created
					throw new RuntimeException("Error while updating category.", e);
				}
			}
		}, "updateTx: " + category.getTitle());
	}
	
	/**
	 * Gets the category count of the course
	 * 
	 * @param courseId	Course id or site id
	 * 
	 * @return	The count of categories of the course
	 */
	protected int selectCategoryCount(String courseId)
	{

		String sql = "SELECT COUNT(1) AS total FROM jforum_sakai_course_categories WHERE course_id = ?";
		
		int i = 0; 
		Object[] fields = new Object[1];
		fields[i++] = courseId;
		
		final List<Integer> categoryCountList = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					
					categoryCountList.add(result.getInt("total"));
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectCategoryCount: " + e, e);
					}					
				}
				return null;
			}
		});
		
		int categoryCount = 0;
		if (categoryCountList.size() == 1)
		{
			categoryCount = categoryCountList.get(0).intValue();
		}
		
		return categoryCount;
	
	}

}
