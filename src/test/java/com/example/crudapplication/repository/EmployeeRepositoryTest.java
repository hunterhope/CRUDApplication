package com.example.crudapplication.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.dao.EmployeeDao;
import com.example.crudapplication.db.entity.Employee;

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
        remoteData.add(new EmployeeJson(1L,"John",18,"0968751923"));
        remoteData.add(new EmployeeJson(2L,"John2",18,"0968751922"));
        //測試物件: EmployeeRepository, 依賴物件: EmployeeRemoteDS,本地資料庫,執行緒池
        //模擬動作
        when(db.employeeDao()).thenReturn(employeeDao);
        when(employeeRemoteDS.fetchAll()).thenReturn(remoteData);
        //測試方法: employeeRepository.fetchAll()
        employeeRepository.fetchAll().join();
        //驗證方式: 1.EmployeeRemoteDS的fetchAll有被呼叫到一次
        verify(employeeRemoteDS).fetchAll();
        //驗證方式: 2.資料庫db.runInTransaction()有被呼叫到(重構程式碼後修改測試,why?測是不是要一值保有?)
        verify(db).runInTransaction(any(Runnable.class));
        //驗證方式: 2.資料庫employeeDao.insert()有被呼叫到,且有內容
//        Employee[] expected= remoteData.stream().map(e-> {
//            Employee employee=new Employee();
//            employee.id=e.getId();
//            employee.name=e.getName();
//            employee.age=e.getAge();
//            employee.phone=e.getCellPhone();
//            return employee;
//        }).toArray(Employee[]::new);
//        verify(db.employeeDao()).insert(expected);

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

    /*
    * 抓取回來的資料存入資料庫,後會通知給前端
    * 所以前端將來要一直觀察本地資料庫變化    *
    * */
    @Test
    public void isReceive_DB_LiveData_test(){
        //測試資料: 假的網路資料
        List<Employee> webData=new ArrayList<>();
        webData.add(new Employee());
        //模擬相依物件動作
        when(db.employeeDao()).thenReturn(employeeDao);
        when(employeeDao.getAll()).thenReturn(new MutableLiveData<>(webData));
        //測試物件: EmployeeRepository, 依賴物件: EmployeeRemoteDS,本地資料庫,執行緒池
        //測試方法
        LiveData<List<Employee>> dbData =employeeRepository.getAllEmployee();
        //預期結果 dbData內容與測試資料相同
        //驗證方式
        Assert.assertEquals(webData,dbData.getValue());
    }
}
