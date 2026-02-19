/*
 * Swing version of SalesStatsPage.fxml
 * Displays sales statistics and analytics
 */
package com.customerninja2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

/**
 * Sales statistics panel for managers
 */
public class SalesStatsPanel extends JPanel {
    
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JLabel totalSalesLabel;
    private JLabel totalCountLabel;
    private Session currentSession;
    private NinjaConn njc;
    
    public SalesStatsPanel(Session session) throws Exception {
        this.currentSession = session;
        setLayout(new BorderLayout());
        setBackground(new Color(200, 200, 200));
        
        // Create menu bar
        add(createMenuBar(), BorderLayout.NORTH);
        
        // Create stats panel
        add(createStatsPanel(), BorderLayout.NORTH);
        
        // Create sales table
        add(createTablePanel(), BorderLayout.CENTER);
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
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(new Color(220, 220, 220));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel label1 = new JLabel("Total Sales Value: ");
        label1.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(label1);
        
        totalSalesLabel = new JLabel("$0.00");
        totalSalesLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalSalesLabel.setForeground(new Color(0, 100, 0));
        panel.add(totalSalesLabel);
        
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        
        JLabel label2 = new JLabel("Total Sales Count: ");
        label2.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(label2);
        
        totalCountLabel = new JLabel("0");
        totalCountLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalCountLabel.setForeground(new Color(0, 100, 0));
        panel.add(totalCountLabel);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            try {
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error refreshing: " + ex.getMessage());
            }
        });
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Sale ID");
        tableModel.addColumn("Date");
        tableModel.addColumn("Customer");
        tableModel.addColumn("Amount");
        
        salesTable = new JTable(tableModel);
        
        try {
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading sales data: " + ex.getMessage());
        }
        
        JScrollPane scrollPane = new JScrollPane(salesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshTable() throws Exception {
        tableModel.setRowCount(0);
        
        double totalSales = 0;
        int salesCount = 0;
        
        try {
            njc = new NinjaConn();
            ResultSet rs = njc.quGetAll("tbSales");
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String date = rs.getString("date");
                String customer = rs.getString("customer");
                double value = rs.getDouble("value");
                
                tableModel.addRow(new Object[]{
                    id,
                    date,
                    customer,
                    String.format("$%.2f", value)
                });
                
                totalSales += value;
                salesCount++;
            }
            
            njc.close();
        } catch (Exception ex) {
            throw new Exception("Error loading sales: " + ex.getMessage());
        }
        
        totalSalesLabel.setText(String.format("$%.2f", totalSales));
        totalCountLabel.setText(String.valueOf(salesCount));
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
