package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;

public interface Handler {
    String handle(HttpRequest request, BufferedOutputStream responseStream) throws IOException;
}
