package com.example.crudapplication.ui.result;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.crudapplication.repository.EmployeeRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

@RunWith(JUnit4.class)
public class ResultViewModelTest {
    @Mock
    EmployeeRepository employeeRepository;

    private ResultViewModel resultViewModel;
    @Mock
    Observer<List<EmployeeUiState>> observer;
    @Mock
    LifecycleOwner lifecycleOwner;
    Lifecycle lifecycle;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        resultViewModel=new ResultViewModel(employeeRepository);
    }

    @After
    public void tearDown() throws Exception {
        employeeRepository=null;
        resultViewModel=null;
    }

    @Test
    public void getAllEmployee() {
        when(employeeRepository.getAll()).thenReturn(new MutableLiveData<>());
        assertNotNull(resultViewModel.getAllEmployee());
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
}