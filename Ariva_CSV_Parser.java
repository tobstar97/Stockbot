package Ariva;

import Datenbank.DBConnection;
import Datenbank.DBOperations;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Tobias Heiner
 * Liest den Kurs und das Datum eines CSV-Files aus
 */
public class Ariva_CSV_Parser {

    DBConnection dbcon = new DBConnection();
    Connection conn;
    String filename;
    Double kurs_s;
    String datum_s;

    HashMap<String, Double> hm = new HashMap<>();

    DBOperations dbop = new DBOperations();

    {
        try {
            conn = dbcon.setupConnection("root", "tobstar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ablauf(){
        ArrayList<String> liste = new ArrayList<>();
        for(String e : liste){
            filename = "CSV-Daten/" + e;
            try {
                test(filename);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }



    }



    public void test(String filename)throws Exception{
        filename = "CSV-Daten/" + filename;


        {
            try {

                CSVReader reader = new CSVReader(new FileReader(filename), ';', '"', 1);
                String [] nextline;
                while ((nextline = reader.readNext()) != null) {
                    if (nextline.length>=5) {
                        //Verifying the read data here
                        String datum = nextline[0];
                        String kurs = nextline[4];
                        kurs = kurs.trim();
                        kurs_s = kurs_format(kurs);
                        datum_s = datum_format(datum);
                        hm.put(datum_s, kurs_s);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public String datum_format(String s){
        int end = s.length();
        if(end>=10){
            String d = s.substring(8, 10);
            String j = s.substring(0, 4);
            String m = s.substring(5, 7);
            s = d + "/" + m + "/" + j;
        }
        return s;
    }


    public double kurs_format(String s){

        if(s.contains(" ")){
            return 0.0;
        }
        if(s.contains(",")){
            s = s.replace(",", ".");
        }
        double d = Double.parseDouble(s);



        return d;
    }



}
