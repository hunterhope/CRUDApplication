package com.example.crudapplication.repository;

import com.example.crudapplication.db.entity.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class RemoteDSImpl implements RemoteDS{

    @Inject
    public RemoteDSImpl() {
    }

    @Override
    public void delete(Set<Long> deleteData) {

    }

    @Override
    public List<Employee> getAll() {
        return createTestDBData();
    }

    public static List<Employee> createTestDBData(){
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(new Employee(1L,"John",2,"0968751923"));
        employeeList.add(new Employee(2L,"Mary",3,"0978570985"));
        return employeeList;
    }
}
