package com.vl.marketing.view;

import com.vl.marketing.db.DBA;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewContactController {

	@FXML private TextField name;
	@FXML private TextField title;
	@FXML private TextField phone;
	@FXML private TextField fax;
	@FXML private TextField email;
	@FXML private ComboBox<String> customers;
	
	private DBA database = new DBA();
	private Stage dialogStage;
	
	@FXML
	private void initialize() {
		customers.getItems().addAll(database.getCustomerNames());
	}
	
	@FXML
	private void addContact() {
		database.addContact(name.getText(), title.getText(), phone.getText(), fax.getText(), email.getText(), customers.getValue());
		dialogStage.close();
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
}
