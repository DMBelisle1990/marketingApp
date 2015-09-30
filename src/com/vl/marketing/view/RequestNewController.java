package com.vl.marketing.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.LinkedHashMap;
import com.vl.marketing.MainApp;
import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Item;
import com.vl.marketing.model.Request;
import com.vl.marketing.util.AlertGenerator;

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
	@FXML private TextField startDateField;
	@FXML private TextField endDateField;
	@FXML private Label date1;
	@FXML private Label date2;
	@FXML private TextField effectiveDateField;
	@FXML private TextField descriptionField;
	@FXML private ComboBox<String> coopType;
	@FXML private ComboBox<String> payment;
	@FXML private GridPane topPane;
	@FXML private Label requestNum;
	@FXML private ButtonBar buttonHolder;
	@FXML private Button deleteButton;
	
	private boolean dateRangePresent;
	
	/**
	 * List of all items being submitted in the new request
	 */
	private ObservableList<Item> data = FXCollections.observableArrayList();
	
	private MainApp mainApp;
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
		effectiveDateField = new TextField();
		
		//Populates all the comboboxes
		coopType.getItems().clear();
		coopType.getItems().addAll(
							"Back End Rebate",
							"Price Protection",
							"Coop");
		payment.getItems().clear();
		payment.getItems().addAll(
							"Credit");
		companyNameField.getItems().addAll(DBAccessor.getCompanyNames());
		
		
		vlNum.setCellValueFactory(cellData -> cellData.getValue().vlNumProperty());
		sku.setCellValueFactory(cellData -> cellData.getValue().skuProperty());
		type.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
		originalSRP.setCellValueFactory(cellData -> cellData.getValue().originalSRPProperty());
		normalCost.setCellValueFactory(cellData -> cellData.getValue().normalCostProperty());
		promoPrice.setCellValueFactory(cellData -> cellData.getValue().promoPriceProperty());
		promoCost.setCellValueFactory(cellData -> cellData.getValue().promoCostProperty());
		ber.setCellValueFactory(cellData -> cellData.getValue().berProperty());
	}
	
	
	@FXML 
	private void setRequestNumLabel() {
		requestNum.setText(companyNameField.getValue() + "_temp_req_num");
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
		startDateField.clear();
		endDateField.clear();
		descriptionField.clear();	
		coopType.getSelectionModel().clearSelection();
		payment.getSelectionModel().clearSelection();
		data.clear();
		if(!dateRangePresent) {
			topPane.getChildren().remove(effectiveDateField);
			topPane.getChildren().add(startDateField);
			topPane.getChildren().add(endDateField);
			date1.setText("Start Date");
			date2.setText("End Date");
			dateRangePresent = true;
		}
		allowEditting(true);
	}
	
	
	@FXML 
	private void handleNewItem() {
		mainApp.showItemNew(this);
	}
	
	
	@FXML
	private void handleSubmit() { 
		if (validInput()) {
			submitHelper();
			request = new Request(requestInitializer, 1, 0, 0);
			if(AlertGenerator.confirmation("confirmation", "Are you sure you want to submit this request?", "This action can't be undone")){
				request.save("Request has been submitted for approval", data);
			}
		}else {
			System.out.println("Bad Input");
		}
	}
	
	
	@FXML
	private void handleSave() {
		if (validInput()) {
			submitHelper();
			request = new Request(requestInitializer, DBAccessor.getPending(requestInitializer.get("request_num")), 0 , 0);
			request.save("Request saved", data);
		} else {
			System.out.println("Bad Input");
		}
	}
	
	
	@FXML
	private void handleLoad() {
		mainApp.showRequestOverview(this);
	}
	
	
	@FXML
	private void autoFillForm() {
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
		if(coopType.getValue() == "Price Protection" && dateRangePresent) {
			topPane.getChildren().remove(startDateField);
			topPane.getChildren().remove(endDateField);
			topPane.add(effectiveDateField, 5, 4);
			effectiveDateField.setMaxWidth(169);
			date1.setText("Effective Date");
			date2.setText("");
			dateRangePresent = false;
		} else if(coopType.getValue() != "Price Protection" && !dateRangePresent){
			topPane.getChildren().remove(effectiveDateField);
			topPane.getChildren().add(startDateField);
			topPane.getChildren().add(endDateField);
			date1.setText("Start Date");
			date2.setText("End Date");
			dateRangePresent = true;
		}
	}
	
	
	@FXML
	private void filterNames() {
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
		requestInitializer.put("start_date", startDateField.getText());
		requestInitializer.put("end_date", endDateField.getText());
		requestInitializer.put("description", descriptionField.getText());
		requestInitializer.put("cootype", coopType.getValue());
		requestInitializer.put("payment", payment.getValue());
	}
	
	
	//TODO Set up validInput checker
	private boolean validInput() {
		return true;
	}
	
	
	//TODO Set up dates 
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
		startDateField.setText(request.getStartDate());
		endDateField.setText(request.getEndDate());
		coopType.setValue(request.getCoopType());
		payment.setValue(request.getPayment());
		for(Item i : DBAccessor.getItems(request.getRequestNum())) {
			data.add(i);
		}
	}
	
	
	public void addDeleteButton() {
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
	
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
