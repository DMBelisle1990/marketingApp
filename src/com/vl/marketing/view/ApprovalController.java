package com.vl.marketing.view;

import com.vl.marketing.model.Request;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ApprovalController {
	
@FXML private TableView<Request> itemTable; 
	
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
	
	private Stage dialogStage;
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setRequest(Request request) {
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
	}
	
}
