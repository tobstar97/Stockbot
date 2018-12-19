import Exceptions.NullStringException;
import Hilfsklassen.Hilfsmethoden;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Tobias Heiner
 * @description stellt die verbindung zur website ariva.de her und liefert den aktuellen Kurs und Gewinn
 */
public class Ariva {

    String aktienname;
    String ISIN;
    String url = "https://www.ariva.de/";
    String strdUrlBilanzGuV = "https://www.ariva.de/amazon-aktie/bilanz-guv#stammdaten";    //+ aktienname mit "-" + "-" + ISIN         Standardurl für bilanz-guv
    Document doc;

    Scanner scan;

    double gewinn;
    double kurs;

    /**
     * Resets all parameters in here.
     */
    public void disconnect() {
        this.ISIN = null;
        this.aktienname = null;
        doc = null;
        this.url = "https://www.ariva.de/";
        this.gewinn = -1.0;
        this.kurs = -1.0;
    }

    /**
     * Stellt eine Connection zur website her
     * @param ISIN_Aktienlink gueltige ISIN oder gueltiger Aktienlink.
     * @throws IOException
     * @throws NullStringException wenn ISIN_Aktienlink null ist
     */
    public void connect(String ISIN_Aktienlink) throws IOException, NullStringException {
        if(ISIN_Aktienlink!= null){
            this.aktienname = null;
            this.ISIN = ISIN_Aktienlink;
            url += ISIN;
            url += "/bilanz-guv";

            //doc = Jsoup.connect(url).get();
            doc = Jsoup.connect(url).get();
            System.out.println("Ariva:connect(): " + url + " verbunden.");
        }else{
            throw new NullStringException("Class: Ariva - Method: connect(String) - übergebener String ist null");
        }
    }

    /**
     * Stellt eine Connection zur website her
     * @param ISIN gueltige ISIN.
     * @param Aktienname gueltiger Aktienname.
     * @throws IOException
     * @throws NullStringException wenn einer der übergebenen Strings null ist
     */
    public void connect(String ISIN, String Aktienname) throws IOException, NullStringException {
        if(ISIN != null && Aktienname!=null){
            //für jedes leerzeichen im aktiennamen ein "-" einfügen
            this.aktienname = Hilfsmethoden.replaceSpaceWithHyphen(Aktienname);
            this.ISIN = ISIN;
            url += ISIN;
            url += "/bilanz-guv";

            //doc = Jsoup.connect(url).get();
            doc = Jsoup.connect(url).get();
            System.out.println("Ariva:connect(): " + url + " verbunden.");
        }else{
            throw new NullStringException("Class: Ariva - Method: connect(String, String) - übergebener String ist null");
        }
    }

    /**
     * Stellt eine Connection zur website her
     * @throws IOException
     */
    public void connect() throws IOException {
        scan = new Scanner(System.in);

        System.out.println("Geben sie den Aktiennamen ein");
        aktienname = scan.next();

        //einzufügen: für jedes leerzeichen im aktiennamen ein "-" einfügen

        System.out.println("Geben sie die ISIN ein");
        ISIN = scan.next();

        url += ISIN;
        url += "/bilanz-guv";

        try{
            //doc = Jsoup.connect(url).get();
            doc = Jsoup.connect(url).get();
            System.out.println("Ariva:connect(): " + url + " verbunden.");
        }catch(IOException e){
            System.err.println("Class: Ariva - Method: connect - Verbindung konnte nicht hergestellt werden");
        }
    }

    /**
     *Liefert den aktuellen Kurs einer Aktie auf ariva
     * @return Kurs als double
     */
    public double getKurs(){
        Element span = doc.select("span[itemprop=price]").first();
        String content;
        content = span.text();
        //System.out.println(content);
        //System.out.println(convert+"TEST");

        if(content.contains(".")){
            content = content.replace(".","");
        }
        if(content.contains(",")){
            content = content.replace(",",".");
        }
        kurs = Double.parseDouble(content);
        //System.out.println(kurs);
        return kurs;
    }


    /**
     * Liefert den aktuellen gewinn eines Unternehmens auf ariva
     * @return gewinnn als double
     */
    public double getGewinn(){
        Element meta = doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(3);
        Element table = meta.siblingElements().get(4);
        //Element tb = table.elementSiblingIndex();
        String content = table.text();

        if(content.contains(".")){
            content = content.replace(".","");
        }
        if(content.contains(",")){
            content = content.replace(",",".");
        }
        gewinn = Double.parseDouble(content);
        return gewinn;
    }


    /**
     * @author Lukas Meinzer, Tobias Heiner
     * funktioniert!
     * soll den hauphandelsplatz einer aktie rausfinden
     * https://www.ariva.de/amazon-aktie/kurs immer der 4. eintrag von oben in der tabelle ist der haupthandelsplatz
     * daraus muss die boerse_id ausgelesen werden, z.B. bei Nasdaq 40
     * @param str in Form von "<name>-aktie"
     * @return
     * @throws IOException
     */
    public String haupthandelsplatz (String str)throws IOException{
        doc = Jsoup.connect("https://www.ariva.de/"+str+"/kurs").get();
        Element e = doc.getElementsByClass("ellipsis padding-right-5").get(3);
        Element meta = e.child(0);
        System.out.println("Handelsplatz: " +e.text());

        //Ab hier etwas unschön programmiert aber funktioniert!
        //meta zu String umwandeln
        String temp = meta.toString();
        int x=0;
        int y=0;

        //meta-String durchlaufen bis man den Anfang von "boerse_id=.." gefunden hat
        while(temp.charAt(x)!='?'){
            x++;
        }
        //und bis man das Ende gefunden hat
        while(y<x){
            while(temp.charAt(y)!='"'){
                y++;

            }
            y++;
        }

        //Substring ausgeben
        String temp2 = temp.substring(x+1, y-1);
        System.out.println(temp2);

        return null;
    }


    /**
     * die WKN einer Aktie herausfinden
     * @author Lukas Meinzer
     * @param str (Aktienname)
     * @throws UnknownHostException
     * @throws ConnectException
     * @throws NullStringException
     * @retun WKN
     */
    public String getWKN(String str) throws IOException, NullStringException{
        if(str!=null){


            //hier irgendwie testen ob der Links mit str+"-aktie" überhaupt existiert,
            //man könnte ja auch nach der WKN eines Indexes fragen
            boolean conn = true;
            try{
                //schauen ob die URL existiert
                java.net.InetAddress.getByName("https://www.ariva.de/"+str+"-aktie");
                //temp bleibt true
            } catch(UnknownHostException e){
                //wenn nicht wird die Bedingung false gesetzt
                conn=false;
            }


            if(conn){
                try{
                    doc = Jsoup.connect("https://www.ariva.de/"+str+"-aktie").get();
                } catch (ConnectException e){
                    System.err.println("Probleme bei der Link-Eingabe.");
                }
            }
            else{
                try{
                    doc = Jsoup.connect("https://www.ariva.de/"+str+"-index").get();
                } catch(ConnectException e){
                    System.err.println("Connection Probleme");
                }
            }



            Element e = doc.getElementsByClass("verlauf snapshotInfo").get(0);
            Element temp = e.child(0);
            String wkn = temp.toString();
            //testen ob überhaupt eine WKN vorhanden ist
            if(wkn.contains("WKN:")){
                wkn = wkn.substring(12,18);


                System.out.println(wkn);


                return wkn;
            }
            else{
                System.out.println("Der eingegebene String enthält keine WKN");
                return "---";
            }

        }
        else{
            throw new NullStringException("Stringübergabe fehlgeschlagen");
        }
    }
}