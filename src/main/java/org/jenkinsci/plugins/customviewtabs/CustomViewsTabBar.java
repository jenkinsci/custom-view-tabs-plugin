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

import static org.jenkinsci.plugins.customviewtabs.LabelPatterns.DISABLED;
import static org.jenkinsci.plugins.customviewtabs.LabelPatterns.FAILED;
import static org.jenkinsci.plugins.customviewtabs.LabelPatterns.NAME;
import static org.jenkinsci.plugins.customviewtabs.LabelPatterns.REGEXNAME;
import static org.jenkinsci.plugins.customviewtabs.LabelPatterns.SHORTNAME;
import static org.jenkinsci.plugins.customviewtabs.LabelPatterns.SUCCESSFUL;
import static org.jenkinsci.plugins.customviewtabs.LabelPatterns.TOTAL;
import static org.jenkinsci.plugins.customviewtabs.LabelPatterns.UNSTABLE;
import hudson.Extension;
import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.plugins.nested_view.NestedView;
import hudson.util.ListBoxModel;
import hudson.views.ViewsTabBar;
import hudson.views.ViewsTabBarDescriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

    @DataBoundConstructor
    public CustomViewsTabBar() {
        super();
    }

    @Override
    public CustomViewsTabBarDescriptor getDescriptor() {
        return (CustomViewsTabBarDescriptor) super.getDescriptor();
    }

    public TabDisplayMetaData getTabData(View v) {

        JobStatusCount jobCount = new JobStatusCount(getItemsInView(v));

        return new TabDisplayMetaData(labelsFor(v, jobCount), colourFor(jobCount));
    }

    private Collection<TopLevelItem> getItemsInView(View v) {
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

    private TabLabels labelsFor(View v, JobStatusCount jobCount) {
        return new TabLabels(jobCount, v);
    }

    private String colourFor(JobStatusCount jobCount) {
        return getDescriptor().getTabColours().getColourFor(jobCount);
    }

    class TabLabels {

        private JobStatusCount jobCount;
        private String displayName;

        TabLabels(JobStatusCount jobCount, View v) {
            this.jobCount = jobCount;
            displayName = v.getDisplayName();
        }

        public String active() {

            if (getDescriptor().getConditionActiveTab() && jobCount.allOk()) {
                return displayName;
            } else {
                return getLabel(displayName, jobCount, getDescriptor().getPatternActiveTab());
            }
        }

        public String inactive() {

            if (getDescriptor().getConditionInactiveTab() && jobCount.allOk()) {
                return displayName;
            } else {
                return getLabel(displayName, jobCount, getDescriptor().getPatternInactiveTab());
            }
        }

        private String getLabel(String name, JobStatusCount jobCount, String pattern) {

            String label = pattern.replaceAll(NAME, name);
            label = label.replaceAll(SHORTNAME, shorten(name));
            label = label.replaceAll(REGEXNAME, getRegexName(name));
            label = label.replaceAll(TOTAL, with(jobCount.total()));
            label = label.replaceAll(FAILED, with(jobCount.failed()));
            label = label.replaceAll(DISABLED, with(jobCount.disabled()));
            label = label.replaceAll(UNSTABLE, with(jobCount.unstable()));
            label = label.replaceAll(SUCCESSFUL, with(jobCount.successful()));

            return label;
        }

        private String with(int i) {
            return Integer.toString(i);
        }

        private String getRegexName(String displayName) {
            return displayName.replaceAll(
                    getDescriptor().getNameRegexMatch(), getDescriptor().getNameRegexReplacement());
        }

        private String shorten(String displayName) {

            int shortNameLength = getDescriptor().getShortNameLength();

            if (displayName.length() > shortNameLength) {
                return displayName.substring(0, shortNameLength) + ".";
            }
            else {
                return displayName;
            }
        }
    }

    @Extension
    public static final class CustomViewsTabBarDescriptor extends ViewsTabBarDescriptor {

        private String patternActiveTab = "$N1";
        private String patternInactiveTab = "$N2";

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
