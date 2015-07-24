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
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class ListApplicationCommandTest {
    private Logger logger = LoggerFactory.getLogger(ListApplicationCommand.class);

    /**
     * Tests the {@link ListApplicationCommand} class for active applications
     */
    @Test
    public void testListApplicationCommandActiveApp() {
        try{
            ApplicationStatus testStatus;
            Application testApp;
            Set<Application> testAppSet = new HashSet<>();
            ApplicationService testAppService = mock(ApplicationServiceImpl.class);

            BundleContext bundleContext = mock(BundleContext.class);
            ServiceReference<ApplicationService> mockFeatureRef;
            mockFeatureRef = (ServiceReference<ApplicationService>) mock(ServiceReference.class);

            Feature testFeature = mock(FeatureImpl.class);
            Set<Feature> featureSet = new HashSet<>();
            featureSet.add(testFeature);

            when(testAppService.getApplications()).thenReturn(testAppSet);
            testApp = mock(Application.class);
            testAppSet.add(testApp);
            testStatus = mock(ApplicationStatusImpl.class);
            when(testAppService.getApplicationStatus(testApp)).thenReturn(testStatus);
            when(testStatus.getState()).thenReturn(ApplicationStatus.ApplicationState.ACTIVE);

            when(testApp.getFeatures()).thenReturn(featureSet);

            when(bundleContext.getServiceReference(ApplicationService.class)).thenReturn(mockFeatureRef);
            when(bundleContext.getService(mockFeatureRef)).thenReturn(testAppService);

            ListApplicationCommand listApplicationCommand = new ListApplicationCommand();
            listApplicationCommand.setBundleContext(bundleContext);


            listApplicationCommand.doExecute();

            verify(testAppService).getApplications();
            verify(testAppService).getApplicationStatus(testApp);
        }catch(Exception e){
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ListApplicationCommand} class for inactive applications
     */
    @Test
    public void testListApplicationCommandInactiveApp() {
        try{
            ApplicationStatus testStatus;
            Application testApp;
            Set<Application> testAppSet = new HashSet<>();
            ApplicationService testAppService = mock(ApplicationServiceImpl.class);

            BundleContext bundleContext = mock(BundleContext.class);
            ServiceReference<ApplicationService> mockFeatureRef;
            mockFeatureRef = (ServiceReference<ApplicationService>) mock(ServiceReference.class);

            Feature testFeature = mock(FeatureImpl.class);
            Set<Feature> featureSet = new HashSet<>();
            featureSet.add(testFeature);

            when(testAppService.getApplications()).thenReturn(testAppSet);
            testApp = mock(Application.class);
            testAppSet.add(testApp);
            testStatus = mock(ApplicationStatusImpl.class);
            when(testAppService.getApplicationStatus(testApp)).thenReturn(testStatus);
            when(testStatus.getState()).thenReturn(ApplicationStatus.ApplicationState.INACTIVE);

            when(testApp.getFeatures()).thenReturn(featureSet);

            when(bundleContext.getServiceReference(ApplicationService.class)).thenReturn(mockFeatureRef);
            when(bundleContext.getService(mockFeatureRef)).thenReturn(testAppService);

            ListApplicationCommand listApplicationCommand = new ListApplicationCommand();
            listApplicationCommand.setBundleContext(bundleContext);


            listApplicationCommand.doExecute();

            verify(testAppService).getApplications();
            verify(testAppService).getApplicationStatus(testApp);
        }catch(Exception e){
            logger.info("Exception: ", e);
            fail();
        }
    }
    /**
     * Tests the {@link ListApplicationCommand} class for failed applications
     */
    @Test
    public void testListApplicationCommandFailedApp() {
        try{
            ApplicationStatus testStatus;
            Application testApp;
            Set<Application> testAppSet = new HashSet<>();
            ApplicationService testAppService = mock(ApplicationServiceImpl.class);

            BundleContext bundleContext = mock(BundleContext.class);
            ServiceReference<ApplicationService> mockFeatureRef;
            mockFeatureRef = (ServiceReference<ApplicationService>) mock(ServiceReference.class);

            Feature testFeature = mock(FeatureImpl.class);
            Set<Feature> featureSet = new HashSet<>();
            featureSet.add(testFeature);

            when(testAppService.getApplications()).thenReturn(testAppSet);
            testApp = mock(Application.class);
            testAppSet.add(testApp);
            testStatus = mock(ApplicationStatusImpl.class);
            when(testAppService.getApplicationStatus(testApp)).thenReturn(testStatus);
            when(testStatus.getState()).thenReturn(ApplicationStatus.ApplicationState.FAILED);

            when(testApp.getFeatures()).thenReturn(featureSet);

            when(bundleContext.getServiceReference(ApplicationService.class)).thenReturn(mockFeatureRef);
            when(bundleContext.getService(mockFeatureRef)).thenReturn(testAppService);

            ListApplicationCommand listApplicationCommand = new ListApplicationCommand();
            listApplicationCommand.setBundleContext(bundleContext);


            listApplicationCommand.doExecute();

            verify(testAppService).getApplications();
            verify(testAppService).getApplicationStatus(testApp);
        }catch(Exception e){
            logger.info("Exception: ", e);
            fail();
        }
    }/**
     * Tests the {@link ListApplicationCommand} class for unknown status applications
     */
    @Test
    public void testListApplicationCommandUnknownApp() {
        try{
            ApplicationStatus testStatus;
            Application testApp;
            Set<Application> testAppSet = new HashSet<>();
            ApplicationService testAppService = mock(ApplicationServiceImpl.class);

            BundleContext bundleContext = mock(BundleContext.class);
            ServiceReference<ApplicationService> mockFeatureRef;
            mockFeatureRef = (ServiceReference<ApplicationService>) mock(ServiceReference.class);

            Feature testFeature = mock(FeatureImpl.class);
            Set<Feature> featureSet = new HashSet<>();
            featureSet.add(testFeature);

            when(testAppService.getApplications()).thenReturn(testAppSet);
            testApp = mock(Application.class);
            testAppSet.add(testApp);
            testStatus = mock(ApplicationStatusImpl.class);
            when(testAppService.getApplicationStatus(testApp)).thenReturn(testStatus);
            when(testStatus.getState()).thenReturn(ApplicationStatus.ApplicationState.UNKNOWN);

            when(testApp.getFeatures()).thenReturn(featureSet);

            when(bundleContext.getServiceReference(ApplicationService.class)).thenReturn(mockFeatureRef);
            when(bundleContext.getService(mockFeatureRef)).thenReturn(testAppService);

            ListApplicationCommand listApplicationCommand = new ListApplicationCommand();
            listApplicationCommand.setBundleContext(bundleContext);


            listApplicationCommand.doExecute();

            verify(testAppService).getApplications();
            verify(testAppService).getApplicationStatus(testApp);
        }catch(Exception e){
            logger.info("Exception: ", e);
            fail();
        }
    }

}
