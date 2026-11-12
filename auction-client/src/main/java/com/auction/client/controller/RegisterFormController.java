package com.auction.client.controller;
import com.auction.client.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class RegisterFormController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void onRegisterSubmit(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();
        if(username.isEmpty() || password.isEmpty()){
            showAlert(Alert.AlertType.ERROR,"REGISTER ERROR","USERNAME OR PASSWORD IS EMPTY");
            return;
        }
        try {
            showAlert(Alert.AlertType.INFORMATION, "SUCCESS","Registration successful! Account: " + username + " (" + role + ")");
            switchToLoginForm(event);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "SYSTEM ERROR", e.getMessage());
        }
    }
    private void switchToLoginForm(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/auction/client/view/login-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 850, 500);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "NAVIGATION ERROR", "Cannot load login-form-view.fxml: " + e.getMessage());
        }
    }
}
