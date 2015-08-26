package com.vl.marketing.view;

import java.util.ArrayList;
import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Request;
import com.vl.marketing.util.AlertGenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RequestOverviewController {
	
	@FXML private ListView<String> requestNumList;
	@FXML private TextField searchBar;
	private String input;
	private String entryText;
	private Request request;
	private RequestNewController caller;
	private final ContextMenu contextMenu = new ContextMenu();
	
	ArrayList<String> result = new ArrayList<>();

	private final ObservableList<String> data =
		        FXCollections.observableArrayList(
		           DBAccessor.getRequestNums(false)
		        );
	 
	
	private Stage dialogStage;
	
	@FXML
	private void initialize() {
		requestNumList.setItems(data);
		//Adds a context menu that can be used to delete requests from the database
		requestNumList.setContextMenu(contextMenu);
		MenuItem delete = new MenuItem("Delete");
		delete.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(AlertGenerator.confirmation("confirmation", "Are you sure you want to delete this request?", "This action can't be undone")) {
					DBAccessor.deleteRequest(requestNumList.getSelectionModel().getSelectedItem());
					data.remove(requestNumList.getSelectionModel().getSelectedIndex());
				}
		    }
		});
		contextMenu.getItems().addAll(delete);
		
		//Allows a user to double click a request for possible editting
		requestNumList.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent e) {
	        	MouseButton button = e.getButton();
	        	if (e.getClickCount() == 2 && button == MouseButton.PRIMARY) {
	            	request = DBAccessor.getRequest(requestNumList.getSelectionModel().getSelectedItem());
	            	caller.setRequest(request);
	            	//Probably to be replaced with a setData method when item table in db is set up
	            	caller.resetTable();
	            	handleCancel();
	        	}
	        	if(button == MouseButton.SECONDARY) {
	        		contextMenu.show(dialogStage, e.getScreenX(), e.getScreenY());
	        	}
	        }
	    });
	}
	
	@FXML
	private void search() {
		input = searchBar.getText().toLowerCase();
		ObservableList<String> subentries = FXCollections.observableArrayList();
		for(Object entry : data) {
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
	
	public void setCaller(RequestNewController caller) {
		this.caller = caller;
	}
	
}
