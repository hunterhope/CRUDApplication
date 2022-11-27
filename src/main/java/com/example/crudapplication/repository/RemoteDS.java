package com.example.crudapplication.repository;

import com.example.crudapplication.db.entity.Employee;

import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface RemoteDS {
    void delete(Set<Long> deleteData)throws IOException;

    List<Employee> getAll() throws IOException;

}
