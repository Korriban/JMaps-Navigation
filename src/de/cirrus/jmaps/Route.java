package de.cirrus.jmaps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.cirrus.jmaps.gsonclasses.*;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Route implements Serializable {

    private String origin;
    private String destination;

    private StartLocation startLocation;
    private EndLocation endLocation;

    private BufferedImage routeImage;
    private String jsonFileName;
    private MainRouteClass mainRouteClass;
    private double distanceInMeters;
    private String distanceAsText;

    public Route(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;

        Gson gson = new GsonBuilder().create();
        Reader reader = getAndDeserializeRouteJSON(origin, destination);

        if(reader != null) {
            this.mainRouteClass = gson.fromJson(reader, MainRouteClass.class);
            if (mainRouteClass == null) {
                throw new RuntimeException("Could not deserialize JSON for route: " + origin + " to " + destination);
            }
        }


        this.distanceInMeters = this.mainRouteClass.getRoutes().get(0).getLegs().get(0).getDistance().getValue();
        this.distanceAsText = this.mainRouteClass.getRoutes().get(0).getLegs().get(0).getDistance().getText();
        this.startLocation = this.mainRouteClass.getRoutes().get(0).getLegs().get(0).getStartLocation();
        this.endLocation = this.mainRouteClass.getRoutes().get(0).getLegs().get(0).getEndLocation();

        this.routeImage = getRouteImage(this.mainRouteClass, origin, destination, true);
        this.showImage(this.routeImage);
    }


    private static byte[] executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            return IOUtils.toByteArray(connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String convertToURL(String txt) {
        String result = null;
        try {
            result = URLEncoder.encode(txt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }



    private Reader getAndDeserializeRouteJSON(String origin, String destination) {
        //get JSON Data
        byte[] json_data = executePost("https://maps.googleapis.com/maps/api/directions/json?origin="
                + convertToURL(origin) +"&destination="+ convertToURL(destination)
                + "&mode=driving&key=" + JMaps.API_KEY, "");

        this.jsonFileName = origin + "_" + destination + ".json";
        Path file_path2 = Paths.get(JMaps.OUTPUT_FOLDER_PATH + this.jsonFileName);
        try {
            Files.write(file_path2, json_data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Reader reader = null;
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file_path2.toAbsolutePath().toString());
            reader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return reader;
    }

    private BufferedImage getRouteImage(MainRouteClass routeClass, String origin, String destination, boolean shouldWriteToFS) {
        ArrayList<OverviewPolyline> opList = new ArrayList<>();
        String overviewPolyline = "";
        for(SubRoute sr : routeClass.getRoutes()) {
            OverviewPolyline op = sr.getOverviewPolyline();
            opList.add(op);
            overviewPolyline += convertToURL(op.getPoints());
        }



        byte[] route_pic_data =
                executePost("\n" +
                        "https://maps.googleapis.com/maps/api/staticmap?sensor=false&size=600x600&scale=2"
                        +"&markers=color:blue%7Clabel:S%7C"+startLocation.getLat()+","+startLocation.getLng()
                        +"&markers=color:red%7Clabel:Z%7C"+endLocation.getLat()+","+endLocation.getLng()
                        +"&path=weight:3%7Ccolor:red%7Cenc:" + overviewPolyline
                        +"&key=" + JMaps.API_KEY, "");

        if(shouldWriteToFS) {
            String map_pic_filename = origin + "_" + destination + ".png";
            String folder_path = "./data_output/";
            String file_path_string = folder_path + map_pic_filename;

            Path file_path = Paths.get(file_path_string);
            try {
                Files.write(file_path, route_pic_data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream in = new ByteArrayInputStream(route_pic_data);

        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void showImage(BufferedImage image) {
        JFrame frame = new JFrame(this.origin + " -> " + this.destination + " --- distance: " + this.distanceAsText);
        frame.add(new JLabel(new ImageIcon(image)));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public String getOrigin() {
        return this.origin;
    }

    public String getDestination() {
        return this.destination;
    }

    public BufferedImage getImage() {
        return this.routeImage;
    }

    public BufferedImage getRouteImage() {
        return routeImage;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public MainRouteClass getMainRouteClass() {
        return mainRouteClass;
    }

    public double getDistanceInMeters() {
        return distanceInMeters;
    }

    public String getDistanceAsText() {
        return distanceAsText;
    }
}
