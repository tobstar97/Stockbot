import java.util.concurrent.ExecutionException;

/**
 * @author Tobias Heiner
 * @description Test für die Kombination von onvista feeder und kurse von ariva
 */





public class Test {


    public static void main(String[] args) {
        OnvistaTest o = new OnvistaTest();

        try {
            o.connect();
            o.laender();
            o.aktienderlaender();
        }catch (Exception e){
            System.out.println(e);
        }




    }
}
