package Ariva;

import Datenbank.DBConnection;
import Datenbank.DBOperations;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tobias Heiner
 * Bilanzdaten von Ariva (Umsatz, Analystenmeinung, Gewinn, EBIT, Eigenkapital, Gesamtkapital, Waehrung, letztes Update, Jahr, ISIN
 */
public class Ariva_Bilanz {
    Document doc;
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
     * liest die Bilanzdaten einer Aktie aus
     *
     * @param link URL einer Aktie auf ariva.de
     */
    public void bilanz(String link) throws IOException {
        //Problem: es gibt einige Aktienlinks die anders aufgebaut sind

        if (link.contains("https://www.ariva.de/quote/profile")) {
            link = link.replace("profile", "statistics/facunda");

            System.out.println(link);

        } else {

            link = link + "/bilanz-guv?page=";         //Link aus Ariva_Feeder zusammensetzen
        }


        doc = Jsoup.connect(link).get();
        Element pagenode;

        try{
            pagenode = doc.select("#pageFundamental > div:nth-child(3) > form:nth-child(1) > select:nth-child(2)").first();
        }catch (Exception e){
            return;
        }


        int page = pagenode.childNodeSize();
        page = (page / 2) - 1;
        //ab hier werden die verschiedenen Zeitintervalle bestimmt, sodass es nur minimale Ueberschneidungen gibt

        int i = 0;
        while (i <= page) {
            String str = Integer.toString(i);
            //hier wird der entgueltige Link zusammengesetzt
            System.out.println(get_link(str, link));
            getdata2(get_link(str, link));
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


    /**
     * Test, ob ein Element null ist
     * Ausserdem wird der string Formatisiert
     * Zielform: z.B.: 2.5000000 oder 57.7 entspricht in der deutschen Sprachgewohnheit 57,7
     * @param e Element (z.B. umsatz1)
     * @return entweder  "-" oder den Text des Elements
     */
    public double ntest(Element e){
        String s = e.text();
        double d = 0.0;
        if((s.contains("- ")) || (s.contentEquals("")&& s.length()<1)){
            return 0.0;

        }
        else{
            //System.out.println(e.text());
            String r = e.text();
            int t = 0;
            boolean q = false;
            if(r.contains(" ")){
                r = r.replace(" ","");
            }
            if(r.contains(",")){
                 r = r.replaceAll(",","");
                 q = true;

            }
            r = r + "000000";       //Bilanzsumme immer in Millionen
            if(e.text().contains("M")){
                r = r.replaceAll("M","000");        //fuer ganz grosse Werte wird ein M dahintergeschrieben( M =: Milliarden)
            }

            if(r.contains(".")){
                r = r.replace(".", "");

            }
            //System.out.println(r);
            if(q){
                t = r.length() - 2;
                r = r.substring(0, t);
            }
            if(s.contains(" ")){
                r.replaceAll("\\S+", "");
            }
            d = Double.parseDouble(r);
            if(r.contains("-")){


                d = d* (-1);
            }

            return d;

        }
    }


    /**
     * ISIN bekommen
     * @return
     */
    public String get_ISIN(){

        Element e = doc.select("#pageSnapshotHeader > div.snapshotHeader.abstand > div.verlauf.snapshotInfo > div:nth-child(2)").first();
        String s = e.text();
        s = s.substring(6);

        return s;
    }

    /**
     * aktuelles Datum auf Tag genau bekommen
     * @return
     */
    public String get_datum(){
        Date date = java.util.Calendar.getInstance().getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormatter.format(date);
        return dateString;
    }



    /**
     * neue Bilanzmmethode, da es leider unterschiedliche Layouts gibt (zb bei Allianz und HSBC)
     */
    public void getdata2(String link)throws IOException{
        //Variablen der Bilanzwerte
        String isin;
        int jahr;
        double umsatz;
        double gewinn;
        double ebit;
        double fremdkapital;
        double eigenkapital;

        String letztesUpdate;

        String s;
        Elements elements;
        Element e;
        doc = Jsoup.connect(link).get();

        //ISIN bekommen
        isin = get_ISIN();

        //letztes Update (heute) bekommen
        letztesUpdate = get_datum();

        //Waehrung bekommen
        Element waehrung = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(2) > h3:nth-child(1)").first();
        String waehrung_str = waehrung.text();
        waehrung_str = waehrung_str.substring(19,22);
        //System.out.println(waehrung_str);


        int j = 2;
        for (j = 2; j <= 7; j++){
            //Jahr
            String query = "div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(" + j + ")";
            e = doc.select(query).first();
            query = e.text();
            jahr = Integer.parseInt(query);

            //Umsatz bzw Gesamtergebnis
            elements = doc.select("td:contains(Gesamtertrag)");
            //System.out.println(elements);
            if(elements.isEmpty()){
                e = doc.select("td:contains(Umsatz)").first();
                s = e.cssSelector();
                s = getSpalte(s, j);
                e = doc.select(s).first();
                //s = ntest(e);
                //umsatz = Double.parseDouble(s);
                umsatz = ntest(e);
            }
            else {
                //System.out.println("HALLO");
                e = doc.select("td:contains(Gesamtertrag)").first();
                s = e.cssSelector();
                s = getSpalte(s, j);
                e = doc.select(s).first();

                //s = ntest(e);
                //umsatz = Double.parseDouble(s);
                umsatz = ntest(e);
            }

            //Gewinn
            e = doc.select("td:contains(Jahresüberschuss)").first();
            s = e.cssSelector();
            s = getSpalte(s, j);
            e = doc.select(s).first();
            //s = ntest(e);
            //gewinn = Double.parseDouble(s);
            gewinn = ntest(e);

            //EBIT
            e = doc.select("td:contains(EBIT)").first();
            s = e.cssSelector();
            s = getSpalte(s, j);
            e = doc.select(s).first();
            //s = ntest(e);
            //ebit = Double.parseDouble(s);
            ebit = ntest(e);

            //Eigenkapital
            e = doc.select("td:contains(Summe Eigenkapital)").first();
            s = e.cssSelector();
            s = getSpalte(s, j);
            e = doc.select(s).first();
            //s = ntest(e);
            //eigenkapital = Double.parseDouble(s);
            eigenkapital = ntest(e);

            //Fremdkapital
            e = doc.select("td:contains(Summe Fremdkapital)").first();
            s = e.cssSelector();
            s = getSpalte(s, j);
            e = doc.select(s).first();
            //s = ntest(e);
            //fremdkapital = Double.parseDouble(s);
            fremdkapital = ntest(e);

            try {
                dbop.bilanz_insert(conn, isin, jahr, umsatz, gewinn, ebit, eigenkapital, fremdkapital, waehrung_str, letztesUpdate);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }


        }


    }


    /**
     * liefert die Spaltennummer des vorgegebenen Wertes
     * @param s CSS-Selektor des vorgegebenen Wertes (z.B. Gesamtergebnis bzw. Umsatz)
     * @param i Spaltennummer für das Jahr (z.B. 2 = 1. Jahr, 3 = 2. Jahr usw.)
     * @return  CSS-Selektor zum passenden Wert der Zeile
     */
    public String getSpalte(String s, int i){
        int end;
        end = s.indexOf(" > td.subtitle.level");
        s = s.substring(0, end);
        s = s + " > td:nth-child(" + i + ")";

        //System.out.println(s);
        return s;

    }

}
