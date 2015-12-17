/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/PagerViewParams.java $
 * $Id: PagerViewParams.java 64148 2009-10-20 19:03:46Z swgithen@mtu.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 The Sakai Foundation.
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

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/**
 * TODO This probably doesn't work at the moment, and should likely be removed
 * completely.
 * 
 * @author sgithens
 *
 */
public class PagerViewParams extends SimpleViewParameters {

    public int current_start = 0;
    public int current_count = 5;

    public PagerViewParams() {}

    public PagerViewParams(String viewId) {
        super(viewId);
    }

    public PagerViewParams(String viewId, int currentStart, int currentCount){
        super(viewId);
        this.current_start = currentStart;
        this.current_count = currentCount;
    }

    public String getParseSpec() {
        // include a comma delimited list of the public properties in this class
        return super.getParseSpec() + ",current_start,current_count";
    }
}