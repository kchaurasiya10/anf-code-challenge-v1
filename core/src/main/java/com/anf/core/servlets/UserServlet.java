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

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Exercise - 1 ## Keshav ##
 * Servlet to get the forms values and processing the response after executing the logic
 */

@Component(service = Servlet.class, immediate = true, property = { "description=User Servlet",
        ServletResolverConstants.SLING_SERVLET_METHODS + "="
                + HttpConstants.METHOD_POST,
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/saveUserDetails" })
public class UserServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(UserServlet.class);
    /** Age Node path To read min and max age property value  */
    private static final String RESOURCE_PATH= "/etc/age";
    /**  save the user details on path node after successful validation  */
    private static final String SAVING_NODE_RESOURCE_PATH= "/var/anf-code-challenge";
    private Session session;

    @Override
    protected void doPost(@NotNull SlingHttpServletRequest req, @NotNull SlingHttpServletResponse res) throws ServletException, IOException {
        ResourceResolver resourceResolver = req.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        session = resourceResolver.adaptTo(Session.class);
        String firstName = req.getRequestParameter("fname").getString();
        String lastName = req.getRequestParameter("lname").getString();
        int age = Integer.parseInt(String.valueOf(req.getRequestParameter("age")));
        String country = req.getRequestParameter("country").getString();
        Resource resource =  resourceResolver.getResource(RESOURCE_PATH);
        Resource targetRes = resourceResolver.getResource(SAVING_NODE_RESOURCE_PATH);
        if(null != resource){
            int minAge = Integer.parseInt(resource.getValueMap().get("minAge", String.class));
            int maxAge = Integer.parseInt(resource.getValueMap().get("maxAge", String.class));
            if(age >= minAge && age <= maxAge){
                if(null != targetRes){
                    Node node = targetRes.adaptTo(Node.class);
                    try {
                        node.setProperty("firstName", firstName);
                        node.setProperty("lastName",lastName);
                        node.setProperty("age", age);
                        node.setProperty("country", country);
                        session.save();
                        session.refresh(true);
                        res.setContentType("text/plain");
                        res.getWriter().write("You are eligible : User details saved Successfully");
                    } catch (RepositoryException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                res.setContentType("text/plain");
                res.getWriter().write("You are not eligible");
            }
        }
    }
}
