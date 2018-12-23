package Feeder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;

public class FeederOnvista {

    String url = "https://www.onvista.de/aktien/aktien-laender/";
    Document doc;
    ArrayList<String>arlaender = new ArrayList<>();
    ArrayList<String>araktien = new ArrayList<>();
    ArrayList<String>arISIN = new ArrayList<>();
    int anzahl;




    public void connect()throws IOException{
        doc = Jsoup.connect(url).get();
    }


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


                String cut = arlaender.get(j);
                System.out.println(cut);
                //String temp = e.child(0).attr("abs:href");
                //temp = temp.replace(cut, "");

                f = f+1;

                int o = 0;
                while(o<=f){
                    o++;
                    cut = cut+o;
                    o--;
                    int t = doc.select("td.TEXT").size();
                    //System.out.println(t);
                    int i = 0;
                    while(i < t){       //try catch benötigt (wie unten) -- erledigt
                        try{
                            doc = Jsoup.connect(cut).get();
                            Element meta = doc.select("td.TEXT").get(i);
                            String table = meta.child(0).attr("abs:href");
                            System.out.println(table);
                            araktien.add(table);
                        }catch (Exception ex){
                            System.err.println("FeederOnvistaLowMem: Z.82: " + ex.getCause() + " | " + ex.getMessage() + " | " + ex.getStackTrace());
                        }finally {
                            i = i+3;
                        }
                    }
                    o++;
                    cut = cut.replace(String.valueOf(o), "");
                }



            }else{      //wenn es ein LETZTE Seite Element gibt
                //sucht sich die letzte Seite, speichert den Wert und läuft dann durch die Seiten durch
                String cut = arlaender.get(j);
                //System.out.println(cut);
                String temp = e.child(0).attr("abs:href");
                temp = temp.replace(cut, "");
                int z = Integer.parseInt(temp);

                int i = 1;

                while(i<=z){
                    cut = cut+i;
                    //System.out.println(cut);
                    //hier wird dann die seite nach aktien durchlaufen
                    //ab hier könnte man bis zur line 136 eine eigene Methode schreiben und die dann einfach hier aufrufen (siehe Kommentar oben, da diese Teile redundant sind)
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
                    cut = cut.replace(String.valueOf(i), "");
                    i++;
                }
            }
            j++;
        }
    }


    public void getISIN()throws IOException{
        int j = araktien.size();
        int i = 0;
        while(i<j){     //try catch benötigt, da seite eventuell nicht mehr verfügbar -- erledigt
            try{
                String temp = araktien.get(i);
                temp = temp.substring((temp.length()-12));
                arISIN.add(temp);
            }catch (Exception ex){
                System.err.println("FeederOnvistaLowMem: Z.82: " + ex.getCause() + " | " + ex.getMessage() + " | " + ex.getStackTrace());
            }finally {
                i++;
            }
        }
    }

    public void ausgeben(){
        System.out.println(arISIN.size());
        System.out.println(arISIN);
    }

}
