/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/TaggableHelperViewParams.java $
 * $Id: TaggableHelperViewParams.java 87902 2015-12-14 18:01:30Z hedrick@rutgers.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2008 The Sakai Foundation.
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
package org.sakaiproject.assignment2.tool.params;

import org.sakaiproject.rsf.helper.HelperViewParameters;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class TaggableHelperViewParams extends SimpleViewParameters // ASNN-521HelperViewParameters
{
    public String helperId;
    public String[] values;	
    public String[] keys;

    public TaggableHelperViewParams() {}

    public TaggableHelperViewParams(String viewId) {
        super(viewId);
    }

    public TaggableHelperViewParams(String viewId, String helperId) {
        super(viewId);
        this.helperId = helperId;
    }

    public TaggableHelperViewParams(String viewId, String helperId, String[] keys, String[] values) {
        super(viewId);
        this.helperId = helperId;
        this.keys = keys;
        this.values = values;
    }


}
