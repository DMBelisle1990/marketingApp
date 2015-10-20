package com.vl.marketing.view;

import java.util.ArrayList;

import org.controlsfx.control.textfield.TextFields;

import com.vl.marketing.Main;
import com.vl.marketing.db.DBA;
import com.vl.marketing.model.Authorization;
import com.vl.marketing.util.ComboBoxUtil;
import com.vl.marketing.util.DummyData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DashboardController {

	// Components of the main authorization table
	@FXML private TableView<Authorization> table;
	@FXML private TableColumn<String, CheckBox> selectAll;
	@FXML private TableColumn<Authorization, String> customer;
	@FXML private TableColumn<Authorization, String> promoType;
	@FXML private TableColumn<Authorization, String> promoDescription;
	@FXML private TableColumn<Authorization, String> startDate;
	@FXML private TableColumn<Authorization, String> endDate;
	@FXML private TableColumn<Authorization, Number> forecast;
	@FXML private TableColumn<Authorization, Number> actual;
	@FXML private TableColumn<Authorization, String> vlMarketingNum;
	@FXML private TableColumn<Authorization, String> marketingNum;
	@FXML private TableColumn<Authorization, String> status;
	
	// ComboBoxes for the filter menu
	@FXML private TextField companies;
	@FXML private ComboBox<String> promoTypes;
	@FXML private ComboBox<String> states;
	@FXML private ComboBox<String> items;
	@FXML private ComboBox<String> vlMarketingNums;
	@FXML private ComboBox<String> marketingNums;
	@FXML private DatePicker startDates;
	@FXML private DatePicker endDates;
	
	// List of authorizations to be displayed in table
	private static ObservableList<Authorization> authorizations = FXCollections.observableArrayList();
	
	// DataBase Access
	private DBA database = new DBA();
	
	// Other Variables
	private ComboBoxUtil cb;
	@FXML private Button reset;
	private Main main;
	
	
	@FXML
	private void initialize() {
		// Link the authorizations list to the table
		authorizations = database.getAuthorizations();
		table.setItems(authorizations);
		
		// Bind the authorization properties to their respective table column
		selectAll.setGraphic(new CheckBox());
		customer.setCellValueFactory(cellData -> cellData.getValue().companyProperty());
		promoType.setCellValueFactory(cellData -> cellData.getValue().promoTypeProperty());
		promoDescription.setCellValueFactory(cellData -> cellData.getValue().promoDescriptionProperty());
		startDate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
		endDate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
		forecast.setCellValueFactory(cellData -> cellData.getValue().forecastProperty());
		actual.setCellValueFactory(cellData -> cellData.getValue().actualProperty());
		vlMarketingNum.setCellValueFactory(cellData -> cellData.getValue().vlMarketingNumProperty());
		marketingNum.setCellValueFactory(cellData -> cellData.getValue().marketingNumProperty());
		status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		
		// MOVE INTO SEPERATE CLASS/METHOD
		status.setCellFactory(column -> {
			return new TableCell<Authorization, String>() {
				@Override
				protected void updateItem(String status, boolean empty) {
					setText(status);
					if(status == null) {
						setStyle("");
					} else {
						if (status.equals("rejected")) {
							setStyle("-fx-text-fill: red");
						} else if(status.equals("approved")) {
							setStyle("-fx-text-fill: lightgreen");
						} else if(status.equals("accepted")) {
							setStyle("-fx-text-fill: green");
						} else if(status.equals("pending")) {
							setStyle("-fx-text-fill: orange");
						} else if(status.equals("archived")) {
							setStyle("-fx-text-fill: grey");
						} else if(status.equals("running")) {
							setStyle("-fx-text-fill: blue");
						} else {
							setStyle("-fx-text-fill: black");
						}
					}
				}
			};
		});
		
		// Populate the ComboBoxes based on authorizations
		cb = new ComboBoxUtil(authorizations);
		populateFilterMenu();
		
	}
	
	//TODO see autoComplete textfield below
	private void populateFilterMenu() {
		// If I stick with a textField refine this process
		TextFields.bindAutoCompletion(companies, DummyData.getCompanies());
		
		promoTypes.getItems().addAll(cb.getPromoTypes());
		states.getItems().addAll(cb.getStates());
		vlMarketingNums.getItems().addAll(cb.getVlMarketingNums());
		marketingNums.getItems().addAll(cb.getMarketingNums());
		
	}
	
	
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                  TABLE FILTERS                   //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////
	
	@FXML 
	private void filter() {
		// Rebuild list from database in case a new authorization has been submitted
		authorizations = database.getAuthorizations();
		table.setItems(authorizations);
		ArrayList<Authorization> toRemove = new ArrayList<Authorization>();

		for(Authorization auth : table.getItems()) {
			// This if statement checks the selections in each filter
			if( 
				( states.getValue() != null && !auth.getStatus().equals(states.getValue()) ) ||
			    ( promoTypes.getValue() != null && !auth.getPromoType().equals(promoTypes.getValue()) ) ||	
			    ( startDates.getValue() != null && cb.isBefore(auth.getStartDate(), startDates.getValue().toString()) ) ||
			    ( endDates.getValue() != null && !cb.isBefore(auth.getEndDate(), endDates.getValue().toString()) ) ||
			    ( !auth.getCompany().toLowerCase().contains(companies.getText().toLowerCase()) )
			  ) { 
					toRemove.add(auth);
			}
			
		}
		authorizations.removeAll(toRemove);
	}
	
	
	@FXML
	private void clearFilters() {
		companies.setText("");
		promoTypes.getSelectionModel().clearSelection();
		vlMarketingNums.getSelectionModel().clearSelection();
		marketingNums.getSelectionModel().clearSelection();
		states.getSelectionModel().clearSelection();
		startDates.setValue(null);
		endDates.setValue(null);
	}
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                 END OF FILTERS                   //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////
	
	@FXML
	private void newRequest() {
		main.showRequestNew();
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public static void addAuthorization(Authorization a) {
		authorizations.add(a);
	}
	
	
	
}

