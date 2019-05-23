package Datenbank;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
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
     * @param index
     */
    public void aktien_insert(Connection conn, String ISIN, int WKN, String Branche, String aktienname, String aktienlink, String index){
        String query = "INSERT INTO Aktie("
                + "ISIN,"
                + "WKN,"
                + "Branche,"
                + "Aktienname,"
                + "Aktienlink,"
                + "Index) VALUES("
                + "?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1,ISIN);
            pstmt.setInt(2, WKN);
            pstmt.setString(3, Branche);
            pstmt.setString(4,aktienname);
            pstmt.setString(5, aktienlink);
            pstmt.setString(6, index);

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
        String query = "INSERT INTO Kurs("
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

    public void bilanz_insert(Connection conn, String ISIN, int jahr, double umsatz, String analystenmeinung, double gewinn, double ebit, double eigenkapital, double fremdkapital, String waehrung, String letztesUpdate){
        String query = "INSERT INTO Bilanz ("
                +"ISIN,"
                +"Jahr,"
                +"Umsatz,"
                +"Analystenmeinung,"
                +"Gewinn,"
                +"EBIT,"
                +"Eihgenkapital,"
                +"Fremdkapital,"
                +"Waehrung,"
                +"letztesUpdate) VALUES("
                +"?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ptsmt = conn.prepareCall(query);
            ptsmt.setString(1, ISIN);
            ptsmt.setInt(2, jahr);
            ptsmt.setDouble(3, umsatz);
            ptsmt.setString(4, analystenmeinung);
            ptsmt.setDouble(5, gewinn);
            ptsmt.setDouble(6, ebit);
            ptsmt.setDouble(7, eigenkapital);
            ptsmt.setDouble(8, fremdkapital);
            ptsmt.setString(9, waehrung);
            ptsmt.setTimestamp(10, Timestamp.valueOf(letztesUpdate));

            ptsmt.executeUpdate();
            ptsmt.close();

        }catch (Exception e){
            System.out.println(e);
        }
    }

}
