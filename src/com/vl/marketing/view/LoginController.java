package com.vl.marketing.view;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.vl.marketing.Main;
import com.vl.marketing.db.DBA;
import com.vl.marketing.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private Label errorMessage;
	@FXML private AnchorPane window;
	
	private Main main;
	private Stage dialogStage;
	private DBA database = new DBA();
	
	@FXML
	private void initialize() {
		// Pressing Enter will log users in if the correct info is input
		window.setOnKeyPressed(event -> {
			if(event.getCode().equals(KeyCode.ENTER))
				try {
					login();
				} catch (Exception e) {
					e.printStackTrace();
				}
		});
	}
	
	
	@FXML
	private void login() throws NoSuchAlgorithmException, InvalidKeySpecException {
		User user = database.getUser(username.getText(), password.getText());
		if(user != null) {
			dialogStage.close();
			if(user.getRank().equals("ADMIN")) {
				main.showAdminDashboard();
			} else {
				main.initRootLayout();
				main.showDashBoard(user);
			}
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
