package com.example.crudapplication.ui.read;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.crudapplication.repository.EmployeeJson;
import com.example.crudapplication.repository.EmployeeRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class ReadViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule=new InstantTaskExecutorRule();

    ReadViewModel readViewModel;

    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    Observer<List<EmployeeUiState>> employeeUiStateObserver;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        readViewModel=new ReadViewModel(employeeRepository);
    }

    /*
    * 在有觀察者下 viewModel觀察DB變化
    * 並轉呈UI state物件
    * */
    @Test
    public void use_repository_get_employee_data_test() {
        //測試資料 3個Json員工
        List<EmployeeJson> webData=TestUtil.createEmployeeList(3);
        //測試物件 ReadViewModel 相依物件 EmployeeRepository
        //模擬方法
        when(employeeRepository.getAllEmployee()).thenReturn(new MutableLiveData<>(webData));
        //測試方法
        readViewModel.getAllEmployee().observeForever(employeeUiStateObserver);
        //預期結果 有發生一次狀態改變
        //驗證
        verify(employeeUiStateObserver,times(1)).onChanged(anyList());
    }

    /*
     *資料庫是空的也會造成觀察者資料變動
     **/
    @Test
    public void use_repository_get_employee_empty_data_test(){
        //測試資料 3個Json員工
        List<EmployeeJson> webData=TestUtil.createEmployeeList(0);
        //測試物件 ReadViewModel 相依物件 EmployeeRepository
        //模擬方法
        when(employeeRepository.getAllEmployee()).thenReturn(new MutableLiveData<>(webData));
        //測試方法
        readViewModel.getAllEmployee().observeForever(employeeUiStateObserver);
        //預期結果 有發生一次狀態改變
        //驗證
        verify(employeeUiStateObserver,times(1)).onChanged(anyList());
    }
}