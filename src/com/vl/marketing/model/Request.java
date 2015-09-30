package com.vl.marketing.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.util.AlertGenerator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;


/**
 * 
 * Model class for a Request submission
 *
 */
public class Request {
	
	private final StringProperty requestNum;
	private final StringProperty companyName;
	private final StringProperty address;
	private final StringProperty cityState;
	private final StringProperty zip;
	private final StringProperty webAddress;
	private final StringProperty contactName;
	private final StringProperty title;
	private final StringProperty phone;
	private final StringProperty fax;
	private final StringProperty email;
	private final StringProperty startDate;
	private final StringProperty endDate;
	private final StringProperty description;
	private final StringProperty coopType;
	private final StringProperty payment;
	private int pending;
	private int approved = 0;
	private int rejected = 0;
	private ArrayList<String> keys = new ArrayList<>(Arrays.asList(
								"request_num", "company_name", "address", "city_state",
								"zip", "web_address", "contact", "title", "phone", "fax",
								"email", "start_date", "end_date", "description", 
								"cootype", "payment"));
	
	private LinkedHashMap<String, String> data;

	public Request() {
		this.requestNum 	= new SimpleStringProperty("");
		this.companyName 	= new SimpleStringProperty("");
		this.address 		= new SimpleStringProperty("");
		this.cityState 		= new SimpleStringProperty("");
		this.zip 			= new SimpleStringProperty("");
		this.webAddress 	= new SimpleStringProperty("");
		this.contactName    = new SimpleStringProperty("");
		this.title 			= new SimpleStringProperty("");
		this.phone 			= new SimpleStringProperty("");
		this.fax 			= new SimpleStringProperty("");
		this.email 			= new SimpleStringProperty("");
		this.startDate	    = new SimpleStringProperty("");
		this.endDate 		= new SimpleStringProperty("");
		this.description    = new SimpleStringProperty("");
		this.coopType 		= new SimpleStringProperty("");
		this.payment		= new SimpleStringProperty("");
	}
	
	public Request(LinkedHashMap<String, String> requestInitializer, int pending, int approved, int rejected) {
		data = requestInitializer;
		this.requestNum 	= new SimpleStringProperty(requestInitializer.get("request_num"));
		this.companyName	= new SimpleStringProperty(requestInitializer.get("company_name"));
		this.address 		= new SimpleStringProperty(requestInitializer.get("address"));
		this.cityState 		= new SimpleStringProperty(requestInitializer.get("city_state"));
		this.zip		    = new SimpleStringProperty(requestInitializer.get("zip"));
		this.webAddress	    = new SimpleStringProperty(requestInitializer.get("web_address"));
		this.contactName    = new SimpleStringProperty(requestInitializer.get("contact"));
		this.title 			= new SimpleStringProperty(requestInitializer.get("title"));
		this.phone 			= new SimpleStringProperty(requestInitializer.get("fax"));
		this.fax 			= new SimpleStringProperty(requestInitializer.get("phone"));
		this.email 			= new SimpleStringProperty(requestInitializer.get("email"));
		this.startDate 		= new SimpleStringProperty(requestInitializer.get("start_date"));
		this.endDate 		= new SimpleStringProperty(requestInitializer.get("end_date"));
		this.description 	= new SimpleStringProperty(requestInitializer.get("description"));
		this.coopType 		= new SimpleStringProperty(requestInitializer.get("cootype"));
		this.payment 		= new SimpleStringProperty(requestInitializer.get("payment"));
		this.pending 		= pending;
		this.approved 		= approved;
		this.rejected		= rejected;
	}
	
	/**
	 * Database modifiers.
	 */
	public void save(String saveMessage, ObservableList<Item> items) {
		if(DBAccessor.addRquest(this)) {
			DBAccessor.addItems(items);
			AlertGenerator.newAlert(AlertType.INFORMATION, "message", "", saveMessage);
		} else {
			AlertGenerator.newAlert(AlertType.ERROR, "error", "Request has failed to submit", "");
		}
	}
	
	public void delete() {
		DBAccessor.deleteRequest(this.getRequestNum());
	}
	
	public void update() {
		//DBAccessor.updateRequest(this);
	}
	

	/**
	 * Getters and Setters for Request.
	 */
	public LinkedHashMap<String, String> getData() { return data; }
	
	public void setData(LinkedHashMap<String, String> data) { this.data = data; }
	
	public ArrayList<String> getKeys() { return keys; }
	
	public ArrayList<String> getValues() { return new ArrayList<>(data.values()); }
	
	
	
	
	public int getPending() { return pending; }
	
	public void setPending(int pending) { this.pending = pending; }

	public int getApproved() { return approved; }

	public void setApproved(int approved) { this.approved= approved; }

	public int getRejected() { return rejected; }

	public void setRejected(int rejected) { this.rejected = rejected; }
	
	
	
	
	public String getRequestNum() { return requestNum.get(); }
	
	public void setRequestNum(String requestNum) { this.requestNum.set(requestNum); }
	
	public StringProperty requestNumProperty() { return requestNum; }
	
	
	
	
	public String getCompanyName() { return companyName.get(); }
	
	public void setCompanyName(String name) { this.companyName.set(name); }
	
	public StringProperty companyNameProperty() { return companyName; }
	
	
	
	
	public String getAddress() { return address.get(); }
	
	public void setAddress(String address) { this.address.set(address); }
	
	public StringProperty addressProperty() { return address; }
	
	
	
	
	public String getcityState() { return cityState.get(); }
	
	public void setCityState(String cityState) { this.cityState.set(cityState); }
	
	public StringProperty cityStateProperty() { return cityState; }
	
	
	
	
	public String getZip() { return zip.get(); }
	
	public void setZip(String zip) { this.zip.set(zip); }
	
	public StringProperty zipProperty() { return zip; }
	
	
	
	
	public String getWebAddress() { return webAddress.get(); }
	
	public void setWebAddress(String webAddress) { this.webAddress.set(webAddress); }
	
	public StringProperty webAddressProperty() { return webAddress; }
	
	
	
	
	public String getContactName() { return contactName.get(); }
	
	public void setContactName(String contactName) { this.contactName.set(contactName); }
	
	public StringProperty contactNameProperty() { return contactName; }
	
	
	
	
	public String getTitle() { return title.get(); }
	
	public void setTitle(String title) { this.title.set(title); }
	
	public StringProperty titleProperty() { return title; }
	
	
	
	
	public String getPhone() { return phone.get(); }
	
	public void setPhone(String phone) { this.phone.set(phone); }
	
	public StringProperty phoneProperty() { return phone; }
	
	
	
	
	public String getFax() { return fax.get(); }
	
	public void setFax(String fax) { this.fax.set(fax); }
	
	public StringProperty faxProperty() { return fax; }
	
	
	
	
	public String getDescription() { return description.get(); }
	
	public void setDescription(String description) { this.description.set(description); }
	
	public StringProperty descriptionProperty() { return description; }
	
	
	
	
	public String getStartDate() { return startDate.get(); }
	
	public void setStartDate(String startDate) { this.startDate.set(startDate); }
	
	public StringProperty startDateProperty() { return startDate; }
	
	
	
	
	public String getEndDate() { return endDate.get(); }
	
	public void setEndDate(String endDate) { this.endDate.set(endDate); }
	
	public StringProperty endDateProperty() { return endDate; }
	
	
	
	
	public String getEmail() { return email.get(); }
	
	public void setEmail(String email) { this.email.set(email); }
	
	public StringProperty emailProperty() { return email; }
	
	
	
	
	public String getCoopType() { return coopType.get(); }
	
	public void setCoopTyoe(String coopType) { this.coopType.set(coopType); }
	
	public StringProperty coopTypeProperty() { return coopType; }
	
	
	
	
	public String getPayment() { return payment.get(); }
	
	public void setPayment(String payment) { this.payment.set(payment); }
	
	public StringProperty paymentProperty() { return payment; }
		
}
