package com.vl.marketing.view;

import com.vl.marketing.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private Label errorMessage;
	
	private Main main;
	private Stage dialogStage;
	
	@FXML
	private void initialize() {
		
	}
	
	
	@FXML
	private void login() {
		if(!username.getText().equals("")) {
			dialogStage.close();
			main.initRootLayout();
			main.showDashBoard();
		} else {
			errorMessage.setText("Invalid Username/Password");
		}
	}
	
	@FXML
	private void register() {
		main.showRegistration();
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
}
