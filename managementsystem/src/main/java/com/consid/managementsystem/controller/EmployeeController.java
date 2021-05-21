package com.consid.managementsystem.controller;

import java.sql.SQLException;

import javax.validation.Valid;

import com.consid.managementsystem.model.Employee;
import com.consid.managementsystem.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

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
        if(result.hasErrors()){
            return "new_employee";
        }
        //Set the correct salary based on rank and if the employee is manger or ceo (Done from another controller to minimize logic here)
        Validations validation = new Validations();
        employee.setSalary(validation.calculateSalary(employee));

        //Function that sets variable as "true" if there is a CEO, only if the updated or created employee is marked as CEO.
        String areThereACeo = validation.checkForCeo(employee);
        //Function that sets variable as true but only if the ManagerId that is set does have a correlating EmployeeId that is a manager.
        String isMangerIdAManager = validation.checkForEmployeeIdAsManagerId(employee);

        if(areThereACeo.equals("false") && isMangerIdAManager.equals("true")){
            //Save employee to database
            employeeService.saveEmployee(employee);
            System.out.println("Successfully added the employee");
            return "redirect:/";
        }
        //Type out that there already is a CEO and that the CEO needs to be updated/deleted OR  
        //That the managerId is wrong, eaither EmployeeId doesnt exist OR The employee isnt a manager.
        System.out.println("There is already a CEO, you choose an employeeId as ManagerID that doesnt exist OR the EmployeeId set as manager isnt a manager.");
        return "new_employee";
    }
   
    //Same as above but for the UPDATEDemployee
    @PostMapping("/saveUpdatedEmployee")
    public String saveUpdatedEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result) throws SQLException{
        if(result.hasErrors()){
            return "update_employee";
        }
        //Set the correct salary based on rank and if the employee is manger or ceo (Done from another controller to minimize logic here)
        Validations validation = new Validations();
        employee.setSalary(validation.calculateSalary(employee));

        //Function that sets variable as "true" if there is a CEO, only if the updated or created employee is marked as CEO.
        String areThereACeo = validation.checkForCeo(employee);
        //Function that sets variable as true but only if the ManagerId that is set does have a correlating EmployeeId that is a manager.
        String isMangerIdAManager = validation.checkForEmployeeIdAsManagerId(employee);

        if(areThereACeo.equals("false") && isMangerIdAManager.equals("true")){
            //Save employee to database
            employeeService.saveEmployee(employee);
            System.out.println("Successfully added the employee");
            return "redirect:/";
        }
        //Type out that there already is a CEO and that the CEO needs to be updated/deleted OR  
        //That the managerId is wrong, eaither EmployeeId doesnt exist OR The employee isnt a manager.
        System.out.println("There is already a CEO, you choose an employeeId as ManagerID that doesnt exist OR the EmployeeId set as manager isnt a manager.");
        return "update_employee";
    }
    
    //Showing the page with a Post-Form to updated a employee
    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
        //Get employee from the service
        Employee employee = employeeService.getEmployeeById(id);

        // set employee as a model attribute to re-populate the form
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    //Deleting a employee unless the employee manage other employees.
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable (value ="id") long id){
        System.out.println("You cant delete this employee becuse this employee is manager of others, please updated those first.");
       
        //call the delete employee method
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/";

        
        
    }

}
