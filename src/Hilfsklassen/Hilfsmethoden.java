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
     * Log-Datei die überall aufrufbar ist
     * einem sagt in welcher Klasse und in welcher Methode sie aufgerufen wurde und zu welcher Zeit sie aufgerufen wurde
     * außerdem hat sie eine variierende Anzahl an Übergabeparametern
     * Parameter werden wie folgt übergeben:
     * "Attribut1","Wert1","Attribut2","Wert2",...
     * die übergebenen Attribute sollen baumartig, jeweils paarweise unter den oben genannten Daten gespeichert werden
     * das ganze soll in eine XML-Datei gespeichert werden und schön übersichtlich sein
     */
    public static void logdatei(String... params) {
        //Methode, Klasse rausfinden in der sie aufgerufen wurde:
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];

        String klassenname = stackTraceElement.getClassName(); //Klassenname
        String methodenName = stackTraceElement.getMethodName(); //Methodenname

        //sowie die Zeit rausfinden in der sie aufgerufen wurde:
        Date datum = new Date();
        String date = datum.toString(); //aktuelle Zeit

        //Anzahl an übergebenen Objekten (muss eine gerade Zahl sein):
        //evtl. unnötig zu wissen
        int i;
        for(i=0;i<params.length;i++){
            //System.out.println(params[i]);
        }
        //System.out.println(i);
        //wenn i gerade ist steht es für das Attribut
        //wenn i ungerade ist steht es für den Attributwert



        //jetzt muss das ganze in eine txt-Datei gespeichert werden:
        BufferedWriter out;
        try{
            out = new BufferedWriter(new FileWriter("test.txt"));
            out.write("Klassenname: "+klassenname);
            out.newLine();
            out.write("Methodenname: "+methodenName);
            out.newLine();
            out.write("Zeit: "+date);
            out.newLine(); out.newLine();
            for(int j=0;j<params.length;j=j+2) {
                out.write(params[j]);   //Attribut
                out.write("\t\t");  //Abstand
                out.write(params[j + 1]); //Attributwert
                out.newLine();          //neue Zeile
            }
            out.close();
        } catch(IOException e){
            System.err.println("Logdatei-Fehler");
            e.printStackTrace();
        }
    }

}
