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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.codice.ddf.admin.application.service.Application;
import org.codice.ddf.admin.application.service.ApplicationService;
import org.codice.ddf.admin.application.service.ApplicationStatus;
import org.junit.Test;

public class NotActiveApplicationsCompleterTest {
    /**
     * Tests the {@link NotActiveApplicationsCompleter} class
     */
    @Test
    public void testNotActiveApplicationsCompleter() {
        Application testApp = mock(ApplicationImpl.class);
        ApplicationService testAppService = mock(ApplicationServiceImpl.class);
        Set<Application> appSet = new HashSet<>();
        appSet.add(testApp);
        ApplicationStatus testStatus = mock(ApplicationStatusImpl.class);
        ApplicationStatus.ApplicationState testState = ApplicationStatus.ApplicationState.INACTIVE;

        when(testAppService.getApplications()).thenReturn(appSet);
        when(testAppService.getApplicationStatus(testApp)).thenReturn(testStatus);
        when(testStatus.getState()).thenReturn(testState);
        when(testApp.getName()).thenReturn("TestApp");

        NotActiveApplicationsCompleter activeApplicationsCompleter = new NotActiveApplicationsCompleter(
                testAppService);

        assertNotNull(activeApplicationsCompleter.complete("Tes", 2, new ArrayList()));
    }
}
