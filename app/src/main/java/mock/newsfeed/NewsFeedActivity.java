package mock.newsfeed;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NewsFeedActivity extends AppCompatActivity implements ResponseCallback{
    private final String TAG = NewsFeedActivity.class.getSimpleName();
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        ServiceDataClass serviceDataClass = new ServiceDataClass(NewsFeedActivity.this,
                "https://api.myjson.com/bins/m47pd");
        serviceDataClass.setResponseCallback(this);
        serviceDataClass.execute();
        showProgressDialog("");
    }

    @Override
    public void onSuccess(String result) {
        Log.d(TAG,"onSuccess - json Result:"+result);
        dismiss();
    }

    @Override
    public void onFailure(String errorMessage) {
        Toast.makeText(getApplicationContext(),"Error:"+errorMessage,Toast.LENGTH_SHORT).show();
        dismiss();
    }


    public void showProgressDialog(String message){
        if(null == message || "".equals(message)){
            message = "Updating";
        }
        if(null == mProgressDialog){
            mProgressDialog = ProgressDialog.show(this, "", message + "...", true, false);
            mProgressDialog.setCancelable(true);
        }
    }

    public void dismiss(){
        try {
            if (null != mProgressDialog && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }catch(IllegalArgumentException e){
            Log.e(TAG, "dismiss() - IllegalArgumentException: " + e.getMessage());
        }catch (Exception e){
            Log.e(TAG, "dismiss() - Exception: " + e.getMessage());
        }
    }

}
