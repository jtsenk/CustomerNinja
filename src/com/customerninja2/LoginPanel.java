/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.customerninja2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login panel - Swing version of LoginPage.fxml
 * @author mellon
 */
public class LoginPanel extends JPanel {
    
    private JTextField userField;
    private JPasswordField pwField;
    private JButton submitButton;
    private JLabel userLabel;
    private JMenuBar menuBar;
    
    public LoginPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(170, 170, 170));
        
        // Create menu bar
        menuBar = createMenuBar();
        add(menuBar, BorderLayout.NORTH);
        
        // Create main panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        fileMenu.add(closeItem);
        bar.add(fileMenu);
        
        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        editMenu.add(deleteItem);
        bar.add(editMenu);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        bar.add(helpMenu);
        
        return bar;
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(170, 170, 170));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        // Title
        JLabel titleLabel = new JLabel("CüstomerNinja");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 96));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(50));
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(100));
        
        // Login form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(170, 170, 170));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username label and field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        
        userField = new JTextField(20);
        userField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(userField, gbc);
        
        // Password label and field
        JLabel pwLabel = new JLabel("Password:");
        pwLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(pwLabel, gbc);
        
        pwField = new JPasswordField(20);
        pwField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(pwField, gbc);
        
        // Submit button
        submitButton = new JButton("Login");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitButton.addActionListener(new SubmitButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);
        
        // Status label
        this.userLabel = new JLabel();
        this.userLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 3;
        formPanel.add(this.userLabel, gbc);
        
        formPanel.setMaximumSize(new Dimension(400, 200));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(formPanel);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = userField.getText();
            String password = new String(pwField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginPanel.this, 
                    "Please enter both username and password", 
                    "Input Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // Attempt to create a new session
                Session currentSession = new Session(username, password);
                
                // if the connection is valid = true
                if (currentSession.connectionValid) {
                    if (!currentSession.getAdminStatus()) {
                        // Show the inventory window if the current user is an employee
                        System.out.println("The connection was valid");
                        System.out.println("Current User: " + currentSession.currentUser.getName());
                        userLabel.setText("Logged in as: " + currentSession.currentUser.getName());
                        
                        try {
                            InventoryPanel inventoryPanel = new InventoryPanel(currentSession);
                            Pos.changePanel(inventoryPanel, "CustomerNinja - Inventory");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(LoginPanel.this, 
                                "Failed to load inventory: " + ex.getMessage(), 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                        
                    } else if (currentSession.getAdminStatus()) {
                        // Show the management navigation window if the current user is a manager
                        System.out.println("The connection was valid");
                        System.out.println("Current User: " + currentSession.currentUser.getName());
                        userLabel.setText("Logged in as: " + currentSession.currentUser.getName());
                        
                        try {
                            ManagerNavPanel navPanel = new ManagerNavPanel(currentSession);
                            Pos.changePanel(navPanel, "CustomerNinja - Manager Menu");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(LoginPanel.this, 
                                "Failed to load manager menu: " + ex.getMessage(), 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, 
                        "Invalid Login Provided", 
                        "Authentication Error", 
                        JOptionPane.ERROR_MESSAGE);
                    System.out.println("Connection invalid");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginPanel.this, 
                    "Login error: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "About CustomerNinja", true);
        aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        aboutDialog.setSize(400, 200);
        aboutDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("CüstomerNinja POS System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(10));
        
        JLabel versionLabel = new JLabel("Version 2.0 (Swing)");
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(versionLabel);
        
        JLabel descLabel = new JLabel("A student project for point of sale management");
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(descLabel);
        
        panel.add(Box.createVerticalGlue());
        
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> aboutDialog.dispose());
        panel.add(okButton);
        
        aboutDialog.add(panel);
        aboutDialog.setVisible(true);
    }
}
