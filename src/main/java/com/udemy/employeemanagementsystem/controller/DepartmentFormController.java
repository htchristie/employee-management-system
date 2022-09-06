package com.udemy.employeemanagementsystem.controller;

import com.udemy.employeemanagementsystem.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {
    // Initializable - Controller initialization interface.

    @FXML
    private TextField txtDepId;

    @FXML
    private TextField txtDepName;

    @FXML
    private Label labelDepNameError;

    @FXML
    private Button btnDepSave;

    @FXML
    private Button btnDepCancel;

    @FXML
    public void onBtnDepSaveClick() {
        System.out.println("save");
    }

    @FXML
    public void onBtnDepCancelClick() {
        System.out.println("cancel");
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtDepId);
        Constraints.setTextFieldMaxLength(txtDepName, 30);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Called to initialize a controller after its root element has been completely processed.
        // location - The location used to resolve relative paths for the root object
        // resources - The resources used to localize the root object, or null if the root object was not localized.

        initializeNodes();
    }
}
