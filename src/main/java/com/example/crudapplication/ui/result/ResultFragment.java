package com.example.crudapplication.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.crudapplication.R;
import com.example.crudapplication.adapter.EmployeeAdapter;
import com.example.crudapplication.databinding.FragmentResultBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ResultFragment extends Fragment{

    FragmentResultBinding binding;
    ResultViewModel resultViewModel;
    @Inject
    EmployeeAdapter employeeAdapter;
    Menu menu;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                ResultFragment.this.menu=menu;
                menuInflater.inflate(R.menu.result_menu,menu);
                menu.findItem(R.id.deleteMenu).setVisible(Boolean.TRUE.equals(resultViewModel.getDeleteBtnVisible().getValue()));
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.deleteMenu:
                        resultViewModel.deleteUserSelectData();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        resultViewModel = new ViewModelProvider(requireActivity()).get(ResultViewModel.class);
        binding.employeeRv.setAdapter(employeeAdapter);
        resultViewModel.getAllEmployee()
                .observe(getViewLifecycleOwner(), employeeUiStates -> employeeAdapter.setEmployeeUiSates(employeeUiStates));
        resultViewModel.getDeleteBtnVisible().observe(getViewLifecycleOwner(), visible ->{
            if(menu!=null){
                menu.findItem(R.id.deleteMenu).setVisible(visible);
            }
        } );
        resultViewModel.getUpdateData().observe(getViewLifecycleOwner(),employeeUiState -> {
            if(employeeUiState!=null){
                NavController navController= NavHostFragment.findNavController(this);
                navController.navigate(R.id.action_global_navigation_create);
            }
        });
        resultViewModel.getScrollTo().observe(getViewLifecycleOwner(), scrollToPosition -> binding.employeeRv.scrollToPosition(scrollToPosition));
        resultViewModel.getToastId().observe(getViewLifecycleOwner(),msgId->{
            if(msgId!=null){
                Toast.makeText(getContext(), getString(msgId), Toast.LENGTH_SHORT).show();
                resultViewModel.msgHasShow();
            }
        });
        resultViewModel.getNetWorkIOUiState().observe(getViewLifecycleOwner(), optNetWorkIOUiState -> {
            if(optNetWorkIOUiState.isPresent()){
                Toast.makeText(requireContext(),optNetWorkIOUiState.get().getMsgId(),Toast.LENGTH_LONG).show();
                resultViewModel.netWorkMsgHasShow(optNetWorkIOUiState.get().getKey());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}