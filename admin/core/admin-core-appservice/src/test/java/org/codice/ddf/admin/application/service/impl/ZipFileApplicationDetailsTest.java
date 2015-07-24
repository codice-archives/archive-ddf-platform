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
package org.codice.ddf.admin.application.service.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ZipFileApplicationDetailsTest {
    /**
     * Tests the {@link ZipFileApplicationDetails#ZipFileApplicationDetails(String, String)} constructor,
     * and the getters related to it
     */
    @Test
    public void testConstructor() {
        ZipFileApplicationDetails testZipFile = new ZipFileApplicationDetails("TestName", "0.0.0");

        assertEquals("TestName", testZipFile.getName());
        assertEquals("0.0.0", testZipFile.getVersion());

        testZipFile.setName("TestName2");
        testZipFile.setVersion("0.0.1");

        assertEquals("TestName2", testZipFile.getName());
        assertEquals("0.0.1", testZipFile.getVersion());
    }
}
