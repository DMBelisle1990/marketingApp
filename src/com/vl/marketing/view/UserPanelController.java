package com.vl.marketing.view;

import com.vl.marketing.Main;

import javafx.fxml.FXML;

public class UserPanelController {
	
	private Main main;
	
	@FXML
	private void swapToDash() {
		main.swapToDash();
	}
	
	@FXML
	private void swapToManageUser() {
		main.swapToManageUser();
	}
	
	
	public void setMain(Main main) {
		this.main = main;
	}
}
