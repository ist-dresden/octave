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
import org.apache.felix.webconsole.SimpleWebConsolePlugin;

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


    private static final String FAILURE_COLOR = "#fef1ec";
    private static final String OK_COLOUR = "#E6EEBD";

    @Reference
    protected TestExecutorService testExecutorService;

    public TestuiWebConsolePlugin() {
        super("octave", "Octave", null);
    }

    /**
     * @see org.apache.felix.webconsole.AbstractWebConsolePlugin#renderContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void renderContent(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (testExecutorService == null) {
            PrintWriter pw = res.getWriter();
            pw.println("<p class='statline'>No TestExecutorService found!</p>");
            return;
        }
        List<Result> runTests = testExecutorService.runTests();
        int fc = 0;
        for(Result r: runTests) {
            if (!r.isSuccess()) {
                fc++;
            }
        }
        int successRate = runTests.size() > 0 ? 100 - (fc * 100 / runTests.size()) : 0;

        PrintWriter pw = res.getWriter();
        pw.println("<p class='statline'>Overall: "+successRate+"%</p>");
        pw.println("<div style='margin-bottom: 5px;'>");
        pw.println("    <div class=\"failureBar\" " +
                "style='background-color: " + FAILURE_COLOR + "; " +
                    "border: #9c9c9c 1px solid;" +
                    "height: 12px;" +
                    "width: 100%;'>");
        pw.println("        <div class=\"okBar\" " +
                "style=\"width: "+successRate+"%;" +
                        "background-color: " + OK_COLOUR + ";" +
                        "height: 12px;\"></div>");
        pw.println("    </div>");
        pw.println("</div>");
        pw.println("<form method='get' enctype='multipart/form-data' action='/system/console/octave'>");
        pw.println("<div class='ui-widget-header ui-corner-top buttonGroup'>");
        pw.println("    <button class='reloadButton' type='submit' name='reload'>Aktualisieren</button>");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("<table id='plugin_table' class='tablesorter nicetable noauto'>");
        pw.println("<thead>" +
                "<tr>" +
                    "<th class='col_Test'>Test</th>" +
                    "<th class='col_Result'>Result</th>" +
                    "<th class='col_Trace'>Trace</th>" +
                "</tr></thead>");
        pw.println("<tbody>");
        for (Result result:runTests) {
            if(result.isSuccess()) {
                pw.println("<tr class='ok' style='background-color: " + OK_COLOUR + ";'>");
                pw.println("    <td>"+result.getDescription().getDisplayName()+"</td><td>ok</td><td>&nbsp;</td>");
                pw.println("</tr>");
            } else {
                pw.println("<tr class='failure' style='background-color: " + FAILURE_COLOR + ";'>");
                pw.println("    <td>"+result.getDescription().getDisplayName()+"</td><td>"+result.getFailure().getMessage()+"</td><td><pre>"+result.getFailure().getTrace()+"</pre></td>");
                pw.println("</tr>");
            }
        }
        pw.println("</tbody>");
        pw.println("</table>");
        pw.println("<div class='ui-widget-header ui-corner-bottom buttonGroup'>&nbsp;</div>");
        pw.println("<p class='statline'>&nbsp;</p>");
    }

}
