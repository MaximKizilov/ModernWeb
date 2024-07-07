package ru.netology;

import java.io.BufferedOutputStream;
import java.io.InputStream;

public class Request {
    private String methodType;
    private String heading;
    private InputStream body;

//    public Request(String methodType, String heading, String body) {
//        this.methodType = methodType;
//        this.heading = heading;
//        this.body = body;
//    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public InputStream getBody() {
        return body;
    }

    public void setBody(InputStream body) {
        this.body = body;
    }
}
