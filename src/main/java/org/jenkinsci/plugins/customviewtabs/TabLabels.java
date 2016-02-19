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
 * Generation of tab labels according to job status details and label pattern configuration.
 * 
 * @author Alistair Todd ringracer@gmail.com
 */
public class TabLabels {

    private String patternActiveTab = "$N1";
    private String patternInactiveTab = "$N1";
    private boolean conditionActiveTab = false;
    private boolean conditionInactiveTab = false;

    private JobStatusCount jobCount;
    private String displayName;

    private LabelGenerator labelGenerator;

    public TabLabels(JobStatusCount jobCount, String displayName) {
        this.jobCount = jobCount;
        this.displayName = displayName;
    }

    /**
     * Get the active tab label for the current data and configuration.
     * 
     * @return label text for active tab
     */
    public String active() {

        if (conditionActiveTab && jobCount.allOk()) {
            return displayName;
        } else {
            return labelGenerator.generateLabel(displayName, jobCount, patternActiveTab);
        }
    }

    /**
     * Get the inactive tab label for the current data and configuration.
     * 
     * @return label text for inactive tab
     */
    public String inactive() {

        if (conditionInactiveTab && jobCount.allOk()) {
            return displayName;
        } else {
            return labelGenerator.generateLabel(displayName, jobCount, patternInactiveTab);
        }
    }

    public LabelGenerator getLabelGenerator() {
        return labelGenerator;
    }

    public void setLabelGenerator(LabelGenerator labelGenerator) {
        this.labelGenerator = labelGenerator;
    }

    public String getPatternActiveTab() {
        return patternActiveTab;
    }

    public void setPatternActiveTab(String patternActiveTab) {
        this.patternActiveTab = patternActiveTab;
    }

    public String getPatternInactiveTab() {
        return patternInactiveTab;
    }

    public void setPatternInactiveTab(String patternInactiveTab) {
        this.patternInactiveTab = patternInactiveTab;
    }

    public boolean isConditionActiveTab() {
        return conditionActiveTab;
    }

    public void setConditionActiveTab(boolean conditionActiveTab) {
        this.conditionActiveTab = conditionActiveTab;
    }

    public boolean isConditionInactiveTab() {
        return conditionInactiveTab;
    }

    public void setConditionInactiveTab(boolean conditionInactiveTab) {
        this.conditionInactiveTab = conditionInactiveTab;
    }

}
