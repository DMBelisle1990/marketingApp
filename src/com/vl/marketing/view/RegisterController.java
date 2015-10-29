package com.vl.marketing.view;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.vl.marketing.db.DBA;
import com.vl.marketing.model.Accountant;
import com.vl.marketing.model.Admin;
import com.vl.marketing.model.Manager;
import com.vl.marketing.model.Sales;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
	
	@FXML private TextField username;
	@FXML private TextField name;
	@FXML private TextField title;
	@FXML private TextField email;
	@FXML private TextField phone;
	@FXML private TextField ext;
	@FXML private TextField fax;
	
	@FXML private PasswordField password;
	@FXML private PasswordField confirm;
	
	@FXML private ComboBox<String> rank;
	
	private DBA database = new DBA();
	private Stage dialogStage;

	@FXML
	private void initialize() {
		rank.getItems().addAll("ACCOUNTING", "ADMIN", "MANAGEMENT", "SALES");
	}
	
	
	@FXML
	private void addUser()
		throws NoSuchAlgorithmException, InvalidKeySpecException 
	{
		String userRank = rank.getValue();
		
		if(validInput()) {
			if(userRank.equals("ACCOUNTING")) 
				database.addUser(new Accountant(name.getText(), username.getText(), title.getText(), email.getText(), phone.getText(), ext.getText(), fax.getText(), rank.getValue(), 0), password.getText());
			else if(userRank.equals("ADMIN")) 
				database.addUser(new Admin(name.getText(), username.getText(), title.getText(), email.getText(), phone.getText(), ext.getText(), fax.getText(), rank.getValue(), 0), password.getText());
			else if(userRank.equals("MANAGEMENT")) 
				database.addUser(new Manager(name.getText(), username.getText(), title.getText(), email.getText(), phone.getText(), ext.getText(), fax.getText(), rank.getValue(), 0), password.getText());
			else if(userRank.equals("SALES")) 
				database.addUser(new Sales(name.getText(), username.getText(), title.getText(), email.getText(), phone.getText(), ext.getText(), fax.getText(), rank.getValue(), 0), password.getText());
			
			
			dialogStage.close();
		}
	}
	
	
	private boolean validInput() {
		return true;
	}
	
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}
