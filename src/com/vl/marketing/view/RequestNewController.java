package com.vl.marketing.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.LinkedHashMap;
import com.vl.marketing.MainApp;
import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Item;
import com.vl.marketing.model.Request;
import com.vl.marketing.util.AlertGenerator;

public class RequestNewController {
	
	@FXML private TableView<Item> itemTable; 
	@FXML private TableColumn<Item, String> itemName;
	@FXML private TableColumn<Item, String> vlNum;
	@FXML private TableColumn<Item, String> sku;
	@FXML private TableColumn<Item, String> type;
	@FXML private TableColumn<Item, Number> originalSRP;
	@FXML private TableColumn<Item, Number> normalCost;
	@FXML private TableColumn<Item, Number> promoPrice;
	@FXML private TableColumn<Item, Number> promoCost;
	@FXML private TableColumn<Item, Number> ber;
	
	@FXML private TextField companyNameField;
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
	@FXML private TextField descriptionField;
	@FXML private ComboBox<String> coopType;
	@FXML private ComboBox<String> payment;
	
	@FXML private Label requestNum;
	
	private ObservableList<Item> data = FXCollections.observableArrayList();
	private MainApp mainApp;
	
	private LinkedHashMap<String, String> requestInitializer = new LinkedHashMap<String, String>();

	
	public RequestNewController() {}
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
	@FXML
	private void initialize() {
		itemTable.setPlaceholder(new Label("No Items Added"));
		itemTable.setItems(data);
		coopType.getItems().clear();
		coopType.getItems().addAll(
							"Back End Rebate");
		payment.getItems().clear();
		payment.getItems().addAll(
							"Credit");
		itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
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
	private void handleRefresh() {
		companyNameField.clear();
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
	}
	
	@FXML 
	private void handleNewItem() {
		mainApp.showItemNew(this);
	}
	
	@FXML
	private void handleSubmit() { 
		if (validInput()) {
			submitHelper();
			Request request = new Request(requestInitializer, 1);
			if(AlertGenerator.confirmation("confirmation", "Are you sure you want to submit this request?", "This action can't be undone")){
				request.save("Request has been submitted for approval");
			}
		}else {
			System.out.println("Bad Input");
		}
	}
	
	
	@FXML
	private void handleSave() {
		if (validInput()) {
			submitHelper();
			Request request = new Request(requestInitializer, DBAccessor.getPending(requestInitializer.get("request_num")));
			request.save("Request saved");
		} else {
			System.out.println("Bad Input");
		}
	}
	
	@FXML private void handleLoad() {
		mainApp.showRequestOverview(this);
	}
	
	//TODO Remove address from request_num
	private void submitHelper() {
		requestInitializer.put("request_num", addressField.getText());
		requestInitializer.put("company_name", companyNameField.getText());
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
	
	//TODO Set up dates and comboboxes
	public void setRequest(Request request) {
		companyNameField.setText(request.getCompanyName());
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
	}
	
	/**
	 *Item Table Methods
	 */
	public void addItem(Item item) {
		data.add(item);
	}
	
	public void resetTable() {
		data.clear();
	}
	
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
