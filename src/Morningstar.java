import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Tobias Heiner
 * @description stellt die verbindung zur website morninstar.de her und liefert den aktuellen Kurs und Gewinn
 */
public class Morningstar {

    String aktienname;
    String ISIN;
    String url = "http://tools.morningstar.de/de/stockreport/default.aspx?Site=de&id=0P000002HD&LanguageId=de-DE&SecurityToken=0P000002HD]3]0]E0WWE$$ALL";      //TODO
    Document doc;
    Scanner scan;

    double kurs;
    double gewinn;


    /**
     * Stellt eine Connection zur website her
     * @throws IOException
     */
    public void connect()throws IOException{        //TODO

        doc = Jsoup.connect(url).get();

    }

    /**
     *Liefert den aktuellen Kurs einer Aktie auf morningstar
     * @return Kurs als double
     */
    public  double getKurs(){
        //choosing the right tag in the html document
        Element span = doc.getElementById("Col0Price");
        String content = span.text();

        //replacing "." and "," for parsing the content to a double value
        if(content.contains(".")){
            content = content.replace(".","");
        }
        if(content.contains(",")){
            content = content.replace(",",".");
        }
        kurs = Double.parseDouble(content);
        return kurs;
    }


    /**
     * Liefert den aktuellen gewinn eines Unternehmens auf Morningstar
     * @return gewinnn als double
     */
    public double getGewinn(){
        Element meta = doc.getElementsByClass("number").get(11);



        String content = meta.text();

        if(content.contains(".")){
            content = content.replace(".","");
        }

        if(content.contains(",")){
            content = content.replace(",",".");
        }

        //System.out.println(content);
        gewinn = Double.parseDouble(content);

        return gewinn;
    }


}