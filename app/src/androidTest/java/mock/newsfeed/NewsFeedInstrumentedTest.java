package mock.newsfeed;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NewsFeedInstrumentedTest {

    String testUrl = Constants.SERVICE_URL;

    @Test
    public void testService() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        NewsFeedComponent component = DaggerNewsFeedComponent.builder().serviceDataClass(new ServiceDataClass(appContext,"https://api.myjson.com/bins/m47pd")).build();
        ServiceDataClass serviceDataClass = component.getService();
        assertNotEquals(null, serviceDataClass);

    }

    @Test
    public void testJsonUtils() throws Exception {
        NewsFeedComponent component = DaggerNewsFeedComponent.builder().jsonUtils(new JsonUtils("")).build();
        JsonUtils jsonUtils = component.getJsonUtils();
        assertNotEquals(null, jsonUtils);

    }
    @Test
    @UiThreadTest
    public void testJsonResults()  throws Throwable{
        String jsonResult = InternetUtil.sendHttpRequest(testUrl,"");
        assertNotEquals(null,jsonResult);
    }

    @Test
    public void testNewsfeedCount() throws Exception {

        String jsonResult = InternetUtil.sendHttpRequest(testUrl,"");
        NewsFeedComponent jsonUtilsComp = DaggerNewsFeedComponent.builder().jsonUtils(new JsonUtils("")).build();
        JsonUtils jsonUtils = jsonUtilsComp.getJsonUtils();
        jsonUtils.parseJson(jsonResult);
        assertEquals(10,NewsData.length);
    }
    @Test
    public void testJsonTitle() throws Exception {

        String jsonResult = InternetUtil.sendHttpRequest(testUrl,"");
        NewsFeedComponent jsonUtilsComp = DaggerNewsFeedComponent.builder().jsonUtils(new JsonUtils("")).build();
        JsonUtils jsonUtils = jsonUtilsComp.getJsonUtils();
        jsonUtils.parseJson(jsonResult);
        assertEquals("rows",NewsData.newsTitle);
    }
    @Test
    public void testFirstNewsFeedTitle() throws Exception {

        String jsonResult = InternetUtil.sendHttpRequest(testUrl,"");
        NewsFeedComponent jsonUtilsComp = DaggerNewsFeedComponent.builder().jsonUtils(new JsonUtils("")).build();
        JsonUtils jsonUtils = jsonUtilsComp.getJsonUtils();
        jsonUtils.parseJson(jsonResult);
        assertEquals("Item 1",NewsUtils.getsNewsFeedHolder().getNewsFeed(0).title);
    }

    @Test
    public void testLastNewsFeedTitle() throws Exception {

        String jsonResult = InternetUtil.sendHttpRequest(testUrl,"");
        NewsFeedComponent jsonUtilsComp = DaggerNewsFeedComponent.builder().jsonUtils(new JsonUtils("")).build();
        JsonUtils jsonUtils = jsonUtilsComp.getJsonUtils();
        jsonUtils.parseJson(jsonResult);
        assertEquals("Item 10",NewsUtils.getsNewsFeedHolder().getNewsFeed(9).title);
    }
}