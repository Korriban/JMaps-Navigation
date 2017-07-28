package de.cirrus.jmaps;

import java.io.*;


public class JMaps implements Runnable {
    public static final String API_KEY = "AIzaSyD5gB64IBSS0wGHZvnNEtTARm6E2iID8OU";
    public static final String OUTPUT_FOLDER_PATH = "./data_output/";


    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        Route route = new Route("Kyoto, Japan", "Okinawa");
        Route route2 = new Route("Rheinstr. 5, Darmstadt Germany", "Rektor-Plum-Weg 12, Mainz, Germany");
        Route route3 = new Route("Gugelstr. 5, Worms", "Mannheim");
    }

    public static void main(String[] args) {
        File output_folder = new File(OUTPUT_FOLDER_PATH);
        if(!output_folder.exists()) {
            output_folder.mkdir();
        }

        JMaps jmaps = new JMaps();
        jmaps.start();
    }
}
