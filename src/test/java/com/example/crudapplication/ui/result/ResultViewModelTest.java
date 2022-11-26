package com.example.crudapplication.ui.result;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.crudapplication.TestUtil;
import com.example.crudapplication.db.entity.Employee;
import com.example.crudapplication.repository.EmployeeRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RunWith(JUnit4.class)
public class ResultViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    EmployeeRepository employeeRepository;

    private ResultViewModel resultViewModel;

    @Mock
    Observer<List<EmployeeUiState>> observer;

    @Mock
    LifecycleOwner lifecycleOwner;
    @Mock
    Lifecycle lifecycle;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        resultViewModel = new ResultViewModel(employeeRepository);

    }

    @After
    public void tearDown() throws Exception {
        employeeRepository = null;
        resultViewModel = null;
    }

    @Test
    public void getAllEmployee() {
        //測試資料
        List<Employee> dbData = TestUtil.createTestDBData();
        //當數據庫被呼叫,回傳測試資料LiveData
        when(employeeRepository.getAll()).thenReturn(new MutableLiveData<>(dbData));
        //加入觀察者
        resultViewModel.getAllEmployee().observeForever(observer);
        //驗證,當觀察者改變,所得到資料是不是我預期的
        List<EmployeeUiState> expected = new ArrayList<>();
        /*
        public EmployeeUiState(
        Long id,
        String name,
        Integer age,
        String phone,
        boolean selected,
        boolean click,
        BiConsumer<Long, Boolean> deleteChockBox,
        Consumer<EmployeeUiState> updateAction) {
         */
        Set<Long> deleteData=new HashSet<>();
        MutableLiveData<Boolean> deleteDataIsEmpty=new MutableLiveData<>();
        BiConsumer<Long,Boolean> deleteChoiceAction = (id, clicked) -> {
            if (clicked) {
                deleteData.add(id);
            } else {
                deleteData.remove(id);
            }
            deleteDataIsEmpty.setValue(deleteData.isEmpty());
        };
        MutableLiveData<EmployeeUiState> updateData = new MutableLiveData<>();
        Consumer<EmployeeUiState> updateAction= updateData::setValue;
        expected.add(new EmployeeUiState(
                1L,"John",2,"0968751923",false,false,deleteChoiceAction,updateAction));
        expected.add(new EmployeeUiState(
                2L,"Mary",3,"0978570985",false,false,deleteChoiceAction,updateAction));
        verify(observer).onChanged(expected);
        System.out.println("預期資料");
        System.out.println(expected);

    }

    @Test
    public void getDeleteDataIsEmpty() {
        assertTrue(true);
    }

    @Test
    public void deleteUserSelectData() {

    }

    @Test
    public void getUpdateData() {
    }

    @Test
    public void updateOrCreateEmployee() {
    }

    @Test
    public void createEmployee() {
    }

    @Test
    public void getScrollTo() {
    }

    @Test
    public void getToastId() {
    }

    @Test
    public void msgHasShow() {
    }

    @Test
    public void testGetAllEmployee() {
    }

    @Test
    public void testGetDeleteDataIsEmpty() {
    }

    @Test
    public void testDeleteUserSelectData() {
    }

    @Test
    public void testGetUpdateData() {
    }

    @Test
    public void testUpdateOrCreateEmployee() {
    }

    @Test
    public void testCreateEmployee() {
    }

    @Test
    public void testGetScrollTo() {
    }

    @Test
    public void testGetToastId() {
    }

    @Test
    public void testMsgHasShow() {
    }
}