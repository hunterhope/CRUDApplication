package com.example.crudapplication;

import com.example.crudapplication.db.entity.Employee;
import com.github.javafaker.Faker;

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
}
