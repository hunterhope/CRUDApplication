package com.example.crudapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudapplication.databinding.EmployeeItemBinding;
import com.example.crudapplication.ui.read.EmployeeUiState;

import java.util.List;

import javax.inject.Inject;

public class EmployeeUiStateAdapter extends RecyclerView.Adapter<EmployeeUiStateAdapter.ViewHolder> {
    private List<EmployeeUiState> data;

    @Inject
    public EmployeeUiStateAdapter() {
    }

    public void setData(List<EmployeeUiState> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EmployeeItemBinding binding = EmployeeItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmployeeUiState item = data.get(position);
        holder.binding.setItem(item);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data==null? 0:data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final EmployeeItemBinding binding;
        public ViewHolder(EmployeeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
