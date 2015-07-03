package biz.altoc.faeit212app;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class RssReader {
    protected static List<FeedItem> readRss(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<FeedItem> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                items.addAll(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    private static List<FeedItem> readChannel(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        List<FeedItem> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                items.add(readFeedItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    private static FeedItem readFeedItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        FeedItem result = new FeedItem();
        parser.require(XmlPullParser.START_TAG, null, "item");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    result.title = readStringValue(parser, "title");
                    break;
                case "link":
                    result.link = readStringValue(parser, "link");
                    break;
                case "pubDate":
                    result.date = readPublishedDateValue(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return result;
    }

    private static String readStringValue(XmlPullParser parser, String valueName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, valueName);
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, valueName);
        return value;
    }

    private static Date readPublishedDateValue(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pubDate");
        String pubDateString = readText(parser);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss");
        Date pubDate = null;
        try {
            pubDate = dateFormat.parse(pubDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parser.require(XmlPullParser.END_TAG, null, "pubDate");
        return pubDate;
    }

    private static String readText(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
