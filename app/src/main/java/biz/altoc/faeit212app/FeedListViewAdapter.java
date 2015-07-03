package biz.altoc.faeit212app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

class FeedListViewAdapter<FeedItem> extends ArrayAdapter {


    public FeedListViewAdapter(Context context, int resource, ArrayList<FeedItem> feedItems) {
        super(context, resource, feedItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return super.getView(position, convertView, parent);
    }

}
