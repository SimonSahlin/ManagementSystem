package com.consid.managementsystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;


@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    @NotNull(message = "Name cant be blank!")
    @Size(min = 2, max = 60, message="Name has to be atleast 2 characters and no more than 60 characters.")
    private String name;
    
    @Column(name = "salary")
    @NotNull()
    @Range(min = 1, message = "You need to choose a rank!")
    private int salary;

    @Column(name = "is_manager")
    @NotNull(message ="Enter Yes or No")
    private String ismanager;

    @Column(name = "is_ceo")
    @NotNull(message ="Enter Yes or No")
    private String isceo;

    @Column(name = "manager_id")
    private String managerid;
    
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIsManager() {
        return ismanager;
    }
    public void setIsManager(String ismanager) {
        this.ismanager = ismanager;
    }
    public String getIsCeo() {
        return isceo;
    }
    public void setIsCeo(String isceo) {
        this.isceo = isceo;
    }
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public String getManagerId() {
        return managerid;
    }
    public void setManagerId(String managerid) {
        this.managerid = managerid;
    }

}
