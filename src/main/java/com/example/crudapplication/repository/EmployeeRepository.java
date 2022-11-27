package com.example.crudapplication.repository;

import androidx.lifecycle.LiveData;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.entity.Employee;
import com.example.crudapplication.db.entity.NetWorkIOResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.inject.Inject;

public class EmployeeRepository {
    private final AppDatabase db;
    private final Executor executor;
    private final RemoteDS remoteDS;

    private CompletableFuture<Void> getAllFuture;
    private CompletableFuture<Void> deleteFuture;

    @Inject
    public EmployeeRepository(AppDatabase db, Executor executor, RemoteDS remoteDS) {
        this.db = db;
        this.executor = executor;
        this.remoteDS = remoteDS;
    }


    public EmployeeRepository(AppDatabase db, Executor executor) {
        this.db = db;
        this.executor = executor;
        remoteDS = null;
    }

    public void getAllServerData() {
        if (getAllFuture != null) {
            getAllFuture.cancel(true);
        }

        getAllFuture = CompletableFuture.runAsync(() -> {
            if (remoteDS != null) {
                try {
                    List<Employee> latestEmployees = remoteDS.getAll();
                    db.runInTransaction(() -> {
                        db.employeeDao().deleteAll();
                        db.employeeDao().insert(latestEmployees.toArray(new Employee[0]));
                    });
                } catch (IOException ex) {
                    getAllFuture.completeExceptionally(ex);
                    //存入資料庫一則例外訊息
                    NetWorkIOResult netWorkIOResult = new NetWorkIOResult();
                    netWorkIOResult.dataTime= LocalDateTime.now();
                    netWorkIOResult.msgId =0;
                    netWorkIOResult.msg=ex.getLocalizedMessage();
                    db.netWorkIOResultDao().insert(netWorkIOResult);
                }
            }
        }, executor);

    }

    public LiveData<Optional<NetWorkIOResult>> getNetWorkIOResult(){
        return db.netWorkIOResultDao().latestResult();
    }

    public LiveData<List<Employee>> getAll() {
        //這邊網路發生IO例外怎麼辦?
        //目前因為這邊的關係,決定將IO例外存入資料庫,才可以再多一個觀察者,才有辦法通知
        getAllServerData();
        return db.employeeDao().getAll();
    }

    public CompletableFuture<Void> delete(Set<Long> deleteData) {
        deleteFuture = CompletableFuture.runAsync(() -> {
            try {
                if (remoteDS != null) {
                    remoteDS.delete(deleteData);
                    getAllServerData();
                } else {
                    db.employeeDao().delete(deleteData);
                    //安排WorkerManager帶網路恢復時執行?
                }
            } catch (IOException ex) {
                deleteFuture.completeExceptionally(ex);
            }
        }, executor);
        return deleteFuture;
    }

    public CompletableFuture<Long> createEmployee(Employee employee) {
        return CompletableFuture.supplyAsync(() -> db.employeeDao().insert(employee)[0], executor);
    }

    public CompletableFuture<Void> updateEmployee(Employee employee) {
        return CompletableFuture.runAsync(() -> db.employeeDao().update(employee), executor);
    }

    public void netWorkIOHasShow(LocalDateTime key) {
        executor.execute(()->{
            db.netWorkIOResultDao().msgHasShow(key);
        });
    }
}
