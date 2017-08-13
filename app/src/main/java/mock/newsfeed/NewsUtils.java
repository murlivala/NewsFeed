package mock.newsfeed;

public class NewsUtils {
    private static NewsFeedHolder sNewsFeedHolder;
    public static void setNewsFeedHolder(NewsFeedHolder newsFeedHolder){
        sNewsFeedHolder = newsFeedHolder;
    }
    public static NewsFeedHolder getsNewsFeedHolder(){
        return sNewsFeedHolder;
    }
}
