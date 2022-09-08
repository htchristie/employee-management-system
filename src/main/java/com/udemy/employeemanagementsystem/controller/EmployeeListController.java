package com.udemy.employeemanagementsystem.controller;

import com.udemy.employeemanagementsystem.Main;
import com.udemy.employeemanagementsystem.db.DbIntegrityException;
import com.udemy.employeemanagementsystem.listener.DataChangeListener;
import com.udemy.employeemanagementsystem.model.entities.Employee;
import com.udemy.employeemanagementsystem.model.services.EmployeeService;
import com.udemy.employeemanagementsystem.util.Alerts;
import com.udemy.employeemanagementsystem.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeListController implements Initializable, DataChangeListener {

    private EmployeeService employeeService;
    private ObservableList<Employee> employeeObservableList;

    @FXML
    private Button btnNew;

    @FXML
    private TableView<Employee> employeeTableView;

    @FXML
    private TableColumn<Employee, Integer> employeeIdTableColumn;

    @FXML
    private TableColumn<Employee, String> employeeNameTableColumn;

    @FXML
    private TableColumn<Employee, String> employeeEmailTableColumn;

    @FXML
    private TableColumn<Employee, Date> employeeBirthdateTableColumn;

    @FXML
    private TableColumn<Employee, Double> employeeSalaryTableColumn;

    @FXML
    private TableColumn<Employee, Employee> tableColumnEdit;

    @FXML
    private TableColumn<Employee, Employee> tableColumnDelete;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @FXML
    public void onBtnNewClick(ActionEvent event) {

        Stage parentStage = Utils.currentStage(event);
        Employee employee = new Employee();
        createDialogForm(employee, "employee-form.fxml", parentStage);
    }

    public void updateTableView() {
        if (employeeService == null) {
            throw new IllegalStateException("Service is null.");
        }

        List<Employee> employeeList = employeeService.findAll();
        employeeObservableList = FXCollections.observableArrayList(employeeList);

        employeeTableView.setItems(employeeObservableList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Employee employee, String viewPath, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(viewPath));
            Pane pane = loader.load();

            EmployeeFormController controller = loader.getController();
            controller.setEmployee(employee);
            controller.setEmployeeService(new EmployeeService());
            controller.assignDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Employee data:");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            Alerts.showAlert("IO Exception", "Error loading View", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Employee, Employee>() {
            private final Button button = new Button("edit");
            @Override
            protected void updateItem(Employee obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "employee-form.fxml",Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDelete.setCellFactory(param -> new TableCell<Employee, Employee>() {
            private final Button button = new Button("remove");
            @Override
            protected void updateItem(Employee obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Employee obj) {
        Optional<ButtonType> confirmation = Alerts.showConfirmation("Confirm action", "Delete this employee?");

        if (confirmation.get() == ButtonType.OK) {
            if (employeeService == null) {
                throw new IllegalStateException("No service was found.");
            }

            try {
                employeeService.remove(obj);
                updateTableView();
            }
            catch (DbIntegrityException e) {
                Alerts.showAlert("Error removing Employee", null, e.getMessage(), Alert.AlertType.ERROR);
            }

        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        // initialize table elements
        employeeIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        employeeBirthdateTableColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        Utils.formatTableColumnDate(employeeBirthdateTableColumn, "dd/MM/yyyy");
        employeeSalaryTableColumn.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(employeeSalaryTableColumn, 2);

        Stage stage = (Stage) Main.getMainScene().getWindow();

        // set table view height to window height
        employeeTableView.prefHeightProperty().bind(stage.heightProperty());
    }
}
