package com.example.crudapplication.db.dao;


import androidx.lifecycle.LiveData;

import com.example.crudapplication.repository.EmployeeJson;

import java.util.List;

public interface EmployeeDao {
    void updateLatestData(EmployeeJson... employeeJson);

    LiveData<List<EmployeeJson>> getAll();
}
