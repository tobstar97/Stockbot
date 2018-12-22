/**
 * @author Tobias Heiner
 * @description onvista
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.HashSet;
import java.util.Scanner;
public class OnvistaTest {

    String url = "https://www.onvista.de/aktien/aktien-laender/";
    Document doc;
    Document l;
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
        int j = 3;
        while(j==3){
            doc = Jsoup.connect(arlaender.get(j)).get();

            Element e = doc.getElementsByClass("LETZTER").first();

            if (e == null){
                int t = doc.select("td.TEXT").size();
                //System.out.println(t);
                int i = 0;
                while(i < t){
                    Element meta = doc.select("td.TEXT").get(i);
                    String table = meta.child(0).attr("abs:href");
                    //System.out.println(table);
                    araktien.add(table);
                    i = i+3;
                }

            }else{
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

                    int t = doc.select("td.TEXT").size();
                    //System.out.println(t);
                    int kl = 0;
                    while(kl < t){
                        try{
                            doc = Jsoup.connect(cut).get();
                            Element meta = doc.select("td.TEXT").get(kl);
                            //System.out.println();
                            String tab = meta.child(0).attr("abs:href");
                            //System.out.println(tab);
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
        while(i<j){

            String temp = araktien.get(i);
            doc = Jsoup.connect(temp).get();
            Element e = doc.select(".WERTPAPIER_DETAILS > dl:nth-child(1) > dd:nth-child(4)").first();

            //System.out.println(e.text());

            arISIN.add(e.text());
            i++;
        }
    }

    public void ausgeben(){
        System.out.println(arlaender);
    }






}
