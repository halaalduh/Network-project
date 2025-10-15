/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project5;

/**
 *
 * @author mesldees
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        final int port = 9090; 
        System.out.println("Server started on port " + port);

        // create Library and Reservation objects (shared between clients)
        Library library = new Library();        
        Reservation reservation = new Reservation(library); 

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // wait for clients forever
            while (true) {
                System.out.println("Waiting for client connection...");
                Socket s = serverSocket.accept(); // accept client connection
                System.out.println("New client connected: " + s.getRemoteSocketAddress());

                // create a thread for each client
                new Thread(new ClientHandler(s, reservation), "Client-" + s.getPort()).start();
            }
        } catch (IOException e) {
            // print error if something goes wrong
            System.err.println("Server error: " + e.getMessage());
        }
    }
}

 

