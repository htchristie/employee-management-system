package com.udemy.employeemanagementsystem.controller;

import com.udemy.employeemanagementsystem.Main;
import com.udemy.employeemanagementsystem.model.services.DepartmentService;
import com.udemy.employeemanagementsystem.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem employee;

    @FXML
    private MenuItem department;

    @FXML
    private MenuItem about;

    @FXML
    protected void onMenuItemEmployeeSelected() {
        System.out.println("You selected employee");
    }

    @FXML
    protected void onMenuItemDepartmentSelected() {

        loadDepartmentTableView("department-list.fxml");
    }

    @FXML
    protected void onMenuItemHelpSelected() {
        loadView("about-view.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public synchronized void loadView(String viewPath) {
        // synchronized -> guarantees that the multi-thread process won't be interrupted
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(viewPath));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVbox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVbox.getChildren().get(0); // gets menu
            mainVbox.getChildren().clear(); // clears all children
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(newVBox.getChildren());

        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading View", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public synchronized void loadDepartmentTableView(String viewPath) {
        // synchronized -> guarantees that the multi-thread process won't be interrupted
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(viewPath));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVbox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVbox.getChildren().get(0); // gets menu
            mainVbox.getChildren().clear(); // clears all children
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(newVBox.getChildren());

            DepartmentListController controller = loader.getController();
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();

        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading View", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}