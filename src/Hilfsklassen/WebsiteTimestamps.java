/*
    @author: andygschaider
    @description: Alles rund um die (last-modified) timestamps von websites.
    @version: 2018-11-11
 */

package Hilfsklassen;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
        String domain = getDomainFromUrl(url);    //sobald ein Punkt in der url auftaucht wird bis zum nächsten Punkt extrahiert
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
                //zu ergänzen
                break;
            }
            case "morningstar": {
                //zu ergänzen
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
     * @param url gueltige url.
     * @return Datum als String.
     * @throws IOException wenn url Mist.
     */
    private String catchLastModifiedDateOnvista (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        //Zeit auslesen
        Element time = doc.select("time[datetime]").first();
        //Zeit extrahieren
        return time.attr("datetime");
        /*
        DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG   =   DEBUG
            Wirecard-Aktie (GUV)
                Query:  time[datetime]
                Match:  <time datetime="2018-11-09T22:25:50+01:00" data-push="12992081:lastTime:1:1:Stock">
                        09.11.2018, 22:25:50
         */
    }

//    === === === === === === === === === === ===  ===  === === === === === === === === === === ===
//    === === === === === === === === === === HILFSMETHODEN === === === === === === === === === ===
//    === === === === === === === === === === ===  ===  === === === === === === === === === === ===


    /**
     * Extrahiert aus einer url die domain.
     * @param url mit folgender Syntax: *.domain.*
     * @return Substring aus url, welcher die domain ist.
     */
    private String getDomainFromUrl(String url) {
        //Die ersten 2 Punkte finden
        int punktanfang = url.indexOf('.');
        int punktende = url.indexOf('.', punktanfang+1);
        //neuen String aus Begrenzung heraus bilden
        return url.substring(punktanfang+1,punktende-1);
    }

}
