package com.vl.marketing.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.LinkedHashMap;
import com.vl.marketing.Main;
import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Item;
import com.vl.marketing.model.Request;
import com.vl.marketing.util.AlertGenerator;
import com.vl.marketing.util.InputChecker;
import com.vl.marketing.util.RequestNumGenerator;

public class RequestNewController {
	
	@FXML private TableView<Item> itemTable; 
	@FXML private TableColumn<Item, String> vlNum;
	@FXML private TableColumn<Item, String> sku;
	@FXML private TableColumn<Item, String> type;
	@FXML private TableColumn<Item, Number> originalSRP;
	@FXML private TableColumn<Item, Number> normalCost;
	@FXML private TableColumn<Item, Number> promoPrice;
	@FXML private TableColumn<Item, Number> promoCost;
	@FXML private TableColumn<Item, Number> ber;
	@FXML private TableColumn<Item, Number> quantity;
	@FXML private TableColumn<Item, Number> allowance;
	
	@FXML private ComboBox<String> companyNameField;
	@FXML private TextField addressField;
	@FXML private TextField cityStateField;
	@FXML private TextField zipField;
	@FXML private TextField webAddressField;
	@FXML private TextField contactNameField;
	@FXML private TextField titleField;
	@FXML private TextField phoneField;
	@FXML private TextField faxField;
	@FXML private TextField emailField;
	@FXML private DatePicker startDateField;
	@FXML private DatePicker endDateField;
	@FXML private TextField descriptionField;
	@FXML private Label date1;
	@FXML private Label date2;
	@FXML private Label requestNum;
	@FXML private Label status;
	@FXML private ComboBox<String> coopType;
	@FXML private ComboBox<String> payment;
	@FXML private GridPane topPane;
	@FXML private ButtonBar buttonHolder;
	@FXML private Button deleteButton;
	
	private boolean dateRangePresent;
	private boolean deletePresent;
	
	/**
	 * List of all items being submitted in the new request
	 */
	private ObservableList<Item> data = FXCollections.observableArrayList();
	
	private Main mainApp;
	private Request request;
	private LinkedHashMap<String, String> requestInitializer = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> companyInfo		 = new LinkedHashMap<String, String>();
	
	
	public RequestNewController() {}
	
	
	/*
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
	@FXML
	private void initialize() {
		itemTable.setPlaceholder(new Label("No Items Added"));
		itemTable.setItems(data);
		
		dateRangePresent = true;
		
		//Dynamically generate all company names
		companyNameField.getItems().addAll(DBAccessor.getCompanyNames());
		
		deleteButton = new Button("Delete");
		deletePresent = false;
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	if(AlertGenerator.confirmation("confirmation", "Are you sure you want to delete this request?", "This action can't be undone")) {
	        		DBAccessor.deleteRequest(getRequestNum());
	        		handleRefresh();
	        	}
	        }
	    });
		
		vlNum.setCellValueFactory(cellData -> cellData.getValue().vlNumProperty());
		sku.setCellValueFactory(cellData -> cellData.getValue().skuProperty());
		type.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
		originalSRP.setCellValueFactory(cellData -> cellData.getValue().originalSRPProperty());
		normalCost.setCellValueFactory(cellData -> cellData.getValue().normalCostProperty());
		promoPrice.setCellValueFactory(cellData -> cellData.getValue().promoPriceProperty());
		promoCost.setCellValueFactory(cellData -> cellData.getValue().promoCostProperty());
		ber.setCellValueFactory(cellData -> cellData.getValue().berProperty());
		quantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
		allowance.setCellValueFactory(cellData -> cellData.getValue().allowanceProperty());
	}
	
	
	@FXML 
	private void setRequestNumLabel() {
		requestNum.setText(RequestNumGenerator.generate(getCompanyName(), coopType.getValue(), startDateField.getValue().toString()));
	}
	
	
	@FXML
	private void handleRefresh() {
		requestNum.setText("");
		companyNameField.getSelectionModel().clearSelection();
		addressField.clear();
		cityStateField.clear();
		zipField.clear();
		webAddressField.clear();
		contactNameField.clear();
		titleField.clear();
		phoneField.clear();
		faxField.clear();
		emailField.clear();
		startDateField.setValue(null);
		endDateField.setValue(null);
		descriptionField.clear();	
		coopType.getSelectionModel().clearSelection();
		payment.getSelectionModel().clearSelection();
		data.clear();
		if(!dateRangePresent) {
			topPane.getChildren().add(endDateField);
			date1.setText("Start Date");
			date2.setText("End Date");
			dateRangePresent = true;
		}
		allowEditting(true);
		removeDeleteButton();
		requestNum.setText("");
	}
	
	
	@FXML 
	private void handleNewItem() {
		mainApp.showItemNew(this);
	}
	
	
	@FXML
	private void handleSubmit() { 
		if (validInput()) {
			submitHelper();
			request = new Request(requestInitializer);
			requestInitializer.put("status", "P");
			if(AlertGenerator.confirmation("confirmation", "Are you sure you want to submit this request?", "This action can't be undone")){
				request.save("Request has been submitted for approval", data);
			}
		}
	}
	
	
	@FXML
	private void handleSave() {
		if (validInput()) {
			submitHelper();
			String status = DBAccessor.getStatus(getRequestNum());
			if(null == status) status = "0";
			requestInitializer.put("status", status);
			request = new Request(requestInitializer);
			request.save("Request saved", data);
		}
	}

	private boolean validInput() {
		return true;
	}
	
	
	@FXML
	private void handleLoad() {
		mainApp.showRequestOverview(this);
	}
	
	
	@FXML
	private void autoFillForm() {
		companyInfo.clear();
		companyInfo = DBAccessor.getCompanyInfo(getCompanyName());
		contactNameField.setText(companyInfo.get("contact"));
		phoneField.setText(companyInfo.get("phone"));
		addressField.setText(companyInfo.get("address"));
		cityStateField.setText(companyInfo.get("city/state"));
		zipField.setText(companyInfo.get("zip"));
	}
	
	
	
	// Changes the date text fields based on selected coop type
	// 	   If Price Protection => effective date
	//     If Coop/BER => start date and end date
	@FXML
	private void setDateFields() {
		if(coopType.getValue() == null) {
			return;
		} else if(coopType.getValue().equals("Price Protection") && dateRangePresent) {
			topPane.getChildren().remove(endDateField);
			date1.setText("Effective Date");
			endDateField.setValue(null);
			date2.setText("");
			dateRangePresent = false;
		} else if(!(coopType.getValue().equals("Price Protection")) && !dateRangePresent) {
			topPane.getChildren().add(endDateField);
			date1.setText("Start Date");
			date2.setText("End Date");
			dateRangePresent = true;
		}
	}
	
	
	private void submitHelper() {
		requestInitializer.put("request_num", requestNum.getText());
		requestInitializer.put("company_name", companyNameField.getValue());
		requestInitializer.put("address", addressField.getText());
		requestInitializer.put("city_state", cityStateField.getText());
		requestInitializer.put("zip", zipField.getText());
		requestInitializer.put("web_address", webAddressField.getText());
		requestInitializer.put("contact", contactNameField.getText());
		requestInitializer.put("title", titleField.getText());
		requestInitializer.put("fax", faxField.getText());
		requestInitializer.put("phone", phoneField.getText());
		requestInitializer.put("email", emailField.getText());
		requestInitializer.put("start_date", startDateField.getValue().toString());
		requestInitializer.put("end_date", endDateField.getValue().toString());
		requestInitializer.put("description", descriptionField.getText());
		requestInitializer.put("cootype", coopType.getValue());
		requestInitializer.put("payment", payment.getValue());
		requestInitializer.put("rejectReason", "");
		requestInitializer.put("approver", "");
	}
	
	
	public void setRequest(Request request) {
		this.request = request;
		requestNum.setText(request.getRequestNum());
		companyNameField.setValue(request.getCompanyName());
		addressField.setText(request.getAddress());
		cityStateField.setText(request.getcityState());
		zipField.setText(request.getZip());
		webAddressField.setText(request.getWebAddress());
		contactNameField.setText(request.getContactName());
		titleField.setText(request.getTitle());
		faxField.setText(request.getFax());
		phoneField.setText(request.getPhone());
		emailField.setText(request.getEmail());
		descriptionField.setText(request.getDescription());
		//startDateField.setValue(request.getStartDate());
		//endDateField.setText(request.getEndDate());
		coopType.setValue(request.getCoopType());
		payment.setValue(request.getPayment());
		setStatus();
		for(Item i : DBAccessor.getItems(request.getRequestNum())) {
			data.add(i);
		}
	}
	
	private void setStatus() {
		if(request.getStatus().equals("P")) {
			status.setText("Pending");
			status.setTextFill(Color.web("#e08d18"));
		} else if(request.getStatus().equals("A")) {
			status.setText("Approved");
			status.setTextFill(Color.web("green"));
		} else if(request.getStatus().equals("R")) {
			status.setText("Rejected");
			status.setTextFill(Color.web("red"));
		}
	}
	
	
	public void addDeleteButton() {
		if(!deletePresent) {
			buttonHolder.getButtons().add(deleteButton);
			deletePresent = true;
		}
	}
	
	public void removeDeleteButton() {
		if(deletePresent) {
			buttonHolder.getButtons().remove(deleteButton);
			deletePresent = false;
		}
	}
	
	
	public void allowEditting(Boolean choice) {
		companyNameField.setDisable(!choice);
		addressField.setEditable(choice);
		cityStateField.setEditable(choice);
		zipField.setEditable(false);
		webAddressField.setEditable(choice);
		contactNameField.setEditable(choice);
		titleField.setEditable(choice);
		phoneField.setEditable(choice);
		faxField.setEditable(choice);
		emailField.setEditable(choice);
		startDateField.setEditable(choice);
		endDateField.setEditable(choice);
		descriptionField.setEditable(choice);
		coopType.setDisable(!choice);
		payment.setDisable(!choice);
		buttonHolder.setVisible(choice);
	}
	
	
	//Item Table Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	 
	
	public void addItem(Item item) {
		data.add(item);
	}
	
	public void resetTable() {
		data.clear();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	
	public String getRequestNum() {
		return requestNum.getText();
	}
	
	
	public String getCompanyName() {
		return companyNameField.getValue();
	}
	
	
	public Request getRequest() {
		return request;
	}
	
	
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}
}
