import java.io.*;

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

        WebsiteTimestamps wsts = new WebsiteTimestamps();
        System.out.println(wsts.getLastModifiedDateFromWebpage("https://www.ariva.de/aktien/"));
        /*Onvista:Daimler      */ System.out.println(wsts.getLastModifiedDateFromWebpage("https://www.onvista.de/aktien/Daimler-Aktie-DE0007100000"));
        /*Morningstar:Schaltbau*/ System.out.println(wsts.getLastModifiedDateFromWebpage("http://tools.morningstar.de/de/stockreport/default.aspx?Site=de&id=0P00009QOV&LanguageId=de-DE&SecurityToken=0P00009QOV]3]0]E0WWE$$ALL"));

    }

}
