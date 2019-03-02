import Ariva.Ariva_Kurs;
import Ariva.Feeder_Ariva;


import java.util.HashSet;
import java.util.Iterator;

public class Steuerung{

    public void feeder() {

        Feeder_Ariva fa = new Feeder_Ariva();
        Ariva_Kurs a = new Ariva_Kurs();




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
                a.kursLink(a.ablauf(setText));
                a.getKursGesamt();
                a.aus();
            }

        }catch (Exception e){
            System.err.println(e);
        }


    }
}
