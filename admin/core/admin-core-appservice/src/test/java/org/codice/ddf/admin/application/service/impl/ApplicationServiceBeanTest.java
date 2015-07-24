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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.karaf.features.BundleInfo;
import org.apache.karaf.features.Feature;
import org.codice.ddf.admin.application.plugin.ApplicationPlugin;
import org.codice.ddf.admin.application.rest.model.FeatureDetails;
import org.codice.ddf.admin.application.service.Application;
import org.codice.ddf.admin.application.service.ApplicationNode;
import org.codice.ddf.admin.application.service.ApplicationService;
import org.codice.ddf.admin.application.service.ApplicationServiceException;
import org.codice.ddf.admin.application.service.ApplicationStatus;
import org.codice.ddf.ui.admin.api.ConfigurationAdminExt;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationServiceBeanTest {
    public ApplicationService testAppService;

    public ConfigurationAdminExt testConfigAdminExt;

    ApplicationNode testNode1;

    ApplicationNode testNode2;

    ApplicationNode testNode3;

    Application testApp;

    ApplicationStatus testStatus;

    Set<ApplicationNode> nodeSet;

    Set<ApplicationNode> childrenSet;

    BundleContext bundleContext;

    private Logger logger = LoggerFactory.getLogger(ApplicationServiceBeanMBean.class);

    @Before
    public void setUp() throws Exception {
        testAppService = mock(ApplicationServiceImpl.class);
        testConfigAdminExt = mock(ConfigurationAdminExt.class);
        testApp = mock(ApplicationImpl.class);

        when(testApp.getName()).thenReturn("TestApp");
        when(testApp.getVersion()).thenReturn("0.0.0");
        when(testApp.getDescription()).thenReturn("Test app for testGetApplicationTree");
        when(testApp.getURI()).thenReturn(
                getClass().getClassLoader().getResource("test-features-with-main-feature.xml")
                        .toURI());
        bundleContext = mock(BundleContext.class);
    }

    /**
     * Tests the {@link ApplicationServiceBean#ApplicationServiceBean(ApplicationService, ConfigurationAdminExt)}
     * constructor
     */
    @Test
    public void testApplicationServiceBeanConstructor() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#init()} method
     */
    @Test
    public void testInit() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            serviceBean.init();
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }

        //Do it twice, should remove and re-init
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            serviceBean.init();
            serviceBean.init();
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#destroy()} method
     */
    @Test
    public void testDestroy() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            serviceBean.init();
            serviceBean.destroy();
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#getInstallationProfiles()} method
     */
    @Test
    public void testGetInstallationProfiles() {
        Feature testFeature1 = mock(Feature.class);
        Feature testFeature2 = mock(Feature.class);
        Feature testFeature3 = mock(Feature.class);
        Feature testFeature4 = mock(Feature.class);

        when(testFeature1.getName()).thenReturn("TestFeature1");
        when(testFeature2.getName()).thenReturn("TestFeature2");
        when(testFeature3.getName()).thenReturn("TestFeature3");
        when(testFeature4.getName()).thenReturn("TestFeature4");
        when(testFeature1.getDescription())
                .thenReturn("Mock Feature for ApplicationServiceBean tests");
        when(testFeature2.getDescription())
                .thenReturn("Mock Feature for ApplicationServiceBean tests");

        List<Feature> dependencies1 = new ArrayList<>();
        dependencies1.add(testFeature3);
        List<Feature> dependencies2 = new ArrayList<>();
        dependencies2.add(testFeature4);

        when(testFeature1.getDependencies()).thenReturn(dependencies1);
        when(testFeature2.getDependencies()).thenReturn(dependencies2);

        List<Feature> featureList = new ArrayList<>();
        featureList.add(testFeature1);
        featureList.add(testFeature2);
        when(testAppService.getInstallationProfiles()).thenReturn(featureList);

        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            serviceBean.init();
            assertNotNull(serviceBean.getInstallationProfiles());
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Sets up an application tree for use in testing
     *
     * @throws Exception
     */
    public void setUpTree() throws Exception {
        testNode1 = mock(ApplicationNodeImpl.class);
        testNode2 = mock(ApplicationNodeImpl.class);
        testNode3 = mock(ApplicationNodeImpl.class);

        testStatus = mock(ApplicationStatus.class);

        when(testNode1.getApplication()).thenReturn(testApp);
        when(testNode2.getApplication()).thenReturn(testApp);
        when(testNode3.getApplication()).thenReturn(testApp);

        when(testNode1.getStatus()).thenReturn(testStatus);
        when(testNode2.getStatus()).thenReturn(testStatus);
        when(testNode3.getStatus()).thenReturn(testStatus);

        nodeSet = new TreeSet<>();
        nodeSet.add(testNode1);
        nodeSet.add(testNode2);
        childrenSet = new TreeSet<>();
        childrenSet.add(testNode3);

        when(testAppService.getApplicationTree()).thenReturn(nodeSet);

        when(testStatus.getState()).thenReturn(ApplicationStatus.ApplicationState.ACTIVE);

        when(testNode1.getChildren()).thenReturn(childrenSet);
        when(testNode2.getChildren()).thenReturn(childrenSet);
        when(testNode3.getChildren()).thenReturn((new TreeSet<ApplicationNode>()));
    }

    /**
     * Tests the {@link ApplicationServiceBean#getApplicationTree()} method
     */
    @Test
    public void testGetApplicationTree() {
        try {
            setUpTree();
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            serviceBean.init();

            assertNotNull(serviceBean.getApplicationTree());
            verify(testApp, atLeastOnce()).getName();
            verify(testNode1).getChildren();
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#getApplications()} method
     */
    @Test
    public void testGetApplications() {
        try {
            setUpTree();
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            serviceBean.init();

            assertNotNull(serviceBean.getApplications());
            verify(testApp, atLeastOnce()).getName();
            verify(testNode1).getChildren();
            verify(testNode1, atLeastOnce()).getApplication();
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#getApplications()} method for the case where
     * the applications have no children
     */
    @Test
    public void testGetApplicationsNoChildren() {
        try {
            setUpTree();
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            serviceBean.init();

            when(testNode1.getChildren()).thenReturn((new TreeSet<ApplicationNode>()));
            when(testNode2.getChildren()).thenReturn((new TreeSet<ApplicationNode>()));
            when(testNode3.getChildren()).thenReturn((new TreeSet<ApplicationNode>()));

            assertNotNull(serviceBean.getApplications());

        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#getApplications()} method for the case where
     * a child node has dependencies
     */
    @Test
    public void testGetApplicationsChildDependencies() {
        try {
            setUpTree();
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            serviceBean.init();

            ApplicationNode testNode4 = mock(ApplicationNodeImpl.class);
            when(testNode4.getApplication()).thenReturn(testApp);
            when(testNode4.getStatus()).thenReturn(testStatus);
            Set<ApplicationNode> testNode3ChildrenSet = new TreeSet<>();
            testNode3ChildrenSet.add(testNode4);
            when(testNode3.getChildren()).thenReturn(testNode3ChildrenSet);

            assertNotNull(serviceBean.getApplications());

        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#getApplications()} method for the case where
     * more than one child node has dependencies
     */
    @Test
    public void testGetApplicationsMultiChildDependencies() {
        try {
            setUpTree();
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            serviceBean.init();

            ApplicationNode testNode4 = mock(ApplicationNodeImpl.class);
            when(testNode4.getApplication()).thenReturn(testApp);
            when(testNode4.getStatus()).thenReturn(testStatus);

            Set<ApplicationNode> testNode1ChildrenSet = new TreeSet<>();
            testNode1ChildrenSet.add(testNode2);
            testNode1ChildrenSet.add(testNode4);

            when(testNode1.getChildren()).thenReturn(testNode1ChildrenSet);

            assertNotNull(serviceBean.getApplications());
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#startApplication(String)} method
     */
    @Test
    public void testStartApplication() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);

            serviceBean.startApplication("TestApp");

            verify(testAppService).startApplication("TestApp");
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#startApplication(String)} method for the case where
     * an exception is thrown
     */
    @Test
    public void testStartApplicationException() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            doThrow(new ApplicationServiceException()).when(testAppService)
                    .startApplication("TestApp");

            assertFalse(serviceBean.startApplication("TestApp"));

            verify(testAppService).startApplication("TestApp");
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#stopApplication(String)}
     */
    @Test
    public void testStopApplication() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);

            serviceBean.stopApplication("TestApp");

            verify(testAppService).stopApplication("TestApp");
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#stopApplication(String)} method for the case where
     * an exception is thrown
     */
    @Test
    public void testStopApplicationException() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            doThrow(new ApplicationServiceException()).when(testAppService)
                    .stopApplication("TestApp");

            assertFalse(serviceBean.stopApplication("TestApp"));

            verify(testAppService).stopApplication("TestApp");
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#addApplications(List)} method
     */
    @Test
    public void testAddApplications() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            List<Map<String, Object>> testURLList = new ArrayList<>();
            Map<String, Object> testURLMap1 = mock(HashMap.class);
            when(testURLMap1.get("value")).thenReturn("TestMockURL1");
            Map<String, Object> testURLMap2 = mock(HashMap.class);
            when(testURLMap2.get("value")).thenReturn("TestMockURL2");
            testURLList.add(testURLMap1);
            testURLList.add(testURLMap2);

            serviceBean.addApplications(testURLList);

            verify(testURLMap1).get("value");
            verify(testURLMap2).get("value");
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#removeApplication(String)} method for the case where
     * the string parameter is valid
     */
    @Test
    public void testRemoveApplication() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);

            serviceBean.removeApplication("TestApp");

            verify(testAppService).removeApplication("TestApp");
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#removeApplication(String)} method for the case where
     * the string parameter is invalid
     */
    @Test
    public void testRemoveApplicationInvalidParam() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);

            serviceBean.removeApplication(StringUtils.EMPTY);

            verifyNoMoreInteractions(testAppService);
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#getServices(String)} method
     */
    @Test
    public void testGetServices() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt) {
                @Override
                protected BundleContext getContext() {
                    return bundleContext;
                }
            };
            Bundle[] bundles = {};
            when(bundleContext.getBundles()).thenReturn(bundles);

            List<Map<String, Object>> services = new ArrayList<>();
            Map<String, Object> testService1 = new HashMap<>();
            List<Map<String, Object>> testService1Configs = new ArrayList<>();
            Map<String, Object> testConfig1 = new HashMap<>();
            testConfig1.put("bundle_location", "TestLocation");
            testService1Configs.add(testConfig1);
            services.add(testService1);
            testService1.put("configurations", testService1Configs);

            BundleInfo testBundle1 = mock(BundleInfo.class);
            Set<BundleInfo> testBundles = new HashSet<>();
            testBundles.add(testBundle1);

            when(testApp.getBundles()).thenReturn(testBundles);
            when(testBundle1.getLocation()).thenReturn("TestLocation");
            when(testAppService.getApplication("TestApp")).thenReturn(testApp);
            when(testConfigAdminExt.listServices(any(String.class), any(String.class)))
                    .thenReturn(services);

            serviceBean.getServices("TestApp");
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#getApplicationPlugins()} method
     * and the {@link ApplicationServiceBean#setApplicationPlugins(List)} method
     */
    @Test
    public void testGetSetApplicationPlugins() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            ApplicationPlugin testPlugin1 = mock(ApplicationPlugin.class);
            ApplicationPlugin testPlugin2 = mock(ApplicationPlugin.class);
            List<ApplicationPlugin> pluginList = new ArrayList<>();
            pluginList.add(testPlugin1);
            pluginList.add(testPlugin2);

            serviceBean.setApplicationPlugins(pluginList);

            assertEquals(pluginList, serviceBean.getApplicationPlugins());

        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#getAllFeatures()} method
     */
    @Test
    public void testGetAllFeatures() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            List<FeatureDetails> testFeatureDetailsList = new ArrayList<>();
            FeatureDetails testFeatureDetails1 = mock(FeatureDetails.class);
            testFeatureDetailsList.add(testFeatureDetails1);
            when(testFeatureDetails1.getName()).thenReturn("TestFeatureDetails1");
            when(testFeatureDetails1.getVersion()).thenReturn("0.0.0");
            when(testFeatureDetails1.getStatus()).thenReturn("TestStatus");
            when(testFeatureDetails1.getRepository()).thenReturn("TestRepo");

            when(testAppService.getAllFeatures()).thenReturn(testFeatureDetailsList);

            assertNotNull(serviceBean.getAllFeatures());

        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#findApplicationFeatures(String)} method
     */
    @Test
    public void testFindApplicationFeatures() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);

            List<FeatureDetails> testFeatureDetailsList = new ArrayList<>();
            FeatureDetails testFeatureDetails1 = mock(FeatureDetails.class);
            testFeatureDetailsList.add(testFeatureDetails1);
            when(testFeatureDetails1.getName()).thenReturn("TestFeatureDetails1");
            when(testFeatureDetails1.getVersion()).thenReturn("0.0.0");
            when(testFeatureDetails1.getStatus()).thenReturn("TestStatus");
            when(testFeatureDetails1.getRepository()).thenReturn("TestRepo");

            when(testAppService.findApplicationFeatures("TestApp"))
                    .thenReturn(testFeatureDetailsList);

            assertNotNull(serviceBean.findApplicationFeatures("TestApp"));

        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationServiceBean#getPluginsForApplication(String)} method
     */
    @Test
    public void testGetPluginsForApplication() {
        try {
            ApplicationServiceBean serviceBean = new ApplicationServiceBean(testAppService,
                    testConfigAdminExt);
            ApplicationPlugin testPlugin1 = mock(ApplicationPlugin.class);
            ApplicationPlugin testPlugin2 = mock(ApplicationPlugin.class);
            List<ApplicationPlugin> pluginList = new ArrayList<>();
            pluginList.add(testPlugin1);
            pluginList.add(testPlugin2);
            Map<String, Object> plugin1JSON = new HashMap<>();
            plugin1JSON.put("TestAppJSON", "Plugin1");
            Map<String, Object> plugin2JSON = new HashMap<>();
            plugin1JSON.put("TestAppJSON", "Plugin1");

            when(testPlugin1.matchesAssocationName("TestApp")).thenReturn(true);
            when(testPlugin2.matchesAssocationName("TestApp")).thenReturn(true);
            when(testPlugin1.toJSON()).thenReturn(plugin1JSON);
            when(testPlugin2.toJSON()).thenReturn(plugin2JSON);

            serviceBean.setApplicationPlugins(pluginList);

            assertNotNull(serviceBean.getPluginsForApplication("TestApp"));
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }
}
