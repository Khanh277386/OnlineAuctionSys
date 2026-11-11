package com.auction.client.controller;

import com.auction.client.App;
import com.auction.client.store.InMemoryStore;
import com.auction.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    public void onConnectClick(ActionEvent event) {
        String username = usernameField.getText() != null ? usernameField.getText().trim() : "";
        String password = passwordField.getText() != null ? passwordField.getText() : "";

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "VALIDATION ERROR", "Please enter both username and password!");
            return;
        }

        User authenticatedUser = InMemoryStore.login(username, password);
        if (authenticatedUser == null) {
            showAlert(Alert.AlertType.ERROR, "LOGIN FAILED", "Invalid username or password!");
            return;
        }

        switch (authenticatedUser.getRole()) {
            case User.ROLE_SELLER -> switchScene(event, "/com/auction/client/view/seller-main-view.fxml", "Auction System - Seller Dashboard");
            case User.ROLE_ADMIN  -> switchScene(event, "/com/auction/client/view/admin-main-view.fxml", "Auction System - Admin Panel");
            default               -> switchScene(event, "/com/auction/client/view/bidder-main-view.fxml", "Auction System - Live Auction Market");
        }
    }

    @FXML
    public void onBackClick(ActionEvent event) {
        switchScene(event, "/com/auction/client/view/login-or-register-view.fxml", "Auction System - Welcome");
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "NAVIGATION ERROR",
                    "Cannot open view [" + fxmlPath + "]:\n" + e.getMessage());
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