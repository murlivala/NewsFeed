package mock.newsfeed;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Iterator;


public class JsonUtils extends AsyncTask<Void, Integer, String> {
    private static final String TAG = JsonUtils.class.getSimpleName();

    Activity activity;
    String jsonString;
    private ResponseCallback responseCallback;

    public JsonUtils(Activity activity, String jsonData) {
        this.activity = activity;
        jsonString = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            if (InternetUtil.isInternetOn(activity)) {
                Log.d(TAG, "#### doInBG---------------------");
                parseJson(jsonString);
            }

        } catch (Exception e) {
            return e.getMessage();
        }
        Log.d(TAG, "#### doInBG---------------------END");
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
            responseCallback.onUpdate(Constants.JSON_PARSE_COMPLETED,0);
        }
    }

    private void parseJson(final String jsonData){

        /****************** Start Parse Response JSON Data *************/

        Log.d(TAG,"JsonUtils - parseJson ---- IN");
        try {
            JSONObject json = (JSONObject) new JSONTokener(jsonData).nextValue();
            Log.d("","#### json : "+json);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String str = String.valueOf(keys.next());
                NewsData.newsTitle = str;
                publishProgress(-1);
                JSONArray jsonArray = json.getJSONArray(str);
                NewsData singleNewsData;
                NewsData.length = jsonArray.length();
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
                    singleNewsData.image = activity.getDrawable(R.drawable.no_image_60_60);
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
