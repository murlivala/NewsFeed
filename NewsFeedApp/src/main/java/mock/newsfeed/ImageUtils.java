package mock.newsfeed;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class ImageUtils extends AsyncTask<Void, Integer, String> {
    private static final String TAG = ImageUtils.class.getSimpleName();

    Activity activity;
    List<NewsData> mList;
    String jsonString;
    private ResponseCallback responseCallback;
    int first;
    int totalVisible;
    int length;

    public ImageUtils(Activity activity, int first,int totalVisible) {
        this.activity = activity;
        mList = NewsUtils.getsNewsFeedHolder().getNewsDataList();
        length = NewsData.length;
        this.first = first;
        this.totalVisible = totalVisible;
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
                for(int index = first;index < first+totalVisible;index++){
                    if(isCancelled()){
                        Log.d(TAG, "#### doInBG-----------Cancelled");
                        break;
                    }
                    if("".equals(mList.get(index).imgUrl)){
                        continue;
                    }
                    if(false == mList.get(index).isDownloaded){
                        Drawable drawable = getImageDrawable(mList.get(index).imgUrl);
                        if(null != drawable){
                            publishProgress(-1);
                            mList.get(index).image = drawable;
                            mList.get(index).isDownloaded = true;
                            NewsUtils.getsNewsFeedHolder().updateFeed(index,mList.get(index));
                            publishProgress(index);
                        }
                    }
                }
            }

        } catch (Exception e) {
            return e.getMessage();
        }
        Log.d(TAG, "#### doInBG---------------------END");
        return jsonString;
    }

    @Override
    protected void onProgressUpdate(Integer... index) {
        if(null != activity &&
                !activity.isFinishing()){
            if(index[0] == -1){
                responseCallback.onUpdate(Constants.SHOW_DIALOG,index[0]);
            }else{
                responseCallback.onUpdate(Constants.LIST_UPDATE,index[0]);
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
            responseCallback.onUpdate(Constants.IMAGE_DOWNLOAD_COMPLETED,0);
        }
    }

    private Drawable getImageDrawable(String url) {
        try {
            InputStream is = getInputStream(url);

            Drawable drawable = Drawable.createFromStream(is, url);

            return drawable;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private InputStream getInputStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection;
        connection = url.openConnection();
        connection.setUseCaches(true);
        connection.connect();
        InputStream response = connection.getInputStream();

        return response;
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

}
