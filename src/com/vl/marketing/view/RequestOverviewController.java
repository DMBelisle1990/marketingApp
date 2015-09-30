package com.vl.marketing.view;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Request;
import com.vl.marketing.util.AlertGenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
	private Thread th;
	private String input;
	private String entryText;
	private Request request;
	private RequestNewController caller;
	private final ContextMenu contextMenu = new ContextMenu();
	
	ArrayList<String> result = new ArrayList<>();

	private final ObservableList<String> data;
	 
	
	private Stage dialogStage;
	
	public RequestOverviewController() {
		data = FXCollections.observableArrayList(DBAccessor.getRequestNums(""));
	}
	
	@FXML
	private void initialize() {
		requestNumList.setItems(data);
		//Adds a context menu that can be used to delete requests from the database
		requestNumList.setContextMenu(contextMenu);
		MenuItem delete = new MenuItem("Delete");
		delete.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(AlertGenerator.confirmation("confirmation", "Are you sure you want to delete this request?", "This action can't be undone")) {
					deleteRequest(requestNumList.getSelectionModel().getSelectedItem());
					data.remove(requestNumList.getSelectionModel().getSelectedItem());
				}
		    }
		});
		contextMenu.getItems().addAll(delete);
		
		//Allows a user to double click a request for possible editing
		requestNumList.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent e) {
	        	caller.resetTable();
	        	MouseButton button = e.getButton();
	        	if (e.getClickCount() == 2 && button == MouseButton.PRIMARY) {
	            	try {
						setRequest();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						e1.printStackTrace();
					}
	            	caller.setRequest(request);
	            	if(request.getApproved() == 1 || request.getRejected() == 1) {
	            		caller.allowEditting(false);
	            	} else {
	            		caller.allowEditting(true);
	            	}
	            	//Probably to be replaced with a setData method when item table in db is set up
	            	handleCancel();
	        	}
	        	if(button == MouseButton.SECONDARY) {
	        		contextMenu.show(dialogStage, e.getScreenX(), e.getScreenY());
	        	}
	        }
	    });
	}
	
	private void setRequest() throws InterruptedException, ExecutionException {
		Task<Request> task = new Task<Request>() {
			@Override protected Request call() throws Exception {
				return DBAccessor.getRequest(requestNumList.getSelectionModel().getSelectedItem());
			}
		};
		th = new Thread(task);
		th.setDaemon(true);
		th.start();
		request = task.get();
	}
	
	/*
	 * Forms a new thread that deletes the selected request from the database
	 */
	private void deleteRequest(String requestNum) {
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {
				DBAccessor.deleteRequest(requestNum);
				return 1;
			}
		};
		th = new Thread(task);
		th.setDaemon(true);
		th.start();
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
