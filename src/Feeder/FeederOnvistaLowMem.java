package Feeder;

import Datenbank.DBOperations;
import Websites.Onvista;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;

public class FeederOnvistaLowMem {

    Boolean debug = true;
    Connection conn;
    Onvista onvista;
    String url = "https://www.onvista.de/aktien/aktien-laender/";

    public FeederOnvistaLowMem(Connection connection) {
        conn = connection;
        onvista = new Onvista();
        try {
            if(debug) System.out.println(">> " + this.getClass().getName());
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
        //wenn es kein LETZTE Seite - Element gibt
        if (e == null){
            int f = doc.select("div.BLAETTER_NAVI:nth-child(1) > span:nth-child(1)").size();

            String cut = input;
            f += 1;
            int o = 0;

            while(o<=f){
                cut += (o+1);
                aktieDurchlaufen(input,cut);
                o++;
                cut = cut.replace(String.valueOf(o), "");
            }
        } else { //wenn es ein LETZTE Seite - Element gibt: letzte Seite wählen, Wert speichern und Seiten durchlaufen
            String cut = input;
            String temp = e.child(0).attr("abs:href");
            temp = temp.replace(cut, "");

            for(int i=1; i<=Integer.parseInt(temp); i++) {
                cut += i;
                aktieDurchlaufen(input,cut);
                cut = cut.replace(String.valueOf(i), "");
                i++;
            }
        }
    }

    private void aktieDurchlaufen(String input, String cut) {
        //Seite nach Aktien durchlaufen
        Document doc = null;
        try {
            doc = Jsoup.connect(input).get();
        } catch (IOException e) {
            System.err.println(this.getClass().getName() + ": aktieDurchlaufen(): Z95: unable to connect to document \"" + input + "\"");
        }
        int t = doc.select("td.TEXT").size();
        for(int kl=0; kl<t; kl+=3) {
            try {
                doc = Jsoup.connect(cut).get();
                Element meta = doc.select("td.TEXT").get(kl);
                String tab = meta.child(0).attr("abs:href");
                onvista.catchBilanzData(tab);
                if (debug) System.out.println("aktieDurchlaufen(): " + tab);
            } catch (Exception ex) {
                System.err.println(this.getClass().getName() + ": aktieDurchlaufen(): Z.100: " + ex.getCause() + " | " + ex.getMessage());
            }
        }
    }
/*
    private void catchBilanzData(String input) throws IOException, SQLException {
        String isin = input.substring(input.length() - 12);
        String aname = input.substring(30, input.length() - 13);
        String wkn = Jsoup.connect(input).get().select("div[class=\"WERTPAPIER_DETAILS\"]").first().text().substring(4, 10);
        Boolean wknBool = true;
        //verifizieren, dass es sich wirklich um eine WKN handelt, oder ob die WKN nicht vorhanden ist
        for (int i = 0; i < wkn.length(); i++) {
            if (wkn.charAt(i) >= 97 && wkn.charAt(i) <= 122) wknBool = false;
        }
        if (!wknBool) wkn = null;
        if (debug) System.out.println("FeederOnvistaLowMem: getData() > ISIN: " + isin);
        if (debug) System.out.println("FeederOnvistaLowMem: getData() > Aktienname: " + aname);
        if (debug) System.out.println("FeederOnvistaLowMem: getData() > WKN: " + wkn);

        //Bilanzdaten
        String guvLink = "https://www.onvista.de/aktien/bilanz-guv/";
        String connGUVLink = guvLink + aname + "-" + isin;
        Document doc = Jsoup.connect(connGUVLink).get();
        //Jahreszahlen extrahieren                  get(26)                 hier keine Suche im Dokument, da dies bisher immer funktioniert hat
        Element years = doc.select("th").get(26);
        Elements yearsTable = years.siblingElements();
        //Umsatz in Mio. EUR                        get(15)
        int getUmsatz = -1;
        String umsatzString = "Umsatz in Mio. EUR";
        for(int i=0;i<doc.select("tr").size();i++) {    //Suche im Dokument nach dem Umsatz
            if(doc.select("tr").get(i).text().substring(0,umsatzString.length()).equals(umsatzString)) getUmsatz = i;
        }
        Element umsatzData = doc.select("tr").get(getUmsatz);
        String umsatzDaten = "- - - - -";
        if(getUmsatz != -1) umsatzDaten= umsatzData.text().substring(umsatzString.length()+1);
        //Eigenkapital                              get(8)      //Suche im Dokument nach dem Eigenkapital
        int getEkap = -1;
        String eKapString = "Eigenkapital";
        for(int i=0;i<doc.select("tr").size();i++) {    //Suche im Dokument nach dem Umsatz
            if(doc.select("tr").get(i).text().substring(0,eKapString.length()).equals(eKapString)) getEkap = i;
        }
        Element ekapData = doc.select("tr").get(getEkap);
        String ekapDaten = "- - - - -";
        if(getEkap != -1) ekapDaten = ekapData.text().substring(eKapString.length()+1);
        //Gesamtkapital (=Bilanzsumme in Mio. EUR)  get(5)
        int getGkap = -1;
        String gKapString = "Bilanzsumme in Mio. EUR";
        for(int i=0;i<doc.select("tr").size();i++) {
            if(doc.select("tr").get(i).text().substring(0,gKapString.length()).equals(gKapString)) getGkap = i;
        }
        Element gkapData = doc.select("tr").get(getGkap);
        String gkapDaten = "- - - - -";
        if(getGkap != -1) gkapDaten = gkapData.text().substring(gKapString.length()+1);
        //EBIT                                      get(26)
        int getEbit = -1;
        String ebitString = "EBIT";
        for(int i=0;i<doc.select("tr").size();i++) {
            if(doc.select("tr").get(i).text().substring(0,ebitString.length()).equals(ebitString) && !doc.select("tr").get(i).text().substring(0,ebitString.length()+2).equals(ebitString+"DA")) getEbit = i;
        }
        Element ebitData = doc.select("tr").get(getEbit);
        String ebitDaten = "- - - - -";
        if(getEbit != -1) ebitDaten = ebitData.text().substring(ebitString.length()+1);
        //Jahresüberschuss (Gewinn)                 get(24)
        int getJue = -1;
        String jueString = "Jahresüberschuss";
        for(int i=0;i<doc.select("tr").size();i++) {
            if(doc.select("tr").get(i).text().substring(0,jueString.length()).equals(jueString)) getJue = i;
        }
        Element jueData = doc.select("tr").get(getJue);
        String jueDaten ="- - - - -";
        if(getJue != -1) jueDaten = jueData.text().substring(jueString.length()+1);



        //Strings
        for (int i = 0; i < yearsTable.size(); i++) {
            String year = yearsTable.get(i).text().replaceAll("&nbsp;", "");
            String umsatz = "";
            //String in folgender Darstellung aufteilen auf die einzelnen Jahreszahlen: "Jahr1 Jahr2 Jahr3 Jahr4 ..."
            for (int j = 1; j < umsatzDaten.length(); j++) {
                if (' ' == (umsatzDaten.charAt(j))) {
                    umsatz = umsatzDaten.substring(0, j);
                    umsatzDaten = umsatzDaten.substring(j + 1);
                    break;
                }
            }
            String ekap = "";
            for (int j = 1; j < ekapDaten.length(); j++) {
                if (' ' == (ekapDaten.charAt(j))) {
                    ekap = ekapDaten.substring(0, j);
                    ekapDaten = ekapDaten.substring(j + 1);
                    break;
                }
            }
            String gkap = "";
            for (int j = 1; j < gkapDaten.length(); j++) {
                if (' ' == (gkapDaten.charAt(j))) {
                    gkap = gkapDaten.substring(0, j);
                    gkapDaten = gkapDaten.substring(j + 1);
                    break;
                }
            }
            String ebit = "";
            for (int j = 1; j < ebitDaten.length(); j++) {
                if (' ' == (ebitDaten.charAt(j))) {
                    ebit = ebitDaten.substring(0, j);
                    ebitDaten = ebitDaten.substring(j + 1);
                    break;
                }
            }
            String jue = "";
            for (int j = 1; j < jueDaten.length(); j++) {
                if (' ' == (jueDaten.charAt(j))) {
                    jue = jueDaten.substring(0, j);
                    jueDaten = jueDaten.substring(j + 1);
                    break;
                }
            }
            //letzte Ausgabe der jeweiligen Datensätze auf die Variable zuweisen; sonst: letzter Datenbankeintrag geschieht nicht weil Strings leer wären!
            if (i == yearsTable.size() - 1) {
                umsatz = umsatzDaten;
                ekap = ekapDaten;
                gkap = gkapDaten;
                ebit = ebitDaten;
                jue = jueDaten;
            }
            //in richtiges Format bringen: alle Kommas zu Punkten ändern
            umsatz = umsatz.replaceAll(",",".");
            ekap = ekap.replaceAll(",",".");
            gkap = gkap.replaceAll(",",".");
            ebit = ebit.replaceAll(",",".");
            jue = jue.replaceAll(",",".");
            //Multiplier        TODO:Multiplier abhängig von Bilanzierungsmethode
            //  FUNKTIONIERT NICHT WENN INPUT = '-'
            //float u = Float.parseFloat(umsatz);
            //u *= 1000000;   //Umsatz *= 1.000.000
            //umsatz = String.valueOf(u);
            //float g = Float.parseFloat(gkap);
            //g *= 1000000;   //Bilanzsumme *= 1.000.000
            //gkap = String.valueOf(g);

            //nur für vorhandene Werte weiterverarbeiten
            //(Achtung: "-" ist gültiger Wert für Bilanzdaten, drückt aus, dass keine Daten vorhanden!)
            //(Achtung: "JahrXe" wird angezeigt, falls keine Daten vorhanden (glaube ich zumindest)!)
            if (!(year.equals("") || umsatz.equals("") || ekap.equals("") || gkap.equals("") || ebit.equals("") || jue.equals(""))) {
                //TODO: hier Datenbank-Operationen oder whatever zur Datenweiterverarbeitung!
                if (InetAddress.getLocalHost().toString().contains("Andreas-PC")) {
                    if (debug) System.out.println("FeederOnvistaLowMem: Schleife(" + i + "),Jahr(" + year + "),Umsatz(" + umsatz + "),Eigenkap(" + ekap + "),Gesamtkap(" + gkap + "),EBIT(" + ebit + "),JÜ(" + jue + ")");
                    DBOperations.dbInsertOnvistaBilanzData(conn, isin, wkn, aname, year, umsatz, ekap, gkap, ebit, jue);
                } else System.out.println("FeederOnvistaLowMem: Z.187: Hier könnte ein Insert-Befehl stehen.");
            }
        }
    }

    private void getData(String input) throws IOException, SQLException {
        String isin = input.substring(input.length()-12);
        String aname = input.substring(30,input.length()-13);
        String wkn = Jsoup.connect(input).get().select("div[class=\"WERTPAPIER_DETAILS\"]").first().text().substring(4,10);
        Boolean wknBool = true;
        //verifizieren, dass es sich wirklich um eine WKN handelt, oder ob die WKN nicht vorhanden ist
        for(int i=0; i<wkn.length(); i++) {
            if(wkn.charAt(i) >= 97 && wkn.charAt(i) <= 122) wknBool = false;
        }
        if(!wknBool) wkn = null;
        if(debug) System.out.println("FeederOnvistaLowMem: getData() > ISIN: " + isin);
        if(debug) System.out.println("FeederOnvistaLowMem: getData() > Aktienname: " + aname);
        if(debug) System.out.println("FeederOnvistaLowMem: getData() > WKN: " + wkn);

        //Bilanzdaten
        String guvLink = "https://www.onvista.de/aktien/bilanz-guv/";
        String connGUVLink = guvLink + aname + "-" + isin;
        Document doc = Jsoup.connect(connGUVLink).get();
        //Jahreszahlen extrahieren                  get(26)
        Element years = doc.select("th").get(26);
        Elements yearsTable = years.siblingElements();
        //Umsatz in Mio. EUR                        get(15)
        Element umsatzData = doc.select("tr").get(15);
        String umsatzDaten = umsatzData.text().substring(19);
        //Eigenkapital                              get(8)
        Element ekapData = doc.select("tr").get(8);
        String ekapDaten = ekapData.text().substring(13);
        //Gesamtkapital (=Bilanzsumme in Mio. EUR)  get(5)
        Element gkapData = doc.select("tr").get(5);
        String gkapDaten = gkapData.text().substring(24);
        //EBIT                                      get(26)
        Element ebitData = doc.select("tr").get(26);
        String ebitDaten = ebitData.text().substring(5);
        //Jahresüberschuss (Gewinn)                 get(24)
        Element jueData = doc.select("tr").get(24);
        String jueDaten = jueData.text().substring(17);
        //Strings
        for(int i=0; i<yearsTable.size(); i++) {
            String year = yearsTable.get(i).text().replaceAll("&nbsp;","");
            String umsatz = "";
            //String in folgender Darstellung aufteilen auf die einzelnen Jahreszahlen: "Jahr1 Jahr2 Jahr3 Jahr4"
            for(int j=1; j<umsatzDaten.length(); j++) {
                if(' ' == (umsatzDaten.charAt(j))) {
                    umsatz = umsatzDaten.substring(0,j);
                    umsatzDaten = umsatzDaten.substring(j+1);
                    //System.out.println("umsatz("+umsatz+"),umsatzDaten("+umsatzDaten+")");
                    break;
                }
                //System.out.println("ZWEI:umsatz("+umsatz+"),umsatzDaten("+umsatzDaten+")");
            }
            String ekap = "";
            for(int j=1; j<ekapDaten.length(); j++) {
                if(' ' == (ekapDaten.charAt(j))) {
                    ekap = ekapDaten.substring(0,j);
                    ekapDaten = ekapDaten.substring(j+1);
                    break;
                }
            }
            String gkap = "";
            for(int j=1; j<gkapDaten.length(); j++) {
                if(' ' == (gkapDaten.charAt(j))) {
                    gkap = gkapDaten.substring(0,j);
                    gkapDaten = gkapDaten.substring(j+1);
                    break;
                }
            }
            String ebit = "";
            for(int j=1; j<ebitDaten.length(); j++) {
                if(' ' == (ebitDaten.charAt(j))) {
                    ebit = ebitDaten.substring(0,j);
                    ebitDaten = ebitDaten.substring(j+1);
                    break;
                }
            }
            String jue = "";
            for(int j=1; j<jueDaten.length(); j++) {
                if(' ' == (jueDaten.charAt(j))) {
                    jue = jueDaten.substring(0,j);
                    jueDaten = jueDaten.substring(j+1);
                    break;
                }
            }
            //letzte Ausgabe der jeweiligen Datensätze auf die Variable zuweisen; sonst: letzter Datenbankeintrag geschieht nicht weil Strings leer wären!
            if(i == yearsTable.size()-1) {
                umsatz = umsatzDaten;
                ekap = ekapDaten;
                gkap = gkapDaten;
                ebit = ebitDaten;
                jue = jueDaten;
            }
            //nur für vorhandene Werte weiterverarbeiten
            //(Achtung: "-" ist gültiger Wert für Bilanzdaten, drückt aus, dass keine Daten vorhanden!)
            //(Achtung: "JahrXe" wird angezeigt, falls keine Daten vorhanden (glaube ich zumindest)!)
            if(!(year.equals("") || umsatz.equals("") || ekap.equals("") || gkap.equals("") || ebit.equals("") || jue.equals(""))) {
                //TODO: hier Datenbank-Operationen oder whatever zur Datenweiterverarbeitung!
                if(InetAddress.getLocalHost().toString().contains("Andreas-PC")) {
                    if(debug) System.out.println("FeederOnvistaLowMem: Schleife("+i+"),Jahr("+year+"),Umsatz("+umsatz+"),Eigenkap("+ekap+"),Gesamtkap("+gkap+"),EBIT("+ebit+"),JÜ("+jue+")");
                    //DBOperations.dbInsertOnvistaBilanzData(conn, isin, wkn, aname, year, umsatz, ekap, gkap, ebit, jue);
                }
                else System.out.println("FeederOnvistaLowMem: getData(): Hier könnte ein Insert-Befehl stehen.");
            }
        }
        //  DATENBANK
        try {
            DBOperations.dbInsertFeeder(conn, isin, wkn, aname, input, "Onvista");
        } catch (SQLException ex) {
            System.err.println("FeederOnvistaLowMem: Z.126: " + ex.getCause() + " | " + ex.getMessage() + " | " + ex.getStackTrace());
        }
    }
*/
}