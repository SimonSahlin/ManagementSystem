package com.consid.managementsystem.repository;

import com.consid.managementsystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeReposity extends JpaRepository<Employee, Long> {
    
}
