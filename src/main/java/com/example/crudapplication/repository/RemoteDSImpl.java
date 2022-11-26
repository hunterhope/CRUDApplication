package com.example.crudapplication.repository;

import com.example.crudapplication.db.entity.Employee;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

public class RemoteDSImpl implements RemoteDS{
    private final List<Employee> remoteData;
    @Inject
    public RemoteDSImpl() {
        remoteData=createTestDBData();
    }

    @Override
    public void delete(Set<Long> deleteData) {
        System.out.println("刪除遠端資料");
        deleteData.forEach(id-> remoteData.removeIf(e->e.id.equals(id)));

    }

    @Override
    public List<Employee> getAll() {
        System.out.println("撈取遠端資料");
        return remoteData;
    }

    public static List<Employee> createTestDBData(){
        Faker faker =new Faker(Locale.TAIWAN);
        Random random=new Random();
        return IntStream.range(0, 100)
                .mapToObj(i -> new Employee(i+1L,faker.name().name(),random.nextInt(82)+18,faker.phoneNumber().subscriberNumber(10)))
                .collect(Collectors.toList());
    }
}
