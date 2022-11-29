package com.example.crudapplication.repository;

import com.example.crudapplication.db.AppDatabase;

public class EmployeeRepository {
    private EmployeeRemoteDS employeeRemoteDS;
    private AppDatabase db;

    public EmployeeRepository(EmployeeRemoteDS employeeRemoteDS, AppDatabase db) {
        this.employeeRemoteDS = employeeRemoteDS;
        this.db = db;
    }

    public void fetchAll() {

    }
}
