package mock.newsfeed;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


public class ServiceDataClass extends AsyncTask<Void, Void, String> {
    private static final String TAG = ServiceDataClass.class.getSimpleName();

    Activity activity;
    String url;
    String jsonString;
    private ResponseCallback responseCallback;

    public ServiceDataClass(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            if (InternetUtil.isInternetOn(activity)) {
                Log.d(TAG, "doInBackground---------------------"+url);
                jsonString = InternetUtil.sendHttpRequest(url,"");

            }

        } catch (Exception e) {
            return e.getMessage();
        }
        return jsonString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (responseCallback != null) {
            if (result == null) {
                responseCallback.onFailure(null);
            } else {
                responseCallback.onSuccess(result);
            }
        }
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }
}
