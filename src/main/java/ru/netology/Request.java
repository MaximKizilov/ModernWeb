package ru.netology;

import java.io.BufferedOutputStream;

public class Request {
    private String methodType;
    private String heading;
    private String body;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
