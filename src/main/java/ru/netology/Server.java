package ru.netology;



import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {
    private static final ConcurrentHashMap<Map<HttpMethod, String>, Handler> handlerMap = new ConcurrentHashMap<>();
    static List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final Socket socket;
    HttpRequest httpRequest;

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
            //чтение headers line
            String requestLine = in.readLine();
            if (requestLine != null) {
                String[] requestComponents = requestLine.split(" ");
                HttpMethod method = HttpMethod.valueOf(requestComponents[0]);
                String pathAndQuery = requestComponents[1];
                String path;
                List<NameValuePair> query = List.of();
                if(pathAndQuery.contains("?")){
                    String[] s = pathAndQuery.split(("\\?"), 2);
                     path = s[0];
                    query=queryParam(s[1]);
                }else {
                    path = pathAndQuery;
                }


                Map<String, String> headers = new HashMap<>();
                String headerLine;
                while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                    String[] headerComponents = headerLine.split(":", 2);
                    headers.put(headerComponents[0].trim(), headerComponents[1].trim());
                }
//чтение боди
                StringBuilder payload = new StringBuilder();
                if (in.ready()) {
                    int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
                    if (contentLength > 0) {
                        char[] buffer = new char[contentLength];
                        in.read(buffer, 0, contentLength);
                        payload.append(buffer);
                    }
                }
                httpRequest = new HttpRequest(method, path, query, headers, payload.toString());
            }
            if (httpRequest != null) {
                for (Map.Entry<Map<HttpMethod, String>, Handler> entry : handlerMap.entrySet()) {
                    Map<HttpMethod, String> mapKey = entry.getKey();
                    for (HttpMethod method : mapKey.keySet()) {
                        if (method.equals(httpRequest.methodType())) {
                            String path;
                            if (mapKey.get(method).equals(httpRequest.path())) {
                                path = mapKey.get(method);
                                Handler handler = handlerMap.get(Map.of(method, path));
                                handler.handle(httpRequest, out);
                                return;
                            }
                        }
                    }
                }
            } else {
                out.write(("HTTP/1.1 404 Not found \r\n " +
                        "Server: local \r\n  " +
                        "Connection: close\r\n\r\n" +
                        "<h1>Not found<h1>").getBytes());
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<NameValuePair> queryParam(String line){
        String[] multiQuery = line.split("\\?");
        for (String s : multiQuery) {
            if (s.contains("=")) {
                return URLEncodedUtils.parse(s, StandardCharsets.UTF_8);
            }
        }
        return List.of();
    }
}





