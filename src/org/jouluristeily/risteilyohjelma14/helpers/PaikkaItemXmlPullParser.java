package org.jouluristeily.risteilyohjelma14.helpers;

import java.io.InputStream;
import java.util.ArrayList;

import org.jouluristeily.risteilyohjelma14.beans.PaikkaItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

/**
 * Inspired by FoamyGuy (https://github.com/FoamyGuy/StackSites)
 */
public class PaikkaItemXmlPullParser {

    static final String KEY_PAIKKA = "paikka";
    static final String KEY_NIMI = "nimi";
    static final String KEY_KANSI = "kansi";
    static final String KEY_KUVAUS = "kuvaus";

    public static ArrayList<PaikkaItem> getPaikkaItemsFromXml(Context ctx,
            int xmlResourceId) {

        // List of PaikkaItems that we will return
        ArrayList<PaikkaItem> paikkaItems;
        paikkaItems = new ArrayList<PaikkaItem>();

        // temp holder for current PaikkaItem while parsing
        PaikkaItem currentPaikkaItem = null;
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
                    if (tagname.equalsIgnoreCase(KEY_PAIKKA)) {
                        // If we are starting a new <paikka> block we need
                        // a new OhjelmaItem object to represent it
                        currentPaikkaItem = new PaikkaItem();
                    }
                    break;

                case XmlPullParser.TEXT:
                    // grab the current text so we can use it in END_TAG event
                    currentText = xpp.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase(KEY_PAIKKA)) {
                        paikkaItems.add(currentPaikkaItem);

                    } else if (tagname.equalsIgnoreCase(KEY_NIMI)) {
                        currentPaikkaItem.setNimi(currentText);

                    } else if (tagname.equalsIgnoreCase(KEY_KUVAUS)) {
                        currentPaikkaItem.setKuvaus(currentText);

                    } else if (tagname.equalsIgnoreCase(KEY_KANSI)) {
                        currentPaikkaItem.setKansi(currentText);
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
        return paikkaItems;
    }

}
