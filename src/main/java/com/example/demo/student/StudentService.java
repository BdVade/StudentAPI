package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;


    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }
    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()){
            throw new IllegalStateException("email taken");

        }
        studentRepository.save(student);
        System.out.println(student);
    }


    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalStateException("student with Id "+ studentId+ " does not exist");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String email, String name){
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalStateException("student with Id "+ studentId+ " does not exist");
        }
        Student student = studentRepository.getById(studentId);
        Optional <String> studentEmail = Optional.ofNullable(email);
        Optional<String> studentName = Optional.ofNullable(name);
        Optional<Student> takenEmail = studentRepository.findStudentByEmail(email);
        if (takenEmail.isPresent()){
            throw new IllegalStateException("email taken");
        }

        studentEmail.ifPresent(student::setEmail);

        studentName.ifPresent(student::setName);

    }
}
