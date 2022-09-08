package com.udemy.employeemanagementsystem.controller;

import com.udemy.employeemanagementsystem.db.DbException;
import com.udemy.employeemanagementsystem.listener.DataChangeListener;
import com.udemy.employeemanagementsystem.model.entities.Employee;
import com.udemy.employeemanagementsystem.model.exceptions.ValidationException;
import com.udemy.employeemanagementsystem.model.services.EmployeeService;
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

public class EmployeeFormController implements Initializable {
    // Initializable - Controller initialization interface.

    private Employee employee;
    private EmployeeService service;
    private List<DataChangeListener> listeners = new ArrayList<>();

    @FXML
    private TextField txtEmpId;

    @FXML
    private TextField txtEmpName;

    @FXML
    private TextField txtEmpEmail;

    @FXML
    private TextField txtEmpBirthdate;

    @FXML
    private TextField txtEmpSalary;

    @FXML
    private Label labelEmpNameError;

    @FXML
    private Label labelEmpEmailError;

    @FXML
    private Label labelEmpBirthdateError;

    @FXML
    private Label labelEmpSalaryError;

    @FXML
    private Button btnEmpSave;

    @FXML
    private Button btnEmpCancel;

    public void setEmployee(Employee entity) {
        this.employee = entity;
    }

    public void setEmployeeService(EmployeeService service) {
        this.service = service;
    }

    public void assignDataChangeListener(DataChangeListener listener) {
        listeners.add(listener);
    }

    @FXML
    public void onBtnDepSaveClick(ActionEvent event) {

        if (employee == null) {
            throw new IllegalStateException("Employee is null.");
        }

        if (service == null) {
            throw new IllegalStateException("No service was found.");
        }

        try {
            employee = getFormData();
            service.saveOrUpdate(employee);
            notifyDataChangeListeners();
            Utils.currentStage(event).close(); //closes form
        }
        catch (DbException e) {
            Alerts.showAlert("Error saving employee", null, e.getMessage(), Alert.AlertType.ERROR);
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
        Constraints.setTextFieldInteger(txtEmpId);
        Constraints.setTextFieldMaxLength(txtEmpName, 30);
    }

    public void updateFormData() {
        if (employee == null) {
            throw new IllegalStateException("Employee entity is null.");
        }

        txtEmpId.setText(String.valueOf(employee.getId()));
        txtEmpName.setText(String.valueOf(employee.getName()));
    }

    private Employee getFormData() {
        Employee dep = new Employee();
        ValidationException exception = new ValidationException("Validation error.");

        dep.setId(Utils.parseToInt(txtEmpId.getText()));

        if (txtEmpName.getText() == null || txtEmpName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty.");
        }

        dep.setName(txtEmpName.getText());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return dep;
    }

    private void setErrorMsgs(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("name")) {
            labelEmpNameError.setText(errors.get("name"));
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
