package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
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
                Server server = new Server(clientSocket);

                Server.addHandler(HttpMethod.GET, "/messages", (request, responseStream) -> {
                        responseStream.write("HTTP/1.1 200 OK\r\nConnection: close\r\n\r\n".getBytes());
                        responseStream.flush();

                });


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
                executorService.execute(server);

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
