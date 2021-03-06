package Websites;

import Hilfsklassen.Hilfsmethoden;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
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
     */
    public void connect(String ISIN_Aktienlink) throws IOException {
        this.aktienname = null;
        this.ISIN = ISIN_Aktienlink;
        url += ISIN;
        url += "/bilanz-guv";

        //doc = Jsoup.connect(url).get();
        doc = Jsoup.connect(url).get();
        System.out.println("Ariva:connect(): " + url + " verbunden.");
    }

    /**
     * Stellt eine Connection zur website her
     * @param ISIN gueltige ISIN.
     * @param Aktienname gueltiger Aktienname.
     * @throws IOException
     */
    public void connect(String ISIN, String Aktienname) throws IOException {
        //für jedes leerzeichen im aktiennamen ein "-" einfügen
        this.aktienname = Hilfsmethoden.replaceSpaceWithHyphen(Aktienname);
        this.ISIN = ISIN;
        url += ISIN;
        url += "/bilanz-guv";

        //doc = Jsoup.connect(url).get();
        doc = Jsoup.connect(url).get();
        System.out.println("Ariva:connect(): " + url + " verbunden.");
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

        //doc = Jsoup.connect(url).get();
        doc = Jsoup.connect(url).get();
        System.out.println("Ariva:connect(): " + url + " verbunden.");
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
}