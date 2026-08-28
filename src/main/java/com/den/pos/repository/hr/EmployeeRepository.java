package com.den.pos.repository.hr;

import com.den.pos.entity.hr.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query(value = """
            SELECT * FROM employees
            WHERE first_name ILIKE CONCAT('%', :keyword, '%')
               OR last_name ILIKE CONCAT('%', :keyword, '%')
               OR phone ILIKE CONCAT('%', :keyword, '%')
            """,
            countQuery = """
            SELECT COUNT(*) FROM employees
            WHERE first_name ILIKE CONCAT('%', :keyword, '%')
               OR last_name ILIKE CONCAT('%', :keyword, '%')
               OR phone ILIKE CONCAT('%', :keyword, '%')
            """, nativeQuery = true)
    Page<Employee> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM employees WHERE user_id = :userId", nativeQuery = true)
    Optional<Employee> findByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT * FROM employees WHERE is_active = true ORDER BY first_name", nativeQuery = true)
    List<Employee> findAllActive();

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE employees
            SET termination_date = :terminationDate, is_active = false, updated_at = CURRENT_TIMESTAMP
            WHERE id = :employeeId AND termination_date IS NULL
            """, nativeQuery = true)
    int terminate(@Param("employeeId") UUID employeeId, @Param("terminationDate") java.time.LocalDate terminationDate);
}
