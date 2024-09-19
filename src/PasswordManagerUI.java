import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PasswordManagerUI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private int currentUserId; // Храним ID текущего пользователя
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField siteField;
    private JPasswordField newPasswordField;

    public PasswordManagerUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Login/Registration Panel
        JPanel loginPanel = new JPanel();
        loginPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        loginPanel.add(passwordField);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        
        // Password Entry Panel
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(new JLabel("Site:"));
        siteField = new JTextField(15);
        passwordPanel.add(siteField);
        passwordPanel.add(new JLabel("Password:"));
        newPasswordField = new JPasswordField(15);
        passwordPanel.add(newPasswordField);
        JButton saveButton = new JButton("Save Password");
        JButton logoutButton = new JButton("Logout");
        JButton viewPasswordsButton = new JButton("View Passwords");
        passwordPanel.add(saveButton);
        passwordPanel.add(logoutButton);
        passwordPanel.add(viewPasswordsButton);

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(passwordPanel, "PasswordEntry");
        
        frame.add(mainPanel);
        frame.setVisible(true);
        
        // Action Listeners
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (UserManager.authenticateUser(username, password)) {
                currentUserId = 1; // В реальном приложении получите ID пользователя
                cardLayout.show(mainPanel, "PasswordEntry");
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials");
            }
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (UserManager.registerUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Registration successful");
            } else {
                JOptionPane.showMessageDialog(frame, "Registration failed");
            }
        });

        saveButton.addActionListener(e -> {
            String site = siteField.getText();
            String password = new String(newPasswordField.getPassword());
            if (UserManager.isPasswordExists(currentUserId, site)) {
                JOptionPane.showMessageDialog(frame, "Password for this site already exists.");
            } else {
                UserManager.savePassword(currentUserId, site, password);
                JOptionPane.showMessageDialog(frame, "Password saved!");
                clearInputFields(); // Очищаем поля после сохранения пароля
            }
        });

        logoutButton.addActionListener(e -> {
            clearInputFields(); // Очищаем поля при выходе
            cardLayout.show(mainPanel, "Login");
        });

        viewPasswordsButton.addActionListener(e -> {
            List<String> passwords = UserManager.getUserPasswords(currentUserId);
            StringBuilder passwordList = new StringBuilder();
            for (String pwd : passwords) {
                passwordList.append(pwd).append("\n");
            }
            JOptionPane.showMessageDialog(frame, passwordList.length() > 0 ? passwordList.toString() : "No passwords saved.");
        });
    }

    private void clearInputFields() {
        usernameField.setText("");
        passwordField.setText("");
        siteField.setText("");
        newPasswordField.setText("");
    }

    public static void main(String[] args) {
        Database.initialize(); // Инициализация базы данных
        SwingUtilities.invokeLater(PasswordManagerUI::new);
    }
}