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
package dev.rico.internal.client.security;

public class KeycloakAuthentification {

    private final String accessToken;

    private final String appName;

    private final String realm;

    public KeycloakAuthentification(final String accessToken, final String appName, final String realm) {
        this.accessToken = accessToken;
        this.appName = appName;
        this.realm = realm;
    }

    public String getAppName() {
        return appName;
    }

    public String getRealm() {
        return realm;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
