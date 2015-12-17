/**********************************************************************************
*
* $Id: PermissionDeleteCellRenderer.java 65847 2010-01-26 01:22:22Z jlrenfro@ucdavis.edu $
*
***********************************************************************************
*
* Copyright (c) 2008, 2009 The Regents of the University of California
*
* Licensed under the
* Educational Community License, Version 2.0 (the "License"); you may
* not use this file except in compliance with the License. You may
* obtain a copy of the License at
* 
* http://www.osedu.org/licenses/ECL-2.0
* 
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS"
* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
* or implied. See the License for the specific language governing
* permissions and limitations under the License.
*
**********************************************************************************/

package org.sakaiproject.gradebook.gwt.client.gxt.view.components;

import org.sakaiproject.gradebook.gwt.client.AppConstants;
import org.sakaiproject.gradebook.gwt.client.resource.GradebookResources;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class PermissionDeleteCellRenderer implements GridCellRenderer<ModelData> {

	private GradebookResources resources;
	
	public PermissionDeleteCellRenderer() {
		this.resources = Registry.get(AppConstants.RESOURCES);
	}
	
	public String render(ModelData model, String property, 
			ColumnData config, int rowIndex, int colIndex, 
			ListStore<ModelData> store, Grid<ModelData> grid) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<span class=\"").append(resources.css().gbCellClickable()).append("\">");
		stringBuilder.append(model.get(property));
		stringBuilder.append("</span>");
		return stringBuilder.toString();
	}
}
