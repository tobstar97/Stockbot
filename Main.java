import Ariva.Ariva_Kurs_CSV;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        //Test auf Funktionalität von Ariva_Kurs_CSV
        Ariva_Kurs_CSV acsv = new Ariva_Kurs_CSV();
        int i = 0;
        System.out.println("Download der CSV-Daten aller Aktien auf ariva.de?       -> 1");
        System.out.println("Download einer einzelnen CSV-Datei einer beliebigen Aktie?      ->2");
        Scanner scan = new Scanner(System.in);
        i = scan.nextInt();
        if(i==1){
            System.out.println("Bist du dir sicher?!? (dauert eventuell etwas länger und hat einen großen Speicherbedarf)!!!");
            System.out.println("Abbruch? yes/no");
            String abbruch = scan.next();
            if(abbruch.contentEquals("yes")){
                System.exit(-1);
            }else{
                Steuerung st = new Steuerung();
                try {
                    st.feeder();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }else if(i == 2){
            System.out.println("Geben sie einen Link einer Aktie ein:\n");

            String aktienlink = scan.next();

            try {
                acsv.kurs_csv_link(aktienlink);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("MACHE KEINE MINDERBEMITTELTEN EINGABEN");
        }





    }


}
