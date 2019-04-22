import Ariva.Ariva_Kurs_CSV;
import Ariva.Feeder_Ariva;


import java.util.HashSet;
import java.util.Iterator;

public class Steuerung{

    public void feeder() {

        Feeder_Ariva fa = new Feeder_Ariva();

        Ariva_Kurs_CSV acsv = new Ariva_Kurs_CSV();




        try{
            fa.indizes();
            fa.aktie();
            //fa.ausgabe();
            HashSet<String> x = new HashSet<>();
            x = fa.get_aktienliste();
            System.out.println("Test\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("================");
            System.out.println("================");
            System.out.println("================");
            System.out.println("================");
            System.out.println("================");
            System.out.println("================");




            Iterator it = x.iterator();
            while (it.hasNext()){
                String setText = (String) it.next();
                acsv.kurs_csv_link(setText);

            }

        }catch (Exception e){
            System.err.println(e);
        }


    }
}
