package com.ecommerce.shoppingcart.school.service;

import com.ecommerce.shoppingcart.school.exception.ResourceNotFoundException;
import com.ecommerce.shoppingcart.school.model.Teacher;
import com.ecommerce.shoppingcart.school.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher findById(Long id) {
        return teacherRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con id " + id));
    }

    public Teacher create(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher update(Long id, Teacher updated) {
        Teacher teacher = findById(id);
        teacher.setFirstName(updated.getFirstName());
        teacher.setLastName(updated.getLastName());
        teacher.setEmail(updated.getEmail());
        teacher.setDepartment(updated.getDepartment());
        teacher.setPhoneNumber(updated.getPhoneNumber());
        return teacherRepository.save(teacher);
    }

    public void delete(Long id) {
        Teacher teacher = findById(id);
        teacherRepository.delete(teacher);
    }
}
