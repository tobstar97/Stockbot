import Ariva.Ariva_Allgemein;
import Ariva.Ariva_Bilanz;
import Ariva.Ariva_Kurs_CSV;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;

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
        /*
        Ariva_Bilanz ab = new Ariva_Bilanz();
        try {
            ab.getdata2("https://www.ariva.de/spotify-aktie/bilanz-guv");
        }catch (Exception e){

        }
        System.exit(123);*/


        /*
        Document doc = Jsoup.connect("https://www.ariva.de/spotify-aktie/bilanz-guv").get();
        Element e = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(22) > td:nth-child(1)").last();
        System.out.println(e.text());
        if(e.text().contains("Summe kurzfristiges Fremdkapital")){
            Element f = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(24) > td:nth-child(2)").first();
            System.out.println(f.text());
        }
        else {
            Element f = doc.select("div.tabelleUndDiagramm:nth-child(4) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(17) > td:nth-child(2)").first();
        }*/
        //System.exit(-1);



        //Test auf Funktionalität von Ariva_Kurs_CSV
        Ariva_Kurs_CSV acsv = new Ariva_Kurs_CSV();

        //Test auf Bilanz und Datenbankeintrag
        Ariva_Bilanz ariva_bilanz = new Ariva_Bilanz();

        //Test der Ariva_Allgemein
        Ariva_Allgemein ag = new Ariva_Allgemein();

        try {

            ag.getInfo("https://www.ariva.de/spotify-aktie");
            ariva_bilanz.bilanz("https://www.ariva.de/spotify-aktie");
            ag.getInfo("https://www.ariva.de/allianz-aktie");
            ariva_bilanz.bilanz("https://www.ariva.de/allianz-aktie/bilanz-gu");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(100);

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
        }else if(i == 3){
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

        Ariva_Kurs_CSV ariva_kurs_csv = new Ariva_Kurs_CSV();
        Ariva_Allgemein ariva_allgemein = new Ariva_Allgemein();
        Ariva_Bilanz ariva_bilanz = new Ariva_Bilanz();


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
            String s = (String) it.next();
            try {
                //acsv.kurs_csv_link(s);
                ariva_allgemein.getInfo(s);
                ariva_bilanz.bilanz(s);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}

