/**
 * @author Tobias Heiner
 * @description Feeder für onvista; Ziel: Weitergabe der ISIN der gefundenen Aktien auf onvista an ariva, da man dort die historischen Kurse viel besser auslesen kann
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OnvistaTest {

    String url = "https://www.onvista.de/aktien/aktien-laender/";
    Document doc;
    ArrayList<String>arlaender = new ArrayList<>();
    ArrayList<String>araktien = new ArrayList<>();
    ArrayList<String>arISIN = new ArrayList<>();
    int anzahl;
    String cut;


    /**
     * Connection zur onvista-Seite
     * @author Tobias Heiner
     * @throws IOException
     */
    public void connect()throws IOException{
        doc = Jsoup.connect(url).get();
    }

    /**
     * liest alle Länderlinks aus und speichert sie in der ArrayList arlaender
     * @author Tobias Heiner
     * @throws IOException
     */
    public void laender()throws IOException{
        Element e = doc.getElementsByClass("land-list").first();
        anzahl = e.childNodeSize()/2;
        int i = 0;
        while (i<anzahl){
            Element table = doc.getElementsByClass("land-list-item").get(i);
            Element meta = table.child(0);
            String result = meta.attr("abs:href");
            arlaender.add(result);
            i++;

        }



    }

    /**
     * liest die Aktienlinks aus und läuft durch alle Seiten eines Länderlinks
     * AKtienlinkks werden in der ArrayList araktien gespeichert
     * @author Tobias Heiner
     * @throws IOException
     */
    public void aktienderlaender()throws IOException{
        //System.out.println(arlaender.get(8));
        int siz = arlaender.size();
        int j = 0;
        while(j<siz){
            doc = Jsoup.connect(arlaender.get(j)).get();

            Element e = doc.getElementsByClass("LETZTER").first();

            //wenn es kein LETZTE Seite Element gibt
            if (e == null){     //e == null auch wenn es mehrere seiten gibt -> müssen noch alle seiten durchlaufen werden
                int f = doc.select("div.BLAETTER_NAVI:nth-child(1) > span:nth-child(1)").size();
                //System.out.println(doc.select("div.BLAETTER_NAVI:nth-child(1) > span:nth-child(1)").text());


                cut = arlaender.get(j);
                //System.out.println(cut);
                //String temp = e.child(0).attr("abs:href");
                //temp = temp.replace(cut, "");

                f = f+1;

                int o = 0;
                while(o<=f){
                    o++;
                    cut = cut+o;
                    o--;

                    aktienDurchlaufen();

                    o++;
                    cut = cut.replace(String.valueOf(o), "");
                }



            }else{      //wenn es ein LETZTE Seite Element gibt
                //sucht sich die letzte Seite, speichert den Wert und läuft dann durch die Seiten durch
                cut = arlaender.get(j);
                //System.out.println(cut);
                String temp = e.child(0).attr("abs:href");
                temp = temp.replace(cut, "");
                int z = Integer.parseInt(temp);

                int i = 1;

                while(i<=z){
                    cut = cut+i;
                    //System.out.println(cut);
                    //hier wird dann die seite nach aktien durchlaufen

                    aktienDurchlaufen();



                    cut = cut.replace(String.valueOf(i), "");

                    i++;

                }
            }
            j++;
        }



    }

    /**
     * liest die ISIN eines Aktienlinks aus und speichert diese in arISIN
     * es werden einfach nur die 12 letzen Stellen eines Aktienlinks abgeschnitten und gespeichert
     * @author Tobias Heiner
     * @throws IOException
     */
    public void getISIN()throws IOException{
        int j = araktien.size();
        int i = 0;
        while(i<j){     //try catch benötigt, da seite eventuell nicht mehr verfügbar -- erledigt 
            try{
                String temp = araktien.get(i);
                temp = temp.substring((temp.length()-12));
                arISIN.add(temp);
            }catch (Exception ex){

            }finally {
                i++;

            }


        }
    }

    /**
     * dient zur Visualisierung der Ergebnisse
     * @author Tobias Heiner
     */
    public void ausgeben(){
        System.out.println(arISIN.size());
        //System.out.println(arISIN);
        System.out.println("-----   -----   -----   -----");
        System.out.println("akitenlinks");
        System.out.println(araktien.size());
        //System.out.println(araktien);
        System.out.println("-----   -----   -----   -----");
        System.out.println("laenderlinks");
        System.out.println(arlaender.size());
        //System.out.println(arlaender);
    }


    /**
     * schreibt in eine .txt Datei die Ergebnisse, damit man nicht immer alle AKtien durchlaufen muss (Zeitersparnis)
     * @author Tobias Heiner
     * @throws IOException
     */
    public void writ()throws IOException{
        FileWriter fw = new FileWriter("ausgabeISIN.txt");
        int i = 0;
        while(i < arISIN.size()){
            fw.write(arISIN .get(i));
            fw.write("\n");
            //fw.close();
            i++;
        }
        fw.close();
    }

    /**
     * durchläuft eine Seite, um alle Links zu den Aktien zu bekommen
     */
    public void aktienDurchlaufen(){
        int t = doc.select("td.TEXT").size();
        //System.out.println(t);
        int kl = 0;
        while(kl < t){
            try{
                doc = Jsoup.connect(cut).get();
                Element meta = doc.select("td.TEXT").get(kl);
                //System.out.println();
                String tab = meta.child(0).attr("abs:href");
                System.out.println(tab);
                araktien.add(tab);
            }catch (Exception ex){
                //System.out.println(ex);
            }finally {
                kl = kl+3;
            }


        }
    }


}
