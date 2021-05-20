package com.consid.managementsystem.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.consid.managementsystem.model.Employee;
import com.consid.managementsystem.service.EmployeeService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import net.bytebuddy.asm.Advice.AllArguments;

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

    public String checkForCeo(@ModelAttribute("employee") Employee employee, Model model) throws SQLException{
        String mysqlUrl = "jdbc:mysql://localhost/consid";
        Connection con = DriverManager.getConnection(mysqlUrl, "myuser", "xxxx");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select is_ceo from employees");
        
        String checkForCeo = "false";

        LinkedList<String> employeeList = new LinkedList<>();
        //Adds all the "is_ceo" values into a linkedlist.
        while(rs.next()){
            String addCeo = rs.getString("is_ceo");
            employeeList.add(addCeo);
        }
        //If-statement that checks if the above linkedlist does include a CEO
        if(employee.getIsCeo().equals("Yes")){
            for(int i = 0; i < employeeList.size(); i++){
                if(employeeList.get(i).equals("Yes")){
                    checkForCeo = "true";
                    return checkForCeo;
                }
            } 
        }
        return checkForCeo;
    }

}
