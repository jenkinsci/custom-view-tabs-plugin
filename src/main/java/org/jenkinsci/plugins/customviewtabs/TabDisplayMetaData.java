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

/**
 * Metadata for tab display customisation accounting for different states.
 * 
 * @author Alistair Todd ringracer@gmail.com
 */
public class TabDisplayMetaData {

    private String activeLabel;
    private String inactiveLabel;
    private String tabColour;

    public TabDisplayMetaData(TabLabels labels, String tabColour) {
        activeLabel = labels.active();
        inactiveLabel = labels.inactive();
        this.tabColour = tabColour;
    }

    /**
     * Get the label text to display when tab is active.
     * 
     * @return label text
     */
    public String getActiveLabel() {
        return activeLabel;
    }

    /**
     * Set label text to display when tab is active. There's no validation here, so it's up to the
     * user not to supply values that disrupt the resulting HTML.
     * 
     * @param activeLabel
     *            Text to display when tab is active
     */
    public void setActiveLabel(String activeLabel) {
        this.activeLabel = activeLabel;
    }

    /**
     * Get the label text to display when tab is inactive.
     * 
     * @return label text
     */
    public String getInactiveLabel() {
        return inactiveLabel;
    }

    /**
     * Set label text to display when tab is inactive. There's no validation here, so it's up to the
     * user not to supply values that disrupt the resulting HTML.
     * 
     * @param inactiveLabel
     *            Text to display when tab is inactive
     */
    public void setInactiveLabel(String inactiveLabel) {
        this.inactiveLabel = inactiveLabel;
    }

    /**
     * Get suggested tab colour hex code.
     * 
     * @return tabColour as a 6 character hex string without leading #
     */
    public String getColour() {
        return tabColour;
    }

    /**
     * Set the required tab colour as a 6 character hex code without the leading #. There's no
     * validation here so it's up to the user not to supply an invalid colour. Invalid codes or
     * empty strings will result in an uncoloured tab or invalid HTML.
     * 
     * @param tabColour
     *            6 character hex colour code without a leading #
     */
    public void setColour(String tabColour) {
        this.tabColour = tabColour;
    }

}
