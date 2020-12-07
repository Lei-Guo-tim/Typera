package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        int port = 7913;
        String ip = "127.0.0.1";

        try {

            do {
                // Connect with server
                Socket connection = new Socket(ip, port);
                System.out.println("Connected! " + connection);
                System.out.println("Enter 'login', 'register', or 'exit'");
                Scanner choice = new Scanner(System.in);
                String input = choice.nextLine();

                // Setting up input and output streams
                DataOutputStream outputChoice = new DataOutputStream(connection.getOutputStream());
                DataOutputStream outputName = new DataOutputStream(connection.getOutputStream());
                DataOutputStream outputPass = new DataOutputStream(connection.getOutputStream());
                DataInputStream inputStr = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                DataInputStream registerMess = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                DataInputStream loginMess = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                //Login
                if(input.equals("register")) {
                    Scanner inputName = new Scanner(System.in);
                    System.out.println("Please enter your username: ");
                    String username = inputName.next();
                    Scanner inputPass = new Scanner(System.in);
                    System.out.println("Please enter your password: ");
                    String password = inputPass.next();

                    // Start communication
                    outputChoice.writeUTF(input); //Writing register, login, exit choice
                    outputChoice.flush();
                    outputName.writeUTF(username); //Writing username
                    outputName.flush();
                    //Writing password
                    outputPass.writeUTF(password);
                    outputPass.flush();
                    //User already exists exist message
                    String regMess = registerMess.readUTF();
                    System.out.println(regMess);


                } if (input.equals("login")) {
                    Scanner inputLog = new Scanner(System.in);
                    System.out.println("Please enter your username: ");
                    String username = inputLog.next();
                    System.out.println("Please enter your password: ");
                    String password = inputLog.next();
                    // Start communication
                    outputChoice.writeUTF(input); //Writing register, login, exit choice
                    outputChoice.flush();
                    outputName.writeUTF(username); //Writing username
                    outputName.flush();
                    //Writing password
                    outputPass.writeUTF(password);
                    outputPass.flush();
                    //User exist or does not exist message
                    String logMess = loginMess.readUTF();
                    System.out.println(logMess);

                }else {
                    System.out.println("Invalid login. Try again.");
                }
                // Close streams and socket
                outputChoice.close();
                outputPass.close();
                outputName.close();
                inputStr.close();
                connection.close();
            } while (true);


        } catch (IOException ex) {
            System.out.println("Connection error! " + ex);
        }
    }
}
