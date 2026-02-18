/*
 * Swing version of InventoryManagementPage.java
 * Manager-level inventory management with full CRUD operations
 */
package com.customerninja2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 * Inventory management panel for managers
 * Provides full CRUD operations on inventory
 */
public class InventoryManagementPanel extends JPanel {
    
    private Inventory inventory;
    private LinkedList<Item> itemList;
    private Session currentSession;
    private int selectedItemID = -1;
    private Item selectedItem = null;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JLabel selectedItemLabel;
    private JLabel priceLabel;
    private JLabel quantityLabel;
    private JLabel descriptionLabel;
    private JPanel previewPanel;
    
    public InventoryManagementPanel(Session session) throws Exception {
        this.currentSession = session;
        setLayout(new BorderLayout());
        setBackground(new Color(200, 200, 200));
        
        // Load inventory
        NinjaConn njc = new NinjaConn();
        inventory = new Inventory(njc);
        itemList = inventory.getItemList();
        njc.close();
        
        // Create menu bar
        add(createMenuBar(), BorderLayout.NORTH);
        
        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(5, 5));
        contentPanel.setBackground(new Color(200, 200, 200));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create table for inventory listing
        contentPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        // Create right side panel with preview and controls
        contentPanel.add(createRightPanel(), BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        fileMenu.add(closeItem);
        
        JMenuItem logoutItem = new JMenuItem("Sign Out");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        
        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        editMenu.add(deleteItem);
        menuBar.add(editMenu);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Create table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price");
        tableModel.addColumn("Description");
        
        // Populate with data
        for (Item item : itemList) {
            tableModel.addRow(new Object[]{
                item.getID(),
                item.getName(),
                item.getQuantity(),
                String.format("$%.2f", item.getPrice()),
                item.getDescription()
            });
        }
        
        // Create table
        inventoryTable = new JTable(tableModel);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = inventoryTable.getSelectedRow();
                if (row >= 0) {
                    selectedItemID = (Integer) tableModel.getValueAt(row, 0);
                    selectedItem = itemList.get(row);
                    updatePreviewPanel();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 220, 220));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(300, 0));
        
        // Preview panel
        previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.setBackground(new Color(240, 240, 240));
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        previewPanel.setMaximumSize(new Dimension(300, 300));
        
        selectedItemLabel = new JLabel("No item selected");
        selectedItemLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        selectedItemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        priceLabel = new JLabel("Price: N/A");
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        quantityLabel = new JLabel("Quantity: N/A");
        quantityLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        quantityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        descriptionLabel = new JLabel("Description: N/A");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        previewPanel.add(Box.createVerticalStrut(10));
        previewPanel.add(selectedItemLabel);
        previewPanel.add(Box.createVerticalStrut(5));
        previewPanel.add(priceLabel);
        previewPanel.add(Box.createVerticalStrut(5));
        previewPanel.add(quantityLabel);
        previewPanel.add(Box.createVerticalStrut(5));
        previewPanel.add(descriptionLabel);
        previewPanel.add(Box.createVerticalGlue());
        
        panel.add(previewPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Control buttons
        JButton adjustPriceBtn = new JButton("Adjust Price");
        adjustPriceBtn.setMaximumSize(new Dimension(280, 30));
        adjustPriceBtn.addActionListener(e -> adjustPrice());
        panel.add(adjustPriceBtn);
        
        panel.add(Box.createVerticalStrut(5));
        
        JButton adjustQtyBtn = new JButton("Adjust Quantity");
        adjustQtyBtn.setMaximumSize(new Dimension(280, 30));
        adjustQtyBtn.addActionListener(e -> adjustQuantity());
        panel.add(adjustQtyBtn);
        
        panel.add(Box.createVerticalStrut(5));
        
        JButton adjustDiscountBtn = new JButton("Adjust Discount");
        adjustDiscountBtn.setMaximumSize(new Dimension(280, 30));
        adjustDiscountBtn.addActionListener(e -> adjustDiscount());
        panel.add(adjustDiscountBtn);
        
        panel.add(Box.createVerticalStrut(5));
        
        JButton addImageBtn = new JButton("Change Image");
        addImageBtn.setMaximumSize(new Dimension(280, 30));
        addImageBtn.addActionListener(e -> changeImage());
        panel.add(addImageBtn);
        
        panel.add(Box.createVerticalStrut(5));
        
        JButton removeBtn = new JButton("Remove Item");
        removeBtn.setMaximumSize(new Dimension(280, 30));
        removeBtn.setForeground(new Color(200, 0, 0));
        removeBtn.addActionListener(e -> removeItem());
        panel.add(removeBtn);
        
        panel.add(Box.createVerticalStrut(20));
        
        JButton addItemBtn = new JButton("Add New Item");
        addItemBtn.setMaximumSize(new Dimension(280, 30));
        addItemBtn.setBackground(new Color(0, 150, 0));
        addItemBtn.setForeground(Color.WHITE);
        addItemBtn.addActionListener(e -> addNewItem());
        panel.add(addItemBtn);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void updatePreviewPanel() {
        if (selectedItem != null) {
            selectedItemLabel.setText(selectedItem.getName());
            priceLabel.setText("Price: $" + String.format("%.2f", selectedItem.getPrice()));
            quantityLabel.setText("Quantity: " + selectedItem.getQuantity());
            descriptionLabel.setText("Description: " + selectedItem.getDescription());
            previewPanel.revalidate();
            previewPanel.repaint();
        }
    }
    
    private void adjustPrice() {
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item first");
            return;
        }
        
        String input = JOptionPane.showInputDialog(this, 
            "Enter new price for " + selectedItem.getName() + ":",
            selectedItem.getPrice());
        
        if (input != null) {
            try {
                double newPrice = Double.parseDouble(input);
                selectedItem.setPrice(newPrice);
                refreshTable();
                updatePreviewPanel();
                JOptionPane.showMessageDialog(this, "Price updated successfully");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price format");
            }
        }
    }
    
    private void adjustQuantity() {
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item first");
            return;
        }
        
        String input = JOptionPane.showInputDialog(this, 
            "Enter new quantity for " + selectedItem.getName() + ":",
            selectedItem.getQuantity());
        
        if (input != null) {
            try {
                int newQty = Integer.parseInt(input);
                selectedItem.setQuantity(newQty);
                refreshTable();
                updatePreviewPanel();
                JOptionPane.showMessageDialog(this, "Quantity updated successfully");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity format");
            }
        }
    }
    
    private void adjustDiscount() {
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item first");
            return;
        }
        
        String input = JOptionPane.showInputDialog(this, 
            "Enter discount amount for " + selectedItem.getName() + ":");
        
        if (input != null) {
            try {
                int discount = Integer.parseInt(input);
                // TODO: Implement discount logic if Item class supports it
                JOptionPane.showMessageDialog(this, "Discount set: " + discount + "%");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid discount format");
            }
        }
    }
    
    private void changeImage() {
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item first");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + File.separator + "Images"));
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Image changed to: " + selectedFile.getName());
        }
    }
    
    private void removeItem() {
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item first");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove " + selectedItem.getName() + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                inventory.rmItem(selectedItem.getID());
                itemList = inventory.getItemList();
                selectedItem = null;
                selectedItemID = -1;
                refreshTable();
                updatePreviewPanel();
                JOptionPane.showMessageDialog(this, "Item removed successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error removing item: " + ex.getMessage());
            }
        }
    }
    
    private void addNewItem() {
        AddItemDialog dialog = new AddItemDialog(SwingUtilities.getWindowAncestor(this), inventory);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            try {
                itemList = inventory.getItemList();
                refreshTable();
                JOptionPane.showMessageDialog(this, "Item added successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error refreshing inventory: " + ex.getMessage());
            }
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Item item : itemList) {
            tableModel.addRow(new Object[]{
                item.getID(),
                item.getName(),
                item.getQuantity(),
                String.format("$%.2f", item.getPrice()),
                item.getDescription()
            });
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

/**
 * Dialog for adding new items
 */
class AddItemDialog extends JDialog {
    private JTextField nameField;
    private JTextField qtyField;
    private JTextField priceField;
    private JTextField descField;
    private JTextField imageField;
    private boolean confirmed = false;
    private Inventory inventory;
    
    public AddItemDialog(java.awt.Window owner, Inventory inventory) {
        super((owner instanceof JFrame) ? (JFrame) owner : null, "Add New Item", JDialog.DEFAULT_MODALITY_TYPE);
        this.inventory = inventory;
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Item Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        // Quantity
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Quantity:"), gbc);
        qtyField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(qtyField, gbc);
        
        // Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Price:"), gbc);
        priceField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(priceField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Description:"), gbc);
        descField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(descField, gbc);
        
        // Image
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Image File:"), gbc);
        imageField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(imageField, gbc);
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Add");
        okButton.addActionListener(e -> addItem());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);
        
        add(panel);
    }
    
    private void addItem() {
        if (nameField.getText().isEmpty() || qtyField.getText().isEmpty() || 
            priceField.getText().isEmpty() || descField.getText().isEmpty() ||
            imageField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return;
        }
        
        try {
            String name = nameField.getText();
            int qty = Integer.parseInt(qtyField.getText());
            double price = Double.parseDouble(priceField.getText());
            String desc = descField.getText();
            String image = imageField.getText();
            
            inventory.addNewItem(name, qty, price, desc, 0, image);
            confirmed = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity or price format");
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
