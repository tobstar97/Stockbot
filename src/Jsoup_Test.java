import java.io.*;

import Feeder.FeederAriva;
import Hilfsklassen.Hilfsmethoden;
import Hilfsklassen.WebsiteTimestamps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Scanner;
import java.sql.*;

/**
 * @author Tobias Heiner, andygschaider
 */
public class Jsoup_Test {

    public static void main (String [] args) throws Exception {

//  --- --- -=- -=- -== -== === === Website Timestamps === === ==- ==- -=- -=- --- ---

        //Timestamps
        System.out.println("-=- -=- Timestamps -=- -=-");
        WebsiteTimestamps wsts = new WebsiteTimestamps();
        /*Ariva:Lufthansa      */ System.out.println(wsts.getLastModifiedDateFromWebpage("https://www.ariva.de/lufthansa-aktie"));
        /*Onvista:Daimler      */ System.out.println(wsts.getLastModifiedDateFromWebpage("https://www.onvista.de/aktien/Daimler-Aktie-DE0007100000"));
        /*Morningstar:Schaltbau*/ System.out.println(wsts.getLastModifiedDateFromWebpage("http://tools.morningstar.de/de/stockreport/default.aspx?Site=de&id=0P00009QOV&LanguageId=de-DE&SecurityToken=0P00009QOV]3]0]E0WWE$$ALL"));

//  --- --- -=- -=- -== -== === === Aktienlinks sammeln === === ==- ==- -=- -=- --- ---

        //Feeder
        System.out.println("-=- -=- Feeder -=- -=-");
        FeederAriva a = new FeederAriva();

//  --- --- -=- -=- -== -== === === Hauptklassen === === ==- ==- -=- -=- --- ---

        //Ariva
        System.out.println("-=- -=- Ariva -=- -=-");
        Ariva ariva = new Ariva();
        ariva.connect("DE0006047004","HeidelbergCement");
        //System.out.println("main: Gewinn HeidelbergCement: " + ariva.getGewinn());    //TODO
        System.out.println("main:Ariva: Kurs HeidelbergCement: " + ariva.getKurs());
        ariva.disconnect();
        ariva.connect("munich_re-aktie");
        System.out.println("main:Ariva: Kurs Munich RE: " + ariva.getKurs());
        //System.out.println("main: Gewinn Munich RE: " + ariva.getGewinn());           //TODO

        //Onvista
/*      System.out.println("-=- -=- Onvista -=- -=-");
        Onvista onvista = new Onvista();    //Wirecard-Aktie-DE0007472060
        System.out.println("main:Onvista: Gewinn Wirecard: " + onvista.getGewinn());            //TODO NULLPOINTER
        System.out.println("main:Onvista: Kurs Wirecard: " + onvista.getKurs());                //TODO NULLPOINTER
*/
        //Morningstar
        System.out.println("-=- -=- Morningstar -=- -=-");
        Morningstar morning = new Morningstar();
        morning.connect();      //Alphabet Inc
        System.out.println("main:Morningstar: Gewinn Alphabet Inc: " + morning.getGewinn());      //TODO NULLPOINTER
        System.out.println("main:Morningstar: Kurs Alphabet Inc: " + morning.getKurs());          //TODO NULLPOINTER

    }

}
