/**
 *
 * Copyright (c) 2013, Linagora
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA 
 *
 */
package org.ow2.play.governance.itest.rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import org.junit.BeforeClass;

import java.util.UUID;

/**
 * @author Christophe Hamerling - chamerling@linagora.com
 */
public class BaseTest {

    public static final String PLATFORM_API_PATH = "/play/api/v1/platform";
    static final String PROPERTY_PREFIX = "test.play.";
    static final String PORT = "server.port";
    static final String USER_KEY = "user.key";

    @BeforeClass
    public static final void configure() {
        RestAssured.port = getPort();

        // create user for testing purposes...
        // TODO
    }

    /**
     * Get the server port
     *
     * @return
     */
    public static final int getPort() {
        return Integer.parseInt(get(PORT));
    }

    public static final String get(String name) {
        return System.getProperty(PROPERTY_PREFIX + name);
    }

    public String getUserKey() {
        return get(USER_KEY);
    }

    /**
     * Get the full path for a resource
     *
     * @param resource
     * @return
     */
    public String getPath(String resource) {
        String result = PLATFORM_API_PATH;
        if (resource == null || resource.trim().length() == 0) {
            return result;
        }
        if (!resource.startsWith("/")) {
            result += "/";
        }
        return result += resource;
    }

    public Header getAuthHeader() {
        return new Header("Authorization", "Bearer " + getUserKey());
    }

    public Header getInvalidUserToken() {
        return new Header("Authorization", "Bearer " + UUID.randomUUID().toString() + "play" + System.currentTimeMillis());
    }
}
