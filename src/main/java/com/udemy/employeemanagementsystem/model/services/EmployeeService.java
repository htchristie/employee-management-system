package com.udemy.employeemanagementsystem.model.services;

import com.udemy.employeemanagementsystem.model.dao.DaoFactory;
import com.udemy.employeemanagementsystem.model.dao.EmployeeDao;
import com.udemy.employeemanagementsystem.model.entities.Employee;

import java.util.List;

public class EmployeeService {

    private EmployeeDao dao = DaoFactory.createEmployeeDao();

    public List<Employee> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Employee employee) {
        if (employee.getId() == null) {
            dao.insert(employee);
        } else {
            dao.update(employee);
        }
    }

    public void remove(Employee employee) {
        dao.deleteById(employee.getId());
    }
}
