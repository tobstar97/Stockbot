package Datenbank;

import java.sql.*;
import java.text.DateFormat;

/**
 * @author Tobias Heiner
 * Datenbankoperationen (sowohl insert als auch select)
 * Änderungen an Datenbank notwendig: ID überall raus, Bilanz: Jahr als int
 */
public class DBOperations {

    /**
     * Insert in die Aktientabelle
     * @param conn
     * @param ISIN
     * @param WKN
     * @param Branche
     * @param aktienname
     * @param aktienlink
     */
    public void aktien_insert(Connection conn, String ISIN, String WKN, String Branche, String aktienname, String aktienlink){
        String query = "INSERT INTO aktie("
                + "ISIN,"
                + "WKN,"
                + "Branche,"
                + "Aktienname,"
                + "Aktienlink) VALUES("
                + "?, ?, ?, ?, ?)";
        System.out.println(query);
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1,ISIN);
            pstmt.setString(2, WKN);
            pstmt.setString(3, Branche);
            pstmt.setString(4,aktienname);
            pstmt.setString(5, aktienlink);
            //pstmt.setString(6, index);

            // execute the Statement
            pstmt.executeUpdate();
            pstmt.close();

        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Insert in die Kurstabelle
     * @param conn
     * @param ISIN
     * @param datum
     * @param kurs
     * @param waehrung
     * @param letztesUpdate
     */
    public void kurs_insert(Connection conn, String ISIN, String datum, double kurs, String waehrung, String letztesUpdate){
        String query = "INSERT INTO kurs("
                + "ISIN,"
                + "Datum,"
                + "Kurs,"
                + "Waehrung,"
                + "letztesUpdate) VALUES("
                + "?, ?, ?, ?, ?)";
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, ISIN);
            pstmt.setDate(2, Date.valueOf(datum));
            pstmt.setDouble(3, kurs);
            pstmt.setString(4, waehrung);
            pstmt.setTimestamp(5, Timestamp.valueOf(letztesUpdate));

            pstmt.executeUpdate();
            pstmt.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * insert in die Bilanz Tabelle
     * @param conn
     * @param ISIN
     * @param jahr
     * @param umsatz
     * @param gewinn
     * @param ebit
     * @param eigenkapital
     * @param fremdkapital
     * @param waehrung
     * @param letztesUpdate
     */
    public void bilanz_insert(Connection conn, String ISIN, int jahr, double umsatz, double gewinn, double ebit, double eigenkapital, double fremdkapital, String waehrung, String letztesUpdate){
        String query = "INSERT INTO bilanz ("
                +"ISIN,"
                +"Jahr,"
                +"Umsatz,"
                +"Gewinn,"
                +"EBIT,"
                +"Eigenkapital,"
                +"Fremdkapital,"
                +"Waehrung,"
                +"letztesUpdate) VALUES("
                +"?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ptsmt = conn.prepareCall(query);
            ptsmt.setString(1, ISIN);
            ptsmt.setInt(2, jahr);
            ptsmt.setDouble(3, umsatz);
            ptsmt.setDouble(4, gewinn);
            ptsmt.setDouble(5, ebit);
            ptsmt.setDouble(6, eigenkapital);
            ptsmt.setDouble(7, fremdkapital);
            ptsmt.setString(8, waehrung);
            ptsmt.setString(9, letztesUpdate);

            ptsmt.executeUpdate();
            ptsmt.close();

        }catch (Exception e){
            System.out.println(e);
        }


    }

    /**
     * insert in die Index Tabelle
     * @param conn
     * @param isin
     * @param wkn
     * @param indexname
     * @param land
     */
    public void index_insert(Connection conn, String isin, int wkn, String indexname, String land){
        String query = "INSERT INTO index("
                + "ISIN,"
                + "WKN,"
                + "Indexname,"
                + " Land) VALUES("
                + "?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, isin);
            pstmt.setInt(2, wkn);
            pstmt.setString(3, indexname);
            pstmt.setString(4, land);
        }catch (Exception e){
            System.out.println(e);
        }


    }


    /**
     * Test-select, um Datenbankinhalt zu prüfen
     * @param conn
     * @param isin
     * @throws SQLException
     */
    public void aktie_select(Connection conn, String isin) throws SQLException{
        Statement stmt = conn.createStatement();

        String query = "SELECT * FROM Aktie WHERE ISIN = '" + isin + "'";
        ResultSet rs = stmt.executeQuery("SELECT * FROM Aktie WHERE ISIN =");

        while (rs.next()){
            System.out.println(rs.getNString("Aktienname"));
            System.out.println(rs.getNString("ISIN"));
        }

    }


    public void kurs_select(){

    }


    public void bilanz_select(){

    }
}
