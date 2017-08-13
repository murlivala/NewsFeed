package mock.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceDataClass extends AsyncTask<Void, Void, String> {
    private static final String TAG = ServiceDataClass.class.getSimpleName();

    Activity activity;
    Context mContext;
    String url;
    String jsonString;
    private ResponseCallback responseCallback;

    public ServiceDataClass(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    public ServiceDataClass(Context context, String url) {
        mContext = context;
        this.url = url;
        Log.d("","#### ServiceDataClass -----  cTor:"+url);
    }
    public ServiceDataClass(){

    }

    @Provides
    @Singleton
    ServiceDataClass getServiceDataClass(){
        return new ServiceDataClass(mContext,url);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            if (InternetUtil.isInternetOn(activity)) {
                Log.d("","#### ServiceDataClass -----  DoInBG");
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

        if(null != activity &&
                !activity.isFinishing()){
            if (responseCallback != null) {
                if (result == null) {
                    responseCallback.onFailure(null);
                } else {
                    responseCallback.onSuccess(result);
                }
            }
        }
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public String getJsonResult(){
        return jsonString;
    }
}
