package com.anf.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.anf.core.constants.Constants;
import com.day.cq.dam.api.Asset;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Exercise - 1 ## Keshav ##
 * Servlet to populate Countries list in Country Component Dropdown.
 */

@Component(service = Servlet.class, immediate = true, property = {
		"description=Populate Country list Servlet",
		ServletResolverConstants.SLING_SERVLET_METHODS + "="
				+ HttpConstants.METHOD_GET,
		ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=anf/countrylist/datasource"})
@SuppressWarnings("serial")
public class CountryListServlet extends SlingSafeMethodsServlet {

	private static Logger log = LoggerFactory.getLogger(CountryListServlet.class);

	@Override
	protected void doGet(final SlingHttpServletRequest request,
						 final SlingHttpServletResponse response) throws ServletException,
			IOException {
		
		request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());		  
		ResourceResolver resourceResolver = request.getResourceResolver();
		
		String artPath = request.getPathInfo();
		log.debug("artPath :" + artPath);		
		
		List<Resource> fakeResourceList = new ArrayList<Resource>();
		List<String> countries_list = new ArrayList<String>();

		Resource resource = resourceResolver.getResource(Constants.COUNTRY_LIST);
		Asset asset = resource.adaptTo(Asset.class);
		Resource original = asset.getOriginal();
		InputStream content = original.adaptTo(InputStream.class);
		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				content, StandardCharsets.UTF_8));
		br.readLine().replace("{","");
		while ((line = br.readLine()) != null) {
			String res = line.split(":")[0].replace("\"","");
			if(!line.contains("}")) {
				countries_list.add(res);
			}
		}

		if(null != artPath && artPath.contains(Constants.EN_PAGE_PATH)) {
			getResourceList(resourceResolver, fakeResourceList, countries_list);
		}

		DataSource ds = new SimpleDataSource(fakeResourceList.iterator());
		request.setAttribute(DataSource.class.getName(), ds);
	}

	/**
	 * @param resourceResolver
	 * @param fakeResourceList
	 * @param countries
	 */
	private void getResourceList(ResourceResolver resourceResolver, List<Resource> fakeResourceList,
			List<String> countries) {
		ValueMap vm;
		for (String country: countries) {
			vm = new ValueMapDecorator(new HashMap<String, Object>());		 
			vm.put("text",country);
			vm.put("value",country);
			fakeResourceList.add(new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", vm));
		}
	}
	

}