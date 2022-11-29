package com.example.crudapplication.repository;

import com.example.crudapplication.db.AppDatabase;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EmployeeRepository {
    private EmployeeRemoteDS employeeRemoteDS;
    private AppDatabase db;
    private final Executor executor;
    private CompletableFuture<Void> fetchRemoteDataFuture;

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
                    db.employeeDao().updateLatestData(data.toArray(new EmployeeJson[0]));
                }
            } catch (IOException e) {
                fetchRemoteDataFuture.completeExceptionally(e);
            }
        },executor);
        return fetchRemoteDataFuture;
    }
}
