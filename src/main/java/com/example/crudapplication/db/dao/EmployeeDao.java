package com.example.crudapplication.db.dao;


import com.example.crudapplication.repository.EmployeeJson;

public interface EmployeeDao {
    void updateLatestData(EmployeeJson... employeeJson);
}
