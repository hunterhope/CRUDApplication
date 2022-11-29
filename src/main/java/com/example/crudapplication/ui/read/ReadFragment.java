package com.example.crudapplication.ui.read;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.crudapplication.adapter.EmployeeUiStateAdapter;
import com.example.crudapplication.databinding.FragmentReadBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReadFragment extends Fragment {

    ReadViewModel readViewModel;
    FragmentReadBinding dataBinding;
    @Inject
    EmployeeUiStateAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        readViewModel = new ViewModelProvider(this).get(ReadViewModel.class);
        dataBinding=FragmentReadBinding.inflate(inflater,container,false);
        dataBinding.setReadViewModel(readViewModel);
        dataBinding.setAdapter(adapter);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readViewModel.getAllEmployee().observe(getViewLifecycleOwner(),employeeUiStates ->{
            System.out.println("通知Adapter改變");
            System.out.println(employeeUiStates);
            adapter.setData(employeeUiStates);
        });
        readViewModel.fetchLatestEmployee();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataBinding=null;
    }
}