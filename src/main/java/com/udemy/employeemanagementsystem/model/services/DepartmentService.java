package com.udemy.employeemanagementsystem.model.services;

import com.udemy.employeemanagementsystem.model.dao.DaoFactory;
import com.udemy.employeemanagementsystem.model.dao.DepartmentDao;
import com.udemy.employeemanagementsystem.model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll() {

        return dao.findAll();
    }
}
