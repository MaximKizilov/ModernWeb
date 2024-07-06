package ru.netology;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {
    static List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final Socket socket;
    ConcurrentHashMap<Map <String, String>, Handler> handlerMap = new ConcurrentHashMap<>();

    public Server() {
    }

    public Server(Socket socket) {
        this.socket = socket;
    }

    void addHandler(String methodType, String path, Handler handler){
        handlerMap.put(Map.of(methodType, path),  handler);
    }

    @Override
    public void run() {

        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {
            byte[] separatorTwo = {'\r', '\n', '\r', '\n'};
            Request request = new Request();
            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

                if (parts.length != 3) {
                    // just close socket
                    return;
                }
                request.setMethodType(parts[0]);
            StringBuilder sb = new StringBuilder();
            String headersAndBody;
            while ((headersAndBody = in.readLine()) != null && !headersAndBody.isEmpty()) {
                sb.append(headersAndBody).append("\n");
            }
            request.setHeading(sb.toString());
            Set<Map<String, String>> entrySet = handlerMap.keySet();
            for (Map<String, String> mapkey : entrySet){
                for (Map.Entry<String, String> pair : mapkey.entrySet()){
                   if(pair.getKey().equals(request.getMethodType()) && (pair.getValue().equals(request.getHeading()))){
                       System.out.println(handlerMap.get(mapkey));
                    }
                }
            }



//                // read only request line for simplicity
//                // must be in form GET /path HTTP/1.1
//                final var requestLine = in.readLine();
//                final var parts = requestLine.split(" ");
//
//                if (parts.length != 3) {
//                    // just close socket
//                    return;
//                }
//
//                final var path = parts[1];
//                if (!validPaths.contains(path)) {
//                    out.write((
//                            "HTTP/1.1 404 Not Found\r\n" +
//                                    "Content-Length: 0\r\n" +
//                                    "Connection: close\r\n" +
//                                    "\r\n"
//                    ).getBytes());
//                    out.flush();
//                    return;
//                }
//

//
//                final var length = Files.size(filePath);
//                out.write((
//                        "HTTP/1.1 200 OK\r\n" +
//                                "Content-Type: " + mimeType + "\r\n" +
//                                "Content-Length: " + length + "\r\n" +
//                                "Connection: close\r\n" +
//                                "\r\n"
//                ).getBytes());
//                Files.copy(filePath, out);
//                out.flush();


            } catch (IOException e) {
                e.printStackTrace();
            }


    }
    }



