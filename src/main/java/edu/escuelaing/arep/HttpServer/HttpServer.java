package edu.escuelaing.arep.HttpServer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

public class HttpServer {
    private String[] extensions = new String[]{"html"};
    private static final HttpServer _instance = new HttpServer();
    public static HttpServer getInstance(){return _instance;}
    private String ApiKey ="65d3dc12b4e7da8442638198b0e9f716M";
    private String URL = "hapi.openweathermap.org/data/2.5/weather?q=";


    public void start(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running=true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            serveConneciton(clientSocket);
        }
        serverSocket.close();
    }

    public void serveConneciton(Socket clientSocket) throws IOException {
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        ArrayList<String> request = new ArrayList<String>();
        while ((inputLine = bufferedReader.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            request.add(inputLine);
            if (!bufferedReader.ready()) {
                break;
            }
        }
        String UrlStr="";
        if(request.size()>0) {
            UrlStr = request.get(0).split(" ")[1];
        }
        if (UrlStr.equals("/clima")){
            outputLine="";
            printWriter.println(outputLine);
        }
        else if(UrlStr.contains("/consulta?lugar=")){
            String site = URL+UrlStr.replace("/consulta?lugar=","")+ApiKey;
            outputLine="";
            System.out.println(outputLine);
            printWriter.println(outputLine);

        }
        printWriter.close();
    }



    public int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }

}
