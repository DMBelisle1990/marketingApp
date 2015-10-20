package com.vl.marketing.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Authorization {

	private final StringProperty company;
	private final StringProperty promoType;
	private final StringProperty promoDescription;
	private final StringProperty startDate;
	private final StringProperty endDate;
	private final StringProperty vlMarketingNum;
	private final StringProperty marketingNum;
	private final StringProperty status;
	private final DoubleProperty forecast;
	private final DoubleProperty actual;
	
	
	public Authorization(String company, String promoType, String promoDescription, String startDate, String endDate,
						 String vlMarketingNum, String marketingNum, String status, Double forecast, Double actual) {
		
		this.company 		  = new SimpleStringProperty(company);
		this.promoType		  = new SimpleStringProperty(promoType);
		this.promoDescription = new SimpleStringProperty(promoDescription);
		this.startDate		  = new SimpleStringProperty(startDate);
		this.endDate 		  = new SimpleStringProperty(endDate);
		this.vlMarketingNum   = new SimpleStringProperty(vlMarketingNum);
		this.marketingNum 	  = new SimpleStringProperty(marketingNum);
		this.status 		  = new SimpleStringProperty(status);
		this.forecast 		  = new SimpleDoubleProperty(forecast);
		this.actual 		  = new SimpleDoubleProperty(actual);
		
	}
	
	
	//TODO Implement setters IF NEEDED
	public String getCompany() {
		return company.get();
	}
	
	public StringProperty companyProperty() {
		return company;
	}
	
	
	
	public String getPromoType() {
		return promoType.get();
	}
	
	public StringProperty promoTypeProperty() {
		return promoType;
	}
	
	
	
	public String getPromoDescription() {
		return promoDescription.get();
	}
	
	public StringProperty promoDescriptionProperty() {
		return promoDescription;
	}
	
	
	
	public String getStartDate() {
		return startDate.get();
	}
	
	public StringProperty startDateProperty() {
		return startDate;
	}
	
	
	
	public String getEndDate() {
		return endDate.get();
	}
	
	public StringProperty endDateProperty() {
		return endDate;
	}
	
	
	
	public String getVlMarketingNum() {
		return vlMarketingNum.get();
	}
	
	public StringProperty vlMarketingNumProperty() {
		return vlMarketingNum;
	}
	
	
	
	public String getMarketingNum() {
		return marketingNum.get();
	}
	
	public StringProperty marketingNumProperty() {
		return marketingNum;
	}
	

	
	public String getStatus() {
		return status.get();
	}
	
	public StringProperty statusProperty() {
		return status;
	}

	
	
	public Double getForecast() {
		return forecast.get();
	}
	
	public DoubleProperty forecastProperty() {
		return forecast;
	}
	
	
	
	public Double getActual() {
		return actual.get();
	}
	
	public DoubleProperty actualProperty() {
		return actual;
	}
	
	
	
}
