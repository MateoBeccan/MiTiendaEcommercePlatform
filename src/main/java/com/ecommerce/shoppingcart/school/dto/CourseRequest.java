package com.ecommerce.shoppingcart.school.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CourseRequest {

    @NotBlank(message = "El nombre del curso es obligatorio")
    private String name;

    private String description;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "El curso debe tener al menos un crédito")
    private Integer credits;

    @NotNull(message = "El profesor es obligatorio")
    private Long teacherId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
