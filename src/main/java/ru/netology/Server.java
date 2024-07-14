package ru.netology;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {
    private static final ConcurrentHashMap<Map<String, String>, Handler> handlerMap = new ConcurrentHashMap<>();
    static List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final Socket socket;
    //   private final Handler handler;


    public Server(Socket socket) {
        this.socket = socket;
    }

    static void addHandler(String methodType, String path, Handler handler) {
        handlerMap.put(Map.of(methodType, path), handler);
    }

    @Override
    public void run() {

        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {
            StringBuilder request = new StringBuilder();
            int value;
            while ((value = in.read()) != -1) {
                request.append((char) value);
            }
            HttpRequest httpRequest = new HttpRequest(request.toString());

            for (Map.Entry<Map<String, String>, Handler> entry : handlerMap.entrySet()) {
                Map<String, String> mapKey = entry.getKey();

                for (String method : mapKey.keySet()) {
                    if (method.equals(httpRequest.getMethodType().toString())) {
                        String path;
                        if (mapKey.get(method).equals(httpRequest.getPath())) {
                            path = mapKey.get(method);
                            Handler handler = handlerMap.get(Map.of(method, path));
                            handler.handle(httpRequest, out);
                            return;
                        } else {

                        }
                    }
//                    handler.handle(httpRequest, out); // Assuming the Handler interface has a handle method
//                    break; // Exit the loop after finding a matching handler
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



