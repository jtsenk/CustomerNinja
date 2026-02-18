/*
 * Swing version of CustomerManagementPage.fxml
 * Manages customers, their addresses, and sales information
 */
package com.customerninja2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

/**
 * Customer management panel for managers
 */
public class CustomerManagementPanel extends JPanel {
    
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private Session currentSession;
    private NinjaConn njc;
    private Customer selectedCustomer = null;
    
    public CustomerManagementPanel(Session session) throws Exception {
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
        
        // Create customer table
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
        tableModel.addColumn("Name");
        tableModel.addColumn("Address");
        tableModel.addColumn("Sales Count");
        tableModel.addColumn("Sales Value");
        
        // Load customers from database
        try {
            njc = new NinjaConn();
            ResultSet rs = njc.quGetAll("tbCustomers");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getInt("sales"),
                    String.format("$%.2f", rs.getDouble("sales_val"))
                });
            }
            njc.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + ex.getMessage());
        }
        
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = customerTable.getSelectedRow();
                if (row >= 0) {
                    int customerId = (Integer) tableModel.getValueAt(row, 0);
                    try {
                        selectedCustomer = new Customer(customerId, new NinjaConn());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(CustomerManagementPanel.this, 
                            "Error loading customer: " + ex.getMessage());
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 220, 220));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(200, 0));
        
        JButton addBtn = new JButton("Add Customer");
        addBtn.setMaximumSize(new Dimension(180, 30));
        addBtn.addActionListener(e -> addCustomer());
        panel.add(addBtn);
        
        panel.add(Box.createVerticalStrut(10));
        
        JButton editNameBtn = new JButton("Edit Name");
        editNameBtn.setMaximumSize(new Dimension(180, 30));
        editNameBtn.addActionListener(e -> editName());
        panel.add(editNameBtn);
        
        panel.add(Box.createVerticalStrut(10));
        
        JButton editAddressBtn = new JButton("Edit Address");
        editAddressBtn.setMaximumSize(new Dimension(180, 30));
        editAddressBtn.addActionListener(e -> editAddress());
        panel.add(editAddressBtn);
        
        panel.add(Box.createVerticalStrut(10));
        
        JButton deleteBtn = new JButton("Delete Customer");
        deleteBtn.setMaximumSize(new Dimension(180, 30));
        deleteBtn.setForeground(new Color(200, 0, 0));
        deleteBtn.addActionListener(e -> deleteCustomer());
        panel.add(deleteBtn);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void addCustomer() {
        String name = JOptionPane.showInputDialog(this, "Enter customer name:");
        if (name != null && !name.isEmpty()) {
            String address = JOptionPane.showInputDialog(this, "Enter customer address:");
            if (address != null) {
                try {
                    NinjaConn njc = new NinjaConn();
                    njc.addRowCustomers(name, address);
                    njc.close();
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Customer added successfully");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error adding customer: " + ex.getMessage());
                }
            }
        }
    }
    
    private void editName() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer first");
            return;
        }
        
        String newName = JOptionPane.showInputDialog(this, 
            "Enter new name:", selectedCustomer.getName());
        
        if (newName != null && !newName.isEmpty()) {
            selectedCustomer.setName(newName);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Customer name updated");
        }
    }
    
    private void editAddress() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer first");
            return;
        }
        
        String newAddr = JOptionPane.showInputDialog(this, 
            "Enter new address:", selectedCustomer.getAddress());
        
        if (newAddr != null && !newAddr.isEmpty()) {
            selectedCustomer.setAddress(newAddr);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Customer address updated");
        }
    }
    
    private void deleteCustomer() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer first");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this customer?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                NinjaConn njc = new NinjaConn();
                njc.rmRowCustomers(selectedCustomer.getID());
                njc.close();
                selectedCustomer = null;
                refreshTable();
                JOptionPane.showMessageDialog(this, "Customer deleted successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting customer: " + ex.getMessage());
            }
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            NinjaConn njc = new NinjaConn();
            ResultSet rs = njc.quGetAll("tbCustomers");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getInt("sales"),
                    String.format("$%.2f", rs.getDouble("sales_val"))
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
