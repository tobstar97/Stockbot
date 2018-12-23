package Datenbank;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Stellt die Datenbankverbindung her.
 * @author Lukas Meinzer, andygschaider
 * @since poc
 */
public class DBConnection {

    //vordefinierte Datenbank-Connection-einstellungen
    String url = "jdbc:mysql://localhost:3306/datenbank_stockbot?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    String username = "root";
    String password = "Luke";

    public Connection setupConnection() throws Exception {
        //wird das Programm auf dem Computer von @andygschaider ausgeführt?
        String hostadr = null;
        try {
            hostadr = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if(hostadr.contains("Andreas-PC")) {
            url = "jdbc:mysql://localhost:3306/stockbot?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            username = "root";
            password = "admin";
        }

        try {
            String driver = "com.mysql.cj.jdbc.Driver";

            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("-=- -=- -=- Database Connected -=- -=- -=-");

            return conn;

        }catch (Exception e){
            System.err.println(e);
        }

        System.out.println("-=- -=- Unable to connect to Database -=- -=-");
        return null;
    }

    public Connection setupConnection(String username, String password) throws Exception {

        try {
            String driver = "com.mysql.cj.jdbc.Driver";

            this.username = username;
            this.password = password;

            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("-=- -=- -=- Database Connected -=- -=- -=-");

            return conn;

        }catch (Exception e){
            System.err.println(e);
        }

        System.out.println("-=- -=- Unable to connect to Database -=- -=-");
        System.err.println("-=- -=- Database Connection Failed : DBConnection:setupConnection() -=- -=-");
        return null;
    }

    public Connection setupConnection(String url, String username, String password) throws Exception {

        try {
            String driver = "com.mysql.cj.jdbc.Driver";

            this.url = url;
            this.username = username;
            this.password = password;

            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("-=- -=- -=- Database Connected -=- -=- -=-");

            return conn;

        }catch (Exception e){
            System.err.println(e);
        }

        System.out.println("-=- -=- Unable to connect to Database -=- -=-");
        return null;
    }
}