import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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


    public void connect() throws IOException {
        scan = new Scanner(System.in);

        System.out.println("Geben sie den Aktiennamen ein");
        aktienname = scan.next();

        //einzufügen: für jedes leerzeichen im aktiennamen ein "-" einfügen

        System.out.println("Geben sie die ISIN ein");
        ISIN = scan.next();

        url = url + ISIN;

        //doc = Jsoup.connect(url).get();
        doc = Jsoup.connect(strdUrlBilanzGuV).get();
        System.out.println("basst");

    }

    public double getKurs(){
        Element span = doc.select("span[itemprop=price]").first();
        String content;

        content = span.text();


        //System.out.println(content);

        //System.out.println(convert+"TEST");

        if(content.contains(",")){
            content = content.replace(",",".");
        }

        if(content.contains(".")){
            content = content.replace(".","");
        }
        double r = Double.parseDouble(content);
        r = r/10;
        //System.out.println(r);
        return r;

    }

    public double getGewinn(){
        Element meta = doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(3);
        Element table = meta.siblingElements().get(4);
        //Element tb = table.elementSiblingIndex();
        String content = table.text();

        if(content.contains(".")){
            content = content.replace(".","");
        }

        double r = Double.parseDouble(content);

        System.out.println(r);
        return r;
    }
}
