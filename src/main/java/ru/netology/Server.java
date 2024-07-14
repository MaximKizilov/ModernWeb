package ru.netology;


import javax.crypto.spec.PSource;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {
    private static final ConcurrentHashMap<Map<HttpMethod, String>, Handler> handlerMap = new ConcurrentHashMap<>();
    static List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final Socket socket;


    public Server(Socket socket) {
        this.socket = socket;
    }


    static void addHandler(HttpMethod methodType, String path, Handler handler) {
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

            for (Map.Entry<Map<HttpMethod, String>, Handler> entry : handlerMap.entrySet()) {
                Map<HttpMethod, String> mapKey = entry.getKey();

                for (HttpMethod method : mapKey.keySet()) {
                    if (method.equals(httpRequest.getMethodType())) {
                        String path;
                        if (mapKey.get(method).equals(httpRequest.getPath())) {
                            path = mapKey.get(method);
                            Handler handler = handlerMap.get(Map.of(method, path));
                            handler.handle(httpRequest, out);
                            return;
                        } else {


                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}



