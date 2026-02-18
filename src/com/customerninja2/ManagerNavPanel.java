/*
 * Swing version of ManagerFunctionsNavPage.fxml
 * Navigation menu for manager functions
 */
package com.customerninja2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manager navigation panel for choosing between management functions
 */
public class ManagerNavPanel extends JPanel {
    
    private Session currentSession;
    
    public ManagerNavPanel(Session session) {
        this.currentSession = session;
        setLayout(new BorderLayout());
        setBackground(new Color(170, 170, 170));
        
        // Create menu bar
        add(createMenuBar(), BorderLayout.NORTH);
        
        // Create main content panel
        add(createMainPanel(), BorderLayout.CENTER);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem signOutItem = new JMenuItem("Sign Out");
        signOutItem.addActionListener(e -> signOut());
        fileMenu.add(signOutItem);
        
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        fileMenu.add(closeItem);
        menuBar.add(fileMenu);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(170, 170, 170));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        // Title
        JLabel titleLabel = new JLabel("Manager Functions Navigation");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(100));
        
        // Button panel 1
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel1.setBackground(new Color(170, 170, 170));
        buttonPanel1.setMaximumSize(new Dimension(1000, 80));
        
        JButton invManButton = new JButton("Inventory Management");
        invManButton.setPreferredSize(new Dimension(250, 60));
        invManButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        invManButton.addActionListener(e -> goToInventoryManagement());
        buttonPanel1.add(invManButton);
        
        JButton empManButton = new JButton("Employee Management");
        empManButton.setPreferredSize(new Dimension(250, 60));
        empManButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        empManButton.addActionListener(e -> goToEmployeeManagement());
        buttonPanel1.add(empManButton);
        
        JButton custManButton = new JButton("Customer Management");
        custManButton.setPreferredSize(new Dimension(250, 60));
        custManButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        custManButton.addActionListener(e -> goToCustomerManagement());
        buttonPanel1.add(custManButton);
        
        panel.add(buttonPanel1);
        
        panel.add(Box.createVerticalStrut(40));
        
        // Button panel 2
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel2.setBackground(new Color(170, 170, 170));
        buttonPanel2.setMaximumSize(new Dimension(1000, 80));
        
        JButton salesButton = new JButton("Sales Statistics");
        salesButton.setPreferredSize(new Dimension(250, 60));
        salesButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        salesButton.addActionListener(e -> goToSalesStats());
        buttonPanel2.add(salesButton);
        
        panel.add(buttonPanel2);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void goToInventoryManagement() {
        try {
            InventoryManagementPanel panel = new InventoryManagementPanel(currentSession);
            Pos.changePanel(panel, "CustomerNinja - Inventory Management");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading inventory management: " + ex.getMessage());
        }
    }
    
    private void goToEmployeeManagement() {
        try {
            EmployeeManagementPanel panel = new EmployeeManagementPanel(currentSession);
            Pos.changePanel(panel, "CustomerNinja - Employee Management");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading employee management: " + ex.getMessage());
        }
    }
    
    private void goToCustomerManagement() {
        try {
            CustomerManagementPanel panel = new CustomerManagementPanel(currentSession);
            Pos.changePanel(panel, "CustomerNinja - Customer Management");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading customer management: " + ex.getMessage());
        }
    }
    
    private void goToSalesStats() {
        try {
            SalesStatsPanel panel = new SalesStatsPanel(currentSession);
            Pos.changePanel(panel, "CustomerNinja - Sales Statistics");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading sales stats: " + ex.getMessage());
        }
    }
    
    private void signOut() {
        try {
            LoginPanel loginPanel = new LoginPanel();
            Pos.changePanel(loginPanel, "CustomerNinja - Login");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error signing out: " + ex.getMessage());
        }
    }
    
    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "About CustomerNinja", true);
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
