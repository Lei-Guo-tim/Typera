package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

class Server {
    private static Socket connection;
    private static ExecutorService thPoolServer = Executors.newFixedThreadPool(10);
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        System.out.println("Server has started");
        serverSocket = new ServerSocket(7913);
        while (true) {
            connection = serverSocket.accept();//Accept when a request arrives
            serverTask task = new serverTask(connection);//Start a task Thread to handle client request
            thPoolServer.execute(task);
        }
    }
}