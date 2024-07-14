package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static ExecutorService executorService = Executors.newFixedThreadPool(64);
    static ConcurrentHashMap<Map<String, String>, Handler> handlerMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
//                server.addHandler("GET", "/index.html", (request, responseStream) -> {
//                    final var filePath = Path.of(".", "public", "/index.html");
//                    final var mimeType = Files.probeContentType(filePath);
//                    final var length = Files.size(filePath);
//                    responseStream.write((
//                            "HTTP/1.1 200 OK\r\n" +
//                                    "Content-Type: " + mimeType + "\r\n" +
//                                    "Content-Length: " + length + "\r\n" +
//                                    "Connection: close\r\n" +
//                                    "\r\n"
//                    ).getBytes());
//                    Files.copy(filePath, responseStream);
//                    responseStream.flush();
//                });
                executorService.execute(new Server(clientSocket));



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
