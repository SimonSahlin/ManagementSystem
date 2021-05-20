package com.consid.managementsystem.controller;

import java.util.LinkedList;
import java.util.List;

import com.consid.managementsystem.model.Employee;
import com.consid.managementsystem.service.EmployeeService;

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
    /*
    @Autowired
    private EmployeeService employeeService;

    public String checkForCeo(@ModelAttribute("employee") Employee employee, Model model){
        LinkedList<String> listEmployees = new LinkedList<>();

        model.addAllAttributes(listEmployees);
        
        
        String checkForCeo = "false";
        
        if(employee.getIsCeo().equals("Yes")){
            for(int i = 0; i < listEmployees.size(); i++){
                
                if(areThereACeo.get(i).contains("Yes")){
                    checkForCeo = "true";
                    return checkForCeo;
                }
            } 
        }
        return checkForCeo;
    }*/

}
