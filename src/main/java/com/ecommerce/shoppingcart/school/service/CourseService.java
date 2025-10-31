package com.ecommerce.shoppingcart.school.service;

import com.ecommerce.shoppingcart.school.dto.CourseRequest;
import com.ecommerce.shoppingcart.school.exception.ResourceNotFoundException;
import com.ecommerce.shoppingcart.school.model.Course;
import com.ecommerce.shoppingcart.school.model.Teacher;
import com.ecommerce.shoppingcart.school.repository.CourseRepository;
import com.ecommerce.shoppingcart.school.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public CourseService(CourseRepository courseRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(Long id) {
        return courseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id " + id));
    }

    public Course create(CourseRequest request) {
        Teacher teacher = teacherRepository
                .findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con id " + request.getTeacherId()));

        Course course = new Course();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public Course update(Long id, CourseRequest request) {
        Course course = findById(id);
        Teacher teacher = teacherRepository
                .findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con id " + request.getTeacherId()));

        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public void delete(Long id) {
        Course course = findById(id);
        courseRepository.delete(course);
    }
}
