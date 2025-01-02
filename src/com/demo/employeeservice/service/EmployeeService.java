package com.demo.employeeservice.service;

import com.demo.employeeservice.model.Employee;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final String FILE_PATH = "employees.txt";
    private final Map<Integer, Employee> employeeMap = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    // Load employees from file 
    public void loadEmployees() {
        lock.lock();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.lines()  // Use stream to read lines from the file
                    .map(line -> {
                        String[] data = line.split(",");
                        return new Employee(
                                Integer.parseInt(data[0].trim()),
                                data[1].trim(),
                                data[2].trim()
                        );
                    })
                    .forEach(employee -> employeeMap.put(employee.getId(), employee));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // Save employee to file and the map
    public void saveEmployee(Employee employee) {
        lock.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(employee.getId() + "," + employee.getName() + "," + employee.getPosition());
            writer.newLine();
            employeeMap.put(employee.getId(), employee);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // Fetch employee by ID
    public Employee getEmployeeById(int id) {
        return employeeMap.get(id);
    }

    // Fetch all employees
    public Collection<Employee> getAllEmployees() {
        return employeeMap.values();
    }

    // Check if an ID is unique
    public boolean isIdUnique(int id) {
        return !employeeMap.containsKey(id);
    }

    // Refresh employee data asynchronously (Java 8 CompletableFuture)
    public CompletableFuture<Void> refreshEmployeeDataAsync() {
        return CompletableFuture.runAsync(this::loadEmployees);
    }
}
