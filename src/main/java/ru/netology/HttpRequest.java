package ru.netology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequest {
    private final static String HEADER_DELIMITER = ":";


    private HttpMethod methodType;
    private String path;
    private final Map<String, String> heading;
    private String body;

    public HttpRequest(Socket socket) {
        try (final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String requestLine = in.readLine();
            if (requestLine != null) {
                String[] requestComponents = requestLine.split(" ");
                methodType = HttpMethod.valueOf(requestComponents[0]);
                path = requestComponents[1];
            }
            heading = new HashMap<>();
            String headerLine;
            while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                String[] headerComponents = headerLine.split(HEADER_DELIMITER, 2);
                heading.put(headerComponents[0].trim(), headerComponents[1].trim());
            }
            StringBuilder payload = new StringBuilder();
            if (in.ready()) {
                int contentLength = Integer.parseInt(heading.getOrDefault("Content-Length", "0"));
                if (contentLength > 0) {
                    char[] buffer = new char[contentLength];
                    in.read(buffer, 0, contentLength);
                    payload.append(buffer);
                    body=payload.toString();
                }else{
                    body = "";
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public HttpMethod getMethodType() {
        return methodType;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeading() {
        return heading;
    }

    public String getBody() {
        return body;
    }
}

