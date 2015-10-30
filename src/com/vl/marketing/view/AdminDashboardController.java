package com.vl.marketing.view;

import com.vl.marketing.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminDashboardController {
	
	private Main main;
	@FXML private Button dashButton;
	
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	private void loadDashboard() {
		main.showDashboard();
	}
	
	@FXML
	private void loadPrivileges() {
		main.showManagePrivilege();
	}
	
	
	public void setMain(Main main) {
		this.main = main;
	}
}
