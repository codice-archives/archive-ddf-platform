/**
 * Copyright (c) Codice Foundation
 * <p>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.admin.application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationFileInstallerTest {

    private Logger logger = LoggerFactory.getLogger(ApplicationFileInstaller.class);

    /**
     * Tests the {@link ApplicationFileInstaller#install(File)} method
     */
    @Test
    public void testInstall() {
        ApplicationFileInstaller testInstaller = new ApplicationFileInstaller();
        File testFile = new File(File.class.getResource("/test-kar.zip").getPath());

        try {
            assertNotNull(testInstaller.install(testFile));
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationFileInstaller#getAppDetails(File)} method
     */
    @Test
    public void testGetAppDetails() {
        ApplicationFileInstaller testInstaller = new ApplicationFileInstaller();
        File testFile = new File(File.class.getResource("/test-kar.zip").getPath());
        ZipFileApplicationDetails testFileDetails;
        try {
            testFileDetails = testInstaller.getAppDetails(testFile);
            assertNotNull(testFileDetails);
            //Verify other stuff
            assertEquals("main-feature", testFileDetails.getName());
            assertEquals("1.0.1", testFileDetails.getVersion());
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }
}
