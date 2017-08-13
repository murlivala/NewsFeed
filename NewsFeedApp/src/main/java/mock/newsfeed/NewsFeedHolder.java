package mock.newsfeed;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedHolder {
    List<NewsData> newsData = new ArrayList<NewsData>();
    public void addNewsFeed(NewsData aFeed){
        newsData.add(aFeed);
    }

    public void addNewsFeed(int index,NewsData aFeed){
        newsData.add(index,aFeed);
    }

    public NewsData getNewsFeed(int index){
        return newsData.get(index);
    }

    public List<NewsData> getNewsDataList(){
        return newsData;
    }

    public void updateFeed(int index,NewsData aFeed){
        newsData.get(index).title = aFeed.title;
        newsData.get(index).description = aFeed.description;
        newsData.get(index).imgUrl = aFeed.imgUrl;
        newsData.get(index).image = aFeed.image;
        newsData.get(index).isDownloaded = aFeed.isDownloaded;
    }
}
