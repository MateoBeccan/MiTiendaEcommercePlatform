package com.ecommerce.shoppingcart.school.repository;

import com.ecommerce.shoppingcart.school.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
