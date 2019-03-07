package Ariva;


/**
 * @author Tobias Heiner
 * @description stellt die verbindung zur website ariva.de her und liefert den aktuellen Kurs und Gewinn
 * @problem ariva block nach einer bestimmten Anzahl von Abrufen in einer gewissen Zeit die IP des Computers
 * @loesung wir starten eine "Versuchsreihe", um die genauen Parameter rauszufinden, wann ariva die IP blockiert
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Scanner;


public class Ariva_Kurs {

    String aktienname;
    String ISIN;
    String url = "https://www.ariva.de/";
    String strdUrlBilanzGuV = "https://www.ariva.de/amazon-aktie/bilanz-guv#stammdaten";    //+ aktienname mit "-" + "-" + ISIN         Standardurl für bilanz-guv
    Document doc;

    Scanner scan;

    double gewinn;
    double kurs;
    HashMap<String, Double> hm = new HashMap<String, Double>();
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();

    /**
     * Connection zu den unterschiedlichen urls für die historischen Kurse
     * @param url
     * @throws IOException
     */
    public void connectKurs(String url) throws IOException {
        doc = Jsoup.connect(url).get();

    }


    /**
     * Stellt eine Connection zur website her
     * @throws IOException
     */
    public void connect() throws IOException {
        scan = new Scanner(System.in);

        System.out.println("Geben sie den Aktiennamen ein");
        aktienname = scan.next();

        //einzufügen: für jedes leerzeichen im aktiennamen ein "-" einfügen

        System.out.println("Geben sie die ISIN ein");
        ISIN = scan.next();

        url = url + ISIN;

        //doc = Jsoup.connect(url).get();
        doc = Jsoup.connect("https://www.ariva.de/wirecard-aktie/bilanz-guv").get();
        System.out.println("basst");

    }

    /**
     *Liefert den aktuellen Kurs einer Aktie auf ariva
     * @return Kurs als double
     */
    public double getKurs() {
        Element span = doc.select("span[itemprop=price]").first();
        String content;

        content = span.text();


        //System.out.println(content);

        //System.out.println(convert+"TEST");

        content = mod(content);
        kurs = Double.parseDouble(content);
        //System.out.println(kurs);
        return kurs;

    }


    /**
     * Liefert den aktuellen gewinn eines Unternehmens auf ariva
     * @return gewinnn als double
     */
    public double getGewinn() {
        Element meta = doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(3);
        Element table = meta.siblingElements().get(4);
        //Element tb = table.elementSiblingIndex();
        String content = table.text();

        content = mod(content);

        gewinn = Double.parseDouble(content);
        System.out.println(gewinn);

        return gewinn;
    }

    /**
     * Liefert die Analysen für 12 Monate
     */
    public void getAnalyse() {
        Element meta = doc.getElementsByClass("ag_analysis analysis abstand").first();

        Element table = meta.children().first();
        Element kaufen = table.select("td").get(1);
        Element halten = table.select("td").get(2);
        Element verkaufen = table.select("td").get(3);
        System.out.println(kaufen.text());
        System.out.println(halten.text());
        System.out.println(verkaufen.text());
    }


    /**
     *
     * liest den kurs und das zugehörige datum einer aktie aus
     * bekommt die links aus der arraylist, die vorher mit der methode kursLink reingeschrieben wurden
     * @throws IOException
     */
    public void getKursGesamt() throws IOException {

        int i = 0;
        int k = 0;
        double d = 0;
        //Loesung für das blockier-Problem - Versuch
        int anzahl = 0;         //Variable, um die Anzahl der Abrufe zu zählen
        String st;
        Element testdate = doc.getElementsByClass("arrow0").get(0);
        String s = testdate.text();


        int z = 0;
        while (z < al.size()) {
            anzahl++;           //Anzahl der Verbindungen auf die ariva seite
            System.out.println(anzahl);         //gibt die Anzahl aus
            System.out.println(al.get(z));
            connectKurs(al.get(z));
            //doc = Jsoup.connect("https://www.ariva.de/allianz-aktie/historische_kurse?boerse_id=6&month=2018-10-31&currency=&clean_split=1&clean_split=0&clean_payout=0&clean_bezug=1&clean_bezug=0").get();
            k = doc.getElementsByClass("arrow0").size();
            //System.out.println(k);

            while (i < k) {
                //System.out.println("Test");
                String content;
                Element meta = doc.getElementsByClass("arrow0").get(i);
                Element date = meta.child(0);
                Element value = meta.child(4);

                content = value.text();


                content = mod(content);

                d = Double.parseDouble(content);
                st = date.text();
                System.out.println(d + "  " + st);
                System.out.println(anzahl);
                hm.put(st, d);
                //System.out.println(hm.size());

                i++;

            }
            z++;
            i = 0;
        }
        al.clear();


    }


    /**
     * @author Lukas Meinzer, Tobias Heiner
     * funktioniert!
     * soll den hauphandelsplatz einer aktie rausfinden
     * https://www.ariva.de/amazon-aktie/kurs immer der 4. eintrag von oben in der tabelle ist der haupthandelsplatz
     * daraus muss die boerse_id ausgelesen werden, z.B. bei Nasdaq 40
     * @param str in Form von "<name>-aktie"
     * @return Teilstring, "boerse_id=..."
     * @throws IOException
     */
    public String haupthandelsplatz(String str) throws IOException {

        try {
            //doc = Jsoup.connect("https://www.ariva.de/"+str+"/kurs").get();
            doc = Jsoup.connect(str + "/kurs").get();
        } catch (ConnectException e) {
            System.err.println("Probleme bei der Link-Eingabe");
        }


        //Hier testen ob die Aktie überhaupt eingetragene Kurse hat:
        Element e = doc.getElementById("result_table_0").child(2);
        String temp = e.toString();
        //System.out.println(temp2);

        if (temp.contains("keine Kurse vorhanden")) {
            return "boerse_id=";
        }


        //Ab hier der Normalfall/Gutfall


        //Hier erstmal rausfinden welches der erste Kurs der Aktie ist, der kein realtime-Kurs ist
        String text;
        int count = 0;
        for (int i = 0; i < 101000010; i++) {
            Element e2 = doc.getElementsByClass("ellipsis padding-right-5").get(i);
            text = e2.toString();
            if (text.contains("mp_realtime")) {
                count++;
            } else {
                break;
            }
        }
        //System.out.println(count);


        Element e3 = doc.getElementsByClass("ellipsis padding-right-5").get(count);
        Element meta = e3.child(0);
        //System.out.println("Handelsplatz: " +e.text());

        //Ab hier etwas unschön programmiert aber funktioniert!
        //meta zu String umwandeln
        String temp2 = meta.toString();
        int x = 0;
        int y = 0;

        //meta-String durchlaufen bis man den Anfang von "boerse_id=.." gefunden hat
        while (temp2.charAt(x) != '?') {
            x++;
        }
        //und bis man das Ende gefunden hat
        while (y < x) {
            while (temp2.charAt(y) != '"') {
                y++;

            }
            y++;
        }

        //Substring ausgeben
        String temp3 = temp2.substring(x + 1, y - 1);
        //System.out.println(temp3);

        return temp3;
    }


    /**
     * die WKN einer Aktie herausfinden
     * @author Lukas Meinzer
     * @param str (Aktienname) in der Form <name>-aktie
     * @throws UnknownHostException
     * @throws ConnectException
     * @retun WKN
     */
    public String getWKN(String str) throws IOException {
        if (str != null) {


            try {
                doc = Jsoup.connect("https://www.ariva.de/" + str).get();
            } catch (ConnectException e) {
                System.err.println("Probleme bei der Link-Eingabe.");
            }


            Element e = doc.getElementsByClass("verlauf snapshotInfo").get(0);
            Element temp = e.child(0);
            String wkn = temp.toString();
            //testen ob überhaupt eine WKN vorhanden ist
            if (wkn.contains("WKN:")) {
                wkn = wkn.substring(12, 18);


                System.out.println(wkn);


                return wkn;
            } else {
                System.out.println("Der eingegebene String enthält keine WKN");
                return "---";
            }

        } else {
            throw new IOException("Stringübergabe fehlgeschlagen");
        }
    }


    /**
     * liest alle links zu den monaten der historischen kurse in eine arraylist ein
     * @param str sollte später die url einer aktie sein, Verbindung zur feeder klasse
     * @throws IOException
     */
    public void kursLink(String str) throws IOException {

        String link = str;

        //link = "https://www.ariva.de/allianz-aktie/historische_kurse?boerse_id=6&month=1900-10-31&currency=&clean_split=1&clean_split=0&clean_payout=0&clean_bezug=1&clean_bezug=0";
        String d = str + "/historische_kurse";
        doc = Jsoup.connect(d).get();
        int i = link.indexOf("month=");
        int laenge = link.length();
        String teil1;
        String teil2;
        String link2;
        teil1 = link.substring(0, i);
        teil2 = link.substring(i + 16, laenge);


        Element e = doc.select("label").get(1);
        int groesse = e.childNodeSize();
        int e2 = e.nextElementSibling().childNodeSize();
        Element e3 = e.nextElementSibling().child(2);
        //System.out.println(e2);


        int j = 1;
        while (j < (e2 / 2)) {
            String erg = e.nextElementSibling().child(j).val();
            String res;
            res = teil1 + "month=" + erg + teil2;
            al.add(res);

            j++;

        }
        //System.out.println(al.size());


    }

    /**
     * ausgabe der größe der hashmap, in der die key value paare von datum und kurs gespeichert sind
     */
    public void aus() {
        System.out.println(hm.size());
        System.out.println(hm);
    }

    /**
     * bringt den kurs in das richtige float/double format (ersetzt , mit . usw)
     * @param content
     * @return
     */
    public String mod(String content) {

        if (content.contains(".")) {
            content = content.replace(".", "");
        }

        if (content.contains(",")) {
            content = content.replace(",", ".");
        }

        return content;
    }


    public String ablauf(String link) throws IOException {


        String g = link + "/historische_kurse?" + haupthandelsplatz(link) + "&month=2019-03-31&clean_split=1&clean_split=0&clean_payout=0&clean_bezug=1&clean_bezug=0";

        return g;
    }


}
