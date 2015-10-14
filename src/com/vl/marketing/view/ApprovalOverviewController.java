package com.vl.marketing.view;


import java.util.concurrent.ExecutionException;

import com.vl.marketing.MainApp;
import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Request;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ApprovalOverviewController {

	@FXML private ListView<String> requestNumList;
	@FXML private TextField searchBar;
	@FXML private ComboBox<String> filter;
	private String input;
	private String entryText;
	private MainApp mainApp;
	private Thread th;
	
	private ObservableList<String> pendingRequests;
	private ObservableList<String> approvedRequests;
	private ObservableList<String> rejectedRequests;
	private ObservableList<String> activeList;
	
	public ApprovalOverviewController()  {}
	
	@FXML
	private void initialize() throws InterruptedException, ExecutionException {
		requestNumList.setItems(pendingRequests);
		setList("A");
		setList("R");
		setList("P");
		requestNumList.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent e) {
	        	if (e.getClickCount() == 2) {
	        		try {
						fetchAndShowRequest();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						e1.printStackTrace();
					}
	        	}
	        }
	    });
	}
	
	private void fetchAndShowRequest() throws InterruptedException, ExecutionException {
		Task<Request> task = new Task<Request>() {
			@Override protected Request call() throws Exception {
				return DBAccessor.getRequest(requestNumList.getSelectionModel().getSelectedItem());
			}
		};
		th = new Thread(task);
		th.setDaemon(true);
		th.start();
		mainApp.showApproval(task.get(), this);
	}
	
	public void removeRequest(String requestNum, String list) {
		if(list == "pending") {
			pendingRequests.remove(requestNum);
		} else if(list == "approved") {
			approvedRequests.remove(requestNum);
		} else if(list == "rejected") {
			rejectedRequests.remove(requestNum);
		}
	}
	
	@FXML
	private void search() {
		input = searchBar.getText().toLowerCase();
		ObservableList<String> subentries = FXCollections.observableArrayList();
		for(Object entry : activeList) {
		    entryText = (String) entry;
			if (entryText.toLowerCase().contains(input)) subentries.add(entryText);
		}
		requestNumList.setItems(subentries);
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	
	@FXML 
	private void filterResults() throws InterruptedException, ExecutionException {
		switch(filter.getValue()) {
			case "All": setList(""); break;
			case "Approved": setList("A"); break;
			case "Rejected": setList("R"); break;
			case "Pending": setList("P"); break;
		}
	}
	
	/**
	 * Creates a new thread and updates the list view based on approvers selection.
	 */
	private void setList(String filter) throws InterruptedException, ExecutionException {
		Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
			@Override protected ObservableList<String> call() throws Exception {
				return FXCollections.observableArrayList(DBAccessor.getRequestNums(filter));
			}
		};
		th = new Thread(task);
		th.setDaemon(true);
		th.start();
		if(filter == "P") {
			pendingRequests = task.get();
			requestNumList.setItems(pendingRequests);
			activeList = pendingRequests;
		} else if(filter == "A") {
			approvedRequests = task.get();
			requestNumList.setItems(approvedRequests);
			activeList = approvedRequests;
		} else if(filter == "R") {
			rejectedRequests = task.get();
			requestNumList.setItems(rejectedRequests);
			activeList = rejectedRequests;
		}
	}
	
}
