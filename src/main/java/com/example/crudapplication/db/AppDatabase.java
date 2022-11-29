package com.example.crudapplication.db;

import com.example.crudapplication.db.dao.EmployeeDao;

public abstract class AppDatabase {
    public abstract EmployeeDao employeeDao();
}
