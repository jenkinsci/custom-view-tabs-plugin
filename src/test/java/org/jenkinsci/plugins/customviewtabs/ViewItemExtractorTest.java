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

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.plugins.nested_view.NestedView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

public class ViewItemExtractorTest {

    @Test
    public void shouldGetItemsInView() throws Exception {

        View v = createMock(View.class);
        Collection<TopLevelItem> items = new ArrayList<TopLevelItem>();

        items.add(createMock(TopLevelItem.class));
        items.add(createMock(TopLevelItem.class));
        items.add(createMock(TopLevelItem.class));

        expect(v.getItems()).andReturn(items);
        replay(v);

        assertThat(new ViewItemExtractor().getItemsInView(v).size(), is(3));
    }

    @Test
    public void shouldWorkWithNoItemsInView() throws Exception {

        View v = createMock(View.class);
        Collection<TopLevelItem> items = new ArrayList<TopLevelItem>();

        expect(v.getItems()).andReturn(items);
        replay(v);

        assertThat(new ViewItemExtractor().getItemsInView(v).size(), is(0));
    }

    @Test
    public void shouldGetItemsInNestedView() throws Exception {

        View v = createMock(View.class);
        Collection<TopLevelItem> items = new ArrayList<TopLevelItem>();

        items.add(createMock(TopLevelItem.class));
        items.add(createMock(TopLevelItem.class));
        items.add(createMock(TopLevelItem.class));
        items.add(createMock(TopLevelItem.class));

        expect(v.getItems()).andReturn(items);
        replay(v);

        NestedView n = createMock(NestedView.class);
        expect(n.getViews()).andReturn(Collections.singletonList(v));
        replay(n);

        assertThat(new ViewItemExtractor().getItemsInView(n).size(), is(4));

    }
}
