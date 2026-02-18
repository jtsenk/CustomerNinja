# Quick Reference - JavaFX to Swing Conversion Patterns

## Common Conversion Patterns Used in This Refactoring

### 1. Application Entry Point

**JavaFX Pattern:**
```java
public class Pos extends Application {
    public static void main(String[] args) {
        launch(args);  // Calls start() method
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        // UI initialization
    }
}
```

**Swing Pattern:**
```java
public class Pos {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Title");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
```

### 2. Scene Navigation

**JavaFX:**
```java
Stage stage = ...;
Scene newScene = new Scene(fxmlRoot, width, height);
stage.setScene(newScene);
stage.show();
```

**Swing:**
```java
JFrame frame = ...;
frame.getContentPane().removeAll();
frame.getContentPane().add(newPanel);
frame.revalidate();
frame.repaint();
```

### 3. UI Panel Creation

**JavaFX (FXML):**
```xml
<?xml version="1.0"?>
<BorderPane>
    <top>
        <MenuBar>...</MenuBar>
    </top>
    <center>
        <GridPane>...</GridPane>
    </center>
</BorderPane>
```

**Swing:**
```java
public class MyPanel extends JPanel {
    public MyPanel() {
        setLayout(new BorderLayout());
        add(createMenuBar(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
    }
}
```

### 4. Event Handling

**JavaFX (FXML):**
```xml
<Button text="Click Me" onAction="#handleClick"/>
```

**JavaFX (Code):**
```java
Button btn = new Button("Click Me");
btn.setOnAction(event -> handleClick());
```

**Swing:**
```java
JButton btn = new JButton("Click Me");
btn.addActionListener(event -> handleClick());
```

### 5. Menu Creation

**JavaFX:**
```java
MenuBar menuBar = new MenuBar();
Menu fileMenu = new Menu("File");
MenuItem exitItem = new MenuItem("Exit");
exitItem.setOnAction(e -> System.exit(0));
fileMenu.getItems().add(exitItem);
menuBar.getMenus().add(fileMenu);
```

**Swing:**
```java
JMenuBar menuBar = new JMenuBar();
JMenu fileMenu = new JMenu("File");
JMenuItem exitItem = new JMenuItem("Exit");
exitItem.addActionListener(e -> System.exit(0));
fileMenu.add(exitItem);
menuBar.add(fileMenu);
```

### 6. Table/List Display

**JavaFX:**
```java
// Using TableView with ObservableList and SimpleStringProperty bindings
TableView<Item> table = new TableView<>();
TableColumn<Item, String> nameCol = new TableColumn<>("Name");
nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
table.getColumns().add(nameCol);
```

**Swing:**
```java
DefaultTableModel model = new DefaultTableModel();
model.addColumn("Name");
for (Item item : itemList) {
    model.addRow(new Object[]{item.getName()});
}
JTable table = new JTable(model);
```

### 7. Dialog Windows

**JavaFX:**
```java
Stage dialog = new Stage();
Parent root = FXMLLoader.load(...);
dialog.setScene(new Scene(root));
dialog.initModality(Modality.APPLICATION_MODAL);
dialog.initOwner(mainStage);
dialog.showAndWait();
```

**Swing:**
```java
JDialog dialog = new JDialog(owner, "Title", true);
dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
dialog.setSize(300, 200);
JPanel content = new JPanel();
dialog.add(content);
dialog.setVisible(true);
```

### 8. Layout Management

**JavaFX Layouts:**
```java
BorderPane          // Main container (NORTH, SOUTH, EAST, WEST, CENTER)
HBox/VBox          // Horizontal/Vertical stacking
GridPane           // Grid-based layout
FlowPane           // Flow-based layout
AnchorPane         // Anchor-based positioning
StackPane          // Stacking layers
```

**Swing Equivalents:**
```java
BorderLayout       // Main container
BoxLayout          // Horizontal/Vertical stacking
GridLayout         // Grid-based layout
FlowLayout         // Flow-based layout
GridBagLayout      // Complex grid with weights
CardLayout         // Card-based switching
```

### 9. Data Binding

**JavaFX (Two-way binding):**
```java
private SimpleStringProperty nameProperty = new SimpleStringProperty();

public StringProperty nameProperty() {
    return nameProperty;
}

public String getName() {
    return nameProperty.get();
}

public void setName(String name) {
    nameProperty.set(name);
}

// In UI:
textField.textProperty().bindBidirectional(nameProperty);
```

**Swing (Manual update):**
```java
private String name;

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
    // Manually update UI
    textField.setText(name);
    // Refresh table models, etc.
}
```

### 10. Input Fields

**JavaFX:**
```java
TextField field = new TextField();
PasswordField pwField = new PasswordField();
TextArea area = new TextArea();
ComboBox<String> combo = new ComboBox<>();
```

**Swing:**
```java
JTextField field = new JTextField(20);
JPasswordField pwField = new JPasswordField(20);
JTextArea area = new JTextArea();
JComboBox<String> combo = new JComboBox<>(items);
```

### 11. Buttons and Labels

**JavaFX:**
```java
Button btn = new Button("Click");
Button btnWithIcon = new Button("Icon", new ImageView(image));
Label label = new Label("Text");
Text text = new Text("Text");
```

**Swing:**
```java
JButton btn = new JButton("Click");
JButton btnWithIcon = new JButton("Icon", icon);
JLabel label = new JLabel("Text");
```

### 12. Color and Styling

**JavaFX:**
```java
// FXML:
<VBox style="-fx-background-color: gray; -fx-padding: 10;"/>

// Code:
vbox.setStyle("-fx-background-color: gray;");
```

**Swing:**
```java
JPanel panel = new JPanel();
panel.setBackground(new Color(128, 128, 128));
panel.setBorder(new EmptyBorder(10, 10, 10, 10));
```

### 13. Images

**JavaFX:**
```java
Image image = new Image("file:path/to/image.png");
ImageView imageView = new ImageView(image);
imageView.setFitHeight(100);
imageView.setFitWidth(100);
```

**Swing:**
```java
BufferedImage image = ImageIO.read(new File("path/to/image.png"));
Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
ImageIcon icon = new ImageIcon(scaledImage);
JLabel label = new JLabel(icon);
```

### 14. Model Class Changes

**JavaFX Model:**
```java
import javafx.beans.property.SimpleStringProperty;

public class Customer {
    private SimpleStringProperty nameProperty;
    
    public StringProperty nameProperty() {
        return nameProperty;
    }
    
    public String getName() {
        return nameProperty.get();
    }
}
```

**Swing Model:**
```java
public class Customer {
    private String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        // Notify UI of changes if needed
        // (Manual or via listener pattern)
    }
}
```

### 15. Exception Handling UI

**JavaFX:**
```java
Alert alert = new Alert(Alert.AlertType.ERROR);
alert.setTitle("Error");
alert.setContentText("Error message");
alert.showAndWait();
```

**Swing:**
```java
JOptionPane.showMessageDialog(parent, "Error message", 
    "Error", JOptionPane.ERROR_MESSAGE);

// For confirmation:
int result = JOptionPane.showConfirmDialog(parent, "Continue?",
    "Confirm", JOptionPane.YES_NO_OPTION);
```

## Property Mapping Summary

| JavaFX | Swing | Notes |
|--------|-------|-------|
| Stage | JFrame | Top-level window |
| Scene | JPanel | Content container |
| FXML | Java code | UI definition |
| @FXML | None | All UI in code |
| SimpleStringProperty | String | Plain property |
| ObservableList | DefaultTableModel | Data model |
| TableView | JTable | Table display |
| Button.setOnAction() | button.addActionListener() | Event handling |
| Stage.setTitle() | frame.setTitle() | Window title |
| Modality.APPLICATION_MODAL | new JDialog(..., true) | Modal dialog |

## Import Changes

### Remove These Imports:
```java
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
```

### Add These Imports:
```java
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
```

## Best Practices for Swing Development

1. **Always extend JPanel for reusable components**
   ```java
   public class MyComponent extends JPanel { }
   ```

2. **Use BorderLayout for main containers**
   ```java
   setLayout(new BorderLayout());
   add(component, BorderLayout.NORTH);
   ```

3. **Use nested classes for dialogs**
   ```java
   private class MyDialog extends JDialog { }
   ```

4. **Handle thread safety with SwingUtilities**
   ```java
   SwingUtilities.invokeLater(() -> updateUI());
   ```

5. **Properly manage resources**
   ```java
   try (Resource r = new Resource()) {
       // Use resource
   }
   ```

## Testing Checklist Template

```java
@Test
public void testPanelCreation() {
    MyPanel panel = new MyPanel();
    assertNotNull(panel);
}

@Test
public void testButtonAction() {
    MyPanel panel = new MyPanel();
    JButton button = (JButton) panel.getComponent(0);
    button.doClick(); // Simulate click
    // Assert expected behavior
}

@Test
public void testTableModel() {
    DefaultTableModel model = createTableModel();
    assertEquals(5, model.getRowCount());
}
```

---

This quick reference provides pattern-based conversions for the most common JavaFX to Swing scenarios encountered in this refactoring project.
