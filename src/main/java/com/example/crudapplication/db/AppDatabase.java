package com.example.crudapplication.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.crudapplication.db.dao.EmployeeDao;
import com.example.crudapplication.db.entity.Employee;

@Database(entities = {Employee.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmployeeDao employeeDao();
}
