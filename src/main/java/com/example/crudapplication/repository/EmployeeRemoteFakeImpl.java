package com.example.crudapplication.repository;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.LongStream;

public class EmployeeRemoteFakeImpl implements EmployeeRemoteDS {
    private List<EmployeeJson> fakeRemoteDB;

    @Override
    public List<EmployeeJson> fetchAll() throws IOException {
        fakeRemoteDB=new ArrayList<>();
        Faker faker=new Faker(Locale.TAIWAN);
        Random random=new Random();
        //產生虛擬資料
        LongStream.range(1,101).forEach(id->{
            fakeRemoteDB.add(new EmployeeJson(id,faker.name().name(),
                    random.nextInt(100)+18,
                    faker.phoneNumber().subscriberNumber(10)));
        });
        return fakeRemoteDB;
    }
}
