package com.example.crudapplication.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.crudapplication.db.entity.Employee;

import java.util.List;

@Dao
public interface EmployeeDao {
    @Insert
    void insert(Employee... employees);

    @Query("select * from employee order by id desc")
    LiveData<List<Employee>> getAll();

    @Query("delete from employee")
    void deleteAll();
}
