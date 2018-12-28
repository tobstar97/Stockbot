
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



/**
 * @author Eugen Kildau
 * 
 * Aktueller Stand 28.12.2018 
 * Eine Aktie - hier BMW
 * Alle benötigten Werte
 * Die benötigten Werte werden ausgelesen und ausgegeben
 * 
 * * Benötigte Werte:
 * 
 * Umsatz
 * Eigenkapital (ekp)
 * Gesammtkapital(Summe Passiva)(gkp)
 * Operatives Ebit
 * Jahresüberschuss/Jahresverlust (jue)
 */



public class History {
	
	/**
	 * Feeder übergibt Link.
	 * Test an einem Beispiel
	 *  * 
	 */
	
	
	static Ariva ariva = new Ariva();
	
	/* Benötigte Daten zum runterziehen */
    static double umsatz;
    static double EBIT;
    static double jue;
    static double ekp;
    static double gkp;
    
    
    static int seitenAnzahl = 1;  //Die Pages die am Ende der URL eingefügt werden 
    static String url = "https://www.ariva.de/bmw-aktie/bilanz-guv?page=";   //BMW-Aktie
	static int taballenAnzahl = 5;  //Tabellen in Ariva (Kennzahlen)
	static int jahr = 2017;         //Aktuell nur zur übersicht
	
    static Document doc;
    
    /*Hilfsvariablen */
    
    static int gkpHv = 9; // Gesammtkapital Hilfsvariable
    static int gkpHvS = 17; //Gesammtkapital Hilfsvariable ab Page 7
    static int ekpHv = 16; //Eigenkapital Hilfsvariable
    
    /* Auf der ersten Seite werden die Daten aus dem Jahr 2017-2012 ausgelesen */

public static void getDataPageOne() throws Exception{
		/* Überprüfung ob Minderheitenanteile vorhanden sind
		 *  */
	
		Element metaMA = ariva.doc.getElementsByClass("subtitle level").get(7);
		Element tableMA = metaMA.siblingElements().get(taballenAnzahl);
		String contentMA = tableMA.text();
		if (contentMA.contentEquals("-")){
			gkpHv = gkpHv - 1;
		}
	
		Element metaUmsatz = ariva.doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(0);
		Element metaEBIT = ariva.doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(1);
		Element metaJue = ariva.doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(3);
		Element metaEkp = ariva.doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(7);
		Element metaGkp = ariva.doc.getElementsByClass("subtitle level").get(gkpHv);
        
		while(taballenAnzahl>=0){
		Element tableUmsatz = metaUmsatz.siblingElements().get(taballenAnzahl);
		Element tableEBIT = metaEBIT.siblingElements().get(taballenAnzahl);
		Element tableJue= metaJue.siblingElements().get(taballenAnzahl);
		Element tableEkp= metaEkp.siblingElements().get(taballenAnzahl);
		Element tableGkp= metaGkp.siblingElements().get(taballenAnzahl);
		
		String contentUmsatz = newFormat(tableUmsatz.text());
		String contentEBIT = newFormat(tableEBIT.text());
		String contentJue = newFormat(tableJue.text());
		String contentEkp = newFormat(tableEkp.text());
		String contentGkp = newFormat(tableGkp.text());
		
        umsatz = Double.parseDouble(contentUmsatz);
        EBIT =  Double.parseDouble(contentEBIT);
        jue =  Double.parseDouble(contentJue);
        ekp =  Double.parseDouble(contentEkp);
        gkp =  Double.parseDouble(contentGkp);
        
        System.out.println("Jahr:   "+ jahr + " ---- " + "Umsatz: " + umsatz + " EBIT: " + EBIT + " Gewinn/Verlust: " + jue + " Eigenkapital: " + ekp + " Gesammtkaptial: " + gkp);  // Zum Test wird es ausgegeben, wird in Datenbank geschrieben. ** Muss noch implementiert werden
		jahr--;
		taballenAnzahl--;
		}
		
		seitenAnzahl++;
	
   }

/*  Daten von Seite 2 - 7 werden ausgelesen */

public static void getDataPageTwo() throws Exception{
	Element metaMA = ariva.doc.getElementsByClass("subtitle level").get(7);
	Element tableMA = metaMA.siblingElements().get(0);
	String contentMA = tableMA.text();
	if (contentMA.contentEquals("-") && gkpHv ==9){
		gkpHv = gkpHv - 1;
	}
	
	Element metaUmsatz = ariva.doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(0);
	Element metaEBIT = ariva.doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(1);
	Element metaJue = ariva.doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(3);
	Element metaEkp = ariva.doc.getElementsByClass("subtitle level text-linkblue clickCursor").get(7);
	Element metaGkp = ariva.doc.getElementsByClass("subtitle level").get(gkpHv);
	
	
	Element tableUmsatz = metaUmsatz.siblingElements().get(0);
	Element tableEBIT = metaEBIT.siblingElements().get(0);
	Element tableJue= metaJue.siblingElements().get(0);
	Element tableEkp= metaEkp.siblingElements().get(0);
	Element tableGkp= metaGkp.siblingElements().get(0);
	
	String contentUmsatz = newFormat(tableUmsatz.text());
	String contentEBIT = newFormat(tableEBIT.text());
	String contentJue = newFormat(tableJue.text());
	String contentEkp = newFormat(tableEkp.text());
	String contentGkp = newFormat(tableGkp.text());
	
    umsatz = Double.parseDouble(contentUmsatz);
    EBIT =  Double.parseDouble(contentEBIT);
    jue =  Double.parseDouble(contentJue);
    ekp =  Double.parseDouble(contentEkp);
    gkp =  Double.parseDouble(contentGkp);
    
    System.out.println("Jahr:   "+ jahr + " ---- " + "Umsatz: " + umsatz + " EBIT: " + EBIT + " Gewinn/Verlust: " + jue + " Eigenkapital: " + ekp + " Gesammtkaptial: " + gkp);  // Zum Test wird es ausgegeben, wird in Datenbank geschrieben. ** Muss noch implementiert werden
	jahr--;
	seitenAnzahl++;
		
}
public static void getDataPageSeven() throws Exception{
	Element metaMA = ariva.doc.getElementsByClass("subtitle level").get(14);
	Element tableMA = metaMA.siblingElements().get(0);
	String contentMA = tableMA.text();
	if (contentMA.contentEquals("-") && gkpHvS==17){
		gkpHvS = gkpHvS - 1;
		ekpHv = ekpHv -1;
	
	}

	Element metaUmsatz = ariva.doc.getElementsByClass("subtitle level").get(0);
	Element metaEBIT = ariva.doc.getElementsByClass("subtitle level").get(2);
	Element metaJue = ariva.doc.getElementsByClass("subtitle level").get(8);  
	Element metaEkp = ariva.doc.getElementsByClass("subtitle level").get(ekpHv); 
	Element metaGkp = ariva.doc.getElementsByClass("subtitle level").get(gkpHvS);  
				
	Element tableUmsatz = metaUmsatz.siblingElements().get(0);
	Element tableEBIT = metaEBIT.siblingElements().get(0);
	Element tableJue= metaJue.siblingElements().get(0);
	Element tableEkp= metaEkp.siblingElements().get(0);
	Element tableGkp= metaGkp.siblingElements().get(0);
	
	
    String contentUmsatz = newFormat(tableUmsatz.text());
    String contentEBIT = newFormat(tableEBIT.text());
	String contentJue = newFormat(tableJue.text());
	String contentEkp = newFormat(tableEkp.text());
	String contentGkp = newFormat(tableGkp.text());

      umsatz = Double.parseDouble(contentUmsatz);
      EBIT = Double.parseDouble(contentEBIT);
      jue =  Double.parseDouble(contentJue);
      ekp =  Double.parseDouble(contentEkp);
      gkp =  Double.parseDouble(contentGkp);
      
      System.out.println("Jahr:   "+ jahr + " ---- " + "Umsatz: " + umsatz + " EBIT: " + EBIT + " Gewinn/Verlust: " + jue + " Eigenkapital: " + ekp + " Gesammtkaptial: " + gkp);  // Zum Test wird es ausgegeben, wird in Datenbank geschrieben. ** Muss noch implementiert werden
      jahr--;
	  seitenAnzahl++;
		
		
		}

	public static void getData() throws Exception{
try{

    		ariva.connectURL(url+seitenAnzahl);   //Verbinde zur Page 1
    		getDataPageOne();
    		ariva.disconnect();
    		
    		while(seitenAnzahl<7){                //Seiten 2-7

    			ariva.connectURL(url+seitenAnzahl);
    			getDataPageTwo();
    		    ariva.disconnect();
    		}
    			while(seitenAnzahl>=7){                         //Ab Seite 7
        			ariva.connectURL(url+seitenAnzahl);
        			getDataPageSeven();
        		    ariva.disconnect();
    			}
    
    			

}
catch (NumberFormatException e) {
	
}
	}
	
	public static String newFormat(String content){
	       if(content.contains(".")){
	           content = content.replace(".","");
	       }
	       if(content.contains(",")){
	           content = content.replace(",",".");
	       }
	       return content;
	}
	
	public static void main (String [] args) throws Exception {

    	System.out.println("--------------------");
        System.out.println("-=- -=- Ariva -=- -=-");
        getData();


    }
}

/*Folgende Methode muss in die Klasse Ariva um mein Code zu testen:
 * 
 * 
 *     public void connectURL(String url) throws IOException {

       doc = Jsoup.connect(url).get();
    }
 * 
 *  
 *  
 *  
 *  */
