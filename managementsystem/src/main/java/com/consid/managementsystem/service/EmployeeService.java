package com.consid.managementsystem.service;

import java.util.List;

import com.consid.managementsystem.model.Employee;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    void saveEmployee(Employee employee);
    Employee getEmployeeById(int id);
    void deleteEmployeeById(int id);
    public int calculateSalary(Employee employee);
    public String checkForCeo(Employee employee);
    public String checkForEmployeeIdAsManagerId(Employee employee);
    public String isTheEmployeeAManager(int id);
    public String doesTheManagerManageAnyone(int id);
}
