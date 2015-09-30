package com.vl.marketing.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import com.vl.marketing.db.DBAccessor;
import com.vl.marketing.model.Item;

public class ItemNewController {
	
	@FXML private TextField originalSRPField;
	@FXML private TextField normalCostField;
	@FXML private TextField promoPriceField;
	@FXML private TextField promoCostField;
	@FXML private TextField berField;
	
	@FXML private ComboBox<String> vlField;
	@FXML private ComboBox<String> skuField;
	@FXML private ComboBox<String> typeBox;
	
	private RequestNewController caller;
	private Stage dialogStage;
	private double normalCost = 0;
	private double promoCost = 0;
	private boolean match1;
	private boolean match2;
	private Item item;
	private static final double PROMO_SCALE = 1.1;
	
	private static List<Double> prices = new ArrayList<Double>();
	
	
	@FXML
	private void initialize() {
		typeBox.getItems().clear();
		typeBox.getItems().addAll(
							"Online/Retail"
							);
		
		setAsNumberField(normalCostField);
		setAsNumberField(promoCostField);
		setAsNumberField(promoPriceField);
		setAsNumberField(originalSRPField);
		setAsNumberField(berField);
	}
	
	private void setAsNumberField(TextField t) {
		t.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
		        if (newPropertyValue && !t.getText().contains("$")) t.setText("$" + t.getText());
		    }
		});
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
			berField.setText("$" + Double.toString(normalCost - promoCost));
		} else {
			berField.setText("");
		}
	}
	
	@FXML
	private void calculatePromoPrice() {
		promoPriceField.setText((Double.parseDouble(promoCostField.getText().replace("$", "")) * PROMO_SCALE) + "");
	}
	
	@FXML
	private void handleAdd() {
		try {
			item = new Item(
					vlField.getValue(),
					skuField.getValue(),
					typeBox.getValue(),
					Double.parseDouble(originalSRPField.getText().replace("$", "")),
					Double.parseDouble(normalCostField.getText().replace("$", "")),
					Double.parseDouble(promoPriceField.getText().replace("$", "")),
					Double.parseDouble(promoCostField.getText().replace("$", "")),
					Double.parseDouble(berField.getText().replace("$", "")),
					caller.getRequestNum()
					);
			caller.addItem(item);
			dialogStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void setVL() {
		vlField.setValue(DBAccessor.getVL(skuField.getValue(), caller.getCompanyName()));
		setPriceFields();
	}
	
	@FXML
	private void setSKU() {
		skuField.setValue(DBAccessor.getSKU(vlField.getValue(), caller.getCompanyName()));
		setPriceFields();
	}
	
	private void setPriceFields() {
		prices = DBAccessor.getPrices(vlField.getValue());
		if(prices.isEmpty()) {
			originalSRPField.setText("");
			normalCostField.setText("");
		} else {
			originalSRPField.setText("$" + prices.get(0));
			normalCostField.setText("$" + prices.get(1));
		}
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setCaller(RequestNewController caller) {
		this.caller = caller;
		vlField.getItems().addAll(DBAccessor.getProductNumbers(caller.getCompanyName(), "number"));
		skuField.getItems().addAll(DBAccessor.getProductNumbers(caller.getCompanyName(), "sku"));
	}
}
