import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class Onvista {

    String aktienname;
    String ISIN;
    String url = "http://www.onvista.de/aktien/bilanz-guv/Wirecard-Aktie-DE0007472060";
    String strdUrlBilanzGuV = "http://www.onvista.de/aktien/bilanz-guv/";    //+ aktienname mit "-" + "-" + ISIN         Standardurl für bilanz-guv
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

        //url = url + ISIN;

        doc = Jsoup.connect(url).get();
    }



    

    public double getKurs(){

        //meta property tag auswaehlen, in dem der Aktienkurs steht
        Element meta = doc.select("meta[property=schema:price]").first();

        //meta attribut auslesen
        String content = meta.attr("content");


        double r = Double.parseDouble(content);
        return r;

    }

    public double getGewinn(){
        //meta property tag auswaehlen, in dem der Aktienkurs steht
        Element meta = doc.getElementsByClass("SUMME").first();



        //meta attribut auslesen


        //System.out.println(meta);

        Element table = meta.children().get(1);

        Elements tds = table.select("td");




        System.out.println(tds.get(0).text());

        String content ="1";


        double r = Double.parseDouble(content);
        return r;
    }




}
