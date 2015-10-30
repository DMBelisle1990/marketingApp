package com.vl.marketing.view;

import com.vl.marketing.Main;
import com.vl.marketing.db.DBA;
import com.vl.marketing.model.Item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ItemFilterController {

	@FXML private Label filter;
	@FXML private ListView<String> leftList;
	@FXML private ListView<String> rightList;
	@FXML private TextField textField;
	@FXML private Button add;
	@FXML private Button delete;
	@FXML private Button submit;


	private Main main;
	private Stage dialogStage;
	private DashboardController dashboardController;
	private DBA database = new DBA();
	private ObservableList<String> leftListItems;
	private ObservableList<String> rightListItems;
	private String removedItem;

	@FXML
	private void initialize() {
		leftListItems = database.getDistinctItems();
		rightListItems = FXCollections.observableArrayList();
		
		leftList.setItems(leftListItems);

		rightList.setItems(rightListItems);
		
		leftList.getSelectionModel().select(0);
	}

	@FXML
	private void add() {
		removedItem = leftList.getSelectionModel().getSelectedItem();
		leftListItems.remove(removedItem);
		rightListItems.add(removedItem);
	}

	@FXML
	private void delete() {
		removedItem = rightList.getSelectionModel().getSelectedItem();
		rightListItems.remove(removedItem);
		leftListItems.add(removedItem);		
	}

	@FXML
	private void submit() {
		dashboardController.filterByItem(rightListItems);
		dialogStage.close();
	}


	public void setMain(Main main) {
		this.main = main;
	}
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	public void setDashboardController(DashboardController dashboardController) {
		this.dashboardController = dashboardController;
	}
}
