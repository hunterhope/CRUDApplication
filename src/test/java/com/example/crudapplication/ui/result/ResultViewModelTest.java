package com.example.crudapplication.ui.result;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.crudapplication.TestUtil;
import com.example.crudapplication.db.entity.Employee;
import com.example.crudapplication.repository.EmployeeRepository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RunWith(JUnit4.class)
public class ResultViewModelTest {
    @Rule//讓執行緒符合結構元件要的執行緒
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    EmployeeRepository employeeRepository;

    private ResultViewModel resultViewModel;

    @Mock
    Observer<List<EmployeeUiState>> employeeUiStateObserver;
    @Mock
    Observer<Boolean> deleteDataObserver;

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
        resultViewModel.getAllEmployee().observeForever(employeeUiStateObserver);
        //驗證,當觀察者改變,所得到資料是不是我預期的
        List<EmployeeUiState> expected = new ArrayList<>();
        /*
        public EmployeeUiState(Long id,String name,Integer age,String phone,boolean selected,boolean click,
        BiConsumer<Long, Boolean> deleteChockBox,Consumer<EmployeeUiState> updateAction) {
         */
        expected.add(new EmployeeUiState(
                1L,"John",2,"0968751923",false,false,null,null));
        expected.add(new EmployeeUiState(
                2L,"Mary",3,"0978570985",false,false,null,null));
        verify(employeeUiStateObserver).onChanged(expected);
        System.out.println("預期資料");
        System.out.println(expected);

    }

    @Test
    public void getDeleteBtnIsVisible() {
        //加入是否有刪除資料的觀察者
        resultViewModel.getDeleteBtnVisible().observeForever(deleteDataObserver);
        //測試資料,取得經過getAll()的UI狀態,裡面才有按鈕動作的method
        letViewModelHasUiState();
        List<EmployeeUiState> states = resultViewModel.getAllEmployeeStates();
        //當按下第一筆資料 刪除checkedBox時,要做的動作
        states.get(0).idClick(true);
        //驗證 第一筆資料 刪除checkedBox被按下
        //預期值 true
        Boolean expectedChecked=true;
        verify(deleteDataObserver,times(1)).onChanged(expectedChecked);
        //驗證 第一筆資料 刪除checkedBox再被按下
        states.get(0).idClick(false);
        //預期值 false
        Boolean expectedUnchecked=false;
        verify(deleteDataObserver,times(2)).onChanged(expectedUnchecked);
    }

    private List<Employee> letViewModelHasUiState(){
        List<Employee> dbData = TestUtil.createTestDBData();
        //當數據庫被呼叫,回傳測試資料LiveData
        when(employeeRepository.getAll()).thenReturn(new MutableLiveData<>(dbData));
        //加入觀察者
        resultViewModel.getAllEmployee().observeForever(employeeUiStateObserver);
        //驗證,當觀察者改變,所得到資料是不是我預期的
        List<EmployeeUiState> expected = new ArrayList<>();
        /*
        public EmployeeUiState(Long id,String name,Integer age,String phone,boolean selected,boolean click,
        BiConsumer<Long, Boolean> deleteChockBox,Consumer<EmployeeUiState> updateAction) {
         */
        expected.add(new EmployeeUiState(
                1L,"John",2,"0968751923",false,false,null,null));
        expected.add(new EmployeeUiState(
                2L,"Mary",3,"0978570985",false,false,null,null));
        verify(employeeUiStateObserver).onChanged(expected);
        return dbData;
    }

    @Test
    public void deleteUserSelectData() {
        //測試資料準備
        List<Employee> dbData = letViewModelHasUiState();
        resultViewModel.getDeleteBtnVisible().observeForever(deleteDataObserver);//一般而言觀察者會在onCreate加入,所以這必須寫在 勾選第2筆 之前,不然裡面的setValue會碰到null
        //勾選第2筆
        EmployeeUiState uiState2 = resultViewModel.getAllEmployeeStates().get(1);
        uiState2.idClick(true);
        //模擬動作
        when(employeeRepository.delete(Collections.singleton(uiState2.getId()))).then(new Answer<CompletableFuture<Void>>() {
            @Override
            public CompletableFuture<Void> answer(InvocationOnMock invocation) throws Throwable {
                Set<Long> ids =invocation.getArgument(0);
                ids.forEach(deleteId-> dbData.removeIf(e->e.id.equals(deleteId)));
                return CompletableFuture.completedFuture(null);//這邊回傳是要讓viewModel真的動作,因為他還有後續動作
            }
        }).thenReturn(CompletableFuture.completedFuture(null));//這邊是模擬回傳的結果,因為employeeRepository是模擬,所以employeeRepository.delete()一定要有thenReturn
        //測試方法
        resultViewModel.deleteUserSelectData();
        //預期結果 dbData.size()==1 或是內部的 deleteDataObserver 要等於false
        Assert.assertEquals(1, dbData.size());
        System.out.println("DB資料");
        System.out.println(dbData);
        verify(deleteDataObserver).onChanged(false);
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
}