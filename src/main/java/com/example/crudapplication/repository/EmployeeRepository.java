package com.example.crudapplication.repository;

import androidx.lifecycle.LiveData;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.entity.Employee;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.inject.Inject;

public class EmployeeRepository {
    private final EmployeeRemoteDS employeeRemoteDS;
    private final AppDatabase db;
    private final Executor executor;
    private CompletableFuture<Void> fetchRemoteDataFuture;

    @Inject
    public EmployeeRepository(EmployeeRemoteDS employeeRemoteDS, AppDatabase db, Executor executor) {
        this.employeeRemoteDS = employeeRemoteDS;
        this.db = db;
        this.executor = executor;
    }

    //要有回傳質比較好寫非同步執行的測試,可以加入join
    public CompletableFuture<Void> fetchAll() {
        if(fetchRemoteDataFuture!=null){
            fetchRemoteDataFuture.cancel(true);
        }
        fetchRemoteDataFuture= CompletableFuture.runAsync(()->{
            try {
                List<EmployeeJson> data = employeeRemoteDS.fetchAll();
                if(data!=null && !data.isEmpty()){
                    db.runInTransaction(()->{
                        db.employeeDao().deleteAll();
                        db.employeeDao().insert(data.stream().map(json->{
                            Employee e=new Employee();
                            e.id= json.getId();
                            e.name=json.getName();
                            e.age= json.getAge();
                            e.phone= json.getCellPhone();
                            return e;
                        }).toArray(Employee[]::new));
                    });
                }
            } catch (IOException e) {
                fetchRemoteDataFuture.completeExceptionally(e);
            }
        },executor);
        return fetchRemoteDataFuture;
    }

    public LiveData<List<Employee>> getAllEmployee() {
        return db.employeeDao().getAll();
    }
}
