package com.udemy.employeemanagementsystem.controller;

import com.udemy.employeemanagementsystem.Main;
import com.udemy.employeemanagementsystem.listener.DataChangeListener;
import com.udemy.employeemanagementsystem.model.entities.Department;
import com.udemy.employeemanagementsystem.model.services.DepartmentService;
import com.udemy.employeemanagementsystem.util.Alerts;
import com.udemy.employeemanagementsystem.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable, DataChangeListener {

    private DepartmentService departmentService;
    private ObservableList<Department> departmentObservableList;

    @FXML
    private Button btnNew;

    @FXML
    private TableView<Department> departmentTableView;

    @FXML
    private TableColumn<Department, Integer> departmentIdTableColumn;

    @FXML
    private TableColumn<Department, String> departmentNameTableColumn;

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @FXML
    public void onBtnNewClick(ActionEvent event) {

        Stage parentStage = Utils.currentStage(event);
        Department department = new Department();
        createDialogForm(department, "department-form.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        // initialize table elements
        departmentIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        departmentNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        Stage stage = (Stage) Main.getMainScene().getWindow();

        // set table view height to window height
        departmentTableView.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (departmentService == null) {
            throw new IllegalStateException("Service is null.");
        }

        List<Department> departments = departmentService.findAll();
        departmentObservableList = FXCollections.observableArrayList(departments);

        departmentTableView.setItems(departmentObservableList);
    }

    private void createDialogForm(Department department, String viewPath, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(viewPath));
            Pane pane = loader.load();

            DepartmentFormController controller = loader.getController();
            controller.setDepartment(department);
            controller.setDepartmentService(new DepartmentService());
            controller.assignDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Department data:");
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

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
