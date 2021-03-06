package mock.newsfeed;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Iterator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class JsonUtils extends AsyncTask<Void, Integer, String> {
    private static final String TAG = JsonUtils.class.getSimpleName();

    private Activity activity;
    private String jsonString;
    private ResponseCallback responseCallback;
    private boolean isNetworkFailure;

    public JsonUtils(Activity activity, String jsonData) {
        this.activity = activity;
        jsonString = jsonData;
    }

    public JsonUtils(String jsonData) {
        jsonString = jsonData;
    }

    public JsonUtils(){

    }

    @Provides
    @Singleton
    JsonUtils getJsonUtils(){
        return new JsonUtils(jsonString);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            if (InternetUtil.isInternetOn(activity)) {
                parseJson(jsonString);
            }else{
                isNetworkFailure = true;
            }

        } catch (Exception e) {
            return e.getMessage();
        }
        return jsonString;
    }

    protected void onProgressUpdate(Integer... index) {
        if(null != activity &&
                !activity.isFinishing()){
            if(index[0] == -1){
                responseCallback.onUpdate(Constants.UPDATE_TITLE,index[0]);
            }else{
                responseCallback.onUpdate(Constants.JSON_PARSE_PARTIAL,index[0]);
            }
        }else{
            cancel(true);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(null != activity &&
                !activity.isFinishing()){
            if(isNetworkFailure){
                new ShowErrorDialogAndCloseApp(activity).getAlert(activity.getString(R.string.network_error)).show();
            }
            responseCallback.onUpdate(Constants.JSON_PARSE_COMPLETED,0);
        }
    }

    public void parseJson(final String jsonData){

        /****************** Start Parse Response JSON Data *************/

        Log.d(TAG,"JsonUtils - parseJson ---- IN");
        try {
            JSONObject json = (JSONObject) new JSONTokener(jsonData).nextValue();
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String str = String.valueOf(keys.next());
                NewsData.newsTitle = str;
                publishProgress(-1);
                JSONArray jsonArray = json.getJSONArray(str);
                NewsData singleNewsData;
                NewsData.length = jsonArray.length();
                if(null == NewsUtils.getsNewsFeedHolder()){
                    NewsUtils.setNewsFeedHolder(new NewsFeedHolder());
                }
                for(int i=0;i<NewsData.length;i++){
                    NewsUtils.getsNewsFeedHolder().addNewsFeed(new NewsData());
                }

                for(int index=0 ; index < NewsData.length;index++){
                    if(isCancelled()){
                        Log.d(TAG, "#### doInBG-----------Cancelled");
                        break;
                    }

                    JSONObject country = jsonArray.getJSONObject(index);
                    singleNewsData = new NewsData();
                    if(country.has("title")){
                        singleNewsData.title = country.getString("title");
                    }

                    if(country.has("description")){
                        singleNewsData.description = country.getString("description");
                    }

                    if(country.has("imageHref")){
                        singleNewsData.imgUrl = country.getString("imageHref");
                    }
                    if(null != activity){
                        /**
                         * initially dummy images,
                         * same will be used for newsfeed items which don't have any images
                         */
                        singleNewsData.image = activity.getDrawable(R.drawable.no_image_60_60);
                    }
                    NewsUtils.getsNewsFeedHolder().updateFeed(index,singleNewsData);
                    publishProgress(index);
                }

            }

            /****************** End Parse Response JSON Data *************/

        } catch (JSONException e) {
            Log.d(TAG,"NewsFeedActivity - parseJson -------- Error parsing jSon:"+e.getMessage());
            e.printStackTrace();
			responseCallback.onUpdate(Constants.DIALOG_DISMISS,0);
        }
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

}
