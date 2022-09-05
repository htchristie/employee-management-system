package com.udemy.employeemanagementsystem.model.dao;

import com.udemy.employeemanagementsystem.db.DB;
import com.udemy.employeemanagementsystem.model.dao.impl.DepartmentDaoJDBC;
import com.udemy.employeemanagementsystem.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    // has static methods to instantiate Daos

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
