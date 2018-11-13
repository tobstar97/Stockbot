/**
 * Automatisches Abfragen von Aktiennamen von bekannten Indizes von onvista.
 * Klasse Onvista wird dann mit den Aktiennamen gefuettert und zieht die jeweiligen Daten aus dem Netz.
 *
 * @author andygschaider
 * @since poc
 */


package Feeder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class FeederOnvista {

    private Document doc;

    private String url = "https://www.onvista.de/";

    private void connect() throws IOException {
        doc = Jsoup.connect(url).get();
    }

}