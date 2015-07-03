package biz.altoc.faeit212app;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;


class FeedItemExtractor {

    private final ArrayList<String> feedStrings;

    public FeedItemExtractor(ArrayList<String> feedStrings)
    {
        this.feedStrings = feedStrings;
    }

    public ArrayList<FeedItem> getFeedItems() {
        ArrayList<FeedItem> feedItems = new ArrayList<>();
        for(String feedString: feedStrings) {
            try {
                XmlPullParser parser = getXmlPullParser(feedString);

                if(isAtomFeed(parser)) {
                    feedItems.addAll(AtomReader.readAtom(parser));
                }
                else {
                    feedItems.addAll(RssReader.readRss(parser));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(feedItems, Collections.reverseOrder());

        return feedItems;
    }

    private boolean isAtomFeed(XmlPullParser parser) throws ParserConfigurationException, IOException, SAXException {
        String name = parser.getName();

        return name.equals("feed");
    }

    private XmlPullParser getXmlPullParser(String source) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(source));

        skipFirstXMLTag(xpp);
        return xpp;
    }

    private void skipFirstXMLTag(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.next();
    }

}
