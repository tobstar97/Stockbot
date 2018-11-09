/*
    @author andygschaider
    @description: automatisches Abfragen von Aktien von bekannten Indizes von onvista.
    Klasse Onvista wird dann mit den Aktiennamen gefüttert und zieht die jeweiligen Daten aus dem Netz.
 */

package Feeder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class FeederOnvista {

    private Document doc;

    private String url = "https://www.onvista.de/";
    //Document... IntelliJ macht bei mir keine Autovervollständigung für Jsoup... Jemand einen Plan warum?

    private void connect() throws IOException {
        doc = Jsoup.connect(url).get();
    }

}