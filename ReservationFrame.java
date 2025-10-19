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
import java.util.ArrayList;

public class ReservationFrame extends JFrame {
    private final client connection;
    private final String username;

    private JComboBox<String> libraryCombo;
    private JComboBox<String> topicCombo;
    private JList<String> bookList;
    private DefaultListModel<String> bookListModel;
    private JButton showTopicsButton, showBooksButton, reserveButton, myResButton, logoutButton;

    public ReservationFrame(client connection, String username) {
        this.connection = connection;
        this.username = username;

        setTitle("Library Reservation - " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Book Reservation System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // مكتبات - مواضيع - كتب
        libraryCombo = new JComboBox<>(new String[]{"Main Library", "Engineering Library", "Science Library"});
        topicCombo = new JComboBox<>();
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);

        showTopicsButton = new JButton("Show Topics");
        showBooksButton = new JButton("Show Books");
        reserveButton = new JButton("Reserve");
        myResButton = new JButton("My Reservations");
        logoutButton = new JButton("Logout");

        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        topPanel.add(new JLabel("Select Library:"));
        topPanel.add(libraryCombo);
        topPanel.add(new JLabel("Select Topic:"));
        topPanel.add(topicCombo);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(showTopicsButton);
        buttonPanel.add(showBooksButton);
        buttonPanel.add(reserveButton);
        buttonPanel.add(myResButton);
        buttonPanel.add(logoutButton);

        JScrollPane bookScroll = new JScrollPane(bookList);
        bookScroll.setBorder(BorderFactory.createTitledBorder("Available Books"));

        setLayout(new BorderLayout(10, 10));
        add(title, BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(bookScroll, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

        // الأحداث
        showTopicsButton.addActionListener(e -> loadTopics());
        showBooksButton.addActionListener(e -> loadBooks());
        reserveButton.addActionListener(e -> reserveBook());
        myResButton.addActionListener(e -> openMyReservations());
        logoutButton.addActionListener(e -> logout());
    }

    private void loadTopics() {
        try {
            int libIndex = libraryCombo.getSelectedIndex();
            connection.send(String.valueOf(libIndex + 1)); // ClientHandler expects library number
            JOptionPane.showMessageDialog(this, "Topics loaded for " + libraryCombo.getSelectedItem());
            topicCombo.removeAllItems();

            if (libIndex == 0)
                topicCombo.addItem("Computer Science");
            else if (libIndex == 1)
                topicCombo.addItem("Engineering");
            else
                topicCombo.addItem("Biology");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading topics: " + ex.getMessage());
        }
    }

    private void loadBooks() {
        try {
            int libIndex = libraryCombo.getSelectedIndex();
            int topicIndex = topicCombo.getSelectedIndex();
            connection.send(String.valueOf(libIndex + 1));
            connection.send(String.valueOf(topicIndex + 1));
            JOptionPane.showMessageDialog(this, "Books loaded for topic " + topicCombo.getSelectedItem());

            bookListModel.clear();
            if (libIndex == 0)
                bookListModel.addElement("Intro to Java");
            else if (libIndex == 1)
                bookListModel.addElement("Software Eng");
            else
                bookListModel.addElement("Genetics");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + ex.getMessage());
        }
    }

    private void reserveBook() {
        try {
            int libIndex = libraryCombo.getSelectedIndex();
            int topicIndex = topicCombo.getSelectedIndex();
            int bookIndex = bookList.getSelectedIndex();

            if (bookIndex == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to reserve.");
                return;
            }

            connection.send(String.valueOf(libIndex + 1));
            connection.send(String.valueOf(topicIndex + 1));
            connection.send(String.valueOf(bookIndex + 1));

            JOptionPane.showMessageDialog(this, "Book reserved successfully!");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reserving book: " + ex.getMessage());
        }
    }

    private void openMyReservations() {
        dispose();
        new MyReservationsFrame(connection, username).setVisible(true);
    }

    private void logout() {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dispose();
        new LoginFrame().setVisible(true);
    }
}
