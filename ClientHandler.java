/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplicationn;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
/**
 *
 * @author deems
 */
public class ClientHandler implements Runnable{
    private final Socket socket;
    private final Reservation reservation; // الكلاس المسؤول عن البيانات
    private String username;
    

    public ClientHandler(Socket socket, Reservation reservation) {
        this.socket = socket;
        this.reservation = reservation;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // هنا تبدأ مراحل التفاعل (الـ GUI ممكن تستدعي كل وحدة لحال)
            handleRegistration(in, out);
            handleLibraries(in, out);
            handleTopic(in, out);
            handleBooks(in, out);
            handleReservation(in, out);

            out.println("Thank you, " + username + "! Connection closed.");
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🟢 1. تسجيل المستخدم
    private void handleRegistration(BufferedReader in, PrintWriter out) throws IOException {
        System.out.println("Enter your name:");
        username = in.readLine();
        System.out.println("Welcome, " + username + "!");
    }

    // 🟢 2. عرض المكتبات
    private void handleLibraries(BufferedReader in, PrintWriter out) throws IOException {
        ArrayList<String> libraries = reservation.getLibraries();
        System.out.println("Available Libraries:");
        for (int i = 0; i < libraries.size(); i++) {
            System.out.println((i + 1) + ". " + libraries.get(i));
        }
    }

    // 🟢 3. اختيار الموضوع
    private void handleTopic(BufferedReader in, PrintWriter out) throws IOException {
        System.out.println("Enter library number:");
        int libNum = Integer.parseInt(in.readLine()) - 1;
        ArrayList<String> topics = reservation.getTopics(libNum);

        out.println("Available Topics:");
        for (int i = 0; i < topics.size(); i++) {
            System.out.println((i + 1) + ". " + topics.get(i));
        }
    }

    // 🟢 4. عرض الكتب
    private void handleBooks(BufferedReader in, PrintWriter out) throws IOException {
        System.out.println("Enter library number:");
        int libNum = Integer.parseInt(in.readLine()) - 1;
        System.out.println("Enter topic number:");
        int topicNum = Integer.parseInt(in.readLine()) - 1;

        ArrayList<String> books = reservation.getBooks(libNum, topicNum);
        System.out.println("Books:");
        for (int i = 0; i < books.size(); i++) {
            out.println((i + 1) + ". " + books.get(i));
        }
    }

    // 🟢 5. تنفيذ الحجز
    private void handleReservation(BufferedReader in, PrintWriter out) throws IOException {
        System.out.println("Enter library number:");
        int libNum = Integer.parseInt(in.readLine()) - 1;
        System.out.println("Enter topic number:");
        int topicNum = Integer.parseInt(in.readLine()) - 1;
        System.out.println("Enter book number:");
        int bookNum = Integer.parseInt(in.readLine()) - 1;

        String result = reservation.reserveBook(libNum, topicNum, bookNum, username);
        System.out.println(result);
    }
    private void handleCancel(BufferedReader in, PrintWriter out) throws IOException {
    System.out.println("Enter library number:");
    int libNum = Integer.parseInt(in.readLine()) - 1;

    System.out.println("Enter topic number:");
    int topicNum = Integer.parseInt(in.readLine()) - 1;

    System.out.println("Enter book name to cancel:");
    String bookName = in.readLine();

    String result = reservation.cancelBook(libNum, topicNum, bookName, username);
    System.out.println(result);
}
     
}
