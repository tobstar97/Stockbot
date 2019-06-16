package Ariva;


/**
 * @author Tobias Heiner
 * @description downloadet ein CSV-File mit allen historischen Kursdaten einer Aktie auf ariva.de
 * @verbindung um das Programm automatisiert ablaufen zu lassen, muss die Methode kurs_csv_link automatisch links
 *              von der Feeder_Ariva Klasse bekommen -> siehe Steuerung-Klasse
 *
 * @anleitung: Objekt der Klasse Ariva_Kurs_CSV erstellen und die Methode csv-parser starten
 * diese bekommt einfach nen Aktienlink übergeben (z.B. aus dem Feeder oder der Aktienliste für Herr Lehner)
 */

import Datenbank.DBConnection;
import Datenbank.DBOperations;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import java.net.URL;

import java.io.*;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class Ariva_Kurs_CSV {

    Document doc;
    ArrayList<String> arrayList = new ArrayList<>();
    String waehrungslink;
    String i;
    String isin_s;
    String letztesUpdate;


    DBConnection dbcon = new DBConnection();
    Connection conn;

    DBOperations dbop = new DBOperations();

    {
        try {
            conn = dbcon.setupConnection("root", "tobstar");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * lädt die CSV-Datei des kompletten historischen Kurses runter
     * Schwierigkeit: Linkzusammensetzung automatisieren
     *
     * @param link: URL der Aktie (bekommt man von Feeder_Ariva) -> Steuerungsklasse
     * @return fertiger CSV-Download-Link
     */
    public void kurs_csv_link(String link) throws IOException {
        //Problem: es gibt einige Aktienlinks die anders aufgebaut sind

        if (link.contains("https://www.ariva.de/quote/profile")) {
            link = link.replace("profile", "historic");

            System.out.println(link);

        } else if (link.contains("https://www.ariva.de/anleihen")) {
            //@Lukas hier
            link = link.replace("anleihen/profil", "quote/historic");

        } else {
            //@Lukas hier
            link = link + "/historische_kurse";         //Link aus Ariva_Feeder zusammensetzen
        }

        doc = Jsoup.connect(link).get();            //html-Dokument downloaden
        Element secu = doc.select("div.formRow:nth-child(4) > form:nth-child(2) > input:nth-child(1)").first();            //"secu"-Element auswaehlen und in secu speichern
        String secu_s = secu.val();             //value des CSS-Elements als String in secu_s
        Element boerse_id = doc.select("div.formRow:nth-child(4) > form:nth-child(2) > input:nth-child(2)").first();            //"boerse_id"-Element auswaehlen
        String boerse_id_s = boerse_id.val();           //value des CSS-Elements als String in boerse_id_s


        //Waehrung des Kurses herausfinden und in der Arraylist speichern
        waehrungslink = link + "?boerse_id=" + boerse_id_s;







        //Downloadlink zusammensetzen
        String csv_down = "https://www.ariva.de/quote/historic/historic.csv?secu=" + secu_s + "&boerse_id=" + boerse_id_s + "&clean_split=1&clean_payout=0&clean_bezug=1&min_time=01.01.90&max_time=" + aktuelles_datum() + "&trenner=%3B&go=Download[HTTP/2.0";
        System.out.println(csv_down);

        //Benennung der CSV-Datei mit der ISIN der Aktie; Speicherung in Ordner CSV-Daten
        //mein Verzeichnis sieht folgendermaßen aus: Ariva_Test > .idea, CSV-Daten, out,  src(> Main, Steuerung, ..)
        Element isin = doc.select(".verlauf > div:nth-child(2)").first();
        i = isin.text();
        //isin_s = isin.text();
        i = i.replace("ISIN: ", "");
        isin_s = i;
        i = i + ".csv";

        arrayList.add(i);

        File targetDir = new File("CSV-Daten");
        File targetFile = new File(targetDir, i);

        System.out.println(i);

        //Download der CSV-Datei
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(csv_down).openStream());
             FileOutputStream fileOS = new FileOutputStream(targetFile)) {          //die Daten werden in ein CSV-File gespeichert; Name des Files: ISIN
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            // handles IO exceptions
            System.err.println(e);
        }
    }


    /**
     * @return aktuelles Datum als String
     */
    public String aktuelles_datum() {
        Date d = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String datum = df.format(d);
        //System.out.println(datum);
        return datum;
    }

    /**
     * @Lukas hier die Anleitung
     * hier muss einen Methode hin, die die Waehrung des Kurses einer Aktie besimmt
     * am einfachsten ist es, wenn du die Methoden in der kurs_csv_link Methode aufrufst ( direkt nach dem Zumassemsetzen des links zu den historischen Kursen (hab es dir markiert)
     * "Anleitung": lies am besten einfach den letzten Schlusswert einer Aktie aus (z.b. https://www.ariva.de/amazon-aktie/historische_kurse
     * da steht dann die Waehrung hhinten
     * und nun zur komplizierteren Sache: du musst es irgendwie schaffen, dass du die CSV-Daten + die Währung in die Datenbank bekommst
     * die dürfen halt nicht durchgemischt sein (also z.b. amazon mit Euro, Lufthansa mit Pfund usw.)
     * Viel Spaß!!!
     */
    /**
     * @author Lukas Meinzer
     * @param link (In welcher Form wird der hier uebergeben? --> muss man dann evtl noch anpassen)
     * @return Waehrung als String
     * @throws IOException
     */
    public String bestimme_waehrung(String link) throws IOException{
        //Bestimme die Waehrung eines Kurses:
        System.out.println(link);
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            System.err.println("Probleme bei der Link-Eingabe");
        }
        Element e = doc.select("#pageHistoricQuotes > div.column.twothirds > div > table > tbody > tr:nth-child(3) > td.font-size-14.left.nobr.colloss").first();
        if(e==null){
            e = doc.select("#pageHistoricQuotes > div.column.twothirds > div > table > tbody > tr:nth-child(3) > td.font-size-14.left.nobr.colwin").first();
        }
        String test = e.text();
        System.out.println(test);
        if (test.contains("$")){
            return "USD";
        }
        if (test.contains("€")){
            return "EUR";
        }
        if(test.contains("£")){
            return "GBP";
        }
        if (test.contains("CHF")){
            return "CHF";
        }
        return "";
    }




    public void csv_parser(String link)throws IOException{

        kurs_csv_link(link);
        String w = bestimme_waehrung(waehrungslink);
        //System.out.println("Hallo");
        Ariva_CSV_Parser ariva_csv_parser = new Ariva_CSV_Parser();
        try {
            ariva_csv_parser.test(i);
            //System.out.println("hi");
            for(String datum : ariva_csv_parser.hm.keySet()){
                //System.out.println("Test1");

                double kurs = ariva_csv_parser.hm.get(datum);
                //System.out.println("Test2");

                letztesUpdate = get_datum();
                //System.out.println(isin_s + "  " +  datum + "   " + kurs + "    " + letztesUpdate);

                dbop.kurs_insert(conn, isin_s, datum, kurs, w, letztesUpdate);
                //System.out.println("TESTE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public String get_datum(){
        Date date = java.util.Calendar.getInstance().getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormatter.format(date);
        return dateString;
    }


}
