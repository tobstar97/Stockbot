package Datenbank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Alle noetigen Operationen auf der Datenbank.
 * @author Lukas Meinzer, andygschaider
 * @since poc
 */
public class DBOperations {

    /**
     * Select-Statement.
     * @param conn Verbindung zur Datenbank.
     * @throws Exception
     */
    public void dbselect(Connection conn) throws Exception {
        Statement stmt = conn.createStatement() ;
        String query = "select Kurs from aktie ;" ;
        ResultSet rs = stmt.executeQuery(query) ;
        while(rs.next()){
            double k = rs.getDouble("Kurs");
            System.out.println(k);
        }
    }

    /**
     * Insert-Statement.
     * @param conn Verbindung zur Datenbank.
     * @param kurs
     * @param name
     * @param isin
     * @param wkn
     * @param umsatz
     * @param gewinn
     * @throws Exception
     */
    public void dbinsert(Connection conn, double kurs, String name, String isin, int wkn, double umsatz, double gewinn) throws Exception {
        try{
            //weiß nicht ob der Part so stimmt..            ..stimmte nicht, weiß aber nicht ob dashier jetzt funktioniert #andy
            PreparedStatement posted = conn.prepareStatement("INSERT INTO aktie(Aktienname, ISIN, WKN, Kurs, Umsatz, Gewinn)VALUES ('"+name+"', '"+isin+"',"+wkn+","+kurs+","+umsatz+","+gewinn+")" );
            posted.executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println("INSERT completed");
        }
    }
}