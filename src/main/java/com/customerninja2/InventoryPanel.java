/*
 * Swing version of InventoryPage.java
 * Employee-level inventory view with basic item display
 */
package com.customerninja2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
 * Inventory display panel for employees
 * Shows items in a grid layout
 */
public class InventoryPanel extends JPanel {
    
    private Inventory inventory;
    private LinkedList<Item> itemList;
    private int inventoryPointer = 0;
    private int itemsPerPage = 48; // 8 columns x 6 rows
    private int currentPageStart = 0;
    private JPanel itemGridPanel;
    private Session currentSession;
    
    public InventoryPanel(Session session) throws Exception {
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
        
        // Create item grid panel
        itemGridPanel = new JPanel();
        itemGridPanel.setLayout(new GridLayout(6, 8, 5, 5));
        itemGridPanel.setBackground(new Color(200, 200, 200));
        itemGridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        refreshItemGrid();
        
        JScrollPane scrollPane = new JScrollPane(itemGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create navigation panel
        add(createNavigationPanel(), BorderLayout.SOUTH);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        
        JMenu viewMenu = new JMenu("View");
        JMenuItem refreshItem = new JMenuItem("Refresh");
        refreshItem.addActionListener(e -> {
            try {
                refreshItemGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error refreshing: " + ex.getMessage());
            }
        });
        viewMenu.add(refreshItem);
        menuBar.add(viewMenu);
        
        return menuBar;
    }
    
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(200, 200, 200));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton leftButton = new JButton("<-- Previous");
        leftButton.addActionListener(e -> previousPage());
        
        JLabel pageLabel = new JLabel("Page Navigation", JLabel.CENTER);
        pageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        JButton rightButton = new JButton("Next -->");
        rightButton.addActionListener(e -> nextPage());
        
        panel.add(leftButton, BorderLayout.WEST);
        panel.add(pageLabel, BorderLayout.CENTER);
        panel.add(rightButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void refreshItemGrid() {
        itemGridPanel.removeAll();
        
        int endIndex = Math.min(currentPageStart + itemsPerPage, itemList.size());
        
        for (int i = currentPageStart; i < endIndex; i++) {
            Item item = itemList.get(i);
            JButton itemButton = createItemButton(item);
            itemGridPanel.add(itemButton);
        }
        
        // Add empty panels to fill the grid
        int remaining = itemsPerPage - (endIndex - currentPageStart);
        for (int i = 0; i < remaining; i++) {
            itemGridPanel.add(new JPanel());
        }
        
        itemGridPanel.revalidate();
        itemGridPanel.repaint();
    }
    
    private JButton createItemButton(Item item) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(90, 90));
        button.setMaximumSize(new Dimension(90, 90));
        button.setMinimumSize(new Dimension(90, 90));
        
        // Try to load image
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    String imagePath = System.getProperty("user.dir") + File.separator + "Images" + 
                        File.separator + item.getImagePath();
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        BufferedImage image = ImageIO.read(imageFile);
                        if (image != null) {
                            // Scale image to fit
                            Image scaledImage = image.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                            g.drawImage(scaledImage, 10, 5, this);
                        }
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(10, 5, 70, 70);
                        g.setColor(Color.BLACK);
                        g.drawString("No Image", 20, 40);
                    }
                } catch (Exception e) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(10, 5, 70, 70);
                    g.setColor(Color.BLACK);
                    g.drawString("Error", 25, 40);
                }
            }
        };
        imagePanel.setOpaque(true);
        imagePanel.setBackground(Color.WHITE);
        
        // Item info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 240, 240));
        
        JLabel nameLabel = new JLabel(item.getName().length() > 12 ? 
            item.getName().substring(0, 12) + "..." : item.getName());
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 8));
        
        JLabel qtyLabel = new JLabel("Qty: " + item.getQuantity());
        qtyLabel.setFont(new Font("SansSerif", Font.PLAIN, 8));
        qtyLabel.setForeground(new Color(200, 0, 0));
        
        infoPanel.add(nameLabel);
        infoPanel.add(qtyLabel);
        
        button.add(imagePanel, BorderLayout.CENTER);
        button.add(infoPanel, BorderLayout.SOUTH);
        
        // Add click listener
        button.addActionListener(e -> onItemSelected(item));
        
        return button;
    }
    
    private void onItemSelected(Item item) {
        // TODO: Implement item details view or purchase dialog
        JOptionPane.showMessageDialog(this, 
            "Item: " + item.getName() + "\n" +
            "Price: $" + item.getPrice() + "\n" +
            "Qty: " + item.getQuantity(),
            "Item Details",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void nextPage() {
        if (currentPageStart + itemsPerPage < itemList.size()) {
            currentPageStart += itemsPerPage;
            refreshItemGrid();
        }
    }
    
    private void previousPage() {
        if (currentPageStart > 0) {
            currentPageStart -= itemsPerPage;
            refreshItemGrid();
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
