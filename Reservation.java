/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;
public class Reservation {
    private final Library library;

    // ===== المستخدمين =====
    private final String[] usernames = new String[100];
    private final String[] passwords = new String[100];
    private int userCount = 0;

    // ===== الحجوزات =====
    private final String[] reservationIds = new String[200];
    private final String[] reservationUsers = new String[200];
    private final String[] reservationSlots = new String[200];
    private int reservationCount = 0;

    public Reservation(Library library) {
        this.library = library;
    }

    // ===== تسجيل مستخدم جديد =====
    public String register(String user, String pass) {
        if (isBlank(user) || isBlank(pass)) return "ERR|BAD_ARGS";
        if (findUserIndex(user) != -1) return "ERR|USER_EXISTS";

        usernames[userCount] = user;
        passwords[userCount] = pass;
        userCount++;
        return "OK|REGISTERED";
    }

    // ===== تسجيل الدخول =====
    public String login(String user, String pass) {
        int i = findUserIndex(user);
        if (i == -1 || !passwords[i].equals(pass)) return "ERR|INVALID_CREDENTIALS";
        return "OK|LOGGED_IN";
    }

    // ===== حجز كتاب =====
    public String reserveBook(String user, int libIndex, int topicIndex, int bookIndex) {
        String slot = library.makeSlotId(libIndex, topicIndex, bookIndex);

        // التحقق من وجود الكتاب أصلاً
        if (libIndex < 0 || topicIndex < 0 || bookIndex < 0) return "ERR|BAD_INDEX";

        // التحقق إن الكتاب غير محجوز مسبقًا
        for (int i = 0; i < reservationCount; i++) {
            if (reservationSlots[i].equals(slot)) return "ERR|NOT_AVAILABLE";
        }

        // إنشاء رقم حجز بسيط
        String id = "RES" + (reservationCount + 1);

        // حفظ بيانات الحجز
// حفظ بيانات الحجز
        reservationIds[reservationCount] = id;
        reservationUsers[reservationCount] = user;
        reservationSlots[reservationCount] = slot;
        reservationCount++;

        return "OK|RESERVED|" + id;
    }

    // ===== عرض حجوزات المستخدم =====
    public String myReservations(String user) {
        StringBuilder sb = new StringBuilder("OK|MYRES|");
        boolean found = false;

        for (int i = 0; i < reservationCount; i++) {
            if (reservationUsers[i].equals(user)) {
                sb.append(reservationIds[i]).append("@").append(reservationSlots[i]).append(",");
                found = true;
            }
        }

        if (!found) return "OK|MYRES|";
        return sb.toString();
    }

    // ===== إلغاء الحجز =====
    public String cancelReservation(String user, String id) {
        for (int i = 0; i < reservationCount; i++) {
            if (reservationIds[i].equals(id) && reservationUsers[i].equals(user)) {
                // نحذف الحجز بتحريك العناصر بعده
                for (int j = i; j < reservationCount - 1; j++) {
                    reservationIds[j] = reservationIds[j + 1];
                    reservationUsers[j] = reservationUsers[j + 1];
                    reservationSlots[j] = reservationSlots[j + 1];
                }
                reservationCount--;
                return "OK|CANCELLED";
            }
        }
        return "ERR|NOT_FOUND";
    }

    // ===== دوال مساعدة =====
    private int findUserIndex(String user) {
        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equals(user)) return i;
        }
        return -1;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}