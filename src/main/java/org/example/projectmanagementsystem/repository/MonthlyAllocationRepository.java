package org.example.projectmanagementsystem.repository;

import org.example.projectmanagementsystem.entity.MonthlyAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyAllocationRepository extends JpaRepository<MonthlyAllocation, Long>{

}
