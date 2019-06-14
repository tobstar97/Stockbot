package Datenbank;


import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Stellt die Datenbankverbindung her
 * @author  Tobias Heiner
 */
public class DBConnection {

    //vordefinierte Datenbank-Connection-einstellungen
    String url = "jdbc:mysql://localhost:3306/Stock_Data?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    String username = "root";
    String password = "tobstar";            //Passwort Lukas: Luke



    public Connection setupConnection(String username, String password) throws Exception {

        try {
            //String driver = "com.mysql.jdbc.Driver";

            this.username = username;
            this.password = password;

            //Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("-=- -=- -=- Database Connected -=- -=- -=-");
            //System.out.println(conn.getSchema());

            return conn;

        }catch (Exception e){
            System.err.println(e);
        }

        System.out.println("-=- -=- Unable to connect to Database -=- -=-");
        return null;
    }


}
