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
            outputLine=RespuestaClima();
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

    public string RespuestaClima(){
        String outputLine +=   "<!DOCTYPE html>\n
                <html>\n
                       <head>\n
                           <title>Weather Consult</title>\n
                           <meta charset=\"UTF-8\">\n
                           <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.8\">
                       </head>\n
                       <body>\n
                       <div class='container center'>\n
                           <div class='card'>\n
                           <h1> <span class='dot'></span> CONSULT THE WHEATER API HERE <span class='dot'></span></h1>\n
                           <p>Type here the city you want to consult</p>\n
                           <div class='info-wrapper'>\n
                           <input id='inputField'>\n
                           <button id='submit'>Sent it</button>\n
                       </div>\n
                       <p><strong>Side Note: </strong> To go back from the resource just erase the last part of the URL until the last / inclusive</p>\n
                      </div>\n"
                      </body>\n"
                       <script>\n"
                           document.addEventListener('DOMContentLoaded', () => {\n
                            document.getElementById('submit').addEventListener('click', () => {\n
                        var request = 'consulta?lugar=' + document.getElementById('inputField').value\n
                        window.location.replace(request);\n
                    "})\n
                "});\n
                       </script>
                </html>;

        return outputLine;
    }



    public int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }

}
