package com.auction.client.controller;

import com.auction.client.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginOrRegisterController {

    @FXML
    public void onLoginClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/auction/client/view/login-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 500);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    @FXML
    public void onRegisterClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/auction/client/view/register-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 500);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
