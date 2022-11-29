package com.example.crudapplication.repository;

import static org.mockito.Mockito.verify;

import com.example.crudapplication.db.AppDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

@RunWith(JUnit4.class)
public class EmployeeRepositoryTest {

    @Mock
    EmployeeRemoteDS employeeRemoteDS;
    @Mock
    AppDatabase db;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    /*
    * 由遠端抓取到資料後,存入本地資料庫
    * */
    @Test
    public void remote_fetch_And_save_DB_test() throws IOException {
        //測試資料: 輸入:無
        //測試物件: EmployeeRepository, 依賴物件: EmployeeRemoteDS,本地資料庫
        EmployeeRepository employeeRepository=new EmployeeRepository(employeeRemoteDS,db);
        //模擬動作

        //測試方法: employeeRepository.fetchAll()
        employeeRepository.fetchAll();
        //驗證方式: 1.EmployeeRemoteDS的fetchAll有被呼叫到一次
        verify(employeeRemoteDS).fetchAll();
        //驗證方式: 2.資料庫employeeDao有被呼叫到
        verify(db).employeeDao();

    }
}
