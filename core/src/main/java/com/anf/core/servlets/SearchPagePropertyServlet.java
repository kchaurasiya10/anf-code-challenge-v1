package com.anf.core.servlets;

import com.anf.core.services.SearchService;
import com.anf.core.services.SearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
/**
 * Exercise 3: Query JCR - Query Builder API - Keshav
 * Servlet to return the result (10 pages) after passing property name and value
 * Example path : http://localhost:4502/api/velo/searchProperty?propertyName=jcr:content/anfCodeChallenge&propertyValue=true
 */

@Component(service = Servlet.class, immediate = true, property = { "description=Get Page Property Search Result",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/api/velo/searchProperty"})
public class SearchPagePropertyServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(SearchPagePropertyServlet.class);

    @Reference
    SearchService searchService;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        LOG.info(" ***Begin Code - Keshav *** ");
        JSONObject searchResult=null;
        try {
            String propertyName = req.getRequestParameter("propertyName").getString();
            String propertyValue = req.getRequestParameter("propertyValue").getString();
            searchResult=searchService.searchResult(propertyName, propertyValue);
        } catch (Exception e) {
            LOG.info("\n ERROR {} ", e.getMessage());
        }

        resp.setContentType("application/json");
        resp.getWriter().write(searchResult.toString());
            LOG.info(" *** END Code ***** ");
    }

}
