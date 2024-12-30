package com.demo.employeeservice.controller;

import com.demo.employeeservice.model.Employee;
import com.demo.employeeservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public Collection<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public String addEmployee(@RequestBody Employee employee) {
        if (employeeService.isIdUnique(employee.getId())) {
            employeeService.saveEmployee(employee);
            return "Employee added successfully!";
        } else {
            return "Employee ID already exists!";
        }
    }

    @PostMapping("/refresh")
    public String refreshEmployeeData() {
        employeeService.refreshEmployeeDataAsync();
        return "Employee data refresh initiated!";
    }
}
