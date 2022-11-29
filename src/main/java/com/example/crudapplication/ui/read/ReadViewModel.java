package com.example.crudapplication.ui.read;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.crudapplication.repository.EmployeeRepository;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReadViewModel extends ViewModel {
    private final EmployeeRepository employeeRepository;

    @Inject
    public ReadViewModel(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public LiveData<List<EmployeeUiState>> getAllEmployee() {
        return Transformations.map(employeeRepository.getAllEmployee(), input -> {
                    System.out.println("input=" + input);
                    return input.stream()
                            .map(e -> new EmployeeUiState(e.id, e.name, e.age, e.phone))
                            .collect(Collectors.toList());
                }
        );
    }

    public void fetchLatestEmployee() {

        employeeRepository.fetchAll().whenComplete(new BiConsumer<Void, Throwable>() {
            @Override
            public void accept(Void unused, Throwable throwable) {
                System.out.println("抓取任務結束");
                if(throwable!=null){
                    System.out.println("發生例外");
                }
            }
        });
    }
}
