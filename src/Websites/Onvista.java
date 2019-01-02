package Websites;

import Datenbank.DBConnection;
import Datenbank.DBOperations;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Andreas Gschaider
 * @description fetcht Bilanz- und Kursdaten einer Aktie von Onvista.
 */
public class Onvista {

    private static boolean debug = true;
    private static Connection conn;
    private Document doc;

    {
        try {
            conn = new DBConnection().setupConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void catchBilanzData(String input) throws UnknownHostException {
        String isin = input.substring(input.length() - 12);
        String aname = input.substring(30, input.length() - 13);
        String wkn = null;
        Boolean wknBool = true;
        try {
            wkn = Jsoup.connect(input).get().select("div[class=\"WERTPAPIER_DETAILS\"]").first().text().substring(4, 10);//verifizieren, dass es sich wirklich um eine WKN handelt, oder ob die WKN nicht vorhanden ist
            for (int i = 0; i < wkn.length(); i++) {
                if (wkn.charAt(i) >= 97 && wkn.charAt(i) <= 122) wknBool = false;
            }
        } catch (IOException e){
            System.err.println(this.getClass().getName() + ": catchBilanzData: Z.41: no wkn - IOException" + e.getMessage() + " | " + e.getCause());
        } catch (NullPointerException e) {
            System.err.println(this.getClass().getName() + ": catchBilanzData: Z.41: no wkn - Nullpointer" + e.getMessage() + " | " + e.getCause());
        }

        if (!wknBool) wkn = null;
        if (debug) System.out.println(this.getClass().getName() + ": getData() > ISIN: " + isin);
        if (debug) System.out.println(this.getClass().getName() + ": getData() > Aktienname: " + aname);
        if (debug) System.out.println(this.getClass().getName() + ": getData() > WKN: " + wkn);

        //Bilanzdaten
        String guvLink = "https://www.onvista.de/aktien/bilanz-guv/";
        String connGUVLink = guvLink + aname + "-" + isin;
        try {
            doc = Jsoup.connect(connGUVLink).get();
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": unable to connect to Document doc with link \"" + connGUVLink + "\"");
        }
        //Plausibilisierung des Links zu den Bilanzdaten der Aktie
        boolean valid = true;
        if(doc.select("h1[property=\"v:title\"]").get(0).text().equals("404 Seite")) {
            valid = false;  //Error 404 page not found.
        } else if(doc.select("a[href]").size() < 150 || doc.select("a[href]").size() > 300) {
            valid = false;  //< 150 || > 300: wir sind gerade auf keiner Bilanzseite, da die Anzahl der auf ihr befindlichen Links nicht stimmt!
        }

        //nur falls auf valide Bilanzseite verbunden ausführen:
        if(valid) {

            //Jahreszahlen extrahieren                  get(26)                 hier keine Suche im Dokument, da dies bisher immer funktioniert hat
            Element years = doc.select("th").get(26);
            Elements yearsTable = years.siblingElements();
            //Umsatz in Mio. EUR                        get(15)
            int getUmsatz = -1;
            String umsatzString = "Umsatz in Mio. EUR";
            for (int i = 0; i < doc.select("tr").size(); i++) {    //Suche im Dokument nach dem Umsatz
                if (doc.select("tr").get(i).text().length() >= umsatzString.length())
                    if (doc.select("tr").get(i).text().substring(0, umsatzString.length()).equals(umsatzString))
                        getUmsatz = i;
            }
            Element umsatzData = null;
            if (getUmsatz != -1) umsatzData = doc.select("tr").get(getUmsatz);
            String umsatzDaten = "- - - - -";
            if (getUmsatz != -1) umsatzDaten = umsatzData.text().substring(umsatzString.length() + 1);
            //Eigenkapital                              get(8)      //Suche im Dokument nach dem Eigenkapital
            int getEkap = -1;
            String eKapString = "Eigenkapital";
            for (int i = 0; i < doc.select("tr").size(); i++) {    //Suche im Dokument nach dem Umsatz
                if (doc.select("tr").get(i).text().length() >= eKapString.length())
                    if (doc.select("tr").get(i).text().substring(0, eKapString.length()).equals(eKapString))
                        getEkap = i;
            }
            Element ekapData = null;
            if (getEkap != -1) ekapData = doc.select("tr").get(getEkap);
            String ekapDaten = "- - - - -";
            if (getEkap != -1) ekapDaten = ekapData.text().substring(eKapString.length() + 1);
            //Gesamtkapital (=Bilanzsumme in Mio. EUR)  get(5)
            int getGkap = -1;
            String gKapString = "Bilanzsumme in Mio. EUR";
            for (int i = 0; i < doc.select("tr").size(); i++) {
                if (doc.select("tr").get(i).text().length() >= gKapString.length())
                    if (doc.select("tr").get(i).text().substring(0, gKapString.length()).equals(gKapString))
                        getGkap = i;
            }
            Element gkapData = null;
            if (getGkap != -1) gkapData = doc.select("tr").get(getGkap);
            String gkapDaten = "- - - - -";
            if (getGkap != -1) gkapDaten = gkapData.text().substring(gKapString.length() + 1);
            //EBIT                                      get(26)
            int getEbit = -1;
            String ebitString = "EBIT";
            for (int i = 0; i < doc.select("tr").size(); i++) {
                if (doc.select("tr").get(i).text().length() >= ebitString.length())
                    if (doc.select("tr").get(i).text().substring(0, ebitString.length()).equals(ebitString) && !doc.select("tr").get(i).text().substring(0, ebitString.length() + 2).equals(ebitString + "DA"))
                        getEbit = i;
            }
            Element ebitData = null;
            if (getEbit != -1) ebitData = doc.select("tr").get(getEbit);
            String ebitDaten = "- - - - -";
            if (getEbit != -1) ebitDaten = ebitData.text().substring(ebitString.length() + 1);
            //Jahresüberschuss (Gewinn)                 get(24)
            int getJue = -1;
            String jueString = "Jahresüberschuss";
            for (int i = 0; i < doc.select("tr").size(); i++) {
                if (doc.select("tr").get(i).text().length() >= jueString.length())
                    if (doc.select("tr").get(i).text().substring(0, jueString.length()).equals(jueString)) getJue = i;
            }
            Element jueData = null;
            if (getJue != -1) jueData = doc.select("tr").get(getJue);
            String jueDaten = "- - - - -";
            if (getJue != -1) jueDaten = jueData.text().substring(jueString.length() + 1);

            //Multiplier abhängig von jeweiliger Bilanzierungsmethode TODO:Multiplier abhängig von Bilanzierungsmethode
            int getMultiplier = -1;
            String multiplierString = "nach ";
            for (int i = 0; i < doc.select("td[class=\"ZAHL\"]").size(); i++) {
                if (doc.select("td[class=\"ZAHL\"]").get(i).text().length() >= multiplierString.length())
                    if (doc.select("td[class=\"ZAHL\"]").get(i).text().substring(0, multiplierString.length()).equals(multiplierString))
                        getMultiplier = i;
            }
            Element multiplierData = null;
            if (getMultiplier != -1) multiplierData = doc.select("td[class=\"ZAHL\"]").get(getMultiplier);
            String multiplierDaten = "- - - - -";
            String bilanzierungsmethode = null;
            String multiplierValueString = null;
            String waehrung = null;
            int multiplier = 1;
            if (getMultiplier != -1) {
                multiplierDaten = multiplierData.text().substring(multiplierString.length());
                //if (debug) System.out.println("multiplierDaten: " + multiplierDaten);
                bilanzierungsmethode = multiplierDaten.substring(0, multiplierDaten.indexOf(" in "));
                multiplierValueString = multiplierDaten.substring(multiplierDaten.indexOf(" in ") + 4, multiplierDaten.lastIndexOf('.'));
                waehrung = multiplierDaten.substring(multiplierDaten.lastIndexOf('.') + 2);
                if (debug) System.out.println(this.getClass().getName() + ": getData() > Bilanzierungsmethode: " + bilanzierungsmethode);
                if (debug) System.out.println(this.getClass().getName() + ": getData() > Multiplier: " + multiplierValueString);
                if (debug) System.out.println(this.getClass().getName() + ": getData() > Währung: " + waehrung);
                if(multiplierValueString.equals("Mio")) multiplier = 1000000;
                if(multiplierValueString.equals("Tsd")) multiplier = 1000;  //kein Plan ob es den Multiplier Tausend gibt... aber einfach mal vorsorglich.
            }

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
                umsatz = umsatz.replaceAll(",", ".");
                ekap = ekap.replaceAll(",", ".");
                gkap = gkap.replaceAll(",", ".");
                ebit = ebit.replaceAll(",", ".");
                jue = jue.replaceAll(",", ".");
                //Anwendung des Multipliers
                if(!umsatz.equals("-")) {  //parseFloat funktioniert nicht wenn input = '-'
                    float u = Float.parseFloat(umsatz); //muss float sein für Berechnung
                    u *= multiplier;   //Umsatz *= 1.000.000
                    umsatz = String.valueOf(u); //Konvertierung in String für Speicherung in DB (ggf. anzupassen: TODO)
                }
                if(!ekap.equals("-")) {
                    float e = Float.parseFloat(ekap);
                    e *= multiplier;
                    ekap = String.valueOf(e);
                }
                if(!gkap.equals("-")) {
                    float g = Float.parseFloat(gkap);
                    g *= multiplier;   //Bilanzsumme *= 1.000.000
                    gkap = String.valueOf(g);
                }
                if(!ebit.equals("-")) {
                    float e = Float.parseFloat(ebit);
                    e *= multiplier;
                    ebit = String.valueOf(e);
                }
                if(!jue.equals("-")) {
                    float j = Float.parseFloat(jue);
                    j *= multiplier;
                    jue = String.valueOf(j);
                }

                //nur für vorhandene Werte weiterverarbeiten
                //(Achtung: "-" ist gültiger Wert für Bilanzdaten, drückt aus, dass keine Daten vorhanden!)
                //(Achtung: "JahrXe" wird angezeigt, falls keine Daten vorhanden (glaube ich zumindest)!)
                if (!(year.equals("") || umsatz.equals("") || ekap.equals("") || gkap.equals("") || ebit.equals("") || jue.equals(""))) {
                    //TODO: hier Datenbank-Operationen oder whatever zur Datenweiterverarbeitung!
                    if (InetAddress.getLocalHost().toString().contains("Andreas-PC")) {
                        if (debug)
                            System.out.println("FeederOnvistaLowMem: Schleife(" + i + "),Jahr(" + year + "),Währung(" + waehrung + "),Umsatz(" + umsatz + "),Eigenkap(" + ekap + "),Gesamtkap(" + gkap + "),EBIT(" + ebit + "),JÜ(" + jue + ")");
                    /*
                    try {
                        DBOperations.dbInsertOnvistaBilanzData(conn, isin, wkn, aname, year, waehrung, umsatz, ekap, gkap, ebit, jue);
                    } catch(SQLException e) {
                        System.err.println(this.getClass().getName() + ": catchBilanzData: Z.200: " + e.getMessage() + " | " + e.getCause());
                    }
                    */
                    } else System.out.println("FeederOnvistaLowMem: Z.266: Hier könnte ein Insert-Befehl stehen.");
                }
            }
        } else System.err.println(this.getClass().getName() + ": catchBilanzData(): kein gültiger Bilanzlink: \"" + input + "\"");
    }



}