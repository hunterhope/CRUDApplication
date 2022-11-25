package com.example.crudapplication.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.dao.EmployeeDao;
import com.example.crudapplication.db.entity.Employee;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class TestUtil {

    public static Employee createEmployee(){
        Faker faker=new Faker(Locale.TAIWAN);
        Random random=new Random();
        return new Employee(
                faker.name().name(),
                random.nextInt(82)+18,
                faker.phoneNumber().subscriberNumber(10));
    }

    public static AppDatabase createFakeAppDatabase(){
        return new AppDatabase() {
            private final List<Employee> employeesDB;
            {
                employeesDB = new ArrayList<>();
                IntStream.range(0,100).forEach(i->{
                    Employee employee = TestUtil.createEmployee();
                    employee.id= (long) (i + 1);
                    employeesDB.add(employee);
                });
            }
            @Override
            public EmployeeDao employeeDao() {
                return new EmployeeDao() {
                    @Override
                    public int count() {
                        return employeesDB.size();
                    }

                    @Override
                    public long[] insert(Employee... employees) {
                        return LongStream.range(101,101+employees.length)
                                .peek(i->{
                                    System.out.println("執行新增動作");
                                    employees[(int) (i-101)].id=i;
                                    employeesDB.add(employees[(int) (i-101)]);
                                }).toArray();
                    }

                    @Override
                    public LiveData<List<Employee>> getAll() {
                        return new MutableLiveData<>(employeesDB);
                    }

                    @Override
                    public void delete(Set<Long> deleteIds) {
                        deleteIds.forEach(id->{
                            Employee em = employeesDB.stream().filter(e -> e.id.equals(id)).findAny().orElseGet(Employee::new);
                            employeesDB.remove(em);
                        });
                    }

                    @Override
                    public void update(Employee employee) {
                        employeesDB.stream().filter(e->e.id.equals(employee.id)).forEach(e->{
                            e.age=employee.age;
                            e.phone=employee.phone;
                            e.name=employee.name;
                        });
                    }

                    @Override
                    public List<Long> selectAllIds() {
                        return employeesDB.stream().map(e->e.id).collect(Collectors.toList());
                    }

                    @Override
                    public List<Employee> getAllEmployees() {
                        return employeesDB;
                    }
                };
            }

            @NonNull
            @Override
            protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
                return null;
            }

            @NonNull
            @Override
            protected InvalidationTracker createInvalidationTracker() {
                return null;
            }

            @Override
            public void clearAllTables() {

            }
        };
    }
}
