package com.vl.marketing.view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.vl.marketing.model.Item;

public class ItemNewController {
	
	@FXML private TextField vlField;
	@FXML private TextField skuField;
	@FXML private TextField originalSRPField;
	@FXML private TextField normalCostField;
	@FXML private TextField promoPriceField;
	@FXML private TextField promoCostField;
	@FXML private TextField berField;
	
	@FXML private ComboBox<String> itemBox;
	@FXML private ComboBox<String> typeBox;
	
	private RequestNewController caller;
	private Stage dialogStage;
	private double normalCost = 0;
	private double promoCost = 0;
	private boolean match1;
	private boolean match2;
	private Item item;
	
	@FXML
	private void initialize() {
		itemBox.getItems().clear();
		itemBox.getItems().addAll(
							"Item1",
							"Item2"
							);
		typeBox.getItems().clear();
		typeBox.getItems().addAll(
							"Online/Retail"
							);
	}
	
	//Auto-fills the BER field to normalCost - promoCost
	@FXML
	private void calculateBER() {
		match1 = normalCostField.getText().replace("$", "").matches("^[\\d]+([\\.][\\d]+)?$");
		match2 = promoCostField.getText().replace("$", "").matches("^[\\d]+([\\.][\\d]+)?$");
		
		if(match1) {
			normalCost = Double.parseDouble(normalCostField.getText().replace("$", ""));
		}
		if(match2) {
			promoCost = Double.parseDouble(promoCostField.getText().replace("$", ""));
		}
		
		if(match1 && match2) {
			berField.setText(Double.toString(normalCost - promoCost));
		} else {
			berField.setText("");
		}
	}
	
	@FXML
	private void handleAdd() {
		try {
		item = new Item(
					itemBox.getValue(),
					vlField.getText(),
					skuField.getText(),
					typeBox.getValue(),
					Double.parseDouble(originalSRPField.getText()),
					Double.parseDouble(normalCostField.getText()),
					Double.parseDouble(promoPriceField.getText()),
					Double.parseDouble(promoCostField.getText()),
					Double.parseDouble(berField.getText())
				);
		caller.addItem(item);
		dialogStage.close();
		} catch (Exception e) {
			System.out.println("Don't leave fields blank");
		}
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setCaller(RequestNewController caller) {
		this.caller = caller;
	}
}
