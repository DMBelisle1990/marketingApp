package com.vl.marketing.view;

import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Item;
import com.vl.marketing.model.Request;
import com.vl.marketing.util.AlertGenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
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
	/*Sup girl*/
	@FXML private Label companyName;
	@FXML private Label address;
	@FXML private Label cityState;
	@FXML private Label zip;
	@FXML private Label webAddress;
	@FXML private Label contactName;
	@FXML private Label title;
	@FXML private Label phone;
	@FXML private Label fax;
	@FXML private Label email;
	@FXML private Label startDate;
	@FXML private Label endDate;
	@FXML private Label description;
	@FXML private Label coopType;
	@FXML private Label payment;
	@FXML private Label status;
	
	private Stage dialogStage;
	private Request request;
	private Thread th;
	private ApprovalOverviewController caller;
	private ObservableList<Item> items = FXCollections.observableArrayList();
	
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
	}
	
	public void setCaller(ApprovalOverviewController caller) {
		this.caller = caller;
	}
	
	public void setRequest(Request request) {
		this.request = request;
		setStatus();
		companyName.setText(request.getCompanyName());
		address.setText(request.getAddress());
		cityState.setText(request.getcityState());
		zip.setText(request.getZip());
		webAddress.setText(request.getWebAddress());
		contactName.setText(request.getContactName());
		title.setText(request.getTitle());
		fax.setText(request.getFax());
		phone.setText(request.getPhone());
		email.setText(request.getEmail());
		description.setText(request.getDescription());
		for(Item i : DBAccessor.getItems(request.getRequestNum())) {
			items.add(i);
		}
	}
	
	private void setStatus() {
		if(request.getStatus().equals("P")) {
			status.setText("Pending");
			status.setTextFill(Color.web("#e08d18"));
		}
		if(request.getStatus().equals("A")) {
			status.setText("Approved");
			status.setTextFill(Color.web("green"));
		}
		if(request.getStatus().equals("R")) {
			status.setText("Rejected");
			status.setTextFill(Color.web("red"));
		}
		
	}
	
	@FXML
	private void handleReject() {
		String reason = AlertGenerator.textInput("Confirm Reject", "Are you sure you want to reject?", "Reason: ");
		if(reason != "NO_MSG") {
			setStatus("R" + reason);
			caller.removeRequest(request.getRequestNum(), "approved");
			handleCancel();
		}
	}
	
	@FXML
	private void handleApprove() {
		if(AlertGenerator.confirmation("Confirm Approval", "Are you sure you want to approve?", "")) {
			setStatus("A");
			caller.removeRequest(request.getRequestNum(), "rejected");
			handleCancel();
		}
	}
	
	/**
	 * Starts a new thread to update approved/rejected status
	 */
	private void setStatus(String decision) {
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {
				DBAccessor.updateStatus(request.getRequestNum(), decision);
				return 1;
			}
		};
		th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
	
	private void handleCancel() {
		dialogStage.close();
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}
