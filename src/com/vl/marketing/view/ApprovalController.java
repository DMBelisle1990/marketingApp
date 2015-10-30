package com.vl.marketing.view;

import java.util.LinkedHashMap;
import com.vl.marketing.db.DBA;
import com.vl.marketing.model.Authorization;
import com.vl.marketing.model.Item;
import com.vl.marketing.util.AlertGenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class ApprovalController {
	
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
	@FXML private Label companyName;
	@FXML private Label address;
	@FXML private Label city;
	@FXML private Label state;
	@FXML private Label zip;
	@FXML private Label webAddress;
	@FXML private Label contactName;
	@FXML private Label title;
	@FXML private Label phone;
	@FXML private Label fax;
	@FXML private Label email;
	@FXML private Label effectiveDate;
	@FXML private Label startDate;
	@FXML private Label optionalDate;
	@FXML private Label endDate;
	@FXML private Label description;
	@FXML private Label coopType;
	@FXML private Label payment;
	@FXML private Label status;
	@FXML private Label aName;
	@FXML private Label aTitle;
	@FXML private Label aEmail;
	@FXML private Label aPhone;
	@FXML private Label aExt;
	@FXML private Label aFax;
	@FXML private TextField aNameField;
	@FXML private TextField aTitleField;
	@FXML private TextField aEmailField;
	@FXML private TextField aPhoneField;
	@FXML private TextField aExtField;
	@FXML private TextField aFaxField;
	@FXML private ButtonBar bb;
	
	private Stage dialogStage;
	private Authorization authorization;
	private DBA database = new DBA();
	private DashboardController caller;
	private ObservableList<Item> items = FXCollections.observableArrayList();
	private LinkedHashMap<String, String> customerInfo = new LinkedHashMap<String, String>();
	
	@FXML
	private void initialize() {
		itemTable.setItems(items);
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
	
	
	public void setAuthorization(Authorization authorization) {
		this.authorization = authorization;
		// setStatus();
		companyName.setText(authorization.getCompany());
		customerInfo = database.getCustomerInfo(authorization.getCompany());
		address.setText(customerInfo.get("address"));
		city.setText(customerInfo.get("city"));
		state.setText(customerInfo.get("state"));
		zip.setText(customerInfo.get("zip"));
		webAddress.setText(customerInfo.get("webAddress"));
		contactName.setText(customerInfo.get("contact"));
		title.setText(customerInfo.get("title"));
		fax.setText(customerInfo.get("fax"));
		phone.setText(customerInfo.get("phone"));
		email.setText(customerInfo.get("email"));
		description.setText(authorization.getPromoDescription());
		coopType.setText(authorization.getPromoType());
//		startDate.setText(request.getStartDate());
//		if(request.getEndDate().equals("")) {
//			optionalDate.setText("");
//			effectiveDate.setText("  Effective Date:");
//		} else {
//			endDate.setText(request.getEndDate());
//		}
		setApprover();
		for(Item i : database.getItems(authorization.getVlMarketingNum())) {
			items.add(i);
		}
	}
	
	private void setApprover() {
		// aNameField.setText(request.getApprover());
	}
	
//	private void setStatus() {
//		if(request.getStatus().equals("P")) {
//			status.setText("Pending");
//			status.setTextFill(Color.web("#e08d18"));
//		} else if(request.getStatus().equals("A")) {
//			status.setText("Approved");
//			status.setTextFill(Color.web("green"));
//		} else if(request.getStatus().equals("R")) {
//			status.setText("Rejected");
//			status.setTextFill(Color.web("red"));
//		}
//	}
	
	@FXML
	private void handleReject() {
		if(caller.getUser().getPrivileges().get("approver")) {
			String reason = AlertGenerator.textInput("Confirm Reject", "Are you sure you want to reject?", "Reason: ");
			if(reason != "NO_MSG") {
				database.updateStatus(authorization.getVlMarketingNum(), "rejected");
				caller.refreshTable();
				handleCancel();
			}
		} else {
			System.out.println("Cant reject");
		}
	}
//	
	@FXML
	private void handleApprove() {
		if(caller.getUser().getPrivileges().get("approver")) {
			if(AlertGenerator.confirmation("Confirm Approval", "Are you sure you want to approve?", "")) {
				database.updateStatus(authorization.getVlMarketingNum(), "approved");
				caller.refreshTable();
	
	//			THIS IS THE PRICE UPDATE SECTION
	//			if(coopType.getText().equals("Price Protection")) {
	//				List<String> skuData = new ArrayList<>();
	//				List<Number> promoPriceData = new ArrayList<>();
	//				List<Number> promoCostData = new ArrayList<>();
	//				for (Item item : itemTable.getItems()) {
	//				    skuData.add(sku.getCellObservableValue(item).getValue());
	//				    promoPriceData.add(promoPrice.getCellObservableValue(item).getValue());
	//				    promoCostData.add(promoCost.getCellObservableValue(item).getValue());
	//				}
	//				DBAccessor.priceProtectionUpdate(companyName.getText(), skuData, promoPriceData, promoCostData);
	//			}
			handleCancel();
			}
		} else {
			System.out.println("Can't approve");
		}
	}
	
	
	public void setCaller(DashboardController caller) {
		this.caller = caller;
		if(!caller.getUser().getPrivileges().get("approver")) bb.setVisible(false);
	}
	
	private void handleCancel() {
		dialogStage.close();
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}
