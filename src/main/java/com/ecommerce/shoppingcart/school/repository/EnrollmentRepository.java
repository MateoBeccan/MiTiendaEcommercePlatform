package com.ecommerce.shoppingcart.school.repository;

import com.ecommerce.shoppingcart.school.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}
