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
 * Name: RegisterServletComponent.java
 * Autor: Mirko Zeibig
 * Datum: 13.03.2013 10:23:47
 */
package com.composum.osgi.core.test.httpinterface;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.*;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.composum.osgi.core.test.TestExecutorService;

/**
 * @author Mirko Zeibig
 *
 */
@Component(metatype=true, label="Octave Test HTTP interface - RegisterServletComponent")
public class RegisterServletComponent {

    @Reference
    private HttpService httpService;

    @Reference
    protected TestExecutorService testExecutorService;

    @Property(label="alias", value="/testrunner", description="alias name in the URI namespace at which the servlet is registered")
    private static final String ALIAS_PROP = "httpinterface.alias";

    /**
     * @see com.composum.osgi.core.test.httpinterface.RegisterServletComponent#activate()
     */
    @Activate
    public void activate(final ComponentContext context) throws ServletException, NamespaceException {
        TestExecutorServlet servlet = new TestExecutorServlet();
        String alias = String.valueOf(context.getProperties().get(ALIAS_PROP));
        servlet.bindTestExecutorService(testExecutorService);
        httpService.registerServlet(alias, servlet, null, null);
    }
}
