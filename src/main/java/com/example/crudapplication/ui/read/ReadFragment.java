package com.example.crudapplication.ui.read;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ReadFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReadViewModel readViewModel =
                new ViewModelProvider(this).get(ReadViewModel.class);


        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}