package edu.escuelaing.arep.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public abstract class HttpStockService {

    private static final String USER_AGENT = "Mozilla/5.0";
    private HashMap<URL, String> Cache = new HashMap <URL, String> ();


    public String TimeSeriesDaily() throws IOException{
        String responseStr =  "None";
        System.out.println(getURL());
        URL obj = new URL(getURL());
        if(Cache.containsKey(obj)){
            System.out.println("Contenido");
            return Cache.get(obj);
        }
        System.out.println("Nuevo");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            responseStr=response.toString();
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
        Cache.put(obj,responseStr);
        return responseStr;
    }

    abstract public String getURL();

    abstract public void setStock(String stock);

    abstract public String getStock();
}