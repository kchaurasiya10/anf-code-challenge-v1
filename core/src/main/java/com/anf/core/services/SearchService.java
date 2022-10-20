package com.anf.core.services;

import org.json.JSONObject;
/**
 * Exercise 3: Query JCR - Query Builder API - Keshav
 * Search Service Interface
 */
public interface SearchService {
    public JSONObject searchResult(String propertyName, String propertyValue);
}
