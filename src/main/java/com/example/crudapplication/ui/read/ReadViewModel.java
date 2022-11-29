package com.example.crudapplication.ui.read;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.crudapplication.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ReadViewModel extends ViewModel {
    private final EmployeeRepository employeeRepository;

    public ReadViewModel(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public LiveData<List<EmployeeUiState>> getAllEmployee() {
        return Transformations.map(employeeRepository.getAllEmployee(), input -> {
                    System.out.println("input=" + input);
                    return input.stream()
                            .map(e -> new EmployeeUiState(e.getId(), e.getName(), e.getAge(), e.getCellPhone()))
                            .collect(Collectors.toList());
                }
        );
    }
}
