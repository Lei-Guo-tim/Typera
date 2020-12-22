package com.company;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class clientTask implements Runnable{
    int port = 7913;
    String ip = "127.0.0.1";
    private Socket connection;  //Create Socket
    /**
     * define the transport address (network address IP + Port number) */

    public String clientRequest = "";
    public clientTask(String _clientRequest) {
        clientRequest = _clientRequest;}
        @Override
    public void run() {
            try{
                // Connect with server
                Socket connection = new Socket(ip, port);
                System.out.println("Connected! " + connection);


                // Setting up input and output streams
                DataOutputStream outputChoice = new DataOutputStream(connection.getOutputStream());
                DataOutputStream outputName = new DataOutputStream(connection.getOutputStream());
                DataOutputStream outputPass = new DataOutputStream(connection.getOutputStream());
                DataOutputStream outputNewPass = new DataOutputStream(connection.getOutputStream());
                DataInputStream inputStr = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                DataInputStream registerMess = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                DataInputStream loginMess = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                //Login
                if(clientRequest.equals("register")) {
                    Scanner inputName = new Scanner(System.in);
                    System.out.println("Please enter your username: ");
                    String username = inputName.next();
                    Scanner inputPass = new Scanner(System.in);
                    System.out.println("Please enter your password: ");
                    String newPassword = inputPass.next();

                    // Start communication
                    outputChoice.writeUTF(clientRequest); //Writing register, login, exit choice
                    outputChoice.flush();
                    outputName.writeUTF(username); //Writing username
                    outputName.flush();
                    //Writing password
                    outputNewPass.writeUTF(newPassword);
                    outputNewPass.flush();
                    //User already exists exist message
                    String regMess = registerMess.readUTF();
                    System.out.println(regMess);


                } if (clientRequest.equals("login")) {
                    Scanner inputLog = new Scanner(System.in);
                    System.out.println("Please enter your username: ");
                    String username = inputLog.next();
                    System.out.println("Please enter your password: ");
                    String password = inputLog.next();
                    // Start communication
                    outputChoice.writeUTF(clientRequest); //Writing register, login, exit choice
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
                outputNewPass.close();
                outputName.close();
                inputStr.close();
                connection.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }



