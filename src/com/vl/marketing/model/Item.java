package com.vl.marketing.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
	
	private final StringProperty itemName;
	private final StringProperty vlNum;
	private final StringProperty sku;
	private final StringProperty type;
	private final DoubleProperty originalSRP;
	private final DoubleProperty normalCost;
	private final DoubleProperty promoPrice;
	private final DoubleProperty promoCost;
	private final DoubleProperty ber;

	public Item(String itemName, String vlNum, String sku, String type,
			Double originalSRP, Double normalCost, Double promoPrice,
			Double promoCost, Double ber) {
		this.itemName = new SimpleStringProperty(itemName);
		this.vlNum = new SimpleStringProperty(vlNum);
		this.sku = new SimpleStringProperty(sku);
		this.type = new SimpleStringProperty(type);
		this.originalSRP = new SimpleDoubleProperty(originalSRP);
		this.normalCost = new SimpleDoubleProperty(normalCost);
		this.promoPrice = new SimpleDoubleProperty(promoPrice);
		this.promoCost = new SimpleDoubleProperty(promoCost);
		this.ber = new SimpleDoubleProperty(ber);
	}
	
	public StringProperty itemNameProperty() { return itemName; }
	
	public StringProperty vlNumProperty() { return vlNum; }

	public StringProperty skuProperty() { return sku; }
	
	public StringProperty typeProperty() { return type; }
	
	public DoubleProperty originalSRPProperty() { return originalSRP; }
	
	public DoubleProperty normalCostProperty() { return normalCost; }
	
	public DoubleProperty promoPriceProperty() { return promoPrice; }
	
	public DoubleProperty promoCostProperty() { return promoCost; }
	
	public DoubleProperty berProperty() { return ber; }

}
