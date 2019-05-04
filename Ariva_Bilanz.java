package Ariva;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * @author Tobias Heiner
 * Bilanzdaten von Ariva (Umsatz, Analystenmeinung, Gewinn, EBIT, Eigenkapital, Gesamtkapital, Waehrung, letztes Update, Jahr, ISIN
 */
public class Ariva_Bilanz {
    Document doc;


    /**
     * liest die Bilanzdaten einer Aktie aus
     *
     * @param link URL einer Aktie auf ariva.de
     */
    public void bianz(String link) throws IOException {
        //Problem: es gibt einige Aktienlinks die anders aufgebaut sind

        if (link.contains("https://www.ariva.de/quote/profile")) {
            link = link.replace("profile", "statistics/facunda");

            System.out.println(link);

        } else {

            link = link + "/bilanz-guv?page=";         //Link aus Ariva_Feeder zusammensetzen
        }


        doc = Jsoup.connect(link).get();
        Element pagenode = doc.select("#pageFundamental > div:nth-child(3) > form:nth-child(1) > select:nth-child(2)").first();
        int page = pagenode.childNodeSize();
        page = (page / 2) - 1;
        //ab hier werden die verschiedenen Zeitintervalle bestimmt, sodass es nur minimale Ueberschneidungen gibt

        int i = 0;
        while (i <= page) {
            String str = Integer.toString(i);
            //hier wird der entgueltige Link zusammengesetzt
            System.out.println(get_link(str, link));
            get_data(get_link(str, link));
            if ((i + 6) > page) {         //es werden immer 6 Jahre gleichzeitig angezeigt


                if (i == page) {
                    break;
                }
                i = page;

            } else {


                i = i + 6;
            }
        }



    }

    /**
     * setzt den Link aus url + pagenummer zusammen
     * @param i pagenummer
     * @param link url
     * @return
     */
    public String get_link(String i, String link) {
        return link + i;

    }


    public void get_data(String link)throws IOException{

        doc = Jsoup.connect(link).get();

        //Waehrung bekommen
        Element waehrung = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(2) > h3:nth-child(1)").first();
        String waehrung_str = waehrung.text();
        waehrung_str = waehrung_str.substring(19,22);
        System.out.println(waehrung_str);

        //Variablenbenennung
        // erstes Jahr(ganz links)
        //Jahr auswaehlen
        Element jahr1 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2)").last();
        String jahr_str1 = ntest(jahr1);
        //Umsatz
        Element umsatz1 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2)").last();
        String umsatz_str1 = ntest(umsatz1);
        //ebit
        Element ebit1 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2)").first();
        String ebit_str1 = ntest(ebit1);
        //Gewinn
        Element gewinn1 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(12) > td:nth-child(2)").first();
        String gewinn_str1 = ntest(gewinn1);
        //Fremdkapital
        Element fremdkapital1 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(18) > td:nth-child(2)").first();
        String fremdkapital_str1 = ntest(fremdkapital1);
        //Eigenkapital
        Element eigenkapital1 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(21) > td:nth-child(2)").first();
        String eigenkapital_str1 = ntest(eigenkapital1);

        //zweites Jahr (wie oben)
        Element jahr2 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(3)").first();
        String jahr_str2 = ntest(jahr2);
        Element umsatz2 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(3)").first();
        String umsatz_str2 = ntest(umsatz2);
        Element ebit2 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(3)").first();
        String ebit_str2 = ntest(ebit2);
        Element gewinn2 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(12) > td:nth-child(3)").first();
        String gewinn_str2 = ntest(gewinn2);
        Element fremdkapital2 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(18) > td:nth-child(3)").first();
        String fremdkapital_str2 = ntest(fremdkapital2);
        Element eigenkapital2 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(21) > td:nth-child(3)").first();
        String eigenkapital_str2 = ntest(eigenkapital2);

        //drittes Jahr (wie oben)
        Element jahr3 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(4)").first();
        String jahr_str3 = ntest(jahr3);
        Element umsatz3 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(4)").first();
        String umsatz_str3 = ntest(umsatz3);
        Element ebit3 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(4)").first();
        String ebit_str3 = ntest(ebit3);
        Element gewinn3 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(12) > td:nth-child(4)").first();
        String gewinn_str3 = ntest(gewinn3);
        Element fremdkapital3 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(18) > td:nth-child(4)").first();
        String fremdkapital_str3 = ntest(fremdkapital3);
        Element eigenkapital3 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(21) > td:nth-child(4)").first();
        String eigenkapital_str3 = ntest(eigenkapital3);

        //viertes Jahr (wie oben)
        Element jahr4 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(5)").first();
        String jahr_str4 = ntest(jahr4);
        Element umsatz4 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(5)").first();
        String umsatz_str4 = ntest(umsatz4);
        Element ebit4 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(5)").first();
        String ebit_str4 = ntest(ebit4);
        Element gewinn4 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(12) > td:nth-child(5)").first();
        String gewinn_str4 = ntest(gewinn4);
        Element fremdkapital4 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(18) > td:nth-child(5)").first();
        String fremdkapital_str4 = ntest(fremdkapital4);
        Element eigenkapital4 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(21) > td:nth-child(5)").first();
        String eigenkapital_str4 = ntest(eigenkapital4);

        //fuenftes Jahr (wie oben)
        Element jahr5 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(6)").first();
        String jahr_str5 = ntest(jahr5);
        Element umsatz5 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(6)").first();
        String umsatz_str5 = ntest(umsatz5);
        Element ebit5 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(6)").first();
        String ebit_str5 = ntest(ebit5);
        Element gewinn5 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(12) > td:nth-child(6)").first();
        String gewinn_str5 = ntest(gewinn5);
        Element fremdkapital5 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(18) > td:nth-child(6)").first();
        String fremdkapital_str5 = ntest(fremdkapital5);
        Element eigenkapital5 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(21) > td:nth-child(6)").first();
        String eigenkapital_str5 = ntest(eigenkapital5);

        //sechstes Jahr (wie oben)
        Element jahr6 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(7)").first();
        String jahr_str6 = ntest(jahr6);
        Element umsatz6 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(7)").first();
        String umsatz_str6 = ntest(umsatz6);
        Element ebit6 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(7)").first();
        String ebit_str6 = ntest(ebit6);
        Element gewinn6 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(12) > td:nth-child(7)").first();
        String gewinn_str6 = ntest(gewinn6);
        Element fremdkapital6 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(18) > td:nth-child(7)").first();
        String fremdkapital_str6 = ntest(fremdkapital6);
        Element eigenkapital6 = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(21) > td:nth-child(7)").first();
        String eigenkapital_str6 = ntest(eigenkapital6);

    }

    /**
     * Test, ob ein Element null ist
     * @param e Element (z.B. umsatz1)
     * @return entweder  "-" oder den Text des Elements
     */
    public String ntest(Element e){
        if(e == null){
            return "-";

        }else{
            return e.text();
        }
    }


}
