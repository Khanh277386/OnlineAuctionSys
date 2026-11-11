package com.auction.client.controller;

import com.auction.client.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {

    @FXML
    public void onConnectClick(ActionEvent event) throws IOException {
        System.out.println("Đã bấm nút đăng nhập");
    }
}
