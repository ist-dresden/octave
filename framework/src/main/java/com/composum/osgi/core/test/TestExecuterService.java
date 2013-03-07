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
 * Name: TestExecuterService.java
 * Autor: Mirko Zeibig
 * Datum: 22.01.2013 08:11:08
 */
package com.composum.osgi.core.test;

import java.util.List;

/**
 * Serviceinterface to execute all found TestServices.
 *
 * @author Mirko Zeibig
 */
public interface TestExecuterService {

    /**
     * Runs all found tests.
     *
     * @return execution results
     */
    List<Result> runTests();

}