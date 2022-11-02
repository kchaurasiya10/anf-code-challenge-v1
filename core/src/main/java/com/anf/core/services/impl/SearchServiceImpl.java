package com.anf.core.services.impl;
import com.anf.core.services.SearchService;
import com.anf.core.utils.ResolverUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exercise 3: Query JCR - Query Builder API - Keshav
 * Service to return the result (10 pages) after passing property name and value from the servlet
 */
@Component(service = SearchService.class, immediate = true)
public class SearchServiceImpl implements SearchService {

    private static final Logger LOG= LoggerFactory.getLogger(SearchServiceImpl.class);

    @Reference
    QueryBuilder queryBuilder;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    ResourceResolver resourceResolver;

    SearchResult result;

    Session session;

    Query query;

    JSONArray resultArray;

    JSONObject searchResult;

    @Activate
    public void activate(){
        LOG.info("\n ----ACTIVATE METHOD----");
    }

    public Map<String,String> createTextSearchQuery(String propertyName, String propertyValue){
        Map<String,String> queryMap=new HashMap<>();
        queryMap.put("path","/content/anf-code-challenge/us/en");
        queryMap.put("type","cq:Page");
        queryMap.put("property",propertyName);
        queryMap.put("property.value", propertyValue);
        queryMap.put("p.limit", "10");
        return queryMap;
    }

    @Override
    public JSONObject searchResult(String propertyName, String propertyValue){
        LOG.info(" ***Begin Code - Keshav *** ");
        searchResult = new JSONObject();
        try {
            resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            LOG.info(" *** Resource Resolver *** "+resourceResolver.getUserID());
            session = resourceResolver.adaptTo(Session.class);
            LOG.info(" *** session *** "+session.getUserID());
            query = queryBuilder.createQuery(PredicateGroup.create(createTextSearchQuery(propertyName, propertyValue)), session);
            result = query.getResult();

            List<Hit> hits =result.getHits();
            resultArray=new JSONArray();
            for(Hit hit: hits){
                Page page=hit.getResource().adaptTo(Page.class);
                JSONObject resultObject=new JSONObject();
                resultObject.put("title",page.getTitle());
                resultObject.put("path",page.getPath());
                resultArray.put(resultObject);
            }
            searchResult.put("results",resultArray);

        }catch (Exception e){
            LOG.info("\n ----ERROR -----{} ",e.getMessage());
        }
        return searchResult;
    }
}
