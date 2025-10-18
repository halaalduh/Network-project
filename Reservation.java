/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Reservation {
    private final Library library;

    // ===== users
    private final Map<String, String> users = new ConcurrentHashMap<>(); // user -> pass

    // ===== reservations
    static class Res {
        final String id, user, slot; // slot = "Library | Topic | Book"
        Res(String id, String user, String slot) { 
            this.id = id; 
            this.user = user; 
            this.slot = slot; 
        }
    }

    private final Map<String, Res> byId = new ConcurrentHashMap<>();
    private final Map<String, List<Res>> byUser = new ConcurrentHashMap<>();

    public Reservation(Library library) {
        this.library = library;
    }

    // ===== Users =====
    public synchronized String register(String user, String pass) {
        if (isBlank(user) || isBlank(pass)) return "ERR|BAD_ARGS";
        if (users.containsKey(user)) return "ERR|USER_EXISTS";
        users.put(user, pass);
        return "OK|REGISTERED";
    }

    public synchronized String login(String user, String pass) {
        String p = users.get(user);
        return (p != null && p.equals(pass)) ? "OK|LOGGED_IN" : "ERR|INVALID_CREDENTIALS";
    }

    // ===== Library browsing =====
    public synchronized String listLibraries() {
        return "OK|LIBS|" + String.join(",", library.getLibraries());
    }

    public synchronized String listTopics(String lib) {
        Set<String> topics = library.getTopics(lib);
        return topics.isEmpty() ? "OK|TOPICS|" : "OK|TOPICS|" + String.join(",", topics);
    }

    public synchronized String listBooks(String lib, String topic) {
        List<String> books = library.getBooks(lib, topic);
        return books.isEmpty() ? "OK|BOOKS|" : "OK|BOOKS|" + String.join(",", books);
    }

    // ===== Reserve / MyRes / Cancel =====
    public synchronized String reserveBook(String user, String lib, String topic, String book) {
        if (!library.getLibraries().contains(lib) ||
            !library.getTopics(lib).contains(topic) ||
            !library.getBooks(lib, topic).contains(book)) {
            return "ERR|UNKNOWN_BOOK";
        }

        // user reservation limit (optional)
        List<Res> mine = byUser.getOrDefault(user, Collections.emptyList());
        if (mine.size() >= 2) return "ERR|LIMIT";

        // Attempt reservation
        boolean success = library.reserveBook(lib, topic, book);
        if (!success) return "ERR|NOT_AVAILABLE";

        String slot = library.makeSlotId(lib, topic, book);
        String id = UUID.randomUUID().toString();
        Res r = new Res(id, user, slot);

        byId.put(id, r);
        byUser.computeIfAbsent(user, k -> new ArrayList<>()).add(r);
        return "OK|RESERVED|" + id;
    }

    public synchronized String myRes(String user) {
        List<Res> mine = byUser.getOrDefault(user, Collections.emptyList());
        if (mine.isEmpty()) return "OK|MYRES|";
        String payload = String.join(",", mine.stream().map(x -> x.id + "@" + x.slot).toArray(String[]::new));
        return "OK|MYRES|" + payload;
    }

    public synchronized String cancel(String id, String user) {
        Res r = byId.get(id);
        if (r == null || !r.user.equals(user)) return "ERR|NOT_FOUND";

        byId.remove(id);
        byUser.getOrDefault(user, new ArrayList<>()).removeIf(x -> x.id.equals(id));
        library.freeBook(r.slot);
        return "OK|CANCELLED";
    }

    private boolean isBlank(String s) { 
        return s == null || s.trim().isEmpty(); 
    }
}
