package com.customerninja2;

import java.sql.*;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class NinjaConn {

    private Connection conn;
    protected Statement stmt;
    private String sqlText;
    private ResultSet rSet;
    
    //generic constructor
    public NinjaConn() {
    	
    	try {
    	    	
    		conn = DriverManager.getConnection("jdbc:sqlite:NinjaDB.db");
    	   	stmt = conn.createStatement();
    	   	sqlText = "";
    	
    	} catch (Exception ex) {
    		
    		System.out.println("Fail!");
    		conn = null;
    		stmt = null;
    		sqlText = null;
    	}
    	
    } //end generic constructor
    
    public ResultSet quID(int id, String table) {
    	try {
    		
    		sqlText = "SELECT * FROM " + table + " WHERE ID=" + id;
    		return stmt.executeQuery(sqlText);
    	
    	} catch (Exception ex) {
    		
    		System.out.println("Fetch fail!  " + ex.getLocalizedMessage() );
    		return null;
    		
    	}
    }
    
    public ResultSet quName(String name, String table) {
    	try {
  
    		sqlText = "SELECT * FROM " + table + " WHERE NAME=\"" + name + "\"";
    		return stmt.executeQuery(sqlText);
    	
    	} catch (Exception ex) {
    		
    		System.out.println("Fetch fail!  " + ex.getLocalizedMessage() );
    		return null;
    		
    	}
    }
    
    public ResultSet quUname(String uname, String table) {
    	try {
                sqlText = "SELECT * FROM " + table + " WHERE USERNAME=\"" + uname + "\"";
                return stmt.executeQuery(sqlText);
                
    	} catch (Exception exc) {
    		System.out.println("quUname fail: " + exc.toString() );
    		return null;
    	}
    }
    
    public ResultSet quGetAll(String table) {
    	try {
    	
    		sqlText = "SELECT * FROM " + table;
    		return stmt.executeQuery(sqlText);
    	
    	} catch (Exception ex) {
    		
    		System.out.println("Get All fail!");
    		return null;
    		
    	}
    }
    
    public void updateDBDouble(String table, String field, double newVal, int IDnum) {
    	try {
    		sqlText = "UPDATE " + table + " SET " + field + "=" + newVal + " WHERE ID=" + IDnum;
    		stmt.executeUpdate(sqlText);
    		
    	} catch (Exception ex) {
    		System.out.println("Update fail!  " + ex.getLocalizedMessage() );
    	}
    }
    
    public void updateDBInt(String table, String field, int newVal, int IDnum) {
    	try {
    		sqlText = "UPDATE " + table + " SET " + field + "=" + newVal + " WHERE ID=" + IDnum;
    		stmt.executeUpdate(sqlText);
    		
    	} catch (Exception ex) {
    		System.out.println("Update fail!  " + ex.getLocalizedMessage() );
    	}
    }
    
    public void updateDBString(String table, String field, String newVal, int IDnum) {
    	try {
    		sqlText = "UPDATE " + table + " SET " + field + "=\"" + newVal + "\" WHERE ID=" + IDnum;
    		stmt.executeUpdate(sqlText);
    		
    	} catch (Exception ex) {
    		System.out.println("Update fail!  " + ex.getLocalizedMessage() );
    	}
    }
    
    public void addRowInventory(String name, int quantity, double price, String description, int discount, String imgFile) {
    	try {
    		sqlText = "INSERT INTO tbINVENTORY VALUES(NULL, \"" + name + "\", " + quantity + ", " + price +  ", \"" + description +  "\", " + discount +  ", \"" + imgFile + "\")";
    		stmt.executeUpdate(sqlText);
    	} catch (Exception ex) {
    		System.out.println("Insert new Inventory row fail! " + ex.toString() );
    	}
    }
    
    public void addRowCustomers(String name, String address) {
    	try {
    		sqlText = "INSERT INTO tbCUSTOMERS VALUES(NULL, \"" + name + "\", \"" + address + "\", 0, 0)";
    		stmt.executeUpdate(sqlText);
    	} catch (Exception ex) {
    		System.out.println("Insert new Customer row fail! " + ex.toString() );
    	}
    }
    
    public void addRowSales(String date, Double value, String customerName) {
        try {
    		sqlText = "INSERT INTO tbSALES VALUES(NULL, \"" + date + "\", " + value +  ", \"" + customerName + "\")";
    		stmt.executeUpdate(sqlText);
    	} catch (Exception ex) {
    		System.out.println("Insert new Sales row fail! " + ex.toString() );
    	}
    }
    
    public void addRowUsers(String input) {
        
        try {

            sqlText = "INSERT INTO tbUSERS VALUES(NULL," + input + ")";
            stmt.executeUpdate(sqlText);
            
        } catch(Exception exc) {
            System.out.println("addRowUsers Error! " + exc.toString());
        }
    }
    
    
    public void rmRowInventory(int IDnum) {
    	try {
    		sqlText = "DELETE FROM tbINVENTORY WHERE ID=" + IDnum;
    		stmt.executeUpdate(sqlText);
    	} catch (Exception ex) {
    		System.out.println("Remove inventory row fail! " + ex.toString() );
    	}
    }
    
    public void rmRowCustomers(int IDnum) {
    	try {
    		sqlText = "DELETE FROM tbCUSTOMERS WHERE ID=" + IDnum;
    		stmt.executeUpdate(sqlText);
    	} catch (Exception ex) {
    		System.out.println("Remove customers row fail! " + ex.toString() );
    	}
    }
    
    public void rmRowSales(int IDnum) {
        try {
    		sqlText = "DELETE FROM tbSALES WHERE ID=" + IDnum;
    		stmt.executeUpdate(sqlText);
    	} catch (Exception ex) {
    		System.out.println("Remove sales row fail! " + ex.toString() );
    	}
    }
    
    /*
     * ========== PASSWORD MANAGEMENT (Bcrypt-based) ==========
     * 
     * Bcrypt provides industry-standard, secure password hashing with:
     * - Built-in salt generation
     * - Adaptive cost (resistant to GPU/ASIC attacks)
     * - Recommended by OWASP and NIST
     * 
     * Replaces deprecated Triple DES (3DES) encryption
     */
    
    /**
     * Hash a password using Bcrypt and store it in the database
     * @param name Username
     * @param password Plain-text password to hash
     * @return true if successful, false otherwise
     */
    public Boolean hashAndStore(String name, String password) {
        rSet = quName(name, "tbUsers");
        
        try {
            // Hash password with Bcrypt (cost factor 12 = ~250ms on modern hardware)
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            
            // Store only the hash in database (password can never be decrypted)
            sqlText = "UPDATE tbUsers SET P2='" + hashedPassword + "' WHERE ID=" + rSet.getString("ID");
            stmt.executeUpdate(sqlText);
            
            return true;
            
        } catch (Exception exc) {
            System.out.println("hashAndStore Error: " + exc.toString());
            return false;
        }
    }
    
    /**
     * Verify a password against the stored Bcrypt hash
     * @param plainPassword Plain-text password to verify
     * @param username Username to look up
     * @return true if password matches, false otherwise
     */
    public Boolean verifyPassword(String plainPassword, String username) {
        rSet = quUname(username, "tbUsers");
        
        try {
            // Get the stored hash from database
            String storedHash = rSet.getString("P2");
            
            // Verify password against hash (Bcrypt internally extracts salt from hash)
            BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), storedHash);
            
            System.out.println("Password verification: " + (result.verified ? "SUCCESS" : "FAILED"));
            return result.verified;
            
        } catch (Exception exc) {
            System.out.println("verifyPassword Error: " + exc.toString());
            return false;
        }
    }
    
    /**
     * Check access - verify username and password for login
     * @param password Plain-text password
     * @param username Username
     * @return true if credentials valid, false otherwise
     */
    public Boolean checkAccess(String password, String username) {
        return verifyPassword(password, username);
    }
    
    public void close() {
        
        try {
            
            conn.close();
            
        } catch (Exception exc) {
            
            System.out.println("Close fail! " + exc.toString() );
            
        }
        
        
    }
    

} //end class