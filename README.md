# CustomerNinja - POS System (Swing Edition)

## Overview
CustomerNinja is a student project implementing a Point-of-Sale (POS) management system. This version has been completely refactored from JavaFX to Swing for improved portability and maintainability.

## Architecture
- **UI Framework**: Swing (Java SE standard library)
- **Database**: SQLite (NinjaDB.db)
- **Language**: Java 8+

## Features

### Employee Functions
- View inventory items in grid layout
- Navigate through inventory pages
- View item details (price, quantity, description)

### Manager Functions
- **Inventory Management**: Add, edit, and remove items; adjust prices and quantities
- **Customer Management**: Add/edit customer information and track sales
- **Employee Management**: Manage user accounts and permissions
- **Sales Statistics**: View and analyze sales data

### Security
- User authentication with encrypted password storage (DESede encryption)
- Role-based access control (Employee/Manager)
- Session management

## Setup Requirements

### Prerequisites
1. Java 8 or higher
2. SQLite JDBC driver (sqlite-jdbc.jar)
3. NinjaDB.db database file with proper schema

### Building
```bash
javac -cp src:lib/sqlite-jdbc.jar -d build src/com/customerninja2/*.java
```

### Running
```bash
java -cp build:lib/sqlite-jdbc.jar com.customerninja2.Pos
```

## File Structure

### Source Code
```
src/com/customerninja2/
├── Pos.java (main entry point)
├── LoginPanel.java (authentication)
├── InventoryPanel.java (employee view)
├── ManagerNavPanel.java (manager navigation)
├── InventoryManagementPanel.java (inventory CRUD)
├── CustomerManagementPanel.java (customer CRUD)
├── EmployeeManagementPanel.java (employee CRUD)
├── SalesStatsPanel.java (sales analytics)
├── Customer.java (model)
├── User.java (model)
├── Item.java (model)
├── Sale.java (model)
├── Inventory.java (model)
├── Session.java (session management)
└── NinjaConn.java (database layer)
```

### Documentation
- `REFACTORING_GUIDE.md` - Detailed JavaFX to Swing conversion guide
- `REFACTORING_SUMMARY.md` - Complete refactoring summary and improvements

## Database
If your program is failing to create the database connection, ensure the JDBC JAR file is properly configured in your classpath.

Required database schema (SQLite):
- tbCustomers (id, name, address, sales, sales_val)
- tbUsers (id, username, name, address, sales, sales_val, permissions, P1, P2)
- tbInventory (id, name, quantity, price, description, sale_item, image_file)
- tbSales (id, date, value, customer)

## Refactoring Notes
This application was originally built with JavaFX and has been completely refactored to use Swing. All original functionality has been preserved with the following improvements:
- **No external framework dependencies** - Uses only Java SE standard library
- **Simplified architecture** - All UI components in Java (no FXML)
- **Broader compatibility** - Works with Java 8+
- **Easier maintenance** - Pure Java code for all UI logic

See `REFACTORING_GUIDE.md` and `REFACTORING_SUMMARY.md` for detailed information about the migration.

## Contributors
- Original JavaFX version: (Greg, Mellon, JTS)
- Swing refactoring: AI Assistant (2026)

## License
Student project - Educational use only
