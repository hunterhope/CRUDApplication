package com.example.crudapplication.hilt;

import com.example.crudapplication.repository.EmployeeRemoteDS;
import com.example.crudapplication.repository.EmployeeRemoteFakeImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public abstract class ViewModelModule {
    @Binds
    public abstract EmployeeRemoteDS employeeRemoteDS(EmployeeRemoteFakeImpl employeeRemoteFake);
}
