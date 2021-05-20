package com.consid.managementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import com.consid.managementsystem.model.Employee;
import com.consid.managementsystem.repository.EmployeeReposity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class FunctionsController {

    public int calculateSalary(@ModelAttribute("employee") Employee employee){
        int salary;
        if (employee.getIsManager().equals("Yes") && employee.getIsCeo().equals("Yes")){
            salary = employee.getSalary() * 2725;
        } else if (employee.getIsManager().equals("Yes") && employee.getIsCeo().equals("No")){
            salary = employee.getSalary() * 1725;
        } else {
            salary = employee.getSalary() * 1125;
        }
        return salary;
    }
    
    @Autowired
    private EmployeeReposity employeeReposity;

    public boolean checkForCeo(@ModelAttribute("employee") Employee employee, Model model){
        List<Employee> listEmployees = employeeReposity.findAll();
        List<String> areThereACeo = new ArrayList<>();
        boolean checkForCeo = false;

        if(employee.getIsCeo().equals("Yes")){
            for(int i = 0; i < listEmployees.size(); i++){
                areThereACeo.add(listEmployees.get(i).getIsCeo());
                if(areThereACeo.get(i).contains("Yes")){
                    checkForCeo = true;
                    return checkForCeo;
                }
            } 
        }
        return checkForCeo;
    }

}
