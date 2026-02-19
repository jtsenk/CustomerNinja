/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.customerninja2;

import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author JTS
 */
public class Sale {

    private int id;
    private String date;
    private Double value;
    private String customerName;
    private final String table = "tbSALES";
    private NinjaConn njc;
    private ResultSet rset;
    
    public Sale(int id) {
		
		this.id = id;
		this.njc = new NinjaConn();
		rset = null;
                
		try {
		
		rset = njc.quID(this.id, this.table);
		date = rset.getString("date");
		value = rset.getDouble("value");		
		customerName = rset.getString("customer");
		
		} catch (Exception ex) {
			System.out.println("Sale Construct Fail!  " + ex.getLocalizedMessage() );
		} finally {
                    njc.close();
                }
		
	}

    public int getId() {
        return id;
    }
    
    public String getDate() {
        return date;
    }
    
    public Double getValue() {
        return value;
    }

    public String getCustomerName() {
        return customerName;
    }

}

