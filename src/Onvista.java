import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class Onvista {

    Document doc;   //Website

    //Alle Daten die potenziell aus dem Dokument gezogen werden sollen
    String aktienname;
    String ISIN;
    double gewinn;
    double kurs;

    //Beispiel URL - Wirecard-Aktie
    String url = "http://www.onvista.de/aktien/bilanz-guv/Wirecard-Aktie-DE0007472060";
    //Standard URLs für die Website onvista, an welche nurnoch die jeweiligen daten gehängt werden
    String strdUrlBilanzGuV = "http://www.onvista.de/aktien/bilanz-guv/";    //+ aktienname mit "-" + "-" + ISIN         Standardurl für bilanz-guv



    public void connect() throws IOException {
        Scanner scan = new Scanner(System.in);  //muss nicht in klasse definiert werden

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

        //meta property tag auswaehlen, in dem der Gewinn steht
        Element meta = doc.getElementsByClass("SUMME").first();
        //System.out.println(meta);

        //meta attribut auslesen
        Element table = meta.children().get(1);

        Elements tds = table.select("td");

        System.out.println(tds.get(0).text());

        String content ="1";

        double r = Double.parseDouble(content);
        return r;
    }



}
