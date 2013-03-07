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
 * Name: Result.java
 * Autor: Mirko Zeibig
 * Datum: 21.01.2013 16:38:43
 */
package com.composum.osgi.core.test;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * Contains the result of the execution of one test.
 *
 * @author Mirko Zeibig
 */
public class Result implements Comparable<Result> {

    protected String message;
    protected String name;
    protected boolean success;
    protected boolean error = false;
    protected Failure failure;
    protected Description description;

    protected Result(final Description description) {
        this.description = description;
        this.success = true;
        this.message = description.toString();
    }

    /** */
    public String getName() {
        return name;
    }

    /**
     * @return true, if the test  succeeded
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return true, if the test failed
     */
    public boolean isError() {
        return error;
    }

    /**
     * @return a string description of the result
     */
    public String getMessage() {
        return message;
    }

    public Failure getFailure() {
        return failure;
    }

    public Description getDescription() {
        return description;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Result [message=" + message + ", name=" + name
                + ", success=" + success + ", error=" + error + "]";
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Result o) {
        return this.message.compareTo(o.message);
    }

    protected void setFailure(final Failure failure) {
        this.success = false;
        this.failure = failure;
        final Throwable throwable = failure.getException();
        if (!(throwable instanceof AssertionError)) {
            this.error = true;
            this.message = failure.getTestHeader() + ": " + throwable.toString();
        } else {
            this.message = failure.toString();
        }
    }

}
