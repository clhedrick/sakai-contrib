/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/params/ThickboxHelperViewParams.java $
 * $Id: ThickboxHelperViewParams.java 87902 2015-12-14 18:01:30Z hedrick@rutgers.edu $
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

import org.sakaiproject.rsf.helper.HelperViewParameters;

/**
 * These view parameters contain information for creating links that will
 * use the <a href="http://jquery.com/demo/thickbox/">Thickbox JQuery Plugin</a>
 * 
 * 
 * @author rjlowe
 * @author sgithens
 *
 */
public class ThickboxHelperViewParams extends HelperViewParameters  {


    /**
     * I am not sure what this does, but it's in the Thickbox iframe demo.
     */
    public Boolean keepThis;

    /**
     * When true tells thickbox to use an iframe for content.
     */
    public Boolean TB_iframe;

    /**
     * Height of the thickbox.
     */
    public int height;

    /**
     * Width of the iframe.
     */
    public int width;

    public ThickboxHelperViewParams() {}

    public ThickboxHelperViewParams(String viewId) {
        super(viewId);
    }

    public ThickboxHelperViewParams(String viewId, Boolean KeepThis, Boolean TB_iframe, int height, int width){
        super(viewId);
        this.keepThis = KeepThis;
        this.TB_iframe = TB_iframe;
        this.height = height;
        this.width = width;
    }

    public String getParseSpec() {
        // include a comma delimited list of the public properties in this class
        return super.getParseSpec() + ",keepThis,TB_iframe,height,width";
    }
}

