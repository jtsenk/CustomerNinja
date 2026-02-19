/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.customerninja2;

import javax.swing.*;

/**
 * Main application entry point - converted from JavaFX to Swing
 * @author mellon
 */
public class Pos {
    static JFrame primaryFrame;
    
    /**
     * Change the current view to a new panel
     * @param panel - the panel to be displayed next
     * @param title - the title of the new frame
     * @throws Exception
     */
    public static void changePanel(JPanel panel, String title) throws Exception {
        if (primaryFrame != null) {
            primaryFrame.getContentPane().removeAll();
            primaryFrame.getContentPane().add(panel);
            primaryFrame.setTitle(title);
            primaryFrame.revalidate();
            primaryFrame.repaint();
        }
    }
    
    /**
     * Get the primary frame reference
     * @return the main application frame
     */
    public static JFrame getPrimaryFrame() {
        return primaryFrame;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            primaryFrame = new JFrame("CustomerNinja - POS System");
            primaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            primaryFrame.setSize(1280, 720);
            primaryFrame.setLocationRelativeTo(null);
            
            try {
                // Load the login panel
                LoginPanel loginPanel = new LoginPanel();
                primaryFrame.setContentPane(loginPanel);
                primaryFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(primaryFrame, "Failed to load login panel: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
}
