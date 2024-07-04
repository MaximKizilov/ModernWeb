package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static ExecutorService executorService = Executors.newFixedThreadPool(64);


    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                final var server = new Server(clientSocket);
                server.addHandler("GET", "/messages", new Handler() {
                   public void handle(Request request, BufferedOutputStream responseStream) {
//                        // TODO: handlers code
                    }
                });
                executorService.execute(new Server(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
