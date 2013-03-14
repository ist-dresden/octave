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
 * Name: OctaveMojo.java
 * Autor: Mirko Zeibig
 * Datum: 13.03.2013 15:17:22
 */
package com.composum.osgi.core.test.maven;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.google.gson.Gson;

/**
 * Executes Octave tests on a remote server.
 * @author Mirko Zeibig
 */
@Mojo(name = "octave", defaultPhase = LifecyclePhase.TEST, threadSafe = true)
public class OctaveMojo extends AbstractMojo {

    /**
     * Remote URL where to run the tests.
     */
    @Parameter(property="octave.url", defaultValue="http://localhost:8080/testrunner")
    private URL url;

    /**
     * If set to true, no tests are executed at all.
     */
    @Parameter(property = "skipTests", defaultValue = "false")
    private boolean skipTests;

    /**
     * If set to true, no tests are executed at all.
     */
    @Parameter(property = "maven.test.skip", defaultValue = "false")
    private boolean mavenTestSkip;

    /**
     * If set to true, test failures are ignored.
     */
    @Parameter(property="maven.test.failure.ignore", defaultValue = "false")
    private boolean mavenTestFailureIgnore;

    /**
     * unused. NYI
     */
    @Parameter(property="octave.username", required=false)
    private String username;

    /**
     * unused. NYI
     */
    @Parameter(property="octave.password", required=false)
    private String passowrd;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if(skipTests || mavenTestSkip) {
            getLog().info("Skipping all Octave tests, because 'skipTests' is set to true.");
        } else {
            getLog().info("running Octave tests @" + url);
            try {
                final HttpClient httpclient = new DefaultHttpClient();
                final HttpGet httpget = new HttpGet(new URI(url.toString()));
                final HttpResponse response = httpclient.execute(httpget);
                final StatusLine statusLine = response.getStatusLine();
                getLog().debug(statusLine.toString());
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (InputStream instream = entity.getContent())  {
                        final Result result = new Gson().fromJson(new InputStreamReader(instream), Result.class);
                        boolean failed = false;
                        for (final Test test: result.tests) {
                            getLog().info(" * name: " + test.name + ", success: " + test.success);
                            failed |= test.success;
                        }
                        if (failed && !mavenTestFailureIgnore) {
                            throw new MojoFailureException("Octave tests failed.");
                        } else if (failed && mavenTestFailureIgnore) {
                            getLog().warn("Octave test failures ignored, because 'maven.test.failure.ignore' is set to true.");
                        }
                    }
                }
            } catch (IOException | URISyntaxException e) {
                throw new MojoExecutionException("Error executing Octave tests: " + e.getMessage(), e);
            }
        }
    }

    protected static class Result {
        List<Test> tests;
    }

    protected static class Test {
        String name;
        boolean success;
    }
}
