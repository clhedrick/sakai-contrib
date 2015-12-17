package org.sakaiproject.gradebook.gwt.test.mock;

import org.sakaiproject.gradebook.gwt.client.action.PageRequestAction;
import org.sakaiproject.gradebook.gwt.client.action.UserEntityUpdateAction;
import org.sakaiproject.gradebook.gwt.client.action.Action.EntityType;
import org.sakaiproject.gradebook.gwt.client.action.UserEntityAction.ClassType;
import org.sakaiproject.gradebook.gwt.client.exceptions.FatalException;
import org.sakaiproject.gradebook.gwt.client.exceptions.InvalidInputException;
import org.sakaiproject.gradebook.gwt.client.gxt.multigrade.MultiGradeLoadConfig;
import org.sakaiproject.gradebook.gwt.client.model.StudentModel;
import org.sakaiproject.gradebook.gwt.client.model.GradebookModel.CategoryType;
import org.sakaiproject.gradebook.gwt.client.model.GradebookModel.GradeType;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public abstract class AbstractToolFacadePercentageTest extends AbstractToolFacadeTest {

	protected void initialize(CategoryType categoryType) throws InvalidInputException, FatalException {
		initialize(categoryType, GradeType.PERCENTAGES);
	}
	
	/*
	 * If a user enters negative scores for a learner when in percentages mode, an exception
	 * should be thrown
	 */
	public void testNegativeScores() throws FatalException {
		boolean isThrown = false;
		try {
			gradeAllSameScore(ScoreType.NEGATIVE, null);
			// Should not get to this line
			assertTrue(false);
		} catch (InvalidInputException e) {
			isThrown = true;
		}
		assertTrue(isThrown);
	}
	
	/*
	 * Creates a grade that will produce different scores in different configs
	 */
	protected void setRepresentativePercentagesGrade() throws InvalidInputException, FatalException {
		PagingLoadConfig loadConfig = new MultiGradeLoadConfig();
		loadConfig.setOffset(0);
		loadConfig.setLimit(20);
		PagingLoadResult<StudentModel> learnerResult = facade.getEntityPage(new PageRequestAction(EntityType.STUDENT, "emptyid", Long.valueOf(1l)), loadConfig);
		
		for (StudentModel learner : learnerResult.getData()) {
			
			facade.updateEntity(new UserEntityUpdateAction<StudentModel>(gbModel, learner, essay1.getIdentifier(), ClassType.DOUBLE, Double.valueOf(100d), null));
			facade.updateEntity(new UserEntityUpdateAction<StudentModel>(gbModel, learner, essay2.getIdentifier(), ClassType.DOUBLE, Double.valueOf(75d), null));
			facade.updateEntity(new UserEntityUpdateAction<StudentModel>(gbModel, learner, essay3.getIdentifier(), ClassType.DOUBLE, Double.valueOf(25d), null));
			
			facade.updateEntity(new UserEntityUpdateAction<StudentModel>(gbModel, learner, hw1.getIdentifier(), ClassType.DOUBLE, Double.valueOf(100d), null));
			facade.updateEntity(new UserEntityUpdateAction<StudentModel>(gbModel, learner, hw2.getIdentifier(), ClassType.DOUBLE, Double.valueOf(100d), null));
			facade.updateEntity(new UserEntityUpdateAction<StudentModel>(gbModel, learner, hw3.getIdentifier(), ClassType.DOUBLE, Double.valueOf(80d), null));
			facade.updateEntity(new UserEntityUpdateAction<StudentModel>(gbModel, learner, hw4.getIdentifier(), ClassType.DOUBLE, Double.valueOf(20d), null));
		}
	}
	

}
