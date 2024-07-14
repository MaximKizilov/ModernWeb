package ru.netology;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequest {
    private final static String DELIMITER = "\r\n\r\n";
    private final static String NEW_LINE = "\r\n";
    private final static String HEADER_DELIMITER = ":";

    private final String message;
    ConcurrentHashMap<Map<String, String>, Handler> handlerMap;
    private HttpMethod methodType;
    private final String url;
    private String heading;
    private String body;

    public HttpRequest(String message, ConcurrentHashMap<Map<String, String>, Handler> handlerMap) {
        this.message = message;
        this.handlerMap = handlerMap;

        String[] parts = message.split(DELIMITER);
        String head = parts[0];
        String[] headers = head.split(NEW_LINE);

        String[] firstLine = headers[0].split(" ");
        methodType = HttpMethod.valueOf(firstLine[0]);
        url = firstLine[1];
    }



    //    public Request(String methodType, String heading, String body) {
//        this.methodType = methodType;
//        this.heading = heading;
//        this.body = body;
//    }

}
