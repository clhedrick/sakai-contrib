/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/assignment2/trunk/tool/src/java/org/sakaiproject/assignment2/tool/DecoratedTaggingProvider.java $
 * $Id: DecoratedTaggingProvider.java 54926 2008-11-12 21:55:40Z swgithen@mtu.edu $
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
import java.util.List;

import org.sakaiproject.taggable.api.Tag;
import org.sakaiproject.taggable.api.TagColumn;
import org.sakaiproject.taggable.api.TagList;
import org.sakaiproject.taggable.api.TaggableActivity;
import org.sakaiproject.taggable.api.TaggingProvider;

/**
 * Wrapper around {@link TaggingProvider} for displaying a pageable/sortable
 * list of tags for an activity or item. Since there may be multiple providers
 * each with a list of tags displayed on a single page, this class enables each
 * provider to maintain the page/sort state of its list separate from others.
 * 
 * @author carl.hall
 * @author chmaurer
 */
public class DecoratedTaggingProvider {
    TaggingProvider provider;

    Sort sort;

    Pager pager;

    TaggableActivity lastActivity;

    TagList tagList;

    final static int[] PAGESIZES = { 5, 10, 20, 50, 100, 200 };

    public DecoratedTaggingProvider(TaggingProvider provider) {
        this.provider = provider;
        sort = new Sort("", true);
    }

    public TaggingProvider getProvider() {
        return provider;
    }

    public Sort getSort() {
        return sort;
    }

    public Pager getPager() {
        return pager;
    }

    protected TagList getTagList(TaggableActivity activity) {
        if (!activity.equals(lastActivity)) {
            sort = new Sort("", true);
            pager = null;
            tagList = null;
            lastActivity = activity;
        }
        if (tagList == null) {
            tagList = provider.getTags(activity);
        }
        return tagList;
    }

    public List<TagColumn> getColumns(TaggableActivity activity) {
        return getTagList(activity).getColumns();
    }

    public List<Tag> getTags(TaggableActivity activity) {
        TagList list = getTagList(activity);
        List<Tag> tags = new ArrayList<Tag>();
        if (pager == null) {
            pager = new Pager((list == null) ? 0 : list.size(), 0, 20);
        }
        if (list != null) {
            list.sort(list.getColumn(sort.getSort()), sort.isAscending());
            tags = list
            .subList(pager.getFirstItem(), pager.getLastItemNumber());
        }
        return tags;
    }

    public static class Sort {

        protected String sortString = "";

        protected boolean ascending = true;

        public Sort(String sort, boolean ascending) {
            sortString = sort;
            this.ascending = ascending;
        }

        public boolean isAscending() {
            return ascending;
        }

        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }

        public String getSort() {
            return sortString;
        }

        public void setSort(String sort) {
            sortString = sort;
        }
    }

    public static class Pager {

        protected int totalItems;

        protected int firstItem;

        protected int pageSize;

        public static final String FIRST = "|<", PREVIOUS = "<", NEXT = ">",
        LAST = ">|";

        public Pager(int totalItems, int firstItem, int pageSize) {
            this.totalItems = totalItems;
            this.firstItem = firstItem;
            this.pageSize = pageSize;
        }

        public int getFirstItemNumber() {
            return firstItem + 1;
        }

        public int getLastItemNumber() {
            int n = firstItem + pageSize;
            return (totalItems < n ? totalItems : n);
        }

        public boolean canFirst() {
            return firstItem > 0;
        }

        public boolean canPrevious() {
            return canFirst();
        }

        public boolean canNext() {
            return getLastItemNumber() < totalItems;
        }

        public boolean canLast() {
            return canNext();
        }

        public int[] getPageSizes() {
            return PAGESIZES;
        }

        public int getFirstItem() {
            return firstItem;
        }

        public void setFirstItem(int firstItem) {
            this.firstItem = firstItem;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(int totalItems) {
            this.totalItems = totalItems;
        }
    }
}
