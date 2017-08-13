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

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        responseCallback.onUpdate(0);
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
                JSONArray jsonArray = json.getJSONArray(str);
                Log.d(TAG, "#### doInBG------- Total NewsFeeds:"+jsonArray.length());

                for(int index=0 ; index < jsonArray.length();index++){
                    if(isCancelled()){
                        Log.d(TAG, "#### doInBG-----------Cancelled");
                        break;
                    }

                    JSONObject country = jsonArray.getJSONObject(index);
                    if(country.has("title")){
                        Log.d(TAG, "#### doInBG------- title:"+country.getString("title"));
                    }

                    if(country.has("description")){
                        Log.d(TAG, "#### doInBG------- description:"+country.getString("description"));
                    }

                    if(country.has("imageHref")){
                        Log.d(TAG, "#### doInBG------- imageHref:"+country.getString("imageHref"));
                    }
                }

            }

            /****************** End Parse Response JSON Data *************/

        } catch (JSONException e) {
            Log.d(TAG,"NewsFeedActivity - parseJson -------- Error parsing jSon:"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

}
