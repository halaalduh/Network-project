/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package networkhala;

/**
 *
 * @author renad
 */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class RegisterFrame extends JFrame {
    private JTextField userField; // يوزر نيم 
    private JPasswordField passField;//الباسورد 
    private JButton registerBtn; // ينقله للفريم الثاني 
    private static final String HOST = "172.20.10.4"; 
    private static final int PORT = 9090;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { new RegisterFrame().setVisible(true); } // يعرض الالشكل 
        });
    }

    public RegisterFrame() { // التسجيل 
        setTitle("Register"); // عنوان الصفحه 
        setSize(420, 220);// حجمها 
        setLocationRelativeTo(null); // تصير بالوسط 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // يتسكر لما اضغط على الاكس 
        
        JLabel title = new JLabel("Create a new account", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        userField = new JTextField(18); // مربع الايميل 
        passField = new JPasswordField(18); // مربع الباسوورد 
        registerBtn = new JButton("Register & Continue"); // بوتم التسجيل 
        JPanel grid = new JPanel(new GridLayout(2, 2, 8, 8)); 
        
        grid.add(new JLabel("Username (email):"));
        grid.add(userField);
        grid.add(new JLabel("Password:"));
        grid.add(passField);
        
        JPanel root = new JPanel(new BorderLayout(10, 10)); // هنا يقسم الصفحه ويرتبها
        root.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // بادنق 
        
        root.add(title, BorderLayout.NORTH); // يحدد اماكنهم 
        root.add(grid, BorderLayout.CENTER);
        root.add(registerBtn, BorderLayout.SOUTH);
        
        
        setContentPane(root); 
        registerBtn.addActionListener(e -> handleRegisterButton());
        
    }

    private void handleRegisterButton() {// الشي الي يصير عند ضغط البوتن
        
        String user = userField.getText().trim(); // تقرا من اليوزر 
        String pass = new String(passField.getPassword()).trim(); 
        if (user.length() == 0 || pass.length() == 0) { // يتاكد انه الخانات متعبيه 
            
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        
        Connection conn = null; // ربط الفريم ب السيرفير 
        
        try {
            conn = new Connection(HOST, PORT); // كونكشن بالسيرفير 
            String resp = conn.sendRequest("REGISTER:" + user + ":" + pass); // يرسل الكوماند للسيرفير 
            
            if ("OK".equals(resp)) { // اذا رجع السيرفير اوك يفتح له 
                JOptionPane.showMessageDialog(this, "Registered ✓");
                openReservationWindow(conn, user);
                return;
                
            } else if ("ERR:Email exists".equals(resp)) {// اذا صار مسجل مسبقا
                conn.closeConnection();
                JOptionPane.showMessageDialog(this, "Username exists. Choose another.");
            } 
            
            else { // اي شي ثاني جاي من السيرفير 
                conn.closeConnection();
                JOptionPane.showMessageDialog(this, resp);
            }
        } catch (IOException ex) { // اذا فشل التصال بالسيرفير
            if (conn != null) conn.closeConnection();
            JOptionPane.showMessageDialog(this, "Cannot connect to server.");
        }
    }

    private void openReservationWindow(Connection conn, String username) { // فتح خانه الحجز 
        dispose();
        ReservationFrame rf = new ReservationFrame(conn, username);
        rf.setVisible(true);
    }
}
