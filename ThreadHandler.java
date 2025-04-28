package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

class Thread_Handler {
    Thread_Handler() {
    }

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(1234); //declare server socket
        com.company.Db db = new com.company.Db();

        while(true) {
            while(true) { //while loop to give a thread and a socket to each player that connects
                try {
                    System.out.println("Waiting for client connection ...");
                    Socket connection = ss.accept();
                    System.out.println("Client connected");
                    String ID = UUID.randomUUID().toString(); //generates a random ID for each player that connects
                    com.company.Player player = new com.company.Player(connection, ID); //declare a player object for each players that joins
                    DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                    DataOutputStream output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
                    Thread t = new ClientHandler(connection, input, output, db, player);
                    t.start();
                } catch (Exception var9) {
                    ss.close();
                    System.out.println("The server encountered an error");
                }
            }
        }
    }
}
