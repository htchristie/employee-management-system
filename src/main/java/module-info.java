module com.udemy.employeemanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.udemy.employeemanagementsystem to javafx.fxml;
    exports com.udemy.employeemanagementsystem;
    exports com.udemy.employeemanagementsystem.controllers;
    opens com.udemy.employeemanagementsystem.controllers to javafx.fxml;
}