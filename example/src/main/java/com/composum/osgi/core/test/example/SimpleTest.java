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
 * Name: SimpleTest.java
 * Autor: Mirko Zeibig
 * Datum: 22.01.2013 08:25:15
 */
package com.composum.osgi.core.test.example;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.junit.*;

import com.composum.osgi.core.test.TestService;

/**
 * An example for a simple testcase.
 *
 * @author Mirko Zeibig
 */
@Component
@Service
public class SimpleTest implements TestService {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void a_ersterTest() {
        assertTrue(true);
    }

    @Test
    public void b_zweiterTest() {
        fail("Failure was intended");
    }

    @Test(expected=NullPointerException.class)
    public void c_dritterTest() {
        assertTrue(true);
    }

    @Test(expected=NullPointerException.class)
    public void d_vierterTest() {
        throw new NullPointerException();
    }

    @Test
    public void e_fueenfterTest() {
        throw new RuntimeException();
    }

    @Test(timeout=10)
    public void f_sechsterTest() throws Exception {
        Thread.sleep(100);
    }

    @Test
    public void g_siebenterTest() {
        assertTrue("this is my message...", false);
    }

    @Ignore
    public void h_keinTest() {
        fail();
    }
}
