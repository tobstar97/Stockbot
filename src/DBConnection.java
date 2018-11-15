import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Scanner;
import  java.sql.*;



public class DBConnection {

    public static Connection getConnection() throws Exception{

        try{
            String driver = "com.mysql.cj.jdbc.Driver";

            String url = "jdbc:mysql://localhost:3306/datenbank_stockbot?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

            String username = "root";
            String password = "Luke";

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
}
