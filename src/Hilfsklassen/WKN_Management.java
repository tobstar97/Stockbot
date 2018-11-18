package Hilfsklassen;

import java.util.HashMap;

/**
 * @author Tobias Heiner
 * speichert alle ISINs und WKNs in einer HashMap (notwendig, da die linkzusammensetzungen nicht mit wkns funktioniert
 * später soll die HashMap eventuell über die Feeder Klassen "gefüttert" werden
 */
public class WKN_Management {
    /**
     * Map aller WKN mit zugehöriger ISIN
     */
    HashMap<String, String> wm = new HashMap<>();

    /**
     * fügt ein WKN ISIN Paar hinzu
     * @param isin
     * @param wkn
     */
    public void add(String isin, String wkn){
        wm.put(wkn, isin);
        //wm.put("1", "2");
    }

    /**
     * liefert die ISIN einer Aktie
     * @param wkn
     * @return ISIN einer Aktie
     */
    public String getISIN(String wkn){
        System.out.println("WKN_Management:getISIN(): " + wm.get(wkn));
        return wm.get(wkn);
    }
}