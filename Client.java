package project5;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mesldees
 */

 import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    // this method connects the client to the server using host and port
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        System.out.println("Connected to server " + host + ":" + port);
    }

    // this method sends a message to the server and waits for the reply
    public String send(String line) throws IOException {
        pw.println(line); // send data to server
        return br.readLine(); // read server response
    }

    // this method closes the connection
    public void close() throws IOException {
        if (socket != null) socket.close();
    }
}



