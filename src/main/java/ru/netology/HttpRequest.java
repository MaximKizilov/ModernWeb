package ru.netology;

import java.util.Map;

public record HttpRequest(HttpMethod methodType, String path, Map<String, String> heading, String body) {


}
