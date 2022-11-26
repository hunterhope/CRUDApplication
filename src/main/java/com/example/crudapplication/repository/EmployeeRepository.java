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
    private final RemoteDS remoteDS;

    @Inject
    public EmployeeRepository(AppDatabase db, Executor executor, RemoteDS remoteDS) {
        this.db = db;
        this.executor = executor;
        this.remoteDS = remoteDS;
    }


    public EmployeeRepository(AppDatabase db, Executor executor) {
        this.db = db;
        this.executor = executor;
        remoteDS=null;
    }

    public void getAllServerData(){
        CompletableFuture.runAsync(()->{
            if(remoteDS!=null){
                List<Employee> latestEmployees=remoteDS.getAll();
                db.runInTransaction(()->{
                    db.employeeDao().deleteAll();
                    db.employeeDao().insert(latestEmployees.toArray(new Employee[0]));
                });
            }
        },executor);
    }
    public LiveData<List<Employee>> getAll() {
        getAllServerData();
        return db.employeeDao().getAll();
    }

    public CompletableFuture<Void> delete(Set<Long> deleteData) {
        return CompletableFuture.runAsync(()-> {
            if(remoteDS!=null){
                remoteDS.delete(deleteData);
                getAllServerData();
            }else {
                db.employeeDao().delete(deleteData);
                //安排WorkerManager帶網路恢復時執行?
            }
        },executor);
    }

    public CompletableFuture<Long> createEmployee(Employee employee) {
        return CompletableFuture.supplyAsync(()-> db.employeeDao().insert(employee)[0],executor);
    }

    public CompletableFuture<Void> updateEmployee(Employee employee) {
        return CompletableFuture.runAsync(()-> db.employeeDao().update(employee),executor);
    }
}
