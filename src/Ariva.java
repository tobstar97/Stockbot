/**
 * @author Tobias Heiner
 * @description stellt die verbindung zur website ariva.de her und liefert den aktuellen Kurs und Gewinn
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;



public class Ariva {

    String aktienname;
    String ISIN;
    String url = "https://www.ariva.de/";
    String strdUrlBilanzGuV = "https://www.ariva.de/amazon-aktie/bilanz-guv#stammdaten";    //+ aktienname mit "-" + "-" + ISIN         Standardurl für bilanz-guv
    Document doc;

    Scanner scan;

    double gewinn;
    double kurs;
    HashMap<String, Double> hm = new HashMap<String, Double>();


    public void connectKurs() throws IOException{
        doc = Jsoup.connect("https://www.ariva.de/amazon-aktie/historische_kurse?boerse_id=40&month=2018-11-30&currency=&clean_split=1&clean_split=0&clean_payout=0&clean_bezug=1&clean_bezug=0").get();
        System.out.println("Connection erfolgreich");
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

        url = url + ISIN;

        //doc = Jsoup.connect(url).get();
        doc = Jsoup.connect("https://www.ariva.de/wirecard-aktie/bilanz-guv").get();
        System.out.println("basst");

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
        System.out.println(gewinn);

        return gewinn;
    }

    /**
     * Liefert die Analysen für 12 Monate
     */
    public void getAnalyse(){
        Element meta = doc.getElementsByClass("ag_analysis analysis abstand").first();

        Element table = meta.children().first();
        Element kaufen = table.select("td").get(1);
        Element halten = table.select("td").get(2);
        Element verkaufen = table.select("td").get(3);
        System.out.println(kaufen.text());
        System.out.println(halten.text());
        System.out.println(verkaufen.text());
    }


    public void getKursGesamt()throws IOException{
        connectKurs();
        int i = 0;
        int k = 0;
        double d = 0;
        String st;
        Element testdate = doc.getElementsByClass("arrow0").get(0);
        String s = testdate.text();

        k = doc.getElementsByClass("arrow0").size();


        while(i<k) {
            String content;
            Element meta = doc.getElementsByClass("arrow0").get(i);
            Element date = meta.child(0);
            Element value = meta.child(4);

            content = value.text();

            if(content.contains(".")){
                content = content.replace(".","");
            }

            if(content.contains(",")){
                content = content.replace(",",".");
            }

            d = Double.parseDouble(content);
            st = date.text();
            hm.put(st, d);

            i++;

        }
    }
}
