package mock.newsfeed;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ListView;
import android.widget.Toast;

public class NewsFeedActivity extends AppCompatActivity implements ResponseCallback{
    private final String TAG = NewsFeedActivity.class.getSimpleName();
	private ListView newsList;
    CustomAdapter adapter = null;
    
    ProgressDialog mProgressDialog;
    NewsFeedHolder newsFeedHolder = new NewsFeedHolder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);
        newsList = (ListView)findViewById(R.id.menuList);
        NewsUtils.setNewsFeedHolder(newsFeedHolder);
        ServiceDataClass serviceDataClass = new ServiceDataClass(NewsFeedActivity.this,
                "https://api.myjson.com/bins/m47pd");
        serviceDataClass.setResponseCallback(this);
        serviceDataClass.execute();
        showProgressDialog("");
    }

    @Override
    public void onSuccess(String result) {
        JsonUtils jsonUtils = new JsonUtils(NewsFeedActivity.this,
                result);
        jsonUtils.setResponseCallback(this);
        jsonUtils.execute();
    }

    @Override
    public void onFailure(String errorMessage) {
        Toast.makeText(getApplicationContext(),"Error:"+errorMessage,Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void onUpdate(int state,int index){
        switch(state){
            case Constants.DIALOG_DISMISS:
                dismiss();
                break;
            case Constants.LIST_UPDATE:
                break;
            case Constants.JSON_PARSE_PARTIAL:
                update(index);
                break;
            case Constants.JSON_PARSE_COMPLETED:
                dismiss();
                break;
            case Constants.SHOW_DIALOG:
                showProgressDialog("");
                break;
            case Constants.UPDATE_TITLE:
                getSupportActionBar().setTitle(NewsData.newsTitle);
                break;

            default:
        }
    }

    public NewsData getNewsItem(int index){
        return newsFeedHolder.getNewsDataList().get(index);
    }

    public void update(int index){
        if(adapter == null){
            adapter = new CustomAdapter(this,this);
            newsList.setAdapter(adapter);
            getSupportActionBar().setTitle(NewsData.newsTitle);
        }
        adapter.updateNewsFeed();
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
