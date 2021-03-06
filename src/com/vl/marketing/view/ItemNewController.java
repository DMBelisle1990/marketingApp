package com.vl.marketing.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vl.marketing.Main;
import com.vl.marketing.db.DBA;
import com.vl.marketing.model.Item;

public class ItemNewController {
	
	@FXML private TextField originalSRPField;
	@FXML private TextField normalCostField;
	@FXML private TextField promoPriceField;
	@FXML private TextField promoCostField;
	@FXML private TextField berField;
	@FXML private TextField quantityField;
	@FXML private TextField allowanceField;
	@FXML private ComboBox<String> vlField;
	@FXML private ComboBox<String> skuField;
	@FXML private ComboBox<String> typeBox;
	
	private NewAuthorizationController caller;
	private Stage dialogStage;
	private DBA database = new DBA();
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
	private void handlePlus() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/AddCustomerItem.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage newStage = new Stage();
			newStage.setTitle("New Item");
			newStage.initModality(Modality.WINDOW_MODAL);
			//dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			newStage.setScene(scene);

			CustomerItemNewController controller = loader.getController();
			//controller.setCaller(nac);
			controller.setDialogStage(newStage);
			newStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleAdd() {
		System.out.println("Adding Item with pk " + caller.getRequestNum());
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
					Double.parseDouble(quantityField.getText()),
					Double.parseDouble(allowanceField.getText()),
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
		vlField.setValue(database.getVL(skuField.getValue(), caller.getCompanyName()));
		setPriceFields();
	}
	
	@FXML
	private void setSKU() {
		skuField.setValue(database.getSKU(vlField.getValue(), caller.getCompanyName()));
		setPriceFields();
	}
	
	private void setPriceFields() {
		prices = database.getPrices(skuField.getValue(), caller.getCompanyName());
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
	
	public void setCaller(NewAuthorizationController nac) {
		this.caller = nac;
		vlField.getItems().addAll(database.getProductNumbers(nac.getCompanyName(), "model"));
		skuField.getItems().addAll(database.getProductNumbers(nac.getCompanyName(), "resellerSku"));
	}
}
