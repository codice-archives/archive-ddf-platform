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
package org.codice.ddf.admin.application.rest.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.karaf.features.Feature;
import org.junit.Test;

public class FeatureDetailsTest {
    /**
     * Tests the {@link FeatureDetails#FeatureDetails(Feature, String, String)} constructor,
     * and all associated getters
     */
    @Test
    public void testFeatureDetails() {
        Feature testFeature = mock(Feature.class);
        when(testFeature.getName()).thenReturn("TestFeature");
        when(testFeature.getId()).thenReturn("001");
        when(testFeature.getVersion()).thenReturn("0.0.0");
        when(testFeature.getInstall()).thenReturn("TestInstallString");
        when(testFeature.getDescription()).thenReturn("Feature for testing FeatureDetails");
        when(testFeature.getDetails()).thenReturn("TestDetails");
        when(testFeature.getResolver()).thenReturn("TestResolver");

        FeatureDetails testDetails = new FeatureDetails(testFeature, "TestStatus", "TestRepo");

        assertEquals("TestFeature", testDetails.getName());
        assertEquals("001", testDetails.getId());
        assertEquals("0.0.0", testDetails.getVersion());
        assertEquals("TestInstallString", testDetails.getInstall());
        assertEquals("Feature for testing FeatureDetails", testDetails.getDescription());
        assertEquals("TestDetails", testDetails.getDetails());
        assertEquals("TestResolver", testDetails.getResolver());
        assertEquals("TestRepo", testDetails.getRepository());
        assertEquals("TestStatus", testDetails.getStatus());
    }
}
