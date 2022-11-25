package com.example.crudapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.dao.EmployeeDao;
import com.example.crudapplication.db.entity.Employee;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class EmployeeDaoTest {
    private AppDatabase db;
    private EmployeeDao employeeDao;
    private final Object waitResultKey=new Object();
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.databaseBuilder(context, AppDatabase.class, "test.db").build();
        db = Room.inMemoryDatabaseBuilder(context,AppDatabase.class).build();
        employeeDao = db.employeeDao();
    }

    @After
    public void closed() {
        db.close();
    }

    @Test
    public void writeEmployeeAndReadInList() throws InterruptedException {
        Employee employee= TestUtil.createEmployee();
        employeeDao.insert(employee);
        ApplicationProvider.getApplicationContext().getMainExecutor().execute(()->{
            employeeDao.getAll().observeForever(employees -> {
                employees.forEach(e->{
                    Log.d("test",e.toString());
                });
                Assert.assertTrue(employees.contains(employee));
                notifyWaiter();
            });
        });
        waitResult();
    }

    private void notifyWaiter(){
        synchronized (waitResultKey){
            waitResultKey.notifyAll();
        }
    }
    private void waitResult() throws InterruptedException {
        Log.d("test","等待查詢結果");
        synchronized (waitResultKey){
            waitResultKey.wait(10_000);
        }
        Log.d("test","測試結束");
    }

    @Test
    public void count() {
        //刪除所有資料
        List<Long> ids=employeeDao.selectAllIds();
        employeeDao.delete(new HashSet<>(ids));
        //塞入一筆資料
        employeeDao.insert(TestUtil.createEmployee());
        //預計只會顯示1筆資料
        int num = employeeDao.count();
        assertEquals(1,num);
    }

    @Test
    public void delete() {
        //查出所有資料
        Long firstId = getFirstId();
        //刪除第1比
        Set<Long> deleteIds= Collections.singleton(firstId);
        employeeDao.delete(deleteIds);
        //在查詢全部則無該筆資料
        List<Long> afterDeleteIds=employeeDao.selectAllIds();
        assertFalse(afterDeleteIds.contains(deleteIds));
    }
    private Long getFirstId(){
        //查出所有資料
        Long firstId;
        List<Long> allIds = employeeDao.selectAllIds();
        if(allIds.isEmpty()){
            firstId = employeeDao.insert(TestUtil.createEmployee())[0];
        }else{
            firstId=allIds.get(0);
        }
        return firstId;
    }
    @Test
    public void update() throws InterruptedException {
        //查出所有資料
        Long firstId = getFirstId();
        //選擇第一筆
        Employee updateEmployee = TestUtil.createEmployee();
        updateEmployee.id=firstId;
        //修改資料
        employeeDao.update(updateEmployee);
        //在查出資料,必須要有剛修改的資料
        ApplicationProvider.getApplicationContext().getMainExecutor().execute(()->{
            employeeDao.getAll().observeForever(new Observer<List<Employee>>() {
                @Override
                public void onChanged(List<Employee> employees) {
                    boolean has = employees.stream().anyMatch(employee -> employee.equals(updateEmployee)&& Objects.equals(employee.id, updateEmployee.id));
                    assertTrue(has);
                    notifyWaiter();
                }
            });
        });
        waitResult();
    }
}
