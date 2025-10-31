package com.ecommerce.shoppingcart.school.repository;

import com.ecommerce.shoppingcart.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
