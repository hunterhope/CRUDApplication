package com.example.crudapplication.hilt;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.crudapplication.db.AppDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public ExecutorService provideExecutorService(){
        return Executors.newFixedThreadPool(2);
    }

    @Provides
    @Singleton
    public Executor provideExecutor(ExecutorService executorService){
        return executorService;
    }

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context){
        return Room.databaseBuilder(context,AppDatabase.class,"crudApp.db").build();
    }

}
