package com.example.crudapplication.db;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.crudapplication.db.conveter.MyTypeConverter;
import com.example.crudapplication.db.dao.EmployeeDao;
import com.example.crudapplication.db.dao.NetWorkIOResultDao;
import com.example.crudapplication.db.entity.Employee;
import com.example.crudapplication.db.entity.NetWorkIOResult;

@Database(entities = {Employee.class, NetWorkIOResult.class},version = 2,
        autoMigrations = {
        @AutoMigration(from = 1, to = 2)})
@TypeConverters(MyTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmployeeDao employeeDao();
    public abstract NetWorkIOResultDao netWorkIOResultDao();
}
