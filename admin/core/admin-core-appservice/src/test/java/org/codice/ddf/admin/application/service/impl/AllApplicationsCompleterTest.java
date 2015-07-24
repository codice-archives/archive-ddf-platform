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

import org.codice.ddf.admin.application.service.Application;
import org.codice.ddf.admin.application.service.ApplicationService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AllApplicationsCompleterTest {
    /**
     * Tests the {@link AllApplicationsCompleter#complete(String, int, List)} method,
     * and the {@link AllApplicationsCompleter#AllApplicationsCompleter(ApplicationService)} constructor
     */
    @Test
    public void testAllApplicationsCompleter() {
        Application testApp = mock(ApplicationImpl.class);
        ApplicationService testAppService = mock(ApplicationServiceImpl.class);
        Set<Application> testAppSet = new HashSet<>();
        testAppSet.add(testApp);
        when(testAppService.getApplications()).thenReturn(testAppSet);
        when(testApp.getName()).thenReturn("TestApp");

        AllApplicationsCompleter applicationsCompleter = new AllApplicationsCompleter(testAppService);

        assertNotNull(applicationsCompleter.complete("Tes", 2, new ArrayList()));
    }
}
