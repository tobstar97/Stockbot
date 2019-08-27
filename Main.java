import Ariva.Ariva_Allgemein;
import Ariva.Ariva_Bilanz;
import Ariva.Ariva_Kurs_CSV;
import Morningstar.Morningstar_Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author Tobias Heiner
 * @description historischer Kurs von Aktien auf ariva.de; 2 Möglichkeiten: CSS-Selektoren oder CSV-Files (deutlich schneller)
 * @lukas programmiere deinen Parser am besten auch hier rein (aber natürlich in eine eigene Klasse usw.)
 * @ziel: wie beim letzten Treffen mit Herrn Lehner besprochen: Werte aller DAX_Konzerne und ein paar internationale Aktien von ariva holen und in eine Datenbank speichern
 * @fehlt_noch: wir müssen noh die Währung des Kurses einer AKtie bekommen (kein großer Aufwand @Lukas); dazu einfach eine Methode in die Ariva_kurs_CSV schreiben)
 * hab dir da schon mal eine kleine Anleitung reingeschrieben (ganz unten)
 * Parser für die CSV-Daten (@Lukas)
 */


/**     ++++        ++++        ++++        ++++        ++++        ++++        ++++        ++++        ++++        ++++
 *      +    ANLEITUNG:                                                                                                +
 *      +    um Lehners Aufgabe zu erfüllen:                                                                           +
 *      +    Man benötigt 3 Klassen: Ariva_Allgemein, Ariva_Bilanz, Ariva_Kurs_CSV                                     +
 *      +    Erzeuge jeweils ein Objekt dieser Klassen                                                                 +
 *      +    am einfachsten ist es, wenn man diese 3 Klassen in der Methode aktienListe() anlegt                       +
 *      +    dann die ganzen vorherigen Statements in der main auskommentieren                                         +
 *      +    Wenn man dann gefragt wird, welche Option man wählen will: nimm Option 3!!                                +
 *      +    Viel Spaß!!!                                                                                              +
 *      +    Bei Fehlern und Anmerkungen bitte mir (Tobias Heiner) Bescheid geben!                                     +
 *      +    Ach ja: alle Rechtschreibfehler sind so gewollt und gehören zum Gesamtkunstwerk                           +
 *      ++++        ++++        ++++        ++++        ++++        ++++        ++++        ++++        ++++        ++++
 * */
public class Main {

    public static void main(String[] args)throws IOException {

        Morningstar_Test morningstar_test = new Morningstar_Test();
        morningstar_test.steuern();
        System.exit(7);



        //Test auf Funktionalität von Ariva_Kurs_CSV
        //Ariva_Kurs_CSV acsv = new Ariva_Kurs_CSV();

        //Test auf Bilanz und Datenbankeintrag
        //Ariva_Bilanz ariva_bilanz = new Ariva_Bilanz();

        //Test der Ariva_Allgemein
        //Ariva_Allgemein ag = new Ariva_Allgemein();
/*
        try {

            ag.getInfo("https://www.ariva.de/spotify-aktie");
            ariva_bilanz.bilanz("https://www.ariva.de/spotify-aktie");
            ag.getInfo("https://www.ariva.de/allianz-aktie");
            ariva_bilanz.bilanz("https://www.ariva.de/allianz-aktie/bilanz-gu");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(100);*/
        
        
        //_______________nötig um hier operationen auf der DB durchführen zu können_____________

        DBConnection dbcon = new DBConnection();
        Connection conn;

        DBOperations dbop = new DBOperations();

        {
            try {
                conn = dbcon.setupConnection("root", "Luke");
            } catch (Exception e) {
                conn = null;
                e.printStackTrace();
            }
        }

//________________________________________________________________________________________
//___________________USER ABFRAGE ROHFASSUNG_______________________________________________


        /**
         *  Abfrage bzw. Prüfung, ob die entsprechenden Daten bereits gedownloaded worden sind:
         *  Aber Achtung: Das ist noch eine sehr schlechte Lösung. Wenn der User angibt dass er die Daten bereits gedownloaded hat, 
         *  er es aber nicht getan hat, werden Errors auftauchen. Hier muss ich noch eine bessere Lösung finden.
         */

        boolean datenvorhanden = false;

        String temporaer;
        System.out.println("Haben Sie die gewünschten Daten bereits runtergeladen?\nGeben sie entweder 'Ja' oder 'Nein' ein.");
        Scanner janein = new Scanner(System.in);
        temporaer = janein.next();
        switch(temporaer){
            case "Ja": datenvorhanden = true; break;
            case "ja": datenvorhanden = true; break;
            default: datenvorhanden = false; break;
        }



        while(datenvorhanden){
            
            File dir = new File("C:\\Users\\Luke the Duke\\Desktop\\Java\\Programme\\Studienprojekt_Ariva_neu\\CSV-Daten");
                    //Alle Dateien in dem genannten Pfad auflisten lassen
            // Achtung!! Hier muss natürlich jeder seinen eigenen Pfad hernehmen
                File[] files = dir.listFiles();
                //String den wir für die Abfrage getName brauchen:
                String temp;
                if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
                    for (int i = 0; i < files.length; i++) {
                        //Substring um ".csv" am Ende wegzuschneiden
                        temp = files[i].getName().substring(0,12);
                        //Printe die ISIN und den Namen
                        System.out.println(temp + "\t" + dbop.getName(conn,temp));
                    }
                }

                System.out.println("\n");
                String auswahl;
                System.out.println("Gib den Namen der Aktie ein, die du dir ansehen möchtest.");
                Scanner scan2 = new Scanner(System.in);
                auswahl = scan2.next();




                boolean auswahlverfuegbar = false;
                if(auswahl != null){
                    //Überprüfe ob auswahl überhaupt in der angezeigten Aktienliste vorkommt
                    String isinalsString;
                    String isinToName;

                    for (int i = 0; i < files.length; i++) {
                        //Substring um ".csv" am Ende wegzuschneiden
                        isinalsString = files[i].getName().substring(0,12);
                        isinToName = dbop.getName(conn,isinalsString);
                        if(auswahl.equals(isinToName)){
                               auswahlverfuegbar = true;
                        }
                    }
                    if(!auswahlverfuegbar){
                        System.out.println("Falsche Eingabe.");
                        System.exit(10);
                    }
                }
                else{
                    System.err.println("ungültige Eingabe.");
                }

                if(auswahlverfuegbar){
                    //der Nutzer hat eine gültige Auswahl getroffen
                    System.out.println("Was möchtest du dir anzeigen lassen?");
                    System.out.println("Kurs            -> 1");
                    System.out.println("Bilanz          -> 2");
                    System.out.println("Kurs & Bilanz   -> 3");

                    //___________Kurs funktioniert!______________

                    Scanner scan = new Scanner(System.in);
                    int j = scan.nextInt();

                    if(j==1){
                        try{
                            DBOperations.kurs_select(conn,auswahl);
                        } catch(SQLException e){
                            System.err.println(e);
                        }
                    }
                    if(j==2){
                        try{
                            DBOperations.bilanz_select(conn,auswahl);
                        } catch(SQLException e){
                            System.err.println(e);
                        }
                    }
                    if(j==3){
                        System.out.println("KURS:\n\n");
                        try{
                            DBOperations.kurs_select(conn,auswahl);
                        } catch(SQLException e){
                            System.err.println(e);
                        }
                        System.out.println("\n\n");
                        System.out.println("BILANZ:\n\n");
                        try{
                            DBOperations.bilanz_select(conn,auswahl);
                        } catch(SQLException e){
                            System.err.println(e);
                        }

                    }
                    if(j!=1 && j!=2 && j!=3){
                        System.err.println("ungültige Eingabe.");
                    }
                }

        System.exit(100);
//_________________________________________________________________________________________
//_________________________________________________________________________________________
        } //Hier ist die while-Schleife vorbei
        

        System.out.println("Download der CSV-Daten aller Aktien auf ariva.de?       -> 1");
        System.out.println("Download einer einzelnen CSV-Datei einer beliebigen Aktie?      ->2");
        System.out.println("Download einer Aktienliste (von Herr Lehner empfohlen)      ->3");
        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        if(i==1){
            System.out.println("Bist du dir sicher?!? (dauert eventuell etwas länger und hat einen großen Speicherbedarf)!!!");
            System.out.println("Abbruch? yes/no");
            String abbruch = scan.next();
            if(abbruch.contentEquals("yes")){
                System.exit(-1);
            }else{
                Steuerung st = new Steuerung();
                try {
                    System.out.println("\n\n\nAchtung! Das Herunterladen der Daten wird einige Zeit in Anspruch nehmen.\nSobald es fertg ist, starte das Programm erneut.\n\n\n");
                    st.feeder();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }else if(i == 2){
            System.out.println("Geben sie einen Link einer Aktie ein:\n");

            String aktienlink = scan.next();

            try {
                System.out.println("\n\n\nAchtung! Das Herunterladen der Daten wird einige Zeit in Anspruch nehmen.\nSobald es fertg ist, starte das Programm erneut.\n\n\n");
                //acsv.kurs_csv_link(aktienlink);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(i == 3){
            System.out.println("\n\n\nAchtung! Das Herunterladen der Daten wird einige Zeit in Anspruch nehmen.\nSobald es fertg ist, starte das Programm erneut.\nStellen Sie außerdem sicher, dass Sie während des gesamten Downloads eine stabile Internetverbindung haben.\n\n\n");
            aktienliste();
        }
        else {
            System.out.println("MACHE KEINE MINDERBEMITTELTEN EINGABEN");
        }





    }

    /**
     * wie beim letzen Treffen mit Herrn Lehner besprochen: eine AKtienliste mit allen DAX-Konzernen und einzelne internationale Aktien
     * @lukas wäre super, wenn die daraus gedownloadeten CSV-Files geparst und in der Datenbank gespeichert werden
     */
    public static void aktienliste(){

        ArrayList<String> liste = new ArrayList<>();
        liste.add("https://www.ariva.de/adidas-aktie");
        liste.add("https://www.ariva.de/allianz-aktie");
        liste.add("https://www.ariva.de/basf-aktie");
        liste.add("https://www.ariva.de/bayer-aktie");
        liste.add("https://www.ariva.de/beiersdorf-aktie");
        liste.add("https://www.ariva.de/bmw-aktie");
        liste.add("https://www.ariva.de/continental-aktie");
        liste.add("https://www.ariva.de/covestro-aktie");
        liste.add("https://www.ariva.de/daimler-aktie");
        liste.add("https://www.ariva.de/deutsche_bank-aktie");
        liste.add("https://www.ariva.de/deutsche_b%C3%B6rse-aktie");
        liste.add("https://www.ariva.de/deutsche_post-aktie");
        liste.add("https://www.ariva.de/deutsche_telekom-aktie");
        liste.add("https://www.ariva.de/e-on-aktie");
        liste.add("https://www.ariva.de/fresenius-aktie");
        liste.add("https://www.ariva.de/fresenius_medical_care-aktie");
        liste.add("https://www.ariva.de/heidelbergcement-aktie");
        liste.add("https://www.ariva.de/henkel_vz-aktie");
        liste.add("https://www.ariva.de/infineon-aktie");
        liste.add("https://www.ariva.de/linde_plc-aktie");
        liste.add("https://www.ariva.de/lufthansa-aktie");
        liste.add("https://www.ariva.de/merck_kgaa-aktie");
        liste.add("https://www.ariva.de/munich_re-aktie");
        liste.add("https://www.ariva.de/rwe-aktie");
        liste.add("https://www.ariva.de/sap-aktie");
        liste.add("https://www.ariva.de/siemens-aktie");
        liste.add("https://www.ariva.de/thyssenkrupp-aktie");
        liste.add("https://www.ariva.de/volkswagen_vz-aktie");
        liste.add("https://www.ariva.de/vonovia-aktie");
        liste.add("https://www.ariva.de/wirecard-aktie");
        liste.add("https://www.ariva.de/cancom-aktie");
        liste.add("https://www.ariva.de/innogy-aktie");
        liste.add("https://www.ariva.de/amazon-aktie");
        liste.add("https://www.ariva.de/coca-cola-aktie");
        liste.add("https://www.ariva.de/l%27or%C3%A9al-aktie");
        liste.add("https://www.ariva.de/easyjet_airline-aktie");
        liste.add("https://www.ariva.de/fuji_electric-aktie");
        liste.add("https://www.ariva.de/hang_seng_bank-aktie");
        liste.add("https://www.ariva.de/commonwealth_bank-aktie");
        liste.add("https://www.ariva.de/royal_dutch_shell_a-aktie");

        Iterator it = liste.iterator();
        while (it.hasNext()){
            Ariva_Kurs_CSV ariva_kurs_csv3 = new Ariva_Kurs_CSV();
            Ariva_Allgemein ariva_allgemein3 = new Ariva_Allgemein();
            Ariva_Bilanz ariva_bilanz3 = new Ariva_Bilanz();

            String s = (String) it.next();
            try {
                //acsv.kurs_csv_link(s);
                ariva_allgemein3.getInfo(s);
                ariva_kurs_csv3.csv_parser(s);
                System.out.println("=========================================");
                System.out.println("=========================================");
                System.out.println("=========================================");


                ariva_bilanz3.bilanz(s);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
