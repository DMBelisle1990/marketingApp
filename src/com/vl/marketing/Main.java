package com.vl.marketing;

import java.io.IOException;
import com.vl.marketing.model.Request;
import com.vl.marketing.view.ApprovalController;
import com.vl.marketing.view.DashboardController;
import com.vl.marketing.view.ItemNewController;
import com.vl.marketing.view.RequestNewController;
import com.vl.marketing.view.RequestOverviewController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private BorderPane requestNew;
	private BorderPane approval;
	
	
	@Override
	public void start(Stage primaryStage) {
		// Uncomment to generate more data, DELETE ONCE USING ACTUAL DATA
		// DummyData dummy = new DummyData();
		// dummy.populateTable(1000);
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Dashboard");
		
		initRootLayout();
		
		showDashBoard();
	}
	
	/**
     * Initializes the root layout.
     */
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = loader.load();
			
			// Show the scene containing the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
     * Launches the Authorization DashBoard
     */
	public void showDashBoard() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Dashboard.fxml"));
			AnchorPane dashboard = loader.load();
			
			// Set the dashboard as the center of the root layout
			rootLayout.setCenter(dashboard);
			
			DashboardController controller = loader.getController();
			controller.setMain(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches the New Request Form
	 */
	public void showRequestNew() {
		try {
			// Load request overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RequestNew.fxml"));
			requestNew = (BorderPane) loader.load();
			// rootLayout.setCenter(requestNew);
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Request");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(requestNew);
			dialogStage.setScene(scene);
	
			RequestNewController controller = loader.getController();
			controller.setMainApp(this);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches the Approver View of the selected Authorization
	 */
	public void showApproval(Request request) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Approval.fxml"));
			approval = (BorderPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Requests");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(approval);
			dialogStage.setScene(scene);
	
			ApprovalController controller = loader.getController();
			controller.setRequest(request);
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches an Overview of all Requests
	 */
	public void showRequestOverview(RequestNewController rnc) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RequestOverview.fxml"));
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
	
	/**
	 * Launches a small menu used to attach items to a marketing authorization
	 * @param rnc : The associated request form
	 */
	public void showItemNew(RequestNewController rnc) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ItemNew.fxml"));
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
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

}
