/*
 * Copyright 2012-2013 Alistair Todd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jenkinsci.plugins.customviewtabs;

import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.plugins.nested_view.NestedView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Extract all items from views including nested views.
 * 
 * @author Alistair Todd ringracer@gmail.com
 */
public class ViewItemExtractor {

    public Collection<TopLevelItem> getItemsInView(View v) {
        return allItems(Collections.singleton(v), new ArrayList<TopLevelItem>());
    }

    private Collection<TopLevelItem> allItems(Collection<View> views, Collection<TopLevelItem> items) {

        for (View v : views) {

            if (v instanceof NestedView) {
                allItems(((NestedView) v).getViews(), items);
            }
            else {
                items.addAll(v.getItems());
            }
        }

        return items;
    }
}
