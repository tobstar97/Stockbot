/**
 * @author Tobias Heiner
 * @description stellt die verbindung zur website ariva.de her und liefert den aktuellen Kurs und Gewinn
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
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
    Hashtable<String, Double> hm = new Hashtable<String, Double>();
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();


    public void connectKurs(String url) throws IOException{
        doc = Jsoup.connect(url).get();
        //System.out.println("Connection erfolgreich");
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

        int i = 0;
        int k = 0;
        double d = 0;
        String st;
        Element testdate = doc.getElementsByClass("arrow0").get(0);
        String s = testdate.text();


        int z=0;
        while(z<al.size()) {
            System.out.println(al.get(z));
            connectKurs(al.get(z));
            doc = Jsoup.connect("https://www.ariva.de/spotify-aktie/historische_kurse?boerse_id=21&amp;month=2018-10-31&amp;currency=&amp;clean_split=1&amp;clean_split=0&amp;clean_payout=0&amp;clean_bezug=1&amp;clean_bezug=0").get();
            k = doc.getElementsByClass("arrow0").size();
            //System.out.println(k);

            while(i<k) {
                //System.out.println("Test");
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
                System.out.println(d+"  "+st);
                hm.put(st, d);
                System.out.println(hm.size());

                i++;

            }
            z++;
            i = 0;
        }




    }

    public String haupthandeslplatz (String str)throws IOException{
        doc = Jsoup.connect("https://www.ariva.de/allianz-aktie").get();
        Element e = doc.getElementById("list_short");
        Element meta = e.child(1);
        System.out.println(meta);

        return null;
    }

    public void kursLink(String str) throws IOException{

        String link = "https://www.ariva.de/spotify-aktie/historische_kurse?boerse_id=21&month=2018-11-30&currency=&clean_split=1&clean_split=0&clean_payout=0&clean_bezug=1&clean_bezug=0";
        doc = Jsoup.connect("https://www.ariva.de/spotify-aktie/historische_kurse").get();
        int i = link.indexOf("month=");
        int laenge = link.length();
        String teil1;
        String teil2;
        String link2;
        teil1 = link.substring(0, i);
        teil2 = link.substring(i+16, laenge);


        Element e = doc.select("label").get(1);
        int groesse = e.childNodeSize();
        int e2 = e.nextElementSibling().childNodeSize();
        Element e3 = e.nextElementSibling().child(2);
        //System.out.println(e2);


        int j = 1;
        while(j<(e2/2)){
            String erg = e.nextElementSibling().child(j).val();
            String res;
            res = teil1+"month="+erg+teil2;
            al.add(res);

            j++;

        }
        //System.out.println(al.size());





    }


    public void aus(){
        //System.out.println(hm.values());
    }
}
