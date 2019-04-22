package Ariva;


/**
 * @author Tobias Heiner
 * @description downloadet ein CSV-File mit allen historischen Kursdaten einer Aktie auf ariva.de
 * @verbindung um das Programm automatisiert ablaufen zu lassen, muss die Methode kurs_csv_link automatisch links
 *              von der Feeder_Ariva Klasse bekommen -> siehe Steuerung-Klasse
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import java.net.URL;

import java.io.*;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.Scanner;


public class Ariva_Kurs_CSV {

    Document doc;





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
     * @param link: URL der Aktie (bekommt man von Feeder_Ariva) -> Steuerungsklasse
     * @return fertiger CSV-Download-Link
     */
    public void kurs_csv_link(String link)throws IOException{
        link = link + "/historische_kurse";         //Link aus Ariva_Feeder zusammensetzen
        doc = Jsoup.connect(link).get();            //html-Dokument downloaden
        Element secu = doc.select("div.formRow:nth-child(4) > form:nth-child(2) > input:nth-child(1)").first();            //"secu"-Element auswaehlen und in secu speichern
        String secu_s = secu.val();             //value des CSS-Elements als String in secu_s
        Element boerse_id = doc.select("div.formRow:nth-child(4) > form:nth-child(2) > input:nth-child(2)").first();            //"boerse_id"-Element auswaehlen
        String boerse_id_s = boerse_id.val();           //value des CSS-Elements als String in boerse_id_s
        //Downloadlink zusammensetzen
        String csv_down = "https://www.ariva.de/quote/historic/historic.csv?secu=" + secu_s + "&boerse_id=" + boerse_id_s + "&clean_split=1&clean_payout=0&clean_bezug=1&min_time=01.01.90&max_time=" + aktuelles_datum() + "&trenner=%3B&go=Download[HTTP/2.0";
        System.out.println(csv_down);

        //Benennung der CSV-Datei mit der ISIN der Aktie; Speicherung in Ordner CSV-Daten
        //mein Verzeichnis sieht folgendermaßen aus: Ariva_Test > .idea, CSV-Daten, out,  src(> Main, Steuerung, ..)
        Element isin = doc.select(".verlauf > div:nth-child(2)").first();
        String i = isin.text();
        i = i.replace("ISIN: ", "");
        i = i + ".csv";

        File targetDir=new File("CSV-Daten");
        File targetFile=new File(targetDir, i);

        System.out.println(i);

        //Download der CSV-Datei
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(csv_down).openStream());
             FileOutputStream fileOS = new FileOutputStream(targetFile)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            // handles IO exceptions
        }
    }


    /**
     *
     * @return aktuelles Datum als String
     */
    public String aktuelles_datum(){
        Date d = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String datum = df.format(d);
        //System.out.println(datum);
        return datum;
    }


}
