package Feeder;

import com.sun.javafx.css.parser.CSSParser;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.concurrent.Worker;
import com.sun.webkit.dom.HTMLButtonElementImpl;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.*;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStreamWriter;
import java.util.concurrent.CountDownLatch;

public class FeederArivaFX extends Application {

    boolean debug = true;

    //Application von anderen Klassen aus callbar machen
    public static final CountDownLatch latch = new CountDownLatch(1);
    public static FeederArivaFX faFX = null;

    public static FeederArivaFX waitForFeederArivaFX() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return faFX;
    }
    public static void setFeederArivaFX(FeederArivaFX faFX0) {
        faFX = faFX0;
        latch.countDown();
    }
    public FeederArivaFX() {
        setFeederArivaFX(this);
    }
    public void printSomething() {
        System.out.println("You called a method on the application");
    }

    //Funktionalität der Application
    @Override
    public void start(final Stage stage) {

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.setJavaScriptEnabled(true);

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                Document doc = webEngine.getDocument();
                if(debug) System.out.println("DOC: " + doc);
                //Button klicken
                HTMLButtonElementImpl button = (HTMLButtonElementImpl) doc.getElementById("aktiensuche_btn");
                if(debug) System.out.println("BTN: " + button);
                button.click();
                if(debug) System.out.println("BTN: clicked");
                //CSS querying for links
                System.out.println("I DONT FUCKING KNOW HOW THIS SHOULD WORK AT ANY TIME...");
                System.out.println("finished process");
            }
        });

        //Seite laden
        String urlYT = "https://www.youtube.com/channel/UCrcBPyNUVwr54dIL2gi3-mg/about";
        String urlAriva = "https://www.ariva.de/aktien/suche#page=0&year=_year_2017&sort_n=ariva_name&sort_d=asc";
        webEngine.load(urlAriva);

        //Box
        VBox box = new VBox();
        box.getChildren().addAll(browser); //in die Box muss alles rein was bearbeitet werden soll / mit dem wir arbeiten wollen!
        Scene scene = new Scene(box);

        //die Stage wird für den User / Dev angezeigt
        stage.setTitle("JavaFX WebView (Ariva-Aktiensuche)");
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
