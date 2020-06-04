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
package dev.rico.internal.core.context;

import dev.rico.core.context.ContextManager;
import dev.rico.core.functional.Subscription;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Optional;

import static dev.rico.internal.core.RicoConstants.APPLICATION_CONTEXT;
import static dev.rico.internal.core.RicoConstants.CANONICAL_HOST_NAME_CONTEXT;
import static dev.rico.internal.core.RicoConstants.HOST_ADDRESS_CONTEXT;
import static dev.rico.internal.core.RicoConstants.HOST_NAME_CONTEXT;
import static dev.rico.internal.core.RicoConstants.PLATFORM_VERSION_CONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ContextManagerImplTests {

    private static final String key_1 = "KEY-1";
    private static final String key_2 = "KEY-2";
    private static final String value_1 = "VALUE-1";
    private static final String value_2 = "VALUE-2";

    private ContextManager manager;

    @BeforeMethod
    public void setup() {
        manager = new ContextManagerImpl();
    }

    @Test
    public void testInitialState() {
        assertEquals(manager.getThreadLocalAttributes().size(), 0);

        assertEquals(manager.getGlobalAttributes().size(), 5);
        assertNameExists(manager.getGlobalAttributes(), HOST_NAME_CONTEXT);
        assertNameExists(manager.getGlobalAttributes(), PLATFORM_VERSION_CONTEXT);
        assertNameExists(manager.getGlobalAttributes(), CANONICAL_HOST_NAME_CONTEXT);
        assertNameExists(manager.getGlobalAttributes(), HOST_ADDRESS_CONTEXT);
        assertNameExists(manager.getGlobalAttributes(), APPLICATION_CONTEXT);

        assertEquals(manager.getAttributes().size(), 5);
        assertNameExists(manager.getAttributes(), HOST_NAME_CONTEXT);
        assertNameExists(manager.getAttributes(), PLATFORM_VERSION_CONTEXT);
        assertNameExists(manager.getAttributes(), CANONICAL_HOST_NAME_CONTEXT);
        assertNameExists(manager.getAttributes(), HOST_ADDRESS_CONTEXT);
        assertNameExists(manager.getAttributes(), APPLICATION_CONTEXT);

        assertTrue(manager.getAttribute(HOST_NAME_CONTEXT).isPresent());
        assertTrue(manager.getAttribute(PLATFORM_VERSION_CONTEXT).isPresent());
        assertTrue(manager.getAttribute(CANONICAL_HOST_NAME_CONTEXT).isPresent());
        assertTrue(manager.getAttribute(HOST_ADDRESS_CONTEXT).isPresent());
        assertTrue(manager.getAttribute(APPLICATION_CONTEXT).isPresent());
    }

    @Test
    public void testSettingGlobalContext() {
        // when
        manager.setGlobalAttribute(key_1, value_1);

        // then
        assertValue(manager.getAttribute(key_1), value_1);

        assertEquals(manager.getThreadLocalAttributes().size(), 0);
        assertNameDoesNotExist(manager.getThreadLocalAttributes(), key_1);

        assertEquals(manager.getGlobalAttributes().size(), 6);
        assertContainsKeyValue(manager.getGlobalAttributes(), key_1, value_1);

        assertEquals(manager.getAttributes().size(), 6);
        assertContainsKeyValue(manager.getAttributes(), key_1, value_1);
    }

    @Test
    public void testOverrideGlobalContext() {
        // when
        manager.setGlobalAttribute(key_1, value_1);
        manager.setGlobalAttribute(key_1, value_2);

        // then
        assertValue(manager.getAttribute(key_1), value_2);

        assertEquals(manager.getThreadLocalAttributes().size(), 0);
        assertNameDoesNotExist(manager.getThreadLocalAttributes(), key_1);

        assertEquals(manager.getGlobalAttributes().size(), 6);
        assertContainsKeyValue(manager.getGlobalAttributes(), key_1, value_2);

        assertEquals(manager.getAttributes().size(), 6);
        assertContainsKeyValue(manager.getAttributes(), key_1, value_2);
    }

    @Test
    public void testTwoGlobalContext() {
        // when
        manager.setGlobalAttribute(key_1, value_1);
        manager.setGlobalAttribute(key_2, value_2);

        // then
        assertValue(manager.getAttribute(key_1), value_1);
        assertValue(manager.getAttribute(key_2), value_2);

        assertEquals(manager.getThreadLocalAttributes().size(), 0);
        assertNameDoesNotExist(manager.getThreadLocalAttributes(), key_1);
        assertNameDoesNotExist(manager.getThreadLocalAttributes(), key_2);

        assertEquals(manager.getGlobalAttributes().size(), 7);
        assertContainsKeyValue(manager.getGlobalAttributes(), key_1, value_1);
        assertContainsKeyValue(manager.getGlobalAttributes(), key_2, value_2);

        assertEquals(manager.getAttributes().size(), 7);
        assertContainsKeyValue(manager.getAttributes(), key_1, value_1);
        assertContainsKeyValue(manager.getAttributes(), key_2, value_2);
    }

    @Test
    public void testRemoveGlobalContext() {
        // when
        final Subscription subscription = manager.setGlobalAttribute(key_1, value_1);
        subscription.unsubscribe();

        // then
        assertFalse(manager.getAttribute(key_1).isPresent());

        assertEquals(manager.getThreadLocalAttributes().size(), 0);
        assertNameDoesNotExist(manager.getThreadLocalAttributes(), key_1);

        assertEquals(manager.getGlobalAttributes().size(), 5);
        assertNameDoesNotExist(manager.getGlobalAttributes(), key_1);

        assertEquals(manager.getAttributes().size(), 5);
        assertNameDoesNotExist(manager.getAttributes(), key_1);
    }

    @Test
    public void testSettingThreadLocalContext() {
        // when
        manager.setThreadLocalAttribute(key_1, value_1);

        // then
        assertValue(manager.getAttribute(key_1), value_1);

        assertEquals(manager.getThreadLocalAttributes().size(), 1);
        assertContainsKeyValue(manager.getThreadLocalAttributes(), key_1, value_1);

        assertEquals(manager.getGlobalAttributes().size(), 5);
        assertNameDoesNotExist(manager.getGlobalAttributes(), key_1);

        assertEquals(manager.getAttributes().size(), 6);
        assertContainsKeyValue(manager.getAttributes(), key_1, value_1);
    }

    @Test
    public void testOverrideThreadLocalContext() {
        // when
        manager.setThreadLocalAttribute(key_1, value_1);
        manager.setThreadLocalAttribute(key_1, value_2);

        // then
        assertValue(manager.getAttribute(key_1), value_2);

        assertEquals(manager.getThreadLocalAttributes().size(), 1);
        assertContainsKeyValue(manager.getThreadLocalAttributes(), key_1, value_2);

        assertEquals(manager.getGlobalAttributes().size(), 5);
        assertNameDoesNotExist(manager.getGlobalAttributes(), key_1);

        assertEquals(manager.getAttributes().size(), 6);
        assertContainsKeyValue(manager.getAttributes(), key_1, value_2);
    }

    @Test
    public void testTwoThreadLocalContext() {
        // when
        manager.setThreadLocalAttribute(key_1, value_1);
        manager.setThreadLocalAttribute(key_2, value_2);

        // then
        assertValue(manager.getAttribute(key_1), value_1);
        assertValue(manager.getAttribute(key_2), value_2);

        assertEquals(manager.getThreadLocalAttributes().size(), 2);
        assertContainsKeyValue(manager.getThreadLocalAttributes(), key_1, value_1);
        assertContainsKeyValue(manager.getThreadLocalAttributes(), key_2, value_2);

        assertEquals(manager.getGlobalAttributes().size(), 5);
        assertNameDoesNotExist(manager.getGlobalAttributes(), key_1);
        assertNameDoesNotExist(manager.getGlobalAttributes(), key_2);

        assertEquals(manager.getAttributes().size(), 7);
        assertContainsKeyValue(manager.getAttributes(), key_1, value_1);
        assertContainsKeyValue(manager.getAttributes(), key_2, value_2);
    }

    @Test
    public void testRemoveThreadLocalContext() {
        // when
        final Subscription subscription = manager.setThreadLocalAttribute(key_1, value_1);
        subscription.unsubscribe();

        // then
        assertFalse(manager.getAttribute(key_1).isPresent());

        assertEquals(manager.getThreadLocalAttributes().size(), 0);
        assertNameDoesNotExist(manager.getThreadLocalAttributes(), key_1);

        assertEquals(manager.getGlobalAttributes().size(), 5);
        assertNameDoesNotExist(manager.getGlobalAttributes(), key_1);

        assertEquals(manager.getAttributes().size(), 5);
        assertNameDoesNotExist(manager.getAttributes(), key_1);
    }


    @Test
    public void testThreadLocalContextOverwritesGlobalContext() {
        // when
        manager.setGlobalAttribute(key_1, value_1);
        manager.setThreadLocalAttribute(key_1, value_2);
        manager.setGlobalAttribute(key_1, value_1);

        // then
        assertValue(manager.getAttribute(key_1), value_2);

        assertEquals(manager.getGlobalAttributes().size(), 6);
        assertContainsKeyValue(manager.getGlobalAttributes(), key_1, value_1);

        assertEquals(manager.getThreadLocalAttributes().size(), 1);
        assertContainsKeyValue(manager.getThreadLocalAttributes(), key_1, value_2);

        assertEquals(manager.getAttributes().size(), 6);
        assertContainsKeyValue(manager.getAttributes(), key_1, value_2);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public void assertValue(Optional<String> actual, String expected) {
        assertEquals(actual.orElseThrow(), expected);
    }

    private void assertNameDoesNotExist(Map<String, String> attributes, final String name) {
        assertFalse(attributes.containsKey(name), "Global attribute with name '" + name + "' found");
    }

    private void assertNameExists(Map<String, String> attributes, final String name) {
        assertTrue(attributes.containsKey(name), "Global attribute with name '" + name + "' not found");
    }

    private void assertContainsKeyValue(Map<String, String> attributes, final String name, final String expectedValue) {
        final String foundValue = attributes.get(name);
        assertEquals(foundValue, expectedValue, "Wrong global attribute with name '" + name + "' expected '" + expectedValue + "' but found'" + foundValue + "'");
    }

}
