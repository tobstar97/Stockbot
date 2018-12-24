package Feeder;

import Datenbank.DBOperations;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class FeederOnvistaLowMem {

    Boolean debug = true;
    Connection conn;
    String url = "https://www.onvista.de/aktien/aktien-laender/";

    public FeederOnvistaLowMem(Connection connection) {
        conn = connection;
        try {
            if(debug) System.out.println("FeederOnvista()");
            searchLaender();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void searchLaender() throws IOException{
        Document doc = Jsoup.connect(url).get();
        Element e = doc.getElementsByClass("land-list").first();
        int i = 0;
        while (i<e.childNodeSize()/2){
            Element table = doc.getElementsByClass("land-list-item").get(i);
            Element meta = table.child(0);
            String result = meta.attr("abs:href");
            searchAktien(result);
            i++;
            if(debug) System.out.println("getLaender(): " + result);
        }
    }

    private void searchAktien(String input) throws IOException {
        Document doc = Jsoup.connect(input).get();
        Element e = doc.getElementsByClass("LETZTER").first();

        if (e == null){
            int f = doc.select("div.BLAETTER_NAVI:nth-child(1) > span:nth-child(1)").size();

            String cut = input;
            f += 1;
            int o = 0;

            while(o<=f){
                cut += (o+1);
                int t = doc.select("td.TEXT").size();
                //System.out.println(t);
                int i = 0;
                while(i < t){       //try catch benötigt (wie unten) -- erledigt
                    try{
                        doc = Jsoup.connect(cut).get();
                        Element meta = doc.select("td.TEXT").get(i);
                        String table = meta.child(0).attr("abs:href");
                        if (debug) System.out.println("getAktien(): " + table);
                        getData(table);
                    }catch (Exception ex){
                        System.err.println("FeederOnvistaLowMem: Z.82: " + ex.getCause() + " | " + ex.getMessage() + " | " + ex.getStackTrace());
                    }finally {
                        i = i+3;
                    }
                }
                o++;
                cut = cut.replace(String.valueOf(o), "");
            }
        } else {
            String cut = input;
            String temp = e.child(0).attr("abs:href");
            temp = temp.replace(cut, "");

            for(int i=1; i<=Integer.parseInt(temp); i++) {
                cut += i;
                //seite nach aktien durchlaufen
                int t = doc.select("td.TEXT").size();
                for(int kl=0; kl<t; kl++) {
                    try {
                        doc = Jsoup.connect(cut).get();
                        Element meta = doc.select("td.TEXT").get(kl);
                        String tab = meta.child(0).attr("abs:href");
                        getData(tab);
                        if (debug) System.out.println("getAktien(): " + tab);
                    } catch (Exception ex) {
                        System.err.println("FeederOnvistaLowMem: Z.107: " + ex.getCause() + " | " + ex.getMessage() + " | " + ex.getStackTrace());
                    } finally {
                        kl += 3;
                    }
                }
                cut = cut.replace(String.valueOf(i), "");
                i++;
            }
        }
    }

    private void getData(String input) throws IOException {
        String isin = input.substring(input.length()-12);
        String aname = input.substring(31,input.length()-13);
        String wkn = Jsoup.connect(input).get().select("div[class=\"WERTPAPIER_DETAILS\"]").first().text().substring(4,10);
        if(debug) System.out.println("FeederOnvistaLowMem: getData() > ISIN: " + isin);
        if(debug) System.out.println("FeederOnvistaLowMem: getData() > Aktienname: " + aname);
        if(debug) System.out.println("FeederOnvistaLowMem: getData() > WKN: " + wkn);

        //Fundamentalkennzahlen (Jahresabschluss):
        String basicLink = "https://www.onvista.de/aktien/fundamental/";
        String connectToLink = basicLink + aname + "-" + isin;
        Document doc = Jsoup.connect(connectToLink).get();
        //Umsatz in Mio. EUR
            //Jahre
        Element years = doc.select("th").get(32);
        Elements yearsTable = years.siblingElements();
            //Daten
        Element data = doc.getElementsByClass("INFOTEXT").get(8);
        Elements dataTable = data.siblingElements();
        //System.out.println(dataTable);
/*            //Strings
        for(int i=0; i<yearsTable.size() && i<dataTable.size(); i++) {
            String year = yearsTable.get(i).text().replaceAll("&nbsp;","null");
            String dat = dataTable.get(i).text().replaceAll("&nbsp;","null");
            dat = dat.replaceAll("e","");
            year = year.replaceAll("e","");
            if(debug) System.out.println("FeederOnvistaLowMem: Umsatz: Jahr(" + i + "): " + year);
            if(debug) System.out.println("FeederOnvistaLowMem: Umsatz: Daten(" + i + "): " + dat);
            if(!(year == null || year.equals("") || dat == null || dat.equals(""))) {   //nur für vorhandene Werte weiterverarbeiten (Achtung: "-" ist vorhandener Wert, aber ungültig!)
                //TODO: hier Datenbank-Operationen oder whatever zur Datenweiterverarbeitung!
                System.out.println("Hier könnte Ihre Werbung stehen (" + i + ")");
            }
        }
*/
        //Bilanz-GUV
        String guvLink = "https://www.onvista.de/aktien/bilanz-guv/";
        String connGUVLink = guvLink + aname + "-" + isin;
        doc = Jsoup.connect(connGUVLink).get();
        //Umsatz in Mio. EUR
            //Jahre
        years = doc.select("th").get(26);
        yearsTable = years.siblingElements();
            //Daten
        data = doc.select("tr").get(15);
        String daten = data.text().substring(19);
            //Strings
        for(int i=0; i<yearsTable.size(); i++) {
            String year = yearsTable.get(i).text().replaceAll("&nbsp;","null");
            String dat = "";
            //String in folgender Darstellung aufteilen auf die einzelnen Jahreszahlen: "Jahr1 Jahr2 Jahr3 Jahr4"
            for(int j=0; j<daten.length(); j++) {
                if(' ' == (daten.charAt(j))) {
                    dat = daten.substring(0,j);
                    daten = daten.substring(j+1);
                    break;
                }
            }
            dat = dat.replaceAll("&nbsp;","null");
            dat = dat.replaceAll("e","");
            year = year.replaceAll("e","");
            if(debug) System.out.println("FeederOnvistaLowMem: Umsatz: Jahr(" + i + "): " + year);
            if(debug) System.out.println("FeederOnvistaLowMem: Umsatz: Daten(" + i + "): " + dat);
            if(!(year == null || year.equals("") || dat == null || dat.equals(""))) {   //nur für vorhandene Werte weiterverarbeiten (Achtung: "-" ist vorhandener Wert, aber ungültig!)
                //TODO: hier Datenbank-Operationen oder whatever zur Datenweiterverarbeitung!
                System.out.println("Hier könnte Ihre Werbung stehen (" + i + ")");
            }
        }
        /*
        try {
            DBOperations.dbInsertFeeder(conn, isin, wkn, aname, input, "Onvista");
        } catch (SQLException ex) {
            System.err.println("FeederOnvistaLowMem: Z.126: " + ex.getCause() + " | " + ex.getMessage() + " | " + ex.getStackTrace());
        }
        */
    }

}