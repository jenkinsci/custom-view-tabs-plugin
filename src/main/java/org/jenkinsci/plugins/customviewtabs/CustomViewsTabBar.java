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

import hudson.Extension;
import hudson.model.View;
import hudson.util.ListBoxModel;
import hudson.views.ViewsTabBar;
import hudson.views.ViewsTabBarDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Given a View, count the number of jobs in the view by status and generate a label and a colour
 * for the tab accordingly.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class CustomViewsTabBar extends ViewsTabBar {

    private ViewItemExtractor extractor = new ViewItemExtractor();

    @DataBoundConstructor
    public CustomViewsTabBar() {
        super();
    }

    @Override
    public CustomViewsTabBarDescriptor getDescriptor() {
        return (CustomViewsTabBarDescriptor) super.getDescriptor();
    }

    public TabDisplayMetaData getTabData(View v) {

        JobStatusCount jobCount = new JobStatusCount(extractor.getItemsInView(v));

        return new TabDisplayMetaData(labelsFor(v, jobCount), colourFor(jobCount));
    }

    private TabLabels labelsFor(View v, JobStatusCount jobCount) {

        TabLabels tabLabels = new TabLabels(jobCount, v.getDisplayName());

        CustomViewsTabBarDescriptor d = getDescriptor();

        tabLabels.setConditionActiveTab(d.getConditionActiveTab());
        tabLabels.setConditionInactiveTab(d.getConditionInactiveTab());

        tabLabels.setPatternActiveTab(d.getPatternActiveTab());
        tabLabels.setPatternInactiveTab(d.getPatternInactiveTab());

        tabLabels.setLabelGenerator(
                new StringReplacementLabelGenerator(
                        d.getNameRegexMatch(), d.getNameRegexReplacement(), d.getShortNameLength()));

        return tabLabels;
    }

    private String colourFor(JobStatusCount jobCount) {
        return getDescriptor().getTabColours().getColourFor(jobCount);
    }

    @Extension
    public static final class CustomViewsTabBarDescriptor extends ViewsTabBarDescriptor {

        private String patternActiveTab = "$N1";
        private String patternInactiveTab = "$N1";

        private boolean conditionActiveTab = false;
        private boolean conditionInactiveTab = false;

        private int shortNameLength = 3;
        private String nameRegexMatch = "(\\w)\\w*|\\s";
        private String nameRegexReplacement = "$1";

        private TabColours tabColours = new TabColours();

        public CustomViewsTabBarDescriptor() {
            load();
        }

        @Override
        public String getDisplayName() {
            return "Custom Views TabBar";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {

            patternActiveTab = formData.getString("patternActiveTab");
            patternInactiveTab = formData.getString("patternInactiveTab");

            shortNameLength = formData.getInt("shortNameLength");
            nameRegexMatch = formData.getString("nameRegexMatch");
            nameRegexReplacement = formData.getString("nameRegexReplacement");

            conditionActiveTab = formData.getBoolean("conditionActiveTab");
            conditionInactiveTab = formData.getBoolean("conditionInactiveTab");

            tabColours.configure(formData);

            save();

            return false;
        }

        public String getPatternActiveTab() {
            return patternActiveTab;
        }

        public String getPatternInactiveTab() {
            return patternInactiveTab;
        }

        public int getShortNameLength() {
            return shortNameLength;
        }

        public String getNameRegexMatch() {
            return nameRegexMatch;
        }

        public String getNameRegexReplacement() {
            return nameRegexReplacement;
        }

        public boolean getConditionActiveTab() {
            return conditionActiveTab;
        }

        public boolean getConditionInactiveTab() {
            return conditionInactiveTab;
        }

        public TabColours getTabColours() {
            return tabColours;
        }

        public ListBoxModel doFillTabColourDefaultItems() {
            return tabColours.doFillTabColourDefaultItems();
        }

        public ListBoxModel doFillTabColourDisabledItems() {
            return tabColours.doFillTabColourDisabledItems();
        }

        public ListBoxModel doFillTabColourUnstableItems() {
            return tabColours.doFillTabColourUnstableItems();
        }

        public ListBoxModel doFillTabColourFailedItems() {
            return tabColours.doFillTabColourFailedItems();
        }

        public String getTabColourDefault() {
            return tabColours.getTabColourDefault();
        }

        public String getTabColourDisabled() {
            return tabColours.getTabColourDisabled();
        }

        public String getTabColourUnstable() {
            return tabColours.getTabColourUnstable();
        }

        public String getTabColourFailed() {
            return tabColours.getTabColourFailed();
        }

        public String getTabColourCustom1() {
            return tabColours.getTabColourCustom1();
        }

        public String getTabColourCustom2() {
            return tabColours.getTabColourCustom2();
        }

        public String getTabColourCustom3() {
            return tabColours.getTabColourCustom3();
        }

        public String getTabColourCustom4() {
            return tabColours.getTabColourCustom4();
        }

    }
}
