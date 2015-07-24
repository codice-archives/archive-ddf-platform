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
package org.codice.ddf.admin.application.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.codice.ddf.admin.application.service.ApplicationService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationUploadEndpointTest {
    private Logger logger = LoggerFactory.getLogger(ApplicationUploadEndpoint.class);

    /**
     * Tests the {@link ApplicationUploadEndpoint#update(MultipartBody, UriInfo)} method
     */
    @Test
    public void testApplicationUploadEndpointUpdate() {
        ApplicationService testAppService = mock(ApplicationService.class);
        MultipartBody testMultipartBody = mock(MultipartBody.class);
        UriInfo testUriInfo = mock(UriInfo.class);
        List<Attachment> attachmentList = new ArrayList<>();
        Attachment testAttach1 = mock(Attachment.class);
        attachmentList.add(testAttach1);
        ContentDisposition testDisp = mock(ContentDisposition.class);
        DataHandler testDataHandler = mock(DataHandler.class);

        when(testAttach1.getDataHandler()).thenReturn(testDataHandler);
        when(testAttach1.getContentDisposition()).thenReturn(testDisp);
        when(testMultipartBody.getAllAttachments()).thenReturn(attachmentList);

        try {
            File testFile = new File(File.class.getResource("/test-kar.zip").getPath());
            InputStream testIS = new FileInputStream(testFile);
            when(testDataHandler.getInputStream()).thenReturn(testIS);

            ApplicationUploadEndpoint applicationUploadEndpoint = new ApplicationUploadEndpoint(
                    testAppService);

            applicationUploadEndpoint
                    .setDefaultFileLocation("target/ApplicationUploadEndpointTest");
            assertNotNull(applicationUploadEndpoint.update(testMultipartBody, testUriInfo));
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }

    /**
     * Tests the {@link ApplicationUploadEndpoint#create(MultipartBody, UriInfo)} method
     */
    @Test
    public void testApplicationUploadEndpointCreate() {
        ApplicationService testAppService = mock(ApplicationService.class);
        MultipartBody testMultipartBody = mock(MultipartBody.class);
        UriInfo testUriInfo = mock(UriInfo.class);
        List<Attachment> attachmentList = new ArrayList<>();
        Attachment testAttach1 = mock(Attachment.class);
        attachmentList.add(testAttach1);
        ContentDisposition testDisp = mock(ContentDisposition.class);
        DataHandler testDataHandler = mock(DataHandler.class);

        when(testAttach1.getDataHandler()).thenReturn(testDataHandler);
        when(testAttach1.getContentDisposition()).thenReturn(testDisp);
        when(testMultipartBody.getAllAttachments()).thenReturn(attachmentList);

        try {
            File testFile = new File(File.class.getResource("/test-kar.zip").getPath());
            InputStream testIS = new FileInputStream(testFile);
            when(testDataHandler.getInputStream()).thenReturn(testIS);

            ApplicationUploadEndpoint applicationUploadEndpoint = new ApplicationUploadEndpoint(
                    testAppService);

            applicationUploadEndpoint
                    .setDefaultFileLocation("target/ApplicationUploadEndpointTest");
            assertNotNull(applicationUploadEndpoint.create(testMultipartBody, testUriInfo));
            verify(testAppService).addApplication(any(URI.class));
        } catch (Exception e) {
            logger.info("Exception: ", e);
            fail();
        }
    }
}
