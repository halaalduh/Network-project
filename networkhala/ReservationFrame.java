package networkhala;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ReservationFrame extends JFrame {

    private final Connection connection; // يتعامل مع السيرفير
    private final String username;// يخزن اليوزر نيم 
    private JComboBox<String> libraryCombo; // المنيو
    private JComboBox<String> topicCombo; // المنيو     private JComboBox<String> topicCombo; // المنيو 

    private DefaultListModel<String> bookModel;// يخز اسماء الكتب 
    private JList<String> bookList; // الكتب المتاحه 
    private JButton btnTopics, btnBooks, btnReserve, btnLogout; // البوتنز

    public ReservationFrame(Connection connection, String username) {
        this.connection = connection;// تجيه من الفريم الي قبل 
        this.username = username;
        // تنسيق للفريم
        setTitle("Book Reservation");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        buildUI();
        loadLibrariesFromServer();
    }

    private void buildUI() {
        JLabel title = new JLabel("Book Reservation", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        JLabel lblLibrary = new JLabel("Library:");
        lblLibrary.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblTopic = new JLabel("Topic:");
        lblTopic.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblBooks = new JLabel("Available Books");
        lblBooks.setFont(new Font("Segoe UI", Font.BOLD, 15));

        libraryCombo = createStyledComboBox(new String[]{});
        topicCombo = createStyledComboBox(new String[]{});

        bookModel = new DefaultListModel<>();// تخزين اسماء الكتب 

        bookList = new JList<>(bookModel); 
        bookList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane bookScroll = new JScrollPane(bookList);

        bookScroll.setPreferredSize(new Dimension(330, 120));
        bookScroll.setMinimumSize(new Dimension(330, 120));
        bookScroll.setBorder(BorderFactory.createTitledBorder(""));

        btnTopics = createStyledButton("Show Topics");
        btnBooks = createStyledButton("Show Books");
        btnReserve = createStyledButton("Reserve");
        btnLogout = createStyledButton("Logout");

        JPanel panel = new JPanel(new GridBagLayout());//تجمع الفاربل 
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.insets = new Insets(8, 10, 8, 10);
        
        c.anchor = GridBagConstraints.WEST;
        // هنا يحدد المواقع 
        c.gridx = 0;
        c.gridy = 0;
        panel.add(lblLibrary, c);

        c.gridx = 1;
        c.gridy = 0;
        panel.add(libraryCombo, c);

        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(lblBooks, c);

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        panel.add(lblTopic, c);

        c.gridx = 1;
        c.gridy = 1;
        panel.add(topicCombo, c);

        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 2;
        panel.add(bookScroll, c);
        c.gridheight = 1;

        // يحدد المواقع 
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.add(btnTopics);
        buttonsPanel.add(btnBooks);
        buttonsPanel.add(btnReserve);
        buttonsPanel.add(btnLogout);

        // تنسيق للفريم 
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        root.add(title, BorderLayout.NORTH);
        root.add(panel, BorderLayout.CENTER);
        root.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(root);

        btnTopics.addActionListener(e -> loadTopicsForSelectedLibrary());
        btnBooks.addActionListener(e -> loadBooksForSelectedTopic());
        btnReserve.addActionListener(e -> doReserveNow());
        btnLogout.addActionListener(e -> handleLogoutButton());
    }
    
    

    private JComboBox<String> createStyledComboBox(String[] items) { // تنسيق  للكومبوبوكس
        
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setPreferredSize(new Dimension(220, 35));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setFocusable(false);
        combo.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        return combo;
    }

    private JButton createStyledButton(String text) { // تنسيق البوتنز
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(245, 245, 245));
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        btn.setPreferredSize(new Dimension(140, 36));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(230, 230, 230));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(245, 245, 245));
            }
        });
        return btn;
    }

    private void loadLibrariesFromServer() {
        try {
            String resp = connection.sendRequest("LIBRARIES");// يطلب اللايبرريز من السيرفير 
            libraryCombo.removeAllItems();// ينظف الكومبو بكس 
            if (resp != null && resp.startsWith("LIBRARIES:")) { //يتاكد من رد السيرفير
                String body = resp.substring("LIBRARIES:".length()); // يجيب اسم الايبرري بس 
                String[] libs = body.split(",");// يقسمها في الاراي 
                for (String lib : libs) { // يضيف اسماء الايبرري في كومبوبكس 
                    libraryCombo.addItem(lib.trim()); 
                }
            } else { // اذا ما رد السيرفير 
                JOptionPane.showMessageDialog(this, "Failed to load libraries.\n" + resp);
            }
        } catch (IOException ex) {// لو صار مشكله في الكونكشن 
            JOptionPane.showMessageDialog(this, "I/O: " + ex.getMessage());
        }
    }

    private void loadTopicsForSelectedLibrary() {
        try {
            Object sel = libraryCombo.getSelectedItem();// ياخذ الايبرري المختاره 
            if (sel == null) {// اذا ما اختار
                return;
            }
            String lib = sel.toString(); 
            String resp = connection.sendRequest("TOPICS:" + lib); // يطلب التوبكس من السيرفر 
            topicCombo.removeAllItems(); // يشيل القديم 
            if (resp != null && resp.startsWith("TOPICS:")) {// يتاكد من رد السيرفي 
                String[] parts = resp.split(":"); // يحط التوبكس في اراي
                if (parts.length >= 3) { 
                    String[] topics = parts[2].split(",");
                    for (String t : topics) {// يضيف اسماء التوبكس في الكومبو بوكس 
                        topicCombo.addItem(t.trim());
                    }
                }
            } else {// اذا رد السيرفير غير المطلوب 
                JOptionPane.showMessageDialog(this, "No topics.\n" + resp);
            }
        } catch (IOException ex) { // اذا فشل الكونكشن 
            JOptionPane.showMessageDialog(this, "I/O: " + ex.getMessage());
        }
    }

    private void loadBooksForSelectedTopic() { // تجيب الكتب المتةفره 
        try {
            Object l = libraryCombo.getSelectedItem();// يسند الايبرري
            Object t = topicCombo.getSelectedItem();// يسند التوبكس 
            if (l == null || t == null) {// اذا ماتم اختيارهم 
                return;
            }
            String resp = connection.sendRequest("BOOKS:" + l + ":" + t); // يطلب الكتب من السيرفير
            bookModel.clear();//يشيل الكتب القديمه 
            if (resp != null && resp.contains(":NO_BOOKS")) { // اذا رد عليه السيرفر لا مناته الكتب غير متوفره
                bookModel.addElement("No available books."); 
                
            } else if (resp != null && resp.startsWith("BOOKS:")) {// اذا فيه كتب 
                String[] parts = resp.split(":"); // يقسمهم في الاراي 
                if (parts.length >= 4) { 
                    String[] books = parts[3].split(",");  
                    for (String b : books) { // يضيفها في اللست 
                        bookModel.addElement(b.trim());
                    }
                }
            } else { // 
                JOptionPane.showMessageDialog(this, "Failed to load books.\n" + resp);
            }
        } catch (IOException ex) {//
            JOptionPane.showMessageDialog(this, "I/O: " + ex.getMessage());
        }
    }

    private void doReserveNow() {
        try {
            Object l = libraryCombo.getSelectedItem();
            Object t = topicCombo.getSelectedItem();
            String b = bookList.getSelectedValue();
            
            if (l == null || t == null || b == null) {
                return;
            }
            
            if (b.startsWith("No available")) {//اذا الكتاب محجوز 
                return;
            }
            
            String resp = connection.sendRequest("RESERVE:" + l + ":" + t + ":" + b);// يرسل الحجز للسيرفر
            if ("RESERVED".equals(resp)) {
                JOptionPane.showMessageDialog(this, "Reserved ✓");
                loadBooksForSelectedTopic();// يسوي ابديت للست عشان يشيل المحجوز 
                
            } else if ("NOT_AVAILABLE".equals(resp)) {// اذا كان الكتاب محجوز بس ما تحدث 
                JOptionPane.showMessageDialog(this, "Already reserved by someone else.");
                loadBooksForSelectedTopic();
                
            } else {
                JOptionPane.showMessageDialog(this, resp);
            }
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "I/O: " + ex.getMessage());
        }
    }

    private void handleLogoutButton() {
        try {
            connection.closeConnection();// يسكر الكونكشن مع السيرفر 
        } catch (Exception ignore) {
        }
        dispose();
        RegisterFrame rf = new RegisterFrame();// يرجع للريجستر فريم ن يبدا من جديد 
        rf.setVisible(true);
    }
}
