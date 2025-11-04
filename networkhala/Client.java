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
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;// يقرا البيانات الي جايه من السيرفير 
    private PrintWriter out;// يرسل البيانات الى السيرفير 
    private final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) { new Client().startProgram(); }// هنا عشان يشتغل النظام 

    // هذي الميثود هي الي تشغل الكلاينت 
    private void startProgram() { 
        System.out.println("=== Library Reservation (Console) ===");

        String host = "172.20.10.4";
        int port = 9090;

        if (!connectToServer(host, port)) return;// هنا الحين اذا مات الاكونكشن مع السيرفير را يطلع من النظام 

        registerUser();// هنا يتم تسجيل الكلاينت عند السيرفير

        while (true) {
            System.out.println("\n1) Libraries  2) Topics  3) Books  4) Reserve  5) Exit");
            System.out.print("Choose: ");
            String ch = sc.nextLine().trim();

            switch (ch) {
                case "1":// يطلب ال ليبرري من السيرفير
                    sendCommandAndPrint("LIBRARIES");
                    break;
                case "2":// يطلب ال توبكز من السيرفير
                    System.out.print("Library: ");
                    String lib2 = sc.nextLine().trim();
                    sendCommandAndPrint("TOPICS:" + lib2);
                    break;
                case "3":// يطلب ال الكتب  من السيرفير
                    System.out.print("Library: ");
                    String lib3 = sc.nextLine().trim();
                    System.out.print("Topic  : ");
                    String top3 = sc.nextLine().trim();
                    sendCommandAndPrint("BOOKS:" + lib3 + ":" + top3);
                    break;
                case "4":// يحجز الكتاب  من السيرفير
                    System.out.print("Library: ");
                    String lib4 = sc.nextLine().trim();
                    System.out.print("Topic  : ");
                    String top4 = sc.nextLine().trim();
                    System.out.print("Book   : ");
                    String bk4 = sc.nextLine().trim();
                    sendCommandAndPrint("RESERVE:" + lib4 + ":" + top4 + ":" + bk4);
                    break;
                case "5": // يسكر الاتصال بالسيرفير 
                    closeConnection();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private boolean connectToServer(String host, int port) { 
        try {
            socket = new Socket(host, port); // يفتح سوكت ويتصل بالسيرفير 
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream())); // تقراء من السيرفير
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);// ترسل للسيرفير 
            System.out.println("Connected to server on " + host + ":" + port); 
            return true;
        } catch (IOException e) {
            System.out.println(" Connection failed: " + e.getMessage());
            return false;
        }
    }

    private void registerUser() {  // تسجيل اليوزر 
        while (true) {
            System.out.print("Username: ");
            String user = sc.nextLine().trim();
            System.out.print("Password: ");
            String pass = sc.nextLine().trim();

            out.println("REGISTER:" + user + ":" + pass);
            String resp = readOneLine(); // ينتظر رد السيرفير 

            if ("OK".equals(resp)) { // اذا نجح التسجيل
                System.out.println("Registered successfully!");
                break;
            } else {
                System.out.println(resp + " — try again.");
            }
        }
    }

    private void sendCommandAndPrint(String line) { // ترسل كوماند للسيرفير 
        out.println(line);
        String resp = readOneLine();
        System.out.println(resp == null ? "(no response)" : resp);
    }

    private String readOneLine() { // تقراء من السيرفير 
        try {
            String s = in.readLine();// تقرا السطر الاول من السيرفير 
            if (s == null) { System.out.println("Disconnected."); closeConnection(); }// اذا صار null معناته انه الاتصال انقطع 
            return s;
        } catch (IOException e) {
            System.out.println("Read error: " + e.getMessage());
            return null;
        }
    }

    private void closeConnection() {
        try { if (socket != null) socket.close(); } catch (IOException ignored) {}  // تسكر الاتصال 
    }
}

    

