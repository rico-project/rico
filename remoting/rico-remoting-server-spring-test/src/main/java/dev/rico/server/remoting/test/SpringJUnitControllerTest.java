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
package dev.rico.server.remoting.test;

import dev.rico.internal.core.Assert;
import dev.rico.internal.server.remoting.test.ClientTestFactory;
import dev.rico.internal.server.remoting.test.SpringTestBootstrap;
import dev.rico.internal.server.remoting.test.TestClientContext;
import org.apiguardian.api.API;

//change this into junit5
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
//change to junit5
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.rules.ExternalResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//What does this do?
//import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.apiguardian.api.API.Status.MAINTAINED;

/**
 * Base class for JUnit based controller tests in Spring. This class can be extended to write custom controller tests.
 *
 * @see ControllerTest
 * @see //AbstractJUnit4SpringContextTests
 * @see ControllerUnderTest
 *
 * @author Hendrik Ebbers
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = SpringTestBootstrap.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@API(since = "0.x", status = MAINTAINED)
public abstract class SpringJUnitControllerTest  implements ControllerTest {

    @Autowired
    private TestClientContext clientContext;

    //Turn this to junit5
//    @Rule
//    public ExternalResource clientConnector = new ExternalResource() {
//        @Override
//        protected void before() throws Throwable {
//            super.before();
//            clientContext.connect().get();
//        }
//
//        @Override
//        protected void after() {
//            super.after();
//            try {
//                clientContext.disconnect().get();
//            } catch (Exception e) {
//                throw new ControllerTestException("Can not disconnect client context!", e);
//            }
//        }
//    };

    public static class clientConnector implements BeforeAllCallback, AfterAllCallback{

        private TestClientContext testClientContext;

        public TestClientContext getTestClientContext(){
            return testClientContext;
        }

        @Override
        public void afterAll(final ExtensionContext context) throws Exception {
            try {
                testClientContext.disconnect().get();
            } catch (Exception e) {
                throw new ControllerTestException("Can not disconnect client context!", e);
            }
        }

        @Override
        public void beforeAll(final ExtensionContext context) throws Exception {
            testClientContext.connect().get();
        }
    }

    @RegisterExtension
    static clientConnector connector = new clientConnector();

    @Test
    void clientRunning(){
        Assertions.assertTrue(true);
    }







    public <T> ControllerUnderTest<T> createController(final String controllerName) {
        Assert.requireNonBlank(controllerName, "controllerName");
        try {
            return ClientTestFactory.createController(clientContext, controllerName);
        } catch (Exception e) {
            throw new ControllerTestException("Can't create controller proxy", e);
        }
    }
}
