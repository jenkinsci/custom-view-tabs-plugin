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

import org.junit.Before;
import org.junit.Test;

public class TabColoursTest {

    private String green = "green";
    private String red = "red";
    private String yellow = "yellow";
    private String grey = "grey";

    private TabColours tabColours;

    @Before
    public void setUp() {

        tabColours = new TabColours();

        tabColours.setTabColourDefault(green);
        tabColours.setTabColourFailed(red);
        tabColours.setTabColourUnstable(yellow);
        tabColours.setTabColourDisabled(grey);
    }

    @Test
    public void shouldGetSuccessColourForSuccessfulJob() throws Exception {

        boolean failures = false;
        boolean unstable = false;
        boolean disabled = false;

        assertThat(tabColours.getColourFor(countsFor(failures, unstable, disabled)), is(green));
    }

    @Test
    public void shouldGetFailedColourForFailedJob() throws Exception {

        boolean failures = true;
        boolean unstable = false;
        boolean disabled = false;

        assertThat(tabColours.getColourFor(countsFor(failures, unstable, disabled)), is(red));
    }

    @Test
    public void shouldGetUnstableColourForUnstableJob() throws Exception {

        boolean failures = false;
        boolean unstable = true;
        boolean disabled = false;

        assertThat(tabColours.getColourFor(countsFor(failures, unstable, disabled)), is(yellow));
    }

    @Test
    public void shouldGetDisabledColourForDisabledJob() throws Exception {

        boolean failures = false;
        boolean unstable = false;
        boolean disabled = true;

        assertThat(tabColours.getColourFor(countsFor(failures, unstable, disabled)), is(grey));
    }

    @Test
    public void shouldGetFailedColourForFailedAndUnstableJobs() throws Exception {

        boolean failures = true;
        boolean unstable = true;
        boolean disabled = false;

        assertThat(tabColours.getColourFor(countsFor(failures, unstable, disabled)), is(red));
    }

    @Test
    public void shouldGetUnstableColourForUnstableAndDisabledJobs() throws Exception {

        boolean failures = false;
        boolean unstable = true;
        boolean disabled = true;

        assertThat(tabColours.getColourFor(countsFor(failures, unstable, disabled)), is(yellow));
    }

    private JobStatusCount countsFor(boolean failures, boolean unstable, boolean disabled) {

        JobStatusCount jobCounts = createMock(JobStatusCount.class);

        expect(jobCounts.hasFailures()).andStubReturn(failures);
        expect(jobCounts.hasUnstable()).andStubReturn(unstable);
        expect(jobCounts.hasDisabled()).andStubReturn(disabled);

        replay(jobCounts);

        return jobCounts;
    }
}
