# Getting Started - CustomerNinja Swing Edition

## Quick Start (5 minutes)

### 1. Setup
```bash
# Clone or open the repository
cd /path/to/CustomerNinja

# Create build directory
mkdir -p build

# Download SQLite JDBC if not present
# Place sqlite-jdbc.jar in lib/ directory
```

### 2. Build
```bash
javac -cp src:lib/sqlite-jdbc.jar -d build src/com/customerninja2/*.java
```

### 3. Run
```bash
java -cp build:lib/sqlite-jdbc.jar com.customerninja2.Pos
```

### 4. Login
- **Employee credentials**: (Ask your database admin)
- **Manager credentials**: (Ask your database admin)

---

## Understanding the Code Structure

### Main Entry Point
**File:** `src/com/customerninja2/Pos.java`

Starts the application with a JFrame and initializes LoginPanel.

### UI Panels (All in `src/com/customerninja2/`)

#### For Employees:
1. **LoginPanel** - Authentication screen
2. **InventoryPanel** - View items in grid format

#### For Managers:
1. **LoginPanel** - Authentication screen
2. **ManagerNavPanel** - Navigation hub with 4 buttons
3. **InventoryManagementPanel** - Add/Edit/Delete items
4. **CustomerManagementPanel** - Manage customer database
5. **EmployeeManagementPanel** - Manage users and permissions
6. **SalesStatsPanel** - View sales analytics

### Database Models (in `src/com/customerninja2/`)
- **NinjaConn.java** - Database access layer (SQLite)
- **Customer.java** - Customer data model
- **User.java** - User/Employee data model
- **Item.java** - Inventory item model
- **Sale.java** - Sales transaction model
- **Inventory.java** - Inventory collection
- **Session.java** - User session management

---

## Common Tasks

### Task 1: Modify the Login Screen

**File to edit:** `src/com/customerninja2/LoginPanel.java`

**Change title:**
```java
// In constructor
setBackground(new Color(170, 170, 170));
// Add this line to change the main frame title when LoginPanel loads
```

**Change colors:**
```java
// In createMainPanel() method
panel.setBackground(new Color(255, 255, 255)); // Change from gray to white
```

**Add logo:**
```java
// In createMainPanel() method, after title label
JLabel logo = new JLabel(new ImageIcon("path/to/logo.png"));
logo.setAlignmentX(Component.CENTER_ALIGNMENT);
panel.add(logo);
```

### Task 2: Customize the Inventory Grid

**File to edit:** `src/com/customerninja2/InventoryPanel.java`

**Change grid size (8×6 to something else):**
```java
private int itemsPerPage = 48; // Change: 8 columns × 6 rows = 48 items
// To 5×5: change to 25
// To 10×8: change to 80
```

**Update the GridLayout:**
```java
// In refreshItemGrid() method
itemGridPanel.setLayout(new GridLayout(6, 8, 5, 5)); // Change 6 and 8
```

**Add product description tooltip:**
```java
// In createItemButton() method
button.setToolTipText(item.getDescription());
```

### Task 3: Add a New Manager Function

**File to edit:** `src/com/customerninja2/ManagerNavPanel.java`

**Add new button:**
```java
JButton newFeatureBtn = new JButton("New Feature");
newFeatureBtn.setPreferredSize(new Dimension(250, 60));
newFeatureBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
newFeatureBtn.addActionListener(e -> goToNewFeature());
buttonPanel1.add(newFeatureBtn); // or buttonPanel2
```

**Add handler method:**
```java
private void goToNewFeature() {
    try {
        // Create your new panel class
        NewFeaturePanel panel = new NewFeaturePanel(currentSession);
        Pos.changePanel(panel, "CustomerNinja - New Feature");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, 
            "Error: " + ex.getMessage());
    }
}
```

### Task 4: Modify a Table Display

**File to edit:** `src/com/customerninja2/InventoryManagementPanel.java`

**Add a new table column:**
```java
// In createTablePanel() method
tableModel.addColumn("ID");
tableModel.addColumn("Name");
tableModel.addColumn("Quantity");
tableModel.addColumn("Price");
tableModel.addColumn("Description");
tableModel.addColumn("NEW COLUMN"); // Add here

// Update row data:
tableModel.addRow(new Object[]{
    item.getID(),
    item.getName(),
    item.getQuantity(),
    String.format("$%.2f", item.getPrice()),
    item.getDescription(),
    "NEW DATA HERE" // Add here
});
```

**Make a column read-only:**
```java
// After creating the table
inventoryTable.setModel(new DefaultTableModel(tableModel.getDataVector(), 
    tableModel.getColumnIdentifiers()) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0; // Column 0 (ID) is read-only
    }
});
```

### Task 5: Add Error Handling

**Common pattern for all panels:**
```java
try {
    // Your code here
    someOperation();
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(this, 
        "Database Error: " + ex.getMessage(),
        "Error",
        JOptionPane.ERROR_MESSAGE);
} catch (Exception ex) {
    JOptionPane.showMessageDialog(this,
        "Error: " + ex.getMessage(),
        "Error",
        JOptionPane.ERROR_MESSAGE);
    ex.printStackTrace();
}
```

### Task 6: Create a New Panel

**Template:**
```java
package com.customerninja2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyNewPanel extends JPanel {
    
    private Session currentSession;
    
    public MyNewPanel(Session session) throws Exception {
        this.currentSession = session;
        setLayout(new BorderLayout());
        setBackground(new Color(200, 200, 200));
        
        // Add components
        add(createMenuBar(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        
        return menuBar;
    }
    
    private JPanel createMainContent() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        // Add your content here
        return panel;
    }
}
```

---

## Debugging Tips

### Enable Debug Logging
Add to Pos.java main method:
```java
System.setProperty("swing.debug.graphics", "true");
```

### Check Database Connection
```java
NinjaConn njc = new NinjaConn();
ResultSet rs = njc.quGetAll("tbCustomers");
System.out.println("Database connection OK");
```

### Print Stack Traces
```java
catch (Exception ex) {
    ex.printStackTrace(); // Prints full stack trace
    JOptionPane.showMessageDialog(this, ex.getMessage());
}
```

### UI Component Tree
```java
// Add to panel constructor
printComponents(this, 0);

private void printComponents(Container c, int indent) {
    System.out.println(" ".repeat(indent) + c.getClass().getSimpleName());
    for (Component comp : c.getComponents()) {
        if (comp instanceof Container) {
            printComponents((Container)comp, indent+2);
        } else {
            System.out.println(" ".repeat(indent+2) + comp.getClass().getSimpleName());
        }
    }
}
```

---

## Common Issues & Solutions

### Issue 1: "Class not found" error
**Solution:**
```bash
# Ensure classpath includes src for compilation
javac -cp src:lib/sqlite-jdbc.jar -d build src/com/customerninja2/*.java
```

### Issue 2: Database connection fails
**Solution:**
```bash
# Ensure NinjaDB.db exists in current directory
# Or modify NinjaConn.java:
// Change this line:
conn = DriverManager.getConnection("jdbc:sqlite:NinjaDB.db");
// To:
conn = DriverManager.getConnection("jdbc:sqlite:/full/path/to/NinjaDB.db");
```

### Issue 3: JDBC driver not found
**Solution:**
```bash
# Ensure sqlite-jdbc.jar is in classpath
java -cp build:lib/sqlite-jdbc.jar com.customerninja2.Pos
```

### Issue 4: Panel components not showing
**Solution:**
```java
// Call these after adding components:
panel.revalidate();  // Recalculate layout
panel.repaint();     // Redraw components
```

### Issue 5: Dialog not appearing
**Solution:**
```java
// Ensure dialog is modal and visible:
JDialog dialog = new JDialog(owner, "Title", true); // true = modal
dialog.setVisible(true); // Must call this
```

---

## Performance Optimization

### Reduce Database Queries
```java
// Instead of querying in a loop:
for (int id : itemIds) {
    Item item = new Item(id); // Multiple DB queries!
}

// Do this:
NinjaConn njc = new NinjaConn();
ResultSet rs = njc.quGetAll("tbInventory");
while (rs.next()) {
    Item item = new Item(rs); // Better: use ResultSet constructor
}
```

### Optimize Table Display
```java
// Limit initial rows displayed
tableModel.setRowCount(Math.min(itemList.size(), 100));

// Add pagination or lazy loading for large datasets
```

### Cache Database Connections
```java
// Instead of:
NinjaConn njc = new NinjaConn();
njc.close();

// Consider: Reuse connection in static field (with thread safety)
private static NinjaConn conn;
```

---

## Testing Your Changes

### Unit Test Template
```java
import javax.swing.*;

public class TestCustomerPanel {
    
    @Test
    public void testPanelCreation() throws Exception {
        Session mockSession = new Session("test", "test");
        CustomerManagementPanel panel = new CustomerManagementPanel(mockSession);
        
        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }
    
    @Test
    public void testTablePopulation() throws Exception {
        // Your test here
    }
}
```

### Manual Testing Checklist
- [ ] Login with valid credentials
- [ ] Access all manager functions
- [ ] Add a new item
- [ ] Edit existing item
- [ ] Delete an item
- [ ] Add a customer
- [ ] View sales statistics
- [ ] Logout and login again
- [ ] Verify data persists in database

---

## Resources & References

### Java SE Documentation
- [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Layout Managers](https://docs.oracle.com/javase/tutorial/uiswing/layout/index.html)
- [Event Handling](https://docs.oracle.com/javase/tutorial/uiswing/events/index.html)

### SQLite JDBC
- [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)

### Project Documentation
- Read: `REFACTORING_GUIDE.md` - How the conversion was done
- Read: `CONVERSION_PATTERNS.md` - Common JavaFX→Swing patterns
- Read: `FINAL_REPORT.md` - Complete refactoring report

---

## Next Steps

### For Enhancement
1. Add search/filter capabilities to tables
2. Implement export to CSV
3. Add graphical charts for sales
4. Implement user preferences/settings
5. Add audit logging

### For Maintenance
1. Add unit tests
2. Improve error messages
3. Add input validation
4. Implement transaction management
5. Add database backup functionality

### For Deployment
1. Create executable JAR with manifest
2. Bundle with SQLite JDBC
3. Create installer for end users
4. Add version checking
5. Implement auto-update mechanism

---

## Support & Questions

For issues or questions:
1. Check the exception stack trace
2. Review relevant documentation files
3. Check database connectivity
4. Verify classpath configuration
5. Enable debug logging

Good luck with your CustomerNinja POS system!
