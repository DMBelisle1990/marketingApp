package com.vl.marketing.view;

import com.vl.marketing.MainApp;
import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Request;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ApprovalOverviewController {

	@FXML private ListView<String> requestNumList;
	@FXML private TextField searchBar;
	private String input;
	private String entryText;
	private Request request;
	private Stage dialogStage;
	private MainApp mainApp;
	
	
	private final ObservableList<String> pendingRequests =
	        FXCollections.observableArrayList(
	           DBAccessor.getRequestNums(true)
	        );
	
	@FXML
	private void initialize() {
		requestNumList.setItems(pendingRequests);
		requestNumList.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent e) {
	        	if (e.getClickCount() == 2) {
	        		request = DBAccessor.getRequest(requestNumList.getSelectionModel().getSelectedItem());
	            	mainApp.showApproval(request);
	            	handleCancel();
	        	}
	        }
	    });
	}
	
	@FXML
	private void search() {
		input = searchBar.getText().toLowerCase();
		ObservableList<String> subentries = FXCollections.observableArrayList();
		for(Object entry : pendingRequests) {
		    entryText = (String) entry;
			if (entryText.toLowerCase().contains(input)) subentries.add(entryText);
		}
		requestNumList.setItems(subentries);
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
}
