package com.example.crudapplication.repository;

import com.example.crudapplication.db.entity.Employee;

import java.util.List;
import java.util.Set;

public interface RemoteDS {
    void delete(Set<Long> deleteData);

    List<Employee> getAll();

}
