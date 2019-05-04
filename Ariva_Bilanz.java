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
     * @param link URL einer Aktie auf ariva.de
     */
    public void bianz(String link) throws IOException {
        //Problem: es gibt einige Aktienlinks die anders aufgebaut sind

        if (link.contains("https://www.ariva.de/quote/profile")) {
            link = link.replace("profile", "statistics/facunda");

            System.out.println(link);

        } else {

            link = link + "/bilanz-guv?page=0";         //Link aus Ariva_Feeder zusammensetzen
        }


        doc = Jsoup.connect(link).get();
        Element pagenode = doc.select("#pageFundamental > div:nth-child(3) > form:nth-child(1) > select:nth-child(2)").first();
        int page = pagenode.childNodeSize();
        page = (page/2) - 1;
        //ab hier werden die verschiedenen Zeitintervalle bestimmt, sodass es nur minimale Ueberschneidungen gibt
        //spaeter mit strings statt int i, damit der entgültige Link zusammengesetzt wird
        int i = 0;
        while (i <= page){

            if((i+6)>page){         //es werden immer 6 Jahre gleichzeitig angezeigt

                System.out.println(pagenode.child(i).text());
                if(i==page){
                    break;
                }
                i = page;

            }else{
                System.out.println(pagenode.child(i).text());
                i = i+6;
            }
        }
        System.out.println(pagenode.childNodeSize());


    }



}
