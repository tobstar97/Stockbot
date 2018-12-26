import Datenbank.DBConnection;
import Datenbank.DBOperations;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Tobias Heiner
 * @description stellt die verbindung zur website onvista.de her und liefert den aktuellen Kurs und Gewinn
 */
public class Onvista {

    private static boolean debug = true;
    private static Connection conn;

    {
        try {
            conn = new DBConnection().setupConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void catchBilanzData(String input) throws IOException, SQLException {
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
            float u = Float.parseFloat(umsatz);
            u *= 1000000;   //Umsatz *= 1.000.000
            umsatz = String.valueOf(u);
            float g = Float.parseFloat(gkap);
            g *= 1000000;   //Bilanzsumme *= 1.000.000
            gkap = String.valueOf(g);
            //nur für vorhandene Werte weiterverarbeiten
            //(Achtung: "-" ist gültiger Wert für Bilanzdaten, drückt aus, dass keine Daten vorhanden!)
            //(Achtung: "JahrXe" wird angezeigt, falls keine Daten vorhanden (glaube ich zumindest)!)
            if (!(year.equals("") || umsatz.equals("") || ekap.equals("") || gkap.equals("") || ebit.equals("") || jue.equals(""))) {
                //TODO: hier Datenbank-Operationen oder whatever zur Datenweiterverarbeitung!
                if (InetAddress.getLocalHost().toString().contains("Andreas-PC")) {
                    if (debug) System.out.println("FeederOnvistaLowMem: Schleife(" + i + "),Jahr(" + year + "),Umsatz(" + umsatz + "),Eigenkap(" + ekap + "),Gesamtkap(" + gkap + "),EBIT(" + ebit + "),JÜ(" + jue + ")");
                        //DBOperations.dbInsertOnvistaBilanzData(conn, isin, wkn, aname, year, umsatz, ekap, gkap, ebit, jue);
                } else System.out.println("FeederOnvistaLowMem: Z.187: Hier könnte ein Insert-Befehl stehen.");
            }
        }
    }



}