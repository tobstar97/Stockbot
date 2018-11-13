package Hilfsklassen;

/**
 * @author andygschaider
 * @since poc
 */
public class Hilfsmethoden {


    /**
     * Extrahiert aus einer url die domain.
     * @author andygschaider
     * @param url mit folgender Syntax: *.domain.*
     * @return Substring aus url, welcher die domain ist.
     */
    public static String getDomainFromUrl(String url) {
        //Die ersten 2 Punkte finden
        int punktanfang = url.indexOf('.');
        int punktende = url.indexOf('.', punktanfang+1);
        //neuen String aus Begrenzung heraus bilden
        return url.substring(punktanfang+1,punktende);
    }

    /**
     * Kuerzt alle url-Bestandteile vor der domain heraus.
     * @author andygschaider
     * @deprecated
     * @param url mit folgender Syntax: *.domain.*
     * @return Substring aus url: domain.*
     */
    public static String urlKuerzenBisDomain(String url) {
        int punktanfang = url.indexOf('.');
        return url.substring(punktanfang+1);
    }

    /**
     * Zeigt ob die url eine url zu einem Index ist oder nicht.
     * @author andygschaider
     * @deprecated
     * @param url mit folgender Syntax: *.ariva.de/*
     * @return true wenn Index-url, false wenn nicht.
     */
    public static boolean istArivaIndex (String url) {
        //System.out.println(">> Hilfsmethoden.istArivaIndex");
        if(url.contains("index")) return true;
        if(url.contains("kursliste")) return true;
        return url.contains("dax");
    }

    /**
     * Kuerzt einen String auf die 8 ersten Chars herunter
     * @author andygschaider
     * @param text String mit Format Zeit, irgendein Text
     * @return Zeit
     */
    public static String shortenTimeAriva(String text) {
        return text.substring(0,8);
    }

    /**
     * Kuerzt einen String auf die Zeitinformaiton herunter
     * @author andygschaider
     * @param text String mit Format per , Zeit, irgendein Text
     * @return Zeit
     */
    public static String shortenTimeMorningstar(String text) {
        return text.substring(4,22);
    }

    /**
     * Ersetzt alle Leerzeichen mit Bindestrichen
     * @author andygschaider
     * @param s String mit Leerzeichen
     * @return String mit Bindestrich statt Leerzeichen
     */
    public static String replaceSpaceWithHyphen (String s) {
        String r = "";
        for(int i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if(c == ' ') {
                c = '-';
            }
            r = r + c;
        }
        return r;
    }

}
