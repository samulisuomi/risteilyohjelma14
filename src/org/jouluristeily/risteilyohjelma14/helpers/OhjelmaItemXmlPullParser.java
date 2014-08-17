package org.jouluristeily.risteilyohjelma14.helpers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jouluristeily.risteilyohjelma14.beans.OhjelmaItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

/**
 * Inspired by FoamyGuy (https://github.com/FoamyGuy/StackSites)
 */
public class OhjelmaItemXmlPullParser {

    static final String KEY_TAPAHTUMA = "tapahtuma";
    static final String KEY_ALKAA = "alkaa";
    static final String KEY_PAATTYY = "paattyy";
    static final String KEY_NIMI = "nimi";
    static final String KEY_PAIKKA = "paikka";
    static final String KEY_KUVAUS = "kuvaus";
    static final String KEY_KANSI = "kansi";

    public static List<OhjelmaItem> getOhjelmaItemsFromXml(Context ctx,
            int xmlResourceId) {

        // List of OhjelmaItems that we will return
        List<OhjelmaItem> ohjelmaItems;
        ohjelmaItems = new ArrayList<OhjelmaItem>();

        // temp holder for current OhejalmaItem while parsing
        OhjelmaItem currentOhjelmaItem = null;
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
                    if (tagname.equalsIgnoreCase(KEY_TAPAHTUMA)) {
                        // If we are starting a new <tapahtuma> block we need
                        // a new OhjelmaItem object to represent it
                        currentOhjelmaItem = new OhjelmaItem();
                    }
                    break;

                case XmlPullParser.TEXT:
                    // grab the current text so we can use it in END_TAG event
                    currentText = xpp.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase(KEY_TAPAHTUMA)) {
                        ohjelmaItems.add(currentOhjelmaItem);

                    } else if (tagname.equalsIgnoreCase(KEY_ALKAA)) {
                        currentOhjelmaItem.setAlkaa(currentText);

                    } else if (tagname.equalsIgnoreCase(KEY_PAATTYY)) {
                        currentOhjelmaItem.setPaattyy(currentText);

                    } else if (tagname.equalsIgnoreCase(KEY_NIMI)) {
                        currentOhjelmaItem.setNimi(currentText);

                    } else if (tagname.equalsIgnoreCase(KEY_PAIKKA)) {
                        currentOhjelmaItem.setPaikka(currentText);

                    } else if (tagname.equalsIgnoreCase(KEY_KUVAUS)) {
                        currentOhjelmaItem.setKuvaus(currentText);

                    } else if (tagname.equalsIgnoreCase(KEY_KANSI)) {
                        currentOhjelmaItem.setKansi(currentText);

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
        return ohjelmaItems;
    }
}
