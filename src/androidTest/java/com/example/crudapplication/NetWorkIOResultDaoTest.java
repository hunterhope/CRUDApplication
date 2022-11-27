package com.example.crudapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.UiThread;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.dao.NetWorkIOResultDao;
import com.example.crudapplication.db.entity.NetWorkIOResult;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RunWith(AndroidJUnit4.class)
public class NetWorkIOResultDaoTest {
    private AppDatabase db;
    private NetWorkIOResultDao netWorkIOResultDao;
    private final Object waitResultKey=new Object();
    @Before
    public void createDb() throws InterruptedException {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context,AppDatabase.class).build();
        netWorkIOResultDao = db.netWorkIOResultDao();
    }

    @After
    public void closed() {
        db.close();
    }

    private void notifyWaiter(){
        synchronized (waitResultKey){
            waitResultKey.notifyAll();
        }
    }
    private void waitResult() throws InterruptedException {
        Log.d("NetWorkIOResultDaoTest","等待查詢結果");
        synchronized (waitResultKey){
            waitResultKey.wait(10_000);
        }
    }
    private void waitTestEnd() throws InterruptedException {
        synchronized (waitResultKey){
            waitResultKey.wait(10_000);
        }
        Log.d("NetWorkIOResultDaoTest","測試結束");
    }

    @Test
    public void insertTest() throws InterruptedException {
        //建立測試資料
        NetWorkIOResult insertData = createNetWorkIOResult();
        //預期結果
        Optional<NetWorkIOResult> expected1=Optional.empty();
        Optional<NetWorkIOResult> expected2=Optional.of(insertData);
        addNetWorkIOResultTableObserver(Arrays.asList(expected1,expected2));
        //測試方法
        netWorkIOResultDao.insert(insertData);
        waitTestEnd();
    }

    @Test
    public void latestResultTest() throws InterruptedException {
        //預期結果
        Optional<NetWorkIOResult> expected1=Optional.empty();
        addNetWorkIOResultTableObserver(Arrays.asList(expected1));
        Log.d("NetWorkIOResultDaoTest","測試結束");
    }
    @Test
    public void msgHasShowTest() throws InterruptedException {
        //建立一個假訊息
        NetWorkIOResult testData=createNetWorkIOResult();
        //預期結果
        Optional<NetWorkIOResult> expected1=Optional.empty();
        Optional<NetWorkIOResult> expected2=Optional.of(testData);
        Optional<NetWorkIOResult> expected3=Optional.empty();
        //加入觀察者
        addNetWorkIOResultTableObserver(Arrays.asList(expected1,expected2,expected3));
        //放入資料庫
        netWorkIOResultDao.insert(testData);
        waitResult();
        //修改訊息已顯示
        netWorkIOResultDao.msgHasShow(testData.dataTime);
        waitTestEnd();
    }

    private NetWorkIOResult createNetWorkIOResult(){
        NetWorkIOResult data = new NetWorkIOResult();
        data.dataTime= LocalDateTime.now();
        data.msgId=1;
        data.hasShow=0;
        data.msg="測試資料1";
        return data;
    }
    private void addNetWorkIOResultTableObserver(List<Optional<NetWorkIOResult>> expectedValues) throws InterruptedException {

        ApplicationProvider.getApplicationContext().getMainExecutor().execute(()->{
            try {
                Thread.sleep(30);//確保等待者先進入等待狀態
            } catch (InterruptedException ignored) {
            }
            netWorkIOResultDao.latestResult().observeForever(new Observer<Optional<NetWorkIOResult>>() {
                private int count;
                @Override
                public void onChanged(Optional<NetWorkIOResult> netWorkIOResult) {
                    Log.d("NetWorkIOResultDaoTest","LiveData得到結果,第"+count+"次收到訊息");
                    Log.d("NetWorkIOResultDaoTest",netWorkIOResult.toString());
                    Assert.assertEquals(expectedValues.get(count++),netWorkIOResult);
                    notifyWaiter();
                }
            });
        });
        //加入觀察者,基本上就會有log輸出,避免誤會所以這邊等他回應,在繼續往下做
        waitResult();
    }
}
