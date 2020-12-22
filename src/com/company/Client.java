package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {


        while (true) {
            System.out.println("Enter 'login', 'register', or 'exit'");
            Scanner choice = new Scanner(System.in);
            String input = choice.nextLine();
            if ("exit".equals(input)) {
                System.out.println("Exit!"); // if keyboard input equal to ´q´ close client process
                break;
            }
            clientTask clientThread = new clientTask(input); // create a new socket task
            clientThread.run(); //Run Task

        }
    }
}