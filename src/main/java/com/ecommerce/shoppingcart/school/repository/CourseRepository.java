package com.ecommerce.shoppingcart.school.repository;

import com.ecommerce.shoppingcart.school.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
