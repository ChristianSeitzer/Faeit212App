package biz.altoc.faeit212app;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class FeedDownloader extends AsyncTask<String, Void, ArrayList<String>> {
    private MainActivity callback;

    protected void setCallback(MainActivity callback) {
        this.callback = callback;
    }

    protected ArrayList<String> doInBackground(String... urls) {
        ArrayList<String> result = new ArrayList<>();
        for (String url : urls) {
            try {
                String rssFeed = downloadRSSFeed(url);
                result.add(rssFeed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @NonNull
    private String downloadRSSFeed(String urlString) throws IOException {
        InputStream in;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        in = conn.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        byte[] response = out.toByteArray();
        return new String(response, "UTF-8");
    }

    protected void onPostExecute(ArrayList<String> result) {
        callback.callBackAfterFeedsHaveBeenDownloaded(result);
    }
}
