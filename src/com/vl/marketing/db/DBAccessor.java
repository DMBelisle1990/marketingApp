package com.vl.marketing.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.vl.marketing.model.Request;
import com.vl.marketing.util.AlertGenerator;

public class DBAccessor {
	
	private static String MYSQL_DRIVER =  "com.mysql.jdbc.Driver";
	private static String MYSQL_URL    =  "jdbc:mysql://localhost:3306/test_database";
	
	private static Connection con;
	private static ResultSet  rs;
	private static PreparedStatement ps;
	private static String query;
	private static LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
	private static ArrayList<String> keys;
	private static ArrayList<String> values; 
	private static ArrayList<String> dbHeaders = new ArrayList<String>(
				   Arrays.asList("request_num", "company_name", "address", "city_state", "zip", 
						  "web_address", "contact", "title", "phone", "fax", "email", "start_date", 
						  "end_date", "description", "cootype", "payment"));
	
	private static final int NUM_HEADERS = dbHeaders.size();
	
	private DBAccessor() {}
	
	public static boolean addRquest(Request request) {
		try {
			openConnection();
			//Build the query.
			values = request.getValues();
			query = "INSERT into requests (";
			String questionMarks = "";
			for (int i = 0; i < NUM_HEADERS; i++) {
				query += dbHeaders.get(i) + ", ";
				questionMarks += "?, ";
			}
			query += "pending) values(" + questionMarks + "?)";
			
			ps = con.prepareStatement(query);
			for (int i = 0; i < values.size(); i++) {
				ps.setString(i+1, values.get(i));
			}
			ps.setInt(values.size()+1, request.getPending());
			ps.executeUpdate();
			return true;
		} catch (MySQLIntegrityConstraintViolationException e) {
			if(AlertGenerator.confirmation("Alert", "Request with request number '" + request.getRequestNum() + "' already exists",
										"Would you like to overwrite it?")) {
				updateRequest(request);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return false;
	}
	
	public static void updateRequest(Request request) {
		try {
			openConnection();
			values = request.getValues();
			query = "UPDATE requests SET "; 
			for(int i = 0; i < NUM_HEADERS; i++) {
				query += dbHeaders.get(i) + " = ?, ";
			}
			query += "pending = ? WHERE request_num = ?";
			ps = con.prepareStatement(query);
			System.out.println(query);
			for(int i = 0; i < values.size(); i++) {
				ps.setString(i+1, values.get(i));
			}
			ps.setInt(values.size()+1, request.getPending());
			ps.setString(values.size()+2, request.getRequestNum());
			ps.executeUpdate();
			} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	public static void deleteRequest(String requestNum) {
		try {
			openConnection();
			query = "DELETE FROM requests WHERE request_num = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, requestNum);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	public static Request getRequest(String requestNum) {
		Request request = new Request();
		try {
			openConnection();
			query = "SELECT * FROM requests WHERE request_num = '" + requestNum + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) {
				for (int i = 1; i < NUM_HEADERS; i++) {
					data.put(dbHeaders.get(i-1), rs.getString(i));
				}
				request = new Request(data, rs.getInt(NUM_HEADERS + 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return request;
	}
	
	/**
	 * This method returns the pending status on the request with the supplied requestNum.
	 * Returns not pending if the request doesn't exist
	 */
	public static int getPending(String requestNum) {
		try {
			openConnection();
			query = "SELECT EXISTS(SELECT pending FROM requests WHERE request_num = '" + requestNum + "')";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getInt(1) == 1) {
					query = "SELECT pending FROM requests WHERE request_num = '" + requestNum + "'";
					ps = con.prepareStatement(query);
					rs = ps.executeQuery();
					if(rs.next()) { return rs.getInt(1); }
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return 0;
	}
	
	public static ArrayList<String> getRequestNums(boolean pending) {
		ArrayList<String> result = new ArrayList<>();
		try {
			openConnection();
			query = "SELECT request_num FROM requests";
			if(pending) query += " WHERE pending = 1";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){ result.add(rs.getString(1)); }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return result;
	}
	
	public static void closeConnection() {
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("SQLException:\n"+e.toString());
				e.printStackTrace();
			}
		}
	}
	
	public static void openConnection() {
		try {
			Class.forName(MYSQL_DRIVER);
			System.out.println("Class Loaded....");
	        con = DriverManager.getConnection(MYSQL_URL,"root","Daniscool2");
	        System.out.println("Connected to the database....");
		} catch (ClassNotFoundException e) {
	        System.out.println("ClassNotFoundException:\n"+e.toString());
	        e.printStackTrace();
	    } catch (SQLException e) {
	        System.out.println("SQLException:\n"+e.toString());
	        e.printStackTrace();
	    }
	}
}
