package com.vl.marketing;

import java.io.IOException;
import java.util.Optional;

import com.vl.marketing.model.Request;
import com.vl.marketing.view.ApprovalController;
import com.vl.marketing.view.ApprovalOverviewController;
import com.vl.marketing.view.ItemNewController;
import com.vl.marketing.view.RequestNewController;
import com.vl.marketing.view.RequestOverviewController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private BorderPane requestNew;
	private BorderPane approval;
	
	/**
	 * The data as an observable list of Requests.
	 */
	private ObservableList<Request> requestData = FXCollections.observableArrayList();
	
	
	public MainApp() {}
	
	public ObservableList<Request> getRequestData() {
		return requestData;
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Request Authorization");

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Log-in");
		alert.setHeaderText("Temporary log-in system");
		ButtonType buttonTypeOne = new ButtonType("Requester");
		ButtonType buttonTypeTwo = new ButtonType("Approver");
		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			initRootLayout();
			showRequestNew();
		} else if (result.get() == buttonTypeTwo) {
			showApprovalOverview();
		}
	}
	
	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load the root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the request overview inside the root layout
	 */
	public void showRequestNew() {
		try {
			// Load request overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RequestNew.fxml"));
			requestNew = (BorderPane) loader.load();
			rootLayout.setCenter(requestNew);
	
			RequestNewController controller = loader.getController();
			controller.setMainApp(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showApproval(Request request) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Approval.fxml"));
			approval = (BorderPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Requests");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(approval);
			dialogStage.setScene(scene);
	
			ApprovalController controller = loader.getController();
			controller.setRequest(request);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showRequestOverview(RequestNewController rnc) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RequestOverview.fxml"));
			BorderPane page = (BorderPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Requests");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			RequestOverviewController controller = loader.getController();
			controller.setCaller(rnc);
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showItemNew(RequestNewController rnc) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ItemNew.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Item");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			ItemNewController controller = loader.getController();
			controller.setCaller(rnc);
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showApprovalOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ApprovalOverview.fxml"));
			BorderPane page = (BorderPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Requests");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			ApprovalOverviewController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
