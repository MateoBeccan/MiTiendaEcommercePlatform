package com.ecommerce.shoppingcart.school.service;

import com.ecommerce.shoppingcart.school.exception.ResourceNotFoundException;
import com.ecommerce.shoppingcart.school.model.Student;
import com.ecommerce.shoppingcart.school.repository.StudentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id " + id));
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Long id, Student updated) {
        Student student = findById(id);
        student.setFirstName(updated.getFirstName());
        student.setLastName(updated.getLastName());
        student.setEmail(updated.getEmail());
        student.setDateOfBirth(updated.getDateOfBirth());
        student.setGradeLevel(updated.getGradeLevel());
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        Student student = findById(id);
        studentRepository.delete(student);
    }
}
