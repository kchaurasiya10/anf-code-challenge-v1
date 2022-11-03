package com.anf.core.listeners;

import com.anf.core.utils.ResolverUtil;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Node;

/**
 * Excercise -4 *** Keshav ***
 * Event listens will trigger if any page created under news-room path (/content/anf-code-challenge/us/en)
 * And will add the property - pageCreated: {Boolean}true on the new page
 */

@Component(service = EventHandler.class,
        immediate = true,
        property = {
                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED",
                EventConstants.EVENT_FILTER +"=(path=/content/anf-code-challenge/us/en/*)"
        })
public class SavePropertyEventListener implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SavePropertyEventListener.class);
    private static final String PAGE_PATH= "/content/anf-code-challenge/us/en";


    @Reference
    ResourceResolverFactory resourceResolverFactory;

    ResourceResolver resourceResolver;

    public void handleEvent(final Event event) {
        LOG.info(" ***Begin Code - Keshav *** ");
        LOG.info("\n Resource event: {} at: {}", event.getTopic(), event.getProperty(SlingConstants.PROPERTY_PATH));
        try {
            resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            LOG.info(" *** Resource Resolver *** "+resourceResolver.getUserID());
            Resource resource = resourceResolver.getResource(event.getProperty(SlingConstants.PROPERTY_PATH).toString());
            Node node = resource.adaptTo(Node.class);
            if(null != node) {
                node.setProperty("pageCreated", true);
                resourceResolver.commit();
            }

        } catch (Exception e) {
            LOG.error("*** Exception *** " + e.getMessage());
        }finally {
            resourceResolver.close();
        }
        LOG.info(" *** END Code ***** ");
    }
}