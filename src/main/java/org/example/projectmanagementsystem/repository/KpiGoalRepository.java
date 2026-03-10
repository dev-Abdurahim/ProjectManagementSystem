package org.example.projectmanagementsystem.repository;

import org.example.projectmanagementsystem.entity.KpiGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KpiGoalRepository extends JpaRepository<KpiGoal, Long> {

    Optional<KpiGoal> findByYear(Integer year);
}
