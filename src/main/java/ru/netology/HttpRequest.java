package ru.netology;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Map;

public record HttpRequest(HttpMethod methodType, String path, List<NameValuePair> query, Map<String, String> heading, String body) {

    public String getQueryParam(String name){
        for (NameValuePair x: query){
            if(x.getName().equals(name)){
                return x.getValue();
            }
        }
        return "";
    }
    public List<NameValuePair> getQueryParams(){
        return query.stream().toList();
    }
}
