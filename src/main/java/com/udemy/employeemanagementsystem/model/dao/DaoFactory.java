package com.udemy.employeemanagementsystem.model.dao;

import com.udemy.employeemanagementsystem.db.DB;
import com.udemy.employeemanagementsystem.model.dao.impl.DepartmentDaoJDBC;
import com.udemy.employeemanagementsystem.model.dao.impl.EmployeeDaoJDBC;

public class DaoFactory {
    // has static methods to instantiate Daos

    public static EmployeeDao createEmployeeDao() {
        return new EmployeeDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
