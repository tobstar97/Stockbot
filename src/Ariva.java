import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

/*
    === === === === === Relevante Infos für Ariva.de === === === === ===

    Eine Liste aller Aktien bekommt man unter https://www.ariva.de/aktien/suche#page=0&year=_year_2017&sort_n=ariva_name&sort_d=asc
    Dies ist relevant für die Klasse FeederAriva.java, um
        "&" -> "-"
        " " -> "_"
        "-aktie" nach dem aktiennamen in der url

        tbody -> tr -> td -> a href="/aktienname"

 */

public class Ariva {

    private String aktienname;
    private String ISIN;
    private String url = "https://www.ariva.de/";
    private String strdUrlBilanzGuV = "http://www.onvista.de/aktien/bilanz-guv/";    //+ aktienname mit "-" + "-" + ISIN         Standardurl für bilanz-guv
    private Document doc;

    private Scanner scan;

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

        doc = Jsoup.connect(url).get();

        System.out.println("basst");

    }
}
