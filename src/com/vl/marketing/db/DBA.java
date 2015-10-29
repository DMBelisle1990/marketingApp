package com.vl.marketing.db;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.vl.marketing.model.Accountant;
import com.vl.marketing.model.Admin;
import com.vl.marketing.model.Authorization;
import com.vl.marketing.model.Item;
import com.vl.marketing.model.Manager;
import com.vl.marketing.model.Sales;
import com.vl.marketing.model.User;
import com.vl.marketing.util.PasswordHash;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBA {
	
	// Database variables
	private static String MYSQL_DRIVER =  "com.mysql.jdbc.Driver";
	private static String MYSQL_URL    =  "jdbc:mysql://192.168.1.203:3306/VLDEV";
	private Connection con;
	private ResultSet  rs;
	private PreparedStatement ps;
	private String query;
	private String questionMarks = "";
	
	// All Database headers: CHANGE THESE IF DATABASE CHANGES
	private static List<String> authorizationsHeaders = new ArrayList<String>(
				   Arrays.asList("customer", "promoType", "promoDescription", "startDate", "endDate", 
						   		 "vlMarketingNum", "marketingNum", "status", "forecast", "actual"));
	
	public static List<String> itemHeaders = new ArrayList<String>(
				  Arrays.asList("vl_num", "sku", "type", "retailPrice", "normalCost", "promoPrice", 
						  		"promoCost", "ber", "quantity", "allowance", "vlMarketingNum"));
	
	public static List<String> customerHeaders = new ArrayList<String>(
				  Arrays.asList("name", "address", "city", "state", "zip", "webAddress"));
	
	public static List<String> contactHeaders = new ArrayList<String>(
			  	  Arrays.asList("customerName", "name", "title", "phone", "fax", "email"));
	
	public static List<String> userHeaders = new ArrayList<String>(
		  	  Arrays.asList("username", "name", "title", "email", "phone", "ext", "fax", "rank", "activated"));
	
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                    MKT_USERS		                //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////
	
	public void addUser(User user, String password) 
		throws NoSuchAlgorithmException, InvalidKeySpecException 
	{
		try {
			questionMarks = "";
			query = "INSERT into mkt_users (";
			for(int i = 0; i < userHeaders.size(); i++) {
				query += userHeaders.get(i) + ", ";
				questionMarks += "?, ";
			}
			query += "password_hash) values(" + questionMarks + "?)";
			
			openConnection();
			ps = con.prepareStatement(query);
			
			int i = 1;
			ps.setString(i++, user.getUsername());
			ps.setString(i++, user.getName());
			ps.setString(i++, user.getTitle());
			ps.setString(i++, user.getEmail());
			ps.setString(i++, user.getPhone());
			ps.setString(i++, user.getExt());
			ps.setString(i++, user.getFax());
			ps.setString(i++, user.getRank());
			ps.setDouble(i++, user.getActivated());
			ps.setString(i++, PasswordHash.createHash(password));
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	public User getUser(String username, String password) 
		throws NoSuchAlgorithmException, InvalidKeySpecException 
	{
		try {
			openConnection();
			query = "SELECT * FROM mkt_users WHERE username = '" + username + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) {
				// This will break if the hash isn't the last column in the database table
				System.out.println(rs.getString(userHeaders.size() + 1));
				if(PasswordHash.validatePassword(password, rs.getString(userHeaders.size() + 1))) {
					System.out.println("You have logged in");
					String rank = rs.getString(8);
					int i = 1;
					if(rank.equals("ADMIN"))
						return new Admin(rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++), 
										 rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getInt(i++));
					else if(rank.equals("ACCOUNTANT"))
						return new Accountant(rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++), 
								 rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getInt(i++));
					else if(rank.equals("MANAGEMENT"))
						return new Manager(rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++), 
								 rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getInt(i++));
					else if(rank.equals("SALES"))
						return new Sales(rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++), 
								 rs.getString(i++), rs.getString(i++), rs.getString(i++), rs.getInt(i++));
				} else {
					System.out.println("Invalid Login");
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			closeConnection();
		}
		return null;
	}
				
	
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                 MKT_AUTHORIZATIONS               //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////
	
	

	/**
	 * @return List of all non archived authorizations in the database
	 */
	public ObservableList<Authorization> getAuthorizations() {
		try {
			openConnection();
			
			query = "SELECT * FROM mkt_authorizations";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			ObservableList<Authorization> authorizations = FXCollections.observableArrayList();
			while(rs.next()) {
				int i = 1;
				authorizations.add(new Authorization(rs.getString(i++), rs.getString(i++), rs.getString(i++),
													 rs.getString(i++), rs.getString(i++), rs.getString(i++),
													 rs.getString(i++), rs.getString(i++), rs.getDouble(i++),
													 rs.getDouble(i++)));
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
			query = "INSERT into mkt_authorizations (";
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
	
	
	public void updateAuthorization(Authorization authorization) {
		try {
			openConnection();
			
			query = "UPDATE mkt_authorizations SET "; 
			for(int i = 0; i < authorizationsHeaders.size() - 1; i++) {
				query += authorizationsHeaders.get(i) + " = ?, ";
			}
			query += authorizationsHeaders.get(authorizationsHeaders.size()-1) + " = ? WHERE vlMarketingNum = ?";
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
			ps.setString(i++, authorization.getVlMarketingNum());
			
			ps.executeUpdate();
			System.out.println("Update successful");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	public void removeAuthorizations(ObservableList<Authorization> toRemove) {
		try {
			openConnection();
			query = "DELETE FROM mkt_authorizations WHERE vlMarketingNum IN(";
			for(int i = 0; i < toRemove.size() - 1; i++) {
				query += "'" + toRemove.get(i).getVlMarketingNum() + "',";
			}
			query += "'" + toRemove.get(toRemove.size()-1).getVlMarketingNum() + "')";
			
			ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	/**
	 * @param vlMarketingNum - Marketing Number of the authorization to be updated
	 * @param status - New desired status
	 */
	public void updateStatus(String vlMarketingNum, String status) {
		try {
			openConnection();
			
			query = "UPDATE mkt_authorizations SET status = ? WHERE vlMarketingNum = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, vlMarketingNum);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	/**
	 * @param vlMarketingNum - Marketing Number of the authorization to be updated
	 * @param status - New desired status
	 */
	public void updateStatus(ObservableList<Authorization> toUpdate, String status) {
		try {
			openConnection();
			
			query = "UPDATE mkt_authorizations SET status = ? WHERE vlMarketingNum IN (";
			for(int i = 0; i < toUpdate.size() - 1; i++) {
				query += "'" + toUpdate.get(i).getVlMarketingNum() + "',";
			}
			query += "'" + toUpdate.get(toUpdate.size()-1).getVlMarketingNum() + "')";
			
			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	/**
	 * @param vl - Should be either "vl" or "" depending on which Marketing Nums desired
	 */
	public ObservableList<String> getMarketingNums(String vl) {
		try {
			openConnection();
			
			ObservableList<String> marketingNums = FXCollections.observableArrayList();
			
			query = "SELECT " + vl + "marketingNum FROM mkt_authorizations";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				marketingNums.add(rs.getString(1));
			}
			
			return marketingNums;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return null;
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                  MKT_CUSTOMERS              		//////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////
	
	
	
	/**
	 * @return a list of all company names stored in the database
	 */
	public ObservableList<String> getCustomerNames() {
		try {
			openConnection();
			
			ObservableList<String> customerNames = FXCollections.observableArrayList();
			
			query = "SELECT name FROM mkt_customers";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				customerNames.add(rs.getString(1));
			}
			
			return customerNames;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return null;
	}
	
	
	/**
	 * @param customer: Name of customer who's info is needed
	 * @return: A map of all the necessary customer information
	 */
	public LinkedHashMap<String, String> getCustomerInfo(String customer) {
		try {
			openConnection();
			
			LinkedHashMap<String, String> customerInfo = new LinkedHashMap<String, String>();
			
			query = "SELECT * FROM mkt_customers JOIN mkt_contacts WHERE customerName = mkt_customers.name AND mkt_customers.name = '" + customer + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
		
			int i = 1;
			if(rs.next()) {
				customerInfo.put("name", rs.getString(i++));
				customerInfo.put("address", rs.getString(i++));
				customerInfo.put("city", rs.getString(i++));
				customerInfo.put("state", rs.getString(i++));
				customerInfo.put("zip", rs.getString(i++));
				customerInfo.put("webAddress", rs.getString(i++));
				customerInfo.put("contact", rs.getString(i++));
				customerInfo.put("title", rs.getString(i++));
				customerInfo.put("phone", rs.getString(i++));
				customerInfo.put("fax", rs.getString(i++));
				customerInfo.put("email", rs.getString(i++));
			}
			return customerInfo;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return null;
	}
	
	
	
	public void addCustomer(String name, String address, String city, String state, String zip, String webAddress) {
		try {
			openConnection();
			
			query = "INSERT into mkt_customers (";
			questionMarks = "";
			for(int i = 0; i < customerHeaders.size()-1; i++) {
				query += customerHeaders.get(i) + ", ";
				questionMarks += "?, ";
			}
			query += customerHeaders.get(customerHeaders.size()-1) + ") values (" + questionMarks + "?)";
			
			ps = con.prepareStatement(query);
			int i = 1;
			ps.setString(i++, name);
			ps.setString(i++, address);
			ps.setString(i++, city);
			ps.setString(i++, state);
			ps.setString(i++, zip);
			ps.setString(i++, webAddress);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                  MKT_CONTACTS              		//////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////
	
	
	public void addContact(String name, String title, String phone, String fax, String email, String customer) {
		try {
			openConnection();
			
			query = "INSERT into mkt_contacts (";
			questionMarks = "";
			for(int i = 0; i < contactHeaders.size()-1; i++) {
				query += contactHeaders.get(i) + ", ";
				questionMarks += "?, ";
			}
			query += contactHeaders.get(contactHeaders.size()-1) + ") values (" + questionMarks + "?)";
			
			ps = con.prepareStatement(query);
			int i = 1;
			ps.setString(i++, customer);
			ps.setString(i++, name);
			ps.setString(i++, title);
			ps.setString(i++, phone);
			ps.setString(i++, fax);
			ps.setString(i++, email);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                  MKT_ITEMS              			//////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////
	
	
	/**
	 * @param vlMarketingNum
	 * @return returns a list of items associated with the supplied request number
	 */
	public ObservableList<Item> getItems(String vlMarketingNum) {
		try {
			openConnection();
			
			query = "SELECT * FROM mkt_items WHERE vlMarketingNum = '" + vlMarketingNum + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			ObservableList<Item> items = FXCollections.observableArrayList();
			while(rs.next()) {
				int i = 1;
				items.add(new Item(rs.getString(i++), rs.getString(i++), rs.getString(i++), 
								   rs.getDouble(i++), rs.getDouble(i++), rs.getDouble(i++), rs.getDouble(i++),
								   rs.getDouble(i++), rs.getDouble(i++), rs.getDouble(i++), rs.getString(i++)));
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return null;
	}
	
	
	public void clearItems(String vlMarketingNum) {
		try {
			openConnection();
			
			query = "DELETE FROM mkt_items WHERE vlMarketingNum = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, vlMarketingNum);
			ps.execute();
		} catch (SQLException e) {
			closeConnection();
		}
	}
	
	
	/**
	 * Iterates through the list of items and adds them one at a time
	 */
	public void addItems(ObservableList<Item> items) {
		try {
			openConnection();
			
			for(Item item : items) {
				query = "INSERT into mkt_items (";
				questionMarks = "";
				for (int i = 0; i < itemHeaders.size()-1; i++) {
					query += itemHeaders.get(i) + ", ";
					questionMarks += "?, ";
				}
				query += itemHeaders.get(itemHeaders.size()-1) + ") values (" + questionMarks + "?)";
				
				ps = con.prepareStatement(query);
				int i = 1;
				ps.setString(i++, item.getVlNum());
				ps.setString(i++, item.getSku());
				ps.setString(i++, item.getType());
				ps.setDouble(i++, item.getSRP());
				ps.setDouble(i++, item.getNormalCost());
				ps.setDouble(i++, item.getPromoPrice());
				ps.setDouble(i++, item.getPromoCost());
				ps.setDouble(i++, item.getBer());
				ps.setDouble(i++, item.getQuantity());
				ps.setDouble(i++, item.getAllowance());
				ps.setString(i++, item.getRequestNum());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                  MKT_CUSTOMER_ITEMS              //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * @param customer
	 * @param type 
	 * @return
	 */
	public ObservableList<String> getProductNumbers(String customer, String type) {
		try {
			openConnection();
			
			query = "SELECT " + type + " FROM mkt_customer_items WHERE customer = '" + customer + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			ObservableList<String> productNumbers = FXCollections.observableArrayList();
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
	
	
	
	public void addCustomerItem(String a, String b, String c, String d, String f){
		try {
			openConnection();

			query = "INSERT INTO mkt_customer_items VALUES (?, ? , ?, ?, ?)";

			ps = con.prepareStatement(query);

			
			ps.setString(1, a);
			ps.setString(2, b);
			ps.setString(3, c);
			ps.setDouble(4, Double.parseDouble(d));
			ps.setDouble(5, Double.parseDouble(f));

			System.out.println(query);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	
	public String getVL(String sku, String customer) {
		try {
			openConnection();
			query = "SELECT model FROM mkt_customer_items WHERE resellerSku = '" + sku + "' AND customer = '" + customer + "'";
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
	
	
	public String getSKU(String model, String customer) {
		try {
			openConnection();
			query = "SELECT resellerSku FROM mkt_customer_items WHERE model = '" + model + "' AND customer = '" + customer + "'";
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
	
	
	public List<Double> getPrices(String resellerSku, String customer) {
		try {
			openConnection();
			query = "SELECT retailPrice, normalCost FROM mkt_customer_items WHERE resellerSku = '" + resellerSku + "' AND customer = '" + customer + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			List<Double> prices = new ArrayList<Double>();
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
	
	
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                  HELPERS                         //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////
	
	
	public void openConnection() {
		try {
			Class.forName(MYSQL_DRIVER);
	        con = DriverManager.getConnection(MYSQL_URL,"Dan","mktdb");
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
