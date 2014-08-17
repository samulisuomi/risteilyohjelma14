package org.jouluristeily.risteilyohjelma14.beans;

import java.util.List;

public class PaikkaItem {

    private String nimi;
    private String kansi;
    private String kuvaus; // can be null
    private List<AukioloItem> aukiolot;
    private boolean nytAvoinna;
    private String lastRefreshTime = null;

    public List<AukioloItem> getAukiolot() {
        return aukiolot;
    }

    public void setAukiolot(List<AukioloItem> aukiolot) {
        this.aukiolot = aukiolot;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKansi() {
        return kansi;
    }

    public void setKansi(String kansi) {
        this.kansi = kansi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public boolean onkoAvoinnaNyt(String currentTimeInUTC2) {
        if (currentTimeInUTC2.equals(lastRefreshTime)) {
            // nytAvoinna = nytAvoinna;
        } else {
            AukioloItem current;
            lastRefreshTime = currentTimeInUTC2;
            int i = 0;
            nytAvoinna = false;
            while (i < aukiolot.size()) {
                current = aukiolot.get(i);
                if (current.onkoAvoinnaNyt(currentTimeInUTC2)) {
                    nytAvoinna = true;
                }
                i++;
            }
        }
        return nytAvoinna;

    }

    @Override
    public String toString() {
        String avoinna = "";
        for (int i = 0; i < this.getAukiolot().size(); i++) {
            avoinna = avoinna + this.getAukiolot().get(i).toString() + "\n";
        }
        String palautettava = "Kansi: " + this.getKansi() + "\n" + "\n"
                + this.getKuvaus() + "\n" + "\n" + "Avoinna:" + "\n" + "\n"
                + avoinna;
        return palautettava;
    }

}
