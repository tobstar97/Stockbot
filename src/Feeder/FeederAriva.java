package Feeder;

import java.io.IOException;
import java.net.MalformedURLException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Automatisches Abfragen von Aktiennamen von bekannten Indizes von ariva.
 * Klasse Ariva wird dann mit den Aktiennamen gefuettert und zieht die jeweiligen Daten aus dem Netz.
 *
 * @author andygschaider
 * @version poc
 * @since poc
 */
public class FeederAriva {

    public FeederAriva() throws IOException {
        //alles mit "dax" als bestandteil und
        //alles mit "-index/kursliste" als ende
        String url = "https://www.ariva.de/dax-30";
        Document doc = Jsoup.connect(url).get();
        for(int i=0; i<doc.select("").size(); i++) {
            //fuer jeden Aktienindex die jeweiligen Aktien raussuchen und dann den Link zur Aktie weiterreichen
            Element elem = doc.select("").get(i);
            getAndSendNames(url);
        }

    }

    public void getAndSendNames(String res) throws MalformedURLException, IOException {
        Document doc = Jsoup.connect(res).get();
        //aus jedem Element den Link rausziehen
        for(int i=0;i<doc.select("a[href$=\"-aktie\"]").size(); i++) {
            Element elem = doc.select("a[href$=\"-aktie\"]").get(i);
            String s = elem.attributes().toString();
            //auf Link kuerzen
            s = s.substring(8);
            s = s.substring(0,s.length()-1);
            System.out.println(s);
            sendNameToAriva(s);
        }

    }

    private void sendNameToAriva(String s) {
        //Uebermitteln an Klasse Ariva.java

        //ZU IMPLEMENTIEREN     //ZU IMPLEMENTIEREN     //ZU IMPLEMENTIEREN     //ZU IMPLEMENTIEREN     //ZU IMPLEMENTIEREN
    }


}
