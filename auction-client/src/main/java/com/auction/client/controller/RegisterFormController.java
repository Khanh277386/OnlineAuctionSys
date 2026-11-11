package com.auction.client.controller;

import com.auction.client.App;
import com.auction.client.store.InMemoryStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterFormController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    @FXML
    public void onRegisterSubmit(ActionEvent event) {
        String username = usernameField.getText() != null ? usernameField.getText().trim() : "";
        String password = passwordField.getText() != null ? passwordField.getText() : "";
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "VALIDATION ERROR", "Username and Password cannot be empty!");
            return;
        }

        if (role == null || role.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "VALIDATION ERROR", "Please select a role (Bidder or Seller)!");
            return;
        }

        // Lưu tài khoản mới vào RAM thông qua InMemoryStore
        boolean created = InMemoryStore.registerUser(username, password, role);
        if (!created) {
            showAlert(Alert.AlertType.WARNING, "REGISTRATION FAILED", "Username '" + username + "' already exists in RAM!");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "REGISTRATION SUCCESS",
                "Account [" + username + "] registered as " + role + ". You can now log in!");
        switchToLoginForm(event);
    }

    @FXML
    public void onBackClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/auction/client/view/login-or-register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Auction System - Welcome");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "NAVIGATION ERROR", e.getMessage());
        }
    }
    private void switchToLoginForm(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/auction/client/view/login-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Auction System - Login");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "NAVIGATION ERROR", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}