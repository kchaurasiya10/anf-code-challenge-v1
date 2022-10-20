/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anf.core.servlets;

import com.anf.core.services.ContentService;
import com.drew.lang.annotations.NotNull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Exercise 1: Test Form / Servlet - Keshav
 *
 */
@Component(service = Servlet.class, immediate = true, property = { "description=User Servlet",
        ServletResolverConstants.SLING_SERVLET_METHODS + "="
                + HttpConstants.METHOD_POST,
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/saveUserDetails" })
public class UserServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(UserServlet.class);
    private static final String RESOURCE_PATH= "/etc/age";
    private Session session;

    @Reference
    private ContentService contentService;

    @Override
    protected void doPost(@NotNull SlingHttpServletRequest req, @NotNull SlingHttpServletResponse res) throws ServletException, IOException {
        log.info(" *** inside Post method() *** ");
        ResourceResolver resourceResolver = req.getResourceResolver();
        session = resourceResolver.adaptTo(Session.class);
        String firstName = req.getRequestParameter("fname").getString();
        String lastName = req.getRequestParameter("lname").getString();
        String age = req.getRequestParameter("age").getString();
        Resource resource =  resourceResolver.getResource(RESOURCE_PATH);
        if(null != resource){
            String minAge = resource.getValueMap().get("minAge", String.class);
            String maxAge = resource.getValueMap().get("maxAge", String.class);
        }

    }
}
