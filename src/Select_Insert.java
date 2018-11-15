import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SELECT_INSERT {


    //Select
    public static void test_select(Connection conn)throws Exception{
        Statement stmt = conn.createStatement() ;
        String query = "select Kurs from aktie ;" ;
        ResultSet rs = stmt.executeQuery(query) ;
        while(rs.next()){
            double k = rs.getDouble("Kurs");
            System.out.println(k);
        }
    }

    //insert
    public static  void post(double x, String s, String i, int w, double u, double g) throws Exception{
        //x = Kurs  s = Name    i = ISIN    w = WKN     u = Umsatz      g = Gewinn

        try{

            //weiß nicht ob der Part so stimmt..
            Connection conn;
            DBConnection db = new DBConnection();
            conn = db.getConnection();
            PreparedStatement posted = conn.prepareStatement("INSERT INTO aktie(Aktienname, ISIN, WKN, Kurs, Umsatz, Gewinn)VALUES ('"+s+"', '"+i+"',"+w+","+x+","+u+","+g+")" );

            posted.executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println("INSERT completed");
        }
    }
}
