package com.udemy.employeemanagementsystem.model.dao;

import com.udemy.employeemanagementsystem.model.entities.Department;
import com.udemy.employeemanagementsystem.model.entities.Employee;

import java.util.List;

public interface EmployeeDao {

    void insert(Employee employee);
    void update(Employee employee);
    void deleteById(Integer id);
    Employee findById(Integer id);
    List<Employee> findAll();
    List<Employee> findByDepartment(Department department);
}