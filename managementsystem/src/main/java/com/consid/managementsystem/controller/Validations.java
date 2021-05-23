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
    
    //fetch the employee that matches specific employeeId
    public String fetchEmployeeById(long id) throws SQLException{
        Statement stmt = connectionForQuery().createStatement();
        ResultSet rs = stmt.executeQuery("select * from employees WHERE id = " + id);

        String employeeManager = "";

        while(rs.next()){
            employeeManager = rs.getString("is_manager");
        }
        connectionForQuery().close();
        return employeeManager;
    }

    //Fetch all managerIds
    public LinkedList<Integer> fetchAllManagerIds() throws SQLException{
        Statement stmt = connectionForQuery().createStatement();
        ResultSet rs = stmt.executeQuery("select manager_id from employees");

        LinkedList<Integer> allEmployeeIds = new LinkedList<>();
 
        while(rs.next()){
            int thisEmployeeId = rs.getInt("manager_id");
            allEmployeeIds.add(thisEmployeeId);
        }
        connectionForQuery().close();
        return allEmployeeIds;
    }
    
    //Function to check if there is a CEO already pressent in the db. 
    public String checkForCeo(@ModelAttribute("employee") Employee employee) throws SQLException{
        Statement stmt = connectionForQuery().createStatement();
        ResultSet rs = stmt.executeQuery("select is_ceo from employees");
        
        String checkForCeo = "";

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
                } else {
                    checkForCeo = "false";
                }
            } 
        }
        return checkForCeo;
    }
    
    //Checking if the employeeId is valid and if the EmployeeId is a manager.
    public String checkForEmployeeIdAsManagerId(@ModelAttribute("employee") Employee employee) throws SQLException{
        
        LinkedList<String> employeeIds = fetchEmployeeId();
        LinkedList<String> isManager = fetchIsManager();

        String employeeIdForManager = "";

        if(employeeIds.size()==0){
            return employeeIdForManager = "true";
        }
        //Check if input of employeeId as managerId is present in the database.
        for (int i = 0; i < employeeIds.size(); i++){
            if(employee.getManagerId().equals(employeeIds.get(i)) && isManager.get(i).equals("Yes")){
                //Check if the EmployeeId is a manager.
                employeeIdForManager = "true";
                employeeIds.clear();
                isManager.clear();
                return employeeIdForManager;
            }
        } 
        employeeIdForManager = "false";
        return employeeIdForManager;
    }

    //Checking if the deleted employee is a manager over another employee.
    public String isTheEmployeeAManager(long id) throws SQLException{
        String isTheEmployeeAManager = fetchEmployeeById(id);

        if (isTheEmployeeAManager.equals("Yes")){
            //Would return a Yes and the manager cant be removed until the other employees are updated.
            return isTheEmployeeAManager;
        }
        return isTheEmployeeAManager;
    }

    public String doesTheManagerManageAnyone(long id) throws SQLException{
        LinkedList<Integer> allManagerIds = fetchAllManagerIds();

        String theManagerManageAnEmployee = "No";

        for (int i = 0; i  < allManagerIds.size(); i++){
            if(allManagerIds.get(i) == id){
                theManagerManageAnEmployee = "Yes";
                allManagerIds.clear();
                return theManagerManageAnEmployee;
            }
        }
        return theManagerManageAnEmployee;
    }

}
