package mock.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private final String TAG = CustomAdapter.class.getSimpleName();
    private Context mContext;
    private Activity mActivity;
    private List<NewsData> mList;

    public CustomAdapter(Context context, Activity activity) {
        mContext = context;
        mList = NewsUtils.getsNewsFeedHolder().getNewsDataList();
        if(activity instanceof NewsFeedActivity){
            mActivity = activity;
        }
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return NewsData.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return "";
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        try {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView item = null;
            TextView title;
            ImageView imageView;

            row = inflater.inflate(R.layout.row_newsfeed, parent, false);
            item = (TextView) row.findViewById(R.id.item);
            imageView = (ImageView) row.findViewById(R.id.imgIcon);
            title = (TextView) row.findViewById(R.id.txtTitle);

            if(mActivity instanceof NewsFeedActivity){
                title.setText(((NewsFeedActivity) mActivity).getNewsItem(position).title);
                item.setText(((NewsFeedActivity) mActivity).getNewsItem(position).description);
            }
            imageView.setImageDrawable(mList.get(position).image);


        }catch(Exception e){
            Log.d(TAG,"CustomAdapter - getView ERROR => "+e.getMessage());
        }

        return (row);
    }

    public void updateNewsFeed() {
        mList = NewsUtils.getsNewsFeedHolder().getNewsDataList();
        notifyDataSetChanged();
    }
}