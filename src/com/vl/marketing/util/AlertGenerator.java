package com.vl.marketing.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class AlertGenerator {

	private AlertGenerator() {}
	
	public static void newAlert(AlertType type, String title, String headerText, String contentText) {
		Alert alert = new Alert(type, contentText);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.showAndWait();
	}
	
	public static boolean confirmation(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String textInput(String title, String headerText, String contentText) {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);
		Optional<String> result = dialog.showAndWait();
		return result.isPresent() ? result.get() : "NO_MSG";
	}
}
