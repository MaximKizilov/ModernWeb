package ru.netology;

import java.util.concurrent.ConcurrentHashMap;

public class HttpRequest {
    private final static String DELIMITER = "\r\n\r\n";
    private final static String NEW_LINE = "\r\n";
    private final static String HEADER_DELIMITER = ":";

    private final String message;

    private final HttpMethod methodType;
    private final String path;
    private final ConcurrentHashMap<String, String> heading;
    private final String body;

    public HttpRequest(String message) {
        this.message = message;


        String[] parts = message.split(DELIMITER);
        String head = parts[0];
        String[] headers = head.split(NEW_LINE);

        String[] firstLine = headers[0].split(" ");
        methodType = HttpMethod.valueOf(firstLine[0]);
        path = firstLine[1];

        this.heading = new ConcurrentHashMap<>();
        for (int i = 1; i < headers.length; i++) {
            String[] headerPart = headers[i].split(HEADER_DELIMITER, 2);
            heading.put(headerPart[0].trim(), headerPart[1].trim());
        }
        String bodyLength = heading.get("Content-Length");
        int length = bodyLength != null ? Integer.parseInt(bodyLength) : 0;
        this.body = parts.length > 1 ? parts[1].trim().substring(0, length) : "";
    }

    public String getMessage() {
        return message;
    }

    public HttpMethod getMethodType() {
        return methodType;
    }

    public String getPath() {
        return path;
    }

    public ConcurrentHashMap<String, String> getHeading() {
        return heading;
    }

    public String getBody() {
        return body;
    }
}
