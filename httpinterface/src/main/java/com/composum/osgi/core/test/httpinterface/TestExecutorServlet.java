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
 * Name: TestExecutorServlet.java
 * Autor: Mirko Zeibig
 * Datum: 13.03.2013 09:49:32
 */
package com.composum.osgi.core.test.httpinterface;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONWriter;
import org.junit.runner.notification.Failure;

import com.composum.osgi.core.test.Result;
import com.composum.osgi.core.test.TestExecutorService;

/**
 * @author Mirko Zeibig
 *
 */
public class TestExecutorServlet extends HttpServlet {

    protected TestExecutorService testExecutorService;

    public void bindTestExecutorService(final TestExecutorService testExecutorService) {
        this.testExecutorService = testExecutorService;
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final List<Result> runTests = this.testExecutorService.runTests();
        final JSONWriter jsonWriter = new org.json.JSONWriter(resp.getWriter());
        try {
            JSONWriter tests = jsonWriter.object().key("tests").array();
            for (final Result result : runTests) {
                final String name = result.getDescription().getDisplayName();
                final boolean success = result.isSuccess();
                final Failure failure = result.getFailure();
                final String message = success ? "ok" : failure.getMessage();
                final String trace = success ? "" : failure.getTrace();
                tests = tests.object()
                            .key("name").value(name)
                            .key("success").value(success)
                            .key("message").value(message)
                            .key("trace").value(trace)
                        .endObject();
            }
            tests.endArray().endObject();
        } catch (final JSONException e) {
            throw new ServletException("error writing JSON response: " + e.getMessage(), e);
        }
    }
}
