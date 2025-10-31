package com.ecommerce.shoppingcart.school.dto;

import com.ecommerce.shoppingcart.school.model.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;

public class EnrollmentRequest {

    @NotNull(message = "El estudiante es obligatorio")
    private Long studentId;

    @NotNull(message = "El curso es obligatorio")
    private Long courseId;

    private EnrollmentStatus status;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}
