package com.example.crudapplication;

import android.app.Application;

import com.example.crudapplication.db.AppDatabase;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {
    @Inject
    ExecutorService executorService;
    @Inject
    AppDatabase db;

    @Override
    public void onTerminate() {
        if(executorService!=null){
            executorService.shutdown();
        }

        if(db!=null){
            db.close();
        }
        super.onTerminate();
    }
}
