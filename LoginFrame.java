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

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final client connection;

    public LoginFrame() {
        setTitle("Library Reservation System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // الاتصال بالسيرفر
        connection = new client();
        try {
            connection.connect("localhost", 9090);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to server on port 9090", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // مكونات الواجهة
        JLabel titleLabel = new JLabel("Welcome to Library System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(userLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passLabel);
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        setLayout(new BorderLayout(10, 10));
        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // حدث تسجيل الدخول
        loginButton.addActionListener(e -> handleLogin());
        // حدث التسجيل
        registerButton.addActionListener(e -> handleRegister());
    }

    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            connection.send(user);
            connection.send(pass);
            JOptionPane.showMessageDialog(this, "Logged in successfully!");
            dispose();
            new ReservationFrame(connection, user).setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error during login: " + ex.getMessage());
        }
    }

    private void handleRegister() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            connection.send(user);
            connection.send(pass);
            JOptionPane.showMessageDialog(this, "Registered successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error during registration: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
