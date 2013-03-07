/*
 * Copyright (c) 2013 IST GmbH Dresden, the original author or authors
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
 *
 * IST GmbH Dresden
 * Eisenstuckstraße 10, 01069 Dresden, Germany
 * All rights reserved.
 *
 * Name: JUnit4TestExecuterServiceImpl.java
 * Autor: Mirko Zeibig
 * Datum: 21.01.2013 16:38:43
 */
package com.composum.osgi.core.test;

import java.util.*;

import org.apache.felix.scr.annotations.*;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.osgi.service.component.ComponentContext;

/**
 * @author Mirko Zeibig
 *
 */
@Component
@Service
public class JUnit4TestExecuterServiceImpl implements TestExecuterService {

    @Reference(cardinality=ReferenceCardinality.OPTIONAL_MULTIPLE,
            name="TestCase", referenceInterface=TestService.class)
    private List<TestService> testCases = new ArrayList<>();

    protected void bindTestCase(TestService testService) {
        testCases.add(testService);
    }

    protected void unbindTestCase(TestService testService) {
        testCases.remove(testService);
    }

    @SuppressWarnings("unused")
    private ComponentContext context;

    @Activate
    protected void activate(final ComponentContext context) {
        this.context = context;
    }

    @Deactivate
    protected void deactivate() {
        this.context = null;
    }

    @Override
    public List<Result> runTests() {
        List<Result> testResults = new ArrayList<>();
        try {
            final Map<String, Result> m = new HashMap<>();
            for (final TestService test: testCases) {
                Runner runner = new BlockJUnit4ClassRunner(test.getClass()){
                    @Override
                    protected Object createTest() {
                        return test;
                    }
                };
                RunNotifier notifier = new RunNotifier();
                RunListener listener = new RunListener() {
                    @Override
                    public void testStarted(Description description) {
                        Result r = new Result(description);
                        m.put(description.getDisplayName(), r);
                    }
                    @Override
                    public void testFailure(Failure failure) {
                        Result r = m.get(failure.getDescription().getDisplayName());
                        r.setFailure(failure);
                    }

                };
                notifier.addListener(listener);
                runner.run(notifier);
            }
            testResults.addAll(m.values());
            Collections.sort(testResults);
        } catch (InitializationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return testResults;

    }

}
