package com.example.crudapplication.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudapplication.R;
import com.example.crudapplication.databinding.EmployeeItemBinding;
import com.example.crudapplication.ui.result.EmployeeUiState;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private List<EmployeeUiState> employeeUiSates;

    @Inject
    public EmployeeAdapter() {
        employeeUiSates= Collections.emptyList();
    }

    public void setEmployeeUiSates(List<EmployeeUiState> employeeUiSates) {
        this.employeeUiSates = employeeUiSates;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EmployeeItemBinding binding=EmployeeItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmployeeUiState item = employeeUiSates.get(position);
        holder.binding.itemId.setTag(item);
        holder.binding.itemId.setChecked(item.isSelected());
        holder.binding.itemName.setText(item.getName());
        holder.binding.itemAge.setText(String.valueOf(item.getAge()));
        holder.binding.itemPhone.setText(item.getPhone());
        Resources r = holder.binding.getRoot().getResources();
        holder.binding.getRoot().setBackgroundColor(
                item.isSelected() || item.isClick()?
                r.getColor(R.color.click,null):r.getColor(R.color.white,null));
    }

    @Override
    public long getItemId(int position) {
        return employeeUiSates.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return employeeUiSates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final EmployeeItemBinding binding;
        public ViewHolder(EmployeeItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            initEvent();
        }

        private void initEvent() {
            binding.itemId.setOnCheckedChangeListener((buttonView, isChecked) -> {
                EmployeeUiState employeeUiState  = (EmployeeUiState) binding.itemId.getTag();
                employeeUiState.idClick(isChecked);
            });

            binding.getRoot().setOnClickListener(v -> {
                EmployeeUiState employeeUiState  = (EmployeeUiState) binding.itemId.getTag();
                employeeUiState.update();
            });
        }
    }
}
