/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package networkhala;

/**
 *
 * @author renad
 */

import java.io.*;
import java.net.Socket;

public class Connection {
    private Socket socket; // للاتصال بالسيرفير 
    private BufferedReader in;// يقرا من السيرفر 
    private PrintWriter out;// يرسل للسيرفر 

    public Connection(String host, int port) throws IOException {
        socket = new Socket(host, port); // يسوي سوكت تربط الكلاينت بالسيرفر 
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream())); //ياخذ البيانات من السيرفر 
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);// يرسل للسيرفر 
    }

    public String sendRequest(String line) throws IOException {
        out.println(line); // ترسل الكوماند للسيرفر 
        return in.readLine();// يرجع رد السيرفر 
    }

    public void closeConnection() { 
        try { if (in  != null) in.close();  } catch (Exception ignore) {}
        try { if (out != null) out.close(); } catch (Exception ignore) {}
        try { if (socket != null) socket.close(); } catch (Exception ignore) {}
    }
}








