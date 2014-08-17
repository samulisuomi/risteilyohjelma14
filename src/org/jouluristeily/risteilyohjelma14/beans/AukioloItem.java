package org.jouluristeily.risteilyohjelma14.beans;

public class AukioloItem {

    private String alkaa; // "hh:MM" if 20:15...23:59 -> 28.11
                          // else 00:00...19:15 -> 29.11
    private String paattyy; // "hh:MM" if 20:15...23:59 -> 28.11
                            // else 00:00...19:15 -> 29.11
    private String lisatieto;

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

    public String getLisatieto() {
        return lisatieto;
    }

    public void setLisatieto(String lisatieto) {
        this.lisatieto = lisatieto;
    }

    public boolean onkoAvoinnaNyt(String currentTimeInUTC2) {
        boolean onAvoinna = false;
        if (alkaa.compareTo(paattyy) < 0) { // tapahtuu saman vuorokauden aikana
            if ((alkaa.compareTo(currentTimeInUTC2) <= 0)
                    && (currentTimeInUTC2.compareTo(paattyy) < 0)) {
                onAvoinna = true;
            } else {
                onAvoinna = false;
            }
        } else if (alkaa.compareTo(paattyy) > 0) { // tapahtuma jatkuu keskiyön
                                                   // yli
            if (currentTimeInUTC2.compareTo("20:00") >= 0) { // keskiyötä ennen
                if (alkaa.compareTo(currentTimeInUTC2) <= 0) {
                    onAvoinna = true;
                } else {
                    onAvoinna = false;
                }
            } else { // keskiyön jälkeen
                if (currentTimeInUTC2.compareTo(paattyy) < 0) {
                    onAvoinna = true;
                } else {
                    onAvoinna = false;
                }
            }
        } else if (alkaa.length() < 4) {
            onAvoinna = false;
        }
        return onAvoinna;
    }

    @Override
    public String toString() {
        String palautettava = this.getAlkaa() + "–" + this.getPaattyy() + " "
                + this.getLisatieto();
        if (this.alkaa.length() < 4) {
            palautettava = "Suljettu jouluristeilyn ajan.";
        }
        return palautettava;
    }
}
