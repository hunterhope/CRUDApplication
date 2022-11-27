package com.example.crudapplication.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.dao.NetWorkIOResultDao;
import com.example.crudapplication.db.entity.NetWorkIOResult;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.hilt.android.qualifiers.ApplicationContext;

@RunWith(JUnit4.class)
public class EmployeeRepositoryExceptionTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();//讓執行緒符合結構元件要的執行緒

    private EmployeeRepository employeeRepository;
    @Mock
    AppDatabase db;

    @Mock
    NetWorkIOResultDao netWorkIOResultDao;

    @Mock
    RemoteDS remoteDS;
    ExecutorService executorService;

    @Mock
    Observer<Optional<NetWorkIOResult>> netWorkIOResultObserver;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);//沒寫這行前面宣告的@Mock都會是null物件
        executorService = Executors.newFixedThreadPool(1);
        employeeRepository = new EmployeeRepository(db, executorService, remoteDS);
        //模擬動作
        when(db.netWorkIOResultDao()).thenReturn(netWorkIOResultDao);//大家都相同的動作模擬
    }

    @After
    public void tearDown() throws Exception {
        executorService.shutdown();
        employeeRepository = null;
    }

    /**
     * 模擬要執行的動作,when()用法寫在你動作發生之前會產生的結果,有一個when以後,後面都會共用,除非再該動作發生前再寫一個when蓋過去,所以測試流程如下
     * 受測方法 employeeRepository.getAllServerData();
     * 預計會執行例外處理流程SocketTimeoutException
     *  例外流程會執行一個db插入網路查詢結果的動作,這動作要產生一個觀察者改變,可以從employeeRepository.getNetWorkIOResult().getValue()拿到結果
     * 預期整個流程可以正常跑完,表示employeeRepository.getAllServerData();方法發生SocketTimeoutException有照著我的流程執行
     * 而塞入資料庫的動作,已經在Android Test下的NetWorkIOResultDaoTest測試過
     * 後續是不是真的有讓觀察者收到結果,是另外更上面的測試,不屬於此方法,此方法是要測試我有正確捕捉SocketTimeoutException,並完整執行例外處理流程不在發生其他例外
     * 所以要驗證下面的發生
     * 1.future不可以是null
     * 2.future是發生例外結束 且是 SocketTimeoutException
     * 3.netWorkIOResultDao.insert()有被呼叫一次,時間是多少不知道而已
     */
    @Test
    public void getAllServerData_SocketTimeoutExceptionTest() throws IOException {
        //測試資料,產生一個連線超時例外
        String msg = "伺服器連線超時";
        SocketTimeoutException socketTimeoutException=new SocketTimeoutException(msg);
        //模擬動作, 沒有這一行會無法模擬發生例外,因為remoteDS是一個模擬,不是真的物件,而他又因為@mock所以不是null,會讓回傳值是null,但不在這個測試方法範圍,應該要寫在DB測試或不應該產生null
        when(remoteDS.getAll()).thenThrow(socketTimeoutException);
        CompletableFuture<Void> future=null;
        try {
            //測試方法
            future= employeeRepository.getAllServerData();//這個方法被呼叫,上面的,而裡面是開執行緒在做,所以為了確保執行緒做完,所以修改方法回傳值
            future.join();//等他完成,不然有時煮執行緒比較快
        }catch (Exception ignored){
        }

        //預期結果 結果驗證: 1.future不可以是null 2.future是發生例外結束 3.netWorkIOResultDao.insert()有被呼叫一次,時間是多少不知道而已
        Assert.assertNotNull(future);
        //預期結果需要用到的中間值,真正預期結果在後面
        NetWorkIOResult netWorkIOResult = new NetWorkIOResult();
        netWorkIOResult.dataTime=LocalDateTime.now();
        netWorkIOResult.msgId = 0;
        netWorkIOResult.hasShow=0;
        netWorkIOResult.msg = socketTimeoutException.getLocalizedMessage();
        verify(netWorkIOResultDao,times(1)).insert(netWorkIOResult);
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    public void getNetWorkIOResult() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void createEmployee() {
    }

    @Test
    public void updateEmployee() {
    }

    @Test
    public void netWorkIOHasShow() {
    }
}