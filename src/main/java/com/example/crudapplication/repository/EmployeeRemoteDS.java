package com.example.crudapplication.repository;

import java.io.IOException;
import java.util.List;

public interface EmployeeRemoteDS {
    List<EmployeeJson> fetchAll()throws IOException;
}
