package com.consid.managementsystem.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;

import com.consid.managementsystem.model.Employee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class Validations {


    //Calculating the salary based on rank, if the employee is a manager and if the employee is the CEO.
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

    //Function for connecting to database to make queryRequests to database.
    public Connection connectionForQuery() throws SQLException{
        String mysqlUrl = "jdbc:mysql://localhost/consid";
        Connection con = DriverManager.getConnection(mysqlUrl, "myuser", "xxxx");
        return con;
    }

    //Function to check if there is a CEO already pressent in the db. 
    public String checkForCeo(@ModelAttribute("employee") Employee employee) throws SQLException{
        Statement stmt = connectionForQuery().createStatement();
        ResultSet rs = stmt.executeQuery("select is_ceo from employees");
        
        String checkForCeo = "false";

        LinkedList<String> employeeList = new LinkedList<>();
        //Adds all the "is_ceo" values into a linkedlist.
        while(rs.next()){
            String addCeo = rs.getString("is_ceo");
            employeeList.add(addCeo);
        }
        connectionForQuery().close();
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

    //fetch all employeeIds from the database and return a LinkedList with all employeeIds.
    public LinkedList<String> fetchEmployeeId() throws SQLException{
        Statement stmt = connectionForQuery().createStatement();
        ResultSet rs = stmt.executeQuery("select id from employees");

        LinkedList<String> employeeIds = new LinkedList<>();

        while(rs.next()){
            String checkForEmployeeId = rs.getString("id");
            employeeIds.add(checkForEmployeeId);
        }
        connectionForQuery().close();
        return employeeIds;

    }
    //fetch all is_manager from the database and return a LinkedList with all values.
    public LinkedList<String> fetchIsManager() throws SQLException{
        Statement stmt = connectionForQuery().createStatement();
        ResultSet rs = stmt.executeQuery("select is_manager from employees");
        
        LinkedList<String> isManager = new LinkedList<>();
        while(rs.next()){
            String checkForManager = rs.getString("is_manager");
            isManager.add(checkForManager);
        }
        connectionForQuery().close();
        return isManager;
    }

    //Checking if the employeeId is valid and if the EmployeeId is a manager.
    public String checkForEmployeeIdAsManagerId(@ModelAttribute("employee") Employee employee) throws SQLException{
        
        LinkedList<String> employeeIds = fetchEmployeeId();
        LinkedList<String> isManager = fetchIsManager();

        String employeeIdForManager = "false";

        if(employeeIds.size()==0){
            return employeeIdForManager = "true";
        }
        //Check if input of employeeId as managerId is present in the database.
        for (int i = 0; i < employeeIds.size(); i++){
            if(employee.getManagerId().equals(employeeIds.get(i)) && isManager.get(i).equals("Yes")){
                //Check if the EmployeeId is a manager.
                employeeIdForManager = "true";
                return employeeIdForManager;
            }
        } 
        return employeeIdForManager;
    }

}