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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URL;

import org.apache.karaf.features.FeaturesService;
import org.codice.ddf.admin.application.service.Application;
import org.codice.ddf.admin.application.service.ApplicationService;
import org.codice.ddf.admin.application.service.ApplicationServiceException;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * Tests the application config installer code
 */
public class ApplicationConfigInstallerTest {

    private static final String START_FEATURE = "startFeature";

    private static final String STOP_FEATURE = "stopFeature";

    private static final String BAD_FILE = "NOFILE.properties";

    private static final String GOOD_FILE = "applist.properties";

    private static final String EMPTY_FILE = "empty_applist.properties";

    private static final String INSTALL_FILE = "install_applist.properties";

    private static final String INVALID_FILE = "invalid_applist.properties";

    private static final String RUN_ASE_EX = "Could not start";

    private static final String RUN_NO_CONFIG = "No config file located, cannot load from it.";

    private static final String RUN_INVALID_URI = "Could not install application, location is not a valid URI";

    private static final String RUN_NO_FILE = "Could not file the configuration file";

    /**
     * Tests with a valid file that contains one application that all of the
     * services were called correctly.
     *
     * @throws Exception
     */
    @Test
    public void testFileValid() throws Exception {
        FeaturesService featuresService = mock(FeaturesService.class);
        ApplicationService appService = mock(ApplicationService.class);

        URL fileURL = this.getClass().getResource("/" + GOOD_FILE);
        ApplicationConfigInstaller configInstaller = new ApplicationConfigInstaller(
                fileURL.getFile(), appService, featuresService, START_FEATURE, STOP_FEATURE);

        configInstaller.run();

        // verify that the correct application was started
        verify(appService, never()).addApplication(any(URI.class));
        verify(appService).startApplication("solr-app");

        // verify the post start and post stop features were called
        verify(featuresService).installFeature(START_FEATURE);
        verify(featuresService).uninstallFeature(STOP_FEATURE);
    }

    /**
     * Tests with a valid file that contains one non-local application that all of the
     * services were called correctly.
     *
     * @throws Exception
     */
    @Test
    public void testFileInstall() throws Exception {
        FeaturesService featuresService = mock(FeaturesService.class);
        ApplicationService appService = mock(ApplicationService.class);

        URL fileURL = this.getClass().getResource("/" + INSTALL_FILE);
        ApplicationConfigInstaller configInstaller = new ApplicationConfigInstaller(
                fileURL.getFile(), appService, featuresService, START_FEATURE, STOP_FEATURE);

        configInstaller.run();

        // verify that the correct application was added and then started
        verify(appService).addApplication(new URI("file:/location/to/solr-app-1.0.0.kar"));
        verify(appService).startApplication("solr-app");

        // verify the post start and post stop features were called
        verify(featuresService).installFeature(START_FEATURE);
        verify(featuresService).uninstallFeature(STOP_FEATURE);
    }

    /**
     * Tests the use case that there is a file but it does not have any apps
     * listed in it (they are commented out). The test should not call the
     * features stop and start since no apps were loaded.
     *
     * @throws Exception
     */
    @Test
    public void testFileEmpty() throws Exception {
        FeaturesService featuresService = mock(FeaturesService.class);
        ApplicationService appService = mock(ApplicationService.class);

        URL fileURL = this.getClass().getResource("/" + EMPTY_FILE);
        ApplicationConfigInstaller configInstaller = new ApplicationConfigInstaller(
                fileURL.getFile(), appService, featuresService, START_FEATURE, STOP_FEATURE);

        configInstaller.run();

        // verify that the app service was never called
        verify(appService, never()).addApplication(any(URI.class));
        verify(appService, never()).startApplication(anyString());

        // verify the post start and post stop features were not called
        verify(featuresService, never()).installFeature(anyString());
        verify(featuresService, never()).uninstallFeature(anyString());
    }

    /**
     * Tests that when a file is not found, the post install start and post
     * install stop features are NOT called.
     */
    @Test
    public void testFileNotValid() throws Exception {
        FeaturesService featuresService = mock(FeaturesService.class);

        ApplicationConfigInstaller configInstaller = new ApplicationConfigInstaller(BAD_FILE, null,
                featuresService, START_FEATURE, STOP_FEATURE);

        configInstaller.run();

        // verify the post start and post stop features were not called
        verify(featuresService, never()).installFeature(anyString());
        verify(featuresService, never()).uninstallFeature(anyString());

    }

    /**
     * Tests the {@link ApplicationConfigInstaller#run()} method for the case
     * where an ApplicationServiceException is thrown by appService.addApplication(..)
     *
     * @throws Exception
     */
    @Test
    public void testRunASE() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

        FeaturesService featuresService = mock(FeaturesService.class);
        ApplicationService testAppService = mock(ApplicationServiceImpl.class);

        doThrow(new ApplicationServiceException()).when(testAppService)
                .startApplication(anyString());

        URL fileURL = this.getClass().getResource("/" + GOOD_FILE);
        ApplicationConfigInstaller configInstaller = new ApplicationConfigInstaller(
                fileURL.getFile(), testAppService, featuresService, START_FEATURE, STOP_FEATURE);

        configInstaller.run();

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent) argument).getFormattedMessage().contains(RUN_ASE_EX);
            }
        }));
    }

    /**
     * Tests the {@link ApplicationConfigInstaller#run()} method for the case
     * where the configFile doesn't exist
     *
     * @throws Exception
     */
    @Test
    public void testRunConfigFileNotExist() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);
        root.setLevel(Level.ALL);

        ApplicationConfigInstaller configInstaller = new ApplicationConfigInstaller(
                BAD_FILE, null, null, START_FEATURE, STOP_FEATURE);

        configInstaller.run();

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent) argument).getFormattedMessage().contains(RUN_NO_CONFIG);
            }
        }));
    }

    /**
     * Tests the {@link ApplicationConfigInstaller#run()} method to test if it handles
     * an invalid URI cleanly
     *
     * @throws Exception
     */
    @Test
    public void testRunInvalidURI() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

        ApplicationServiceImpl testAppService = mock(ApplicationServiceImpl.class);

        URL fileURL = this.getClass().getResource("/" + INVALID_FILE);
        ApplicationConfigInstaller configInstaller = new ApplicationConfigInstaller(
                fileURL.getFile(), testAppService, null, START_FEATURE, STOP_FEATURE);

        configInstaller.run();

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent) argument).getFormattedMessage().contains(RUN_INVALID_URI);
            }
        }));
    }
}
