package com.vl.marketing.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.vl.marketing.model.Item;
import com.vl.marketing.model.Request;
import com.vl.marketing.util.AlertGenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBAccessor {
	
	private static String MYSQL_DRIVER =  "com.mysql.jdbc.Driver";
	private static String MYSQL_URL    =  "jdbc:mysql://192.168.1.203:3306/VLDEV";
	
	private static Connection con;
	private static ResultSet  rs;
	private static PreparedStatement ps;
	private static String query;
	private static LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
	private static LinkedHashMap<String, String> companyInfo = new LinkedHashMap<String, String>();
	private static ArrayList<String> values; 
	private static ArrayList<Double> doubleValues; 
	private static ArrayList<String> dbRequestHeaders = new ArrayList<String>(
				   Arrays.asList("request_num", "company_name", "address", "city_state", "zip", 
						  "web_address", "contact", "title", "phone", "fax", "email", "start_date", 
						  "end_date", "description", "cootype", "payment", "rejectReason", "approver"));
	public static ArrayList<String> dbItemHeaders = new ArrayList<String>(
				  Arrays.asList("vl_num", "sku", "type", "srp", 
						  		"normal_cost", "promo_price", "promo_cost", "ber", "quantity", "allowance"));
	private static ObservableList<Item> items = FXCollections.observableArrayList();
	private static ObservableList<String> companyNames = FXCollections.observableArrayList();
	private static ObservableList<String> productNumbers = FXCollections.observableArrayList();
	private static List<Double> prices = new ArrayList<Double>();
	private static final int NUM_REQ_HEADERS = dbRequestHeaders.size();
	
	
	
	
	
	private DBAccessor() {}
	
	/**
	 * @param request
	 * @return true if request successfully added
	 * 
	 * Attempts to add the supplied request to the database
	 */
	public static boolean addRquest(Request request) {
		try {
			openConnection();
			values = request.getValues();
			query = "INSERT into mktr_requests (";
			String questionMarks = "";
			for (int i = 0; i < NUM_REQ_HEADERS; i++) {
				query += dbRequestHeaders.get(i) + ", ";
				questionMarks += "?, ";
			}
			query += "status) values(" + questionMarks + "?)";
			
			ps = con.prepareStatement(query);
			for (int i = 0; i < values.size(); i++) {
				ps.setString(i+1, values.get(i));
			}
			ps.executeUpdate();
			return true;
		} catch (MySQLIntegrityConstraintViolationException e) {
			if(AlertGenerator.confirmation("Alert", "Request with request number '" + request.getRequestNum() + "' already exists", "Would you like to overwrite it?")) {
				deleteItems(request.getRequestNum());
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
	
	/**
	 * @param request
	 * 
	 * Attempts to update the supplied request
	 */
	public static void updateRequest(Request request) {
		try {
			openConnection();
			values = request.getValues();
			query = "UPDATE mktr_requests SET "; 
			for(int i = 0; i < NUM_REQ_HEADERS; i++) {
				query += dbRequestHeaders.get(i) + " = ?, ";
			}
			query += "status = ? WHERE request_num = ?";
			ps = con.prepareStatement(query);
			for(int i = 0; i < values.size(); i++) {
				ps.setString(i+1, values.get(i));
			}
			ps.setString(values.size()+1, request.getRequestNum());
			ps.executeUpdate();
		} catch (SQLException e) {
				e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	/**
	 * @param requestNum
	 * 
	 * Delete the request with supplied request number
	 */
	public static void deleteRequest(String requestNum) {
		try {
			openConnection();
			query = "DELETE FROM mktr_requests WHERE request_num = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, requestNum);
			ps.execute();
			query = "DELETE FROM mktr_items WHERE request_num = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, requestNum);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	public static void deleteItems(String requestNum) {
		try {
			openConnection();
			query = "DELETE FROM mktr_items WHERE request_num = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, requestNum);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	/**
	 * @param requestNum
	 * @return returns the request with request number requestNum
	 */
	public static Request getRequest(String requestNum) {
		Request request = new Request();
		try {
			openConnection();
			query = "SELECT * FROM mktr_requests WHERE request_num = '" + requestNum + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) {
				for (int i = 1; i <= NUM_REQ_HEADERS; i++) {
					data.put(dbRequestHeaders.get(i-1), rs.getString(i));
				}
				data.put("status", rs.getString(NUM_REQ_HEADERS + 1));
				request = new Request(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return request;
	}
	
	
	public static String getStatus(String requestNum) {
		try {
			openConnection();
			query = "SELECT status FROM mktr_requests WHERE request_num = '" + requestNum + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}
	
	
	
	public static ArrayList<String> getRequestNums(String constraint) {
		try {
			ArrayList<String> result = new ArrayList<>();
			openConnection();
			query = "SELECT request_num FROM mktr_requests";
			if(constraint != "") query += " WHERE status = '" + constraint + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){ result.add(rs.getString(1)); }
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
			
		}
		return null;
	}
	
	/**
	 * @param requestNum - requestNum of the request to be updated
	 * @param status - Status of request, A for approve, R for reject, P for pending
	 * 				 - An optional message can be attached if R
	 */
	public static void updateStatus(String requestNum, String status, String approver) {
		try {
			openConnection();
			query = "UPDATE mktr_requests SET status = ?, rejectReason = ?, approver = ? WHERE request_num = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, status.substring(0,1));
			ps.setString(2, status.substring(1));
			ps.setString(3, approver);
			ps.setString(4, requestNum);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	/**
	 * Iterates through the list of items and adds them one at a time
	 */
	public static void addItems(ObservableList<Item> items) {
//		try {
//			openConnection();
//			
//			for(Item item : items) {
//				values = item.getItemStrings();
//				query = "INSERT into mktr_items (";
//				String questionMarks = "";
//				for (int i = 0; i < values.size(); i++) {
//					query += dbItemHeaders.get(i) + ", ";
//					questionMarks += "?, ";
//				}
//				
//				doubleValues = item.getItemDoubles();
//				for (int i = 0; i < doubleValues.size(); i++) {
//					query += dbItemHeaders.get(values.size()+i) + ", ";
//					questionMarks += "?, ";
//				}
//				query +=  "request_num) values (" + questionMarks + "?)";
//				
//				ps = con.prepareStatement(query);
//				for (int i = 0; i < values.size(); i++) {
//					ps.setString(i+1, values.get(i));
//				}
//				
//				for (int i = 0; i < doubleValues.size(); i++) {
//					ps.setDouble(i + values.size() + 1, doubleValues.get(i));
//				}
//				ps.setString(values.size() + doubleValues.size() + 1, item.getRequestNum());
//				ps.executeUpdate();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			closeConnection();
//		}
	}
	
	/**
	 * @param requestNum
	 * @return returns a list of items associated with the supplied request number
	 */
	public static ObservableList<Item> getItems(String requestNum) {
		try {
			openConnection();
			query = "SELECT * FROM mktr_items WHERE request_num = '" + requestNum + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			items.clear();
			while(rs.next()) {
				items.add(new Item(rs.getString(1), rs.getString(2), rs.getString(3), 
								   rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7),
								   rs.getDouble(8), rs.getDouble(9), rs.getDouble(10), rs.getString(9)));
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}
	
	/**
	 * @return a list of all company names stored in the database
	 */
	public static ObservableList<String> getCompanyNames() {
		try {
			openConnection();
			query = "SELECT DISTINCT company FROM mktr_number_sku";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			companyNames.clear();
			while(rs.next()) {
				companyNames.add(rs.getString(1));
			}
			return companyNames;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}
	
	/**
	 * @param companyName obtain numbers for the supplied company name
	 * @param type can either be sku or vl, depending on desired product numbers
	 * @return
	 */
	public static ObservableList<String> getProductNumbers(String companyName, String type) {
		try {
			openConnection();
			query = "SELECT " + type.toUpperCase() + " FROM mktr_number_sku WHERE company = '" + companyName + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			productNumbers.clear();
			while(rs.next()) {
				if(rs.getString(1) != null) {
					productNumbers.add(rs.getString(1));
				}
			}
			return productNumbers;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}
	
	public static String getVL(String sku, String company) {
		try {
			openConnection();
			query = "SELECT number FROM mktr_number_sku WHERE SKU = '" + sku + "' AND company = '" + company + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}
	
	public static String getSKU(String number, String company) {
		try {
			openConnection();
			query = "SELECT SKU FROM mktr_number_sku WHERE number = '" + number + "' AND company = '" + company + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}
	
	public static LinkedHashMap<String, String> getCompanyInfo(String company) {
		try {
			openConnection();
			query = "SELECT * FROM mktr_customers WHERE company = '" + company + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) {
				companyInfo.put("contact", rs.getString(2));
				companyInfo.put("phone", rs.getString(3));
				companyInfo.put("address", rs.getString(4) + " " + rs.getString(5));
				companyInfo.put("city/state", rs.getString(6) + " " + rs.getString(7));
				companyInfo.put("zip", rs.getString(8));
			}
			return companyInfo;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}
	
	public static List<Double> getPrices(String model, String company) {
		try {
			openConnection();
			query = "SELECT retailprice, originalcost FROM mktr_prices WHERE model = '" + model + "' AND company = '" + company + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			prices.clear();
			
			if(rs.next()) {
				prices.add(rs.getDouble(1));
				prices.add(rs.getDouble(2));
			}
			return prices;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}
	
	public static void priceProtectionUpdate(String company, List<String> model, List<Number> promoPrice, List<Number> promoCost) {
		try {
			openConnection();
			for(int i = 0; i < model.size(); i++) {
				query = "UPDATE mktr_prices SET retailprice = ?, originalcost = ? WHERE model = ? AND company = ?";
				ps = con.prepareStatement(query);
				ps.setDouble(1, (double)promoPrice.get(i));
				ps.setDouble(2, (double)promoCost.get(i));
				ps.setString(3, model.get(i));
				ps.setString(4, company);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	
	/* 
	  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	  Helper functions
	  
	  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
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
			System.out.println("OLD DATABASE BEING ACCESSED");
	        con = DriverManager.getConnection(MYSQL_URL,"Dan","");
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
