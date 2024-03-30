package notes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HomePage extends javax.swing.JFrame {
    
    private JPanel mainPanel;
    private Connection connection;

    public HomePage() {
        initComponents();
        mainPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        jPanel1.add(scrollPane, BorderLayout.CENTER);
        setSize(900, 600);
        setLocationRelativeTo(getOwner());
        connectToDatabase();
        loadNotes();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        buttomNavBar = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addButton.setText("Add Note");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttomNavBarLayout = new javax.swing.GroupLayout(buttomNavBar);
        buttomNavBar.setLayout(buttomNavBarLayout);
        buttomNavBarLayout.setHorizontalGroup(
            buttomNavBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttomNavBarLayout.createSequentialGroup()
                .addContainerGap(666, Short.MAX_VALUE)
                .addComponent(addButton)
                .addGap(52, 52, 52))
        );
        buttomNavBarLayout.setVerticalGroup(
            buttomNavBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttomNavBarLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(addButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttomNavBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttomNavBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }                      

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {                                          
        new AddNewNote().setVisible(true);
    }                                         

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3307/testdb";
            String user = "root";
            String password = "Onkar@tv";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadNotes() {
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM PersonalNotes ORDER BY creation_datetime DESC LIMIT 50");
            ResultSet resultSet = pst.executeQuery();
            System.out.println("Executing query: " + pst.toString());
            int count = 0;
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String creationDatetime = resultSet.getString("creation_datetime");
                String lastEditDatetime = resultSet.getString("last_edit_datetime");
                JPanel cardPanel = createCardPanel(title, content, creationDatetime, lastEditDatetime);
                mainPanel.add(cardPanel);
                count++;
            }
            System.out.println("Number of rows returned: " + count);
            mainPanel.revalidate();
            mainPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createCardPanel(String title, String content, String creationDatetime, String lastEditDatetime) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false); // Making content not editable
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        panel.add(contentScrollPane, BorderLayout.CENTER);

        JLabel creationLabel = new JLabel("Created: " + creationDatetime);
        JLabel lastEditLabel = new JLabel("Last Edit: " + lastEditDatetime);
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(creationLabel);
        infoPanel.add(lastEditLabel);
        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton addButton;
    private javax.swing.JPanel buttomNavBar;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration                   
}
