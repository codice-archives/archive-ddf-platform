/**
 * Copyright (c) Codice Foundation
 * <p/>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.admin.application.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApplicationServiceExceptionTest {
    /**
     * Tests the {@link ApplicationServiceException#ApplicationServiceException(String)} constructor
     */
    @Test
    public void testApplicationServiceExceptionStringParam() {
        try{
            throw new ApplicationServiceException("TestMessage");
        }catch(Exception e){
            assertEquals("TestMessage", e.getMessage());
        }
    }

    /**
     * Tests the {@link ApplicationServiceException#ApplicationServiceException(String, Throwable)} constructor
     */
    @Test
    public void testApplicationServiceExceptionStringThrowableParams() {
        try {
            Throwable testThrowable = new Throwable("ThrowableMessage");
            throw new ApplicationServiceException("TestMessage", testThrowable);
        }catch(Exception e) {
            assertEquals("TestMessage", e.getMessage());
            assertEquals("ThrowableMessage", e.getCause().getMessage());
        }
    }
    /**
     * Tests the {@link ApplicationServiceException#ApplicationServiceException(Throwable)} constructor
     */
    @Test
    public void testApplicationServiceExceptionThrowableParam() {
        try{
            Throwable testThrowable = new Throwable("ThrowableMessage");
            throw new ApplicationServiceException(testThrowable);
        }catch(Exception e){
            assertEquals("ThrowableMessage", e.getCause().getMessage());
        }
    }
}
