package biz.altoc.faeit212app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FeedDownloader feedDownloader = new FeedDownloader();
        feedDownloader.setCallback(this);
        feedDownloader.execute("http://faeit212communitynews.blogspot.com/feeds/posts/default",
                "http://natfka.blogspot.com/feeds/posts/default");

    }

    protected void callBackAfterFeedsHaveBeenDownloaded(ArrayList<String> result) {
        FeedItemExtractor extractor = new FeedItemExtractor(result);
        ArrayList<FeedItem> feedItems = extractor.getFeedItems();

        FeedListViewAdapter adapter = new FeedListViewAdapter(this,
                android.R.layout.simple_list_item_1, feedItems);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(mMessageClickedHandler);
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            FeedItem feedItem = (FeedItem)parent.getItemAtPosition(position);

            String url = feedItem.link;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
