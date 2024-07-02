package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static ExecutorService executorService = Executors.newFixedThreadPool(64);
    static List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new Server(clientSocket, validPaths));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
