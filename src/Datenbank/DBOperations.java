package Datenbank;

import java.sql.*;

/**
 * Alle noetigen Operationen auf der Datenbank.
 * @author Lukas Meinzer, andygschaider
 * @since poc
 */
public class DBOperations {

    private static boolean debug = true;

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
            PreparedStatement posted = conn.prepareStatement("INSERT INTO aktie(Aktienname, ISIN, WKN, Kurs, Umsatz, Gewinn)VALUES ('"+name+"', '"+isin+"',"+wkn+","+kurs+","+umsatz+","+gewinn+")" );
            posted.executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println("INSERT completed");
        }
    }

    public static void dbInsertFeeder(Connection conn, String isin, String wkn, String aktienname, String link, String quelle) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeQuery(
            "REPLACE INTO feeder (isin, wkn, aktienname, link, quelle)" + "\n" +
                "VALUES ('"+ isin +"', '"+ wkn +"', '"+ aktienname +"', '"+ link +"', '"+ quelle +"')"
        );
        if (debug) System.out.println("INSERTED into Feeder: ISIN(" + isin + "), WKN(" + wkn + "), Aktienname(" + aktienname + "), Link(" + link + "), Quelle(" + quelle + ")");
    }

    public static void dbInsertOnvistaBilanzData(Connection conn, String isin, String wkn, String aktienname, String jahr, String waehrung, String umsatz, String ekap, String gkap, String ebit, String jue) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeQuery(
            "REPLACE INTO bilanzOnvista (isin, wkn, aktienname, jahr, waehrung, umsatz, eigenkapital, gesamtkapital, ebit, gewinn)" + "\n" +
                "VALUES ('"+ isin +"', '"+ wkn +"', '"+ aktienname +"', '"+ jahr +"', '"+ waehrung +"', '"+ umsatz +"', '"+ ekap +"', '"+ gkap +"', '"+ ebit +"', '"+ jue +"')"
        );
        if (debug) System.out.println("INSERTED into Feeder: ISIN(" + isin + "), WKN(" + wkn + "), Aktienname(" + aktienname + "), Jahr(" + jahr + "), Währung(" + waehrung + "), Umsatz(" + umsatz + "), Ekap(" + ekap + "), Gkap(" + gkap + "), EBIT(" + ebit + "), Gewinn(" + jue + ")");
    }

}