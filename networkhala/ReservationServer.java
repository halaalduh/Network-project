/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package networkhala;

/**
 *
 * @author renad
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ReservationServer {
    private static final int PORT = 9090;
    static final Map<String, String> USERS = new HashMap<>();
    static final Library CATALOG = new Library();
    static final Reservation STORE = new Reservation(CATALOG);

    public static void main(String[] args) {
        System.out.println("Library Reservation Server running on port " + PORT);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);// تشغيل السيرفير 
            while (true) {
                System.out.println("Waiting for client connection...");
                Socket socket = serverSocket.accept(); // هنا السيرفير ينتظر كونكشن من الكلاينت 
                System.out.println("[New Connection] " + socket.getInetAddress() + ":" + socket.getPort());// يطبع بيانات اتصال الكلاينت 
                ClientHandler handler = new ClientHandler(socket, USERS, CATALOG, STORE);// هنا السيرفير يتعامل مع كل كلاينت لحاله 
                Thread t = new Thread(handler, "Client-" + socket.getPort());// هنا عندي ثريد لكل كلاينت لحاله عشان يقدر يتعامل مع اكثر من كلاينت 
                t.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            try { if (serverSocket != null) serverSocket.close(); } catch (IOException ignore) {}
        }
    }
}