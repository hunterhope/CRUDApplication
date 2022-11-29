package com.example.crudapplication.ui.read;

import com.example.crudapplication.db.entity.Employee;
import com.example.crudapplication.repository.EmployeeJson;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

public class TestUtil {
    public static List<Employee> createEmployeeList(int num) {
        List<Employee> employees=new ArrayList<>();
        Faker faker=new Faker(Locale.TAIWAN);
        Random random=new Random();
        IntStream.range(0,num).forEach(i->{
            Employee e = new Employee();
            e.id=i+1L;
            e.name=faker.name().name();
            e.age=random.nextInt(100)+18;
            e.phone=faker.phoneNumber().subscriberNumber(10);
            employees.add(e);
        });
        return employees;
    }
}
