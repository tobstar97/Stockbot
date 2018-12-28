import Datenbank.DBConnection;
import Feeder.FeederArivaFX;
import Feeder.FeederOnvistaLowMem;
import Hilfsklassen.Hilfsmethoden;
import javafx.stage.Stage;

import java.sql.Connection;

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
        //conn = new DBConnection().setupConnection();
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
        //FeederOnvistaLowMem fo = new FeederOnvistaLowMem(conn);
        /*
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(FeederArivaFX.class);
            }
        }.start();
        FeederArivaFX startUpTest = FeederArivaFX.waitForFeederArivaFX();
        startUpTest.printSomething();
        */
        //Hilfsmethoden.broodForce("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_","",8);
        //Hilfsmethoden.broodForce("abcdefghijklmnopqrstuvwxyz-_","",8);
        Hilfsmethoden.broodForce("0123456789","",8);
        //Onvista.catchBilanzData("https://www.onvista.de/aktien/SCHALTBAU-HOLDING-AG-Aktie-DE000A2NBTL2");
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
        System.out.println("main:Onvista: Gewinn Wirecard: " + onvista.getGewinn());
        System.out.println("main:Onvista: Kurs Wirecard: " + onvista.getKurs());
*//*
        //Morningstar
        System.out.println("---------------------------");
        System.out.println("-=- -=- Morningstar -=- -=-");
        Morningstar morning = new Morningstar();
        morning.connect();      //Alphabet Inc
        System.out.println("main:Morningstar: Gewinn Alphabet Inc: " + morning.getGewinn());
        System.out.println("main:Morningstar: Kurs Alphabet Inc: " + morning.getKurs());
*/
/*      HOTFIXING der Insert-Kommando-Parameter
        String umsatzDaten = "-1 -2 -3 -4 -5";
        for(int i=0;i<5;i++) {
            String umsatz = "";
            for (int j = 0; j < umsatzDaten.length(); j++) {
                if (' ' == (umsatzDaten.charAt(j))) {
                    umsatz = umsatzDaten.substring(0, j);
                    umsatzDaten = umsatzDaten.substring(j + 1);
                    System.out.println("umsatzDaten(" + umsatzDaten + "),umsatz(" + umsatz + ")");
                    break;
                }
            }
        }
        System.out.println("=======================");
        umsatzDaten = "-1 -2 -3 -4 -5";
        int cnt = 0;
        for(int i=0;i<5;i++) {
            String umsatz = "";
            cnt++;
            for (int j = 0; j < umsatzDaten.length(); j++) {
                if (' ' == (umsatzDaten.charAt(j))) {
                    umsatz = umsatzDaten.substring(0, j);
                    umsatzDaten = umsatzDaten.substring(j + 1);
                    System.out.println("umsatzDaten(" + umsatzDaten + "),umsatz(" + umsatz + "),cnt("+cnt+")");
                    break;
                }
            }
            if(cnt==4) {
                umsatz = umsatzDaten;
                System.out.println("umsatzDaten(" + umsatzDaten + "),umsatz(" + umsatz + "),cnt("+cnt+")");
            }
        }
*/
    }

}