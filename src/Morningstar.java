/**
 * @author Tobias Heiner
 */


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;




public class Morningstar {

    String aktienname;
    String ISIN;
    String url = "http://tools.morningstar.de/de/stockreport/default.aspx?Site=de&id=0P000002HD&LanguageId=de-DE&SecurityToken=0P000002HD]3]0]E0WWE$$ALL";
    Document doc;
    Scanner scan;

    double kurs;
    double gewinn;

    public void connect()throws IOException{
        //problem mit der url von morningstar, da diese nicht so einfach ist wie bei ariva/onvista
        /*scan = new Scanner(System.in);

        System.out.println("Geben sie den Aktiennamen ein");
        aktienname = scan.next();

        //einzufügen: für jedes leerzeichen im aktiennamen ein "-" einfügen

        System.out.println("Geben sie die ISIN ein");
        ISIN = scan.next();

        url = url + ISIN;*/

        //doc = Jsoup.connect(url).get();
        doc = Jsoup.connect(url).get();
        System.out.println("basst");

    }

    public  double getKurs(){
        Element span = doc.getElementById("Col0Price");

        String content = span.text();

        if(content.contains(".")){
            content = content.replace(".","");
        }

        if(content.contains(",")){
            content = content.replace(",",".");
        }

        kurs = Double.parseDouble(content);


        return kurs;

    }


}
