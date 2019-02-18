package Ariva;
/**
 * @author Tobias Heiner
 * @description Liefert die Links zu den einzelnen Aktien auf ariva.de
 * @andi Lass bitte ALLE Kommentare stehen und füge nichts mit debug etc. hinzu (macht das Ganze nur viel unübersichtlicher)
 * Intellij hat einen Debugger integriert -> man muss nirgendwo einen debugmodus hinzufügen
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class Feeder_Ariva {

    String url = "https://www.ariva.de/aktien/indizes";
    Document doc;
    ArrayList<String> indizesliste = new ArrayList<>();
    ArrayList<String> aktienliste = new ArrayList<>();

    /**
     * Laedt Website herunter
     *
     * @throws IOException
     */
    public void connect() throws IOException {
        doc = Jsoup.connect(url).get();
    }

    /**
     * Waehlt die "Regionen" und fuehrt dann die Methode getIndizes aus
     *
     * @throws IOException
     */
    public void indizes() throws IOException {

        connect();         //stelle Verbindung zur Website her
        Element indextabelle = doc.select("#result_table_0 > tbody:nth-child(3)").first();         //gehe in die Tabelle dees Aktienindizes, z.B. Deutschland, Europa
        getIndizes(indextabelle);

        indextabelle = doc.select("#result_table_1 > tbody:nth-child(3)").first();        //Europa
        getIndizes(indextabelle);

        indextabelle = doc.select("#result_table_2 > tbody:nth-child(3)").first();         //Amerika
        getIndizes(indextabelle);

        indextabelle = doc.select("#result_table_3 > tbody:nth-child(3)").first();         //Welt
        getIndizes(indextabelle);

    }

    /**
     * liefert die Links zu den Indizes eine "Region
     *
     * @param indextabelle "Region" z.B. Deutschland
     */
    public void getIndizes(Element indextabelle) {

        int groesse;          //Groesse der Tabelle
        groesse = indextabelle.childNodeSize() - 1;         //-1 deswegen, um die unterste Zeile (Leerzeile) wegzurechnen

        int i = 0;       //Zaehlvariable zum Durchlaufen der Tabelle
        while (i < groesse) {           //Durchlaufen der Tabelle und Speicherung alller Links in der Arraylist indizesListe

            Element index = indextabelle.child(i);        //Tabellenelement auswaehlen
            Element meta = index.child(0).select("a").first();           //Weg zum href
            String link = meta.attr("href");            //href bekommen; From: "/dax..."; kompletter Link -> "ariva.de" + link
            link = indexlinkZusammensetzen(link);           //Link zusammenbauen
            System.out.println(link);
            indizesliste.add(link);         //Hinzufuegen des Links zur indizesListe
            i++;
        }
    }

    /**
     * baut den Indexlink komplett zusammen
     *
     * @param l Linkbaustein(nur Ende)
     * @return fertiger Link
     */
    public String indexlinkZusammensetzen(String l) {
        String result = "https://www.ariva.de" + l;           //Strings zusammensetzen
        return result;
    }


    public int seitenanzahl(int k) throws IOException {


        doc = Jsoup.connect(indizesliste.get(k)).get();

        Element leiste = doc.getElementsByClass("pageNavigate").first();
        if (leiste == null) {
            return 0;
        } else {
            Element zahl = leiste.child(1);         //Seitenauswahlelement

            Element t = zahl.getElementsByClass("prgLink").last();
            String s = t.attr("data-submit");

            int j;
            j = s.indexOf("page");
            s = s.substring(j + 5);
            j = Integer.parseInt(s);
            return j;
        }

    }


    public void aktie() throws IOException {
        int max = indizesliste.size();
        int i = 0;

        while (i < max) {         //Index auswaehlen


            doc = Jsoup.connect(indizesliste.get(i)).get();
            int seiteMax = seitenanzahl(i);
            String s = indizesliste.get(i);


            Element test = doc.select("#result_table_0 > tbody:nth-child(3)").first();
            if (test == null) {
                System.out.println("Keine Aktien vorhanden");
            } else {

                if (max == 0) {         //wenn kein Seitenelement vorhanden ist
                    doc = Jsoup.connect(s).get();
                    int t;
                    Element meta = doc.select("#result_table_0 > tbody:nth-child(3)").first();          //Tabelle auswaehlen
                    t = meta.childNodeSize();           //Anzahl der Elemente rausfinden
                    t = t - 1;
                    //System.out.println(t);
                    //s = indizesliste.get(i);
                    int q = 0;


                    while (q < t) {         //Elemente (Aktien) durchlaufen
                        Element o = meta.child(q);
                        //System.out.println(o.text());


                        o = o.select("a").first();          //Link aus dem CSS-Selektor bekommen
                        String result = o.attr("href");
                        result = indexlinkZusammensetzen(result);
                        System.out.println(result);

                        aktienliste.add(result);
                        q++;


                    }
                } else {

                    int k = 0;

                    while (k <= seiteMax) {         //wenn mehrere Seiten vorhanden sind
                                                    //aequivalent wie oben
                        s = s + "?page=" + k + "&sort_d=asc&sort=agname";
                        doc = Jsoup.connect(s).get();
                        int t;
                        Element meta = doc.select("#result_table_0 > tbody:nth-child(3)").first();
                        t = meta.childNodeSize();
                        t = t - 1;
                        //System.out.println(t);
                        s = indizesliste.get(i);
                        k++;
                        int q = 0;
                        while (q < t) {
                            Element o = meta.child(q);
                            //System.out.println(o.text());


                            o = o.select("a").first();
                            String result = o.attr("href");
                            result = indexlinkZusammensetzen(result);
                            System.out.println(result);
                            aktienliste.add(result);
                            q++;


                        }
                    }


                }


            }
            i++;
        }
    }

    public void ausgabe() {
        System.out.println(aktienliste.size());
    }

}

