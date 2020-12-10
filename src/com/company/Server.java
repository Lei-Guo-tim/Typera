package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("To start to the server please enter 'start'       To stop the server please enter 'stop'");
        Scanner choice1 = new Scanner(System.in);
        String input;
        input = choice1.nextLine();

        if (input.equals("start")) {
            System.out.println("~~Welcome to the Type Racing Game Server~~");

            do {
                //Register service in the port 7913 and wait for connections
                ServerSocket server = new ServerSocket(7913);
                System.out.println("waiting for connections");
                Socket connection = server.accept();
                System.out.println("User> connected " + connection);
                //Setting up input output streams
                DataInputStream inputPass = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                DataInputStream inputUser = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                DataOutputStream outputStr = new DataOutputStream(connection.getOutputStream());
                DataInputStream inputChoice = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                DataOutputStream registerMess = new DataOutputStream(connection.getOutputStream());
                DataOutputStream loginMess = new DataOutputStream(connection.getOutputStream());

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
                            String password = inputPass.readUTF();
                            System.out.println("User> Created password '" + password + "'");
                            FileWriter writer;
                            writer = new FileWriter(userNameFile);
                            writer.write(password);        //Writing password into the username file
                            writer.write("\r\n");   // write new line
                            System.out.println("User " + username.toUpperCase() +"'s password is successfully saved!");
                            //Closing streams
                            writer.close();
                        } else {
                            registerMess.writeUTF("Server> '" + username.toUpperCase() + "' is already exist. Please choose another username.");
                            System.out.println("User> '" + username.toUpperCase() + "' is already exist.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

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
                        Scanner scanPass = new Scanner(new FileInputStream( username + ".txt"));
                        while (scanPass.hasNextLine()) {
                            String line = scanPass.nextLine();
                            if (line.contains(password)) {
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

                    }catch (IOException e) {
                        System.out.println("User not found! Please register.");
                        loginMess.writeUTF("Server> Username " + username.toUpperCase() + " is not found! Please register!");
                    }



                }

                // Close streams and socket
                registerMess.close();
                inputChoice.close();
                inputUser.close();
                inputPass.close();
                outputStr.close();
                loginMess.close();
                connection.close();
                server.close();
            } while(true);


        }

        if (input.equals("stop"))
            System.out.println("You pressed 'stop'. Goodbye. ");
    }
}

