package com.consid.managementsystem.service;

import com.consid.managementsystem.model.Employee;
import com.consid.managementsystem.repository.EmployeeReposity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public class EmployeeServiceImpm implements EmployeeService {

    @Autowired
    private EmployeeReposity employeeReposity;

    // Get all employees from the DB
    @Override
    public List<Employee> getAllEmployees() {
        return employeeReposity.findAll();
    }

    // Saving the employee to the db
    @Override
    public void saveEmployee(Employee employee) {
        this.employeeReposity.save(employee);
    }

    // Getting an employee from the DB by Id
    @Override
    public Employee getEmployeeById(int id) {
        Optional<Employee> optional = employeeReposity.findById(id);
        Employee employee = null;
        if (optional.isPresent()) {
            employee = optional.get();
        } else {
            throw new RuntimeException(" Employee not found for id :: " + id);

        }
        return employee;
    }

    // Deleting the employee from the db
    @Override
    public void deleteEmployeeById(int id) {
        this.employeeReposity.deleteById(id);
    }

    // Calculating the salary based on rank, if the employee is a manager and if the
    // employee is the CEO.
    public int calculateSalary(@ModelAttribute("employee") Employee employee) {
        int salary;
        if (employee.getIsManager().equals("Yes") && employee.getIsCeo().equals("Yes")) {
            salary = employee.getSalary() * 2725;
        } else if (employee.getIsManager().equals("Yes") && employee.getIsCeo().equals("No")) {
            salary = employee.getSalary() * 1725;
        } else {
            salary = employee.getSalary() * 1125;
        }
        return salary;
    }

    //Function to check if the updatedEmployee is the current CEO
    public String isTheUpdatedEmployeeCurrentCeo(Employee employee){
        Employee ceoEmployee = getEmployeeById(employee.getId());

        String checkForCeo = "";

        if(ceoEmployee.getIsCeo().equals("Yes")){
            checkForCeo = "true";
            return checkForCeo;
        }
        checkForCeo = "false";
        return checkForCeo;
    }

    // Function to check if there is a CEO already pressent in the db.
    public String checkForCeo(@ModelAttribute("employee") Employee employee) {
        List<Employee> employeesInDb = getAllEmployees();
        String checkForCeo = "";

        // If-statement that checks if the above linkedlist does include a CEO
        if (employee.getIsCeo().equals("Yes")) {
            for (int i = 0; i < employeesInDb.size(); i++) {
                if (employeesInDb.get(i).getIsCeo().equals("Yes")) {
                    checkForCeo = "true";
                    return checkForCeo;
                } else {
                    checkForCeo = "false";
                }
            }
        }
        return checkForCeo;
    }

    // Checking if the employeeId is valid and if the EmployeeId is a manager.
    public String checkForEmployeeIdAsManagerId(@ModelAttribute("employee") Employee employee) {
        List<Employee> employeesInDb = getAllEmployees();

        String employeeIdForManager = "";

        if (employeesInDb.size() == 0) {
            return employeeIdForManager = "true";
        }
        // Check if input of employeeId as managerId is present in the database.
        for (int i = 0; i < employeesInDb.size(); i++) {
            try {
                if (Integer.parseInt(employee.getManagerId()) == employeesInDb.get(i).getId()
                        && employeesInDb.get(i).getIsManager().equals("Yes")) {
                    // Check if the EmployeeId is a manager.
                    employeeIdForManager = "true";
                    return employeeIdForManager;
                }
            } catch (Exception e) {
                
            }
        }
        employeeIdForManager = "false";
        return employeeIdForManager;
    }

    // Checking if the deleted employee is a manager over another employee.
    public String isTheEmployeeAManager(int id) {
        // List<Employee> employeesInDb = getAllEmployees();

        String isTheEmployeeAManager = "";

        if (getEmployeeById(id).getIsManager().equals("Yes")) {
            // Would return a Yes and the manager cant be removed until the other employees
            // are updated.
            isTheEmployeeAManager = "Yes";
            return isTheEmployeeAManager;
        }
        isTheEmployeeAManager = "No";
        return isTheEmployeeAManager;
    }

    public String doesTheManagerManageAnyone(int id) {
        List<Employee> employeesInDb = getAllEmployees();

        String theManagerManageAnEmployee = "";

        for (int i = 1; i < employeesInDb.size(); i++) {
            if (Integer.parseInt(employeesInDb.get(i).getManagerId()) == id) {
                theManagerManageAnEmployee = "Yes";
                return theManagerManageAnEmployee;
            }
        }
        theManagerManageAnEmployee = "No";
        return theManagerManageAnEmployee;
    }

    public int currentCeoEmployeeId(){
        List<Employee> employeesInDb = getAllEmployees();
        
        int theCeosEmployeeId; 

        for(int i = 0; i < employeesInDb.size(); i++){
            if(employeesInDb.get(i).getIsCeo().equals("Yes")){
                theCeosEmployeeId = employeesInDb.get(i).getId();
                return theCeosEmployeeId;
            }
        }
        return 0;
    }

}
