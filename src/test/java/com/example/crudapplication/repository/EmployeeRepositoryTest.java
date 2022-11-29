package com.example.crudapplication.repository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.dao.EmployeeDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

@RunWith(JUnit4.class)
public class EmployeeRepositoryTest {

    @Mock
    EmployeeRemoteDS employeeRemoteDS;
    @Mock
    AppDatabase db;
    @Mock
    EmployeeDao employeeDao;

    EmployeeRepository employeeRepository;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        //測試物件: EmployeeRepository, 依賴物件: EmployeeRemoteDS,本地資料庫,執行緒池
        employeeRepository=new EmployeeRepository(employeeRemoteDS,db, Executors.newFixedThreadPool(1));
    }

    /*
    * 由遠端抓取到資料後,存入本地資料庫
    * 所以測試內容需要有資料
    * */
    @Test
    public void remote_fetch_And_save_DB_test() throws IOException {
        //測試資料: 輸入:遠端要有資料
        List<EmployeeJson> remoteData=new ArrayList<>();
        remoteData.add(new EmployeeJson());
        //測試物件: EmployeeRepository, 依賴物件: EmployeeRemoteDS,本地資料庫,執行緒池
        //模擬動作
        when(db.employeeDao()).thenReturn(employeeDao);
        when(employeeRemoteDS.fetchAll()).thenReturn(remoteData);
        //測試方法: employeeRepository.fetchAll()
        employeeRepository.fetchAll().join();
        //驗證方式: 1.EmployeeRemoteDS的fetchAll有被呼叫到一次
        verify(employeeRemoteDS).fetchAll();
        //驗證方式: 2.資料庫employeeDao.updateLatestData()有被呼叫到,且有內容
        verify(db.employeeDao()).updateLatestData(remoteData.toArray(new EmployeeJson[0]));

    }
    /*
    * 遠端回傳null不產生錯誤,不造成執行緒例外結束
    * */
    @Test
    public void employeeRemoteDS_fetchAll_return_null_test() throws IOException {
        //測試資料: 回傳:null
        when(employeeRemoteDS.fetchAll()).thenReturn(null);
        //測試物件: EmployeeRepository, 依賴物件: EmployeeRemoteDS,本地資料庫,執行緒池
        //測試方法: employeeRepository.fetchAll()
        employeeRepository.fetchAll().join();
        //驗證方式: 流程正常走完,employeeRemoteDS.fetchAll()有被呼叫
        verify(employeeRemoteDS).fetchAll();
    }
    /*
    *遠端抓取產生例外IOException
    * */
    @Test
    public void employeeRemoteDS_fetchAll_return_IOException_test() throws IOException {
        //測試資料: 回傳:IOException
        IOException ioException=new IOException();
        when(employeeRemoteDS.fetchAll()).thenThrow(ioException);
        //測試物件: EmployeeRepository, 依賴物件: EmployeeRemoteDS,本地資料庫,執行緒池
        //測試方法: employeeRepository.fetchAll()
        CompletableFuture<Void> future = employeeRepository.fetchAll();
        //因為join()關係,他會丟出非受檢例外,這邊捕捉不做處理,平常不會用join來等他完成
        try{
            future.whenComplete((unused, throwable) -> {
                Assert.assertEquals(ioException,throwable);
                System.out.println("完成");
            }).join();
        }catch (Exception ignored){

        }
        //驗證方式: 1.employeeRemoteDS.fetchAll()被呼叫一次
        verify(employeeRemoteDS).fetchAll();
        //驗證方式: 1.future結束於例外
        Assert.assertTrue(future.isCompletedExceptionally());
    }
}
