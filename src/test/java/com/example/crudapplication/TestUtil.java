package com.example.crudapplication;

import com.example.crudapplication.db.entity.Employee;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TestUtil {

    public static Employee createEmployee(){
        Faker faker=new Faker(Locale.TAIWAN);
        Random random=new Random();
        return new Employee(
                faker.name().name(),
                random.nextInt(82)+18,
                faker.phoneNumber().subscriberNumber(10));
    }
    public static List<Employee> createTestDBData(){
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(new Employee(1L,"John",2,"0968751923"));
        employeeList.add(new Employee(2L,"Mary",3,"0978570985"));
        System.out.println("資料庫目前有:");
        System.out.println(employeeList);
        return employeeList;
    }
}
