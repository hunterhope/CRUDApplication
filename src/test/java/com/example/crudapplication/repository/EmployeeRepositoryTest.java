package com.example.crudapplication.repository;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.crudapplication.TestUtil;
import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.dao.EmployeeDao;
import com.example.crudapplication.db.entity.Employee;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeRepositoryTest {

    private EmployeeRepository employeeRepository;
    @Mock
    private AppDatabase db;
    @Mock
    private EmployeeDao employeeDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        Executor executor = Executors.newFixedThreadPool(1);//執行緒池不要用模擬,不然要自己寫
        employeeRepository = new EmployeeRepository(db, executor);
        //大家都會用到的,模擬動作發生,且DAO取得以測試過
        when(db.employeeDao()).thenReturn(employeeDao);
    }

    @After
    public void tearDown() throws Exception {
        employeeRepository=null;
    }

    @Test
    public void getAll() {
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(new Employee(1L,"John",2,"0968751923"));
        when(db.employeeDao().getAll()).thenReturn(new MutableLiveData<>(employeeList));
        Assert.assertEquals(employeeList,employeeRepository.getAll().getValue());
    }

    @Test
    public void delete() {
        //模擬資料庫資料
        List<Employee> employeeList=createTestDBData();
        //預刪除的資料id
        Set<Long> deleteIds= Collections.singleton(1L);
        //模擬動作發生
        doAnswer(invocation -> {
            Set<Long> ids=invocation.getArgument(0);
            employeeList.removeIf(e->ids.contains(e.id));
            return false;
        }).when(employeeDao).delete(deleteIds);
        //要測試方法
        employeeRepository.delete(deleteIds).join();
        System.out.println("刪除ids="+deleteIds);
        System.out.println(employeeList);
        //次試結果驗證
        //預期值
        List<Employee> expected=new ArrayList<>();
        expected.add(new Employee(2L,"Mary",3,"0978570985"));
        Assert.assertEquals(expected,employeeList);

    }

    @Test
    public void createEmployee() {
        //模擬資料庫資料
        List<Employee> employeeList=createTestDBData();
        //要新增的資料
        Employee newEmployee = TestUtil.createEmployee();
        //模擬動作發生
        when(db.employeeDao().insert(newEmployee))
                .thenAnswer(invocation -> {
                    employeeList.add(invocation.getArgument(0));
                    Employee employee=invocation.getArgument(0);
                    employee.id= (long) employeeList.size();
                    return new long[]{employee.id};
                });
        //要測試的方法
        Long insertIds = employeeRepository.createEmployee(newEmployee).join();
        //預期結果1 id正確
        Long expected=3L;
        Assert.assertEquals(expected,insertIds);
        //預期結果2 資料庫大小有正確
        Assert.assertEquals(3,employeeList.size());
        //預期結果3 資料庫有該筆資料
        Assert.assertTrue(employeeList.contains(newEmployee));
        System.out.println("測試結束後資料庫資料");
        System.out.println(employeeList);
    }

    @Test
    public void updateEmployee() {
        //模擬資料庫資料
        List<Employee> employeeList=createTestDBData();
        //要修改的資料,假設是第2筆資料
        Employee updateEmployee=TestUtil.createEmployee();
        updateEmployee.id=2L;
        //模擬動作發生
        doAnswer(invocation -> {
            Employee uE=invocation.getArgument(0);
            employeeList.stream().filter(e -> e.id.equals(uE.id))
                    .findFirst()
                    .ifPresent(e->{
                        e.name= uE.name;
                        e.age= uE.age;
                        e.phone=uE.phone;
                    });
            return true;
        }).when(employeeDao).update(updateEmployee);
        //要測試的方法
        employeeRepository.updateEmployee(updateEmployee).join();
        //預期結果 第2筆資料要等於更新資料
        Assert.assertEquals(updateEmployee,employeeList.get(1));
        System.out.println("更新後資料");
        System.out.println(employeeList);

    }

    @Test
    public void getAllSync() {
    }

    private List<Employee> createTestDBData(){
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(new Employee(1L,"John",2,"0968751923"));
        employeeList.add(new Employee(2L,"Mary",3,"0978570985"));
        System.out.println("資料庫目前有:");
        System.out.println(employeeList);
        return employeeList;
    }
}