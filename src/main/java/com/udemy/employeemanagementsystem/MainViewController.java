package com.udemy.employeemanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
        System.out.println("You selected department");
    }

    @FXML
    protected void onMenuItemHelpSelected() {
        System.out.println("You selected help");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}