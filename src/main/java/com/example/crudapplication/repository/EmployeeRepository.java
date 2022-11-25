package com.example.crudapplication.repository;

import androidx.lifecycle.LiveData;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.entity.Employee;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.inject.Inject;

public class EmployeeRepository {
    private final AppDatabase db;
    private final Executor executor;

    @Inject
    public EmployeeRepository(AppDatabase db, Executor executor) {
        this.db = db;
        this.executor = executor;
    }


    public LiveData<List<Employee>> getAll() {
        return db.employeeDao().getAll();
    }

    public CompletableFuture<Boolean> delete(Set<Long> deleteData) {
        return CompletableFuture.supplyAsync(()->{
            db.employeeDao().delete(deleteData);
            return true;
        },executor);
    }

    public CompletableFuture<Long> createEmployee(Employee employee) {
        return CompletableFuture.supplyAsync(()->{
            return db.employeeDao().insert(employee)[0];
        },executor);
    }

    public CompletableFuture<Boolean> updateEmployee(Employee employee) {
        return CompletableFuture.supplyAsync(()->{
            db.employeeDao().update(employee);
            return true;
        },executor);
    }

    public List<Employee> getAllSync() {
        return db.employeeDao().getAllEmployees();
    }
}
