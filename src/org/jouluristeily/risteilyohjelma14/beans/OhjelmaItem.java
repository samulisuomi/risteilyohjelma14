package org.jouluristeily.risteilyohjelma14.beans;

import org.jouluristeily.risteilyohjelma14.helpers.TimeHelper;

public class OhjelmaItem {

    private String alkaa;
    private String paattyy; // can be ""
    private String nimi;
    private String paikka; // can be ""
    private String kuvaus; // can be ""
    private String kansi; // can be ""
    private boolean onPaattynyt;
    private String lastRefreshTime = null;

    public String getAlkaa() {
        return alkaa;
    }

    public void setAlkaa(String alkaa) {
        this.alkaa = alkaa;
    }

    public String getPaattyy() {
        return paattyy;
    }

    public void setPaattyy(String paattyy) {
        this.paattyy = paattyy;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getPaikka() {
        return paikka;
    }

    public void setPaikka(String paikka) {
        this.paikka = paikka;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getKansi() {
        return kansi;
    }

    public void setKansi(String kansi) {
        this.kansi = kansi;
    }

    public boolean onkoPaattynyt() {
        if (TimeHelper.getCurrentTime().equals(lastRefreshTime)) {
            // onPaattynyt = onPaattynyt
        } else {
            lastRefreshTime = TimeHelper.getCurrentTime();
            long paattyyTime;
            if (getPaattyy().length() < 4) {
                paattyyTime = TimeHelper.convertTimeToMs(getAlkaa());
            } else {
                paattyyTime = TimeHelper.convertTimeToMs(getPaattyy());
            }
            if (getAlkaa().equals("19:15")) {
                onPaattynyt = false; // the last event never ends!
            } else {
                onPaattynyt = paattyyTime < TimeHelper.getTimeInMs();
            }
        }
        return onPaattynyt;

    }
}
