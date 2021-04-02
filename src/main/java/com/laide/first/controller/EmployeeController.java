package com.laide.first.controller;

import com.laide.first.Exception.ResourceNotFoundException;
import com.laide.first.FirstApplication;
import com.laide.first.model.Employee;
import com.laide.first.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(FirstApplication.class);

    @Autowired
    private EmployeeRepository repository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        logger.info("Get all the employees...");
        return repository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") long employeeId) throws ResourceNotFoundException {
        logger.info("Get employee by id...");
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id:: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    //There was a @Valid annotation before the @RequestBody annotation
    // in the parameters field but I removed it because the code wouldn't compile
    public Employee createEmployee(@RequestBody Employee employee){
        logger.info("Insert employee...");
        return repository.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable(value = "id") long employeeId, @RequestBody Employee updatedEmployee) throws ResourceNotFoundException {
        logger.info("Update employee...");
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id:: " + employeeId));
        employee.setName(updatedEmployee.getName());
        employee.setAge(updatedEmployee.getAge());
        repository.save(employee);
        return ResponseEntity.ok().body(employee);
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable(value = "id") long employeeId) throws ResourceNotFoundException {
        logger.info("Delete employee...");
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id:: " + employeeId));
        repository.delete(employee);
    }
}
