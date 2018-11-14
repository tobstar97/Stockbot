/*
    @author: andygschaider
    @description: Alles rund um die (last-modified) timestamps von websites.
    @version: 2018-11-11
 */

package Hilfsklassen;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

public class WebsiteTimestamps {

    /**
     * Liefert Datum der letzten Modifizierung einer der folgenden websites: onvista, ariva, morningstar
     * @param url gueltige url.
     * @return Datum als String.
     */
    public String getLastModifiedDateFromWebpage (String url) {
        String date = "ERROR";
        //Welche Website?
        String domain = Hilfsmethoden.getDomainFromUrl(url);    //sobald ein Punkt in der url auftaucht wird bis zum nächsten Punkt extrahiert
        switch(domain) {
            case "onvista": {
                try {
                    date = catchLastModifiedDateOnvista(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Class: WebsiteTimestamps.java - Method: getLastModifiedDateFromWebpage - case: onvista - catchLastModifiedDateOnvista wirft IOException!");
                }
                break;
            }
            case "ariva": {
                try {
                    date = catchLastModifiedDateAriva(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Class: WebsiteTimestamps.java - Method: getLastModifiedDateFromWebpage - case: ariva - catchLastModifiedDateAriva wirf IOException!");
                }
                break;
            }
            case "morningstar": {
                try {
                    date = catchLastModifiedDateMorningstar(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Class: WebsiteTimestamps.java - Method: getLastModifiedDateFromWebpage - case: morningstar - catchLastModifiedDateMorningstar wirf IOException!");
                }
                break;
            }
            default: {
                System.err.println("Class: WebsiteTimestamps.java - Method: getLastModifiedDateFromWebpage - case: default - url ungueltig!");
            }
        }

        return date;
    }

    /**
     * Bringt das Datum der letzten Modifizierung an der website (onvista.*) in Erfahrung.
     * @author andygschaider
     * @param url gueltige url.
     * @return Datum als String.
     * @throws IOException wenn url Mist.
     */
    private String catchLastModifiedDateOnvista (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        //Zeit auslesen
        Element time = doc.select("time[datetime]").first();
        //Zeit extrahieren
        return Hilfsmethoden.formatTimeOnvista(time.attr("datetime"));
        /*
        DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG
            Wirecard-Aktie (GUV)
                Query:  time[datetime]
                Match:  <time datetime="2018-11-09T22:25:50+01:00" data-push="12992081:lastTime:1:1:Stock">
                        09.11.2018, 22:25:50
         */
    }

    /**
     * Bringt das Datum der letzten Modifizierung an der website (ariva.*) in Erfahrung.
     * @author andygschaider
     * @param url gueltige url.
     * @return Datum als String.
     * @throws IOException wenn url Mist.
     */
    private String catchLastModifiedDateAriva (String url) throws IOException {
        String pass;
        Document doc = Jsoup.connect(url).get();
        Element time = doc.select("span[class*=time-with-sec-or-date]").first();
        if(time != null) pass = time.text();
        else {
            time = doc.select("span[class=push_time]").first();
            if(time != null) pass = time.text();
            else {
                time = doc.select("div[class=\"snapshotInfo right\"]").first();
                if(time != null) pass = time.text();
                else {
                    if(url.contains("ariva.de/aktien/indizes")) return "non-existent";
                    else return "ERROR IN catchLastModifiedDateAriva";
                }
            }
        }


        /*
        if(Hilfsmethoden.istArivaIndex(url) == true) {
            //System.out.println(">> WebsiteTimestamps.catchLastModifiedDateAriva >> if");
            Document doc = Jsoup.connect(url).get();
            Element time = doc.select("span[class*=time-with-sec-or-date]").first();
            //System.out.println(">> " + time.text());
            if(time == null) return "BLACK PANTER";
            return time.text();
        }

        switch(shorturl) {
            //für ariva.de/aktien/indizes gibt es keine timestamps
            case "ariva.de/aktien/indizes": {
                pass = "non-existent";
                break;
            }
            //für ariva.de/aktien
            case "ariva.de/aktien": {
                Document doc = Jsoup.connect(url).get();
                Element time = doc.select("span[class=push_time]").first();
                pass = time.text();
                break;
            }
            //ATX: div class="snapshotInfo right"
            //für die Uebersicht-Seite: div[class="snapshotInfo right"]
            //ebenso für die Chart-Seite



            //für !!kurslisten!! von ariva.de/dax-30 oder aequivalente indizes
            //span class="arp_290@16.4_t_format=time-with-sec-or-date"
            //span[class^=arp_290@16.4_t_format]
            case "ariva.de/eurostoxx-50":
            case "ariva.de/aex":
            case "ariva.de/atx":
            case "ariva.de/cac40":
            case "ariva.de/ibex":
            case "ariva.de/smi":
            case "ariva.de/hang-seng":
            case "ariva.de/nikkei":
            case "ariva.de/quote/profile.m?secu=674776":
            case "ariva.de/dow-jones-industrial-average":
            case "ariva.de/dax-30": {
                Document doc = Jsoup.connect(url).get();
                Element time = doc.select("span[class*=time-with-sec-or-date]").first();
                pass = time.text();
                break;
            }
        } */
        return Hilfsmethoden.formatTimeAriva(Hilfsmethoden.shortenTimeAriva(pass));
    }

    /**
     * Bringt das Datum der letzten Modifizierung an der website (morningstar.*) in Erfahrung.
     * @author andygschaider
     * @param url gueltige url.
     * @return Datum als String.
     * @throws IOException wenn url Mist.
     */
    private String catchLastModifiedDateMorningstar(String url) throws IOException {

        //update-time
        //div[class="qs-marketindex-legend"]
        //span class="date"
        //span class="time"
        //p id="Col0PriceTime" class="priceInformation"

        Document doc = Jsoup.connect(url).get();
        Element time = doc.select("p[id=\"Col0PriceTime\"]").first();
        if(time != null) return Hilfsmethoden.formatTimeMorningstar(Hilfsmethoden.shortenTimeMorningstar(time.text()));
        return "non-existent";
    }

}