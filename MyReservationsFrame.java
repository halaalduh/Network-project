/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package networproject;

/**
 *
 * @author renad
 */

    

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MyReservationsFrame extends JFrame {
    private final client connection;
    private final String username;
    private final DefaultListModel<String> reservationListModel;
    private final JList<String> reservationList;
    private final JButton refreshButton;
    private final JButton cancelButton;
    private final JButton backButton;

    public MyReservationsFrame(client connection, String username) {
        this.connection = connection;
        this.username = username;

        setTitle("My Reservations - " + username);
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("My Reservations", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        reservationListModel = new DefaultListModel<>();
        reservationList = new JList<>(reservationListModel);
        JScrollPane scrollPane = new JScrollPane(reservationList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Current Reservations"));

        refreshButton = new JButton("Refresh");
        cancelButton = new JButton("Cancel Selected");
        backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);

        setLayout(new BorderLayout(10, 10));
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // الأحداث
        refreshButton.addActionListener(e -> loadReservations());
        cancelButton.addActionListener(e -> cancelReservation());
        backButton.addActionListener(e -> goBack());

        // أول تشغيل
        loadReservations();
    }

    private void loadReservations() {
        try {
            // نرسل اسم المستخدم للسيرفر عشان يرجع حجوزاته
            connection.send(username);
            String response = connection.send("MYRES");

            reservationListModel.clear();
            if (response == null || response.isEmpty() || response.contains("NO RESERVATIONS")) {
                reservationListModel.addElement("You have no reservations.");
            } else {
                for (String item : response.split(",")) {
                    reservationListModel.addElement(item.trim());
                }
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + ex.getMessage());
        }
    }

    private void cancelReservation() {
        String selected = reservationList.getSelectedValue();
        if (selected == null || selected.startsWith("You have no")) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.");
            return;
        }

        try {
            String id = selected.split(" ")[0]; // نأخذ رقم الحجز (RES1 مثلاً)
            connection.send("CANCEL");
            connection.send(id);
            connection.send(username);
            JOptionPane.showMessageDialog(this, "Reservation cancelled successfully!");
            loadReservations();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error cancelling reservation: " + ex.getMessage());
        }
    }

    private void goBack() {
        dispose();
        new ReservationFrame(connection, username).setVisible(true);
    }
}


