package biz.altoc.faeit212app;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class AtomReader {
    protected static List<FeedItem> readAtom(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<FeedItem> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("entry")) {
                items.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    private static FeedItem readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        FeedItem result = new FeedItem();
        parser.require(XmlPullParser.START_TAG, null, "entry");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    result.title = readStringValue(parser, "title");
                    break;
                case "feedburner:origLink":
                    String url = readStringValue(parser, "feedburner:origLink");
                    String urlWithoutBrokenEnding = url.replaceAll("\\?m=1$", "");
                    result.link = urlWithoutBrokenEnding;
                    break;
                case "published":
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
        parser.require(XmlPullParser.START_TAG, null, "published");
        String pubDateString = readText(parser);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSzzzz");
        Date pubDate = null;
        try {
            pubDate = dateFormat.parse(pubDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parser.require(XmlPullParser.END_TAG, null, "published");
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
