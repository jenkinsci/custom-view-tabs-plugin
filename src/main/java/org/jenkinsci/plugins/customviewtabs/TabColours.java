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

import hudson.util.ListBoxModel;
import hudson.util.ListBoxModel.Option;
import net.sf.json.JSONObject;

/**
 * Subset of the descriptor to handle tab colour settings. Ugly. When I understand how to properly
 * work with all this Jelly stuff, I'm sure I can make this much neater.
 * 
 * @author Alistair Todd ringracer@gmail.com
 */
public class TabColours {

    private String tabColourDefault = "";
    private String tabColourDisabled = "";
    private String tabColourUnstable = "";
    private String tabColourFailed = "";

    private String tabColourCustom1 = "";
    private String tabColourCustom2 = "";
    private String tabColourCustom3 = "";
    private String tabColourCustom4 = "";

    /**
     * Remember values received from config page.
     * 
     * @param formData
     */
    public void configure(JSONObject formData) {

        tabColourCustom1 = formData.getString("tabColourCustom1");
        tabColourCustom2 = formData.getString("tabColourCustom2");
        tabColourCustom3 = formData.getString("tabColourCustom3");
        tabColourCustom4 = formData.getString("tabColourCustom4");

        tabColourDefault = swapCustomColours(formData.getString("tabColourDefault"));
        tabColourDisabled = swapCustomColours(formData.getString("tabColourDisabled"));
        tabColourUnstable = swapCustomColours(formData.getString("tabColourUnstable"));
        tabColourFailed = swapCustomColours(formData.getString("tabColourFailed"));
    }

    /**
     * Supply options for config page drop down.
     * 
     * @return
     */
    public ListBoxModel doFillTabColourDefaultItems() {
        return colourOptionsFor(tabColourDefault);
    }

    /**
     * Supply options for config page drop down.
     * 
     * @return
     */
    public ListBoxModel doFillTabColourDisabledItems() {
        return colourOptionsFor(tabColourDisabled);
    }

    /**
     * Supply options for config page drop down.
     * 
     * @return
     */
    public ListBoxModel doFillTabColourUnstableItems() {
        return colourOptionsFor(tabColourUnstable);
    }

    /**
     * Supply options for config page drop down.
     * 
     * @return
     */
    public ListBoxModel doFillTabColourFailedItems() {
        return colourOptionsFor(tabColourFailed);
    }

    private ListBoxModel colourOptionsFor(String selectionColour) {

        ListBoxModel options = withDefaultColourOptions();

        for (Option o : options) {
            if (o.value.equalsIgnoreCase(selectionColour)) {
                o.selected = true;
            } else {
                o.selected = false;
            }
        }

        return options;
    }

    private ListBoxModel withDefaultColourOptions() {

        Option noColourOption = new Option("None", "", false);
        Option greenColourOption = new Option("Green", "98fb98", false);
        Option blueColourOption = new Option("Blue", "0000ff", false);
        Option greyColourOption = new Option("Grey", "cdc9c9", false);
        Option yellowColourOption = new Option("Yellow", "ffff00", false);
        Option redColourOption = new Option("Red", "ff0000", false);

        Option customColourOption1 = new Option("Custom1", "Custom1", false);
        Option customColourOption2 = new Option("Custom2", "Custom2", false);
        Option customColourOption3 = new Option("Custom3", "Custom3", false);
        Option customColourOption4 = new Option("Custom4", "Custom4", false);

        return new ListBoxModel(
                noColourOption,
                greenColourOption,
                blueColourOption,
                greyColourOption,
                yellowColourOption,
                redColourOption,
                customColourOption1,
                customColourOption2,
                customColourOption3,
                customColourOption4);
    }

    /**
     * Returns the appropriate colour setting for the supplied job status counts, in the correct
     * order or precedence.
     * 
     * @param jobCounts
     *            Job status counts
     * @return colour as configured for the worst job status indicated by jobCounts
     */
    public String getColourFor(JobStatusCount jobCounts) {

        if (jobCounts.hasFailures()) {
            return getTabColourFailed();
        }
        if (jobCounts.hasUnstable()) {
            return getTabColourUnstable();
        }
        if (jobCounts.hasDisabled()) {
            return getTabColourDisabled();
        }
        else {
            return getTabColourDefault();
        }
    }

    public String getTabColourDefault() {
        return tabColourDefault;
    }

    public String getTabColourDisabled() {
        return tabColourDisabled;
    }

    public String getTabColourUnstable() {
        return tabColourUnstable;
    }

    public String getTabColourFailed() {
        return tabColourFailed;
    }

    public String getTabColourCustom1() {
        return tabColourCustom1;
    }

    public String getTabColourCustom2() {
        return tabColourCustom2;
    }

    public String getTabColourCustom3() {
        return tabColourCustom3;
    }

    public String getTabColourCustom4() {
        return tabColourCustom4;
    }

    private String swapCustomColours(String colour) {

        if (colour.startsWith("Custom")) {

            if (colour.equalsIgnoreCase("Custom1")) {
                return tabColourCustom1;
            }
            if (colour.equalsIgnoreCase("Custom2")) {
                return tabColourCustom2;
            }
            if (colour.equalsIgnoreCase("Custom3")) {
                return tabColourCustom3;
            }
            if (colour.equalsIgnoreCase("Custom4")) {
                return tabColourCustom4;
            }
        }

        return colour;
    }

    public void setTabColourDefault(String tabColourDefault) {
        this.tabColourDefault = tabColourDefault;
    }

    public void setTabColourDisabled(String tabColourDisabled) {
        this.tabColourDisabled = tabColourDisabled;
    }

    public void setTabColourUnstable(String tabColourUnstable) {
        this.tabColourUnstable = tabColourUnstable;
    }

    public void setTabColourFailed(String tabColourFailed) {
        this.tabColourFailed = tabColourFailed;
    }

    public void setTabColourCustom1(String tabColourCustom1) {
        this.tabColourCustom1 = tabColourCustom1;
    }

    public void setTabColourCustom2(String tabColourCustom2) {
        this.tabColourCustom2 = tabColourCustom2;
    }

    public void setTabColourCustom3(String tabColourCustom3) {
        this.tabColourCustom3 = tabColourCustom3;
    }

    public void setTabColourCustom4(String tabColourCustom4) {
        this.tabColourCustom4 = tabColourCustom4;
    }

}
