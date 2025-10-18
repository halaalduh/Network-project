/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;
import java.util.ArrayList;

public class Reservation {
    private final Library library;

    // Users data
    private final ArrayList<String> users = new ArrayList<>();
    private final ArrayList<String> passwords = new ArrayList<>();

    // Reservation data
    private final ArrayList<String> resIds = new ArrayList<>();
    private final ArrayList<String> resUsers = new ArrayList<>();
    private final ArrayList<String> resSlots = new ArrayList<>();

    // Counter for generating reservation IDs
    private int reservationCount = 0;

    public Reservation(Library library) {
        this.library = library;
    }

    // Register a new user
    public String register(String user, String pass) {
        if (user == null || pass == null || user.isEmpty() || pass.isEmpty())
            return "ERROR: Username or password cannot be empty";
        if (users.contains(user))
            return "ERROR: User already exists";
        users.add(user);
        passwords.add(pass);
        return "SUCCESS: User registered";
    }

    // Login
    public String login(String user, String pass) {
        int idx = users.indexOf(user);
        if (idx == -1 || !passwords.get(idx).equals(pass))
            return "ERROR: Invalid username or password";
        return "SUCCESS: Logged in";
    }

    // Reserve a book
    public String reserveBook(String user, String lib, String topic, String book) {
        String slot = library.makeSlotId(lib, topic, book);
        if (resSlots.contains(slot))
            return "ERROR: Book is not available";

        // Generate simple ID for reservation
        reservationCount++;
        String id = "RES" + reservationCount;

        resIds.add(id);
        resUsers.add(user);
        resSlots.add(slot);
        return "SUCCESS: Reservation confirmed with ID " + id;
    }

    // Show user's reservations
    public String myRes(String user) {
        StringBuilder sb = new StringBuilder("YOUR RESERVATIONS: ");
        boolean hasAny = false;
        for (int i = 0; i < resUsers.size(); i++) {
            if (resUsers.get(i).equals(user)) {
                sb.append(resIds.get(i)).append(" @ ").append(resSlots.get(i)).append(", ");
                hasAny = true;
            }
        }
        if (!hasAny) return "YOU HAVE NO RESERVATIONS";
        // Remove the last comma and space
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    // Cancel a reservation
    public String cancel(String id, String user) {
        for (int i = 0; i < resIds.size(); i++) {
            if (resIds.get(i).equals(id) && resUsers.get(i).equals(user)) {
                resIds.remove(i);
                resUsers.remove(i);
                resSlots.remove(i);
                return "SUCCESS: Reservation cancelled";
            }
        }
        return "ERROR: Reservation not found";
    }
}
