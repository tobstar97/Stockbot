package Feeder;

import Datenbank.DBOperations;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
            getLaender();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getLaender() throws IOException{
        Document doc = Jsoup.connect(url).get();
        Element e = doc.getElementsByClass("land-list").first();
        int i = 0;
        while (i<e.childNodeSize()/2){
            Element table = doc.getElementsByClass("land-list-item").get(i);
            Element meta = table.child(0);
            String result = meta.attr("abs:href");
            getAktien(result);
            i++;
            if(debug) System.out.println("getLaender(): " + result);
        }
    }

    private void getAktien(String input) throws IOException {
        Document doc = Jsoup.connect(input).get();
        Element e = doc.getElementsByClass("LETZTER").first();

        if (e == null){
            /*
            int t = doc.select("td.TEXT").size();
            for(int i=0; i<t; i++) {
                Element meta = doc.select("td.TEXT").get(i);
                try {
                    String table = meta.child(0).attr("abs:href");
                    getData(table);
                    if (debug) System.out.println("getAktien(): " + table);
                } catch (IndexOutOfBoundsException ex) {
                    System.err.println("FeederOnvistaLowMem: Z.60: " + ex.getCause() + " | " + ex.getMessage() + " | " + ex.getStackTrace());
                } finally {
                    i += 3;
                }
            }
            */
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

    private void getData(String input) {
        String isin = input.substring(input.length()-12);
        String aname = input.substring(31,input.length()-13);
        if(debug) System.out.println("getData() > ISIN: " + isin);
        if(debug) System.out.println("getData() > Aktienname: " + aname);
        try {
            DBOperations.dbInsert(conn, isin, aname, input, "Onvista");
        } catch (SQLException ex) {
            System.err.println("FeederOnvistaLowMem: Z.126: " + ex.getCause() + " | " + ex.getMessage() + " | " + ex.getStackTrace());
        }
    }

}