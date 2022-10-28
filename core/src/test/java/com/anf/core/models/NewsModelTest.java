package com.anf.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.RepositoryException;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class NewsModelTest {
private final AemContext aemContext = new AemContext();
public final static String JUNIT_TEST_CONTENT_ROOT ="/content/anf-code-challenge/us/en/page-2/jcr:content/root/container/container/news";
NewsModel newsfeed;
protected Resource currentResc;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(NewsModel.class);
        aemContext.load().json("/newsModel/NewsModelTest.json","/component");
        aemContext.load().json("/newsModel/Page.json","/page");
    }

    @Test
    void getNewsData() throws RepositoryException {
        aemContext.load().json("/newsModel/Page.json", JUNIT_TEST_CONTENT_ROOT);
        currentResc = aemContext.resourceResolver().getResource(JUNIT_TEST_CONTENT_ROOT);
        assertNotNull(currentResc);
        newsfeed = currentResc.adaptTo(NewsModel.class);

    }

    @Test
    void getTitle() {
        aemContext.currentResource("/component/newsfeed");
        newsfeed = aemContext.request().adaptTo(NewsModel.class);
        final String expected = "UFC 273: Five things we learned as Alexander Volkanovski dominates 'Korean Zombie'";
        String actual = newsfeed.getTitle();
        assertEquals(expected, actual);
    }

    @Test
    void getAuthor() {
        aemContext.currentResource("/component/newsfeed");
        newsfeed = aemContext.request().adaptTo(NewsModel.class);
        final String expected = "Caroline Fox";
        String actual = newsfeed.getAuthor();
        assertEquals(expected, actual);
    }

    @Test
    void getDescription() {
        aemContext.currentResource("/component/newsfeed");
        newsfeed = aemContext.request().adaptTo(NewsModel.class);
        final String expected = "BBC Sport looks at the big talking points from UFC 273 as Alexander Volkanovski beats the 'Korean Zombie' Chan Sung Jung to retain his featherweight title.";
        String actual = newsfeed.getDescription();
        assertEquals(expected, actual);
    }

    @Test
    void getImage() {
        aemContext.currentResource("/component/newsfeed");
        newsfeed = aemContext.request().adaptTo(NewsModel.class);
        final String expected = "https://ichef.bbci.co.uk/live-experience/cps/624/cpsprodpb/B536/production/_124109364_gettyimages-1390585815.jpg";
        String actual = newsfeed.getImage();
        assertEquals(expected, actual);
    }
}