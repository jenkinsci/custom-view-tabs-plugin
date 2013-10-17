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

/**
 * Generate labels using simple string replacement in a crude, ugly and inefficient manner.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class StringReplacementLabelGenerator implements LabelGenerator {

    private String nameRegexMatch;
    private String nameRegexReplacement;
    private int shortNameLength;

    public StringReplacementLabelGenerator(String nameRegexMatch, String nameRegexReplacement, int shortNameLength) {
        this.nameRegexMatch = nameRegexMatch;
        this.nameRegexReplacement = nameRegexReplacement;
        this.shortNameLength = shortNameLength;
    }

    public String generateLabel(String displayName, JobStatusCount jobCount, String pattern) {

        String label = pattern.replaceAll(NAME, displayName);
        label = label.replaceAll(SHORTNAME, shorten(displayName));
        label = label.replaceAll(REGEXNAME, getRegexName(displayName));
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
        return displayName.replaceAll(nameRegexMatch, nameRegexReplacement);
    }

    private String shorten(String displayName) {

        if (displayName.length() > shortNameLength) {
            return displayName.substring(0, shortNameLength) + ".";
        }
        else {
            return displayName;
        }
    }
}
