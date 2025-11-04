/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package networkhala;


/**
 * import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

 *
 * @author renad
 */
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Set;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Map<String, String> users;
    private final Library catalog;
    private final Reservation store;
    private BufferedReader in;// يقرا من الكلاينت 
    private PrintWriter out;// يرسل للكلاينت 
    private String email;

    public ClientHandler(Socket socket, Map<String, String> users, Library catalog, Reservation store) { // يستقبل الفاربل من السيرفير 
        this.socket = socket;
        this.users = users;
        this.catalog = catalog;
        this.store = store;
    }

    @Override
    public void run() { // تشغل الكلاس في الثريد 
        try {
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));// يقراها من الكلاينت 
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);// يرسل الى الكلاينت 
            logMessage("Connected.");
            String line;
            while ((line = in.readLine()) != null) { // مادام الاتصال مع الكلاينت يشتغل 
                String msg = normalizeText(line);  
                if (msg.length() == 0) continue;
                logMessage("REQ  :" + msg);
                handleRequest(msg);// تتعامل مع الكوماند الي جاي من الكلاينت 
            }
        } catch (IOException e) {
            logMessage("IO error: " + e.getMessage());
        } finally {
            closeClientConnection();
        }
    }

    private void handleRequest(String msg) { // تاخذ الكوماند من الكلاينت 
        try {
            if (msg.startsWith("REGISTER:")) {
                handleRegister(msg);
            } else if (msg.equals("LIBRARIES")) {
                handleLibraries();
            } else if (msg.startsWith("TOPICS:")) {
                handleTopics(msg);
            } else if (msg.startsWith("BOOKS:")) {
                handleBooks(msg);
            } else if (msg.startsWith("RESERVE:")) {
                handleReserve(msg);
            } else if (msg.equals("PING")) {// اختبار الاتصال 
                sendResponse("PONG");
            } else {
                sendResponse("ERR:Unknown command");
            }
        } catch (Exception e) {
            sendResponse("ERR:" + e.getMessage());
        }
    }

    private void handleRegister(String msg) {
        String[] p = msg.split(":", 3); // يقسم الرساله الي جايه من الكلاينت
        if (p.length != 3) { sendResponse("ERR:Use REGISTER:email:password"); return; }
        String mail = normalizeText(p[1]);
        String pass = normalizeText(p[2]);
        if (mail.length() == 0 || pass.length() == 0) { sendResponse("ERR:Empty email/password"); return; }
        synchronized (users) {
            if (users.containsKey(mail)) { sendResponse("ERR:Email exists"); return; }
            users.put(mail, pass);
        }
        this.email = mail;
        sendResponse("OK"); // هنا يرسل رساله تاكيد للكلاينت 
    }

    private void handleLibraries() {
        Set<String> libs = catalog.getLibraries();
        sendResponse("LIBRARIES:" + joinWithComma(libs));
    }

    private void handleTopics(String msg) {
        String[] p = msg.split(":", 2);
        if (p.length != 2) { sendResponse("ERR:Use TOPICS:LibraryName"); return; }
        String lib = p[1].trim();
        Set<String> topics = catalog.getTopics(lib);
        if (topics == null || topics.isEmpty()) { sendResponse("ERR:Library not found or no topics"); return; }
        sendResponse("TOPICS:" + lib + ":" + joinWithComma(topics));
    }

    private void handleBooks(String msg) {
        String[] p = msg.split(":", 3);
        if (p.length != 3) { sendResponse("ERR:Use BOOKS:Library:Topic"); return; }
        String lib = p[1].trim();
        String topic = p[2].trim();
        List<String> list = catalog.getAvailableBooks(lib, topic);
        if (list == null || list.isEmpty()) {
            sendResponse("BOOKS:" + lib + ":" + topic + ":NO_BOOKS");
        } else {
            sendResponse("BOOKS:" + lib + ":" + topic + ":" + joinListWithComma(list));
        }
    }

    private void handleReserve(String msg) {
        String[] p = msg.split(":", 4);
        if (p.length != 4) { sendResponse("ERR:Use RESERVE:Library:Topic:Book"); return; }
        String lib = p[1].trim();
        String topic = p[2].trim();
        String book = p[3].trim();
        String who = (email == null ? "anonymous" : email);
        String result = store.reserveBook(who, lib, topic, book);
        if (result.startsWith("SUCCESS")) {
            sendResponse("RESERVED");
        } else if (result.indexOf("already reserved") >= 0) {
            sendResponse("NOT_AVAILABLE");
        } else {
            sendResponse("ERR:" + result.replace("ERROR:", ""));
        }
    }

    private void sendResponse(String s) {
        out.println(s);
        logMessage("RESP -> " + s);
    }

    private void closeClientConnection() {
        logMessage("Disconnected.");
        try { if (in != null) in.close(); } catch (IOException ignore) {}
        try { if (out != null) out.close(); } catch (Exception ignore) {}
        try { socket.close(); } catch (IOException ignore) {}
    }

    private String normalizeText(String s) {
        if (s == null) return "";
        return s.trim().replaceAll("\\s+", " ");
    }

    private String joinWithComma(Set<String> set) {
        if (set == null || set.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String x : set) {
            if (sb.length() > 0) sb.append(",");
            sb.append(x.trim());
        }
        return sb.toString();
    }

    private String joinListWithComma(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String x : list) {
            if (sb.length() > 0) sb.append(",");
            sb.append(x.trim());
        }
        return sb.toString();
    }

    private void logMessage(String m) {
        System.out.println("[Client-" + socket.getPort() + "] " + m);
    }
}

    

