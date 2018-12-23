package Feeder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import Datenbank.DBOperations;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Automatisches Abfragen von Aktiennamen von bekannten Indizes von ariva.
 * Klasse Ariva wird dann mit den Aktiennamen gefuettert und zieht die jeweiligen Daten aus dem Netz.
 *
 * Achtung: Erstellen einses Objekts dieser Klasse startet sofort den Fuetterungsvorgang!
 * Diese Klasse wird lediglich beim ersten Programmstart nach der Installation benoetigt.
 *
 * Das Array macht leichte Probleme, es werden ab und an einfach Indizes uebersprungen.
 * Workaround: Klasse einfach mehrfach hintereinander ausfuehren.
 *
 * Zeitaufwand fuer diese Klasse: ~7h
 *
 * @author andygschaider
 * @version poc
 * @since poc
 */
public class FeederAriva {

    //debug-Mode on?
    private boolean debug = true;

    private Set<String> set = new HashSet<>(3000);
    private Set<String> iot = new HashSet<>(19);
    private String[] arr = new String[19];

    public FeederAriva() {
        //alle Links zu Aktienindizes die nicht auf das unten in getIndizes() gelistete Schema matchen
        iot.add("https://www.ariva.de/eurostoxx-50");
        iot.add("https://www.ariva.de/aex");
        iot.add("https://www.ariva.de/atx");
        iot.add("https://www.ariva.de/cac40");
        iot.add("https://www.ariva.de/ibex");
        iot.add("https://www.ariva.de/smi");
        iot.add("https://www.ariva.de/dow-jones-industrial-average");
        iot.add("https://www.ariva.de/nikkei");
        iot.add("https://www.ariva.de/hang-seng");
        iot.add("https://www.ariva.de/dj_canada-index");
        iot.add("https://www.ariva.de/tsx-60-index");
        iot.add("https://www.ariva.de/mexican_bolsa-index");
        iot.add("https://www.ariva.de/quote/profile.m?secu=674776");
        iot.add("https://www.ariva.de/set_50-index");
        iot.add("https://www.ariva.de/egx_30-index");
        iot.add("https://www.ariva.de/ise_national_30-index");
        iot.add("https://www.ariva.de/omx_kopenhagen_20_kurs-index");
        iot.add("https://www.ariva.de/wbi_wiener_b%C3%B6rse_index");
        iot.add("https://www.ariva.de/ptx_eur-index");

        Object[] iotarray = iot.toArray();
        for(int i=0;i<iotarray.length;i++) {
            arr[i] = (String) iotarray[i];
            arr[i] = arr[i].substring(20);
            if(debug) System.out.println("FeederAriva():" + arr[i]);
        }

        //sobald ein Objekt dieser Klasse erstellt wird, wird automatisch losgefuettert.
        try {
            getIndizes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Holt die Links zu den Aktienindizes von Ariva.de
     *
     * Beim Erstellen eines Objekts dieser Klasse wird diese Methode einmalig aufgerufen,
     * danach kann sie von aussen angesteuert und manuell gestartet werden.
     *
     * @author andygschaider
     * @throws IOException
     */
    public void getIndizes() throws IOException {
        String baseurl = "https://www.ariva.de/aktien/indizes";
        Document doc = Jsoup.connect(baseurl).get();

        System.out.println("Durchsuche Ariva.de nach gueltigen Aktienlinks. Dies kann einige Minuten dauern...");

        for(int i=0; i<doc.select("a[href*=\"dax\"],a[href$=\"-index/kursliste\"],a[href=\"/quote/aktienkurse.m?list=\"],a[href=\"performance_index\"]").size(); i++) {
            //fuer jeden Aktienindex die jeweiligen Aktien raussuchen und dann den Link zur Aktie weiterreichen
            //matcht (liefert ca. 500 Aktienlinks):
            //alles mit "dax" als bestandteil und
            //alles mit "-index/kursliste" als ende
            Element elem = doc.select("a[href*=\"dax\"],a[href$=\"-index/kursliste\"],a[href=\"/quote/aktienkurse.m?list=\"],a[href=\"performance_index\"]").get(i);
            String url = elem.attr("href");
            if(debug) System.out.println("getIndizes(): " + " https://www.ariva.de" + url);
            getAndSendNames("https://www.ariva.de" + url);
        }
        if(debug) System.out.println("=============================================");
        //jetzt noch alle Indizes, welche nicht auf den select matchen (liefert ca. 300 Aktienlinks)
        for(int j=0; j<3; j++) {
            for (int i = 0; i < arr.length - 1; i++) {
                if (debug)
                    System.out.println("getIndizes(): " + arr[i]);
                getAndSendNames("https://www.ariva.de" + arr[i]);
            }
        }

        //if(debug)
        System.out.println(set);
        //if(debug)
        System.out.println(set.size());     //550 -> jetzt 800
    }

    /**
     * Holt die Links zu den Aktien eines Aktienindex von Ariva.de
     * @author andygschaider
     * @param res
     * @throws IOException
     */
    private void getAndSendNames(String res) throws IOException {
        Document doc = Jsoup.connect(res).get();
        //aus jedem Element den Link rausziehen
        for(int i=0;i<doc.select("a[href$=\"-aktie\"]").size(); i++) {
            Element elem = doc.select("a[href$=\"-aktie\"]").get(i);
            String s = elem.attributes().toString();
            //auf Link kuerzen
            s = s.substring(8);
            s = s.substring(0,s.length()-1);    //letztes Zeichen = '>' weg
            if(debug) System.out.println("getAndSendNames(): " + s);
            if(!set.contains(s)) {
                set.add(s);
            }
        }
    }

    /**
     * Speichert alle gefundenen Links in einer Textdatei.
     * Falls neue Links gefunden werden, werden sie hinzugefuegt.
     * @author andygschaider
     */
    public Set<String> sendSetToAriva() {
        //Uebermitteln des Sets an Klasse Ariva.java
        if(debug) System.out.println("sendSetToAriva(): " + set.size());
        return set;
    }

    /**
     * @author andygschaider
     * @description Versuch, die Suchseiten so zu durchlaufen wie die Indizes selbst
     * @throws IOException
     *
     * gescheitert.
     */
    public static void getSucheIndizes() throws IOException {
        String linkv = "https://www.ariva.de/aktien/suche#page=";
        String linkh= "&year=_year_2017&sort_n=ariva_name&sort_d=asc";
        //insgesamt 133 Seiten in der Suche
        for(int i=0; i<133; i++) {
            String link = linkv + i + linkh;
            Document doc = Jsoup.connect(link).get();
            for(int j=0; j<doc.select("a[href*=\"aktie\"]").size(); j++) {
                Element elem = doc.select("a[href*=\"aktie\"]").get(j);
                System.out.println(elem);
            }
        }
    }

    /**
     * @author andygschaider
     * @description Versuch den Quelltext an sich auszulesen
     * @param surl
     * @return
     *
     * gescheitert
     */
    public static String getSeitenquelltext(String surl){
        final String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.8.1.12) Gecko/20080201 Firefox/2.0.0.12";
        try {
            URL url = new URL(surl);
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent", userAgent);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str, str1;
            str = new String();
            while ((str1 = in.readLine()) != null) {
                str = str + str1;
            }
            in.close();
            return str;
        } catch (MalformedURLException e) {
            System.out.println( e.getMessage());
            return "catch1";
        } catch (IOException e) {
            System.out.println( e.getMessage());
            return "catch2";
        }
    }

    /**
     * @author andygchaider
     * @description Versuch, das JavaScript auszulesen
     */
    public static void readJavaScript() {
        WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        webEngine.load("https://www.ariva.de/aktien/suche#page=0&year=_year_2017&sort_n=ariva_name&sort_d=asc");
        org.w3c.dom.Document doc = webEngine.getDocument();
        System.out.println(webEngine.getTitle());
    }

}