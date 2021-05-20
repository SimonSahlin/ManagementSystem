package com.consid.managementsystem.service;

import com.consid.managementsystem.model.Employee;
import com.consid.managementsystem.repository.EmployeeReposity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpm implements EmployeeService{
    
    @Autowired
    private EmployeeReposity employeeReposity;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeReposity.findAll();
    }

    @Override
    public void saveEmployee(Employee employee) {
        this.employeeReposity.save(employee);
        
    }

    @Override
    public Employee getEmployeeById(long id) {
        Optional<Employee> optional = employeeReposity.findById(id);
        Employee employee = null;
        if(optional.isPresent()){
            employee = optional.get();
        } else {
            throw new RuntimeException(" Employee not found for id :: " + id);
            
        }
        return employee;
    }

    @Override
    public void deleteEmployeeById(long id) {
        this.employeeReposity.deleteById(id);
    }

}
