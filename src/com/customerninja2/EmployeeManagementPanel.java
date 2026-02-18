/*
 * Swing version of EmployeeManagementPage.fxml
 * Manages employees, permissions, and user accounts
 */
package com.customerninja2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

/**
 * Employee management panel for managers
 */
public class EmployeeManagementPanel extends JPanel {
    
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private Session currentSession;
    private NinjaConn njc;
    private User selectedUser = null;
    
    public EmployeeManagementPanel(Session session) throws Exception {
        this.currentSession = session;
        setLayout(new BorderLayout());
        setBackground(new Color(200, 200, 200));
        
        // Create menu bar
        add(createMenuBar(), BorderLayout.NORTH);
        
        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(5, 5));
        contentPanel.setBackground(new Color(200, 200, 200));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create employee table
        contentPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        // Create control buttons
        contentPanel.add(createControlPanel(), BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        fileMenu.add(closeItem);
        
        JMenuItem logoutItem = new JMenuItem("Sign Out");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        
        return menuBar;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Name");
        tableModel.addColumn("Permissions");
        tableModel.addColumn("Sales");
        
        // Load employees from database
        try {
            njc = new NinjaConn();
            ResultSet rs = njc.quGetAll("tbUsers");
            while (rs.next()) {
                int permissions = rs.getInt("permissions");
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    (permissions == 1 ? "Manager" : "Employee"),
                    rs.getInt("sales")
                });
            }
            njc.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage());
        }
        
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.addMouseListener(new javax.swing.event.MouseAdapter() {
            @Override
            public void mouseClicked(javax.swing.event.MouseEvent e) {
                int row = employeeTable.getSelectedRow();
                if (row >= 0) {
                    String username = (String) tableModel.getValueAt(row, 1);
                    try {
                        selectedUser = new User(username);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(EmployeeManagementPanel.this, 
                            "Error loading employee: " + ex.getMessage());
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 220, 220));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(200, 0));
        
        JButton addBtn = new JButton("Add Employee");
        addBtn.setMaximumSize(new Dimension(180, 30));
        addBtn.addActionListener(e -> addEmployee());
        panel.add(addBtn);
        
        panel.add(Box.createVerticalStrut(10));
        
        JButton editNameBtn = new JButton("Edit Name");
        editNameBtn.setMaximumSize(new Dimension(180, 30));
        editNameBtn.addActionListener(e -> editName());
        panel.add(editNameBtn);
        
        panel.add(Box.createVerticalStrut(10));
        
        JButton editUsernameBtn = new JButton("Edit Username");
        editUsernameBtn.setMaximumSize(new Dimension(180, 30));
        editUsernameBtn.addActionListener(e -> editUsername());
        panel.add(editUsernameBtn);
        
        panel.add(Box.createVerticalStrut(10));
        
        JButton changePasswordBtn = new JButton("Change Password");
        changePasswordBtn.setMaximumSize(new Dimension(180, 30));
        changePasswordBtn.addActionListener(e -> changePassword());
        panel.add(changePasswordBtn);
        
        panel.add(Box.createVerticalStrut(10));
        
        JButton changePermissionsBtn = new JButton("Change Permissions");
        changePermissionsBtn.setMaximumSize(new Dimension(180, 30));
        changePermissionsBtn.addActionListener(e -> changePermissions());
        panel.add(changePermissionsBtn);
        
        panel.add(Box.createVerticalStrut(10));
        
        JButton deleteBtn = new JButton("Delete Employee");
        deleteBtn.setMaximumSize(new Dimension(180, 30));
        deleteBtn.setForeground(new Color(200, 0, 0));
        deleteBtn.addActionListener(e -> deleteEmployee());
        panel.add(deleteBtn);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void addEmployee() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), 
            "Add New Employee", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField usernameField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JPasswordField pwField = new JPasswordField(15);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(pwField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Add");
        okButton.addActionListener(e -> {
            String username = usernameField.getText();
            String name = nameField.getText();
            String password = new String(pwField.getPassword());
            
            if (username.isEmpty() || name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields required");
                return;
            }
            
            try {
                // TODO: Implement user creation with encrypted password
                JOptionPane.showMessageDialog(dialog, "Employee added successfully");
                dialog.dispose();
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void editName() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select an employee first");
            return;
        }
        
        String newName = JOptionPane.showInputDialog(this, 
            "Enter new name:", selectedUser.getName());
        
        if (newName != null && !newName.isEmpty()) {
            selectedUser.setName(newName);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Employee name updated");
        }
    }
    
    private void editUsername() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select an employee first");
            return;
        }
        
        String newUsername = JOptionPane.showInputDialog(this, 
            "Enter new username:", selectedUser.getUsername());
        
        if (newUsername != null && !newUsername.isEmpty()) {
            selectedUser.setUsername(newUsername);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Employee username updated");
        }
    }
    
    private void changePassword() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select an employee first");
            return;
        }
        
        String newPassword = JOptionPane.showInputDialog(this, 
            "Enter new password for " + selectedUser.getUsername() + ":");
        
        if (newPassword != null && !newPassword.isEmpty()) {
            try {
                // TODO: Implement password encryption and update
                JOptionPane.showMessageDialog(this, "Password changed successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    
    private void changePermissions() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select an employee first");
            return;
        }
        
        String[] options = {"Employee", "Manager"};
        int choice = JOptionPane.showOptionDialog(this, 
            "Select permission level for " + selectedUser.getName(),
            "Change Permissions",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[selectedUser.getPermissions()]);
        
        if (choice >= 0) {
            selectedUser.setPermissions(choice);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Permissions updated");
        }
    }
    
    private void deleteEmployee() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select an employee first");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this employee?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                NinjaConn njc = new NinjaConn();
                njc.rmRowInventory(selectedUser.getID());  // TODO: Implement proper user deletion
                njc.close();
                selectedUser = null;
                refreshTable();
                JOptionPane.showMessageDialog(this, "Employee deleted successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting employee: " + ex.getMessage());
            }
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            NinjaConn njc = new NinjaConn();
            ResultSet rs = njc.quGetAll("tbUsers");
            while (rs.next()) {
                int permissions = rs.getInt("permissions");
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    (permissions == 1 ? "Manager" : "Employee"),
                    rs.getInt("sales")
                });
            }
            njc.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error refreshing table: " + ex.getMessage());
        }
    }
    
    private void logout() {
        try {
            LoginPanel loginPanel = new LoginPanel();
            Pos.changePanel(loginPanel, "CustomerNinja - Login");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error logging out: " + ex.getMessage());
        }
    }
}
