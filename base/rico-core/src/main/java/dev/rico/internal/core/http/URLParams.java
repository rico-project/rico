/*
 * Copyright 2018-2019 Karakun AG.
 * Copyright 2015-2018 Canoo Engineering AG.
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
package dev.rico.internal.core.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class URLParams {

    private Map<String, String> map;

    private URLParams() {
        this.map = new HashMap<>();
    }

    public static URLParams of(final String key, final String value) {
        final URLParams params = new URLParams();
        params.and(key, value);
        return params;
    }

    public static URLParams of(final String key, final int value) {
        final URLParams params = new URLParams();
        params.and(key, value);
        return params;
    }

    public static URLParams of(final String key, final long value) {
        final URLParams params = new URLParams();
        params.and(key, value);
        return params;
    }

    public static URLParams of(final String key, final boolean value) {
        final URLParams params = new URLParams();
        params.and(key, value);
        return params;
    }

    public URLParams and(final String key, final String value) {
        map.put(key, value);
        return this;
    }

    public URLParams and(final String key, final int value) {
        map.put(key, String.valueOf(value));
        return this;
    }

    public URLParams and(final String key, final long value) {
        map.put(key, String.valueOf(value));
        return this;
    }

    public URLParams and(final String key, final boolean value) {
        map.put(key, String.valueOf(value));
        return this;
    }

    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(map);
    }
}
