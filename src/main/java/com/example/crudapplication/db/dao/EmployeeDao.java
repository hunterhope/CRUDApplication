package com.example.crudapplication.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.crudapplication.db.entity.Employee;

import java.util.List;
import java.util.Set;

@Dao
public interface EmployeeDao {
    @Query("select count(*) from employee")
    int count();

    @Insert
    long[] insert(Employee... employees);

    @Query("select * from employee order by id desc")
    LiveData<List<Employee>> getAll();

    @Query("delete from employee where id in(:deleteIds)")
    void delete(Set<Long> deleteIds);

    @Update
    void update(Employee employee);

    @Query("select id from employee")
    List<Long> selectAllIds();

    @Query("select * from employee")
    List<Employee> getAllEmployees();

    @Query("delete from employee")
    void deleteAll();
}
