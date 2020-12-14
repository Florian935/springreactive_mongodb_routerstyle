package com.reactive.mongodb.router.function.reactive.service;

import com.reactive.mongodb.router.function.reactive.model.Student;
import com.reactive.mongodb.router.function.reactive.repository.StudentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

import static java.time.Duration.ofMillis;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Flux<Student> findAll() {
        return studentRepository.findAll();
    }

    public Flux<Student> findAllStream() {
        return studentRepository
                .findAll()
                .zipWith(
                        Flux
                                .interval(ofMillis(1000)))
                .map(Tuple2::getT1)
                .share()
                .log();
    }

    @Override
    public Mono<Student> save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Mono<Student> findById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return studentRepository.deleteById(id);
    }
}
