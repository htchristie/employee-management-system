module com.udemy.employeemanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.udemy.employeemanagementsystem to javafx.fxml;
    exports com.udemy.employeemanagementsystem;
    exports com.udemy.employeemanagementsystem.controller;
    exports com.udemy.employeemanagementsystem.model.entities;
    opens com.udemy.employeemanagementsystem.controller to javafx.fxml;
    opens com.udemy.employeemanagementsystem.model.entities to javafx.fxml;
}