package com.vl.marketing.view;

import com.vl.marketing.db.DBA;
import com.vl.marketing.util.ComboBoxUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewCustomerController {

	@FXML private TextField name;
	@FXML private TextField address;
	@FXML private TextField city;
	@FXML private TextField zip;
	@FXML private TextField webAddress;
	@FXML private ComboBox<String> state;
	
	private ComboBoxUtil cb = new ComboBoxUtil();
	private DBA database = new DBA();
	private Stage dialogStage;
	
	
	@FXML
	private void initialize() {
		state.getItems().addAll(cb.getStateAbbr());
	}
	
	@FXML
	private void addCustomer() {
		database.addCustomer(name.getText(), address.getText(), city.getText(), state.getValue(), zip.getText(), webAddress.getText());
		dialogStage.close();
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
}
