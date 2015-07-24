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
import java.util.List;
import java.util.Set;

import org.codice.ddf.admin.application.service.Application;
import org.codice.ddf.admin.application.service.ApplicationService;
import org.codice.ddf.admin.application.service.ApplicationStatus;
import org.codice.ddf.admin.application.service.ApplicationStatus.ApplicationState;
import org.junit.Test;

public class ActiveApplicationsCompleterTest {
    /**
     * Tests the {@link ActiveApplicationsCompleter#complete(String, int, List)} method,
     * as well as the associated constructor
     */
    @Test
    public void testActiveApplicationsCompleter() {
        ApplicationService testAppService = mock(ApplicationServiceImpl.class);
        Application testApp = mock(ApplicationImpl.class);
        Set<Application> appSet = new HashSet<>();
        appSet.add(testApp);
        ApplicationStatus testStatus = mock(ApplicationStatusImpl.class);
        ApplicationState testState = ApplicationState.ACTIVE;

        when(testAppService.getApplications()).thenReturn(appSet);
        when(testAppService.getApplicationStatus(testApp)).thenReturn(testStatus);
        when(testStatus.getState()).thenReturn(testState);
        when(testApp.getName()).thenReturn("TestApp");

        ActiveApplicationsCompleter activeApplicationsCompleter = new ActiveApplicationsCompleter(
                testAppService);

        assertNotNull(activeApplicationsCompleter.complete("Tes", 2, new ArrayList()));
    }
}
