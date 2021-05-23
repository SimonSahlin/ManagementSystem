package com.consid.managementsystem.controller;

import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;

import com.consid.managementsystem.model.Employee;
import com.consid.managementsystem.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    //Display list of employees
    @GetMapping("/")
    public String viewHopePage(Model model){
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "index";
    }

    //Showing the page with a Post-Form to the new employee
    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model){
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    //Post-request with several funtions to check that everything is good before saving the NEWemployee.
    @PostMapping("/saveEmployee")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result) throws SQLException{
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                System.out.println(error.getDefaultMessage());
            }
            return "new_employee";
        }
        //Set the correct salary based on rank and if the employee is manger or ceo (Done from another controller to minimize logic here)
        employee.setSalary(employeeService.calculateSalary(employee));

        //Function that sets variable as "true" if there is a CEO, only if the updated or created employee is marked as CEO otherwise "false".
        String areThereACeo = employeeService.checkForCeo(employee);
        //Function that sets variable as "true" but only if the ManagerId that is set does have a correlating EmployeeId that is a manager otherwise "false".
        String isManagerIdAManager = employeeService.checkForEmployeeIdAsManagerId(employee);

        //IF-statement if the employee that is created is the CEO.
        if(employee.getIsCeo().equals("Yes") && employee.getIsManager().equals("No")){
            System.out.println("The CEO has to be a manager!");
            return "new_employee";
        } 
        //IF-statment that if the created employee is CEO and is manager
        else if (employee.getIsCeo().equals("Yes") && employee.getIsManager().equals("Yes") && employee.getManagerId().equals("")){
            if(areThereACeo.equals("true")){
                System.out.println("There already is a CEO, please remove current CEO before adding a new one.");
                return "new_employee";
            } else {
                //Save employee to database
                employeeService.saveEmployee(employee);
                System.out.println("Successfully added the employee");
                return "redirect:/";
            } 
        }
        //If-statement that checks that the CEO doesnt have a manager!
        if (employee.getIsCeo().equals("Yes") && employee.getIsManager().equals("Yes") && !employee.getManagerId().equals("")){
            System.out.println("The CEO cant have a manager!");
            return "new_employee";
        }

        //IF-statement that if the employee that is created isnt CEO, then the employee needs a manager!
        if(employee.getIsCeo().equals("No")){
            if(isManagerIdAManager.equals("false")){
                System.out.println("Need to set a correct EmployeeId as managerId!");
                return "new_employee";
            }
            //Save employee to database
            System.out.println("Successfully added the employee");
            employeeService.saveEmployee(employee);
            return "redirect:/";
        }
        //Something went very wrong, should be impossible to get here.
        System.out.println("Something went very wrong, should be impossible to get here.");
        return "new_employee";
    }
    
    //Same as above but for the UPDATEDEmployee!
    @PostMapping("/saveUpdatedEmployee")
    public String saveUpdatedEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result) throws SQLException{
        if(result.hasErrors()){
            return "update_employee";
        }
        //Set the correct salary based on rank and if the employee is manger or ceo (Done from another controller to minimize logic here)
        employee.setSalary(employeeService.calculateSalary(employee));

        //Function that sets variable as "true" if there is a CEO, only if the updated or created employee is marked as CEO otherwise "false".
        String areThereACeo = employeeService.checkForCeo(employee);
        //Function that sets variable as "true" but only if the ManagerId that is set does have a correlating EmployeeId that is a manager otherwise "false".
        String isManagerIdAManager = employeeService.checkForEmployeeIdAsManagerId(employee);

        //IF-statement if the employee that is created is the CEO.
        if(employee.getIsCeo().equals("Yes") && employee.getIsManager().equals("No")){
            System.out.println("The CEO has to be a manager!");
            return "update_employee";
        } 
        //IF-statment that if the created employee is CEO and is manager
        else if (employee.getIsCeo().equals("Yes") && employee.getIsManager().equals("Yes") && employee.getManagerId().equals("")){
            if(areThereACeo.equals("true")){
                System.out.println("There already is a CEO, please remove current CEO before adding a new one.");
                return "update_employee";
            } else {
                //Save employee to database
                employeeService.saveEmployee(employee);
                System.out.println("Successfully added the employee");
                return "redirect:/";
            } 
        }
        //If-statement that checks that the CEO doesnt have a manager!
        if (employee.getIsCeo().equals("Yes") && employee.getIsManager().equals("Yes") && !employee.getManagerId().equals("")){
            System.out.println("The CEO cant have a manager!");
            return "update_employee";
        }

        //IF-statement that if the employee that is created isnt CEO, then the employee needs a manager!
        if(employee.getIsCeo().equals("No")){
            if(isManagerIdAManager.equals("false")){
                System.out.println("Need to set a correct EmployeeId as managerId!");
                return "update_employee";
            }
            //Save employee to database
            System.out.println("Successfully added the employee");
            employeeService.saveEmployee(employee);
            return "redirect:/";
        }
        //Something went very wrong, should be impossible to get here.
        System.out.println("Something went very wrong, should be impossible to get here.");
        return "update_employee";
    }

    //Showing the page with a Post-Form to updated a employee
    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable ( value = "id") int id, Model model) {
        
        //Get employee from the service
        Employee employee = employeeService.getEmployeeById(id);

        // set employee as a model attribute to re-populate the form
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    //Deleting a employee unless the employee manage other employees.
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable (value ="id") int id) throws SQLException{
        String isTheEmployeeAManager = employeeService.isTheEmployeeAManager(id);
        String doesTheManagerManageAnEmployee = employeeService.doesTheManagerManageAnyone(id);
        if(isTheEmployeeAManager.equals("Yes") && doesTheManagerManageAnEmployee.equals("Yes")){
            //Print out a wrong message!
            System.out.println("You Cant delete this employee becuse this employee is a manager, update the employees this employee manage. Before deleting.");
            return "redirect:/";

        }
        //call the delete employee method
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/";
    }

}
