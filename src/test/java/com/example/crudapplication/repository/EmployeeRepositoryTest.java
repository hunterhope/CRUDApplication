package com.example.crudapplication.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import com.example.crudapplication.db.AppDatabase;
import com.example.crudapplication.db.entity.Employee;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class EmployeeRepositoryTest {
    private EmployeeRepository employeeRepository;
    private final AppDatabase db = TestUtil.createFakeAppDatabase();
    private final Object waitResultKey = new Object();

    @Before
    public void setUp() {
        employeeRepository = new EmployeeRepository(db, Executors.newFixedThreadPool(1));
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void getAll() {
        assertTrue(true);
    }

    @Test
    public void delete() throws InterruptedException {
        Random r = new Random();
        //刪除指定id
        Set<Long> deleteIds = LongStream.generate(() -> r.nextInt(100) + 1L).limit(10).boxed().collect(Collectors.toSet());
        System.out.println("刪除Id=" + deleteIds);
        CompletableFuture<Boolean> future = employeeRepository.delete(deleteIds).whenComplete(new BiConsumer<Boolean, Throwable>() {
            @Override
            public void accept(Boolean aBoolean, Throwable throwable) {
                if (aBoolean) {
                    List<Employee> result = employeeRepository.getAllSync();
                    Set<Long> dbRemainIds = result.stream().map(e -> e.id).collect(Collectors.toSet());
                    System.out.println("db剩下Id=" + dbRemainIds);
                    assertFalse(dbRemainIds.containsAll(deleteIds));
                }
                assertTrue(aBoolean);
                notifyWaiter();
            }
        });
        waitResult();
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    public void createEmployee() throws InterruptedException {
        Employee createE = TestUtil.createEmployee();
        CompletableFuture<Long> future = employeeRepository.createEmployee(createE).whenComplete((id, throwable) -> {
                assertNotNull(id);
                Employee newE = employeeRepository.getAllSync().stream().filter(e -> Objects.equals(e.id, id)).findFirst().orElse(new Employee());
                assertTrue(createE.equals(newE) && id.equals(newE.id));
                notifyWaiter();
        });
        waitResult();
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    public void updateEmployee() throws InterruptedException {
        Employee employee=TestUtil.createEmployee();
        employee.id=2L;
        CompletableFuture<Boolean> future = employeeRepository.updateEmployee(employee).whenComplete(new BiConsumer<Boolean, Throwable>() {
            @Override
            public void accept(Boolean aBoolean, Throwable throwable) {
                assertTrue(aBoolean);
                assertTrue(employeeRepository.getAllSync().stream().anyMatch(e -> e.equals(employee)));
                notifyWaiter();
            }
        });
        waitResult();
        assertFalse(future.isCompletedExceptionally());
        
    }

    private void notifyWaiter() {
        synchronized (waitResultKey) {
            waitResultKey.notifyAll();
        }
    }

    private void waitResult() throws InterruptedException {
        System.out.println("等待查詢結果");
        synchronized (waitResultKey) {
            waitResultKey.wait(10_000);
        }
        System.out.println("測試結束");
    }
}