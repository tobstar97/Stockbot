package Hilfsklassen;

/**
 * @author andygschaider
 * @version poc
 * @since poc
 */
public class Hilfsmethoden {

    private static boolean debug = true;

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

//    ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== =====
//                                               Timestamp - Formatting
//    ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== =====

//    === === === === === === === === === === = Alles Rund Um Die Domain ==== === === === === === === === === ===

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

//    === === === === === === === === === === = Alles Rund Um Die Zeit == === === === === === === === === === ===

    /**
     * Kuerzt einen String auf die 8 ersten Chars herunter
     * @author andygschaider
     * @param text String mit Format Zeit, irgendein Text
     * @return Zeit
     */
    public static String shortenTimeAriva(String text, int i) {
        //16.11.18 17:35:21 Uhr Xetra | Mehr Kurse »
        if(i==1) {
            if(text.charAt(2) == ':') {
                if(debug) System.out.println("shortenTimeAriva: int i=" + i + ", String text=" + text + ", String return=" + "1900-01-01 " + text.substring(0,8));
                return "1900-01-01 " + text.substring(0,8);
            } else {
                if(debug) System.out.println("shortenTimeAriva: int i=" + i + ", String text=" + text + ", String return =" + "20" + text.substring(6, 8) + "-" + text.substring(3, 5) + "-" + text.substring(0, 2) + " " + text.substring(9, 17));
                return "20" + text.substring(6, 8) + "-" + text.substring(3, 5) + "-" + text.substring(0, 2) + " " + text.substring(9, 17);
            }
        }
        else {
            if(debug) System.out.println("shortenTimeAriva: int i=" + i + ", String text=" + text + ", String return=" + text.substring(0,8));
            return text.substring(0,8);
        }
    }

    /**
     * Kuerzt einen String auf die Zeitinformaiton herunter
     * @author andygschaider
     * @param text String mit Format per , Zeit, irgendein Text
     * @return Zeit
     */
    public static String shortenTimeMorningstar(String text) {
        return text.substring(4,23);
    }

    /**
     * Timestamps der Website OnVista auf einheitliches Format bringen.
     * @author andgschaider
     * @param s String
     * @return String
     */
    public static String formatTimeOnvista(String s) {
        String f = "";
        f = f + s.substring(0,10);
        f = f + " " + s.substring(11,19);
        return f;
    }

    /**
     * Timestamps der Website Ariva auf einheitliches Format bringen
     * @author andygschaider
     * @param s String
     * @return String
     */
    public static String formatTimeAriva(String s, int i) {
        if(i==1) {return s;}
        if(debug) System.out.println("formatTimeAriva: i=" + i + ", String s=" + s);
        return "1900-01-01 " + s;
    }

    /**
     * Timestamps der Website Morningstar auf einheitliches Format bringen.
     * @author andygschaider
     * @param s String
     * @return String
     */
    public static String formatTimeMorningstar(String s) {
        String f ="";
        f = f + s.substring(6,10);
        f = f + '-';
        f = f + s.substring(3,5);
        f = f + '-';
        f = f + s.substring(0,2);
        f = f + ' ';
        f = f + s.substring(11);
        return f;
    }

    public static void broodForce(String alphabet, String result, int length) {
        long sum = 0;
        for(int i=1; i<=length; i++) {
            System.out.println("broodForce(" + i + "): " + (int) Math.pow(alphabet.length(),i) + " Lösungen.");
            sum += (int) Math.pow(alphabet.length(),i);
        }
        System.out.println("broodForce(x): " + sum + " Lösungen.");
        for(int i=0; i < length; i++) {
            broodForceInsinde(alphabet,"",i+1);
        }
    }

    private static void broodForceInsinde(String alphabet, String result, int length) {
        char[] chararr = alphabet.toCharArray();
        if(result.length() < length) {
            for (int i = 0; i < chararr.length; i++) {
                String temp = result;
                temp += chararr[i];
                //if(debug) System.out.println("broodForce(" + temp + ")");
                broodForceInsinde(alphabet, temp, length);
            }
        }
    }

}