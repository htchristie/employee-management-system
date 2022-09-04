package com.udemy.employeemanagementsystem.model.services;

import com.udemy.employeemanagementsystem.model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public List<Department> findAll() {

        // mock data
        List<Department> departments = new ArrayList<>();

        departments.add(new Department(1, "Books"));
        departments.add(new Department(2, "Eletronics"));
        departments.add(new Department(3, "Computers"));

        return departments;
    }
}
