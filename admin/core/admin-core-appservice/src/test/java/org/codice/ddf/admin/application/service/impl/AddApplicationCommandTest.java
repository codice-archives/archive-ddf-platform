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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.codice.ddf.admin.application.service.ApplicationService;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddApplicationCommandTest {

    private Logger logger = LoggerFactory.getLogger(AddApplicationCommand.class);

    /**
     * Tests the {@link AddApplicationCommand} class and its contained methods
     */
    @Test
    public void testAddApplicationCommand() {
        ApplicationService testAppService = mock(ApplicationServiceImpl.class);
        BundleContext bundleContext = mock(BundleContext.class);
        ServiceReference<ApplicationService> mockFeatureRef;
        mockFeatureRef = (ServiceReference<ApplicationService>) mock(ServiceReference.class);

        AddApplicationCommand addApplicationCommand = new AddApplicationCommand();
        addApplicationCommand.appName = "TestApp";
        addApplicationCommand.setBundleContext(bundleContext);

        when(bundleContext.getServiceReference(ApplicationService.class))
                .thenReturn(mockFeatureRef);
        when(bundleContext.getService(mockFeatureRef)).thenReturn(testAppService);

        try {
            addApplicationCommand.doExecute();
            verify(testAppService).addApplication(any(URI.class));
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }
}
