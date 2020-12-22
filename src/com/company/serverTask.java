package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class serverTask implements Runnable {
    private Socket connection;  // Create Socket

    public serverTask(Socket s) {
        this.connection = s;
    }

    @Override
    public void run() {
        Scanner choice1 = new Scanner(System.in);
        String input;
        input = choice1.nextLine();
        while (true) {
            if (input.equals("start")) {
                System.out.println("~~Welcome to  the Type Racing Game Server~~");

                do {
                    //Register service in the port 7913 and wait for connections
                    ServerSocket server = null;
                    try {

                        System.out.println("waiting for connections");

                        System.out.println("User> connected " + connection);
                        //Setting up input output streams
                        DataInputStream inputChoice = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                        DataInputStream inputUser = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                        DataInputStream inputNewPass = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                        DataInputStream inputPass = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                        DataOutputStream registerMess = new DataOutputStream(connection.getOutputStream());
                        DataOutputStream loginMess = new DataOutputStream(connection.getOutputStream());
                        FileWriter writer;

                        //Login, register or exit choice
                        String choice2 = inputChoice.readUTF();
                        if (choice2.equals("register")) {
                            String username = inputUser.readUTF();
                            File userNameFile = new File(username + ".txt");
                            boolean result;
                            try {
                                result = userNameFile.createNewFile();
                                if (result) {
                                    System.out.println("User> Created username '" + username + "'");
                                    //Creating .txt file for user and save password into the
                                    String newPassword = inputNewPass.readUTF();   ////Something is wrong here

                                    System.out.println("User> Created password '" + newPassword + "'");
//                            FileWriter writer;
                                    writer = new FileWriter(userNameFile);
                                    writer.write(newPassword);        //Writing password into the username file
                                    writer.write("\r\n");   // write new line
                                    System.out.println("User " + username.toUpperCase() + "'s password is successfully saved!");
                                    //Closing streams
                                } else {
                                    registerMess.writeUTF("Server> '" + username.toUpperCase() + "' is already exist. Please choose another username.");
                                    System.out.println("User> '" + username.toUpperCase() + "' is already exist.");
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                System.out.println("Please start running the client server again!");

                            }
                        } else if (choice2.equals("login")) {
                            //User login loop
                            String username = inputUser.readUTF();
                            System.out.println("User> Entered username '" + username + "'");
                            String password = inputPass.readUTF();
                            System.out.println("User> Entered password '" + password + "'");
                            boolean flag = false;
                            //Reading the contents of the file
                            try {
                                Scanner scanPass = new Scanner(new FileInputStream(username + ".txt"));
                                while (scanPass.hasNextLine()) {
                                    String line = scanPass.nextLine();
                                    if (line.indexOf(password) != -1) {
                                        flag = true;
                                    } else {
                                        loginMess.writeUTF("Server> Log in error! Please check your username and password again.");
                                        System.out.println("Log in error!");
                                    }
                                }
                                if (flag) {
                                    loginMess.writeUTF("Server> ~~Welcome back " + username.toUpperCase() + "! You have logged in successfully. \n Game will start soon...");
                                    System.out.println("Woo hoo! User '" + username.toUpperCase() + "' logged in successfully.");
                                }

                            } catch (IOException e) {
                                System.out.println("User not found! Please register.");
                                loginMess.writeUTF("Server> Username " + username.toUpperCase() + " is not found! Please register!");
                            }
                        }




                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while (true);
            }
        }
    }
}