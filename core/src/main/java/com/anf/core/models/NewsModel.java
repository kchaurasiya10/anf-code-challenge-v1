package com.anf.core.models;

import java.util.Calendar;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Exercise 2 : News Feed Component - Keshav
 * Service to return the result (10 pages) after passing property name and value from the servlet
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "anf-code-challenge/components/news")
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true") })

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NewsModel {

	private static Logger LOG = LoggerFactory.getLogger(NewsModel.class);

	private static final String COMP_PATH = "/jcr:content/root/container/container";
	private static final String PARSYS_COMP = "wcm/foundation/components/responsivegrid";
	private static final String NEWS_COMP_PATH = "anf-code-challenge/components/news";

	@ResourcePath(path="/var/commerce/products/anf-code-challenge/newsData")
	Resource newsDataRes;

	private Session session;

	@Inject
	private ResourceResolver resourceResolver;

	@ScriptVariable
	Page currentPage;

	@Inject
	@Via("resource")
	private String title;

	@Inject
	@Via("resource")
	private String author;

	@Inject
	@Via("resource")
	private String description;

	@Inject
	@Via("resource")
	private String image;

	@PostConstruct
	private void init() throws RepositoryException {
		getNewsData();
	}

	public void getNewsData() throws RepositoryException {
		LOG.info("*** getNewsData - START  *** ");
		if (null != newsDataRes) {
			try {
				String currentPagePath = currentPage.getPath();
				Resource compResource = resourceResolver.resolve(currentPagePath + COMP_PATH);
				session = resourceResolver.adaptTo(Session.class);
				Iterator<Resource> ItrRes = newsDataRes.listChildren();
				int i = 0;
				while (ItrRes.hasNext()) {
					Resource newsRes = ItrRes.next();
					String title = newsRes.getValueMap().get("title", String.class);
					String author = newsRes.getValueMap().get("author", String.class);
					String description = newsRes.getValueMap().get("description", String.class);
					String image = newsRes.getValueMap().get("urlImage", String.class);
					if(null != compResource){
						Node node  = compResource.adaptTo(Node.class);
						if(!node.hasNode("news_" + i)) {
							Node compNode = node.addNode("news_" + i);
							compNode.setProperty("sling:resourceType", NEWS_COMP_PATH);
							compNode.setProperty("title", title);
							compNode.setProperty("author", author);
							compNode.setProperty("description", description);
							compNode.setProperty("image", image);
							i = i + 1;
							session.save();
							session.refresh(true);
						}
					}
				}
			}catch (Exception e) {
				LOG.error("*** Exception *** " + e.getMessage());
			}
		}
		LOG.info("*** getNewsData - End  *** ");
	}

	public String getTitle() {
		return this.title;
	}

	public String getAuthor() {
		return this.author;
	}

	public String getDescription() {
		return this.description;
	}

	public String getImage() {
		return this.image;
	}
}

