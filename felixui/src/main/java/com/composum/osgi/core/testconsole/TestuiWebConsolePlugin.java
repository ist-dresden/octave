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
 * Name: TestuiWebConsolePlugin.java
 * Autor: Mirko Zeibig
 * Datum: 06.03.2013 12:19:48
 */
package com.composum.osgi.core.testconsole;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.webconsole.DefaultVariableResolver;
import org.apache.felix.webconsole.SimpleWebConsolePlugin;
import org.apache.felix.webconsole.WebConsoleUtil;

import com.composum.osgi.core.test.Result;
import com.composum.osgi.core.test.TestExecutorService;

/**
 * @author Mirko Zeibig
 *
 */
@Component
@Service
@Property(name="felix.webconsole.label", value="octave")
public class TestuiWebConsolePlugin extends SimpleWebConsolePlugin {

    @Reference
    protected TestExecutorService testExecutorService;

    public TestuiWebConsolePlugin() {
        super("octave", "Octave", new String[]{"/octave/res/octave.css"});
    }

    /**
     * @see org.apache.felix.webconsole.AbstractWebConsolePlugin#renderContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void renderContent(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        if (testExecutorService == null) {
            final PrintWriter pw = res.getWriter();
            pw.println("<p class='statline'>No TestExecutorService found!</p>");
            return;
        }
        final List<Result> runTests = testExecutorService.runTests();
        int fc = 0;
        for(final Result r: runTests) {
            if (!r.isSuccess()) {
                fc++;
            }
        }
        final int successRate = runTests.size() > 0 ? 100 - (fc * 100 / runTests.size()) : 0;

        final PrintWriter pw = res.getWriter();
        final String topTemplate = readTemplateFile("/templates/top.html");
        final String bottomTemplate = readTemplateFile("/templates/bottom.html");
        ((DefaultVariableResolver) WebConsoleUtil.getVariableResolver(req)).put("successRate", successRate);

        pw.println(topTemplate);
        pw.println("<tbody class='ui-widget-content'>");
        int testNr = 0;
        for (final Result result:runTests) {
            testNr++;
            if(result.isSuccess()) {
                pw.println("<tr class='ok'>");
                pw.println("    <td>"+result.getDescription().getDisplayName()+"</td><td>ok</td><td>&nbsp;</td>");
                pw.println("</tr>");
            } else {
                pw.println("<tr class='failure'>");
                pw.println("    <td>"+result.getDescription().getDisplayName()+"</td><td>"+result.getFailure().getMessage()+"</td>");
                pw.println("    <td>");
                pw.println("        <div id='test-detail-button-"+testNr+"' class='detailButton bIcon ui-icon ui-icon-triangle-1-e' title='Show Details'>&nbsp;</div>");
                pw.println("        <div id='test-detail-"+testNr+"' style='display: none;'>");
                pw.println("            <pre>"+result.getFailure().getTrace()+"</pre>");
                pw.println("        </div>");
                pw.println("    </td>");
                pw.println("</tr>");
            }
        }
        pw.println("</tbody>");
        pw.println(bottomTemplate);
    }

}
