package com.vl.marketing.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vl.marketing.model.Authorization;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBA {
	
	// Database variables
	private static String MYSQL_DRIVER =  "com.mysql.jdbc.Driver";
	private static String MYSQL_URL    =  "jdbc:mysql://localhost:3306/marketing";
	private Connection con;
	private ResultSet  rs;
	private PreparedStatement ps;
	private String query;
	private String questionMarks = "";
	
	// All Database headers: CHANGE THESE IF DATABASE CHANGES
	private static List<String> authorizationsHeaders = new ArrayList<String>(
				   Arrays.asList("customer", "promoType", "promoDescription", "startDate", "endDate", 
						   		 "vlMarketingNum", "marketingNum", "status", "forecast", "actual"));
				
	

	/**
	 * @return List of all non archived authorizations in the database
	 */
	public ObservableList<Authorization> getAuthorizations() {
		try {
			openConnection();
			
			query = "SELECT * FROM authorizations";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			ObservableList<Authorization> authorizations = FXCollections.observableArrayList();
			while(rs.next()) {
				authorizations.add(new Authorization(rs.getString(1), rs.getString(2), rs.getString(3),
													 rs.getString(4), rs.getString(5), rs.getString(6),
													 rs.getString(7), rs.getString(8), rs.getDouble(9),
													 rs.getDouble(10)));
			}
			
			return authorizations;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return null;
		
	}
	
	
	public void addAuthorization(Authorization authorization) {
		try {
			// Builds the query, lastHeader is used to prevent a trailing comma
			questionMarks = "";
			query = "INSERT into authorizations (";
			for(int i = 0; i < authorizationsHeaders.size() - 1; i++) {
				query += authorizationsHeaders.get(i) + ", ";
				questionMarks += "?, ";
			}
			String lastHeader = authorizationsHeaders.get(authorizationsHeaders.size() - 1);
			query += lastHeader + ") values(" + questionMarks + "?)";
			
			openConnection();
			ps = con.prepareStatement(query);
			int i = 1;
			ps.setString(i++, authorization.getCompany());
			ps.setString(i++, authorization.getPromoType());
			ps.setString(i++, authorization.getPromoDescription());
			ps.setString(i++, authorization.getStartDate());
			ps.setString(i++, authorization.getEndDate());
			ps.setString(i++, authorization.getVlMarketingNum());
			ps.setString(i++, authorization.getMarketingNum());
			ps.setString(i++, authorization.getStatus());
			ps.setDouble(i++, authorization.getForecast());
			ps.setDouble(i++, authorization.getActual());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	
	public void openConnection() {
		try {
			Class.forName(MYSQL_DRIVER);
	        con = DriverManager.getConnection(MYSQL_URL,"root","Daniscool2");
		} catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public void closeConnection() {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
