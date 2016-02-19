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

import java.util.regex.Pattern;

/**
 * Just a bunch of regex patterns for the labels.
 * 
 * @author Alistair Todd ringracer@gmail.com
 */
public abstract class LabelPatterns {

    public static final String NAME = Pattern.quote("$N1");
    public static final String SHORTNAME = Pattern.quote("$N2");
    public static final String REGEXNAME = Pattern.quote("$N3");
    public static final String TOTAL = Pattern.quote("$T");
    public static final String FAILED = Pattern.quote("$F");
    public static final String DISABLED = Pattern.quote("$D");
    public static final String UNSTABLE = Pattern.quote("$U");
    public static final String SUCCESSFUL = Pattern.quote("$S");
}
