package Ariva;
/**
 * @author Tobias Heiner
 * liefert die allgemeinnen Daten zu den Aktien auf ariva.de
 * Daten werden in die Tabelle aktie eingefügt
 */

import Datenbank.DBConnection;
import Datenbank.DBOperations;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Ariva_Allgemein {

    Document doc;

    DBConnection dbcon = new DBConnection();
    Connection conn;
    DBOperations dbop = new DBOperations();

    {
        try {
            conn = dbcon.setupConnection("root", "tobstar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getInfo(String url)throws IOException {

        doc = Jsoup.connect(url).get();
        String isin = get_ISIN();
        String wkn = get_WKN();
        String branche = get_branche();
        String aktienname = get_aktienname();


        //Insert in die Tabelle aktie der Datenbank
        try {
            dbop.aktien_insert(conn, isin, wkn, branche, aktienname, url);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public String get_ISIN(){
        Element e = doc.select("#pageSnapshotHeader > div.snapshotHeader.abstand > div.verlauf.snapshotInfo > div:nth-child(2)").first();
        String s = e.text();
        s = s.substring(6);

        return s;
    }

    public String get_WKN(){
        Element e = doc.select("#pageSnapshotHeader > div.snapshotHeader.abstand > div.verlauf.snapshotInfo > div:nth-child(1)").first();
        String s = e.text();
        s = s.substring(5);

        return s;
    }

    public String get_branche(){
        Element e = doc.select("#pageSnapshot > div.column.third.last > div.infoBox.abstand.new > table > tbody > tr:nth-child(1) > td:nth-child(2) > a").first();
        return (e.text());
    }

    public String get_aktienname(){
        Element e = doc.select("#pageSnapshotHeader > div.snapshotHeader.abstand > div.snapshotName > h1 > span").first();
        System.out.println(e.text());
        return (e.text());
    }




}
