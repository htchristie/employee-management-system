package com.udemy.employeemanagementsystem.model.dao.impl;

import com.udemy.employeemanagementsystem.db.DB;
import com.udemy.employeemanagementsystem.db.DbException;
import com.udemy.employeemanagementsystem.model.dao.EmployeeDao;
import com.udemy.employeemanagementsystem.model.entities.Department;
import com.udemy.employeemanagementsystem.model.entities.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoJDBC implements EmployeeDao {
    //jdbc implementation of EmployeeDao interface

    private Connection connection;

    public EmployeeDaoJDBC(Connection connection) {
        // dependency injection
        this.connection = connection;
    }

    @Override
    public void insert(Employee employee) {

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                    "INSERT INTO seller "
                            + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                            + "VALUES "
                            + "(?, ?, ?, ?, ?)",
                    statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setDate(3, new Date(employee.getBirthdate().getTime()));
            statement.setDouble(4, employee.getBaseSalary());
            statement.setInt(5, employee.getDepartment().getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    employee.setId(id);
                }
                DB.closeResultSet(resultSet);

            } else {
                throw new DbException("Unexpected error: no line was altered.");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void update(Employee employee) {

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                    "UPDATE seller "
                            + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                            + "WHERE Id = ?"
            );

            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setDate(3, new Date(employee.getBirthdate().getTime()));
            statement.setDouble(4, employee.getBaseSalary());
            statement.setInt(5, employee.getDepartment().getId());
            statement.setInt(6, employee.getId());

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                    "DELETE FROM seller "
                            + "WHERE Id = ?"
            );

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("No employee was found.");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
        }

    }

    @Override
    public Employee findById(Integer id) {

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?"
            );

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);
                return instantiateEmployee(resultSet, department);
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Employee> findAll() {

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name"
            );

            resultSet = statement.executeQuery();

            List<Employee> employees = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()) {

                // if department already exists
                Department dep = departmentMap.get(resultSet.getInt("DepartmentId"));

                // if department doesn't exist yet
                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), dep);
                }

                Employee employee = instantiateEmployee(resultSet, dep);
                employees.add(employee);
            }
            return employees;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE DepartmentId = ? "
                            + "ORDER BY Name"
            );

            statement.setInt(1, department.getId());
            resultSet = statement.executeQuery();

            List<Employee> employees = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()) {

                // if department already exists
                Department dep = departmentMap.get(resultSet.getInt("DepartmentId"));

                // if department doesn't exist yet
                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), dep);
                }

                Employee employee = instantiateEmployee(resultSet, dep);
                employees.add(employee);
            }
            return employees;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    private Employee instantiateEmployee(ResultSet resultSet, Department department) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("Id"));
        employee.setName(resultSet.getString("Name"));
        employee.setEmail(resultSet.getString("Email"));
        employee.setBaseSalary(resultSet.getDouble("BaseSalary"));
        employee.setBirthdate(new java.util.Date(resultSet.getTimestamp("BirthDate").getTime()));
        employee.setDepartment(department);

        return employee;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));

        return department;
    }
}