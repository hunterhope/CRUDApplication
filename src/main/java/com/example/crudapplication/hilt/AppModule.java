package com.example.crudapplication.hilt;

import android.content.Context;

import androidx.room.Room;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.entity.Employee;
import com.example.crudapplication.repository.RemoteDS;
import com.example.crudapplication.repository.RemoteDSImpl;
import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

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
    public AppDatabase provideAppDatabase(@ApplicationContext Context context,Executor executor){
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "employee.db").build();
        //初始化測試資料
        executor.execute(()->{
            if(db.employeeDao().count()==0){
                Faker faker =new Faker(Locale.TAIWAN);
                Random random=new Random();
                Employee[] testEmployeeList = IntStream.range(0, 100)
                        .mapToObj(i -> new Employee(faker.name().name(),random.nextInt(82)+18,faker.phoneNumber().subscriberNumber(10)))
                        .toArray(Employee[]::new);
                db.employeeDao().insert(testEmployeeList);
            }
        });
        return db;
    }

    @Provides
    @Singleton
    public ExecutorService providerExecutorService(){
        return Executors.newFixedThreadPool(2);
    }

    @Provides
    @Singleton
    public Executor provideExecutor(ExecutorService executorService) {
        return executorService;
    }

    @Provides
    @Singleton
    public RemoteDS provideRemoteDS(){
        return new RemoteDSImpl();
    }
}
