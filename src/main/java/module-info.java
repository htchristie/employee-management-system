module com.udemy.employeemanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.udemy.employeemanagementsystem to javafx.fxml;
    exports com.udemy.employeemanagementsystem;
    exports com.udemy.employeemanagementsystem.controller;
    exports com.udemy.employeemanagementsystem.model.entities;
    exports com.udemy.employeemanagementsystem.model.services;
    opens com.udemy.employeemanagementsystem.controller to javafx.fxml;
    opens com.udemy.employeemanagementsystem.model.entities to javafx.fxml;
}