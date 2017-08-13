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
    List<NewsData> iList;
    String jsonString;
    private ResponseCallback responseCallback;
    int first;
    int totalVisible;
    int length;

    public ImageUtils(Activity activity, int first,int totalVisible) {
        this.activity = activity;
        iList = NewsUtils.getsNewsFeedHolder().getNewsDataList();
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
                    if("".equals(iList.get(index).imgUrl)){
                        continue;
                    }
                    if(false == iList.get(index).isDownloaded){
                        Drawable drawable = getImageDrawable(iList.get(index).imgUrl);
                        if(null != drawable){
                            publishProgress(-1);
                            iList.get(index).image = drawable;
                            iList.get(index).isDownloaded = true;
                            NewsUtils.getsNewsFeedHolder().updateFeed(index,iList.get(index));
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
        if(index[0] == -1){
            responseCallback.onUpdate(Constants.SHOW_DIALOG,index[0]);
        }else{
            responseCallback.onUpdate(Constants.LIST_UPDATE,index[0]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        responseCallback.onUpdate(Constants.IMAGE_DOWNLOAD_COMPLETED,0);
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
