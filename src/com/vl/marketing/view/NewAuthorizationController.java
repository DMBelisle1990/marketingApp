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
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import com.vl.marketing.Main;
import com.vl.marketing.db.DBA;
import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Authorization;
import com.vl.marketing.model.Item;
import com.vl.marketing.util.AlertGenerator;
import com.vl.marketing.util.ComboBoxUtil;
import com.vl.marketing.util.DummyData;
import com.vl.marketing.util.InputChecker;
import com.vl.marketing.util.RequestNumGenerator;

public class NewAuthorizationController {
	
	// Table Components
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
	
	// All fields required in the form
	@FXML private ComboBox<String> companyNameField;
	@FXML private ComboBox<String> stateField;
	@FXML private ComboBox<String> promoType;
	@FXML private ComboBox<String> payment;
	@FXML private TextField addressField;
	@FXML private TextField cityField;
	@FXML private TextField zipField;
	@FXML private TextField webAddressField;
	@FXML private TextField contactNameField;
	@FXML private TextField titleField;
	@FXML private TextField phoneField;
	@FXML private TextField faxField;
	@FXML private TextField emailField;
	@FXML private TextField promoDescription;
	@FXML private DatePicker startDate;
	@FXML private DatePicker endDate;
	@FXML private Label date1;
	@FXML private Label date2;
	@FXML private Label requestNum;
	@FXML private Label status;
	@FXML private GridPane topPane;
	@FXML private ButtonBar buttonHolder;
	@FXML private Button deleteButton;
	@FXML private Button submit;
	
	// Determines how the dates should be presented
	private boolean dateRangePresent;
	
	// Detects presence of the Delete Button
	private boolean deletePresent;
	
	// List of all items being submitted in the new request
	private ObservableList<Item> items = FXCollections.observableArrayList();
	
	// Other variables
	private Main main;
	private Authorization authorization;
	private LinkedHashMap<String, String> customerInfo = new LinkedHashMap<String, String>();
	private DashboardController caller;
	private DBA database = new DBA();
	private ComboBoxUtil cb = new ComboBoxUtil();
	private String temp;
	private Stage dialogStage;
	
	public NewAuthorizationController() {}
	
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
	@FXML
	private void initialize() {
		temp = "R" + DummyData.randInt(1000) + DummyData.randInt(1000);
		itemTable.setPlaceholder(new Label("No Items Added"));
		itemTable.setItems(items);
		
		dateRangePresent = true;
		
		// Dynamically generate all company names
		companyNameField.getItems().addAll(database.getCustomerNames());
		stateField.getItems().addAll(cb.getStateAbbr());
		
		// TODO Change this to always attached but toggle hidden attribute
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
	
	// TODO fix requestNumLabel
	@FXML 
	private void setRequestNumLabel() {
		if(startDate.getValue() == null) return;
		requestNum.setText(RequestNumGenerator.generate(getCompanyName(), promoType.getValue(), startDate.getValue().toString()));
	}
	
	
	@FXML
	private void handleRefresh() {
		requestNum.setText("");
		companyNameField.getSelectionModel().clearSelection();
		addressField.clear();
		cityField.clear();
		stateField.getSelectionModel().clearSelection();
		zipField.clear();
		webAddressField.clear();
		contactNameField.clear();
		titleField.clear();
		phoneField.clear();
		faxField.clear();
		emailField.clear();
		startDate.setValue(null);
		endDate.setValue(null);
		promoDescription.clear();	
		promoType.getSelectionModel().clearSelection();
		payment.getSelectionModel().clearSelection();
		items.clear();
		if(!dateRangePresent) {
			topPane.getChildren().add(endDate);
			date1.setText("Start Date");
			date2.setText("End Date");
			dateRangePresent = true;
		}
		AllowEditing(true);
		removeDeleteButton();
		requestNum.setText("");
	}
	
	public void AllowEditing(Boolean choice) {
		companyNameField.setDisable(!choice);
		companyNameField.setStyle("-fx-opacity: 1;");
		stateField.setDisable(!choice);
		stateField.setStyle("-fx-opacity: 1;");
		promoType.setDisable(!choice);
		promoType.setStyle("-fx-opacity: 1;");
		payment.setDisable(!choice);
		payment.setStyle("-fx-opacity: 1;");
		startDate.setDisable(!choice);
		startDate.setStyle("-fx-opacity: 1;");
		endDate.setDisable(!choice);
		endDate.setStyle("-fx-opacity: 1;");
		
		addressField.setEditable(choice);
		cityField.setEditable(choice);
		zipField.setEditable(choice);
		webAddressField.setEditable(choice);
		contactNameField.setEditable(choice);
		titleField.setEditable(choice);
		phoneField.setEditable(choice);
		faxField.setEditable(choice);
		emailField.setEditable(choice);
		promoDescription.setEditable(choice);
		payment.setDisable(!choice);
		buttonHolder.setVisible(choice);
	}
	
	public void addDeleteButton() {
		if(!deletePresent) {
			buttonHolder.getButtons().add(deleteButton);
			deletePresent = true;
		}
	}
	
	
	@FXML
	private void handleDeleteItem() {
		removeItem(itemTable.getSelectionModel().getSelectedItem());
	}
	
	public void removeDeleteButton() {
		if(deletePresent) {
			buttonHolder.getButtons().remove(deleteButton);
			deletePresent = false;
		}
	}
	
	
	@FXML 
	private void handleNewItem() {
		main.showItemNew(this);
	}
	
	
	@FXML
	private void handleSubmit() { 
		if (validInput()) {
			authorization = new Authorization(companyNameField.getValue(), promoType.getValue(), promoDescription.getText(),
											  InputChecker.formatDate(startDate.getValue()), InputChecker.formatDate(endDate.getValue()), 
											  requestNum.getText(), "", "pending",0.0,0.0);
			
			if(AlertGenerator.confirmation("confirmation", "Are you sure you want to submit?", "This action can't be undone")) {
				if(submit.getText().equals("Update")) {
					if(AlertGenerator.confirmation("confirmation", "An authorization with that Marketing Number already exists", "Are you sure you want to overwrite?"))
						database.updateAuthorization(authorization);
					 	database.clearItems(authorization.getVlMarketingNum());
					 	database.addItems(items);
					 	for(int i = 0; i < items.size(); i++) {
					 		System.out.println(items.get(i).getRequestNum());
					 	}
				} else {
					caller.addAuthorization(authorization);
					database.addItems(items);
					dialogStage.close();
				}
			}
		}
	}
	
	
	@FXML
	private void addCustomer() {
		main.showAddCustomer();
	}
	
	
	@FXML
	private void addContact() {
		main.showAddContact();
	}
	
	
	private boolean validInput() {
		return true;
	}
	
	
	@FXML
	private void autoFillForm() {
		customerInfo.clear();
		customerInfo = database.getCustomerInfo(getCompanyName());
		addressField.setText(customerInfo.get("address"));
		cityField.setText(customerInfo.get("city"));
		stateField.setValue(customerInfo.get("state"));
		zipField.setText(customerInfo.get("zip"));
		webAddressField.setText(customerInfo.get("webAddress"));
		contactNameField.setText(customerInfo.get("contact"));
		titleField.setText(customerInfo.get("title"));
		phoneField.setText(customerInfo.get("phone"));
		faxField.setText(customerInfo.get("fax"));
		emailField.setText(customerInfo.get("email"));
	}
	
	
	// Changes the date text fields based on selected coop type
	// 	   If Price Protection => effective date
	//     If Coop/BER => start date and end date
	@FXML
	private void setDateFields() {
		if(promoType.getValue() == null) {
			return;
		} else if(promoType.getValue().equals("Price Protection") && dateRangePresent) {
			topPane.getChildren().remove(endDate);
			date1.setText("Effective Date");
			endDate.setValue(null);
			date2.setText("");
			dateRangePresent = false;
		} else if(!(promoType.getValue().equals("Price Protection")) && !dateRangePresent) {
			topPane.getChildren().add(endDate);
			date1.setText("Start Date");
			date2.setText("End Date");
			dateRangePresent = true;
		}
	}
	
	
	public void setAuthorization(Authorization authorization) {
		this.authorization = authorization;
		requestNum.setText(authorization.getVlMarketingNum());
		companyNameField.setValue(authorization.getCompany());
		autoFillForm();
		promoDescription.setText(authorization.getPromoDescription());
		startDate.setValue(InputChecker.formatDateString(authorization.getStartDate()));
		endDate.setValue(InputChecker.formatDateString(authorization.getEndDate()));
		promoType.setValue(authorization.getPromoType());
		// payment.setValue(request.getPayment());
		for(Item i : database.getItems(authorization.getVlMarketingNum())) {
			items.add(i);
		}
	}
	
	
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                 ITEM TABLE		                //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////	 
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public void removeItem(Item item) {
		items.remove(item);
	}
	
	public void resetTable() {
		items.clear();
	}
	

	
	
	
	public String getRequestNum() {
		return requestNum.getText();
	}
	
	
	public String getCompanyName() {
		return companyNameField.getValue();
	}
	
	
	public Authorization getAuthorization() {
		return authorization;
	}
	
	
	public void setCaller(DashboardController caller) {
		this.caller = caller;
	}
	
	public void setEditing() {
		// Only allow editing if new/pending/rejected authorization
		if(!(caller.getClickedStatus().equals("pending") || caller.getClickedStatus().equals("rejected") || caller.getClickedStatus().equals(""))) {
			AllowEditing(false); 
		} else if(!caller.getClickedStatus().equals("")) {
			submit.setText("Update");
		}
	}
	
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setMainApp(Main main) {
		this.main = main;
	}
}