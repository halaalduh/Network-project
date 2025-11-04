/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package networkhala;

/**
 *
 * @author renad
 */
import java.util.*;

public class Reservation {
    private final Library catalog; // عشان نعرف هل الكتاب محجوز او لا 
    private final Map<String, String> reservations = new HashMap<>(); // تربط الكتاب باليوزر الي استخدمه 

    public Reservation(Library catalog) {
        this.catalog = catalog;
    }

    public synchronized String reserveBook(String user, String library, String topic, String book) { // هذي الي تخلي يوزر واحد يحجز الكتاب 
        if (isBlankString(user) || isBlankString(library) || isBlankString(topic) || isBlankString(book)) {
            return "ERROR:Bad args";
        }

        Map<String, Map<String, Boolean>> lib = catalog.getRawData().get(library);
        if (lib == null) return "ERROR:Library not found";
        Map<String, Boolean> topicBooks = lib.get(topic);
        if (topicBooks == null) return "ERROR:Topic not found";
        Boolean available = topicBooks.get(book); // يتاكد هل موجود او لا 
        if (available == null) return "ERROR:Book not found";

        String key = library + ":" + topic + ":" + book;
        String owner = reservations.get(key);
        
        if (owner != null && !owner.equals(user)) return "ERROR:Book already reserved";
        if (!Boolean.TRUE.equals(available)) return "ERROR:Book already reserved";

        topicBooks.put(book, Boolean.FALSE);  // يغير حاله الكتاب في الايبرري
        reservations.put(key, user);

        return "SUCCESS:Book '" + book + "' reserved from " + library + " (" + topic + ")";
    }

    private boolean isBlankString(String s) {
        return s == null || s.trim().isEmpty();
    }
}
