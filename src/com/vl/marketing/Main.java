package com.vl.marketing;

import java.io.IOException;

import com.vl.marketing.model.Authorization;
import com.vl.marketing.util.DummyData;
import com.vl.marketing.view.ApprovalController;
import com.vl.marketing.view.DashboardController;
import com.vl.marketing.view.ItemNewController;
import com.vl.marketing.view.LoginController;
import com.vl.marketing.view.NewAuthorizationController;
import com.vl.marketing.view.NewContactController;
import com.vl.marketing.view.NewCustomerController;
import com.vl.marketing.view.RegisterController;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private BorderPane requestNew;
	private BorderPane approval;
	private Boolean windowAlreadyOpen = false;
	
	
	@Override
	public void start(Stage primaryStage) {
		// Uncomment to generate more data, DELETE ONCE USING ACTUAL DATA
		//DummyData dummy = new DummyData();
		//dummy.populateTable(1000);
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Dashboard");
		
		showLogin();
		//initRootLayout();
		//showDashBoard();
	}
	
	public void close() {
		primaryStage.hide();
	}
	
	
	public void showLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Login.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Log in");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			LoginController controller = loader.getController();
			controller.setMain(this);
			controller.setDialogStage(dialogStage);
			
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void showRegistration() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Register.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Register");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			RegisterController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public void showNewAuthorization(DashboardController caller, Authorization auth) {
		try {
			// Load request overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RequestNew.fxml"));
			requestNew = (BorderPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Request");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(requestNew);
			dialogStage.setScene(scene);
	
			NewAuthorizationController controller = loader.getController();
			controller.setMainApp(this);
			controller.setCaller(caller);
			controller.setEditing();
			controller.setDialogStage(dialogStage);
			if(auth != null) controller.setAuthorization(auth);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches the Approver View of the selected Authorization
	 */
	public void showApproval(DashboardController caller, Authorization authorization) {
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
			controller.setAuthorization(authorization);
			controller.setCaller(caller);
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
	public void showItemNew(NewAuthorizationController nac) {
		if(windowAlreadyOpen == false) {
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

				windowAlreadyOpen = true;
				dialogStage.setOnHidden(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						windowAlreadyOpen = false;
					}
				});

				ItemNewController controller = loader.getController();
				controller.setCaller(nac);
				controller.setDialogStage(dialogStage);
				dialogStage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void showAddCustomer() {
		if(windowAlreadyOpen == false) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("view/AddCustomer.fxml"));
				AnchorPane page = (AnchorPane) loader.load();

				Stage dialogStage = new Stage();
				dialogStage.setTitle("New Customer");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(page);
				dialogStage.setScene(scene);

				windowAlreadyOpen = true;
				dialogStage.setOnHidden(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						windowAlreadyOpen = false;
					}
				});

				NewCustomerController controller = loader.getController();
				controller.setDialogStage(dialogStage);
				dialogStage.showAndWait();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void showAddContact() {
		if(windowAlreadyOpen == false) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("view/AddContact.fxml"));
				AnchorPane page = (AnchorPane) loader.load();

				Stage dialogStage = new Stage();
				dialogStage.setTitle("New Contact");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(page);
				dialogStage.setScene(scene);

				windowAlreadyOpen = true;
				dialogStage.setOnHidden(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						windowAlreadyOpen = false;
					}
				});

				NewContactController controller = loader.getController();
				controller.setDialogStage(dialogStage);
				dialogStage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
