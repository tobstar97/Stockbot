package Hilfsklassen;

import java.util.Date;
import java.text.SimpleDateFormat;

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
        return text.substring(4,23);
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

    public static String formatTimeOnvista(String s) {
        String f = "";
        f = f + s.substring(0,10);
        f = f + " " + s.substring(11,19);
        return f;
    }

    /**
     * Timestamps auf einheitliches Format bringen
     * @param s String
     * @return String
     */
    public static String formatTimeAriva(String s) {
        String f = "";
        f = f + "3000-01-01 " + s;
        return f;
    }

    /**
     * Timestamps auf einheitliches Format bringen.
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
    
    
   /**
     * @author Lukas Meinzer
     * @merge 2019-01-06
     * Log-Datei die überall aufrufbar ist
     * einem sagt in welcher Klasse und in welcher Methode sie aufgerufen wurde und zu welcher Zeit sie aufgerufen wurde
     * außerdem hat sie eine variierende Anzahl an Übergabeparametern
     * Parameter werden wie folgt übergeben:
     * "Attribut1","Wert1","Attribut2","Wert2",...
     * die übergebenen Attribute sollen baumartig, jeweils paarweise unter den oben genannten Daten gespeichert werden
     * das ganze soll in eine XML-Datei gespeichert werden und schön übersichtlich sein
     */
   public static void logdatei(String... params) {
        //Wie soll die Log-Datei heißen?:
        String logdateiName = "test.txt";

        //Methode, Klasse rausfinden in der sie aufgerufen wurde:
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];

        String klassenname = stackTraceElement.getClassName(); //Klassenname
        String methodenName = stackTraceElement.getMethodName(); //Methodenname

        //sowie die Zeit rausfinden in der sie aufgerufen wurde:
        Date datum = new Date();
        //Format-Änderung der Datumsanzeige:
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String date = simpleDateFormat.format(datum);

        //jetzt muss das ganze in eine txt-Datei gespeichert werden:
        //("\r\n" sorgt für eine neue Zeile in der txt-Datei)
        PrintWriter out;
        try{
            out = new PrintWriter(new FileWriter(logdateiName,true));
            out.append("Klassenname: "+klassenname+"\r\n");
            out.append("Methodenname: "+methodenName+"\r\n");
            out.append("Datum: "+date+"\r\n");
            out.append("\r\n"); out.write("\r\n");
            for(int j=0;j<params.length;j=j+2) {
                out.append(params[j]);   //Attribut
                out.append("\t\t");  //Abstand
                out.append(params[j + 1]); //Attributwert
                out.append("\r\n");         //neue Zeile
            }
            out.close();
            System.out.println("Log-Datei namens "+logdateiName+" erfolgreich erstellt.");
        } catch(IOException e){
            System.err.println("Logdatei-Fehler");
            e.printStackTrace();
        }
    }

}
