package biz.altoc.faeit212app;


import android.support.annotation.NonNull;

import java.util.Date;

class FeedItem implements Comparable<FeedItem> {
    protected String link;
    protected String title;
    protected Date date;

    @Override
    public int compareTo(@NonNull FeedItem another) {
        return this.date.compareTo(another.date);
    }

    @Override
    public String toString() {
        return title;
    }
}
