package Feeder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FeederMorningstar {

    private boolean debug = true;
    private String url = "http://tools.morningstar.de/de/stockquickrank/default.aspx?Site=de&LanguageId=de-DE";
    private ArrayList<String> arrAktienlinks = new ArrayList<>();
    private ArrayList<String> arrIsin = new ArrayList<>();

    public FeederMorningstar() {
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        print();
    }

    private void connect() throws IOException {
        //FileInputStream in = new FileInputStream("E:\\Program Files (x86)\\GoogleDrive\\WIF3\\Studienprojekt StockBot Lehner\\@andygschaider\\index.html");
        File file = new File("E:\\Program Files (x86)\\GoogleDrive\\WIF3\\Studienprojekt StockBot Lehner\\@andygschaider\\index.html");
        Document doc = Jsoup.parse(file,"UTF-8","");

        if(debug) System.out.println("Anzahl Aktien: " + doc.select("a[href*=\"../../\"]").size());
        for(int i=0; i<doc.select("a[href*=\"../../\"]").size(); i++) {
            //Element e = doc.select("a[href*=\"/stockreport/default.aspx?id=\"").get(i);
            Element e = doc.select("a[href*=\"../../\"]").get(i);
            arrAktienlinks.add("http://tools.morningstar.de/" + e.attr("href").substring(6));
            if(debug) System.out.println(i + ": Link: " + "tools.morningstar.de/" + e.attr("href").substring(6));

            //Isin ziehen
            System.out.println(arrAktienlinks.get(i));
            Document docdeep = Jsoup.connect(arrAktienlinks.get(i)).get();
            arrIsin.add(docdeep.select("td[id=\"Col0Isin\"]").text());
            if(debug) System.out.println(i + ": ISIN: " + docdeep.select("td[id=\"Col0Isin\"]").text());
        }
    }

    private void print() {
        System.out.println("Aktienlinks: " + "\n" + arrAktienlinks.size() + "\n" + arrAktienlinks);
        System.out.println("ISINs: " + "\n" + arrIsin.size() + "\n" + arrIsin);
    }
}
