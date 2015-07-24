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

import org.apache.karaf.features.Feature;
import org.apache.karaf.features.internal.FeatureImpl;
import org.codice.ddf.admin.application.service.Application;
import org.codice.ddf.admin.application.service.ApplicationService;
import org.codice.ddf.admin.application.service.ApplicationStatus;
import org.codice.ddf.admin.application.service.ApplicationStatus.ApplicationState;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class StatusApplicationCommandTest {
    private Logger logger = LoggerFactory.getLogger(StatusApplicationCommand.class);

    /**
     * Tests the {@link StatusApplicationCommand} class and all associated methods for the case
     * where there are no features not started and no bundles not started
     */
    @Test
    public void testStatusApplicationCommandNoError() {
        ApplicationService testAppService = mock(ApplicationServiceImpl.class);
        BundleContext bundleContext = mock(BundleContext.class);
        ServiceReference<ApplicationService> mockFeatureRef;
        mockFeatureRef = (ServiceReference<ApplicationService>) mock(ServiceReference.class);
        Application testApp = mock(ApplicationImpl.class);
        Feature testFeature = mock(FeatureImpl.class);
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(testFeature);
        ApplicationStatus testStatus = mock(ApplicationStatusImpl.class);

        StatusApplicationCommand statusApplicationCommand = new StatusApplicationCommand();
        statusApplicationCommand.appName = "TestApp";
        statusApplicationCommand.setBundleContext(bundleContext);

        when(testStatus.getErrorBundles()).thenReturn(new HashSet<Bundle>());
        when(testStatus.getErrorFeatures()).thenReturn(new HashSet<Feature>());
        when(testStatus.getState()).thenReturn(ApplicationState.ACTIVE);
        when(testFeature.getName()).thenReturn("TestFeature");
        when(testApp.getName()).thenReturn("TestApp");
        when(bundleContext.getServiceReference(ApplicationService.class)).thenReturn(mockFeatureRef);
        when(bundleContext.getService(mockFeatureRef)).thenReturn(testAppService);
        when(testAppService.getApplication("TestApp")).thenReturn(testApp);
        when(testAppService.getApplicationStatus(testApp)).thenReturn(testStatus);

        try {
            when(testApp.getFeatures()).thenReturn(featureSet);
            statusApplicationCommand.doExecute();
            verify(testAppService).getApplicationStatus(testApp);
        }catch(Exception e){
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link StatusApplicationCommand} class and all associated methods for the case
     * where there are features that have not been started
     */
    @Test
    public void testStatusApplicationCommandErrorFeatures() {
        ApplicationService testAppService = mock(ApplicationServiceImpl.class);
        BundleContext bundleContext = mock(BundleContext.class);
        ServiceReference<ApplicationService> mockFeatureRef;
        mockFeatureRef = (ServiceReference<ApplicationService>) mock(ServiceReference.class);
        Application testApp = mock(ApplicationImpl.class);
        Feature testFeature = mock(FeatureImpl.class);
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(testFeature);
        ApplicationStatus testStatus = mock(ApplicationStatusImpl.class);
        Set<Feature> errorFeatureSet = new HashSet<>();
        errorFeatureSet.add(testFeature);

        StatusApplicationCommand statusApplicationCommand = new StatusApplicationCommand();
        statusApplicationCommand.appName = "TestApp";
        statusApplicationCommand.setBundleContext(bundleContext);

        when(testStatus.getErrorBundles()).thenReturn(new HashSet<Bundle>());
        when(testStatus.getErrorFeatures()).thenReturn(errorFeatureSet);
        when(testStatus.getState()).thenReturn(ApplicationState.ACTIVE);
        when(testFeature.getName()).thenReturn("TestFeature");
        when(testApp.getName()).thenReturn("TestApp");
        when(bundleContext.getServiceReference(ApplicationService.class)).thenReturn(mockFeatureRef);
        when(bundleContext.getService(mockFeatureRef)).thenReturn(testAppService);
        when(testAppService.getApplication("TestApp")).thenReturn(testApp);
        when(testAppService.getApplicationStatus(testApp)).thenReturn(testStatus);

        try {
            when(testApp.getFeatures()).thenReturn(featureSet);
            statusApplicationCommand.doExecute();
            verify(testAppService).getApplicationStatus(testApp);
        }catch(Exception e){
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link StatusApplicationCommand} class and all associated methods for the case
     * where there are bundles that have not been started
     */
    @Test
    public void testStatusApplicationCommandErrorBundles() {
        ApplicationService testAppService = mock(ApplicationServiceImpl.class);
        BundleContext bundleContext = mock(BundleContext.class);
        ServiceReference<ApplicationService> mockFeatureRef;
        mockFeatureRef = (ServiceReference<ApplicationService>) mock(ServiceReference.class);
        Application testApp = mock(ApplicationImpl.class);
        Feature testFeature = mock(FeatureImpl.class);
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(testFeature);
        ApplicationStatus testStatus = mock(ApplicationStatusImpl.class);
        Set<Bundle> errorBundleSet = new HashSet<>();
        Bundle testBundle = mock(Bundle.class);
        errorBundleSet.add(testBundle);

        StatusApplicationCommand statusApplicationCommand = new StatusApplicationCommand();
        statusApplicationCommand.appName = "TestApp";
        statusApplicationCommand.setBundleContext(bundleContext);

        when(testBundle.getBundleId()).thenReturn((long) 1);
        when(testBundle.getSymbolicName()).thenReturn("TestBundle");
        when(testStatus.getErrorBundles()).thenReturn(errorBundleSet);
        when(testStatus.getErrorFeatures()).thenReturn(new HashSet<Feature>());
        when(testStatus.getState()).thenReturn(ApplicationState.ACTIVE);
        when(testFeature.getName()).thenReturn("TestFeature");
        when(testApp.getName()).thenReturn("TestApp");
        when(bundleContext.getServiceReference(ApplicationService.class)).thenReturn(mockFeatureRef);
        when(bundleContext.getService(mockFeatureRef)).thenReturn(testAppService);
        when(testAppService.getApplication("TestApp")).thenReturn(testApp);
        when(testAppService.getApplicationStatus(testApp)).thenReturn(testStatus);

        try {
            when(testApp.getFeatures()).thenReturn(featureSet);
            statusApplicationCommand.doExecute();
            verify(testAppService).getApplicationStatus(testApp);
        }catch(Exception e){
            logger.info("Exception: ", e);
            fail();
        }
    }
}
