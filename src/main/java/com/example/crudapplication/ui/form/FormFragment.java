package com.example.crudapplication.ui.form;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.crudapplication.R;
import com.example.crudapplication.databinding.FragmentFormBinding;
import com.example.crudapplication.ui.result.ResultViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class FormFragment extends DialogFragment {

    private FragmentFormBinding binding;
    ResultViewModel resultViewModel;
    NavController navController;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFormBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        resultViewModel = new ViewModelProvider(requireActivity()).get(ResultViewModel.class);
        navController=NavHostFragment.findNavController(this);
        resultViewModel.getUpdateData().observe(getViewLifecycleOwner(),employeeUiState -> {
            if(employeeUiState!=null){
                binding.formName.setText(employeeUiState.getName());
                binding.formAge.setText(String.valueOf(employeeUiState.getAge()));
                binding.formPhone.setText(employeeUiState.getPhone());
            }else{
                binding.formOkBtn.setText(getString(R.string.form_btn_create));
            }
        });
        binding.formCancleBtn.setOnClickListener(v -> navController.popBackStack());
        binding.formOkBtn.setOnClickListener(v-> {
            resultViewModel.updateOrCreateEmployee(
                    binding.formName.getText().toString(),
                    binding.formAge.getText().toString(),
                    binding.formPhone.getText().toString());
            navController.popBackStack();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}