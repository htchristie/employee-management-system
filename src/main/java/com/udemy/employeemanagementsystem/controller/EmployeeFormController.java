package com.udemy.employeemanagementsystem.controller;

import com.udemy.employeemanagementsystem.db.DbException;
import com.udemy.employeemanagementsystem.listener.DataChangeListener;
import com.udemy.employeemanagementsystem.model.entities.Department;
import com.udemy.employeemanagementsystem.model.entities.Employee;
import com.udemy.employeemanagementsystem.model.exceptions.ValidationException;
import com.udemy.employeemanagementsystem.model.services.DepartmentService;
import com.udemy.employeemanagementsystem.model.services.EmployeeService;
import com.udemy.employeemanagementsystem.util.Alerts;
import com.udemy.employeemanagementsystem.util.Constraints;
import com.udemy.employeemanagementsystem.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class EmployeeFormController implements Initializable {
    // Initializable - Controller initialization interface.

    private Employee employee;
    private EmployeeService service;

    private DepartmentService departmentService;
    private List<DataChangeListener> listeners = new ArrayList<>();

    @FXML
    private TextField txtEmpId;

    @FXML
    private TextField txtEmpName;

    @FXML
    private TextField txtEmpEmail;

    @FXML
    private DatePicker txtEmpBirthdate;

    @FXML
    private TextField txtEmpSalary;

    @FXML
    private ComboBox<Department> comboBoxDep;

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

    private ObservableList<Department> observableListDep;

    public void setEmployee(Employee entity) {
        this.employee = entity;
    }

    public void setServices(EmployeeService employeeService, DepartmentService departmentService) {

        this.service = employeeService;
        this.departmentService = departmentService;
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
        Constraints.setTextFieldMaxLength(txtEmpName, 70);
        Constraints.setTextFieldMaxLength(txtEmpEmail, 60);
        Utils.formatDatePicker(txtEmpBirthdate, "dd/MM/yyyy");
        Constraints.setTextFieldDouble(txtEmpSalary);
        initializeComboBoxDep();
    }

    public void updateFormData() {
        if (employee == null) {
            throw new IllegalStateException("Employee entity is null.");
        }

        txtEmpId.setText(String.valueOf(employee.getId()));
        txtEmpName.setText(String.valueOf(employee.getName()));
        txtEmpEmail.setText(String.valueOf(employee.getEmail()));
        Locale.setDefault(Locale.US);
        txtEmpSalary.setText(String.format("%.2f", employee.getBaseSalary()));

        if (employee.getBirthdate() != null) {
            txtEmpBirthdate.setValue(LocalDate.ofInstant(employee.getBirthdate().toInstant(), ZoneId.systemDefault()));
        }

        if (employee.getDepartment() == null) {
            comboBoxDep.getSelectionModel().selectFirst();
        } else {
            comboBoxDep.setValue(employee.getDepartment());
        }
    }

    private Employee getFormData() {
        Employee emp = new Employee();
        ValidationException exception = new ValidationException("Validation error.");

        emp.setId(Utils.parseToInt(txtEmpId.getText()));

        // name
        if (txtEmpName.getText() == null || txtEmpName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty.");
        }

        emp.setName(txtEmpName.getText());

        // email
        if (txtEmpEmail.getText() == null || txtEmpEmail.getText().trim().equals("")) {
            exception.addError("email", "Field can't be empty.");
        }

        emp.setEmail(txtEmpEmail.getText());

        // birthdate
        if (txtEmpBirthdate.getValue() == null) {
            exception.addError("birthdate", "Field can't be empty");
        } else {
            Instant instant = Instant.from(txtEmpBirthdate.getValue().atStartOfDay(ZoneId.systemDefault()));
            emp.setBirthdate(Date.from(instant));
        }

        // salary
        if (txtEmpSalary.getText() == null || txtEmpSalary.getText().trim().equals("")) {
            exception.addError("salary", "Field can't be empty.");
        }

        emp.setBaseSalary(Utils.parseToDouble(txtEmpSalary.getText()));

        // department
        emp.setDepartment(comboBoxDep.getValue());


        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return emp;
    }

    private void setErrorMsgs(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelEmpNameError.setText((fields.contains("name") ? errors.get("name") : ""));
        labelEmpEmailError.setText((fields.contains("email") ? errors.get("email") : ""));
        labelEmpBirthdateError.setText((fields.contains("birthdate") ? errors.get("birthdate") : ""));
        labelEmpSalaryError.setText((fields.contains("salary") ? errors.get("salary") : ""));
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener: listeners) {
            listener.onDataChanged();
        }
    }

    public void loadAssociatedObjects() {
        if (departmentService == null) {
            throw new IllegalStateException("Department Service is null.");
        }

        List<Department> departmentList = departmentService.findAll();
        observableListDep = FXCollections.observableArrayList(departmentList);
        comboBoxDep.setItems(observableListDep);
    }

    private void initializeComboBoxDep() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDep.setCellFactory(factory);
        comboBoxDep.setButtonCell(factory.call(null));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Called to initialize a controller after its root element has been completely processed.
        // location - The location used to resolve relative paths for the root object
        // resources - The resources used to localize the root object, or null if the root object was not localized.

        initializeNodes();
    }
}
