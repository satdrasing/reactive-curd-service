package com.satendra.employeeservice.daos;

import com.satendra.employeeservice.models.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}
