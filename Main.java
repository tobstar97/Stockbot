import Ariva.Feeder_Ariva;

import java.io.*;





public class Main {

    public static void main(String[] args) {
        Steuerung st = new Steuerung();
        try {
            st.feeder();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
