package com.satendra.employeeservice.controller;

import com.satendra.employeeservice.daos.EmployeeRepository;
import com.satendra.employeeservice.models.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping(value = "/employee")
    public Flux<Employee> getAllEmployee() {
        return employeeRepository.findAll().switchIfEmpty(Mono.empty());
    }

    @GetMapping(value = "/employee1")
    public Flux<Employee> getAllEmployee1() {
        return employeeRepository.findAll().switchIfEmpty(Mono.empty());
    }


    @PostMapping("/employee")
    public Mono<ResponseEntity> addEmployee(@RequestBody @Valid Employee employee) {
        return this.employeeRepository
                .save(employee)
                .map(saved -> created(URI.create("/employee/" + saved.getId())).build());
    }

    @PutMapping("/employee/{id}")
    public Mono<ResponseEntity> update(@PathVariable("id") String id, @RequestBody @Valid Employee employee) {
        return this.employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException()))
                .map(p -> {
                    p.setDept(employee.getDept());
                    p.setName(employee.getName());
                    return p;
                })
                .flatMap(this.employeeRepository::save)
                .flatMap(data -> Mono.empty());
    }
}
