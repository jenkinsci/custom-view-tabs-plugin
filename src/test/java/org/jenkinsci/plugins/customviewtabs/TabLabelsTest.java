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

public class TabLabelsTest {

    private String displayName = "display name";
    private String nameRegexMatch = "(\\w)\\w*|\\s";
    private String nameRegexReplacement = "$1";
    private int shortNameLength = 3;

    private TabLabels tabLabels;

    @Before
    public void setUp() {

        int successful = 1;
        int failures = 2;
        int unstable = 3;
        int disabled = 4;

        tabLabels = new TabLabels(countsFor(successful, failures, unstable, disabled), displayName);

        StringReplacementLabelGenerator labelGenerator =
                new StringReplacementLabelGenerator(nameRegexMatch, nameRegexReplacement, shortNameLength);

        tabLabels.setLabelGenerator(labelGenerator);
    }

    @Test
    public void shouldReplaceNameTagWithName() throws Exception {

        tabLabels.setPatternActiveTab("$N1");

        assertThat(tabLabels.active(), is(displayName));
    }

    @Test
    public void shouldReplaceShortNameTagWithShortName() throws Exception {

        tabLabels.setPatternActiveTab("$N2");

        assertThat(tabLabels.active(), is("dis."));
    }

    @Test
    public void shouldReplaceRegNameTagWithRegexName() throws Exception {

        tabLabels.setPatternActiveTab("$N3");

        assertThat(tabLabels.active(), is("dn"));
    }

    @Test
    public void shouldReplaceTotalTagWithTotalCount() throws Exception {

        tabLabels.setPatternActiveTab("$T");

        assertThat(tabLabels.active(), is("10"));
    }

    @Test
    public void shouldReplaceFailedTagWithFailedCount() throws Exception {

        tabLabels.setPatternActiveTab("$F");

        assertThat(tabLabels.active(), is("2"));
    }

    @Test
    public void shouldReplaceUnstableTagWithUnstableCount() throws Exception {

        tabLabels.setPatternActiveTab("$U");

        assertThat(tabLabels.active(), is("3"));
    }

    @Test
    public void shouldReplaceDisabledTagWithDisabledCount() throws Exception {

        tabLabels.setPatternActiveTab("$D");

        assertThat(tabLabels.active(), is("4"));
    }

    @Test
    public void shouldReplaceSucceedingTagWithSucceedingCount() throws Exception {

        tabLabels.setPatternActiveTab("$S");

        assertThat(tabLabels.active(), is("1"));
    }

    @Test
    public void shouldReplaceAllOccurrencesOfTag() throws Exception {

        tabLabels.setPatternActiveTab("$D$D");

        assertThat(tabLabels.active(), is("44"));
    }

    @Test
    public void shouldIgnoreUnknownTags() throws Exception {

        tabLabels.setPatternActiveTab("$D$X");

        assertThat(tabLabels.active(), is("4$X"));
    }

    @Test
    public void shouldShortenNameToRequiredNumberOfChars() throws Exception {

        tabLabels.setLabelGenerator(new StringReplacementLabelGenerator(nameRegexMatch, nameRegexReplacement, 4));

        tabLabels.setPatternActiveTab("$N2");

        assertThat(tabLabels.active(), is("disp."));
    }

    @Test
    public void shouldReturnDisplayNameWhenConditionalAndNoErrors() throws Exception {

        tabLabels = new TabLabels(countsFor(1, 0, 0, 0), displayName);

        StringReplacementLabelGenerator labelGenerator =
                new StringReplacementLabelGenerator(nameRegexMatch, nameRegexReplacement, shortNameLength);

        tabLabels.setLabelGenerator(labelGenerator);

        tabLabels.setConditionActiveTab(true);

        tabLabels.setPatternActiveTab("$N2 $T");

        assertThat(tabLabels.active(), is(displayName));
    }

    @Test
    public void shouldReturnLabelWhenConditionalWithErrors() throws Exception {

        tabLabels.setConditionActiveTab(false);

        tabLabels.setPatternActiveTab("$N2 $T");

        assertThat(tabLabels.active(), is("dis. 10"));
    }

    private JobStatusCount countsFor(int successful, int failures, int unstable, int disabled) {

        JobStatusCount jobCounts = createMock(JobStatusCount.class);

        expect(jobCounts.hasFailures()).andStubReturn(failures > 0);
        expect(jobCounts.hasUnstable()).andStubReturn(unstable > 0);
        expect(jobCounts.hasDisabled()).andStubReturn(disabled > 0);

        expect(jobCounts.total()).andStubReturn(successful + failures + unstable + disabled);
        expect(jobCounts.failed()).andStubReturn(failures);
        expect(jobCounts.unstable()).andStubReturn(unstable);
        expect(jobCounts.disabled()).andStubReturn(disabled);
        expect(jobCounts.successful()).andStubReturn(successful);
        expect(jobCounts.allOk()).andStubReturn((failures == 0) && (disabled == 0) && (unstable == 0));

        replay(jobCounts);

        return jobCounts;
    }
}
