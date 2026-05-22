package labQuestions.swingGUI;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailAttachSend extends JFrame {
    JTextField toField, subjectField;
    JTextArea messageArea;
    JButton attachBtn, sendBtn, viewBtn;
    File attachmentFile = null;
    JTable table;

    public EmailAttachSend(){
        // Apply System Look and Feel for modern UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Swing Email Sender - Nishan Dhakal");
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10)); // Add gaps between regions

        // Setup Form Panel using GridBagLayout for alignment
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Padding around elements
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // To Field
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("To:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        toField = new JTextField();
        toField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(toField, gbc);

        // Subject Field
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        subjectField = new JTextField();
        subjectField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(subjectField, gbc);

        // Message Area
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("Message:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        messageArea = new JTextArea(6, 30);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(messageArea), gbc);

        // Setup Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        attachBtn = new JButton("Attach File");
        sendBtn = new JButton("Send Email");
        viewBtn = new JButton("View Sent Emails");

        // Styling Buttons
        sendBtn.setFont(new Font("Arial", Font.BOLD, 14)); // Keep it bold to stand out slightly
        attachBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        viewBtn.setFont(new Font("Arial", Font.PLAIN, 14));

        buttonPanel.add(attachBtn);
        buttonPanel.add(sendBtn);
        buttonPanel.add(viewBtn);

        // Combine Form and Buttons into a Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Setup Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sent Emails History"));
        table = new JTable();
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Add padding around the table panel itself
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 15, 20),
                tablePanel.getBorder()
        ));
        
        add(tablePanel, BorderLayout.CENTER);

        // Action Listeners
        attachBtn.addActionListener(e -> chooseFile());
        sendBtn.addActionListener(e -> sendEmail());
        viewBtn.addActionListener(e -> loadEmails());

        setLocationRelativeTo(null); // Center window on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    void chooseFile(){
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            attachmentFile = chooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "File Attached");
        }
    }

    void sendEmail(){
        String to = toField.getText();
        String subject = subjectField.getText();
        String message = messageArea.getText();

        // final String from = "your_email@gmail.com";
        // final String password = "your_app_password";
         final String from = "nissandhakal11@gmail.com";
        final String password = "gybo hucz umys qeqo";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            msg.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message);
            multipart.addBodyPart(textPart);

            if (attachmentFile != null) {
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.attachFile(attachmentFile);
                multipart.addBodyPart(attachPart);
            }

            msg.setContent(multipart);

            Transport.send(msg);

            saveToDatabase(to, subject, message);

            JOptionPane.showMessageDialog(this, "Email Sent");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    void saveToDatabase(String to, String subject, String message) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/maildb", "postgres", "nishandhakal");

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO sent_emails(recipient,subject,message,attachment) VALUES(?,?,?,?)");

            ps.setString(1, to);
            ps.setString(2, subject);
            ps.setString(3, message);
            ps.setString(4, attachmentFile == null ? "None" : attachmentFile.getName());

            ps.executeUpdate();
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void loadEmails() {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/maildb", "postgres", "nishandhakal");

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM sent_emails");

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"ID", "Recipient", "Subject", "Message", "Attachment", "Time"}, 0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("recipient"),
                        rs.getString("subject"),
                        rs.getString("message"),
                        rs.getString("attachment"),
                        rs.getTimestamp("sent_time")
                });
            }

            table.setModel(model);
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new EmailAttachSend();
    }
}
