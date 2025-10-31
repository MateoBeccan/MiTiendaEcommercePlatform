package com.ecommerce.shoppingcart.school.service;

import com.ecommerce.shoppingcart.school.dto.EnrollmentRequest;
import com.ecommerce.shoppingcart.school.exception.ResourceNotFoundException;
import com.ecommerce.shoppingcart.school.model.Course;
import com.ecommerce.shoppingcart.school.model.Enrollment;
import com.ecommerce.shoppingcart.school.model.EnrollmentStatus;
import com.ecommerce.shoppingcart.school.model.Student;
import com.ecommerce.shoppingcart.school.repository.CourseRepository;
import com.ecommerce.shoppingcart.school.repository.EnrollmentRepository;
import com.ecommerce.shoppingcart.school.repository.StudentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public Enrollment findById(Long id) {
        return enrollmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada con id " + id));
    }

    public Enrollment create(EnrollmentRequest request) {
        Student student = studentRepository
                .findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id " + request.getStudentId()));
        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id " + request.getCourseId()));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        EnrollmentStatus status = request.getStatus();
        if (status != null) {
            enrollment.setStatus(status);
        }
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment update(Long id, EnrollmentRequest request) {
        Enrollment enrollment = findById(id);

        if (request.getStudentId() != null) {
            Student student = studentRepository
                    .findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id " + request.getStudentId()));
            enrollment.setStudent(student);
        }

        if (request.getCourseId() != null) {
            Course course = courseRepository
                    .findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id " + request.getCourseId()));
            enrollment.setCourse(course);
        }

        if (request.getStatus() != null) {
            enrollment.setStatus(request.getStatus());
        }

        return enrollmentRepository.save(enrollment);
    }

    public void delete(Long id) {
        Enrollment enrollment = findById(id);
        enrollmentRepository.delete(enrollment);
    }
}
