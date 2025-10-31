package com.ecommerce.shoppingcart.school.controller;

import com.ecommerce.shoppingcart.school.dto.EnrollmentRequest;
import com.ecommerce.shoppingcart.school.model.Enrollment;
import com.ecommerce.shoppingcart.school.service.EnrollmentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/school/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<Enrollment> listEnrollments() {
        return enrollmentService.findAll();
    }

    @GetMapping("/{id}")
    public Enrollment getEnrollment(@PathVariable Long id) {
        return enrollmentService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Enrollment> createEnrollment(@Valid @RequestBody EnrollmentRequest request) {
        Enrollment created = enrollmentService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public Enrollment updateEnrollment(@PathVariable Long id, @Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
