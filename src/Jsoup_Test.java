import Datenbank.DBConnection;
import Datenbank.DBOperations;
import Feeder.FeederMorningstar;
import Feeder.FeederOnvista;
import Feeder.FeederOnvistaLowMem;
import Hilfsklassen.WebsiteTimestamps;
import Feeder.FeederAriva;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tobias Heiner, andygschaider
 */
public class Jsoup_Test {

    private static Connection conn;

    public static void main (String [] args) throws Exception {

//  --- --- -=- -=- -== -== === === Website Timestamps === === ==- ==- -=- -=- --- ---

        //Timestamps
        //System.out.println("-=- -=- Timestamps -=- -=-");
        //WebsiteTimestamps wsts = new WebsiteTimestamps();
        /*Ariva:Lufthansa      */ //System.out.println(wsts.getLastModifiedDateFromWebpage("https://www.ariva.de/lufthansa-aktie"));
        /*Ariva:Lufthansa      */ //System.out.println(wsts.getLastModifiedDateFromWebpage("https://www.ariva.de/"));
        /*Ariva:Lufthansa      */ //System.out.println(wsts.getLastModifiedDateFromWebpage("https://www.ariva.de/tecdax"));
        /*Ariva:Lufthansa      */ //System.out.println(wsts.getLastModifiedDateFromWebpage("https://www.ariva.de/isra_vision-aktie"));
        /*Onvista:Daimler      */ //System.out.println(wsts.getLastModifiedDateFromWebpage("https://www.onvista.de/aktien/Daimler-Aktie-DE0007100000"));
        /*Morningstar:Schaltbau*/ //System.out.println(wsts.getLastModifiedDateFromWebpage("http://tools.morningstar.de/de/stockreport/default.aspx?Site=de&id=0P00009QOV&LanguageId=de-DE&SecurityToken=0P00009QOV]3]0]E0WWE$$ALL"));

//  --- --- -=- -=- -== -== === === Aktienlinks sammeln === === ==- ==- -=- -=- --- ---
/*
        //Feeder
        System.out.println("----------------------");
        System.out.println("-=- -=- Feeder -=- -=-");
        FeederAriva a = new FeederAriva();
*/
        //FeederOnvista fo = new FeederOnvista();
        //FeederAriva fa = new FeederAriva();
        //System.out.println(fa.sendSetToAriva());
        //FeederMorningstar fm = new FeederMorningstar();
        conn = new DBConnection().setupConnection();
        //ARIVA
        /*
        FeederAriva fa = new FeederAriva();
        Set<String> set = fa.sendSetToAriva();
        ArrayList<String> arrAriva = new ArrayList<>();
        arrAriva.addAll(set);
        System.out.println("Jsoup_Test: " + arrAriva);
        for(int i=0; i<arrAriva.size(); i++) {
            DBOperations.dbInsert(conn, String.valueOf(i), arrAriva.get(i), null, "Ariva");
        }
        */
        //ONVISTA
        FeederOnvistaLowMem fo = new FeederOnvistaLowMem(conn);
        /*
        ArrayList<String> arrOnvistaAktiennamen = fo.sendAktiennamen();
        ArrayList<String> arrOnvistaISINs = fo.sendISINs();
        System.out.println("Jsoup_Test: " + arrOnvistaAktiennamen + "\n" + arrOnvistaISINs);
        for(int i=0; i<arrOnvistaAktiennamen.size() && i<arrOnvistaISINs.size(); i++) {
            DBOperations.dbInsert(conn,arrOnvistaISINs.get(i),arrOnvistaAktiennamen.get(i),null,"Onvista");
        }
        */

//  --- --- -=- -=- -== -== === === Hauptklassen === === ==- ==- -=- -=- --- ---
/*
        //Ariva
        System.out.println("---------------------");
        System.out.println("-=- -=- Ariva -=- -=-");
        Ariva ariva = new Ariva();
        ariva.connect("DE0006047004","HeidelbergCement");
        System.out.println("main: Gewinn HeidelbergCement: " + ariva.getGewinn());
        System.out.println("main:Ariva: Kurs HeidelbergCement: " + ariva.getKurs());
        ariva.disconnect();
        ariva.connect("munich_re-aktie");
        System.out.println("main:Ariva: Kurs Munich RE: " + ariva.getKurs());
        System.out.println("main: Gewinn Munich RE: " + ariva.getGewinn());
*//*
        //Onvista
        System.out.println("-----------------------");
        System.out.println("-=- -=- Onvista -=- -=-");
        Onvista onvista = new Onvista();    //Wirecard-Aktie-DE0007472060
        System.out.println("main:Onvista: Gewinn Wirecard: " + onvista.getGewinn());            //TODO NULLPOINTER
        System.out.println("main:Onvista: Kurs Wirecard: " + onvista.getKurs());                //TODO NULLPOINTER
*//*
        //Morningstar
        System.out.println("---------------------------");
        System.out.println("-=- -=- Morningstar -=- -=-");
        Morningstar morning = new Morningstar();
        morning.connect();      //Alphabet Inc
        System.out.println("main:Morningstar: Gewinn Alphabet Inc: " + morning.getGewinn());
        System.out.println("main:Morningstar: Kurs Alphabet Inc: " + morning.getKurs());
*/
    }

}