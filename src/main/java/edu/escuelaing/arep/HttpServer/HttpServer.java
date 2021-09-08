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
    public HttpServer(){}
    public void start(String[] args) throws IOException{
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
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        ArrayList<String> request = new ArrayList<String>();
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            request.add(inputLine);
            if (!in.ready()) {
                break;
            }
        }
        String UrlStr="";
        ;
        if(request.size()>0) {
            UrlStr = request.get(0).split(" ")[1];
        }
        File archivo = new File("public_html/"+UrlStr);
        if(UrlStr.equals("/")){UrlStr="/index.js";}
        if(FilenameUtils.isExtension(UrlStr, extensions) && archivo.exists()) {
            outputLine = getResouce(UrlStr);
            out.println(outputLine);
        }else if (!FilenameUtils.getExtension(UrlStr).equals("") && archivo.exists()){
            outimage(UrlStr,clientSocket.getOutputStream());
        }else {
            outputLine =errorResponse(UrlStr);
            System.out.println(outputLine);
            out.println(outputLine);
        }
        out.close();
        clientSocket.close();
        in.close();
    }

    public String errorResponse(String UrlStr) throws IOException {
        String val = "public_html/Error404.html";
        File archivo = new File(val);
        BufferedReader in = new BufferedReader(new FileReader(archivo));
        String output = "HTTP/1.1 200 OK\r\nContent - Type: text/html \r\n\r\n", str,res;
        while ((str = in.readLine()) != null) {
            if (str.contains("404 Not Found")){
                output+=str+"\n <br> <b><big><FONT COLOR=\"white\" size=\"500\"> The requested URL "+UrlStr+" not found  on this server</FONT></big></b>\n";
            }else {
                output+=str+"\n";
            }
        }
        return output;
    }

    public String outimage(String UrlStr, OutputStream output) {
        File file = new File("public_html/"+UrlStr);
        String extension = FilenameUtils.getExtension(UrlStr);
        try {
            BufferedImage image = ImageIO.read(file);
            ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
            DataOutputStream writeimg = new DataOutputStream(output);
            ImageIO.write(image, extension, ArrBytes);
            writeimg.writeBytes("HTTP/1.1 200 OK \r\n" + "Content-Type: image/"+extension+" \r\n" + "\r\n");
            writeimg.write(ArrBytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extension;
    }

    public String getResouce(String resourceURL) throws IOException {
        String extension=resourceURL.split("/")[1];
        return RequestResponseDiscText(extension);
    }

    public ArrayList<String> Listar (String extension){
        File carpeta;
        File[] listado;
        ArrayList<String> archivements=new ArrayList<>();
        if (extension.equals("index.js")){
            carpeta = new File("public_html");
            listado = carpeta.listFiles();
            for (int i = 0; i < listado.length; i++) {
                if (listado[i].isFile()) {
                    archivements.add(listado[i].getName());
                }
            }
        }
        return archivements;
    }

    public String RequestResponseDiscText(String extension) throws IOException {
        System.out.println(extension);
        boolean index=false;
        ArrayList<String> archivements = Listar(extension);
        if (archivements.size()>0){index=true;}
        String val = "public_html/"+extension;
        File archivo = new File(val);
        BufferedReader in = new BufferedReader(new FileReader(archivo));
        String output = "HTTP/1.1 200 OK\r\nContent - Type: text/"+FilenameUtils.getExtension(extension)+"\r\n\r\n", str,res;
        while ((str = in.readLine()) != null) {
            if (index){
                res=Rellenar(output,archivements,str);
                if (res!=""){output=res;}
                else {output+=str+"\n";}
            }
            else{output+=str+"\n";}
        }
        return  output;
    }

    public String Rellenar(String output, ArrayList<String> archivements,String str) throws IOException {
        if (str.contains("<!-- Reemplazar por menu desplegable .html -->")){
            output=buttons(output,".html", archivements);
            return output;
        }
        else if (str.contains("<!-- Reemplazar por menu desplegable .js -->") ) {
            output = buttons(output, ".js", archivements);
            return output;
        }
        return "";
    }
    
    public String buttons(String output, String type, ArrayList<String> archivements){
        for (int i=0; i< archivements.size(); i++) {
            if (archivements.get(i).contains(type) || (type=="images" && !archivements.get(i).contains(".js") && !archivements.get(i).contains(".html") && !archivements.get(i).contains(".css"))) {
                output += "                <a class=\"dropdown-item\" href=\"" + archivements.get(i) + "\">" + archivements.get(i) + "</a>\n";
            }
        }
        return output;
    }

    public int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }

}
