package com.example.crudapplication.ui.read;

import com.example.crudapplication.repository.EmployeeJson;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

public class TestUtil {
    public static List<EmployeeJson> createEmployeeList(int num) {
        List<EmployeeJson> employeeJsonList=new ArrayList<>();
        Faker faker=new Faker(Locale.TAIWAN);
        Random random=new Random();
        IntStream.range(0,num).forEach(i->{
            employeeJsonList.add(new EmployeeJson(i+1L,faker.name().name(),
                    random.nextInt(100)+18,
                    faker.phoneNumber().subscriberNumber(10)));
        });
        return employeeJsonList;
    }
}
