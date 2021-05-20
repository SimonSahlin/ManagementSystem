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

    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model){
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, Model model) throws SQLException{
        if(result.hasErrors()){
            return "new_employee";
        }
        //Set the correct salary based on rank and if the employee is manger or ceo (Done from another controller to minimize logic here)
        FunctionsController funcCon = new FunctionsController();
        employee.setSalary(funcCon.calculateSalary(employee));
        //Function that checks if there is a CEO, only if the updated or created employee is marked as CEO, 
        //return a String depending on the result.
        String areThereACeo = funcCon.checkForCeo(employee, model);
        if(areThereACeo.equals("false")){
            //Save employee to database
            employeeService.saveEmployee(employee);
        }
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
        //Get employee from the service
        Employee employee = employeeService.getEmployeeById(id);

        // set employee as a model attribute to re-populate the form
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable (value ="id") long id){
        //call the delete employee method
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/";
    }

}
