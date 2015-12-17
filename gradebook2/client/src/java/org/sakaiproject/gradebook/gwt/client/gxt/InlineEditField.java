/**********************************************************************************
*
* $Id:$
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
package org.sakaiproject.gradebook.gwt.client.gxt;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.dom.client.KeyCodes;

public class InlineEditField<T> extends TextField<T> {

	public InlineEditField() {
		super();
	}
	
	@Override
	protected void onKeyPress(FieldEvent fe) {
		super.onKeyPress(fe);
		
		switch (fe.getEvent().getKeyCode()) {
		case KeyCodes.KEY_ENTER:
			complete();
			break;
		}
		
	}
	
	public void complete() {
		if (!GXT.isOpera && focusStyle != null) {
			getFocusEl().removeStyleName(focusStyle);
		}
		hasFocus = false;

		validate();

		if ((focusValue == null && getValue() != null)
				|| (focusValue != null && !focusValue
						.equals(getValue()))) {
			fireChangeEvent(focusValue, getValue());
		}
		fireEvent(Events.Blur, new FieldEvent(this));
	}
	
}
