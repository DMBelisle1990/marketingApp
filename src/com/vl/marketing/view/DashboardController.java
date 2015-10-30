package com.vl.marketing.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.controlsfx.control.textfield.TextFields;

import com.opencsv.CSVWriter;
import com.vl.marketing.Main;
import com.vl.marketing.db.DBA;
import com.vl.marketing.model.Authorization;
import com.vl.marketing.util.ComboBoxUtil;
import com.vl.marketing.util.PDFGenerator;
import com.vl.marketing.util.SendMailTLS;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;


public class DashboardController {

	// Components of the main authorization table
	@FXML private TableView<Authorization> table;
	@FXML private TableColumn<Authorization, CheckBox> selectAll;
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
	@FXML private TextField vlMarketingNums;
	@FXML private TextField marketingNums;
	@FXML private ComboBox<String> promoTypes;
	@FXML private ComboBox<String> states;
	@FXML private ComboBox<String> items;
	@FXML private DatePicker startDates;
	@FXML private DatePicker endDates;

	// Buttons
	@FXML private Button authorize;
	@FXML private Button reject;
	@FXML private Button delete;
	@FXML private Button email;
	@FXML private Button reset;
	@FXML private Button csv;
	@FXML private Button logout;
	@FXML private Button itemFilter;

	// List of authorizations to be displayed in table
	private ObservableList<Authorization> authorizations = FXCollections.observableArrayList();
	private ObservableList<String> itemsToFilter = FXCollections.observableArrayList();

	// DataBase Access
	private DBA database = new DBA();

	// Other Variables
	private ComboBoxUtil cb;
	private String clickedStatus = "";
	private Main main;
	private CheckBox checkBox = new CheckBox();


	@FXML
	private void initialize() {
		// Link the authorizations list to the table
		authorizations = database.getAuthorizations();

		selectAll.setGraphic(checkBox);


		TableColumn<Authorization, String> customer = new TableColumn<Authorization, String>("company");
		customer.setCellValueFactory(new PropertyValueFactory<Authorization, String>("company"));

		TableColumn<Authorization, String> promoType = new TableColumn<Authorization, String>("promoType");
		promoType.setCellValueFactory(new PropertyValueFactory<Authorization, String>("promoType"));

		TableColumn<Authorization, String> promoDescription = new TableColumn<Authorization, String>("promoDescription");
		promoDescription.setCellValueFactory( new PropertyValueFactory<Authorization, String>("promoDescription"));

		TableColumn<Authorization, String> startDate = new TableColumn<Authorization, String>("startDate");
		startDate.setCellValueFactory(new PropertyValueFactory<Authorization, String>("startDate"));

		TableColumn<Authorization, String> endDate = new TableColumn<Authorization, String>("endDate");
		endDate.setCellValueFactory(new PropertyValueFactory<Authorization, String>("endDate"));

		TableColumn<Authorization, Number> forecast = new TableColumn<Authorization, Number>("forecast");
		forecast.setCellValueFactory(new PropertyValueFactory<Authorization, Number>("forecast"));

		TableColumn<Authorization, Number> actual = new TableColumn<Authorization, Number>("actual");
		actual.setCellValueFactory(new PropertyValueFactory<Authorization, Number>("actual"));

		TableColumn<Authorization, String> vlMarketingNum = new TableColumn<Authorization, String>("vlMarketingNum");
		vlMarketingNum.setCellValueFactory(new PropertyValueFactory<Authorization, String>("vlMarketingNum"));

		TableColumn<Authorization, String> marketingNum = new TableColumn<Authorization, String>("marketingNum");
		marketingNum.setCellValueFactory(new PropertyValueFactory<Authorization, String>("marketingNum"));

		TableColumn<Authorization, String> status = new TableColumn<Authorization, String>("status");
		status.setCellValueFactory(new PropertyValueFactory<Authorization, String>("status"));

		table.setItems(authorizations);
		table.getColumns().setAll(selectAll, customer, promoType, promoDescription, startDate, endDate, forecast, actual, vlMarketingNum, marketingNum, status);

		// TODO MOVE INTO SEPERATE CLASS/METHOD
		status.setCellFactory(column -> {
			return new TableCell<Authorization, String>() {
				@Override
				protected void updateItem(String status, boolean empty) {
					setText(status);
					if(status == null) {
						setStyle("");
					} else {
						if(status.equals("rejected")) 	   setStyle("-fx-text-fill: red");
						else if(status.equals("approved")) setStyle("-fx-text-fill: lightgreen");
						else if(status.equals("accepted")) setStyle("-fx-text-fill: green");
						else if(status.equals("pending"))  setStyle("-fx-text-fill: orange");
						else if(status.equals("archived")) setStyle("-fx-text-fill: grey");
						else if(status.equals("running"))  setStyle("-fx-text-fill: blue");
						else 							   setStyle("-fx-text-fill: black");
					}
				}
			};
		});

		// Populate the ComboBoxes based on authorizations
		cb = new ComboBoxUtil(authorizations);
		populateFilterMenu();

		actual.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

		actual.setOnEditCommit(new EventHandler<CellEditEvent<Authorization,Number>>() {
			@Override public void handle(CellEditEvent t) {
				Authorization k = (Authorization) t.getTableView().getItems().get(t.getTablePosition().getRow());
			}});


		//		table.setOnMouseClicked(event -> {
		//			TablePosition tp = table.getFocusModel().getFocusedCell();
		//			System.out.println(tp);
		//			table.edit(tp.getRow(),  tp.getTableColumn());
		//		});

		// TODO Replace isShiftDown() with proper login system
		table.setRowFactory( tv -> {
			TableRow<Authorization> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				Authorization rowData = row.getItem();
				if (!event.isAltDown() && !event.isShiftDown() && event.getClickCount() == 2 && (!row.isEmpty()) ) {
					setClickedStatus(rowData.getStatus());
					main.showNewAuthorization(this, rowData);
				} else if(event.isShiftDown() && event.getClickCount() == 2 && (!row.isEmpty())) {
					main.showApproval(this, rowData);
				} else if (event.isAltDown() && event.getClickCount() == 2 && (!row.isEmpty())) {
					PDFGenerator pdfGen = new PDFGenerator();
					try {
						pdfGen.setFields(rowData.getCompany(), rowData.getStartDate(), rowData.getEndDate(), rowData.getForecast(), rowData.getStatus(), rowData.getVlMarketingNum(), rowData.getActual(), rowData.getPromoDescription(), rowData.getMarketingNum(), rowData.getPromoType());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			return row ;
		});


		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				for(int i = 0; i < authorizations.size(); i++){
					table.getSelectionModel().select(i, status);
					table.getSelectionModel().getSelectedItem().setChecked(newValue);
				}			
			}
		});


	}


	private void populateFilterMenu() {
		TextFields.bindAutoCompletion(companies, database.getCustomerNames());
		TextFields.bindAutoCompletion(vlMarketingNums, database.getMarketingNums("vl"));
		TextFields.bindAutoCompletion(marketingNums, database.getMarketingNums(""));
		promoTypes.getItems().addAll(cb.getPromoTypes());
		states.getItems().addAll(cb.getStates());
	}



	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                  TABLE FILTERS                   //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////

	@FXML 
	private void filter() {
		// sendEmail("dbelisle@visual-land.com");

		// Rebuilds list from database in case a new authorization has been submitted
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
			
			int temp = 1;
			
			for(String s : itemsToFilter) {
				if(auth.getVlMarketingNum().equals(s)) {
					temp = 0;
				}
			}
			
			if(temp == 1)
				toRemove.add(auth);
		}

		authorizations.removeAll(toRemove);
	}


	@FXML
	private void clearFilters() {
		companies.setText("");
		promoTypes.getSelectionModel().clearSelection();
		vlMarketingNums.setText("");
		marketingNums.setText("");
		states.getSelectionModel().clearSelection();
		startDates.setValue(null);
		endDates.setValue(null);
		itemsToFilter = FXCollections.observableArrayList();
		refreshTable();
	}

	
	@FXML
	private void launchItemFilter() {
		main.showItemFilter(this);
	}
	
	public void filterByItem(ObservableList<String> rightListItems) {
		itemsToFilter = database.get_All_VL_Marketing_Nums_That_Contain_An_Item_In_The_Item_Filter_Menu(rightListItems);
		filter();
	}
	
	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                 BUTTON METHODS                   //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////


	@FXML 
	private void deleteAll() {
		ObservableList<Authorization> toDelete = FXCollections.observableArrayList();
		Authorization auth;

		for(int i = 0; i < authorizations.size(); i++){
			table.getSelectionModel().select(i);
			auth = table.getSelectionModel().getSelectedItem();

			// Only pending and rejected authorizations will be rejected
			if(auth.getChecked() == true && (auth.getStatus().equals("pending") || auth.getStatus().equals("rejected"))) {
				toDelete.add(auth);
			};
		}	
		if(toDelete.size() > 0) {
			database.removeAuthorizations(toDelete);
			refreshTable();
			toDelete.clear();
		}
	}


	@FXML
	private void authorizeAll() {
		ObservableList<Authorization> toAuthorize = FXCollections.observableArrayList();
		Authorization auth;

		for(int i = 0; i < authorizations.size(); i++){
			table.getSelectionModel().select(i);
			auth = table.getSelectionModel().getSelectedItem();
			if(auth.getChecked() == true && auth.getStatus().equals("pending") || auth.getStatus().equals("rejected")) {
				toAuthorize.add(auth);					
			};
		}	

		if(toAuthorize.size() > 0) {
			database.updateStatus(toAuthorize, "approved");
			refreshTable();
			toAuthorize.clear();
		}
	}


	@FXML
	private void rejectAll() {
		ObservableList<Authorization> toReject = FXCollections.observableArrayList();
		Authorization auth;

		for(int i = 0; i < authorizations.size(); i++){
			table.getSelectionModel().select(i);
			auth = table.getSelectionModel().getSelectedItem();
			if(auth.getChecked() == true && auth.getStatus().equals("pending")) {
				toReject.add(auth);					
			};
		}	

		if(toReject.size() > 0) {
			database.updateStatus(toReject, "rejected");
			refreshTable();
			toReject.clear();
		}
	}



	private void sendEmail(String to) {
		SendMailTLS.send(to);
	}

	@FXML
	private void logout() {
		main.close();
		main.showLogin();
	}

	@FXML
	private void generateCSV() {
		try {
			ResultSet rs = database.getResultSet();

			FileWriter writer = new FileWriter("Authorizations.csv");
			
			CSVWriter csvWriter = new CSVWriter(writer);
			csvWriter.writeAll(rs, true);

			writer.close();
			
			Runtime.getRuntime().exec("Authorizations.csv");

			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}



	//////////////////////////////////////////////////////////////////////
	//////////													//////////
	//////////                      OTHER			            //////////
	////////// 													//////////
	//////////////////////////////////////////////////////////////////////

	public void setClickedStatus(String status) {
		clickedStatus = status;
	}

	public String getClickedStatus() {
		return clickedStatus;
	}

	@FXML
	private void newRequest() {
		setClickedStatus("");
		main.showNewAuthorization(this, null);
	}

	public void refreshTable() {
		authorizations = database.getAuthorizations();
		table.setItems(authorizations);
		filter();
		selectAll.setGraphic(new CheckBox());
	}


	public void setMain(Main main) {
		this.main = main;
	}

	public void addAuthorization(Authorization a) {
		authorizations.add(a);
		database.addAuthorization(a);
	}


}

