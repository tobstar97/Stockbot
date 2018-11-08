/**
 * @author Tobias Heiner
 */
//Arbeitsanweisungen
//@luketheduke mach mal bitte eigene Klassen für die DatenbankConnection und Daten auslesen ( also zb Klassenname: Database und Auslesen) 
//@andi probier du mal aus wie du des mit den timestamps machen willst
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Scanner;
import  java.sql.*;

//dies ist ein test von andy

//der bullshit geht nicht wie ich will #andy

public class Jsoup_Test {

    public static void main (String [] args) throws Exception{
        Connection c;

        c = getConnection();
        double x;
        Onvista o = new Onvista();

        o.connect();
        o.getGewinn();

        /*x = Daten_auslesen();
        post(x,"Wirecard","DE0007472060", 747206, 50000.5,200.2);
        test_select(c);*/

    }


    public static  void post(double x, String s, String i, int w, double u, double g) throws Exception{
        //x = Kurs  s = Name    i = ISIN    w = WKN     u = Umsatz      g = Gewinn



        try{
            Connection conn = getConnection();
            PreparedStatement posted = conn.prepareStatement("INSERT INTO aktie(Aktienname, ISIN, WKN, Kurs, Umsatz, Gewinn)VALUES ('"+s+"', '"+i+"',"+w+","+x+","+u+","+g+")" );

            posted.executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println("INSERT completed");
        }
    }


    public static Connection getConnection() throws Exception{

        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            System.out.println("TEST1");
            String url = "jdbc:mysql://localhost:3306/Datenbank_Stockbot?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            System.out.println("TEST2");
            String username = "root";
            String password = "tobstar";
            System.out.println("Test3");
            Class.forName(driver);

            Connection conn;
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");


            return conn;
            
            
        }catch (Exception e){
            System.out.println(e);
        }

        return null;
    }

    public static double Daten_auslesen() throws IOException{
        //bisher nur für gewinn
        
        
        String aktienname;
        String ISIN;
        String url = "FEHLER!!!"; ;
        int pro;
        
        Scanner scan = new Scanner(System.in);


        System.out.println("Geben Sie den Aktiennamen an");
        aktienname = scan.next();

        //Sollte später automatisch mit hilfe einer Map funktionieren
        System.out.println("Geben Sie die ISIN an");
        ISIN = scan.next();

        //Portal waehlen
        System.out.println("Waehlen sie das Portal:  1 onvista      2 ariva");

        pro = scan.nextInt();

        if(pro == 1){
            url = "http://www.onvista.de/aktien/";
        }else if (pro == 2){
            //funktioniert noch nicht vollstaendig, da die html struktur anders ist als bei onvista.de
            url = "http://www.ariva.de/";
        }
        /*www.ariva.de/ funktioniert genau so wie onvista.de
        einfach nach dem / die ISIN Nummer eingeben
         */

        //Suche wirecard Aktie auf onvista.de
        //ISIN-Nummer: wird hinten in der url durchnummeriert (DE...)


        url = url + ISIN;

        //Jsoup aufruf
        Document doc = Jsoup.connect(url).get();

        //meta property tag auswaehlen, in dem der Aktienkurs steht
        Element meta = doc.select("meta[property=schema:price]").first();

        //meta attribut auslesen
        String content = meta.attr("content");

        //inhalt ausgeben
        //System.out.println(content);
        double r = Double.parseDouble(content);
        return r;
    }

    public static void test_select(Connection conn)throws Exception{
        Statement stmt = conn.createStatement() ;
        String query = "select Kurs from aktie ;" ;
        ResultSet rs = stmt.executeQuery(query) ;
        while(rs.next()){
            double k = rs.getDouble("Kurs");
            System.out.println(k);
        }
    }



}
