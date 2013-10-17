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

import static hudson.model.BallColor.BLUE;
import static hudson.model.BallColor.DISABLED;
import static hudson.model.BallColor.RED;
import static hudson.model.BallColor.YELLOW;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hudson.model.BallColor;
import hudson.model.TopLevelItem;
import hudson.model.Job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

public class JobStatusCountTest {

    @Test
    public void shouldAnswerZeroForNullItems() throws Exception {
        assertThat(new JobStatusCount(null).total(), is(0));
    }

    @Test
    public void shouldAnswerZeroForEmptyItems() throws Exception {
        assertThat(new JobStatusCount(items()).total(), is(0));
    }

    @Test
    public void shouldCountTotalJobs() throws Exception {

        assertThat(new JobStatusCount(items(BLUE)).total(), is(1));
        assertThat(new JobStatusCount(items(BLUE, RED)).total(), is(2));
        assertThat(new JobStatusCount(items(BLUE, RED, YELLOW, RED, DISABLED)).total(), is(5));
    }

    @Test
    public void shouldCountFailedJobs() throws Exception {

        assertThat(new JobStatusCount(items(RED, RED, BLUE)).failed(), is(2));

        assertTrue(new JobStatusCount(items(RED, RED, BLUE)).hasFailures());

        assertFalse(new JobStatusCount(items(RED, RED, BLUE)).allOk());
    }

    @Test
    public void shouldCountSuccessfulJobs() throws Exception {

        assertThat(new JobStatusCount(items(BLUE, BLUE, BLUE, BLUE, BLUE)).successful(), is(5));

        assertTrue(new JobStatusCount(items(BLUE, BLUE, BLUE, BLUE, BLUE)).allOk());
    }

    @Test
    public void shouldCountUnstableJobs() throws Exception {

        assertThat(new JobStatusCount(items(YELLOW)).unstable(), is(1));

        assertTrue(new JobStatusCount(items(YELLOW)).hasUnstable());

        assertFalse(new JobStatusCount(items(YELLOW)).hasFailures());
        assertFalse(new JobStatusCount(items(YELLOW)).allOk());
    }

    @Test
    public void shouldCountDisabledJobs() throws Exception {

        assertThat(new JobStatusCount(items(DISABLED)).disabled(), is(1));

        assertTrue(new JobStatusCount(items(DISABLED)).hasDisabled());

        assertFalse(new JobStatusCount(items(DISABLED)).hasFailures());
        assertFalse(new JobStatusCount(items(DISABLED)).allOk());
    }

    private Collection<TopLevelItem> items(BallColor... colours) {

        Collection<TopLevelItem> items = new ArrayList<TopLevelItem>();

        for (BallColor colour : colours) {
            items.add(item(withJobColour(colour)));
        }

        return items;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private TopLevelItem item(Collection jobs) {

        TopLevelItem item = createMock("item", TopLevelItem.class);
        expect(item.getAllJobs()).andStubReturn((jobs));

        replay(item);

        return item;
    }

    @SuppressWarnings("rawtypes")
    private Collection withJobColour(BallColor colour) {

        List<Job> jobs = new ArrayList<Job>();

        jobs.add(aJobWith(colour));

        return jobs;
    }

    @SuppressWarnings("rawtypes")
    private Job aJobWith(final BallColor colour) {

        Job job = org.easymock.classextension.EasyMock.createMock("job", Job.class);

        EasyMock.expect(job.getIconColor()).andStubReturn(colour);
        org.easymock.classextension.EasyMock.replay(job);

        return job;
    }

}