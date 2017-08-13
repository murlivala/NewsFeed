package mock.newsfeed;
import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {JsonUtils.class ,ServiceDataClass.class})
public interface NewsFeedComponent {
    JsonUtils getJsonUtils();
    ServiceDataClass getService();
}