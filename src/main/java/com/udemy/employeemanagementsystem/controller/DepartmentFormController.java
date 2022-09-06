package com.udemy.employeemanagementsystem.controller;

import com.udemy.employeemanagementsystem.db.DbException;
import com.udemy.employeemanagementsystem.listener.DataChangeListener;
import com.udemy.employeemanagementsystem.model.entities.Department;
import com.udemy.employeemanagementsystem.model.exceptions.ValidationException;
import com.udemy.employeemanagementsystem.model.services.DepartmentService;
import com.udemy.employeemanagementsystem.util.Alerts;
import com.udemy.employeemanagementsystem.util.Constraints;
import com.udemy.employeemanagementsystem.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {
    // Initializable - Controller initialization interface.

    private Department department;
    private DepartmentService service;
    private List<DataChangeListener> listeners = new ArrayList<>();

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

    public void setDepartment(Department entity) {
        this.department = entity;
    }

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    public void assignDataChangeListener(DataChangeListener listener) {
        listeners.add(listener);
    }

    @FXML
    public void onBtnDepSaveClick(ActionEvent event) {

        if (department == null) {
            throw new IllegalStateException("Department is null.");
        }

        if (service == null) {
            throw new IllegalStateException("No service was found.");
        }

        try {
            department = getFormData();
            service.saveOrUpdate(department);
            notifyDataChangeListeners();
            Utils.currentStage(event).close(); //closes form
        }
        catch (DbException e) {
            Alerts.showAlert("Error saving department", null, e.getMessage(), Alert.AlertType.ERROR);
        }
        catch (ValidationException e) {
            setErrorMsgs(e.getErrors());
        }
    }

    @FXML
    public void onBtnDepCancelClick(ActionEvent event) {

        Utils.currentStage(event).close();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtDepId);
        Constraints.setTextFieldMaxLength(txtDepName, 30);
    }

    public void updateFormData() {
        if (department == null) {
            throw new IllegalStateException("Department entity is null.");
        }

        txtDepId.setText(String.valueOf(department.getId()));
        txtDepName.setText(String.valueOf(department.getName()));
    }

    private Department getFormData() {
        Department dep = new Department();
        ValidationException exception = new ValidationException("Validation error.");

        dep.setId(Utils.parseToInt(txtDepId.getText()));

        if (txtDepName.getText() == null || txtDepName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty.");
        }

        dep.setName(txtDepName.getText());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return dep;
    }

    private void setErrorMsgs(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("name")) {
            labelDepNameError.setText(errors.get("name"));
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener: listeners) {
            listener.onDataChanged();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Called to initialize a controller after its root element has been completely processed.
        // location - The location used to resolve relative paths for the root object
        // resources - The resources used to localize the root object, or null if the root object was not localized.

        initializeNodes();
    }
}
