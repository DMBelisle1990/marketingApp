package com.vl.marketing.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.vl.marketing.db.DBA;

public class CustomerItemNewController {

	@FXML private TextField customer;
	@FXML private TextField model;
	@FXML private TextField sku;
	@FXML private TextField price;
	@FXML private TextField normalCost;
	
	
	private Stage dialogStage;
	private DBA database = new DBA();

	
	@FXML
	private void initialize() {

	}
	
	@FXML
	private void handleAdd() {		
		database.addCustomerItem(customer.getText(), model.getText(), sku.getText(), price.getText(), normalCost.getText());
		dialogStage.close();	
	}
	
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}
