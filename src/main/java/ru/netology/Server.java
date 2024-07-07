package ru.netology;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {
    static List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final Socket socket;
    ConcurrentHashMap<Map <String, String>, Handler> handlerMap = new ConcurrentHashMap<>();


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
            String headers;
            while ((headers = in.readLine()) != null && !headers.isEmpty()) {
                sb.append(headers).append("\n");
            }
            request.setHeading(sb.toString());

            StringBuilder bodyBuilder = new StringBuilder();
            char[] buffer = new char[1024];
            int bytesRead;
            boolean bodyStarted = false;
            while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                if (!bodyStarted) {
                    String line = new String(buffer, 0, bytesRead);
                    if (line.contains("\r\n\r\n")) {
                        bodyStarted = true;
                        bodyBuilder.append(line, line.indexOf("\r\n\r\n") + 4, bytesRead);
                    }
                } else {
                    bodyBuilder.append(buffer, 0, bytesRead);
                }
            }
            InputStream bodyInputStream = new ByteArrayInputStream(bodyBuilder.toString().getBytes(StandardCharsets.UTF_8));
            request.setBody(bodyInputStream);

            for (Map.Entry<Map<String, String>, Handler> entry : handlerMap.entrySet()) {
                Map<String, String> mapKey = entry.getKey();
                Handler handler = entry.getValue();
                if (mapKey.get("GET").equals(request.getHeading())) {
                    handler.handle(request, out); // Assuming the Handler interface has a handle method
                    break; // Exit the loop after finding a matching handler
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



