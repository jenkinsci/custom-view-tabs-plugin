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

import hudson.model.TopLevelItem;
import hudson.model.Job;

import java.util.Collection;
import java.util.Iterator;

/**
 * Sums jobs according to build status as indicated by the iconColour. This is very crude. There
 * must be a more direct way to determine the status of a job than by looking at the icon colour.
 * Anyone?
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class JobStatusCount {

    private int total = 0;
    private int failed = 0;
    private int disabled = 0;
    private int unstable = 0;
    private int successful = 0;

    public JobStatusCount(Collection<TopLevelItem> items) {

        total = items.size();

        for (TopLevelItem item : items) {

            @SuppressWarnings("rawtypes")
            Iterator<? extends Job> iterator = item.getAllJobs().iterator();

            if (iterator.hasNext()) {
                countJobStatus(iterator.next());
                // TODO I must be missing something here. When does a TopLevelItem have more than
                // one Job?
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void countJobStatus(Job j) {

        switch (j.getIconColor()) {

            case BLUE:
                successful++;
                break;
            case YELLOW:
                unstable++;
                break;
            case RED:
                failed++;
                break;
            case DISABLED:
                disabled++;
                break;
            default:
                // Can't tell what this is
                break;
        }
    }

    public boolean allOk() {
        return !(hasFailures() || hasDisabled() || hasUnstable());
    }

    public int total() {
        return total;
    }

    public int failed() {
        return failed;
    }

    public int disabled() {
        return disabled;
    }

    public int unstable() {
        return unstable;
    }

    public int successful() {
        return successful;
    }

    public boolean hasFailures() {
        return failed > 0;
    }

    public boolean hasDisabled() {
        return disabled > 0;
    }

    public boolean hasUnstable() {
        return unstable > 0;
    }
}
