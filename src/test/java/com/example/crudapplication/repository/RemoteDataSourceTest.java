package com.example.crudapplication.repository;

import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

@RunWith(JUnit4.class)
public class RemoteDataSourceTest {

    EmployeeRemoteFakeImpl employeeRemoteRetrofit;//目前要的不是依賴,所以不需要模擬,而是要自己new

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        employeeRemoteRetrofit=new EmployeeRemoteFakeImpl();
    }

    @Test
    public void fetch_all_remote_employee_test() throws IOException {
        //測試資料 : 無
        //預期解果 : 有list 容器裝解析後的JSON字串成物件EmployeeJson
        //測試物件 EmployeeRemoteFakeImpl 的 fetchAll()方法
        //驗證方式: 回傳不可是空的
        Assert.assertTrue(employeeRemoteRetrofit.fetchAll().size()>0);
    }
}
