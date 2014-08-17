package org.jouluristeily.risteilyohjelma14.helpers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jouluristeily.risteilyohjelma14.beans.AukioloItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

/**
 * Inspired by FoamyGuy (https://github.com/FoamyGuy/StackSites)
 */
public class AukioloItemXmlPullParser {

    static final String KEY_AUKIOLO = "aukiolo";
    static final String KEY_ALKAA = "alkaa";
    static final String KEY_PAATTYY = "paattyy";
    static final String KEY_LISATIETO = "lisatieto";

    public static List<AukioloItem> getAukioloItemsFromXml(Context ctx,
            int xmlResourceId) {

        // List of AukioloItems that we will return
        List<AukioloItem> aukioloItems;
        aukioloItems = new ArrayList<AukioloItem>();

        // temp holder for current AukioloItem while parsing
        AukioloItem currentAukioloItem = null;
        // temp holder for current text value while parsing
        String currentText = "";

        try {
            // Get our factory and PullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            // Open up InputStream and Reader of our file.
            // FileInputStream fis = ctx.openFileInput("netistaladattu.xml");
            InputStream ins = ctx.getResources().openRawResource(xmlResourceId);

            // point the parser to our file.
            xpp.setInput(ins, "UTF-8");
            // get initial eventType
            int eventType = xpp.getEventType();

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                String tagname = xpp.getName();
                // React to different event types appropriately
                switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase(KEY_AUKIOLO)) {
                        // If we are starting a new <aukiolo> block we need
                        // a new OhjelmaItem object to represent it
                        currentAukioloItem = new AukioloItem();
                    }
                    break;

                case XmlPullParser.TEXT:
                    // grab the current text so we can use it in END_TAG event
                    currentText = xpp.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase(KEY_AUKIOLO)) {
                        aukioloItems.add(currentAukioloItem);

                    } else if (tagname.equalsIgnoreCase(KEY_ALKAA)) {
                        currentAukioloItem.setAlkaa(currentText);

                    } else if (tagname.equalsIgnoreCase(KEY_PAATTYY)) {
                        currentAukioloItem.setPaattyy(currentText);

                    } else if (tagname.equalsIgnoreCase(KEY_LISATIETO)) {
                        currentAukioloItem.setLisatieto(currentText);
                    }
                    break;

                default:
                    break;
                }
                // move on to next iteration

                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the populated list.
        return aukioloItems;
    }
}
