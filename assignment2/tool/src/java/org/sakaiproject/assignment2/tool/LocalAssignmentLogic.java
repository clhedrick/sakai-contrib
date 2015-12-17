/**********************************************************************************
 * $URL:https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/beans/locallogic/LocalAssignmentLogic.java $
 * $Id:LocalAssignmentLogic.java 48274 2008-04-23 20:07:00Z wagnermr@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007 The Sakai Foundation.
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

package org.sakaiproject.assignment2.tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment2.model.AssignmentGroup;


/**
 * Contains logic methods that are used by the UI, currently for the Student
 * Assignment List only
 * 
 * @author rjlowe
 * @author sgithens
 */
public class LocalAssignmentLogic {

    private static final Log LOG = LogFactory.getLog(LocalAssignmentLogic.class);

    // sorting that uses non-persisted fields populated in the UI
    public static final String SORT_BY_FOR = "for";
    public static final String SORT_BY_STATUS = "status";
    public static final String SORT_BY_GRADE = "grade";

    /**
     * For a set of AssignmentGroups and a Map of Group ID's to Names, return a
     * list of group names comma delimited.
     * 
     * The AssignmentGroups GroupID must match the GroupId keys in the map. 
     * 
     * Wow, this would only be like 2 lines in Python or Lisp.
     * TODO FIXME This was only being used to generate the access column in 
     * the Student Assignment view, which isn't in the spec anymore. So we
     * might be able to get rid of this source part all together. The same with
     * the constants above, they are no longer being used. This is all commented
     * out in StudentAssignmentListProducer.
     * 
     * @param groups
     * @return a comma-delimited String representation of the given list of
     * groups/section. 
     */
    public String getListOfGroupRestrictionsAsString(Collection<AssignmentGroup> restrictedGroups, Map<String, String> siteGroupIdNameMap) {
        StringBuilder sb = new StringBuilder();

        if (restrictedGroups != null) {
            List<String> groupNameList = new ArrayList<String>();

            for (AssignmentGroup group : restrictedGroups) {
                if (group != null) {
                    if (siteGroupIdNameMap.containsKey(group.getGroupId())) {
                        String groupName = (String)siteGroupIdNameMap.get(group.getGroupId());
                        groupNameList.add(groupName);
                    }
                }
            }

            Collections.sort(groupNameList);

            for (int i=0; i < groupNameList.size(); i++) {

                String groupName = (String) groupNameList.get(i);
                if (groupName != null) {
                    if (i != 0) {
                        sb.append(", ");
                    }

                    sb.append(groupName);
                }
            }	
        }

        return sb.toString();
    }

}